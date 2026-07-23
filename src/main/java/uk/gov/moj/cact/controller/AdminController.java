package uk.gov.moj.cact.controller;

import com.opencsv.exceptions.CsvValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.gov.moj.cact.service.CsvImportService;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    private static final String SESSION_USER = "UserName";
    private static final int ADMIN_SESSION_SECONDS = 3600;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".csv");

    private final CsvImportService csvImportService;
    private final String adminUser;
    private final String adminPassword;

    public AdminController(CsvImportService csvImportService,
                           @Value("${app.admin.user}") String adminUser,
                           @Value("${app.admin.password}") String adminPassword) {
        this.csvImportService = csvImportService;
        this.adminUser = adminUser;
        this.adminPassword = adminPassword;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name = "userid", required = false) String userId,
                        @RequestParam(name = "password", required = false) String password,
                        HttpServletRequest request) {

        if (userId != null && !adminUser.isEmpty()
                && userId.equals(adminUser) && adminPassword.equals(password)) {

            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute(SESSION_USER, userId);
            session.setMaxInactiveInterval(ADMIN_SESSION_SECONDS);

            LOGGER.info("Admin login successful for user: {}", userId);
            return "redirect:/admin/upload";
        }
        LOGGER.info("Admin login failed for user: {}", userId);
        return "admin-error";
    }

    @GetMapping("/upload")
    public String uploadForm(HttpSession session) {
        return notLoggedIn(session) ? "admin-error" : "admin-upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(name = "uploadFile", required = false) MultipartFile file,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        if (notLoggedIn(session)) {
            return "admin-error";
        }

        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("msg", "Import failed: no file selected");
            return "redirect:/admin/upload";
        }

        String fileName = extractSafeFileName(file.getOriginalFilename());

        if (!ALLOWED_EXTENSIONS.contains(extensionOf(fileName))) {
            LOGGER.warn("Rejected upload with disallowed extension: {}", fileName);
            redirectAttributes.addFlashAttribute("msg", "Import failed: file type not allowed");
            return "redirect:/admin/upload";
        }

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            int rowCount = csvImportService.replaceDatabase(reader);
            LOGGER.info("Imported {} rows from {}", rowCount, fileName);
            redirectAttributes.addFlashAttribute("msg",rowCount + " rows imported from " + fileName);
        } catch (IOException | CsvValidationException e) {
            LOGGER.error("CSV import failed for {}", fileName, e);
            redirectAttributes.addFlashAttribute("msg",
                    "Import failed: the file could not be read. Check the format and try again.");
        }

        return "redirect:/admin/upload";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    private static boolean notLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_USER) == null;
    }

    private String extractSafeFileName(String originalFilename) {
        String cleaned = StringUtils.cleanPath(Objects.requireNonNullElse(originalFilename, ""));
        return Objects.requireNonNullElse(StringUtils.getFilename(cleaned), "");
    }

    private String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot).toLowerCase(Locale.UK);
    }
}