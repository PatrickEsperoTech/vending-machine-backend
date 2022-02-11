package tech.espero.vendingmachine.exception

class ProductAmountTooLowException(override val message: String?) : RuntimeException(message) {
}