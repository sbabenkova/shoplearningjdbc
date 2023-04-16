package ru.inex.demoshop.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderList {
    private String name;
    private List<UserOrder> orderList;
}
