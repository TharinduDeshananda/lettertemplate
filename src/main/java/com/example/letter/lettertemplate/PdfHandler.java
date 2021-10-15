package com.example.letter.lettertemplate;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.htmlconverter.LoadHtmlType;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.spire.pdf.htmlconverter.qt.Size;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class PdfHandler {

    public static void pdfToHtml(File file, OutputStream outStream){
        try{
            PdfDocument pdfDoc = new PdfDocument();
            pdfDoc.loadFromStream(new FileInputStream(file));
            pdfDoc.getConvertOptions().setPdfToHtmlOptions(true,true);
            pdfDoc.saveToStream(outStream, FileFormat.HTML);
            System.out.println("Done outputting to stream");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public static void htmlToPdf(String htmlContent,OutputStream outputStream){

        HtmlConverter.setPluginPath("C:\\Users\\tdesh\\Desktop\\New folder\\plugins-windows-x64");
        HtmlConverter.convert(htmlContent, outputStream, true, 1000000, new Size(600f, 900f), new PdfMargins(0), LoadHtmlType.Source_Code);




    }

}
