package com.mercadolibre.freecouponbenefit.configurations

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.Ticker
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableCaching
@ConfigurationProperties(prefix = "custom.cache")
class CacheConfig(var specs: Map<String, CacheSpec>?) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun cacheManager(ticker: Ticker): CacheManager? {
        val manager = SimpleCacheManager()
        if (specs != null) {
            val caches = specs!!.entries.stream().map { (key, value): Map.Entry<String, CacheSpec> ->
                buildCache(
                    key,
                    value,
                    ticker
                )
            }
                .collect(Collectors.toList())
            manager.setCaches(caches)
        }
        return manager
    }

    private fun buildCache(name: String, spec: CacheSpec, ticker: Ticker): CaffeineCache {
        logger!!.info(
            "Cache '{}' specified timeout of {} {}, max entries={}", name, spec.timeout, spec.timeUnit.name,
            spec.maxEntries
        )
        val builder: Caffeine<Any, Any> = Caffeine.newBuilder().expireAfterWrite(spec.timeout, spec.timeUnit)
            .maximumSize(spec.maxEntries).ticker(ticker)
        return CaffeineCache(name, builder.build())
    }

    @Bean
    fun ticker(): Ticker? {
        return Ticker.systemTicker()
    }

    class CacheSpec {
        var timeUnit = TimeUnit.HOURS
        var timeout: Long = 1
        var maxEntries = 200L
    }
}
