<!DOCTYPE html>
<html>
<head>
    <link rel="shortcut icon" href="${ctx}favicon.ico">
    <link href="${ctx}css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx}css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx}css/animate.min.css" rel="stylesheet">
    <link href="${ctx}css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx}css/page.css" rel="stylesheet">
    <link href="${ctx}css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <form id="userSearchForm" method="post" class="form-horizontal">
                        <input type="hidden" name="pageNum" id="pageNo" value="1">
                        <input type="hidden" name="pageSize" id="pageSize" value="10">
                        <div class="form-group">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">用户ID：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="userId" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">电话号码：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="mobile" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">订单号：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="orderNumber" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">收货方式：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="deliveryMode" id="deliveryMode">
                                            <option value="">全部</option>
                                        <#list DeliveryModeEnum?values as e>
                                            <option value="${e.value}">${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">订单类型：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="type" id="type">
                                            <option value="">全部</option>
                                        <#list UserOrderTypeEnum?values as e>
                                            <option value="${e.value}" <#if e.value == 2>selected="selected"</#if>>${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">状态：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="status" id="status">
                                            <option value="">请选择</option>
                                        <#list UserOrderStatusEnum?values as e>
                                            <option value="${e.value}">${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">注册开始时间：</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="startTime" name="startTime" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">注册结束时间：</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="endTime" name="endTime" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-2">
                                <button id="btnSearch" class="btn btn-primary" data-loading-text="正在加载..." type="button">搜索</button>
                                <button id="btnCancel" class="btn btn-white" type="button">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>订单ID</th>
                                    <th>用户ID</th>
                                    <th>订单号</th>
                                    <th>收货方式</th>
                                    <th>收货地址</th>
                                    <th>联系人</th>
                                    <th>电话号码</th>
                                    <th>商品总价格</th>
                                    <th>订单运费</th>
                                    <th>订单总金额</th>
                                    <th>订单类型</th>
                                    <th>订单状态</th>
                                    <th>创建时间</th>
                                    <th>修改时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="userTbody">
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="ibox-content" style="height: 53px;">
                    <div class="page" id="userPageDiv">

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal inmodal" id="modalUserNumberModify" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width: 500px;">
        <form id="userOrderNumberUpdateForm" method="post" class="form-horizontal">
            <input type="hidden" name="orderId" value="${orderId}" id="numberOrderId">
            <input type="hidden" name="orderNumber" value="${orderNumber}" id="numberOrderNumber">
            <div class="modal-content animated fadeIn" >
                <div class="modal-header" style="height: 20px;">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                            class="sr-only">Close</span></button>
                    <h4 class="modal-title" style="font-size: 22px;">修改物流订单号</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="col-sm-12">
                            <textarea name="logisticsNumber" class="form-control" rows="8" cols="60" id="addLogisticsNumber"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnNumberCancel" class="btn btn-white" type="button">取消</button>
                        </div>
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnNumberSave" class="btn btn-success" type="button">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<textarea id="userTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <td>{$T.user.id}</td>
            <td>{$T.user.userId}</td>
            <td>{$T.user.orderNumber}</td>
            <td>
            <#list DeliveryModeEnum?values as e>
                {#if $T.user.deliveryMode == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{$T.user.address}</td>
            <td>{$T.user.contact}</td>
            <td>{$T.user.mobile}</td>
            <td>{Fen2Yuan($T.user.productPrice)}</td>
            <td>{Fen2Yuan($T.user.freight)}</td>
            <td>{Fen2Yuan($T.user.price)}</td>
            <td>
            <#list UserOrderTypeEnum?values as e>
                {#if $T.user.type == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>
            <#list UserOrderStatusEnum?values as e>
                {#if $T.user.status == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{#if $T.user.createTime!=null}{new Date($T.user.createTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
            <td>{#if $T.user.updateTime!=null}{new Date($T.user.updateTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
            <td>
            <@sec.any name="USER_ORDER_UPDATE">
                {#if $T.user.type == 2}
                    <button type="button" user-order-id="{$T.user.id}" user-order-number="{$T.user.orderNumber}" class="btn btn-primary btnUserUpdate">详情</button>
                {#/if}
                <button type="button" user-order-id="{$T.user.id}" user-order-number="{$T.user.orderNumber}" class="btn btn-primary btnUserUpdateNumber">修改物流订单号</button>
            </@sec.any>
            </td>
        </tr>
    {#else}
		<tr>
            <td colspan="4">暂无数据</td>
        </tr>
	{#/if}
</textarea>

<#include "/page.ftl"/>

<script>
    var _rootPath="${ctx}";
</script>
<script src="${ctx}js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx}js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx}js/plugins/validate/messages_zh.min.js"></script>
<script src="${ctx}js/content.min.js?v=1.0.0"></script>
<script src="${ctx}js/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}js/common.js"></script>
<script src="${ctx}js/jquery-jtemplates.js"></script>
<script src="${ctx}js/dateutil.js"></script>
<script src="${ctx}js/plugins/layer/laydate/laydate.js"></script>
<script src="${ctx}${getVersion('js/page.js')}"></script>
<@sec.any name="USER_ORDER_MANAGE">
<script src="${ctx}${getVersion('js/order/user-order-list.js')}"></script>
</@sec.any>
</body>

</html>
