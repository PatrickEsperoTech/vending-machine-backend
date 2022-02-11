package tech.espero.vendingmachine

import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest
class DepositTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val seller1Login = "Basic c2VsbGVyMToxMjM0"
    private val buyer1Login = "Basic YnV5ZXIxOjEyMzQ="

    @Test
    fun `sellerCannotDeposit`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", seller1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100}")
        )
            .andExpect(MockMvcResultMatchers.status().isUnauthorized)
    }

    @Test
    fun `canOnlyDepositCorrectAmounts`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":1}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `canNotDepositNegativeAmount`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":-1}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)

    }

    @Test
    fun `returnsCorrectNewDepositAmount`() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\"depositedAmount\":100,\"newDeposit\":100}"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":50}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\"depositedAmount\":50,\"newDeposit\":150}"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/deposit")
                .header("Authorization", buyer1Login)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":5}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("{\"depositedAmount\":5,\"newDeposit\":155}"))

    }
}