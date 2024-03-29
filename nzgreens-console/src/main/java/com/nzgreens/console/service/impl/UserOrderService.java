package com.nzgreens.console.service.impl;

import com.github.pagehelper.PageHelper;
import com.nzgreens.common.enums.*;
import com.nzgreens.common.exception.ErrorCodes;
import com.nzgreens.common.form.console.PageSearchForm;
import com.nzgreens.common.form.console.UserOrderExportForm;
import com.nzgreens.common.form.console.UserOrderForm;
import com.nzgreens.common.model.console.OrdersModel;
import com.nzgreens.common.model.console.UserOrderExportModel;
import com.nzgreens.common.model.console.UserOrderModel;
import com.nzgreens.common.utils.BeanMapUtil;
import com.nzgreens.common.utils.CollectionUtil;
import com.nzgreens.console.service.BaseService;
import com.nzgreens.console.service.IUserOrderService;
import com.nzgreens.console.util.CosUtils;
import com.nzgreens.console.web.common.UploadTempImageUtil;
import com.nzgreens.dal.user.example.*;
import com.nzgreens.dal.user.mapper.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:helizheng
 * @Date: Created in 2018/5/2 22:02
 */
@Service
public class UserOrderService extends BaseService implements IUserOrderService {
    @Resource
    private SubUserOrderMapper subUserOrderMapper;
    @Resource
    private OrderCertificateMapper orderCertificateMapper;
    @Resource
    private UserOrderMapper userOrderMapper;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private SubUserMapper subUserMapper;
    @Resource
    private AgentRebateMapper agentRebateMapper;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private AccountLogsMapper accountLogsMapper;
    @Resource
    private AgentRebateAuditMapper agentRebateAuditMapper;
    @Resource
    private UserAgentMapper userAgentMapper;
    @Value("${images.host}")
    private String imageHost;
    @Value("${images.user.order.cert.path}")
    private String imageUserOrderCertPath;
    @Value("${images.upload.path}")
    private String imageRoot;
    @Resource
    private CosUtils cosUtils;
    private static final BigDecimal HUNDRED = new BigDecimal("100");

    @Override
    public List<UserOrderModel> selectUserOrderForPage(UserOrderForm form) throws Exception {
        PageHelper.startPage(form.getPageNum(),form.getPageSize());
        List<UserOrderModel> userOrderModels = subUserOrderMapper.selectUserOrderForPage(form);
        for(UserOrderModel model : userOrderModels){
            if(StringUtils.isNotBlank(model.getAddress())){
                model.setAddress(model.getAddress().replace("$",""));
            }
        }
        return userOrderModels;
    }

    @Override
    public List<UserOrderExportModel> selectUserOrderExportExcel(UserOrderExportForm form) throws Exception {
        if(StringUtils.isBlank(form.getOrderIdsExport())){
            thrown(ErrorCodes.EXPORT_ORDERID_ILLEGAL);
        }
        if(form.getOrderNumberExport() == null){
            thrown(ErrorCodes.EXPORT_ORDERNUMBER_ILLEGAL);
        }
        if(!NumberUtils.isDigits(String.valueOf(form.getOrderNumberExport()))){
            thrown(ErrorCodes.EXPORT_ORDERNUMBER_FORMAT_ILLEGAL);
        }
        Long orderNum = form.getOrderNumberExport();
        List<UserOrderExportModel> list = new ArrayList<>();

        List<String> orderList = Arrays.asList(form.getOrderIdsExport().split(","));
        List<Long> longs = new ArrayList<>();
        for(String order : orderList){
            longs.add(Long.valueOf(order));
        }
        UserOrderExample example = new UserOrderExample();
        UserOrderExample.Criteria criteria = example.createCriteria();
        criteria.andIdIn(longs);
        if(form.getStartTime() != null){
            criteria.andCreateTimeGreaterThanOrEqualTo(form.getStartTime());
        }
        if(form.getEndTime() != null){
            criteria.andCreateTimeLessThanOrEqualTo(form.getEndTime());
        }
        if(form.getStatus() != null){
            criteria.andStatusEqualTo(form.getStatus().byteValue());
        }
        example.setOrderByClause(" id desc");
        List<UserOrder> userOrders = userOrderMapper.selectByExample(example);
        for(UserOrder userOrder : userOrders){
            StringBuilder builder = new StringBuilder();
            builder.append("单号 " + orderNum + "  ");
            UserOrderExportModel model = new UserOrderExportModel();
            List<OrdersModel> ordersModels = subUserOrderMapper.selectOrdersForPage(userOrder.getOrderNumber());
            for(OrdersModel ordersModel : ordersModels){
                builder.append(ordersModel.getTitle() + " * " + ordersModel.getProductNumber() + "，");
            }
            if(StringUtils.isNotBlank(userOrder.getAddress())) {
                userOrder.setAddress(userOrder.getAddress().replace("$", ""));
            }
            builder.append(userOrder.getAddress() + "，" + userOrder.getContact() + "，" + userOrder.getTelephone());

            orderNum += 1;

            model.setOrderContent(builder.toString());
            list.add(model);
        }
        return list;
    }

    @Override
    public List<UserOrderExportModel> selectUserOrderExportExcelV2(UserOrderExportForm form) throws Exception {
        if(StringUtils.isBlank(form.getOrderIdsExport())){
            thrown(ErrorCodes.EXPORT_ORDERID_ILLEGAL);
        }

        List<UserOrderExportModel> list = new ArrayList<>();

        List<String> orderList = Arrays.asList(form.getOrderIdsExport().split(","));
        List<Long> longs = new ArrayList<>();
        for(String order : orderList){
            longs.add(Long.valueOf(order));
        }
        form.setIds(longs);

        Long orderNum = form.getOrderNumberExport();

        List<UserOrderModel> userOrderModels = subUserOrderMapper.selectUserOrderExportForPage(form);
        for(UserOrderModel userOrder : userOrderModels){
            StringBuilder builder = new StringBuilder();
            builder.append("单号 " + orderNum + "  ");
            UserOrderExportModel model = new UserOrderExportModel();
            List<OrdersModel> ordersModels = subUserOrderMapper.selectOrdersForPage(userOrder.getOrderNumber());
            for(OrdersModel ordersModel : ordersModels){
                builder.append(ordersModel.getTitle() + " * " + ordersModel.getProductNumber() + "，");
            }
            if(StringUtils.isNotBlank(userOrder.getAddress())) {
                userOrder.setAddress(userOrder.getAddress().replace("$", ""));
            }
            builder.append(userOrder.getAddress() + "，" + userOrder.getContact() + "，" + userOrder.getTelephone());

            orderNum += 1;

            model.setId(userOrder.getId());
            model.setOrderPrice(new BigDecimal(userOrder.getPrice()).divide(new BigDecimal(100))
                    .setScale(2).doubleValue());
            model.setMobile(userOrder.getMobile());
            model.setOrderContent(builder.toString());
            list.add(model);
        }
        return list;
    }

    @Override
    public List<OrdersModel> selectOrdersForPage(String orderNumber, PageSearchForm page) throws Exception {
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        return subUserOrderMapper.selectOrdersForPage(orderNumber);
    }

    @Override
    public List<OrderCertificate> selectOrderCertForPage(String orderNumber, PageSearchForm page) throws Exception {
        //PageHelper.startPage(page.getPageNum(),page.getPageSize());
        OrderCertificateExample example = new OrderCertificateExample();
        example.createCriteria().andOrderNumberEqualTo(orderNumber);
        List<OrderCertificate> orderCertificates = orderCertificateMapper.selectByExample(example);
        for(OrderCertificate cert : orderCertificates){
            cert.setCertificateUrl(imageHost + imageUserOrderCertPath + "/" + cert.getCertificateUrl());
        }
        return orderCertificates;
    }

    @Override
    public UserOrderModel selectUserOrderDetail(Long id) throws Exception {
        UserOrderModel orderModel = new UserOrderModel();
        UserOrder userOrder = userOrderMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(userOrder,orderModel);
        orderModel.setDeliveryMode(userOrder.getDeliveryMode().intValue());
        orderModel.setStatus(userOrder.getStatus().intValue());
        orderModel.setType(userOrder.getType().intValue());
        if(StringUtils.isNotBlank(orderModel.getAddress())) {
            orderModel.setAddress(orderModel.getAddress().replace("$", ""));
        }
        //查询返佣
        AgentRebateAuditExample example = new AgentRebateAuditExample();
        example.createCriteria().andUserOrderIdEqualTo(id);
        List<AgentRebateAudit> audits = agentRebateAuditMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(audits)){
            AgentRebateAudit audit = audits.get(0);
            orderModel.setRebateId(audit.getId());
            orderModel.setRebateType(audit.getType().intValue());
            orderModel.setRebatePrice(audit.getRebatePrice());
            orderModel.setActualRebatePrice(audit.getActualRebatePrice());
            orderModel.setRebateStatus(audit.getStatus().intValue());
            orderModel.setRebateRemark(audit.getRemark());
            orderModel.setRebateCreateTime(audit.getCreateTime());
            orderModel.setRebateUpdateTime(audit.getUpdateTime());
        }
        Users users = usersMapper.selectByPrimaryKey(userOrder.getUserId());
        orderModel.setBalance(users.getBalance());
        orderModel.setTotalBalance(users.getBalance());
        orderModel.setMobile(users.getTelephone());
        if (users.getType() == UserTypeEnum._AGENT.getValue()) {
            UserAgentExample agentExample = new UserAgentExample();
            agentExample.createCriteria().andAgentUserIdEqualTo(users.getId());
            List<UserAgent> userAgentList = userAgentMapper.selectByExample(agentExample);
            if (CollectionUtils.isEmpty(userAgentList)) {
                return orderModel;
            }
            UsersExample usersExample = new UsersExample();
            usersExample.createCriteria().andIdIn(userAgentList.stream().map(UserAgent::getUserId)
                    .collect(Collectors.toList())).andBalanceLessThan(0L);
            List<Users> usersList = usersMapper.selectByExample(usersExample);
            if (CollectionUtils.isEmpty(usersList)) {
                return orderModel;
            }
            orderModel.setSubUserLiabilities(usersList.stream().map(Users::getBalance).reduce(Long::sum).get());
            orderModel.setTotalBalance(users.getBalance() + orderModel.getSubUserLiabilities());
        }
/*        if (userOrder.getStatus() == UserOrderStatusEnum._PROCESSED.getValue()
                || userOrder.getStatus() == UserOrderStatusEnum._DONE.getValue()) {
            Users users = usersMapper.selectByPrimaryKey(userOrder.getUserId());
            orderModel.setBalance(users.getBalance());
        }*/
        return orderModel;
    }

    @Override
    public void insertOrderCert(OrderCertificate orderCertificate) throws Exception {
        if(orderCertificate == null){
            thrown(ErrorCodes.DATA_ILLEGAL);
        }
        if(orderCertificate.getOrderNumber() == null){
            thrown(ErrorCodes.ORDER_NUMBER_ILLEGAL);
        }
        if(StringUtils.isBlank(orderCertificate.getCertificateUrl())){
            thrown(ErrorCodes.ORDER_CERT_URL_ILLEGAL);
        }
        UserOrderExample orderExample = new UserOrderExample();
        orderExample.createCriteria().andOrderNumberEqualTo(orderCertificate.getOrderNumber());
        List<UserOrder> userOrders = userOrderMapper.selectByExample(orderExample);
        UserOrder oldUserOrder = null;
        if(CollectionUtils.isNotEmpty(userOrders)){
            oldUserOrder = userOrders.get(0);
        }
        if(oldUserOrder == null || oldUserOrder.getType() != UserOrderTypeEnum._SYSTEM.getValue()){
            thrown(ErrorCodes.ORDER_TYPE_ILLEGAL);
        }
        if (oldUserOrder.getStatus() == UserOrderStatusEnum._REFUSED.getValue()) {
            thrown(ErrorCodes.ORDER_STATUS_ILLEGAL);
        }

        String[] certUrl = orderCertificate.getCertificateUrl().split(",");
        for(int i = 0,len = certUrl.length;i < len;i ++){
            orderCertificate.setCertificateUrl(certUrl[i]);
            if(orderCertificateMapper.insertSelective(orderCertificate) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }
        }

        //修改订单状态为已上传凭证
        if(CollectionUtils.isNotEmpty(userOrders)){
            if (oldUserOrder.getStatus() == UserOrderStatusEnum._PROCESSED.getValue()) {
                UserOrder userOrder = new UserOrder();
                userOrder.setId(oldUserOrder.getId());
                userOrder.setStatus((byte) UserOrderStatusEnum._DONE.getValue());
                if(userOrderMapper.updateByPrimaryKeySelective(userOrder) < 1){
                    thrown(ErrorCodes.UPDATE_ERROR);
                }
            } else if(oldUserOrder.getStatus() == UserOrderStatusEnum._PENDING.getValue()){
                UserOrder userOrder = new UserOrder();
                userOrder.setId(oldUserOrder.getId());
                userOrder.setStatus((byte) UserOrderStatusEnum._DONE.getValue());
                if(userOrderMapper.updateByPrimaryKeySelective(userOrder) < 1){
                    thrown(ErrorCodes.UPDATE_ERROR);
                }

                OrdersExample example = new OrdersExample();
                example.createCriteria().andOrderNumberEqualTo(orderCertificate.getOrderNumber());
                Orders orders = new Orders();
                orders.setStatus((byte) OrderHandleStatusEnum.SUCCESS.getValue());
                if(ordersMapper.updateByExampleSelective(orders,example) < 1){
                    thrown(ErrorCodes.UPDATE_ERROR);
                }

                //处理订单
                handlerOrder(UserOrderStatusEnum._DONE.getValue(),oldUserOrder);
            }
        }
    }

    @Override
    public void updateOrderStatus(Long orderId,Integer status) throws Exception {
        if(orderId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(status == null){
            thrown(ErrorCodes.ORDER_STATUS_ILLEGAL);
        }
        UserOrder order = userOrderMapper.selectByPrimaryKey(orderId);
        if(order.getType() != UserOrderTypeEnum._SYSTEM.getValue()){
            thrown(ErrorCodes.ORDER_TYPE_ILLEGAL);
        }
        if (status == UserOrderStatusEnum._DONE.getValue() && order.getStatus() == UserOrderStatusEnum._REFUSED.getValue()) {
            thrown(ErrorCodes.ORDER_STATUS_ILLEGAL);
        } else if (status != UserOrderStatusEnum._DONE.getValue() && order.getStatus() != UserOrderStatusEnum._PENDING.getValue()) {
            thrown(ErrorCodes.ORDER_STATUS_ILLEGAL);
        //已处理->上传凭证，直接修改user_order状态
        } else if (order.getStatus() == UserOrderStatusEnum._PROCESSED.getValue()
                && status == UserOrderStatusEnum._DONE.getValue()) {
            //修改用户订单状态
            updateUserOrder(order,status);
            return;
        }
        //修改用户订单状态
        updateUserOrder(order,status);
        //修改订单状态
        updateOrder(order);
        //处理订单
        handlerOrder(status,order);
    }

    private void updateUserOrder(UserOrder order,Integer status) throws Exception{
        UserOrderExample example = new UserOrderExample();
        example.clear();
        UserOrder userOrder = new UserOrder();
        userOrder.setId(order.getId());
        userOrder.setStatus(status.byteValue());

        UserOrderExample.Criteria criteria = example.createCriteria().andIdEqualTo(order.getId());
        //拒绝、通过订单，只能处理中
        if (UserOrderStatusEnum._REFUSED.getValue() == status
                || UserOrderStatusEnum._PROCESSED.getValue() == status) {
            criteria.andStatusEqualTo((byte) UserOrderStatusEnum._PENDING.getValue());
        //已上传凭证，只能非拒绝状态
        } else if (UserOrderStatusEnum._DONE.getValue() == status) {
            criteria.andStatusNotEqualTo((byte) UserOrderStatusEnum._REFUSED.getValue());
        }
        if(userOrderMapper.updateByExampleSelective(userOrder,example) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
        //拒绝状态，修改合并的订单状态为未处理
        if(status.intValue() == UserOrderStatusEnum._REFUSED.getValue()){
            //合并单处理
            if(StringUtils.isNotBlank(order.getUserOrderNumber())){
                for(String orderNumber : order.getUserOrderNumber().split(",")){
                    //处理user_order
                    example.clear();
                    example.createCriteria().andOrderNumberEqualTo(orderNumber);
                    List<UserOrder> userOrders = userOrderMapper.selectByExample(example);
                    if(CollectionUtils.isEmpty(userOrders)){
                        continue;
                    }
                    UserOrder ord = userOrders.get(0);
                    Users users = usersMapper.selectByPrimaryKey(ord.getUserId());
                    UserOrder urd = new UserOrder();
                    if(users != null && users.getType().intValue() != UserTypeEnum._AGENT.getValue()){
                        urd.setStatus((byte) UserOrderStatusEnum._PENDING.getValue());
                    }else{
                        urd.setStatus((byte) UserOrderStatusEnum._REFUSED.getValue());
                    }
                    userOrderMapper.updateByExampleSelective(urd,example);

                    //处理orders
                    updateChildOrder(users,orderNumber);
                }
            }else{
                //用户自收，代理直接购买处理
                example.clear();
                example.createCriteria().andOrderNumberEqualTo(order.getOrderNumber());

                UserOrder urd = new UserOrder();
                urd.setStatus((byte) UserOrderStatusEnum._REFUSED.getValue());
                userOrderMapper.updateByExampleSelective(urd,example);
            }
        }
    }

    private void updateOrder(UserOrder order) throws Exception{
        OrdersExample example = new OrdersExample();
        example.createCriteria().andOrderNumberEqualTo(order.getOrderNumber());
        Orders orders = new Orders();
        orders.setStatus((byte) OrderHandleStatusEnum.SUCCESS.getValue());
        if(ordersMapper.updateByExampleSelective(orders,example) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    private void updateChildOrder(Users users,String orderNumber) throws Exception{
        Orders o1 = new Orders();

        if(users != null && users.getType().intValue() != UserTypeEnum._AGENT.getValue()){
            o1.setStatus((byte) OrderStatusEnum.PENDING.getValue());
        }else{
            o1.setStatus((byte) OrderStatusEnum.SUCCESS.getValue());
        }
        o1.setOrderNumber(orderNumber);

        OrdersExample ordersExample = new OrdersExample();
        ordersExample.createCriteria().andOrderNumberEqualTo(orderNumber);
        ordersMapper.updateByExampleSelective(o1,ordersExample);
    }

    @Override
    public void updateLogisticsNumber(Long orderId, String logisticsNumber) throws Exception {
        if(orderId == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(StringUtils.isBlank(logisticsNumber)){
            thrown(ErrorCodes.LOGISTICS_NUMBER_ILLEGAL);
        }
        String number = logisticsNumber;
        if(logisticsNumber.contains("\n")){
            number = logisticsNumber.replace("\r", "");
            number = number.replace("\n", ",");
        }
        UserOrder userOrder = new UserOrder();
        userOrder.setId(orderId);
        userOrder.setLogisticsNumber(number);
        if(userOrderMapper.updateByPrimaryKeySelective(userOrder) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    @Override
    public void deleteOrderCert(Long id) throws Exception {
        if(id == null){
            thrown(ErrorCodes.ID_ILLEGAL);
        }
        if(orderCertificateMapper.deleteByPrimaryKey(id) < 1){
            thrown(ErrorCodes.UPDATE_ERROR);
        }
    }

    protected void handlerOrder(Integer orderStatus,UserOrder order) throws Exception{
        //查询代理
        Users agentUser = usersMapper.selectByPrimaryKey(order.getUserId());
        //查询系统
        Users systemUser = selectSystemUser();

        //拒绝订单，给代理退币，是否需要给系统减币
        if(orderStatus == UserOrderStatusEnum._REFUSED.getValue()){
            subUserMapper.addBalance(order.getUserId(),order.getPrice().intValue());

            //代理退币日志
            AccountLogs rebateLog = new AccountLogs();
            rebateLog.setUserId(order.getUserId());
            rebateLog.setType((byte) AccountLogsTypeEnum._ORDER_REFUSED.getValue());
            rebateLog.setRecordId(order.getId());
            rebateLog.setBefore(agentUser.getBalance());
            rebateLog.setAmount(order.getPrice());
            rebateLog.setAfter(rebateLog.getBefore() + rebateLog.getAmount());
            rebateLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(rebateLog) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }

            //系统减币
            int row = subUserMapper.reduceBalance(systemUser.getId(), order.getPrice().intValue());
            if(row < 1){
                thrown(ErrorCodes.SYSTEM_BALANCE_NOT_ENOUGH);
            }
            //系统减币日志
            AccountLogs systemLog = new AccountLogs();
            systemLog.setUserId(systemUser.getId());
            systemLog.setType((byte) AccountLogsTypeEnum._ORDER_REFUSED.getValue());
            systemLog.setRecordId(order.getId());
            systemLog.setBefore(systemUser.getBalance());
            systemLog.setAmount(-order.getPrice());
            systemLog.setAfter(systemUser.getBalance() - order.getPrice());
            rebateLog.setTriggerUserId(systemUser.getId());
            if(accountLogsMapper.insertSelective(systemLog) < 1){
                thrown(ErrorCodes.UPDATE_ERROR);
            }

            //库存处理
            OrdersExample ordersExample = new OrdersExample();
            ordersExample.createCriteria().andOrderNumberEqualTo(order.getOrderNumber());
            List<Orders> orders = ordersMapper.selectByExample(ordersExample);
            if(CollectionUtils.isNotEmpty(orders)){
                subUserMapper.batchUpdateProductNumber(orders);
            }
        } else if(orderStatus == UserOrderStatusEnum._PROCESSED.getValue()
                    || orderStatus == UserOrderStatusEnum._DONE.getValue()){
            //已处理订单给代理返佣，给系统减币
            AgentRebateExample example = new AgentRebateExample();
            example.createCriteria().andAgentUserIdEqualTo(order.getUserId());
            List<AgentRebate> agentRebates = agentRebateMapper.selectByExample(example);
            if(CollectionUtils.isNotEmpty(agentRebates)){
                AgentRebate agentRebate = agentRebates.get(0);
                BigDecimal rebateDec = new BigDecimal(agentRebate.getOrderRebate());
                BigDecimal priceDec = new BigDecimal(order.getPrice());
                BigDecimal divide = rebateDec.multiply(priceDec).divide(HUNDRED)
                        .setScale(0,BigDecimal.ROUND_HALF_UP);
                AgentRebateAudit rebate = new AgentRebateAudit();
                rebate.setAgentUserId(order.getUserId());
                rebate.setRebatePrice(divide.longValue());
                rebate.setAmount(order.getPrice());
                rebate.setUserOrderId(order.getId());
                rebate.setStatus((byte) AgentRebateAuditStatusEnum._PENDING.getValue());
                rebate.setType((byte) AgentRebateTypeEnum._ORDER.getValue());
                if(agentRebateAuditMapper.insertSelective(rebate) < 1){
                    thrown(ErrorCodes.UPDATE_ERROR);
                }
            }

            //修改产品销量
            OrdersExample ordersExample = new OrdersExample();
            ordersExample.createCriteria().andOrderNumberEqualTo(order.getOrderNumber());
            List<Orders> orders = ordersMapper.selectByExample(ordersExample);
            for(Orders model : orders){
                subUserMapper.updateProductSalesVolume(model.getProductId(),model.getProductNumber());
            }
        }
    }

    @Override
    public String uploadImg(MultipartFile[] multiFile,String orderNumer) throws Exception {
        if (multiFile != null) {
            String imagePath = imageUserOrderCertPath;
            StringBuilder builder = new StringBuilder();
            for(int i = 0,len = multiFile.length;i < len;i++){
                File file = UploadTempImageUtil.processImage(multiFile[i], imagePath);
                builder.append(file.getName());
                cosUtils.upload(file.getAbsolutePath(),"/data/upload/"+imagePath+"/"+file.getName());
                if(i + 1 != len){
                    builder.append(",");
                }
//                OrderCertificate cert = new OrderCertificate();
//                cert.setOrderNumber(orderNumer);
//                cert.setCertificateUrl(file.getName());
//                orderCertificateMapper.insertSelective(cert);
            }
            return builder.toString();
        }
        return null;
    }
}
