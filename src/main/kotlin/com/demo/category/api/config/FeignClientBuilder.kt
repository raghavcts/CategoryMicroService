package com.demo.category.api.config

import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandGroupKey
import com.netflix.hystrix.HystrixCommandKey
import feign.Logger
import feign.Request
import feign.RequestLine
import feign.Retryer
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import feign.hystrix.HystrixFeign
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import org.springframework.stereotype.Component


@Component
class FeignClientBuilder(private val apiTimeoutProperties: APITimeoutProperties,
                         private val apiKeyInterceptor: ApiKeyInterceptor) {

    fun <T> getFeignClient(categoryApiConfiguration: ApiConfiguration, apiClass: Class<T>): T {

        return HystrixFeign.builder().setterFactory { target, method ->
            HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey(target.name()))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(method.getAnnotation(RequestLine::class.java).value))
        }
                .options(Request.Options(apiTimeoutProperties.connectMillis.toInt(),
                        apiTimeoutProperties.readMillis.toInt()))
                .client(OkHttpClient())
                .encoder(GsonEncoder())
                .requestInterceptor(apiKeyInterceptor)
                .decoder(GsonDecoder())
                .logger(Slf4jLogger(apiClass))
                .logLevel(Logger.Level.FULL)
                .retryer(Retryer.NEVER_RETRY)
                .errorDecoder(CategoryApiErrorDecoder)
                .target(apiClass, categoryApiConfiguration.apiHost)
    }
}

