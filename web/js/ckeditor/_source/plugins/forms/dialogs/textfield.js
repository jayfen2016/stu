/*
 Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
 1.data-cke-saved-name  name 与id一样设置 表单名称
 2.value 初始值
 3.size 字符宽度（文本宽度）
 4.maxlength  最多字符数
 5.data-xtype  控件类型
 6.type input类型 默认都是文本框 text
 7.data-dtype 是数据库的类型 默认都是 string
 8.data-datacode 数据字典值
 9 class 类样式
 10 style 行内样式
 11 bindobject 绑定对象  user 用户 org 部门
 12 bindobjectvalue 绑定对象值
 13 这个只有意见标签advice才有param参数的 param 是为要是用户"user:id(用户的id)"若为组织机构用"org:id(组织机构id)"
 */
CKEDITOR.dialog.add( 'textfield', function( editor )
{
	var autoAttributes =
	{
		value : 1,
		size : 1,
		maxLength : 1
	};

	var acceptedTypes =
	{
		text : 1,
		password : 1,
		advice:1,
		sign:1,
		endorse:1,
		fileadvice:1,
		officeupload:1,
		uploadactivex:1,
		countersign:1,
		filenumber:1,
		uploadread:1,
		fileuploadcountersign:1,
		flowupload:1,
		datetime:1
	};
	var setupParams = function( page, data )
	{
		if ( data[page] )
			this.setValue( data[page][this.id] || '' );
	};


	var setupAdvParams = function( data )
	{
		return setupParams.call( this, 'adv', data );
	};

	var commitParams = function( page, data )
	{
		if ( !data[page] )
			data[page] = {};

		data[page][this.id] = this.getValue() || '';
	};
	var commitAdvParams = function( data )
	{
		return commitParams.call( this, 'adv', data );
	};
	return {
		title : editor.lang.textfield.title,
		minWidth : 350,
		minHeight : 300,
		onShow : function()
		{
			delete this.textField;

			var element = this.getParentEditor().getSelection().getSelectedElement();
			if ( element && element.getName() == "input" &&
			( acceptedTypes[ element.data( 'xtype' ) ] || !element.data( 'xtype' ) ) )
		{
			this.textField = element;
			this.setupContent( element );
		}
		},
		onOk : function()
		{
			var editor,
				element = this.textField,
				isInsertMode = !element;

			if ( isInsertMode )
			{
				editor = this.getParentEditor();
				element = editor.document.createElement( 'input' );
				element.setAttribute( 'type', 'text' );
				element.data( 'text' );//默认选中
			}

			if ( isInsertMode )
				editor.insertElement( element );
			this.commitContent( { element : element } );

		},
		onLoad : function()
		{
			var autoSetup = function( element )
			{
				var value = element.hasAttribute( this.id ) && element.getAttribute( this.id );
				this.setValue( value || '' );
			};

			var autoCommit = function( data )
			{
				var element = data.element;
				var value = this.getValue();

				if ( value )
					element.setAttribute( this.id, value );
				else
					element.removeAttribute( this.id );
			};

			this.foreach( function( contentObj )
				{
					if ( autoAttributes[ contentObj.id ] )
					{
						contentObj.setup = autoSetup;
						contentObj.commit = autoCommit;
					}
				} );
		},
		contents : [
			{
				id : 'info',
				label : editor.lang.textfield.title,
				title : editor.lang.textfield.title,
				elements : [
					{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : '_cke_saved_name',
								type : 'text',
								label : editor.lang.textfield.name,
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

									if ( this.getValue() ){
										element.data( 'cke-saved-name', this.getValue() );
									    element.setAttribute("name",this.getValue());
										element.setAttribute("id",this.getValue());
									}else
									{
										element.data( 'cke-saved-name', false );
										//element.removeAttribute( 'name' );
										//element.setAttribute("name","");
										//element.setAttribute("id","");
									}
								}
							},
							{
								id : 'value',
								type : 'text',
								label : editor.lang.textfield.value,
								'default' : '',
								accessKey : 'V'
							}
						]
					},
					{
						type : 'hbox',
						widths : [ '50%', '50%' ],
						children :
						[
							{
								id : 'size',
								type : 'text',
								label : editor.lang.textfield.charWidth,
								'default' : '',
								accessKey : 'C',
								style : 'width:50px',
								validate : CKEDITOR.dialog.validate.integer( editor.lang.common.validateNumberFailed )
							},
							{
								id : 'maxLength',
								type : 'text',
								label : editor.lang.textfield.maxChars,
								'default' : '',
								accessKey : 'M',
								style : 'width:50px',
								validate : CKEDITOR.dialog.validate.integer( editor.lang.common.validateNumberFailed )
							}
						],
						onLoad : function()
						{
							// Repaint the style for IE7 (#6068)
							if ( CKEDITOR.env.ie7Compat )
								this.getElement().setStyle( 'zoom', '100%' );
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
										[ editor.lang.textfield.typeText, 'text' ],
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
								commit : function( data )
								{
									var element = data.element;

									if ( CKEDITOR.env.ie )
									{
										var elementType =element.data( 'xtype' );
										var myType = this.getValue();

										if ( elementType != myType )
										{
											var dtype=myType=="datetime"?"date":"string";
											var replace = CKEDITOR.dom.element.createFromHtml( '<input type="text"  data-xtype="' + myType + '"  data-dtype="' + dtype + '"     ></input>', editor.document );
											element.copyAttributes( replace, { type : 1 } );
											replace.replace( element );
											editor.getSelection().selectElement( replace );
											data.element = replace;
										}
									}
									else{
										element.setAttribute( 'type', 'text' );
										element.data( 'xtype', this.getValue());
										element.data( 'dtype', this.getValue()=="datetime"?"date":"string"  );
									}
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
								commit: function( data )
								{
									var element = data.element;

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
									commit: function( data )
									{
										var element = data.element;

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
									commit: function( data )
									{
										var element = data.element;
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
