/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
	For licensing, see LICENSE.html or http://ckeditor.com/license
*/

KEDITOR.dialog.add(
   "code",
   function (a)
   {
       return {
           title:"插入代码",
           minWidth:590,
           minHeight:300,
           contents:
           [
               {
                   id:"tab1",
                   label:"",
                   title:"",
                   expand:true,
                   padding:0,
                   elements:
                   [
                       {
                           type:"html",
                           html:""
                       }
                   ]
               }
           ],
           onOk: function()
           {
           }
       };
   }
 );
