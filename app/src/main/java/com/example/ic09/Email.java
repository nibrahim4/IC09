package com.example.ic09;

import java.io.Serializable;

public class Email implements Serializable {
    public String subject, message,senderFName,senderLName;

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String date;
}
