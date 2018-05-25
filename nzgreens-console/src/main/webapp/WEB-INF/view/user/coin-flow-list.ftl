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
                            <#--<div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">用户ID：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="userId" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>-->
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">手机号：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="telephone" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">流水类型：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="type" id="type">
                                            <option value="">全部</option>
                                        <#list AccountLogsTypeEnum?values as e>
                                            <option value="${e.value}">${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">用户类型：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="userType" id="userType">
                                            <option value="">全部</option>
                                            <option value="0">系统</option>
                                            <option value="1">非系统</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">开始时间：</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="startTime" name="startTime" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">结束时间：</label>
                                    <div class="col-sm-6">
                                        <input type="text" id="endTime" name="endTime" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                        <@sec.any name="COIN_FLOW_MANAGE">
                            <div class="form-group">
                                <div class="col-sm-4 col-sm-offset-2">
                                    <button id="btnSearch" class="btn btn-primary" data-loading-text="正在加载..." type="button">搜索</button>
                                    <button id="btnCancel" class="btn btn-white" type="button">取消</button>
                                </div>
                            </div>
                        </@sec.any>
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
                                    <th>用户ID</th>
                                    <th>电话</th>
                                    <th>账户流水ID</th>
                                    <th>流水类型</th>
                                    <th>流水前余额</th>
                                    <th>金额</th>
                                    <th>流水后余额</th>
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
            <td>{$T.t.userId}</td>
            <td>{$T.t.telephone}</td>
            <td>{$T.t.recordId}</td>
            <td>
            <#list AccountLogsTypeEnum?values as e>
                {#if $T.t.type == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{Fen2Yuan($T.t.before)}</td>
            <td>{Fen2Yuan($T.t.amount)}</td>
            <td>{Fen2Yuan($T.t.after)}</td>
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
<@sec.any name="COIN_FLOW_MANAGE">
<script src="${ctx}${getVersion('js/user/coin-flow-list.js')}"></script>
</@sec.any>
</body>

</html>
