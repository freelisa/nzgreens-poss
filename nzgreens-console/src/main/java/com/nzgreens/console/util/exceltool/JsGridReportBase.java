/*
 * 文件名：JsGridReportBase.java
 * 版权：
 * 描述：
 * 修改人：
 * 修改时间：
 * 修改内容：
 */
package com.nzgreens.console.util.exceltool;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JsGridReportBase {
	public SimpleDateFormat timeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	private HttpServletResponse response;

	private HttpSession session;

	private ServletOutputStream out;

	private InputStream downloadFile;

	/**
	 * 大数据量导出静态变量
	 */
	public static int EXCEL_MAX_CNT = 100000; //每个excel文件多少条数据
	public static int SHEET_MAX_CNT = 20000; //每个sheet多少条数据

	public JsGridReportBase() {
	}

	public JsGridReportBase(HttpServletRequest request,
							HttpServletResponse response) throws Exception {
		this.response = response;
		session = request.getSession();
		init(this.session);
	}

	private void init(HttpSession session) throws Exception {
		out = response.getOutputStream();
	}

	/**
	 * 向浏览器输出JSON数据
	 *
	 * @param
	 * @return void
	 */
	public void outDataToBrowser(TableData tableData) {
		StringBuffer outData = new StringBuffer();

		// 向前台输出数据
		outData.append("{pageInfo: {totalRowNum: " + tableData.getTotalRows()
				+ "},");
		outData.append("data: [");
		boolean isFirst = true;

		TableHeaderMetaData headerMetaData = tableData.getTableHeader();
		List<TableDataRow> dataRows = tableData.getRows();
		try {
			for (TableDataRow dataRow : dataRows) {
				List<TableDataCell> dataCells = dataRow.getCells();
				int size = dataCells.size();
				if (!isFirst) {
					outData.append(",{");
					for (int i = 0; i < size; i++) {
						outData.append(headerMetaData.getColumnAt(i).getId()
								+ ": '" + dataCells.get(i).getValue() + "',");
					}
					int index = outData.lastIndexOf(",");
					outData.deleteCharAt(index);
					outData.append("}");
				} else {
					outData.append("{");
					for (int i = 0; i < size; i++) {
						outData.append(headerMetaData.getColumnAt(i).getId()
								+ ": '" + dataCells.get(i).getValue() + "',");
					}
					int index = outData.lastIndexOf(",");
					outData.deleteCharAt(index);
					outData.append("}");
					isFirst = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		outData.append("]");
		outData.append("}");

		try {
			out.print(outData.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param
	 * @return void
	 */
	private void stopGrouping(HSSFSheet sheet, HashMap<Integer, String> word,
                              HashMap<Integer, Integer> counter, int i, int size, int rownum,
                              HSSFCellStyle style) {
		String w = word.get(i);
		if (w != null) {
			int len = counter.get(i);
			CellRangeAddress address = new CellRangeAddress(rownum - len,
					rownum - 1, i, i);
			sheet.addMergedRegion(address);
			fillMergedRegion(sheet, address, style);
			word.remove(i);
			counter.remove(i);
		}
		if (i + 1 < size) {
			stopGrouping(sheet, word, counter, i + 1, size, rownum, style);
		}
	}

	/**
	 *
	 * @param
	 * @return void
	 */
	private void generateColumn(HSSFSheet sheet, TableColumn tc, int maxlevel,
                                int rownum, int colnum, HSSFCellStyle headerstyle) {
		HSSFRow row = sheet.getRow(rownum);
		if (row == null)
			row = sheet.createRow(rownum);

		HSSFCell cell = row.createCell(colnum);
		cell.setCellValue(tc.getDisplay());

		if (headerstyle != null)
			cell.setCellStyle(headerstyle);
		if (tc.isComplex()) {
			CellRangeAddress address = new CellRangeAddress(rownum, rownum,
					colnum, colnum + tc.getLength() - 1);
			sheet.addMergedRegion(address);
			fillMergedRegion(sheet, address, headerstyle);

			int cn = colnum;
			for (int i = 0; i < tc.getChildren().size(); i++) {
				if (i != 0) {
					cn = cn + tc.getChildren().get(i - 1).getLength();
				}
				generateColumn(sheet, tc.getChildren().get(i), maxlevel,
						rownum + 1, cn, headerstyle);
			}
		} else {
			CellRangeAddress address = new CellRangeAddress(rownum, rownum
					+ maxlevel - tc.level, colnum, colnum);
			sheet.addMergedRegion(address);
			fillMergedRegion(sheet, address, headerstyle);
		}
		sheet.autoSizeColumn(colnum, true);
	}

	/**
	 *
	 * @param
	 * @return void
	 */
	private void fillMergedRegion(HSSFSheet sheet, CellRangeAddress address,
                                  HSSFCellStyle style) {
		for (int i = address.getFirstRow(); i <= address.getLastRow(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (row == null)
				row = sheet.createRow(i);
			for (int j = address.getFirstColumn(); j <= address.getLastColumn(); j++) {
				HSSFCell cell = row.getCell(j);
				if (cell == null) {
					cell = row.createCell(j);
					if (style != null)
						cell.setCellStyle(style);
				}
			}
		}
	}

	/**
	 * 写入工作表
	 * @param wb Excel工作簿
	 * @param title Sheet工作表名称
	 * @param styles 表头样式
	 * @param creator 创建人
	 * @param tableData 表格数据
	 * @throws Exception
	 */
	public HSSFWorkbook writeSheet(HSSFWorkbook wb, String title, HashMap<String, HSSFCellStyle> styles, String creator, TableData tableData, String[] totalHeaders) throws Exception {

		TableHeaderMetaData headerMetaData = tableData.getTableHeader();// 获得HTML的表头元素

		SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String create_time = formater.format(new Date());

		HSSFSheet sheet = wb.createSheet(title);// 在Excel工作簿中建一工作表
		sheet.setDisplayGridlines(false);// 设置表标题是否有表格边框
		sheet.setColumnWidth(0, 20 * 256);
		//创建标题
		HSSFRow row = sheet.createRow(0);// 创建新行
		HSSFCell cell = row.createCell(0);// 创建新列
		int rownum = 0;
		cell.setCellValue(new HSSFRichTextString(title));
		HSSFCellStyle style = styles.get("TITLE");//设置标题样式
		if (style != null)
			cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerMetaData
				.getColumnCount() - 1));//合并标题行：起始行号，终止行号， 起始列号，终止列号

		if(totalHeaders != null){
			// 产生表格统计
			HSSFRow totalRow = sheet.createRow(1);
			for (short i = 0; i < totalHeaders.length; i++) {
				HSSFCell cell1 = totalRow.createCell(i);
				cell1.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(totalHeaders[i]);
				cell1.setCellValue(text);
			}
		}

		//创建副标题
		row = sheet.createRow(3);
		cell = row.createCell(0);
		cell.setCellValue(new HSSFRichTextString("创建人:"));
		style = styles.get("SUB_TITLE");
		if (style != null)
			cell.setCellStyle(style);

		cell = row.createCell(1);
		cell.setCellValue(new HSSFRichTextString(creator));
		style = styles.get("SUB_TITLE2");
		if (style != null)
			cell.setCellStyle(style);

		cell = row.createCell(2);
		cell.setCellValue(new HSSFRichTextString("创建时间:"));
		style = styles.get("SUB_TITLE");
		if (style != null)
			cell.setCellStyle(style);

		cell = row.createCell(3);
		style = styles.get("SUB_TITLE2");
		cell.setCellValue(new HSSFRichTextString(create_time));
		if (style != null)
			cell.setCellStyle(style);

		rownum = 5;// 如果rownum = 1，则去掉创建人、创建时间等副标题；如果rownum = 0， 则把标题也去掉

		HSSFCellStyle headerstyle = styles.get("TABLE_HEADER");

		int colnum = 0;
		for (int i = 0; i < headerMetaData.getOriginColumns().size(); i++) {
			TableColumn tc = headerMetaData.getOriginColumns().get(i);
			if (i != 0) {
				colnum += headerMetaData.getOriginColumns().get(i - 1)
						.getLength();
			}
			generateColumn(sheet, tc, headerMetaData.maxlevel, rownum, colnum,
					headerstyle);
		}
		rownum += headerMetaData.maxlevel;

		List<TableDataRow> dataRows = tableData.getRows();

		HashMap<Integer, Integer> counter = new HashMap<Integer, Integer>();
		HashMap<Integer, String> word = new HashMap<Integer, String>();
		int index = 0;
		for (TableDataRow dataRow : dataRows) {
			row = sheet.createRow(rownum);

			List<TableDataCell> dataCells = dataRow.getCells();
			int size = headerMetaData.getColumns().size();
			index = -1;
			for (int i = 0; i < size; i++) {
				TableColumn tc = headerMetaData.getColumns().get(i);
				if (!tc.isVisible())
					continue;
				index++;

				String value = dataCells.get(i).getValue();
				if (tc.isGrouped()) {
					String w = word.get(index);
					if (w == null) {
						word.put(index, value);
						counter.put(index, 1);
						createCell(row, tc, dataCells, i, index, styles);
					} else {
						if (w.equals(value)) {
							counter.put(index, counter.get(index) + 1);
						} else {
							stopGrouping(sheet, word, counter, index, size,
									rownum, styles.get("STRING"));

							word.put(index, value);
							counter.put(index, 1);
							createCell(row, tc, dataCells, i, index, styles);
						}
					}
				} else {
					createCell(row, tc, dataCells, i, index, styles);
				}
			}
			rownum++;
		}

		stopGrouping(sheet, word, counter, 0, index, rownum, styles
				.get("STRING"));
		// 设置前两列根据数据自动列宽
		for (int c = 0; c < headerMetaData.getColumns().size(); c++) {
			sheet.autoSizeColumn((short) c);
			sheet.setColumnWidth(c, (c + 1)*2560);
//			String t = headerMetaData.getColumns().get(c).getDisplay();
//			//if(sheet.getColumnWidth(c)<t.length()*256*3)
//				sheet.setColumnWidth(c, t.length()*256*3);
		}
		sheet.setGridsPrinted(true);

		return wb;
	}

	/**
	 * 写入工作表
	 * @param wb Excel工作簿
	 * @param styles Sheet工作表名称
	 * @param styles 表头样式
	 * @param creator 创建人
	 * @param tableDataLst 表格数据
	 * @throws Exception
	 */
	public HSSFWorkbook writeSheet(HSSFWorkbook wb, HashMap<String, HSSFCellStyle> styles, String creator, List<TableData> tableDataLst) throws Exception {

		SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		String create_time = formater.format(new Date());

		int cnt = 1;
		for(TableData tableData : tableDataLst){
			String sheetTitle = tableData.getSheetTitle();
			sheetTitle = sheetTitle==null||sheetTitle.equals("")?"sheet"+cnt:sheetTitle;
			cnt++;

			TableHeaderMetaData headerMetaData = tableData.getTableHeader();// 获得HTML的表头元素
			HSSFSheet sheet = wb.createSheet(sheetTitle);// 在Excel工作簿中建一工作表
			sheet.setDisplayGridlines(false);// 设置表标题是否有表格边框
			wb.cloneSheet(0);

			//创建标题
			HSSFRow row = sheet.createRow(0);// 创建新行
			HSSFCell cell = row.createCell(0);// 创建新列
			int rownum = 0;
			cell.setCellValue(new HSSFRichTextString(sheetTitle));
			HSSFCellStyle style = styles.get("TITLE");//设置标题样式
			if (style != null)
				cell.setCellStyle(style);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headerMetaData
					.getColumnCount() - 1));//合并标题行：起始行号，终止行号， 起始列号，终止列号

			//创建副标题
			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue(new HSSFRichTextString("创建人:"));
			style = styles.get("SUB_TITLE");
			if (style != null)
				cell.setCellStyle(style);

			cell = row.createCell(1);
			cell.setCellValue(new HSSFRichTextString(creator));
			style = styles.get("SUB_TITLE2");
			if (style != null)
				cell.setCellStyle(style);

			cell = row.createCell(2);
			cell.setCellValue(new HSSFRichTextString("创建时间:"));
			style = styles.get("SUB_TITLE");
			if (style != null)
				cell.setCellStyle(style);

			cell = row.createCell(3);
			style = styles.get("SUB_TITLE2");
			cell.setCellValue(new HSSFRichTextString(create_time));
			if (style != null)
				cell.setCellStyle(style);

			rownum = 3;// 如果rownum = 1，则去掉创建人、创建时间等副标题；如果rownum = 0， 则把标题也去掉

			HSSFCellStyle headerstyle = styles.get("TABLE_HEADER");

			int colnum = 0;
			for (int i = 0; i < headerMetaData.getOriginColumns().size(); i++) {
				TableColumn tc = headerMetaData.getOriginColumns().get(i);
				if (i != 0) {
					colnum += headerMetaData.getOriginColumns().get(i - 1).getLength();
				}
				generateColumn(sheet, tc, headerMetaData.maxlevel, rownum, colnum, headerstyle);
			}
			rownum += headerMetaData.maxlevel;

			List<TableDataRow> dataRows = tableData.getRows();

			int index = 0;
			for (TableDataRow dataRow : dataRows) {
				row = sheet.createRow(rownum);

				List<TableDataCell> dataCells = dataRow.getCells();
				int size = headerMetaData.getColumns().size();
				index = -1;
				for (int i = 0; i < size; i++) {
					TableColumn tc = headerMetaData.getColumns().get(i);
					if (!tc.isVisible())
						continue;
					index++;

					createCell(row, tc, dataCells, i, index, styles);
				}
				rownum++;
			}
			// 设置前两列根据数据自动列宽
			for (int c = 0; c < headerMetaData.getColumns().size(); c++) {
				sheet.autoSizeColumn((short) c);
				String t = headerMetaData.getColumns().get(c).getDisplay();
				if(sheet.getColumnWidth(c)<t.length()*256*3)
					sheet.setColumnWidth(c, t.length()*256*3);
			}
			sheet.setGridsPrinted(true);
		}

		return wb;
	}

	/**
	 * 导出Excel(单工作表)
	 *
	 * @param title
	 *            文件名
	 * @param creator
	 *            创建人
	 * @param tableData
	 *            表格数据
	 * @return void <style name="dataset"> case SYSROWNUM%2==0?#row0:#row1;
	 *         fontsize:9px; </style> <style name="row0"> import(parent);
	 *         bgcolor:#FFFFFF; </style> <style name="row1"> import(parent);
	 *         bgcolor:#CAEAFE; </style>
	 */
	public void exportToExcel(String title, String creator, TableData tableData,String[] totalHeaders)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();// 创建新的Excel 工作簿

		HashMap<String, HSSFCellStyle> styles = initStyles(wb);// 根据模板文件，初始化表头样式

		wb = writeSheet(wb,title,styles,creator,tableData,totalHeaders);//写入工作表

		String sFileName = title + ".xls";
		response.setHeader("Content-Disposition", "attachment;filename="
				.concat(String.valueOf(URLEncoder.encode(sFileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");

		wb.write(response.getOutputStream());
	}

	/**
	 * 导出Excel(多工作表)
	 *
	 * @param title
	 *            文件名
	 * @param creator
	 *            创建人
	 * @param tableDataLst
	 *            各工作格数据(注意：每个tableData要设置sheet名称，否则按默认呈现)
	 * @return void <style name="dataset"> case SYSROWNUM%2==0?#row0:#row1;
	 *         fontsize:9px; </style> <style name="row0"> import(parent);
	 *         bgcolor:#FFFFFF; </style> <style name="row1"> import(parent);
	 *         bgcolor:#CAEAFE; </style>
	 */
	public void exportToExcel(String title, String creator, List<TableData> tableDataLst)
			throws Exception {

		HSSFWorkbook wb = new HSSFWorkbook();// 创建新的Excel 工作簿
		HashMap<String, HSSFCellStyle> styles = initStyles(wb);// 初始化表头样式

		int i = 1;
		for(TableData tableData : tableDataLst){
			String sheetTitle = tableData.getSheetTitle();
			sheetTitle = sheetTitle==null||sheetTitle.equals("")?"sheet"+i:sheetTitle;
			wb = writeSheet(wb,tableData.getSheetTitle(),styles,creator,tableData,null);//写入工作表
			i++;
		}

		String sFileName = title + ".xls";
		response.setHeader("Content-Disposition", "attachment;filename="
				.concat(String.valueOf(URLEncoder.encode(sFileName, "UTF-8"))));
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/vnd.ms-excel");

		wb.write(response.getOutputStream());
	}

	/**
	 * 大数据量ZIP包导出Excel(多文件、多工作表)
	 *
	 * @param zout
	 *            Excel文件名
	 * @param bean
	 *            创建人
	 * @param zout
	 *            zip压缩输出流
	 * @return void <style name="dataset"> case SYSROWNUM%2==0?#row0:#row1;
	 *         fontsize:9px; </style> <style name="row0"> import(parent);
	 *         bgcolor:#FFFFFF; </style> <style name="row1"> import(parent);
	 *         bgcolor:#CAEAFE; </style>
	 */
	@SuppressWarnings("unchecked")
	public void exportToExcel(ZipOutputStream zout, ExcelBean bean)throws Exception {
		List list = bean.getList();

		HSSFWorkbook wb = new HSSFWorkbook();// 创建新的Excel 工作簿
		HashMap<String, HSSFCellStyle> styles = initStyles(wb);// 初始化表头样式

		int excel_num = list.size() / EXCEL_MAX_CNT; // 此数据分几个excel
		if (list.size() % EXCEL_MAX_CNT > 0) {
			excel_num += 1;
		}
		int start=0,end=0;//xls文件的开始记录和结束记录索引
		int _start=0,_end=0;//sheet的开始记录和结束记录索引
		for (int i = 1; i <= excel_num; i++) {
			start = (i-1) * EXCEL_MAX_CNT;
			end = i * EXCEL_MAX_CNT - 1;
			end = end>list.size()?list.size():end;
			List<DataCount> sublist = list.subList(start, end);
			System.out.println("正在导出第 " + i + " 个文件！");
			int sheet_num = sublist.size() / SHEET_MAX_CNT; // 此excel文件分几个sheet
			if (sublist.size() % SHEET_MAX_CNT > 0) {
				sheet_num += 1;
			}

			List<TableData> tds = new ArrayList<TableData>();
			TableData td = null;
			for(int j = 1; j <= sheet_num; j++){
				_start = (j-1) * SHEET_MAX_CNT;
				_end = j * SHEET_MAX_CNT - 1;
				_end = _end>sublist.size()?sublist.size():_end;
				System.out.println("正在导出第"+j+"个sheet：第"+(start+_start+1)+"~"+(start+_start+_end+1)+"条记录");
				List<DataCount> sheetLst = sublist.subList(_start, _end);
				if(bean.getChildren()!=null && bean.getChildren().length>0)
					td = ExcelUtils.createTableData(sheetLst, ExcelUtils.createTableHeader(bean.getHearders(), bean.getChildren()), bean.getFields());
				else
					td = ExcelUtils.createTableData(sheetLst, ExcelUtils.createTableHeader(bean.getHearders()), bean.getFields());
				td.setSheetTitle("第"+(start+_start+1)+"~"+(start+_start+_end+1)+"条记录");
				tds.add(td);
			}
			wb = writeSheet(wb,styles,bean.getCreator(),tds);//写入工作表

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			wb.write(baos);
			baos.flush();
			byte[] aa = baos.toByteArray();

			String sFileName = bean.getTitle() + "(" + (start+1) + "~" + (end+1) + ")" + ".xls";
//			fileNames = java.net.URLEncoder.encode(sFileName, "UTF-8");// 处理中文文件名的问题
//			fileNames = new String(sFileName.getBytes("UTF-8"), "GBK"); // 处理中文文件名的问题
			downloadFile = new ByteArrayInputStream(aa, 0, aa.length);
			ZipEntry entry = new ZipEntry(sFileName); // 实例化条目列表
			zout.putNextEntry(entry); // 将ZIP条目列表写入输出流
			while (downloadFile.read(aa) > 0) { // 如果文件未读完
				zout.write(aa); // 写入缓冲数据
			}
			baos.close();
			downloadFile.close();
		}
	}

	/**
	 * 创建单元格（带隔行背景色）
	 *
	 * @param
	 * @return void
	 */
	private void createCell(HSSFRow row, TableColumn tc,
                            List<TableDataCell> data, int i, int index,
                            HashMap<String, HSSFCellStyle> styles) {
		TableDataCell dc = data.get(i);
		HSSFCell cell = row.createCell(index);
		switch (tc.getColumnType()) {
			case TableColumn.COLUMN_TYPE_INTEGER:
				cell.setCellValue(dc.getIntValue());
				HSSFCellStyle style = styles.get("INT");
				if (row.getRowNum() % 2 != 0)
					style = styles.get("INT_C");
				if (style != null)
					cell.setCellStyle(style);
				break;
			case TableColumn.COLUMN_TYPE_FLOAT_2:
				cell.setCellValue(dc.getDoubleValue());
				style = styles.get("D2");
				if (row.getRowNum() % 2 != 0)
					style = styles.get("D2_C");
				if (style != null)
					cell.setCellStyle(style);
				break;
			case TableColumn.COLUMN_TYPE_FLOAT_3:
				cell.setCellValue(dc.getDoubleValue());
				style = styles.get("D3");
				if (row.getRowNum() % 2 != 0)
					style = styles.get("D3_C");
				if (style != null)
					cell.setCellStyle(style);
				break;
			case TableColumn.COLUMN_TYPE_RED_BG:
				cell.setCellValue(dc.getValue());
				style = styles.get("RED_BG");
				if (style != null)
					cell.setCellStyle(style);
				break;
			case TableColumn.COLUMN_TYPE_YELLOW_BG:
				cell.setCellValue(dc.getValue());
				style = styles.get("YELLOW_BG");
				if (style != null)
					cell.setCellStyle(style);
				break;
			case TableColumn.COLUMN_TYPE_GREEN_BG:
				cell.setCellValue(dc.getValue());
				style = styles.get("GREEN_BG");
				if (style != null)
					cell.setCellStyle(style);
				break;
			default:
				if (dc.getValue().equalsIgnoreCase("&nbsp;"))
					cell.setCellValue("");
				else
					cell.setCellValue(dc.getValue());
				style = styles.get("STRING");
				if (row.getRowNum() % 2 != 0)
					style = styles.get("STRING_C");
				if (style != null)
					cell.setCellStyle(style);
		}
	}

	/**
	 * 根据模板初始化样式
	 * 		注意：module.xls模板文件跟该类同一路径
	 * @param wb
	 * @return
	 */
	private HashMap<String, HSSFCellStyle> initStyles(HSSFWorkbook wb) {
		HashMap<String, HSSFCellStyle> ret = new HashMap<String, HSSFCellStyle>();
		try {
			ClassPathResource res = new ClassPathResource("module.xls", this.getClass());//注意：module.xls模板文件跟该类同一路径
			InputStream is = res.getInputStream();
			POIFSFileSystem fs = new POIFSFileSystem(is);

			HSSFWorkbook src = new HSSFWorkbook(fs);
			HSSFSheet sheet = src.getSheetAt(0);

			buildStyle(wb, src, sheet, 0, ret, "TITLE");//标题样式
			buildStyle(wb, src, sheet, 1, ret, "SUB_TITLE");//副标题样式
			buildStyle(wb, src, sheet, 2, ret, "SUB_TITLE2");//副标题2样式

			buildStyle(wb, src, sheet, 4, ret, "TABLE_HEADER");//表头样式
			buildStyle(wb, src, sheet, 5, ret, "STRING");//字符串单元格样式
			buildStyle(wb, src, sheet, 6, ret, "INT");//整数单元格样式
			buildStyle(wb, src, sheet, 7, ret, "D2");//2位小数单元格样式
			buildStyle(wb, src, sheet, 8, ret, "D3");//3位小数单元格样式

			buildStyle(wb, src, sheet, 10, ret, "STRING_C");//字符串单元格样式（带背景色）
			buildStyle(wb, src, sheet, 11, ret, "INT_C");//整数单元格样式（带背景色）
			buildStyle(wb, src, sheet, 12, ret, "D2_C");//2位小数单元格样式（带背景色）
			buildStyle(wb, src, sheet, 13, ret, "D3_C");//3位小数单元格样式（带背景色）

			buildStyle(wb, src, sheet, 15, ret, "RED_BG");//红色单元格背景
			buildStyle(wb, src, sheet, 16, ret, "YELLOW_BG");//黄色单元格背景
			buildStyle(wb, src, sheet, 17, ret, "GREEN_BG");//绿色单元格背景
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return ret;
	}

	/**
	 *
	 * @param
	 * @return void
	 */
	private void buildStyle(HSSFWorkbook wb, HSSFWorkbook src, HSSFSheet sheet,
                            int index, HashMap<String, HSSFCellStyle> ret, String key) {
		HSSFRow row = sheet.getRow(index);
		HSSFCell cell = row.getCell(1);
		HSSFCellStyle nstyle = wb.createCellStyle();
		ExcelUtils.copyCellStyle(wb, nstyle, src, cell.getCellStyle());
		ret.put(key, nstyle);
	}

	/**
	 * 工具方法，将一个字符串转换为UTF-8编码
	 *
	 * @param string
	 *            需要转换的字符串
	 * @return String 转换后的UTF-8字符串
	 */
	protected String getUTF8String(String string) {
		if (string == null) {
			return null;
		} else {
			try {
				String str = new String(string.getBytes("ISO8859-1"), "UTF-8");
				return str;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return string;
			}
		}
	}

	/**
	 * 工具方法，将一个字符串转换为GBK编码
	 *
	 * @param string
	 *            需要转换的字符串
	 * @return String 转换后的GBK字符串
	 */
	protected String getGBKString(String string) {
		if (string == null) {
			return null;
		} else {
			try {
				String str = new String(string.getBytes("ISO8859-1"), "GBK");
				return str;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return string;
			}
		}
	}

	/**
	 * 单元格值为空处理
	 *
	 * @param value
	 *            单元格值
	 * @return String 如果单元格值为空，则返回"&nbsp;"，否则返回原值
	 */
	public String fieldRender(String value) {
		if (value == null) {
			return "&nbsp;";
		} else {
			return value;
		}
	}

}
