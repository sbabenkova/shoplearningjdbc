package ru.inex.demoshop.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.inex.demoshop.entity.Category;

@Repository
//@AllArgsConstructor
//public interface CategoryRepositoryImpl extends CrudRepository<Category, Integer>
/*public class CategoryRepositoryImpl {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public List<Category> getAllProductsCategories() {
        return jdbcTemplate.query("SELECT * FROM Category", new BeanPropertyRowMapper<>(Category.class));
    }*/

 public interface CategoryRepositoryImpl extends CrudRepository<Category,Integer> {

    //@Autowired
    //private final JdbcTemplate jdbcTemplate;

Category findByName(String name);
Category findByIdLessThan(Integer id);

@Query("SELECT id FROM Category where name = :name")
Integer findIDByCategoryName(String name);




}
