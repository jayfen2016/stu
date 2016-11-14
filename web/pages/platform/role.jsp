<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../theme/default/ui.custom.css" rel="stylesheet" />
<link href="../theme/default/ui.jqgrid.css" rel="stylesheet" />
<link href="../theme/default/font.awesome.css" rel="stylesheet" />
<link href="../theme/default/ui.chosen.css" rel="stylesheet" />
<link href="../theme/default/ui.uploadfile.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<!--<script type="text/javascript" src="../js/jquery.metadata.js"></script>-->
<script type="text/javascript" src="../js/page.common.js"></script>
<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style type="text/css">
	#funTab,#dataTab {
		border-top: 1px solid #DADADA;
        border-left: 1px solid #DADADA;
    }
    #funTab td,#dataTab td {
        border-right: 1px solid #DADADA;
        border-bottom: 1px solid #DADADA;
        padding: 6px 0px;
    }
     #funTab thead label,#dataTab thead label{
     margin:0 2px 0 8px;
    }
    #funTab tbody,#dataTab tbody{
    background-color: white;
    }
    #funTab tbody tr:nth-child(2n+1) ,#dataTab tbody tr:nth-child(2n+1){
     background-color:#f5f5f5;
    }
    
</style>
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = '#pager2', 
	loadUrl = "../platform/role.do?command=load", 
	deleteUrl = "../platform/role.do?command=delete", 
	saveUrl = "../platform/role.do?command=submit", 
	listUrl = "../platform/role.do?command=search",
	loadFunctionUrl = "../platform/role.do?command=initFunctions",
	getFunctionUrl = "../platform/role.do?command=getFunctions",
	saveFunctionUrl = "../platform/role.do?command=saveFunctions",
	loadDataUrl = "../platform/role.do?command=initDatas",
	getDataUrl = "../platform/role.do?command=getDatas",
	saveDataUrl = "../platform/role.do?command=saveDatas";

	var reloadFunctions = function () {
        $.getJSON(loadFunctionUrl,
                        function (data) {
                            $("#purviewFunSaveBTN").button("enable");
                            var $purfun = $("#funTab tbody"), funAry = [];
                            for (var i in data) {
                                var tda = data[i];
                                var el = "<tr>";
                                el += "<td align='center'>"+(parseInt(i)+1)+"</td>";
                                el += "<td align='center'><input value='"+tda.id+"' type='checkbox' /></td>";
                                el += "<td align='left'>";
                                var lev=parseInt(tda.menuLevel,10),name=tda.menuName,paddingLeft=(lev-1)*24+6,iconClass="fa-link";
                 			   	if(lev>=2){
                 			   		iconClass="fa-chain-broken";
                 			   	}
                                el += "<label style='padding-left:"+paddingLeft+"px;'><i class='fa "+iconClass+"' style='margin-right:3px;'></i></label>"+name;
                                el += "</td>";
                                el += "<td align='center'>";
                                el += "<input type='checkbox' value='insert'/>添加";
                                el += "<input type='checkbox'  value='edit'/>修改";
                                el += "<input type='checkbox'  value='delete'/>删除";
                                el += "<input type='checkbox'  value='export'/>导出";
                                el += "</td>";
                                el += "</tr>";
                                funAry.push(el);
                            }
                            $purfun.append(funAry);
                            $purfun.find("tr").find("td:eq(1) input[type=checkbox]").click(function(){
                			    var checked=this.checked,$el=$(this);
                			    $el.closest("tr").find("td:not(:eq(1)) input[type=checkbox]").each(function(index,item){
                						item.checked=checked;
                				    });
                			});
                            $purfun.find("tr").find("td:not(:eq(1)) input[type=checkbox]").click(function(){
                			    var checked=this.checked,$el=$(this);
                			    if($el.parent().find("input[type=checkbox]:checked").length>0){
                			    	checked = true;
                			    } else {
                			    	checked = false;
                    			}
                			    $el.closest("tr").find("td:eq(1) input[type=checkbox]").each(function(index,item){
                						item.checked=checked;
                				    });
                			});
                        });
    };

    var reloadDatas = function () {
        $.getJSON(loadDataUrl,
                        function (data) {
                            $("#purviewDataSaveBTN").button("enable");
                            var $purData = $("#dataTab tbody"), dataAry = [];
                            for (var i in data) {
                                var tda = data[i];
                                var el = "<tr>";
                                el += "<td align='center'>"+(parseInt(i)+1)+"</td>";
                                el += "<td align='center'><input value='"+tda.taxFilingUnitTmisCode+"' type='checkbox' /></td>";
                                el += "<td align='left'>";
                                var lev=parseInt(tda.taxFilingUnitLevel,10),name=tda.taxFilingUnitName,paddingLeft=(lev-1)*24+6,iconClass="fa-link";
                 			   	if(lev>=2){
                 			   		iconClass="fa-chain-broken";
                 			   	}
                                el += "<label style='padding-left:"+paddingLeft+"px;'><i class='fa "+iconClass+"' style='margin-right:3px;'></i></label>"+name;
                                el += "</td>";
                                el += "<td align='center'>";
                                el += tda.taxFilingUnitJdeCode;
                                el += "</td>";
                                el += "<td align='center'>";
                                el += tda.hyperionCode;
                                el += "</td>";
                                el += "<td align='center'>";
                                el += tda.taxFilingUnitType;
                                el += "</td>";
                                el += "</tr>";
                                dataAry.push(el);
                            }
                            $purData.append(dataAry);
                        });
    };

var fillFunPurview=function(data){
	$("#menuSearch").val("").trigger("keypress");
    $("#funTab input[type=checkbox]:checked").each(function(i,item){item.checked=false});
	$("#funTab tbody tr").find("td:eq(1) input[type=checkbox]").each(function(inex,item){
		  var $el= $(item);
           for(var i in data){
                 var dataItem =data[i],btcodes=data[i].buttonCodes+",";
                 if(item.value===dataItem.menuId){
                	 item.checked=true;
                	 $othc= $el.closest("tr").find("td:not(:eq(1)) input[type=checkbox]").each(function(cindex,citem){
                             if( btcodes.indexOf( citem.value+",")!==-1){
                                    citem.checked=true;
                                 }
                    	 });
                     }
               }     
		});
	
};

var fillDataPurview=function(data){
	$("#buJdeSearch").val("");
	$("#buNameSearch").val("");
	$("#buHyperionSearch").val("");
	$("#buTypeSearch").val("");
	$("#dataAuthoritySearch").trigger("click");
    $("#dataTab input[type=checkbox]:checked").each(function(i,item){item.checked=false});
	$("#dataTab tbody tr").find("td input[type=checkbox]").each(function(inex,item){
           for(var i in data){
                 var dataItem =data[i];
                 if(item.value===dataItem){
                	 item.checked=true;
                 }
               }     
		});
	
};
	
	$(function() {
		_initButtons({
			funPurviewBTN : function(ev) {
			    var idAry = $(listId).jqGrid("getGridParam", "selarrrow"),
			    $i = $(ev.currentTarget).find("i"),
			    $piel = $(".page-purview-fun-panel");
			    if (idAry.length === 0) {
                    window.message({ text: "请选择要维护功能权限的角色记录!", title: "提示" });
                    return;
                }
			    if (idAry.length > 1) {
					window.message({
						text : "每次只能维护单条角色的功能权限!",
						title : "提示"
					});
					return;
				}
                window.editId = idAry[0];
			    $piel.show({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 600
				}).find("h4 i").removeClass();
				if ($i.length) {
					$piel.addClass($i.attr("class"));
				}
				$.ajax({
                    url: getFunctionUrl,
                    type: "GET",
                    data: {
                        roleId: idAry[0]
                    },
                    dataType: "JSON",
                    success: function (data, xhr) {
                        if (data.error) {
                            window.error(data.error);
                            return;
                        }
                     
                        fillFunPurview(data);
                        
                        $piel = $piel.show({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 600
                        }).find("h4 i").removeClass();
                        if ($i.length) {
                            $piel.addClass($i.attr("class"));
                        }

                        if (data.message) {
                            window.message({
                                text: data.message,
                                title: "消息"
                            });
                        }
                    }
                });
			},
			purviewFunSaveBTN:function(ev){
                var result=[];
				$("#funTab tbody tr").each(function(inex,item){
                     var r={},$el=$(item).find("td:eq(1) input[type=checkbox]"),val=$el.val();
                      r["menuId"]=val;
                      var buttons=[];
                      $el.closest("tr").find("td:not(:eq(1)) input[type=checkbox]:checked").each(function(index,item){
                    	  buttons.push(item.value);
                      });
                      if(buttons==null||buttons.length==0)
                          return;
                      r["buttonCodes"]=buttons.join(",");
	  				  result.push(r);
					});
				$.ajax({
                    url: saveFunctionUrl, dataType: "JSON", type: "POST", data: { "roleId": editId, "functions": result }, success: function (data, xhr) {
                        if (data.error) {
                            window.error(data.error);
                            return;
                        }
                        $(".page-purview-fun-panel").hide({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 400
                        });
                        if (data.message) {
                            window.message({
                                text: data.message,
                                title: "消息"
                            });
                        }
                    }
                }).always(function () {
                    $("body").unMask();
                });
				},
			purviewFunCancelBTN : function(ev) {
				$(".page-purview-fun-panel").hide({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 400
				});
			},
			distributeUsersBTN : function(ev) {
			    var idAry = $(listId).jqGrid("getGridParam", "selarrrow");
			    $i = $(ev.currentTarget).find("i"),
			    $piel = $(".page-distribute-user-panel");
			    
			    if (idAry.length === 0) {
                    window.message({ text: "请选择需要分配用户的角色记录!", title: "提示" });
                    return;
                }
			    if (idAry.length > 1) {
					window.message({
						text : "每次只能维护单条角色!",
						title : "提示"
					});
					return;
				}
			    
			    $piel.show({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 600
				}).find("h4 i").removeClass();
			    
			    if ($i.length) {
					$piel.addClass($i.attr("class"));
				}
				
			    var users = getUsersByRoleId(idAry[0]);
			    var ids = users[0].join(",");
			    var names = users[1].join(";");

			    $("#ids").val(ids);
			    $("#userNames").val(names);
			    $("#roleId").val(idAry[0]);
			},
			distributeSaveBTN : function(ev){
				
				//为角色关联用户
				var roleId = $("#roleId").val();
				var ids = $("#ids").val();
				
				if(!ids) {
					window.Msg.alert("请选择要分配的用户!");
					return;
				}
				
				$(".page-distribute-user-panel").hide({
                    effect: "slide",
                    direction: "up",
                    easing: 'easeInOutExpo',
                    duration: 400
                });
				
		        var url="../platform/role.do?command=makeRoleUserMapping";
				$.post(url, { roleId: roleId, userIds: ids },function(data){
		            window.Msg.alert("分配成功!");
				});
			},
			distributeCancelBTN : function(ev) {
				$(".page-distribute-user-panel").hide({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 400
				});
			},
			purviewDataSaveBTN:function(ev)
			{
                var result=[];
				$("#dataTab tbody tr").find("input[type=checkbox]:checked").each(function(inex,item){
                     var $el=$(item),val=$el.val();
	  				  result.push(val);
					});
				$.ajax({
                    url: saveDataUrl, dataType: "JSON", type: "POST", data: { "roleId": editId, "datas": result }, success: function (data, xhr) {
                        if (data.error) {
                            window.error(data.error);
                            return;
                        }
                        $(".page-purview-data-panel").hide({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 400
                        });
                        if (data.message) {
                            window.message({
                                text: data.message,
                                title: "消息"
                            });
                        }
                    }
                }).always(function () {
                    $("body").unMask();
                });
				},
			purviewDataCancelBTN : function(ev) {
				$(".page-purview-data-panel").hide({
					effect : "slide",
					direction : "up",
					easing : 'easeInOutExpo',
					duration : 400
				});
			}
		});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		var _colModel = [
		    {name : 'id',key : true,width : 60,hidden : true},
			{name : 'roleName',width : 200}, 
			{name : 'createTime',width : 150,align:"center"},
			{name : 'remark',autoWidth : true}, 
			{name : 'status',width : 100,align : "center"} 
			], 
			_colNames = ['编号', '角色名称', '创建时间', '备注', '状态'];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId}));
		resizeFun();
		reloadFunctions();
		reloadDatas();
		$("#buTypeSearchSelect").chosen();
		$("#dataAuthoritySearch").button();


		$("#funTab thead input[type=checkbox]").change(function(){
			var checked=this.checked;
			     $("#funTab tbody tr:not(:hidden)").find("td:eq(1) input[type=checkbox]").each(function(index,item){
                   if( item.checked===checked){return;}
                   $(item).parent().next().next().find("input[type=checkbox]").each(function(cIndex,cItem){
                	   cItem.checked=checked;
                   });
                   item.checked=checked;
                   $(item).trigger("change");
				});
			});
		$("#dataTab thead input[type=checkbox]").change(function(){
			var checked=this.checked;
			     $("#dataTab tbody tr:not(:hidden)").find("input[type=checkbox]").each(function(index,item){
                   if( item.checked===checked){return;}
                   item.checked=checked;
                   $(item).trigger("change");
				});
			});
$("#menuSearch").bind("keypress",function(){
  var value=this.value;
  $("#funTab tbody tr").each(function(index,item){
           var t= $(item).find("td:eq(2):contains('"+value+"')");
           if(t.length){
               $(item).css("display"," table-row");
           }else{
               $(item).css("display","none");
                   }
	  });
});
$("#dataAuthoritySearch").click(function(){
	  	var buJdeSearch = $("#buJdeSearch").val();
	  	var buNameSearch = $("#buNameSearch").val();
	  	var buHyperionSearch = $("#buHyperionSearch").val();
	  	var buTypeSearch = $("#buTypeSearchSelect").data("chosen").selectedItem();
	  	if(buTypeSearch){
           buTypeSearch=buTypeSearch.text;
		  	}else{
           buTypeSearch="";
    	}
	  $("#dataTab tbody tr").each(function(index,item){
		  		var buNameCount= $(item).find("td:eq(2):contains('"+buNameSearch+"')");
	           var buJdeCount= $(item).find("td:eq(3):contains('"+buJdeSearch+"')");
	           var buHyperionCount= $(item).find("td:eq(4):contains('"+buHyperionSearch+"')");
	           var buTypeCount= $(item).find("td:eq(5):contains('"+buTypeSearch+"')");
	           if(buJdeCount.length&&buNameCount.length&&buHyperionCount.length&&buTypeCount.length){
	               $(item).css("display"," table-row");
	           }else{
	               $(item).css("display","none");
	                   }
		  });
	});	
		$("#chooseUsers").button();
	});
	
	//根据角色ID查找用户IDs
	function getUsersByRoleId(roleId) {
		
		var userIds = [];
		var names = [];
		
		var getUsersByIdUrl = "../platform/role.do?command=selectUsersByRoleId";
				
		$.ajax({
			async : false,
            url: getUsersByIdUrl,
            type: "GET",
            data: {
                roleId: roleId
            },
            dataType: "JSON",
            success : function (data, xhr) {
                if (data.error) {
                    window.error(data.error);
                    return;
                }
                
                $.each(data,function(index,value){
                	userIds.push(value.id);
                	names.push(value.chineseName);
                });
            }
        });
		
		var res = [];
		res.push(userIds);
		res.push(names);
		return res;
	}
	
	function showChooseUsersTree() {
		
		//如果在用户保存之前已经选择了一些人员，那么再次选择时，以前的人员要有数据回选
		var ids = $("#ids").val();
	    var url="";
	    if(ids!="undefined" && ids!=""){
	      url="common/choosePerson.do?command=init&idsParam="+ids+"&moduleType=organization";
	    }else{
	       url="common/choosePerson.do?command=init&moduleType=organization";
	    }
	   frameDialog(url, "请选择", {mode:"middle",resizable:false,width:400,height:550,buttons:[
			        { text:"确定", icons:{ primary:"ui-icon-check" },click : function( ev )
			        	{
				            var $this   = window.top.$( this ),
				                dial    = $this.find( "iframe" )[0].contentWindow ;
				            var rowData = dial.getData().split(";");
				            var rowIdData = rowData[1];
				            var rowNameData = rowData[0];
				            
				            if(rowIdData == ""){
			                    window.Msg.alert("请选择要分配的用户!");
			                    return;
			                }
				            
						    var names = rowNameData.split(",").join(";");

						    $("#ids").val(rowIdData);
						    $("#userNames").val(names);
						    
						    $this.dialog( "close" ) ;
						    
			        	}},
			        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
			       	 	{
				            var $this = window.top.$( this ) ;
				            $this.dialog( "close" ) ;
			        	}
			       	}
			     ]}); 
	   
	}
</script>
</head>
<body>
	<div class="page-list-panel">
		<div class="head-panel">
			<div class="search-panel">
				<div class="form-panel">
				<form id="searchForm">
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td>角色名称：</td>
							<td><input name="roleName" type="text"></td>
								<td style="padding-left: 12px;text-align:right;">状态：</td>
							<td><select data-xtype="chosen" name="status" style="width:154px;">
									<option value="">全部</option>
									<option value="1">启用</option>
									<option value="0">停用</option>
							</select></td>
							<td rowspan="1"
								style="padding-left: 30px;">
								<button type="button" id="advancedSearch">
									<i class="fa fa-search"></i>查询
								</button>
								<button type="button" id="searchRipClose" title="点击收起查询面板">
									<i class="fa  fa-angle-down" style="margin-right:0px;"></i>
								</button>
							</td>
						</tr>
						
					</table>
					</form>
				</div>
			</div>
			<div class="toolbar">
				<table style="height: 100%;" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td style="padding-left: 12px; padding-right: 24px;"><i
							class="fa fa-list-ul micon"></i></td>
						<td class="buttons">
							<button id="insertBTN">
								<i class="fa fa-plus"></i>添加
							</button>
							<button id="editBTN">
								<i class="fa fa-pencil"></i>修改
							</button>
							<button id="deleteBTN">
								<i class="fa fa-trash-o"></i>删除
							</button>
							<!-- <button>
								<i class="fa fa-paperclip"></i>导出
							</button> -->
							<button id="funPurviewBTN">
								<i class="fa fa-th-large"></i>功能权限
							</button>
							<button id="distributeUsersBTN">
								<i class="fa fa-list-alt"></i>分配用户
							</button>
						</td>
						<td style="padding-left: 24px; padding-right: 5px;"><input
							id="fastQueryText" type="text"
							style="line-height: 1em; font-size: 1em; cursor: text;" /></td>
						<td>
							<button id="fastSearch" title="查询" style="margin-left:0px;">
								<i class="fa fa-search" style="margin-right:0px;"></i>
							</button>
							<button id="searchRip" title="点击展开高级查询面板">
								<i class="fa fa-angle-up" style="margin-right:0px;"></i>
							</button>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<table id="list2"></table>
		<div id="pager2"></div>
	</div>
	<div class="page-editor-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="editorSave">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="resetBTN" style="margin-right: 24px;">
						<i class="fa fa-undo"></i>重置
					</button>
					<button id="cancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
				    <input name="id" type="hidden" />
					<table class="editorTable" cellpadding="0" cellspacing="0">
						<tr>
							<td class="labelTd"><label for="text_roleName">角色名称：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="roleName" id="text_roleName" class="form-control" /></td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="status">状态：</label></td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								id="statusON" type="radio" name="status" value="1"
								checked="checked"><label for="statusON">启用</label> 
								<input type="radio" id="statusOFF" name="status" value="0">
								<label for="statusOFF">停用</label>
								</td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd" style="vertical-align: top;"><label
								for="remark">描述：</label></td>
							<td class="inputTd" style="position:relative;z-index:1;"><textarea
									name="remark" style="width:300px;" rows="5"></textarea></td>
							<td class="messageTd"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<!--<div class="tool-bar"></div>-->
	</div>
	<!-- 功能权限 -->
	<div class="page-purview-fun-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class=""></i>维护功能权限
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button  id="purviewFunSaveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<button id="purviewFunCancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
			<table id="funTab" border="0" cellspacing="0" width="100%" cellpadding="3" align="center">
	                <thead>
	                <tr style="height:30px;">
	                  <td colspan='4' style="background-color: rgb(248, 248, 248);padding: 9px 0px;">
	                  <label for="menuSearch">筛选:</label><input type="text" id="menuSearch">
	                  </td>
	                </tr>
	                <tr style="height:30px;background-color: white;">
	                    <td align="center" style="width: 40px;"></td>
	                    <td align="center" style="width: 40px;"><input type="checkbox"></td>
	                    <td align="center" style="width: 350px;">菜单</td>
	                    <td align="center">按钮</td>
	                </tr>
	                </thead>
	                <tbody>
					</tbody>
        		</table>
			</div>
		</div>
	</div>
	<!-- 分配用户 -->
	<div class="page-distribute-user-panel full-drop-panel">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="distributeSaveBTN">
						<i class="fa fa-check"></i>保存
					</button>
					<!-- <button id="distributeResetBTN" style="margin-right: 24px;">
						<i class="fa fa-undo"></i>重置
					</button> -->
					<button id="distributeCancelBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorForm">
				    <input name="ids" id="ids" type="hidden" />
				    <input name="roleId" id="roleId" type="hidden" />
					<table class="editorTable" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td class="labelTd" style="vertical-align: top;">
								<label for="remark">分配用户列表：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;">
								<textarea readonly="readonly" name="userNames" id="userNames" style="width:500px;" rows="30"></textarea>
							</td>
							<td class="labelTd" style="vertical-align: top;">
								<button id="chooseUsers" style="margin-left: 20px;" onclick="showChooseUsersTree()">选择</button>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<!--<div class="tool-bar"></div>-->
	</div>
</body>
</html>