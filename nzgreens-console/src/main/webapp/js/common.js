var select_styles = '<style type="text/css">.loading{position:absolute;background:url(../images/loading.gif) #ECECEC no-repeat center; width:100%; height:60px;z-index:9999;filter:alpha(opacity=50);-moz-opacity:0.5;}</style>';
jQuery('head').append(select_styles);
//异步方式提交数据
function post(urls, datas, callback) {
    try {
        $.ajax({
            async: true,
            type: 'POST',
            dataType: 'json',
            url: urls,
            data: datas,
            success: function(result){
                if(result.success==false){
                    $(".btn").button("reset");
                    if(result.errorCode == "401") {
                        swal({title:"提示", text:"您没有登录",type:'warning'},function () {
                            window.parent.location.href=_rootPath+"login";
                        });
                    }else if(result.errorCode == "403"){
                        swal({title:"提示", text:"您没有权限访问",type:'error'});
                    }else if(result.errorCode == "500"){
                        swal({title:"提示", text:"系统异常",type:'error'});
                    }else{
                        swal({title:"提示", text:result.errorInfo,type:'warning'});
                    }
                } else if(result.success==true) {
                    callback(result);
                    //if(result.data.pageNum > result.data.pages) $(".page").find("button")[0].click();
                }
            },
            error: function (msg) {
                console.log(msg.responseText);
            }
        });
    } catch (e) {
        $("body").find('.loading').remove();
        console.log(e.message);
        return;
    }
}

function postSync(urls,datas,callback){
    try {
        $.ajax({
            async: false,
            type: 'POST',
            dataType: 'json',
            url: urls,
            data: datas,
            success: function(result){
                if(result.code == "407") {
                    bsAlert(result.msg,null,function(){
                        window.location.href = "/jjslogin/tologin";
                    });
                } else {
                    callback(result);
                }
            },
            error: function (msg) {
                console.log(msg.message);
            }
        });
    } catch (e) {
        console.log(e.message);
        return;
    }
};


//判断非空
function isEmpty(value) {
    if (value == undefined || value == null || value == 'null'
        || value == 'undefined' || value.length == 0) {
        return true;
    }
    return false;
}


function isInteger(letterid){
    var re=/^[0-9]*[1-9][0-9]*$/;
    var kk=letterid;
    if(re.test(kk)){
        return true;}
    else{
        return false;
    }
}

function isLng(value){
    var re = /^[-]?(\d|([1-9]\d)|(1[0-7]\d)|(180))(\.\d*)?$/g;
    if(re.test(value)){
        if(parseFloat(value) > 180 || parseFloat(value) < -180) {
            return false;
        }
        return true;
    }
    return false;
}


function isLat(value){
    var re = /^-?((0|[1-8]\d|)(\.\d{1,7})?|90(\.0{1,7})?)?$/;
    if(re.test(value)){
        return true;
    }
    return false;
}

function isNumber(value){
    var re = /^[0-9]*$/
    if(re.test(value)){
        return true;
    }
    return false;
}

function isChinese(temp) {
    var re = /[^\u4e00-\u9fa5]+/;
    if (re.test(temp)) {
        return false;
    }
    return true;
}

/**
 * 表单验证，只能输入数字和小数点
 *
 * @param obj
 * @return
 */
function clearNoNum(obj,type){
    if(type==0){
        obj.value = obj.value.replace(/[^\d]/g,"");
    }else if(type==9){
        // 先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d-,]/g,"");
        var reg1=/\-/g;
        if(reg1.test(obj.value.substring(0,obj.value.length-1))){
            // 保证只有出现一个-而没有多个-
            obj.value = obj.value.replace(/\-{2,}/g,"-");
            obj.value = obj.value.replace(/\,/g,"");
            // 保证-只出现一次，而不能出现两次以上
            obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
        }
    }else{
        // 先把非数字的都替换掉，除了数字和.
        obj.value = obj.value.replace(/[^\d.]/g,"");
        // 必须保证第一个为数字而不是.
        obj.value = obj.value.replace(/^\./g,"");
        // 保证只有出现一个.而没有多个.
        obj.value = obj.value.replace(/\.{2,}/g,".");
        // 保证.只出现一次，而不能出现两次以上
        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
        // 保证两位小数
        if(!isNaN(obj.value) && obj.value.indexOf('\.')>0 && obj.value.substring(obj.value.indexOf('\.')+1).length>2){
            obj.value = obj.value.substring(0,obj.value.length-1);
        }
    }
}


/**
 * 只能输入字母和数字
 * @return
 */
function clearInputNotLetterOrNum(obj){
    var reg = /[^\a-zA-Z0-9]/g;
    if(reg.test(obj.value)){
        obj.value = obj.value.replace(/[^\a-zA-Z0-9]/g,"");
    }
}

/*
 * 转大写
 */
function toUpperCase(obj){
    obj.value = obj.value.toUpperCase();
}

function bsAlert(msg,title,callback) {
    if(!title) {
        title = '操作提示'
    }
    $("#bsAlert").remove();
    var _alertContent = '<div class="modal fade dele-line" id="bsAlert" style="word-break: break-all;word-wrap: break-word;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" >';
    _alertContent += '<div class="modal-dialog result-dele" role="document"><div class="modal-content"><div class="modal-header no-border">'+title+'</div>';
    _alertContent += '<div class="modal-body"><div class="modal-cont-list">'+msg+'</div><div class="modal-btn text-center">';
    _alertContent += '<button type="button" class="btn btn-primary " id="bsAlertButton" data-dismiss="modal" aria-label="Close">关闭</button>';
    _alertContent += '</div> </div> </div> </div> </div>';
    $("body").append(_alertContent);
    $("#bsAlert").modal("show");
    if(callback){
    	$("#bsAlertButton").on("click",callback);
    }
}

function bsConfirm(msg,title,callback) {
    if(!title){
        title = '操作确认';
    }
    $("#bsConfirm").remove();
    var confirmContent = '<div class="modal" id="bsConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">';
    confirmContent += '<div class="modal-dialog" role="document"><div class="modal-content">';
    confirmContent += '<div class="modal-header no-border">'+title+'</div>';
    confirmContent += '<div class="modal-body"><div class="modal-cont-list">'+msg+'</div>';
    confirmContent += '<div class="modal-btn text-right"><button type="button" data-dismiss="modal" aria-hidden="true"' +
        ' class="btn btn-default" aria-label="Close">取消</button>';
    confirmContent += '<button type="button" id="bsConfirmButton"  class="btn btn-danger">删除</button>';
    confirmContent += '</div></div></div></div></div>';
    $("body").append(confirmContent);
    $("#bsConfirm").modal("show");
    $("#bsConfirmButton").on("click",callback);
}

function bsConfirmClose(){
    $("#bsConfirm").modal("hide");
}

function deleteConfirm(msg,callback,operator) {
    if(!operator) {
        operator = "删除";
    }
    $("#deleteConfirmModel").remove();
    var content = [];
    content.push('<div class="modal fade" id="deleteConfirmModel" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" >');
    content.push('<div class="modal-dialog" role="document"><div class="modal-content">');
    content.push('<div class="modal-header no-border">确认'+operator+'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button></div><div class="modal-body"><div class=" form-group" style="margin-bottom: 0;">')
    content.push('<label class="form-left" style="min-width: 0;margin-right: 0;">'+operator+'对象：</label><div class="form-right form-text">');
    content.push(msg);content.push('</div></div><div class=" form-group"><label class="form-left" style="min-width: 0;margin-right: 0;">'+operator+'理由：</label>')
    content.push('<div class="form-right"><textarea class="form-control" style="width:100%;min-height:75px;" rows="6" ' +
        'name="deleteConfirm_textArea" id="deleteConfirm_textArea" placeholder="请输入4~100个字" operator="'+operator+'"></textarea>');
    content.push(' <p class="error-tips red mt5"></p>');
    content.push('</div></div><div class="modal-btn tc mt15"><button type="button" class="btn btn-primary mr10" id="deleteConfirmButton"  aria-label="Close">确认</button>');
    content.push('<button type="button" class="btn btn-white" data-dismiss="modal" aria-label="Close">取消</button>');
    content.push('</div></div></div></div></div>');
    $("body").append(content.join(''));
    $("#deleteConfirmModel").modal("show");
    $("#deleteConfirmButton").on("click",function(){
        var obj = $("#deleteConfirm_textArea");
        var _reason = obj.val();
        if(isEmpty(_reason)) {
            obj.addClass("error-input");
            obj.closest("div").find("p.error-tips").html('请输入'+obj.attr("operator")+'理由');
            return false;
        }
        if(_reason.length < 4 || _reason.length > 100) {
            obj.addClass("error-input");
            obj.closest("div").find("p.error-tips").html(''+obj.attr("operator")+'理由为4~100个字，请修改');
            return false;
        }
        obj.removeClass("error-input");
        obj.closest("div").find("p.error-tips").html("");
        callback(_reason);
    });
}

function deleteConfirmClose(){
    $("#deleteConfirmModel").modal("hide");
}

//打开ajax请求等待效果层
showLoading = function(obj){
    var w = obj.outerWidth();  //innerWidth
    var h = obj.outerHeight(); //innerHeight
    if(w==0 || h==0){
        var len = obj.children().length;
        if(len>0){
            w = obj.children(":first").innerWidth();
            h = obj.children(":first").innerHeight();
        }
    }
    h = (h>35) ? h : 35;
    var x = obj.position().left + parseInt(obj.css("marginLeft"));
    var y = obj.position().top;

    obj.after('<div class="loading"></div>');
    obj.parent().children('.loading').width(w);
    obj.parent().children('.loading').height(h);
    //alert(x +"\t"+ y);
    try{
        obj.parent().children('.loading').css({top:y,left:x});
    }catch(e){
        obj.parent().children('.loading').css({top:y,left:obj.position().left});
    }
    obj.parent().children('.loading').fadeTo(500,0.5);
};
//关闭ajax请求等待效果层
hideLoading = function(obj){
    obj.parent().children('.loading').remove();
};

function openWindow(a) {
    var b, c, d = '<a href="#" id="serdbclick" style="position:absolute;" target="_blank">&nbsp;</a>;';
    $("body").append(d), b = document.getElementById("serdbclick"), $("#serdbclick")
        .attr("href", a), document.all ? b.click() : document.createEvent
    && (c = document.createEvent("MouseEvents"), c.initEvent("click",
        !1, !0), b.dispatchEvent(c)), $("#serdbclick").remove();
}

function Fen2Yuan( num ) {
    var amount = 0;
    try {
        amount = ( num / 100 ).toFixed( 2 )
    } catch (e) {}
    return amount;
}

function Fen2MilliLi( num ) {
    var amount = 0;
    try {
        amount = ( num / 10000 ).toFixed( 2 )
    } catch (e) {}
    return amount;
}

//新开Tab页面
//前提 1.index.ftl必须有跳转目的页的菜单项，不想显示可以隐藏
//     2.点击触发的组件必须设置keyStr中的各属性名以及属性对应的值，多个参数时使用&分隔
function jumpTabPage(menuId, keyStr) {
    var jump = parent.document.getElementById(menuId);

    //用户明细Tab需要显示用户名

    var keyParams = keyStr.split('&');
    var replaceTabName = false;
    for (var index = 0; index < keyParams.length; index++) {
        var keyParam = keyParams[index];
        if (menuId == 'menuUserOrderDetail') {
            if (keyParam.startsWith('orderNumber')) {
                jump.innerText = keyParam.substring(keyParam.indexOf('=') + 1);
                replaceTabName = true;
                break;
            }
        }
        if (menuId == 'menuUserAgentDetail') {
            if (keyParam.startsWith('agentMobile')) {
                jump.innerText = keyParam.substring(keyParam.indexOf('=') + 1);
                replaceTabName = true;
                break;
            }
        }
    }
    if (!replaceTabName) {
        jump.innerText = 'User Order Detail';
    }


    //跳转
    var jumpUrl = jump.href;
    if (jumpUrl.indexOf('?') > 0) {
        jumpUrl = jumpUrl.substring(0, jumpUrl.indexOf('?'));
    }
    jump.href = jumpUrl + '?' + keyStr;
    jump.click();
}