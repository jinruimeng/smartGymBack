package common.utils;

import java.io.*;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Created by zhouhang on 2019/3/28.
 */
public class ExcelHelper {
	public static String[] playersExcelTitleArray = new String[] { "参赛号", "学号", "姓名", "性别", "学院", "赛事", "类别", "项目",
			"组别", "职责", "组号", "道次", "成绩", "小组排名", "总排名", "备注" };
	public static String[] applicationsExcelTitleArray = new String[] { "学号", "姓名", "性别", "学院", "赛事", "类别", "项目",
			"组别", "职责", "状态", "备注" };
	/**
	 * //将数据写入文件 by zh
	 * 
	 * @param title
	 * @param data
	 * @param filePath
	 * @throws IOException
	 */
	public static void writeExcel(List<String> title, List<List<String>> data, String filePath) throws IOException {

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("参赛人员总表");
		// 写入标题行
		HSSFRow titleRow = sheet.createRow(0);
		writeRowData(titleRow, title);
		// 写入player数据
		for (int i = 0; i < data.size(); i++) {
			HSSFRow dataRow = sheet.createRow(i + 1);
			writeRowData(dataRow, data.get(i));
		}
		// 写入文件
		File oldFile = new File(filePath);
		if (oldFile.exists() && oldFile.isFile())
			oldFile.delete();
		File newFile = new File(filePath);
		FileOutputStream xlsStream = new FileOutputStream(newFile);
		workbook.write(xlsStream);
		xlsStream.close();
	}

	/**
	 * 写行数据 by zh
	 * 
	 * @param row
	 * @param data
	 */
	public static void writeRowData(HSSFRow row, List<String> data) {
		for (int i = 0; i < data.size(); i++) {
			row.createCell(i).setCellValue(data.get(i));
		}
	}
	/**
	 * 根据文件路径得到EXCEL对象 by zh
	 *
	 */
	public static HSSFWorkbook getExcel(String filePath) throws IOException {
		FileInputStream excelStream = new FileInputStream(filePath);
		HSSFWorkbook excel = new HSSFWorkbook(excelStream);
		return excel;
	}

}
