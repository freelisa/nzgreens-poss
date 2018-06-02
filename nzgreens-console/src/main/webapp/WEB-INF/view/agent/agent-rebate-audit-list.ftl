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
                                    <label class="col-sm-4 control-label">代理用户账号：</label>
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
                                    <label class="col-sm-4 control-label">返佣类型：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="type" id="type">
                                            <option value="">全部</option>
                                        <#list AgentRebateTypeEnum?values as e>
                                            <option value="${e.value}">${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">审核状态：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="status" id="status">
                                            <option value="">全部</option>
                                        <#list AgentRebateAuditStatusEnum?values as e>
                                            <option value="${e.value}" <#if e.value == 0>selected="selected"</#if>>${e.text}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
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
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>代理ID</th>
                                    <th>代理用户账号</th>
                                    <th>订单号</th>
                                    <th>返佣类型</th>
                                    <th>返佣金额</th>
                                    <th>实际返佣金额</th>
                                    <th>审核状态</th>
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
            <@sec.any name="AGENT_REBATE_AUDIT_UPDATE">
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
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">代理用户账号</label>
            <div class="col-sm-6">
                <p class="form-control-static">{$T.data.mobile}</p>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">订单号</label>
            <div class="col-sm-6">
                <p class="form-control-static">{$T.data.orderNumber}</p>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">返佣类型</label>
            <div class="col-sm-6">
                <p class="form-control-static">{$T.data.typeDesc}</p>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">返佣金额</label>
            <div class="col-sm-6">
                <p class="form-control-static">{Fen2Yuan($T.data.rebatePrice)}</p>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">实际返佣金额</label>
            <div class="col-sm-6">
                <input type="number" name="actualRebatePrice" class="form-control" id="actualRebatePrice" value="{Fen2Yuan($T.data.actualRebatePrice)}">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 20%">状态</label>
            <div class="col-sm-6">
                <select class="form-control" name="status" id="updateStatus">
                    <option value="">请选择</option>
                <#list AgentRebateAuditStatusEnum?values as e>
                    <#if e.value != 0>
                        <option value="${e.value}">${e.text}</option>
                    </#if>
                </#list>
                </select>
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
            <td>{$T.agent.agentUserId}</td>
            <td>{$T.agent.mobile}</td>
            <td>{$T.agent.orderNumber}</td>
            <td>
                <#list AgentRebateTypeEnum?values as e>
                    {#if $T.agent.type == ${e.value}}${e.text}{#/if}
                </#list>
            </td>
            <td>{Fen2Yuan($T.agent.rebatePrice)}</td>
            <td>{Fen2Yuan($T.agent.actualRebatePrice)}</td>
            <td>
                <#list AgentRebateAuditStatusEnum?values as e>
                    {#if $T.agent.status == ${e.value}}${e.text}{#/if}
                </#list>
            </td>
            <td>{new Date($T.agent.createTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>{new Date($T.agent.updateTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>
                <@sec.any name="AGENT_REBATE_AUDIT_UPDATE">
                    {#if $T.agent.status == 0}
                     <button type="button" agentId="{$T.agent.id}" class="btn btn-primary btnDetail">审核</button>
                    {#/if}
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
<script src="${ctx}${getVersion('js/agent/agent-rebate-audit-list.js')}"></script>
</body>

</html>
