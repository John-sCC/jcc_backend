package com.nighthawk.spring_portfolio.mvc.imagerec;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class OCRService {
    
    public String performOcr(String imagePath) {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("eng");
        tesseract.setDatapath("./tessdata");

        tesseract.setTessVariable("tessedit_char_whitelist", "0123456789, ");

        try {
            File imageFile = new File(imagePath);
            String result = tesseract.doOCR(imageFile);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during OCR processing: " + e.getMessage(); // Include the exception message
        }
    }
}
