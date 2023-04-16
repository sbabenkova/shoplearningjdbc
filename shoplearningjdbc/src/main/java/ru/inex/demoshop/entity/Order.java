package ru.inex.demoshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer amount;

    public Order(Integer userId, Integer productId, Integer amount) {
        this.userId = userId;
        this.productId = productId;
        this.amount = amount;
    }


}

