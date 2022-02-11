package tech.espero.vendingmachine.repository

import org.springframework.data.jpa.repository.JpaRepository
import tech.espero.vendingmachine.model.Product

interface ProductRepository : JpaRepository<Product, Long> {
    fun findProductByProductName(productName: String): Product?
}