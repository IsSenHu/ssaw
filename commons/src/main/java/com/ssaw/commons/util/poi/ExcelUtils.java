package com.ssaw.commons.util.poi;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Color;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by HuSen on 2018/11/9 10:12.
 */
@SuppressWarnings("ALL")
public class ExcelUtils {
    private static final int MAX_ROWS = 1048576;

    public static List<String> read(String filePath) {
        FileInputStream fis =null;
        Workbook wookbook = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            wookbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到一个工作表
        Sheet sheet = wookbook.getSheetAt(0);

        //获得表头
        Row rowHead = sheet.getRow(0);

        //获得数据的总行数
        int totalRowNum = sheet.getLastRowNum();

        //要获得属性

        List<String> os = new ArrayList<>();
        //获得所有数据
        for(int i = 1 ; i <= totalRowNum ; i++)
        {
            //获得第i行对象
            Row row = sheet.getRow(i);

            String orderBn = row.getCell(2).getStringCellValue();

            os.add(orderBn);
        }
        return os;
    }

    public static void export(List data, Class clazz, OutputStream outputStream, String excelName) throws IllegalAccessException, IOException {
        int totals = data.size();
        int size = MAX_ROWS - 2;
        int totalPages = totals % size == 0 ? (totals / size) : (totals / size) + 1;
        int page = 1;
        List<InputStream> files = new ArrayList<>(1);
        while (page <= totalPages) {
            List excelVos = data.subList((page - 1) * size, page * size < totals ? (page * size) : totals);
            InputStream inputStream = write(excelVos, clazz, excelName);
            if(null != inputStream) {
                files.add(inputStream);
            }
            page++;
        }
        byte[] buffer = new byte[1024];
        if(files.size() == 1) {
            InputStream inputStream = files.get(0);
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            inputStream.close();
        }else {
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
                for (InputStream inputStream : files) {
                    zipOutputStream.putNextEntry(new ZipEntry(UUID.randomUUID().toString() + ".xlsx"));
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    inputStream.close();
                }
                zipOutputStream.finish();
                zipOutputStream.closeEntry();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static InputStream write(List data, Class clazz, String excelName) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Field.setAccessible(fields, true);
        List<ExcelColumn> excelColumns = new ArrayList<>(fields.length);
        Arrays.stream(fields).forEach(field -> {
            ExcelColumn excelColumn = getExcelColumn(field);
            if(null != excelColumn) {
                excelColumns.add(excelColumn);
            }
        });
        int colNumbers = excelColumns.size();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet(UUID.randomUUID().toString());

        // 产生标题行
        SXSSFRow row = sheet.createRow(0);
        SXSSFCell cellTitle = row.createCell(0);

        // sheet样式定义
        CellStyle columnTopStyle = getColumnTopStyle(workbook);
        CellStyle titleStyle = getTitleStyle(workbook);
        CellStyle valueStyle = getValueStyle(workbook);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, (colNumbers - 1)));
        cellTitle.setCellStyle(columnTopStyle);
        cellTitle.setCellValue(excelName);

        // 定义第二行为列名行
        SXSSFRow sheetRow = sheet.createRow(1);
        for(int i = 0; i < colNumbers; i++) {
            SXSSFCell cell = sheetRow.createCell(i);
            cell.setCellType(CellType.STRING);
            XSSFRichTextString text = new XSSFRichTextString(excelColumns.get(i).value());
            cell.setCellValue(text);
            cell.setCellStyle(titleStyle);
        }

        // 将查询出的数据设置到sheet对应的单元格中
        int n = 0;
        for(Object object : data) {
            SXSSFRow sxssfRow = sheet.createRow(n + 2);
            int i = 0;
            for (Field field : fields) {
                ExcelColumn excelColumn = getExcelColumn(field);
                if(null == excelColumn) {
                    continue;
                }
                SXSSFCell sxssfCell;
                Object value = field.get(object);
                if(value instanceof String) {
                    sxssfCell = sxssfRow.createCell(i, CellType.STRING);
                    sxssfCell.setCellStyle(valueStyle);
                    sxssfCell.setCellValue(value.toString());
                }else if(value instanceof Date) {
                    sxssfCell = sxssfRow.createCell(i, CellType.STRING);
                    sxssfCell.setCellStyle(valueStyle);
                    Date dateValue = (Date) value;
                    String format = getFormat(field);
                    if(StringUtils.isNotBlank(format)) {
                        sxssfCell.setCellValue(new SimpleDateFormat(format).format(dateValue));
                    }else {
                        sxssfCell.setCellValue(dateValue.toString());
                    }
                }else if(value instanceof LocalDate) {
                    sxssfCell = sxssfRow.createCell(i, CellType.STRING);
                    sxssfCell.setCellStyle(valueStyle);
                    LocalDate localDateValue = (LocalDate) value;
                    String format = getFormat(field);
                    if(StringUtils.isNotBlank(format)) {
                        sxssfCell.setCellValue(localDateValue.format(DateTimeFormatter.ofPattern(format)));
                    }else {
                        sxssfCell.setCellValue(localDateValue.toString());
                    }
                }else if(value instanceof LocalDateTime) {
                    sxssfCell = sxssfRow.createCell(i, CellType.STRING);
                    sxssfCell.setCellStyle(valueStyle);
                    LocalDateTime localDateTimeValue = (LocalDateTime) value;
                    String format = getFormat(field);
                    if(StringUtils.isNotBlank(format)) {
                        sxssfCell.setCellValue(localDateTimeValue.format(DateTimeFormatter.ofPattern(format)));
                    }else {
                        sxssfCell.setCellValue(localDateTimeValue.toString());
                    }
                }else if(value instanceof Integer) {
                    sxssfCell = sxssfRow.createCell(i, CellType.NUMERIC);
                    sxssfCell.setCellStyle(valueStyle);
                    sxssfCell.setCellValue(value.toString());
                }else if(value instanceof Double) {
                    sxssfCell = sxssfRow.createCell(i, CellType.NUMERIC);
                    sxssfCell.setCellStyle(valueStyle);
                    sxssfCell.setCellValue((Double) value);
                }else if(value instanceof BigDecimal) {
                    sxssfCell = sxssfRow.createCell(i, CellType.NUMERIC);
                    sxssfCell.setCellStyle(valueStyle);
                    sxssfCell.setCellValue(((BigDecimal)value).doubleValue());
                }
                i++;
            }
            n++;
        }
        try {
            OutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return parseInputStream(outputStream);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ExcelColumn getExcelColumn(Field field) {
        Annotation annotation = field.getDeclaredAnnotation(ExcelColumn.class);
        if(null != annotation) {
            return (ExcelColumn) annotation;
        }
        return null;
    }

    private static String getFormat(Field field) {
        Annotation annotation = field.getDeclaredAnnotation(ExcelColumn.class);
        if(null != annotation) {
            ExcelColumn excelColumn = (ExcelColumn) annotation;
            return excelColumn.format();
        }
        return null;
    }

    private static CellStyle getColumnTopStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置底边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色
        cellStyle.setBottomBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.RED, new DefaultIndexedColorMap())).getIndex());
        // 设置左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色
        cellStyle.setLeftBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLUE, new DefaultIndexedColorMap())).getIndex());
        // 设置右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色
        cellStyle.setRightBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLUE, new DefaultIndexedColorMap())).getIndex());
        // 设置顶边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色
        cellStyle.setTopBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.RED, new DefaultIndexedColorMap())).getIndex());
        // 在样式中应用设置的字体
        cellStyle.setFont(font);
        // 设置自动换行
        cellStyle.setWrapText(false);
        // 设置垂直对齐的样式为居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    private static CellStyle getTitleStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        font.setBold(true);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置底边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色
        cellStyle.setBottomBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色
        cellStyle.setLeftBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色
        cellStyle.setRightBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置顶边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色
        cellStyle.setTopBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 在样式中应用设置的字体
        cellStyle.setFont(font);
        // 设置自动换行
        cellStyle.setWrapText(false);
        // 设置垂直对齐的样式为居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    private static CellStyle getValueStyle(SXSSFWorkbook workbook) {
        // 设置字体
        Font font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        font.setBold(false);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式
        CellStyle cellStyle = workbook.createCellStyle();
        // 设置底边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        // 设置底边框颜色
        cellStyle.setBottomBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置左边框
        cellStyle.setBorderLeft(BorderStyle.THIN);
        // 设置左边框颜色
        cellStyle.setLeftBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置右边框
        cellStyle.setBorderRight(BorderStyle.THIN);
        // 设置右边框颜色
        cellStyle.setRightBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 设置顶边框
        cellStyle.setBorderTop(BorderStyle.THIN);
        // 设置顶边框颜色
        cellStyle.setTopBorderColor(XSSFColor.toXSSFColor(new XSSFColor(Color.BLACK, new DefaultIndexedColorMap())).getIndex());
        // 在样式中应用设置的字体
        cellStyle.setFont(font);
        // 设置自动换行
        cellStyle.setWrapText(false);
        // 设置垂直对齐的样式为居中对齐
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    private static ByteArrayInputStream parseInputStream(OutputStream out) {
        ByteArrayOutputStream  byteArrayOutputStream = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public static void main(String[] args) throws IOException, IllegalAccessException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<ExcelTestVo> data = new ArrayList<>();
        for(int i = 0; i < 2000000; i++) {
            ExcelTestVo excelTestVo = new ExcelTestVo();
            excelTestVo.setFieldOne("列一" + (i + 1));
            excelTestVo.setFieldTwo(i + 1);
            excelTestVo.setFieldThree(i + 1.01);
            excelTestVo.setFieldFour(Date.from(Instant.now()));
            excelTestVo.setFieldFive(LocalDateTime.now());
            excelTestVo.setFieldSix(LocalDate.now());
            data.add(excelTestVo);
        }
        if(data.size() + 2 > MAX_ROWS) {
            export(data, ExcelTestVo.class, new FileOutputStream(new File("C:\\Users\\HS\\Desktop\\桌面壁纸\\Excel.zip")), "测试Excel导出");
        }else {
            export(data, ExcelTestVo.class, new FileOutputStream(new File("C:\\Users\\HS\\Desktop\\Excel.xlsx")), "测试Excel导出");
        }
        System.out.println(stopWatch.getTime(TimeUnit.MILLISECONDS));
        read("C:\\Users\\hszyp\\Desktop\\美团取消订单-经纬度查询.xlsx");
    }
}
