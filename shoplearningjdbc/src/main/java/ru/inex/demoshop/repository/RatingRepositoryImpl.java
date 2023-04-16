package ru.inex.demoshop.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.entity.Product;
import ru.inex.demoshop.entity.Rating;

import java.util.List;

@Repository
@AllArgsConstructor
//public interface RatingRepositoryImpl extends CrudRepository<Rating, Integer>
public class RatingRepositoryImpl {

    private final JdbcTemplate jdbcTemplate;

    public Rating addRating(Integer productId, Integer userId, Integer orderId, Integer mark) {
        Rating result;
        int n = jdbcTemplate.update("INSERT INTO rating(product_id, user_id, order_id, mark) VALUES (?,?,?,?)", productId, userId, orderId, mark);
        int k = jdbcTemplate.queryForObject("SELECT currval('rating_id_seq')", Integer.class);
        result = jdbcTemplate.queryForObject("SELECT * FROM rating WHERE id=?", new BeanPropertyRowMapper<>(Rating.class), k);
        return result;
    }

    public List<Rating> findByProductId(Integer productId) {
        return jdbcTemplate.query("SELECT * FROM Rating WHERE product_id=?", new BeanPropertyRowMapper<>(Rating.class), productId);
    }

    public Product updateProductRating(Double avgRating, Integer productId) {
        jdbcTemplate.update("UPDATE Product SET rating=? WHERE ID=?", avgRating, productId);
        return jdbcTemplate.queryForObject("SELECT * FROM product WHERE id=?", new BeanPropertyRowMapper<Product>(Product.class), productId);

    }

    public Double calculateAvgRating(Integer productId, Integer userId, Integer orderId, Integer mark) {
        Rating rating = new Rating(productId, userId, orderId, mark);
        rating = addRating(productId, userId, orderId, mark);
        List<Rating> ratings = findByProductId(productId);
        System.out.println("rating = " + rating);
        System.out.println("ratings = " + ratings);
        Double sumMark = 0.0;
        Integer count = 0;
        Double avgRating = 0.0;
        for (Rating ratingFor : ratings) {
            if (ratingFor.getMark() != null) {
                sumMark += ratingFor.getMark();
                count++;
            }
            if (count >= 3) {
                avgRating = (sumMark / count);
            }
            System.out.println("sumMark " + sumMark);
            System.out.println("count " + count);
            System.out.println("avgRating " + avgRating);
        }
        return avgRating;
    }
}