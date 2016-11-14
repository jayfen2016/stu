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
11.autoSelect=''
12.showTime=''
13.rightVisible=''
*/
CKEDITOR.dialog.add('formcalendar', function (editor) {


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
        bizTableName: 1
    };

    return {
        title: "表单日历",
        minWidth: 350,
        minHeight: 360,
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
            var editor,
				element = this.textField,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = new CKEDITOR.dom.element("input");
                element.setAttribute('type', 'text');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
                element.setAttribute('id', $(editor).formdesign().Guid());
            }

            if (isInsertMode) {
                editor.insertElement(element);
            }
            this.commitContent(element);
        },
        onLoad: function () {
            var autoSetup = function (element) {
                var value = element.hasAttribute(this.id) && element.getAttribute(this.id);
                this.setValue(value || '');
            };

            var autoCommit = function (data) {
                var element = data;
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
							        var element = data;

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
							    accessKey: 'V'
							}
						]
					},
					{
					    type: 'hbox',
					    widths: ['50%', '50%'],
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
							}
						],
					    onLoad: function () {
					        // Repaint the style for IE7 (#6068)
					        if (CKEDITOR.env.ie7Compat)
					            this.getElement().setStyle('zoom', '100%');
					    }
					},
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                            {
                                id: 'mode',
                                type: 'select',
                                label: "模式",
                                'default': 'input',
                                accessKey: 'S',
                                items:
                                [
                                    ["图片", "img"],
                                    ["按钮", "button"],
                                    ["文本框", "input"]
                                ],
                                value: "checked",
                                setup: function (element) {
                                    if ($("input[id=" + element.$["id"] + "_button]", editor.document.$).length > 0) {
                                        this.setValue("button");
                                    } else if ($("img[id=" + element.$["id"] + "_img]", editor.document.$).length > 0) {
                                        this.setValue("img");
                                    } else {
                                        this.setValue("input");
                                    }
                                },
                                commit: function (data) {
                                    var element = data.$;
                                    var value = this.getValue();
                                    var id = element["id"];
                                    if (value == "img") {
                                        $("input[id=" + id + "_button]", editor.document.$).remove();
                                        if ($("img[id=" + id + "_img]", editor.document.$).length == 0) {
                                            var selection = editor.getSelection();
                                            var ranges = selection.getRanges(true);
                                            if (ranges.length == 1) {
                                                ranges[0].collapse();
                                                var img = new CKEDITOR.dom.element("img", editor.document);
                                                img.setAttribute("dir", "ltr");
                                                img.setAttribute("id", id + "_img");
                                                ranges[0].insertNode(img);
                                                selection.selectElement(data);
                                            }
                                        }

                                        $(element).attr("onchange", "CalendarSetup('" + id + "','" + id + "_img','" + element["showtime"] + "')");
                                    }
                                    else if (value == "button") {
                                        $("img[id=" + id + "_img]", editor.document.$).remove();
                                        if ($("input[id=" + id + "_button]", editor.document.$).length == 0) {
                                            var selection = editor.getSelection();
                                            var ranges = selection.getRanges(true);
                                            if (ranges.length == 1) {
                                                ranges[0].collapse();
                                                var button = new CKEDITOR.dom.element("input", editor.document);
                                                button.setAttribute("dir", "ltr");
                                                button.setAttribute("id", id + "_button");
                                                button.setAttribute("type", "button");
                                                ranges[0].insertNode(button);
                                                selection.selectElement(data);
                                            }
                                        }
                                        $(element).attr("onchange", "CalendarSetup('" + id + "','" + id + "_button','" + element["showtime"] + "')");
                                    } else {
                                        $("img[id=" + id + "_img]", editor.document.$).remove();
                                        $("input[id=" + id + "_button]", editor.document.$).remove();
                                        $(element).attr("onchange", "CalendarSetup('" + id + "','" + id + "','" + element["showtime"] + "')");
                                    }
                                }
                            },
                            CKEDITOR.dialog.beyondbit.entity.isEmpty
                        ]
                    },
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                            {
                                id: 'autoSelect',
                                type: 'checkbox',
                                label: "默认当前日期",
                                'default': '',
                                accessKey: 'S',
                                value: "true",
                                setup: function (element) {
                                    if ($(element.$).attr("autoSelect") == "true")
                                        this.setValue($(element.$).attr("autoSelect"));
                                },
                                commit: function (data) {
                                    var element = data.$;
                                    var value = this.getValue();
                                    if (value)
                                        $(element).attr("autoSelect", "true");
                                    else
                                        $(element).attr("autoSelect", "false");
                                }
                            },
                            {
                                id: 'showTime',
                                type: 'checkbox',
                                label: "显示时间",
                                'default': '',
                                accessKey: 'S',
                                value: "true",
                                setup: function (element) {
                                    if ($(element.$).attr("showTime") == "true")
                                        this.setValue($(element.$).attr("showTime"));
                                },
                                commit: function (data) {
                                    var element = data.$;
                                    var value = this.getValue();
                                    if (value)
                                        $(element).attr("showTime", "true");
                                    else
                                        $(element).attr("showTime", "false");
                                }
                            }
                        ]
                    },
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
                    }
				]
			}
		]
    };
});
