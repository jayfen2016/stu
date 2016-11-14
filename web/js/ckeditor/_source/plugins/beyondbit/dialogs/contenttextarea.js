/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
1.formFlag='true' 
2.canWrite='true'
3.isEmpty='true'
4.bindFlowID=''
5.bindFlow=''
6.bizTableName=''
7.relTableName=''
8.fieldName=''
9.editeNode=''
10.inVisibleNode=''
11.areaMode='inputShow'
12.saveColumns=''
*/
CKEDITOR.dialog.add('formcontenttextarea', function (editor) {
    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1
    };
    return {
        title: "表单正文文本框",
        minWidth: 350,
        minHeight: 500,
        onShow: function () {
            delete this.textarea;
            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });
            var element = this.getParentEditor().getSelection().getSelectedElement();
            if (element && element.getName() == "textarea") {
                this.textarea = element;
                this.setupContent(element);
            }
        },
        onOk: function () {
            var editor,
				element = this.textarea,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = editor.document.createElement('textarea');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
                element.setAttribute('id', $(editor).formdesign().Guid());
            }
            this.commitContent(element);

            if (isInsertMode) {
                editor.insertElement(element);
                var scripts =
                        'var editorOption = {' +
                    	'toolbars:editor_config_dy.ToolBars.FromBase,' +
                        'autoClearinitialContent: true,' +
                        'autoHeightEnabled:false, ' +
                        'wordCount: false,' +
                        'minFrameHeight: 200,' +
                        'elementPathEnabled: false,' +
                        'UEDITOR_HOME_URL: \'../Ueditor/\'' +
                        '}; ' +
                        '	var editor_a = new baidu.editor.ui.Editor(editorOption);  ' +
                        '   	editor_a.render(\'editor\');' +
                        '      editor_a.addListener("blur", function(){' +
                        '     var val = editor_a.getContent();' +
                        '    $("#' + element.getAttribute("id") + '").val(val);' +
                        '   }); ';
                var ele = editor.document.createElement('script');
                ele.setAttribute("id", "editor");
                ele.setAttribute("class", "widthform_wanquan_ueditor");
                ele.setAttribute("type", "text/plain");
                editor.insertElement(ele);

                ele = editor.document.createElement('script');
                ele.setAttribute("type", "text/javascript");
                if (CKEDITOR.env.ie) {
                    var el = editor.document.createElement("div");
                    el.$.style.display = "none";
                    var scriptStr = '<div style="display:none">ie</div><script type="text/javascript" defer>' + scripts + "</script>";
                    //当节点被移除的时候，ie会重新解析节点内部的html，有脚本则会执行相关的脚本，script要加上defer属性 
                    
                    try {
                        el.setHtml(scriptStr);
                        el.$.removeChild(el.$.firstChild);
                    } catch (e) {

                    }

                    editor.insertElement(el);

                } else {
                    ele.setHtml(scripts);
                    editor.insertElement(ele);
                }


            }
        },
        contents: [
			{
			    id: 'info',
			    label: editor.lang.textarea.title,
			    title: editor.lang.textarea.title,
			    elements: [
					{
					    id: '_cke_saved_name',
					    type: 'text',
					    label: editor.lang.common.name,
					    'default': '',
					    accessKey: 'N',
					    setup: function (element) {
					        this.setValue(
									element.data('cke-saved-name') ||
									element.getAttribute('name') ||
									'');
					    },
					    commit: function (element) {
					        if (this.getValue())
					            element.data('cke-saved-name', this.getValue());
					        else {
					            element.data('cke-saved-name', false);
					            element.removeAttribute('name');
					        }
					    }
					},
					{
					    type: 'hbox',
					    widths: ['33%', '33%', '33%'],
					    children: [
							{
							    id: 'cols',
							    type: 'text',
							    label: editor.lang.textarea.cols,
							    'default': '',
							    accessKey: 'C',
							    style: 'width:50px',
							    validate: CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed),
							    setup: function (element) {
							        var value = element.hasAttribute('cols') && element.getAttribute('cols');
							        this.setValue(value || '');
							    },
							    commit: function (element) {
							        if (this.getValue())
							            element.setAttribute('cols', this.getValue());
							        else
							            element.removeAttribute('cols');
							    }
							},
							{
							    id: 'rows',
							    type: 'text',
							    label: editor.lang.textarea.rows,
							    'default': '',
							    accessKey: 'R',
							    style: 'width:50px',
							    validate: CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed),
							    setup: function (element) {
							        var value = element.hasAttribute('rows') && element.getAttribute('rows');
							        this.setValue(value || '');
							    },
							    commit: function (element) {
							        if (this.getValue())
							            element.setAttribute('rows', this.getValue());
							        else
							            element.removeAttribute('rows');
							    }
							},
                            {
                                id: 'maxLength',
                                type: 'text',
                                label: editor.lang.textfield.maxChars,
                                'default': '',
                                accessKey: 'M',
                                style: 'width:50px',
                                validate: CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed),
                                setup: function (element) {
                                    var value = element.hasAttribute('maxLength') && element.getAttribute('maxLength');
                                    this.setValue(value || '');
                                },
                                commit: function (element) {
                                    if (this.getValue())
                                        element.setAttribute('maxLength', this.getValue());
                                    else
                                        element.removeAttribute('maxLength');
                                }
                            }
						]
					},
					{
					    id: 'value',
					    type: 'textarea',
					    label: editor.lang.textfield.value,
					    'default': '',
					    setup: function (element) {
					        this.setValue(element.$.defaultValue);
					    },
					    commit: function (element) {
					        element.$.value = element.$.defaultValue = this.getValue();
					    }
					},
                    CKEDITOR.dialog.beyondbit.entity.isEmpty,
                    CKEDITOR.dialog.beyondbit.entity.maxtitle,
                    CKEDITOR.dialog.beyondbit.entity.title,
                    CKEDITOR.dialog.beyondbit.entity.editeNode,
                    CKEDITOR.dialog.beyondbit.entity.inVisibleNode,
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                            CKEDITOR.dialog.beyondbit.entity.bizTableName,
                            CKEDITOR.dialog.beyondbit.entity.fieldName
                        ]
                    },
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                            {
                                id: 'areaMode',
                                type: 'select',
                                label: "*类型",
                                'default': 'inputShow',
                                accessKey: 'M',
                                items:
                                [
                                    ["输入显示", "inputShow"],
                                    ["仅输入", "onlyInput"]
                                ],
                                setup: function (element) {
                                    this.setValue($(element.$).attr("areaMode"));
                                },
                                onChange: function () {
                                    var dialog = this.getDialog(),
										fieldvalues = $(dialog.getContentElement('info', 'saveColumns').getInputElement().$)
                                                        .parent().parent().parent(),
                                        typecode = $(dialog.getContentElement('info', 'typecode').getInputElement().$)
                                                        .parent().parent().parent(),
                                        selecttext = this.getValue();
                                    if (selecttext == "inputShow") {
                                        dialog.getContentElement('info', 'saveColumns').validate = CKEDITOR.dialog.validate.columnsNotEmpty("保存字段格式错误！");
                                        typecode.css("display", "block");
                                        fieldvalues.css("display", "block");
                                    } else {
                                        dialog.getContentElement('info', 'saveColumns').validate = function () { return true; };
                                        typecode.css("display", "none");
                                        fieldvalues.css("display", "none");
                                    }

                                },
                                commit: function (data) {
                                    var element = data.$;
                                    value = this.getValue();
                                    $(element).attr("areaMode", value);
                                }
                            },
                            {
                                id: 'typecode',
                                type: 'text',
                                label: "附件类型标识(格式:正整数)",
                                'default': '',
                                accessKey: 'M',
                                validate: CKEDITOR.dialog.validate.integer("标识必需为正整数！"),
                                setup: function (element) {
                                    this.setValue($(element.$).attr("typecode"));
                                },
                                commit: function (data) {
                                    $(data.$).attr("typecode", this.getValue());
                                }
                            }
                        ]
                    },
                      {
                          type: 'hbox',
                          widths: ['50%', '50%'],
                          children:
						    [
                                {
                                    id: 'saveColumns',
                                    type: 'text',
                                    label: "*保存字段(格式：NodeName:%currentNodeName%,SignerName:%currentUserName%)",
                                    'default': "NodeName:%currentNodeName%,SignerName:%currentUserName%",
                                    accessKey: 'M',
                                    items: [],
                                    validate: CKEDITOR.dialog.validate.columnsNotEmpty("保存字段格式错误！"),
                                    setup: function (element) {
                                        this.setValue($(element.$).attr("saveColumns"));
                                    },
                                    commit: function (data) {
                                        var element = data.$;
                                        if ($(this.getDialog().getContentElement('info', 'areaMode').getInputElement().$).val() == "inputShow") {
                                            var value = this.getValue();
                                            $(element).attr("saveColumns", value);
                                        } else {
                                            $(element).attr("saveColumns", "");
                                        };
                                    }
                                }
                            ]
                      }

				]
			}
		]
    };
});
