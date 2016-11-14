package data.oa.common.controller;

public class ExportExcelTemplateBean
{
    public String fieldName ;
    public String align ;
    public int width ;
    public boolean number ;
    public boolean bold ;

    public ExportExcelTemplateBean( String fieldName, String align, int width, boolean number, boolean bold )
    {
        this.fieldName = fieldName ;
        this.align = align ;
        this.width = width ;
        this.number = number ;
        this.bold = bold ;
    }
}
