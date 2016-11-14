/*! Jquery UI pagedmenu
*
*version 1.0 2014-02-26
*author Qunpeng.Li
*
*/
(function ($) {
    $.widget("ui.pagedmenu", {
        version: "1.0",
        contentElement: null,//内容区域元素
        pagerPreviousBar: null,//分页上一页按钮
        pagerNextBar: null,//分页下一页按钮
        menuItems: null,//菜单集合
        menuItemsTotalWidth: 0,//菜单总宽度
        previousBarWidth: 0,//上一页按钮宽度
        nextBarWidth: 0,//下一页按钮宽度
        _oldWidth: 0,
        pagerData: {
            pageCount: 1,
            currentPage: 0,
            pagerAry: []
        },
        options: {
            cls: "ui-pagedmenu-panel",
            change: null, //Events
            sourceUrl: null,
            selectedItem: undefined
        }, _create: function () {//CREATE
            if (!this.element.hasClass(this.options.cls)) {
                this.element.addClass();
            }
            this.contentElement = this.element.find(".ui-pagedmenu-content");
            this.pagerPreviousBar = this.element.find(".ui-pagedmenu-pager-bar:eq(0)");
            this.pagerNextBar = this.element.find(".ui-pagedmenu-pager-bar:eq(1)");
            this.pagerPreviousBar.click(this, this._previousPage);
            this.pagerNextBar.click(this, this._nextPage);
            this.previousBarWidth = this.pagerPreviousBar.outerWidth(true);
            this.nextBarWidth = this.pagerNextBar.outerWidth(true);
            this.pagerNextBar[0].onselectstart = this.pagerPreviousBar[0].onselectstart = this.contentElement[0].onselectstart = function () {
                if (event.preventDefault) {
                    event.preventDefault();
                } else {
                    return false;
                }
            };
            this._initContentConfig();
            //bind resize event
            if ($.autoLayout) {
                $.layoutMgr.push({ data: this, fun: this._resize });
            } else {
                $(window).resize(this, this._resize);
            }
            if (this.options.sourceUrl) {
                this.loadMenusForUrl(this.options.sourceUrl);
            }
        }, _initContentConfig: function () {//
            var ofeWidth = 0,
                vthis = this;
            this.menuItems = this.contentElement.children();
            this.menuItems.each(function (index, item) {
                $(item).data({ "al": ofeWidth }).click(vthis, vthis._itemClick);
                ofeWidth += $(item).outerWidth(true);
            });
            this.menuItemsTotalWidth = ofeWidth;
        }, loadMenusForUrl: function (url) {
            var vthis = this;
            if (url && url!="") {
                this.options.sourceUrl = url;
            }
            vthis._clearItems();
            $.getJSON(this.options.sourceUrl).done(function (data) {
                vthis._addItems(data.response);
                vthis._initContentConfig();
                vthis._refresh();
            }).fail(function () {
                vthis._initContentConfig();
                vthis._refresh();
            });
        }, _clearItems: function () {
            this.contentElement.children().remove();
            this._oldWidth = 0;
            this.options.selectedItem = undefined;
        }, cancelSelected: function () {
            if (this.options.selectedItem) {
                $(this.options.selectedItem).removeClass("selected");
                this.options.selectedItem = undefined;
            }
        }, _addItems: function (items) {
            var $cel = this.contentElement, elAry = [], item = undefined;
            for (var i = 0, len = items.length; i < len; i++) {
                item = items[i];
                elAry.push($("<div class='ui-pagedmenu-menu-item'>").text(item.title).data(item).addClass(item.cls));
            }
            $cel.append(elAry);
        }, _setOptions: function (options) {//OVERVIEW SETOPTIONS
            var that = this,
			  reload = false;
            $.each(options, function (key, value) {
                that._setOption(key, value);
                if (key === "sourceUrl") {
                    reload = true;
                }
            });
            if (reload) {
                this.loadMenusForUrl(this.options.sourceUrl);
            }
        }
        , _init: function () {//INIT
            console.log("pagedmenu init");
            this._refresh();
        }, _itemClick: function (le) {
            var $vthis = le.data,
            $item = $(this);
            if ($item.hasClass("selected")) {
                return;
            }
            $vthis.menuItems.filter(".selected").removeClass("selected");
            $item.addClass("selected");
            $vthis.options.selectedItem = this;
            $vthis._trigger("change", new jQuery.Event(), this);
        }, _resize: function (le) {//EVENT RESIZE
            le.data._refresh();
        }, _refresh: function () {//RESET LAYOUT
            //console.time("Refresh Time");
            var $el = this.element,
                allWidth = $el.outerWidth(true),
                contentWidth = this.menuItemsTotalWidth,
                isPager = false,
                offsetTotalWidth = allWidth,
                $pbar = this.pagerPreviousBar,
                $nbar = this.pagerNextBar,
                pageCount = 1,
                currentPage = 0,
                pagerAry = [];

//            if (this._oldWidth === allWidth) {
//                return;
//            }
            this._oldWidth = allWidth;
            if (allWidth < contentWidth) {
                isPager = true;
                offsetTotalWidth = allWidth - this.previousBarWidth - this.nextBarWidth;
            }
            if (isPager) {
                $pbar.show();
                $nbar.show();
                if (contentWidth % allWidth === 0) {
                    pageCount = parseInt(contentWidth / offsetTotalWidth);
                } else {
                    pageCount = parseInt(contentWidth / offsetTotalWidth) + 1;
                }
                var tindex = 0, tw = 0;
                this.menuItems.each(function (index, item) {
                    var $item = $(item),
                        itw = $item.outerWidth(true);
                    tw += itw;
                    if (tw > offsetTotalWidth) {
                        tindex++;
                        tw = itw;
                    }
                    if (!pagerAry[tindex]) {
                        pagerAry[tindex] = [];
                    }
                    if ($item.hasClass("selected")) {
                        currentPage = tindex;
                    }
                    pagerAry[tindex].push($item);
                });
            } else {
                $pbar.hide();
                $nbar.hide();
            }
            this.pagerData.currentPage = currentPage;
            this.pagerData.pageCount = pageCount;
            this.pagerData.pagerAry = pagerAry;
            this._doPage(currentPage, true);
            //console.timeEnd("Refresh Time");
        }, _previousPage: function (le) {//PREVIOUS PAGE
            var vthis = le.data,
             cp = vthis.pagerData.currentPage;
            if (vthis.pagerPreviousBar.hasClass("disabled")) {
                return;
            }
            vthis._doPage(cp - 1);
        }, _nextPage: function (le) {//NEXT PAGE
            var vthis = le.data,
            cp = vthis.pagerData.currentPage;
            if (vthis.pagerNextBar.hasClass("disabled")) {
                return;
            }
            vthis._doPage(cp + 1, true);
        }, _doPage: function (currentPage, isfirh) {//DO PAGE
            var pageCount = this.pagerData.pageCount,
                currentIndex = (currentPage != undefined) ? currentPage : this.pagerData.currentPage,
                pagerAry = this.pagerData.pagerAry,
                $cEle = this.contentElement;
            this.pagerData.currentPage = currentIndex;
            if (pageCount == 1) {
                this.menuItems.each(function (index, item) {
                    item.style.visibility = "visible";
                });
                $cEle.animate({ "left": 0 });
                return;
            }
            var pageData = pagerAry[currentIndex];
            var needHideObj = null;
            if (pagerAry.length - 1 !== currentIndex) {
                needHideObj = pagerAry[currentIndex + 1][0];
            }
            for (var i in pageData) {
                pageData[i].css("visibility", "visible");
            }
            if (isfirh) {
                if (needHideObj) {
                    needHideObj.css("visibility", "hidden");
                }
                $cEle.animate({ "left": -pageData[0].data("al") });
            } else {
                $cEle.animate({ "left": -pageData[0].data("al") }, function () {
                    if (needHideObj) {
                        needHideObj.css("visibility", "hidden");
                    }
                });
            }
            this.pagerPreviousBar.removeClass("disabled");
            this.pagerNextBar.removeClass("disabled");
            if (currentIndex === 0) {
                this.pagerPreviousBar.addClass("disabled");
            }
            if (currentIndex === pageCount - 1) {
                this.pagerNextBar.addClass("disabled");
            }
        }
    });
})(jQuery);
