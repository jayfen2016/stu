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
CKEDITOR.dialog.add('formtextfield', function(editor) {
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
        title: "表单文本框",
        minWidth: 350,
        minHeight: 320,
        onShow: function() {
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
        onOk: function() {
            var editor,
				element = this.textField,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = editor.document.createElement('input');
                element.setAttribute('type', 'text');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('canWrite', 'true');
            }

            if (isInsertMode)
                editor.insertElement(element);
            this.commitContent(element);
        },
        onLoad: function() {
            var autoSetup = function(element) {
                var value = element.hasAttribute(this.id) && element.getAttribute(this.id);
                this.setValue(value || '');
            };

            var autoCommit = function(data) {
                var element = data;
                var value = this.getValue();

                if (value)
                    element.setAttribute(this.id, value);
                else
                    element.removeAttribute(this.id);
            };

            this.foreach(function(contentObj) {
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
							    setup: function(element) {
							        this.setValue(
											element.data('cke-saved-name') ||
											element.getAttribute('name') ||
											'');
							    },
							    commit: function(data) {
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
					    onLoad: function() {
					        // Repaint the style for IE7 (#6068)
					        if (CKEDITOR.env.ie7Compat)
					            this.getElement().setStyle('zoom', '100%');
					    }
					},
                    CKEDITOR.dialog.beyondbit.entity.maxtitle,
                    CKEDITOR.dialog.beyondbit.entity.title,
                    CKEDITOR.dialog.beyondbit.entity.isEmpty,
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
