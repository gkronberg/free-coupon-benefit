package com.mercadolibre.freecouponbenefit.commons

object Constants {
    const val BASE_ROUTE = "/api/ms/free-coupon-benefit"
//    const val BASE_INTERNAL = "$BASE_ROUTE/internal-ms"
//    const val BASE_AUTH = "$BASE_ROUTE/auth"

    const val REQUEST_HEADERS_X_DATE = "X-Date"
    const val REQUEST_HEADERS_X_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val REQUEST_HEADERS_X_LOGIN = "X-Login"
    const val REQUEST_HEADERS_X_TRANS_KEY = "X-Trans-Key"
    const val REQUEST_HEADERS_X_IDEMPOTENCY_KEY = "X-Idempotency-Key"
    const val REQUEST_HEADERS_AUTHORIZATION = "Authorization"

    const val COUPON_PATH: String = "/coupon/"
    const val COUPON_STATS_PATH: String = "/coupon/stats"
    const val PIX_REFUND_WEBHOOK_PATH_KEY: String = "\${dlocal.pix.refund.webhook.path}"
    const val PIX_CANCEL_PATH = "/dlocal/pix/cancel/{purchaseId}"
}
