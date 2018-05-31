$(document).ready(function () {
    searchList();

    //拒绝
    $(document).on("click",".btnDelete",function(){
        var id = $(this).attr("productId");
        updateData(id,0);
    });

    //通过
    $(document).on("click",".btnDetail",function(){
        var id = $(this).attr("productId");
        updateData(id,1);
    });

    pageFunInit(searchList);
});

//查询列表
function searchList() {
    post(_rootPath + "product/crawl/search-list", $("#searchForm").serialize(), function (result) {
        $("#tbody").setTemplateElement("appConTr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);
    });
}

function updateData(id,status) {
    var message = "";
    if(status == 0){
        message = "是否拒绝该抓取的产品，拒绝的产品不会出现的app列表中";
    }else{
        message = "是否通过该抓取的产品，设置为产品";
    }
    layer.confirm(message, function(index){
        post(_rootPath + "product/crawl/update", {"productId":id,"status":status}, function (result) {
            if (result.success == true) {
                layer.close(index);
                swal({title: "提示", text: "审核成功", type: 'success'}, function () {
                    searchList();
                });
            }
        });
    });
}