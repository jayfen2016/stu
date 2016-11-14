<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<link href="../theme/default/ui.pick.css" rel="stylesheet" />
<link href="../theme/default/page.common.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/ui.custom.js"></script>
<script type="text/javascript" src="../js/ui.jqgrid.js"></script>
<script type="text/javascript" src="../js/ui.autosearch.js"></script>
<script type="text/javascript" src="../js/ui.chosen.js"></script>
<script type="text/javascript" src="../js/ui.uploadfile.js"></script>
<script type="text/javascript" src="../js/ui.pick.js"></script>
<script type="text/javascript" src="../js/ui.common.js"></script>
<script type="text/javascript" src="../js/jquery.validate.js"></script>
<!--<script type="text/javascript" src="../js/jquery.metadata.js"></script>-->
<script type="text/javascript" src="../js/page.common.js"></script>
<!--[if IE 7]>
         <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
         <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
    <![endif]-->
<style>
 .customStyle{
		background:#FFFFFF;
		border:1px solid #3babe3;
		color:#3babe3;
		cursor:pointer;
		font-weight: bold;
		padding: .4em 1em;
	}
	.customStyle:hover{
		color:#FFFFFF;
		background:#3babe3;
		}
</style>
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',editorRelatedFormId = "#editorRelatedForm",
	   loadUrl = "../platform/organization.do?command=load",
	   loadRelatePersonUrl = "../platform/organization.do?command=loadRelatePersonUrl",
	   deleteUrl = "../platform/organization.do?command=delete",
	   saveUrl = "../platform/organization.do?command=submit", 
	   listUrl = "../platform/organization.do?command=search",
	   saveRelatePersonUrl = "../platform/organization.do?command=saveRelatePerson";
	   
    var getSystemAdmin=function(currentObj){
       var idsParam=$(currentObj).parent().prev().find("input[type=hidden]").val();
       var url="";
       if(idsParam!="" && idsParam!="undefined"){
         url="common/choosePerson.do?command=init&orgId="+$("#orgId").val()+"&orgName="+$("#organizationName").val()+"&idsParam="+idsParam+"&moduleType=organization&isNeedAllOrg=yes";
       }else{
         url="common/choosePerson.do?command=init&orgId="+$("#orgId").val()+"&orgName="+$("#organizationName").val()+"&moduleType=organization&isNeedAllOrg=yes";
       }
	   
	   frameDialog(url, "请选择", {mode:"middle",resizable:false,width:400,height:550,buttons:[
			        { text:"确定", icons:{ primary:"ui-icon-check" },click:function( ev )
			        {
			             var $this   = window.top.$( this ),
			                dial    = $this.find( "iframe" )[0].contentWindow ;
			            var rowData = dial.getData().split(";");
			            var rowIdData = rowData[1];
			            var rowNameData = rowData[0];
			            var tempId=rowIdData.split(",");
			           
			             if(rowIdData==""){
		                    window.Msg.alert("请选择要关联的人!");
		                    return;
		                 }
		                 if(tempId.length>=2){
		                    window.Msg.alert("只能关联一个人!");
		                    return;
		                 }
		                $(currentObj).parent().prev().find("input[type=text]").val(rowNameData);
			            $(currentObj).parent().prev().find("input[type=hidden]").val(rowIdData);
			            $this.dialog( "close" ) ;
                      
			        }},
			        { text:"返回", icons:{ primary:"ui-icon-cancel" }, click:function( ev )
			        {
			            var $this = window.top.$( this ) ;
			            $this.dialog( "close" ) ;
			        }}
			     ]});
	       };
	       
	$(function() {
		_initButtons({
		    insertBTN : function(ev) {
			var $i=$(ev.currentTarget).find("i"),
			$piel= $(".page-editor-panel").show({
				effect : "slide",
				direction : "up",
				easing : 'easeInOutExpo',
				duration : 900
			}).find("h4 i").removeClass();
			if($i.length){
				$piel.addClass($i.attr("class"));
			}
			window._EDITDATA = undefined;
			var $grid=$(listId), idAry = $grid.jqGrid("getGridParam", "selarrrow");
			if (idAry.length === 0) {
			   $(editorFormId).resetHasXTypeForm();
			}else{
			   var data= $grid.jqGrid("getRowData",idAry[0]);
			   
			   $(editorFormId).resetHasXTypeForm({"parentOrganization":[{text:data.organizationName,value:data.id}]});
			}
		},
		saveRelatePerson : function() {
			if ($(editorRelatedFormId + " [data-validate]").valid()) {
			    var orgId=$("#orgId").val();
			    var orgName=$("#organizationName").val();
			    var parmAry=new Array();
			    parmAry.push(orgId);
			    parmAry.push(orgName);
			    $("#dynamicTab").find("tr:gt(0)").each(function(index,item){
			      var paramObj=new Object;
			      var $this=$(item);
			      
			      var signId=$this.find("td:last").find("input").val();
			      
			      var $firstTd=$this.find("td:eq(0)");
			      var moduleName=$firstTd.find("label").text().split("：")[0];
			      var moduleId=$firstTd.find("input").val();
			      
			      var $secondTd=$this.find("td:eq(1)");
			      var signPersonName=$secondTd.find("input[type=text]").val();
			      var signPersonId=$secondTd.find("input[type=hidden]").val();
			      
			      paramObj.signId=signId;
			      paramObj.moduleName=moduleName;
			      paramObj.moduleId=moduleId;
			      paramObj.signPersonName=signPersonName;
			      paramObj.signPersonId=signPersonId;
			      parmAry.push(paramObj);
			    });
				POST(saveRelatePersonUrl,{"parameter":parmAry},function(data){
					hideSlidePanel("#relateId");
				});
			}
		},
		cancelRelateBTN : function() {
			hideSlidePanel("#relateId,.page-view-panel");
		}
		});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		var _colModel = [
		    {name : 'id',key : true,width : 60,hidden : true,search:false},
		    {name : 'organizationName',width : 60,hidden : true,search:false},
			{name : 'organizationDisplayName',width : 300,title:true,sortable:true,resizable:false,search:false,formatter:function(value,colmodel,data,type){
			   var lev=parseInt(data.orgLevel,10),name=data.organizationName,paddingLeft=(lev-1)*24+6,iconClass="fa-link";
			   if(lev>=2){
			   iconClass="fa-chain-broken";
			   }
			   return "<label style='padding-left:"+paddingLeft+"px;'><i class='fa "+iconClass+"' style='margin-right:3px;'></i>"+name+"</label>";
			}}, 
			{name : 'organizationCode',width : 120,align:"left",title:false,sortable:true,resizable:false,search:false},
			{name : 'orgLevel',width : 130,align:"center",title:false,sortable:true,resizable:false,search:false,formatter:function(value){
			   switch(value){
			   case "1":
			       return "一级"
			     case "2":
			       return "二级"
			     case "3":
			       return "三级"
			     case "4":
			       return "四级"
			     default:
			      return value+"级";
			   }
			}},
			{name : 'remark',autoWidth : true,title:false,sortable:true,resizable:false,search:false}, 
			], 
			_colNames = ['编号', 'name', '组织机构名称', '组织机构代码','组织机构级别', '描述'];

		$(listId).jqGrid($.extend(defaultGridOpts, {url : listUrl,colNames : _colNames,colModel : _colModel,pager : pagerId}));
		resizeFun();
	});
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
							<td>组织机构名称：</td>
							<td><input name="organizationName" type="text"></td>
								<td style="padding-left: 12px;text-align:right;">组织机构代码：</td>
							<td><input name="organizationCode" type="text"></td>
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
							<td class="labelTd"><label for="text_organizationName">组织机构名称：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="organizationName" id="text_organizationName" class="form-control" /></td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="text_organizationCode">组织机构代码：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="organizationCode" id="text_organizationCode" class="form-control" /></td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="pick1">上级组织机构：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;">
							<input data-xtype="pick" data-url="pages/dialog/chooseOrganization.html" data-title="请选择上级组织机构" data-dialogwidth="680" data-dialogheight="400" id="pick1" class="form-control" name="parentOrganization" />
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
	
	
	<div class="full-drop-panel" id="relateId">
		<div class="title-bar">
			<h4>
				<i class="fa fa-plus"></i>
			</h4>
			<div class="btn-area">
				<div style="margin-top: 4px;">
					<button id="saveRelatePerson">
						<i class="fa fa-check"></i>保存
					</button>
					<!-- <button id="resetBTN" style="margin-right: 24px;">
						<i class="fa fa-undo"></i>重置
					</button> -->
					<button id="cancelRelateBTN">
						<i class="fa fa-times"></i>取消
					</button>
				</div>
			</div>
		</div>
		<div class="page-content">
			<div class="page-inner-content">
				<form id="editorRelatedForm">
				    <input name="id" type="hidden" />
				    <input name="signId" type="hidden" id="signId"/>
					<table class="editorTable" cellpadding="0" cellspacing="0" id="dynamicTab">
						<tr>
						    <input type="hidden" id="orgId"/>
							<td class="labelTd"><label for="text_organizationName">组织机构名称：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" readonly="readonly"
								name="organizationName" id="organizationName" class="form-control" /></td>
							<td class="messageTd" colspan="2"></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<!--<div class="tool-bar"></div>-->
	</div>
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>