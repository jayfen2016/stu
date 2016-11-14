<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
       <%-- <link rel="shortcut icon" href="theme/default/icon/" />--%>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link href="theme/default/page.common.css" rel="stylesheet" />
        <link href="theme/default/ui.custom.css" rel="stylesheet" />
        <link href="theme/default/ui.jqgrid.css" rel="stylesheet" />
        <link href="theme/default/font.awesome.css" rel="stylesheet" />
        <link href="theme/default/ui.pagedmenu.css" rel="stylesheet" />
        <link href="theme/default/ui.dropdown.css" rel="stylesheet" />
        <link href="theme/default/ui.accordionmenu.css" rel="stylesheet" />
        <link href="theme/default/ui.common.css" rel="stylesheet" />
        <link href="theme/default/page.index.css" rel="stylesheet" />
        <link href="theme/default/ui.pick.css" rel="stylesheet" />
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/ui.custom.js"></script>
        <script type="text/javascript" src="js/ui.jqgrid.js"></script>
        <script type="text/javascript" src="js/ui.common.js"></script>
        <script type="text/javascript" src="js/ui.chosen.js"></script>
        <script type="text/javascript" src="js/ui.pagedmenu.js"></script>
        <script type="text/javascript" src="js/ui.dropdown.js"></script>
        <script type="text/javascript" src="js/ui.accordionmenu.js"></script>
        <script type="text/javascript" src="js/ui.nodeline.js"></script>
        <script type="text/javascript" src="js/page.index.js"></script>
        <script type="text/javascript" src="js/page.common.js"></script>
        
        <title>欢迎进入系统</title>
        <!--[if IE 7]>
        <link href="theme/default/font.awesome.ie7.css" rel="stylesheet" />
        <style type="text/css">
            .fa-lg {
              line-height: 68px !important;
            }
        </style>
        <![endif]-->
    </head>
    <script type="text/javascript">
     var validUserInputInfo=function(ev){
      var flag=false;
      var $obj=$(ev);
      var inpVal=$obj.val();
      var nameAttr=$obj.attr("name");
     
      if("officePhone"==nameAttr){
         if(inpVal!=""){
            $obj.parent().next().html("<em class='error valid'></em>");
            flag=true;
         }else{
           $obj.parent().next().html("<em class='error'>必须</em>");
           flag=false;
           return flag; 
         }
      }else if("mobile"==nameAttr){
          if(inpVal!=""){
           $obj.parent().next().html("<em class='error valid'></em>");
           flag=true;
         }else{
           $obj.parent().next().html("<em class='error'>必须</em>");
           flag=false;
           return flag; 
         }
      }else if("officeMail"==nameAttr){
         if(inpVal!=""){
            $obj.parent().next().html("<em class='error valid'></em>");
            flag=true;
         }else{
           $obj.parent().next().html("<em class='error'>必须</em>");
           flag=false;
           return flag; 
         }
      }else if("prevPasswd"==nameAttr){
        if(inpVal!=""){
          $obj.parent().next().html("<em class='error valid'></em>");
          flag=true;
         }else{
           $obj.parent().next().html("<em class='error'>必须</em>");
           flag=false;
           return flag; 
         }
        
      }else if("newPasswd"==nameAttr){
        if(inpVal!=""){
         $obj.parent().next().html("<em class='error valid'></em>");
         flag=true;
        }else{
          $obj.parent().next().html("<em class='error'>必须</em>");
          flag=false;
          return flag; 
        }
      }else if("confirmNewPasswd"==nameAttr){
        if(inpVal!=""){
          $obj.parent().next().html("<em class='error valid'></em>");
          flag=true;
        }else{
          $obj.parent().next().html("<em class='error'>必须</em>");
          flag=false;
          return flag; 
        }
      }  
      return flag; 
     }; 
     
     //编辑用户资料的校验
     var validateUserInfo=function(){
       var flag=true;
       $("#editorForm").find("input").each(function(index,item){
         var f=validUserInputInfo(item);
         if(!f){
           flag=false;
           return;
         }
       });
       return flag;
     };
     
      //修改口令的校验
     var validateUserPasswd=function(){
       var flag=true;
       $("#editorFormOther").find("input").each(function(index,item){
         var f=validUserInputInfo(item);
         if(!f){
           flag=false;
           return;
         }
       });
       return flag;
     };
     
     var saveData=function(callback){
        if(!validateUserInfo()){
          return;
        }
        POST("homePage_/editUserInfo.do?command=saveUserInfos", $("#editorForm").getFormData(), function(data, xhr) {
                        callback();
          });
       };
      var editPasswd=function(callback){
       if(!validateUserPasswd()){
          return;
        }
        
        POST("homePage_/updatePasswd.do?command=updatePasswd", $("#editorFormOther").getFormData(), function(data, xhr) {
                        callback();
          });
     };
     
     var clearPrevValue=function(editor){
        $(editor).find("input").each(function(index,item){
          $(item).val("");
        });
        $(editor).find(".messageTd").each(function(index,item){
          $(item).empty();
        });
     };
     
     
      $(function() {
         $("#editor").dialog({
                    autoOpen: false,
                    width: 500,
                    height: 300,
                    title: "添加",
                    modal: true,
                    buttons: {
                        "保存": function() {
                            var $this=$(this);
                            saveData(function() {
                               $this.dialog("close");
                            });
                        },
                        "取消": function() {
                            $(this).dialog("close");
                        }
                    },
                    close: function() {
					 clearPrevValue("#editor");
                    }
                });
                
                 $("#editorOther").dialog({
                    autoOpen: false,
                    width: 500,
                    height: 300,
                    title: "添加",
                    modal: true,
                    buttons: {
                        "确认": function() {
                            var $this=$(this);
                            editPasswd(function() {
                               $this.dialog("close");
                            });
                        },
                        "取消": function() {
                            $(this).dialog("close");
                        }
                    },
                    close: function() {
					  clearPrevValue("#editorFormOther");
                    }
                });
      });
      
    </script>
    <body>
        <div class="auto-hide full-screen-button"></div>
        <div class="nx-header">
           <div class="logo" onclick="backHome();"></div>
            <div class="ui-pagedmenu-panel">
                <div class="ui-pagedmenu-content"></div>
                <div class="ui-pagedmenu-pager">
                    <div class="ui-pagedmenu-pager-bar">&lt;</div>
                    <div class="ui-pagedmenu-pager-bar">&gt;</div>
                </div>
            </div>
            <div class="menubar">
                <div id="nxbbbb" class="nx-menubutton nx-dropdown" style="min-width: 136px;">
                    <img class="user-icon" src="" style=""
                         alt="" />
                    <label></label>
                    <!-- <div class="message-mas">0</div> -->
                    <div class="nx-dropdown-content"><ul>
                            <li><h4>基本资料</h4></li>
                            <li><a actiontype="editInfo" tabindex="-1" onclick="editUserInfo();" class="main-link">
                               <i class="fa fa-edit"></i>编辑资料</a>
                            </li>
                            <script type="text/javascript">
        
					           var editUserInfo=function(){
					                POST("homePage_.do?command=getUserSomeInfo",null, function(data, xhr) {
					                for(var item in data){
					                    $("#editorForm").find("input[name="+item+"]").val(data[item]);
					                  }
					                 $("#editor").dialog("option","title","编辑资料").dialog("open");
					                });
					             
					             };
					       </script>
                            <li><a actiontype="changePassword" tabindex="-1" onclick="updatePasswd();" class="main-link"><i
                                        class="fa fa-edit"></i>修改口令</a></li>
                           
                           <script type="text/javascript">
					           var updatePasswd=function(){
					               $("#editorOther").dialog("option","title","修改口令").dialog("open");
					           };
       						</script>
                            <!--<li><a actionType="switchUser" uid="d-234ab234-cd232-cb23a-23cbf" tabindex="-1" href="javascript:;" class="main-link">Dreamer</a></li>-->
                        </ul></div>
                </div>
                <div class="nx-menubutton" id="logoutbtn" title="退出"
                     style="line-height: 68px; color: white; font-size: 20px; vertical-align: middle;">
                    <i class="fa fa-power-off fa-large" style="font-size:1.33em;line-height: 2.7em;"></i>
                </div>
            </div>
            
        </div>
        <!-- <div style="background-color:red;"></div>  -->
        <div class="nx-left"></div>
        <div class="nx-content" style="overflow: hidden;">
            <div class="ui-loading">
                <ul>
                    <li id="y1">T</li>
                    <li id="y2">M</li>
                    <li id="y3">I</li>
                    <li id="y4">S</li>
                    <li id="y5">.</li>
                    <li id="y6">.</li>
                    <li id="y7">.</li>
                </ul>
            </div>
            <div class="nx-breadcrumb">
                <ul class="ui-breadcrumb">
                    <li><i class="fa fa-home"></i><a href="javascript:;">主页</a></li>
                    <li class="aw-right">&gt;</li>
                    <li>主档信息</li>
                    <li class="aw-right">&gt;</li>
                    <li class="active">账期配置</li>
                </ul>
            </div>
            <iframe id="contentFrame" name="contentFrame"
                    style="width: 100%; border: none; outline: hidden;" frameborder="0"
                    marginwidth="0" marginheight="0" src="pages/dashboard.jsp"></iframe>
        </div>
        
        
        
         <div style="display:none;" id="editor">
            <form id="editorForm" style="margin-top:20px;">
                <table  align="center" cellpadding="0" cellspacing="0" style="border-spacing:10px; ">
                    <tr>
                        <td class="labelTd" style="text-align:right;">
                        
                            <label for="officePhone"><font style="color:#ff0000;">*</font>办公电话：</label>
                        </td>
                        <td class="inputTd">
                            <input value="${user.officePhone}" onchange="validUserInputInfo(this);" data-xtype="text" style="text-align:left;" class="form-control" name="officePhone"  />
                        </td>
                        <td class="messageTd" style="width:44px;height:23px;"></td>
                        
                    </tr>
                    <tr>
                        <td class="labelTd" style="text-align:right;"><label for="mobile"><font style="color:#ff0000;">*</font>手机：</label></td>
                        <td class="inputTd">
                            <input  value="${user.mobile}" onchange="validUserInputInfo(this);" data-xtype="text" style="text-align:left;" name="mobile" class="form-control" />
                        </td>
                         <td class="messageTd"></td>
                    </tr>
                    <tr>
                        <td class="labelTd" style="text-align:right;"><label for="officeMail"><font style="color:#ff0000;">*</font>办公邮箱：</label></td>
                        <td class="inputTd">
                            <input value="${user.officeMail}" onchange="validUserInputInfo(this);" data-xtype="text" style="text-align:left;" name="officeMail" class="form-control" />
                        </td>
                         <td class="messageTd"></td>
                    </tr>
                </table>
            </form>
        </div>
        
        <div style="display:none;" id="editorOther">
            <form id="editorFormOther" style="margin-top:20px;">
                <table  align="center" cellpadding="0" cellspacing="0" style="border-spacing:10px; ">
                    <tr>
                        <td class="labelTd" style="text-align:right;">
                            <label for="prevPasswd"><font style="color:#ff0000;">*</font>原始密码：</label>
                        </td>
                        <td class="inputTd">
                            <input type="password" onchange="validUserInputInfo(this);" style="text-align:left;" class="form-control" name="prevPasswd"  />
                        </td>
                         <td class="messageTd" style="width:44px;height:23px;"></td>
                    </tr>
                    <tr>
                        <td class="labelTd" style="text-align:right;"><label for="newPasswd"><font style="color:#ff0000;">*</font>新密码：</label></td>
                        <td class="inputTd">
                            <input type="password" onchange="validUserInputInfo(this);" style="text-align:left;" name="newPasswd" class="form-control" />
                        </td>
                         <td class="messageTd"></td>
                    </tr>
                    <tr>
                        <td class="labelTd" style="text-align:right;"><label for="confirmNewPasswd"><font style="color:#ff0000;">*</font>新密码确认：</label></td>
                        <td class="inputTd">
                            <input type="password" onchange="validUserInputInfo(this);" style="text-align:left;" name="confirmNewPasswd" class="form-control" />
                        </td>
                         <td class="messageTd"></td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>