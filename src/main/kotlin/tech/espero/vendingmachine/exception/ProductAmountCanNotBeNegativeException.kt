package tech.espero.vendingmachine.exception

class ProductAmountCanNotBeNegativeException(override val message: String?) : RuntimeException(message) {
}