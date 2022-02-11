package tech.espero.vendingmachine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tech.espero.vendingmachine.controller.UserController
import tech.espero.vendingmachine.exception.*
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.model.UserRole
import tech.espero.vendingmachine.repository.ProductRepository
import tech.espero.vendingmachine.repository.UserRepository

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun findUser(username: String): User {
        val currentUser = currentLoggedInUser()

        if (currentUser.username != username) {
            throw UserRoleNotAuthorizedException("You can get only get your own user profile")
        }

        return userRepository.findUserByUsername(username)
            ?: throw UserNotFoundException("User with username $username not found")
    }

    fun createNewUser(newUser: User): User {
        if (userRepository.findUserByUsername(newUser.username) != null) {
            throw UserAlreadyExistsException(newUser.username)
        }

        if (newUser.deposit < 0) {
            throw DepositNegativeException()
        }

        if (newUser.deposit % 5 != 0) {
            throw ChangeNotPossibleException("Deposit must be divisible by 5")
        }

        newUser.password = passwordEncoder.encode(newUser.password)

        userRepository.save(newUser)

        return newUser
    }

    fun updateUser(updateUser: User, username: String): User {
        val findUser = userRepository.findUserByUsername(updateUser.username)

        // Check if other user with same username already exists
        if (findUser != null && username != findUser.username) {
            throw UserAlreadyExistsException(updateUser.username)
        }

        if (updateUser.deposit < 0) {
            throw DepositNegativeException()
        }

        if (updateUser.deposit % 5 != 0) {
            throw ChangeNotPossibleException("Deposit must be divisible by 5")
        }

        updateUser.username = username

        userRepository.save(updateUser)

        return updateUser
    }

    fun deleteUser(username: String): User {
        val currentUser = currentLoggedInUser()

        if (currentUser.username != username) {
            throw UserCouldNotBeDeletedException("Can't delete other users than yourself")
        }

        val deletedUser = userRepository.findUserByUsername(username)
            ?: throw UserCouldNotBeDeletedException("User with username $username does not exist")

        userRepository.deleteById(currentUser.id!!)

        return deletedUser
    }

    fun deposit(depositAmount: UserController.DepositAmount): Map<String, Any> {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.BUYER) {
            throw UserRoleNotAuthorizedException("Only users with the role ${UserRole.BUYER} can deposit money")
        }

        val acceptedValues = arrayOf(5, 10, 20, 50, 100)

        if (depositAmount.amount !in acceptedValues) {
            throw IncorrectAmountException("Deposit amount not in list of accepted values: ${acceptedValues.toList()}")
        }

        val newDeposit = currentUser.deposit + depositAmount.amount

        currentUser.deposit = newDeposit

        userRepository.save(currentUser)

        return mutableMapOf<String, Any>(
            Pair("depositedAmount", depositAmount.amount),
            Pair("newDeposit", newDeposit)
        )
    }

    @Transactional
    fun buy(buyProduct: UserController.BuyProduct): Map<String, Any> {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.BUYER) {
            throw UserRoleNotAuthorizedException("Only users with role ${UserRole.BUYER} can buy products")
        }

        if (buyProduct.amount < 1) {
            throw ProductAmountTooLowException("You must at least buy one product")
        }

        val product = productRepository.findById(buyProduct.productId).orElseThrow {
            ProductNotFoundException("Product with id ${buyProduct.productId} not found")
        }

        if (product.amountAvailable < buyProduct.amount) {
            throw ProductNotAvailableException(
                "Not enough products of id ${product.id} available (Requested: ${buyProduct.amount}, Available: ${product.amountAvailable})"
            )
        }

        val totalAmount = product.cost * buyProduct.amount

        if (currentUser.deposit < totalAmount) {
            throw DepositTooLowException("Deposit of ${currentUser.deposit} is less than than the total amount ($totalAmount)")
        }

        val seller = userRepository.findById(product.sellerId).orElseThrow {
            UserNotFoundException("Seller with id ${product.sellerId} could not be found")
        }

        val change = currentUser.deposit - totalAmount

        val map = mapOf<String, Any>(
            Pair("totalAmountSpent", totalAmount),
            Pair("purchasedItemName", product.productName),
            Pair("amount", buyProduct.amount),
            Pair("totalChange", change),
            Pair("change", calculateChange(change))
        )

        currentUser.deposit = 0
        product.amountAvailable -= buyProduct.amount
        seller.deposit += totalAmount

        userRepository.save(currentUser)
        userRepository.save(seller)
        productRepository.save(product)

        return map
    }

    fun reset() {
        val currentUser = currentLoggedInUser()

        if (currentUser.role != UserRole.BUYER) {
            throw UserRoleNotAuthorizedException("Only users with role ${UserRole.BUYER} can reset their deposit")
        }

        currentUser.deposit = 0

        userRepository.save(currentUser)
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

    /**
     * Calculates the amount of each coin that should be returned as change to the user
     * @param totalChange The total change in cents
     * @return Array of change outputs [amount of 5 cent coins, amount of 10 cent coins, ...]
     */
    private fun calculateChange(totalChange: Int): List<Int> {

        if (totalChange % 5 != 0) {
            throw ChangeNotPossibleException("Change can not be calculated since it is not divisible by 5")
        }

        var runningTotal = totalChange

        val coinMap = mutableMapOf<Int, Int>(
            Pair(100, 0),
            Pair(50, 0),
            Pair(20, 0),
            Pair(10, 0),
            Pair(5, 0)
        )

        loop@ while (runningTotal > 0) {
            for (currentCoin in arrayOf(100, 50, 20, 10 , 5)) {
                if (runningTotal - currentCoin >= 0) {
                    coinMap[currentCoin] = coinMap[currentCoin]!!.plus(1)
                    runningTotal -= currentCoin
                    continue@loop
                }
            }
        }

        return coinMap.values.reversed()
    }

}