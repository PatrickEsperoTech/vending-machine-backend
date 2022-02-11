package tech.espero.vendingmachine

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import tech.espero.vendingmachine.configuration.SecurityConfig
import tech.espero.vendingmachine.service.SecurityUserDetailsService

@AutoConfigureMockMvc
@SpringBootTest
class GetUserTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val seller1Login = "Basic c2VsbGVyMToxMjM0"
    private val buyer1Login = "Basic YnV5ZXIxOjEyMzQ="

    @Test
    fun `seller1CanRetrieveSeller1`() {
        mockMvc.perform(get("/users/seller1").header("Authorization", seller1Login))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `seller1CanNotRetrieveSeller2`() {
        mockMvc.perform(get("/users/seller2"). header("Authorization", seller1Login))
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `buyer1CanRetrieveBuyer1`() {
        mockMvc.perform(get("/users/buyer1").header("Authorization", buyer1Login))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

}