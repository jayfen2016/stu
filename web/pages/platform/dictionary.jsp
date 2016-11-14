<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript">
	var listId = "#list2", editorFormId = "#editorForm", pagerId = '#pager2',
	   loadUrl = "../platform/dictionary.do?command=load",
	   deleteUrl = "../platform/dictionary.do?command=delete",
	   saveUrl = "../platform/dictionary.do?command=submit", 
	   listUrl = "../platform/dictionary.do?command=search";

	$(function() {
		_initButtons({insertBTN:function(ev) {
			var $i=$(ev.currentTarget).find("i"),
			$piel= $(".page-editor-panel").show({
				effect:"slide",
				direction:"up",
				easing:'easeInOutExpo',
				duration:900
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
			   
			   $(editorFormId).resetHasXTypeForm({"parentDictionary":[{text:data.dictionaryName,value:data.id}]});
			}
			//jqGrid("getRowData","333")
			
		}});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		var _colModel = [
		    {name:'id',key:true,width:60,hidden:true,search:false},
		    {name : 'dictionaryName',width : 60,hidden : true,search:false},
			{name:'dictionaryDisplayName',index:false,width:250,title:true,sortable:false,resizable:false,search:false,formatter:function(value,colmodel,data,type){
			   var lev=parseInt(data.dictionaryLevel,10),name=data.dictionaryName,paddingLeft=(lev-1)*24+6,iconClass="fa-link";
			   if(lev>=2){
			   iconClass="fa-chain-broken";
			   }
			   return "<label style='padding-left:"+paddingLeft+"px;'><i class='fa "+iconClass+"' style='margin-right:3px;'></i>"+name+"</label>";
			}},
			{name:'dictionaryCode',width:250,align:"left",title:false,sortable:false,resizable:false,search:false},
			{name:'dictionaryType',width:120,align:"center",title:false,sortable:false,resizable:false,search:false},
			{name:'dictionaryModule',width:200,align:"left",hidden:true,title:false,sortable:false,resizable:false,search:false}, 
			{name:'remark',width:100,autoWidth:true,align:"left",title:true,sortable:false,resizable:false,search:false} 
			], 
			_colNames = ['编号','name', '字典项名称', '字典项代码', '字典项类型', '所属模块', '描述'];

		$(listId).jqGrid($.extend(defaultGridOpts, {url:listUrl,colNames:_colNames,colModel:_colModel,pager:pagerId}));
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
							<td>字典项名称：</td>
							<td><input name="dictionaryName" id="s_dictionaryName" type="text"></td>
								<td style="padding-left: 12px;text-align:right;">字典项代码：</td>
							<td><input name="dictionaryCode" id="s_dictionaryCode" type="text"></td>
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
							<td class="labelTd"><label for="text_menuName">字典项名称：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="dictionaryName" id="text_dictionaryName" class="form-control" /></td>
							<td class="messageTd"></td>
						</tr>
							<tr>
							<td class="labelTd"><label for="text_menuCode">字典项代码：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="dictionaryCode" id="text_dictionaryCode" class="form-control" /></td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="text_iconClass">字典项类型：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:3;">
							<select data-xtype="chosen" name="dictionaryType" style="width:300px;">
									<option></option>
									<option value="公共">公共</option>
									<option value="其他">其他</option>
							</select></td>
						</tr>
						<tr style="display:none;" >
							<td class="labelTd"><label for="text_iconClass">所属模块：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:2;">
							<select data-xtype="chosen" name="dictionaryModule" style="width:300px;">
									<option></option>
									<option value="主档">主档</option>
									<option value="差异分析调整及比对">差异分析调整及比对</option>
							</select></td>
						</tr>
						<tr>
							<td class="labelTd"><label for="pick1">上级字典项：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;">
							<input data-xtype="pick" data-url="pages/dialog/chooseDictionary.html" data-title="请选择上级字典项" data-dialogwidth="680" data-dialogheight="400" id="pick1" class="form-control" name="parentDictionary" />
								</td>
							<td class="messageTd"></td>
						</tr>
						<tr>
							<td class="labelTd" style="vertical-align: top;"><label
								for="p_remark">描述：</label></td>
							<td class="inputTd" style="position:relative;z-index:1;"><textarea  
									name="remark" id="p_remark" style="width:300px;" rows="5"></textarea></td>
							<td class="messageTd"></td>
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