$(document).ready(function () {
    searchList();
    pageFunInit(searchList);
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