package com.odeng.finance.auth.interfaces.rest

import com.odeng.finance.auth.application.CreateUserInput
import com.odeng.finance.auth.application.UserService
import com.odeng.finance.interfaces.rest.api.UserApi
import com.odeng.finance.interfaces.rest.api.model.CreateUserRequest
import com.odeng.finance.interfaces.rest.api.model.CreateUserResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UserApi {
    private companion object {
        val logger = KotlinLogging.logger {}
    }

    override fun createUser(createUserRequest: CreateUserRequest): ResponseEntity<CreateUserResponse> {
        logger.info { "Creating user with request: $createUserRequest" }
        val user = userService.createUser(
            CreateUserInput(
                username = createUserRequest.username,
                email = createUserRequest.email,
                password = createUserRequest.password
            )
        )

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                CreateUserResponse(
                    username = user.username,
                    email = user.email

                )
            )
    }

}