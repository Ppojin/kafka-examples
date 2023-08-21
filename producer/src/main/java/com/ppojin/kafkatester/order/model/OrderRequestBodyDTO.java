package com.ppojin.kafkatester.order.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class OrderRequestBodyDTO {
    @Getter @Setter
    private List<OrderMessageDTO> messages;
}
