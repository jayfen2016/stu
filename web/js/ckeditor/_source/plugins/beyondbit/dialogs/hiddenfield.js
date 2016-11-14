/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
1.formFlag='true' 
2.canWrite='true'
4.bindFlowID=''
5.bindFlow=''
6.bizTableName=''
7.relTableName=''
8.fieldName=''
9.relationControl=''
*/
CKEDITOR.dialog.add( 'formhiddenfield', function( editor ) {
    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1
    };
	return {
		title : editor.lang.hidden.title,
		hiddenField : null,
		minWidth : 350,
		minHeight : 300,
		onShow : function()
		{
			delete this.hiddenField;

			var editor = this.getParentEditor(),
				selection = editor.getSelection(),
				element = selection.getSelectedElement();
			this.foreach(function (contentObj) {
			    if (onloadTypes[contentObj.id]) {
			        contentObj.onload();
			    }
			});
			if ( element && element.data( 'cke-real-element-type' ) && element.data( 'cke-real-element-type' ) == 'hiddenfield' )
			{
				this.hiddenField = element;
				element = editor.restoreRealElement( this.hiddenField );
				this.setupContent( element );
				selection.selectElement( this.hiddenField );
			}
		},
		onOk : function()
		{
			var name = this.getValueOf( 'info', '_cke_saved_name' ),
				value = this.getValueOf( 'info', 'value' ),
				editor = this.getParentEditor(),
				element = CKEDITOR.env.ie && !( CKEDITOR.document.$.documentMode >= 8 ) ?
					editor.document.createElement( '<input name="' + CKEDITOR.tools.htmlEncode( name ) + '">' )
					: editor.document.createElement( 'input' );

			element.setAttribute('type', 'hidden');
			element.setAttribute('formFlag', 'true');
			element.setAttribute('canWrite', 'true');

			this.commitContent( element );
			var fakeElement = editor.createFakeElement( element, 'cke_hidden', 'hiddenfield' );
			if ( !this.hiddenField )
				editor.insertElement( fakeElement );
			else
			{
				fakeElement.replace( this.hiddenField );
				editor.getSelection().selectElement( fakeElement );
			}
			return true;

},
 
		contents : [
			{
				id : 'info',
				label : editor.lang.hidden.title,
				title : editor.lang.hidden.title,
				elements : [
					{
						id : '_cke_saved_name',
						type : 'text',
						label : editor.lang.hidden.name,
						'default' : '',
						accessKey : 'N',
						setup : function( element )
						{
							this.setValue(
									element.data( 'cke-saved-name' ) ||
									element.getAttribute( 'name' ) ||
									'' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'name', this.getValue() );
							else
							{
								element.removeAttribute( 'name' );
							}
						}
					},
					{
						id : 'value',
						type : 'text',
						label : editor.lang.hidden.value,
						'default' : '',
						accessKey : 'V',
						setup : function( element )
						{
							this.setValue( element.getAttribute( 'value' ) || '' );
						},
						commit : function( element )
						{
							if ( this.getValue() )
								element.setAttribute( 'value', this.getValue() );
							else
								element.removeAttribute( 'value' );
						}

                    },
                    CKEDITOR.dialog.beyondbit.entity.isEmpty,
                    CKEDITOR.dialog.beyondbit.entity.editeNode,
                    CKEDITOR.dialog.beyondbit.entity.inVisibleNode,
                    {
                        id: 'relationControl',
                        type: 'text',
                        label: "关联控件的ID",
                        'default': '',
                        accessKey: 'M',
                        setup: function (element) {
                            this.setValue($(element.$).attr("relationControl"));
                        },
                        commit: function (data) {
                            var element = data.$;
                            var value = this.getValue();
                            $(element).attr("relationControl", value);
                        }
                    },
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
