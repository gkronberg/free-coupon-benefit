package com.mercadolibre.freecouponbenefit.clients.items.service

import com.mercadolibre.freecouponbenefit.clients.items.dto.ItemResponse
import com.mercadolibre.freecouponbenefit.commons.BeanNames
import com.mercadolibre.freecouponbenefit.utils.ExceptionUtils
import com.mercadolibre.freecouponbenefit.utils.truncatedStackTrace
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ItemsRestClient(
    @Qualifier(BeanNames.ITEMS_CLIENT_REST_TEMPLATE)
    private val restTemplate: RestTemplate
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${mercadolibre.client.base_url}")
    lateinit var baseUrl: String

    @Value("\${mercadolibre.client.items.uri}")
    lateinit var itemsUri: String

    fun fxRateInquiry(itemId: String): ItemResponse {
        logger.info("MERCADOLIBRE REQUEST items itemId:'$itemId'")

        val url = baseUrl + itemsUri + itemId

        return try {
            val headers = HttpHeaders()
            val entity = HttpEntity("", headers)

            val response = restTemplate.exchange(url, HttpMethod.GET, entity, ItemResponse::class.java)
            logger.info("MERCADOLIBRE RESPONSE fxRateInquiry response [status:${response.statusCode}]")

            val body = response.body
            requireNotNull(body) { "MERCADOLIBRE response body cannot be null" }

            body
        } catch (e: Exception) {
            val msg = ExceptionUtils.getMessage(e)
            logger.error("MERCADOLIBRE RESPONSE ERROR items: ERROR while processing response with itemId:'$itemId', msg= $msg" + e.truncatedStackTrace())
            throw e
        }
    }
}