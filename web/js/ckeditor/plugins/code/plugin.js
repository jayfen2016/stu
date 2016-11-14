/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Blockquote.
 */

 CKEDITOR.plugins.add(
	    "code",
	    {
	        requires:["dialog"],
	       // lang:["en"],
	        init:function (a)
	        {
	            a.addCommand("code", new CKEDITOR.dialogCommand("code"));
	            a.ui.addButton(
	                "Code",
	                {
	                    label:"插入代码",
	                    command:"code",
	                    icon:this.path + "code.gif"
	                });
	            CKEDITOR.dialog.add("code", this.path + "dialogs/code.js");
	        }
	    }
	);