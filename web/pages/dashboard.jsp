<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
 
<link href="../theme/default/portal.css" rel="stylesheet" type="text/css"/>
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<link href='../theme/default/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='../theme/default/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<script type="text/javascript" src="../js/page.common.js"></script>
<script type="text/javascript" src='../js/fullcalendar/moment.min.js'></script>
<script type="text/javascript" src='../js/fullcalendar/fullcalendar.js'></script>
<script type="text/javascript" src='../js/fullcalendar/zh-cn.js'></script>
<style type="text/css">
   .meetingNoticeMore{
	 display: inline-block;
	 padding: 0px 10px;
	 font-size: 9px;
	 color: black;
	}
	.left02{
		height:20%;
	}
	.right01{
		height:auto;
	}
	
	.fc-view-container td{
		border-color: Silver;
	}
	.ui-widget-header .ui-widget-header{
		border-color: Silver;
	}
	.table01 .td_right{
		 width: 220px;
	}
	.right{
		 width: 100%;
		 background-size:100%;
	}
	.fc-basic-view tbody .fc-row {
 		 min-height: 10px;
    }
	.right01_nr table td {
		 height: 2px;
		 line-height: 15px;
	}
	body{
		background: #FFFFFF;
	}
	.customBack{  
	  border: 1px solid #3babe3;
	  background: #ffffff;
	  font-weight: bold;
	  color: #3babe3;
	  font-size: 1.2em;
	  line-height: 2;
	  cursor: pointer;
	  float:left;
     }
  
   .right02_nr input {
	  width: 127px;
	  line-height: 28px;
	  border: 1px solid #e2e5e7;
	  float: left;
	  color: #666666;
	 }
	 
	.fc-toolbar button{
	  	  border: 1px solid #3babe3;
		  background: #ffffff;
		  color: #3babe3;
		  line-height: 2;
		  cursor: pointer;
		  float:left;
	} 
	.fc-toolbar button:hover{
	 	color: #fff;
	 	background: #3babe3;
	}
	
	.fc-view-container table th{
		background: #ecebda url(../theme/default/images/ui-bg_layered-circles_100_ecebda_13x13.png) 50% 50% repeat;
	}
	.fc-view-container table td{
		background: #ffffff url(../theme/default/images/ui-bg_flat_100_ffffff_40x100.png) 50% 50% repeat-x;
	}
	
/* 	.announce{
		overflow:hidden;
		word-break:keep-all;
		white-space:nowrap;
		text-overflow:ellipsis;
		width:528px;
		height:20px;
		font-size:12px;
		} */
</style>
<script type="text/javascript">
     var loadMeetingNoticeUrl="../meetingMangement/meetingNoticeAccepter/meetingNoticeAwaitingSignedRecordForAccepter.do?command=load",
         loadAnnouncementUrl="../msgBoard/msgBoardList.do?command=searchUnreadTopicByUserId",
         loadPublishedAnnouncementsUrl="../announcement/announcementList.do?command=loadAllPublishedAnnouncements",
         loadMyPersonalScheduleUrl="../schedule/personalSchedule.do?command=searchPersonalSchedule",
         loadContactsUrl="../communication/communicationUnitList.do?command=searchContacts",
         loadCCListUrl="<%=request.getContextPath()%>/oa/CCBox.do?command=indexCCList";
     
         
     var viewMeetingNoticeItem = function( id ,showType,attendUserId,organizationId,organizationName)
	 {
	   frameDialog("meetingMangement/meetingNoticeAccepter/meetingNoticeBasicInfoForAccepter.do?meetingId=" + id+"&updateFlag=1"+"&showType="+showType+"&attendUserId="+attendUserId+"&currentOrgId="+organizationId+"&currentOrgName="+organizationName, "会议通知内容",
	    {
	        mode    : "full",
	        buttons : [
	            {text:"确定",icons:{ primary: "ui-icon-check"},click:function(ev){
	             		             var $this=window.top.$(this),dial= $this.find("iframe")[0].contentWindow;
	             		                  dial.saveData(function(){
	                		            	 setTimeout(function(){ $this.dialog("close");},200);
	                		            	 /* window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                		            	 window.parent.location.reload(); */
	                		            	 loadMeetingNotice();
	                		            	}); 
	                		            	
	            		            	}
	            		            }
	        ]
	    });
	 };
	
	var viewAnnouncementItem = function(id)
	 {
	   frameDialog("announcement/announcementView.do?id=" + id, "通知公告",
	    {
	        mode    : "full",
	        buttons : [
	            {text:"返回",icons:{ primary: "ui-icon-check"},click:function(ev){
	                		            	   var $this = window.top.$( this ) ;
	                		            	   $this.dialog( "close" ) ;
	            		            	}
	            		            }
	        ]
	    });
	 };
	 
	 //查看我个人日程中的某个日程详情
     var viewMyPersonalSchedule=function(id){
        var checkUrl="schedule/personalSchedule/viewSchedule.do?command=init&id="+id;	  
		  frameDialog(checkUrl, "我的日程详情", {mode:"middle",width:480,height:500,buttons:[         
            { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
               {
                      var $this = window.top.$( this ) ;
                      $this.dialog( "close" ) ;
                  }}
             ]});
     };
	
     var dealAgencyTaskOfMeeting=function(id,actionUrl,meetingId){
       var taskUrl=actionUrl+"&meetId="+meetingId+"&taskId="+id;  
       frameDialog(taskUrl, "会议冲突详情", {mode:"full",buttons:[
		                 { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		                          {
		                	 		  window.location.href="/ZZ_OA/pages/dashboard.jsp";
         	                  		   window.parent.location.reload();
         	                  		/*var $this = window.top.$( this ) ;
		                              $this.dialog( "close" ) ; */
		                             
		                           }}
		                  ]});
     };
     
      var dealAgencyTaskOfWorkflow=function(id,actionUrl,meetingId){
        var editUrl=actionUrl+"&id="+meetingId;  
		      frameDialog(editUrl, "公文审批", {mode:"full",buttons:[
		                 { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		                          {
		                              var $this = window.top.$( this ) ;
		                               $this.dialog( "close" ) ;
		                               loadAgencyMissions();
		                           }}
		                  ]});
     };
     //首页对未处理的资产操作  
     var dealAgencyTaskOfAssetMaintenance = function(id,actionUrl,meetingId){
     	var addTeachUrl= actionUrl+"&id="+id+"&meetingId="+meetingId;
     	//var addTeachUrl = "propertyManage/advancedQuery/assetOper.do?id=" + meetingId;
 			frameDialog(
 					addTeachUrl,
 					"资产处理",
 					{
 						mode : "middle",
 						width : 400,
 						height : 380,
 						buttons : [

 								{
 									text : "保存",
 									icons : {
 										primary : "ui-icon-check"
 									},
 									click : function(ev) {
 										var $this = window.top.$(this), dial = $this.find("iframe")[0].contentWindow;
 										var data = dial.getData();
 										
 										if (data.verification) {
 											var saveTeachUrl = "../propertyManage/advancedQuery/assetOper.do?command=submit";
 											data.id = id;
 											console.log(data);
 											POST(saveTeachUrl, data,
 													function(data) {
 														$this.dialog("close");
 														loadAgencyMissions();
 												});
 										}
 									}
 								}, {
 									text : "返回",
 									icons : {
 										primary : "ui-icon-cancel"
 									},
 									click : function(ev) {
 										var $this = window.top.$(this);
 										$this.dialog("close");
 									}
 								} ]
 					});
 		};   
	 //加载
     var loadMeetingNotice=function(){
        var child=$("#meetingNoticeId").children();
        if(child.length>0){
          $("#meetingNoticeId").empty();
        }
        POST(loadMeetingNoticeUrl,null, function(data){
           var $ulObj=$("#meetingNoticeId");
           var $spanObj=$("#meetingNoticeMoreId");
            for(var i=0;i<data.length;i++){
               if(i>2){
                break;
              }
              var obj=data[i];
              var name = obj.MeetingName.length>20?(obj.MeetingName.substr(0, 20)+"..."):obj.MeetingName;
              var liStr="<li title='"+obj.MeetingName+"'><a onclick=viewMeetingNoticeItem(\""+obj.id+"\",\""+obj.showType+"\",\""+obj.attendUserId+"\",\""+obj.organizationId+"\",\""+obj.organizationName+"\");>"+name+"</a><img src='../theme/default/images/new02.gif'></li>";
              $ulObj.append(liStr);
            }
            $spanObj.empty();
           if(data.length>0){
              $spanObj.append("<a style='display:inline-block;border:none;padding:0px 10px;font-size:9px;color:black;' onclick='showMeetingNotices();'>更多>></a>");
           }else{
              $spanObj.append("<a style='display:inline-block;border:none;padding:0px 10px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
           }
        });
     };
     
     //加载已发布的留言通知
     var loadAllPublishedAnnouncements=function(){
       POST(loadAnnouncementUrl,null, function(data){
    	   console.log(data);
            var $ulObj=$("#announcementId");
            var $announcementObj=$("#moreAnnouncementsId");
            
            for(var i=0;i<data.length;i++){
              if(i>4){
                break;
              }
              var obj = data[i];
              var content=obj.content.length>40?(obj.content.substr(0,40)+"..."):obj.content;
              var liStr = "<li><a style='padding-left:5px;height:25px;' onclick=showAnnouncementLists(\""+obj.id+"\");>" + obj.creator.chineseName + " : " +content+ "</a><img src='../theme/default/images/new02.gif'></li>";
              
              $ulObj.append(liStr);
            }
        });
     };
     
     //加载我的个人日程信息
     var loadMyPersonalSchedule=function(){
        $.getJSON("../schedule/personalSchedule.do?command=searchPersonalScheduleForDashBoard",
               function (data) {
                 var myPersonalSchedule=$("#myPersonalSchedule");
                 var count=0;
                 var validScheduleCount=0;
                 for(var i=0;i<data.length;i++){
                   var obj=data[i];
                   if(((obj.startDateForDashboard<=obj.todayDate) && (obj.todayDate<=obj.endDateForDashboard)) || (obj.todayDate<obj.startDateForDashboard)){
                     validScheduleCount++;
                   }
                 }
                 for(var i=0;i<data.length;i++){
                   var obj=data[i];
                   // var divStr="<div class='rc01' style='margin-top:10px;height:20px;'> <div class='rc01_l' style='height:20px;'><p>"+obj.startTimeForDashboard+"</p></div>  <div style='margin-left:35px;'><a style='width:100px;height:20px;line-height:20px;background-color:#E8FFC4;' onclick='viewMyPersonalSchedule(\""+obj.id+"\");'>"+obj.title+"</a></div><div class='clear'></div></div>";
                   var divStr;
                   if(((obj.startDateForDashboard<=obj.todayDate) && (obj.todayDate<=obj.endDateForDashboard))){
                      divStr="<div class='rc01' style='padding-bottom: 0px;margin-top:10px;height:20px;'> <div class='rc01_l' style='height:20px;'><p>今天</p></div>  <div style='margin-left:35px;'><a style='width:100px;height:20px;line-height:20px;background-color:#E8FFC4;word-break:break-all;' onclick='viewMyPersonalSchedule(\""+obj.id+"\");'>"+obj.title+"</a></div><div class='clear'></div></div>";
                      count++;
                    }else if(obj.todayDate<obj.startDateForDashboard){
                      divStr="<div class='rc01' style='margin-top:10px;height:20px;'> <div class='rc01_l' style='height:20px;'><p>"+obj.startDateForShow+"</p></div>  <div style='margin-left:35px;'><a style='width:100px;height:20px;line-height:20px;background-color:#E8FFC4;' onclick='viewMyPersonalSchedule(\""+obj.id+"\");'>"+obj.title+"</a></div><div class='clear'></div></div>";
                      count++;
                    }
                    myPersonalSchedule.append(divStr);
                    if(count==3){
                      break;
                    } 
                 }
                 $("#myScheduleNums").text(data.length);
                 if(validScheduleCount>count){
                    myPersonalSchedule.append(" <a onclick='goMySchedulePage();' class='other' style='margin-top:10px;height:20px;line-height:20px;'>其他"+(validScheduleCount-count)+"个日程</a>");
                 }else{
                  myPersonalSchedule.append(" <a onclick='goMySchedulePage();' class='other' style='margin-top:10px;height:20px;line-height:20px;'>其他0个日程</a>");
                 }
               });
     };
     
     //加载待办事宜信息(信息来源于任务中心)
     var loadAgencyMissions=function(){
        if($("#agencyMissionId").children().length>0){
           $("#agencyMissionId").empty();
        }
        $.getJSON("../taskManagement/taskCenter/taskCenterAgencyMissionList.do?command=selectAllAgencyMissions",
               function (data) {
                 var $agencyMissionUl=$("#agencyMissionId");
                 var $moreagencyMissions=$("#moreagencyMissions");
                 $("#toDoCount").html("<a onclick='showAgencyMissions();' style='text-decoration:none;color:#e20000;display:inline-block;border-bottom:0px;padding:0px 0px;font-size:12px;'>"+(data.length)+"</a>");
                 for(var i=0;i<data.length;i++){
	               if(i==3){
	                break;
	              }
                   var obj=data[i];
                   var liStr="";
                   if("TaskCenter_MeetingConflict"==obj.taskKindCode){
	                liStr="<li><a onclick=dealAgencyTaskOfMeeting(\""+obj.id+"\",\""+obj.actionUrl+"\",\"" + obj.exfield + "\");>. "+ obj.title+"有冲突</a></li>";
	               }else if("TaskCenter_Workflow"==obj.taskKindCode){
	                liStr="<li><a onclick=dealAgencyTaskOfWorkflow(\""+obj.id+"\",\""+obj.actionUrl+"\",\"" + obj.exfield + "\");>. "+ obj.title+"</a></li>";
	               }else if("TaskCenter_AssetMaintenance"==obj.taskKindCode){
	                liStr="<li><a onclick=dealAgencyTaskOfAssetMaintenance(\""+obj.clientTxSeq+"\",\""+obj.actionUrl+"\",\"" + obj.exfield + "\");>. "+ obj.title+"</a></li>";
	               }
	               $agencyMissionUl.append(liStr);
                 }
                 $moreagencyMissions.empty();
                 if(data.length>0){
                   $moreagencyMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:black;' onclick='showAgencyMissions();'>更多>></a>");
                 }else{
                   $moreagencyMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
                 }
               });
       };
       
     //显示更多待办事宜
     var showAgencyMissions=function(){
        frameDialog("taskManagement/taskCenter/taskCenterAgencyMissionList.do", "待办事宜", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		            window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                window.parent.location.reload();
		        }}
		     ]});
     };
     
     //显示更多全文检索
     var showSearch = function(){
        frameDialog("solrManagement/solrSearch.do", "全文检索", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		            window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                window.parent.location.reload();
		        }}
		     ]});
     };
     
   //add by lidong at 2015-03-23 
     //加载抄送list信息
       var pageSize = 5; //设置页面显示的条数
       var loadCCList=function(){
    	  var senddata = {};
          $.post(loadCCListUrl+"&page=1&rows="+pageSize,senddata,function (data) {
        	  var jsondata = $.parseJSON(data);
        	  var getdata = jsondata.infoList,
        	  records = jsondata.records,
        	  $ccul=$("#ccul"),
        	  $ccMissions = $("#ccMissions");
        	  if(records=="undefined" || records==null){
        		  records = "0";
        	  }
        	  $("#ccCount").html("<a onclick='showCCListMissions();' style='text-decoration:none;color:#e20000;display:inline-block;border-bottom:0px;padding:0px 0px;font-size:12px;'>"+(records)+"</a>");
        	  	if(getdata != null){
	               for(var i=0;i<getdata.length;i++){
	                 var obj=getdata[i];
	                 var liStr="";
	                 var id = obj.id,instanceID = obj.instanceID,
	                 activityID = obj.activityID,
	                 businessTableID = obj.businessTableID,
	                 formAddress = obj.formAddress,
	                 workflowId = obj.workflowId,
	                 titleValue = obj.titleValue;
	                 var actionUrl="<%=request.getContextPath()%>/workFlow/FileManager/CommonForm.do?instanceID="+instanceID+"&activityId="+activityID+"&flag=ccBox&businessID="+businessTableID+"&workflowId="+workflowId+"&ccid="+id;
	               	liStr="<li><a onclick=dealCC(\""+actionUrl+"\");>. "+ titleValue+"</a></li>";
	              	$ccul.append(liStr);
	               }
	               if(getdata.length>0){
	                 $ccMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:black;' onclick='showCCListMissions();'>更多>></a>");
	               }else{
	                 $ccMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
	               }
        	  	}
             });
         };
       //显示更多抄送信息
         var showCCListMissions=function(){
            window.location.href="<%=request.getContextPath()%>/oa/CCBox.do";
         };
         //显示详细的抄送信息
         var dealCC=function(actionUrl){
 		      frameDialog(actionUrl, "抄送信息", {mode:"full",buttons:[
             { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
                      {
                          var $this = window.top.$( this ) ;
                           $this.dialog( "close" ) ;
		            	 		   window.parent.location.reload();
                       }}
              ]});
          };
       //end by lidong at 2015-03-23 
     
     //加载已办事宜信息(信息来源于任务中心)
     var loadHandledMissions=function(){
        $.getJSON("../taskManagement/taskCenter/taskCenterHandledMissionList.do?command=selectAllHandledMissions",
               function (data) {
                 var $handledMissionUl=$("#handledMissionId");
                 var $moreHandledMissions=$("#moreHandledMissions");
                  $("#haveTodoCount").html("<a onclick='showHandledMissions();' style='text-decoration:none;color:#e20000;display:inline-block;border-bottom:0px;padding:0px 0px;font-size:12px;'>"+(data.length)+"</a>");
                 for(var i=0;i<data.length;i++){
	               if(i==3){
	                break;
	              }
                   var obj=data[i];
	               var liStr="<li>. "+ obj.titleName+"</li>";
	               $handledMissionUl.append(liStr);
                 }
                 if(data.length>0){
                   $moreHandledMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:black;' onclick='showHandledMissions();'>更多>></a>");
                 }else{
                   $moreHandledMissions.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
                 }
               });
       };
       
     var convert=function(curDate,type,operType){
      if(type=="1"){//type=1--->表示 操作的是年月日
          var dateAry=curDate.split(" ");
          if("meetingNotice"==operType){
              dateAryOne=dateAry[0].split("-");
              dateAryTwo=dateAry[1].split(":");
              var result="";
	          for(var i=0;i<dateAryOne.length;i++ ){
	           result+=dateAryOne[i];
	          }
              for(var i=0;i<dateAryTwo.length;i++ ){
	           result+=dateAryTwo[i];
	          }
             return parseInt(result);
          }else if("announcement"==operType){
             dateAry=dateAry[0].split("-");
	         var result="";
	         for(var i=0;i<dateAry.length;i++ ){
	         result+=dateAry[i];
	       }
	       return parseInt(result);
          }
          
      }else{
        var dig=parseInt(curDate);
        if(dig<10)
          return "0"+curDate;//对月月份是1到9的 将其转化成01---09
        return dig;
      }
     };
     
    //点击"更多" 跳转到 会议签收主列表主页面
     var showMeetingNotices=function(){
      //window.location.href="../meetingMangement/meetingNoticeAccepter.do";
       frameDialog("meetingMangement/meetingNoticeAccepter.do", "会议签收情况", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		            window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                window.parent.location.reload();
		        }}
		     ]});
     };
     
     //点击"更多" 跳转到 通知公告主列表页面
     var showAnnouncementLists=function(topicId){
       //window.location.href="../msgBoard/msgBoardList.do";
        frameDialog("msgBoard/msgBoardList.do?topicId=" + topicId + "&type=1", "留言详细", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		        	window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                window.parent.location.reload();
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		        }}
		     ]});
        updateSingleTopic(topicId);
     };
     
     //加载已发布的通知公告
     var loadAllAnnouncements=function(){
       POST(loadPublishedAnnouncementsUrl,null, function(data){
            var $ulObj=$("#tzgg");
            var $announcementObj=$("#tzgg");
            $("#announcementNums").text(data.rows.length);
            for(var i=0;i<data.rows.length;i++){
              if(i>4){
                break;
              }
              
              if(data.rows[i].status!=2 || data.rows[i].status!="2"){
            	  break;
              }
              
              var obj=data.rows[i];
              var liStr="";
              var currentDate=new Date();
              var digDate=currentDate.getFullYear()+""+convert((currentDate.getMonth()+1),"0","announcement")+""+convert(currentDate.getDate(),"0","announcement");
              if(convert(obj.publishDate,"1","announcement")>=parseInt(digDate)){
                 liStr= "<li><a onclick=viewAnnouncementItem(\""+obj.id+"\");>"+". "+obj.title+"</a><img src='../theme/default/images/new02.gif'></li>";
              }else{
                  liStr= "<li><a onclick=viewAnnouncementItem(\""+obj.id+"\");>"+". "+obj.title+"</a></li>";
              }
              $ulObj.append(liStr);
            }
            if(data.length>0){
               $announcementObj.append("<a style='display:inline-block;border:none;padding:0px 10px;font-size:9px;color:black;' onclick='showAnnouncementLists();'>更多>></a>");
            }else{
               $announcementObj.append("<a style='display:inline-block;border:none;padding:0px 10px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
            }
        });
     };
     
     function updateSingleTopic(topicId) {
		 var url = "../msgBoard/msgBoardList.do?command=updateMsg";
		 $.ajax({
				async : false,
	            url: url,
	            type: "POST",
	            data: {
	            	topicId : topicId
	            },
	            dataType: "JSON",
	            success : function (data, xhr) {
	                if (data.error) {
	                    window.error(data.error);
	                    return;
	                }
	            }
	        });
	 }
     
      //点击快捷导航中的"我的日程"图片 跳转到 通知"我的日程"列表页面
     var goMySchedulePage=function(){
         frameDialog("schedule/personalSchedule.do", "我的日程", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		            window.location.href="/ZZ_OA/pages/dashboard.jsp";
	                window.parent.location.reload();
		        }}
		     ]});
     };
     
      //点击快捷导航中的"通讯录"图片 跳转到 "通讯录"列表页面
     var goComunicationPage=function(){
         frameDialog("communication/communicationPersonList.do", "通讯录", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		        }}
		     ]});
     };
     
      //点击快捷导航中的"目标管理"图片 跳转到 "目标管理"列表页面
     var goGoalManagementPage=function(){
       frameDialog("goalManagement/goalManagementList.do", "目标管理", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		        }}
		     ]});
     };
     
      //点击快捷导航中的"计划管理"图片 跳转到 "计划管理"列表页面
     var goPlanManagementPage=function(){
       frameDialog("planManagement/workPlanMangement.do", "计划管理", {mode:"full",width:480,height:500,buttons:[
		        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
		        {
		            var $this = window.top.$( this ) ;
		            $this.dialog( "close" ) ;
		        }}
		     ]});
     };
     
     //按照条件查询通讯录用户信息
     /* var seach=function(){
        var contactInput=$("#contactInputId").val();
        var reg=new RegExp("^[0-9]*$");
        var otherReg=new RegExp("^[a-z|[A-Z]+$");
        var chineseName="";
        var telephone="";
        var simpleUserName="";
        if(reg.test(contactInput)){
          telephone=contactInput;
        }else if(otherReg.test(contactInput)){
             simpleUserName=contactInput;
        }else{
          chineseName=contactInput;
        }
       loadContacts(chineseName,telephone,simpleUserName);
     }; */
     //加载通讯录信息
     var loadContacts=function(chineseName,telephone,simpleUserName){
        if($("#contactId").children().length>0){
           $("#contactId").empty();
        }
        $.getJSON(loadContactsUrl+"&chineseName="+chineseName+"&telephone="+telephone+"&simpleUserName="+simpleUserName,function (data) {
                 var $contactTab=$("#contactId");
                 var $moreContacts=$("#moreContactId");
                 for(var i=0;i<data.length;i++){
	               if(i==5){
	                break;
	              }
                   var obj=data[i];
                   var tr="<tr height='30'>"+
                            "<td><i class='fa fa-user'></i> "+obj.userName+"</td><td>"+(obj.tel==""?"无":obj.tel)+"</td>"+
                           "</tr>";
	               $contactTab.append(tr);
                 }
                 /* $("#moreContactId").empty();
                 if(data.length>0){
                  $moreContacts.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:black;' onclick='showAgencyMissions();'>更多>></a>");
                 }else{
                   $moreContacts.append("<a style='display:inline-block;border:none;padding:0px 0px;font-size:9px;color:#cccccc;float:right;'>更多>></a>");
                 } */
               });
       };
       //首页加载待办资产 
       
    	 var loadEntryTaskCenter = function()
  	 {
  		 var url =  "../propertyManage/advancedQuery.do?command=entryTaskCenter";
  		 $.getJSON(url,
  			function(data) {
  				if (data.error) {
  					window.error({"title" : "发生错误",text : data.error});
  					return;
  				}
  			}
  		 );
  	 }
	 $(document).ready(function(){
	 $("#calendar").fullCalendar({
			header: {
				left: 'prev',
				center: 'title',
				right: 'next'
			},
			buttonText: {
				 prev: '',
	             next: ''
			},  
			theme:false,
			editable: false,
		    timezone:"local"/* ,
            eventSources: [
		        '../schedule/personalSchedule.do?command=searchPersonalMonthScheduleForDashBoard'
		     ], */
		     //eventBorderColor: 'red',
		   /*   eventRender: function(event, element) {
	          element.html(event.msg);
          } */
		});
	 //loadAgencyMissions();
	 //loadHandledMissions(); 
	 //loadMeetingNotice();
	 //loadAllPublishedAnnouncements();
	 //loadMyPersonalSchedule();
	 //loadCCList();
	 //loadEntryTaskCenter();
	 //loadAllAnnouncements();
	 //loadContacts("","");
	 $(".right").css("height",$(".table01").height());
	});
</script>

</head>
<body style="overflow-y:auto;">
<table width="0" border="0" cellpadding="0" cellspacing="0" class="table01" style="height:100%;">
  <tr>
    <td class="td_center">
    <div class="center" style="height:100%;">
    	<div class="left02">
        <span class="left01_title"><span class="more" id="moreagencyMissions"></span><a>菜单一</a><span class="ts">
        	<!-- （您有<span id="toDoCount" class="color_red">  </span>条待办） -->
        </span></span>
          <div id="todoDiv" class="left02_nr" style="height:180px;">
             <ul id="agencyMissionId">
             </ul>
          </div>
      </div>
      <!-- add by lidong at 2015-03-23 -->
      <div class="left02">
        <span class="left01_title"><span class="more" id="ccMissions"></span><a>菜单二</a><span class="ts">
        	<!-- （您有<span id="ccCount" class="color_red"> 0 </span>条未读抄送） -->
        </span></span>
          <div id="ccDiv" class="left02_nr" style="height:180px;">
             <ul id="ccul">
             </ul>
          </div>
      </div>
      <!-- end by lidong at 2015-03-23 -->
      <!-- <div class="left02">
        <span class="left01_title"><span class="more" id="moreHandledMissions"></span><a>已办事宜</a><span class="ts">（您有<span id="haveTodoCount" class="color_red">  </span>条已办）</span></span>
          <div id="haveTodoDiv" class="left02_nr">
             <ul id="handledMissionId">
             </ul>
          </div>
      </div> -->
      <div class="left02">
        <span class="left01_title"><span class="more" id="moreAnnouncementsId"></span><a>菜单三</a></span>
          <div class="left02_nr" style="height:180px;">
             <ul id="announcementId">
             </ul>
          </div>
      </div>
      
      
      <!-- <div class="left02 left_kj">
          <span class="left_kj_title"><a class="title">快捷导航</a></span>
          <div class="left02_nr left_kj_nr">
              <div class="task_content">
                 <table width="0" border="0" cellpadding="0" cellspacing="0">
                      <tr>
                        <td><img src="../theme/default/images/portal/kj_icon_l.png"/></td>
                        <td><img src="../theme/default/images/portal/kj_icon01.png"/><br /><a>公文管理</a></td>
                        <td><img src="../theme/default/images/portal/kj_icon02.png" onclick="goComunicationPage();" style="cursor:pointer;"/><br /><a>通讯录 </a></td>
                        <td class="meeting"><div><span id="myScheduleNums"></span><img src="../theme/default/images/portal/kj_icon03.png" onclick="goMySchedulePage();" style="cursor:pointer;"/></div><a>我的日程</a></td>
                        <td><img src="../theme/default/images/portal/kj_icon_r.png"/></td> 
                      </tr>
                </table>
       </div>
          </div>
      </div> -->
     <%--  <div style="overflow-y:auto;height:40%;padding-bottom:0px;">
          <span class="left01_title"><span class="more" style="display:none;" id="moreSolrSearch"><a style="display:inline-block;border:none;padding:0px 0px;font-size:9px;color:black;" onclick="showSearch();">更多&gt;&gt;</a></span><a class="title">全文检索</a></span>
          <br/>
          <div style="width:100%;clear:left;">
             <iframe src="<%=request.getContextPath() %>/solrManagement/solrSearch.do" frameborder="no" style="width: 100%;height:100%;"></iframe>
   		  </div>
   	  </div> --%>
   	  <div class="left02">
        <span class="left01_title"><span class="more" id="announcements"></span><a>菜单四</a></span>
          <div class="left02_nr" style="height:140px;">
             <ul id="tzgg">
             </ul>
          </div>
      </div>
   </div>
   </td>
    <td class="td_right" style="background:url(../theme/image/bg.jpg);width:220px;">
    <div class="right">
      <!-- <div class="right01" style="height:40%;min-height:370px;">
         <span class="right01_title"><a>我的日程</a></span>
         <div class="right01_nr" id="myPersonalSchedule">
          <div id='calendar' ></div>
        </div> 
        <div class="clear"></div>
      </div> -->
      <div class="right02" style="height:25%;padding-top: 10px;">
         <!-- <span class="right01_title"><a href="">会议通知</a></span> -->
         <span class="left01_title"><span class="more" id="meetingNoticeMoreId"></span><a>侧栏一</a></span>
         <div class="right02_nr">
          <ul id="meetingNoticeId">
          </ul>
         </div>
         <div class="clear"></div>
      </div>
      <div class="clear"></div>
      <div class="right02" style="height:25%;padding-top: 10px;">
         <span class="left01_title"><span class="more" id=""></span><a>侧栏二</a></span>
         <div class="right02_nr">
          <ul id="meetingNoticeId">
          </ul>
         </div>
         <div class="clear"></div>
      </div>
      
      <div class="clear"></div>
      
       <div class="right02" style="height:25%;padding-top: 10px;">
         <!-- <span class="right01_title"><a href="">会议通知</a></span> -->
         <span class="left01_title"><span class="more" id=""></span><a>侧栏三</a></span>
         <div class="right02_nr">
          <ul id="meetingNoticeId">
          </ul>
         </div>
         <div class="clear"></div>
      </div>
      
   </div>
  </td>
  </tr>
   </table>
</body>
</html>
