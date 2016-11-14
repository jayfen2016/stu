/*! Jquery UI timer line
 *
 *version 1.0 2014-02-26
 *author Qunpeng.Li
 *
 */
(function($) {
	var dateCompare = function(asStartDate, asEndDate) {
		var miStart = typeof asStartDate === "string" ? Date.parse(asStartDate.replace(/\-/g, '/')) : asStartDate.getTime();
		var miEnd = typeof asEndDate === "string" ? Date.parse(asEndDate.replace(/\-/g, '/')) : asEndDate.getTime();
		return (miEnd - miStart) / (1000 * 24 * 3600);
	}, dateFormat = function(date, format) {
		/*
		 * eg:format="yyyy-MM-dd hh:mm:ss";
		 */
		if (!format) {
			format = "yyyy-MM-dd hh:mm:ss";
		}
		var o = {
			"M+" : date.getMonth() + 1, // month
			"d+" : date.getDate(), // day
			"h+" : date.getHours(), // hour
			"m+" : date.getMinutes(), // minute
			"s+" : date.getSeconds(), // second
			"q+" : Math.floor((date.getMonth() + 3) / 3), // quarter
			"S" : date.getMilliseconds()
			// millisecond
		};
		if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		}

		for (var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	};
	$.widget("ui.timerline", {
		version : "1.0",
		defaultElement : "<div>",
		lineElement : null,
		//maskElement: null,
		lineWidth : 0,
		elements : [],
		options : {
			width : 700, //
			height : 160, //
			autowidth : false,
			margin : {
				left : 38,
				right : 38,
				top : 34
			},
			cls : "ui-timerline-panel",
			lineCls : "ui-timerline",
			source : []
		},
		_create : function() {//CREATE
			this.element.addClass(this.options.cls);
			var tthis = this;
			$(window).bind("resize", this, function() {
				if (tthis.options.autowidth) {
					tthis.refresh();
				}
			});
			this.lineElement = $("<div>").addClass(this.options.lineCls).appendTo(this.element).hide();
			//this.maskElement = $("<div>").addClass("mask-panel").appendTo(this.element);
			$(this.element).tooltip({
				items : ".circle,.bottomlabel,.toplabel",
				position : {
					my : "left+15 center",
					at : "right center"
				},
				content : function() {
					var element = $(this);
					if (element.is("[title]")) {
						return element.attr("title");
					}
				}
			}).find(".ui-timerline").tooltip({
				position : {
					my : "center top-50",
					at : "center center"
				},
				content : function() {
					var element = $(this);
					if (element.is("[title]")) {
						return element.attr("title");
					}
				}
			});

		},
		_init : function() {//INIT
			//console.log("_init");
			this.refresh();
		},
		refresh : function() {//REFRESH
			if (this.options.autowidth) {
				this.options.width = this.element.parent().innerWidth();
			}
			this.element.width(this.options.width).height(this.options.height);
			this.lineWidth = this.options.width - this.options.margin.left - this.options.margin.right;
			this.lineElement.css({
				width : this.lineWidth,
				left : this.options.margin.left,
				top : this.options.margin.top
			}).attr("title", "");

			this._clearAll();
			var source = this.options.source;
			if (source.length >= 2) {
				this.lineElement.show();
				var endIndex = source.length - 1, startItem = source[0], endItem = source[endIndex];
				startItem._date = new Date(startItem.date.replace(/-/g, "/"));
				endItem._date = new Date(endItem.date.replace(/-/g, "/"));
				var daycount = dateCompare(startItem._date, endItem._date), operMonth = startItem._date.getMonth(), operYear = startItem._date.getFullYear();
				//console.log(daycount);
				this.lineElement.attr("title", dateFormat(startItem._date, "yyyy年MM月dd日") + " - " + dateFormat(endItem._date, "yyyy年MM月dd日"));
				for (var i in source) {
					var item = source[i];
					if (i == 0) {
						//start
						item.x = 0;
						item.topLabel = item.topLabel ? item.topLabel : dateFormat(item._date, "yyyy-MM-dd");
						item.topLabelCls = "fulldate toplabel";
					} else if (i == endIndex) {
						//end
						item.x = this.lineWidth;
						item.topLabel = item.topLabel ? item.topLabel : dateFormat(item._date, "yyyy-MM-dd");
						item.topLabelCls = "fulldate toplabel";
					} else {
						item._date = new Date(item.date.replace(/-/g, "/"));
						item.x = parseInt((dateCompare(startItem._date, item._date) / daycount) * this.lineWidth, 10);
						item.topLabelCls = "day toplabel";
						var formatStr = "dd";
						if (operYear !== item._date.getFullYear()) {
							formatStr = "yyyy-MM-dd";
							item.topLabelCls = "fulldate toplabel";
						} else if (operMonth !== item._date.getMonth()) {
							formatStr = "MM-dd";
							item.topLabelCls = "monthday toplabel";
							item.topLabelOffTop = 10;
						}
						item.topLabel = item.topLabel ? item.topLabel : dateFormat(item._date, formatStr);

						operYear = item._date.getFullYear();
						operMonth = item._date.getMonth();
						//alert(item.x);
					}
					item.color = item.color ? item.color : "gray";
				    this.elements.push($("<div>").attr("title", item.title).addClass(item.color + " circle").css({
						left : item.x + this.options.margin.left,
						top : this.options.margin.top
					}).appendTo(this.element));
					 this.elements.push($("<label>").attr("title", dateFormat(item._date, "yyyy年MM月dd日")+(item.topLabelLast?item.topLabelLast:"")).text(item.topLabel+(item.topLabelLast?item.topLabelLast:"")).addClass(item.topLabelCls).css({
						bottom : this.options.height - this.options.margin.top + (item.topLabelOffTop ? item.topLabelOffTop : 0),
						left : item.x + this.options.margin.left
					}).appendTo(this.element));
					if (item.bottomLabel) {
						 this.elements.push($("<div>").attr("title", item.title).text(item.bottomLabel).addClass(item.color + " bottomlabel").css({
							left : item.x + this.options.margin.left,
							top : this.options.margin.top
						}).appendTo(this.element));
					}
				}
				//console.log(source);
			}else{
				this.lineElement.hide();
			}

		},
		drawContent : function(source) {

		},
		_destroy : function() {//DESTROY
			this.element.removeClass(this.options.cls);
			//$(window).unbind("resize", this._resize);
		},
		_clearAll : function() {
			for (var i in this.elements) {
				this.elements[i].remove();
			}
			this.elements=[];
		},
		_setOptions : function(options) {//OVERVIEW SETOPTIONS
			var that = this, resize = false;
			$.each(options, function(key, value) {
				if (key == "cls") {
					that.element.removeClass(that.options.cls).addClass(value);
				}
				that._setOption(key, value);
				if (key === "height" || key === "width" || key === "autowidth" || key === "source") {
					resize = true;
				}
			});
			if (resize) {
				this.refresh();
			}
		}
	});
})(jQuery);
