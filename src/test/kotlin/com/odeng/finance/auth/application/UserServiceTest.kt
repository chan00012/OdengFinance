package com.odeng.finance.auth.application

import com.odeng.finance.auth.domain.model.UserStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.UUID

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Test
    fun `should successfully create a user with valid input`() {
        // Given: Valid user input with username, email, and password
        // Use UUID to ensure uniqueness across test runs
        val uniqueId = UUID.randomUUID().toString().substring(0, 8)
        val createUserInput = CreateUserInput(
            username = "testuser_$uniqueId",
            email = "testuser_$uniqueId@example.com",
            password = "SecureP@ss123"
        )

        // When: Creating a new user
        val createdUser = userService.createUser(createUserInput)

        // Then: User should be created successfully with correct properties
        assertThat(createdUser).isNotNull
        assertThat(createdUser.id).isNotNull
        assertThat(createdUser.username).isEqualTo("testuser_$uniqueId")
        assertThat(createdUser.email).isEqualTo("testuser_$uniqueId@example.com")
        assertThat(createdUser.status).isEqualTo(UserStatus.CREATED)
        // Password should be hashed, not plain text
        assertThat(createdUser.hashPassword).isNotEqualTo("SecureP@ss123")
        assertThat(createdUser.hashPassword).isNotEmpty()
    }
}

