/**
 * Created by zhangyuanyuan on 2014/12/24.
 */

/*
 设置权限的方法
 */
var powerRuleJson={};
var setpower = function (workflowId, nowActiveId,callback) {
    var senddata = {
        workflowId: workflowId,
        activeId: nowActiveId
    };
    $.post(projectName+"/WorkFlowUtilsPower.do?command=pwoer", senddata, function (jsondata) {
        if ($.trim(jsondata) !== "error") {
            var json = $.parseJSON(jsondata);
            powerRuleJson=json;
            if (!$.isEmptyObject(json)) {
                givePower(json);
                callback();
            }
        }
    });
};
//已办箱过来的权限
var setpowerdo = function (workflowId, nowActiveId,callback) {
    var senddata = {
            workflowId: workflowId,
            activeId: nowActiveId
        };
        $.post(projectName+"/WorkFlowUtilsPower.do?command=pwoer", senddata, function (jsondata) {
            if ($.trim(jsondata) !== "error") {
                var json = $.parseJSON(jsondata);
                powerRuleJson=json;
                if (!$.isEmptyObject(json)) {
                    givePowerdo(json);
                    callback();
                }
            }
        });
    };

//给表单权限
function givePower(json) {
    for (var i = 0; i < json.length; i++) {
        var item = json[i], elementName = item.elementName,
            elementType = item.elementType, elementDataType = item.elementDataType,
            elementFlag = item.elementFlag, isread = item.isread, isreadonly = item.isreadonly,
            nameval = "[name=" + elementName + "]", $node = $(nameval);
        switch (elementType) {
            case "text":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "mychosen":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {
                    $node.hide();
                }
                break;
            }
            case "datetime":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "checkbox":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "radio":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "number":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "float":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "uploadactivex":{
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                }
                break;
            }
            case "countersign":{
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1","data-isreadonly":"1"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                }
                break;
            }
            case "officeupload":{
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                        $node.prev().hide();
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                }else{
                    $node.parent().hide();
                }


                break;
            }
            case "defaultadvice":{//默认意见标签
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1","data-isreadonly":"1"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                }
                break;
            }
            case "advice":
            {//意见
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});

                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                }else{
                    $node.hide();
                }
                break;
            }
            case "defaultsign":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "sign":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "endorse":{
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "fileadvice":{
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "uploadread":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    } else {//可以编辑
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "filenumber":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    }else {
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "fileuploadcountersign":{//附件意见会签
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    }else {
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            default :
            {
                break;
            }
        }
    }
};
//已办箱过来的权限
function givePowerdo(json) {
    for (var i = 0; i < json.length; i++) {
        var item = json[i], elementName = item.elementName,
            elementType = item.elementType, elementDataType = item.elementDataType,
            elementFlag = item.elementFlag, isread = item.isread, isreadonly = item.isreadonly,
            nameval = "[name=" + elementName + "]", $node = $(nameval);
        switch (elementType) {
            case "text":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "mychosen":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                    
                } else {
                    $node.hide();
                }
                break;
            }
            case "datetime":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    if (isreadonly == "1") {//设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    }else if(isreadonly == "2"){
                        $node.attr({"data-isread":"1","data-isreadonly":"2"});
                    }
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "checkbox":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "radio":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({disabled: "disabled","data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "number":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "float":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({readonly: true,"data-isread":"1","data-isreadonly":"1"});
                        $node.css({background: "#FFFFFF"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "uploadactivex":{
                if (isread == "1") {//设置为可以查看就是显示
                     //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    
                }
                break;
            }
            case "countersign":{
                if (isread == "1") {//设置为可以查看就是显示
                   //设置为只读
                        $node.attr({"data-isread":"1","data-isreadonly":"1"});
                	}
                
                break;
            }
            case "officeupload":{
                if (isread == "1") {//设置为可以查看就是显示
                   //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                        $node.prev().hide();
                    
                }else{
                    $node.parent().hide();
                }


                break;
            }
            case "defaultadvice":{//默认意见标签
                if (isread == "1") {//设置为可以查看就是显示
                   //设置为只读
                        $node.attr({"data-isread":"1","data-isreadonly":"1"});
                	}
                
                break;
            }
            case "advice":
            {//意见
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});

                   
                }else{
                    $node.hide();
                }
                break;
            }
            case "defaultsign":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "sign":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "endorse":{
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                    
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "fileadvice":{
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                   
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "uploadread":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                   
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "filenumber":
            {
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                   
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            case "fileuploadcountersign":{//附件意见会签
                if (isread == "1") {//设置为可以查看就是显示
                    //设置为只读
                        $node.attr({"data-isread":"1"});
                        $node.attr({"data-isreadonly":"1"});
                   
                } else {//设置为隐藏
                    $node.hide();
                }
                break;
            }
            default :
            {
                break;
            }
        }
    }
};

//给页面元素加权限控制
/**
 * 给元素赋权限的方法
 * @param url  /工程名/workFlow/FileManager/FormPowerSetting.do?command=setFormStylePower
 * @param sendata 要上这种json格式的数据类型 {workflowid:workflowId,activeid:nowActiveId}
 */
var setElementStylePower= function (workflowId,nowActiveId) {
//表单样式权限设置的url
    var sendata={workflowid:workflowId,activeid:nowActiveId};
    $.getJSON(projectName+"/workFlow/FileManager/FormPowerSetting.do?command=setFormStylePower",sendata, function (data) {
        if(!$.isEmptyObject(data)){
            for(var i= 0,len=data.length;i<len;i++){
                var item=data[i],nodesel="[elementstyle="+item.elementName+"]",$node=$(nodesel),
                    eleStyle=item.elementStyle;
                if(eleStyle==1){
                    $node.show();
                }else{
                    $node.hide();
                }
            }
        }

    });
};

