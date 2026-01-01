package com.odeng.finance.common

import org.springframework.http.HttpStatus

class BusinessException(message: String, code: HttpStatus = HttpStatus.BAD_REQUEST) : RuntimeException(message) {
}