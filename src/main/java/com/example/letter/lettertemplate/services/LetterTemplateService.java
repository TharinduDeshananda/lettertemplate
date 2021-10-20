package com.example.letter.lettertemplate.services;

import com.example.letter.lettertemplate.entities.LetterTemplate;

import java.util.List;

public interface LetterTemplateService {

    LetterTemplate saveTemplate(LetterTemplate template);
    boolean deleteTemplate(String templateId);
    LetterTemplate updateTemplate(LetterTemplate template);
    List<LetterTemplate> getAllTemplates();
    List<LetterTemplate> getAllTemplatesPaged(int page,int pageSize);
    LetterTemplate saveTemplate(String templateString,String templateName,String templateDescr);
    LetterTemplate getTemplateById(String templateId);
}
