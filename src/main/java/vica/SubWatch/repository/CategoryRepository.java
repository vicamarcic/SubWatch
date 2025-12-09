package vica.SubWatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vica.SubWatch.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
