package com.mercadolibre.freecouponbenefit.controller

import com.mercadolibre.freecouponbenefit.commons.Constants
import com.mercadolibre.freecouponbenefit.dto.CouponBenefitRequest
import com.mercadolibre.freecouponbenefit.dto.CouponBenefitResponse
import com.mercadolibre.freecouponbenefit.dto.ItemStatsResponse
import com.mercadolibre.freecouponbenefit.service.CouponService
import com.mercadolibre.freecouponbenefit.utils.toJson
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Constants.BASE_ROUTE)
class CouponController(
    private val service: CouponService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping(Constants.COUPON_PATH)
    fun getCouponsToBuy(@RequestBody request: CouponBenefitRequest, requestHttp: HttpServletRequest): ResponseEntity<CouponBenefitResponse> {
        logger.info("Incoming request: ${request.toJson()} ")
        val response = service.getCouponsToBuy(request, requestHttp)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping(Constants.COUPON_STATS_PATH)
    fun getCouponsStats(): ResponseEntity<List<ItemStatsResponse>> {
        val response = service.getCouponsStats()

        return ResponseEntity(response, HttpStatus.OK)
    }
}
