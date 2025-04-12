package br.com.ecommerce.repositories;

import org.springframework.stereotype.Repository;

import br.com.ecommerce.entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
}
