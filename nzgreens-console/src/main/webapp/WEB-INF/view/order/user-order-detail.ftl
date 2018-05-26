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
    <link href="${ctx}css/plugins/dropzone/dropzone.css" rel="stylesheet">
    <link href="${ctx}css/plugins/blueimp/css/blueimp-gallery.min.css" rel="stylesheet">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <input type="hidden" name="orderId" id="orderId" value="${orderId}">
    <input type="hidden" name="orderNumber" id="orderNumber" value="${orderNumber}">
    <div class="modal-body" id="userDetailDiv">

    </div>
    <#--Tab页-->
    <div class="clients-list" style="background-color: white;">
        <ul id="userTabs" class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1">订单</a></li>
            <li class=""><a data-toggle="tab" href="#tab-2">凭证</a></li>
        </ul>
        <div style="display: none;">
            <form id="userOrderSearchForm" method="post" class="form-horizontal">
                <input type="hidden" name="pageNum" id="pageNo" value="1">
                <input type="hidden" name="pageSize" id="pageSize" value="10">
                <input type="hidden" name="orderNumber" value="${orderNumber}">
            </form>
            <form id="userOrderCertSearchForm" method="post" class="form-horizontal">
                <input type="hidden" name="pageNum" id="pageNo" value="1">
                <input type="hidden" name="pageSize" id="pageSize" value="10">
                <input type="hidden" name="orderNumber" value="${orderNumber}">
            </form>
        </div>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane active" style="height: 700px;">
                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-content">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>订单ID</th>
                                        <th>订单号</th>
                                        <th>产品名称</th>
                                        <th>商品数量</th>
                                        <th>商品价格</th>
                                        <th>状态</th>
                                        <th>评价状态</th>
                                        <th>创建时间</th>
                                    </tr>
                                    </thead>
                                    <tbody id="userOrderTbody">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="ibox-content" style="height: 53px;">
                            <div class="page" id="userOrderPageDiv">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-2" class="tab-pane" style="height: 850px;">
                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-content">
                            <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                    <tr>
                                        <th>订单号</th>
                                        <th>凭证</th>
                                        <th>创建时间</th>
                                        <th>操作</th>
                                    </tr>
                                    </thead>
                                    <tbody id="userOrderCertTbody">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <#--<div class="ibox-content" style="height: 53px;">
                            <div class="page" id="userOrderCertPageDiv">
                            </div>
                        </div>-->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 下面的Tab页 -->
<#include "/order/tab/orders-list.ftl"/>
<#include "/order/tab/orders-cert-list.ftl"/>

<!-- 用户详情模板 -->
<textarea id="userDetail-template" style="display: none;">
    <form id="userDetailForm" method="post" class="form-horizontal">
        <div class="form-group">
            <!-- 左边 -->
            <div class="col-sm-6">
                <div class="form-group">
                    <#--<label class="col-sm-4 control-label">用户ID: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.userId}</p>
                    </div>-->
                    <label class="col-sm-4 control-label">订单号: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.orderNumber}</p>
                    </div>
                    <label class="col-sm-4 control-label">收货方式: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">
                        <#list DeliveryModeEnum?values as e>
                            {#if $T.data.deliveryMode == ${e.value}}${e.text}{#/if}
                        </#list>
                        </p>
                    </div>
                    <label class="col-sm-4 control-label">收货地址: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.address}</p>
                    </div>
                    <label class="col-sm-4 control-label">联系人: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.contact}</p>
                    </div>
                     <label class="col-sm-4 control-label">联系电话: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.telephone}</p>
                    </div>
                    <label class="col-sm-4 control-label">订单时间: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">
                            {#if $T.data.createTime!=null}{new Date($T.data.createTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}
                        </p>
                    </div>
                </div>
            </div>
            <!-- 右边 -->
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="col-sm-4 control-label">商品总金额: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.productPrice}</p>
                    </div>
                    <label class="col-sm-4 control-label">订单运费: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.freight}</p>
                    </div>
                    <label class="col-sm-4 control-label">订单总金额: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">{$T.data.price}</p>
                    </div>
                    <label class="col-sm-4 control-label">订单类型: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">
                        <#list UserOrderTypeEnum?values as e>
                            {#if $T.data.type == ${e.value}}${e.text}{#/if}
                        </#list>
                        </p>
                    </div>
                    <label class="col-sm-4 control-label">订单状态: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">
                        <#list UserOrderStatusEnum?values as e>
                            {#if $T.data.status == ${e.value}}${e.text}{#/if}
                        </#list>
                        </p>
                    </div>
                    <label class="col-sm-4 control-label">修改时间: </label>
                    <div class="col-sm-8">
                        <p class="form-control-static">
                            {#if $T.data.updateTime!=null}{new Date($T.data.updateTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}
                        </p>
                    </div>
                    <div class="col-sm-4">
                        <button id="btnUserStatusUpdate"  class="btn btn-success" type="button">状态修改</button>
                    </div>
                    <div class="col-sm-4">
                        <button id="btnUploadCert" class="btn btn-success" type="button">上传凭证</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
</textarea>

<div class="modal inmodal" id="modalUserStatusModify" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width: 300px;">
        <form id="userOrderStatusUpdateForm" method="post" class="form-horizontal">
            <input type="hidden" name="orderId" value="${orderId}">
            <input type="hidden" name="orderNumber" value="${orderNumber}">
            <div class="modal-content animated fadeIn" >
                <div class="modal-header" style="height: 20px;">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                            class="sr-only">Close</span></button>
                    <h4 class="modal-title" style="font-size: 22px;">修改订单状态</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="col-sm-12">
                            <select class="form-control" name="status" id="status">
                            <#list UserOrderStatusEnum?values as e>
                                <option value="${e.value}">${e.text}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnStatusCancel" class="btn btn-white" type="button">取消</button>
                        </div>
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnStatusSave" class="btn btn-success" type="button">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>


<div class="modal inmodal" id="modalUserCertModify" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width: 300px;">
        <form id="userOrderCertUpdateForm" method="post" class="form-horizontal">
            <input type="hidden" name="orderId" value="${orderId}">
            <input type="hidden" name="orderNumber" value="${orderNumber}">
            <div class="modal-content animated fadeIn" >
                <div class="modal-header" style="height: 20px;">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                            class="sr-only">Close</span></button>
                    <h4 class="modal-title" style="font-size: 22px;">上传凭证</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="col-sm-12">
                            <div id="file" class="dropzone" style="display: none"></div>
                            <label class="col-sm-4 control-label" style="text-align: left; width: 10%">产品图片</label>
                            <div id="addPicDiv">
                            </div>
                            <button type="button" class="btn btn-primary pull-right" style="margin-left:5px" id="addPic">选择图片</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnCertCancel" class="btn btn-white" type="button">取消</button>
                        </div>
                        <div class="col-sm-4 col-sm-offset-1">
                            <button id="btnCertSave" class="btn btn-success" type="button">保存</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- 分页模板 -->
<#include "/page.ftl"/>

<script>
    var _rootPath = "${ctx}";
</script>
<script src="${ctx}js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx}js/content.min.js?v=1.0.0"></script>
<script src="${ctx}js/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}js/common.js"></script>
<script src="${ctx}js/jquery-jtemplates.js"></script>
<script src="${ctx}js/dateutil.js"></script>
<script src="${ctx}js/plugins/dropzone/dropzone.js"></script>
<script src="${ctx}js/plugins/layer/layer.min.js"></script>
<script src="${ctx}js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script src="${ctx}${getVersion('js/page.js')}"></script>
<@sec.any name="USER_ORDER_UPDATE">
<script src="${ctx}${getVersion('js/order/user-order-detail.js')}"></script>
</@sec.any>
</body>
</html>