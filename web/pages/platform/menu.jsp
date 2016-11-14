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
                    loadUrl = "../platform/menu.do?command=load",
                    deleteUrl = "../platform/menu.do?command=delete",
                    saveUrl = "../platform/menu.do?command=submit",
                    listUrl = "../platform/menu.do?command=search";

            $(function() {
                _initButtons({insertBTN: function(ev) {
                        var $i = $(ev.currentTarget).find("i"),
                                $piel = $(".page-editor-panel").show({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 900
                        }).find("h4 i").removeClass();
                        if ($i.length) {
                            $piel.addClass($i.attr("class"));
                        }
                        window._EDITDATA = undefined;
                        var $grid = $(listId), idAry = $grid.jqGrid("getGridParam", "selarrrow");
                        if (idAry.length === 0) {
                            $(editorFormId).resetHasXTypeForm();
                        } else {
                            var data = $grid.jqGrid("getRowData", idAry[0]);

                            $(editorFormId).resetHasXTypeForm({"parentMenu": [{text: data.menuName, value: data.id}]});
                        }
                        //jqGrid("getRowData","333")

                    }});//from page.common.js
                _initFormControls();//from page.common.js
                _initValidateForXTypeForm(editorFormId);

                var _colModel = [
                    {name: 'id', key: true, width: 60, hidden: true, search: false, sortable: false},
                    {name: 'menuName', width: 60, hidden: true, search: false, sortable: false},
                    {name: 'menuDisplayName', index: "menuName", width: 300, title: true, sortable: false, resizable: false, search: false, formatter: function(value, colmodel, data, type) {
                            var lev = parseInt(data.menuLevel, 10), name = data.menuName, paddingLeft = (lev - 1) * 24 + 6, iconClass = "fa-link";
                            if (lev >= 2) {
                                iconClass = "fa-chain-broken";
                            }
                            return "<label style='padding-left:" + paddingLeft + "px;'><i class='fa " + iconClass + "' style='margin-right:3px;'></i>" + name + "</label>";
                        }},
                    {name: 'menuCode', width: 120, align: "left", title: false, resizable: false, search: false, sortable:false},
                    {name: 'menuLevel', width: 68, align: "center", title: false,  resizable: false, sortable:false, search: false, formatter: function(value) {
                            switch (value) {
                                case "1":
                                    return "一级";
                                case "2":
                                    return "二级";
                                case "3":
                                    return "三级";
                                case "4":
                                    return "四级";
                                default:
                                    return value + "级";
                            }
                        }},
                    {name: 'menuAddress', autoWidth: true, title: false, resizable: false, search: false, sortable:false},
                    {name: 'status', width: 100, align: "center", title: false, resizable: false, search: false, sortable:false}
                ],
                        _colNames = ['编号', 'name', '菜单名称', '菜单代码', '菜单级别', '路径', '状态'];

                $(listId).jqGrid($.extend(defaultGridOpts, {url: listUrl, colNames: _colNames, colModel: _colModel, pager: pagerId}));
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
                                    <td>菜单名称：</td>
                                    <td><input name="menuName" id="s_menuName" type="text"></td>
                                    <td style="padding-left: 12px;text-align:right;">状态：</td>
                                    <td><select data-xtype="chosen" name="status" id="s_status" style="width:154px;">
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
                                <td class="labelTd"><label for="text_menuName">菜单名称：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;"><input
                                        data-xtype="text" data-validate="{required:true}" 
                                        name="menuName" id="text_menuName" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="text_menuCode">菜单代码：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;"><input
                                        data-xtype="text" data-validate="{required:true}" 
                                        name="menuCode" id="text_menuCode" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="text_iconClass">样式：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;"><input
                                        data-xtype="text"
                                        name="iconClass" id="text_iconClass" class="form-control" /></td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="pick1">上级菜单：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;">
                                    <input data-xtype="pick" data-url="pages/dialog/chooseMenu.html" data-title="请选择上级菜单" data-dialogwidth="680" data-dialogheight="400" id="pick1" class="form-control" name="parentMenu" />
                                </td>
                                <td class="messageTd"></td>
                            </tr>
                            <tr>
                                <td class="labelTd"><label for="text_address">菜单路径：</label>
                                </td>
                                <td class="inputTd" style="position:relative;z-index:1;">
                                    <input
                                        data-xtype="text"
                                        name="menuAddress" id="text_address" class="form-control" />
                                </td>
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