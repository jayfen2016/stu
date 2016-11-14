package data.framework.utility;

import org.apache.poi.ss.usermodel.Workbook ;

public interface ExcelImportCallbcak
{
    public void callback( Workbook workBook ) throws Exception ;
}
