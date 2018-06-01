<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>后台</title>
    <!--[if lt IE 9]>
	<meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <link rel="shortcut icon" href="favicon.ico">
    <link href="${ctx}css/bootstrap.min14ed.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx}css/font-awesome.min93e3.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx}css/animate.min.css" rel="stylesheet">
    <link href="${ctx}css/style.min862f.css?v=4.1.0" rel="stylesheet">
    <link href="${ctx}css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element" id="current-env" env="daily" style="text-align: center;font-size: 18px;font-weight: 600; color: #fff;">管理后台</div>
                    <div class="logo-element">8</div>
                </li>

                <@sec.any name="ROLE_MANAGE,ADMIN_USER_MANAGE">
                    <li>
                        <a href="#">
                            <i class="fa fa-cogs"></i>
                            <span class="nav-label">系统管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                        <#--<@sec.any name="ROLE_ADD">
                            <li><@s.a href="${ctx}adminRole/to-add" class="J_menuItem">角色添加</@s.a></li>
                        </@sec.any>-->
                        <@sec.any name="ROLE_MANAGE">
                            <li><@s.a href="${ctx}adminRole/to-list" class="J_menuItem">角色列表</@s.a></li>
                        </@sec.any>
                        <#--<@sec.any name="ADMIN_USER_ADD">
                            <li><@s.a href="${ctx}adminUser/to-add" class="J_menuItem">账户添加</@s.a></li>
                        </@sec.any>-->
                        <@sec.any name="ADMIN_USER_MANAGE">
                            <li><@s.a href="${ctx}adminUser/to-list" class="J_menuItem">账户列表</@s.a></li>
                        </@sec.any>
                        </ul>
                    </li>
                </@sec.any>
                <@sec.any name="COIN_FLOW_MANAGE,ORDER_MANAGE,HISTORY_ORDER_MANAGE">
                    <li>
                        <a href="#">
                            <i class="fa fa-user"></i>
                            <span class="nav-label">用户管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <@sec.any name="USER_MANAGE">
                                <li><@s.a href="${ctx}user/to-list" class="J_menuItem">用户管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="COIN_FLOW_MANAGE">
                                <li><@s.a href="${ctx}coin/flow/to-list" class="J_menuItem">流水管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="CHARGE_RECORD_MANAGE">
                                <li><@s.a href="${ctx}charge/record/to-list" class="J_menuItem">充值记录</@s.a></li>
                            </@sec.any>
                            <@sec.any name="WITHDRAW_RECORD_MANAGE">
                                <li><@s.a href="${ctx}withdraw/record/to-list" class="J_menuItem">提现记录</@s.a></li>
                            </@sec.any>
                        </ul>
                    </li>
                </@sec.any>
                <@sec.any name="AGENT_REBATE_MANAGE">
                    <li>
                        <a href="#">
                            <i class="fa fa-user"></i>
                            <span class="nav-label">代理管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <@sec.any name="AGENT_REBATE_MANAGE">
                                <li><@s.a href="${ctx}agent/rebate/to-list" class="J_menuItem">代理返佣管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="AGENT_REBATE_AUDIT_MANAGE">
                                <li><@s.a href="${ctx}agent/rebate/audit/to-list" class="J_menuItem">返佣审核管理</@s.a></li>
                            </@sec.any>
                        </ul>
                    </li>
                </@sec.any>
                <@sec.any name="PRODUCT_MANAGE">
                    <li>
                        <a href="#">
                            <i class="fa fa-user"></i>
                            <span class="nav-label">产品管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <@sec.any name="PRODUCT_MANAGE">
                                <li><@s.a href="${ctx}product/to-list" class="J_menuItem">产品管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="PRODUCT_AUDIT_MANAGE">
                                <li><@s.a href="${ctx}product/audit/to-list" class="J_menuItem">产品审核管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="PRODUCT_BRAND_MANAGE">
                                <li><@s.a href="${ctx}product/brand/to-list" class="J_menuItem">品牌管理</@s.a></li>
                            </@sec.any>
                            <@sec.any name="PRODUCT_CATEGORY_MANAGE">
                                <li><@s.a href="${ctx}product/category/to-list" class="J_menuItem">分类管理</@s.a></li>
                            </@sec.any>
                            <#--<@sec.any name="COIN_SETTING_MANAGE">
                                <li><@s.a href="${ctx}coin/setting/to-list" class="J_menuItem">金币比例设置</@s.a></li>
                            </@sec.any>-->
                            <@sec.any name="PRODUCT_FREIGHT_MANAGE">
                                <li><@s.a href="${ctx}product/freight/to-list" class="J_menuItem">商品运费设置</@s.a></li>
                            </@sec.any>
                            <@sec.any name="SEARCH_KEYWORD_MANAGE">
                                <li><@s.a href="${ctx}search/keyword/to-list" class="J_menuItem">关键字设置</@s.a></li>
                            </@sec.any>
                            <@sec.any name="PRODUCT_PRICE_CHANGE_MANAGE">
                                <li><@s.a href="${ctx}product/change/to-list" class="J_menuItem">产品价格变更记录</@s.a></li>
                            </@sec.any>
                        </ul>
                    </li>
                </@sec.any>
                <@sec.any name="USER_ORDER_MANAGE">
                    <li>
                        <a href="#">
                            <i class="fa fa-user"></i>
                            <span class="nav-label">订单管理</span>
                            <span class="fa arrow"></span>
                        </a>
                        <ul class="nav nav-second-level">
                            <li style="display: none;"><@s.a href="${ctx}user/order/to-detail" class="J_menuItem" id="menuUserOrderDetail">User Order Detail</@s.a></li>
                            <@sec.any name="USER_ORDER_MANAGE">
                                <li><@s.a href="${ctx}user/order/to-list" class="J_menuItem">订单管理</@s.a></li>
                            </@sec.any>
                        </ul>
                    </li>
                </@sec.any>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header" style="width: 97%"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>

                    <div class="dropdown profile-element" style="float: right">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#" aria-expanded="false">
                                <span class="clear">
                               <span class="block m-t-xs"><strong class="font-bold">${currentUser.username}</strong></span>
                                <span class="text-muted text-xs block">${currentUser.realName}<b class="caret"></b></span>
                                </span>
                        </a>

                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                           <#-- <li><a class="J_menuItem" href="form_avatar.html" data-index="0">修改头像</a>
                            </li>
                            <li><a class="J_menuItem" href="profile.html" data-index="1">个人资料</a>
                            </li>
                            <li><a class="J_menuItem" href="contacts.html" data-index="2">联系我们</a>
                            </li>
                            <li><a class="J_menuItem" href="mailbox.html" data-index="3">信箱</a>
                            </li>-->
                            <li class="divider"></li>
                            <li><a href="${ctx}logout">安全退出</a>
                               <li><a href="javascript:updatePassword();">修改密码</a>
                            </li>
                        </ul>
                    </div>
                    <div class="dropdown profile-element" style="float: right">
                        <ul class="nav navbar-top-links navbar-right">
                            <li class="dropdown">
                                <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                    <i class="fa fa-bell"></i> <span class="label label-primary" id="productChange"></span>
                                </a>
                                <ul class="dropdown-menu dropdown-alerts">
                                    <li>
                                        <a href="${ctx}product/change/to-list" class="J_menuItem">
                                            <div>
                                                <i class="fa fa-envelope fa-fw"></i> 有商品的价格变更
                                            </div>
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>

            </nav>
        </div>
        <div class="row content-tabs">
            <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
            </button>
            <nav class="page-tabs J_menuTabs">
                <div class="page-tabs-content">
                    <a href="javascript:;" class="active J_menuTab" data-id="index_v1.html">首页</a>
                </div>
            </nav>
            <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
            </button>
            <div class="btn-group roll-nav roll-right">
                <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                </button>
                <ul role="menu" class="dropdown-menu dropdown-menu-right">
                    <li class="J_tabShowActive"><a>定位当前选项卡</a>
                    </li>
                    <li class="divider"></li>
                    <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                    </li>
                    <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                    </li>
                </ul>
            </div>
            <a href="logout" class="roll-nav roll-right J_tabExit"><i class="fa fa fa-sign-out"></i> 退出</a>
        </div>
        <div class="row J_mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}welcome" frameborder="0" data-id="index_v1.html" seamless></iframe>
        </div>
    <#--<div class="footer">
        <div class="pull-right">&copy; 2014-2015
            </div>
        </div>-->
    </div>
    <!--右侧部分结束-->

    <!--mini聊天窗口开始-->
</div>
<div class="modal inmodal" id="passwdUpdateModal" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content animated fadeIn">
            <div class="modal-header" style="height: 20px;">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" style="font-size: 22px;">密码修改</h4>
            </div>
            <div class="modal-body">
                <form id="updatePasswordForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">原密码</label>
                        <div class="col-sm-8">
                            <input type="password" name="oldPassword" id="oldPassword" required="" minlength="2" class="form-control" placeholder="请输入原密码">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">新密码</label>
                        <div class="col-sm-8">
                            <input type="password" name="newPassword" id="newPassword" required="" minlength="2" class="form-control" placeholder="请输入新密码">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">新密码确认</label>
                        <div class="col-sm-8">
                            <input type="password" name="newPasswordConfirm" id="newPasswordConfirm" required="" minlength="2" class="form-control" placeholder="请确认密码">
                        </div>
                    </div>
                    <div class="hr-line-dashed"></div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                <button type="button" id="btnSaveUpdate" class="btn btn-primary">保存</button>
            </div>
        </div>
    </div>
</div>
<script src="${ctx}js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx}js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx}js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${ctx}js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script src="${ctx}js/plugins/layer/layer.min.js"></script>
<script src="${ctx}js/hplus.min.js?v=4.1.0"></script>
<script type="text/javascript" src="${ctx}js/contabs.min.js"></script>
<script src="${ctx}js/plugins/pace/pace.min.js"></script>
<script src="${ctx}js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="${ctx}js/common.js"></script>
<script>
    var _rootPath="${ctx}";
    function updatePassword() {
        $("#passwdUpdateModal").modal();
    }
    $("#btnSaveUpdate").on("click",function () {
        post(_rootPath+"adminUser/updatePasswd",$("#updatePasswordForm").serialize(),function (result) {
            swal({title:"提示", text:"操作成功",type:'success'},function () {
                //window.parent.location.href=_rootPath+"login";
                $("#passwdUpdateModal").modal("hide");
            });
        });
    })

    post(_rootPath+"product/change/count",null,function (result) {
        $("#productChange").html(result.data);
    });
</script>
</body>


<!-- Mirrored from www.zi-han.net/theme/hplus/ by HTTrack Website Copier/3.x [XR&CO'2014], Wed, 20 Jan 2016 14:17:11 GMT -->
</html>
