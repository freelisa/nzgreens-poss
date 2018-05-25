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
                "productWeight": $("#addProductWeight").val(),
                "freight": $("#addFreight").val()
            }

            post(_rootPath + "product/freight/insert", params, function (result) {
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
    post(_rootPath + "product/freight/search-list", $("#searchForm").serialize(), function (result) {
        $("#tbody").setTemplateElement("tr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);

        $(".btnDetail").on("click", function () {
            var agentId = $(this).attr("agentId");
            searchDetail(agentId);
        });

        $(".btnDelete").on("click", function () {
            var agentId = $(this).attr("agentId");
            deleteData(agentId);
        });
    });
}

//详情
function searchDetail(agentId) {
    post(_rootPath + "product/freight/search-detail", {"id": agentId}, function (result) {
        $("#detailDiv").setTemplateElement("detailTr-template", null, {filter_data: false});
        $("#detailDiv").processTemplate(result);

        $("#modalDetail").modal();

        $("#updateFreight").val(Fen2Yuan(result.data.freight));
    });
}

//保存修改
function saveUpdate() {
    var params = {
        "id": $("#agentId").val(),
        "productWeight": $("#updateProductWeight").val(),
        "freight": $("#updateFreight").val()
    }
    post(_rootPath + "product/freight/update", params, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                $("#modalDetail").modal("hide");
                searchList();
            });
        }
    });
}


function deleteData(id) {
    layer.confirm('是否删除？', function(index){
        post(_rootPath + "product/freight/delete", {"id":id}, function (result) {
            if (result.success == true) {
                layer.close(index);
                swal({title: "提示", text: "删除成功", type: 'success'}, function () {
                    searchList();
                });
            }
        });
    });
}