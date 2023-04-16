package ru.inex.demoshop.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.inex.demoshop.dto.UserOrder;
import ru.inex.demoshop.entity.*;
import ru.inex.demoshop.repository.*;

import java.util.Collections;
import java.util.List;

@Service
@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class ShopService {

    //@Autowired
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final ProductRepositoryImpl productRepositoryImpl;
    private final CategoryRepositoryImpl categoryRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;
    private final OrderRepositoryImpl orderRepositoryImpl;
    private final RatingRepositoryImpl ratingRepositoryImpl;
    //private final SimpleJdbcTemplate

    @Autowired
    public ShopService(JdbcTemplate jdbcTemplate, ProductRepositoryImpl productRepositoryImpl, CategoryRepositoryImpl categoryRepositoryImpl, UserRepositoryImpl userRepositoryImpl, OrderRepositoryImpl orderRepositoryImpl, RatingRepositoryImpl ratingRepositoryImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.productRepositoryImpl = productRepositoryImpl;
        this.categoryRepositoryImpl = categoryRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
        this.orderRepositoryImpl = orderRepositoryImpl;
        this.ratingRepositoryImpl = ratingRepositoryImpl;
        }
        public List<Product> getAllProducts(String sortByName) {return productRepositoryImpl.getAllProducts(sortByName);}

    public Product addProduct(Product product) {return productRepositoryImpl.addProduct(product);}

/*//INSERT через SimpleJdbcInsert
    public Product addProduct(Product product) {
        Product result = product;
                SimpleJdbcInsert sji = new SimpleJdbcInsert(jdbcTemplate);
                sji.withTableName("product").usingGeneratedKeyColumns("id");
                Number id = sji.executeAndReturnKey(new BeanPropertySqlParameterSource(product));
                System.out.println("id= " +id);
        //System.out.println("product =" + product);
        result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), id);
        System.out.println("result = " + result);
        return result;
    }*/

/*    //INSERT через RETURNING id
    public Product addProduct(Product product) {
        Product result = product;
        int n = simpleJdbcInsert("INSERT INTO Product(id,name, price, rating, count, category) VALUES (default,?,?,?,?,?) RETURNING id", product.getName(),product.getPrice(),product.getRating(),product.getCount(), product.getCategory());
        System.out.println("n = " + n + "product = "+ product + "product.getId() = " + product.getId());
       //result = jdbcTemplate.queryForObject("SELECT * FROM Product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), id);
        //System.out.println("result = " + result);
        return result;
    }*/

    public Product updateProductCount(Integer id, Integer count) {return productRepositoryImpl.updateProductCount(id, count);}

    public Product updateProductPrice(Integer id, Double price) {return productRepositoryImpl.updateProductPrice(id, price);}

    public List<User> getAllUsers() {return userRepositoryImpl.getAllUsers();}

    public User addUser(User user) {return userRepositoryImpl.addUser(user);}

    public Order addOrder(Order order, User user) {return orderRepositoryImpl.addOrder(order, user);}

    public Order buyProduct(String name, UserOrder userOrder) {
        Product product = productRepositoryImpl.findByUserOrder(userOrder);
        if (product.getCount() >= userOrder.getAmount()) {
            User user;
            if (StringUtils.hasText(name)) {
                //Integer countUserName = jdbcTemplate.queryForObject("SELECT count(*) FROM public.user WHERE name=?", Integer.class, name);
                Integer countUserByName = userRepositoryImpl.countUserByName(name);
                if (countUserByName==0) {
                    user = addUser(new User(name));
                    System.out.println("if user = " + user);
                }
                else {
                    User userExists = userRepositoryImpl.findByName(name);
                        System.out.println("findByName = " + userExists);
                        user = userExists;
                    System.out.println("else user = " + user);
                }
                Order order = new Order(user.getId(), product.getId(), userOrder.getAmount());
                System.out.println("order = " + order);
                product.setCount(product.getCount() - order.getAmount());
                updateProductCount(product.getId(), product.getCount());
                System.out.println("order addOrder = " + order);
                addOrder(order, user);
                System.out.println("order addOrder after= " + order);
                return order;
            }
            throw new RuntimeException("User has no login(name is empty)");
        }
        throw new RuntimeException("No enough count");
    }

    public Product addRatingProduct(Integer productId, Integer userId, Integer orderId, Integer mark) {
        Order order = orderRepositoryImpl.findById(orderId);
        Product product = productRepositoryImpl.findById(productId);
        if (order !=null
                && order.getUserId() == userId
                && order.getProductId() == productId) {
            Double avgRating = ratingRepositoryImpl.calculateAvgRating(productId, userId, orderId, mark);
                System.out.println(product);
                product.setRating(avgRating);
                System.out.println(product);
                product = ratingRepositoryImpl.updateProductRating(avgRating, productId);
        } else {
            throw new RuntimeException("No user order with product to rate");
        }
        return product;
    }

    //@Scheduled(initialDelay = 3000, fixedDelay = 60000)
public Product marketingProductLottery() {
    Integer n = productRepositoryImpl.productCount();
    Product productRandom = null;
        if (n!=0) {
            productRandom = productRepositoryImpl.productRandom();
            System.out.println("productRandom = " + productRandom);
            Integer count = 1 ;
            Integer productCount = productRandom.getCount();
            if (productCount >= count) {
                productRandom = updateProductCount(productRandom.getId(), productCount - count);
            } else {
                throw new RuntimeException("Not enough product" + productRandom);
            }
        } else {
            throw new RuntimeException("No product in the shop");
        }
        return productRandom;
}

//@Scheduled(initialDelay = 3000, fixedDelay = 15000)
    public List<Product> changingProductPrice() {
        return productRepositoryImpl.changingProductPrice();
    }

//------------test CRUDRepository Category
public List<Category> getAllProductsCategories() {return (List<Category>) categoryRepositoryImpl.findAll();}

    public Object getCategoryById(Integer categoryId) {return categoryRepositoryImpl.findById(categoryId);}

    public Category addCategory(Category category) {return categoryRepositoryImpl.save(category);}

    public Category updateProductsCategoryName (Integer categoryId, String name) {
            Category categoryUpdated = (Category) categoryRepositoryImpl.findAllById(Collections.singleton(categoryId));
            categoryUpdated.setName(name);
            categoryRepositoryImpl.save(categoryUpdated);
            return categoryUpdated;}

    public Category findByName(String name) {return categoryRepositoryImpl.findByName(name);}
    public Category findByIdLessThan(Integer id) {return categoryRepositoryImpl.findByIdLessThan(id);}
    public Integer findIdByCategoryName(String name) {return categoryRepositoryImpl.findIDByCategoryName(name);}

//------------test CRUDRepository Category
//
}
