$(document).ready(function () {
    searchList();
    pageFunInit(searchList);

    //删除
    $(document).on("click",".btnUpdate",function(){
        var id = $(this).attr("productId");
        updateData(id);
    });
});

function searchList() {
    $("#btnSearch").button('loading');
    post(_rootPath+"product/change/search-list",$("#searchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#tbody").setTemplateElement("tr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);
    });

}

function updateData(id) {
    layer.confirm('是否将改条数据设置为已读？如有需要请去商品管理设置最新售价', function(index){
        post(_rootPath + "product/change/update", {"id":id}, function (result) {
            if (result.success == true) {
                layer.close(index);
                swal({title: "提示", text: "设置成功", type: 'success'}, function () {
                    searchList();
                });
            }
        });
    });
}