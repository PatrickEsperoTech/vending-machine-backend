package tech.espero.vendingmachine.exception

class UserAlreadyExistsException(username: String) : RuntimeException("User with username $username already exists") {
}