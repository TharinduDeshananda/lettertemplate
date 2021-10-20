package com.example.letter.lettertemplate;


import com.example.letter.lettertemplate.entities.LetterTemplate;
import com.example.letter.lettertemplate.htmlhandler.HtmlHandler;
import com.example.letter.lettertemplate.pdfhandler.PdfHandler;
import com.example.letter.lettertemplate.services.LetterTemplateService;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.FileUtils;

import org.dom4j.rule.Mode;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;


@Controller
public class MainController {

    @Autowired
    LetterTemplateService letterTemplateService;

    @GetMapping("/")
    public String homePage(Model model){
        List<LetterTemplate> templateList = letterTemplateService.getAllTemplates();
        model.addAttribute("templates",templateList);
        return "homePage";
    }

    @GetMapping("/templateUpload")
    public String fileUploadPAge(){
        return "fileUploadPage";
    }

    @PostMapping("/templateUploadHandle")
    public String handleFile(@RequestParam("ppfile") MultipartFile file, Model model){
        String content = "";

        try{
            
            File file1 = File.createTempFile("testFile","pdf");
            FileUtils.writeByteArrayToFile(file1,file.getBytes());
            content = FileUtils.readFileToString(file1);
            model.addAttribute("textContent",content);
        }catch(Exception e){
            System.out.println(e);
        }


        return "fileView";
    }

    @PostMapping("/createTemplate")
    public void handleChanges(@RequestParam(value = "letterContent") String htmlLetterContent ,
                              @RequestParam(value = "textContentBack")String textContentBack,
                              @RequestParam("templateName")String templateName,
                              @RequestParam("templateDesc")String templateDesc,
                              HttpServletResponse response, Model model){
        LetterTemplate letterTemplate = letterTemplateService.saveTemplate(htmlLetterContent,templateName,templateDesc);
        model.addAttribute("template",letterTemplate);

        //HtmlHandler.htmlToPdf(htmlLetterContent,textContentBack,response);

    }

    @PostMapping("/deleteTemplate")
    public String deleteTemplate(@RequestParam("templateId")String templateId,Model model){
        boolean deleteStatus = letterTemplateService.deleteTemplate(templateId);
        model.addAttribute("deleteStatus",deleteStatus);
        return "/";
    }
}


