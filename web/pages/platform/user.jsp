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
    <link href="../theme/default/page.common.css" rel="stylesheet" />
    <link href="../theme/default/ui.pick.css" rel="stylesheet" />
    <script type="text/javascript" src="../js/jquery.js"></script>
    <script type="text/javascript" src="../js/ui.custom.js"></script>
    <script type="text/javascript" src="../js/ui.jqgrid.js"></script>
    <script type="text/javascript" src="../js/ui.autosearch.js"></script>
    <script type="text/javascript" src="../js/ui.chosen.js"></script>
    <script type="text/javascript" src="../js/ui.uploadfile.js"></script>
    <script type="text/javascript" src="../js/ui.common.js"></script>
    <script type="text/javascript" src="../js/jquery.validate.js"></script>
    <script type="text/javascript" src="../js/ui.pick.js"></script>
    <!--<script type="text/javascript" src="../js/jquery.metadata.js"></script>-->
    <script type="text/javascript" src="../js/page.common.js"></script>
    <!--[if IE 7]>
             <link href="../theme/default/font.awesome.ie7.css" rel="stylesheet" />
             <link href="../theme/default/page.common.ie7.css" rel="stylesheet" />
        <![endif]-->
    <style>
        #purfo {
            margin-left: auto;
            margin-right: auto;
            width: 400px;
            margin-top: 24px;
            line-height: 28px;
        }

            #purfo input {
                margin: 0px;
                position: relative;
                top: 2px;
            }

            #purfo label {
                margin-left: 5px;
            }
            #orgTree td{
            	height: 40px;
            	line-height: 40px;
            	font-size:20px;
            }
    </style>
    <script type="text/javascript">
        var listId = "#list2",
        editorFormId = "#editorForm",
        pagerId = '#pager2',
        loadUrl = "../platform/user.do?command=load",
        deleteUrl = "../platform/user.do?command=delete",
        saveUrl = "../platform/user.do?command=submit",
        listUrl = '../platform/user.do?command=search',
        loadRoleUrl = "../platform/user.do?command=initRoles",
        getRoleUrl = "../platform/user.do?command=getRole",
        saveRoleUrl = "../platform/user.do?command=saveRoles",
        changePwdUrl = "../platform/user.do?command=changePwd",
        resetPwdUrl=   "../platform/user.do?command=resetPwd",
        loadOrgUrl= "../platform/organization.do?command=getTree";
        var _insert=function(ev)
    	{
    		$(".editorTable tr:eq(4)").css("display","");
    		$(".editorTable tr:eq(4) input").attr("data-validate","{required:true}");
    	};
    	var _edit=function(ev)
    	{
    	};
        var reloadRoles = function () {
        	 $.getJSON(loadRoleUrl,
                     function (data) {
                         $("#purviewSaveBTN").button("enable");
                         var $purfo = $(".page-purview-panel #purfo"), inputAry = [];
                         for (var i in data) {
                             var tda = data[i], el = ["&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input id='purfo_", i, "' type='checkbox' value='", tda.value, "'>", "&nbsp;<label for='purfo_", i, "'>", tda.text, "</label></br>"].join('');
                             inputAry.push(el);
                         }
                         $purfo.append(inputAry);
                     });
        };
        function selectRole(el)
        {
            var $item = $(el);
            var nextItem = $item.parent().next().find("input[type=radio]");
            var checked = $item.prop("checked");
            if(checked)
            {
            	nextItem.removeAttr("disabled");
            }
            else
            {
            	nextItem.prop("checked",false);
            	nextItem.attr("disabled","disabled");
            }
        };
        
        function loadOrg()
        {
        	 $.getJSON(loadOrgUrl,function (data) {
        		 data = data[0].children;
        		 for(var k in data)
        		 {
        			 var str="<tr class='orgRow'><td><input name='selectOrg' type='checkbox' style='margin-top:20px;' onclick='setSort(this);' value='"+data[k].id+"'></input>"+
		        			 "<lable style='margin-right:30px;'>"+data[k].label+"</lable></td>"+
		        			 "<td>排序:<input id='input_"+data[k].id+"' disabled='disabled' style='margin-left:10px;'/></td></tr>" ;
		        			 $("#orgTree table").append(str);
        		 }
             });
        }
        function setSort(obj)
        {
        	$("#input_"+$(obj).val()).val("");
        	
        	if($(obj).prop("checked"))
        	{
        		$("#input_"+$(obj).val()).attr("disabled",false);
        	}else{
        		$("#input_"+$(obj).val()).attr("disabled","disabled");
        	}
        }
        
        $(function () {
        	loadOrg();
            _initButtons({
                updatePwdBTN: function (ev) {
                    var idAry = $(listId).jqGrid("getGridParam", "selarrrow"), $i = $(ev.currentTarget).find("i"), 
                    $piel = $("#resetUserPwdId");
                    if (idAry.length === 0) {
                        window.message({ text: "请选择要维护用户的记录!", title: "提示" });
                        return;
                    }
                    $("#hiddenUserId").val(idAry);
                        $piel = $piel.show({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 600
                        }).find("h4 i").removeClass();
                        if ($i.length) { $piel.addClass($i.attr("class")); }
                },resetPwd:function (ev) {
                	//某个用户密码重置
                    var $piel = $("#resetUserPwdId");
                    var newPwd=$("input[name=newPasswd]").val();
                    var ids=$("#hiddenUserId").val();
                     $.ajax({
                        url: resetPwdUrl,
                        type: "POST",
                        data: {
                            id: ids,
                            newPwd:newPwd
                        },
                        dataType: "JSON",
                        success: function (data, xhr) {
                            if (data.error) {
                                window.error(data.error);
                                return;
                            }
                            $piel = $piel.hide({
                                effect: "slide",
                                direction: "up",
                                easing: 'easeInOutExpo',
                                duration: 600
                            });

                            if (data.message) {
                                window.message({
                                    text: data.message,
                                    title: "消息"
                                });
                            }
                        }
                    });
                },
                purviewBTN: function (ev) {
                    var idAry = $(listId).jqGrid("getGridParam", "selarrrow"), $i = $(ev.currentTarget).find("i"), $piel = $(".page-purview-panel");
                    if (idAry.length === 0) {
                        window.message({ text: "请选择要维护角色的记录!", title: "提示" });
                        return;
                    }
                    window.needEditIDS = idAry;
                    if (idAry.length !== 1) {
                        $piel.find("#purfo").find("input[type=checkbox],input[type=radio]").each(function (inde, item) {
                            item.checked = false;
                        });
                        $piel = $piel.show({
                            effect: "slide",
                            direction: "up",
                            easing: 'easeInOutExpo',
                            duration: 600
                        }).find("h4 i").removeClass();
                        if ($i.length) { $piel.addClass($i.attr("class")); }
                        return;
                    }

                    $.ajax({
                        url: getRoleUrl,
                        type: "GET",
                        data: {
                            id: idAry[0]
                        },
                        dataType: "JSON",
                        success: function (data, xhr) {
                            if (data.error) {
                                window.error(data.error);
                                return;
                            }
                            var tdata = data.roles;
                            $piel.find("#purfo").find("input[type=checkbox]").each(function (inde, item) {
                                var needCheck = false;
                                for (var i in tdata) {
                                    if (tdata[i].RoleID === item.value) {
                                        needCheck = true;
                                    }
                                }
                                item.checked = needCheck;
                            });

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
                purviewSaveBTN: function (ev) {
                    $("body").mask();
                    var roles = [];
                    $("#purfo").find("input[type=checkbox]:checked").each(function (index, item) {
                    	roles.push(item.value);
                    });
                    $.ajax({
                        url: saveRoleUrl, dataType: "JSON", type: "POST", data: { "users": needEditIDS, "roles": roles }, success: function (data, xhr) {
                            if (data.error) {
                                window.error(data.error);
                                return;
                            }
                            $(".page-purview-panel").hide({
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
                purviewCancelBTN: function (ev) {
                    $(".page-purview-panel").hide({
                        effect: "slide",
                        direction: "up",
                        easing: 'easeInOutExpo',
                        duration: 400
                    });
                },
                pwdCancelBTN: function (ev) {
                    $("#resetUserPwdId").hide({
                        effect: "slide",
                        direction: "up",
                        easing: 'easeInOutExpo',
                        duration: 400
                    });
                }
                
            });//from page.common.js
            _initFormControls();//from page.common.js
            _initValidateForXTypeForm(editorFormId);

            var _colModel = [{
                name: 'id',
                key: true,
                width: 60,
                hidden: true,
                title: false,
                resizable: false,
                search: false
            }, {
                name: 'chineseName',
                width: 65,
                title: false,
                resizable: false,
                search: false
            },
            {
                name: 'loginAccount',
                width: 90,
                title: false,
                resizable: false,
                search: false
            },
            {
                name: 'organizationName',
                width: 90,
                title: false,
                resizable: false,
                search: false,
                sortable:false
            },{
                name: 'roles',
                width: 285,
                title: false,
                resizable: false,
                search: false, 
                sortable:false
            } ,{
                name: 'officePhone',
                width: 90,
                align: "center",
                title: false,
                resizable: false,
                search: false
            }, {
                name: 'mobile',
                width: 90,
                align: "center",
                title: false,
                resizable: false,
                search: false
            }, {
                name: 'officeMail',
                width: 145,
                resizable: false,
                search: false
            }, {
                name: 'lastLoginTime',
                width: 120,
                align: "center",
                title: false,
                resizable: false,
                search: false
            }, {
                name: 'status',
                width: 70,
                align: "center",
                title: false,
                resizable: false,
                search: false
            }], _colNames = ['用户编号', '姓名', '账号', '所属部门','用户角色',
                    '办公电话', '手机', '办公邮箱', '最后登录时间', '状态'];

            $(listId).jqGrid($.extend(defaultGridOpts, {
                url: listUrl,
                colNames: _colNames,
                colModel: _colModel,
                pager: pagerId
            }));
            resizeFun();
            reloadRoles();
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
                                <td>姓名：</td>
                                <td><input name="chineseName" type="text"></td>
                                <td style="padding-left: 12px;">职务：</td>
                                <td><input name="englishName" type="text"></td>
                                <td style="padding-left: 12px;">账号：</td>
                                <td><input name="loginAccount" type="text"></td>
                                <td style="padding-left: 12px;">所属部门：</td>
                                <td>
                                	<select data-xtype="ajaxchosen" id="s_organization" name="parentOrganization" style="width:154px;" data-url="../platform/user.do?command=searchTerm" data-placeholder="请输入部门名称">
                                    	<option></option>
                                	</select>
                                </td>
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
                                <td style="text-align:right;">办公电话：</td>
                                <td><input name="officePhone" type="text"></td>
                                <td style="padding-left: 12px;text-align:right;">手机：</td>
                                <td><input name="mobile" type="text"></td>
                                <td style="padding-left: 12px;text-align:right;">办公邮箱：</td>
                                <td><input name="officeMail" type="text"></td>
                                <td style="padding-left: 12px;text-align:right;">状态：</td>
                                <td>
                                    <select data-xtype="chosen" name="status" style="width:154px;">
                                        <option value="">全部</option>
                                        <option value="1">启用</option>
                                        <option value="0">停用</option>
                                    </select>
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
                        <td style="padding-left: 12px; padding-right: 24px;">
                            <i class="fa fa-list-ul micon"></i>
                        </td>
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
                           <!--  <button>
                                <i class="fa fa-paperclip"></i>导出
                            </button> -->
                            <button id="purviewBTN">
                                <i class="fa fa-paperclip"></i>角色
                            </button>
                            <button id="updatePwdBTN">
                                <i class="fa fa-paperclip"></i>重置密码
                            </button>
                        </td>
                        <td style="padding-left: 24px; padding-right: 5px;">
                            <input id="fastQueryText" type="text"
                                   style="line-height: 1em; font-size: 1em; cursor: text;" />
                        </td>
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
                            <td class="labelTd">
                                <label for="chineseName">姓名：</label>
                            </td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text" data-validate="{required:true}"
                                       name="chineseName" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                         <tr>
                            <td class="labelTd"><label for="status">性别：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input id="sexM" type="radio" name="sex" value="1" checked="checked"><label for="sexM">男</label>
                                <input type="radio" id="sexW" name="sex" value="2" <c:if test="${sex=='2'}">checked='checked'</c:if>>
                                <label for="sexW">女</label>
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                   <%--      <tr>
                            <td class="labelTd">
                                <label for="position">职务：</label>
                            </td>
							<td class="inputTd" style="position:relative;z-index:3;">
							<select data-xtype="chosen" name="position" style="width:300px;">
									 <option value=""></option>
									<c:forEach items="${positionList}" var="position" >
									   <option value="${position.id}">${position.dictionaryName}</option>
									</c:forEach>
							</select></td>
                        </tr> --%>
                        
                        <tr>
                            <td class="labelTd"><label for="loginAccount">系统账号：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text" data-validate="{required:true}"
                                       name="loginAccount" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                         
                         
                        <tr>
                            <td class="labelTd"><label for="loginPassword">密码：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="password" type="password" data-validate="{required:true}"
                                       name="loginPassword" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="pick1">所属部门：</label></td>
                            <td class="inputTd" style="position:relative;z-index:2;">
                                <input data-xtype="pick" data-url="pages/dialog/chooseOrganization.html" data-validate="{pickRequired:true}" data-errormessage="请选择所属部门" data-title="请选择上级组织机构" data-dialogwidth="680" data-dialogheight="400" id="pick1" class="form-control" name="parentOrganization" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="officePhone">办公电话：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text" 
                                       name="officePhone" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="mobile">手机：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text"  name="mobile"
                                       class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="officeMail">办公邮箱：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="email" 
                                       name="officeMail" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="seqNums">排序：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input data-xtype="text"
                                       name="seqNums" class="form-control" />
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd"><label for="status">状态：</label></td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <input id="statusON" type="radio" name="status" value="1"
                                       checked="checked"><label for="statusON">启用</label>
                                <input type="radio" id="statusOFF" name="status" value="0">
                                <label for="statusOFF">停用</label>
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                        <tr>
                            <td class="labelTd" style="vertical-align: top;">
                                <label for="remark">描述：</label>
                            </td>
                            <td class="inputTd" style="position:relative;z-index:1;">
                                <textarea name="remark" style="width:300px;" rows="5"></textarea>
                            </td>
                            <td class="messageTd"></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <!--<div class="tool-bar"></div>-->
    </div>
    <div class="page-purview-panel full-drop-panel">
        <div class="title-bar">
            <h4>
                <i class=""></i>维护角色
            </h4>
            <div class="btn-area">
                <div style="margin-top: 4px;">
                    <button disabled="disabled" id="purviewSaveBTN">
                        <i class="fa fa-check"></i>保存
                    </button>
                    <button id="purviewCancelBTN">
                        <i class="fa fa-times"></i>取消
                    </button>
                </div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-inner-content">
                <div id="purfo"></div>
            </div>
        </div>
    </div>
    
    <div class="full-drop-panel" id="resetUserPwdId">
        <div class="title-bar">
            <h4>
                <i class=""></i>重置密码
            </h4>
            <div class="btn-area">
                <div style="margin-top: 4px;">
                    <button  id="resetPwd">
                        <i class="fa fa-check"></i>重置
                    </button>
                    <button id="pwdCancelBTN">
                        <i class="fa fa-times"></i>取消
                    </button>
                </div>
            </div>
        </div>
        <div class="page-content">
            <div class="page-inner-content">
                <form id="editorFormOther" style="margin-top:20px;">
                <table  align="center" cellpadding="0" cellspacing="0" style="border-spacing:10px; ">
                    <tr>
                        <td class="labelTd" style="text-align:right;"><label for="newPasswd"><font style="color:#ff0000;">*</font>新密码：</label></td>
                        <td class="inputTd">
                            <input type="password"  style="text-align:left;" name="newPasswd" value="1"  class="form-control" />
                            <input type="hidden"  id="hiddenUserId"/>
                        </td>
                         <td class="messageTd"></td>
                    </tr>
                </table>
            </form>
            </div>
        </div>
    </div>
    
    
    
</body>
</html>