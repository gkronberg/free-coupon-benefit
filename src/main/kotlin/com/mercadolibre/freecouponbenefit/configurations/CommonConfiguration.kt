package com.mercadolibre.freecouponbenefit.configurations

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mercadolibre.freecouponbenefit.commons.BeanNames
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

val mapper: ObjectMapper = ObjectMapper()
    .registerModule(JavaTimeModule())
    .registerModule(KotlinModule())
    .enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
    // set options
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)

@Configuration
class CommonConfiguration(
    @Value("\${mercadolibre.client.items.timeout}") val mercadolibreItemsTimeout: Int
) {

    @Bean(BeanNames.ITEMS_CLIENT_REST_TEMPLATE)
    fun mercadolibreItemsClientRestTemplate(): RestTemplate {
        val clientHttpRequestFactory = HttpComponentsClientHttpRequestFactory()

        clientHttpRequestFactory.setConnectionRequestTimeout(mercadolibreItemsTimeout)
        clientHttpRequestFactory.setConnectTimeout(mercadolibreItemsTimeout)
        clientHttpRequestFactory.setReadTimeout(mercadolibreItemsTimeout)

        return RestTemplate(clientHttpRequestFactory)
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper? {
        return mapper
    }
}
