/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

/**
beyondbit
**/
(function () {

    CKEDITOR.plugins.add('beyondbit',
{
    init: function (editor) {
        var lang = editor.lang;

        //#region 命令
        // All buttons use the same code to register. So, to avoid
        // duplications, let's use this tool function.
        var addButtonCommand = function (buttonName, commandName, dialogFile, label) {
            editor.addCommand(commandName, new CKEDITOR.dialogCommand(commandName));

            editor.ui.addButton(buttonName,
				{
				    label: label,
				    command: commandName
				});
            CKEDITOR.dialog.add(commandName, dialogFile);
        };

        var dialogPath = this.path + 'dialogs/';
        addButtonCommand('FormCheckBox', 'formcheckbox', dialogPath + 'checkbox.js', "表单复选框");
        addButtonCommand('FormRadio', 'formradio', dialogPath + 'radio.js', '表单单选框');
        addButtonCommand('FormTextBox', 'formtextfield', dialogPath + 'textfield.js', '表单文本框');
        addButtonCommand('FormTextarea', 'formtextarea', dialogPath + 'textarea.js', '表单多行文本框');
        addButtonCommand('FormContentTextarea', 'formcontenttextarea', dialogPath + 'contenttextarea.js', '表单正文文本框');
        addButtonCommand('FormSelect', 'formselect', dialogPath + 'select.js', '表单下拉框');
        addButtonCommand('FormButton', 'formbutton', dialogPath + 'button.js', '表单按钮');
        addButtonCommand('FormSignLink', 'formsignlink', dialogPath + 'link.js', '表单签名');
        addButtonCommand('FormHiddenField', 'formhiddenfield', dialogPath + 'hiddenfield.js', '表单隐藏域');
        addButtonCommand('FormCalendar', 'formcalendar', dialogPath + 'calendar.js', '表单日历');
        addButtonCommand('FormSelectTextBox', 'formselecttextbox', dialogPath + 'selecttextbox.js', '表单选择');
        addButtonCommand('FormSelectButtonText', 'formselectbuttontext', dialogPath + 'selectbuttontext.js', '表单按钮选择');
        addButtonCommand('FormShowTable', 'formshowtable', dialogPath + 'showtable.js', '表单显示列表');
        addButtonCommand('FormShowTableProperties', 'formshowtableProperties', dialogPath + 'showtable.js', '表单显示列表属性');
        addButtonCommand('FormEditGrid', 'formeditgrid', dialogPath + 'editgrid.js', '表单编辑列表');
        addButtonCommand('FormEditGridProperties', 'formeditgridProperties', dialogPath + 'editgrid.js', '表单编辑列表属性');
        addButtonCommand('FormUploadify', 'formuploadify', dialogPath + 'uploadify.js', '表单附件');
        addButtonCommand('FormUploadifyProperties', 'formuploadifyProperties', dialogPath + 'uploadify.js', '表单附件属性');

        addButtonCommand('CellInsertTextBox', 'cellInsertTextBox', dialogPath + 'celltextfield.js', '单元格文本框');
        addButtonCommand('CellInsertCalendar', 'cellInsertCalendar', dialogPath + 'cellcalendar.js', '单元格日历');
        addButtonCommand('CellInsertSelect', 'cellInsertSelect', dialogPath + 'cellselect.js', '单元格下拉框');
        addButtonCommand('CellInsertnumTextBox', 'cellInsertnumTextBox', dialogPath + 'cellnumtextfield.js', '单元格数字文本框');

        //#endregion

        //#region 右键菜单项  If the "contextmenu" plugin is loaded, register the listeners.
        if (editor.contextMenu) {
            editor.contextMenu.addListener(function (element) {
                if (!element || element.isReadOnly())
                    return null;
                var isTable = element.hasAscendant('table', 1);

                if (isTable) {
                    var table = element.getAscendant('table');
                    if (table != null) {
                        if (table.getAttribute("sql") !== null) {
                            return {
                                formshowtableProperties: CKEDITOR.TRISTATE_OFF
                            };
                        } else if (table.getAttribute("editeFlag") !== null) {
                            return {
                                formeditgridProperties: CKEDITOR.TRISTATE_OFF,
                                beyondbittablecell: CKEDITOR.TRISTATE_OFF
                            };
                        }
                    }
                }


                var isDiv = element.hasAscendant('div', 1);
                if (isDiv) {
                    var div = element.getAscendant('div');
                    if (div != null && div.getAttribute("fileflag") !== null) {
                        return { formuploadifyProperties: CKEDITOR.TRISTATE_OFF };
                    }
                }

                var anchor = CKEDITOR.plugins.link.tryRestoreFakeAnchor(editor, element);

                if (!anchor && !(anchor = CKEDITOR.plugins.link.getSelectedLink(editor))) { }
                else {
                    if (anchor && anchor.hasAttribute('formFlag'))
                        return { formsignlink: CKEDITOR.TRISTATE_OFF };
                }


                if (element.is('select') && element.getAttribute("formFlag") !== null)
                    return { formselect: CKEDITOR.TRISTATE_OFF };
                else if (element.is('textarea') && element.getAttribute("formFlag") !== null)
                    return { formtextarea: CKEDITOR.TRISTATE_OFF };
                else if (element.is('img') && element.data('cke-real-element-type') == 'hiddenfield')
                    return { formhiddenfield: CKEDITOR.TRISTATE_OFF };
                else if (element.is('input') && element.getAttribute("showtime") !== null)
                    return { formcalendar: CKEDITOR.TRISTATE_OFF };
                else if (element.is('input') && element.getAttribute("ischangetext") !== null)
                    return { formselecttextbox: CKEDITOR.TRISTATE_OFF };
                else if (element.is('input') && element.getAttribute("canNumSelect") !== null)
                    return { formselectbuttontext: CKEDITOR.TRISTATE_OFF };
                else if (element.is('input') && element.getAttribute('type') == "file")
                    return { formuploadifyProperties: CKEDITOR.TRISTATE_OFF };
                else if (element.is('input') && element.getAttribute("formFlag") !== null) {
                    switch (element.getAttribute('type')) {
                        case 'button':
                        case 'submit':
                        case 'reset':
                            return { formbutton: CKEDITOR.TRISTATE_OFF };
                            break;
                        case 'checkbox':
                            return { formcheckbox: CKEDITOR.TRISTATE_OFF };
                            break;
                        case 'radio':
                            return { formradio: CKEDITOR.TRISTATE_OFF };
                            break;
                        case 'image':
                            return { formimagebutton: CKEDITOR.TRISTATE_OFF };
                            break;
                        case 'file':
                            return { formuploadifyProperties: CKEDITOR.TRISTATE_OFF };
                            break;
                        default:
                            return { formtextfield: CKEDITOR.TRISTATE_OFF };
                    }
                }
                return null;
            });
        }
        //#endregion

        //#region 菜单项 If the "menu" plugin is loaded, register the menu items.
        if (editor.addMenuItems) {
            editor.addMenuItems(
				{
				    formcalendar:
					{
					    label: "表单日历属性",
					    command: 'formcalendar',
					    group: 'beyondbit'
					},
				    formsignlink:
					{
					    label: "表单签名属性",
					    command: 'formsignlink',
					    group: 'beyondbit'
					},
				    formselecttextbox:
					{
					    label: "表单选择属性",
					    command: 'formselecttextbox',
					    group: 'beyondbit'
					},

				    formselectbuttontext:
					{
					    label: "表单按钮选择属性",
					    command: 'formselectbuttontext',
					    group: 'beyondbit'
					},

				    formshowtable:
					{
					    label: "表单显示列表属性",
					    command: 'formshowtable',
					    group: 'beyondbit'
					},

				    formeditgridProperties:
					{
					    label: "表单编辑列表属性",
					    command: 'formeditgridProperties',
					    group: 'beyondbit'
					},

				    formuploadifyProperties:
					{
					    label: "表单附件属性",
					    command: 'formuploadifyProperties',
					    group: 'beyondbit'
					},
				    formshowtableProperties:
					{
					    label: "表单显示列表属性",
					    command: 'formshowtableProperties',
					    group: 'beyondbit'
					},

				    formcheckbox:
					{
					    label: "表单复选框属性",
					    command: 'formcheckbox',
					    group: 'beyondbit'
					},

				    formuploadify:
					{
					    label: "表单上传属性",
					    command: 'formuploadify',
					    group: 'beyondbit'
					},
				    formradio:
					{
					    label: "表单单选框属性",
					    command: 'formradio',
					    group: 'beyondbit'
					},

				    formtextfield:
					{
					    label: "表单文本框属性",
					    command: 'formtextfield',
					    group: 'beyondbit'
					},

				    formhiddenfield:
					{
					    label: "表单隐藏域属性",
					    command: 'formhiddenfield',
					    group: 'beyondbit'
					},

				    formsignlink:
					{
					    label: "表单签名属性",
					    command: 'formsignlink',
					    group: 'beyondbit'
					},

				    formbutton:
					{
					    label: "表单按钮属性",
					    command: 'formbutton',
					    group: 'beyondbit'
					},

				    formselect:
					{
					    label: "表单下拉框属性",
					    command: 'formselect',
					    group: 'beyondbit'
					},

				    formtextarea:
					{
					    label: "表单多行文本框属性",
					    command: 'formtextarea',
					    group: 'beyondbit'
					},
				    beyondbittablecell:
						{
						    label: "表单单元格",
						    group: 'beyondbit',
						    order: 1,
						    getItems: function () {
						        var selection = editor.getSelection();
						        return {
						            beyondbittablecell_insertTextBox: CKEDITOR.TRISTATE_OFF,
						            beyondbittablecell_insertCalendar: CKEDITOR.TRISTATE_OFF,
						            beyondbittablecell_insertSelect: CKEDITOR.TRISTATE_OFF,
						            beyondbittablecell_insertnumTextBox: CKEDITOR.TRISTATE_OFF
						        };
						    }
						},

				    beyondbittablecell_insertTextBox:
						{
						    label: "文本框",
						    group: 'beyondbit',
						    command: 'cellInsertTextBox',
						    order: 5
						},
				    beyondbittablecell_insertnumTextBox:
						{
						    label: "数字文本框",
						    group: 'beyondbit',
						    command: 'cellInsertnumTextBox',
						    order: 7
						},
				    beyondbittablecell_insertCalendar:
						{
						    label: "日历",
						    group: 'beyondbit',
						    command: 'cellInsertCalendar',
						    order: 10
						},
				    beyondbittablecell_insertSelect:
						{
						    label: "下拉框",
						    group: 'beyondbit',
						    command: 'cellInsertSelect',
						    order: 6
						}
				});
        }
        //#endregion 

        //#region 双击事件
        editor.on('doubleclick', function (evt) {
            var element = evt.data.element;
            if (element.is('table') && element.getAttribute("sql") !== null)
                evt.data.dialog = 'formshowtableProperties';
            if (element.is('table') && element.getAttribute("editeFlag") !== null)
                evt.data.dialog = 'formeditgridProperties';
            if (element.is('select') && element.getAttribute("formFlag") !== null)
                evt.data.dialog = 'formselect';
            else if (element.is('textarea') && element.getAttribute("formFlag") !== null)
                evt.data.dialog = 'formtextarea';
            else if (element.is('img') && element.data('cke-real-element-type') == 'hiddenfield')
                evt.data.dialog = 'formhiddenfield';
            else if (element.is('input') && element.getAttribute("showtime") !== null)
                evt.data.dialog = 'formcalendar';
            else if (element.is('input') && element.getAttribute("ischangetext") !== null)
                evt.data.dialog = 'formselecttextbox';
            else if (element.is('input') && element.getAttribute("canNumSelect") !== null)
                evt.data.dialog = 'formselectbuttontext';
            else if (element.is('input') && element.getAttribute("type") == "file")
                evt.data.dialog = 'formuploadifyProperties';
            else if (element.is('input') && element.getAttribute("formFlag") !== null) {
                switch (element.getAttribute('type')) {
                    case 'button':
                    case 'submit':
                    case 'reset':
                        evt.data.dialog = 'formbutton';
                        break;
                    case 'checkbox':
                        evt.data.dialog = 'formcheckbox';
                        break;
                    case 'radio':
                        evt.data.dialog = 'formradio';
                        break;
                    case 'image':
                        evt.data.dialog = 'formimagebutton';
                        break;
                    case 'file':
                        evt.data.dialog = 'formuploadify';
                        break;
                    default:
                        evt.data.dialog = 'formtextfield';
                        break;
                }
            }
        });
        //#endregion 
    },

    afterInit: function (editor) {
        var dataProcessor = editor.dataProcessor,
			htmlFilter = dataProcessor && dataProcessor.htmlFilter,
			dataFilter = dataProcessor && dataProcessor.dataFilter;

        // Cleanup certain IE form elements default values.
        if (CKEDITOR.env.ie) {
            htmlFilter && htmlFilter.addRules(
			{
			    elements:
				{
				    input: function (input) {
				        var attrs = input.attributes,
							type = attrs.type;
				        // Old IEs don't provide type for Text inputs #5522
				        if (!type)
				            attrs.type = 'text';
				        if (type == 'checkbox' || type == 'radio')
				            attrs.value == 'on' && delete attrs.value;
				    },
				    'vt:else': function (element) {
				        var ele = {};
				        ele.type = CKEDITOR.NODE_TEXT;
				        ele.value = "<vt:else/>";
				        //return ele;
				        return "";
				    }
				}
			});
        }

        if (dataFilter) {
            dataFilter.addRules(
			{
			    elements:
				{
				    'vt:else': function (element) {
				        var ele = {};
				        ele.type = CKEDITOR.NODE_TEXT;
				        ele.value = "vt_else";
				        return ele;
				    }
				}
			});
        }

        //#region 实体
        ///textfield textarea radio checkbox hidden calendar
        //CKEDITOR.dialog.beyondbit.entity.isEmpty,
        //CKEDITOR.dialog.beyondbit.entity.editeNode,
        //CKEDITOR.dialog.beyondbit.entity.inVisibleNode,
        //CKEDITOR.dialog.beyondbit.entity.bizTableName,
        //CKEDITOR.dialog.beyondbit.entity.fieldName
        //CKEDITOR.dialog.beyondbit.entity.onchange
        //CKEDITOR.dialog.beyondbit.entity.title
        CKEDITOR.dialog.beyondbit = {
            entity:
            {
                title:
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
                        $(data.$).attr("title", this.getValue());
                    }
                },

                maxtitle:
                {
                    id: 'maxtitle',
                    type: 'text',
                    label: "超过最多字符数提示",
                    'default': '',
                    accessKey: 'M',
                    setup: function (element) {
                        this.setValue($(element.$).attr("maxtitle"));
                    },
                    commit: function (data) {
                        $(data.$).attr("maxtitle", this.getValue());
                    }
                },




                editeNode:
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
                        $(data.$).attr("editeNode", this.getValue());
                    }
                },
                inVisibleNode: {
                    id: 'inVisibleNode',
                    type: 'text',
                    label: "不可见节点(格式：[流程节点1,流程节点X])",
                    'default': '',
                    accessKey: 'M',
                    validate: CKEDITOR.dialog.validate.nodeListOrEmpty("格式错误"),
                    setup: function (element) {
                        this.setValue($(element.$).attr("inVisibleNode"));
                    },
                    commit: function (data) {
                        var element = data.$;
                        $(element).attr("inVisibleNode", this.getValue());
                    }
                },
                isEmpty: {
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
                        var element = data.$;
                        if (this.getValue())
                            $(element).attr("isEmpty", "true");
                        else
                            $(element).attr("isEmpty", "false");

                    }
                },
                bizTableName:
                            {
                                id: 'bizTableName',
                                type: 'select',
                                label: "*绑定业务表",
                                'default': '',
                                accessKey: 'M',
                                items: [],
                                setup: function (element) {
                                    var selecttext = $(element.$).attr("relTableName");
                                    if (selecttext === "") {
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
										fieldvalues = $(dialog.getContentElement('info', 'fieldName').getInputElement().$),
                                        selecttext = $($(this.getInputElement().$).find('option:selected')).attr("text"),
                                        _fields = $(editor).formdesign().GetfieldName(selecttext);
                                    fieldvalues.empty();
                                    $(_fields).each(function (i, obj) {
                                        fieldvalues.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
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
                fieldName: {
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

                onclick: {
                    id: 'onclick',
                    type: 'text',
                    label: "点击脚本",
                    'default': '',
                    accessKey: 'M',
                    setup: function (element) {
                        this.setValue($(element.$).attr("onclick") || $(element.$).attr("data-cke-pa-onclick"));
                    },
                    commit: function (data) {
                        var element = data;
                        if (this.getValue())
                            element.data('cke-pa-onclick', this.getValue());
                        else {
                            element.data('cke-pa-onclick', false);
                            element.removeAttribute('onclick');
                        }
                    }
                },
                onchange: {
                    id: 'onchange',
                    type: 'text',
                    label: "值改变脚本",
                    'default': '',
                    accessKey: 'M',
                    setup: function (element) {
                        this.setValue($(element.$).attr("onchange") || $(element.$).attr("data-cke-pa-onchange"));
                    },
                    commit: function (data) {
                        var element = data;
                        if (this.getValue())
                            element.data('cke-pa-onchange', this.getValue());
                        else {
                            element.data('cke-pa-onchange', false);
                            element.removeAttribute('onchange');
                        }
                    }
                },
                onmouseover: {
                    id: 'onmouseover',
                    type: 'text',
                    label: "鼠标停留脚本",
                    'default': '',
                    accessKey: 'M',
                    setup: function (element) {
                        this.setValue($(element.$).attr("onmouseover") || $(element.$).attr("data-cke-pa-onmouseover"));
                    },
                    commit: function (data) {
                        var element = data;
                        if (this.getValue())
                            element.data('cke-pa-onmouseover', this.getValue());
                        else {
                            element.data('cke-pa-onmouseover', false);
                            element.removeAttribute('onmouseover');
                        }
                    }
                },
                onmouseout: {
                    id: 'onmouseout',
                    type: 'text',
                    label: "鼠标移开脚本",
                    'default': '',
                    accessKey: 'M',
                    setup: function (element) {
                        this.setValue($(element.$).attr("onmouseout") || $(element.$).attr("data-cke-pa-onmouseout"));
                    },
                    commit: function (data) {
                        var element = data;
                        if (this.getValue())
                            element.data('cke-pa-onmouseout', this.getValue());
                        else {
                            element.data('cke-pa-onmouseout', false);
                            element.removeAttribute('onmouseout');
                        }
                    }
                }
            }
        };
        //#endregion

    },

    requires: ['image', 'fakeobjects']
});

    if (CKEDITOR.env.ie) {
        CKEDITOR.dom.element.prototype.hasAttribute = CKEDITOR.tools.override(CKEDITOR.dom.element.prototype.hasAttribute,
		function (original) {
		    return function (name) {
		        var $attr = this.$.attributes.getNamedItem(name);

		        if (this.getName() == 'input') {
		            switch (name) {
		                case 'class':
		                    return this.$.className.length > 0;
		                case 'checked':
		                    return !!this.$.checked;
		                case 'value':
		                    var type = this.getAttribute('type');
		                    return type == 'checkbox' || type == 'radio' ? this.$.value != 'on' : this.$.value;
		            }
		        }

		        return original.apply(this, arguments);
		    };
		});
    }

})();