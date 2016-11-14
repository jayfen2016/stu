package data.framework.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取excel 方法
 * 
 * @author tang
 * 
 */
public class ExcelReadUtils {
	/**
	 * 读取2003excel
	 * 
	 * @param file
	 * @param filedType 字段类型，指定某个字段对应的类型 格式如同：String,date,date,date,date,String,String,String
	 * @return
	 */
	public static List<List<Object>> read2003Excel(File file,
			InputStream inputStream,String filedType) throws IOException {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		HSSFWorkbook wb = null;
		if (file != null) {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} else {
			wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		Object val = null;
		DecimalFormat df = new DecimalFormat("0");// 格式化数字
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		String [] filedTypeArray=filedType.split(",");
		//SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");// 格式化日期字符串
		//SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");// 格式化日期字符串
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> objList = new ArrayList<Object>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell((short) j);
				if (cell == null) {
					val = null;
					objList.add(val);
					continue;
				}
				switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					val = cell.getStringCellValue();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormat())) {
						val = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormat())) {
						val = df.format(cell.getNumericCellValue());
					} else {
						 if (HSSFDateUtil.isCellDateFormatted(cell)||filedTypeArray[j].equals("date")) {
							 val = sdf.format(HSSFDateUtil.getJavaDate(cell
										.getNumericCellValue()));
						 }else if(filedTypeArray[j].equals("String")){//把小数截取掉
							 String temp=String.valueOf(cell.getNumericCellValue());
							 val = temp.substring(0,temp.lastIndexOf("."));
						 }else{
							 val = String.valueOf(cell.getNumericCellValue());
						 }
						
					}
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					val = cell.getBooleanCellValue();
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					val = "";
					break;
				default:
					val = cell.toString();
					break;
				}
				objList.add(val);
			}
			dataList.add(objList);
		}
		return dataList;
	}

	/**
	 * 读取excel表头
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String[] readExcelHead(File file, InputStream inputStream)
			throws IOException {
		HSSFWorkbook wb = null;
		if (file != null) {
			wb = new HSSFWorkbook(new FileInputStream(file));
		} else {
			wb = new HSSFWorkbook(new POIFSFileSystem(inputStream));
		}
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		row = sheet.getRow(0);
		String[] buff = new String[row.getLastCellNum()];
		for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
			cell = row.getCell((short) i);
			buff[i] = cell.getStringCellValue();
		}
		return buff;
	}

	/**
	 * 读取2007excel
	 * 
	 * @param file
	 * @param inputStream
	 *            两个参数任选其一
	 * @return
	 */

	public static List<List<Object>> read2007Excel(File file,
			InputStream inputStream) throws IOException {
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		XSSFWorkbook xwb = null;
		if (file != null) {
			xwb = new XSSFWorkbook(new FileInputStream(file));
		} else {
			xwb = new XSSFWorkbook(inputStream);
		}

		XSSFSheet sheet = xwb.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		Object val = null;
		DecimalFormat df = new DecimalFormat("0");// 格式化数字
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		for (int i = sheet.getFirstRowNum(); i < sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> objList = new ArrayList<Object>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					val = null;
					objList.add(val);
					continue;
				}
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:// XSSFCell.CELL_TYPE_STRING
					val = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:// XSSFCell.CELL_TYPE_NUMERIC
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						val = df.format(cell.getNumericCellValue());
					} else if ("General".equals(cell.getCellStyle()
							.getDataFormatString())) {
						val = df.format(cell.getNumericCellValue());
					} else {
						val = sdf.format(HSSFDateUtil.getJavaDate(cell
								.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:// XSSFCell.CELL_TYPE_BOOLEAN
					val = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:// XSSFCell.CELL_TYPE_BLANK
					val = "";
					break;
				default:
					val = cell.toString();
					break;
				}
				objList.add(val);
			}
			dataList.add(objList);
		}
		return dataList;
	}

	// 对外提供读取excel文件的接口
	public static List<List<Object>> readExcel(File file) throws IOException {
		String fName = file.getName();
		String extension = fName.lastIndexOf(".") == -1 ? "" : fName
				.substring(fName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {// 2003
			System.err.println("读取excel2003文件内容");
			return read2003Excel(file,null,"String,date,date,date,date,String,String,String");
		} else if ("xlsx".equals(extension)) {// 2007
			System.err.println("读取excel2007文件内容");
			return read2007Excel(file,null);
		} else {
			throw new IOException("不支持的文件类型:" + extension);
		}
	}
	
	public static void main(String[] args) {
		try {
			List<List<Object>>  list =readExcel(new File("C:\\Users\\tang\\Desktop\\personalTemplate1.xlsx"));
			for (int i=1;i<list.size();i++) {//除了第一行表头
				List<Object> list2=list.get(i);
				for (Object object : list2) {
					System.out.println(object);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
