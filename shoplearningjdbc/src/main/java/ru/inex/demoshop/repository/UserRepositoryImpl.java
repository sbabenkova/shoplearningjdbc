package ru.inex.demoshop.repository;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.entity.User;

import java.util.List;

@Repository
@AllArgsConstructor
//public interface UserRepositoryImpl extends CrudRepository<User, Integer>
public class UserRepositoryImpl {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query("select * from public.user", new BeanPropertyRowMapper<>(User.class));
        return users;
    }

    public User addUser(User user) {
        User result;
        //int n = jdbcTemplate.update("INSERT INTO User (id,name) VALUES (default,?) RETURNING id", user.getName());
        System.out.println(user.getName());
        int n = jdbcTemplate.update("INSERT INTO public.user (name) VALUES (?)", user.getName());
        //System.out.println("n = " + n + "product = "+ product + "product.getId() = " + product.getId());
        int k = jdbcTemplate.queryForObject("SELECT currval('user_id_seq')", Integer.class);
        System.out.println("n= " + n + "k= " + k);
        result = jdbcTemplate.queryForObject("SELECT * FROM public.user WHERE id=?", new BeanPropertyRowMapper<>(User.class), k);
        System.out.println("result = " + result);
        return result;
    }

    public Integer countUserByName (String name) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM public.user WHERE name=?", Integer.class, name);
    }

    public User findByName(String name) {
        return jdbcTemplate.queryForObject("SELECT * FROM public.user WHERE name=?", new BeanPropertyRowMapper<>(User.class), name);
    }
}
