$(document).ready(function () {
    var startTime={elem:"#startTime",format:"YYYY-MM-DD",max:"2099-06-16",istime:false,istoday:true};
    var endTime={elem:"#endTime",format:"YYYY-MM-DD",max:"2099-06-16",istime:false,istoday:true};
    laydate(startTime);
    laydate(endTime);

    $("#btnCancel").on("click", function () {
        $("#userSearchForm")[0].reset();
    });
    $("#btnSearch").on("click", function () {
        searchUserList();
    });

    searchUserList();
    pageFunInit(searchUserList);
});

//查询用户列表
function searchUserList() {
    $("#btnSearch").button('loading');
    post(_rootPath+"user/order/search-list",$("#userSearchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#userTbody").setTemplateElement("userTr-template", null, {filter_data: false});
        $("#userTbody").processTemplate(result);
        $("#userPageDiv").setTemplateElement("pager_template");
        $("#userPageDiv").processTemplate(result);

        $(".btnUserUpdate").on("click",function () {
            var orderId = $(this).attr("user-order-id");
            var orderNumber = $(this).attr("user-order-number");
            jumpTabPage("menuUserOrderDetail", "id=" + orderId + "&orderNumber=" + orderNumber);
        });
    });

}