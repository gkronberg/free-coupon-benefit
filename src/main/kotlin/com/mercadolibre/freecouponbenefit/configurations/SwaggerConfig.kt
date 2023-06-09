package com.mercadolibre.freecouponbenefit.configurations

import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    fun apiInfo(): ApiInfo {
        // @formatter:off
        return ApiInfoBuilder()
            .title("Free Coupon Benefit API")
            .description("API que dado una lista de item_id y el monto total, pueda devolver la lista de ítems que maximice el total a gastar.")
            .version("1.0.0")
            .contact(Contact("Mercadolibre UY Team", "", "gkronberg@hotmail.com"))
            .build()
        // @formatter:on
    }

    @Bean
    fun api(): Docket {
        // @formatter:off
        return Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(Predicates.not(PathSelectors.regex("/error.*")))
            .paths(Predicates.not(PathSelectors.regex("/.*actuator.*")))
            .build()
            .apiInfo(apiInfo())

        // @formatter:on
    }
}
