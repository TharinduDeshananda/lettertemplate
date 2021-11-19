package com.example.letter.lettertemplate.contentHandlers;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STLexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlContentHandler {

    public static String replaceFormContent(String originalContent, Map<String,String> map){
        ST template = new ST(originalContent,'$','$');
        List<TemplateAttribute> attrList = getAttributeList(template);

        for(TemplateAttribute attr : attrList){
            String inpuTName = attr.getAttributeName();
            //template.add(attr.getInitName(),map.get(inpuTName));
            template.add(attr.getInitName(),"<span class='fff'>"+map.get(inpuTName) +"</span>");
        }


        return template.render();
    }

    public static String insertTextAreaToString(String htmlContent){

        ST template = new ST(htmlContent,'$','$');
        List<TemplateAttribute> attributeList = getAttributeList(template);
        for (TemplateAttribute attribute: attributeList){
            template.add(attribute.getInitName()
                    , "<textarea name=\""+ attribute.getAttributeName()
                            +"\" placeholder=\""+attribute.getPlaceHolder()+"\" cols=\""+attribute.getColsCount()+"\" rows=\""+ attribute.getRowsCount()
                            +"\" style=\"vertical-align: text-top; display:inline-block; overflow: hidden; resize: node;\"></textarea>");
        }

        return template.render();
    }

    public static String replaceHashSymbols(String htmlContent){
        StringBuilder htmlString = new StringBuilder(htmlContent);

        Pattern pattern = Pattern.compile("#{3,}");
        Matcher matcher = pattern.matcher(htmlString);
        while(matcher.find()){
            int length = matcher.group().length()>5? matcher.group().length():5;
            String fieldName = RandomStringUtils.random(length,true,false);
            htmlString.replace(matcher.start(),matcher.end(),"$"+fieldName+"-1-"+length+"$");
        }
        return htmlString.toString();
    }


    public static List<TemplateAttribute> getAttributeList(ST template){
        List<TemplateAttribute> expressions = new ArrayList<>();
        TokenStream tokens = template.impl.tokens;
            for (int i = 0; i < tokens.range(); i++) {
                Token token = tokens.get(i);
                if (token.getType() == STLexer.ID) {
                    expressions.add(new TemplateAttribute(token.getText(),token.getText().split("-")[0],
                            Integer.parseInt(token.getText().split("-")[1]),
                            Integer.parseInt(token.getText().split("-")[2]),token.getText().split("-")[3]));
                }
            }
        return expressions;
    }

    public static String addPaddingDiv(String htmlContent,int margin_v,int margin_h){
        Element divElement = new Element("div");
        Document doc = Jsoup.parse(htmlContent);

        for(Element child:doc.body().children()){
            divElement.appendChild(child);
        }
        //divElement.attr("style","padding: "+margin_v+"rem "+margin_h+"rem;");
        divElement.attr("style","margin: "+margin_v+"rem "+margin_h+"rem;");
        System.out.println(Jsoup.parse(divElement.toString()));
        return divElement.toString();
    }

    public static String generateForm(String htmlContentWithAttributeList){
        ST template = new ST(htmlContentWithAttributeList,'$','$');
        List<TemplateAttribute> attrList = getAttributeList(template);
        Document doc = Jsoup.parse("<div id='mainDiv'></div>");
        doc.getElementById("mainDiv");
        for(TemplateAttribute attr: attrList){
            doc.getElementById("mainDiv").append("<label>"+attr.getPlaceHolder()+"  : </label><br>" +
                    "<textarea name='"+attr.getAttributeName()+"' cols='"+attr.getColsCount()+"' rows='"+attr.getRowsCount()+"'></textarea><br><br>");
        }
        return doc.body().html();
    }

    public static Document makeElementUnbreakable(Document doc){

        doc.head().append("<style type=\"text/css\">\n" +
                "      img   {\n" +
                "         page-break-inside:avoid; \n" +
                "         page-break-after:auto;\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "      .fff  {\n" +
                "\n" +
                "\n" +
                "         page-break-inside:avoid; \n" +
                "         page-break-after:auto;\n" +
                "\n" +
                "      }\n" +
                "\n" +
                "      .specDiv {\n" +
                "\n" +
                "         background-color: lavender;\n" +
                "         border: 2px solid red;\n" +
                "\n" +
                "         margin: 2px 2px;\n" +
                "      }\n" +
                "</style>");
        return doc;
    }

}
