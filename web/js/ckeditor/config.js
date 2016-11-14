/*
Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function (config) {
    // Define changes to default configuration here. For example:
    config.language = "zh-cn"; //配置语言
    // config.uiColor = '#AADC6E';
    config.toolbar = 'Full';
    //config.toolbarStartupExpanded = false;
    //config.startupMode = 'source';
  //  config.extraPlugins = 'myplugin,beyondbit'; //新建插件
    config.toolbar_Full =

  [

       ['Source', '-', 'Preview', '-', 'Templates'],

       ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Print', 'SpellChecker', 'Scayt'],

       ['Undo', 'Redo', '-', 'Find', 'Replace', '-', 'SelectAll', 'RemoveFormat'],

       ['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'],

       ['Image', 'Flash', 'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak'],

       '/',

       ['Bold', 'Italic', 'Underline', 'Strike', '-', 'Subscript', 'Superscript'],

       ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', 'Blockquote'],

       ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
       ['Link', 'Unlink', 'Anchor'],
        ['FormTextBox', 'FormTextarea', 'FormContentTextarea', 'FormButton', 'FormRadio', 'FormCheckBox', 'FormSelect', 'FormHiddenField'],
        ['FormCalendar', 'FormSignLink', 'FormSelectTextBox', 'FormSelectButtonText', 'FormUploadify', 'FormShowTable', 'FormEditGrid'],

       '/',

       ['Styles', 'Format', 'Font', 'FontSize'],

       ['TextColor', 'BGColor'],

       ['Maximize', 'ShowBlocks', '-', 'About','MyButton']

  ];





    config.menu_groups = 'clipboard,form,tablecell,tablecellproperties,tablerow,tablecolumn,table,anchor,link,image,flash,checkbox,radio,textfield,hiddenfield,imagebutton,button,select,textarea,beyondbit';
};
