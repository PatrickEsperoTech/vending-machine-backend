package tech.espero.vendingmachine.exception

class ProductCostMustBeDivisibleByFiveException(override val message: String?) : RuntimeException(message) {
}