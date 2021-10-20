package com.example.letter.lettertemplate.wordhandler;

import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.AbstractHtmlExporter;
import org.docx4j.convert.out.html.HtmlExporterNG2;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WordHandler {

    public static String wordToHtml(File doc){
            try{
                String content="";
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
                return content;
            }catch(Exception e){
                System.out.println(e);
            }
            return null;
    }


    private static void htmlToWord(String htmlLetterContent, String textContentBack, HttpServletResponse response){
        try{
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
            System.out.println(e);
        }

    }

}
