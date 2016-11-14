/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
 1.data-cke-saved-name  name 与id一样设置 表单名称
 2.value 初始值
 3.cols 字符宽度
 4.rows 行数
 5.data-xtype  控件类型  默认text
 6.data-dtype 是数据库的类型 默认都是 string
 7 class 类样式
 8 style 行内样式
*/
CKEDITOR.dialog.add( 'textarea', function( editor )
{
	return {
		title : editor.lang.textarea.title,
		minWidth : 350,
		minHeight : 240,
		onShow : function()
		{
			delete this.textarea;

			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.getName() == "textarea" )
			{
				this.textarea = element;
				this.setupContent( element );
			}
		},
		onOk : function()
		{
			var editor,
				element = this.textarea,
				isInsertMode = !element;

			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				element = editor.document.createElement( 'textarea' );
			}
			this.commitContent( element );

			if ( isInsertMode )
				editor.insertElement( element );
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.textarea.title,
				title : editor.lang.textarea.title,
				elements : [
					{
						id : '_cke_saved_name',
						type : 'text',
						label : editor.lang.common.name,
						'default' : '',
						accessKey : 'N',
						validate: CKEDITOR.dialog.validate.notEmpty("名称不能为空！"),
						setup : function( element )
						{
							this.setValue(
									element.data( 'cke-saved-name' ) ||
									element.getAttribute( 'name' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() ) {
								element.data('cke-saved-name', this.getValue());
								element.setAttribute("name",this.getValue());
								element.setAttribute("id",this.getValue());
							}else
							{
								element.data( 'cke-saved-name', false );
								//element.removeAttribute( 'name' );
							}
						}
					},
					{
						type : 'hbox',
						widths:['50%','50%'],
						children:[
							{
								id : 'cols',
								type : 'text',
								label : editor.lang.textarea.cols,
								'default' : '',
								accessKey : 'C',
								style : 'width:50px',
								validate : CKEDITOR.dialog.validate.integer( editor.lang.common.validateNumberFailed ),
								setup : function( element )
								{
									var value = element.hasAttribute( 'cols' ) && element.getAttribute( 'cols' );
									this.setValue( value || '' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'cols', this.getValue() );
									else
										element.removeAttribute( 'cols' );
								}
							},
							{
								id : 'rows',
								type : 'text',
								label : editor.lang.textarea.rows,
								'default' : '',
								accessKey : 'R',
								style : 'width:50px',
								validate : CKEDITOR.dialog.validate.integer( editor.lang.common.validateNumberFailed ),
								setup : function( element )
								{
									var value = element.hasAttribute( 'rows' ) && element.getAttribute( 'rows' );
									this.setValue( value || '' );
								},
								commit : function( element )
								{
									if ( this.getValue() )
										element.setAttribute( 'rows', this.getValue() );
									else
										element.removeAttribute( 'rows' );
								}
							}
						]
					},
					{
						id : 'value',
						type : 'textarea',
						label : editor.lang.textfield.value,
						'default' : '',
						setup : function( element )
						{
							this.setValue( element.$.defaultValue );
						},
						commit : function( element )
						{
							//提交设置值
							element.data( 'xtype', 'text' );
							element.data( 'dtype', 'string' );
							element.$.value = element.$.defaultValue = this.getValue() ;
						}
					},
					{
						type: 'hbox',
						widths: ['50%', '50%'],
						children: [
							{
								type: 'text',
								label: editor.lang.link.cssClasses,
								'default': '',
								id: 'advCSSClasses',
								setup:  function( element )
								{
									this.setValue( element.getAttribute( 'class' ));
								},
								commit: function( element )
								{
									if ( this.getValue() ){//类样式设置
										element.setAttribute("class",this.getValue());
									}
								}
							},
							{
								type: 'text',
								label: editor.lang.link.styles,
								'default': '',
								id: 'advStyles',
								validate: CKEDITOR.dialog.validate.inlineStyle(editor.lang.common.invalidInlineStyle),
								setup:  function( element )
								{
									this.setValue( element.getAttribute( 'style' ));
								},
								commit: function( element )
								{
									if ( this.getValue() ){//行内样式设置
										element.setAttribute("style",this.getValue());
									}
								}
							}
						]
					}

				]
			}
		]
	};
});
