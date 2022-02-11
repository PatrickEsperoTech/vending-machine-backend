package tech.espero.vendingmachine.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import tech.espero.vendingmachine.exception.*
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.model.UserRole
import tech.espero.vendingmachine.service.UserService

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/users/{username}")
    fun one(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.findUser(username)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PostMapping("/users")
    fun newUser(@RequestBody newUser: User): ResponseEntity<User> {
        return ResponseEntity(userService.createNewUser(newUser), HttpStatus.CREATED)
    }

    @PutMapping("/users/{username}")
    fun updateUser(@RequestBody updateUser: User, @PathVariable username: String): ResponseEntity<User> {
        return ResponseEntity(userService.updateUser(updateUser, username), HttpStatus.OK)
    }

    @DeleteMapping("/users/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<User> {
        return ResponseEntity(userService.deleteUser(username), HttpStatus.OK)
    }

    class DepositAmount(val amount: Int)

    @PostMapping("/deposit")
    fun deposit(@RequestBody depositAmount: DepositAmount): ResponseEntity<*> {
        return ResponseEntity(userService.deposit(depositAmount), HttpStatus.OK)
    }

    class BuyProduct(val productId: Long, val amount: Int)

    @PostMapping("/buy")
    fun buy(@RequestBody buyProduct: BuyProduct): ResponseEntity<*> {
        return ResponseEntity(userService.buy(buyProduct), HttpStatus.OK)
    }

    @PostMapping("/reset")
    fun reset(): ResponseEntity<*> {
        userService.reset()
        return ResponseEntity(mapOf(Pair("newDeposit", 0)), HttpStatus.OK)
    }
}