;/**!
 * jQuery Common Function
 * 2014-02-27 / *
 * author Qunpeng.Li
 ***/
var DEBUG=true;
var getDc=function(){
    return (new Date()).getTime();
};
var getDcUrl=function(url){
    return  url.indexOf("?")===-1?url+"?dc="+getDc():url+"&dc="+getDc();
};

var userAgent = navigator.userAgent.toLowerCase();
// Figure out what browser is being used
jQuery.browser = {
    version: (userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
    safari: /webkit/.test(userAgent),
    opera: /opera/.test(userAgent),
    msie: /msie/.test(userAgent) && !/opera/.test(userAgent),
    mozilla: /mozilla/.test(userAgent) && !/(compatible|webkit)/.test(userAgent)
};
function toDecimal2( x )
{
    var f = parseFloat( x ) ;
    if( isNaN( f ) )
    {
        return x ;
    }
    var f = Math.round( x * 100 ) / 100 ;
    var s = f.toString() ;
    var rs = s.indexOf( '.' ) ;
    if( rs < 0 )
    {
        rs = s.length ;
        s += '.' ;
    }
    while( s.length <= rs + 2 )
    {
        s += '0' ;
    }
    return s ;
}
function fmoney( s, n )
{
    n = n > 0 && n <= 20 ? n : 2 ;
    s = parseFloat( ( s + "" ).replace( /[^\d\.-]/g, "" ) ).toFixed( n ) + "" ;
    var l = s.split( "." )[0].split( "" ).reverse(), r = s.split( "." )[1] ;
    t = "" ;
    for( var i = 0; i < l.length; i ++ )
    {
        t += l[i] + ( ( i + 1 ) % 3 == 0 && ( i + 1 ) != l.length ? "," : "" ) ;
    }
    t = t.split( "" ).reverse().join( "" ) + "." + r ;
    return t.replace( '-,', '-' ) ;
}
(function($) {
    /*
     * Window Console
     */
    if (!window.console) {
        window.console = {};
    }
    var cfuns = ["log", "time", "timeEnd", "error"];
    for (var i in cfuns) {
        if (!window.console[cfuns[i]]) {
            window.console[cfuns[i]] = function() {
            };
        }
    }
    /*
     * Lock form controls and buttons
     * callback:function
     */
    $.fn.formMask = function(callback) {
        if (!this.is("form")) {
            console.log("非表单不能处理");
            return this;
        }
        if (this.data("ismask")) {
            console.log("不需要重复处理");
            return this;
        }
        this.find("input[type=text],input[type=password],input[type=checkbox],input[type=file],input[type=radio],select").not(".disabled").each(function(index, item) {
            $(item).data("rn_oper", true).attr("disabled", true);
            if (!$(item).hasClass("disabled")) {
                $(item).addClass("disabled");
            }
        });
        this.find("input[type=submit],input[type=reset],input[type=button],button").not(".disabled").each(function(index, item) {
            $(item).data("rn_oper", true).attr("disabled", true);
            if (!$(item).hasClass("disabled")) {
                $(item).addClass("disabled");
            }
        });
        this.data("ismask", true);
        if (callback !== undefined) {
            callback();
        }
        return this;
    };
    /*
     * Unlock form controls and buttons
     * callback:function
     */
    $.fn.unformMask = function(callback) {
        if (!this.is("form")) {
            console.log("非表单不能处理");
            return this;
        }
        if (!this.data("ismask")) {
            console.log("没有可清除的对象");
            return this;
        }
        this.find("input[type=text],input[type=password],input[type=checkbox],input[type=file],input[type=radio],select").each(function(index, item) {
            var $item = $(item);
            if ($item.data("rn_oper")) {
                $item.removeAttr("disabled");
                $item.removeData("rn_oper");
                if ($item.hasClass("disabled")) {
                    $item.removeClass("disabled");
                }
            }
        });
        this.find("input[type=submit],input[type=reset],input[type=button],button").each(function(index, item) {
            var $item = $(item);
            if ($item.data("rn_oper")) {
                $item.removeAttr("disabled");
                $item.removeData("rn_oper");
                if ($item.hasClass("disabled")) {
                    $item.removeClass("disabled");
                }
            }
        });
        this.removeData("ismask");
        if (callback !== undefined) {
            callback();
        }
        return this;
    };

    $.fn.mask = function(title) {
        if (this.is("div") || this.is("body")) {
            if (this.data("ismask")) {
                this.find(".ui-mask div:last").text(title);
                return this;
            }

            //var h = this.outerHeight(),
            //    w = this.outerWidth();
            var html = ["<div class='ui-mask' style='width:100%; height:100%; position:absolute;z-index:100;left:0px; bottom:0px;right:0px; top:0px; opacity:0;'>", "<div class='ui-widget-overlay'></div>", "<div style='color:white;font-size: 15px;font-weight: bold;font-family: 微软雅黑;position:absolute;left:50%;top:50%; margin-left:-130px;width:260px;'>", title ? title : "", "</div>", "</div>"].join("");
            var $thtml = $(html);

            this.append($thtml);
            $thtml.fadeIn('fast', function() {//'slow'
                $(this).fadeTo(400, 1);
            });
            this.data("ismask", true);
        } else {
            console.log("非容器元素不能处理");
            return this;
        }
        return this;
    };
    $.fn.unMask = function() {
        this.find(".ui-mask").fadeOut(200, 0, function() {
            $(this).remove();
        });
        this.removeData("ismask");
        return this;
    };
    $.fn.getFormData = function() {
        var obj = {};
        this.find("input:not([type=button]):not([type=radio]),select,textarea,input[type=radio]:checked,input[type=checkbox]").each(function(index, item) {
            var $item = $(item), data = $item.data(), xtype = data.xtype, label = $item.attr("name") || $item.attr("id"), val = null;
            if (label) {
                if (data.xtype) {
                    switch (data.xtype.toLowerCase()) {
                        case "ajaxchosen":
                        case "chosen":
                            val = $item.data("chosen").selectedItem();
                            break;
                        case "upload":
                            val = data.fileItems();
                            break;
                        case "pick":
                            val= $item.pick("getValue");
                            break;
                        default:
                            val = $item.val();
                            break;
                    }
                } else {
                    if(item.type==="checkbox"){
                        val=item.checked?true:false;
                    }else{
                        val = $item.val();
                    }

                }
                obj[label] = val;
            }
        });
        return obj;
    };
    $.fn.getDtypeFormData=function(){
        var formElements= $(this).find("input[data-dtype]:not([type=button]):not([type=radio]),select[data-xtype],textarea[data-dtype],input[data-dtype][type=radio]:checked,input[data-dtype][type=checkbox]"),
            objArr=[];
        for(var index= 0,length=formElements.length;index<length;index++){
            var item=formElements[index], $item=$(item),
                data=$item.data(),dtype=data.dtype,xtype=data.xtype,
                isread=$item.attr("data-isread"),isreadonly=$item.attr("data-isreadonly"),
                label=$item.attr("name") || $item.attr("id"), val = null,obj={};
            if(typeof xtype ==="undefined"){
                xtype="text";
            }
            if (label && dtype) {
                if (xtype) {
                    xtype=xtype.toLowerCase();
                    xtype= $.trim(xtype);
                    switch (xtype) {
                        case ("ajaxchosen"):{}
                        case ("chosen"):{
                            var selectItemData= $item.data("chosen").selectedItem();
                            val= selectItemData ? selectItemData.value:"";
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case "officeupload":{//正文标签
                            var uploadnode=$item.next().find("input[data-xtype=upload]"),
                                officeuploadval=$item.val(),
                                officeids=[];
                            if(uploadnode && uploadnode.data() &&uploadnode.data().fileItems()){
                                var officeuploaddata = uploadnode.data().fileItems();
                                for(var m= 0,officelen=officeuploaddata.length;m<officelen;m++){
                                    var officeuploaditemfile=officeuploaddata[m];
                                    officeids.push(officeuploaditemfile.id);
                                }
                                var officefileids=officeids.join(",");
                                val={
                                    officeid:officeuploadval?officeuploadval:"",
                                    uploadfileid:(officeids && officefileids) ? officefileids:""
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case ("uploadactivex"):{//上传控件
                            var uploadActivexId=$item.val(),
                                uploadids="",ids=[];
                            if($item.next().find("input[data-xtype=upload]").eq(0) && $item.next().find("input[data-xtype=upload]").eq(0).data() && $item.next().find("input[data-xtype=upload]").eq(0).data().fileItems()){
                                var uploaddata=$item.next().find("input[data-xtype=upload]").eq(0).data().fileItems();
                                for(var v= 0,len=uploaddata.length;v<len;v++){
                                    var uploaditemfile=uploaddata[v];
                                    ids.push(uploaditemfile.id);
                                }
                                if(ids && ids.length>0){
                                    uploadids=ids.join(",");
                                }
                                val={
                                    uploadactivexId:uploadActivexId,//上传控件的id
                                    uploadids:uploadids //上传多个文件的id
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case ("countersign"):{//会签意见
                            var countersignId=$item.val(),
                                $contextdiv=$item.next().find("div").eq(0);
                            if($contextdiv){
                                var  adviceId=$contextdiv.find("input[name=countersign]").val(),
                                    adviceContext=$contextdiv.find("input[name=set-countersign]").val();
                                val={
                                    countersign:countersignId,//保存在业务表中的数据(bussinessId)
                                    adviceId:adviceId,//保存在意见表中的
                                    AdviceContext:adviceContext,
                                    isread:isread,//是否显示
                                    isreadonly:isreadonly//是否只读
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case ("pick"):{
                            val= $item.pick("getValue");
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case ("defaultadvice"):{//默认意见标签
                            var defaultadviceId=$item.val(),
                                $contextdiv=$item.next().find("div").eq(0);
                            if($contextdiv){
                                var  adviceId=$contextdiv.find("input[name=defaultadvice]").val(),
                                    adviceContext=$contextdiv.find("input[name=set-defaultadvice]").val();
                                val={
                                	defaultadvice:defaultadviceId,//保存在业务表中的数据(bussinessId)
                                    adviceId:adviceId,//保存在意见表中的
                                    AdviceContext:adviceContext,
                                    isread:isread,//是否显示
                                    isreadonly:isreadonly//是否只读
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case ("advice"):{//意见标签
                            var hiddenval= $item.val(),
                                $contextdiv=$item.next();
                            /*selval= $contextdiv.find("select").val(),
                                advicetype=$contextdiv.find("select").attr("tempflag"),
                                selectdiv= $contextdiv.find("select").next(),
                                checkedids= selectdiv.find("input[type=hidden]").val(),
                                checkednames=selectdiv.find("textarea").val();*/
                           var selval= "",
                               advicetype=$contextdiv.find("button").attr("tempflag"),
                               selectdiv= $contextdiv.find("button").parent(),
                               checkedids= selectdiv.find("input[type=hidden]").val(),
                               checkednames=selectdiv.find("textarea").val();
                            val={
                                adviceid:hiddenval,
                                advicetype:advicetype==="user"?"1":"2",//1 是人员 2 是部门
                                advicetemptid:selval,
                                advicecheckedids:checkedids,
                                adviceContext:checkednames
                            };
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case ("defaultsign"):{//默认签字
                        	 var $div=$item.next(), $contextspan=$div.find("span");
                             val={
                                 adviceid:$item.val(),
                                 CreateTime:$contextspan.eq(0).find("input[name=savenowtime]").val(),
                                 CreatePerson:$contextspan.eq(1).text()
                             };
                             obj={
                                 name:label,
                                 value:val,
                                 dtype:dtype,
                                 xtype:xtype
                             };
                             objArr.push(obj);
                             break;
                        }
                        case ("sign"):{//签字标签
                            var $div=$item.next(), $contextspan=$div.find("span");
                            val={
                                adviceid:$item.val(),
                                CreateTime:$contextspan.eq(0).find("input[name=savenowtime]").val(),
                                CreatePerson:$contextspan.eq(1).text()
                            };
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case ("endorse"):{//签署意见
                            var $div=$item.next(),$contextspan=$div.find("span");
                            val={
                                adviceid:$item.val(),
                                AdviceContext:$div.find("textarea").val(),
                                CreateTime:$div.find("input[name=savenowtime]").val(),
                                CreatePerson:$contextspan.eq(0).text(),
                                isread:isread,
                                isreadonly:isreadonly
                            };
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case ("fileadvice"):{//附件意见标签
                            var fileids=[];
                            if($item.next() && $item.next().find("input[data-xtype=upload]").data() && $item.next().find("input[data-xtype=upload]").data().fileItems()){
                                var $div=$item.next(),$messdiv=$div.find(".fileAdvice-message"),
                                    uploadval=$div.find("input[data-xtype=upload]").data().fileItems(),
                                    advicecontext=$div.find("textarea").val();//意见内容
                                for(var w= 0,len=uploadval.length;w<len;w++){
                                    var uploaditem=uploadval[w];
                                    fileids.push(uploaditem.id);
                                }
                                val={
                                    adviceid:$item.val(),
                                    AdviceContext:advicecontext,
                                    FileID:fileids.join(","),
                                    date:$messdiv.find("span[name=date]").text(),
                                    dept:$messdiv.find("span[name=dept]").text(),
                                    user:$messdiv.find("span[name=user]").text(),
                                    isread:isread,
                                    isreadonly:isreadonly
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case "filenumber":{
                            val=$item.next().find("input").val();
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        case "uploadread":{//收文阅件
                            var plusval=$item.val(),
                                $alltr= $item.next().find("table").eq(0).find("tbody tr"),
                                uploadfileids=[];
                            if(typeof $alltr[0] !=="undefined"){
                                for(var i= 0,len=$alltr.length;i<len;i++){
                                    var itemtr=$alltr[i],
                                        $itemtr=$(itemtr);
                                    uploadfileids.push($itemtr.attr("id"));
                                }
                                val={
                                    id:plusval,//hidden 即控件的id
                                    uploadids:uploadfileids //上传的所有文件的id
                                };
                                obj={
                                    name:label,
                                    value:val,
                                    dtype:dtype,
                                    xtype:xtype
                                };
                                objArr.push(obj);
                            }
                            break;
                        }
                        case "fileuploadcountersign":{//附件意见会签标签
                            var plusval=$item.val(),
                                $parentDiv= $item.next(),fileId=[],
                                $saveCountent=$parentDiv.find("input[name=countersigncontent]:eq(0)"),
                                $uploadInput=$parentDiv.find("div[name=uploadactivex]").find("input[data-xtype=upload]");

                            if($uploadInput.length > 0) {
                                var alluploadData = $uploadInput.data().fileItems();
                                if (alluploadData.length > 0) {
                                    for (var i = 0, len = alluploadData.length; i < len; i++) {
                                        var itemData = alluploadData[i];
                                        fileId.push(itemData.id);
                                    }
                                }

                            }
                            val={
                                businessId: plusval,//业务id
                                AdviceContext:$saveCountent.val(),//意见内容
                                uploadFileId:(fileId.length > 0) ? fileId.join(","):null,//上传文件的所有id
                                isread:isread,
                                isreadonly:isreadonly
                            };
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                        }
                        default:
                            val = $item.val();
                            obj={
                                name:label,
                                value:val,
                                dtype:dtype,
                                xtype:xtype
                            };
                            objArr.push(obj);
                            break;
                    }

                } else {
                    if(item.type==="checkbox"){
                        val=item.checked?true:false;//如果要是checkbox的话，选中就是true，否则就是flase
                        obj={
                            name:label,
                            value:val,
                            dtype:dtype,
                            xtype:xtype
                        };
                        objArr.push(obj);
                    }else{
                        val = $item.val();
                        obj={
                            name:label,
                            value:val,
                            dtype:dtype,
                            xtype:xtype
                        };
                        objArr.push(obj);
                    }
                }

            }
        }
        return objArr;
    };

    $.fn.allSelect=function(){
        return this.each(function(index,item){
            $(item).bind("change",function(el){
                var $this= $(this),$trs=$this.closest("table").find("tbody tr"),checked=this.checked;
                $trs.each(function(tidx,titem){
                    var $ipu=$(titem).find("input[type=checkbox]:eq(0)");
                    if($ipu.length>0){
                        $ipu[0].checked=checked;
                    }
                });
            });
        });
    };

    $.fn.setFormData = function(formdata,flag)
    {
        this.find("input:not([type=button]),select,textarea,label[name]").each(function(index, item)
        {
            var $item = $(item), data = $item.data(),
                xtype = data.xtype, isread=$item.attr("data-isread"),isreadonly=$item.attr("data-isreadonly"),
                label = $item.attr("name") || $item.attr("id"), val = null;
            if (label)
            {
                if(flag){
                    isreadonly="1";
                }
                val = formdata[label];
                if (val) {
                    if (xtype) {
                        switch (xtype.toLowerCase())
                        {
                            case "ajaxchosen":
                            case "chosen":{
                                val = $item.data( "chosen" ).selectedItem( val+"" ) ;
                                break;
                            }
                            case "mychosen":{
                                val = $item.data( "chosen" ).selectedItem( val+"" ) ;
                                break ;
                            }
                            case "uploadactivex":
                            {
                                if(val && !$.isEmptyObject(val)){
                                    if(val.id){
                                        $item.val(val.id);
                                    }
                                    if(val.files){
                                        var uploadfiles=val.files;
                                        $item.next().find("input[data-xtype=upload]").data().fileItems(uploadfiles);
                                    }
                                }
                                break;
                            }
                            case "countersign":{//会签意见
                                var $parentDiv=$item.next();
                                if(val){
                                    $parentDiv.find("div:not(:eq(0))").remove();
                                    if(val.countersign) $item.val(val.countersign);
                                    if(val.data && !$.isEmptyObject(val.data)){
                                        var userdata=val.data,htmlArr=[],html;
                                        for(var countNum= 0,len=userdata.length;countNum<len;countNum++){
                                            var oUData=userdata[countNum];
                                            html="<div><input type='hidden' name='countersign' value='"+oUData.id+"'/>" +"<span style='display: block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;width: 300px;margin-left: 10px;'>"+oUData.context+"</span>"+
                                                "<span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'>"+
                                                oUData.name +"</span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'>"+
                                                dateFormat(new Date(oUData.time),'yyyy-MM-dd') +"</span></div>";
                                            htmlArr.push(html);
                                        }
                                        $parentDiv.append(htmlArr.join(""));
                                    }
                                }
                                break;
                            }
                            case "officeupload":
                            {
                                if(val){
                                    if(val.officeid){
                                        $item.val(val.officeid);
                                    }
                                    if(val.uploadfile){
                                        var $officeuploadInput=$item.next().find("input[data-xtype=upload]");
                                        $officeuploadInput.data().fileItems(val.uploadfile);
                                        $officeuploadInput.prev().hide();
                                    }
                                    if(val.isred==1){
                                        redshoworhide($item,true);
                                    }else{
                                        redshoworhide($item,false);
                                    }
                                }
                                break;
                            }
                            case "radio":{
                                if ( $item.removeAttr( "checked" ).val() == val + "" )
                                {
                                    $item.attr( "checked", true )[0].checked = true ;
                                }
                                break;
                            }
                            case "pick":{
                                $item.pick( "setValue", val ) ;
                                break;
                            }
                            case "defaultadvice":{//默认意见标签
                                var $parentDiv=$item.next();
                                if(val){
                                    $parentDiv.find("div:not(:eq(0))").remove();
                                    if(val.defaultadvice) $item.val(val.defaultadvice);
                                    if(val.data && !$.isEmptyObject(val.data)){
                                        var userdata=val.data,htmlArr=[],html;
                                        for(var countNum= 0,len=userdata.length;countNum<len;countNum++){
                                            var oUData=userdata[countNum];
                                            html="<div><input type='hidden' name='defaultadvice' value='"+oUData.id+"'/>" +"<span style='display: block;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;width: 300px;margin-left: 10px;'>"+oUData.context+"</span>"+
                                                "<span name='set-name' style='display: inline-block;size: 14px;margin-left: 10px;'>"+
                                                oUData.name +"</span><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'>"+
                                                dateFormat(new Date(oUData.time),'yyyy-MM-dd') +"</span></div>";
                                            htmlArr.push(html);
                                        }
                                        $parentDiv.append(htmlArr.join(""));
                                    }
                                }
                                break;
                            }
                            case "advice":{//意见标签
                                var advicejson=formdata[label],
                                    $sel="select[name=sel-"+label+"]",$ids="input[name=ids-"+label+"]",
                                    $names="textarea[name=names-"+label+"]",$nextdiv=$item.next();
                                $item.val(advicejson.id);
                               if($nextdiv.find($names).val() == ""||$nextdiv.find($names).val()==null){
                                	$nextdiv.find($sel).val(advicejson.sel);
                                    $nextdiv.find($ids).val((advicejson.ids)?advicejson.ids.join(","):"");
                                    $nextdiv.find($names).val((advicejson.names)?advicejson.names.join(","):"");
                                }
                                break;
                            }
                            case "defaultsign":{//默认签字
                                var $nextdiv=$item.next(),
                                    signjson=formdata[label],
                                    savenowtime="input[name=savenowtime]",
                                    showname="span[name=showname]",showdate="span[name=showdate]",
                                    time= signjson.time, hmstime,yMdtime;
                                $item.val(signjson.id);
                                if(time!=null && time!=""){
                                    hmstime=dateFormat(new Date(time),'yyyy-MM-dd hh:mm:ss');
                                    yMdtime=dateFormat(new Date(time),'yyyy-MM-dd');
                                }
                               if(signjson.name != "" && signjson.name != null){
                            	   $nextdiv.find(savenowtime).val(hmstime);
                                   $nextdiv.find(showdate).text(yMdtime);
                                   $nextdiv.find(showname).text(signjson.name);
                               }
                                break;
                            }
                            case "sign":{//签字标签
                                var $nextdiv=$item.next(),
                                    signjson=formdata[label],
                                    savenowtime="input[name=savenowtime]",
                                    showname="span[name=showname]",showdate="span[name=showdate]",
                                    time= signjson.time, hmstime,yMdtime;
                                $item.val(signjson.id);
                                if(time!=null && time!=""){
                                    hmstime=dateFormat(new Date(time),'yyyy-MM-dd hh:mm:ss');
                                    yMdtime=dateFormat(new Date(time),'yyyy-MM-dd');
                                }
                                $nextdiv.find(savenowtime).val(hmstime);
                                $nextdiv.find(showdate).text(yMdtime);
                                $nextdiv.find(showname).text(signjson.name);
                                break;
                            }
                            case "endorse":{//签署意见
                                var $nextdiv=$item.next(),$endorsetextarea=$nextdiv.find("textarea"),
                                    $span=$nextdiv.find("span"),timeinput=$nextdiv.find("input"),
                                    shownamespan=$span.eq(0),showtimespan=$span.eq(1),
                                    signjson=formdata[label];
                                $item.val(signjson.id);
                                $endorsetextarea.val(signjson.advicecontext);
                                var time=signjson.time,getdatetime,setname=signjson.name,
                                    showtime,savetime;
                                if(time && time !=null && time !=""){
                                    getdatetime=new Date(time);
                                    showtime=dateFormat(getdatetime,'yyyy-MM-dd');
                                    savetime=dateFormat(getdatetime,'yyyy-MM-dd hh:mm:ss');
                                    showtimespan.text(showtime);
                                    timeinput.val(savetime);
                                }
                                if(setname && setname !=null && setname !=""){
                                    shownamespan.text(setname);
                                }
                                break;
                            }
                            case "fileadvice":{
                                var $nextdiv=$item.next(),jsondata=formdata[label],
                                    messdiv=$nextdiv.find(".fileAdvice-message"),
                                    adviceContext=jsondata.AdviceContext,
                                    date=jsondata.date,
                                    dept=jsondata.dept,
                                    user=jsondata.user,
                                    fileIDs=jsondata.FileID;
                                $item.val(jsondata.id);
                                if(date && dept && user){
                                    messdiv.find("span[name=date]").text(dateFormat(new Date(date),'yyyy-MM-dd'));
                                    messdiv.find("span[name=dept]").text(dept);
                                    messdiv.find("span[name=user]").text(user);
                                }
                                $nextdiv.find("textarea").val(adviceContext);
                                if($nextdiv.find("input[data-xtype=upload]").data()){
                                    $nextdiv.find("input[data-xtype=upload]").data().fileItems(fileIDs);
                                }
                                break;
                            }
                            case "filenumber":{
                                $item.next().find("input").val(val);
                                break;
                            }
                            case "uploadread":{
                                var $nextdiv=$item.next(),
                                    $paramDiv=$nextdiv.find("div[class=saveuploadMessDiv]").eq(0),
                                    ids=val.uploadids;//所有上传文件的id
                                $item.val(val.id);
                                if(isread==='1' && isreadonly==='2'){//显示
                                    setUploadDataInToTable($paramDiv,ids,true);
                                }else if(isread==='1' && isreadonly==='1'){//只读
                                    setUploadDataInToTable($paramDiv,ids);
                                }
                                break;
                            }
                            case "fileuploadcountersign":{//附件意见会签标签
                                var $nextdiv=$item.next(),contentData,
                                    $contentDiv=$nextdiv.find("div[class=fileuploadPsersonMess]");
                                $item.val(val.id);
                                if(  val.content ){
                                    $contentDiv.empty();
                                    contentData=val.content;
                                    for(var i= 0,len=contentData.length;i<len;i++){
                                        var item=contentData[i],
                                            adviceContext=item.adviceContext,
                                            files=item.files,
                                            time=dateFormat(new Date(item.time),'yyyy-MM-dd'),
                                            dept=item.dept,
                                            name=item.name,
                                            uploadActivex,
                                            html="<div>"+adviceContext+"</div><div name='uploadActivex'><input data-xtype='upload' data-appendto='div[name=fileListTD]' type='file' name='uploadActivex-upload'  style='width:255px;' data-button-text='请上传文件'/><div name='fileListTD'></div></div>" +
                                                "<div class='personMes'><span name='time' style='display: inline-block;size: 14px;margin-left: 10px;'>"+time+"</span><span name='dept' style='display: inline-block;size: 14px;margin-left: 10px;'>"+dept+"</span><span name='name' style='display: inline-block;size: 14px;margin-left: 10px;'>"+name+"</span></div>";
                                        $contentDiv.append(html);
                                        uploadActivex=$contentDiv.find("div[name=uploadActivex]").find("input[data-xtype=upload]");
                                        uploadFile(uploadActivex);
                                        if(uploadActivex.length > 0){
                                            var uploadActivexObject=uploadActivex.data();
                                            uploadActivexObject.fileItems(files);
                                            uploadActivexObject.onlyView(true);
                                        }
                                    }
                                }

                                break;
                            }
                            default:
                            {
                                if( $item.attr( "data-format" ) )
                                {
                                    var formatValue = $item.attr( "data-format" ) ;
                                    if( formatValue == "0.00;#" )
                                    {
                                        $item.val( toDecimal2( val ) ) ;
                                    }
                                    else if( formatValue == "0.######;#" )
                                    {
                                        $item.val( parseFloat( val ) ) ;
                                    }
                                    else if( formatValue == "0;#" )
                                    {
                                        $item.val( parseInt( val ) ) ;
                                    }
                                }
                                else
                                {
                                    $item.val( val ) ;
                                }
                                break;
                            }
                        }
                    }
                    else{
                        if( $item.is("[type=radio]") )
                        {
                            if ( $item.removeAttr( "checked" ).val() == val + "" )
                            {
                                $item.attr( "checked", true )[0].checked = true ;
                            }
                        }
                        else if( $item.is( "[type=checkbox]" ) )
                        {
                            $item[0].checked = val ;
                        }
                        else if( $item.is( "label" ) )
                        {
                            if( $item.attr( "data-format" ) )
                            {
                                var formatValue = $item.attr( "data-format" ) ;
                                if( formatValue == "0.00;#" )
                                {
                                    $item.text( toDecimal2( val ) ) ;
                                }
                                else if( formatValue == "0.######;#" )
                                {
                                    $item.text( parseFloat( val ) ) ;
                                }
                                else if( formatValue == "0;#" )
                                {
                                    $item.text( parseInt( val ) ) ;
                                }
                                else if( formatValue == "#,##0.00;#" )
                                {
                                    $item.text( fmoney( val, 2 ) ) ;
                                }
                            }
                            else
                            {
                                $item.text( val ) ;
                            }
                        }
                        else
                        {
                            if( $item.attr( "data-format" ) )
                            {
                                var formatValue = $item.attr( "data-format" ) ;
                                if( formatValue == "0.00;#" )
                                {
                                    $item.val( toDecimal2( val ) ) ;
                                }
                                else if( formatValue == "0.######;#" )
                                {
                                    $item.val( parseFloat( val ) ) ;
                                }
                                else if( formatValue == "0;#" )
                                {
                                    $item.val( parseInt( val ) ) ;
                                }
                            }
                            else
                            {
                                $item.val( val ) ;
                            }
                        }

                    }

                }

            }
        });
        return this;
    };
    $.fn.configFormField=function(fieldConfig){
        for(var fieldName in fieldConfig){
            var config=fieldConfig[fieldName];
            this.find("select[name="+fieldName+"]").each(function(index,item){
                var $item=$(item),data=$item.data(),xtype=data.xtype;
                switch(xtype){
                    case "ajaxchosen":
                        data.chosen._clearOptions(1);
                        data.chosen._addOptions(config);
                        break;
                    case "chosen":
                        data.chosen._clearOptions(1);
                        data.chosen._addOptions(config);
                        break;
                    default:
                        break;
                }
            });
        }
    };
    $.fn.resetHasXTypeForm = function(data) {
        if (this.is("form")) {
            if (this.resetForm) {
                this.resetForm();
                this.find("input[type=hidden]").val('');
            };
        }
        this.find("[data-xtype]").each(function(index, item) {
            var $item = $(item), xtype = $item.data("xtype");
            //console.log(xtype);
            switch (xtype) {
                case "ajaxchosen":
                    var cdata=$item.data("chosen");
                    if(cdata.is_multiple){
                        cdata._clearOptions();
                    }else{
                        cdata._clearOptions(1);
                    }
                    break;
                case "chosen":
                    $item.data("chosen").selectedItem("");
                    break;
                case "upload":
                    $item.data("fileItems")([]);
                    break;
                case "pick":
                    $item.pick("setValue", []);
                    break;
            }
        });

        if (data) {
            this.setFormData(data);
        }

        this.find("em.valid,em.error,div.valid,div.error,span.error,span.valid").hide();
        return this;
    };

})(jQuery);
