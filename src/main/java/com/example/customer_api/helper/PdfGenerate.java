package com.example.customer_api.helper;

import com.lowagie.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

@Service
@Slf4j
public class PdfGenerate {
    private Logger logger = LoggerFactory.getLogger(PdfGenerate.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pdf.directory}")
    private String pdfDirectory;

    public void generatePdfFile(String templateName, Map<String, Object> data,String pdfFilename) {
        Context context = new Context();
        context.setVariables(data);

       String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pdfDirectory+pdfFilename);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
            
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (DocumentException e) {
            logger.error(e.getMessage(), e);
        }
        
    }
}
