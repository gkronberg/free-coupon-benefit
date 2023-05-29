package com.mercadolibre.freecouponbenefit.repository

import com.mercadolibre.freecouponbenefit.dto.ItemStatsResponse
import java.sql.ResultSet
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ItemStatsRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun getTop5ItemStatsResponse(timeToExpire: String, states: List<Int>): List<ItemStatsResponse> {
//        val sql = queryForTop5()
//        val params = mapOf(
//            "state_id" to states,
//            "time_to_expire" to timeToExpire
//        )

        return try {
//            jdbcTemplate.query(sql, params) { rs, _ -> mapItemStats(rs) }
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