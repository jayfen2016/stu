/**
 * 对GooFlow控件扩展修改
 * @type {{width: number, height: number, toolBtns: string[], haveHead: boolean, headBtns: string[], haveTool: boolean, haveGroup: boolean, useOperStack: boolean}}
 */
var property={
    width:1040,
    height:600,
    toolBtns:["start round","end round","task round","chat","state","plug","complex"],//,"join","fork"  //这个主要控制css ,"node"
    toolApproveType:["","","HDLX-C","HDLX-Q","HDLX-Y","HDLX-W","HDLX-Z",],//审批类型英文显示
    toolApproveTypeName:["","","串行审批","抢占式审批","有序会签","无序会签","子流程",],//审批类型名称
    haveHead:true,
    headBtns:["new","open","save","undo","redo","reload"],//如果haveHead=true，则定义HEAD区的按钮
    haveTool:true,
    haveGroup:false,
    useOperStack:true
};
/**
 * key 为左边控件中 type="cursor" ，为其添加备注
 * @type {{cursor: string, direct: string, start: string, end: string, task: string, node: string, chat: string, state: string, plug: string, fork: string, join: string, complex mix: string, group: string, mutiselect: string}}
 */
var remark={
    cursor:"选择指针",
    direct:"结点连线",
    start:"入口结点",
    "end":"结束结点",
    "task":"串行结点",
   // node:"自动结点",
    chat:"抢占式结点",
    state:"有序结点",
    plug:"无序节点",
  //  fork:"分支结点",
   // "join":"联合结点",
    "complex":"子流程结点",
  //  group:"组织划分框编辑开关",
    mutiselect:"多选择节点"
};
var demo;
$(document).ready(function(){
    demo=$.createGooFlow($("#demo"),property);
    demo.setNodeRemarks(remark);
    //demo.loadData(jsondata);
    //删除确认事件
    demo.onItemDel=function(id,type){
        if(confirm("确定要删除该单元吗?")){
            this.blurItem();
            return true;
        }else{
            return false;
        }
    };
    //删除线和结点 ajax调用
    demo.onItemDeleteCallBack=function(id,type){
         if(type=="node"){
              console.log("node  ajax");
         }else if(type=="line"){
              console.log("line  ajax");
         }
    };

    demo.onItemFocus=function(id,model){
        var obj;
        $("#ele_model").val(model);
        $("#ele_id").val(id);
        if(model=="line"){
            obj=this.$lineData[id];
            $("#ele_from").val(obj.fromNodeName);
            $("#ele_to").val(obj.toNodeName);
            //线内容显示与隐藏
            showLineInfor(obj);
        }else if(model=="node"){
            obj=this.$nodeData[id];
            //结点内容显示与隐藏
            showNodeInfor(obj);
        }
        $("#text_ActivityName").val(obj.name);
        return true;
    };
    demo.onItemBlur=function(id,model){
        document.getElementById("propertyForm").reset();
        return true;
    };
});
/**
 * 控制线信息的显示与隐藏
 * @param obj
 */
var showLineInfor=function(obj){
      $("#ele_from").parent().parent().show();
      $("#ele_to").parent().parent().show();
      $("#text_ActivityName").parent().parent().hide();
      $("#checkbox_isAuto").parent().parent().hide();
      $("#approveType").parent().parent().hide();
      $("#getOperatorType").parent().parent().hide();
}
/**
 *  控制节点信息的显示与隐藏
 */
var showNodeInfor=function(obj){
    approveTypeControl(obj);
    $("#ele_from").parent().parent().hide();
    $("#ele_to").parent().parent().hide();
    $("#text_ActivityName").parent().parent().show();
    $("#checkbox_isAuto").parent().parent().show();
    $("#approveType").parent().parent().show();
    $("#getOperatorType").parent().parent().show();

    if(obj.isSaved){//保存过的信息

        $("#getRoleID option[value='"+obj.roleID+"']").attr("selected", true);
        $("#getOrganizationID option[value='"+obj.organizationID+"']").attr("selected", true);
        $("#objGetType option[value='"+obj.getType+"']").attr("selected", true);
        $("#getOperatorType option[value='"+obj.operatorType+"']").attr("selected", true);
        $("#getFormLabel option[value='"+obj.formLabel+"']").attr("selected", true);
        $("#getActivity option[value='"+obj.selActivityID+"']").attr("selected", true);
        $("#getChildrenWorkFlowID").val(obj.childrenWorkFlowID);

        if(obj.isAuto){
            $("#checkbox_isAuto").attr("checked",true);
        }else{
            $("#checkbox_isAuto").attr("checked",false);
        }

        //操作者类型编号
        var operatorTypeCode = obj.operatorType;
        if(obj.activityType == "HDLX-Z"){
            $("#getChildrenWorkFlowID").show();
        }
        //判断显示框
        if(operatorTypeCode == ""){
            $("#tr_role").hide();
            $("#tr_get").hide();
            $("#tr_bm").hide();
            $("#tr_label").hide();
            $("#tr_ac").hide();
        }else if(operatorTypeCode == "CZZLX-JS"){
            $("#tr_role").show();
            $("#tr_get").hide();
            $("#tr_bm").hide();
            $("#tr_label").hide();
            $("#tr_ac").hide();
        }
        //判断是放为环节获取
        else if(operatorTypeCode == "CZZLX-H"){
            $("#tr_role").hide();
            $("#tr_get").hide();
            $("#tr_bm").hide();
            $("#tr_label").hide();
            $("#tr_ac").show();
        }else if(operatorTypeCode == "CZZLX-BM" || operatorTypeCode == "CZZLX-BMGLR" || operatorTypeCode == "CZZLX-BMFGLR") {
            //获取方式编号
            var getTypeCode = obj.getType;
            //部门编号
            var bmCode = obj.organizationID;
            var formLabel=obj.formLabel;//表单标签
            //判断是否为固定部门
            if ((getTypeCode == "" || getTypeCode == "undefined" || getTypeCode == null) && bmCode != "" && bmCode != "undefined" && bmCode != null) {
                $("#tr_role").hide();
                $("#tr_get").show();
                $("#tr_bm").show();
                $("#tr_label").hide();
                $("#tr_ac").hide();
                $("#objGetType option[value='HQFS-G']").attr("selected", true);
            }
            //判断是放为动态获取部门
            else if (getTypeCode == "HQFS-D") {
                $("#tr_role").hide();
                $("#tr_get").show();
                $("#tr_bm").hide();
                $("#tr_label").hide();
                $("#tr_ac").hide();
            }
            //判断是否表单获取
            else if (getTypeCode == "HQFS-B" && formLabel != "" && formLabel != null) {
                $("#tr_role").hide();
                $("#tr_get").show();
                $("#tr_bm").hide();
                $("#tr_label").show();
                $("#tr_ac").hide();
            }
        }
    }else{//未保存的信息
            $("#tr_role").hide();
            $("#tr_get").hide();
            $("#tr_bm").hide();
            $("#tr_label").hide();
            $("#tr_ac").hide();
            $("#checkbox_isAuto").attr("checked",false);
           //将所有select都未选中
            $("select option ").attr("selected",false);
    }
}

/**
 * 审批类型控制
 * @param obj
 */
var approveTypeControl=function(obj){
    var $td=$("#approveType").parent();
    $td.children().remove();
    if(obj.type=="start round"||obj.type=="end round"){
        var selectHtml="<select id='approveType' name='activityTypeName' onchange='getActivityVal(' aria-invalid='false' class='valid'>"+
            "<option title='串行审批' value='HDLX-C'>串行审批</option>                                                       "+
            "<option title='抢占式审批' value='HDLX-Q' >抢占式审批</option>                                                  "+
                // "<option title='无序会签' value='HDLX-W'>无序会签</option>                                                       "+
                // "<option title='有序会签' value='HDLX-Y'>有序会签</option>                                                       "+
                // "<option title='子流程' value='HDLX-Z'>子流程</option>                                                           "+
            "</select>                                                                                                       ";
        $td.append(selectHtml);
    }else{
        $td.append("<input id='approveType' name='activityTypeName' disabled='disabled'  />");
    }
    $("#approveType").val(obj.activityTypeName);
}
/**
 * 保存当前流程信息
 * 这里保存数据的方法
 */
var saveCurrentInfor=function(){
    //要区分是节点还是线
    var model=$("#ele_model").val();
    if(model=="line"){

    }else if(model=="node"){
        var formData=$("#propertyForm").getFormData();//表单里面的内容
        var singleNodeObject=demo.$nodeData[$("#ele_id").val()];//单个结点对象
        singleNodeObject.isSaved=true;//增加是否保存的属性 false or true
        singleNodeObject.name= $("#text_ActivityName").val();//环节名称
        for(var key in formData){//将表单数据放到单个结点中
            singleNodeObject[key]=formData[key];
        }

        //假如已经画好了节点和线，当修改环节名称，线的起始节点或终止节点需要改变  begin
        var lineIds=demo.$nodeToLineRelaion[$("#ele_id").val()];
         if(lineIds){//如果存在这个节点 并且有关联的线
             if(lineIds.indexOf("#")>-1){
                   var lineIdArray=lineIds.split("#");
                   for(var j in lineIdArray){
                       lineDataFill(lineIdArray[j],singleNodeObject);
                   }
             }else{
                 lineDataFill(lineIds,singleNodeObject);
             }
         }
        //假如已经画好了节点和线，当修改环节名称，线的起始节点或终止节点需要改变  end

        // 当更改环节名称，该空间的 显示的name值也需要更改
        if (demo.$nodeData[$("#ele_id").val()].type.indexOf("round") > 1) {
            demo.$nodeDom[$("#ele_id").val()].children(".span").text(singleNodeObject.name);
        }
        else {
            demo.$nodeDom[$("#ele_id").val()].find("td:eq(1)").text(singleNodeObject.name);
        }
        // 当更改环节名称，该空间的 显示的name值也需要更改

        var allData=JSON.stringify(demo.exportData());//得到整个导出的json数据
        console.log(formData+"---"+singleNodeObject);


    }
}

/**
 *
 * @param lineId  节点id
 * @param singleNodeObject  节点对象
 */
var lineDataFill=function(lineId,singleNodeObject){
    if(demo.$lineData[lineId].from==$("#ele_id").val()){
        demo.$lineData[lineId].fromNodeName=singleNodeObject.name;
    }
    if(demo.$lineData[lineId].to==$("#ele_id").val()){
        demo.$lineData[lineId].toNodeName=singleNodeObject.name;
    }
}