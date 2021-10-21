package com.example.letter.lettertemplate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailUser {
    String userName;
    String userEmail;
    String emailBody;
    String emailSubject;
    String emailTitle;
}
