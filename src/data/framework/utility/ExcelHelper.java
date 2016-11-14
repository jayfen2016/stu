package data.framework.utility;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import data.framework.support.AbstractEntity;
import data.oa.common.controller.ExportExcelTemplateBean;

/**
 * <p>
 * Excel 导出工具类
 * </p>
 * 
 * @author 不想再加班的码农
 */
public final class ExcelHelper {
	private final static String BACKGROUND_COLOR = "#92d050";

	public static void exportExcelByEntityList(
			List<? extends AbstractEntity> list,
			Map<String, ExportExcelTemplateBean> headerTemplate, String name,
			HttpServletResponse response) {
		OutputStream os = null;
		WritableWorkbook wbook = null;
		try {
			String fileName = new String((name + ".xls").getBytes(),
					"iso-8859-1");
			os = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename="
					+ fileName);
			response.setContentType("application/msexcel");

			wbook = Workbook.createWorkbook(os);
			Color color = Color.decode(BACKGROUND_COLOR);
			wbook.setColourRGB(Colour.BLUE_GREY, color.getRed(),
					color.getGreen(), color.getBlue());

			WritableCellFormat titleFormatCenter = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			titleFormatCenter.setBackground(Colour.BLUE_GREY);
			titleFormatCenter.setAlignment(Alignment.CENTRE);
			titleFormatCenter.setVerticalAlignment(VerticalAlignment.CENTRE);
			titleFormatCenter.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			titleFormatCenter.setWrap(true);

			WritableCellFormat cellFormatTextLeft = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextLeft.setBackground(Colour.WHITE);
			cellFormatTextLeft.setAlignment(Alignment.LEFT);
			cellFormatTextLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextLeft.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextLeft.setWrap(true);
			WritableCellFormat cellFormatTextCenter = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextCenter.setBackground(Colour.WHITE);
			cellFormatTextCenter.setAlignment(Alignment.CENTRE);
			cellFormatTextCenter.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextCenter.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextCenter.setWrap(true);
			WritableCellFormat cellFormatTextRight = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextRight.setBackground(Colour.WHITE);
			cellFormatTextRight.setAlignment(Alignment.RIGHT);
			cellFormatTextRight.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextRight.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextRight.setWrap(true);
			WritableCellFormat cellFormatTextLeftB = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextLeftB.setBackground(Colour.WHITE);
			cellFormatTextLeftB.setAlignment(Alignment.LEFT);
			cellFormatTextLeftB.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextLeftB.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextLeftB.setWrap(true);
			WritableCellFormat cellFormatTextCenterB = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextCenterB.setBackground(Colour.WHITE);
			cellFormatTextCenterB.setAlignment(Alignment.CENTRE);
			cellFormatTextCenterB
					.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextCenterB.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextCenterB.setWrap(true);
			WritableCellFormat cellFormatTextRightB = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
			cellFormatTextRightB.setBackground(Colour.WHITE);
			cellFormatTextRightB.setAlignment(Alignment.RIGHT);
			cellFormatTextRightB.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellFormatTextRightB.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			cellFormatTextRightB.setWrap(true);

			WritableCellFormat cellNumberFormat2 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.00"));
			cellNumberFormat2.setBackground(Colour.WHITE);
			cellNumberFormat2.setAlignment(Alignment.RIGHT);
			cellNumberFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat2.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			WritableCellFormat cellNumberFormat4 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.0000"));
			cellNumberFormat4.setBackground(Colour.WHITE);
			cellNumberFormat4.setAlignment(Alignment.RIGHT);
			cellNumberFormat4.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat4.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			WritableCellFormat cellNumberFormat6 = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE,
							WritableFont.NO_BOLD, false,
							UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.000000"));
			cellNumberFormat6.setBackground(Colour.WHITE);
			cellNumberFormat6.setAlignment(Alignment.RIGHT);
			cellNumberFormat6.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat6.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			WritableCellFormat cellNumberFormat2B = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.00"));
			cellNumberFormat2B.setBackground(Colour.WHITE);
			cellNumberFormat2B.setAlignment(Alignment.RIGHT);
			cellNumberFormat2B.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat2B.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			WritableCellFormat cellNumberFormat4B = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.0000"));
			cellNumberFormat4B.setBackground(Colour.WHITE);
			cellNumberFormat4B.setAlignment(Alignment.RIGHT);
			cellNumberFormat4B.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat4B.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			WritableCellFormat cellNumberFormat6B = new WritableCellFormat(
					new WritableFont(WritableFont.ARIAL,
							WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD,
							false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK),
					new NumberFormat("#,##0.000000"));
			cellNumberFormat6B.setBackground(Colour.WHITE);
			cellNumberFormat6B.setAlignment(Alignment.RIGHT);
			cellNumberFormat6B.setVerticalAlignment(VerticalAlignment.CENTRE);
			cellNumberFormat6B.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);

			int totalCount = list.size();
			if (totalCount != 0) {
				WritableSheet sheet = wbook.createSheet(name, 0);
				sheet.setRowView(0, 600, false);

				int rowIndex = 0;
				for (int i = 0; i < list.size(); ++i) {
					if (rowIndex == 0) {
						int titleIndex = 0;
						for (String key : headerTemplate.keySet()) {
							ExportExcelTemplateBean formatBean = headerTemplate
									.get(key);
							sheet.addCell(new Label(titleIndex, 0, key,
									titleFormatCenter));
							sheet.setColumnView(titleIndex, formatBean.width);
							titleIndex++;
						}
					}

					// 写入数据行
					Object item = list.get(i);
					int cellIndex = 0;
					for (String key : headerTemplate.keySet()) {
						ExportExcelTemplateBean formatBean = headerTemplate
								.get(key);
						if (formatBean.number) {
							sheet.addCell(new jxl.write.Number(cellIndex,
									rowIndex + 1, parseDoubleValue(item,
											formatBean.fieldName),
									formatBean.bold ? cellNumberFormat2B
											: cellNumberFormat2));
						} else {
							if (formatBean.fieldName.equals("status")) {
								if (!formatBean.bold) {
									String value = parseStringValue(item,
											formatBean.fieldName);
									sheet.addCell(new Label(
											cellIndex,
											rowIndex + 1,
											value == null || value.equals("0") ? "禁用"
													: "启用",
											formatBean.bold ? cellFormatTextCenterB
													: cellFormatTextCenter));
								} else {
									String value = parseStringValue(item,
											formatBean.fieldName);
									sheet.addCell(new Label(cellIndex,
											rowIndex + 1, value == null
													|| value.equals("0") ? "异常"
													: "正常",
											cellFormatTextCenter));
								}
							} else {
								if (formatBean.align.equals("center")) {
									sheet.addCell(new Label(
											cellIndex,
											rowIndex + 1,
											parseStringValue(item,
													formatBean.fieldName),
											formatBean.bold ? cellFormatTextCenterB
													: cellFormatTextCenter));
								} else if (formatBean.align.equals("left")) {
									sheet.addCell(new Label(
											cellIndex,
											rowIndex + 1,
											parseStringValue(item,
													formatBean.fieldName),
											formatBean.bold ? cellFormatTextLeftB
													: cellFormatTextLeft));
								} else {
									sheet.addCell(new Label(
											cellIndex,
											rowIndex + 1,
											parseStringValue(item,
													formatBean.fieldName),
											formatBean.bold ? cellFormatTextRightB
													: cellFormatTextRight));
								}
							}
						}
						cellIndex++;
					}
					rowIndex++;
				}
			} else {
				WritableSheet sheet = wbook.createSheet(name, 0);
				int titleIndex = 0;
				for (String key : headerTemplate.keySet()) {
					ExportExcelTemplateBean formatBean = headerTemplate
							.get(key);
					sheet.addCell(new Label(titleIndex, 0, key,
							titleFormatCenter));
					sheet.setColumnView(titleIndex, formatBean.width);
					titleIndex++;
				}
			}

			// excel 内容写入流
			wbook.write();
		} catch (Exception ex) {
			throw new RuntimeException("导出 Excel 文件异常：" + ex.getMessage(), ex);
		} finally {
			if (wbook != null) {
				try {
					wbook.close();
				} catch (Exception ex) {
				}
			}
			wbook = null;

			if (os != null) {
				try {
					os.close();
				} catch (Exception ex) {
				}
			}
			os = null;
		}
	}

	private static String parseStringValue(Object bean, String propertyName) {
		String result = formateBeanValue(bean, propertyName);
		if (result == null) {
			return "";
		} else {
			return result;
		}
	}

	private static String formateBeanValue(Object value, String name) {
		if (value == null || name == null || name.length() == 0)
			return "";

		List<String> nameList = Arrays.asList(name.split("\\."));
		Object tempObject = value;
		for (String itemName : nameList) {
			BeanWrapper itemBean = new BeanWrapperImpl(tempObject);
			tempObject = itemBean.getPropertyValue(itemName);
			if (tempObject == null)
				break;
		}

		return formatBeanValue(tempObject);
	}

	private static String formatBeanValue(Object value) {
		if (value == null) {
			return "";
		}

		String selfName = value.getClass().getSimpleName();
		if (selfName.equals("String")) {
			return FormatConvertor.removeLineSeparator(value);
		} else if (selfName.equals("Date") || selfName.equals("Timestamp")) {
			return new SimpleDateFormat("yyyy年MM月dd日").format((Date) value);
		} else if (selfName.equals("Boolean")) {
			return (Boolean) value ? "是" : "否";
		} else {
			return value.toString();
		}
	}

	private static double parseDoubleValue(Object bean, String propertyName) {
		String result = formateBeanValue(bean, propertyName);
		if (result == null || result.length() == 0) {
			return 0;
		} else {
			return Double.parseDouble(result);
		}
	}

	private ExcelHelper() {
	}

	/**
	 * 将excel转化成List<List<Object>>
	 * 
	 * @param filePath
	 *            导入的文件路径
	 * @return
	 */
	public static List<List<Object>> excelToMapList(String filePath) {
		try {
			File file = new File(filePath);
			FileInputStream readFile = new FileInputStream(file);
			String fileType = filePath.substring(filePath.lastIndexOf("."));
			
			org.apache.poi.ss.usermodel.Workbook xssfWorkbook = null;
			
			if (".xls".equals(fileType)) {
				xssfWorkbook = new HSSFWorkbook(readFile);
			} else if (".xlsx".equals(fileType)) {
				xssfWorkbook = new XSSFWorkbook(readFile);
			}
			Sheet xssfSheet = xssfWorkbook.getSheetAt(0);
			if (xssfSheet == null) {
				return null;
			}
			int rowLineCount = xssfSheet.getLastRowNum();
			Row xssfRow = xssfSheet.getRow(0);
			int cellLineCount = xssfRow.getLastCellNum();
			List<List<Object>> list = new ArrayList<List<Object>>();
			if (xssfWorkbook.getNumberOfSheets() > 0) {
				if (null != xssfWorkbook.getSheetAt(0)) {
					Sheet aSheet = xssfWorkbook.getSheetAt(0); // 获得一个sheet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= rowLineCount; rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							List<Object> valueList = new ArrayList<Object>();
							Row aRow = aSheet.getRow(rowNumOfSheet); // 获得一行
							for (short cellNumOfRow = 0; cellNumOfRow <= cellLineCount; cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									org.apache.poi.ss.usermodel.Cell aCell = aRow.getCell(cellNumOfRow); // 获得列值
									valueList.add(parseExcel(aCell));
								}else{
									valueList.add("");
								}
							}
							list.add(valueList);
						}
					}
				}
			}
			return list;
		
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static String parseExcel(org.apache.poi.ss.usermodel.Cell cell) {
		String result = new String();
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
			if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
				SimpleDateFormat sdf = null;
				if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
						.getBuiltinFormat("h:mm")) {
					sdf = new SimpleDateFormat("HH:mm");
				} else {// 日期
					sdf = new SimpleDateFormat("yyyy-MM-dd");
				}
				Date date = cell.getDateCellValue();
				result = sdf.format(date);
				
				/**万能处理方案：
				所有日期格式都可以通过getDataFormat()值来判断
				yyyy-MM-dd-----	14
				yyyy年m月d日---	31
				yyyy年m月-------	57
				m月d日  ----------	58
				HH:mm-----------	20
				h时mm分  -------	32*/
			
			//1、判断是否是数值格式
//			if(cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
//				short format = cell.getCellStyle().getDataFormat();
//				SimpleDateFormat sdf = null;
//				if(format == 14 || format == 31 || format == 57 || format == 58){
//					//日期
//					sdf = new SimpleDateFormat("yyyy-MM-dd");
//				}else if (format == 20 || format == 32) {
//					//时间
//					sdf = new SimpleDateFormat("HH:mm");
//				}
//				double value = cell.getNumericCellValue();
//				Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
//				result = sdf.format(date);
//			}
				
			} else if (cell.getCellStyle().getDataFormat() == 58) {
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				double value = cell.getNumericCellValue();
				Date date = org.apache.poi.ss.usermodel.DateUtil
						.getJavaDate(value);
				result = sdf.format(date);
			} else {
				double value = cell.getNumericCellValue();
				CellStyle style = cell.getCellStyle();
				DecimalFormat format = new DecimalFormat();
				String temp = style.getDataFormatString();
				// 单元格设置成常规
				if (temp.equals("General")) {
					format.applyPattern("#");
				}
				result = format.format(value);
			}
			
			break;
		case HSSFCell.CELL_TYPE_STRING:// String类型
			result = cell.getRichStringCellValue().toString();
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			result = "";
			break;
		default:
			result = "";
			break;
		}
		return result;
	}

}
