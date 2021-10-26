package com.example.letter.lettertemplate.contentHandlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TemplateAttribute {
    private String initName;
    private String attributeName;
    private int rowsCount;
    private int colsCount;
    private String placeHolder="Enter details here..";

}
