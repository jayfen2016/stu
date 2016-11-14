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
11.relationControl=''
12.parentCode=''
13.multipleSelect=''
14.loadDataBindDepth=''
15.selectmode=''
16.maxDataBindDepth=''
17.depthCanSelect=''
18.canNumSelect=''
19.isChangeText=''
20.onlyUnit=''
*/
CKEDITOR.dialog.add('formselecttextbox', function (editor) {


    var autoAttributes =
	{
	    value: 1,
	    size: 1,
	    maxLength: 1
	};

    var acceptedTypes =
	{
	    text: 1,
	    password: 1
	};

    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1,
        fieldvalue: 1
    };

    return {
        title: "表单选择",
        minWidth: 400,
        minHeight: 500,
        onShow: function () {
            delete this.textField;
            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });
            var element = this.getParentEditor().getSelection().getSelectedElement();
            if (element && element.getName() == "input" &&
					(acceptedTypes[element.getAttribute('type')] || !element.getAttribute('type'))) {
                this.textField = element;
                this.setupContent(element);
            }
        },
        onOk: function () {
            var editor = this.getParentEditor(),
				element = this.textField,
				isInsertMode = !element;

            if (isInsertMode) {
                element = editor.document.createElement('input');
                element.setAttribute('type', 'text');
                element.setAttribute("ischangetext", "false");
                element.setAttribute("typeCode", "");
                element.setAttribute("canWrite", "true");
                element.setAttribute("formFlag", "true");
                element.setAttribute('id', $(editor).formdesign().Guid());
            }

            this.commitContent({ element: element });

            var setHiddenValue = function (hidden) {
                hidden.setAttribute("typeCode", element.getAttribute("typeCode"));
                hidden.setAttribute("fieldvalue", element.getAttribute("fieldValue"));
                hidden.setAttribute("bizTableName", element.getAttribute("bizTableName"));
                hidden.setAttribute("relTableName", element.getAttribute("relTableName"));
                hidden.setAttribute("editeNode", element.getAttribute("editeNode"));
                hidden.setAttribute("inVisibleNode", element.getAttribute("inVisibleNode"));
            }

            if (isInsertMode) {
                editor.insertElement(element);
                var selection = editor.getSelection();
                var ranges = selection.getRanges(true);
                if (ranges.length == 1 && ranges[0].collapsed) {
                    var hidden = new CKEDITOR.dom.element("input", editor.document);
                    hidden.setAttribute("id", element.getAttribute("id") + "_hidden");
                    hidden.setAttribute("type", "hidden");
                    hidden.setAttribute("canWrite", "true");
                    hidden.setAttribute("formFlag", "true");
                    setHiddenValue(hidden);
                    var fakeElement = editor.createFakeElement(hidden, 'cke_hidden', 'hiddenfield');
                    ranges[0].insertNode(fakeElement);
                }
                selection.selectElement(element);
            } else {
                if ($("img[data-cke-realelement*=" + element.getAttribute("id") + "_hidden]", editor.document.$).length > 0) {
                    var hidden = new CKEDITOR.dom.element("input", editor.document);
                    var hiddenelement =
                        $("img[data-cke-realelement*=" + element.getAttribute("id") + "_hidden]", editor.document.$)[0];
                    hiddenelement = CKEDITOR.dom.element.get(hiddenelement);
                    hidden = editor.restoreRealElement(hiddenelement);
                    setHiddenValue(hidden);
                    var fakeElement = editor.createFakeElement(hidden, 'cke_hidden', 'hiddenfield');
                    fakeElement.replace(hiddenelement);

                }
            }

        },
        onLoad: function () {
            var autoSetup = function (element) {
                var value = element.hasAttribute(this.id) && element.getAttribute(this.id);
                this.setValue(value || '');
            };

            var autoCommit = function (data) {
                var element = data.element;
                var value = this.getValue();

                if (value)
                    element.setAttribute(this.id, value);
                else
                    element.removeAttribute(this.id);
            };

            this.foreach(function (contentObj) {
                if (autoAttributes[contentObj.id]) {
                    contentObj.setup = autoSetup;
                    contentObj.commit = autoCommit;
                }

            });
        },
        contents: [
			{
			    id: 'info',
			    label: editor.lang.textfield.title,
			    title: editor.lang.textfield.title,
			    elements: [
					{
					    type: 'hbox',
					    widths: ['50%', '50%'],
					    children:
						[
							{
							    id: '_cke_saved_name',
							    type: 'text',
							    label: editor.lang.textfield.name,
							    'default': '',
							    accessKey: 'N',
							    setup: function (element) {
							        this.setValue(
											element.data('cke-saved-name') ||
											element.getAttribute('name') ||
											'');
							    },
							    commit: function (data) {
							        var element = data.element;

							        if (this.getValue())
							            element.data('cke-saved-name', this.getValue());
							        else {
							            element.data('cke-saved-name', false);
							            element.removeAttribute('name');
							        }
							    }
							},
							{
							    id: 'value',
							    type: 'text',
							    label: editor.lang.textfield.value,
							    'default': '',
							    accessKey: 'V',
							    commit: function (data) {
							        var element = data.element.$;
							        var value = this.getValue();
							        $(element).attr("maxDataBindDepth", value);
							    }
							}
						]
					},
					{
					    type: 'hbox',
					    widths: ['25%', '25%', '25%', '25%'],
					    children:
						[
							{
							    id: 'size',
							    type: 'text',
							    label: editor.lang.textfield.charWidth,
							    'default': '',
							    accessKey: 'C',
							    style: 'width:50px',
							    validate: CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed)
							},
							{
							    id: 'maxLength',
							    type: 'text',
							    label: editor.lang.textfield.maxChars,
							    'default': '',
							    accessKey: 'M',
							    style: 'width:50px',
							    validate: CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed)
							},
                             {
                                 id: 'selectmode',
                                 type: 'select',
                                 label: "模式",
                                 'default': 'org',
                                 accessKey: 'S',

                                 items:
                                [
                                    ["字典", "dictionary"],
                                    ["模块", "module"],
                                    ["文号", "num"],
                                    ["组织", "org"],
                                    ["用户", "user"]
                                ],
                                 setup: function (element) {
                                     this.setValue($(element.$).attr("selectmode"));
                                 },
                                 commit: function (data) {
                                     var element = data.element.$;
                                     var value = this.getValue();
                                     $(element).attr("selectmode", value);
                                     if (value == "num") {
                                         $(element).attr("canNumSelect", "0");
                                     } else {
                                         $(element).removeAttr("canNumSelect");
                                     }
                                 },
                                 onChange: function () {
                                     var value = this.getValue();
                                     var dialog = this.getDialog(),
										onlyUnit = $(dialog.getContentElement('info', 'onlyUnit').getInputElement().$).parent().parent()
                                     if (value == "org") {
                                         onlyUnit.css("display", "block");
                                     } else {
                                         onlyUnit.css("display", "none");
                                     }
                                 }
                             },
                            {
                                id: 'loadDataBindDepth',
                                type: 'text',
                                label: "默认加载层数",
                                'default': '',
                                accessKey: 'S',
                                value: "",
                                setup: function (element) {
                                    this.setValue($(element.$).attr("loadDataBindDepth"));
                                },
                                commit: function (data) {
                                    var element = data.element.$;
                                    var value = this.getValue();
                                    $(element).attr("loadDataBindDepth", value);
                                }
                            }
						],
					    onLoad: function () {
					        // Repaint the style for IE7 (#6068)
					        if (CKEDITOR.env.ie7Compat)
					            this.getElement().setStyle('zoom', '100%');
					    }

					},
                   
                          {
                              id: 'onclick',
                              type: 'text',
                              label: "点击脚本",
                              'default': '',
                              padding: 0,
                              setup: function (element) {
                                  this.setValue($(element.$).attr("onclick") || $(element.$).attr("data-cke-pa-onclick"));
                              },
                              commit: function (data) {
                                  var element = data.element;
                                  if (this.getValue())
                                      element.data('cke-pa-onclick', this.getValue());
                                  else {
                                      element.data('cke-pa-onclick', false);
                                      element.removeAttribute('onclick');
                                  }
                              }
                          },
                {
                    id: 'onchange',
                    type: 'text',
                    label: "值改变脚本",
                    'default': '',
                    accessKey: 'M',
                    padding: 0,

                    setup: function (element) {
                        this.setValue($(element.$).attr("onchange") || $(element.$).attr("data-cke-pa-onchange"));
                    },
                    commit: function (data) {
                        var element = data.element;
                        if (this.getValue())
                            element.data('cke-pa-onchange', this.getValue());
                        else {
                            element.data('cke-pa-onchange', false);
                            element.removeAttribute('onchange');
                        }
                    }
                },
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[

                            {
                                id: 'maxDataBindDepth',
                                type: 'text',
                                label: "最大加载层数",
                                'default': '',
                                accessKey: 'S',
                                value: "",
                                setup: function (element) {
                                    this.setValue($(element.$).attr("maxDataBindDepth"));
                                },
                                commit: function (data) {
                                    var element = data.element.$;
                                    var value = this.getValue();
                                    $(element).attr("maxDataBindDepth", value);
                                }
                            },  {
                        id: 'depthCanSelect',
                        type: 'text',
                        label: "可选层级(示例：1,2)",
                        'default': '',
                        accessKey: 'S',
                        setup: function (element) {
                            this.setValue($(element.$).attr("depthCanSelect"));
                        },
                        commit: function (data) {
                            var element = data.element.$;
                            var value = this.getValue();
                            $(element).attr("depthCanSelect", value);
                        }
                    }
                        ]
                    },
                  

                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        padding: 0,
                        children:
						[
                              {
                                  id: 'parentCode',
                                  type: 'text',
                                  label: "*字典Code",
                                  'default': '',
                                  accessKey: 'S',
                                  validate: CKEDITOR.dialog.validate.notEmpty("字典Code不能为空！"),
                                  setup: function (element) {
                                      var selecttext = $(element.$).attr("parentCode");
                                      this.setValue(selecttext);
                                  },
                                  commit: function (data) {
                                      var element = data.element.$;
                                      $(element).attr("parentCode", this.getValue());
                                  }
                              }
                        ]
                    },
{
    type: 'hbox',
    widths: ['30%', '30%', '40%'],
    children:
						[
                            {
                                id: 'multipleSelect',
                                type: 'checkbox',
                                label: "允许多选",
                                'default': '',
                                accessKey: 'S',
                                value: "checked",
                                setup: function (element) {
                                    if ($(element.$).attr("multipleSelect") == "true")
                                        this.setValue($(element.$).attr("multipleSelect"));
                                },
                                commit: function (data) {
                                    var element = data.element.$;
                                    var value = this.getValue();
                                    if (value)
                                        $(element).attr("multipleSelect", "true");
                                    else
                                        $(element).attr("multipleSelect", "false");
                                }
                            },
                            {
                                id: 'onlyUnit',
                                type: 'checkbox',
                                label: "只显示单位",
                                'default': '',
                                accessKey: 'S',
                                value: "checked",
                                setup: function (element) {
                                    if ($(element.$).attr("onlyUnit") == "true" || $(element.$).attr("data-cke-pa-onlyunit") == "true")
                                        this.setValue("checked");
                                },
                                commit: function (data) {
                                    var element = data.element;
                                    if (this.getValue())
                                        element.data('cke-pa-onlyunit', "true");
                                    else {
                                        element.data('cke-pa-onlyunit', "false");
                                    }
                                }
                            },
                            {
                                id: 'isEmpty',
                                type: 'checkbox',
                                label: "允许为空(不为空时，将提示用户)",
                                'default': '',
                                accessKey: 'S',
                                value: "checked",
                                setup: function (element) {
                                    if ($(element.$).attr("isEmpty") == "true")
                                        this.setValue($(element.$).attr("isEmpty"));
                                },
                                commit: function (data) {
                                    var element = data.element.$;

                                    var value = this.getValue();
                                    if (value)
                                        $(element).attr("isEmpty", "true");
                                    else
                                        $(element).attr("isEmpty", "false");

                                }
                            }
                        ]
},
 {
     id: 'title',
     type: 'text',
     label: "提示信息",
     'default': '',
     accessKey: 'M',
     setup: function (element) {
         this.setValue($(element.$).attr("title"));
     },
     commit: function (data) {
         var element = data.element.$;
         var value = this.getValue();
         $(element).attr("title", value);
     }
 },
                    {
                        id: 'editeNode',
                        type: 'text',
                        label: "*编辑节点(格式：[流程节点1,流程节点X])",
                        'default': '',
                        accessKey: 'M',
                        validate: CKEDITOR.dialog.validate.nodeList("格式错误"),
                        setup: function (element) {
                            this.setValue($(element.$).attr("editeNode"));
                        },
                        commit: function (data) {
                            var element = data.element.$;
                            var value = this.getValue();
                            $(element).attr("editeNode", value);
                        }
                    },
                    {
                        id: 'inVisibleNode',
                        type: 'text',
                        label: "不可见节点(格式：[流程节点1,流程节点X])",
                        validate: CKEDITOR.dialog.validate.nodeListOrEmpty("格式错误"),
                        'default': '',
                        accessKey: 'M',
                        setup: function (element) {
                            this.setValue($(element.$).attr("inVisibleNode"));
                        },
                        commit: function (data) {
                            var element = data.element.$;
                            var value = this.getValue();
                            $(element).attr("inVisibleNode", value);
                        }
                    },
                    {
                        type: 'hbox',
                        widths: ['30%', '30%', '30%'],
                        children:
						[

                            {
                                id: 'bizTableName',
                                type: 'select',
                                label: "*绑定业务表",
                                'default': '',
                                accessKey: 'M',
                                items: [],
                                setup: function (element) {
                                    var selecttext = $(element.$).attr("relTableName");
                                    if (selecttext == "") {
                                        selecttext = $(element.$).attr("bizTableName");
                                    }
                                    $("option", this.getInputElement().$).each(function (i, obj) {
                                        $(this).removeAttr("selected");
                                    });

                                    $("option", this.getInputElement().$).each(function (i, obj) {
                                        if ($(this).attr("text") === selecttext) {
                                            $(this).attr("selected", "selected");
                                        }
                                    });
                                    this.onChange();
                                },
                                onChange: function () {
                                    var dialog = this.getDialog(),
										fieldvalues = $(dialog.getContentElement('info', 'fieldvalue').getInputElement().$),
                                        fieldNames = $(dialog.getContentElement('info', 'fieldName').getInputElement().$),
                                        selecttext = $($(this.getInputElement().$).find('option:selected')).attr("text"),
                                        _fields = $(editor).formdesign().GetfieldName(selecttext);
                                    fieldvalues.empty();
                                    fieldNames.empty();
                                    $(_fields).each(function (i, obj) {
                                        fieldvalues.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                        fieldNames.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                    });

                                },
                                commit: function (data) {
                                    var element = data.element.$;
                                    var value = this.getValue();
                                    if (value == "") {
                                        //无附表
                                        $(element).attr("bizTableName", $(this.getInputElement().$).find('option:selected').attr("text"));
                                        $(element).attr("relTableName", value);

                                    } else {
                                        $(element).attr("relTableName", $(this.getInputElement().$).find('option:selected').attr("text"));
                                        $(element).attr("bizTableName", value);
                                    }
                                },
                                onload: function () {
                                    var _tables = $(editor).formdesign().GetbizTableName();
                                    if (_tables === null) {
                                        //this.getDialog().hide();
                                    } else {
                                        var select = $(this.getInputElement().$);
                                        select.empty();
                                        $(_tables).each(function (i, obj) {
                                            if (obj.rel == "") {
                                                select.append("<option value='" + obj.rel + "'  selected=\"selected\" text='" + obj.biz + "' >" + obj.bizName + "</option>");
                                            } else {
                                                select.append("<option value='" + obj.biz + "'  text='" + obj.rel + "' >" + obj.relName + "</option>");
                                            }
                                        });
                                    }
                                }
                            },
                             {
                                 id: 'fieldName',
                                 type: 'select',
                                 label: "*绑定字段",
                                 'default': '',
                                 accessKey: 'M',
                                 items: [],
                                 validate: CKEDITOR.dialog.validate.notEmpty("绑定字段不能为空！"),
                                 setup: function (element) {
                                     this.setValue($(element.$).attr("fieldName"));
                                 },
                                 commit: function (data) {
                                     var element = data.element.$;
                                     var value = this.getValue();
                                     $(element).attr("fieldName", value);
                                 },
                                 onload: function () {
                                     var _fields = $(editor).formdesign().GetfieldName();
                                     var select = $(this.getInputElement().$);
                                     select.empty();
                                     $(_fields).each(function (i, obj) {
                                         select.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                     });
                                 }
                             },
                             {
                                 id: 'fieldvalue',
                                 type: 'select',
                                 label: "*绑定保存字段",
                                 'default': '',
                                 accessKey: 'M',
                                 items: [],
                                 validate: CKEDITOR.dialog.validate.notEmpty("绑定字段不能为空！"),
                                 setup: function (element) {
                                     this.setValue($(element.$).attr("fieldvalue"));
                                 },
                                 commit: function (data) {
                                     var element = data.element.$;
                                     var value = this.getValue();
                                     $(element).attr("fieldvalue", value);
                                 },
                                 onload: function () {
                                     var _fields = $(editor).formdesign().GetfieldName();
                                     var select = $(this.getInputElement().$);
                                     select.empty();
                                     $(_fields).each(function (i, obj) {
                                         select.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                     });
                                 }
                             }
                        ]
                    }
				]
			}
		]
    };
});
