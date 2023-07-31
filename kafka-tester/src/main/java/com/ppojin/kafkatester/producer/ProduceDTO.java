package com.ppojin.kafkatester.producer;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ProduceDTO {
    @Getter @Setter
    private String acks = "0";

    @Getter @Setter
    private List<MessageDTO> messages;
}
