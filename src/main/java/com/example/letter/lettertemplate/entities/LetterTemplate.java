package com.example.letter.lettertemplate.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document
public class LetterTemplate {
    @Id
    private String id;
    private String templateName;
    private String templateDescription;
    private String templateContent;
    @CreatedDate
    private Date dateCreated;


}
