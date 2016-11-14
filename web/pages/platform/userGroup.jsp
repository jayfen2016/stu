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
<script type="text/javascript">
	var listId = "#list2", 
	editorFormId = "#editorForm", 
	pagerId = '#pager2', 
	loadUrl = "../platform/userGroup.do?command=load", 
	deleteUrl = "../platform/userGroup.do?command=delete", 
	saveUrl = "../platform/userGroup.do?command=submit", 
	listUrl = "../platform/userGroup.do?command=search";

	$(function() {
		_initButtons({});//from page.common.js
		_initFormControls();//from page.common.js
		_initValidateForXTypeForm(editorFormId);
		
		var _colModel = [
		    {name : 'id',key : true,width : 60,hidden : true},
			{name : 'groupName',width : 250}, 
			{name : 'createTime',width : 200,align:"center"},
			{name : 'remark',autoWidth : true}, 
			{name : 'status',width : 100,align : "center"} 
			], 
			_colNames = ['编号', '用户组名称', '创建时间', '描述', '状态'];

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
							<td>用户组名称：</td>
							<td><input name="groupName" type="text"></td>
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
							<button>
								<i class="fa fa-paperclip"></i>导出
							</button>
							<button id="funPurviewBTN">
								<i class="fa fa-th-large"></i>用户
							</button>
							<button id="funPurviewBTN">
								<i class="fa fa-th-large"></i>角色
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
							<td class="labelTd"><label for="text_groupName">用户组名称：</label>
							</td>
							<td class="inputTd" style="position:relative;z-index:1;"><input
								data-xtype="text" data-validate="{required:true}" 
								name="groupName" id="text_groupName" class="form-control" /></td>
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
	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>