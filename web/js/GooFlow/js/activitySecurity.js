var workflowId = getUrlParam("workflowId"),
    loadUrl = projectName+"/oa/activitySecurityList.do?command=load",
    deleteUrl = projectName+"/oa/activitySecurityList.do?command=delete",
    saveUrl = projectName+"/oa/activitySecurityList.do?command=submit&workflowid="+workflowId,
    listUrl = projectName+"/oa/activitySecurityList.do?command=search&workflowid="+workflowId,
    loadRoleUrl = projectName+"/platform/user.do?command=initRoles",
    loadOrganizationUrl = projectName+"/oa/activitySecurityList.do?command=loadOrganization",
  //  loadActivityTypeUrl = projectName+"/oa/activitySecurityList.do?command=loadActivityType",
    loadOperatorTypeUrl = projectName+"/oa/activitySecurityList.do?command=loadOperatorType",
    loadGetTypeUrl = projectName+"/oa/activitySecurityList.do?command=loadGetType",
    loadFormPowerUrl=projectName+"/workFlow/FileManager/FormPowerSetting.do?command=loadFormPower&workFlowId="+workflowId,//加载表单字段信息
    loadformworkFlowUrl=projectName+"/oa/activitySecurityList.do?command=loadActivityByWorkFlow&workflowId="+workflowId,//加载业务环节
    loadWorkFlowUrl = projectName+"/workFlow/FileManager/ProcessList.do?command=loadWorkFlow";
$(function() {

    $.getJSON(loadRoleUrl,null,
        function (data) {
            var inputAry = "<option></option>";
            for (var i in data) {
                var tda = data[i],
                    el = "<option title='"+tda.text+"' value='"+tda.value+"'>"+ tda.text+"</option>";
                inputAry += el;
            }

            $("#getRoleID").append(inputAry);
        });
    $.getJSON(loadOrganizationUrl,
        function (data) {
            var inputAry = "<option></option>";
            for (var i in data.info) {
                var tda = data.info[i],
                    el = "<option title='"+tda.organizationName+"' value='"+tda.id+"'>"+ tda.organizationName+"</option>";
                inputAry += el;
            }

            $("#getOrganizationID").append(inputAry);
        });

/*    $.getJSON(loadActivityTypeUrl,
        function (data) {
            var inputAry = "<option></option>";
            for (var i in data.info) {
                var tda = data.info[i],
                    el = "<option title='"+tda.label+"' value='"+tda.dictionaryCode+"'>"+ tda.label+"</option>";
                inputAry += el;
            }
            $("#getActivityType").append(inputAry);
        });*/

    $.getJSON(loadOperatorTypeUrl,
        function (data) {
            var inputAry = "<option></option>";
            for (var i in data.info) {
                var tda = data.info[i],
                    el = "<option title='"+tda.label+"' value='"+tda.dictionaryCode+"'>"+ tda.label+"</option>";
                inputAry += el;
            }
            $("#getOperatorType").append(inputAry);
        });

    $.getJSON(loadGetTypeUrl,
        function (data) {
            var inputAry = "<option></option>";
            for (var i in data.info) {
                var tda = data.info[i],
                    el = "<option title='"+tda.label+"' value='"+tda.dictionaryCode+"'>"+ tda.label+"</option>";
                inputAry += el;
            }
            $("#objGetType").append(inputAry);
        });

    $.getJSON(loadFormPowerUrl,
        function (data) {
            if(!$.isEmptyObject(data)){
                var folwFormColumlist = data.folwFormColumlist;
                var inputAry = "<option></option>";
                for (var i in folwFormColumlist) {
                    var tda = folwFormColumlist[i],
                        el = "<option title='"+tda.formElementChineseName+"' value='"+tda.id+"'>"+ tda.formElementChineseName+"</option>";
                    inputAry += el;
                }
                $("#getFormLabel").append(inputAry);
            }
        });
    $.getJSON(loadformworkFlowUrl,
        function (data) {
            if(!$.isEmptyObject(data)){

                var inputAry = "<option></option>";
                var dataArr=data.info;
                if(dataArr !=null && dataArr.length>0){
                    for(var i=0;i<dataArr.length;i++){
                        var item=dataArr[i],
                            html="<option title='"+item.activityName+"' value='"+item.id+"'>"+item.activityName+"</option>";
                        inputAry+=html;
                    }
                }
                $("#getActivity").append(inputAry);

            }
        });

    $.getJSON(loadWorkFlowUrl,
        function (data) {
            if(!$.isEmptyObject(data)){

                var inputAry = "<option value=''>请选择</option>";
                var dataArr=data.info;
                if(dataArr !=null && dataArr.length>0){
                    for(var i=0;i<dataArr.length;i++){
                        var item=dataArr[i],
                            html="<option value='"+item.id+"'>"+item.workFlowName+"</option>";
                        inputAry+=html;
                    }
                }
                $("#getChildrenWorkFlowID").append(inputAry);
            }
        });


    $("#tr_role").hide();
    $("#tr_get").hide();
    $("#tr_bm").hide();
    $("#tr_label").hide();
    $("#tr_ac").hide();
    //end workflow info by lidong
});

/**
 * 操作者类型
 */
function getOperatorTypeVal(){
    var operatorType = $("#getOperatorType").val();
    if(operatorType == "CZZLX-BM" || operatorType == "CZZLX-BMGLR" || operatorType == "CZZLX-BMFGLR"){
        $("#tr_role").hide();
        $("#tr_get").show();
        $("#tr_bm").hide();
        $("#tr_label").hide();
        $("#tr_ac").hide();

        $("#getRoleID").val("");
        $("#getFormLabel").val("");
        $("#getActivity").val("");
        $("#objGetType").val("");
        $("#getOrganizationID").val("");
    }else if(operatorType == "CZZLX-JS"){
        $("#tr_role").show();
        $("#tr_get").hide();
        $("#tr_bm").hide();
        $("#tr_label").hide();
        $("#tr_ac").hide();

        $("#objGetType").val("");
        $("#getFormLabel").val("");
        $("#getActivity").val("");
        $("#getOrganizationID").val("");
    }else if(operatorType == "CZZLX-H"){

        $("#tr_role").hide();
        $("#tr_get").hide();
        $("#tr_bm").hide();
        $("#tr_label").hide();
        $("#tr_ac").show();

        $("#getRoleID").val("");
        $("#getFormLabel").val("");
        $("#objGetType").val("");
        $("#getOrganizationID").val("");
    }else{
        $("#tr_role").hide();
        $("#tr_get").hide();
        $("#tr_bm").hide();
        $("#tr_label").hide();
        $("#tr_ac").hide();

        $("#getRoleID").val("");
        $("#objGetType").val("");
        $("#getFormLabel").val("");
        $("#getActivity").val("");
        $("#getOrganizationID").val("");
    }
}
/**
 * 审批类型选择
 */
/*function getActivityVal(){
    var getActivityType = $("#getActivityType").val();
    if(getActivityType == "HDLX-Z"){
        $("#tr_childrenWorkFlowID_ac").show();
    }
}*/
/**
 * 获取方式
 */
function getTypeVal(){
    var getType = $("#objGetType").val();
    if(getType == "HQFS-G"){
        $("#tr_bm").show();
        $("#tr_label").hide();
        $("#tr_ac").hide();

        $("#getFormLabel").val("");
        $("#getActivity").val("");
    }else if(getType == "HQFS-B"){
        $("#tr_bm").hide();
        $("#tr_label").show();
        $("#tr_ac").hide();

        $("#getOrganizationID").val("");
        $("#getActivity").val("");
    }else{
        $("#tr_bm").hide();
        $("#tr_label").hide();
        $("#tr_ac").hide();

        $("#getOrganizationID").val("");
        $("#getFormLabel").val("");
        $("#getActivity").val("");
    }
}