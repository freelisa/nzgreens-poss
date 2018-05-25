$(document).ready(function () {
    $("#btnCancel").on("click", function () {
        $("#searchForm")[0].reset();
    });
    $("#btnSearch").on("click", function () {
        searchList();
    });
    $("#btnSaveUpdate").on("click", function () {
        saveUpdate();
    });
    searchList();

    $("#add").on("click", function () {
        $("#modalAdd").modal();
    });

    $("#btnAdd").on("click", function () {
        if ($("#addForm").valid()) {
            $(this).button('loading');

            var params = {
                "name": $("#addName").val(),
                "isValid": $("#addStatus").val()
            }

            post(_rootPath + "product/brand/insert", params, function (result) {
                $("#btnAdd").button('reset');
                if (result.success == true) {
                    swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                        $("#modalAdd").modal("hide");
                        searchList();
                    });
                }
            });
        }
    });

    pageFunInit(searchList);
});

//查询列表
function searchList() {
    post(_rootPath + "product/brand/search-list", $("#searchForm").serialize(), function (result) {
        $("#tbody").setTemplateElement("tr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);

        $(".btnDetail").on("click", function () {
            var agentId = $(this).attr("agentId");
            searchDetail(agentId);
        });
    });
}

//详情
function searchDetail(agentId) {
    post(_rootPath + "product/brand/search-detail", {"id": agentId}, function (result) {
        $("#detailDiv").setTemplateElement("detailTr-template", null, {filter_data: false});
        $("#detailDiv").processTemplate(result);

        $("#modalDetail").modal();

        $("#updateStatus").val(result.data.isValid);
    });
}

//保存修改
function saveUpdate() {
    var params = {
        "id": $("#agentId").val(),
        "name": $("#updateName").val(),
        "isValid": $("#updateStatus").val()
    }
    post(_rootPath + "product/brand/update", params, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                $("#modalDetail").modal("hide");
                searchList();
            });
        }
    });
}
