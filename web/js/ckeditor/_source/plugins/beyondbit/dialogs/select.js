/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
1.formFlag='true' 
2.canWrite='true'
3.bindFlowID=''
4.bindFlow=''
5.bizTableName=''
6.relTableName=''
7.fieldName=''
8.editeNode=''
9.inVisibleNode=''
10.mode=''
11.parentCode=''
12.selectSQL=''
13.setSpace=''
*/
CKEDITOR.dialog.add('formselect', function (editor) {
    // Add a new option to a SELECT object (combo or list).
    function addOption(combo, optionText, optionValue, documentObject, index) {
        combo = getSelect(combo);
        var oOption;
        if (documentObject)
            oOption = documentObject.createElement("OPTION");
        else
            oOption = document.createElement("OPTION");

        if (combo && oOption && oOption.getName() == 'option') {
            if (CKEDITOR.env.ie) {
                if (!isNaN(parseInt(index, 10)))
                    combo.$.options.add(oOption.$, index);
                else
                    combo.$.options.add(oOption.$);

                oOption.$.innerHTML = optionText.length > 0 ? optionText : '';
                oOption.$.value = optionValue;
            }
            else {
                if (index !== null && index < combo.getChildCount())
                    combo.getChild(index < 0 ? 0 : index).insertBeforeMe(oOption);
                else
                    combo.append(oOption);

                oOption.setText(optionText.length > 0 ? optionText : '');
                oOption.setValue(optionValue);
            }
        }
        else
            return false;

        return oOption;
    }
    // Remove all selected options from a SELECT object.
    function removeSelectedOptions(combo) {
        combo = getSelect(combo);

        // Save the selected index
        var iSelectedIndex = getSelectedIndex(combo);

        // Remove all selected options.
        for (var i = combo.getChildren().count() - 1; i >= 0; i--) {
            if (combo.getChild(i).$.selected)
                combo.getChild(i).remove();
        }

        // Reset the selection based on the original selected index.
        setSelectedIndex(combo, iSelectedIndex);
    }
    //Modify option  from a SELECT object.
    function modifyOption(combo, index, title, value) {
        combo = getSelect(combo);
        if (index < 0)
            return false;
        var child = combo.getChild(index);
        child.setText(title);
        child.setValue(value);
        return child;
    }
    function removeAllOptions(combo) {
        combo = getSelect(combo);
        while (combo.getChild(0) && combo.getChild(0).remove())
        { /*jsl:pass*/ }
    }
    // Moves the selected option by a number of steps (also negative).
    function changeOptionPosition(combo, steps, documentObject) {
        combo = getSelect(combo);
        var iActualIndex = getSelectedIndex(combo);
        if (iActualIndex < 0)
            return false;

        var iFinalIndex = iActualIndex + steps;
        iFinalIndex = (iFinalIndex < 0) ? 0 : iFinalIndex;
        iFinalIndex = (iFinalIndex >= combo.getChildCount()) ? combo.getChildCount() - 1 : iFinalIndex;

        if (iActualIndex == iFinalIndex)
            return false;

        var oOption = combo.getChild(iActualIndex),
			sText = oOption.getText(),
			sValue = oOption.getValue();

        oOption.remove();

        oOption = addOption(combo, sText, sValue, (!documentObject) ? null : documentObject, iFinalIndex);
        setSelectedIndex(combo, iFinalIndex);
        return oOption;
    }
    function getSelectedIndex(combo) {
        combo = getSelect(combo);
        return combo ? combo.$.selectedIndex : -1;
    }
    function setSelectedIndex(combo, index) {
        combo = getSelect(combo);
        if (index < 0)
            return null;
        var count = combo.getChildren().count();
        combo.$.selectedIndex = (index >= count) ? (count - 1) : index;
        return combo;
    }
    function getOptions(combo) {
        combo = getSelect(combo);
        return combo ? combo.getChildren() : false;
    }
    function getSelect(obj) {
        if (obj && obj.domId && obj.getInputElement().$)				// Dialog element.
            return obj.getInputElement();
        else if (obj && obj.$)
            return obj;
        return false;
    }
    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1,
        mode: 1,
        savevalue:1
    };
    return {
        title: editor.lang.select.title,
        minWidth: CKEDITOR.env.ie ? 450 : 385,
        minHeight: CKEDITOR.env.ie ? 500 : 500,
        onShow: function () {
            delete this.selectBox;
            //this.setupContent('clear');
            var element = this.getParentEditor().getSelection().getSelectedElement();
            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });
            //this.setupContent('clear');
            if (element && element.getName() == "select") {
                this.selectBox = element;
                this.setupContent(element.getName(), element);

                // Load Options into dialog.
                var objOptions = getOptions(element);
                for (var i = 0; i < objOptions.count(); i++)
                    this.setupContent('option', objOptions.getItem(i));
            }
        },
        onOk: function () {
            var editor = this.getParentEditor(),
				element = this.selectBox,
				isInsertMode = !element;

            if (isInsertMode) {
                element = editor.document.createElement('select');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
            }
            this.commitContent(element);

            if (isInsertMode) {
                editor.insertElement(element);
                if (CKEDITOR.env.ie) {
                    var sel = editor.getSelection(),
						bms = sel.createBookmarks();
                    setTimeout(function () {
                        sel.selectBookmarks(bms);
                    }, 0);
                }
            }
        },

        contents: [
			{
			    id: 'info',
			    label: editor.lang.select.selectInfo,
			    title: editor.lang.select.selectInfo,
			    accessKey: '',
			    elements: [
					{
					    id: 'txtName',
					    type: 'text',
					    widths: ['25%', '75%'],
					    labelLayout: 'horizontal',
					    label: editor.lang.common.name,
					    'default': '',
					    accessKey: 'N',
					    style: 'width:350px',
					    setup: function (name, element) {
					        if (name == 'clear')
					            this.setValue(this['default'] || '');
					        else if (name == 'select') {
					            this.setValue(
										element.data('cke-saved-name') ||
										element.getAttribute('name') ||
										'');
					        }
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
					    id: 'txtValue',
					    type: 'text',
					    widths: ['25%', '75%'],
					    labelLayout: 'horizontal',
					    label: editor.lang.select.value,
					    style: 'width:350px',
					    'default': '',
					    className: 'cke_disabled',
					    onLoad: function () {
					        this.getInputElement().setAttribute('readOnly', true);
					    },
					    setup: function (name, element) {
					        if (name == 'clear')
					            this.setValue('');
					        else if (name == 'option' && element.getAttribute('selected'))
					            this.setValue(element.$.value);
					    }
					},
	                {
                    type: 'hbox',
                    widths: ['50%', '50%'],
                    children:
				    [
                    {
							    id: 'txtSize',
							    type: 'text',
							    labelLayout: 'horizontal',
							    label: editor.lang.select.size,
							    'default': '',
							    accessKey: 'S',
							    style: 'width:175px',
							    validate: function () {
							        var func = CKEDITOR.dialog.validate.integer(editor.lang.common.validateNumberFailed);
							        return ((this.getValue() === '') || func.apply(this));
							    },
							    setup: function (name, element) {
							        if (name == 'select')
							            this.setValue(element.getAttribute('size') || '');
							        if (CKEDITOR.env.webkit)
							            this.getInputElement().setStyle('width', '86px');
							    },
							    commit: function (element) {
							        if (this.getValue())
							            element.setAttribute('size', this.getValue());
							        else
							            element.removeAttribute('size');
							    }
							},
							{
							    type: 'html',
							    html: '<span>' + CKEDITOR.tools.htmlEncode(editor.lang.select.lines) + '</span>'
							},
                        {
                            id: 'isEmpty',
                            type: 'checkbox',
                            label: "允许为空(不为空时，将提示用户)",
                            'default': '',
                            accessKey: 'S',
                            value: "checked",
                            setup: function (name, element) {
                                if (name == 'select') {
                                    if ($(element.$).attr("isEmpty") == "true")
                                        this.setValue($(element.$).attr("isEmpty"));
                                }
                            },
                            commit: function (data) {
                                var element = data.$;

                                var value = this.getValue();
                                if (value)
                                    $(element).attr("isEmpty", "true");
                                else
                                    $(element).attr("isEmpty", "false");

                            }
                        },
                        {
                            id: 'setSpace',
                            type: 'checkbox',
                            label: "添加空行",
                            'default': '',
                            accessKey: 'M',
                            setup: function (name, element) {
                                if (name == 'select') {
                                    if ($(element.$).attr("setSpace") == "true")
                                        this.setValue($(element.$).attr("setSpace"));
                                }
                            },
                            commit: function (data) {
                                var element = data.$;
                                var value = this.getValue();
                                if (value)
                                    $(element).attr("setSpace", "true");
                                else
                                    $(element).attr("setSpace", "false");
                            }
                        },
				    {
				        id: 'chkMulti',
				        type: 'checkbox',
				        label: editor.lang.select.chkMulti,
				        'default': '',
				        accessKey: 'M',
				        value: "checked",
				        setup: function (name, element) {
				            if (name == 'select')
				                this.setValue(element.getAttribute('multiple'));
				            if (CKEDITOR.env.webkit)
				                this.getElement().getParent().setStyle('vertical-align', 'middle');
				        },
				        commit: function (element) {
				            if (this.getValue())
				                element.setAttribute('multiple', this.getValue());
				            else
				                element.removeAttribute('multiple');
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
                    setup: function (name, element) {
                        if (name == 'select') 
                        this.setValue($(element.$).attr("title"));
                    },
                    commit: function (data) {
                        $(data.$).attr("title", this.getValue());
                    }
                },
                {
                    id: 'editeNode',
                    type: 'text',
                    label: "*编辑节点(格式：[流程节点1,流程节点X])",
                    'default': '',
                    accessKey: 'M',
                    validate: CKEDITOR.dialog.validate.nodeList("格式错误"),
                    setup: function (name, element) {
                        if (name == 'select') {
                            this.setValue($(element.$).attr("editeNode"));
                        }

                    },
                    commit: function (data) {
                        var element = data.$;
                        var value = this.getValue();
                        $(element).attr("editeNode", value);
                    }
                },
                {
                    id: 'inVisibleNode',
                    type: 'text',
                    label: "不可见节点(格式：[流程节点1,流程节点X])",
                    'default': '',
                    accessKey: 'M',
                    validate: CKEDITOR.dialog.validate.nodeListOrEmpty("格式错误"),
                    setup: function (name, element) {
                       if (name == 'select') {
                            this.setValue($(element.$).attr("inVisibleNode"));
                        }

                    },
                    commit: function (data) {
                        var element = data.$;
                        var value = this.getValue();
                        $(element).attr("inVisibleNode", value);
                    }
                },
                {
                    type: 'hbox',
                    widths: ['50%', '50%'],
                    children:
			    [
                    {
                        id: 'bizTableName',
                        type: 'select',
                        label: "*绑定业务表",
                        'default': '',
                        accessKey: 'M',
                        items: [],
                        setup: function (name, element) {
                            var select = this;
                            if (name == 'select') {
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
                            }

                        },
                        onChange: function () {
                            var dialog = this.getDialog(),
										fieldvalues = $(dialog.getContentElement('info', 'fieldName').getInputElement().$),
                                        savevalues = $(dialog.getContentElement('info', 'savevalue').getInputElement().$),
                                        selecttext = $($(this.getInputElement().$).find('option:selected')).attr("text"),
                                        _fields = $(editor).formdesign().GetfieldName(selecttext);
                            fieldvalues.empty();
                            savevalues.empty();
                            $(_fields).each(function (i, obj) {
                                fieldvalues.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                savevalues.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                            });
                        },
                        commit: function (data) {
                            var element = data.$;
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
                        setup: function (name, element) {
                            if (name == 'select') {
                                this.setValue($(element.$).attr("fieldName"));
                            }

                        },
                        commit: function (data) {
                            var element = data.$;
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
                            id: 'savevalue',
                            type: 'select',
                            label: "*绑定字段",
                            'default': '',
                            accessKey: 'M',
                            items: [],
                            validate: CKEDITOR.dialog.validate.notEmpty("绑定保存字段不能为空！"),
                            setup: function (name, element) {
                                if (name == 'select') {
                                    this.setValue($(element.$).attr("savevalue"));
                                }

                            },
                            commit: function (data) {
                                var element = data.$;
                                var value = this.getValue();
                                $(element).attr("savevalue", value);
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
                          id: 'mode',
                          type: 'select',
                          label: "*数据源模式",
                          'default': 'input',
                          accessKey: 'M',
                          items:
                            [
                                ["字典", "dictionary"],
                                ["输入", "input"],
                                ["数据库", "sql"]
                            ],
                          setup: function (name, element) {
                              if (name == 'clear')
                                  this.setValue('input');
                              else if (name == 'select') {
                                  var selecttext = $(element.$).attr("mode");
                                  this.setValue(selecttext);
                              }
                          },
                          onChange: function () {
                              var dialog = this.getDialog(),
                        selectSQL = $(dialog.getContentElement('info', 'selectSQL').getInputElement().$).parent().parent().parent(),
                        parentCode = $(dialog.getContentElement('info', 'parentCode').getInputElement().$).parent().parent().parent(),
                        optionToolTip = $(dialog.getContentElement('info', 'optionToolTip').getInputElement().$).parent().parent();
                              switch (this.getValue()) {
                                  case "dictionary":
                                      parentCode.css("display", "block");
                                      selectSQL.css("display", "none");
                                      optionToolTip.css("display", "none");
                                      break;
                                  case "sql":
                                      selectSQL.css("display", "block");
                                      parentCode.css("display", "none");
                                      optionToolTip.css("display", "none");
                                      break;
                                  default:
                                      selectSQL.css("display", "none");
                                      parentCode.css("display", "none");
                                      optionToolTip.css("display", "block");
                              }

                          },
                          commit: function (data) {
                              var element = data.$;
                              var value = this.getValue();
                              $(element).attr("mode", value);
                          },
                          onload: function () {
                              this.onChange();
                          }
                      }
                ]
                },
                {
                    type: 'hbox',
                    widths: ['20%', '40%', '40%'],
                    children:
			        [
                    
                        {
                            id: 'parentCode',
                            type: 'text',
                            label: "字典Code",
                            'default': '',
                            accessKey: 'S',
                            value: "checked",
                            setup: function (name, element) {
                                if (name == 'select') {
                                    var selecttext = $(element.$).attr("parentCode");
                                    this.setValue(selecttext);
                                }
                            },
                            commit: function (data) {
                                var element = data.$;
                                $(element).attr("parentCode", this.getValue());
                            }
                        }, {
                            id: 'selectSQL',
                            type: 'text',
                            label: "查询SQL",
                            'default': '',
                            accessKey: 'S',
                            value: "checked",
                            setup: function (name, element) {
                                if (name == 'select') {
                                    var selecttext = $(element.$).attr("selectSQL");
                                    this.setValue(selecttext);
                                }
                            },
                            commit: function (data) {
                                var element = data.$;
                                $(element).attr("selectSQL", this.getValue());
                            }
                        }
                    ]
                    }, 
                {
                    id: "optionToolTip",
                    type: 'vbox',
                    widths: ['100%', '100%', '100%'],
                    children:
				    [
					{

					    type: 'html',
					    html: '<span>' + CKEDITOR.tools.htmlEncode(editor.lang.select.opAvail) + '</span>'
					},
					{
					    type: 'hbox',
					    widths: ['115px', '115px', '100px'],
					    children:
						[
							{
							    type: 'vbox',
							    children:
								[
									{
									    id: 'txtOptName',
									    type: 'text',
									    label: editor.lang.select.opText,
									    style: 'width:115px',
									    setup: function (name, element) {
									        if (name == 'clear')
									            this.setValue("");
									    }
									},
									{
									    type: 'select',
									    id: 'cmbName',
									    label: '',
									    title: '',
									    size: 5,
									    style: 'width:115px;height:75px',
									    items: [],
									    onChange: function () {
									        var dialog = this.getDialog(),
												values = dialog.getContentElement('info', 'cmbValue'),
												optName = dialog.getContentElement('info', 'txtOptName'),
												optValue = dialog.getContentElement('info', 'txtOptValue'),
												iIndex = getSelectedIndex(this);

									        setSelectedIndex(values, iIndex);
									        optName.setValue(this.getValue());
									        optValue.setValue(values.getValue());
									    },
									    setup: function (name, element) {
									        if (name == 'select')
									            removeAllOptions(this);
									         if (name == 'option')
									            addOption(this, element.getText(), element.getText(),
													this.getDialog().getParentEditor().document);
									    },
									    commit: function (element) {
									        var dialog = this.getDialog(),
												optionsNames = getOptions(this),
												optionsValues = getOptions(dialog.getContentElement('info', 'cmbValue')),
												selectValue = dialog.getContentElement('info', 'txtValue').getValue();

									        removeAllOptions(element);

									        for (var i = 0; i < optionsNames.count(); i++) {
									            var oOption = addOption(element, optionsNames.getItem(i).getValue(),
													optionsValues.getItem(i).getValue(), dialog.getParentEditor().document);
									            if (optionsValues.getItem(i).getValue() == selectValue) {
									                oOption.setAttribute('selected', 'selected');
									                oOption.selected = true;
									            }
									        }
									    }
									}
								]
							},
							{
							    type: 'vbox',
							    children:
								[
									{
									    id: 'txtOptValue',
									    type: 'text',
									    label: editor.lang.select.opValue,
									    style: 'width:115px',
									    setup: function (name, element) {
									        if (name == 'clear')
									            this.setValue("");
									    }
									},
									{
									    type: 'select',
									    id: 'cmbValue',
									    label: '',
									    size: 5,
									    style: 'width:115px;height:75px',
									    items: [],
									    onChange: function () {
									        var dialog = this.getDialog(),
												names = dialog.getContentElement('info', 'cmbName'),
												optName = dialog.getContentElement('info', 'txtOptName'),
												optValue = dialog.getContentElement('info', 'txtOptValue'),
												iIndex = getSelectedIndex(this);

									        setSelectedIndex(names, iIndex);
									        optName.setValue(names.getValue());
									        optValue.setValue(this.getValue());
									    },
									    setup: function (name, element) {
									        if(name=='select')
									            removeAllOptions(this);
									        if (name == 'option') {
									            var oValue = element.getValue();
									            addOption(this, oValue, oValue,
													this.getDialog().getParentEditor().document);
									            if (element.getAttribute('selected') == 'selected')
									                this.getDialog().getContentElement('info', 'txtValue').setValue(oValue);
									        }
									    }
									}
								]
							},
							{
							    type: 'vbox',
							    padding: 5,
							    children:
								[
									{
									    type: 'button',
									    id: 'btnAdd',
									    style: '',
									    label: editor.lang.select.btnAdd,
									    title: editor.lang.select.btnAdd,
									    style: 'width:100%;',
									    onClick: function () {
									        //Add new option.
									        var dialog = this.getDialog(),
												parentEditor = dialog.getParentEditor(),
												optName = dialog.getContentElement('info', 'txtOptName'),
												optValue = dialog.getContentElement('info', 'txtOptValue'),
												names = dialog.getContentElement('info', 'cmbName'),
												values = dialog.getContentElement('info', 'cmbValue');

									        addOption(names, optName.getValue(), optName.getValue(), dialog.getParentEditor().document);
									        addOption(values, optValue.getValue(), optValue.getValue(), dialog.getParentEditor().document);

									        optName.setValue("");
									        optValue.setValue("");
									    }
									},
									{
									    type: 'button',
									    id: 'btnModify',
									    label: editor.lang.select.btnModify,
									    title: editor.lang.select.btnModify,
									    style: 'width:100%;',
									    onClick: function () {
									        //Modify selected option.
									        var dialog = this.getDialog(),
												optName = dialog.getContentElement('info', 'txtOptName'),
												optValue = dialog.getContentElement('info', 'txtOptValue'),
												names = dialog.getContentElement('info', 'cmbName'),
												values = dialog.getContentElement('info', 'cmbValue'),
												iIndex = getSelectedIndex(names);

									        if (iIndex >= 0) {
									            modifyOption(names, iIndex, optName.getValue(), optName.getValue());
									            modifyOption(values, iIndex, optValue.getValue(), optValue.getValue());
									        }
									    }
									},
									{
									    type: 'button',
									    id: 'btnUp',
									    style: 'width:100%;',
									    label: editor.lang.select.btnUp,
									    title: editor.lang.select.btnUp,
									    onClick: function () {
									        //Move up.
									        var dialog = this.getDialog(),
												names = dialog.getContentElement('info', 'cmbName'),
												values = dialog.getContentElement('info', 'cmbValue');

									        changeOptionPosition(names, -1, dialog.getParentEditor().document);
									        changeOptionPosition(values, -1, dialog.getParentEditor().document);
									    }
									},
									{
									    type: 'button',
									    id: 'btnDown',
									    style: 'width:100%;',
									    label: editor.lang.select.btnDown,
									    title: editor.lang.select.btnDown,
									    onClick: function () {
									        //Move down.
									        var dialog = this.getDialog(),
												names = dialog.getContentElement('info', 'cmbName'),
												values = dialog.getContentElement('info', 'cmbValue');

									        changeOptionPosition(names, 1, dialog.getParentEditor().document);
									        changeOptionPosition(values, 1, dialog.getParentEditor().document);
									    }
									}
								]
							}
						]
					}, {
					    type: 'hbox',
					    widths: ['40%', '20%', '40%'],
					    children:
			    [
				    {
				        type: 'button',
				        id: 'btnSetValue',
				        label: editor.lang.select.btnSetValue,
				        title: editor.lang.select.btnSetValue,
				        onClick: function () {
				            //Set as default value.
				            var dialog = this.getDialog(),
							    values = dialog.getContentElement('info', 'cmbValue'),
							    txtValue = dialog.getContentElement('info', 'txtValue');
				            txtValue.setValue(values.getValue());
				        }
				    },
				    {
				        type: 'button',
				        id: 'btnDelete',
				        label: editor.lang.select.btnDelete,
				        title: editor.lang.select.btnDelete,
				        onClick: function () {
				            // Delete option.
				            var dialog = this.getDialog(),
							    names = dialog.getContentElement('info', 'cmbName'),
							    values = dialog.getContentElement('info', 'cmbValue'),
							    optName = dialog.getContentElement('info', 'txtOptName'),
							    optValue = dialog.getContentElement('info', 'txtOptValue');

				            removeSelectedOptions(names);
				            removeSelectedOptions(values);

				            optName.setValue("");
				            optValue.setValue("");
				        }
				    }
			    ]
					}
    ]
                }
				]
			}
		]
    };
});
