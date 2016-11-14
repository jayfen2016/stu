<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
		<link href="../theme/default/ui.ztree.css" rel="stylesheet" />
		<link href="../theme/default/style01.css" rel="stylesheet" />
		<script type="text/javascript" src="../js/jquery.js"></script>
		<script type="text/javascript" src="../js/ui.ztree.js"></script>
		<style type="text/css">
			body {
				font-size: 75%;
                                padding:12px 12px 12px 12px;
			}
		</style>
		<script type="text/javascript">
		  var setting = {
		    isSimpleData : true, //数据是否采用简单 Array 格式，默认false
			treeNodeKey : "id", //在isSimpleData格式下，当前节点id属性
			treeNodeParentKey : "pId", //在isSimpleData格式下，当前节点的父节点id属性
			showLine : true, //是否显示节点间的连线
			checkable : true, //每个节点上是否显示 CheckBox
			check: {
				enable: true
			}
		};

         function getData(){
           var len=$("#test").find("li").length;
		    if(len>0){
		       $("#test").empty();
		    }
		    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getCheckedNodes(true);
            var hiddenIdStr="";
            var hiddenNameStr="";
            for(var i=0;i<nodes.length;i++){
               var str="<li id="+nodes[i].id+">"+nodes[i].name+"</li>";
			   $("#test").append(str);
			   hiddenIdStr=hiddenIdStr.length>0 ? hiddenIdStr+"," +nodes[i].id :  nodes[i].id ;
			   hiddenNameStr=hiddenNameStr.length>0 ? hiddenNameStr+"," +nodes[i].name :  nodes[i].name ;
            }
            $("#hiddenIdData").val(hiddenIdStr);
            $("#hiddenNameData").val(hiddenNameStr);
          return  hiddenNameStr+";"+hiddenIdStr;
         };
		function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			return childNodes;
		}

		$(document).ready(function(){
		     $.ajax({
                         url: "../common/choosePerson.do?command=selectAllDatas&orgId="+$("#hiddenOrgId").val()+"&orgName="+encodeURI($("#hiddenOrgName").val())+"&moduleType="+$("#hiddenModuleType").val()+"&currentUserId="+$("#currentUserId").val()+"&isNeedAllOrg="+$("#isNeedAllOrg").val(),
                         type: "POST",
                         data: {
                             idsParam: $("#idsParam").val()
                         },
                         dataType: "JSON",
                         success: function(data, xhr) {
                             if (data.error) {
                                  window.Msg.error(data.error,"发生错误");
                                 return;
                             }
                              $.fn.zTree.init($("#treeDemo"), setting,eval(data));
                              /* var moveNode = $.fn.zTree.init($("#treeDemo"), setting,eval(data)).getNodeByTId("treeDemo_2");
                              $.fn.zTree.init($("#treeDemo"), setting,eval(data)).removeNode(moveNode,true); */
                              
                         }
                     });
		});
		</script>
	</head>
	<body>
	     <input type="hidden" id="hiddenIdData"/>
	     <input type="hidden" id="hiddenNameData"/>
	     <input type="hidden" id="idsParam" value="${idsParam}"/>
	     <input type="hidden" id="hiddenOrgId" value="${orgId}"/>
	     <input type="hidden" id="hiddenOrgName" value="${orgName}"/>
	     <input type="hidden" id="hiddenModuleType" value="${moduleType}"/>
	     <input type="hidden" id="currentUserId" value="${currentUserId}"/>
	     <input type="hidden" id="isNeedAllOrg" value="${isNeedAllOrg}"/>
		 <div align="center">
           <div class="cenbox_tree" align="center" style="float:left">
             <div class="zTreeDemoBackground left">
		       <ul id="treeDemo" class="ztree"></ul>
	         </div>
           </div>
        </div>
	</body>
</html>