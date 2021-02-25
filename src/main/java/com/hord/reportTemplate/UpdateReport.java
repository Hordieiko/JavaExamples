package com.hord.reportTemplate;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateReport {

    private static final String REPORT_TEMPLATE_CATEGORY = "ReportTemplateCategory";
    private static final String TALENT_CASTING_REPORTS = "Talent Casting Reports";

    private static final String baseReportPath = "C:\\Users\\vhordieiko.PV\\IdeaProjects\\PS_DCP\\templates\\reports\\1100\\";

    private static final String pathTo22189 = "1102\\22189";
    private static final String pathTo2404838 = "1102\\2404838";
    private static final String pathTo2404857 = "1102\\2404857";
    private static final String pathTo1102templates = "1102\\templates\\";
    private static final List<String> files1102templates = new ArrayList<String>() {{
        add("DJs_-_contracts_in_last_365_days.xml");
        add("Outstanding_Talent_Agreements_-_Pre-Today.xml");
        add("Unsigned_events_happening_this_week.xml");
        add("WDW_acts_with_no_rates.xml");
        add("WDW_BTA_Upcoming_End_Dates.xml");
        add("WDW_LTA_Upcoming_End_Dates.xml");
    }};

    private static final String pathTo3561 = "3561";
    private static final String pathTo3569 = "3569";
    private static final String pathTo18677 = "18677";

    private static final List<String> filesRootTemplates = new ArrayList<String>() {{
        add("templates\\AEA_Contract_Data_for_Database.xml");
        add("templates\\AEA_Identified_to_Train_for_Database.xml");
        add("templates\\AEA_Monthly_Report.xml");
        add("templates\\AEA_Unsigned_Contracts.xml");
        add("templates\\AFM_Weekly_Report.xml");
        add("templates\\Alerts_-_check_for_alerts_services_contracts.xml");
        add("templates\\DCL_Talent_Directory.xml");
        add("templates\\DJs_by_Contract.xml");
        add("templates\\DLR_Aladdin_Contracts.xml");
        add("templates\\DLR_Broadcast_Ops_Consultant_Log.xml");
        add("templates\\DLR_Broadcast_Ops_Consultant_Log_2.xml");
        add("templates\\DLR_Consultant_Log.xml");
        add("templates\\DLR_Services_Contracts.xml");
        add("templates\\DLR_TALENT_Vendor_Log.xml");
        add("templates\\Equity_Roles.xml");
        add("templates\\Filing_FY_15.xml");
        add("templates\\Generic_Booking_Report.xml");
        add("templates\\Name_Talent_no_vendor.xml");
        add("templates\\Name_Talent_outstanding_reports.xml");
        add("templates\\Number_of_Service_Agreements_by_FY.xml");
        add("templates\\Number_of_Talent_Agreements_by_FY.xml");
        add("templates\\Outstanding_Talent_Agreements_-_Pre-Today.xml");
        add("templates\\Report_By_Allison_La_Spata.xml");
        add("templates\\Report_By_Christopher_Litven.xml");
        add("templates\\Services_by_Category.xml");
        add("templates\\Special_Report.xml");
        add("templates\\Sue_Bs_report_to_check_for_unsigned.xml");
        add("templates\\Talent_Access_Report_-_For_Talent_Database.xml");
        add("templates\\WDW_Broadcast_Ops_Consultant_Log.xml");
        add("templates\\WDW_Consultant_Log.xml");
        add("templates\\WDW_TALENT_Vendor_Log.xml");
        add("templates\\Zero_Dollar_Contracts.xml");
    }};

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        List<File> files = new ArrayList<>();
        files.addAll(getFiles(baseReportPath + pathTo22189));
        files.addAll(getFiles(baseReportPath + pathTo2404838));
        files.addAll(getFiles(baseReportPath + pathTo2404857));
        files.addAll(getFiles(baseReportPath + pathTo1102templates, files1102templates));
        files.addAll(getFiles(baseReportPath + pathTo3561));
        files.addAll(getFiles(baseReportPath + pathTo3569));
        files.addAll(getFiles(baseReportPath + pathTo18677));
        files.addAll(getFiles(baseReportPath, filesRootTemplates));

        System.out.println("Files collected: " + files.size());

        for (File file : files) {
            updateReportCategory(file);
        }
    }

    private static void updateReportCategory(File file) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        System.out.println("File in progress: " + file.getAbsolutePath());

        final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        final Document document = documentBuilder.parse(file);

        final Node reportTemplate = document.getFirstChild();

        final NodeList nodeList = document.getElementsByTagName(REPORT_TEMPLATE_CATEGORY);
        if (nodeList.getLength() > 0) {
            final Node node = nodeList.item(0);
            node.setTextContent(TALENT_CASTING_REPORTS);
        } else {
            final Text talentCastingReportsTextNode = document.createTextNode(TALENT_CASTING_REPORTS);
            final Element reportTemplateCategoryElement = document.createElement(REPORT_TEMPLATE_CATEGORY);
            reportTemplateCategoryElement.appendChild(talentCastingReportsTextNode);
            reportTemplate.appendChild(reportTemplateCategoryElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        System.out.println("File updated");
    }

    private static List<File> getFiles(String pathToRootDir) throws IOException {
        final List<File> collect = Files.walk(Paths.get(pathToRootDir))
                .map(Path::toFile)
                .filter(File::isFile)
                .collect(Collectors.toList());
        System.out.println("Root: " + pathToRootDir + " Count: " + collect.size());
        return collect;
    }

    private static List<File> getFiles(String pathToRootDir, List<String> names) {
        final List<File> collect = names.stream()
                .map(name -> new File(pathToRootDir + name))
                .collect(Collectors.toList());
        System.out.println("Root: " + pathToRootDir + " Count: " + collect.size());
        return collect;
    }
}
