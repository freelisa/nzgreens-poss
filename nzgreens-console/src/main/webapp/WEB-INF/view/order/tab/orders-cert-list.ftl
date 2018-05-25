<textarea id="userOrderCertTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <td>{$T.user.orderNumber}</td>
            <td><a href="{$T.user.certificateUrl}" title="图片" data-gallery=""><img src="{$T.user.certificateUrl}" width="60px" /></a></td>
            <td>{#if $T.user.createTime!=null}{new Date($T.user.createTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
            <td>
            <@sec.any name="USER_ORDER_UPDATE">
                <button type="button" user-order-id="{$T.user.id}" class="btn btn-primary btnUserCertDel">删除</button>
            </@sec.any>
            </td>
        </tr>
    {#else}
		<tr>
            <td colspan="4">暂无数据</td>
        </tr>
	{#/if}
</textarea>
<div id="blueimp-gallery" class="blueimp-gallery">
    <div class="slides"></div>
    <h3 class="title"></h3>
    <a class="prev"><</a>
    <a class="next">></a>
    <a class="close">×</a>
    <a class="play-pause"></a>
    <ol class="indicator"></ol>
</div>