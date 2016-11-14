/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/
CKEDITOR.dialog.add( 'hiddenfield', function( editor )
{
	return {
		title : editor.lang.hidden.title,
		hiddenField : null,
		minWidth : 350,
		minHeight : 110,
		onShow : function()
		{
			delete this.hiddenField;

			var editor = this.getParentEditor(),
				selection = editor.getSelection(),
				element = selection.getSelectedElement();

			if ( element && element.data( 'cke-real-element-type' ) && element.data( 'cke-real-element-type' ) == 'hiddenfield' )
			{
				this.hiddenField = element;
				element = editor.restoreRealElement( this.hiddenField );
				this.setupContent( element );
				selection.selectElement( this.hiddenField );
			}
		},
		onOk : function()
		{
			var name = this.getValueOf( 'info', '_cke_saved_name' ),
				value = this.getValueOf( 'info', 'value' ),
				editor = this.getParentEditor(),
				element = CKEDITOR.env.ie && !( CKEDITOR.document.$.documentMode >= 8 ) ?
					editor.document.createElement( '<input name="' + CKEDITOR.tools.htmlEncode( name ) + '">' )
					: editor.document.createElement( 'input' );

			element.setAttribute( 'type', 'hidden' );
			this.commitContent( element );
			var fakeElement = editor.createFakeElement( element, 'cke_hidden', 'hiddenfield' );
			if ( !this.hiddenField )
				editor.insertElement( fakeElement );
			else
			{
				fakeElement.replace( this.hiddenField );
				editor.getSelection().selectElement( fakeElement );
			}
			return true;
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.hidden.title,
				title : editor.lang.hidden.title,
				elements : [
					{
						id : '_cke_saved_name',
						type : 'text',
						label : editor.lang.hidden.name,
						'default' : '',
						accessKey : 'N',
						setup : function( element )
						{
							this.setValue(
									element.data( 'cke-saved-name' ) ||
									element.getAttribute( 'name' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'name', this.getValue() );
							else
							{
								element.removeAttribute( 'name' );
							}
						}
					},
					{
						id : 'value',
						type : 'text',
						label : editor.lang.hidden.value,
						'default' : '',
						accessKey : 'V',
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'value' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'value', this.getValue() );
							else
								element.removeAttribute( 'value' );
						}
					},
					{
						type: 'hbox',
						widths: ['50%', '50%'],
						children: [
							{
								id : 'type',
								type : 'select',
								label : editor.lang.textfield.type,
								'default' : 'advice',
								accessKey : 'M',
								items :
									[
										[ '普通隐藏框', 'text' ],
										//[ editor.lang.textfield.typePass, 'password' ],
										[ "意见标签", 'advice' ],
										[ "签字标签", 'sign' ],
										[ "签署意见标签", 'endorse' ],
										[ "附件意见标签", 'fileadvice' ],
										[ "公文文档上传控件", 'officeupload' ],
										[ "普通文件上传控件", 'uploadactivex' ],
										[ "会签意见", 'countersign' ],
										[ "文号标签", 'filenumber' ],
										[ "收文阅件", 'uploadread' ],
										[ "附件意见会签", 'fileuploadcountersign' ],
										[ "流程控件", 'flowupload' ],
										[ "时间控件", 'datetime' ]
									],
								setup : function( element )
								{
									this.setValue( element.data( 'xtype' ));
								},
								commit : function( element )
								{
									//element.setAttribute( 'type', 'hidden' );
									element.data( 'xtype', this.getValue());
									element.data( 'dtype', this.getValue()=="datetime"?"date":"string"  );
								},
								onChange: function () {
									var value = this.getValue();
									var dialog = this.getDialog(),
										bindObject = $(dialog.getContentElement('info', 'bindObject').getInputElement().$).parent().parent(),
										bindObjectValue = $(dialog.getContentElement('info', 'bindObjectValue').getInputElement().$).parent().parent().parent();
									if (value == "advice") {
										bindObject.css("display", "block");
										bindObjectValue.css("display", "block");
									} else {
										bindObject.css("display", "none");
										bindObjectValue.css("display", "none");
									}
								}
							},
							{
								type: 'text',
								label: '数据字典code',
								'default': '',
								id: 'advDataCode',
								setup:  function( element )
								{
									this.setValue( element.data( 'datacode' ));
								},
								commit: function( element )
								{
									if ( this.getValue() ){//数据字典设置
										element.data("datacode",this.getValue());
									}
								}
							}

						]
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
					},
					{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
							[
								{
									id : 'bindObject',
									type : 'select',
									label :'绑定对象',
									'default' : 'user',
									items :
										[
											[ "用户", 'user' ],
											[ "部门", 'org' ]
										],
									setup:  function( element )
									{
										this.setValue( element.getAttribute( 'bindObject' ));
									},
									commit: function( element )
									{
										if ( this.getValue() ){//绑定对象设置
											element.setAttribute("bindObject",this.getValue());
										}
									}
								},
								{
									id : 'bindObjectValue',
									type : 'text',
									label : '绑定对象值',
									'default' : '',
									setup:  function( element )
									{
										this.setValue( element.getAttribute( 'bindObjectValue' ));
									},
									commit: function( element )
									{
										if ( this.getValue() ){//绑定对象值设置
											var value = this.getValue();
											var dialog = this.getDialog(),
												bindObject = $(dialog.getContentElement('info', 'bindObject').getInputElement().$);
											element.setAttribute("bindObjectValue",this.getValue());
											element.setAttribute("param",bindObject.find('option:selected').val()+":"+this.getValue());
										}
									}
								}
							],
						onLoad : function()
						{
							// Repaint the style for IE7 (#6068)
							if ( CKEDITOR.env.ie7Compat )
								this.getElement().setStyle( 'zoom', '100%' );
						}
					}
				]
			}
		]
	};
});
