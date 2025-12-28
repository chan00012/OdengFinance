package com.odeng.finance.auth.interfaces.rest

import com.odeng.finance.auth.application.AuthService
import com.odeng.finance.auth.application.AuthenticationInput
import com.odeng.finance.interfaces.rest.api.AuthApi
import com.odeng.finance.interfaces.rest.api.model.AuthenticationRequest
import com.odeng.finance.interfaces.rest.api.model.AuthenticationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authService: AuthService
) : AuthApi {
    override fun authenticate(authenticationRequest: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        val session = authService.authenticate(
            AuthenticationInput(
                email = authenticationRequest.email,
                password = authenticationRequest.password

            )
        )

        return ResponseEntity
            .ok()
            .body(
                AuthenticationResponse(
                    token = session.token
                )
            )
    }
}