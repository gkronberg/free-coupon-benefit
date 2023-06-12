package com.mercadolibre.freecouponbenefit.utils

import com.mercadolibre.freecouponbenefit.configurations.mapper

fun Any?.toJson(): String = if (this != null) mapper.writeValueAsString(this) else "{}"

fun Any?.toJsonPretty(): String = if (this != null) mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this) else "{}"
