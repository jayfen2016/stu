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

CKEDITOR.dialog.add('formsignlink', function (editor) {

    var onloadTypes =
    {
        fieldName: 1,
        bizTableName: 1
    };

    var plugin = CKEDITOR.plugins.link;
    // Handles the event when the "Target" selection box is changed.
    var targetChanged = function () {
        var dialog = this.getDialog(),
			popupFeatures = dialog.getContentElement('target', 'popupFeatures'),
			targetName = dialog.getContentElement('target', 'linkTargetName'),
			value = this.getValue();

        if (!popupFeatures || !targetName)
            return;

        popupFeatures = popupFeatures.getElement();
        popupFeatures.hide();
        targetName.setValue('');

        switch (value) {
            case 'frame':
                targetName.setLabel(editor.lang.link.targetFrameName);
                targetName.getElement().show();
                break;
            case 'popup':
                popupFeatures.show();
                targetName.setLabel(editor.lang.link.targetPopupName);
                targetName.getElement().show();
                break;
            default:
                targetName.setValue(value);
                targetName.getElement().hide();
                break;
        }

    };

    // Handles the event when the "Type" selection box is changed.
    var linkTypeChanged = function () {
        var dialog = this.getDialog(),
			partIds = ['urlOptions', 'anchorOptions', 'emailOptions'],
			typeValue = this.getValue(),
			uploadTab = dialog.definition.getContents('upload'),
			uploadInitiallyHidden = uploadTab && uploadTab.hidden;

        if (typeValue == 'url') {
            if (editor.config.linkShowTargetTab)
                dialog.showPage('target');
            if (!uploadInitiallyHidden)
                dialog.showPage('upload');
        }
        else {
            dialog.hidePage('target');
            if (!uploadInitiallyHidden)
                dialog.hidePage('upload');
        }

        for (var i = 0; i < partIds.length; i++) {
            var element = dialog.getContentElement('info', partIds[i]);
            if (!element)
                continue;

            element = element.getElement().getParent().getParent();
            if (partIds[i] == typeValue + 'Options')
                element.show();
            else
                element.hide();
        }

        dialog.layout();
    };

    // Loads the parameters in a selected link to the link dialog fields.
    var javascriptProtocolRegex = /^javascript:/,
		emailRegex = /^mailto:([^?]+)(?:\?(.+))?$/,
		emailSubjectRegex = /subject=([^;?:@&=$,\/]*)/,
		emailBodyRegex = /body=([^;?:@&=$,\/]*)/,
		anchorRegex = /^#(.*)$/,
		urlRegex = /^((?:http|https|ftp|news):\/\/)?(.*)$/,
		selectableTargets = /^(_(?:self|top|parent|blank))$/,
		encodedEmailLinkRegex = /^javascript:void\(location\.href='mailto:'\+String\.fromCharCode\(([^)]+)\)(?:\+'(.*)')?\)$/,
		functionCallProtectedEmailLinkRegex = /^javascript:([^(]+)\(([^)]+)\)$/;

    var popupRegex =
		/\s*window.open\(\s*this\.href\s*,\s*(?:'([^']*)'|null)\s*,\s*'([^']*)'\s*\)\s*;\s*return\s*false;*\s*/;
    var popupFeaturesRegex = /(?:^|,)([^=]+)=(\d+|yes|no)/gi;

    var parseLink = function (editor, element) {
        var href = (element && (element.data('cke-saved-href') || element.getAttribute('href'))) || '',
		 	javascriptMatch,
			emailMatch,
			anchorMatch,
			urlMatch,
			retval = {};


        //
        if ((javascriptMatch = href.match(javascriptProtocolRegex))) {
            if (emailProtection == 'encode') {
                href = href.replace(encodedEmailLinkRegex,
						function (match, protectedAddress, rest) {
						    return 'mailto:' +
							       String.fromCharCode.apply(String, protectedAddress.split(',')) +
							       (rest && unescapeSingleQuote(rest));
						});
            }
            // Protected email link as function call.
            else if (emailProtection) {
                href.replace(functionCallProtectedEmailLinkRegex, function (match, funcName, funcArgs) {
                    if (funcName == compiledProtectionFunction.name) {
                        retval.type = 'email';
                        var email = retval.email = {};

                        var paramRegex = /[^,\s]+/g,
							paramQuoteRegex = /(^')|('$)/g,
							paramsMatch = funcArgs.match(paramRegex),
							paramsMatchLength = paramsMatch.length,
							paramName,
							paramVal;

                        for (var i = 0; i < paramsMatchLength; i++) {
                            paramVal = decodeURIComponent(unescapeSingleQuote(paramsMatch[i].replace(paramQuoteRegex, '')));
                            paramName = compiledProtectionFunction.params[i].toLowerCase();
                            email[paramName] = paramVal;
                        }
                        email.address = [email.name, email.domain].join('@');
                    }
                });
            }
        }

        if (!retval.type) {

            // urlRegex matches empty strings, so need to check for href as well.
            if (href && (urlMatch = href.match(urlRegex))) {
                retval.type = 'url';
                retval.url = {};
                retval.url.protocol = urlMatch[1];
                retval.url.url = urlMatch[2];
            }
            else
                retval.type = 'url';
        }

        // Load target and popup settings.
        if (element) {
            var target = element.getAttribute('target');
            retval.target = {};
            retval.adv = {};
            retval.info = {};


            //签名信息
            retval.info.isEmpty = element.getAttribute("isEmpty");
            retval.info.bizTableName = element.getAttribute("bizTableName");
            retval.info.relTableName = element.getAttribute("relTableName");
            retval.info.fieldName = element.getAttribute("fieldName");
            retval.info.editeNode = element.getAttribute("editeNode");
            retval.info.title = element.getAttribute("title");
            retval.info.inVisibleNode = element.getAttribute("inVisibleNode");
            retval.info.txtClass = $("span", element.$).attr("class");
            retval.info.txtValue = $("span", element.$).text();

            if ($("a[id=" + element.getAttribute("id") + "_input]", editor.document.$).length > 0) {
                retval.info.isHandWriting = "true";
            }

            // IE BUG: target attribute is an empty string instead of null in IE if it's not set.
            if (!target) {
                var onclick = element.data('cke-pa-onclick') || element.getAttribute('onclick'),
					onclickMatch = onclick && onclick.match(popupRegex);
                if (onclickMatch) {
                    retval.target.type = 'popup';
                    retval.target.name = onclickMatch[1];

                    var featureMatch;
                    while ((featureMatch = popupFeaturesRegex.exec(onclickMatch[2]))) {
                        // Some values should remain numbers (#7300)
                        if ((featureMatch[2] == 'yes' || featureMatch[2] == '1') && !(featureMatch[1] in { height: 1, width: 1, top: 1, left: 1 }))
                            retval.target[featureMatch[1]] = true;
                        else if (isFinite(featureMatch[2]))
                            retval.target[featureMatch[1]] = featureMatch[2];
                    }
                }
            }
            else {
                var targetMatch = target.match(selectableTargets);
                if (targetMatch)
                    retval.target.type = retval.target.name = target;
                else {
                    retval.target.type = 'frame';
                    retval.target.name = target;
                }
            }

            var me = this;
            var advAttr = function (inputName, attrName) {
                var value = element.getAttribute(attrName);
                if (value !== null)
                    retval.adv[inputName] = value || '';
            };
            advAttr('advId', 'id');
            advAttr('advLangDir', 'dir');
            advAttr('advAccessKey', 'accessKey');

            retval.adv.advName =
				element.data('cke-saved-name')
				|| element.getAttribute('name')
				|| '';
            advAttr('advLangCode', 'lang');
            advAttr('advTabIndex', 'tabindex');
            advAttr('advTitle', 'title');
            advAttr('advContentType', 'type');
            CKEDITOR.plugins.link.synAnchorSelector ?
				retval.adv.advCSSClasses = getLinkClass(element)
				: advAttr('advCSSClasses', 'class');
            advAttr('advCharset', 'charset');
            advAttr('advStyles', 'style');
            advAttr('advRel', 'rel');
        }

        // Find out whether we have any anchors in the editor.
        var anchors = retval.anchors = [],
			i, count, item;

        // For some browsers we set contenteditable="false" on anchors, making document.anchors not to include them, so we must traverse the links manually (#7893).
        if (CKEDITOR.plugins.link.emptyAnchorFix) {
            var links = editor.document.getElementsByTag('a');
            for (i = 0, count = links.count(); i < count; i++) {
                item = links.getItem(i);
                if (item.data('cke-saved-name') || item.hasAttribute('name'))
                    anchors.push({ name: item.data('cke-saved-name') || item.getAttribute('name'), id: item.getAttribute('id') });
            }
        }
        else {
            var anchorList = new CKEDITOR.dom.nodeList(editor.document.$.anchors);
            for (i = 0, count = anchorList.count(); i < count; i++) {
                item = anchorList.getItem(i);
                anchors[i] = { name: item.getAttribute('name'), id: item.getAttribute('id') };
            }
        }

        if (CKEDITOR.plugins.link.fakeAnchor) {
            var imgs = editor.document.getElementsByTag('img');
            for (i = 0, count = imgs.count(); i < count; i++) {
                if ((item = CKEDITOR.plugins.link.tryRestoreFakeAnchor(editor, imgs.getItem(i))))
                    anchors.push({ name: item.getAttribute('name'), id: item.getAttribute('id') });
            }
        }

        // Record down the selected element in the dialog.
        this._.selectedElement = element;
        return retval;
    };

    var setupParams = function (page, data) {
        if (data[page])
            this.setValue(data[page][this.id] || '');
    };

    var setupPopupParams = function (data) {
        return setupParams.call(this, 'target', data);
    };

    var setupAdvParams = function (data) {
        return setupParams.call(this, 'adv', data);
    };

    var commitParams = function (page, data) {
        if (!data[page])
            data[page] = {};

        data[page][this.id] = this.getValue() || '';
    };

    var commitPopupParams = function (data) {
        return commitParams.call(this, 'target', data);
    };

    var commitAdvParams = function (data) {
        return commitParams.call(this, 'adv', data);
    };

    function unescapeSingleQuote(str) {
        return str.replace(/\\'/g, '\'');
    }

    function escapeSingleQuote(str) {
        return str.replace(/'/g, '\\$&');
    }

    var emailProtection = editor.config.emailProtection || '';

    // Compile the protection function pattern.
    if (emailProtection && emailProtection != 'encode') {
        var compiledProtectionFunction = {};

        emailProtection.replace(/^([^(]+)\(([^)]+)\)$/, function (match, funcName, params) {
            compiledProtectionFunction.name = funcName;
            compiledProtectionFunction.params = [];
            params.replace(/[^,\s]+/g, function (param) {
                compiledProtectionFunction.params.push(param);
            });
        });
    }

    function protectEmailLinkAsFunction(email) {
        var retval,
			name = compiledProtectionFunction.name,
			params = compiledProtectionFunction.params,
			paramName,
			paramValue;

        retval = [name, '('];
        for (var i = 0; i < params.length; i++) {
            paramName = params[i].toLowerCase();
            paramValue = email[paramName];

            i > 0 && retval.push(',');
            retval.push('\'',
						 paramValue ?
						 escapeSingleQuote(encodeURIComponent(email[paramName]))
						 : '',
						 '\'');
        }
        retval.push(')');
        return retval.join('');
    }

    function protectEmailAddressAsEncodedString(address) {
        var charCode,
			length = address.length,
			encodedChars = [];
        for (var i = 0; i < length; i++) {
            charCode = address.charCodeAt(i);
            encodedChars.push(charCode);
        }
        return 'String.fromCharCode(' + encodedChars.join(',') + ')';
    }

    function getLinkClass(ele) {
        var className = ele.getAttribute('class');
        return className ? className.replace(/\s*(?:cke_anchor_empty|cke_anchor)(?:\s*$)?/g, '') : '';
    }

    var commonLang = editor.lang.common,
		linkLang = editor.lang.link;

    return {
        title: "表单签名",
        minWidth: 350,
        minHeight: 270,
        contents: [
			{
			    id: 'info',
			    label: "签名",
			    title: "表单签名",
			    elements:
				[
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                         {
                             id: 'txtValue',
                             type: 'text',
                             label: "*说明文字",
                             'default': '签名',
                             accessKey: 'S',
                             validate: CKEDITOR.dialog.validate.notEmpty("说明文字不能为空！"),
                             setup: function (data) {
                                 if (data["info"]) {
                                     if (data["info"][this.id] == "") {
                                         this.setValue("签名");
                                     } else {
                                         this.setValue(data["info"][this.id]);
                                     }
                                 }
                             },
                             commit: function (data) {
                                 var value = this.getValue();
                                 data.txtValue = value;

                             }
                         },
                         {
                             id: 'txtClass',
                             type: 'text',
                             label: "文字样式",
                             'default': '',
                             accessKey: 'S',
                             setup: function (data) {
                                 if (data["info"]) {
                                     this.setValue(data["info"][this.id]);
                                 }
                             },
                             commit: function (data) {
                                 var value = this.getValue();
                                 data.txtClass = value;
                             }
                         }
                        ]
                    },
                    {
                        type: 'hbox',
                        widths: ['50%', '50%'],
                        children:
						[
                          {
                              id: 'isEmpty',
                              type: 'checkbox',
                              label: "允许为空(不为空时，将提示用户)",
                              'default': '',
                              accessKey: 'S',
                              value: "checked",
                              setup: function (data) {
                                  if (data["info"]) {
                                      if (data["info"][this.id] == "true")
                                          this.setValue(data["info"][this.id]);
                                  }
                              },
                              commit: function (data) {
                                  var value = this.getValue();
                                  if (value)
                                      data.isEmpty = "true";
                                  else
                                      data.isEmpty = "false";

                              }
                          },
                          {
                              id: 'isHandWriting',
                              type: 'checkbox',
                              label: "手写",
                              'default': '',
                              accessKey: 'S',
                              value: "checked",
                              setup: function (data) {
                                  if (data["info"]) {
                                      if (data["info"][this.id] == "true")
                                          this.setValue(data["info"][this.id]);
                                  }
                              },
                              commit: function (data) {
                                  var value = this.getValue();
                                  if (value)
                                      data.isHandWriting = "true";
                                  else
                                      data.isHandWriting = "false";

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
                         setup: function (data) {
                             if (data["info"]) {
                                 this.setValue(data["info"][this.id]);
                             }
                         },
                         commit: function (data) {
                             var value = this.getValue();
                             data.title = value;
                         }
                     },
                    {
                        id: 'editeNode',
                        type: 'text',
                        label: "*编辑节点(格式：[流程节点1,流程节点X])",
                        'default': '',
                        accessKey: 'M',
                        validate: CKEDITOR.dialog.validate.nodeList("格式错误"),
                        setup: function (data) {
                            if (data["info"]) {
                                this.setValue(data["info"][this.id]);
                            }
                        },
                        commit: function (data) {

                            var value = this.getValue();
                            data.editeNode = value;
                        }
                    },
                    {
                        id: 'inVisibleNode',
                        type: 'text',
                        label: "不可见节点(格式：[流程节点1,流程节点X])",
                        'default': '',
                        accessKey: 'M',
                        validate: CKEDITOR.dialog.validate.nodeListOrEmpty("格式错误"),
                        setup: function (data) {
                            if (data["info"]) {
                                this.setValue(data["info"][this.id]);
                            }
                        },
                        commit: function (data) {
                            var value = this.getValue();
                            data.inVisibleNode = value;
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
                                setup: function (data) {
                                    if (data["info"]) {
                                        var selecttext = data["info"]["relTableName"];
                                        if (selecttext == "") {
                                            selecttext = data["info"][this.id];
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
                                        selecttext = $($(this.getInputElement().$).find('option:selected')).attr("text"),
                                        _fields = $(editor).formdesign().GetfieldName(selecttext);
                                    fieldvalues.empty();
                                    $(_fields).each(function (i, obj) {
                                        fieldvalues.append("<option value='" + obj.code + "'>" + obj.name + "</option>");
                                    });
                                },
                                commit: function (data) {

                                    var value = this.getValue();
                                    if (value == "") {
                                        //无附表
                                        data.bizTableName = $(this.getInputElement().$).find('option:selected').attr("text");
                                        data.relTableName = value;

                                    } else {
                                        data.relTableName = $(this.getInputElement().$).find('option:selected').attr("text");
                                        data.bizTableName = value;
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
                                setup: function (data) {
                                    if (data["info"]) {
                                        this.setValue(data["info"][this.id]);
                                    }
                                },
                                commit: function (data) {

                                    var value = this.getValue();
                                    data.fieldName = value;
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
			},
			{
			    id: 'advanced',
			    label: linkLang.advanced,
			    title: linkLang.advanced,
			    elements:
				[
					{
					    type: 'vbox',
					    padding: 1,
					    children:
						[
							{
							    type: 'hbox',
							    widths: ['45%', '35%', '20%'],
							    children:
								[
									{
									    type: 'text',
									    id: 'advId',
									    label: linkLang.id,
									    setup: setupAdvParams,
									    commit: commitAdvParams
									},
									{
									    type: 'select',
									    id: 'advLangDir',
									    label: linkLang.langDir,
									    'default': '',
									    style: 'width:110px',
									    items:
										[
											[commonLang.notSet, ''],
											[linkLang.langDirLTR, 'ltr'],
											[linkLang.langDirRTL, 'rtl']
										],
									    setup: setupAdvParams,
									    commit: commitAdvParams
									},
									{
									    type: 'text',
									    id: 'advAccessKey',
									    width: '80px',
									    label: linkLang.acccessKey,
									    maxLength: 1,
									    setup: setupAdvParams,
									    commit: commitAdvParams

									}
								]
							},
							{
							    type: 'hbox',
							    widths: ['45%', '35%', '20%'],
							    children:
								[
									{
									    type: 'text',
									    label: linkLang.name,
									    id: 'advName',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									},
									{
									    type: 'text',
									    label: linkLang.langCode,
									    id: 'advLangCode',
									    width: '110px',
									    'default': '',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									},
									{
									    type: 'text',
									    label: linkLang.tabIndex,
									    id: 'advTabIndex',
									    width: '80px',
									    maxLength: 5,
									    setup: setupAdvParams,
									    commit: commitAdvParams

									}
								]
							}
						]
					},
					{
					    type: 'vbox',
					    padding: 1,
					    children:
						[
							{
							    type: 'hbox',
							    widths: ['45%', '55%'],
							    children:
								[
									{
									    type: 'text',
									    label: linkLang.advisoryTitle,
									    'default': '',
									    id: 'advTitle',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									},
									{
									    type: 'text',
									    label: linkLang.advisoryContentType,
									    'default': '',
									    id: 'advContentType',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									}
								]
							},
							{
							    type: 'hbox',
							    widths: ['45%', '55%'],
							    children:
								[
									{
									    type: 'text',
									    label: linkLang.cssClasses,
									    'default': '',
									    id: 'advCSSClasses',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									},
									{
									    type: 'text',
									    label: linkLang.charset,
									    'default': '',
									    id: 'advCharset',
									    setup: setupAdvParams,
									    commit: commitAdvParams

									}
								]
							},
							{
							    type: 'hbox',
							    widths: ['45%', '55%'],
							    children:
								[
									{
									    type: 'text',
									    label: linkLang.rel,
									    'default': '',
									    id: 'advRel',
									    setup: setupAdvParams,
									    commit: commitAdvParams
									},
									{
									    type: 'text',
									    label: linkLang.styles,
									    'default': '',
									    id: 'advStyles',
									    validate: CKEDITOR.dialog.validate.inlineStyle(editor.lang.common.invalidInlineStyle),
									    setup: setupAdvParams,
									    commit: commitAdvParams
									}
								]
							}
						]
					}
				]
			}
		],
        onShow: function () {
            var editor = this.getParentEditor(),
				selection = editor.getSelection(),
				element = null;

            this.foreach(function (contentObj) {
                if (onloadTypes[contentObj.id]) {
                    contentObj.onload();
                }
            });
            // Fill in all the relevant fields if there's already one link selected.
            if ((element = plugin.getSelectedLink(editor)) && element.hasAttribute('href'))
                selection.selectElement(element);
            else
                element = null;



            this.setupContent(parseLink.apply(this, [editor, element]));
        },
        onOk: function () {
            var attributes = {},
				removeAttributes = [],
				data = {},
				me = this,
				editor = this.getParentEditor();

            this.commitContent(data);

            // Compose the URL.
            //attributes['data-cke-saved-href'] = "javascript:signName('"+ attributes["id"] +"','%currentUserName%','%currentUserCode%');";

            attributes["formFlag"] = "true";
            attributes["canWrite"] = "true";
            attributes["isEmpty"] = data.isEmpty;
            attributes["bizTableName"] = data.bizTableName;
            attributes["relTableName"] = data.relTableName;
            attributes["fieldName"] = data.fieldName;
            attributes["editeNode"] = data.editeNode;
            attributes["title"] = data.title;
            attributes["inVisibleNode"] = data.inVisibleNode;


            // Advanced attributes.
            if (data.adv) {
                var advAttr = function (inputName, attrName) {
                    var value = data.adv[inputName];
                    if (value)
                        attributes[attrName] = value;
                    else
                        removeAttributes.push(attrName);
                };

                advAttr('advId', 'id');
                advAttr('advLangDir', 'dir');
                advAttr('advAccessKey', 'accessKey');

                if (data.adv['advName'])
                    attributes['name'] = attributes['data-cke-saved-name'] = data.adv['advName'];
                else
                    removeAttributes = removeAttributes.concat(['data-cke-saved-name', 'name']);

                advAttr('advLangCode', 'lang');
                advAttr('advTabIndex', 'tabindex');
                advAttr('advTitle', 'title');
                advAttr('advContentType', 'type');
                advAttr('advCSSClasses', 'class');
                advAttr('advCharset', 'charset');
                advAttr('advStyles', 'style');
                advAttr('advRel', 'rel');
            }


            var selection = editor.getSelection();

            // Browser need the "href" fro copy/paste link to work. (#6641)
            attributes.href = attributes['data-cke-saved-href'];

            if (!this._.selectedElement) {
                attributes['id'] = $(editor).formdesign().Guid();
                attributes.href = "javascript:signName('" + attributes["id"] + "','%currentUserName%','%currentUserCode%');";
                // Create element if current selection is collapsed.
                var ranges = selection.getRanges(true);
                if (ranges.length == 1 && ranges[0].collapsed) {

                    var span = new CKEDITOR.dom.element("span", editor.document);
                    span.appendText(data.txtValue);
                    span.addClass(data.txtClass);
                    ranges[0].insertNode(span);

                    var img = new CKEDITOR.dom.element("img", editor.document);
                    img.setAttribute("id", attributes["id"] + "_img");
                    img.setStyle("display", "none");
                    ranges[0].insertNode(img);
                    selection.selectRanges(ranges);
                    //手写
                    if (data.isHandWriting == "true") {
                        //ranges[0].collapse();
                        var a = new CKEDITOR.dom.element("a");
                        a.setAttribute("href", "#");
                        a.setAttribute("id", attributes["id"] + "_input");
                        a.setAttribute("class", "thickbox");
                        a.setAttribute("onclick", "ShowInput('" + attributes["id"] + "','"+attributes["id"] +"','手写控件路径');");
                        a.appendText("手写");
                        ranges[0].insertNode(a);
                    }
                }

                // Apply style.
                var style = new CKEDITOR.style({ element: 'a', attributes: attributes });
                style.type = CKEDITOR.STYLE_INLINE; 	// need to override... dunno why.
                style.apply(editor.document);
            }
            else {
                // We're only editing an existing link, so just overwrite the attributes.
                var element = this._.selectedElement,
					href = element.data('cke-saved-href'),
					textView = element.getHtml();

                var span = $("span", element.$)
                span.text(data.txtValue);
                span.attr("class", data.txtClass);

                element.setAttributes(attributes);
                element.removeAttributes(removeAttributes);


                if (data.isHandWriting == "true") {
                    var ranges = selection.getRanges(true);
                    if (ranges.length == 1) {
                        ranges[0].collapse();
                        var a = new CKEDITOR.dom.element("a");
                        a.setAttribute("href", "#");
                        a.setAttribute("id", attributes["id"] + "_input");
                        a.setAttribute("class", "thickbox");
                        a.setAttribute("onclick", "ShowInput('" + attributes["id"] + "','" + attributes["id"] + "','手写控件路径');");
                        a.appendText("手写");
                        ranges[0].insertNode(a);
                    }
                } else {
                    $("a[id=" + attributes["id"] + "_input]", editor.document.$).remove();
                }

                selection.selectElement(element);
                delete this._.selectedElement;
            }
        },
        onLoad: function () {
            if (!editor.config.linkShowAdvancedTab)
                this.hidePage('advanced'); 	//Hide Advanded tab.

        },
        // Inital focus on 'url' field if link is of type URL.
        onFocus: function () {
            var linkType = this.getContentElement('info', 'linkType'),
					urlField;
            if (linkType && linkType.getValue() == 'url') {
                urlField = this.getContentElement('info', 'url');
                urlField.select();
            }
        }
    };
});

/**
* The e-mail address anti-spam protection option. The protection will be
* applied when creating or modifying e-mail links through the editor interface.<br>
* Two methods of protection can be choosed:
* <ol>	<li>The e-mail parts (name, domain and any other query string) are
*			assembled into a function call pattern. Such function must be
*			provided by the developer in the pages that will use the contents.
*		<li>Only the e-mail address is obfuscated into a special string that
*			has no meaning for humans or spam bots, but which is properly
*			rendered and accepted by the browser.</li></ol>
* Both approaches require JavaScript to be enabled.
* @name CKEDITOR.config.emailProtection
* @since 3.1
* @type String
* @default '' (empty string = disabled)
* @example
* // href="mailto:tester@ckeditor.com?subject=subject&body=body"
* config.emailProtection = '';
* @example
* // href="<a href=\"javascript:void(location.href=\'mailto:\'+String.fromCharCode(116,101,115,116,101,114,64,99,107,101,100,105,116,111,114,46,99,111,109)+\'?subject=subject&body=body\')\">e-mail</a>"
* config.emailProtection = 'encode';
* @example
* // href="javascript:mt('tester','ckeditor.com','subject','body')"
* config.emailProtection = 'mt(NAME,DOMAIN,SUBJECT,BODY)';
*/


