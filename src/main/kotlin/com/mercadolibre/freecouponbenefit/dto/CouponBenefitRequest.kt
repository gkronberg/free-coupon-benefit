package com.mercadolibre.freecouponbenefit.dto

import java.math.BigDecimal

data class CouponBenefitRequest(
    val itemIds: List<String>,
    val amount: BigDecimal
)