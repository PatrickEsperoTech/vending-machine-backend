package tech.espero.vendingmachine.exception

class DepositTooLowException(override val message: String?) : RuntimeException(message) {
}