package com.example.letter.lettertemplate.htmlhandler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HtmlHandler {

    public static void htmlToPdf(String htmlLetterContent,String textContentBack, OutputStream outputStream){
        try{
            ByteArrayInputStream htmlContentIs = new ByteArrayInputStream(htmlLetterContent.getBytes(StandardCharsets.UTF_8));

            ByteArrayOutputStream ms = new ByteArrayOutputStream();
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(ms));
            pdfDocument.setDefaultPageSize(PageSize.A4);

            HtmlConverter.convertToPdf(htmlContentIs,pdfDocument);



            PdfDocument resultantDocument = new PdfDocument(new PdfWriter(outputStream));
            resultantDocument.setDefaultPageSize(PageSize.A4);

            pdfDocument = new PdfDocument(new PdfReader(new ByteArrayInputStream(ms.toByteArray())));
            for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
                PdfPage page = pdfDocument.getPage(i);
                PdfFormXObject formXObject = page.copyAsFormXObject(resultantDocument);
                PdfCanvas pdfCanvas = new PdfCanvas(resultantDocument.addNewPage());
                pdfCanvas.addXObject(formXObject);

            }
            pdfDocument.close();
            resultantDocument.close();


        }catch(Exception e){
            System.out.println(e);
        }
    }
}
