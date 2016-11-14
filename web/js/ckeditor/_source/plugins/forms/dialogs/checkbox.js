/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
 1.data-cke-saved-name  name 与id一样设置 表单名称
 2.value 初始值
 3.checked 选中 true和fasle
 4.data-xtype  控件类型  默认text
 5.type input类型  checkbox
 6.data-dtype 是数据库的类型 默认都是 int
 7 class 类样式
 8 style 行内样式
*/
CKEDITOR.dialog.add( 'checkbox', function( editor )
{
	return {
		title : editor.lang.checkboxAndRadio.checkboxTitle,
		minWidth : 350,
		minHeight : 180,
		onShow : function()
		{
			delete this.checkbox;

			var element = this.getParentEditor().getSelection().getSelectedElement();

			if ( element && element.getAttribute( 'type' ) == 'checkbox' )
			{
				this.checkbox = element;
				this.setupContent( element );
			}
		},
		onOk : function()
		{
			var editor,
				element = this.checkbox,
				isInsertMode = !element;

			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				element = editor.document.createElement( 'input' );
				element.setAttribute( 'type', 'checkbox' );
				editor.insertElement( element );
			}
			this.commitContent( { element : element } );
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.checkboxAndRadio.checkboxTitle,
				title : editor.lang.checkboxAndRadio.checkboxTitle,
				startupFocus : 'txtName',
				elements : [
					{
						id : 'txtName',
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

							// IE failed to update 'name' property on input elements, protect it now.
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
						id : 'txtValue',
						type : 'text',
						label : editor.lang.checkboxAndRadio.value,
						'default' : '',
						accessKey : 'V',
						setup : function( element )
						{
							var value = element.getAttribute( 'value' );
							// IE Return 'on' as default attr value.
							this.setValue(  CKEDITOR.env.ie && value == 'on' ? '' : value  );
						},
						commit : function( data )
						{
							var element = data.element,
								value = this.getValue();

							if ( value && !( CKEDITOR.env.ie && value == 'on' ) )
								element.setAttribute( 'value', value );
							else
							{
								if ( CKEDITOR.env.ie )
								{
									// Remove attribute 'value' of checkbox (#4721).
									var checkbox = new CKEDITOR.dom.element( 'input', element.getDocument() );
									element.copyAttributes( checkbox, { value: 1 } );
									checkbox.replace( element );
									editor.getSelection().selectElement( checkbox );
									data.element = checkbox;
								}
								else
									element.removeAttribute( 'value' );
							}
						}
					},
					{
						id : 'cmbSelected',
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

							if ( CKEDITOR.env.ie )
							{
								var isElementChecked = !!element.getAttribute( 'checked' ),
									isChecked = !!this.getValue();

								if ( isElementChecked != isChecked )
								{
									var replace = CKEDITOR.dom.element.createFromHtml( '<input type="checkbox"'
										   + ( isChecked ? ' checked="checked"' : '' )
										   + '/>', editor.document );

									element.copyAttributes( replace, { type : 1, checked : 1 } );
									replace.replace( element );
									editor.getSelection().selectElement( replace );
									data.element = replace;
								}
							}
							else
							{
								var value = this.getValue();
								if ( value )
									element.setAttribute( 'checked', 'checked' );
								else
									element.removeAttribute( 'checked' );
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
