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
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">关键字：</label>
                                    <div class="col-sm-8">
                                        <input type="text" name="keyword" class="form-control" placeholder="请输入关键字检索用户名、角色名、手机号码">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">所属角色：</label>
                                    <div class="col-sm-8">
                                        <select class="form-control" name="roleId">
                                            <option value="">请选择</option>
                                        <#list adminRoles as adminRole>
                                            <option value="${adminRole.id}">${adminRole.name}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-2">
                                <button id="btnSearch" class="btn btn-primary" type="button">搜索</button>
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
            <div class="form-group">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <@sec.any name="ADMIN_USER_ADD">
                        <div class="row">
                            <div class="col-sm-3">
                                   <button type="button" class="btn btn-success " id="addAdminUser">
                                       <i class="fa fa-plus"></i>&nbsp;&nbsp;<span class="bold">新增</span>
                                   </button>
                            </div>
                        </div>
                    </@sec.any>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>用户名</th>
                                    <th>角色</th>
                                   <#-- <th>市场</th>-->
                                    <th>手机</th>
                                    <th>真实姓名</th>
                                    <th>创建时间</th>
                                    <th>状态</th>
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

<!-- 详情弹出框 -->
<div class="modal inmodal" id="modalUserAdd" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">新增用户</h4>
            </div>
            <div class="modal-body">
                <form id="userAddForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">用户名</label>
                        <div class="col-sm-6">
                            <input type="text" name="username" required minlength="6" maxlength="255" class="form-control" placeholder="请输入用户名">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">密码</label>
                        <div class="col-sm-6">
                            <input type="password" id="password" required minlength="6" maxlength="255" class="form-control" name="password">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">确认密码</label>
                        <div class="col-sm-6">
                            <input type="password" equalTo="#password" class="form-control" name="confirmpPassword">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">请选择角色</label>
                        <div class="col-sm-6">
                            <select name="roleId" required  class="form-control">
                                <option value="">请选择</option>
                            <#list adminRoles as adminRole>
                                <option value="${adminRole.id}">${adminRole.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">真实姓名</label>
                        <div class="col-sm-6">
                            <input type="text" name="realName" minlength="2" maxlength="20" class="form-control" placeholder="">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="ADMIN_USER_ADD">
                <button type="button" id="btnAddUser" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 详情弹出框 -->
<div class="modal inmodal" id="modalUserDetail" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">用户详情</h4>
            </div>
            <div class="modal-body" id="userDetailDiv">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                <@sec.any name="ADMIN_USER_UPDATE">
                <button type="button" id="btnSaveUpdate" class="btn btn-primary">保存</button>
                </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 用户详情模板 -->
<textarea id="userDetail-template" style="display: none;">
    <form id="userDetailForm" method="post" class="form-horizontal">
        <input type="hidden" name="id" value="{$T.data.id}">
        <div class="form-group">
            <label class="col-sm-3 control-label">用户名</label>
            <div class="col-sm-9">
                <input type="text" value="{$T.data.username}" name="username" required="" minlength="2" class="form-control" placeholder="请输入用户名">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-3 control-label">市场代码</label>
            <div class="col-sm-9">
                <input type="text" value="{$T.data.market}" name="market" maxlength="2" class="form-control" placeholder="请输入市场代码">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-3 control-label">请选择角色</label>
            <div class="col-sm-9">
                <select id="roleId" name="roleId" class="form-control">
                    <option value="">请选择</option>
                <#list adminRoles as adminRole>
                    <option value="${adminRole.id}">${adminRole.name}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-3 control-label">手机号码</label>
            <div class="col-sm-9">
                <input type="text" value="{$T.data.mobile}" name="mobile"  required="" minlength="2" class="form-control" placeholder="">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-3 control-label">真实姓名</label>
            <div class="col-sm-9">
                <input type="text" value="{$T.data.realName}" name="realName" required="" minlength="2" class="form-control" placeholder="">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
    </form>
</textarea>

<textarea id="userTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <td>{$T.user.username}</td>
            <td>{$T.user.roleName}</td>
            <#--<td>{$T.user.market}</td>-->
            <td>{$T.user.mobile}</td>
            <td>{$T.user.realName}</td>
            <td>{new Date($T.user.createTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>{#if $T.user.status==0}正常{#else}冻结{#/if}</td>
            <td>
                <@sec.any name="ADMIN_USER_UPDATE">
                <button type="button" userId="{$T.user.id}" class="btn btn-primary btnUserDetail">查看</button>
                </@sec.any>
                <@sec.any name="ADMIN_USER_PASSWORD_RESET">
                    <button type="button" userId="{$T.user.id}" class="btn btn-warning btnUserReset">重置密码</button>
                </@sec.any>
                <@sec.any name="ADMIN_USER_DELETE">
                <button type="button" userId="{$T.user.id}" class="btn btn-danger btnUserDelete">删除</button>
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
<script src="${ctx}${getVersion('js/page.js')}"></script>
<script src="${ctx}${getVersion('js/system/user-list.js')}"></script>
</body>

</html>
