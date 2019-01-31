package com.demo.category.api.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.stereotype.Component

@Component
class ApiKeyInterceptor(val apiConfiguration: ApiConfiguration) : RequestInterceptor {
    override fun apply(template: RequestTemplate?) {
        template!!.query("key", apiConfiguration.apiKey)
    }
}