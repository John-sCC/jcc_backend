package com.nighthawk.spring_portfolio.mvc.imagerec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/image")
public class ImageApiController {

    @Autowired
    private OCRService ocrService;

    @Value("${java.io.tmpdir}")
    private String tempUploadDir;

    private String uploadDir = "src/main/java/com/nighthawk/spring_portfolio/mvc/imagerec/StoredImages";

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestPart("image") MultipartFile file) {
        try {
            // Create the temporary upload directory if it doesn't exist
            File tempDirectory = new File(tempUploadDir);
            if (!tempDirectory.exists()) {
                tempDirectory.mkdirs();
            }

            // Save the file to the temporary upload directory
            String tempFilePath = tempUploadDir + File.separator + file.getOriginalFilename();
            file.transferTo(new File(tempFilePath));

            // Move the file to the final destination
            String finalFilePath = uploadDir + File.separator + file.getOriginalFilename();
            new File(tempFilePath).renameTo(new File(finalFilePath));
            
            return ResponseEntity.ok(ocrService.performOcr(finalFilePath));
            // return ResponseEntity.ok("Hi");

        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload file");
        }
    }
}