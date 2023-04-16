package ru.inex.demoshop.dto;

import lombok.Data;

@Data
public class UserOrder {
    //private String name;
    private Integer productId;
    private Integer amount;
}
