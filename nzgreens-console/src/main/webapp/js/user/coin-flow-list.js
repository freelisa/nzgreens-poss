$(document).ready(function () {
    var startTime={elem:"#startTime",format:"YYYY-MM-DD",max:"2099-06-16",istime:false,istoday:true};
    var endTime={elem:"#endTime",format:"YYYY-MM-DD",max:"2099-06-16",istime:false,istoday:true};
    laydate(startTime);
    laydate(endTime);

    $("#btnCancel").on("click", function () {
        $("#searchForm")[0].reset();
    });
    $("#btnSearch").on("click", function () {
        searchList();
    });
    searchList();
    pageFunInit(searchList);
});

function searchList() {
    $("#btnSearch").button('loading');
    post(_rootPath+"coin/flow/search-list",$("#searchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#tbody").setTemplateElement("tr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);
    });

}