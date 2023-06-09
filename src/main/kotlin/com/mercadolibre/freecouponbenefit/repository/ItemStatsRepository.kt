package com.mercadolibre.freecouponbenefit.repository

import com.mercadolibre.freecouponbenefit.dto.ItemStatsResponse
import com.mercadolibre.freecouponbenefit.exception.ItemsStatsUpdateException
import com.mercadolibre.freecouponbenefit.utils.getMessage
import com.mercadolibre.freecouponbenefit.utils.truncatedStackTrace
import java.sql.ResultSet
import org.slf4j.LoggerFactory
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

private const val COLUMN_ITEM_ID = "item_id"
private const val COLUMN_CANT = "cant"

@Repository
class ItemStatsRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun updateItemStats(itemId: String) {
        val query = queryForUpsert()
        val params = mapOf(
            "item_id_param" to itemId
        )

        try {
            jdbcTemplate.update(query, params)
        } catch (e: Exception) {
            val errorMsg = e.getMessage()

            logger.error("Unexpected error in ItemStatsRepository.updateItemStats, details='$errorMsg'" + e.truncatedStackTrace())

            throw ItemsStatsUpdateException(errorMsg, e)
        }
    }

    fun getTop5ItemStatsResponse(): List<ItemStatsResponse> {
        val query = queryForTop5()

        return try {
            jdbcTemplate.query(query) { rs, _ -> mapItemStats(rs) }
//            listOf(
//                ItemStatsResponse("MLA34", 145000),
//                ItemStatsResponse("MLA4375", 78705),
//                ItemStatsResponse("MLA6", 13938),
//                ItemStatsResponse("MLA488578", 12503),
//                ItemStatsResponse("MLA247", 9822)
//            )
        } catch (e: EmptyResultDataAccessException) {
            emptyList()
        }
    }

    private fun mapItemStats(rs: ResultSet): ItemStatsResponse {
        return ItemStatsResponse(
            itemId = rs.getString(COLUMN_ITEM_ID),
            cant = rs.getLong(COLUMN_CANT)
        )
    }

    private fun queryForUpsert(): String {
        return """
            INSERT INTO coupon_stats AS cs
            VALUES (:item_id_param, 1)
            ON CONFLICT(item_id) DO UPDATE 
                SET item_id = :item_id_param, cant = cs.cant + 1
        """.trimIndent()
    }

    private fun queryForTop5(): String {
        return "SELECT * FROM coupon_stats ORDER BY cant DESC LIMIT 5"
    }
}
