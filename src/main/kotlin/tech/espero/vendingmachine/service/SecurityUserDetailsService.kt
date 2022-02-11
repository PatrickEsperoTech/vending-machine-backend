package tech.espero.vendingmachine.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.repository.UserRepository

@Service
class SecurityUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findUserByUsername(username)!!
    }

    fun createUser(user: UserDetails) {
        userRepository.save(user as User)
    }
}