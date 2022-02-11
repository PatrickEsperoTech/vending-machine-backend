package tech.espero.vendingmachine.exception

class UserCouldNotBeDeletedException(override val message: String?) : RuntimeException(message) {
}