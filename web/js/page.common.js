
(function (topwin) {
    var tfun = function (obj) {
        window.console.log(obj);
    };
    window.message = topwin.Msg ? topwin.Msg.message : tfun;
    window.Msg = {error: topwin.Msg ? topwin.Msg.error : tfun, alert: topwin.Msg ? topwin.Msg.alert : tfun};
    window.error = topwin.Msg ? topwin.Msg.message : tfun;
    window.frameDialog = topwin.frameDialog;
    window.dialog = topwin.dialog;
    window.frameMask = topwin.frameMask; //全局遮罩
    window.frameUnMask = topwin.frameUnMask; //取消全局遮罩
    window.fullShipFrame = topwin.fullShipFrame;
})(window.top);
var editorFormId = "#editorForm";//默认编辑区表单ID
//默认网格控件配置
var defaultGridOpts = {
    datatype: "json",
    mtype: "POST",
    altclass: 'alt-row',
    altRows: true,
    hoverrows: false,
    viewrecords: true,
    width: $("body").innerWidth() - 24,
    autowidth: false,
    shrinkToFit: false,
    rownumbers: true,
    rownumWidth: 30,
    rowNum: 20,
    rowList: [ 10, 20, 30, 50, 100 ],
    multiboxonly: true,
    multiselect: true,
    gridview: true,
    beforeRequest: function () {
        $(this).jqGrid('setGridParam', {
            postData: getRequestParams ? getRequestParams() : {}
        });
    }
};
var projectName="/ZZ_OA";
/**
 *POST提交方法
 * url
 * data 数据
 * mask bool类型  提交的同时是否要有遮罩 默认是true
 * mel jquery查询表达式 可以是任何block布局的元素  默认是“body”
 * 示例  POST("http://localhost:8080/tmis/masterdata/taxFilingUnit.do?command=load",{id:"1"},function(data){}})
 */
window.POST = function (url, data, callback, mask, mel) {
    var isMask = typeof mask === "undefined" ? true : mask, $maskEl = null;
    if (isMask) {
        $maskEl = typeof mel === "undefined" ? $("body") : $(mel);
        $maskEl.mask("与服务器交互数据中,请耐心等待...");
    }
    return $.ajax({"url": url, "data": data, type: "post", dataType: "json", success: function (data, xhr) {
        if (data.validateErrors) {
            var status = data.status;
            if (status == 0) {
                var errors = [], validateErrors = data.validateErrors;
                for (var i in validateErrors) {
                    var cdata = validateErrors[i];
                    errors.push(cdata.message);
                }
                var msg = errors.join("<br/>");
                window.error({"title": "验证错误", text: msg});
            } else {

            }
            return;
        }
        if (data.error) {
            /*window.error({"title":"发生错误",text:data.error});*/
            window.Msg.error(data.error);
            return;
        }
        callback(data, xhr);
        if (data.message) {
            window.Msg.alert(data.message,
                "消息", "info", 2
            );
        }
    }}).fail(function (err, xhr) {
        if (err && ( err.status == 0 || err.status == 200)) {
            return;
        }
        window.error({title: "发生" + err.status + "错误", text: err.statusText});
    }).always(function () {
        if (isMask) {
            $maskEl.unMask();
        }
    });
};

/**
 *GET提交方法
 * url
 * data 数据
 * mask bool类型  提交的同时是否要有遮罩 默认是true
 * mel jquery查询表达式 可以是任何block布局的元素  默认是“body”
 * 示例  GET("http://localhost:8080/tmis/masterdata/taxFilingUnit.do?command=load",{id:"1"},function(data){}})
 */
window.GET = function (url, data, callback, mask, mel) {
    var isMask = typeof mask === "undefined" ? true : mask, $maskEl = null;
    if (isMask) {
        $maskEl = typeof mel === "undefined" ? $("body") : $(mel);
        $maskEl.mask("与服务器交互数据中,请耐心等待...");
    }
    return $.ajax({"url": url, "data": data, type: "get", dataType: "json", success: function (data, xhr) {
        if (data.error) {
            window.error({"title": "发生错误", text: data.error});
            return;
        }
        callback(data, xhr);
        if (data.message) {
            window.message({
                text: data.message,
                title: "消息"
            });
        }
    }}).fail(function (err, xhr) {
        if (err && ( err.status == 0 || err.status == 200)) {
            return;
        }
        window.error({title: "发生" + err.status + "错误", text: err.statusText});
    }).always(function () {
        if (isMask) {
            $maskEl.unMask();
        }
    });
};

/**
 * 重置窗体大小
 */
var resizeFun = function () {
    var $doc = $("body"), allHei = $doc.height(), allWid = $doc.width(), laveHei = 12, laveWid = allWid - 26;
    $(".head-panel,.ui-jqgrid-view .ui-jqgrid-titlebar,.ui-jqgrid-view .ui-jqgrid-hdiv,.ui-jqgrid-pager").each(function (index, item) {
        var $item = $(item);
        if ($item.css("display") !== "none") {
            laveHei += $item.outerHeight(true);
        }
    });
    laveHei = allHei - laveHei;
    var $list = $(typeof listId === "string" ? listId : "");
    if ($list.length > 0) {
        if (allWid === window.oldWidth) {
            $list.jqGrid("setGridHeight", laveHei);
        } else if (laveHei === window.oldHeight) {
            $list.jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
        } else {
            $list.jqGrid("setGridHeight", laveHei).jqGrid("setGridWidth", laveWid, false).jqGrid("autoFillWidth");
        }
    }
    window.oldWidth = allWid;
    window.oldHeight = laveHei;
    if (window._resize) {
        window._resize(allWid, allHei);
    }
};

/**
 * 获取需要附加的请求参数
 */
var getRequestParams = function (vals) {
    var result = $(".search-panel").getFormData();
    if ($(".search-panel").data("show")) {
        result["isFast"] = false;
    } else {
        result["isFast"] = true;
    }
    result["q"] = $.trim($("#fastQueryText").val());
    result["o"] = $.trim($("#queryParam").val());
    return result;
};

/**
 * 从上方显示出panel
 */
var showSlidePanel = function (el) {
    return $(el).show({
        effect: "slide",
        direction: "up",
        easing: 'easeInOutExpo',
        duration: 600
    });
};
/**
 * 往上方收起panel
 */
var hideSlidePanel = function (el) {
    return $(el).hide({
        effect: "slide",
        direction: "up",
        easing: 'easeInOutExpo',
        duration: 400
    });
};


var showShipPanel = function ($el, callback) {
    $el.show(
        {
            effect: "fade",
            duration: 200,
            complete: function () {
                $(this).find(".bottom-panel").show(
                    {
                        effect: "slide",
                        direction: "down",
                        easing: 'easeInOutExpo',
                        duration: 300,
                        complete: function () {
                            if (callback) {
                                callback();
                            }
                        }
                    }
                );
            }
        }
    );
};
var hideShipPanel = function ($el, callback) {
    $el.find(".bottom-panel").hide(
        {
            effect: "slide",
            direction: "down",
            easing: 'easeInOutExpo',
            duration: 200,
            complete: function () {
                $el.hide(
                    {
                        effect: "fade",
                        duration: 400,
                        complete: function () {
                            if (callback) {
                                callback();
                            }
                        }
                    }
                );
            }
        }
    );
};

/**
 * 初始化按钮
 *
 * funs 对象{buttonIdOrName:function(){}} 处理程序，可以进行覆盖原始的方法
 * buttons 数组，jquery查询表达式  可以把不在默认检索区域的按钮加入
 */
var _initButtons = function (funs, buttons) {//
    var overFuns = $.extend({
        insertBTN: function (ev) {
            //console.log(ev.target);
            var $i = $(ev.currentTarget).find("i"),
                $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
            if ($i.length) {
                $piel.addClass($i.attr("class"));
            }
            window._EDITDATA = undefined;
            $(editorFormId).resetHasXTypeForm();
            if (window._insert) {
                window._insert(ev);
            }
            //console.log(this);
        },
        deployData: function (ev) {
            //console.log(ev.target);
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要发布的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能发布单条记录!",
                    title: "提示"
                });
                return;
            }
            $piel = showSlidePanel("#test").find("h4 i").removeClass();
        },
        editBTN: function (ev) {
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要修改的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能修改单条记录!",
                    title: "提示"
                });
                return;
            }
            GET(loadUrl, {id: idAry[0], dc: (new Date()).getTime()}, function (data) {
                var $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
                if ($i.length) {
                    $piel.addClass($i.attr("class"));
                }
                if (data.fieldConfig) {
                    $(editorFormId).configFormField(data.fieldConfig);
                    window._FIELDCONFIG = data.fieldConfig;
                    $(editorFormId).resetHasXTypeForm(data.entity);
                    window._EDITDATA = data.entity;
                } else {
                    $(editorFormId).resetHasXTypeForm(data);
                    window._EDITDATA = data;
                }
                if (window._edit) {
                    window._edit();
                }
            });
        },
        viewBTN: function (ev) {
            var $i = $(ev.currentTarget).find("i"),
                idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要查看的记录!",
                    title: "提示"
                });
                return;
            }
            if (idAry.length > 1) {
                window.message({
                    text: "每次只能查看单条记录!",
                    title: "提示"
                });
                return;
            }
            GET(loadUrl, {id: idAry[0], dc: (new Date()).getTime()}, function (data) {
                var $piel = showSlidePanel(".page-editor-panel").find("h4 i").removeClass();
                if ($i.length) {
                    $piel.addClass($i.attr("class"));
                }
                if (data.fieldConfig) {
                    $(editorFormId).configFormField(data.fieldConfig);
                    window._FIELDCONFIG = data.fieldConfig;
                    $(editorFormId).resetHasXTypeForm(data.entity);
                    window._EDITDATA = data.entity;
                } else {
                    $(editorFormId).resetHasXTypeForm(data);
                    //window._EDITDATA = data;
                    $(editorFormId).find("input,textarea").each(function () {
                        $(this).attr("disabled", "disabled");
                    });
                    $("#editorSave").css("display", "none");
                    $("#resetBTN").css("display", "none");
                }
                if (window._edit) {
                    window._edit();
                }
            });
        },
        editorSave: function () {
            if ($(editorFormId + " [data-validate]").valid()) {
                POST(saveUrl, $(editorFormId).getFormData(), function (data) {
                    $(listId).trigger("reloadGrid");
                    hideSlidePanel(".page-editor-panel");
                });
            }
        },
        resetBTN: function (ev) {
            var $pn = $(ev.target).closest(".page-editor-panel");
            if (window._FIELDCONFIG) {
                $(editorFormId).configFormField(window._FIELDCONFIG);
            }
            $pn.find("form").resetHasXTypeForm(window._EDITDATA);
            if (window._reset) {
                window._reset($pn);
            }
        },
        deleteBTN: function () {
            var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
            if (idAry.length === 0) {
                window.message({
                    text: "请选择要删除的记录!",
                    title: "提示"
                });
                return;
            }

            window.message({
                text: "确认要删除所选择的记录吗?",
                title: "提醒",
                buttons: {
                    "确认": function () {
                        window.top.$(this).dialog("close");
                        POST(deleteUrl, {id: idAry}, function (data) {
                            $(listId).trigger("reloadGrid");
                            if (window._delete) {
                                window._delete();
                            }
                        });
                    },
                    "取消": function () {
                        window.top.$(this).dialog("close");
                    }
                }
            });

        },
        cancelBTN: function () {
            hideSlidePanel(".page-editor-panel,.page-view-panel");
        },
        cancelBTNForSelf: function () {
            hideSlidePanel(".page-editor-panel,.page-view-panel");
            $("#editorSave").show();
            $("#resetBTN").show();
        },
        cancelBTNForDeploy: function () {
            hideSlidePanel("#test");
        },
        searchRip: function (ev) {
            $(".search-panel").show().data("show", true);
            $(ev.target).closest("td").hide().prev().hide();
            resizeFun();
        },
        searchRipClose: function () {
            $(".search-panel").hide().data("show", false);
            $(".toolbar table td:last").show().prev().show();
            resizeFun();
        },
        advancedSearch: function () {
            $(listId).trigger("reloadGrid", [
                {page: 1}
            ]);
        },
        fastSearch: function () {
            $(listId).trigger("reloadGrid", [
                {page: 1}
            ]);
        },
        exportBTN: function () {
            var url = "/TMIS/common/exportExcel.do?command=export&type=" + exportKey;
            var form = $("#exportExcelForm");
            form.attr("action", url);
            form.find("input[name=data]").val(encodeURI(JSON.stringify(getRequestParams())));
            form.get(0).submit();
        }
    }, funs);
    var $btns = $(".toolbar button,.title-bar button,.search-panel button,.bottom-bar button");
    if (buttons) {
        for (var i in buttons) {
            $btns = $btns.add(buttons[i]);
        }
    }
    $btns.button().click(function (ev) {
        var id = $(this).attr("id") || $(this).attr("name");
        if (id && overFuns[id]) {
            overFuns[id](ev);
        }
    });
};

/**
 *初始化表单中的控件
 * ovFuns 对象{inputNameOrId:function(el){}}  可以复写对某个控件的处理
 */
var _initFormControls = function (ovFuns) {
    var $forms = $("body");
    $forms.find("[data-xtype]").each(function (index, item) {
        var $item = $(item), data = $item.data(),
            ename = $item.attr("name"), eid = $item.attr("id");
        if (data.opt && typeof data.opt === "string") {
            data.opt = new Function("return " + data.opt + ";")();
        }
        if (ovFuns && ovFuns[ename]) {
            ovFuns[ename].call($item);
            return;
        }
        switch (data.xtype.toLowerCase()) {
            case "ajaxchosen":
                $item.ajaxChosen();
                break;
            case "chosen":
                $item.chosen();
                break;
            case "buttonset":
                $item.buttonset();
                break;
            case "datetime":
            	$item.datepicker($.extend({
                    dateFormat: "yy-mm-dd",
                    changeMonth: true,
                    changeYear: true
                }, data.opt));
                break;
            case "upload":
                (function ($titem) {
                	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
                	var username = ""
                	$.post(getNameUrl,function(data){
                		var josndata = $.parseJSON(data);
                		if (!$.isEmptyObject(josndata)) {
                            username = josndata.name;
                            $titem.uploadFile($.extend({
                                url: projectName+"/platform/accessory_.do?command=upload&userId="+username,
                                removeUrl: projectName+"/platform/accessory_.do?command=remove",
                                dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
                                returnType: "JSON",
                                showDone: false,
                                showStatusAfterSuccess: false
                            },data.opt));
                		}
                	});
                })($item);
                break;
            case "digits":
                break;
            case "number":
                break;
            case "email":
                break;
            case "url":
                break;
            case "hidden":
                break;
            case "pick":
                $item.pick();
                break;
        }
    });
};
/**
 *   yuanyuan.zhang
 *   begin
 *
 *   设置表单的属性 data-xtype 时候的生成控件
 *
 */
var formuser=function($parent,deferred){
    if(typeof deferred !="undefined"){
        setFormuser($parent,deferred);
        return deferred.promise();
    }else{
        setFormuser($parent);
    }
};

var setFormuser = function ($parent,deferred){
    var formElements = $parent.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]");
    for (var i = 0; i < formElements.length; i++) {
        var item = formElements[i], $item = $(item),
            data = $item.data(), dtype = data.dtype,
            isread = $item.attr("data-isread"), isreadonly = $item.attr("data-isreadonly"),
            xtype = data.xtype, datacode = data.datacode,code = $item.attr("data-code"),
            label = $item.attr("name") || $item.attr("id");
        var date=new Date(),uploadTime="fileListTD"+date.getTime();
        if ( isread && isreadonly) {
            if (typeof dtype === "undefined") {
                dtype = "string";
            }
            switch (xtype) {
                case "mychosen":{
                    $item.chosen();
                    break;
                }
                case "defaultadvice"://默认意见标签
                {
                	 $item.hide();
                     $("<div class='countersign-div' name='defaultadvice-"+label+"-div'></div>").insertAfter($item);
                     var $pardiv = $item.next(),appendhtml,param=$item.attr("param"), selstr, $sel,paramid,paramkey;
                     if(param && param !=null && param !=""){
                         var paramarr=param.split(":");
                         paramkey=paramarr[0];
                         paramid=paramarr[1];
                     }else{
                         paramkey=null;
                         paramid=null;
                     }
                     $pardiv.empty();
                     if(isread === "1" && isreadonly === "2") {//显示
                         appendhtml="<div><input type='hidden' name='defaultadvice'/><input style='width: 80%;border: none;' type='text' name='set-defaultadvice'/><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:60px;height:28px;margin-left:5px;' value='选择'>选择</button><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>"+
                         "<div id='tabs' class='frametab' title='选择意见' style='display: none;' > <select id='sel-advice' style='width:200px;margin-bottom:10px;'><option></option><option value='1'>同意</option><option value='2'>不同意</option><option value='3'>同意，请#同志#阅示</option></select>"+
                         "<div id='tabs-1' class='subTab' style='display:none;'><div class='zTreeDemoBackground left'><ul id='treeDemo' class='ztree'></ul></div> </div></div>";
                         
                     }else if((isread === "1" && isreadonly === "1")){//只读
                         appendhtml="<div><input type='hidden' name='defaultadvice'/><input readonly='readonly' style='background: #ffffff;width: 80%;border: none;' type='text' name='set-defaultadvice'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                     }
                     $pardiv.append(appendhtml);
                     selstr = "button[name=sel-" + label + "]";
                     $sel = $(selstr);
                     setClick($sel,paramid,paramkey);
                     break;
                }
                case "advice" ://意见标签
                {
                    $item.hide();
                    var $befdiv= $item.parent().children("span"),divWid = $befdiv.attr("width");
                    $("<div class='advice' name='sel-" + label + "-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),param=$item.attr("param"), selstr, $sel,paramid,paramkey;
                    if(param && param !=null && param !=""){
                        var paramarr=param.split(":");
                        paramkey=paramarr[0];
                        paramid=paramarr[1];
                    }else{
                        paramkey=null;
                        paramid=null;
                    }
                    $pardiv.empty();
                    //新advice控件
                    if (isread === "1" && isreadonly === "2") {
                        $pardiv.append("<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' name='names-" + label + "' class='savecheckednametextArea'></textarea><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:60px;height:28px;margin-top:-13px;margin-left:5px;' value='选择'>选择</button></div>" +
                            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
                            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>");
                        if(code === "YES"){
                        	var $defaText = $("textarea[name=names-" + label + "]");
                        	getNowUserName($defaText,paramkey);
                        }
                    } else if ((isread === "1" && isreadonly === "1")) {
                        $pardiv.append("<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' style='background:#ffffff !important;line-height: 28px;' class='savecheckednametextArea' name='names-" + label + "'></textarea><button name='sel-" + label + "' type='button' style='display:none;'></button></div>" +
                            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
                            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>");
                    }
                    //新advice触发事件
                      selstr = "button[name=sel-" + label + "]";
                      $sel = $(selstr);
                      addClick($sel,paramkey,paramid);//给按钮添加点击事件
                    break;
                }
                case "defaultsign":{
                	$item.hide();
                	$("<div class='defaultSign-div' name='defaultSign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){
                    	 $pardiv.append("<span name='ids-" + label + "' style='display: inline-block;width: 80%;height: 30px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='defaultSign-showname' ></span><span  name='showdate'  class='defaultSign-showdate'></span>");
                        var $defaultSign = $("span[name=ids-" + label + "]");
                        setNameAndDate($defaultSign);
                    }else if ((isread === "1" && isreadonly === "1")){
                    	$pardiv.append("<span style='display: inline-block;width: 80%;height: 30px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='defaultSign-showname' ></span><span  name='showdate'  class='defaultSign-showdate'></span>");
                    }
                    break;
                }
                case "sign"://签字标签
                {
                    $item.hide();
                    $("<div class='sign-div' name='sign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){
                        appendhtml="<span onclick='setNameAndDate(this)' style='display: inline-block;width: 30px;height: 50px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<img style='width:37px;' src='"+projectName+"/theme/image/pen.png'/><input type='hidden' name='savenowtime'/></span><span name='showname' class='sign-showname' ></span><span  name='showdate'  class='sign-showdate'></span>";
                    }else if ((isread === "1" && isreadonly === "1")){
                        appendhtml="<span style='display: inline-block;width: 30px;height: 50px;margin-left: 30px;margin-top: 14px;cursor: pointer;'>" +
                            "<input type='hidden' name='savenowtime'/></span><span name='showname' class='sign-showname' ></span><span  name='showdate'  class='sign-showdate'></span>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "endorse":{//签署意见
                    $item.hide();
                    $("<div class='endorse-div' name='endorse-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2"){//显示不只读
                        appendhtml="<textarea  class='endorse-textarea' style='width:98%;height: 33px;'></textarea><span style='display:inline-block;position:absolute;right:128px;width: 100px;height: 24px; font-size: 13px;color: black;line-height: 24px;bottom: 1px; '>" +
                            "</span> <input type='hidden' name='savenowtime'/> <span style='display: inline-block; position: absolute; right: 17px;bottom: 1px;width: 108px;font-size: 13px; color: black;height: 24px;line-height: 24px;'></span>";
                    }else if((isread === "1" && isreadonly === "1")){ //只读
                        appendhtml="<textarea  readonly='readonly' class='endorse-textarea' style='width:98%; background: #ffffff;height: 33px;'></textarea><span style='display:inline-block;position:absolute;right:128px;width: 100px;height: 24px; font-size: 13px;color: black;line-height: 24px;bottom: 1px; '>" +
                            "</span><input type='hidden' name='savenowtime'/><span style='display: inline-block; position: absolute; right: 17px;bottom: 1px;width: 108px;font-size: 13px; color: black;height: 24px;line-height: 24px;'></span>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "fileadvice":{//附件意见标签
                    $item.hide();
                    $("<div class='fileadvice-div' name='fileadvice-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next("div"),appendhtml;$pardiv.empty();
                    $("<div class='fileAdvice-message'><span name='date' style='display: inline-block; margin-left: 10px; margin-top: 5px; margin-bottom: 5px; font-size: 14px;color: black; font-style: italic'></span><span style='display: inline-block; font-size: 14px;margin-left: 10px;color: black; font-style: italic' name='dept'></span><span style='display: inline-block; margin-left: 10px; font-size: 14px;color: black; font-style: italic' name='user'></span></div>").appendTo($pardiv);
                    if(isread === "1" && isreadonly === "2") {//显示不只读
                        appendhtml="<div class='fileadvice-div-firstchild'><textarea name='showadivce'></textarea></div>"+
                            "<div style='margin-left: 8px;'><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                        $pardiv.append(appendhtml);
                        var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                        uploadFile(uploaditem);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendhtml="<div class='fileadvice-div-firstchild'><textarea readonly='readonly' name='showadivce' style='background:#FFFFFF'></textarea></div>"+
                            "<div style='margin-left: 8px;'><input  data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                        $pardiv.append(appendhtml);
                        var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                        uploadFile(uploaditem, function (uploaditem) {
                            uploaditem.attr({"data-disabled":true});
                        });
                        uploaditem.data().onlyView(true);
                    }
                    break;
                }
                case "officeupload":{//公文文档上传控件(套红标签)
                    $item.hide();
                    $("<div class='officeupload-div' name='officeupload-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    $appendhtml=$("<input data-xtype='upload'  data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div>");
                    $pardiv.append($appendhtml);
                    var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2"){//显示
                        officeuploadFile(uploaditem,{render:_fileRender},true);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        officeuploadFile(uploaditem,{render:_fileRender},false,function (uploaditem) {
                            uploaditem.data().onlyView(true);
                        })
                    }
                    break;
                }
                case "uploadactivex":{//普通文件上传控件
                    $item.hide();
                    $("<div class='uploadactivex-div' name='uploadactivex-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml,uploaditem;$pardiv.empty();
                    appendhtml="<div><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                    $pardiv.append(appendhtml);
                    uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2") {//显示
                        uploadFile(uploaditem);
                    }else if((isread === "1" && isreadonly === "1")) {//只读
                        uploadFile(uploaditem, function (uploaditem) {
                            uploaditem.data().onlyView(true);
                        });
                    }
                    break;
                }
                case "countersign":{//会签意见
                    $item.hide();
                    $("<div class='countersign-div' name='countersign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    if(isread === "1" && isreadonly === "2") {//显示
                        appendhtml="<div><input type='hidden' name='countersign'/><input style='width: 95.5%;border: none;' type='text' name='set-countersign'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendhtml="<div><input type='hidden' name='countersign'/><input readonly='readonly' style='background: #ffffff;width: 95.5%;border: none;' type='text' name='set-countersign'/><span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'></span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'></span></div>";
                    }
                    $pardiv.append(appendhtml);
                    break;
                }
                case "filenumber":{//文号标签
                    $item.hide();
                    $("<div class='filenumber-div' name='filenumber-"+label+"-div'></div>").insertAfter($item);
                    var $filenumdiv = $item.next(),appendfilehtml,fileparam=$item.attr("data-datacode");
                    $filenumdiv.empty();
                    if(isread === "1" && isreadonly === "2"){//显示
                        appendfilehtml="<input type='text' style='width:92%;' name='filenumber-"+label+"'/>";
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        appendfilehtml="<input disabled='disabled' style='width:92%;background:#FFFFFF' type='text' name='filenumber-"+label+"'/>";
                    }
                    $filenumdiv.append(appendfilehtml);
                    fileNumAutoCompete(fileparam,$filenumdiv.find("input").eq(0));
                    break;
                }
                case "uploadread":{//收文阅件
                    $item.hide();
                    $("<div class='uploadread-div' name='uploadread-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml;$pardiv.empty();
                    $appendhtml=$("<input data-xtype='upload'  data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div class='saveuploadMessDiv' name='"+uploadTime+"'></div>");
                    $pardiv.append($appendhtml);
                    var uploaditem=$pardiv.find("input[data-xtype=upload]").eq(0);
                    if(isread === "1" && isreadonly === "2"){//显示
                        uploadread(uploaditem,true);
                    }else if((isread === "1" && isreadonly === "1")){//只读
                        uploadread(uploaditem,false,function(uploaditem){
                        	uploaditem.data().onlyView(true);
                        });
                    }
                    break;
                }
                case "fileuploadcountersign"://附件意见会签
                {
                    $item.hide();
                    $("<div class='fileuploadcountersign-div' name='fileuploadcountersign-"+label+"-div'></div>").insertAfter($item);
                    var $pardiv = $item.next(),appendhtml,uploaditem,
                        uploadPlus= "<div name='uploadactivex' style='margin-left: 10px;'><input data-xtype='upload' data-appendto='div[name="+uploadTime+"]' type='file' name='"+label+"-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='"+uploadTime+"'></div></div>";
                    $pardiv.empty();
                    appendhtml="<input style='width: 98%;border: none;' type='text' name='countersigncontent'/>"+uploadPlus+"<div class='fileuploadPsersonMess'></div>";//保存人员信息
                    $pardiv.append(appendhtml);
                    uploaditem=$pardiv.find("div[name=uploadactivex] input[type=file][data-xtype=upload]").eq(0);
                    console.log(uploaditem);
                    uploadFile(uploaditem);
                    if((isread === "1" && isreadonly === "1")){//只读
                        uploaditem.data().onlyView(true);
                        $pardiv.find("input[name=countersigncontent]").css({background: "#ffffff"}).attr({readonly:"readonly"});
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
    if(typeof deferred !="undefined"){
        deferred.resolve();
    }
};


/**
 * 生成 收文阅件(uploadread) 的方法
 * @param $titem 在js中动态的添加type=file的控件
 * @param opt 要扩展的方法
 */
var uploadread= function ($titem,flag,callback) {
	var username = "";
	var url = "",idarr=[];
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
    $titem.uploadFile($.extend({
        url: url,
        removeUrl: projectName+"/platform/accessory_.do?command=remove",
        dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
        returnType: "JSON",
        showDone: false,
        onSuccess: function (files, response, xhr, pd) {
            var $parentNode=$titem.parent().parent().find("div[class=saveuploadMessDiv]").eq(0),
                $nextTable=$parentNode.find("table tbody tr");
            if($nextTable && $nextTable.length && typeof $nextTable[0] !="undefined"){
                for(var i= 0,len=$nextTable.length;i<len;i++){
                    var item=$nextTable[i],$itme=$(item);
                    idarr.push($itme.attr("id"));
                }
            }
            function getuploadfileid(response) {
                if(response && !$.isEmptyObject(response)){
                    idarr.push(response[0].id);
                }
            }
            getuploadfileid(response);
            setUploadDataInToTable($parentNode,idarr,flag);
        },
        showStatusAfterSuccess: false
    }));
    if(typeof callback !=='undefined' ){
        callback($titem);
    }
};

/**
 * 在一个父节点中加入上传文件的所有信息（即给uploadread填充数据）
 * @param $parent 父节点
 * @param idArr 上传文件的所有id
 */
var setUploadDataInToTable= function ($parent, idArr,flag) {
    if($parent.length > 0){
        $parent.empty();
        loadfilesByIdsArr(idArr, function (jsondata) {
            if(!$.isEmptyObject(jsondata)){
//            加载表格数据到页面
                $("<iframe style='display: none;' src='"+projectName+"/pages/ModelHtml.html'></iframe>").appendTo($parent);
                var iframenode=$parent.find("iframe")[0];
                var   addHtml =   function (node) {
                    var childDocument= node.contentWindow.document,
                        tablediv=childDocument.getElementById("model_upload"),
                        $tablediv=$(tablediv).find("table").clone(),htmlarr=[];
                    for(var i= 0,len=jsondata.length;i<len;i++){
                        var item=jsondata[i],
                            href= projectName+"/platform/accessory_.do?command=download&id="+item.id,
                            removeurl=projectName+"/platform/accessory_.do?command=remove&id="+item.id;
                        if(flag){//查看（不只读）
                            htmlarr.push("<tr id='"+item.id+"'><td style='height: 40px; border: 1px solid #D7D7D7;text-align: center;'>"+(i+1)+"</td>" +
                                "<td style='height: 40px; border: 1px solid #D7D7D7;'>"+item.fileName+"</td>" +
                                "<td style='height: 40px; border: 1px solid #D7D7D7;'>" +
                                "<button style='font-size: 12px;width: 75px;margin-right:5px;' type='button'  onclick='lookover(\""+item.id+"\")'><i  class='fa fa-search' ></i>查看</button>" +
                                "<button style='font-size: 12px;width: 75px;margin-right:5px;'  type='button' onclick='downloadfile(\""+href+"\")'><i  class='fa fa-cloud-download' ></i>下载</button>" +
                                "<button class='remove' style='font-size: 12px;width: 75px;margin-right:5px;'  type='button' onclick='removeupload_tab(this,\""+removeurl+"\")'><i  class='fa fa-times-circle' ></i>删除</button></td></tr>");
                        }else{//只读
                            htmlarr.push("<tr id='"+item.id+"'><td style='height: 40px; border: 1px solid #D7D7D7;text-align: center;'>"+(i+1)+"</td>" +
                                "<td style='height: 40px; border: 1px solid #D7D7D7;'>"+item.fileName+"</td>" +
                                "<td style='height: 40px; border: 1px solid #D7D7D7;'>" +
                                "<button style='font-size: 12px;width: 75px;margin-right:5px;' type='button'  onclick='lookover(\""+item.id+"\")'><i  class='fa fa-search' ></i>查看</button>" +
                                "<button style='font-size: 12px;width: 75px;margin-right:5px;'  type='button' onclick='downloadfile(\""+href+"\")'><i  class='fa fa-cloud-download' ></i>下载</button></td></tr>");
                        }
                    }
                    $tablediv.find("tbody").append(htmlarr.join(""));
                    $parent.append($tablediv);
                    $parent.find("button").button();
                };

                if(iframenode.addEventListener){
                    iframenode.addEventListener('load',function(){
                        addHtml(iframenode);
                    },false);
                }else  if(iframenode.attachEvent){//ie
                    iframenode.attachEvent("onload", function () {
                        addHtml(iframenode);
                    });
                }else{//非ie
                    iframenode.onload= function () {
                        addHtml(this);
                    };
                }
            }
        });
    }

};

//收文阅件的删除功能
var removeupload_tab=function(node,url){
    if(window.confirm("您确定要删除吗?")){
        $.get(url,null,function(data){
            if(data==='success'){
                $(node).closest("tr").remove();
                alert("删除成功");
            }else{
                alert("删除失败");
            }
        });
    }
};


/**
 * 传入一个公文文档上传控件的jquery对象控件，如何已经套红的话，把套红小按钮显示出来
 * @param $itme 公文文档（正文） 标签 的对象
 * @param flag 为true的时候就是已经套红了，把红色的小图标显示出来，为false的时候红色小图标不显示
 */
var redshoworhide=function($item,flag){
    if($item){
        var $span=$item.next().find("input[data-xtype=upload]").eq(0).next().next().find("span").eq(0),
            $downLoadRedFile=$span.find(".downloadredfile"),
            $editredfile=$span.find(".editredfile"),
            $lookoverredfile=$span.find(".lookoverredfile");


        if(flag){//已套红
            if($downLoadRedFile){
                $downLoadRedFile.removeClass("hide");
            }
            if($lookoverredfile){
                $lookoverredfile.removeClass("hide");
            }
            if($editredfile){
                $editredfile.removeClass("hide");
            }
        }else {//没有套红
            if($downLoadRedFile){
                $downLoadRedFile.addClass("hide");
            }
            if($lookoverredfile){
                $lookoverredfile.addClass("hide");
            }
            if($editredfile){
                $editredfile.addClass("hide");
            }
        }
    }else{
        return false;
    }
};

/**
 * 放入正文标签以后，得到上传文件内容
 * @param $item 公文文档标签
 * @param callback 文件详细信息
 */
var officeuploadItem=function($item,callback){
    var $itemupload= $item.next().find("input[data-xtype=upload]");
    var uploadfiles=$itemupload.data().fileItems();
    if(!$.isEmptyObject(uploadfiles)){
        var fileid=uploadfiles[0].id;
        if(fileid !=null && fileid !=""){
            loadFileById(fileid, function (data) {
                callback(data);
            });
        }
    }
};


/**
 * 动态加载文号标签内容
 * @param code 数据字典的code值
 * @param $parentNode 文号标签节点
 */
var fileNumAutoCompete= function (code, $parentNode) {
    $.getJSON(projectName+"/workFlow/FileManager/CommonForm.do?command=loadSelect",{code:code}, function (jsondata) {
        if(!$.isEmptyObject(jsondata)){
            var availableTags=[];
            for(var o= 0,len=jsondata.length;o<len;o++){
                availableTags.push(jsondata[o].label);
            }
            $parentNode.autocomplete({
                source:availableTags,
                minLength:0,
                autoFocus:false,
                delay:200
            }).focus(function () {
                $(this).autocomplete("search");
                return false;
            });
        }
    });
};




/**
 * 公文标签要增加的html
 * @param el  元素
 * @param item 上传的文件所有的数据
 * @param flag 显示时为true，只读是为false
 * @private
 */
var _fileRender=function(el,item,flag){
    if(flag){//true的时候为显示
        el.append("<a  href='"+item.href+"' style='display:block;' class='file-name' data-fid='" + item.id + "'><i class='fa fa-file-text-o' style='margin-right:3px;font-size:14px;'></i>" + item.fileName +
            "</a><a title='移除' onclick='removeuploadFiles(this,\""+item.id+"\")' class='remove'><i  class='fa fa-times-circle' ></i></a>" +
            "<a title='编辑' class='editfile' onclick='editfile(\""+item.id+"\")' style='color:#2F97CE;margin-left:6px;'><i  class='fa fa-pencil' ></i></a>"+
            "<a title='下载' class='downloadfile' onclick='downloadfile(\""+item.href+"\")' style='color:#2F97CE;margin-left:6px;'><i  class='fa fa-cloud-download' ></i></a>"+
            "<a title='查看' class='lookover' onclick='lookover(\""+item.id+"\")' style='color:#2F97CE;margin-left:6px;'><i  class='fa fa-search' ></i></a>"+
            "<a title='下载红头文件' class='downloadredfile hide' onclick='downloadredfile(\""+item.id+"\")' style='color:#EA5200;margin-left:6px;'><i class='fa fa-cloud-download' ></i></a>" +
            "<a title='编辑红头文件' class='editredfile hide' onclick='editreadfile(\""+item.id+"\")' style='color:#EA5200;margin-left:6px;'><i  class='fa fa-pencil' ></i></a>"+
            "<a title='查看红头文件' class='lookoverredfile hide' onclick='lookoverredfile(\""+item.id+"\")' style='color:#EA5200;margin-left:6px;'><i class='fa fa-search' ></i></a>");
    }else{//false为只读
        el.append("<a  href='"+item.href+"' style='display:block;' class='file-name' data-fid='" + item.id + "'><i class='fa fa-file-text-o' style='margin-right:3px;font-size:14px;'></i>" + item.fileName +
            "</a>" +
            "<a title='下载' class='downloadfile' onclick='downloadfile(\""+item.href+"\")' style='color:#2F97CE;margin-left:6px;'><i  class='fa fa-cloud-download' ></i></a>"+
            "<a title='查看' class='lookover' onclick='lookover(\""+item.id+"\")' style='color:#2F97CE;margin-left:6px;'><i  class='fa fa-search' ></i></a>"+
            "<a title='下载红头文件' class='downloadredfile hide' onclick='downloadredfile(\""+item.id+"\")' style='color:#EA5200;margin-left:6px;'><i class='fa fa-cloud-download' ></i></a>" +
            "<a title='查看红头文件' class='lookoverredfile hide' onclick='lookoverredfile(\""+item.id+"\")' style='color:#EA5200;margin-left:6px;'><i class='fa fa-search' ></i></a>");
    }
};

var selectZW = function (id){
	$("#"+id).removeClass("whiteZW"); 
	$("#"+id).addClass("selectZW"); 
};
var removeZW = function (id){
	$("#"+id).removeClass("selectZW");
	$("#"+id).addClass("whiteZW"); 
};
var inRed = function (fileName,fileId,replaceData){
	var inRedUrl = projectName+"/workFlow/FileManager/RedTemplateList.do?command=inRed";
	$.post(inRedUrl,{templatefileName:fileName,fileId:fileId,replaceData:replaceData},function(data){
		if ($.isNotBlank(data)) {
            var jsondata = $.parseJSON(data);
	        if(!$.isEmptyObject(jsondata)){
	        	editfile(jsondata.fileId);
	        	$("#dialog-inRed").dialog( "close" ) ;
	        	$("#list2").empty();
	        }
		}
	});
};

//remove图标的方法
var removeuploadFiles= function (node,id) {
    $(node).closest("div[class=officeupload-div]").find("div").eq(0).show();
};
//编辑文件
var editfile= function (id) {
    loadFileById(id, function (data) {
        if(!$.isEmptyObject(data)){
            var filerealpath=data.filePathInServer,operator=data.username,
                url=projectName+"/pages/workFlow/FileManager/officeupload/editfile.jsp?path="+filerealpath+"&operator="+operator+"&id="+data.id;
            lookflowChart(url,"编辑文件");
        }
    });
};
var downloadfile= function (href) {
    window.location.href=href;
};
//查看文件
var lookover= function (id) {
	
    loadFileById(id, function (data) {
        if(!$.isEmptyObject(data)){
            var filerealpath=data.filePathInServer,fileName=data.fileName,
                url=projectName+"/pages/workFlow/FileManager/officeupload/lookover.jsp?path="+filerealpath+"&fileName="+fileName;
            if(data.fileNameExtension==='doc' || data.fileNameExtension==='docx'){
                lookflowChart(url,"查看文件");
            }else{
                alert("您只能打开word文件");
            }
        }
    });
    
    
};
//对红头文件的处理
var downloadredfile= function (id) {//下载红头文件
    loadRedFile(id, function (data) {
        if(!$.isEmptyObject(data)){
            window.location.href=projectName+"/platform/accessory_.do?command=download&id="+data.id;
        }
    });
};
var editreadfile=function(id){
/*	  var editUrl=projectName+"/workFlow/FileManager/RedTemplateList.do?command=init&fileId="+id;
		frameDialog(editUrl, "选择套红模板", {mode:"middle",width:200,height:400,buttons:[
		                                                                          
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		              {
		                  var $this = window.top.$( this ) ;
		                  $this.dialog( "close" ) ;
		     		  }
		        }
		 ]});
		*/
	    loadRedFile(id, function (data) {
	        if(!$.isEmptyObject(data)){
	            var filerealpath=data.filePathInServer,operator=data.username,
	            url=projectName+"/pages/workFlow/FileManager/officeupload/editfile.jsp?path="+filerealpath+"&operator="+operator+"&id="+data.id;
	            lookflowChart(url,"编辑红头文件文件");
	        }
	    });
};
var lookoverredfile= function (id) {
    loadRedFile(id, function (data) {
        if(!$.isEmptyObject(data)){
            var filerealpath=data.filePathInServer,fileName=data.fileName,
                url=projectName+"/pages/workFlow/FileManager/officeupload/lookover.jsp?path="+filerealpath+"&fileName="+fileName;
            lookflowChart(url,"查看红头文件文件");
        }
    });
};

/**
 * 由上传文件的id得到制文件后的套红文件的详细信息
 * @param id  上传文件的id
 * @param callback  把红头文件的详细信息放到回调函数中去。
 */
var loadRedFile=function(id,callback){
	
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	var username = "";
	$.post(getNameUrl,function(data){
		var josndata = $.parseJSON(data);
		if (!$.isEmptyObject(josndata)) {
            username = josndata.name;
            $.getJSON(projectName+"/workFlow/FileManager/Template.do?command=loadRedFile",{id:id},function(data){
            	if(!$.isEmptyObject(josndata)){
            		data.username = username;
            		callback(data);
            	}
            });
		}
	});
};

//由文件的id得到文件的详细信息
var loadFileById=function(id,callback){
	
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	var username = "";
	$.post(getNameUrl,function(data){
		var josndata = $.parseJSON(data);
		if (!$.isEmptyObject(josndata)) {
            username = josndata.name;
            $.getJSON(projectName+"/platform/accessory_.do?command=loadFileById",{id:id}, function (josndata) {
                if(!$.isEmptyObject(josndata)){
                	josndata.username=username;
                    callback(josndata);
                }
            });
		}
	});
};

/**
 * 由文件的数组id加载所有文件信息
 * @param idsArr 数组id
 * @param callback 返回相信的文件信息数据
 */
var loadfilesByIdsArr=function(idsArr,callback){
    POST(projectName+"/platform/accessory_.do?command=loadFileByIdsArr",{id:idsArr},function(data){
        if(!$.isEmptyObject(data)){
            callback(data);
        }
    });

};

var lookflowChart = function (url,title) {
    frameDialog(url, title,
        {
            mode: "full",
            buttons: [
                { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                    var $this = window.top.$(this);
                    $this.dialog("close");
                }}
            ]
        });
};

/**
 * 这个方法是设置表单上所有元素为只读
 * @param $parent 表单元素对象
 */
var setFormIsOnlyRead= function ($parent) {
    var formElements = $parent.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]");
    for (var i = 0; i < formElements.length; i++){
        var item = formElements[i], $item = $(item),
            data = $item.data(),xtype = data.xtype;
        switch (xtype){
            case "advice" ://意见标签
            {
                var $pardiv=$item.next();
                $pardiv.find("button").hide();
                $pardiv.find("div").eq(0).find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                break;
            }
            case "defaultadvice":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=set-defaultadvice]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                }
                break;
            }
            case "sign"://签字标签
            {
                var  $pardiv=$item.next();
                var $span=$pardiv.find("span").eq(0);
                $span.removeAttr("onclick");
                $span.hide();
                break;
            }
            case "endorse"://签署意见
            {
                var $pardiv=$item.next();
                $pardiv.find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                break;
            }
            case "fileadvice"://附件意见标签
            {
                var $pardiv=$item.next();
                if($pardiv.length >0){
                    $pardiv.find("textarea").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                    if( $pardiv.find("input[data-xtype]").data()){
                        $pardiv.find("input[data-xtype]").data().onlyView(true);
                    }
                }
                break;
            }
            case  "uploadactivex":{//附件上传的
                var $pardiv=$item.next();
                if($pardiv.length > 0){
                    if( $pardiv.find("input[data-xtype]").data()){
                        $pardiv.find("input[data-xtype]").data().onlyView(true);
                    }
                }
                break;
            }
            case "uploadread":{//内部签报拟稿
                var $pardiv=$item.next(),$button=$pardiv.find("div[name=fileListTD] table tbody tr");
                $pardiv.find("input[data-xtype=upload]").eq(0).prev().hide();
                break;
            }
            case "datetime":{
                $item.attr({"disabled":"disabled"}).css({background:"#FFFFFF"});
                break;
            }
            case "filenumber":{
                $item.next().find("input[type=text]").attr({disabled:"disabled"}).css({background:"#FFFFFF"});
                break;
            }
            case "mychosen":{
                $item.attr({disabled:"disabled"});
                break;
            }
            case "defaultsign":{
                $item.attr({disabled:"disabled"});
                break;
            }
            case "officeupload":{
                $item.next().find("div[name=fileListTD]").find("#MainBody-upload_list").addClass("officeuploadreadonly");
                break;
            }
            case "fileuploadcountersign":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=countersigncontent]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                    $parentNode.find("div[name=uploadactivex] input[data-xtype=upload]").data().onlyView(true);
                }
                break;
            }
            case "countersign":{
                var $parentNode= $item.next();
                if($parentNode.length > 0){
                    $parentNode.find("input[name=set-countersign]").attr({readonly:"readonly"}).css({background:"#FFFFFF"});
                }
                break;
            }
            default :
            {
                $item.attr({"readonly": "readonly"}).css({background:"#FFFFFF"});
                break;
            }
        }
    }
};

/**
 *
 * @param $titem
 * @param opt
 */
var officeuploadFile=function($titem,opt,flag,callback){
	var username = "",url ="";
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
	    $titem.uploadFile($.extend({
	        url: url,
	        removeUrl: projectName+"/platform/accessory_.do?command=remove",
	        dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
	        returnType: "JSON",
	        showDone: false,
	        allowedTypes:"doc,docx",
	        extErrorStr:"您只能上传doc和docx格式的文件",
	        maxFileCountErrorStr:"您只能上传一份文件",
	        maxFileCount:1,
	        flag:flag,
	        onSuccess: function (files, response, xhr, pd) {
	            $titem.prev().hide();
	        },
	        showStatusAfterSuccess: false
	    },opt));
        if(typeof callback !=='undefined' ){
            callback($titem);
        }
    
};
/**
 * 封装upload方法
 * @param $titem <input type='file'>的控件
 * @param callback 当本方法执行完要执行的函数
 */
var uploadFile= function ($titem,callback) {
	var username = "";
	var url = "";
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.ajax({  
         type : "post",  
         url : getNameUrl,  
         data : "",  
         async : false,  
         success : function(data){  
        	var josndata = $.parseJSON(data);
     		if (!$.isEmptyObject(josndata)) {
     			username = josndata.name;
     			url = projectName+"/platform/accessory_.do?command=upload&userId="+username;
     		} 
         }  
    });
		 $titem.uploadFile({
             url: url,
             removeUrl: projectName+"/platform/accessory_.do?command=remove",
             dowloadUrl: projectName+"/platform/accessory_.do?command=download&id={id}",
             returnType: "JSON",
             showDone: false,
             showStatusAfterSuccess: false
         });
         if(typeof callback !=='undefined' ){
             callback($titem);
         }
};
var getNowUserName = function($parentNode,paramkey){
	var getNameUrl=projectName+"/workFlow/FileManager/CommonForm.do?command=getUserNameAndDept";
	$.post(getNameUrl,function(data){
		var josndata = $.parseJSON(data);
		if (!$.isEmptyObject(josndata)) {
            var username = josndata.name,
            	 userId = josndata.personId,
            	 deptId = josndata.deptId,
                    dept = josndata.dept;
            if(paramkey==="user"){
            	$parentNode.html(username);
            	$parentNode.parent().find("input[type=hidden]").val(userId);
            	$parentNode.next().hide();
        	}else{
        		$parentNode.html(dept);
        		$parentNode.parent().find("input[type=hidden]").val(deptId);
        		$parentNode.next().hide();
        	}
        }
	});
};
/**
 * 给data-xtype='sign' 加点击事件
 * @param node span节点
 */
var setNameAndDate= function (node) {
    var getNameUrl=projectName+"/workFlow/FileManager/FormPowerSetting.do?command=loadUserMessage";
    $.getJSON(getNameUrl,null, function (data) {
        var $node=$(node),showname=$node.next(),savenowtime=$node.find("input[name=savenowtime]"),
            showdate=$node.next().next();
        var nowdate=new Date();
        showdate.text(dateFormat(nowdate,'yyyy-MM-dd'));
        showname.text(data.chineseName);
        savenowtime.val(dateFormat(nowdate,'yyyy-MM-dd hh:mm:ss'));
        $node.hide();
    });
};
var namesD = [];
var addOption = function ($parentNode, datacode,paramkey,paramid) {
    var addoptionurl = projectName+"/workFlow/FileManager/CommonForm.do?command=loadSelect";
    $.getJSON(addoptionurl, {code: datacode}, function (data) {
        addNode2ParentNode($parentNode, data,paramkey,paramid);
    });
};
var addClick = function($parentNode,paramkey,paramid){
	$parentNode.attr({tempflag: paramkey});
	$parentNode.click(function(ev){
		var tempFlag = $(this).attr("tempflag");//弹出框要显示的类型，是人员还是部门
        if (tempFlag != null && tempFlag != "") {
            var $selparentDiv = $(this).closest("div"),
                $treediv = $selparentDiv.find("div[name=showtree]"),//得到ztree需要的div
                $saveIds = $selparentDiv.find("input[type=hidden]"),//得到点中的保存在页面上的所有id
                $saveNames = $selparentDiv.find("textarea");//得到点中的保存在页面上的所有name值
            if(($saveIds != null && $saveIds != "")&&($saveNames != null && $saveNames != "")){
            	clickopenDialog(tempFlag,paramid, $treediv, $saveIds, $saveNames);
            }else{
            	$saveIds.val("");
                $saveNames.val("");
            }
                
        }
	});
	
};
var initTab1 = function($tabs,paramid,paramkey){
	var setting = {
		    isSimpleData : true, 
			treeNodeKey : "id", 
			treeNodeParentKey : "pId", 
			showLine : true, 
			checkable : true,
			check: {
				enable: true
			},
			callback: {
				enable: true,
				onCheck: zTreeOnCheck
			}
		};
	$.ajax({
        url:  projectName+"/common/choosePerson.do?command=selectAllDatas&moduleType=organization&orgId="+paramid+"&orgName=",
        type: "POST",
        data: {
            
        },
        dataType: "JSON",
        success: function(data, xhr) {
            if (data.error) {
                 window.Msg.error(data.error,"加载发生错误！");
                return;
            }
            var treeDemo = $tabs.find("#treeDemo");
            tree1 = $.fn.zTree.init($(treeDemo), setting,eval(data));
        }
    });
};
var setClick = function($sel,paramid,paramkey){
	var $item = $sel.parent();
	$("#tabs").dialog({
	    autoOpen: false,
	    width: 230,
	    modal: true,
	    buttons: {
	      "确定": function() {
	      	$("#tabs").dialog( "close" );
	      	var selectText = $("#tabs").find("option:selected").text(),
	      		selectVal = $("#tabs").find("select[id=sel-advice]").val(),
	      		selName = namesD;
	      	if(selectVal == 3){
	      		var strarr = selectText.split("#"),
	      			begintext = strarr[0],
	      			enttext = strarr[2];
	      			$item.find("input[name=set-defaultadvice]").val(begintext + selName.join("、") + "同志" + enttext);
	      		
	      	}else{
	      		$item.find("input[name=set-defaultadvice]").val(selectText);
	      	}
	      	
	      },
	      "取消": function() {
	      	$("#tabs").dialog( "close" );
	      }
	    }
	  });
	$sel.click(function(ev){
    	 var This = this;
    	 $("#tabs").dialog("open");
    	 var $tabs = $("#tabs"),
    	 	 $selectAdv = $tabs.find("select[id=sel-advice]");
    	 changeSelect($selectAdv,$tabs,$(This).parent(),paramid,paramkey);
	 });
};
var changeSelect = function($parentNode,$tabs,$pardiv,paramid,paramkey){
	$parentNode.change(function(ev){
		var selectVal = $(this).val();
		if(selectVal == 3 ){
			$tabs.find("div[id=tabs-1]").show();
			initTab1($tabs,paramid,paramkey);
		}else{
			$tabs.find("div[id=tabs-1]").hide();
		}
	});
	 
};
var zTreeOnCheck = function(event,treeId,treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
    namesD=[];
    for(var i=0;i<nodes.length;i++){
    	var isParent = nodes[i].isParent;
    	if(isParent == false) {
            var itemName = nodes[i].name.replace(/\s/g, "");
            if (itemName.length == 3) {
            	namesD.push(itemName.substring(1, 3));
            } else if (itemName.length == 4) {
            	namesD.push(itemName.substring(2, 4));
            } else {
            	namesD.push(itemName);
            }
    	}
    }
};
var addNode2ParentNode = function ($parentNode, jsondata,paramkey,paramid) {
    if (!$.isEmptyObject(jsondata)) {
        $parentNode.empty();
        var htmlArr = [];
        htmlArr.push("<option value=''>请选择</option>");
        for (var i = 0; i < jsondata.length; i++) {
            var val = jsondata[i], html = "<option value=" + val.id + " id=" + val.id + ">" + val.label + "</option>";
            htmlArr.push(html);
        }
        $parentNode.append(htmlArr.join(""));
        $parentNode.attr({tempflag: paramkey});
        $parentNode.change(function (ev) {
            var tempFlag = $(this).attr("tempflag");//弹出框要显示的类型，是人员还是部门
            if (tempFlag != null && tempFlag != "") {
                var $selparentDiv = $(this).closest("div"),
                    selectval = $(this).val(),//下拉列表选中的值
                    selectTempText = $(this).find("option:selected").text(),//得选中的下拉列表的文本值
                    $treediv = $selparentDiv.find("div[name=showtree]"),//得到ztree需要的div
                    $saveIds = $selparentDiv.find("input[type=hidden]"),//得到点中的保存在页面上的所有id
                    $saveNames = $selparentDiv.find("textarea");//得到点中的保存在页面上的所有name值
                if (selectval != null && selectval != "") {
                    changeopenDialog(tempFlag,paramid, $treediv, $saveIds, $saveNames, selectTempText);
                } else {
                    $saveIds.val("");
                    $saveNames.val("");
//                    changeopenDialog(tempFlag,$treediv,$saveIds,$saveNames,selectTempText);
                }
            }
        });
    }
};
//给按钮添加click事件
var clickopenDialog = function(tempFlag,paramid, $treediv, $saveIds, $saveNames){
	 var getSelectTreeurl = projectName+"/common/choosePerson.do?command=selectAllDatas", sendurl;
	    if (tempFlag === "user") {//显示人和部门
	        sendurl = getSelectTreeurl + "&moduleType=organization&orgId="+paramid+"&orgName=";
	    } else {//org
	        sendurl = getSelectTreeurl + "&moduleType=meetingApplyReturnList&orgId="+paramid+"&orgName=";
	    }

	    $.post(sendurl,{}, function (data) {
	        if (data != null && data != "") {
	            var json = $.parseJSON(data);
	            var setting = {
	                isSimpleData: true, //数据是否采用简单 Array 格式，默认false
	                treeNodeKey: "id", //在isSimpleData格式下，当前节点id属性
	                treeNodeParentKey: "pId", //在isSimpleData格式下，当前节点的父节点id属性
	                showLine: true, //是否显示节点间的连线
	                checkable: true, //每个节点上是否显示 CheckBox
	                check: {
	                    enable: true
	                }
	            };
	            $.fn.zTree.init($treediv.find("ul[name=ztreeul]"), setting, json);
	            clicksettreedialog(tempFlag,paramid, $saveIds, $saveNames);
	        }
	    });
};

//给表单的select下拉列表加change事件
var changeopenDialog = function (tempFlag,paramid, $treediv, $saveIds, $saveNames, selectTempText) {
    var getSelectTreeurl = projectName+"/common/choosePerson.do?command=selectAllDatas", sendurl;
    if (tempFlag === "user") {//显示人和部门
        sendurl = getSelectTreeurl + "&moduleType=organization&orgId="+paramid+"&orgName=''";
    } else {//org
        sendurl = getSelectTreeurl + "&moduleType=meetingApplyReturnList&orgId="+paramid+"&orgName=";
    }

    $.post(sendurl,{}, function (data) {
        if (data != null && data != "") {
            var json = $.parseJSON(data);
            var setting = {
                isSimpleData: true, //数据是否采用简单 Array 格式，默认false
                treeNodeKey: "id", //在isSimpleData格式下，当前节点id属性
                treeNodeParentKey: "pId", //在isSimpleData格式下，当前节点的父节点id属性
                showLine: true, //是否显示节点间的连线
                checkable: true, //每个节点上是否显示 CheckBox
                check: {
                    enable: true
                }
            };
            $.fn.zTree.init($treediv.find("ul[name=ztreeul]"), setting, json);
            settreedialog(tempFlag,paramid, $saveIds, $saveNames, selectTempText);
        }
    });
};

/*
 meetingApplyReturnList  代表 部门
 organization  人

 */

var clicksettreedialog = function (tempflag,paramid, $saveIds, $saveNames){
	var ids = $saveIds.val(), url;
    if (tempflag === "user") { //user
    	if (ids != null && ids != "") {
    		url="common/choosePerson.do?command=init&orgId="+paramid+"&idsParam="+ids+"&moduleType=organization&orgName=''";
        } else {
            url=projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&moduleType=organization&orgName=''";
        }
    } else {//org
        if (ids != null && ids != "") {
            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=meetingApplyReturnList&orgName=dfg";
        } else {
            url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=meetingApplyReturnList&orgName=aaa";
        }
    }
    frameDialog(url, "请选择", {mode: "middle", width: 227, height: 340,
        buttons: [
            { text: "确定", icons: { primary: "ui-icon-check" }, click: function (ev) {
                var $this = window.top.$(this),
                    dial = $this.find("iframe")[0].contentWindow;
                var selectData=dial.getData();
                if(selectData){
                	var nameidarr = selectData.split(";"), names = nameidarr[0], ids = nameidarr[1],
                    namesarr = names.split(","), idsarr = ids.split(","), alldata = [];
                	for (var i = 0; i < namesarr.length; i++) {
                		alldata.push({
                			id: idsarr[i],
                			name: namesarr[i]
                		});
                	}

                	if (alldata.length > 0) {
                		var idArr = [], nameArr = [];
                		for (var i = 0; i < alldata.length; i++) {//给advice控件编辑框中加数据
                			var item = alldata[i],
                				itemName = item.name.replace(/\s/g, "");
                			idArr.push(item.id);
                			nameArr.push(itemName);
                			
                		}
                		$saveIds.val(idArr.join(","));
                		$saveNames.val(nameArr.join("、"));
                		
                	} else {
                		window.Msg.alert("请选择要通知的人!");
                		return;
                	}
                }
                $this.dialog("close");
            }},
            { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                var $this = window.top.$(this);
                $this.dialog("close");
            }}
        ]});
};
var settreedialog = function (tempflag,paramid, $saveIds, $saveNames, selectTempText) {
    var ids = $saveIds.val(), url, begintext, enttext;
    if (tempflag === "user") {
        //  sendurl= projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&orgName=''&moduleType=organization";
        if (ids != null && ids != "") {

//            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=organization&orgName=''";
            url="common/choosePerson.do?command=init&orgId="+paramid+"&idsParam="+ids+"&moduleType=organization&orgName=''";

        } else {

            //url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=organization&orgName=''";
            url=projectName+"/common/choosePerson.do?command=init&orgId="+paramid+"&moduleType=organization&orgName=''";

        }
    } else {//org
        if (ids != null && ids != "") {
            url = projectName+"/common/choosePerson.do?command=init&idsParam=" + ids + "&moduleType=meetingApplyReturnList&orgName=dfg";
        } else {
            url = projectName+"/common/choosePerson.do?command=init" + "&moduleType=meetingApplyReturnList&orgName=aaa";
        }
    }
    if (selectTempText != null && selectTempText !== "" && selectTempText != "请选择") {

        var strarr = selectTempText.split("#");
        begintext = strarr[0];
        enttext = strarr[2];
    }
    frameDialog(url, "请选择", {mode: "middle", width: 227, height: 340,
        buttons: [
            { text: "确定", icons: { primary: "ui-icon-check" }, click: function (ev) {
                var $this = window.top.$(this),
                    dial = $this.find("iframe")[0].contentWindow;
                var selectData=dial.getData();
                if(selectData){
                	var nameidarr = selectData.split(";"), names = nameidarr[0], ids = nameidarr[1],
                    namesarr = names.split(","), idsarr = ids.split(","), alldata = [];
                	for (var i = 0; i < namesarr.length; i++) {
                		alldata.push({
                			id: idsarr[i],
                			name: namesarr[i]
                		});
                	}

                	if (alldata.length > 0) {
                		var idArr = [], nameArr = [];
                		for (var i = 0; i < alldata.length; i++) {//给advice控件编辑框中加数据
                			var item = alldata[i],
                				itemName = item.name.replace(/\s/g, "");
                			idArr.push(item.id);
                			nameArr.push(itemName);
                			
                		}
                		$saveIds.val(idArr.join(","));
                		if ($.trim(paramid) == '00EB41BF-EB1D-46F9-837E-8F73714407BE') {
                			$saveNames.val(begintext + nameArr.join("、") + "同志" + enttext);
                		} else {
                			$saveNames.val(begintext + nameArr.join("、") + enttext);
                		}
                	} else {
                		window.Msg.alert("请选择要通知的人!");
                		return;
                	}
                }
                $this.dialog("close");
            }},
            { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                var $this = window.top.$(this);
                $this.dialog("close");
            }}
        ]});
};


/**
 * 提供一个方法 若规定 data-xtype='advice' 的时候 要把整个表单的意见标签所有数据封装成一个数组
 *  若规定了 data-xtype='advice' 又给了一个 data-datacode='YJMB_BM' 的时候把满足条件的表单的意见标签所有数据封装成一个数组
 * */

var getAdviceData = function ($form, givedatacode) {
    var formElements = $form.find("input[data-xtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-xtype],input[data-xtype][type=radio]:checked,input[data-xtype][type=checkbox]"),
        objArr = [];
    for (var i = 0; i < formElements.length; i++) {
        var item = formElements[i], $item = $(item), $itemdiv = $item.next("div"),
            data = $item.data(), dtype = data.dtype, xtype = data.xtype, datacode = data.datacode,
            label = $item.attr("name") || $item.attr("id"), obj = {};
        if ($.trim(xtype).toLowerCase() === "advice") {
            var val = $item.val(),//advice 文本的值。
                advicetype = $itemdiv.find("select").attr("tempflag"),//是人员意思还是部门意见 1，是人员 。 2是部门
                selval = $itemdiv.find("select").val(),//表单控件生成的select选中的value值
                checkedids = $itemdiv.find("input[name=savecheckedids]").val(),//弹出框选中的人或者部门的所有ids
                checkednames = $itemdiv.find("textarea[name=savecheckedname]").val();//弹出框选中的内容，即textarea中出现的文字
            if (typeof givedatacode !== "undefined") {
                if ($.trim(givedatacode).toLowerCase() === $.trim(datacode).toLowerCase()) {
                    obj = {
                        adviceid: val,
                        advicenameattr: label,
                        advicetype: advicetype === "person" ? "1" : "2",//1 是人员 2 是部门
                        advicetemptid: selval,
                        advicecheckedids: checkedids,
                        adviceContext: checkednames
                    };
                    objArr.push(obj);
                }
            } else {
                obj = {
                    adviceid: val,
                    advicenameattr: label,
                    advicetype: advicetype === "person" ? "1" : "2",//1 是人员 2 是部门
                    advicetemptid: selval,
                    advicecheckedids: checkedids,
                    adviceContext: checkednames
                };
                objArr.push(obj);
            }
        }
    }
    return objArr;
};
var getItemByName=function($parentNode,nodeName){
	//console.log(window.parent.document.getElementById("contentFrame").contentWindow.document);
       var alldata= $($parentNode).getDtypeFormData(),obj;
        if(alldata.length > 0 ){
            for(var i=0;i<alldata.length ; i++){
                var item=alldata[i],name=item.name;
                if(name==nodeName){
                    obj=item;
                    break;
                }
            }
        }
    return obj;
};
var setAdvice = function($item){
	$item.hide();
    var $befdiv= $item.parent().children("span"),divWid = $befdiv.attr("width"),data = $item.data(), dtype = data.dtype,
    isread = $item.attr("data-isread"), isreadonly = $item.attr("data-isreadonly"),
    xtype = data.xtype, datacode = data.datacode,code = $item.attr("data-code"),
    label = $item.attr("name") || $item.attr("id");
    $("<div class='advice' name='sel-" + label + "-div'></div>").insertAfter($item);
    var $pardiv = $item.next("div"),param=$item.attr("param"), selstr, $sel,paramid,paramkey;
    if(param && param !=null && param !=""){
        var paramarr=param.split(":");
        paramkey=paramarr[0];
        paramid=paramarr[1];
    }else{
        paramkey=null;
        paramid=null;
    }
    $pardiv.empty();
    //新advice控件
    if (isread === "1" && isreadonly === "2") {
    	$html = "<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' name='" + label + "' style='width: 150px; height: 72%;'></textarea><button name='sel-" + label + "' type='button' class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only' style='width:40px;height:30px;margin-top:-25px;margin-left:5px;' value='选择'>选择</button></div>" +
            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>";
        if(code === "YES"){
        	var $defaText = $("textarea[name=" + label + "]");
        	getNowUserName($defaText,paramkey);
        }
    } else if ((isread === "1" && isreadonly === "1")) {
    	$html = "<div class='advice-div'><input type='hidden' name='ids-" + label + "'><textarea readonly='readonly' style='background:#ffffff !important;line-height:28px;width: 150px; height: 72%;' name='" + label + "'></textarea><button name='sel-" + label + "' type='button' style='display:none;'></button></div>" +
            "<div style='display: none;'> <div name='showtree' style='overflow-x: hidden;white-space:normal'><div class='cenbox_tree' align='center' style='float:left'> <div class='zTreeDemoBackground left'> " +
            "<ul name='ztreeul' class='ztree'></ul></div></div></div> </div>";
    }
    	$pardiv.append($html);
    //新advice触发事件
      selstr = $($pardiv).find("button[name=sel-" + label + "]");
      $sel = $(selstr);
      addClick($sel,paramkey,paramid);//给按钮添加点击事件
}; 
/**
 *给endorse 即意见标签加默认值
 * @param $parNote 意见标签
 * @param content 默认内容
 */
var _endorse_Text=function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
        show=$parNote.attr("data-isread"),
        read=$parNote.attr("data-isreadonly");
    if(xtype==="endorse" && show==='1' && read==='2'){//显示的时候
        $parNote.next().find(".endorse-textarea").val(content);
    }
};
var _defaultadvice_Text = function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
    show=$parNote.attr("data-isread"),
    read=$parNote.attr("data-isreadonly");
    if(xtype==="defaultadvice" && show==='1' && read==='2'){//显示的时候
    	$parNote.next().find("input[name=set-defaultadvice]").val(content);
    }
};
var _countersign_Text=function($parNote,content){
    var xtype=$parNote.attr("data-xtype"),
        show=$parNote.attr("data-isread"),
        read=$parNote.attr("data-isreadonly");
    if(xtype==="countersign" && show==='1' && read==='2'){//显示的时候
        $parNote.next().find("input[name=set-countersign]").val(content);
    }
};




/**
 *   yuanyuan.zhang
 *   end
 */


/**
 * 初始化表单的验证(带xtype)
 * formQuery 表单
 * tobj 可以覆盖原先的验证配置
 */
var _initValidateForXTypeForm = function (formQuery, tobj) {
    var $queryForm = $(formQuery), r = {}, m = {};
    $queryForm.find("[data-validate]").each(function (index, item) {
        var $item = $(item), nOi = $item.attr("name") || $item.attr("id");
        if (nOi) {
            r[nOi] = new Function("return " + $item.data("validate") + ";")();
            if ($item.data("errormessage")) {
                m[nOi] = $item.data("errormessage");
            }
        }
    });
    var obj = $.extend({
        rules: r,
        messages: m
    }, tobj);
    $queryForm.validate(obj);
};

//$.metadata.setType("attr", "data-validate");

$("body").ready(function () {
    //$( document ).ajaxStart(function() {$( "body" ).mask();}).ajaxComplete(function(){$("body").unMask();}).ajaxError( function(ar1, ar2, ar3) {console.log(ar3);var eobj = {"text" : ar3 ? ar3.message || ar3 : "","title" : "发生错误"};/*window.message(eobj);*/});
    if (resizeFun) {
        $(window).resize(resizeFun);
    }
});
