package tech.espero.vendingmachine.exception

class ProductNotFoundException(override val message: String?) : RuntimeException(message) {
}