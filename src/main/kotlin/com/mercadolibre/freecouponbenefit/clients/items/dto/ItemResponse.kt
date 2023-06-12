package com.mercadolibre.freecouponbenefit.clients.items.dto

import java.math.BigDecimal

data class ItemResponse(
    val id: String,
    val title: String?,
    val price: BigDecimal?,
    val siteId: String?
)
