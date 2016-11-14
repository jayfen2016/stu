/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
1.formFlag='true' 
2.bindFlowID=''
3.bindFlow=''
4.editeNode=''
5.inVisibleNode=''
*/
CKEDITOR.dialog.add('formbutton', function (editor) {
    function commitAttributes(element) {
        var value = this.getValue();
        if (value)
            element.setAttribute(this.id, value);
        else
            element.removeAttribute(this.id);
    }

    return {
        title: editor.lang.button.title,
        minWidth: 350,
        minHeight: 310,
        onShow: function () {
            delete this.button;
            var element = this.getParentEditor().getSelection().getSelectedElement();
            if (element && element.is('input')) {
                var type = element.getAttribute('type');
                if (type in { button: 1, reset: 1, submit: 1 }) {
                    this.button = element;
                    this.setupContent(element);
                }
            }
        },
        onOk: function () {
            var editor = this.getParentEditor(),
				element = this.button,
				isInsertMode = !element;

            if (isInsertMode) {
                editor = this.getParentEditor();
                element = editor.document.createElement('input');
                element.setAttribute('formFlag', 'true');
                element.setAttribute('buttonFlag', 'true');
                element.setAttribute('type', 'button');
            }
            this.commitContent(element);

            if (isInsertMode)
                editor.insertElement(element);
        },
        contents: [
			{
			    id: 'info',
			    label: editor.lang.button.title,
			    title: editor.lang.button.title,
			    elements: [
					{
					    id: 'value',
					    type: 'text',
					    label: editor.lang.button.text,
					    accessKey: 'V',
					    'default': '',
					    setup: function (element) {
					        this.setValue(element.getAttribute('value') || '');
					    },
					    commit: commitAttributes
					},
                    CKEDITOR.dialog.beyondbit.entity.title,
                    CKEDITOR.dialog.beyondbit.entity.onclick,
                    CKEDITOR.dialog.beyondbit.entity.onmouseover,
                    CKEDITOR.dialog.beyondbit.entity.onmouseout,
                    CKEDITOR.dialog.beyondbit.entity.editeNode,
                    CKEDITOR.dialog.beyondbit.entity.inVisibleNode
				]
			}
		]
    };
});
