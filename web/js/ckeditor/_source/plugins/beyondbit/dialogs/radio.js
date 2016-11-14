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
10.groupname=''
*/
CKEDITOR.dialog.add('formradio', function (editor) {
    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1
    };
    return {
        title: editor.lang.checkboxAndRadio.radioTitle,
        minWidth: 350,
        minHeight: 370,
        onShow: function () {
            delete this.radioButton;
            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });
            var element = this.getParentEditor().getSelection().getSelectedElement();
            if (element && element.getName() == 'input' && element.getAttribute('type') == 'radio') {
                this.radioButton = element;
                this.setupContent(element);
            }
        },
        onOk: function () {
            var editor,
				element = this.radioButton,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = editor.document.createElement('input');
                element.setAttribute('type', 'radio');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
            }

            if (isInsertMode)
                editor.insertElement(element);
            this.commitContent(element);
        },
        contents: [
			{
			    id: 'info',
			    label: editor.lang.checkboxAndRadio.radioTitle,
			    title: editor.lang.checkboxAndRadio.radioTitle,
			    elements: [
					{
					    id: 'name',
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
					    label: "*"+editor.lang.checkboxAndRadio.value,
					    'default': '',
					    accessKey: 'V',
					    validate: CKEDITOR.dialog.validate.notEmpty("选定值不能为空！"),
					    setup: function (element) {
					        this.setValue(element.getAttribute('value') || '');
					    },
					    commit: function (data) {
					        var element = data;

					        if (this.getValue())
					            element.setAttribute('value', this.getValue());
					        else
					            element.removeAttribute('value');
					    }
					},
                     {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                                {
					            id: 'checked',
					            type: 'checkbox',
					            label: editor.lang.checkboxAndRadio.selected,
					            'default': '',
					            accessKey: 'S',
					            value: "checked",
					            setup: function (element) {
					                this.setValue(element.getAttribute('checked'));
					            },
					            commit: function (data) {
					                var element = data;

					                if (!(CKEDITOR.env.ie || CKEDITOR.env.opera)) {
					                    if (this.getValue())
					                        element.setAttribute('checked', 'checked');
					                    else
					                        element.removeAttribute('checked');
					                }
					                else {
					                    var isElementChecked = element.getAttribute('checked');
					                    var isChecked = !!this.getValue();

					                    if (isElementChecked != isChecked) {
					                        var replace = CKEDITOR.dom.element.createFromHtml('<input type="radio"'
											        + (isChecked ? ' checked="checked"' : '')
											        + '></input>', editor.document);
					                        element.copyAttributes(replace, { type: 1, checked: 1 });
					                        replace.replace(element);
					                        editor.getSelection().selectElement(replace);
					                        data = replace;
					                    }
					                }
					            }
					        }
                        ]
					    },
                              CKEDITOR.dialog.beyondbit.entity.title,                    
                     CKEDITOR.dialog.beyondbit.entity.isEmpty,
                     CKEDITOR.dialog.beyondbit.entity.editeNode,
                     CKEDITOR.dialog.beyondbit.entity.inVisibleNode,
                    CKEDITOR.dialog.beyondbit.entity.bizTableName,
                    CKEDITOR.dialog.beyondbit.entity.fieldName
				]
			}
		]
    };
});
