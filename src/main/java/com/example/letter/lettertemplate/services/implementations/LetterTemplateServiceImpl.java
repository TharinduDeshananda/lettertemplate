package com.example.letter.lettertemplate.services.implementations;

import com.example.letter.lettertemplate.entities.LetterTemplate;
import com.example.letter.lettertemplate.services.LetterTemplateService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.internal.bulk.DeleteRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LetterTemplateServiceImpl implements LetterTemplateService {

    @Autowired
    @Qualifier("cmongoTemplate")
    MongoTemplate template;

    @Override
    public LetterTemplate getTemplateById(String templateId) {
        LetterTemplate letterTemplate = template.findById(new ObjectId(templateId),LetterTemplate.class);
        return letterTemplate;
    }

    @Override
    public LetterTemplate saveTemplate(LetterTemplate letterTemplate) {
        return template.save(letterTemplate);

    }

    @Override
    public boolean deleteTemplate(String templateId) {
        DeleteResult result = template.remove(new Query(Criteria.where("id").is(templateId)));
       return result.wasAcknowledged();
    }

    @Override
    public LetterTemplate updateTemplate(LetterTemplate letterTemplate) {
        return template.save(letterTemplate);
    }

    @Override
    public List<LetterTemplate> getAllTemplates() {
        List<LetterTemplate> templates = template.findAll(LetterTemplate.class);
        return templates;
    }

    @Override
    public List<LetterTemplate> getAllTemplatesPaged(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page,pageSize);
        Query query = new Query();
        query.with(pageRequest);
        List<LetterTemplate> templates = template.find(query,LetterTemplate.class);
        return templates;
    }

    @Override
    public LetterTemplate saveTemplate(String templateString,String templateName,String templateDesc) {
        LetterTemplate letterTemplate = new LetterTemplate();
        letterTemplate.setTemplateContent(templateString);
        letterTemplate.setTemplateName(templateName);
        letterTemplate.setTemplateDescription(templateDesc);
        letterTemplate = template.save(letterTemplate);
        return letterTemplate;
    }


}
