package ao.vivalabs.iska_minhas_notas.utils;

import android.os.Environment;
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

public class ConvertToTable {

    private File downloadFolder = null;

    public ConvertToTable(){
        downloadFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + "ISKA");
        if (!getDownloadFolder().exists()) {
            getDownloadFolder().mkdirs();
        }
    }

    public void convert(String fileName, List<ClassTableModel> classificationList) throws Exception{
        List<String> headersNames = Arrays.asList("Disciplina",
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

        XSSFSheet sheet = (XSSFSheet) workbook.createSheet("notas");
        sheet.setFitToPage(true);
        XSSFPrintSetup printSetup = (XSSFPrintSetup) sheet.getPrintSetup();
        printSetup.setLandscape(true);
        printSetup.setPaperSize(XSSFPrintSetup.A4_PAPERSIZE);
        printSetup.setFitWidth((short) 1);
        printSetup.setFitHeight((short) 0);
        printSetup.setTopMargin(0.75);
        printSetup.setLeftMargin(0.25);
        printSetup.setRightMargin(0.25);
        printSetup.setBottomMargin(0.75);
        printSetup.setHeaderMargin(0.3);
        printSetup.setFooterMargin(0.3);

        /* Create an object of type XSSFTable */
        XSSFTable my_table = sheet.createTable(null);

        /* get CTTable object */
        CTTable cttable = my_table.getCTTable();

        /* Let us define the required Style for the table */
        CTTableStyleInfo table_style = cttable.addNewTableStyleInfo();
        table_style.setName("TableStyleMedium3");

        /* Set Table Style Options */
        table_style.setShowColumnStripes(false); // showColumnStripes=0
        table_style.setShowRowStripes(true); // showRowStripes=1

        /* Define the data range including headers */
        AreaReference my_data_range = new AreaReference(new CellReference(0, 0), new CellReference(49, 14),
                SpreadsheetVersion.EXCEL2007);

        /* Set Range to the Table */
        cttable.setRef(my_data_range.formatAsString());
        cttable.setDisplayName("ISKA"); /* this is the display name of the table */
        cttable.setName("ISKA"); /* This maps to "displayName" attribute in <table>, OOXML */
        cttable.setId(1L); // id attribute against table as long value

        CTTableColumns columns = cttable.addNewTableColumns();
        columns.setCount(15L); // define number of columns

        /* Define Header Information for the Table */
        for (int i = 0; i < 15; i++) {
            CTTableColumn column = columns.addNewTableColumn();
            column.setName(headersNames.get(i));
            column.setId(i + 1);
        }

        /* Add remaining Table Data */
        for (int rowIndex = 0; rowIndex < 50; rowIndex++) // we have to populate 50 rows
        {
            /* Create a Row */
            XSSFRow row = sheet.createRow(rowIndex);
            for (int columnIndex = 0; columnIndex < 15; columnIndex++) // Fifteen columns in each row
            {
                XSSFCell localXSSFCell = row.createCell(columnIndex);
                if (rowIndex == 0) {
                    localXSSFCell.setCellValue(headersNames.get(columnIndex));
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

        CTAutoFilter autofilter = cttable.addNewAutoFilter();
        autofilter.setRef("A1:O1");
        sheet.setRepeatingRows(CellRangeAddress.valueOf("A1:O1"));

        // for (int i = 0; i < headersNames.size(); i++) {
        // sheet.autoSizeColumn(i);
        // }

        for (int i = 0; i < headersNames.size(); i++) {
            float widthExcel = (float) ((getMaxTextSizeColumnCell(sheet, i) * 2) + 5);
            int width256 = (int) Math
                    .floor((widthExcel * Units.DEFAULT_CHARACTER_WIDTH + 5) / Units.DEFAULT_CHARACTER_WIDTH * 256);
            sheet.setColumnWidth(i, width256);
        }

        File file = new File(getDownloadFolder().getPath() + File.separator + fileName);

        if (!getDownloadFolder().exists()) {
            getDownloadFolder().mkdirs();
        }

        // lets write the excel data to file now
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        fos.close();

        Log.d("ISKA_LOG","ISKA ==> " + fileName + " written successfully");
    }

    public void convertExcelToPdf(String fileName) throws Exception {
        // Create Workbook to load Excel file
        File excelFile = new File(getDownloadFolder().getPath() + File.separator + fileName);
        com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(excelFile.getAbsolutePath());

        workbook.getWorksheets().get("notas").autoFitRows(true);

        com.aspose.cells.PdfSaveOptions pdfSaveOptions = new com.aspose.cells.PdfSaveOptions();

        // Save the document in PDF format
        File pdfFile = new File(getDownloadFolder().getPath() + File.separator + "ISKA_NOTAS.pdf");
        workbook.save(pdfFile.getAbsolutePath(), pdfSaveOptions);

        Log.d("ISKA_LOG","ISKA ==> " + "ISKA_NOTAS.pdf" + " written successfully");
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
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
