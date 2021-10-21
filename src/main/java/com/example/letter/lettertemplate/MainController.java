package com.example.letter.lettertemplate;


import com.example.letter.lettertemplate.entities.LetterTemplate;
import com.example.letter.lettertemplate.htmlhandler.HtmlHandler;
import com.example.letter.lettertemplate.models.EmailUser;
import com.example.letter.lettertemplate.models.LetterTemplateDto;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class MainController {

    @Autowired
    LetterTemplateService letterTemplateService;

    @Autowired
    ModelMapper modelMapper;

    //get to the home page. displays existing letter templates to the user to choose.
    @GetMapping("/")
    public String homePage(Model model){
        List<LetterTemplate> templateList = letterTemplateService.getAllTemplates();

        model.addAttribute("templates",letterListToDtoList(templateList));//only name, date and decription sent not content
        return "homePage";
    }

    //to the file upload choose page
    @GetMapping("/templateUpload")
    public String fileUploadPAge(){
        return "fileUploadPage";
    }

    //handle the uploaded html letter file. get the html content and view it in front end
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

    //save html content as htmltemplate objects in database. redirect to home page at end
    @PostMapping("/createTemplate")
    public String handleChanges(@RequestParam(value = "letterContent") String htmlLetterContent ,
                              @RequestParam(value = "textContentBack")String textContentBack,
                              @RequestParam(value = "templateName", required = false)String templateName,
                              @RequestParam(value = "templateDesc",required = false)String templateDesc,
                              HttpServletResponse response, Model model){
        LetterTemplate letterTemplate = letterTemplateService.saveTemplate(htmlLetterContent,"temp name","temp desc");

        model.addAttribute("template",letterTemplate);

        return "redirect:/";
        //HtmlHandler.htmlToPdf(htmlLetterContent,textContentBack,response);
    }

    //************************** User parts *********************************

    //View user selected template which is selected in home page
    @RequestMapping("/viewTemplate")
    public String viewSelectedTemplate(@RequestParam("templateId")String templateId,Model model){

        LetterTemplate letterTemplate = letterTemplateService.getTemplateById(templateId);
        model.addAttribute("letter",letterTemplate);
        return "letterView";
    }

    //handle user edited letter and send email to user
    @PostMapping("/handleChosenTemplate")
    public String handleChosenTemplate(@RequestParam(value = "letterContent") String htmlLetterContent,
                                       @RequestParam(value = "textContentBack",required = false)String textContentBack,
                                       @ModelAttribute EmailUser emailUser){
        //send email from here
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        HtmlHandler.htmlToPdf(htmlLetterContent,textContentBack,outStream);
        try{
            File userSentFile = File.createTempFile("userFile","pdf");//file to send to the user
            FileUtils.writeByteArrayToFile(userSentFile,outStream.toByteArray());


        }catch(Exception e){
            System.out.println(e);
        }
        return "redirect:/";
    }

    @PostMapping("/deleteTemplate")
    public String deleteTemplate(@RequestParam("templateId")String templateId,Model model){
        boolean deleteStatus = letterTemplateService.deleteTemplate(templateId);
        model.addAttribute("deleteStatus",deleteStatus);
        return "/";
    }


    public List<LetterTemplateDto> letterListToDtoList(List<LetterTemplate> letters){
        return letters.stream().map(letter->modelMapper.map(letter,LetterTemplateDto.class)).collect(Collectors.toList());
    }

    public LetterTemplateDto letterToLetterDto(LetterTemplate letter){
        return modelMapper.map(letter,LetterTemplateDto.class);
    }

}


