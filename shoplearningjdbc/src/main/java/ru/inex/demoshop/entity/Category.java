package ru.inex.demoshop.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    //private Product[] products;
    //!!! Дописать getеры и setеры
}
