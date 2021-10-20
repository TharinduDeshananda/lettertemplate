package com.example.letter.lettertemplate.htmlhandler;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

public class HtmlHandler {

    public static void htmlToPdf(String htmlLetterContent,String textContentBack, HttpServletResponse response){
        try{

            response.setHeader("Content-Type","application/pdf");
            response.setHeader("Content-Disposition","attachment; filename=mydoc.pdf");

            htmlLetterContent = htmlLetterContent.replaceAll("&"+"nbsp;", " ");
            htmlLetterContent = htmlLetterContent.replaceAll(String.valueOf((char) 160), " ");

            textContentBack = textContentBack.replaceAll("&"+"nbsp;", " ");
            textContentBack = textContentBack.replaceAll(String.valueOf((char) 160), " ");

            String xhtml= Jsoup.parse(htmlLetterContent).html();

            String htmlString = xhtml;



            PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
            ConverterProperties converterProperties = new ConverterProperties();

            FontProvider fontProvider = new DefaultFontProvider(true,true,true);
            String[] fonts = {
                    "src/main/resources/fonts/comic-sans-ms.ttf",
                    "src/main/resources/fonts/arial.ttf",
                    "src/main/resources/fonts/Garamond Regular.ttf",
                    "src/main/resources/fonts/Georgia Regular font.ttf",
                    "src/main/resources/fonts/ComicSansMS3.ttf"

            };
            fontProvider.addDirectory("src/main/resources/fonts");


            converterProperties.setFontProvider(fontProvider);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);

            //For setting the PAGE SIZE
            pdfDocument.setDefaultPageSize(new PageSize(PageSize.A3));

            Document document = HtmlConverter.convertToDocument(htmlString, pdfDocument, converterProperties);
            document.close();

        }catch(Exception e){
            System.out.println(e);
        }
    }
}
