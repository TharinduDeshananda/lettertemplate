package com.example.letter.lettertemplate;


import com.example.letter.lettertemplate.contentHandlers.HtmlContentHandler;
import com.example.letter.lettertemplate.contentHandlers.TemplateAttribute;
import com.example.letter.lettertemplate.contentHandlers.TemplateFileHandler;
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



import java.util.HashMap;
import java.util.List;



@Controller
public class MainController {

    @Autowired
    private TemplateFileHandler fileHandler;


    //get to the home page. displays existing letter templates to the user to choose.
    @GetMapping("/")
    public String homePage(Model model){
        return "newHome";
    }

    @RequestMapping("/crf")
    @ResponseBody
    public ResponseEntity createAFile(){
        boolean state = fileHandler.createLetterTemplate("file1.html","<p>Hello World!!!</P>");
        return ResponseEntity.ok(state);
    }

    @RequestMapping("/gf")
    @ResponseBody
    public ResponseEntity getAllFiles(){

        List<String> files = fileHandler.getAllHtmlFileNames();
        return ResponseEntity.ok(files);
    }

    @RequestMapping("/gc")
    @ResponseBody
    public ResponseEntity getContent(){

        String content = fileHandler.getHtmlContentByFileName("file1");
        return ResponseEntity.ok(content);
    }



    @PostMapping("/handleNewContent")
    public String handleNewContent(@RequestParam("htmlContent") String htmlContent, Model model){
        //htmlContent = HtmlContentHandler.replaceHashSymbols(htmlContent);
        htmlContent = HtmlContentHandler.insertTextAreaToString(htmlContent);
        model.addAttribute("templateContent",htmlContent);
        return "newTemplateView";
    }

    @PostMapping("/handlnewSubmitForm")
    public String handlnewSubmitForm(@RequestParam HashMap<String,String> map,Model model){
        Document doc = Jsoup.parse(map.get("hiddenContent"));
        Elements textAreas = doc.select("textarea");
        for(Element element:textAreas){
            String inputName = element.attr("name");
            element.replaceWith(new TextNode(map.get(inputName)));
        }

        //
        model.addAttribute("templateContent",doc.html());

        return "ViewEditedTemplate";
    }






//    public List<LetterTemplateDto> letterListToDtoList(List<LetterTemplate> letters){
//        return letters.stream().map(letter->modelMapper.map(letter,LetterTemplateDto.class)).collect(Collectors.toList());
//    }
//
//    public LetterTemplateDto letterToLetterDto(LetterTemplate letter){
//        return modelMapper.map(letter,LetterTemplateDto.class);
//    }

}


