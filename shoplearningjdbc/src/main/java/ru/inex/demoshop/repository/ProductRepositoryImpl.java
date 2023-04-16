package ru.inex.demoshop.repository;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.dto.UserOrder;
import ru.inex.demoshop.entity.Product;

import java.util.List;
import java.util.Random;


@Repository
//@AllArgsConstructor
//public interface ProductRepositoryImpl extends CrudRepository<Product,Integer> {
public class ProductRepositoryImpl {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        //this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("product").withSchemaName("public").usingGeneratedKeyColumns("id");
    }

    public List<Product> getAllProducts(@Nullable String sortByName) {
        var result = jdbcTemplate.query("SELECT * FROM Product", new BeanPropertyRowMapper<>(Product.class));
        if (sortByName != null) {
            switch (sortByName) {
                case "asc" -> result = jdbcTemplate.query("SELECT * FROM Product order by name", new BeanPropertyRowMapper<>(Product.class));
                case "desc" -> result = jdbcTemplate.query("SELECT * FROM Product order by name desc", new BeanPropertyRowMapper<>(Product.class));
            }
        }
        return result;
    }

    /*public Product addProduct(Product product) {
        Product result;
        int n = jdbcTemplate.update("INSERT INTO Product(name, price, rating, count, category) VALUES (?,?,?,?,?)", product.getName(), product.getPrice(), product.getRating(), product.getCount(), product.getCategory());
        int k = jdbcTemplate.queryForObject("SELECT currval('product_id_seq')", Integer.class);
        System.out.println("n= " + n + "k= " + k);
        System.out.println("product =" + product);
        result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), k);
        System.out.println("result = " + result);
        return result;
    }*/

    //INSERT через SimpleJdbcInsert
    public Product addProduct(Product product) {
        Product result = product;
        SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate);
        sji.withTableName("product").usingGeneratedKeyColumns("id");
        Number id = sji.executeAndReturnKey(new BeanPropertySqlParameterSource(product));
        System.out.println("id= " +id);
        //System.out.println("product =" + product);
        result = findById(id.intValue());
        //result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), id);
        System.out.println("result = " + result);
        return result;
    }

public Product findByUserOrder(UserOrder userOrder) {
    return jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<>(Product.class), userOrder.getProductId());
}

    public Product findById(Integer productId) {
        return jdbcTemplate.queryForObject("SELECT * FROM product WHERE id=?", new BeanPropertyRowMapper<>(Product.class), productId);
    }

    public Product updateProductCount(Integer id, Integer count) {
        Product result;
        int n = jdbcTemplate.update("UPDATE Product SET count=? WHERE ID=?", count, id);
        result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), id);
        return result;
    }

    public Product updateProductPrice(Integer id, Double price) {
        Product result;
        int n = jdbcTemplate.update("UPDATE Product SET price=? WHERE ID=?", price, id);
        result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), id);
        return result;    }

  public Integer productCount() {
      return jdbcTemplate.queryForObject("SELECT count(*) FROM Product", Integer.class);
  }

  public Product productRandom () {
      return jdbcTemplate.queryForObject("SELECT * FROM Product ORDER BY random() limit 1", new BeanPropertyRowMapper<>(Product.class));
  }

    public List<Product> changingProductPrice() {
        //Integer productId=1;
//        List<Product> products = jdbcTemplate.query("SELECT * FROM Product", new BeanPropertyRowMapper<>(Product.class));
        List<Product> products = getAllProducts("");
        for (Product productFromArrList : products) {
            Random random = new Random();
            Double changing = (0.01 + ((0.1 - 0.01)  * random.nextDouble()))* productFromArrList.getPrice();
            Double price=(random.nextInt(2)==0.0) ? (productFromArrList.getPrice() - changing) : (productFromArrList.getPrice() + changing);
            updateProductPrice(productFromArrList.getId(), price);
        }
        System.out.println(products);
        return products;
    }

//
}
