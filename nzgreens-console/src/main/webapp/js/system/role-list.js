$(document).ready(function () {
    $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
    $("#btnCancel").on("click", function () {
        $("#roleSearchForm")[0].reset();
    });
    $("#btnSearch").on("click", function () {
        searchRoleList();
    });
    $("#btnSaveUpdate").on("click",function () {
        saveRoleUpdate();
    });

    searchRoleList();
    pageFunInit(searchRoleList);

    $("#addAdminRole").on("click",function () {
        $("#modalRoleAdd").modal();
    });

    $("#btnAddRole").on("click",function () {
        if($("#roleAddForm").valid()){
            if($("input[name='permissionIds']:checked").length > 0){
                $(this).button('loading');
                post(_rootPath+"adminRole/insert",$("#roleAddForm").serialize(),function (result) {
                    $("#btnAddRole").button('reset');
                    if(result.success==true){
                        swal({title:"提示", text:"保存角色成功",type:'success'},function () {
                            $("#modalRoleAdd").modal("hide");
                            searchRoleList();
                        });
                    }
                });
            }else{
                swal({title:"提示", text:"至少选择一项权限。",type:'warning'});
            }
        }
    });
});

//查询角色列表
function searchRoleList() {
    post(_rootPath+"adminRole/search-list",$("#roleSearchForm").serialize(),function (result) {
        $("#roleTbody").setTemplateElement("roleTr-template", null, {filter_data: false});
        $("#roleTbody").processTemplate(result);
        $("#rolePageDiv").setTemplateElement("pager_template");
        $("#rolePageDiv").processTemplate(result);
        $(".btnRoleDetail").on("click",function () {
            var roleId = $(this).attr("roleId");
            searchRoleDetail(roleId);
        });
        $(".btnRoleDelete").on("click",function () {
            var roleId = $(this).attr("roleId");
            deleteRole(roleId);
        });

    });
}

//角色详情
function searchRoleDetail(roleId) {
    post(_rootPath+"adminRole/search-detail",{"roleId":roleId},function (result) {
        $("#roleName").val(result.data.name);
        $("#roleId").val(roleId);
        $("input[name='permissionIds']").each(function (index,item) {
            $(item).parent().removeClass("checked");
            var val = $(item).val();
            $(result.data.permissionIds).each(function (index,permissionId) {
                if(val==permissionId){
                    $(item).prop("checked",true);
                    $(item).parent().addClass("checked");
                }
            });
        });
        $("#modalRoleDetail").modal();
    });
}

//保存角色修改
function saveRoleUpdate(){
    post(_rootPath+"adminRole/update",$("#roleDetailForm").serialize(),function (result) {
        $("#modalRoleDetail").modal("hide");
        window.location.reload();
    });
}

//删除角色
function deleteRole(roleId) {
    post(_rootPath+"adminRole/delete",{"roleId":roleId},function (result) {
        searchRoleList();
    });
}
