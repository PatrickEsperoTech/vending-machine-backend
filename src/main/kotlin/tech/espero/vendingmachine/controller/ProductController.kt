package tech.espero.vendingmachine.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import tech.espero.vendingmachine.exception.*
import tech.espero.vendingmachine.model.Product
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.model.UserRole
import tech.espero.vendingmachine.repository.ProductRepository
import tech.espero.vendingmachine.repository.UserRepository
import tech.espero.vendingmachine.service.ProductService

@RestController
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping("/products")
    fun findAll(): ResponseEntity<List<Product>> {
        return ResponseEntity(productService.findAll(), HttpStatus.OK)
    }

    @GetMapping("/products/{id}")
    fun one(@PathVariable id: Long): ResponseEntity<Product> {
        return ResponseEntity(productService.one(id), HttpStatus.OK)
    }

    @PostMapping("/products")
    fun newProduct(@RequestBody newProduct: Product): ResponseEntity<Product> {
        return ResponseEntity(productService.newProduct(newProduct), HttpStatus.CREATED)
    }

    @PutMapping("/products/{id}")
    fun updateProduct(@RequestBody updateProduct: Product, @PathVariable id: Long): ResponseEntity<Product> {
        return ResponseEntity(productService.updateProduct(updateProduct, id), HttpStatus.OK)
    }

    @DeleteMapping("/products/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Product> {
        return ResponseEntity(productService.deleteProduct(id), HttpStatus.OK)
    }
}