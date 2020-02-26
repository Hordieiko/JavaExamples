package com.hord.parser;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TaskNameConverter {

    private static final String APPROVAL_ROOT_TASK = "Approvals/ApprovalRootTask/";
    private static final String PARALLEL_TASK = "ParallelTask/";
    private static final String SIMPLE_TASK_WITH_ATTRIBUTE_NAME = "SimpleTask/display/LocaleSet/LocalizedString[@attribute='%s']";
    private static final String DUMMY_PARALLEL = "DummyParallel";
    private static final String WORKFLOW_DEF = "WorkflowDef";
    private static final String APPROVAL_DEF = "ApprovalDef";
    private static final String WORKFLOW_DEF_PATH = "WorkflowDef:/REQ_ASSM_COLL_NEG_EXE_MAN_WF/RootTask/";
    private static final String US_SP_BO_NAME = "CSalesBO";
    private static final String US_HCP_BO_NAME = "HCPBO";
    private static List<File> files;
    private static XPath xpath = XPathFactory.newInstance().newXPath();

    private static final Map<String, String> workflowNameMap = new HashMap<String, String>() {{
        put("Request", "Request");
        put("Assemble", "Assemble");
        put("CollaborateInt", "Collaborate");
        put("CollaborateExt", "Negotiate");
        put("Execute", "Execute");
        put("Manage", "Manage");
    }};

    private static final File exportedData = new File("D:\\CUSTOMERS\\Edwards Lifesciences\\EL-2886\\ExampleFile.xls");
    private static final String approvalsDirPath = "D:\\CUSTOMERS\\Edwards Lifesciences\\EL-2881\\t_1438259995224";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        files = initFiles();
        List<String> internalTaskNameList = getInternalTaskNameList();
        List<String> displayTaskNameList = getDisplayTaskNamesByInternalName(internalTaskNameList);
        updateExportedDataSheetWithDisplayTaskNames(displayTaskNameList);
    }

    private static List<File> initFiles() throws IOException {
        return Files.walk(Paths.get(approvalsDirPath))
                .filter(Files::isRegularFile)
                .map(path -> path.toFile())
                .filter(fileFilter())
                .collect(Collectors.toList());
    }

    private static void updateExportedDataSheetWithDisplayTaskNames(List<String> displayTaskNameList) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        HSSFWorkbook book = new HSSFWorkbook(new FileInputStream(exportedData));
        HSSFSheet sheet = book.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        boolean skipHeaderRow = true;
        int i = 0;
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
                    if (stringCellValue.startsWith(APPROVAL_DEF) || stringCellValue.startsWith(WORKFLOW_DEF)) {
                        cell.setCellValue(displayTaskNameList.get(i));
                        i++;
                        break;
                    }
                }
            }
        }

        book.write(new FileOutputStream(exportedData));
        book.close();
    }

    private static List<String> getInternalTaskNameList() throws IOException {
        LinkedList list = new LinkedList();

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
                    if (stringCellValue.startsWith(APPROVAL_DEF) || stringCellValue.startsWith(WORKFLOW_DEF)) {
                        list.add(stringCellValue);
                        break;
                    }
                }
            }
        }

        return list;
    }

    private static List<String> getDisplayTaskNamesByInternalName(List<String> internalTaskNameList) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        List<String> list = new LinkedList<>();
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        for (String internalName : internalTaskNameList) {
            if (internalName.startsWith(APPROVAL_DEF)) {
                String simpleTaskAttributeName = "Simple" + internalName.replaceFirst("^.+Simple", "").replace("/", "");
                String expression;
                if (internalName.contains(DUMMY_PARALLEL)) {
                    expression = String.format(APPROVAL_ROOT_TASK + PARALLEL_TASK + SIMPLE_TASK_WITH_ATTRIBUTE_NAME, simpleTaskAttributeName);
                } else {
                    expression = String.format(APPROVAL_ROOT_TASK + SIMPLE_TASK_WITH_ATTRIBUTE_NAME, simpleTaskAttributeName);
                }
                String approvalDisplayTaskName = findApprovalDisplayTaskName(expression, documentBuilder);
                if (!approvalDisplayTaskName.isEmpty())
                    list.add(approvalDisplayTaskName);
                else
                    list.add(internalName);
            } else {
                String internalWorkflowName = internalName.replace(WORKFLOW_DEF_PATH, "").replace("/", "");
                list.add(workflowNameMap.get(internalWorkflowName));
            }
        }
        return list;
    }

    private static String findApprovalDisplayTaskName(String expression, DocumentBuilder documentBuilder) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        for (File file : files) {
            XPathExpression expr = xpath.compile(expression);
            Document document = documentBuilder.parse(file);
            NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            if (nodes.getLength() > 0) {
                Node node = nodes.item(0);
                NamedNodeMap attributes = node.getAttributes();
                return attributes.getNamedItem("value").getTextContent();
            }
        }

        return "";
    }

    private static Predicate<? super File> fileFilter() {
        return file -> {
            String fileName = file.getName();
            return fileName.contains(US_SP_BO_NAME) || fileName.contains(US_HCP_BO_NAME);
        };
    }
}
