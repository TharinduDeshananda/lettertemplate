package com.example.letter.lettertemplate.contentHandlers;

import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.compiler.STLexer;

import java.util.ArrayList;
import java.util.List;

public class HtmlContentHandler {

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


}
