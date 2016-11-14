/**
 *
 */
var resizeFun = function() {
    var $doc = $("body"), allHei = $doc.height(), th = $("#baseTable").outerHeight(), $cdiv = $("#contentDiv"), $tabs = $("#tabs");

    $cdiv.height(allHei - th);
    if ($tabs.length != 0) {
        $tabs.tabs("refresh");
        $cdiv.css("overflow", "hidden");
    } else {
        $cdiv.css("overflow-y", "auto");
    }
};
var updateGS = function($td) {
    var tcode = $.trim($td.parent().find("td:eq(0)").text()), $gstd = $td.next();
    var tary = [], oldFh = {};
    $gstd.find("select").each(function(index, item) {
        //var field= $(item).text();
        oldFh[tcode + (index + 1)] = $(item).val();
    });
    // console.log(oldFh);
    var $allCode = $td.find("label.code"), len = $allCode.length;
    $allCode.each(function(index, item) {
        var text = $(item).text();
        var $select = null;
        if (index == 0) {
            $select = $("<select class='form-control disabled' disabled='disabled' style='width:58px;margin:0 6px;'><option value=''>N/A</option></select>");
        } else {
            $select = $("<select class='form-control' onchange='updateGsLabel(this);' style='width:58px;margin:0 6px;'><option value=''>N/A</option><option value='+'>加</option><option value='-'>减</option></select>");
        }
      //  console.log(text + "  " + oldFh[text]);
        if (oldFh[text]) {
            $select.val(oldFh[text]);
        }
        tary.push($("<div class='y-row'></div>").append($select).append("<label>" + text + "</label>"));
    });
    $gstd.children().remove();
    $gstd.append(tary);
    updateGsLabel($gstd);
};
var updateGsLabel = function(curr) {
    var $td = null, ary = [];
    if ($(curr).is("select")) {
        $td = $(curr).closest("td");
    } else {
        $td = curr;
    }
    var $textGsTD = $td.parent().next().find("td:eq(1)");
    ary.push($.trim($td.parent().find("td:eq(0)").text()) + " = ");

    $td.find(".y-row").each(function(index, item) {
        if (index == 0) {
            ary.push($(item).find("label").text());
        } else {
            var val = $(item).find("select").val();
            if (val) {
                ary.push(val);
                ary.push($(item).find("label").text());
            }
        }
    });
    $textGsTD.text(ary.join(''));
};
var removeRow = function(el) {
    var $el = $(el), $row = $el.closest(".e-row"), $td = $row.parent();
    if ($td.find(".e-row").length > 1) {
        $row.parent().next().find(".y-row").eq($row.index()).remove();
        $row.hide().remove();
        updateCode($td);
        updateGS($td);
    }
};
var changeType = function(el) {
    var $el = $(el), val = $el.val(), $row = $el.closest(".e-row"), $fids = $row.find("[fid]").css("display", "none"), $operator = $fids.filter("[fid=operator]");

    if (val != "") {
        if (val === "fix") {
            $row.find("[fid=fix]").css("display", "inline-block");
            var fixVal = $row.find("[fid=fix]").find("input").val();
			if(fixVal === "") {
				$row.find("[fid=fix]").find("input").val(0);
			}
        } else {
            $fids.filter("[fid=" + val + "]").css("display", "inline-block");
            $operator.css("display", "inline-block");
        }
    }
};

var initRow = function($row) {
    $row.find("[data-xtype=pick]").pick();
    var $td = $row.parent();
    updateCode($td);
    updateGS($td);
};
var updateCode = function($td) {
    var pcode = $.trim($td.parent().find("td:eq(0)").text());
    $td.find(".e-row .code").each(function(index, item) {
        $(item).text(pcode + (index + 1));
    });
};

var getTaxTypeObj = function(obj, type) {
    var tobj = null;
    for (var i = 0, len = obj.list.length; i < len; i++) {
        var item = obj.list[i];
        if (item.type == type) {
            tobj = item;
            break;
        }
    }
    if (!tobj) {
        tobj = {
            "type": type,
            list: []
        };
        obj.list.push(tobj);
    }
    return tobj;
};
var expRowsData = function(cobj, $table) {
    //var cobj=cobj;
    //$.extend(cobj,getBaseInfo($table));
    var list = cobj.list, infoTrs = [], baseTrs = [];
    $table.find("tbody tr").each(function(index, item) {
        var $tr = $(item);
        if ($tr.is(".info")) {
            // infoTrs.push($tr);
            var id = $tr.find("input[name=id]").val(), name = $.trim($(item).find("td:eq(1)").text()), gsStr = $.trim($tr.next().find("td:eq(1)").text()), gs = [], source = [], code = $tr.data("code"), enname = $tr.data("enname");
            //source
            $tr.find("td:eq(2) .e-row").each(function(tidex, titem) {
                var data = $(titem).getFormData();
                data.code = $(titem).find("label.code").text();
                source.push(data);
            });
            //gs
            $tr.find("td:eq(3) select").each(function(va, vc) {
                if (va !== 0) {
                    gs.push($(vc).val());
                }
            });
            cobj.list.push({
                id: id,
                name: name,
                gsStr: gsStr,
                gs: gs,
                source: source,
                enname: enname,
                code: code
            });
        } else {
            baseTrs.push(item);
        }
    });
    $.extend(cobj, $(baseTrs).getFormData());
};
var getData = function() {
    var baseId = $("#baseId").val(), obj = {
        baseId: baseId,
        list: []
    };
    var tables = $("table[data-type]").each(function(index, item) {
        var $table = $(item), data = $table.data(), relTobj = getTaxTypeObj(obj, data.type);
        var name = $.trim($table.find("thead th.title b").text());
        var $hasCheckBox = $table.find("thead th.title input[type=checkbox]");
        if($hasCheckBox.length > 0) {
        	if($hasCheckBox.prop("checked")==false) {
        		return true;
        	}
        }
        var cobj = {
            type: data.taxtype,
            name: name,
            list: []
        };
        expRowsData(cobj, $table);
        relTobj.list.push(cobj);
    });
    return obj;
};
var impRowsData = function(cobj, $table) {
    var baseTrs = [], infoTrs = [];
    $table.find("tbody tr").each(function(index, item) {
        var $tr = $(item);
        if ($tr.is(".info")) {
            infoTrs.push(item);
        } else {
            baseTrs.push(item);
        }
    });

    for (var i = 0, len = cobj.list.length; i < len; i++) {
        var item = cobj.list[i], $tr = $(infoTrs).filter("[data-code=" + item.code + "]"), $etd = $tr.find("td:eq(2)"), $gsStrTd = $tr.next().find("td:eq(1)"), $gsTd = $tr.find("td:eq(3)"), tempEl = "#tempRow";
        if ($tr.find(".fa-plus").data("tempel")) {
            tempEl = $tr.find(".fa-plus").data("tempel");
        }
        for (var j = 0, jlen = item.source.length; j < jlen; j++) {
            var edata = item.source[j], $trow = null;
            if (j === 0) {
                $trow = $etd.find(".e-row");
            } else {
                $trow = $(tempEl).clone().removeAttr("id").css("display", "block");
                $etd.append($trow);
                initRow($trow);
            }
            $trow.setFormData(edata);
            $trow.find("[name=type]").trigger("change");
        }
        $gsTd.find("select:not([disabled])").each(function(idx, select) {
            $(select).val(item.gs[idx]);
        });
        $gsStrTd.text(item.gsStr);
    }
    $(baseTrs).setFormData(cobj);
};
var fillData = function(data) {
    $("#baseId").val(data.baseId);
    for (var i = 0, ilen = data.list.length; i < ilen; i++) {
        var typeItem = data.list[i], type = typeItem.type, $typeTable = $("table[data-type=" + type + "]");
        for (var j = 0, jlen = typeItem.list.length; j < jlen; j++) {
            var taxtypeItem = typeItem.list[j], taxtype = taxtypeItem.type, $taxtypeTable = $typeTable.filter("[data-taxtype=" + taxtype + "]");
            $taxtypeTable.find("thead th.title input[type=checkbox]").attr("checked","checked");
            impRowsData(taxtypeItem, $taxtypeTable);
        }
    }
};

var saveData = function(callback) {
    var ca = callback,
    data=getData(),
    result=checkRepeat(data);
    if(result.length>0){
    	//console.log(result);
    	var strHtml=[];
    	for(var i=0,len=result.length;i<len;i++){
    		strHtml.push((i+1)+"."+result[i]+"<br/>");
    	}
        window.dialog("<div style='margin:0px 12px 12px 12px;'><span>"+strHtml.join("")+"</span></div>",{
    	show : 'blind',
    	width:500,
		modal:true,
		title : "有配置错误,是否要继续保存?",
		buttons : {
			"确认" : function() {
				window.top.$(this).dialog("close");
				POST(saveUrl, data, function(data) {
			        if (ca) {
			            ca();
			        }
			    });
			},
			"取消" : function() {
				window.top.$(this).dialog("close");
			}
		}
    	});
    
    	return;
    }
    POST(saveUrl, data, function(data) {
        if (ca) {
            ca();
        }
    });
};



var checkRepeat = function(data) {
    var result = [];
   for(var i=0,len=data.list.length;i<len;i++){
	   for(j=0,jlen=data.list[i].list.length;j<jlen;j++){
		   var jitem=data.list[i].list[j],
		   jname=jitem.name;
		  // console.log(jitem);
		   if(jitem["consolidateTaxFilingType"]==="1"){
			   if(jitem["consolidateTaxFilingTo"]==="" || jitem["consolidateTaxFilingTo"]==="N/A"){
				   result.push(jname+"&gt; 汇缴到父未配置!");
			   }
		   }
		   if(jitem["taxPaymentTaxBU"]!==undefined && jitem["taxPaymentTaxBU"]===""){
			   result.push(jname+"&gt; 缴税税务局未配置!");
		   }
		   if(jitem["taxPaymentWay"]!==undefined && jitem["taxPaymentWay"]===""){
			   result.push(jname+"&gt; 缴款方式未配置!");
		   }
		   
		   for(var b=0,blen=jitem.list.length;b<blen;b++){
			   var bitem=jitem.list[b],
			   gs=bitem.gs,
			   source=bitem.source,
			   name=bitem.name,
			   code=bitem.code;
			   if(jitem.tmisCodeAdditiveTax && jitem.tmisCodeAdditiveTax !=="" && bitem.enname==="TMISCode_BelongTaxBizType")
				  	continue;
			  for(var p=0,plen=gs.length;p<plen;p++){
			    if(!gs[p]){
			    	 result.push(jname+"&gt; "+name+"("+code+(p+2)+")"+" &gt; 运算逻辑符号未选择!");
			    }
			  }
				  
			   
			   for(var c=0,clen=source.length;c<clen;c++){
				   var citem=source[c],
				   ctype=citem.type,
				   ccode=citem.code,
				   operator=citem.operator,
				   constant=citem.constant,
				   fix=citem.fix;
				   if(!ctype){
					   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 未配置");
					   continue;
				   }
				   var valueItem=citem[ctype];
				   if($.isArray(valueItem)){
					    if(valueItem.length===0){
					    	 result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 数据来源未配置");
					    	 continue;
					    }
				   }else{
					   if(!valueItem){
						   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 数据来源未配置");
					    	 continue;
					   }
				   }
				   if(ctype==="fix" && !$.isNumeric(fix)){
					   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 固定值输入非数字");
				   }
				   if(constant!=="" && operator!==""&& !$.isNumeric(constant)){
					   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 运算值输入非数字");
				   }
				   if(constant!=="" && operator===""){
					   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 在填写了运算数字的情况下,未选择运算符号");
				   }
				   if(operator!=="" && constant===""){
					   result.push(jname+"&gt; "+name+"("+ccode+")"+" &gt;"+" 只选择了运算符号,没有填写运算数字");
				   }
			   }
			   //constant: "1212"fix: ""jde: Array[1]operator: ""
		   }
		   
	   }
	   
   }
    return result;
};
var buildOtherRow = function(data, $el) {
    var $erow = $el.closest(".e-row"), $td = $erow.closest("td"),
            $a = $td.prev().find("i.fa-plus");
    for (var i = 0, len = data.length; i < len; i++) {
        var tdata = $erow.getFormData(),
                valObj = {type: tdata.type};
        valObj[tdata.type] = data[i];
        var tempel = $a.data("tempel"),
                $td = $a.closest("tr").find("td:eq(2)"),
                $trow = $(tempel ? tempel : "#tempRow").clone().removeAttr("id").css("display", "block");
        $erow.after($trow);
        initRow($trow);
        $trow.setFormData(valObj);
        $trow.find("[name=type]").trigger("change");
    }
};

var pickBeforSelect = function(ev, ui) {
    //DEBUG
    // ui.beforVal = [{text: "f", value: "f1"}, {text: "s", value: "s"}, {text: "t", value: "t"}];
    //DEBUG
    if (ui.beforVal.length > 1) {
        var otherAry = [];
        for (var i = 1, len = ui.beforVal.length; i < len; i++) {
            otherAry.push(ui.beforVal[i]);
        }
        ui.beforVal = [ui.beforVal[0]];
        buildOtherRow(otherAry, ui.element);
    }
};

$(function(){
	
	$("input[type=checkbox][name=isHas]").each(function(index){
		$(this).attr("tindex",index+1);
	});
	$("#contentDiv").on("click",".sjpz-fixed-header input[name=isHas]",function(){
        		var $that=$(this),
        		   checked=$that.prop("checked"),
        		   tindex=$that.attr("tindex");
        		$(this).closest("#contentDiv").find("input[name=isHas][tindex="+tindex+"]").removeAttr("checked").attr("checked",checked).prop("checked",checked);
	});
});

