package com.odeng.finance.common

import java.lang.RuntimeException

class ValidationException(
    val errors: List<String>
) : RuntimeException(errors.joinToString(", "))