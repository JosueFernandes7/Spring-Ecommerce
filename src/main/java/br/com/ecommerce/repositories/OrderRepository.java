package br.com.ecommerce.repositories;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ecommerce.entities.Order;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// methods
	List<Order> findAllByCustomerId(Long customerId);
}
