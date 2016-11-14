/* 
 * 
 */
(function($) {
    var FixedHead = function($that, opt) {
        this._el = $that.css({position: "relative"});
        this._headlist = this._el.find(opt.query);
        this.opt={offsetWidth:2,offsetHeight:1,
        titleCls:"sjpz-fixed-header",
    titleContentCls:"sjpz-content-fixed-header"};
     $.extend(this.opt,opt);
        this._fixEl = $("<div>").addClass(this.opt.titleCls).appendTo(this._el);
        this._fixTitleEl = $("<div>").addClass(this.opt.titleContentCls).appendTo(this._fixEl);
        this._fixTitleEl2 = $("<div>").addClass(this.opt.titleContentCls).appendTo(this._fixEl);
        this._el.on("scroll", this, this.scroll);
        // console.log(this);
    };
    FixedHead.prototype.scroll = function(ev) {
        var that = ev.data,
                $needChooseHead = undefined,isShow=true;
        that._headlist.each(function(index, item) {
            if ($(item).position().top < 0) {
                $needChooseHead = $(item);
            }
            if($(item).position().top < 40 && $(item).position().top>0  && index!==0){
            	isShow=false;
            }
        });
        if ($needChooseHead) {
            if (!that._fixOffsetTop) {
                that._fixOffsetTop = that._el.offset().top;//+(ev.target.scrollTop+$needChooseHead.position().top);
                that._fixEl.css("top", that._fixOffsetTop);
                var dcss={
                    "margin-top": Math.ceil(ev.target.scrollTop + $needChooseHead.position().top),
                    "width": $needChooseHead.width()+that.opt.offsetWidth,
                    "font-size": $needChooseHead.css("font-zise"),
                    "font-family": $needChooseHead.css("font-family"),
                    "padding-left": $needChooseHead.css("padding-left"),
                    "padding-right": $needChooseHead.css("padding-right"),
                    "padding-top": $needChooseHead.css("padding-top"),
                    "padding-bottom": $needChooseHead.css("padding-bottom"),
                    // "border": $needChooseHead.css("border"),
                    "height": $needChooseHead.height()+that.opt.offsetHeight,
                    "background-color": $needChooseHead.css("background-color"),
                    "color": $needChooseHead.css("color")
                };
                //console.log(dcss);
                that._fixTitleEl.css(dcss);
            }
            
            that._fixTitleEl.html($needChooseHead.html());
            if(isShow){
            	  that._fixEl.css({"display": "block"});
            }else{
            	  that._fixEl.css({"display": "none"});
            }
          
        } else {
            that._fixEl.css({"display": "none"});
        }
    };
    $.fn.fixedhead = function(opt) {
        return this.each(function(index, item) {
            var $that = $(item);
            if (!$that.data("fixedhead")) {
                $that.data("fixedhead", new FixedHead($that, opt));
            }
        });
    };

})(jQuery);
