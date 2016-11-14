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
	pagerId = '#pager2',
	listUrl = '../platform/businessLog.do?command=search';

	$(function() {
		_initButtons({});//from page.common.js
		_initFormControls();//from page.common.js
		
		var _colModel = [
		    {name : 'id',key:true,width:60,hidden:true,title:false,resizable:false,search:false},
			{name : 'operatorChineseName',width:100,align:"center",title:false,resizable:false,search:false}, 
			{name : 'operatorTMISAccount',width:100,align:"center",title:false,resizable:false,search:false},
			{name : 'operatorADAccount',width:100,align:"center",title:false,resizable:false,search:false},
			{name : 'module',width:100,title:false,resizable:false,search:false},
			{name : 'operateType',width:100,align:"center",title:false,resizable:false,search:false},
			{name : 'ip',width:100,align:"center",title:false,resizable:false,search:false},
			{name : 'operateTime',width:130,align:"center",resizable:false,search:false}, 
			{name : 'operateRemark',width:120,align:"center",title:false,resizable:false,search:false,autoWidth : true}
			], 
			_colNames = [ '业务日志编号', '操作人中文名称', '操作人TMIS账号', '操作人AD账号', '模块','操作类型', 'IP地址', '操作时间', '操作描述' ];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
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
							<td>操作人中文名称：</td>
							<td><input name="operatorChineseName" type="text"></td>
							<td style="padding-left: 12px;">操作人TMIS账号：</td>
							<td><input name="operatorTMISAccount" type="text"></td>
							<td style="padding-left: 12px;">操作人AD账号：</td>
							<td><input name="operatorADAccount" type="text"></td>
							<td rowspan="2"
								style="padding-left: 30px; vertical-align: bottom; padding-bottom: 4px;">
								<button type="button" id="advancedSearch">
									<i class="fa fa-search"></i>查询
								</button>
								<button type="button" id="searchRipClose" title="点击收起查询面板">
									<i class="fa  fa-angle-down" style="margin-right:0px;"></i>
								</button>
							</td>
						</tr>
						<tr>
							<td style="padding-left: 12px;">操作时间：</td>
							<td><input data-xtype="datetime" name="startDate" style="width:80px;"/>
							至<input data-xtype="datetime" name="endDate" style="width:80px;"></td>
							<td style="padding-left: 12px;text-align:right;">操作类型：</td>
							<td><select data-xtype="chosen" name="operateType" style="width:154px;">
									<option value="">全部</option>
									<option value="LOGIN">登陆</option>
									<option value="ADD">新增</option>
									<option value="EDIT">编辑</option>
									<option value="QUERY">查询</option>
									<option value="DELETE">删除</option>
									<option value="TAX">税务操作</option>
									<option value="OTHER">其他</option>
							</select></td>
							<td style="padding-left: 12px;text-align:right;">模块：</td>
							<td><input name="module" type="text"></td>
							<td></td>
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
							<button>
								<i class="fa fa-paperclip"></i>导出
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

	<div class="page-view-panel full-drop-panel"></div>
</body>
</html>