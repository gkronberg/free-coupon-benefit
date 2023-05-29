package com.mercadolibre.freecouponbenefit.utils

import com.mercadolibre.freecouponbenefit.configurations.mapper

inline fun <reified T> String.toValue(): T = mapper.readValue(this, T::class.java)

// @Deprecated("Only use this function if cannot use 'T' as reified type parameter", ReplaceWith("this.toValue<T>()"))
fun <T> String.toValue(clazz: Class<T>): T = mapper.readValue(this, clazz)
