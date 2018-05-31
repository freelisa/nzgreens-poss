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

<textarea id="appConTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as appCon}
        <tr>
            <td>{$T.appCon.id}</td>
            <td>{$T.appCon.reptileProductId}</td>
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
            <td>{$T.appCon.costPrice}</td>
            <td>{$T.appCon.sellingPrice}</td>
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
                <@sec.any name="PRODUCT_CRAWL_UPDATE">
                <button type="button" productId="{$T.appCon.id}" class="btn btn-primary btnDetail">通过</button>
                <button type="button" productId="{$T.appCon.id}" class="btn btn-danger btnDelete">拒绝</button>
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
<script src="${ctx}${getVersion('js/product/product-crawl-audit-list.js')}"></script>
</body>

</html>
