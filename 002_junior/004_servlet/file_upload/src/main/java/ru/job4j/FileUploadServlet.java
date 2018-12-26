package ru.job4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Servlet handling file upload (POST) and download (GET).
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 0.1
 */
@WebServlet("/upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 10,   // 10 MB
        maxFileSize = 1024 * 1024 * 50,         // 50 MB
        maxRequestSize = 1024 * 1024 * 100)     // 100 MB
public class FileUploadServlet extends HttpServlet {
    /**
     * Directory to save uploaded files into.
     * Path is relative to the web application directory.
     */
    private static final String STORE_DIR = "files";

    /**
     * Downloads file.
     *
     * @param request  Request object with file name.
     * @param response Response object.
     * @throws IOException If IO problems happen.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletContext context = this.getServletContext();
        String fileName = request.getParameter("fileName");
        Path file = this.getFile(context, fileName);
        this.setDownloadResponseParameters(response, context, file);
        this.writeFileToOutputStream(response, file);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        Path uploadDir = Paths.get(context.getRealPath(""), STORE_DIR);
        uploadDir.toFile().mkdirs();
        request.setCharacterEncoding("UTF-8");
        uploadFileParts(request, uploadDir);
        response.sendRedirect("index.html");
    }

    private void uploadFileParts(HttpServletRequest request, Path uploadDir) throws IOException, ServletException {
        String dir = uploadDir.toAbsolutePath().toString();
        String fileName;
        for (Part part : request.getParts()) {
            fileName = getFileName(part);
            part.write(Path.of(dir, fileName).toString());
        }
    }

    /**
     * Utility method to get file name from HTTP header content-disposition
     */
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String token = Arrays.stream(contentDisp.split(";"))
                .filter(s -> s.trim().startsWith("filename"))
                .findFirst().orElse("");
        return "".equals(token)
                ? ""
                : token.substring(token.indexOf("=") + 2, token.length() - 1);
    }

    private void writeFileToOutputStream(HttpServletResponse response, Path file) throws IOException {
        try (InputStream fileStream = new FileInputStream(file.toFile().getAbsolutePath());
             ServletOutputStream out = response.getOutputStream()
        ) {
            byte[] bufferData = new byte[1024];
            int read;
            while ((read = fileStream.read(bufferData)) != -1) {
                out.write(bufferData, 0, read);
            }
            out.flush();
        }
    }

    private Path getFile(ServletContext context, String fileName) throws NoSuchFileException {
        Path file = Paths.get(context.getRealPath(""), STORE_DIR, fileName);
        if (!file.toFile().exists()) {
            throw new NoSuchFileException("File not found");
        }
        return file;
    }

    private void setDownloadResponseParameters(HttpServletResponse response, ServletContext context, Path file) throws UnsupportedEncodingException {
        response.setCharacterEncoding("UTF8");
        String mime = context.getMimeType(file.toAbsolutePath().toString());
        response.setContentType(mime != null ? mime : "application/octet-stream");
        String fileNameEncoded = URLEncoder.encode(file.getFileName().toString(), "UTF8");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileNameEncoded));
                response.setContentLength((int) file.toFile().length());
    }
}