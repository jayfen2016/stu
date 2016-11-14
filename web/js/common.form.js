/**
 * Created by zhyuan on 2015/5/25.
 */

define(function (require, exports, module) {
    require("jquery.js");
    require("ui.custom.js");
    require("ui.chosen.js");
    require("ui.uploadfile.js");
    require("ui.common.js");
    require("jquery.validate.js");
    require("page.common.js");
    require("ui.pick.js");
    require("ui.ztree.js");
    require("jquery.metadata.js");
    require("util.js");
    require("power.js");
    require("workflowjs/validateForm.js");
    return function (jQuery) {
        (function ($) {
            $.extend({
                makeform: function (opts) {
                    $.commonform.init(opts);
                },
                /**
                 * 判断参数是否为空
                 * @param param
                 * @returns {boolean}
                 */
                isNotBlank: function (param) {
                    if (typeof param != 'undefined' && param != null && param != "null" && param != "") {
                        return true;
                    } else {
                        return false;
                    }
                },
                /**
                 * 检测参数是否为空
                 * @param param
                 * @returns {*}
                 */
                checknull: function (param) {
                    param = $.trim(param);
                    if (typeof param === 'undefined' || param == null || param === "" || param === 'null') {
                        return null;
                    } else {
                        return param;
                    }
                }
            });

            $.fn.extend({
                gobackDialog: function (ev, dataArr, options) {
                    var This = this;
                    ev.stopPropagation();
                    this.next().remove();
                    var opts = {height: 300, width: 200};
                    opts = $.extend(opts, options), buttop = this.offset().top, butleft = this.offset().left,
                        butHeight = Math.round(this.height()), butWidth = Math.round(this.width()),
                        setTop = Math.round(buttop) - opts.height, setLeft = Math.round(butleft) + (butWidth / 2) - (opts.width / 2);
                    $("<span class='gobackDialog'><ul class='gobackul'></ul></span>").insertAfter(this);
                    this.next().height(opts.height).width(opts.width).css({left: setLeft, top: setTop});

                    $(document.body).scroll(function () {
                        This.next().hide();
                    });

                    /*当鼠标向上滚动的时候清空弹出框*/
                    document.documentElement.onmousewheel = scrollfn;
                    if (document.documentElement.addEventListener) {
                        document.documentElement.addEventListener('DOMMouseScroll', scrollfn, false);
                    }
                    function scrollfn(ev) {
                        var event = ev || event, flag = true;
                        if (event.wheelDelta) {
                            flag = event.wheelDelta > 0 ? true : false;
                        } else {
                            flag = event.detail < 0 ? true : false;
                        }
                        if (flag) {//向上滚动
                            This.next().remove();
                        }

                    }

                    $(document).click(function (ev) {
                        This.next().hide();
                    });

                    This.next().click(function (ev) {
                        ev.stopPropagation();
                    });

                    if (!$.isEmptyObject(dataArr)) {
                        var htmlarr = [];
                        for (var i = 0; i < dataArr.length; i++) {
                            var item = dataArr[i];
                            htmlarr.push("<li id='" + item.id + "'>" + item.ActivityName + "</li>");
                        }
                        This.next().find("ul").append(htmlarr.join(""));
                        var allLi = This.next().find("ul li");
                        for (var i = 0; i < allLi.length; i++) {
                            var itmeLi = allLi[i], $li = $(itmeLi);
                            $li.click(function (ev) {
                                var $this = $(this);
                                var infoName = $this.html();
                            	window.message({
                                    text: "确认要退回到（"+infoName+"）环节吗?",
                                    title: "提醒",
                                    buttons: {
                                        "确认": function () {
                                            window.top.$(this).dialog("close");
                                            $.commonform.workflowBark($this.attr("id"));
                                        },
                                        "取消": function () {
                                            window.top.$(this).dialog("close");
                                        }
                                    }
                                });
                            	
                                
                                /*闭关dialog*/
                                This.next().hide();
                            });
                        }

                    }
                }
            });
//            gobackButtonUrl = projectName+"/workFlow/FileManager/CommonForm.do?command=commandChangeDocument",//退回按钮的url
            function CommonForm() {
                this.saveDocumentUrl = projectName + "/workFlow/FileManager/CommonForm.do?command=commandSaveDocument";//保存的url
                this.submitUrl = projectName + "/workFlow/FileManager/CommonForm.do?command=commandSubmentDocument";//提交的url
                this.gobackButtonUrl = projectName+"/workFlow/FileManager/CommonForm.do?command=commandChangeDocument";//实现退回按钮功能的url
                this.loadpagemesurl = projectName + "/workFlow/FileManager/CommonForm.do?command=loadInfo";//加载页面信息的url
                this.archiveFilesUrl = projectName + "/workFlow/FileManager/CommonForm.do?command=endWorkflow";//归档的url
                this.findLogView = projectName + "/workFlow/FileManager/LogView.do";
                this.ccUrl = projectName + "/oa/CCBox.do?command=read";
                this.showBackInfoUrl = projectName + "/workFlow/FileManager/CommonForm.do?command=showBackInfo";//显示退回按钮的url
                this.printUrl=projectName + "/workFlow/FileManager/CommonForm.do?command=commandPrintDocument";//打印url
                this.$formNode = null;//form表单对象
                this.options = {
                    formId: "submitform",//form表单Id
                    typebox: "",//是已办箱、待办箱、拟稿(默认是拟稿),
                    workflowId: "",
                    instanceId: "",
                    activityId: "",
                    businessId: "",
                    receiveID:"",
                    isComButton: false,//超送
                    ccid: "",//ccid
                    /*给相应的按钮加事件用到的函数*/
                    event: {},
                    buttonArray:[],//所有button都放在共公容器的class名称
                    //初始化流程回调函数
                    initWorkflow: function () {
                    },
                    //提交时表单参数赋值更改
                    updataFormdata:function(){
                    },
                    //归档回调函数
                    achive: function (data) {
                        if (!$.isEmptyObject(data)) {
                            if (data.flag) {
                                window.location.href = projectName + "/workFlow/FileManager/submitsuccess.do?formAddress=" +
                                    data.formAddress + "&instanceID=" + data.instanceID + "&activityId=" + data.activeId +
                                    "&businessID=" + data.businessID + "&workFlowId=" + data.workFlowId + "&userIDs=" + data.userIDs;
                            } else {
                                alert("提交失败");
                            }
                        } else {
                            alert("提交失败");
                        }
                    },
                    //提交归档增加参数
                    validateArchive:function(data){
                    	return data;
                    },
                    //提交时校验环节
                    validata:function(submitsenddata){
                    	return submitsenddata;
                    },
//                    提交弹出框隐藏时回调
                    submitButtonFalse: function (data) {
                        if ($.isNotBlank(data)) {
                            var jsondata = $.parseJSON(data);
                            if (jsondata.flag) {
                                window.location.href = projectName + "/workFlow/FileManager/submitsuccess.do?formAddress=" +
                                    jsondata.formAddress + "&instanceID=" + jsondata.instanceID + "&activityId=" + jsondata.activeId +
                                    "&businessID=" + jsondata.businessID + "&workFlowId=" + jsondata.workFlowId + "&userIDs=" + jsondata.userIDs+
                                    "&nextActivictID="+jsondata.nextActivictID+"&manyNextActivity="+jsondata.manyNextActivity;
                            } else {
                                window.location.href = projectName + "/pages/workFlow/FileManager/submiterror.jsp";
                            }
                        }
                    },
                    selfMakefile:function(){
                    	officeuploadItem($("input[data-xtype=officeupload]"), function (data) {
                            if (data && data.id != null) {
                            	var fileId = data.id;
                            	var listUrl = projectName+"/workFlow/FileManager/RedTemplateList.do?command=search";
                            	$.getJSON(listUrl,{},function(data){
                        	        if(!$.isEmptyObject(data)){
                        	        	var trAry = "";
                        	        	var infoList = data.info;
                        		 		for (var i in infoList) {
                        		 		   var tda = infoList[i];
                        		 		   var inputAry = "";
                        		 		   var filename = tda.filename;
                        		 		   inputAry = "<tr><td class='whiteZW' id='a"+i+"' onMouseOver=\"selectZW('a"+i+"')\" onMouseOut=\"removeZW('a"+i+"')\" onclick=\"inRed('"+filename+"','"+fileId+"')\">"+filename+"</td></tr>";
                        	               trAry += inputAry;
                        	        	} 
                        	            $("#list2").append(trAry);
                        	            $("#dialog-inRed").dialog( "open" ) ;
                        	        }
                        		});
                            } else {
                                alert("您没有上传任何文件");
                            }
                        });
                    },
                    
//                    提交弹出框显示时回调
                    submitButtonTrue: function () {
                        var This = this;
                        var submitUrl = projectName + "/workFlow/FileManager/CommonForm.do?command=commandSubmentDocument";//提交的url
                        frameDialog(projectName + "/workFlow/FileManager/submitDialog.do?workFlowId=" + This.workflowId + "&nowActiveId=" + This.activityId + "&businessID=" + This.businessId + "&instanceID=" + This.instanceId,
                                "提交下一步", {
                                    mode: "middle",
                                    width: 900,
                                    height: 540,
                                    buttons: [
                                        {
                                            text: "确定",
                                            icons: {primary: "ui-icon-"},
                                            click: function (ev) {
                                                var $this = window.top.$(this),
                                                    childdocument = $this.find("iframe")[0].contentWindow.document,
                                                    $childdocument = $(childdocument),
                                                    selectinstancenode = $childdocument.find("input[name='nextooperation']"),
                                                    selectActiveid;
                                                if (selectinstancenode.length > 0) {
                                                    for (var i = 0; i < selectinstancenode.length; i++) {
                                                        if (selectinstancenode[i].checked) {
                                                            selectActiveid = selectinstancenode[i].value;
                                                        }
                                                    }
                                                    var $checkusers = $childdocument.find("#checkedoperators").find("div[flag]"),
                                                        selectAlluserid = [];
                                                    if ($checkusers) {
                                                        for (var j = 0; j < $checkusers.length; j++) {
                                                            var $item = $($checkusers[j]);
                                                            selectAlluserid.push($item.attr("flag"));
                                                        }
                                                    }
                                                    var data = "";
                                                    if ($("#yjFile").data() != null) {
                                                        data = $("#yjFile").data().fileItems();
                                                    }
                                                    var submitsenddata = {
                                                        workFlowId: This.workflowId,
                                                        nowActiveId: This.activityId,
                                                        selectActiveid: selectActiveid,
                                                        selectAlluserid: selectAlluserid,
                                                        instanceID: This.instanceId,//保存时产生的实例id
                                                        businessID: This.businessId,//业务id
                                                        receiveID : This.receiveID,
                                                        formdata: JSON.stringify($("#submitform").getDtypeFormData()),
                                                        yjContext: $("#yjContext").val(),
                                                        yjFile: JSON.stringify(data)
                                                    };
                                                    $.post(submitUrl, submitsenddata, function (data) {
                                                        if ($.isNotBlank(data)) {
                                                            var jsondata = $.parseJSON(data);
                                                            if (jsondata.flag) {
                                                                $this.dialog("close");
                                                                window.location.href = projectName + "/workFlow/FileManager/submitsuccess.do?formAddress=" +
                                                                    jsondata.formAddress + "&instanceID=" + jsondata.instanceID + "&activityId=" + jsondata.activeId +
                                                                    "&businessID=" + jsondata.businessID + "&workFlowId=" + jsondata.workFlowId + "&userIDs=" + jsondata.userIDs +
                                                                    "&nextActivictID="+jsondata.nextActivictID+"&manyNextActivity="+jsondata.manyNextActivity;
                                                            } else {
                                                                window.location.href = projectName + "/pages/workFlow/FileManager/submiterror.jsp";
                                                            }
                                                        } else {
                                                            window.location.href = projectName + "/pages/workFlow/FileManager/submiterror.jsp";
                                                        }
                                                    });
                                                }
                                            }
                                        },
                                        {
                                            text: "清除",
                                            icons: {primary: "ui-icon-"},
                                            click: function (ev) {
                                                var $this = window.top.$(this),
                                                    childdocument = $this.find("iframe")[0].contentWindow.document;
                                                $(childdocument).find("input[name='nextooperation']").eq(0).click();
                                            }
                                        },
                                        {
                                            text: "返回",
                                            icons: { primary: "ui-icon-cancel" },
                                            click: function (ev) {
                                                var $this = window.top.$(this);
                                                $this.dialog("close");
                                            }
                                        }
                                    ]});
                        
                    },
//                    保存数据回调
                    saveCallback: function (data) {
                        if ($.isNotBlank(data)) {
                            var jsonData = JSON.parse(data);
                            if (!$.isEmptyObject(jsonData)) {
                                if (jsonData.flag) {
                                    this.instanceId = jsonData.instanceID;
                                    this.businessId = jsonData.businessID;
                                    var formAddress = jsonData.formAddress;
                                    window.location.href = projectName + "/workFlow/FileManager/CommonForm.do?instanceID=" + this.instanceId + "&activityId=" + this.activityId + "&flag=wattingitem&businessID=" + this.businessId + "&workflowId=" + this.workflowId;
                                    alert("保存成功");
                                } else {
                                    alert("数据保存失败");
                                }
                            } else {
                                alert("数据保存失败");
                            }
                        }

                    },
//                  退回回调函数
                    gobackCallback: function (data, ev) {
                        if ($.isNotBlank(data)) {
                            var arr = [], josndata = $.parseJSON(data);
                            if (!$.isEmptyObject(josndata)) {
                                var info = josndata.info;
                                if (info.length == 1) {
                                	var infoName = info[0].ActivityName;
                                	window.message({
                                        text: "确认要退回到（"+infoName+"）环节吗?",
                                        title: "提醒",
                                        buttons: {
                                            "确认": function () {
                                                window.top.$(this).dialog("close");
                                                $.commonform.workflowBark(info[0].id);
                                            },
                                            "取消": function () {
                                                window.top.$(this).dialog("close");
                                            }
                                        }
                                    });
                                	
                                    
                                }
                                else {
                                    for (var i = 0; i < info.length; i++) {
                                        arr.push(info[i]);
                                    }
                                    $("#goback").gobackDialog(ev, arr, {height: 21 * info.length, width: 150});
                                }
                            }
                        }
                    },
//                从后端加载前端所用的数据后要执行的回调函数
                    loadpageCallback: function (jsondata) {
                        if (!$.isEmptyObject(jsondata)) {
                            var isBackButton = jsondata.isBackButton,
                             	isFile = jsondata.isFile;
                            if (isFile) {//true时显示归档
                                $("#archiveFiles").show();
                                $("#savemessage").hide();
                                $("#submit").hide();
                            } else {//显示提交和保存
                                $("#archiveFiles").hide();
                                $("#savemessage").show();
                                $("#submit").show();
                            }
                            if (isBackButton) {
                                $("#goback").show();
                            } else {
                                $("#goback").hide();
                            }
                        }
                    },
                    /**
                     * 页面加载完成的回调函数
                     * @param loaddata 从后端load的全部数据
                     * @param pagedata 页面上的数据
                     */
                    onComplete: function (loaddata, pagedata) {
                    }
                };
                $( "#dialog-inRed" ).dialog(
	    	      {
	    	          autoOpen : false,
	    	          height   : 500,
	    	          width    : 630,
	    	          modal    : true,
	    	          buttons  :
	    	          {
	    	              "取消" : function()
	    	              {
	    	                  $( this ).dialog( "close" ) ;
	    	                  $("#list2").empty();
	    	              }
	    	          },
	    	          close : function()
	    	          {
	    	              $( this ).dialog( "close" ) ;
	    	              $("#list2").empty();
	    	          }
	    	      });
                $("#dialog-check").dialog(
              	      {
              	          autoOpen : false,
              	          height   : 200,
              	          width    : 500,
              	          modal    : true,
              	          title    :'校对事项清单', 
              	          buttons  :
              	          {
              	              "取消" : function()
              	              {
              	                  $( this ).dialog( "close" ) ;
              	              }
              	          },
              	          close : function()
              	          {
              	              $( this ).dialog( "close" ) ;
              	          }
              	      });
              $("#dialog-printcheck").dialog(
              	      {
              	          autoOpen : false,
              	          height   : 200,
              	          width    : 500,
              	          modal    : true,
              	          title    :'印制校对清单', 
              	          buttons  :
              	          {
              	              "取消" : function()
              	              {
              	                  $( this ).dialog( "close" ) ;
              	              }
              	          },
              	          close : function()
              	          {
              	              $( this ).dialog( "close" ) ;
              	          }
              	      });
            }

            $.extend(CommonForm.prototype, {
                init: function (opts) {
                    $.extend(this.options, opts);
                    this.$formNode = $("#" + this.options.formId);
                    this.initForm();
                    var self = this, optsdata = this.options;
                    if ($.isNotBlank(optsdata.typebox)) {
                        if ('wattingitem' === optsdata.typebox) {//从待箱传过来的数据
                            $("#ccSubmit").show();
                            $("#checklog").remove();
                            $("#checkflowchart").remove();
                            setElementStylePower(optsdata.workflowId, optsdata.activityId);//给元素加权限
                            setpower(optsdata.workflowId, optsdata.activityId, function () {
                                $.when(formuser(self.$formNode, $.Deferred())).done(function () {
                                    self.loadpage({activityId: optsdata.activityId, instanceID: optsdata.instanceId, businessID: optsdata.businessId});//加载页面信息
                                    self.dialogset();//设置弹出框
                                });
                            });
                        } else if ('haveTodoitem' === optsdata.typebox) {//从已办箱传过来
                            $("#ccSubmit").show();
                            $("#printpage").show();
                            $("#archiveFiles").remove();
                            $("#savemessage").remove();
                            $("#submit").remove();
                            $("#goback").remove();
                            $("#makefile").remove();
                            $("#cancel").remove();
                            // setElementStylePower(workflowId,nowActiveId);//给元素加权限
                            setpowerdo(optsdata.workflowId, optsdata.activityId, function () {//给表单元素加权限
                                $.when(formuser(self.$formNode, $.Deferred())).done(function () {
                                    self.loadpage({activityId: optsdata.activityId, instanceID: optsdata.instanceId, businessID: optsdata.businessId}, true);//加载页面信息
                                    setFormIsOnlyRead(self.$formNode);//把表单中的数据全部设置为readonly
                                });
                            });
                        } else if ('ccBox' === optsdata.typebox) {//从抄送箱传过来
                            $("#ccSubmit").show();
                            $("#archiveFiles").remove();
                            $("#savemessage").remove();
                            $("#submit").remove();
                            $("#goback").remove();
                            $("#printpage").remove();
                            $("#cancel").remove();
                            // setElementStylePower(workflowId,nowActiveId);//给元素加权限
                            setpower(optsdata.workflowId, optsdata.activityId, function () {//给表单元素加权限
                                $.when(formuser(self.$formNode, $.Deferred())).done(function () {
                                    self.loadpage({activityId: optsdata.activityId, instanceID: optsdata.instanceId, businessID: optsdata.businessId}, true);//加载页面信息
                                    setFormIsOnlyRead(self.$formNode);//把表单中的数据全部设置为readonly
                                });
                            });
                            $.post(self.ccUrl, { ccids: optsdata.ccid }, function (data) {
                            });
                        }
                    } else {//拟搞
                        this.dialogset();//设置弹出框
                        $("#checklog").remove();
                        $("#checkflowchart").remove();
                        $("#archiveFiles").remove();
                        /*this.options.initWorkflow();*/
                        setElementStylePower(optsdata.workflowId, optsdata.activityId);//给元素加权限
                        setpower(optsdata.workflowId, optsdata.activityId, function () {
                            $.when(formuser(self.$formNode, $.Deferred())).done(function () {
                                optsdata.onComplete();
                            });
                        });
                        this.options.initWorkflow();
                    }
                    this.dialogset4LogInfo();
                },
                initForm: function () {
                    _initFormControls();//设置表单
                    var optdata = this.options,//对象所有options配制参数
                        optFunc = $.extend(this.initFormEvent(), optdata.event),
                        btnArr=[{query:[".buttonStyle button"],eventType:"click"}].concat(optdata.buttonArray);
                    $.each(btnArr,function(i,item){
                        var query=item.query,type=item.eventType;
                        $.each(query,function(j,jtem){
                            $(jtem).button().bind(type,function(ev){
                                var id = $(this).attr("id") || $(this).attr("name");
                                if (id && optFunc[id]) {
                                    optFunc[id](ev);
                                }
                            });
                        });
                    });
                },
//                初始化所有默认按钮点击事件
                initFormEvent: function () {
                    var self = this, optdata = self.options;
                    return {
                        submit: function (ev) {//提交按钮
                        	if(validateForm(powerRuleJson)){
                        		optdata.updataFormdata();
                        		var data = "",
                        			submitsenddata ={};
                                if ($("#yjFile").data() != null) {
                                    data = $("#yjFile").data().fileItems();
                                }
                        		submitsenddata = {
                                        workFlowId: optdata.workflowId,
                                        nowActiveId: optdata.activityId,
                                        instanceID: optdata.instanceId,//保存时产生的实例id
                                        businessID: optdata.businessId,//业务id
                                        receiveID : optdata.receiveID,
                                        formdata: JSON.stringify(self.$formNode.getDtypeFormData()),
                                        yjContext: $("#yjContext").val(),
                                        yjFile: JSON.stringify(data)
                                    };
                        		 optdata.validata(submitsenddata);
                        		 if(submitsenddata.selectActiveid){
                        			 optdata.isComButton="false";
                        		 }
                        		 if (!optdata.isComButton || optdata.isComButton == "false") {
                                     $.post(self.submitUrl, submitsenddata, function (data) {//提交不显示弹出框的回调
                                         optdata.submitButtonFalse(data);
                                     });
                                 } else {
                                     optdata.submitButtonTrue();
                                 }
                        	}
                        },
                        savemessage: function (ev) { //保存按钮
                        	if(validateForm(powerRuleJson)){
                        		optdata.updataFormdata();
	                            var formdata = self.$formNode.getDtypeFormData(),//用它来判断是新增还是修改
	                                sendData = {formdata: JSON.stringify(formdata),activityId:optdata.activityId, workFlowId: optdata.workflowId, requestId: optdata.instanceId, businessID: optdata.businessId,receiveID:optdata.receiveID};
	                            $.post(self.saveDocumentUrl, sendData, function (data) {
	                                optdata.saveCallback(data);
	                            });
                        	}
                        },
                        //退回按钮
                        goback: function (ev) {
                            var senddata = {
                                workflowID: optdata.workflowId,
                                activityID: optdata.activityId
                            };
                            $.post(self.showBackInfoUrl, senddata, function (data) {
                                optdata.gobackCallback(data, ev);
                            });


                            /* var senddata = {
                             workFlowId: optdata.workflowId,
                             nowActiveId:optdata.activityId,
                             instanceID:optdata.instanceId,
                             businessID: optdata.businessId
                             };
                             $.post(self.gobackButtonUrl, senddata, function (data) {
                             optdata.gobackCallback(data);
                             });*/

                        },
                        printpage: function (ev) {//打印按钮
                          /*  $("body").html(self.$formNode.html());
                            window.print();*/
                            var printUrl=projectName + "/workFlow/FileManager/CommonForm.do?command=commandPrintDocument";
                            var  sendData = { workFlowId: optdata.workflowId, activityId: optdata.activityId, instanceID: optdata.instanceId, businessID: optdata.businessId};
                            $.post(self.printUrl, sendData, function (data) {
                                    /*if(!$.isEmptyObject(data)){
                                    	var dataObj=$.parseJSON(data);
                                    	window.location.href=dataObj.filePathInServer;
                                        
                                    	var filerealpath=dataObj.filePathInServer,fileName=dataObj.fileName,
                                        url=projectName+"/pages/workFlow/FileManager/officeupload/lookover.jsp?path="+filerealpath+"&fileName="+fileName;
                                        lookflowChart(url,"查看文件");
                                    }*/
                            	if(!$.isEmptyObject(data)){
                            		var dataObj=$.parseJSON(data);
                                	var filerealpath=dataObj.filePathInServer,fileName=dataObj.fileName;
                                	window.location.href=projectName+"/platform/accessory_.do?command=downloadFile&fileName="+fileName+"&filePathInServer="+filerealpath;
                            	}
                            });
                        },
                        ccSubmit: function (ev) {//超送按钮
                            var This = this;
                            frameDialog(projectName + "/common/choosePerson.do?command=init&moduleType=organization", "请选择", {mode: "middle", width: 400, height: 550, buttons: [
                                { text: "确定", icons: { primary: "ui-icon-check" }, click: function (ev) {
                                    var $this = window.top.$(this),
                                        dial = $this.find("iframe")[0].contentWindow;
                                    var rowData = dial.getData().split(";");
                                    var rowIdData = rowData[1];
                                    var rowNameData = rowData[0];
                                    $("#text_roomAdminNames").val(rowNameData);
                                    $("#roomAdminIDs").val(rowIdData);
                                    if (rowData.length <= 0) {
                                        window.Msg.alert("请选择要抄送的人");
                                        return;
                                    }
                                    var senddata = {
                                        instanceID: optdata.instanceId,
                                        userids: rowIdData
                                    };
                                    $.post(projectName + "/oa/CCBox.do?command=new", senddata, function (data) {
                                        if ($.isNotBlank(data)) {
                                            var josndata = $.parseJSON(data);
                                            if (!$.isEmptyObject(josndata)) {
                                                if (josndata.flag) {
                                                    alert("抄送成功");
                                                } else {
                                                    alert("抄送失败");
                                                }
                                            }
                                        }
                                    });
                                    $this.dialog("close");
                                }},
                                { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                                    var $this = window.top.$(this);
                                    $this.dialog("close");
                                }}
                            ]});

                        },
                        cancel: function (ev) {//取消按钮
                            window.location.href = projectName + "/oa/todoList.do";
                        },
                        checklog: function (ev) {//查看日志按钮
                            var editUrl = self.findLogView + "?command=init&instanceID=" + optdata.instanceId;//加载个人信息
                            frameDialog(editUrl, "查看日志", {mode: "middle", width: 900, height: 540, buttons: [
                                { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                                    var $this = window.top.$(this);
                                    $this.dialog("close");
                                }
                                }
                            ]});
                        },
                        checkflowchart: function (ev) {//查看流程图按钮
                            var url = projectName + "/pages/workFlow/FileManager/proccessPicture/CommonForm_picture.jsp?workflowId=" + optdata.workflowId + "&instanceID=" + optdata.instanceId,
                                title = "查看流程图";
                            self.lookflowChart(url, title);
                        },
                        archiveFiles: function (ev) {//结束按钮(归档提交)
                            var formdata = self.$formNode.getDtypeFormData(),
                                archivedata = {
                                    workFlowId: optdata.workflowId,
                                    nowActiveId: optdata.activityId,
                                    instanceID: optdata.instanceId,
                                    businessID: optdata.businessId,
                                    receiveID:optdata.receiveID,
                                    formdata: JSON.stringify(formdata)
                                };
                            optdata.validateArchive(archivedata);
                            $.ajax({
                                url: self.archiveFilesUrl,
                                data: archivedata,
                                type: "post",
                                dataType: "json",
                                success: function (data) {
                                    optdata.achive(data);
                                }
                            });
                        },
//                        置文默认事件
                        makefile: function (ev) {
                        	optdata.selfMakefile();
                        }

                    };
                },
                lookflowChart: function (url, title) {
                    frameDialog(url, title,
                        {
                            mode: "full",
                            buttons: [
                                { text: "返回", icons: { primary: "ui-icon-cancel" }, click: function (ev) {
                                    var $this = window.top.$(this);
                                    var $iframes = $this.find("iframe");
                                    if ($iframes.length) {
                                        $iframes[0].contentWindow.$("object").remove();
                                    }
                                    $this.dialog("close");
                                }}
                            ]
                        });
                },
                dialogset: function () {
                    var self = this, optsdata = this.options;
                    $("#submit-next").dialog({
                        autoOpen: false,
                        height: 540,
                        width: 900,
                        modal: true,
                        resizable: false,
                        buttons: {
                            "确定": function () {
                                var selectinstancenode = $("input[name='nextooperation']"),
                                    selectActiveid;
                                if (selectinstancenode) {
                                    for (var i = 0; i < selectinstancenode.length; i++) {
                                        if (selectinstancenode[i].checked) {
                                            selectActiveid = selectinstancenode[i].value;
                                        }
                                    }
                                    var $checkusers = $("#checkedoperators").find("div[flag]"),
                                        selectAlluserid = [];
                                    if ($checkusers) {
                                        for (var j = 0; j < $checkusers.length; j++) {
                                            var $item = $($checkusers[j]);
                                            selectAlluserid.push($item.attr("flag"));
                                        }
                                    }
                                    var data = "";
                                    if ($("#yjFile").data() != null) {
                                        data = $("#yjFile").data().fileItems();
                                    }

                                    var submitsenddata = {
                                        workFlowId: optsdata.workflowId,
                                        nowActiveId: optsdata.activityId,
                                        selectActiveid: selectActiveid,
                                        selectAlluserid: selectAlluserid,
                                        instanceID: optsdata.instanceId,//保存时产生的实例id
                                        businessID: optsdata.businessId,//业务id
                                        receiveID : optdata.receiveID,
                                        formdata: JSON.stringify(self.$formNode.getDtypeFormData()),
                                        yjContext: $("#yjContext").val(),
                                        yjFile: JSON.stringify(data)
                                    };
                                    $.post(self.submitUrl, submitsenddata, function (data) {
                                        if ($.isNotBlank(data)) {
                                            var jsondata = $.parseJSON(data);
                                            if (jsondata.flag) {
                                                $("#submit-next").dialog("close");
                                                window.location.href = projectName + "/workFlow/FileManager/submitsuccess.do?formAddress=" +
                                                    jsondata.formAddress + "&instanceID=" + jsondata.instanceID + "&activityId=" + jsondata.activeId +
                                                    "&businessID=" + jsondata.businessID + "&workFlowId=" + jsondata.workFlowId + "&userIDs=" + jsondata.userIDs+
                                                    "&nextActivictID="+jsondata.nextActivictID+"&manyNextActivity="+jsondata.manyNextActivity;
                                            } else {
                                                window.location.href = projectName + "/pages/workFlow/FileManager/submiterror.jsp";
                                            }
                                        } else {
                                            alert("提交出错");
                                        }
                                    });
//                   $("#submit-next").dialog("close");
                                }
                            },
                            "清除": function () {
                                $($("input[name='nextooperation']")[0]).click();
                            },
                            "关闭": function () {
                                $("#submit-next").dialog("close");
                            }
                        }
                    });
                },
                dialogset4LogInfo: function () {
                    $("#log-view").dialog({
                        autoOpen: false,
                        height: 540,
                        width: 900,
                        modal: true,
                        resizable: false,
                        buttons: {
                            "关闭": function () {
                                $("#log-view").dialog("close");
                            }
                        }
                    });
                },
                /**
                 * 给页面load数据
                 * @param senddata 给后端的数据
                 * @param flag 是否让页面数据只读，为true时加载页面时，让页面所有数据都只读，为false该怎么样怎么样
                 */
                loadpage: function (senddata, flag) {
                    var self = this, optsdata = this.options;
                    $.getJSON(this.loadpagemesurl, senddata, function (jsondata) {
                        optsdata.loadpageCallback(jsondata);
                        if (!$.isEmptyObject(jsondata)) {
                            if (!$.isEmptyObject(jsondata.info)) {
                                optsdata.isComButton = jsondata.isComButton;
                                self.$formNode.setFormData(jsondata.info, flag);
                            }
                            optsdata.onComplete(jsondata, jsondata.info); //页面加载完成的回调函数
                        }
                    });
                },
                workflowBark: function (selectActiveid) {
                    var optsdata = this.options;
                    var submitsenddata = {
                        workFlowId: optsdata.workflowId,
                        nowActiveId: optsdata.activityId,
                        instanceID: optsdata.instanceId,//保存时产生的实例id
                        businessID: optsdata.businessId,//业务id
                        selectActiveid: selectActiveid,
                        formdata: JSON.stringify(this.$formNode.getDtypeFormData()),
                        yjContext: $("#yjContext").val(),
                        yjFile: JSON.stringify(  $("#yjFile").data() && typeof $("#yjFile").data().fileItems !=="undefined"  ? $("#yjFile").data().fileItems() :[])
                    };
                    $.post(this.gobackButtonUrl, submitsenddata, function (data) {
                        if ($.isNotBlank(data)) {
                            var jsondata = $.parseJSON(data);
                            if (jsondata.flag) {
                                alert("退回成功");
                            } else {
                                alert("退回失败");
                            }
                            window.location.href = projectName + "/oa/todoList.do";
                        }
                    });
                }
            });
            $.commonform = new CommonForm();//单例模式
        })(jQuery);
    };
});