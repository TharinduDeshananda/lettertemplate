package com.example.letter.lettertemplate;


import com.example.letter.lettertemplate.contentHandlers.HtmlContentHandler;
import com.example.letter.lettertemplate.contentHandlers.TemplateAttribute;
import com.example.letter.lettertemplate.contentHandlers.TemplateFileHandler;
import com.example.letter.lettertemplate.htmlhandler.HtmlHandler;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.stringtemplate.v4.ST;


import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;



@Controller
public class MainController {

    @Autowired
    private TemplateFileHandler fileHandler;

    @GetMapping("/")
    public String homePage(Model model){
        List<String> temlplateNames = fileHandler.getAllHtmlFileNames();
        model.addAttribute("templateNames",temlplateNames);
        return "newHome";
    }

    @PostMapping("/handleNewContent")
    public String handleNewContent(@RequestParam("htmlContent") String htmlContent, Model model){
        //htmlContent = HtmlContentHandler.replaceHashSymbols(htmlContent);
        htmlContent = HtmlContentHandler.insertTextAreaToString(htmlContent);
        model.addAttribute("templateContent",htmlContent);
        return "newTemplateView";
    }

    @PostMapping("/saveTemplateFile")
    public String saveHtmlFile(@RequestParam("hiddenContent")String htmlContentWithTextArea,
                               @RequestParam("templateName")String templateName){
        fileHandler.createLetterTemplate(templateName+".html",htmlContentWithTextArea);
        return "redirect:/";
    }

    @GetMapping("/getTemplate/{templateName}")
    public String getTemplate(@PathVariable("templateName")String templateName,Model model){
        String htmlContent = fileHandler.getHtmlContentByFileName(FilenameUtils.getBaseName(templateName));
        model.addAttribute("templateContent",htmlContent);
        return "ViewEditedTemplate";
    }

    @PostMapping("/handleContent")
    public String handlnewSubmitFormContent(@RequestParam HashMap<String,String> map,Model model){
        Document doc = Jsoup.parse(map.get("hiddenContent"));
        Elements textAreas = doc.select("textarea");
        for(Element element:textAreas){
            String inputName = element.attr("name");
            element.replaceWith(new TextNode(map.get(inputName)));
        }
        model.addAttribute("templateContent",doc.html());

        return "showContent";
    }

    @PostMapping("/mailme")
    public String mailMe(@RequestParam("hiddenContent")String htmlContent,Model model){

        model.addAttribute("htmlContent",htmlContent);
        return "mailMeUserFormPage";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(HttpServletResponse response,@RequestParam("htmlContent")String htmlContent,
                            @RequestParam("userName")String userName,@RequestParam("userEmail")String email){
        response.setHeader("Content-Type","application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=mydoc.pdf");
        try{
            HtmlHandler.htmlToPdf(htmlContent,null,response.getOutputStream());
        }catch(Exception e){
            System.out.println(e);
            return "redirect:/";
        }

        return "redirect:/";
    }
}


