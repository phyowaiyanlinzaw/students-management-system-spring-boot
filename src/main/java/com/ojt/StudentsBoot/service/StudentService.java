package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.Student;
import com.ojt.StudentsBoot.repository.StudentRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Long getStudentsCount() {
        return studentRepository.count();
    }

    public void save(Student student) {
        studentRepository.save(student);
    }

    public DataTablesOutput<Student> findAll(DataTablesInput input) {
        return studentRepository.findAll(input);
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public void generatePdf(HttpServletResponse response) {
        try {
            // Load the Jasper report template
            InputStream reportTemplate = getClass().getResourceAsStream("/student_report.jrxml");

            // Compile the Jasper report from .jrxml to .japser
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Get your data source
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(studentRepository.findAll());

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

            // Export the report to a PDF file
            JasperExportManager.exportReportToPdfFile(jasperPrint, "students_report.pdf");

            // Set response content type and headers for PDF download
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=students_report.pdf");

            // Copy the generated PDF to the response output stream
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

            // Flush and close the response output stream
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here
        }
    }

    public void generateExcel(HttpServletResponse response) {
        // Load the Jasper report template
        try (InputStream reportTemplate = getClass().getResourceAsStream("/student_report.jrxml")) {

            // Compile the Jasper report from .jrxml to .jasper (if needed)
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Create a JRBeanCollectionDataSource with your data
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(studentRepository.findAll());

            // Add parameters if needed
            Map<String, Object> parameters = new HashMap<>();

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

            // Create the XLSX exporter
            JRXlsxExporter exporter = new JRXlsxExporter();

            // Set the XLSX export configuration
            SimpleXlsxReportConfiguration reportConfigXLS = new SimpleXlsxReportConfiguration();
            reportConfigXLS.setSheetNames(new String[]{"Sheet1"}); // Set sheet name(s) as needed
            exporter.setConfiguration(reportConfigXLS);

            // Set response content type and headers for XLSX download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=student_report.xlsx");

            try (OutputStream outputStream = response.getOutputStream()) {
                // Set the exporter input and output
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

                // Export the report to XLSX
                exporter.exportReport();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle any exceptions here
        }
    }
}
