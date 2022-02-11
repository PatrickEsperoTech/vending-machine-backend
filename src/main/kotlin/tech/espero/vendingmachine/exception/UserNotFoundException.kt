package tech.espero.vendingmachine.exception

class UserNotFoundException(override val message: String?) : RuntimeException(message) {
}