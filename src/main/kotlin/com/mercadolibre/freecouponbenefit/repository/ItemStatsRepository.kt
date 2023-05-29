package com.mercadolibre.freecouponbenefit.repository

import com.mercadolibre.freecouponbenefit.dto.ItemStatsResponse
import com.mercadolibre.freecouponbenefit.exception.ItemsStatsUpdateException
import com.mercadolibre.freecouponbenefit.utils.ApiError
import com.mercadolibre.freecouponbenefit.utils.ExceptionUtils
import com.mercadolibre.freecouponbenefit.utils.getMessage
import com.mercadolibre.freecouponbenefit.utils.truncatedStackTrace
import java.sql.ResultSet
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ItemStatsRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun updateItemStats(itemId: String) {
        val query = queryForUpsert()
        val params = mapOf(
            "item_id" to itemId
        )

        try {
            jdbcTemplate.update(query, params)
        } catch (e: Exception) {
            val errorMsg = e.getMessage()

            logger.error("Unexpected error in ItemStatsRepository.updateItemStats, details='$errorMsg'" + e.truncatedStackTrace())

            throw ItemsStatsUpdateException(errorMsg, e)
        }
    }

    fun getTop5ItemStatsResponse(states: List<Int>): List<ItemStatsResponse> {
//        val query = queryForTop5()
//        val params = mapOf(
//            "state_id" to states,
//            "time_to_expire" to timeToExpire
//        )

        return try {
//            jdbcTemplate.query(query, params) { rs, _ -> mapItemStats(rs) }
            listOf(
                ItemStatsResponse("MLA34", 145000),
                ItemStatsResponse("MLA4375", 78705),
                ItemStatsResponse("MLA6", 13938),
                ItemStatsResponse("MLA488578", 12503),
                ItemStatsResponse("MLA247", 9822)
            )
        } catch (e: EmptyResultDataAccessException) {
            emptyList()
        }
    }

//    private fun mapItemStats(rs: ResultSet): ItemStatsResponse {
//        return ItemStatsResponse(
//            itemId = rs.getString("item_id"),
//            cant = rs.getLong("cant")
//        )
//    }

    private fun queryForUpsert(): String {
        return """
            INSERT INTO coupon_stats AS cs
            VALUES (:item_id, 1)
            ON CONFLICT(item_id) DO UPDATE 
                SET item_id = :item_id, cant = cs.cant + 1
        """.trimIndent()
    }

    private fun queryForTop5(): String {
        return """
            SELECT  p.id AS purchase_id,
                    p.created_at,
                    p.created_at + :time_to_expire ::INTERVAL AS expire_date,
                    p.transaction_id,
                    p.reference_id,
                    p.reference_model,
                    p.user_id,
                    t.token AS d_local_pix_id,
                    state_id
            FROM purchases p
            INNER JOIN transactions t ON p.transaction_id  = t.id
            WHERE p.gateway_name = :gateway_name
            AND state_id IN (:state_id)
            AND now() > p.created_at + :time_to_expire ::INTERVAL
            ORDER BY 2 DESC
        """.trimIndent()
    }
}