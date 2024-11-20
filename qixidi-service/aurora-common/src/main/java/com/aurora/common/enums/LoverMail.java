package com.aurora.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LoverMail {
    MailData(1, "");
    private Integer key;
    private String Mail;

    public void setMail(String mail) {
        Mail = mail;
    }
}
