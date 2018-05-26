<textarea id="userOrderTr-template" style="display: none;">
    {#if $T.data.total>0}
        {#foreach $T.data.list as user}
        <tr>
            <td>{$T.user.id}</td>
            <td>{$T.user.orderNumber}</td>
            <td>{$T.user.productId}</td>
            <td>{$T.user.productNumber}</td>
            <td>{$T.user.price}</td>
            <td>
            <#list OrderStatusEnum?values as e>
                {#if $T.user.status == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>
            <#list OrderCommentEnum?values as e>
                {#if $T.user.commentStatus == ${e.value}}${e.text}{#/if}
            </#list>
            </td>
            <td>{#if $T.user.createTime!=null}{new Date($T.user.createTime).Format('yyyy-MM-dd hh:mm:ss')}{#else}--{#/if}</td>
        </tr>
    {#else}
		<tr>
            <td colspan="4">暂无数据</td>
        </tr>
	{#/if}
</textarea>