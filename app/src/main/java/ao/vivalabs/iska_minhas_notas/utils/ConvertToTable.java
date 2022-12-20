package ao.vivalabs.iska_minhas_notas.utils;

import android.content.Context;
import android.os.Environment;
import android.print.PdfConverter;
import android.util.Log;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ao.vivalabs.iska_minhas_notas.models.ClassTableModel;
import ao.vivalabs.iska_minhas_notas.models.HomeModel;

public class ConvertToTable {

    private final File downloadFolder;
    private final String TAG = "ConvertToTable";

    public ConvertToTable() {
        downloadFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "ISKA");
        if (!getDownloadFolder().exists()) {
            final boolean result = getDownloadFolder().mkdirs();
            if (!result) {
                Log.d(TAG, "Unable to create file at specified path. It already exists");
            } else {
                Log.d(TAG, "Created file at specified path.");
            }
        }
    }

    public void convertToExcel(String fileName, HomeModel homeModel, List<ClassTableModel> classificationList) throws Exception {
        List<String> sheet1HeadersNames = Arrays.asList(
                "Número de estudante",
                "Nome estudante",
                "Curso",
                "Instituto do Ensino Superior");

        List<String> sheet2HeadersNames = Arrays.asList(
                "Disciplina",
                "Abrev.",
                "Ano",
                "Turma",
                "Tipo",
                "Nota Final",
                "A. C.",
                "Final Contínua",
                "1º P.",
                "2º P.",
                "Resultado",
                "Exame",
                "Recurso",
                "Ép. Espec.",
                "Melhoria");

        Workbook workbook = new XSSFWorkbook();

        CellStyle style = workbook.createCellStyle(); // Creating Style
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 25);
        font.setFontName("Calibri");
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFSheet sheet1 = (XSSFSheet) workbook.createSheet("estudante");

        XSSFSheet sheet2 = (XSSFSheet) workbook.createSheet("notas");

        sheet1.setFitToPage(true);
        XSSFPrintSetup printSetup1 = sheet1.getPrintSetup();
        printSetup1.setLandscape(true);
        printSetup1.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
        printSetup1.setFitWidth((short) 1);
        printSetup1.setFitHeight((short) 0);
        printSetup1.setTopMargin(0.75);
        printSetup1.setLeftMargin(0.25);
        printSetup1.setRightMargin(0.25);
        printSetup1.setBottomMargin(0.75);
        printSetup1.setHeaderMargin(0.3);
        printSetup1.setFooterMargin(0.3);

        sheet2.setFitToPage(true);
        XSSFPrintSetup printSetup2 = sheet2.getPrintSetup();
        printSetup2.setLandscape(true);
        printSetup2.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
        printSetup2.setFitWidth((short) 1);
        printSetup2.setFitHeight((short) 0);
        printSetup2.setTopMargin(0.75);
        printSetup2.setLeftMargin(0.25);
        printSetup2.setRightMargin(0.25);
        printSetup2.setBottomMargin(0.75);
        printSetup2.setHeaderMargin(0.3);
        printSetup2.setFooterMargin(0.3);

        /* Create an object of type XSSFTable */
        XSSFTable my_table_1 = sheet1.createTable(null);
        XSSFTable my_table_2 = sheet2.createTable(null);

        /* get CTTable object */
        CTTable cttable1 = my_table_1.getCTTable();
        CTTable cttable2 = my_table_2.getCTTable();

        /* Let us define the required Style for the table */
        CTTableStyleInfo table_style_1 = cttable1.addNewTableStyleInfo();
        table_style_1.setName("TableStyleMedium10");
        CTTableStyleInfo table_style_2 = cttable2.addNewTableStyleInfo();
        table_style_2.setName("TableStyleMedium10");

        /* Set Table Style Options */
        table_style_1.setShowColumnStripes(false); // showColumnStripes=0
        table_style_1.setShowRowStripes(true); // showRowStripes=1
        table_style_2.setShowColumnStripes(false); // showColumnStripes=0
        table_style_2.setShowRowStripes(true); // showRowStripes=1

        int my_data_range_2_row_end = classificationList.size();

        AreaReference my_data_range_1 = new AreaReference(new CellReference(0, 0), new CellReference(1, 3),
                SpreadsheetVersion.EXCEL2007);
        AreaReference my_data_range_2 = new AreaReference(new CellReference(0, 0), new CellReference(my_data_range_2_row_end, 14),
                SpreadsheetVersion.EXCEL2007);

        cttable1.setRef(my_data_range_1.formatAsString());
        cttable1.setDisplayName("ISKA_1"); /* this is the display name of the table */
        cttable1.setName("ISKA_1"); /* This maps to "displayName" attribute in <table>, OOXML */
        cttable1.setId(1L);

        cttable2.setRef(my_data_range_2.formatAsString());
        cttable2.setDisplayName("ISKA_2"); /* this is the display name of the table */
        cttable2.setName("ISKA_2"); /* This maps to "displayName" attribute in <table>, OOXML */
        cttable2.setId(2L); // id attribute against table as long value

        CTTableColumns columns = cttable1.addNewTableColumns();
        columns.setCount(2L); // define number of columns

        /* Define Header Information for the Table */
        for (int i = 0; i < 4; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName(sheet1HeadersNames.get(i));
            column.setId(i + 1);
        }

        /* Add remaining Table Data */
        for (int rowIndex = 0; rowIndex < 2; rowIndex++) // we have to populate 50 rows
        {
            /* Create a Row */
            XSSFRow row = sheet1.createRow(rowIndex);
            for (int columnIndex = 0; columnIndex < 4; columnIndex++) // Fifteen columns in each row
            {
                XSSFCell localXSSFCell = row.createCell(columnIndex);
                if (rowIndex == 0) {
                    localXSSFCell.setCellValue(sheet1HeadersNames.get(columnIndex));
                    localXSSFCell.setCellStyle(style);
                } else {

                    switch (columnIndex) {
                        case 0: {
                            localXSSFCell.setCellValue(Integer.parseInt(homeModel.getNumeroAluno().replaceAll("[^0-9]", "")));
                            localXSSFCell.setCellStyle(style);
                            localXSSFCell.setCellType(CellType.NUMERIC);
                            break;
                        }
                        case 1: {
                            localXSSFCell.setCellValue(homeModel.getNomeEstudante());
                            localXSSFCell.setCellStyle(style);
                            localXSSFCell.setCellType(CellType.STRING);
                            break;
                        }
                        case 2: {
                            localXSSFCell.setCellValue(homeModel.getCurso());
                            localXSSFCell.setCellStyle(style);
                            localXSSFCell.setCellType(CellType.STRING);
                            break;
                        }
                        case 3: {
                            localXSSFCell.setCellValue("ISKA");
                            localXSSFCell.setCellStyle(style);
                            localXSSFCell.setCellType(CellType.STRING);
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < sheet1HeadersNames.size(); i++) {
            float widthExcel = (float) ((getMaxTextSizeColumnCell(sheet1, i) * 2) + 5);
            int width256 = (int) Math
                    .floor((widthExcel * Units.DEFAULT_CHARACTER_WIDTH + 5) / Units.DEFAULT_CHARACTER_WIDTH * 256);
            sheet1.setColumnWidth(i, width256);
        }

        columns = cttable2.addNewTableColumns();
        columns.setCount(15L); // define number of columns

        /* Define Header Information for the Table */
        for (int i = 0; i < 15; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName(sheet2HeadersNames.get(i));
            column.setId(i + 1);
        }

        /* Add remaining Table Data */
        for (int rowIndex = 0; rowIndex < 50; rowIndex++) // we have to populate 50 rows
        {
            /* Create a Row */
            XSSFRow row = sheet2.createRow(rowIndex);
            for (int columnIndex = 0; columnIndex < 15; columnIndex++) // Fifteen columns in each row
            {
                XSSFCell localXSSFCell = row.createCell(columnIndex);
                if (rowIndex == 0) {
                    localXSSFCell.setCellValue(sheet2HeadersNames.get(columnIndex));
                    localXSSFCell.setCellStyle(style);
                } else {

                    switch (columnIndex) {
                        case 0: {
                            localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getDisciplina());
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 1: {
                            localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getAbreviatura());
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 2: {
                            localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getAno());
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 3: {
                            localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getTurma());
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 4: {
                            localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getTipo());
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 5: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getNotaFinal())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getNotaFinal()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getNotaFinal());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 6: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getAvaliacaoContinua())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getAvaliacaoContinua()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getAvaliacaoContinua());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 7: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getParcelar1())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getParcelar1()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getParcelar1());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 8: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getParcelar2())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getParcelar2()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getParcelar2());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 9: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getFinalContinua())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getFinalContinua()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getFinalContinua());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 10: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getResultado())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getResultado()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getResultado());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 11: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getExame())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getExame()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getExame());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 12: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getRecurso())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getRecurso()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getRecurso());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 13: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getEpocaEspecial())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getEpocaEspecial()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getEpocaEspecial());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                        case 14: {
                            if (isNumeric(classificationList.get(rowIndex - 1).getMelhoria())) {
                                localXSSFCell.setCellValue(
                                        Float.parseFloat(classificationList.get(rowIndex - 1).getMelhoria()));
                                localXSSFCell.setCellType(CellType.NUMERIC);
                            } else {
                                localXSSFCell.setCellValue(classificationList.get(rowIndex - 1).getMelhoria());
                            }
                            localXSSFCell.setCellStyle(style);
                            break;
                        }
                    }
                }
            }
        }

        CTAutoFilter autofilter2 = cttable2.addNewAutoFilter();
        autofilter2.setRef("A1:O1");
        sheet2.setRepeatingRows(CellRangeAddress.valueOf("A1:O1"));

        for (int i = 0; i < sheet2HeadersNames.size(); i++) {
            float widthExcel = (float) ((getMaxTextSizeColumnCell(sheet2, i) * 2) + 5);
            int width256 = (int) Math
                    .floor((widthExcel * Units.DEFAULT_CHARACTER_WIDTH + 5) / Units.DEFAULT_CHARACTER_WIDTH * 256);
            sheet2.setColumnWidth(i, width256);
        }


        File file = new File(getDownloadFolder().getPath() + File.separator + String.format("%s_", homeModel.getNumeroAluno().replaceAll("[^0-9]", "")) + fileName);

        if (!getDownloadFolder().exists()) {
            final boolean result = getDownloadFolder().mkdirs();
            if (!result) {
                Log.d(TAG, "Unable to create file at specified path. It already exists");
            } else {
                Log.d(TAG, "Created file at specified path.");
            }
        }

        // lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();

        Log.d(TAG, "ISKA ==> " + String.format("%s_", homeModel.getNumeroAluno().replaceAll("[^0-9]", "")) + fileName + " written successfully");
    }

    public void convertToPdf(String fileName, Context context, HomeModel homeModel, List<ClassTableModel> classificationList) {

        final String html_body = "<html><head> <style> #notes { font-family: Arial, Helvetica, sans-serif; border-collapse: collapse; width: 100%; text-align: center; } #notes td, #notes th { border: 1px solid #ddd; padding: 8px 12px 8px 12px; border: none; } #notes tr:nth-child(odd) { /* background-color: #fce4d6; */ background-color: #f8cbad; } #notes tr:nth-child(even) { background-color: #fce4d6; } #notes th { padding-top: 12px; padding-bottom: 12px; background-color: #ed7d31; color: black; } </style></head><body>";
        final String student_div = String.format("<div><p><strong>Número estudante:</strong> %s</p><p><strong>Nome:</strong> %s</p><p><strong>Curso:</strong> %s</p><p><strong>Instituto do Ensino Superior:</strong> ISKA</p></div>", homeModel.getNumeroAluno().replaceAll("[^0-9]", ""), homeModel.getNomeEstudante(), homeModel.getCurso());
        final String table_tbody = "<table id=\"notes\"> <thead> <tr> <th> Disciplina </th> <th> Abrev. </th> <th> Ano </th> <th> Turma </th> <th> Tipo </th> <th> Nota Final </th> <th> A. C. </th> <th> Final Contínua </th> <th> 1º P. </th> <th> 2º P. </th> <th> Resultado </th> <th> Exame </th> <th> Recurso </th> <th> Ép. Espec. </th> <th> Melhoria </th> </tr> </thead> <tbody>";

        StringBuilder tbody_trs = new StringBuilder();

        for (ClassTableModel c : classificationList) {
            tbody_trs.append(String.format("<tr> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> <td>%s</td> </tr>", c.getDisciplina(), c.getAbreviatura(), c.getAno(), c.getTurma(), c.getTipo(), c.getNotaFinal(), c.getAvaliacaoContinua(), c.getFinalContinua(), c.getParcelar1(), c.getParcelar2(), c.getResultado(), c.getExame(), c.getRecurso(), c.getEpocaEspecial(), c.getMelhoria()));
        }

        final String tbody_html = "</tbody> </table></body></html>";

        final String finalString = html_body + student_div + table_tbody + tbody_trs + tbody_html;

        PdfConverter converter = PdfConverter.getInstance();
        File file = new File(getDownloadFolder().getPath() + File.separator + String.format("%s_", homeModel.getNumeroAluno().replaceAll("[^0-9]", "")) + fileName);
        converter.convert(context, finalString, file);

        Log.d(TAG, "ISKA ==> " + String.format("%s_", homeModel.getNumeroAluno().replaceAll("[^0-9]", "")) + fileName + " written successfully");
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public Integer getMaxTextSizeColumnCell(XSSFSheet sheet, int columnNumber) {
        List<Integer> values = new ArrayList<>();
        for (Row r : sheet) {
            Cell c = r.getCell(columnNumber);
            if (c != null) {
                CellType ct = c.getCellType();
                if (ct == CellType.NUMERIC) {
                    values.add((c.getNumericCellValue() + "").length());
                } else {
                    values.add(c.getStringCellValue().length());
                }
            }
        }
        return Collections.max(values);
    }

    public File getDownloadFolder() {
        return downloadFolder;
    }
}
