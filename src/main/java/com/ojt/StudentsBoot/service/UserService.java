package com.ojt.StudentsBoot.service;

import com.ojt.StudentsBoot.model.CustomerUserDetails;
import com.ojt.StudentsBoot.model.Role;
import com.ojt.StudentsBoot.model.User;
import com.ojt.StudentsBoot.repository.RoleRepository;
import com.ojt.StudentsBoot.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        if (username.contains("@")) {
            // Find user by email
            User user = userRepository.findByEmail(username);

            if (user == null) {
                throw new UsernameNotFoundException("Could not find user");
            }

            System.out.println("User: " + user.getEmail() + " " + user.getRoles());

            return new CustomerUserDetails(user);
        }

        // Find user by username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        System.out.println("User: " + user.getUsername() + " " + user.getRoles());

        return new CustomerUserDetails(user);
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public DataTablesOutput<User> findAll(DataTablesInput input) {
        return userRepository.findAll(input);
    }


    public List<User> getUserByRole(String role) {
        Role targetRole = roleRepository.findByName(role);
        return userRepository.findAll().stream().filter(user -> user.getRoles().contains(targetRole)).collect(Collectors.toList());
    }

    public Long getTeachersCount() {
        return userRepository.count();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void generatePdf(HttpServletResponse response) {
        try {
            // Load the Jasper report template
            InputStream reportTemplate = getClass().getResourceAsStream("/user_report.jrxml");

            // Compile the Jasper report from .jrxml to .japser
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Get your data source
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(userRepository.findAll());

            // Add parameters
            Map<String, Object> parameters = new HashMap<>();

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);

            // Export the report to a PDF file
            JasperExportManager.exportReportToPdfFile(jasperPrint, "users_report.pdf");

            // Set response content type and headers for PDF download
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=users_report.pdf");

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
        try (InputStream reportTemplate = getClass().getResourceAsStream("/user_report.jrxml")) {

            // Compile the Jasper report from .jrxml to .jasper (if needed)
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Create a JRBeanCollectionDataSource with your data
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(userRepository.findAll());

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
            response.setHeader("Content-Disposition", "attachment; filename=sample_report.xlsx");

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
