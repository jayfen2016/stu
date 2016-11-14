/*! Jquery UI accordionMenu
*
*version 1.0 2014-02-26
*author Qunpeng.Li
*
*/
(function ($) {
    $.widget("ui.accordionMenu", {
        version: "1.0",
        options: {
            source: null,
            cls: "ui-accordion-menu",
            selectedItem: null
        }, _setOptions: function (options) {//OVERVIEW SETOPTIONS
            var that = this,
			  reload = false;
            $.each(options, function (key, value) {
                that._setOption(key, value);
                if (key === "source") {
                    reload = true;
                }
            });
            if (reload) {
                this.loadMenus();
            }
        }, _create: function () { //CREATE
            var $el = this.element;
            if (this.options.source) {
                this.loadMenus();
            }
            $el.addClass(this.options.cls);
        }, loadMenus: function (optSource) {//load menus
            if (optSource && optSource.menus !== undefined) {
                this.options.source = optSource;
            }
            this.clearContent();
            var $el = this.element,
                osource = this.options.source,
                menus = osource.menus,
                    elAry = [];
            // delete osource.menus;
             $("<div>").addClass("menu-title").text(osource.title).data(osource).appendTo($el);
            for (var i = 0, len = menus.length; i < len; i++) {
                var menuitem = menus[i],
                    $temel = $("<li>"),
                    chitems = undefined,
                        $curEl = $("<a>");
                var conHtml = "";
                if (menuitem.cls && menuitem.cls !== "") {
                    conHtml = '<i class="' + menuitem.cls + '"></i>' + menuitem.title;
                } else {
                    conHtml = menuitem.title;
                }
                $curEl.attr({ tabindex: -1, href: "javascript:;" }).html(conHtml);
                chitems = menuitem.menus;
                // delete menuitem.menus;
                $curEl.data(menuitem).appendTo($temel);
                if (chitems && chitems.length !== 0) {
                    // mu
                    $curEl.addClass("nohover");
                    $temel.addClass("fold");
                    var $innerUl = $("<ul>"), icAry = [];
                    for (var ti = 0, tlen = chitems.length; ti < tlen; ti++) {
                        var tiItemm = chitems[ti],
                            $imenuT = $("<li>");
                        $tiLink = $('<a tabindex="-1" href="javascript:;">' + tiItemm.title + '</a>').data(tiItemm);
                        $imenuT.append($tiLink);
                        icAry.push($imenuT);
                    }
                    $temel.append($innerUl.append(icAry));
                }
                elAry.push($temel);
            }
            if (elAry.length !== 0) {
                 $("<ul>").append(elAry).appendTo($el).find("a").click(this, this._linkClick);
            }
            this.reDraw();//IE 9 AND IE 10
        }, reDraw: function () {
            var $el = this.element;
            $el.css("overflow-y", "hidden");
            setTimeout(function () {
                $el.css("overflow-y", "auto");
            }, 200);
        }, clearContent: function () { // clear old content
            this.element.find("li a").unbind("click", this._linkClick);
            this.element.children().remove();
            this.options.selectedItem = undefined;
        }, cancelSelected: function () {
            if (this.options.selectedItem) {
                $(this.options.selectedItem).removeClass("selected");
                this.options.selectedItem = undefined;
            }
        }, _linkClick: function (le) {
            var $vthis = le.data;
            var $trt = $(le.target);
            if ($trt.hasClass("nohover")) {
                var $par = $trt.parent();
                if ($par.hasClass("open")) {
                    $par.removeClass("open").addClass("fold");
                } else {
                    $par.removeClass("fold").addClass("open");
                }
                $vthis.reDraw();//IE 9 AND IE 10
            } else if (!$trt.hasClass("selected")) {
                if ($vthis.options.selectedItem) {
                    $($vthis.options.selectedItem).removeClass("selected");
                }

                $trt.addClass("selected");
                $vthis.options.selectedItem = le.target;
                $vthis._trigger("change", new jQuery.Event(), this);
            } else{//==============CHANGE==========
                $vthis._trigger("change", new jQuery.Event(), this);
            }//================CHANGE END==========
        }, select: function ($el) {
            $el.trigger("click", new jQuery.Event(), this);
        }
    });
})(jQuery);