$(document).ready(function () {
    searchUserList();
    pageFunInit(searchUserList);
});

//查询用户列表
function searchUserList() {
    $("#btnSearch").button('loading');
    post(_rootPath+"user/agent/search-list",$("#userSearchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#userTbody").setTemplateElement("userTr-template", null, {filter_data: false});
        $("#userTbody").processTemplate(result);
        $("#userPageDiv").setTemplateElement("pager_template");
        $("#userPageDiv").processTemplate(result);
    });

}