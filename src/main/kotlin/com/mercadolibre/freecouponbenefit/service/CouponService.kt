package com.mercadolibre.freecouponbenefit.service

import com.mercadolibre.freecouponbenefit.dto.CouponBenefitRequest
import com.mercadolibre.freecouponbenefit.dto.CouponBenefitResponse
import com.mercadolibre.freecouponbenefit.exception.BadRequestException
import com.mercadolibre.freecouponbenefit.repository.ItemStatsRepository
import com.mercadolibre.freecouponbenefit.utils.ExceptionUtils
import com.mercadolibre.freecouponbenefit.utils.toJson
import java.math.BigDecimal
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val repository: ItemStatsRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getCouponsToBuy(request: CouponBenefitRequest, requestHttp: HttpServletRequest): CouponBenefitResponse {

        try {
            val response = calculateCouponsToBuy(request)

            response.itemIds.forEach { itemId ->
                repository.updateItemStats(itemId)
            }

            return response
//        } catch (e: ResponseStatusException) {
//            logger.error(e.message)
//            throw e
//        } catch (e: NotFoundException) {
//            throw e
        } catch (e: Exception) {
            val msg = ExceptionUtils.getMessage(e)
            logger.error("DLOCAL REQUEST updatePIXPayment: ERROR while processing request= ${request.toJson()} msg= $msg")
            throw BadRequestException(msg)
        }
    }

    private fun calculateCouponsToBuy(request: CouponBenefitRequest): CouponBenefitResponse {
//        return CouponBenefitResponse(request.itemIds, BigDecimal(480))
        logger.info(request.toJson())
        return CouponBenefitResponse(arrayListOf("MLA1", "MLA2", "MLA4", "MLA5"), BigDecimal(480))
    }
}