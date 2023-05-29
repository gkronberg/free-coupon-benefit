package com.mercadolibre.freecouponbenefit.dto

import java.math.BigDecimal

data class CouponBenefitResponse(
    val itemIds: List<String>,
    val total: BigDecimal
)