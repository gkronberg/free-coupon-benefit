package com.mercadolibre.freecouponbenefit.service

import com.mercadolibre.freecouponbenefit.clients.items.service.ItemsClient
import com.mercadolibre.freecouponbenefit.dto.CouponBenefitRequest
import com.mercadolibre.freecouponbenefit.dto.CouponBenefitResponse
import com.mercadolibre.freecouponbenefit.dto.ItemStatsResponse
import com.mercadolibre.freecouponbenefit.repository.ItemStatsRepository
import com.mercadolibre.freecouponbenefit.utils.ExceptionUtils
import com.mercadolibre.freecouponbenefit.utils.toJson
import java.math.BigDecimal
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private const val ITEM_SEPARATOR = "|||"

@Service
class CouponService(
    private val itemStatsRepository: ItemStatsRepository,
    private val itemsClient: ItemsClient
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun getCouponsToBuy(request: CouponBenefitRequest, requestHttp: HttpServletRequest): CouponBenefitResponse {
        try {
            val response = calculateCouponsToBuy(request)

            response.itemIds.forEach { itemId ->
                itemStatsRepository.updateItemStats(itemId)
            }

            return response
        } catch (e: Exception) {
            val msg = ExceptionUtils.getMessage(e)
            logger.error("Mercadolibre request CouponService.getCouponsToBuy: ERROR while processing request= ${request.toJson()} msg= $msg")
            throw e
        }
    }

    fun getCouponsStats(): List<ItemStatsResponse> {
        try {
            return itemStatsRepository.getTop5ItemStatsResponse()
        } catch (e: Exception) {
            val msg = ExceptionUtils.getMessage(e)
            logger.error("Mercadolibre request CouponService.getCouponsStats: ERROR while processing request, msg= $msg")
            throw e
        }
    }

    private fun calculateCouponsToBuy(request: CouponBenefitRequest): CouponBenefitResponse {
        logger.info(request.toJson())

        val itemIdsSet: Set<String> = request.itemIds.toSet()
        val items = mutableListOf<Pair<String, BigDecimal>>()

        itemIdsSet.forEach { itemId ->
            val itemInfo = itemsClient.getItemInfo(itemId)

            // Se excluyen los items de los cuales no se pudo obtener info desde el ws de clientes, o el precio es nulo.
            if (itemInfo?.price != null) {
                items.add(Pair(itemId, itemInfo.price))
            }
        }

        val (itemResponseList, total) = getBestItemsToBuy(items, request.amount)

        return CouponBenefitResponse(itemResponseList, total)
    }

    private fun getBestItemsToBuy(items: List<Pair<String, BigDecimal>>, amount: BigDecimal): Pair<List<String>, BigDecimal> {
        val itemList = arrayListOf<String>()
        var total = BigDecimal.ZERO

        val bestCombination = findBestCombinationOrNull(items, amount)
        bestCombination?.let {
            itemList.addAll(bestCombination.first.split(ITEM_SEPARATOR))
            total = bestCombination.second
        }

        return Pair(itemList, total)
    }

    private fun findBestCombinationOrNull(items: List<Pair<String, BigDecimal>>, amount: BigDecimal): Pair<String, BigDecimal>? {
        var bestCombination = Pair("Not match", BigDecimal.ZERO)

        val listKeyTotal = mutableSetOf<Pair<String, BigDecimal>>()
        // Itero para probar las combinaciones desde 1 hasta la cantidad total de items.
        for (i in 1..items.size) {
            val combinations = findCombinations(items, i)
            val listKeyTotalPartial = combinations.map { listItems ->
                val key = listItems.joinToString(ITEM_SEPARATOR) { item -> item.first }
                var sum = BigDecimal.ZERO

                listItems.parallelStream().forEach { item -> sum = sum.plus(item.second) }

                Pair(key, sum)
            }.filter {
                val sum = it.second

                // Me quedo solo con las sumas que sean menores o iguales al monto buscado.
                sum <= amount
            }
            listKeyTotal.addAll(listKeyTotalPartial)
        }
        return if (listKeyTotal.isNotEmpty()) {
            // Itero las combinaciones para quedarme con la de mayor monto.
            listKeyTotal.forEach {
                if (it.second > bestCombination.second) {
                    bestCombination = it
                }
            }
            bestCombination
        } else {
            // O devuelvo null si no se encontró ninguna.
            null
        }
    }

    private fun findCombinations(items: List<Pair<String, BigDecimal>>, k: Int): Set<List<Pair<String, BigDecimal>>> {
        val subarrays = mutableSetOf<List<Pair<String, BigDecimal>>>()
        findCombinations(items, 0, k, subarrays, ArrayList())
        return subarrays
    }

    private fun findCombinations(items: List<Pair<String, BigDecimal>>, i: Int, k: Int, subarrays: MutableSet<List<Pair<String, BigDecimal>>>, out: MutableList<Pair<String, BigDecimal>>) {
        // entrada inválida
        if (items.isEmpty() || k > items.size) {
            return
        }

        // caso base: el tamaño de la combinación es `k`
        if (k == 0) {
            subarrays.add(ArrayList(out))
            return
        }

        // comienza desde el siguiente índice hasta el último índice
        for (j in i until items.size) {
            out.add(items[j])
            findCombinations(items, j + 1, k - 1, subarrays, out)
            out.removeAt(out.size - 1)
        }
    }
}
