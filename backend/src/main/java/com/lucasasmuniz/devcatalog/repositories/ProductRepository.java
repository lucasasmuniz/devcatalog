package com.lucasasmuniz.devcatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lucasasmuniz.devcatalog.entities.Product;
import com.lucasasmuniz.devcatalog.projections.ProductProjection;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(nativeQuery = true, value = """
			SELECT *
			FROM (
			SELECT DISTINCT tb_product.id , tb_product.name
			FROM tb_product 
			INNER JOIN tb_product_category on tb_product.id = tb_product_category.product_id
			WHERE (:categoriesId IS NULL OR tb_product_category.category_id = ANY(:categoriesId))
			AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
			) AS tb_result
			""", 
			countQuery = """
			SELECT COUNT(*) 
			FROM (
			SELECT DISTINCT tb_product.id , tb_product.name
			FROM tb_product 
			INNER JOIN tb_product_category on tb_product.id = tb_product_category.product_id
			WHERE (:categoriesId IS NULL OR tb_product_category.category_id IN :categoriesId)
			AND LOWER(tb_product.name) LIKE LOWER(CONCAT('%',:name,'%'))
			) AS tb_result
			""")
	Page<ProductProjection> searchProducts(@Param("categoriesId") Long[] categoriesId, @Param("name") String name, Pageable pageable);
	
	@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories WHERE obj.id IN :productId ORDER BY obj.name")
	List<Product> searchProductsWithCategories(List<Long> productId);
}
