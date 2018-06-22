package com.nzgreens.console.util;

import com.nzgreens.common.exception.CommonException;
import com.nzgreens.common.model.console.ProductsModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by z on 2016/11/26.
 */
public class ExcelImportUtil {
    private final static String excel2003L =".xls";    //2003- 版本的excel
    private final static String excel2007U =".xlsx";   //2007+ 版本的excel

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception{
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if(excel2003L.equals(fileType)){
            wb = new HSSFWorkbook(inStr);  //2003-
        }else if(excel2007U.equals(fileType)){
            wb = new XSSFWorkbook(inStr);  //2007+
        }else{
            throw new CommonException("","解析的文件格式有误！");
        }
        return wb;
    }

    public static List<ProductsModel> getVoucherListByExcel(InputStream in, String fileName) throws Exception{
        List<ProductsModel> list = new ArrayList<>();
        //创建Excel工作薄
        Workbook work = ExcelImportUtil.getWorkbook(in,fileName);
        if(null == work){
            throw new CommonException("","创建Excel工作薄为空！");
        }
        Sheet sheet = null;
        Row row = null;
        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if(sheet==null){
                continue;
            }
            //遍历当前sheet中的所有行
            int s = sheet.getLastRowNum();
            for (int j = sheet.getFirstRowNum()+1; j <= s; j++) {
                row = sheet.getRow(j);
                if(row==null){
                    continue;
                }
                if(row.getCell(0)==null || row.getCell(1)==null){
                    continue;
                }
                ProductsModel productsModel = new ProductsModel();
                try {
                    productsModel.setGelinProductId((long) row.getCell(0).getNumericCellValue());
                } catch (Exception e) {
                    productsModel.setGelinProductId(Long.valueOf(row.getCell(0).getStringCellValue()));
                }
                try {
                    productsModel.setSellingPrice(row.getCell(1).getStringCellValue());
                } catch (Exception e) {
                    productsModel.setSellingPrice(String.valueOf(row.getCell(1).getNumericCellValue()));
                }
                if(productsModel.getGelinProductId() != 0 && StringUtils.isNotBlank(productsModel.getSellingPrice())){
                    list.add(productsModel);
                }
            }
        }
        work.close();
        return list;
    }
}