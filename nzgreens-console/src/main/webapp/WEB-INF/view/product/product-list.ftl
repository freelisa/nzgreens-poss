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
    <link href="${ctx}css/plugins/dropzone/basic.css" rel="stylesheet">
    <link href="${ctx}css/plugins/dropzone/dropzone.css" rel="stylesheet">
    <link href="${ctx}css/plugins/summernote/summernote.css" rel="stylesheet">
    <link href="${ctx}css/plugins/summernote/summernote-bs3.css" rel="stylesheet">
    <style>
        .note-editor .note-editable {
            height: 400px !important;
        }
    </style>
</head>

<#assign updateSec = "PRODUCT_UPDATE" />
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
                                    <label class="col-sm-4 control-label">商品ID：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="productId" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">标题：</label>
                                    <div class="col-sm-6">
                                        <input type="text" name="title" class="form-control" placeholder="">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">品牌：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="brandId">
                                            <option value="">请选择</option>
                                        <#list brandList as b>
                                            <option value="${b.id}">${b.name}</option>
                                        </#list>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group">
                                    <label class="col-sm-4 control-label">分类：</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" name="categoryId">
                                            <option value="">请选择</option>
                                        <#list categoryList as b>
                                            <option value="${b.id}">${b.name}</option>
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
                                            <option value="${e.value}" <#if e.value == 1>selected="selected"</#if>>${e.text}</option>
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
                    <@sec.any name=updateSec>
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
                                    <th>格林商品ID</th>
                                    <th>发布内容</th>
                                    <th>品牌</th>
                                    <th>商品重量(单位：克)</th>
                                    <th>商品图片</th>
                                    <th>原价</th>
                                    <th>售价</th>
                                    <th>分类</th>
                                    <th>库存</th>
                                    <th>商品评分</th>
                                    <th>销量</th>
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
        <div class="modal-content animated fadeIn" style="width: 900px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">新增</h4>
            </div>
            <div class="modal-body">
                <form id="addForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">标题</label>
                        <div class="col-sm-6">
                            <textarea name="title" class="form-control" rows="3" cols="40" id="addTitle"></textarea>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">品牌</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="brandId" id="addBrandId">
                                <option value="">请选择</option>
                                <#list brandList as b>
                                    <option value="${b.id}">${b.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">重量</label>
                        <div class="col-sm-6">
                            <input type="text" name="weight" class="form-control" id="addWeight">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">状态</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="IsValid" id="addStatus">
                                <option value="">请选择</option>
                            <#list IsValidEnum?values as e>
                                <option value="${e.value}">${e.text}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <div id="file" class="dropzone" style="display: none"></div>
                        <label class="col-sm-4 control-label" style="text-align: left; width: 10%">产品图片</label>
                        <input type="hidden" name="contentPic" id="contentPicId">
                        <div id="addPicDiv">
                        </div>
                        <button type="button" class="btn btn-primary pull-right" style="margin-left:5px" id="delPic">删除图片</button>
                        <button type="button" class="btn btn-primary pull-right" style="margin-left:5px" id="addPic">选择图片</button>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">原价</label>
                        <div class="col-sm-6">
                            <input type="text" name="costPrice" class="form-control" id="addCostPrice">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">售价</label>
                        <div class="col-sm-6">
                            <input type="text" name="sellingPrice" class="form-control" id="addSellPrice">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">库存</label>
                        <div class="col-sm-6">
                            <input type="text" name="stock" class="form-control" id="addStock">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" style="text-align: left; width: 7%">分类</label>
                        <div class="col-sm-6">
                            <select class="form-control" name="categoryId" id="addCategoryId">
                                <option value="">请选择</option>
                            <#list categoryList as b>
                                <option value="${b.id}">${b.name}</option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <input type="hidden" name="detail" value="" id="addDetail">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="ibox float-e-margins">
                                    <div class="ibox-title">
                                        <h5>详情</h5>
                                        <div class="ibox-tools">
                                            <a class="collapse-link">
                                                <i class="fa fa-chevron-up"></i>
                                            </a>
                                        </div>
                                    </div>
                                    <div class="ibox-content no-padding">
                                        <div class="summernote" id="summernote1"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            <@sec.any name=updateSec>
                <button type="button" id="btnAdd" class="btn btn-primary">保存</button>
            </@sec.any>
            </div>
        </div>
    </div>
</div>


<!-- 详情弹出框 -->
<div class="modal inmodal" id="modalDetail" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog" style="width: 900px">
        <div class="modal-content animated fadeIn" style="width: 900px">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">详情</h4>
            </div>
            <div class="modal-body" id="detailDiv">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                <@sec.any name=updateSec>
                <button type="button" id="btnSaveUpdate" class="btn btn-primary">保存</button>
                </@sec.any>
            </div>
        </div>
    </div>
</div>

<!-- 详情模板 -->
<textarea id="tr-template" style="display: none;">
    <form id="detailForm" method="post" class="form-horizontal">
        <input type="hidden" class="form-control" value="{$T.data.id}" id="productId">
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 10%">商品ID</label>
            <div class="col-sm-6">
                 <p class="form-control-static">{$T.data.id}</p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 10%">格林商品ID</label>
            <div class="col-sm-6">
                <p class="form-control-static">{$T.data.gelinProductId}</p>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">标题</label>
            <div class="col-sm-6" id="detailTextarea">

            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">品牌</label>
            <div class="col-sm-6">
                <select class="form-control" name="brandId" id="detailBrandId">
                    <option value="">请选择</option>
                <#list brandList as b>
                    <option value="${b.id}">${b.name}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">重量</label>
            <div class="col-sm-6">
                <input type="text" name="weight" class="form-control" value="{$T.data.weight}" id="detailWeight">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">状态</label>
            <div class="col-sm-6">
                <select class="form-control" name="IsValid" id="detailIsValid">
                    <option value="">请选择</option>
                <#list IsValidEnum?values as e>
                    <option value="${e.value}">${e.text}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <div id="file" class="dropzone" style="display: none"></div>
            <label class="col-sm-4 control-label" style="text-align: left; width: 10%">产品图片</label>
            <input type="hidden" name="contentPic" id="detailContentPicId" value="{$T.data.image}">
            <div id="detailAddPicDiv">
            </div>
            <button type="button" id="detailDelPic"  class="btn btn-primary pull-right" style="margin-left:5px">删除图片</button>
            <button type="button" id="detailAddPic"  class="btn btn-primary pull-right" style="margin-left:5px">选择图片</button>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">原价</label>
            <div class="col-sm-6">
                <input type="text" name="costPrice" class="form-control" value="{Fen2Yuan($T.data.costPrice)}" id="detailCostPrice">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">售价</label>
            <div class="col-sm-6">
                <input type="text" name="sellingPrice" class="form-control" value="{Fen2Yuan($T.data.sellingPrice)}" id="detailSellPrice">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">库存</label>
            <div class="col-sm-6">
                <input type="text" name="stock" class="form-control" value="{$T.data.stock}" id="detailStock">
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <label class="col-sm-4 control-label" style="text-align: left; width: 7%">分类</label>
            <div class="col-sm-6">
                <select class="form-control" name="categoryId" id="detailCategoryId">
                    <option value="">请选择</option>
                <#list categoryList as b>
                    <option value="${b.id}">${b.name}</option>
                </#list>
                </select>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
        <div class="form-group">
            <input type="hidden" name="detail" value="" id="updateDetail">
            <div class="row">
                <div class="col-sm-12">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>详情</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content no-padding">
                            <div class="summernote" id="summernote2"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="hr-line-dashed"></div>
    </form>
</textarea>

<textarea id="appConTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as appCon}
        <tr>
            <td>{$T.appCon.id}</td>
            <td>{$T.appCon.gelinProductId}</td>
            <td>{$T.appCon.title}</td>
            <td>
            <#list brandList as b>
                {#if $T.appCon.brandId=="${b.id}"}${b.name}{#/if}
            </#list>
            </td>
            <td>{$T.appCon.weight}</td>
            <td>
                {#if $T.appCon.image!=null && $T.appCon.image!=''}
                 <img src="{$T.appCon.image}" width="60px" height="60px">
                {#/if}
            </td>
            <td>{Fen2Yuan($T.appCon.costPrice)}</td>
            <td>{Fen2Yuan($T.appCon.sellingPrice)}</td>
            <td>{Fen2Yuan($T.appCon.crawlSellingPrice)}</td>
             <td>
             <#list categoryList as c>
                 {#if $T.appCon.categoryId=="${c.id}"}${c.name}{#/if}
             </#list>
            </td>
            <td>{#if $T.appCon.stock!=null && $T.appCon.stock!=''}{$T.appCon.stock}{#/if}</td>
            <td>{#if $T.appCon.score!=null && $T.appCon.score!=''}{$T.appCon.score}{#/if}</td>
            <td>{#if $T.appCon.salesVolume!=null && $T.appCon.salesVolume!=''}{$T.appCon.salesVolume}{#/if}</td>
            <td>{new Date($T.appCon.createTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>{new Date($T.appCon.updateTime).Format('yyyy-MM-dd hh:mm:ss')}</td>
            <td>
                <button type="button" productId="{$T.appCon.id}" class="btn btn-primary btnDetail">查看</button>
                <@sec.any name="PRODUCT_UPDATE">
               <button type="button" productId="{$T.appCon.id}" class="btn btn-danger btnDelete">删除</button>
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
<script src="${ctx}js/plugins/dropzone/dropzone.js"></script>
<script src="${ctx}js/plugins/summernote/summernote.min.js"></script>
<script src="${ctx}js/plugins/summernote/summernote-zh-CN.js"></script>
<script src="${ctx}js/plugins/layer/layer.min.js"></script>
<script src="${ctx}${getVersion('js/page.js')}"></script>
<script src="${ctx}${getVersion('js/product/product-list.js')}"></script>
</body>

</html>
