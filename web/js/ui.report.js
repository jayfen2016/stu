function reportSearch( reset )
{
    var currentPage = reset ? 1 : $( "#pageSelector" ).val() ;
    window.location = parseParameter( selfUrl + "?currentPage=" + currentPage ) ;
}
function showDialog( url, classid, value, label )
{
    window.top.frameDialog( url, "请选择",
    {
        mode    : "middle",
        width   : "530",
        height  : "500",
        buttons : [
        {
            text  : "确定",
            icons : { primary:"ui-icon-check" },
            click : function( ev )
            {
                var $this = window.top.$( this ) ;
                var $view = $this.find( "iframe" )[0].contentWindow ;
                $view.getValue( $( "#" + classid ) ) ;
                $this.dialog( "close" ) ;
            }
        },
        {
            text  : "清除",
            icons : { primary:"ui-icon-cancel" },
            click : function( ev )
            {
                var $classic = $( "#" + classid ) ;
                $classic.val( label ) ;
                $classic.data( "value", value ) ;
                $classic.attr( "title", label ) ;
                var $this = window.top.$( this ) ;
                $this.dialog( "close" ) ;
            }
        },
        {
            text  : "关闭",
            icons : { primary:"ui-icon-cancel" },
            click : function( ev )
            {
                var $this = window.top.$( this ) ;
                $this.dialog( "close" ) ;
            }
        }]
    });
}
function parseChooseParameter( url, name )
{
    var value = $( "#" + name ).data( "value" ) ;
    if( value )
    {
        url += "&" + name + "=" + encodeURI( encodeURI( value ) ) ;
        url += "&" + name + "Label=" + encodeURI( encodeURI( $( "#" + name ).val() ) ) ;
    }
    else
    {
        url += "&" + name + "=" ;
        url += "&" + name + "Label=" ;
    }
    return url ;
}
function parseSelectParameter( url, name )
{
    var value = $( "#" + name ).val() ;
    if( value )
    {
        url += "&" + name + "=" + encodeURI( encodeURI( value ) ) ;
    }
    else
    {
        url += "&" + name + "=" ;
    }
    return url ;
}
function exportReportExcel()
{
    var url = "exportExcel.do?command=export&type=" + selfUrl ;
    url = parseParameter( url ) ;

    var form = $( "#exportExcelForm" ) ;
    form.attr( "action", url ) ;
    form.get( 0 ).submit() ;
}