package tech.espero.vendingmachine.exception

class ProductCostCanNotBeNegativeException(override val message: String?) : RuntimeException(message) {
}