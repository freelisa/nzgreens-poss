var uploadType = "0";
Dropzone.autoDiscover = false;
var dropz;
var dropz1;

$(document).ready(function () {

    $("#summernote1").summernote({lang:"zh-CN"});

    $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
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
    $("#addPic").on("click", function () {
        uploadType = "1";
        dropz.removeAllFiles();
        dropz.hiddenFileInput.click();
    });
    $("#delPic").on("click", function () {
        $("#contentPicId").val("");
        $("#addPicDiv").html("");
    });

    $("#btnAdd").on("click", function () {
        if ($("#addForm").valid()) {
            $(this).button('loading');
            $("#addDetail").val($('#summernote1').code());

            var params = {
                "title": $("#addTitle").val(),
                "brandId": $("#addBrandId").val(),
                "weight": $("#addWeight").val(),
                "image": $("#contentPicId").val(),
                "costPrice": $("#addCostPrice").val(),
                "sellingPrice": $("#addSellPrice").val(),
                "categoryId": $("#addCategoryId").val(),
                "detail": $("#addDetail").val(),
                "stock": $("#addStock").val(),
                "salesVolume":$("#addSalesVolume").val(),
                "isValid": $("#addStatus").val()
            }
            post(_rootPath + "product/insert", params, function (result) {
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

    //删除
    $(document).on("click",".btnDelete",function(){
        var id = $(this).attr("productId");
        deleteData(id);
    });

    $("#exportExcel").on("click", function () {
        var form = $("#searchForm");
        form.attr("action", _rootPath + "product/export");
        form.submit();
    });

    initDropz();

    initDropz1();

    $("#importExcel").on("click", function () {
        dropz1.removeAllFiles();
        dropz1.hiddenFileInput.click();
    });

    pageFunInit(searchList);
});

function initDropz() {

    dropz = new Dropzone("#file", {
        url:_rootPath + "product/upload",
        addRemoveLinks: true,
        autoProcessQueue:true,
        parallelUploads:8,
        maxFiles: 1,//最大可上传的文件个数
        maxFilesize: 2,
        acceptedFiles: ".bmp,.jpg,.jpeg,.gif,.png",
        init: function() {
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        },
        success:function(file,data){
            if(uploadType == "1"){
                $("#contentPicId").val(data.data);
                $("#addPicDiv").html('<img src="'+data.data+'" width="101px" height="101px">');
            }else if(uploadType == "2"){
                $("#detailContentPicId").val(data.data);
                $("#detailAddPicDiv").html('<img src="'+data.data+'" width="101px" height="101px">');
            }
        },
        dictMaxFilesExceeded:"文件数量过多",
        dictDefaultMessage:"拖动文件到该区域或点击上传文件",
        dictCancelUpload:"取消",
        dictCancelUploadConfirmation:"取消上传操作",
        dictRemoveFile:"删除",
        dictFileTooBig:"可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb ",
    });
    // dropz.on("removedfile",function(file){})
}

function initDropz1() {

    dropz1 = new Dropzone("#file1", {
        url:_rootPath + "product/importExcel",
        addRemoveLinks: true,
        autoProcessQueue:true,
        parallelUploads:8,
        maxFiles: 1,//最大可上传的文件个数
        maxFilesize: 2,
        acceptedFiles: ".xlsx,.xls",
        init: function() {
            this.on("removedfile", function(file) {
                console.log("File " + file.name + "removed");
            });
        },
        success:function(file,data){
            if(data.success){
                swal({title: "提示", text: "批量修改成功", type: 'success'});
            }else{
                swal({title: "提示", text: data.errorInfo, type: 'error'});
            }
        },
        dictMaxFilesExceeded:"文件数量过多",
        dictDefaultMessage:"拖动文件到该区域或点击上传文件",
        dictCancelUpload:"取消",
        dictCancelUploadConfirmation:"取消上传操作",
        dictRemoveFile:"删除",
        dictFileTooBig:"可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb ",
    });
    // dropz.on("removedfile",function(file){})
}

//查询列表
function searchList() {
    post(_rootPath + "product/search-list", $("#searchForm").serialize(), function (result) {
        $("#tbody").setTemplateElement("appConTr-template", null, {filter_data: false});
        $("#tbody").processTemplate(result);
        $("#pageDiv").setTemplateElement("pager_template");
        $("#pageDiv").processTemplate(result);

        $(".btnDetail").on("click", function () {
            var productId = $(this).attr("productId");
            searchDetail(productId);
        });
    });
}

//详情
function searchDetail(productId) {
    post(_rootPath + "product/search-detail", {"id": productId}, function (result) {
        $("#detailDiv").setTemplateElement("tr-template", null, {filter_data: false});
        $("#detailDiv").processTemplate(result);

        $("#summernote2").summernote({lang:"zh-CN"});

        $("#detailTextarea").html('<textarea name="title" class="form-control" rows="3" cols="40" id="detailTitle">' + result.data.title + '</textarea>');
        $("#modalDetail").modal();
        $("#detailBrandId").val(result.data.brandId);
        $("#detailIsValid").val(result.data.isValid);
        $("#detailCategoryId").val(result.data.categoryId);

        if(result.data.image != ""){
            $("#detailAddPicDiv").html('<img src="'+result.data.image+'" width="101px" height="101px">');
        }
        $("#summernote2").code(result.data.detail);

        $("#detailAddPic").on("click", function () {
            uploadType = "2";
            dropz.removeAllFiles();
            dropz.hiddenFileInput.click();
        });

        $("#detailDelPic").on("click", function () {
            $("#detailContentPicId").val("");
            $("#detailAddPicDiv").html("");
        });

    });
}

//保存修改
function saveUpdate() {
    $("#updateDetail").val($('#summernote2').code());

    var params = {
        "id": $("#productId").val(),
        "title": $("#detailTitle").val(),
        "brandId": $("#detailBrandId").val(),
        "weight": $("#detailWeight").val(),
        "image": $("#detailContentPicId").val(),
        "costPrice": $("#detailCostPrice").val(),
        "sellingPrice": $("#detailSellPrice").val(),
        "categoryId": $("#detailCategoryId").val(),
        "detail": $("#updateDetail").val(),
        "stock": $("#detailStock").val(),
        "salesVolume": $("#detailSalesVolume").val(),
        "isValid": $("#detailIsValid").val()
    }
    post(_rootPath + "product/update", params, function (result) {
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
        post(_rootPath + "product/delete", {"id":id}, function (result) {
            if (result.success == true) {
                layer.close(index);
                swal({title: "提示", text: "删除成功", type: 'success'}, function () {
                    searchList();
                });
            }
        });
    });
}