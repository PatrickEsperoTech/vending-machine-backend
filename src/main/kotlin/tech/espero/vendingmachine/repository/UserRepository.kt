package tech.espero.vendingmachine.repository

import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.JpaRepository
import tech.espero.vendingmachine.model.User

interface UserRepository : JpaRepository<User, Long> {
    fun findUserByUsername(username: String): User?
}