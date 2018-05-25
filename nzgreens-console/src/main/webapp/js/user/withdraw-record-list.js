$(document).ready(function () {
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
    post(_rootPath+"withdraw/record/search-list",$("#searchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#tbody").setTemplateElement("tr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);
    });

}