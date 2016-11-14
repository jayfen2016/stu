/*! Jquery UI nodeline
*
*version 1.0 2014-02-26
*author Qunpeng.Li
*
*/
(function ($) {
    $.widget("ui.nodeline", {
        version: "1.0",
        defaultElement: "<div>",
        lineElement: null,
        maskElement: null,
        options: {
            width: 0,//
            height: 200,//
            source: [
                { tag: "div", left: 0, cls: "node-circle red", label: "01日" },
                { tag: "div", left: parseInt(5 / 31 * 100) + "%", cls: "node-circle red-circle", label: "5日" },
                { tag: "div", left: parseInt(10 / 31 * 100) + "%", cls: "node-circle blue-circle", label: "10日" },
                { tag: "div", left: "100%", cls: "node-circle", label: "31日" },
                { tag: "div", left: 0,right: parseInt(5 / 31 * 100) + "%", cls: "node-descr",content: "手工录入"},
                { tag: "div", left:  parseInt(8 / 31 * 100) + "%",right: parseInt(12 / 31 * 100) + "%", cls: "node-descr",content: "市场算税<br/>差异分析报告比对"},
                { tag: "div", left: parseInt(5 / 31 * 100) + "%", cls: 'node-talg', attr: { title: "如果没有算税，by税种提醒（检查基本数据&算税）" } }
               

            ],
            margin: { left: 50, right: 50, top: 100 },
            cls: "ui-nodeline-panel",
            lineCls: "ui-nodeline"
        }, _create: function () {//CREATE
            this.element.addClass(this.options.cls);
            $(window).bind("resize", this, this._resize);
            $(this.element).tooltip({
                items: ".node-circle,.node-talg",
                position: { my: "left+15 center", at: "right center" },
                content: function () {
                    var element = $(this);
                    if (element.is("[title]")) {
                        return element.attr("title");
                    }
                    if (element.is(".node-circle")) {
                        // return "SB";
                    }
                }
            });
            this.lineElement = $("<div>").addClass(this.options.lineCls).appendTo(this.element);
            this.maskElement = $("<div>").addClass("mask-panel").appendTo(this.element);
        }, _init: function () {//INIT
            console.log("_init");
            this.refresh();
        }, _resize: function (le) {//RESIZE
            le.data.refresh();
        }, refresh: function () {//REFRESH
            console.log("refresh");
            //this.maskElement.animate({ top: "0%" }, 500, "easeOutBounce");

            var $el = this.element,
				tw = this.options.width = $el.parent().width(),
				th = this.options.height,
					lw = tw - this.options.margin.left - this.options.margin.right,
					ll = this.options.margin.left,
					lt = this.options.margin.top;
            $el.height(th);//width(tw).
            this.lineElement.css({
                "width": lw,
                "left": ll,
                "top": lt
            });
            $el.children().not(this.lineElement).not(".mask-panel").remove();
            var source = this.options.source;
            for (var i = 0, len = source.length; i < len; i++) {
                var titem = source[i], left = 0, top = lt,twidth=undefined;
                if (typeof titem.left === "string") {
                    left = parseInt(parseFloat(titem.left) * lw / 100);
                }
                if(titem.right){
                	if(typeof titem.right==="string"){
                		var right = parseInt(parseFloat(titem.right) * lw / 100);
                		twidth=right-left;
                    }else{
                    	twidth=titem.right;
                    }
                }
                
                left += ll;
                var $tempEl = $("<" + titem.tag + ">").addClass(titem.cls).css({ "left": left, "top": top });
                if(twidth){
                	$tempEl.css({"width":twidth});
                }
                
                for (var name in titem.attr) {
                    $tempEl.attr(name, titem.attr[name]);
                }
                if (titem.label) {
                    var $tlabel = $("<label>").text(titem.label).addClass("node-label").css({ "left": left, "top": top }).appendTo($el);
                    $tlabel.css({ "margin-left": -parseInt($tlabel[0].offsetWidth / 2), "visibility": "visible" });
                }
                if(titem.content){
                	$tempEl.html(titem.content);
                }
                $el.append($tempEl);
            }
            setTimeout(function () {
                //vthis.maskElement.animate({ top: "-100%" }, 1000, "easeOutBounce");
            }, 500);
        }, _addItem: function (item) {//ADDITEM

        }, _destroy: function () {//DESTROY
            this.element.removeClass("ui-nodeline");
            $(window).unbind("resize", this._resize);
        },
        _setOptions: function (options) {//OVERVIEW SETOPTIONS
            var that = this,
			  resize = false;
            $.each(options, function (key, value) {
                if (key == "cls") {
                    that.element.removeClass(that.options.cls).addClass(value);
                }
                that._setOption(key, value);
                if (key === "height" || key === "source") {
                    resize = true;
                }
            });
            if (resize) {
                this.refresh();
            }
        }
    });
})(jQuery);
