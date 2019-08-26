var uploadType = "0";
Dropzone.autoDiscover = false;
var dropz;
$(document).ready(function () {
    var orderId = $("#orderId").val();
    searchUserDetail(orderId);

    $("#btnStatusSave").on("click",function () {
        updateUserTradeStatus();
    });

    $("#btnCertSave").on("click",function () {
        saveCertUpload();
    });

    $("#btnRebateAuditSave").on("click",function () {
        rebateAuditUpdate();
    });


    $("#btnStatusCancel").on("click", function () {
        $("#modalUserStatusModify").modal("hide");
    });

    $("#btnCertCancel").on("click", function () {
        $("#modalUserCertModify").modal("hide");
    });

    $("#btnRebateAuditCancel").on("click", function () {
        $("#modalRebateAuditModify").modal("hide");
    });

    $(document).on("click",".btnUserCertDel",function () {
        delUserCert($(this).attr("user-order-id"));
    });
    
    initDropz();

    $("#addPic").on("click", function () {
        uploadType = "1";
        dropz.removeAllFiles();
        dropz.hiddenFileInput.click();
        $("input[name='certificateUrl']").remove();
    });
});
//一键审核
function confirmAudit() {
    var orderId =  $("#orderId").val();
    layer.confirm('确认一键审核？(审核后返佣自动通过，订单已上传凭证)', function(index){
        layer.close(index);
        post(_rootPath + "user/order/onceAudit", {"orderId":orderId}, function (result) {
            if (result.success == true) {
                swal({title: "", text: "操作成功!", type: 'success'}, function () {
                    location.reload();
                });
            } else {
                swal({title: "", text: result.errorInfo, type: 'error'});
            }
        });
    });
}
//用户详情
function searchUserDetail(orderId) {
    post(_rootPath + "user/order/search-detail", {"id": orderId}, function (result) {

        $("#userDetailDiv").setTemplateElement("userDetail-template", null, {filter_data: false});
        $("#userDetailDiv").processTemplate(result);


        $("#btnUserStatusUpdate").on("click",function(){
            $("#modalUserStatusModify").modal();
        });


        $("#btnUploadCert").on("click",function(){
            $("#modalUserCertModify").modal();
        });

        $("#btnRebateAudit").on("click",function(){
            $("#rebateAuditAmount").html($("#userOrderAmount").html());
            $("#rebateAuditPrice").html($("#rebatePriceDetail").html());
            $("#modalRebateAuditModify").modal();
        });

        $("#oneBtnRebateAudit").on("click", function () {
            confirmAudit();
        });

        $("#status").val(result.data.status);

        //默认显示第一个Tab页
        $("#userTabs a:first").tab("show");
        $("#userTabs a:first").css({"background-color": "#2f4050", "color": "#a7b1c2"});
        //清除之前的click事件
        $(".nav-tabs li").off("click");
        //增加click事件
        $(".nav-tabs li").on("click", function () {
            $("#userTabs a").css({"background-color": "white", "color": "#A7B1C2"});
            var tabIndex = $(this).index();
            if (tabIndex == 0) {
                $('#userTabs a[href="#tab-1"]').css({"background-color": "#2f4050", "color": "#a7b1c2"});
                $("#pageNo").val(1);
                selectUserOrder();
                pageFunInit(selectUserOrder);
            } else if (tabIndex == 1) {
                $('#userTabs a[href="#tab-2"]').css({"background-color": "#2f4050", "color": "#a7b1c2"});
                $("#pageNo").val(1);
                selectUserOrderCert();
                pageFunInit(selectUserOrderCert);
            }
        });
        $(".nav-tabs li:eq(0)").click();
    });
}

function selectUserOrder(){
    post(_rootPath+"user/order/detail/search-list",$("#userOrderSearchForm").serialize(),function (result) {
        $("#userOrderTbody").setTemplateElement("userOrderTr-template", null, {filter_data: false});
        $("#userOrderTbody").processTemplate(result);
        $("#userOrderPageDiv").setTemplateElement("pager_template");
        $("#userOrderPageDiv").processTemplate(result);
    });
}

function selectUserOrderCert(){
    post(_rootPath+"user/order/cert/search-list",$("#userOrderCertSearchForm").serialize(),function (result) {
        $("#userOrderCertTbody").setTemplateElement("userOrderCertTr-template", null, {filter_data: false});
        $("#userOrderCertTbody").processTemplate(result);
        $("#userOrderCertPageDiv").setTemplateElement("pager_template");
        $("#userOrderCertPageDiv").processTemplate(result);
    });
}

//更新用户状态
function updateUserTradeStatus() {
    post(_rootPath + "user/order/update", $("#userOrderStatusUpdateForm").serialize(), function (result) {
        if (result.success == true) {
            swal({title: "", text: "修改成功!", type: 'success'}, function () {
                $("#modalUserStatusModify").modal("hide");
                location.reload();
            });
        } else {
            swal({title: "", text: result.errorInfo, type: 'error'}, function(){
                location.reload();
            });
        }
    });
}

//上传
function saveCertUpload() {
    post(_rootPath + "user/order/insert/cert", $("#userOrderCertUpdateForm").serialize(), function (result) {
        if (result.success == true) {
            swal({title: "", text: "上传成功!", type: 'success'}, function () {
                $("#modalUserCertModify").modal("hide");
                location.reload();
            });
        }else{
            swal({title: "", text: result.errorInfo, type: 'error'}, function(){
                location.reload();
            });
        }
    });
}

//删除凭证
function delUserCert(id) {
    layer.confirm('是否删除？', function(index){
        post(_rootPath + "user/order/delete/cert", {"id":id}, function (result) {
            if (result.success == true) {
                layer.close(index);
                swal({title: "", text: "删除成功!", type: 'success'}, function () {
                    selectUserOrderCert();
                });
            } else {
                swal({title: "", text: result.errorInfo, type: 'error'});
            }
        });
    });
}

//返佣审核
function rebateAuditUpdate() {
    var params = {
        "id": $("#rebateId").val(),
        "status": $("#rebateAuditStatus").val(),
        "amount": $("#actualRebatePrice").val(),
        "remark": $("#rebateRemark").val()
    }
    post(_rootPath + "agent/rebate/audit/update", params, function (result) {
        if (result.success == true) {
            swal({title: "提示", text: "保存成功", type: 'success'}, function () {
                $("#modalRebateAuditModify").modal("hide");
                location.reload();
            });
        }
    });
}

//上传组件初始化
function initDropz() {
    dropz = new Dropzone("#file", {
        url: _rootPath + "user/order/upload",
        addRemoveLinks: true,
        autoProcessQueue: true,
        parallelUploads: 8,
        maxFiles: 10,//最大可上传的文件个数
        maxFilesize: 2,
        acceptedFiles: ".bmp,.jpg,.jpeg,.gif,.png",
        init: function () {
            this.on("removedfile", function (file) {
                console.log("File " + file.name + "removed");
            });
        },
        success: function (file, data) {
            if (uploadType == "1") {
                $("#userOrderCertUpdateForm .modal-body .col-sm-12").append('<input type="hidden" name="certificateUrl" value="' + data.data + '">');
                //$("#certificateUrl").val(data.data);
                /*$("#addPicDiv").html('<img src="' + data.data + '" width="300px">');*/
            }
        },
        dictMaxFilesExceeded: "文件数量过多",
        dictDefaultMessage: "拖动文件到该区域或点击上传文件",
        dictCancelUpload: "取消",
        dictCancelUploadConfirmation: "取消上传操作",
        dictRemoveFile: "删除",
        dictFileTooBig: "可添加的最大文件大小为{{maxFilesize}}Mb，当前文件大小为{{filesize}}Mb ",
    });
}