$(document).ready(function () {
    $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});

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

    $("#btnNumberCancel").on("click", function () {
        $("#modalUserNumberModify").modal("hide");
    });

    $("#btnNumberSave").on("click",function () {
        saveLogisticsNumber();
    });

    $('input[name=btnSelectAll]').on("click",function() {
        if($(this).is(':checked')){
            $('input[name="orderCheckId"]').each(function(){
                $(this).prop("checked",true);
            });
        }else{
            $('input[name="orderCheckId"]').each(function(){
                $(this).removeAttr("checked",false);
            });
        }
    });

    $("#exportExcel").on("click", function () {
        $("#modalUserOrderExport").modal();
    });

    $("#btnUserOrderExportCancel").on("click", function () {
        $("#modalUserOrderExport").modal("hide");
    });
    var checkStr = "";
    $("#btnUserOrderExportSave").on("click",function () {
        checkStr = "";
        $("input[name=orderCheckId]:checked").each(function(i,value){
            checkStr += $(this).val() + ",";
        });
        if (checkStr == "") {
            swal({title: "", text: "请选择需要导出的订单", type: 'error'}, function(){});
            return;
        }
        var orderNumber = $("#userOrderExportInput").val();
        if(orderNumber == null || orderNumber == undefined || orderNumber == ""){
            swal({title: "", text: "请填写订单号", type: 'error'}, function(){});
            return;
        }
        $("#orderIdsExport").val(checkStr);
        $("#orderNumberExport").val(orderNumber);
        $("#modalUserOrderExport").modal("hide");
        var form = $("#userSearchForm");
        form.attr("action", _rootPath + "user/order/export");
        form.submit();
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

        $(".btnUserUpdateNumber").on("click",function () {
            var orderId = $(this).attr("user-order-id");
            var orderNumber = $(this).attr("user-order-number");
            $("#numberOrderId").val(orderId);
            $("#numberOrderNumber").val(orderNumber);
            searchNumberDetail(orderId);
        });
    });

}


//详情
function searchNumberDetail(orderId) {
    post(_rootPath + "user/order/search-detail", {"id": orderId}, function (result) {
        $("#addLogisticsNumber").val(result.data.logisticsNumber);
        $("#modalUserNumberModify").modal();
    });
}

//修改物流订单号
function saveLogisticsNumber() {
    post(_rootPath + "user/order/update/logisticsNumber", $("#userOrderNumberUpdateForm").serialize(), function (result) {
        if (result.success == true) {
            swal({title: "", text: "修改成功!", type: 'success'}, function () {
                $("#modalUserNumberModify").modal("hide");
                $("#addLogisticsNumber").val("");
                searchUserList();
            });
        } else {
            swal({title: "", text: result.errorInfo, type: 'error'}, function(){
                //searchUserList();
            });
        }
    });
}