package com.liquid.liquidpedia.repository;

import com.liquid.liquidpedia.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
