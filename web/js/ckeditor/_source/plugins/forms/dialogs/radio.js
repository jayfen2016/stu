/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
 1.data-cke-saved-name  name 与id一样设置 表单名称
 2.value 初始值
 3.checked 选中 true和fasle
 4.data-xtype  控件类型  默认text
 5.type input类型  radio
 6.data-dtype 是数据库的类型 默认都是 int
 7 class 类样式
 8 style 行内样式
*/
CKEDITOR.dialog.add( 'radio', function( editor )
{
	return {
		title : editor.lang.checkboxAndRadio.radioTitle,
		minWidth : 350,
		minHeight : 180,
		onShow : function()
		{
			delete this.radioButton;

			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.getName() == 'input' && element.getAttribute( 'type' ) == 'radio' )
			{
				this.radioButton = element;
				this.setupContent( element );
			}
		},
		onOk : function()
		{
			var editor,
				element = this.radioButton,
				isInsertMode = !element;

			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				element = editor.document.createElement( 'input' );
				element.setAttribute( 'type', 'radio' );
			}

			if ( isInsertMode )
				editor.insertElement( element );
			this.commitContent( { element : element } );
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.checkboxAndRadio.radioTitle,
				title : editor.lang.checkboxAndRadio.radioTitle,
				elements : [
					{
						id : 'name',
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
						commit : function( data )
						{
							var element = data.element;

							if ( this.getValue() ) {
								element.data('cke-saved-name', this.getValue());
								element.setAttribute("name", this.getValue());
								element.setAttribute("id", this.getValue());
							}else
							{
								element.data( 'cke-saved-name', false );
								//element.removeAttribute( 'name' );
							}
						}
					},
					{
						id : 'value',
						type : 'text',
						label : editor.lang.checkboxAndRadio.value,
						'default' : '',
						accessKey : 'V',
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'value' ) || '' );
						},
						commit : function( data )
						{
							var element = data.element;

							if ( this.getValue() )
								element.setAttribute( 'value', this.getValue() );
							else
								element.removeAttribute( 'value' );
						}
					},
					{
						id : 'checked',
						type : 'checkbox',
						label : editor.lang.checkboxAndRadio.selected,
						'default' : '',
						accessKey : 'S',
						value : "checked",
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'checked' ) );
						},
						commit : function( data )
						{
							var element = data.element;
							//提交设置值
							element.data( 'xtype', 'text' );
							element.data( 'dtype', 'int' );
							if ( !( CKEDITOR.env.ie || CKEDITOR.env.opera ) )
							{
								if ( this.getValue() )
									element.setAttribute( 'checked', 'checked' );
								else
									element.removeAttribute( 'checked' );
							}
							else
							{
								var isElementChecked = element.getAttribute( 'checked' );
								var isChecked = !!this.getValue();

								if ( isElementChecked != isChecked )
								{
									var replace = CKEDITOR.dom.element.createFromHtml( '<input type="radio"'
											+ ( isChecked ? ' checked="checked"' : '' )
											+ '></input>', editor.document );
									element.copyAttributes( replace, { type : 1, checked : 1 } );
									replace.replace( element );
									editor.getSelection().selectElement( replace );
									data.element = replace;
								}
							}
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
								commit: function( data )
								{
									var element = data.element;

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
								commit: function( data )
								{
									var element = data.element;

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
