package tech.espero.vendingmachine.exception

class ChangeNotPossibleException(override val message: String?) : RuntimeException(message) {
}