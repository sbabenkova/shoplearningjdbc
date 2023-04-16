package ru.inex.demoshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.inex.demoshop.dto.UserOrderList;
import ru.inex.demoshop.entity.Category;
import ru.inex.demoshop.entity.Order;
import ru.inex.demoshop.entity.Product;
import ru.inex.demoshop.entity.User;
import ru.inex.demoshop.service.ShopCreateTables;
import ru.inex.demoshop.service.ShopService;


import java.util.List;
import java.util.stream.Collectors;

@RestController
//@Controller
//@RequestMapping("")
public class ShopController {
    @Autowired
    private ShopService shopService;
    private ShopCreateTables shopCreateTables;

    @GetMapping("/")
  //@ResponseBody
    public String getStart() {
        return "getStart";
    }

    @PostMapping("/product/createTables")
    public void createTables() {
        shopCreateTables.createTables();
    }
//----------------Product----------
    @GetMapping("/product/list")
    public List<Product> getAllProducts(@RequestParam(name = "sortByName", required = false) String sortByName) {
        return shopService.getAllProducts(sortByName);
    }

    /*@PostMapping("/product/add")
    public String addProduct(@RequestBody Product product) {
        shopService.addProduct(product);
        return "redirect:/";
    }*/

    @PostMapping("/product/add")
    public Product addProduct(@RequestBody Product product) {
        System.out.println("shopService.addProduct(product) = " + shopService.addProduct(product));
        return product;
    }

    @PostMapping("/product/update/count")
    public Product updateProductCount(@RequestBody Product product) {
       shopService.updateProductCount(product.getId(), product.getCount());
                return product;
    }

    @PostMapping("/product/update/price")
        public Product updateProductPrice(@RequestParam(name = "productId") Integer productId, @RequestParam(name = "price") Double price) {
        return shopService.updateProductPrice(productId, price);
    }

    @PostMapping("/product/update/priceid")
    public Product updateProductPrice(@RequestBody Product product) {
        return shopService.updateProductPrice(product.getId(), product.getPrice());
                }


    @PostMapping("/product/buy")
    //public Order buyProduct(@RequestBody Product product, Integer amount, User user) {
    public List<Order> buyProduct(@RequestBody UserOrderList userOrders) {
        return userOrders.getOrderList().stream().map(userOrder -> shopService.buyProduct(userOrders.getName(),userOrder)).collect(Collectors.toList());
        //return shopService.buyProduct(product, amount, user);
    }


    @PostMapping("/product/buyid/{id}/{count}")
    public Product buyProductByIdPV(@PathVariable Integer id, @PathVariable Integer count) {
            Product product = shopService.updateProductCount(id,count);
            return product;
        }

    @PostMapping("/product/buyid")
        public Product buyProductByIdRP(@RequestParam(name = "id") Integer id, @RequestParam(name = "count") Integer count) {
        Product product = shopService.updateProductCount(id,count);
        return product;
    }

    @PostMapping("/product/rate")
    public Product addRatingProduct(@RequestParam(name = "productId") Integer productId, @RequestParam(name = "userId") Integer userId, @RequestParam(name = "orderId") Integer orderId, @RequestParam(name = "mark") Integer mark) {
        Product product = shopService.addRatingProduct(productId, userId, orderId, mark);
        return product;
    }

        @PostMapping("/product/marketing")
        public Product marketingProductLottery() {
            Product product = shopService.marketingProductLottery();
            return product;
        }

            @PostMapping("/product/randomchangingprice")
            public List<Product> changingProductPrice() {
              List<Product> products = shopService.changingProductPrice();
              return products;
            }

        //----------------Category----------
        @GetMapping("/category/list")
    //public List<Category> getAllCategories(@RequestParam(name = "my_param", required = false) String my_param)
    public List<Category> getAllCategories() {
        //System.out.println(my_param);
        return (List<Category>) shopService.getAllProductsCategories();   }

    @GetMapping("/category/id")
    //public List<Category> getAllCategories(@RequestParam(name = "my_param", required = false) String my_param)
    public Object getCategoryById(@RequestParam (name="id") Integer id) {
        return shopService.getCategoryById(id);   }

    @PostMapping("/category/add")
    public Category addCategory(@RequestBody Category category) {
        return shopService.addCategory(category);
    }

    @GetMapping("/category/id/byname")
    //public List<Category> getAllCategories(@RequestParam(name = "my_param", required = false) String my_param)
    public Integer findIdByCategoryName(@RequestParam(name = "name") String name) {
        return shopService.findIdByCategoryName(name);   }

    //----------------User----------
@GetMapping("/user/list")
public List<User> getAllUsers() {
    return shopService.getAllUsers();}

@PostMapping("/user/add")
public User addUser(@RequestBody User user) {
    shopService.addUser(user);
    return user;
}

//
}