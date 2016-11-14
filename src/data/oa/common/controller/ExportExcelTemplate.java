package data.oa.common.controller;

import java.util.HashMap ;
import java.util.LinkedHashMap ;
import java.util.Map ;

public class ExportExcelTemplate
{
    private static Map<String,Map<String,ExportExcelTemplateBean>> formatTemplate = new HashMap<String,Map<String,ExportExcelTemplateBean>>() ;

    static
    {
        initUserDatas() ;
    }

    public static Map<String,ExportExcelTemplateBean> getTemplate( String key )
    {
        return formatTemplate.get( key ) ;
    }

    private static void initUserDatas()
    {
        Map<String,ExportExcelTemplateBean> formatMap = new LinkedHashMap<String,ExportExcelTemplateBean>() ;

        formatMap = new LinkedHashMap<String,ExportExcelTemplateBean>() ;
        formatMap.put( "姓名", new ExportExcelTemplateBean( "chineseName", "center", 20, false, false ) ) ;
        formatMap.put( "部门", new ExportExcelTemplateBean( "organizationId", "center", 15, false, false ) ) ;
        formatMap.put( "职务", new ExportExcelTemplateBean( "position", "center", 15, false, false ) ) ;
        formatMap.put( "手机号码", new ExportExcelTemplateBean( "mobile", "center", 15, false, false ) ) ;
        formatMap.put( "办公电话", new ExportExcelTemplateBean( "officePhone", "left", 16, false, false ) ) ;
        formatMap.put( "电子邮箱", new ExportExcelTemplateBean( "officeMail", "left", 16, false, false ) ) ;
        formatTemplate.put( "unitContact", formatMap ) ;

    }
}
