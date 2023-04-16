package ru.inex.demoshop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer orderId;
    private Integer mark;

    public Rating(Integer productId, Integer userId, Integer orderId, Integer mark) {
        this.productId = productId;
        this.userId = userId;
        this.orderId = orderId;
        this.mark = mark;
}
}
