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
    <form id="searchForm" method="post" class="form-horizontal">
        <input type="hidden" name="pageNum" id="pageNo" value="1">
        <input type="hidden" name="pageSize" id="pageSize" value="10">
    </form>

    <div class="row">
        <div class="col-sm-12">
            <div class="form-group">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <@sec.any name="PRODUCT_FREIGHT_UPDATE">
                        <div class="row">
                            <div class="col-sm-3">
                                   <button type="button" class="btn btn-success " id="add">
                                       <i class="fa fa-plus"></i>&nbsp;&nbsp;<span class="bold">新增</span>
                                   </button>
                            </div>
                        </div>
                    </@sec.any>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>商品重量</th>
                                    <th>运费</th>
                                    <th>创建时间</th>
                                    <th>修改时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody id="tbody">
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="ibox-content" style="height: 53px;">
                    <div class="page" id="pageDiv">

                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- 新增弹出框 -->
<div class="modal inmodal" id="modalAdd" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content animated fadeIn" style="width: 500px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">新增</h4>
            </div>
            <div class="modal-body">
                <form id="addForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 20%">商品重量</label>
                        <div class="col-sm-6">
                            <input type="number" name="productWeight" class="form-control" id="addProductWeight">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 20%">运费</label>
                        <div class="col-sm-6">
                            <input name="freight" class="form-control" id="addFreight">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="PRODUCT_FREIGHT_UPDATE">
                <button type="button" id="btnAdd" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>


<!-- 详情弹出框 -->
<div class="modal inmodal" id="modalDetail" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content animated fadeIn" style="width: 500px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">详情</h4>
            </div>
            <div class="modal-body" id="detailDiv">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="PRODUCT_FREIGHT_UPDATE">
                <button type="button" id="btnSaveUpdate" class="btn btn-primary">保存</button>
                </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 详情模板 -->
<textarea id="detailTr-template" style="display: none;">
    <form id="detailForm" method="post" class="form-horizontal">
        <input type="hidden" class="form-control" value="{$T.data.id}" id="agentId">
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">商品重量</label>
            <div class="col-sm-6">
                <input type="number" name="productWeight" class="form-control" id="updateProductWeight" value="{$T.data.productWeight}">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">运费</label>
            <div class="col-sm-6">
                <input name="freight" class="form-control" id="updateFreight" value="{$T.data.freight}">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
    </form>
</textarea>

<textarea id="tr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as agent}
        <tr>
            <td>{$T.agent.id}</td>
            <td>{$T.agent.productWeight}</td>
            <td>{$T.agent.freight}</td>
            <td>{new Date($T.agent.createTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>{new Date($T.agent.updateTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>
                <button type="button" agentId="{$T.agent.id}" class="btn btn-primary btnDetail">查看</button>
                <@sec.any name="PRODUCT_FREIGHT_UPDATE">
                <button type="button" agentId="{$T.agent.id}" class="btn btn-danger btnDelete">删除</button>
                </@sec.any>
            </td>
        </tr>
    {#else}
		<tr>
            <td colspan="8">暂无数据</td>
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
<script src="${ctx}js/common.js"></script>
<script src="${ctx}js/jquery-jtemplates.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}js/dateutil.js"></script>
<script src="${ctx}js/plugins/layer/layer.min.js"></script>
<script src="${ctx}${getVersion('js/page.js')}"></script>
<script src="${ctx}${getVersion('js/product/product-freight-list.js')}"></script>
</body>

</html>
