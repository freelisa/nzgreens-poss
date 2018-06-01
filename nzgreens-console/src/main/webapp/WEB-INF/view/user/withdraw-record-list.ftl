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
                    <form id="searchForm" method="post" class="form-horizontal">
                        <input type="hidden" name="pageNum" id="pageNo" value="1">
                        <input type="hidden" name="pageSize" id="pageSize" value="10">
                        <div class="form-group">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">代理手机号：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="mobile" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
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
                                    <th>序号</th>
                                    <th>代理ID</th>
                                    <th>代理电话</th>
                                    <th>昵称</th>
                                    <th>提现用户ID</th>
                                    <th>提现金额</th>
                                    <th>创建时间</th>
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

<textarea id="tr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as t}
        <tr>
            <td>{$T.t.id}</td>
            <td>{$T.t.userAgentId}</td>
            <td>{$T.t.mobile}</td>
            <td>{$T.t.nickName}</td>
            <td>{$T.t.userId}</td>
            <td>{Fen2Yuan($T.t.amount)}</td>
            <td>{new Date($T.t.createTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
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
<script src="${ctx}js/content.min.js?v=1.0.0"></script>
<script src="${ctx}js/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}js/common.js"></script>
<script src="${ctx}js/jquery-jtemplates.js"></script>
<script src="${ctx}js/dateutil.js"></script>
<script src="${ctx}js/plugins/layer/laydate/laydate.js"></script>
<script src="${ctx}${getVersion('js/page.js')}"></script>
<script src="${ctx}${getVersion('js/user/withdraw-record-list.js')}"></script>
</body>

</html>
