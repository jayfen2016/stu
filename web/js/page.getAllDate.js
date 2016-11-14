/**
 * Created by zhangyuanyuan on 2014/12/10.
 */
var getMyTypeFormData=function($form){
    var formElements= $form.find("input[data-dtype]:not([type=button]):not([type=radio]),select[data-dtype],textarea[data-dtype],input[data-dtype][type=radio]:checked,input[data-dtype][type=checkbox]"),
        objArr=[];
    for(var i=0;i<formElements.length;i++){
        var item=formElements[i], $item=$(item),
            data=$item.data(),dtype=data.dtype,xtype = data.xtype,
            label=$item.attr("name") || $item.attr("id"), val = null,obj={};
        if (label) {
            if (xtype) {
                switch (data.xtype.toLowerCase()) {
                    case "ajaxchosen":
                    case "chosen":
                        val = $item.data("chosen").selectedItem();
                        break;
                    case "upload":
                        val = data.fileItems();
                        break;
                    case "pick":
                        val= $item.pick("getValue");
                        break;
                    default:
                        val = $item.val();
                        break;
                }
            } else {
                if(item.type==="checkbox"){
                    val=item.checked?true:false;//如果要是checkbox的话，选中就是true，否则就是flase
                }else{
                    val = $item.val();
                }
            }
            obj={
                name:label,
                value:val,
                type:dtype
            }
            objArr.push(obj);
        }
    }
    return objArr;
};



