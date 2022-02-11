package tech.espero.vendingmachine

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class BuyTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val seller1Login = "Basic c2VsbGVyMToxMjM0"
    private val buyer1Login = "Basic YnV5ZXIxOjEyMzQ="

    @Test
    fun `sellerCannotBuy`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/buy")
                .header("Authorization", seller1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"productId\": 5,\n" +
                        "    \"amount\": 2\n" +
                        "}")
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `productIdIsAvailable`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/buy")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"productId\": 999,\n" +
                        "    \"amount\": 2\n" +
                        "}")
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `productAmountIsNotNegative`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/buy")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"productId\": 5,\n" +
                        "    \"amount\": -1\n" +
                        "}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `depositIsNotEnough`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/buy")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"productId\": 5,\n" +
                        "    \"amount\": 100\n" +
                        "}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `returnsCorrectChangeTotalSpend400`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/buy")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"productId\": 5,\n" +
                        "    \"amount\": 2\n" +
                        "}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\n" +
                    "    \"totalAmountSpent\": 400,\n" +
                    "    \"purchasedItemName\": \"Mars\",\n" +
                    "    \"amount\": 2,\n" +
                    "    \"totalChange\": 75,\n" +
                    "    \"change\": [\n" +
                    "        1,\n" +
                    "        0,\n" +
                    "        1,\n" +
                    "        1,\n" +
                    "        0\n" +
                    "    ]\n" +
                    "}"))
    }
}