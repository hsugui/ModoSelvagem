package br.com.rd.ModoSelvagem.repository;

import br.com.rd.ModoSelvagem.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findBySubcategoryId(Long id);

    @Query(value = "SELECT p.* FROM tb_product p INNER JOIN tb_subcategory sub ON p.subcategory_id = sub.id " +
            "INNER JOIN tb_category cat on sub.category_id = cat.id WHERE cat.id = :id ORDER BY update_date DESC", nativeQuery = true)
    List<Product> findByCategoryId(@Param("id") Long id);

    List<Product> findByNameContaining(String productName);

    List<Product> findByOrderByUpdateDateDesc();

    @Query(value = "SELECT p.* FROM tb_product p WHERE p.product_name LIKE REPLACE('%' :text '%', ' ', '%') " +
            "OR p.full_description LIKE REPLACE('%' :text '%', ' ', '%') " +
            "OR p.short_description LIKE REPLACE('%' :text '%', ' ', '%')"
            , nativeQuery = true)
    List<Product> findByProductNameOrDescription(@Param("text") String text);

}

