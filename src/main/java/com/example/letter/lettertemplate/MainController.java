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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.stringtemplate.v4.ST;


import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;



@Controller
public class MainController {

    @Autowired
    private TemplateFileHandler fileHandler;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("/")
    public String homePage(Model model){
        List<String> temlplateNames = fileHandler.getAllHtmlFileNames();
        model.addAttribute("templateNames",temlplateNames);
        return "newHome";
    }

    @PostMapping("/handleNewContent")
    public String handleNewContent(@RequestParam("htmlContent") String htmlContent
            ,@RequestParam("margin_v")int margin_v,@RequestParam("margin_h")int margin_h,
                                   Model model){
        //htmlContent = HtmlContentHandler.replaceHashSymbols(htmlContent);
        htmlContent = HtmlContentHandler.addPaddingDiv(htmlContent,margin_v,margin_h);
        //htmlContent = HtmlContentHandler.insertTextAreaToString(htmlContent);
        System.out.println(margin_h);
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

        String FormhtmlContent = HtmlContentHandler.generateForm(htmlContent);
        model.addAttribute("originalContent",htmlContent);
        model.addAttribute("templateContent",FormhtmlContent);
        return "ViewEditedTemplate";
    }

    @GetMapping("/editTemplate/{templateName}")
    public String editTemplate(@PathVariable("templateName")String templateName,Model model){
        String htmlContent = fileHandler.getHtmlContentByFileName(FilenameUtils.getBaseName(templateName));
        model.addAttribute("templateContent",htmlContent);
        model.addAttribute("templateFileName",templateName);
        return "editTemplate";
    }

    @RequestMapping("/deleteTemplate/{templateName}")
    public String deleteTemplate(@PathVariable("templateName")String templateName){
        fileHandler.deleteTemplateFileByName(templateName);
        return "redirect:/";
    }

    @PostMapping("/saveEditedTamplate")
    public String saveEditedTemplate(@RequestParam("templateFileName") String templateFileName,
                                     @RequestParam("htmlContent")String htmlContent)throws Exception{
        //htmlContent = HtmlContentHandler.insertTextAreaToString(htmlContent);
        boolean status = fileHandler.replaceTemplateFileByName(templateFileName,htmlContent);
        if(!status)throw new Exception("Non existing template file");
        return "redirect:/";
    }

    @PostMapping("/handleContent")
    public String handlnewSubmitFormContent(@RequestParam HashMap<String,String> map,Model model){

        String originalContent = map.get("originalContent");
        String replacedContent = HtmlContentHandler.replaceFormContent(originalContent,map);

        Document doc = Jsoup.parse(replacedContent);
//        doc.head().append("<style type=\"text/css\">\n" +
//                "      img   {\n" +
//                "         page-break-inside:avoid; \n" +
//                "         page-break-after:auto;\n" +
//                "\n" +
//                "      }\n" +
//                "\n" +
//                "      .fff  {\n" +
//                "\n" +
//                "\n" +
//                "         page-break-inside:avoid; \n" +
//                "         page-break-after:auto;\n" +
//                "\n" +
//                "      }\n" +
//                "</style>");

//        Elements textAreas = doc.select("textarea");
//        for(Element element:textAreas){
//            String inputName = element.attr("name");
//            element.replaceWith(new TextNode(map.get(inputName)));
//        }

        model.addAttribute("templateContent",doc.html());

        return "showContent";
    }

    @PostMapping("/mailme")
    public String mailMe(@RequestParam("hiddenContent")String htmlContent, Model model, HttpSession session){

        //model.addAttribute("htmlContent",htmlContent);
        session.setAttribute("htmlContent",htmlContent);
        return "mailMeUserFormPage";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(HttpServletResponse response, HttpServletRequest request
                            // @RequestParam("userName")String userName,@RequestParam("userEmail")String email
    ){
        response.setHeader("Content-Type","application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=mydoc.pdf");
        String htmlContent = (String)request.getSession().getAttribute("htmlContent");
        try{


            File tempFile = File.createTempFile("MyDoc","pdf");
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            Document doc = Jsoup.parse(htmlContent);
            doc = HtmlContentHandler.makeElementUnbreakable(doc);


            htmlContent = doc.html();
            System.out.println(htmlContent);
            HtmlHandler.htmlToPdf(htmlContent,null,outputStream);

            String email=request.getParameter("email");
            String name = request.getParameter("name");



            MimeMessage message= mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            String content=

                    "<h3>Hello " + name+"</h3>" +"<b>This is a test Mail</b><br>" +
                            "here we attach your pdf...."+
                            "<br><hr>" ;

            //helper.setFrom("chathurikatestapp@gmail.com","Letter Generating Tool");
            helper.setFrom("tdeshananda@gmail.com","Letter Generating Tool");
            helper.setTo(email);
            helper.setText(content,true);
            helper.setSubject("letter generator");
            //helper.addAttachment("MyDoc.pdf",response.getHeader());
            helper.addAttachment("MyDoc.pdf",tempFile);

            mailSender.send(message);
            System.out.println("Mail Sent");







        }catch(Exception e){
            System.out.println(e);
            return "redirect:/";
        }

        return "redirect:/";
    }

}


