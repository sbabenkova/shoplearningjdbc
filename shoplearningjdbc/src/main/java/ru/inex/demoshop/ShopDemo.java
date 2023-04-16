package ru.inex.demoshop;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Data
@AllArgsConstructor
public class ShopDemo {
    @Autowired
    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        System.out.println(jdbcTemplate);
        /*ShopService shopService = null;
        User user = new User(1,"name1");
        User user = new User();
        //user.setId(2);
        user.setName("name11");
        System.out.println(user.toString());

        shopService.addUser(user);*/
        //Order order = new Order(1,1,2);
        //System.out.println(order.toString());
        //ShopService shopService = null;

        //JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String sql = "CREATE TABLE product (\n" +
                "\tid serial NOT null PRIMARY KEY,\n" +
                "\tcategory int4 NULL,\n" +
                "\tcount int4 NULL,\n" +
                "\t\"name\" varchar(255) NULL,\n" +
                "\tprice float8 NULL,\n" +
                "\trating float8 NULL\n" +
                //"\t--CONSTRAINT product_pkey PRIMARY KEY (id)\n" +
                ");";
        jdbcTemplate.execute(sql);

    }

}