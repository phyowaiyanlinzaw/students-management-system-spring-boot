package com.ojt.StudentsBoot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {

    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
