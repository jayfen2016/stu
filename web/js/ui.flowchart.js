/*! Jquery UI flow chart
*
*version 1.0 2014-02-26
*author Qunpeng.Li
*
*/
(function ($) {
    $.widget("ui.flowchart", {
        version: "1.0",
        defaultElement: "<div>",
        options: {
            cls: "ui-flowchart",
            lineElements: {},
            nodeElements: {},
            source: {
                jde:
                    {
                        status: "prepare"
                    },
                manual:
                    {
                        status: "prepare"
                    },
                compute:
                    {
                        status: "prepare"
                    },
                comparison:
                    {
                        status: "prepare"
                    },
                returns:
                    {
                        status: "prepare"
                    },
                notice:
                    {
                        status: "prepare"
                    }
            },change:null,
            itemClick:null
        }, _create: function () {//CREATE
        	var $this=this;
            if (!this.element.hasClass(this.options.cls)) {
                this.element.addClass(this.options.cls);
            }
            var lineElements = this.options.lineElements,
                nodeElements = this.options.nodeElements;
            this.element.find("div.node[data-code]").each(function (index, item) {
                var $item = $(item);
                nodeElements[$item.data("code")] = $item;
            }).bind("click",function(ev){
            	var code=$(this).data("code");
            	$this._trigger("itemClick", ev,{code:code,nodeConfig:config[code],nodeData:$this.options.source[code]});
            });
            this.element.find("td.line[data-code]").each(function (index, item) {
                var $item = $(item), code = $item.data("code");
                if (lineElements[code]) {
                    lineElements[code] = lineElements[code].add($item);
                } else {
                    lineElements[code] = $item;
                }
            });
            this._updateStatus();
        }, loadData: function (source) {
            this.options.source = source;
            this._updateStatus();
        }, _updateStatus: function () {
            var nodes = this.options.source,
                lineElements = this.options.lineElements,
                nodeElements = this.options.nodeElements;;
            for (var name in nodes) {
                var statusName = nodes[name].status,
                    pval=typeof nodes[name].value!=="undefined"?nodes[name].value:0,
                    nodeConfig = config[name],
                    $currNode = nodeElements[name],
                    $currLine = lineElements[name];
                if (!nodeConfig) {
                    continue;
                }
                var nodeTitle = nodeConfig.title,
                    status = nodeConfig.status[statusName];
                var needRemoveCls = [];
                for (var tname in nodeConfig.status) {
                    needRemoveCls.push(tname);
                }
                needRemoveCls = needRemoveCls.join(" ");
                if ($currLine) {
                    $currLine.removeClass(needRemoveCls).addClass(statusName);
                }
                if ($currNode) {
                    $currNode.removeClass(needRemoveCls).addClass(statusName).attr("title", status.title).find("> span").text(nodeTitle);
                    $currNode.find(" > .mas").text(status.text);
                    if (status.hasProgress) {
                        $currNode.find(".p-num").text(pval);
                        $currNode.find(".progress-content").css("width",pval+"%");
                    }
                }
            }
            this._trigger("change", new jQuery.Event(), nodes);
        },_setOptions: function (options) {//OVERVIEW SETOPTIONS
        var that = this,
          reload = false;
        $.each(options, function (key, value) {
            if (key == "cls") {
                that.element.removeClass(that.options.cls).addClass(value);
            }
            that._setOption(key, value);
            if ( key === "source") {
                reload = true;
            }
        });
        if (reload) {
            this._updateStatus();
        }
    }
    });


    var config = {
        jde: {
            title: "JDE自动取数",
            status: {
                "prepare": { text: "未启动", title: "" },
                "progress": { text: "正在取数", title: "" },
                "complete": { text: "已完成", title: "JDE取数完毕！" },
                "abnormal": { text: "有异常", title: "有异常的数据，可以在《JDE自动取数》的标签栏中查看并操作!" }
            }
        },
        manual: {
            title: "税务数据采集",
            status: {
                "prepare": { text: "未启动", title: "" },
                "progress": { text: "进行中", title: "" },
                "complete": { text: "已完成", title: "数据采集完毕！" },
                "abnormal": { text: "有异常", title: "有异常的数据，请在《税务数据采集》的标签栏中查看并进行下一步操作！" }
            }
        },
        compute: {
            title: "系统算税",
            status: {
                "prepare": { text: "未启动", title: "" },
                "ready": { text: "可算税", title: "数据已经准备完毕,请在《系统算税》的标签栏中进行下一步操作！" },
                "progress": { hasProgress: true, text: "算税中", title: "" },
                "complete": { text: "算税完成", title: "" },
                "abnormal": { text: "有异常", title: "" }
            }
        },
        comparison: {
            title: "调整与差异分析比对",
            status: {
                "prepare": { text: "未启动", title: "" },
                "progress": { hasProgress: true, text: "处理中", title: "" },
                "complete": { text: "已完成", title: "" }
            }
        },
        returns: {
            title: "税种申报表",
            status: {
                "prepare": { text: "未启动", title: "" },
                "progress": { text: "生成中", title: "" },
                "complete": { text: "已完成", title: "" }
            }
        },
        notice: {
            title: "缴款通知单",
            status: {
                "prepare": { text: "未启动", title: "" },
                "progress": { text: "生成中", title: "" },
                "complete": { text: "已完成", title: "" }
            }
        }
    };
})(jQuery);