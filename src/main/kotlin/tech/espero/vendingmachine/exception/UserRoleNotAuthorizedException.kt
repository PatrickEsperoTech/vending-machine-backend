package tech.espero.vendingmachine.exception

class UserRoleNotAuthorizedException(override val message: String?) : RuntimeException(message) {
}