<textarea id="pager_template" style="display: none;">
    {#if $T.data.total > 0}
        共有 <span>{$T.data.total}</span> 条记录
        <button class="btn btn-white pageButton" type="button" page="1">首页</button>
        <button class="btn btn-white {#if $T.data.pageNum==1} curr-disable {#/if} pageButton"  type="button" page="{$T.data.pageNum-1}">上一页</button>
        {#foreach $T.data.navigatepageNums as p}
            <button class="btn btn-white pageButton {#if $T.data.pageNum==$T.p} curr-page {#/if}" type="button" title="{$T.p}" page="{$T.p}">{$T.p}</button>
        {#/for}
        <button class="btn btn-white {#if $T.data.pageNum==$T.data.pages} curr-disable {#/if}  pageButton" type="button" page="{$T.data.pageNum+1}">下一页</button>
        <button class="btn btn-white pageButton" type="button" page="{$T.data.pages}">尾页</button>
        <input type="hidden" id="totalPage" value="{$T.data.pages}">
        跳转<input type="text" size="3"  id="inputPageNo" placeholder="{$T.data.pageNum}" oldValue="{$T.data.pageNum}"  value="{$T.data.pageNum}" onkeyup="clearNoNum(this,0);" onafterpaste="clearNoNum(this,0);"> /<span class="all-page">{$T.data.pages}</span>页，每页显示
        <input type="text" size="3" id="inputPageSize" placeholder="{$T.data.pageSize}" oldValue="{$T.data.pageSize}" value="{$T.data.pageSize}" onkeyup="clearNoNum(this,0);" onafterpaste="clearNoNum(this,0);">条
    {#/if}
</textarea>