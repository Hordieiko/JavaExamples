package com.hord.parser;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefinitionNameConverter {

    private static final File exportedData = new File("D:\\CUSTOMERS\\Edwards Lifesciences\\EL-2886\\ExampleFile.xls");
    private static final Map<String, String> definitionNameMap = new HashMap<String, String>() {{
        put("BundleDef:/CAgreBO/", "US NDA");
        put("BundleDef:/USalesBO/", "US Sales");
        put("BundleDef:/CSalesBO/", "US SP");
        put("BundleDef:/HCPBO/", "US HCP");
    }};

    public static void main(String[] args) throws IOException {
        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(exportedData));
        HSSFSheet sheet = book.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        boolean skipHeaderRow = true;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (skipHeaderRow) {
                skipHeaderRow = false;
                continue;
            }

            Iterator<Cell> cellIterator = row.iterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CellType.STRING) {
                    String stringCellValue = cell.getStringCellValue();
                    if (stringCellValue.startsWith("BundleDef")) {
                        cell.setCellValue(definitionNameMap.get(stringCellValue));
                        break;
                    }
                }
            }
        }

        book.write(new FileOutputStream(exportedData));
        book.close();
    }
}
