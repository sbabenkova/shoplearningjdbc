package ru.inex.demoshop.service;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShopCreateTables {
    private final JdbcTemplate jdbcTemplate;

    public void createTables() {
        String sqlProduct = "CREATE TABLE IF NOT EXISTS product (\n" +
                "\tid serial NOT null PRIMARY KEY,\n" +
                "\tcategory int4 NULL,\n" +
                "\tcount int4 NULL,\n" +
                "\t\"name\" varchar(255) NULL,\n" +
                "\tprice float8 NULL,\n" +
                "\trating float8 NULL\n" +
                //"\t--CONSTRAINT product_pkey PRIMARY KEY (id)\n" +
                ");";
        //jdbcTemplate.execute("DROP TABLE product");
        jdbcTemplate.execute(sqlProduct);

        String sqlUser = "CREATE TABLE IF NOT EXISTS public.user (\n" +
                "\tid serial NOT null PRIMARY KEY,\n" +
                "\t\"name\" varchar(255) NULL\n" +
                //"\tCONSTRAINT user_pkey PRIMARY KEY (id)\n" +
                ");";
        //jdbcTemplate.execute("DROP TABLE user");
        jdbcTemplate.execute(sqlUser);

        String sqlOrder = "CREATE TABLE IF NOT EXISTS public.order (\n" +
                "\tid serial NOT NULL,\n" +
                "\tamount int4 NULL,\n" +
                "\tproductid int4 NULL,\n" +
                "\tuserid int4 NULL,\n" +
                "\tCONSTRAINT order_pkey PRIMARY KEY (id)\n" +
                ");";
        jdbcTemplate.execute(sqlOrder);

        String sqlCategory = "CREATE TABLE IF NOT EXISTS public.category (\n" +
                "\tid serial NOT NULL,\n" +
                "\t\"name\" varchar(255) NULL,\n" +
                "\tproducts bytea NULL,\n" +
                "\tCONSTRAINT category_pkey PRIMARY KEY (id)\n" +
                ");";
        jdbcTemplate.execute(sqlCategory);

        String sqlRating = "\n" +
                "CREATE TABLE IF NOT EXISTS public.rating (\n" +
                "\tid serial NOT NULL,\n" +
                "\tmark int4 NULL,\n" +
                "\torder_id int4 NULL,\n" +
                "\tproduct_id int4 NULL,\n" +
                "\tuser_id int4 NULL,\n" +
                "\tCONSTRAINT rating_pkey PRIMARY KEY (id)\n" +
                ");";
        jdbcTemplate.execute(sqlRating);
    }

}
