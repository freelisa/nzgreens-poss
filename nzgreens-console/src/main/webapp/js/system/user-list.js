$(document).ready(function () {
    $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
    $("#btnCancel").on("click", function () {
        $("#userSearchForm")[0].reset();
    });
    $("#btnSearch").on("click", function () {
        searchUserList();
    });
    $("#btnSaveUpdate").on("click", function () {
        saveUserUpdate();
    });
    searchUserList();

    $("#addAdminUser").on("click", function () {
        $("#modalUserAdd").modal();
    });

    $("#btnAddUser").on("click", function () {
        if ($("#userAddForm").valid()) {
            $(this).button('loading');
            post(_rootPath + "adminUser/insert", $("#userAddForm").serialize(), function (result) {
                $("#btnAddUser").button('reset');
                if (result.success == true) {
                    swal({title: "提示", text: "保存用户成功", type: 'success'}, function () {
                        $("#modalUserAdd").modal("hide");
                        searchUserList();
                        // window.location.reload();
                    });
                }
            });
        }
    });

    pageFunInit(searchUserList);
});

//查询用户列表
function searchUserList() {
    post(_rootPath + "adminUser/search-list", $("#userSearchForm").serialize(), function (result) {
        $("#userTbody").setTemplateElement("userTr-template", null, {filter_data: false});
        $("#userTbody").processTemplate(result);
        $("#userPageDiv").setTemplateElement("pager_template");
        $("#userPageDiv").processTemplate(result);

        $(".btnUserDetail").on("click", function () {
            var userId = $(this).attr("userId");
            searchUserDetail(userId);
        });
        $(".btnUserReset").on("click", function () {
            var userId = $(this).attr("userId");
            updateResetPasswd(userId);
        });
        $(".btnUserDelete").on("click", function () {
            var userId = $(this).attr("userId");
            deleteUser(userId);
        });
    });
}

//用户详情
function searchUserDetail(userId) {
    post(_rootPath + "adminUser/search-detail", {"userId": userId}, function (result) {
        console.log(result);
        $("#userDetailDiv").setTemplateElement("userDetail-template", null, {filter_data: false});
        $("#userDetailDiv").processTemplate(result);
        $("#modalUserDetail").modal();
        $("#channelId").val(result.data.channelId);
        $("#roleId").val(result.data.roleId);
    });
}

//保存用户修改
function saveUserUpdate() {
    post(_rootPath + "adminUser/update", $("#userDetailForm").serialize(), function (result) {
        $("#modalUserDetail").modal("hide");
        searchUserList();
    });
}

//删除用户
function deleteUser(userId) {
    post(_rootPath + "adminUser/delete", {"userId": userId}, function (result) {
        swal({title: "提示", text: "操作成功", type: 'success'}, function () {
            searchUserList();
        });
    });
}

//重置密码
function updateResetPasswd(userId) {
    post(_rootPath + "adminUser/updateResetPasswd", {"userId": userId}, function (result) {
        swal({title: "提示", text: "操作成功", type: 'success'}, function () {
            searchUserList();
        });
    });
}
