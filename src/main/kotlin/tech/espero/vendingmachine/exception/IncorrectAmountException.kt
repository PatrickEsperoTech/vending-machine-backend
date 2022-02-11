package tech.espero.vendingmachine.exception

class IncorrectAmountException(override val message: String?) : RuntimeException(message) {
}