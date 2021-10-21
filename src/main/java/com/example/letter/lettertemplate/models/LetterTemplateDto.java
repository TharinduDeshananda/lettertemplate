package com.example.letter.lettertemplate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LetterTemplateDto {
    private String id;
    private String templateName;
    private String templateDescription;
    private Date dateCreated;

}
