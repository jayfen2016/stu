/*! Jquery UI dropdown
*
*version 1.0 2014-02-26
*author Qunpeng.Li
*
*/
(function ($) {
    $.widget("ui.dropdown", {
        version: "1.0",
        options: { ccEles: null, downElement: null, heldWidth: true,width:null, menuclick: null },
        _create: function () {
            var vthis = this;
            this.options.downElement = this.element.find(".nx-dropdown-content").click(function (eve) {
                eve.stopPropagation();
                vthis._allClick({ data: vthis });
            });
            this.element.click(this, this._itemClick);
            var tquery = $(window).add(window.document);
            for (var i = 0, len = window.frames.length; i < len; i++) {
                try {
                    tquery = tquery.add(window.frames[i]);
                } catch (ex) { }
                try {
                    tquery = tquery.add(window.frames[i].document);
                } catch (ex2) { }
            }
            this.options.ccEles = tquery;
            this.options.downElement.find("ul").click(function ($ev) {
                if ($ev.target.nodeName === "A") {
                    vthis._trigger("menuclick", new jQuery.Event(), $ev.target);
                }
            });
        }, _itemClick: function (le) {
            var $vthis = le.data,
                isSelect = $vthis.element.hasClass("selected");
            //clear & trigger one event
            try {
                $vthis.options.ccEles.trigger("click");
            } catch (ex) { }
            if (isSelect) {
                return;
            }
            //show
            $vthis._dropdown();
            //one event
            $vthis.options.ccEles.each(function (index, item) {
                try {
                    $(item).one("click", $vthis, $vthis._allClick);
                } catch (ex2) { }
            });
            //stop propagation
            le.stopPropagation();
        }, _allClick: function (le) {
            //if (!le.data.element.hasClass("selected")) { return;}
            try {
                le.data.options.ccEles.unbind("click", le.data._allClick);
            } catch (ex) { }
            le.data.element.removeClass("selected");
            if (le.data.options.downElement) {
                le.data.options.downElement.css({ "visibility": "hidden" });
            }
        }, _dropdown: function () {
            var $el = this.element.addClass("selected");
            if (!this.options.downElement) {
                return;
            }
            var left = $el.offset().left,
                top = $el.offset().top,
                w =  $el.outerWidth(true),
                h = $el.outerHeight(true),
                docW = $("body").outerWidth(true),
                //docH = $("body").outerHeight(true),
                $dEle = this.options.downElement;

            $dEle.css({ "visibility": "visible", top: h + top, "left": 0, width: this.options.width?this.options.width:w });
        }
    });
})(jQuery);