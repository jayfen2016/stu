/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
 * @file Blockquote.
 */

CKEDITOR.plugins.add('myplugin',
	{		
		requires : ['dialog'],
		init : function (editor)
		{
			var pluginName = 'myplugin';
			
			//加载自定义窗口，就是dialogs前面的那个/让我纠结了很长时间
			CKEDITOR.dialog.add('myDialog',this.path + "/dialogs/mydialog.js");
			
			//给自定义插件注册一个调用命令
			editor.addCommand( pluginName, new CKEDITOR.dialogCommand( 'myDialog' ) );
			
			//注册一个按钮，来调用自定义插件
			editor.ui.addButton('MyButton',
					{
						//editor.lang.mine是我在zh-cn.js中定义的一个中文项，
						//这里可以直接写英文字符，不过要想显示中文就得修改zh-cn.js
						label : editor.lang.mine,
						command : pluginName
					});
		}
	}
);