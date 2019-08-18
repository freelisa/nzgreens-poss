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
                                    <label class="col-sm-4 control-label">用户账号：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="mobile" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">用户类型：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="type" id="type">
                                            <option value="">全部</option>
                                            <#list UserTypeEnum?values as e>
                                                <option value="${e.value}">${e.text}</option>
                                            </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">状态：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="isValid">
                                            <option value="">请选择</option>
                                        <#list IsValidEnum?values as e>
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
                            <@sec.any name="USER_MANAGE">
                                <div class="col-md-3">
                                    <div class="form-group">
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
                    <@sec.any name="USER_UPDATE">
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
                                    <#--<th>用户ID</th>-->
                                    <th>用户账号</th>
                                    <th>代理用户账号</th>
                                    <th>用户类型</th>
                                    <th>余额</th>
                                    <th>用户资产</th>
                                    <th>是否有效</th>
                                    <th>备注</th>
                                    <th>总订单量</th>
                                    <th>最后下单时间</th>
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
                        <label class="col-sm-4 control-label" style="text-align: left; width:20%">用户账号</label>
                        <div class="col-sm-6">
                            <input type="text" name="mobile" class="form-control" id="addMobile" maxlength="11">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width:20%">用户类型</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="type" id="addType">
                                <option value="">请选择</option>
                            <#list UserTypeEnum?values as b>
                                <option value="${b.value}">${b.text}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group" id="agentSelectDiv">
                        <label class="col-sm-4 control-label" style="text-align: left; width:20%">代理</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="agentUserId" id="addAgentUserId">
                                <option value="">请选择</option>
                                <#if agentList?? && agentList?size gt 0>
                                    <#list agentList as b>
                                        <option value="${b.id}">${b.telephone}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 20%">备注</label>
                        <div class="col-sm-6">
                            <textarea name="remark" class="form-control" rows="3" cols="40" id="addRemark"></textarea>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="USER_UPDATE">
                <button type="button" id="btnAdd" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 快捷新增弹出框 -->
<div class="modal inmodal" id="modalQuickAdd" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content animated fadeIn" style="width: 500px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">新增</h4>
            </div>
            <div class="modal-body">
                <form id="quickAddForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width:20%">用户账号</label>
                        <div class="col-sm-6">
                            <input type="text" name="telephone" class="form-control" id="quickAddMobile" maxlength="11">
                        </div>
                    </div>
                    <input hidden name="agentUserId" id="quickAgentUserId" value="">
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 20%">备注</label>
                        <div class="col-sm-6">
                            <textarea name="remark" class="form-control" rows="3" cols="40" id="quickAddRemark"></textarea>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="USER_UPDATE">
                <button type="button" id="btnQuickAdd" class="btn btn-primary">保存</button>
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
            <@sec.any name="USER_UPDATE">
                <button type="button" id="btnSaveUpdate" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 详情模板 -->
<textarea id="tr-template" style="display: none;">
    <form id="detailForm" method="post" class="form-horizontal">
        <input type="hidden" class="form-control" value="{$T.data.id}" id="userId">
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">用户账号</label>
            <div class="col-sm-6">
                <input type="text" name="mobile" class="form-control" id="detailMobile" disabled="disabled" value="{$T.data.telephone}">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">用户类型</label>
            <div class="col-sm-6">
                <select class="form-control" name="type" id="detailType">
                    <option value="">请选择</option>
                <#list UserTypeEnum?values as b>
                    <option value="${b.value}">${b.text}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">代理</label>
            <div class="col-sm-6">
                <select class="form-control" name="agentUserId" id="updateAgentUserId">
                    <option value="">请选择</option>
                    <#if agentList?? && agentList?size gt 0>
                        <#list agentList as b>
                            <option value="${b.id}">${b.telephone}</option>
                        </#list>
                    </#if>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">是否冻结</label>
            <div class="col-sm-6">
                <select class="form-control" name="isValid" id="detailStatus">
                    <option value="">请选择</option>
                <#list IsValidEnum?values as e>
                    <option value="${e.value}">${e.text}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">备注</label>
            <div class="col-sm-6">
                <textarea name="remark" class="form-control" rows="3" cols="40" id="updateRemark">{$T.data.remark}</textarea>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
    </form>
</textarea>

<!-- 余额弹出框 -->
<div class="modal inmodal" id="modalBalanceDetail" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content animated fadeIn" style="width: 500px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">详情</h4>
            </div>
            <div class="modal-body" id="balanceDetailDiv">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name="USER_UPDATE">
                <button type="button" id="btnUpdateBalance" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 操作余额模板 -->
<textarea id="balanceTr-template" style="display: none;">
    <form id="balanceDetailForm" method="post" class="form-horizontal">
        <input type="hidden" class="form-control" value="{$T.data.id}" id="balanceUserId">
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">增减余额</label>
            <div class="col-sm-6">
                <input type="text" name="balance" class="form-control" id="updateBalance">
            </div>
        </div>
         <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width:20%">备注</label>
            <div class="col-sm-6">
                <textarea name="remark" class="form-control" id="updateRemark" rows="3" cols="40"></textarea>
            </div>
        </div>
    </form>
</textarea>

<textarea id="userTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <#--<td>{$T.user.id}</td>-->
            <td>{$T.user.telephone}</td>
            <td>{$T.user.agentMobile}</td>
            <td>
            <#list UserTypeEnum?values as e>
                {#if $T.user.type == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{Fen2Yuan($T.user.balance)}</td>
            <td>{#if $T.user.totalBalance!=null}{Fen2Yuan($T.user.totalBalance)}{#else}{Fen2Yuan($T.user.balance)}{#/if}</td>
            <td>
            <#list IsValidEnum?values as e>
                {#if $T.user.isValid == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{$T.user.remark}</td>
            <td>{$T.user.totalOrderCount}</td>
            <td>{#if $T.user.lastOrderTime!=null}{new Date($T.user.lastOrderTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
            <td>
            <@sec.any name="USER_UPDATE">
                <button type="button" userId="{$T.user.id}" class="btn btn-primary btnUserUpdate">修改</button>
            </@sec.any>
            <@sec.any name="USER_UPDATE">
                <button type="button" userId="{$T.user.id}" class="btn btn-primary btnUserBalance">修改用户余额</button>
            </@sec.any>
            <@sec.any name="USER_UPDATE">
                 <button type="button" userId="{$T.user.id}" class="btn btn-primary btnUserReset">重置密码</button>
            </@sec.any>
            <@sec.any name="USER_UPDATE">
                <button type="button" userId="{$T.user.id}" class="btn btn-danger btnUserFrozen">冻结</button>
            </@sec.any>
            {#if $T.user.type == 2}
            <button type="button" user-id="{$T.user.id}" agent-mobile="{$T.user.telephone}" class="btn btn-primary btnUserAgent">代理下用户</button>
            <button type="button" user-id="{$T.user.id}" agent-mobile="{$T.user.telephone}" class="btn btn-primary btnAgentAddUser">新增用户</button>
            {#/if}
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
<@sec.any name="USER_MANAGE">
<script src="${ctx}${getVersion('js/user/user-list.js')}"></script>
</@sec.any>
</body>

</html>
