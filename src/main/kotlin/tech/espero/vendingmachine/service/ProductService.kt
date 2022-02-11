package tech.espero.vendingmachine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import tech.espero.vendingmachine.exception.*
import tech.espero.vendingmachine.model.Product
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.model.UserRole
import tech.espero.vendingmachine.repository.ProductRepository
import tech.espero.vendingmachine.repository.UserRepository

@Service
class ProductService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    fun findAll(): List<Product> {
        return productRepository.findAll()
    }

    fun one(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow {
                ProductNotFoundException("Product with id $id not found")
            }
    }

    fun newProduct(newProduct: Product): Product {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.SELLER) {
            throw UserRoleNotAuthorizedException("Only sellers are allowed to create products")
        }

        if (currentUser.id != newProduct.sellerId) {
            throw IncorrectSellerException("Only the seller can create a product with his own seller id")
        }

        if (newProduct.amountAvailable < 0) {
            throw ProductAmountCanNotBeNegativeException("Product amount available can not be negative")
        }

        if (newProduct.cost < 0) {
            throw ProductCostCanNotBeNegativeException("Product cost can not be negative")
        }

        if (newProduct.cost % 5 != 0) {
            throw ProductCostMustBeDivisibleByFiveException("Product cost must be divisible by 5 to be payable")
        }

        val findProduct = productRepository.findProductByProductName(newProduct.productName)

        if (findProduct != null) {
            throw ProductAlreadyExistsException("Product with name ${newProduct.productName} already exists")
        }

        productRepository.save(newProduct)

        return newProduct
    }

    fun updateProduct(updateProduct: Product, id: Long): Product {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.SELLER) {
            throw UserRoleNotAuthorizedException("Only sellers are allowed to update products")
        }

        if (currentUser.id != updateProduct.sellerId) {
            throw IncorrectSellerException("Only the seller can update a product with his own seller id")
        }

        if (updateProduct.amountAvailable < 0) {
            throw ProductAmountCanNotBeNegativeException("Product amount available can not be negative")
        }

        if (updateProduct.cost < 0) {
            throw ProductCostCanNotBeNegativeException("Product cost can not be negative")
        }

        if (updateProduct.cost % 5 != 0) {
            throw ProductCostMustBeDivisibleByFiveException("Product cost must be divisible by 5 to be payable")
        }

        updateProduct.id = id
        productRepository.save(updateProduct)

        return updateProduct
    }

    fun deleteProduct(id: Long): Product {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.SELLER) {
            throw UserRoleNotAuthorizedException("Only sellers are allowed to delete products")
        }

        val deletedProduct = productRepository.findById(id)

        if (deletedProduct.isEmpty) {
            throw ProductCouldNotBeDeletedException("Product with id $id could not be found")
        }

        if (currentUser.id != deletedProduct.get().sellerId) {
            throw IncorrectSellerException("Sellers can only delete their own products")
        }

        userRepository.deleteById(id)

        return deletedProduct.get()
    }

    /**
     * Get the currently logged-in user from the repository
     * @return Currently logged-in user
     */
    fun currentLoggedInUser(): User {
        val auth = SecurityContextHolder.getContext().authentication
        val principal = auth.principal as UserDetails
        return userRepository.findUserByUsername(principal.username)!!
    }

}