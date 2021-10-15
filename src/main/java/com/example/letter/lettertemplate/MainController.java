package com.example.letter.lettertemplate;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.docx4j.Docx4J;

import org.docx4j.XmlUtils;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;

import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.fit.pdfdom.PDFDomTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Controller
public class MainController {

    @GetMapping("/")
    public String homePage(){
        return "homePage";
    }

    @PostMapping("/fileHandler")
    public String handleFile(@RequestParam("ppfile") MultipartFile file, Model model){
        String content = "";

        try{
            
            File file1 = File.createTempFile("testFile","pdf");
            FileUtils.writeByteArrayToFile(file1,file.getBytes());
            File doc = file1;

            File logFile = new File("C:\\Users\\tdesh\\Desktop\\logFile.txt");
            FileWriter writer = new FileWriter(logFile);


//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            PdfHandler.pdfToHtml(doc,outStream);
//            writer.write(outStream.toString());
//            writer.flush();
//            writer.close();
//            model.addAttribute("textContent",outStream.toString());

            InputStream is = new FileInputStream(doc);
            WordprocessingMLPackage wordMLPackage = Docx4J.load(is);//************

            AbstractHtmlExporter exporter = new HtmlExporterNG2();//***********

            HTMLSettings htmlSettings = Docx4J.createHTMLSettings();
            htmlSettings.setWmlPackage(wordMLPackage);
            htmlSettings.setImageDirPath("/images/");
            htmlSettings.setImageTargetUri("/images/");

            boolean nestLists = true;
            if (nestLists) {
                SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
            } else {
                htmlSettings.getFeatures().remove(ConversionFeatures.PP_HTML_COLLECT_LISTS);
            }

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            Docx4J.toHTML(htmlSettings, outStream, Docx4J.FLAG_NONE);
            content = outStream.toString();


            model.addAttribute("textContent",content);

        }catch(Exception e){
            System.out.println(e);
        }


        return "fileView";
    }

    @PostMapping("/handleEdit")
    public void handleChanges(@RequestParam(value = "letterContent") String htmlLetterContent ,
                              @RequestParam(value = "textContentBack")String textContentBack,
                              HttpServletResponse response){

        try{


//            String xhtml= htmlLetterContent;
//            response.setHeader("Content-Type","application/pdf");
//            response.setHeader("Content-Disposition","attachment; filename=mydoc.pdf");
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            File logFile = new File("C:\\Users\\tdesh\\Desktop\\logFile.pdf");
//
//            PdfHandler.htmlToPdf(htmlLetterContent,outputStream);
//            FileWriter writer = new FileWriter(logFile);
//            writer.write(outputStream.toString(StandardCharsets.UTF_8));

            String xhtml= "";

            htmlLetterContent = htmlLetterContent.replaceAll("&"+"nbsp;", " ");
            htmlLetterContent = htmlLetterContent.replaceAll(String.valueOf((char) 160), " ");

            textContentBack = textContentBack.replaceAll("&"+"nbsp;", " ");
            textContentBack = textContentBack.replaceAll(String.valueOf((char) 160), " ");



            Document mainDoc = Jsoup.parse(textContentBack);
            Document changedSmallDoc = Jsoup.parse(htmlLetterContent);

            mainDoc.body().children().remove();
            mainDoc.body().appendChild(changedSmallDoc.getElementsByClass("document").first());



            xhtml = mainDoc.toString();
//            xhtml = changedSmallDoc.toString();
//            System.out.println(xhtml);




            Document tempDoc = Jsoup.parse(xhtml);
            tempDoc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            xhtml = tempDoc.toString();

            WordprocessingMLPackage docxOut = WordprocessingMLPackage.createPackage();
            NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
            docxOut.getMainDocumentPart().addTargetPart(ndp);
            ndp.unmarshalDefaultNumbering();

            XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(docxOut);
            XHTMLImporter.setHyperlinkStyle("Hyperlink");



            docxOut.getMainDocumentPart().getContent().addAll(
                    XHTMLImporter.convert(xhtml, null) );

            WordprocessingMLPackage wordMLPackage = docxOut;

            response.setHeader("Content-Type","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            response.setHeader("Content-Disposition","attachment; filename=mydoc.docx");
            wordMLPackage.save(response.getOutputStream());


        }catch(Exception e){
            System.out.println("Failed to handle edit "+e.toString());
        }
        finally {

        }

    }
}


