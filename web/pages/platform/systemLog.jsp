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
	listUrl = '../platform/systemLog.do?command=search';

	$(function() {
		_initButtons({});//from page.common.js
		_initFormControls();//from page.common.js
		
		var _colModel = [
		    {name : 'id',key:true,width:60,hidden:true,title:false,resizable:false,search:false},
			{name : 'logTime',width:200,align:"center",title:false,resizable:false,search:false}, 
			{name : 'logType',width:150,align:"center",title:false,resizable:false,search:false},
			{name : 'source',width:200,title:false,resizable:false,search:false,autoWidth:true},
			{name : 'remark',width:100,title:true,resizable:false,search:false,autoWidth:true,formatter:remarkFormat}
			], 
			_colNames = [ '编号', '日志时间', '日志类型', '日志来源', '日志描述' ];

		$(listId).jqGrid($.extend(defaultGridOpts, {
			url : listUrl,
			colNames : _colNames,
			colModel : _colModel,
			pager : pagerId
		}));
		resizeFun();
	});
	function remarkFormat( cellvalue, options, rowObject )
	{
		var str = "";
		if( cellvalue.length>50 )
			str = cellvalue.substring(0,50)+"...";
	    return '<span title="'+cellvalue+'" />'+str+'...</span>';  
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
							<td>日志描述：</td>
							<td><input name="remark" type="text"></td>
							<td style="padding-left: 12px;">日志类型：</td>
							<td><select data-xtype="chosen" name="logType" style="width:154px;">
									<option value="">全部</option>
									<option value="INFO">INFO</option>
									<option value="DEBUG">DEBUG</option>
									<option value="WARN">WARN</option>
									<option value="ERROR">ERROR</option>
							</select></td>
							<td style="padding-left: 12px;">日志时间：</td>
							<td><input data-xtype="datetime" name="startDate" style="width:80px;"/> 
							至 <input data-xtype="datetime" name="endDate" style="width:80px;"/>
							<td rowspan="1"
								style="padding-left: 30px; vertical-align: bottom; padding-bottom: 4px;">
								<button type="button" id="advancedSearch">
									<i class="fa fa-search"></i>查询
								</button>
								<button type="button" id="searchRipClose" title="点击收起查询面板">
									<i class="fa fa-angle-down" style="margin-right:0px;"></i>
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
</body>
</html>