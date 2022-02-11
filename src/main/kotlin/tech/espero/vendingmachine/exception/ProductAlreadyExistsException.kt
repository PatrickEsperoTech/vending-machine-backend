package tech.espero.vendingmachine.exception

class ProductAlreadyExistsException(override val message: String?) : RuntimeException(message) {
}