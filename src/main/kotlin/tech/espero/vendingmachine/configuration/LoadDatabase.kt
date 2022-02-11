package tech.espero.vendingmachine.configuration

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tech.espero.vendingmachine.model.Product
import tech.espero.vendingmachine.model.User
import tech.espero.vendingmachine.model.UserRole
import tech.espero.vendingmachine.repository.ProductRepository
import tech.espero.vendingmachine.repository.UserRepository

@Configuration
class LoadDatabase {

    private val log = LoggerFactory.getLogger(LoadDatabase::class.java)

    @Bean
    fun initDatabase(
        userRepository: UserRepository,
        productRepository: ProductRepository
    ) = CommandLineRunner {
        userRepository.save(User(1, "seller1", "\$2a\$12\$9kGn/3t/tITN2dbsaTgUwueKLZVbRLUoHYgT17A8cpyIIztujBbkS", 0, UserRole.SELLER))
        userRepository.save(User(2, "seller2", "\$2a\$12\$9kGn/3t/tITN2dbsaTgUwueKLZVbRLUoHYgT17A8cpyIIztujBbkS", 0, UserRole.SELLER))
        userRepository.save(User(3, "buyer1", "\$2a\$12\$9kGn/3t/tITN2dbsaTgUwueKLZVbRLUoHYgT17A8cpyIIztujBbkS", 475, UserRole.BUYER))
        userRepository.save(User(4, "buyer2", "\$2a\$12\$9kGn/3t/tITN2dbsaTgUwueKLZVbRLUoHYgT17A8cpyIIztujBbkS", 0, UserRole.BUYER))

        userRepository.findAll().forEach {
            log.info("Preloading $it")
        }

        productRepository.save(Product(5, 10, 200, "Mars", 1))
        productRepository.save(Product(6, 20, 225, "Snickers", 1))
        productRepository.save(Product(7, 5, 400, "Bounty", 2))
        productRepository.save(Product(8, 1, 700, "Nutella", 2))

        productRepository.findAll().forEach {
            log.info("Preloading $it")
        }

    }
}