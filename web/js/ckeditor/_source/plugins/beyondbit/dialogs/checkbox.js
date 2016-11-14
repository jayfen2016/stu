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
*/
CKEDITOR.dialog.add('formcheckbox', function (editor) {
    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1
    };

    return {
        title: "表单" + editor.lang.checkboxAndRadio.checkboxTitle,
        minWidth: 350,
        minHeight: 370,
        onShow: function () {
            delete this.checkbox;

            var element = this.getParentEditor().getSelection().getSelectedElement();
            
            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });

            if (element && element.getAttribute('type') == 'checkbox') {
                this.checkbox = element;
                this.setupContent(element);
            }
        },
        onOk: function () {
            var editor,
				element = this.checkbox,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = editor.document.createElement('input');
                element.setAttribute('type', 'checkbox');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
                editor.insertElement(element);                
            }
            this.commitContent(element);
        },
        contents: [
			{
			    id: 'info',
			    label: editor.lang.checkboxAndRadio.checkboxTitle,
			    title: editor.lang.checkboxAndRadio.checkboxTitle,
			    startupFocus: 'txtName',
			    elements: [
					{
					    id: 'txtName',
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

					        // IE failed to update 'name' property on input elements, protect it now.
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
					    label: "*"+editor.lang.checkboxAndRadio.value,
					    'default': '',
					    accessKey: 'V',
					    validate: CKEDITOR.dialog.validate.notEmpty("选定值不能为空！"),
					    setup: function (element) {
					        var value = element.getAttribute('value');
					        // IE Return 'on' as default attr value.
					        this.setValue(CKEDITOR.env.ie && value == 'on' ? '' : value);
					    },
					    commit: function (data) {
					        var element = data,
								value = this.getValue();

					        if (value && !(CKEDITOR.env.ie && value == 'on'))
					            element.setAttribute('value', value);
					        else {
					            if (CKEDITOR.env.ie) {
					                // Remove attribute 'value' of checkbox (#4721).
					                element.setAttribute('value', "");
					            }
					            else
					                element.removeAttribute('value');
					        }
					    }
					},
                    {
					    type: 'hbox',
					    widths: ['50%', '50%'],
					    children:
						[
					        {
					            id: 'cmbSelected',
					            type: 'checkbox',
					            label: editor.lang.checkboxAndRadio.selected,
					            'default': '',
					            accessKey: 'S',
					            value: "checked",
					            setup: function (element) {
					                if (element.getAttribute('checked') == 'checked') {
					                    this.setValue(element.getAttribute('checked'));
					                }
					            },
					            commit: function (data) {
					                var element = data;

					                if (CKEDITOR.env.ie) {
					                    var isElementChecked = !!element.getAttribute('checked'),
									        isChecked = !!this.getValue();

					                    if (isElementChecked != isChecked) {
					                        var replace = CKEDITOR.dom.element.createFromHtml('<input type="checkbox"'
										           + (isChecked ? ' checked="checked"' : '')
										           + '/>', editor.document);

					                        element.copyAttributes(replace, { type: 1, checked: 1 });
					                        replace.replace(element);
					                        editor.getSelection().selectElement(replace);
					                        data = replace;
					                    }
					                }
					                else {
					                    var value = this.getValue();
					                    if (value)
					                        element.setAttribute('checked', 'checked');
					                    else
					                        element.removeAttribute('checked');
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