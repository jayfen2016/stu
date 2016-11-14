$(document).ready(function() {
    //$("body").on('touchmove', function (event) { event.preventDefault(); });

    var $doc = $("body"), $header = $(".nx-header"), $left = $(".nx-left"), $content = $(".nx-content"), $breadcrumb = $content.find(".nx-breadcrumb"), $iframe = $content.find("#contentFrame");
    //判断是否移动端的IE，如果是加入viewport配置
    if (navigator.userAgent.match(/IEMobile\/10\.0/)) {
        var msViewportStyle = document.createElement("style");
        msViewportStyle.appendChild(document.createTextNode("@media screen and (orientation: landscape) {@-ms-viewport {width: 1024px;height: 768px;user-zoom:fixed;}}       @media screen and (orientation: portrait) { @-ms-viewport { height: 1024px;user-zoom:fixed;}}"));
        document.getElementsByTagName("head")[0].appendChild(msViewportStyle);
    }
    //布局管理
    $.layoutMgr = [];
    $.autoLayout = true;
    $.layoutMode = "noleft";
    // noleft;nomal
    //执行所有代理在布局管理器中的函数
    $.resetLayout = function() {
        for (var i = 0; i < $.layoutMgr.length; i++) {
            var item = $.layoutMgr[i];
            item.fun({
                data : item.data
            });
        }
    };
    $(window).resize($.resetLayout);
    //Window layout Event
    //初始整体布局的函数
    $.layoutMgr.push({
        data : null,
        fun : function() {
            var ah = $doc.outerHeight(true), aw = $doc.outerWidth(true), brcHeight = $breadcrumb.css("display") === "none" ? 0 : $breadcrumb.outerHeight(true), hh = $header.outerHeight(true), htop = $header.position().top, lw = $left.outerWidth(true), lleft = $left.position().left, nh = ah - hh - htop, nw = aw - lw - lleft, lhei = ah - hh;
            $left.height(lhei);
            $content.height(nh).width(nw);
            $iframe.height(nh - brcHeight);
            //配置TOPMENU最大适应宽度
            var ofcw = 0;
            $header.children().not(".ui-pagedmenu-panel").each(function(index, item) {
                ofcw += $(item).outerWidth(true);
            });
            ofcw = aw - ofcw;
            $header.find(".ui-pagedmenu-panel").width(ofcw);
        }
    });
    $.removeLayoutMgrItem=function(data){
        if(!data){return;}
        var tary=[],lm=$.layoutMgr;
        for(var i=lm.length;--i;){
            var item=lm[i];
            if(item.data!==data){
                tary.push(item);
            }
        }
        $.layoutMgr=tary;
    };
    $.resetLayout();
    //TOPMENU 头部的菜单
    $header.find(".ui-pagedmenu-panel").pagedmenu({
        change : function(a, it) {
            var data = $(it).data();
            $(".nx-left").accordionMenu({
                source : data
            });
            if ($.layoutMode === "noleft") {
                swithMode("nomal");
            }
            if (data.url && data.url !== "") {
                //$(".nx-content iframe")[0].src = data.url;
                window.frames["contentFrame"].location.replace(data.url);
            }
        }/*,
         sourceUrl: "files/json/menus.json"*/
    });
    // 全屏方法
    var fullScreen = function() {
        var $he = $(".nx-header"), $le = $(".nx-left"), $con = $(".nx-content"), oh = $he.outerHeight(true), ol = $le.outerWidth(true);

        $con.hide("fade", 100, function() {
            $le.animate({
                left : -ol
            }, 100, "easeOutCirc", function() {
                $he.animate({
                    top : -oh
                }, 500, "easeOutElastic", function() {
                    $con.show("fade", 100);
                    $.resetLayout();
                });
            }).removeClass("right-shadow");
        });
    };
    //取消全屏方法
    var cancleFullScreen = function() {
        var $he = $(".nx-header"), $le = $(".nx-left"), $con = $(".nx-content");
        /*
         * , oh = $he.outerHeight(true), ol =
         * $le.outerWidth(true)
         */
        $con.hide("fade", function() {
            if ($.layoutMode === "nomal") {

                $he.animate({
                    top : 0
                }, 500, "easeOutBounce", function() {
                    $le.animate({
                        left : 0
                    }, 100, "easeOutCirc", function() {
                        $con.show("fade", 100);
                        $.resetLayout();
                    }).addClass("right-shadow");
                });
            } else if ($.layoutMode === "noleft") {
                $he.animate({
                    top : 0
                }, 500, "easeOutBounce", function() {
                    $con.show("fade", 100);
                    $.resetLayout();
                });
            }
        });
    };
    //读取本地存储中全屏按钮的位置，不支持则跳过
    (function() {//加载上次本地存储中全屏按钮的位置
        if (window.localStorage) {
            var r = window.localStorage["fsb_left_or_right"], b = window.localStorage["fsb_top_or_bottom"], lr = window.localStorage["fsb_lr"], tb = window.localStorage["fsb_tb"];
            if (r !== undefined && b !== undefined && lr !== undefined && tb !== undefined) {
                r = parseInt(r, 10);
                b = parseInt(b, 10);
                var $cbtn = $(".full-screen-button");
                var opt = {
                    left : "auto",
                    top : "auto",
                    right : "auto",
                    bottom : "auto"
                };
                opt[lr] = r + "px";
                opt[tb] = b + "px";
                $cbtn.css(opt);
            }
        }
    })();
    //给全屏按钮添加拖拽及单击事件
    $(".full-screen-button").draggable({
        containment : "body",
        scroll : false,
        start : function(evet, ui) {
            this.tempmask = $("<div>").css({
                "background-color" : "rgba(0, 0, 0, 0.43)",
                "position" : "absolute",
                "z-index" : "2",
                "left" : "0px",
                "top" : "0px",
                "width" : $content.outerWidth(),
                "height" : $content.outerHeight()
            }).appendTo($content);
        },
        stop : function(event, ui) {
            if (this.tempmask) {
                this.tempmask.remove();
                var $this = $(this), tw = $doc.innerWidth(), th = $doc.innerHeight(), vw = $this.outerWidth(), vh = $this.outerHeight();
                r = tw - vw - $this.position().left, b = th - vh - $this.position().top;

                var lr = "", tb = "", yuv_lr = 0, yuv_tb = 0, opt = {
                    left : "auto",
                    top : "auto",
                    right : "auto",
                    bottom : "auto"
                };
                if ($this.position().left - (vw / 2) > tw / 2) {
                    lr = "right";
                    yuv_lr = r;
                } else {
                    lr = "left";
                    yuv_lr = $this.position().left;
                }
                if ($this.position().top - (vh / 2) > th / 2) {
                    tb = "bottom";
                    yuv_tb = b;
                } else {
                    tb = "top";
                    yuv_tb = $this.position().top;
                }
                opt[lr] = yuv_lr;
                opt[tb] = yuv_tb;
                if (window.localStorage) {
                    window.localStorage["fsb_lr"] = lr;
                    window.localStorage["fsb_tb"] = tb;
                    window.localStorage["fsb_left_or_right"] = yuv_lr;
                    window.localStorage["fsb_top_or_bottom"] = yuv_tb;
                }
                $(this).css(opt);
            }
        }
    }).click(function() {
        var $vthis = $(this), iscf = $vthis.data("iscf"), canotClick = $vthis.data("canotClick");
        if (canotClick) {
            return;
        }
        if (iscf) {
            cancleFullScreen();
            $vthis.removeClass("cancel-full-screen-button").addClass("full-screen-button");
        } else {
            fullScreen();
            $vthis.removeClass("full-screen-button").addClass("cancel-full-screen-button");
        }
        $vthis.data("iscf", iscf ? false : true);
        $vthis.data("canotClick", true);
        setTimeout(function() {
            $vthis.data("canotClick", false);
        }, 900);
    });
    //视图模式 VIEW MODE  有noleft和nomal
    window.swithMode = function(mode) {
        var $le = $(".nx-left"), $con = $(".nx-content"), left = 0;
        if ($.layoutMode === mode) {
            return;
        }
        $.layoutMode = mode;
        if (mode === "noleft") {
            var ol = $le.outerWidth(true);
            left = -ol;
            // $breadcrumb.css("display", "none");
        } else if (mode === "nomal") {
            left = 0;
        }
        $con.hide("fade", function() {
            $le.animate({
                "left" : left
            }, 100, "easeOutCirc", function() {
                $con.show("fade");
                $.resetLayout();
            });
            if (left === 0) {
                $le.addClass("right-shadow");
            } else {
                $le.removeClass("right-shadow");
            }
        });
    };
    // HEADER BAR
    $(".menubar")[0].onselectstart = function(ev) {
        ev.preventDefault();
    };
    //初始DropDown控件并绑定事件
    $(".menubar .nx-dropdown").dropdown({
        width:231,
        menuclick : function(ev, sender) {
            var $sender = $(sender);
            switch ($sender.attr("actionType")) {
                case "switchUser":
                    var uid = $sender.attr("uid");
                    switchUser(uid);
                    break;
                case "message":
                    break;
                case "editInfo":
                    break;
                case "setting":
                    break;
                case "changePassword":
                    break;
            }
        }
    });
    // 退出按钮事件
    $("#logoutbtn").click(function() {
        $("<div><br/><span>将会销毁当前用户会话,并会放弃所有未保存的操作。</span></div>").dialog({
            show : 'blind',
            hide : 'explode',
            modal : true,
            draggable : false,
            title : "确认要退出吗？",
            buttons : {
                "确认" : function() {
                    $(this).dialog("close");
                    $.ajax({
                        url: "j_spring_security_logout",
                        type: "POST",
                        dataType: "JSON",
                        success: function (data, xhr) {
                            window.location.replace("login.jsp");
                        }
                    });
                },
                "取消" : function() {
                    $(this).dialog("close");
                }
            }
        });
    });
    // 左侧的风琴菜单
    $(".nx-left").accordionMenu({
        change : function(ar1, ar2) {
            var data = $(ar2).data();
            if(window.frames["contentFrame"].location==undefined){
            	window.frames["contentFrame"].contentWindow.location.replace(data.url);
            }else{
            	window.frames["contentFrame"].location.replace(data.url);
            }
            
        }
    });
    // iframe加载完成的时候，修改面包屑内容
    $("#contentFrame").bind("load", function() {
        // Tr Breadcrumb
        var selectedTopMenu = $(".ui-pagedmenu-panel").pagedmenu("option", "selectedItem");
        var $ul = $('<ul class="ui-breadcrumb">');
        if (selectedTopMenu) {
            $ul.append($(' <li><i class="fa fa-home"></i><a href="javascript:;" onclick="backHome();">主页</a></li>'));
            var data = $(selectedTopMenu).data();
            $('<li class="aw-right">&gt;</li>').appendTo($ul);
            $('<li>' + data.title + '</li>').appendTo($ul);
            var selectedLeftMenu = $(".nx-left").accordionMenu("option", "selectedItem");
            if (selectedLeftMenu) {
                var $tslitem = $(selectedLeftMenu);
                var $parent = $tslitem.parent().parent().prev();
                if ($parent.is(".nohover")) {
                    $('<li class="aw-right">&gt;</li>').appendTo($ul);
                    $('<li>' + $parent.data().title + '</li>').appendTo($ul);
                }
                $('<li class="aw-right">&gt;</li>').appendTo($ul);
                $('<li class="active">' + $tslitem.data().title + '</li>').appendTo($ul);
            }
        } else {
            $ul.append($(' <li><i class="fa fa-home"></i>主页</li>'));
        }
        var $ub = $(".nx-breadcrumb");
        $ub.children().remove();
        $ub.append($ul);
    });
    // 回退到主页的方法
    window.backHome = function() {
        if ($.layoutMode === "noleft") {
            return;
        }
        $(".ui-pagedmenu-panel").pagedmenu("cancelSelected");
        $(".nx-left").accordionMenu("cancelSelected");
        window.frames["contentFrame"].location.replace("pages/dashboard.jsp");
        swithMode("noleft");
    };
//定义弹出窗体（可以覆盖到最大）
    window.frameDialog = function(url, title, opts) {
        var $this = undefined;
        if (!url) {
            return;
        }

        var ev_resize = function(event) {
            getWH();
            $this.dialog("option", {
                width : w,
                height : h
            });
        };
        var opt = undefined;
        opt = $.extend({
            mode : 'default',
            resizable : opts.mode !== "full",
            draggable : opts.mode !== "full",
            // show : {
            // effect : "drop",
            // direction : "up",
            // easing : 'easeInOutExpo',
            // duration : 900
            // },
            // hide : {
            // effect : "fade",
            // easing : 'easeOutCirc',
            // duration : 1
            // },
            modal : opts.mode !== "full"
        }, opts), oldopts = $.extend({}, opts), w = 0, h = 0;
        var getWH = function() {
            if (opt.mode === "full") {
                w = $doc.width();
                h = $doc.height();
            } else {
                w = oldopts.width ? oldopts.width : $doc.innerWidth() - 24;
                h = oldopts.height ? oldopts.height : $doc.innerHeight() - 24;
            }
            $.extend(opt, {
                width : w,
                height : h
            });
        };
        getWH();
        $.extend(opt, {
            dialogClass : opt.mode === "full" ? "full-choose-dialog"+(opt.topbar?" top-bar":"") : "choose-dialog"
        });
        if (opt.mode === "full") {
            $(window).resize(ev_resize);
        }
        $this = $(["<div title='", title ? title : "请选择", "'><iframe src='", url, "'></iframe></div>"].join('')).dialog(opt);
        var tf=$this.find("iframe")[0].contentWindow,
            data=opts.data;
        tf.onload=function(){
            if(tf.setValue){
                tf.setValue(data);
            }
            if(tf.initHTML){tf.initHTML();}
        };
        $this.on( "dialogbeforeclose", function( event, ui ) {
            if (opt.mode === "full") {
                $(window).unbind("resize", ev_resize);
            }
            tf.blur();
            tf.document.write("");
            $this.empty();
        } );
        $this.on("dialogclose",function(event,ui){
            $(this).dialog("destroy");
        });
        return $this;
    };
    //定义弹出框
    window.dialog = function(html, opts) {
        var opt = $.extend({
            close : function() {
                $(this).dialog("destroy");
            }
        }, opts);
        return	$(html).dialog(opt);
    };

    window.Msg={};
    //定义消息框
    window.Msg.message = function(opts) {
        var set = $.extend({
            text : "",
            //icon : "info",
            width : 355,
            title : "提示"/*,
             buttonLabel: "确认"*/
        }, opts), opt = $.extend({
            close : function() {
                $(this).dialog("destroy");
            },
            modal : true
        }, set);
        var html = ["<div title='", set.title, "' >", set.icon ? '<span class="ui-icon ui-icon-' + set.icon + '" style="float:left;margin-right:5px;"></span>' : "", set.text, "</div>"];

        return $(html.join("")).dialog(opt);
    };
    window.Msg.error=function(text,title,icon){
        window.Msg.message({title:title||'发生错误',text:text||"",icon:icon||"alert"});
    };
    window.Msg.alert=function(text,title,icon,autoclose){
        var dig= window.Msg.message({title:title||'提醒',text:text||"",icon:icon||"info",buttons:[
            {text:"确认",click:function(ev){
                $(this).dialog("close");
            }}
        ]});
        if(autoclose){
            //var digObj=dig.data("ui-dialog"),
            var $btn=dig.closest(".ui-dialog").find(".ui-dialog-buttonpane button:eq(0)"),
                $btnText=$btn.find(".ui-button-text"),
                oldText=$btnText.text(),
                time=$.isNumeric(autoclose)?autoclose:2,
                doClose=function(){
                    if(time<0){
                        $btn.trigger("click");
                        clearInterval(ds);
                    }
                    $btnText.html(oldText+" (<label style='color:#e0e0e0;'>"+time+"</label>)");
                    time--;
                };
            var ds=setInterval(doClose,1000);
            doClose();
        }
        return dig;
    };
    //定义遮罩
    window.frameMask = function(title) {
        $("body").mask(title);
    };
    //卸载遮罩
    window.frameUnMask = function() {
        $("body").unMask();
    };

    window.fullShipFrame=function(url,title,buttons){
        var $el=$("<div class='full-ship-panel'>"),
            $headBarEl=$("<div class='full-ship-panel-title-bar'>").appendTo($el),
            $titleEl=$("<h4>").html(title).appendTo($headBarEl),
            $buttonsEl=$("<div class='btn-area'>").appendTo($headBarEl),
            $closeBtnEl=$('<button><i class="fa fa-times"></i>关闭</button>').appendTo($buttonsEl),
            $frameEl=$('<div class="full-ship-panel-content"><iframe frameborder="no" border="0" marginheight="0" marginwidth="0" class="drop-frame"></iframe></div>').appendTo($el).find("iframe");
        $closeBtnEl.button().click(function(){
            $el.hide({effect: "fade",
                //direction: "down",
                easing: 'easeInOutExpo',
                duration: 800, complete:function(){
                    $el.remove();
                    window._fullShipFrameEl=null;
                    if($.browser.msie&&($.browser.version === "7.0") ){
                        $.removeLayoutMgrItem("fullShipLayout");
                    }
                }});
            try
            {
                if( buttons)buttons();
            }catch(ex){}
        });
        $el.appendTo("body").show({effect: "fade",//slide
            //direction: "down",
            easing: 'easeInOutExpo',
            duration: 800,
            complete:function(){
                var $p= $frameEl.parent(),h=$p.innerHeight(),w=$p.innerWidth();
                $frameEl.attr("src",url);
                if($.browser.msie&&($.browser.version === "7.0") ){
                    $frameEl.css({width:w,height:h});
                    $.layoutMgr.push({data:"fullShipLayout",fun:function(){
                        var $p= $frameEl.parent(),h=$p.innerHeight(),w=$p.innerWidth();
                        $frameEl.css({width:w,height:h});
                    }});
                }
            }});
        window._fullShipFrameEl=$el;
    };

    //切换用户
    var switchUser = function(roleId) {
        var url = "homePage_.do?command=switchRole"+(roleId?"&roleId="+roleId:"");
        $("body").mask("正在读取用户信息...");
        $.getJSON(url).done(function(data) {
            var n = data.displayName, icon = data.userIcon, ous = data.otherUsers, $nx = $("#nxbbbb"), $icon = $nx.find(".user-icon"), $otherUsers = $nx.find("ul");
            $icon.attr("src", icon).next().text(n);
            $otherUsers.find("li a[actionType=switchUser]").closest("li").remove();
            for (var i = 0, len = ous.length; i < len; i++) {
                var item = ous[i];
                $(['<li><a actionType="switchUser" uid="', item.userId, '" tabindex="-1" href="javascript:;" class="main-link">', item.displayName, '</a></li>'].join("")).appendTo($otherUsers);
            }
            $.resetLayout();
            $(".ui-pagedmenu-panel").pagedmenu("option", {
                sourceUrl : "homePage_.do?command=initMenu"
            });
            backHome();
            $("body").unMask();
            //
        }).fail(function() {
            $("body").unMask();
            //
            message({
                text : "切换用户失败!",
                title : "提醒"
            });
        });
    };
    //初始化用户信息和菜单
    (switchUser)();
});