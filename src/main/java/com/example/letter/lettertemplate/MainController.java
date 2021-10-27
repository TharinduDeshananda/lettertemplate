package com.example.letter.lettertemplate;


import com.example.letter.lettertemplate.contentHandlers.HtmlContentHandler;
import com.example.letter.lettertemplate.contentHandlers.TemplateAttribute;
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
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.io.FileUtils;

import org.dom4j.rule.Mode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return "newHome";
    }

    @PostMapping("/handleNewContent")
    public String handleNewContent(@RequestParam("htmlContent") String htmlContent, Model model, RedirectAttributes redirectAttributes){
        ST template = new ST(htmlContent,'$','$');
        List<TemplateAttribute> attributeList = HtmlContentHandler.getAttributeList(template);
        redirectAttributes.addFlashAttribute("attributeList",attributeList);
        redirectAttributes.addFlashAttribute("originalHtmlContent",htmlContent);
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


