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
    <link href="${ctx}css/plugins/summernote/summernote.css" rel="stylesheet">
    <link href="${ctx}css/plugins/summernote/summernote-bs3.css" rel="stylesheet">
    <style>
        .note-editor .note-editable {
            height: 400px !important;
        }
    </style>
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <form id="userSearchForm" method="post" class="form-horizontal">
        <input type="hidden" name="pageNum" id="pageNo" value="1">
        <input type="hidden" name="pageSize" id="pageSize" value="10">
        <input type="hidden" name="userId" value="${agentUserId}">
    </form>

    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>用户ID</th>
                                    <th>电话号码</th>
                                    <th>用户类型</th>
                                    <th>余额</th>
                                    <th>是否有效</th>
                                    <th>注册时间</th>
                                    <th>更新时间</th>
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

<textarea id="userTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <td>{$T.user.id}</td>
            <td>{$T.user.telephone}</td>
            <td>
            <#list UserTypeEnum?values as e>
                {#if $T.user.type == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{Fen2Yuan($T.user.balance)}</td>
            <td>
            <#list IsValidEnum?values as e>
                {#if $T.user.isValid == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{#if $T.user.createTime!=null}{new Date($T.user.createTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
            <td>{#if $T.user.updateTime!=null}{new Date($T.user.updateTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
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
<@sec.any name="USER_MANAGE">
<script src="${ctx}${getVersion('js/user/user-agent-list.js')}"></script>
</@sec.any>
</body>

</html>
