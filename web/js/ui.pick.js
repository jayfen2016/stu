/*! Jquery UI pick
 *
 *version 1.0 2014-02-26
 *author Qunpeng.Li
 *
 */
(function($) {
	if(!$.generateId){
		$._widgetIdx=0;
		$.generateId=function(fg){
			$._widgetIdx++;
			return (fg?fg:"widget_")+$._widgetIdx;
		};
	}
	
	$.widget("ui.pick", {
		version : "1.0",
		contentElement : null, //内容区域元素
		triggerIconElement : null,
		_oldWidth : 0,
		textElement : null,
		selectedItems:[],
		options : {
			cls : "ui-pick-panel",
			triggerIconClass : "trigger-icon",
			textClass : 'text-panel',
			change : null, //Events
			url : null,
			dialogwidth : 300,
			dialogheight : 500,
			width : 300,
                        beforSelect:null,//Event
			disabled:false,
			onlyvalue:false
		},
		_create : function() {//CREATE
			var $el = this.element, data = $el.hide().data();
			var id=$el.attr("id");
			if(!id){
				$el.attr("id",$.generateId("uipick_"));
			}
			this.options.url = this.options.url || data.url;
			$.extend(this.options,data,JSON.parseForObjectText(data.opt));
			this.options.width=$el.outerWidth()-2;
			this.contentElement = $("<div>").addClass(this.options.cls).insertBefore($el).width(this.options.width).click(this, this._click);
                        this.triggerIconElement = $("<div>").addClass(this.options.triggerIconClass).appendTo(this.contentElement);
			this.textElement = $("<div>").addClass(this.options.textClass).appendTo(this.contentElement);
			//$el.before(this.contentElement);
			if(this.element.attr("disabled")){
				this.disabled();
			}
			if(data.text && data.value){
				this.setValue([{text:data.text,value:data.value}]);
			}
		},
		_updateLabel:function(){
			var labelAry=[];
			for (var i=0; i < this.selectedItems.length; i++) {
			   var item= this.selectedItems[i];
			   labelAry.push(item.text);
			};
			var text=labelAry.join(';');
			this.textElement.text(text).attr("title",text);
		},
		setValue:function(value){
			var val;
			if(!value){
				val=[];
			}else if(typeof value==="string"){
				var ary= value.split(","),tary=[];
				for(var i=0,len=ary.length;i<len;i++){
					var tv=ary[i];
					tary.push({text:tv,value:tv});
				}
				val=tary;
			}else if(value.length!==undefined){
				val=value;
			}else{
				val=[value];
			}
                        this.selectedItems=val;
			this._updateLabel();
		},
		getValue:function(){
			if(this.options.onlyvalue){
				if(this.selectedItems.length>0){
					var r=[];
					for(var i in this.selectedItems){
						r.push(this.selectedItems[i].value);
					}
					return r.join(",");
				}else{
					return "";
				}
			}else{
				return this.selectedItems;
			}
		},
		enabled:function(){
			this.contentElement.removeClass("disabled");
			this.options.disabled=false;
			this.element.attr("disabled",false);
		},
		disabled:function(){
			if(!this.contentElement.hasClass("disabled")){
				this.contentElement.addClass("disabled");
				this.options.disabled=true;
			    this.element.attr("disabled",true);
			}
		},
		_click : function(ev) {
			var $this = ev.data;
			if($this.options.disabled){return;}
			if (window.frameDialog) {
				(function($) {
					//console.log($this.selectedItems);
					window.frameDialog(DEBUG?getDcUrl($this.options.url):$this.options.url, $this.options.title, {
						width : $this.options.dialogwidth,
						height : $this.options.dialogheight,
						data:$this.selectedItems,
						buttons : {
							"确认" : function() {
								var $vt=$(this),
								tf= $vt.find("iframe")[0].contentWindow;
								if(tf.getValue){
                                                                        $this.beforVal=tf.getValue();
                                                                        $this._trigger("beforSelect", new jQuery.Event(),   $this);
									$this.setValue.call($this, $this.beforVal);
								}
								$vt.dialog("close");
							},
							"清除" : function() {
								var $vt=$(this),
								tf= $vt.find("iframe")[0].contentWindow;
								if(tf.clearValue){
									tf.clearValue();
								}
								$this.setValue.call($this,[]);
							},
							"取消" : function() {
								$(this).dialog("close");
							}
						},close : function() {
				if ($(this).dialog("option","mode") === "full") {
					$(window.top).unbind("resize", ev_resize);
				}
				$(this).dialog("destroy");
				if($this.element.valid && $this.element.data("validate")){
					$this.element.valid();
				}
			}
					});
				})(window.top.$);
			}
		}
	});
})(jQuery);
