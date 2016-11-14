/*
 * Copyright (c) 2003-2012, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
1.fileFlag='true' 
2.showQueueClass=''
3.isEmpty='true'
4.typeCode=''
5.folder=''
6.multi=''
7.buttoAlign=''
8.listAlign=''
9.bindFlowID=''
10.bindFlow=''
11.editeNode=''
12.inVisibleNode=''
13.trackSaveNode=''
14.showMode=''
15.whereSql=''
 */

(function () {

    /**
    * Add to collection with DUP examination.
    * @param {Object} collection
    * @param {Object} element
    * @param {Object} database
    */
    function addSafely(collection, element, database) {
        // 1. IE doesn't support customData on text nodes;
        // 2. Text nodes never get chance to appear twice;
        if (!element.is || !element.getCustomData('block_processed')) {
            element.is && CKEDITOR.dom.element.setMarker(database, element, 'block_processed', true);
            collection.push(element);
        }
    }

    function getNonEmptyChildren(element) {
        var retval = [];
        var children = element.getChildren();
        for (var i = 0; i < children.count(); i++) {
            var child = children.getItem(i);
            if (!(child.type === CKEDITOR.NODE_TEXT
				&& (/^[ \t\n\r]+$/).test(child.getText())))
                retval.push(child);
        }
        return retval;
    }


    /**
    * Dialog reused by both 'creatediv' and 'formuploadifyProperties' commands.
    * @param {Object} editor
    * @param {String} command	The command name which indicate what the current command is.
    */
    function divDialog(editor, command) {
        // Definition of elements at which div operation should stopped.
        var divLimitDefinition = (function () {

            // Customzie from specialize blockLimit elements
            var definition = CKEDITOR.tools.extend({}, CKEDITOR.dtd.$blockLimit);

            // Exclude 'div' itself.
            delete definition.div;

            // Exclude 'td' and 'th' when 'wrapping table'
            if (editor.config.div_wrapTable) {
                delete definition.td;
                delete definition.th;
            }
            return definition;
        })();

        // DTD of 'div' element
        var dtd = CKEDITOR.dtd.div;

        /**
        * Get the first div limit element on the element's path.
        * @param {Object} element
        */
        function getDivLimitElement(element) {
            var pathElements = new CKEDITOR.dom.elementPath(element).elements;
            var divLimit;
            for (var i = 0; i < pathElements.length; i++) {
                if (pathElements[i].getName() in divLimitDefinition) {
                    divLimit = pathElements[i];
                    break;
                }
            }
            return divLimit;
        }

        /**
        * Init all fields' setup/commit function.
        * @memberof divDialog
        */
        function setupFields() {
            this.foreach(function (field) {
                // Exclude layout container elements
                if (/^(?!vbox|hbox)/.test(field.type)) {
                    if (!field.setup) {
                        // Read the dialog fields values from the specified
                        // element attributes.
                        field.setup = function (element) {
                            field.setValue(element.getAttribute(field.id) || '');
                        };
                    }
                    if (!field.commit) {
                        // Set element attributes assigned by the dialog
                        // fields.
                        field.commit = function (element) {
                            var fieldValue = this.getValue();
                            // ignore default element attribute values
                            if ('dir' == field.id && element.getComputedStyle('direction') == fieldValue)
                                return;

                            if (fieldValue)
                                element.setAttribute(field.id, fieldValue);
                            else
                                element.removeAttribute(field.id);
                        };
                    }
                }
            });
        }

        /**
        * Wrapping 'div' element around appropriate blocks among the selected ranges.
        * @param {Object} editor
        */
        function createDiv(editor) {
            var element = editor.document.createElement('div');
            editor.insertElement(element);
            return [element];
        }

        function getDiv(editor) {
            var path = new CKEDITOR.dom.elementPath(editor.getSelection().getStartElement()),
				blockLimit = path.blockLimit,
				div = blockLimit && blockLimit.getAscendant('div', true);
            return div;
        }
        /**
        * Divide a set of nodes to different groups by their path's blocklimit element.
        * Note: the specified nodes should be in source order naturally, which mean they are supposed to producea by following class:
        *  * CKEDITOR.dom.range.Iterator
        *  * CKEDITOR.dom.domWalker
        *  @return {Array []} the grouped nodes
        */
        function groupByDivLimit(nodes) {
            var groups = [],
				lastDivLimit = null,
				path, block;
            for (var i = 0; i < nodes.length; i++) {
                block = nodes[i];
                var limit = getDivLimitElement(block);
                if (!limit.equals(lastDivLimit)) {
                    lastDivLimit = limit;
                    groups.push([]);
                }
                groups[groups.length - 1].push(block);
            }
            return groups;
        }

        // Synchronous field values to other impacted fields is required, e.g. div styles
        // change should also alter inline-style text.
        function commitInternally(targetFields) {
            var dialog = this.getDialog(),
				 element = dialog._element && dialog._element.clone()
						 || new CKEDITOR.dom.element('div', editor.document);

            // Commit this field and broadcast to target fields.
            this.commit(element, true);

            targetFields = [].concat(targetFields);
            var length = targetFields.length, field;
            for (var i = 0; i < length; i++) {
                field = dialog.getContentElement.apply(dialog, targetFields[i].split(':'));
                field && field.setup && field.setup(element, true);
            }
        }


        // Registered 'CKEDITOR.style' instances.
        var styles = {};
        /**
        * Hold a collection of created block container elements.
        */
        var containers = [];
        /**
        * @type divDialog
        */
        return {
            title: "表单附件",
            minWidth: 400,
            minHeight: 480,
            contents:
			[
			{
			    id: 'info',
			    label: editor.lang.common.generalTab,
			    title: editor.lang.common.generalTab,
			    elements:
				[
					{
					    type: 'hbox',
					    widths: ['50%', '50%'],
					    children:
						[
							{
							    id: 'elementStyle',
							    type: 'select',
							    style: 'width: 100%;',
							    label: editor.lang.div.styleSelectLabel,
							    'default': '',
							    // Options are loaded dynamically.
							    items:
								[
									[editor.lang.common.notSet, '']
								],
							    onChange: function () {
							        commitInternally.call(this, ['info:class', 'advanced:dir', 'advanced:style']);
							    },
							    setup: function (element) {
							        for (var name in styles)
							            styles[name].checkElementRemovable(element, true) && this.setValue(name);
							    },
							    commit: function (element) {
							        var styleName;
							        if ((styleName = this.getValue())) {
							            var style = styles[styleName];
							            var customData = element.getCustomData('elementStyle') || '';

							            style.applyToObject(element);
							            element.setCustomData('elementStyle', customData + style._.definition.attributes.style);
							        }
							    }
							},
							{
							    id: 'class',
							    type: 'text',
							    label: editor.lang.common.cssClass,
							    'default': ''
							}
						]
					}, {
					    type: 'hbox',
					    widths: ['50%', '50%'],
					    children:
						[
							CKEDITOR.dialog.beyondbit.entity.isEmpty,
                     {
                         id: 'showMode',
                         type: 'select',
                         label: "查看附件的方式",
                         'default': 'onload',
                         accessKey: 'M',
                         items: [
                            ["加载浏览", "onload"],
                            ["下载浏览", "view"]
                          ],
                         setup: function (element) {
                             this.setValue($(element.$).attr("showMode"));
                         },
                         commit: function (data) {
                             var element = data.$;
                             var value = this.getValue();
                             $(element).attr("showMode", value);
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
                          setup: function (element) {
                              this.setValue(
									element.data('cke-saved-title') ||
									element.getAttribute('title') ||
									'');
                          },
                          commit: function (data) {
                              var element = data;

                              // IE failed to update 'name' property on input elements, protect it now.
                              if (this.getValue())
                                  element.data('cke-saved-title', this.getValue());
                              else {
                                  element.data('cke-saved-title', false);
                                  element.removeAttribute('title');
                              }
                          }
                      },
                    CKEDITOR.dialog.beyondbit.entity.editeNode,
                    CKEDITOR.dialog.beyondbit.entity.inVisibleNode,
                    {
                        id: 'showQueueClass',
                        type: 'text',
                        label: "显示附件Div的样式",
                        'default': '',
                        accessKey: 'M',
                        setup: function (element) {
                            this.setValue($(element.$).attr("showQueueClass"));
                        },
                        commit: function (data) {
                            $(data.$).attr("showQueueClass", this.getValue());
                        }
                    },
                     {
                         id: 'folder',
                         type: 'text',
                         label: "上传文件目录",
                         'default': '',
                         accessKey: 'M',
                         setup: function (element) {
                             this.setValue($(element.$).attr("folder"));
                         },
                         commit: function (data) {
                             $(data.$).attr("folder", this.getValue());
                         }
                     },
                     {
                         id: 'typecode',
                         type: 'text',
                         label: "*附件类型标识(格式:正整数)",
                         'default': '',
                         accessKey: 'M',
                         validate: CKEDITOR.dialog.validate.integerNotEmpty("标识必需为正整数！"),
                         setup: function (element) {
                             this.setValue($(element.$).attr("typecode"));
                         },
                         commit: function (data) {
                             $(data.$).attr("typecode", this.getValue());
                         }
                     },
                     {
                         type: 'hbox',
                         widths: ['50%', '50%'],
                         children:
						[{
						    id: 'multi',
						    type: 'checkbox',
						    label: "允许多附件上传",
						    'default': '',
						    accessKey: 'M',
						    value: "checked",
						    setup: function (element) {
						        if ($(element.$).attr("multi") == "true")
						            this.setValue("true");
						    },
						    commit: function (data) {
						        var element = data;
						        var value = this.getValue();
						        if (value)
						            element.setAttribute('multi', 'true');
						        else
						            element.setAttribute('multi', 'false');
						    }
						},
                         {
                             id: 'tracksave',
                             type: 'checkbox',
                             label: "痕迹保留",
                             'default': '',
                             accessKey: 'M',
                             value: "true",
                             setup: function (element) {
                                 if ($(element.$).attr("tracksave") == "true")
                                     this.setValue("true");
                             },
                             commit: function (data) {
                                 var element = data;
                                 var value = this.getValue();
                                 if (value)
                                     element.setAttribute('tracksave', 'true');
                                 else
                                     element.setAttribute('tracksave', 'false');
                             }
                         } ,
                         {
                             id: 'opinionflag',
                             type: 'checkbox',
                             label: "意见关联附件",
                             'default': '',
                             accessKey: 'M',
                             value: "false",
                             setup: function (element) {
                                 if ($(element.$).attr("opinionflag") == "true")
                                     this.setValue("true");
                             },
                             commit: function (data) {
                                 var element = data;
                                 var value = this.getValue();
                                 if (value)
                                     element.setAttribute('opinionflag', 'true');
                                 else
                                     element.setAttribute('opinionflag', 'false');
                             }
                         }
                        ]
                     },
                     {

                         type: 'hbox',
                         widths: ['50%', '50%'],
                         children: [
                            {
                                id: 'listAlign',
                                type: 'select',
                                label: "上传附件信息方位",
                                'default': 'top',
                                accessKey: 'M',
                                items: [
                            ["上方", "top"],
                            ["下方", "bottom"]
                          ],
                                setup: function (element) {
                                    this.setValue($(element.$).attr("listAlign"));
                                },
                                commit: function (data) {
                                    var element = data.$;
                                    var value = this.getValue();
                                    $(element).attr("listAlign", value);
                                }
                            }, {
                                id: 'buttoAlign',
                                type: 'select',
                                label: "选择按钮相对与上传文件所在位子",
                                'default': 'bottom',
                                accessKey: 'M',
                                items: [
                            ["上方", "top"],
                            ["下方", "bottom"]
                          ],
                                setup: function (element) {
                                    this.setValue($(element.$).attr("buttoAlign"));
                                },
                                commit: function (data) {
                                    var element = data.$;
                                    var value = this.getValue();
                                    $(element).attr("buttoAlign", value);
                                }
                            }
                         ]
                     },
                     {
                         id: 'whereSql',
                         type: 'text',
                         label: "SqlWhere查询条件",
                         'default': '',
                         accessKey: 'M',
                         setup: function (element) {
                             this.setValue($(element.$).attr("whereSql"));
                         },
                         commit: function (data) {
                             $(data.$).attr("whereSql", this.getValue());
                         }
                     }
				]
			},
			{
			    id: 'advanced',
			    label: editor.lang.common.advancedTab,
			    title: editor.lang.common.advancedTab,
			    elements:
					[
					{
					    type: 'vbox',
					    padding: 1,
					    children:
						[
							{
							    type: 'hbox',
							    widths: ['50%', '50%'],
							    children:
								[
									{
									    type: 'text',
									    id: 'id',
									    label: editor.lang.common.id,
									    'default': ''
									},
									{
									    type: 'text',
									    id: 'lang',
									    label: editor.lang.link.langCode,
									    'default': ''
									}
								]
							},
							{
							    type: 'hbox',
							    children:
								[
										{
										    type: 'text',
										    id: 'style',
										    style: 'width: 100%;',
										    label: editor.lang.common.cssStyle,
										    'default': '',
										    commit: function (element) {
										        // Merge with 'elementStyle', which is of higher priority.
										        var merged = this.getValue() + (element.getCustomData('elementStyle') || '');
										        element.setAttribute('style', merged);
										    }
										}
								]
							},
							{
							    type: 'hbox',
							    children:
								[
										{
										    type: 'text',
										    id: 'title',
										    style: 'width: 100%;',
										    label: editor.lang.common.advisoryTitle,
										    'default': ''
										}
								]
							},
							{
							    type: 'select',
							    id: 'dir',
							    style: 'width: 100%;',
							    label: editor.lang.common.langDir,
							    'default': '',
							    items:
								[
									[editor.lang.common.notSet, ''],
									[
										editor.lang.common.langDirLtr,
										'ltr'
									],
									[
										editor.lang.common.langDirRtl,
										'rtl'
									]
								]
							}
						]
					}
					]
			}
			],
            onLoad: function () {
                setupFields.call(this);

                // Preparing for the 'elementStyle' field.
                var dialog = this,
					 stylesField = this.getContentElement('info', 'elementStyle');

                // Reuse the 'stylescombo' plugin's styles definition.
                editor.getStylesSet(function (stylesDefinitions) {
                    var styleName;

                    if (stylesDefinitions) {
                        // Digg only those styles that apply to 'div'.
                        for (var i = 0; i < stylesDefinitions.length; i++) {
                            var styleDefinition = stylesDefinitions[i];
                            if (styleDefinition.element && styleDefinition.element == 'div') {
                                styleName = styleDefinition.name;
                                styles[styleName] = new CKEDITOR.style(styleDefinition);

                                // Populate the styles field options with style name.
                                stylesField.items.push([styleName, styleName]);
                                stylesField.add(styleName, styleName);
                            }
                        }
                    }

                    // We should disable the content element
                    // it if no options are available at all.
                    stylesField[stylesField.items.length > 1 ? 'enable' : 'disable']();

                    // Now setup the field value manually.
                    setTimeout(function () { stylesField.setup(dialog._element); }, 0);
                });
            },
            onShow: function () {
                // Whether always create new container regardless of existed
                // ones.
                if (command == 'formuploadifyProperties') {
                    // Try to discover the containers that already existed in
                    // ranges
                    var div = getDiv(editor);
                    div = div.getParent();
                    // update dialog field values
                    div && this.setupContent(this._element = div);
                }
            },
            onOk: function () {
                if (command == 'formuploadifyProperties') {
                    containers = [this._element];
                }
                else {
                    containers = createDiv(editor, true);

                }
                var div = containers[0];

                div.setAttribute("fileFlag", "true");

                this.commitContent(div);
                if (command == 'formuploadifyProperties') 
                {
//                    if (div.getAttribute("id").indexOf("_parentFileDiv") < 0) {
//                        div.setAttribute('id', $(editor).formdesign().Guid() + "_parentFileDiv");
//                    }

                    $("div[id=" + div.getAttribute("id").replace("_parentFileDiv", "") + "_fileDiv]", div.$).remove();

                } else {
                    div.setAttribute('id', $(editor).formdesign().Guid() + "_parentFileDiv");
                }
                var id = div.getAttribute("id").replace("_parentFileDiv", "");
                var input = new CKEDITOR.dom.element('input', editor.document);
                var filediv = new CKEDITOR.dom.element('div', editor.document);
                filediv.setAttribute("id", id + "_fileDiv");
                div.append(filediv);


                // <div fileFlag='true' id='控件ID_parentFileDiv' class='' showQueueClass='' typeCode='' isEmpty='true' folder='' multi='true' buttoAlign='bottom' listAlign='bottom' bindFlowID='' bindFlow='' editeNode='' inVisibleNode='' trackSaveNode='' showMode='view' whereSql=''> 
                //  <script type="text/javascript">附件的JS</script>
                //  <div id='控件ID_fileDiv' >
                //  <input id='控件ID' type='file' name='Filedata' />
                //  <div id="custom-queue"> </div>
                //  </div>  
                //</div>
                //<input type="hidden" name="控件ID_hidden" fileFlag="true" id="控件ID_hidden" />
                if (div.getAttribute("buttoAlign") == "top") {

                    input.setAttribute("id", id);
                    input.setAttribute("type", "file");
                    input.data('cke-saved-name', "Filedata");
                    filediv.append(input);

                    var custom = new CKEDITOR.dom.element('div', editor.document);
                    custom.setAttribute("id", id + "_custom-queue");
                    filediv.append(custom);
                } else {
                    //底部
                    // <div fileFlag='true' id='控件ID_parentFileDiv' class='' showQueueClass='' typeCode='' isEmpty='true' folder='' multi='true' buttoAlign='bottom' listAlign='bottom' bindFlowID='' bindFlow='' editeNode='' inVisibleNode='' trackSaveNode='' showMode='view' whereSql=''> 
                    //  <script type="text/javascript">附件的JS</script>
                    //  <div id='控件ID_fileDiv' >
                    //  <div id="custom-queue"> </div>
                    //  <table><tr style="height:2px;"><td></td></tr></table>
                    //  <input id='控件ID' type='file' name='Filedata' />
                    //  </div>  
                    //</div>
                    //<input type="hidden" name="控件ID_hidden" fileFlag="true" id="控件ID_hidden" />                    

                    var custom = new CKEDITOR.dom.element('div', editor.document);
                    custom.setAttribute("id", id + "_custom-queue");
                    filediv.append(custom);

                    filediv.appendHtml("<table><tr style=\"height:2px;\"><td></td></tr></table>");

                    input.setAttribute("id", id);
                    input.setAttribute("type", "file");
                    input.data('cke-saved-name', "Filedata");
                    filediv.append(input);

                }

                if ($("input[id=" + id + "_hidden]", editor.document.$).length > 0 ||
                   $("img[data-cke-realelement*=" + id + "_hidden]", editor.document.$).length > 0) {
                    //已有
                } else {
                    var hidden = new CKEDITOR.dom.element('input', editor.document);
                    hidden.setAttribute("id", id + "_hidden");
                    hidden.setAttribute("type", "hidden");
                    hidden.setAttribute("fileFlag", "true");
                    hidden.data("cke-saved-name", id + "_hidden");
                    var fakeElement = editor.createFakeElement(hidden, 'cke_hidden', 'hiddenfield');
                    fakeElement.insertAfter(div);
                }
                editor.getSelection().selectElement(input);
                this.hide();
            },
            onHide: function () {
                // Remove style only when editing existing DIV. (#6315)
                if (command == 'formuploadifyProperties')
                    this._element.removeCustomData('elementStyle');
                delete this._element;
            }
        };
    }

    CKEDITOR.dialog.add('formuploadify', function (editor) {
        return divDialog(editor, 'formuploadify');
    });
    CKEDITOR.dialog.add('formuploadifyProperties', function (editor) {
        return divDialog(editor, 'formuploadifyProperties');
    });
})();

/*
 * @name CKEDITOR.config.div_wrapTable
 * Whether to wrap the whole table instead of indivisual cells when created 'div' in table cell.
 * @type Boolean
 * @default false
 * @example config.div_wrapTable = true;
 */
