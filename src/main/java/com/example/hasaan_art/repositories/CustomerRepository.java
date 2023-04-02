package com.example.hasaan_art.repositories;

import com.example.hasaan_art.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository {
    findAllbyCustomer(Customer customer);
}
