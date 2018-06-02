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
    $("#add").on("click", function () {
        $("#modalAdd").modal();
    });

    $("#btnSaveUpdate").on("click", function () {
        saveUpdate();
    });

    $("#btnUpdateBalance").on("click", function () {
        saveUpdateBalance();
    });
    $("#addType").on("change",function (){
        var type = $(this).val();
        if (type == 1){
            $("#agentSelectDiv").show();
        } else {
            $("#agentSelectDiv").hide();
        }
    });
    $("#btnAdd").on("click", function () {
        if ($("#addForm").valid()) {
            $(this).button('loading');
            var params = {
                "telephone": $("#addMobile").val(),
                "type": $("#addType").val(),
                "agentUserId": $("#addAgentUserId").val()
            }

            post(_rootPath + "user/insert", params, function (result) {
                $("#btnAdd").button('reset');
                if (result.success == true) {
                    swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                        $("#modalAdd").modal("hide");
                        searchUserList();
                    });
                }
            });
        }
    });

    searchUserList();
    pageFunInit(searchUserList);
});

//查询用户列表
function searchUserList() {
    $("#btnSearch").button('loading');
    post(_rootPath+"user/search-list",$("#userSearchForm").serialize(),function (result) {
        $("#btnSearch").button('reset');
        $("#userTbody").setTemplateElement("userTr-template", null, {filter_data: false});
        $("#userTbody").processTemplate(result);
        $("#userPageDiv").setTemplateElement("pager_template");
        $("#userPageDiv").processTemplate(result);

        $(".btnUserAgent").on("click",function () {
            var userId = $(this).attr("user-id")
            var agentMobile = $(this).attr("agent-mobile");
            jumpTabPage("menuUserAgentDetail", "id=" + userId + "&agentMobile=" + agentMobile);
        });

        $(".btnUserUpdate").on("click",function () {
            var userId = $(this).attr("userId");
            searchUserDetail(userId);
        });
        $(".btnUserBalance").on("click", function () {
            var userId = $(this).attr("userId");
            searchUserBalanceDetail(userId);
        });
        $(".btnUserReset").on("click", function () {
            var userId = $(this).attr("userId");
            resetPassword(userId);
        });
        $(".btnUserFrozen").on("click", function () {
            var userId = $(this).attr("userId");
            frozenUser(userId);
        });
    });

}

//重置用户密码
function resetPassword(userId) {
    post(_rootPath + "user/reset", {userId:userId}, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "操作成功，重置密码为用户账号", type: 'success'}, function () {
                searchUserList();
            });
        }else{
            swal({title: "提示", text: result.errorInfo, type: 'error'}, function () {

            });
        }
    });
}

//冻结用户
function frozenUser(userId) {
    post(_rootPath + "user/delete", {userId:userId}, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "操作成功", type: 'success'}, function () {
                searchUserList();
            });
        }else{
            swal({title: "提示", text: result.errorInfo, type: 'error'}, function () {

            });
        }
    });
}

//用户详情
function searchUserDetail(userId) {
    post(_rootPath + "user/search-detail", {"userId": userId}, function (result) {
        $("#detailDiv").setTemplateElement("tr-template", null, {filter_data: false});
        $("#detailDiv").processTemplate(result);
        $("#modalDetail").modal();
        $("#detailType").val(result.data.type);
        $("#detailStatus").val(result.data.isValid);
        $("#updateAgentUserId").val(result.data.agentUserId);
    });
}

//保存修改
function saveUpdate() {
    var params = {
        "id": $("#userId").val(),
        "telephone": $("#detailMobile").val(),
        "type": $("#detailType").val(),
        "isValid": $("#detailStatus").val(),
        "agentUserId": $("#updateAgentUserId").val()
    }
    post(_rootPath + "user/update", params, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                $("#modalDetail").modal("hide");
                searchUserList();
            });
        }
    });
}

//余额详情
function searchUserBalanceDetail(userId) {
    post(_rootPath + "user/search-detail", {"userId": userId}, function (result) {
        $("#balanceDetailDiv").setTemplateElement("balanceTr-template", null, {filter_data: false});
        $("#balanceDetailDiv").processTemplate(result);
        $("#modalBalanceDetail").modal();
    });
}

//操作余额
function saveUpdateBalance() {
    var params = {
        "userId": $("#balanceUserId").val(),
        "balance": $("#updateBalance").val()
    }
    post(_rootPath + "user/update/balance", params, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "操作成功", type: 'success'}, function () {
                $("#modalBalanceDetail").modal("hide");
                searchUserList();
            });
        }
    });
}