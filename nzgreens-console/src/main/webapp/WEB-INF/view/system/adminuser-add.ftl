<!DOCTYPE html>
<html>
<!-- Mirrored from www.zi-han.net/theme/hplus/form_basic.html by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:19:15 GMT -->
<head>
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx}css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}css/plugins/iCheck/custom.css" rel="stylesheet">
    <link href="${ctx}css/animate.min.css" rel="stylesheet">
    <link href="${ctx}css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx}css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>

<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-content">
                    <form id="userForm" method="post" class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-4">
                                <input type="text" name="username" required minlength="6" maxlength="255" class="form-control" placeholder="请输入用户名">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">密码</label>
                            <div class="col-sm-4">
                                <input type="password" id="password" required minlength="6" maxlength="255" class="form-control" name="password">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">确认密码</label>
                            <div class="col-sm-4">
                                <input type="password" equalTo="#password" class="form-control" name="confirmpPassword">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">市场代码</label>
                            <div class="col-sm-4">
                                <input type="text" name="market" maxlength="45" class="form-control" placeholder="请输入市场代码">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">请选择角色</label>
                            <div class="col-sm-4">
                                <select name="roleId" required  class="form-control">
                                    <option value="">请选择</option>
                                    <#list adminRoles as adminRole>
                                        <option value="${adminRole.id}">${adminRole.name}</option>
                                    </#list>
                                </select>
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">手机号码</label>
                            <div class="col-sm-4">
                                <input type="text" name="mobile" class="form-control" placeholder="">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">真实姓名</label>
                            <div class="col-sm-4">
                                <input type="text" name="realName" required minlength="2" maxlength="20" class="form-control" placeholder="">
                            </div>
                        </div>
                        <div class="hr-line-dashed"></div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">请选择状态</label>
                            <div class="col-sm-4">
                                <select name="status" class="form-control">
                                    <option value="0">正常</option>
                                    <option value="1">冻结</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-4 col-sm-offset-2">
                                <@sec.any name="ADMIN_USER_ADD">
                                <button class="btn btn-primary" id="btnAddUser" data-loading-text="正在保存..." type="button">添加用户</button>
                                </@sec.any>
                                <button class="btn btn-white" id="btnCancel" type="button">取消</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${ctx}js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx}js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx}js/plugins/validate/messages_zh.min.js"></script>
<script src="${ctx}js/content.min.js?v=1.0.0"></script>
<script src="${ctx}js/plugins/iCheck/icheck.min.js"></script>
<script src="${ctx}js/common.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script>
    $(document).ready(function () {
        $(".i-checks").iCheck({checkboxClass: "icheckbox_square-green", radioClass: "iradio_square-green",});
        $("#btnAddUser").on("click",function () {
            if($("#userForm").valid()){
                $(this).button('loading');
                post("${ctx}adminUser/insert",$("#userForm").serialize(),function (result) {
                    $("#btnAddUser").button('reset');
                    if(result.success==true){
                        swal({title:"提示", text:"保存用户成功",type:'success'},function () {
                            window.location.reload();
                        });
                    }
                });
            }
        });
        $("#btnCancel").on("click",function () {
            $("#userForm")[0].reset();
        });
    });
</script>
</body>
</html>
