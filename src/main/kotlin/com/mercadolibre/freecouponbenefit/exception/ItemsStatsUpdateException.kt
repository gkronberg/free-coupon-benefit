package com.mercadolibre.freecouponbenefit.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
class ItemsStatsUpdateException(message: String? = "Items Stats Update exception", e: Exception) : RuntimeException(message, e)
