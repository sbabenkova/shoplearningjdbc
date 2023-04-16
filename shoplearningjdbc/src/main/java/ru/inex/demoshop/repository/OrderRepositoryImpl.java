/*package ru.inex.demoshop.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.entity.Order;
import ru.inex.demoshop.entity.User;

@Repository
@AllArgsConstructor
//public interface OrderRepositoryImpl extends CrudRepository<Order,Integer>
public class OrderRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;

    public Order findById(Integer orderId) {
        return jdbcTemplate.queryForObject("SELECT * FROM public.order WHERE id=?", new BeanPropertyRowMapper<>(Order.class), orderId);
    }

    public Order addOrder(Order order, User user) {
        Order result;
        int n = jdbcTemplate.update("INSERT INTO public.order (amount, productid, userid) VALUES (?,?,?)", order.getAmount(), order.getProductId(),  user.getId());
        int k = jdbcTemplate.queryForObject("SELECT currval('order_id_seq')", Integer.class);
        result = jdbcTemplate.queryForObject("SELECT * FROM public.order WHERE id=?", new BeanPropertyRowMapper<>(Order.class), k);
        return result;
    }


}*/


package ru.inex.demoshop.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.entity.Order;
import ru.inex.demoshop.entity.User;

@Repository
//public interface OrderRepositoryImpl extends CrudRepository<Order,Integer>
public class OrderRepositoryImpl {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("order").withSchemaName("public").usingGeneratedKeyColumns("id");
    }

    public Order findById(Integer orderId) {
        return jdbcTemplate.queryForObject("SELECT * FROM public.order WHERE id=?", new BeanPropertyRowMapper<>(Order.class), orderId);
    }

    //INSERT через SimpleJdbcInsert
    public Order addOrder(Order order, User user) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("amount", order.getAmount());
        parameterSource.addValue("productid", order.getProductId());
        parameterSource.addValue("userid", user.getId());
        return findById(simpleJdbcInsert.executeAndReturnKey(parameterSource).intValue());
//        Order result;
//        int n = jdbcTemplate.update("INSERT INTO public.order (amount, productid, userid) VALUES (?,?,?)", order.getAmount(), order.getProductId(), user.getId());
//        int k = jdbcTemplate.queryForObject("SELECT currval('order_id_seq')", Integer.class);
//        result = jdbcTemplate.queryForObject("SELECT * FROM public.order WHERE id=?", new BeanPropertyRowMapper<>(Order.class), k);
//        return result;
    }


}