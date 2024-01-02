package org.example.repo;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.example.data.SQLiteDataRetrieval;
import org.example.entity.Holiday;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReportGenerator {
    JasperReport jasperReport;
    SQLiteDataRetrieval dataRetrieval;

    public void generateReports() {
        try {
            var report = ReportGenerator.class.getClassLoader().getResource("templates/report.jasper");
            if (Objects.nonNull(report)) {
                jasperReport = (JasperReport) JRLoader.loadObjectFromFile(report.getPath());
                dataRetrieval = new SQLiteDataRetrieval();

                createReportsFolder();
                generateMapReport();
                generateBeanReport();
                generateResultSetReport();
            } else {
                System.err.println("Report Template not found");
            }
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateMapReport() throws JRException {
        List<Map<String, ?>> holidaysMapsList = dataRetrieval.getDataAsMaps();
        JRMapCollectionDataSource mapDataSource = new JRMapCollectionDataSource(holidaysMapsList);
        JasperPrint mapJasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), mapDataSource);
        JasperExportManager.exportReportToPdfFile(mapJasperPrint, "generatedReports/mapReport.pdf");
    }

    private void generateBeanReport() throws JRException {
        List<Holiday> holidaysEntitiesList = dataRetrieval.getDataAsEntities();
        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(holidaysEntitiesList);
        JasperPrint beanJasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), beanDataSource);
        JasperExportManager.exportReportToPdfFile(beanJasperPrint, "generatedReports/beanReport.pdf");
    }

    private void generateResultSetReport() throws JRException {
        ResultSet holidaysResultSet = dataRetrieval.getDataAsResultSet();
        JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(holidaysResultSet);
        JasperPrint resultSetJasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), resultSetDataSource);
        JasperExportManager.exportReportToPdfFile(resultSetJasperPrint, "generatedReports/resultSetReport.pdf");
    }

    private void createReportsFolder() throws IOException {
        Path folderPath = Paths.get("generatedReports");
        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
        }
    }
}
