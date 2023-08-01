package com.ppojin.kafkatester.tester.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class StringRequestBodyDTO {
    @Getter @Setter
    private String acks = "-1";

    @Getter @Setter
    private List<StringMessageDTO> messages;
}
