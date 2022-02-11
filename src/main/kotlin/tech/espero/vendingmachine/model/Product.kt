package tech.espero.vendingmachine.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Product(
    @Id @GeneratedValue var id: Long,
    var amountAvailable: Int,
    var cost: Int,
    var productName: String,
    var sellerId: Long
) {
    override fun toString(): String {
        return "Product{id=$id, amountAvailable=$amountAvailable, cost=$cost, productName=$productName, sellerId=$sellerId}"
    }
}