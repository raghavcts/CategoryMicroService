package com.demo.category.api

import com.demo.category.api.config.ApiConfiguration
import com.demo.category.api.config.FeignClientBuilder
import com.google.gson.JsonObject
import com.netflix.hystrix.HystrixCommand
import org.springframework.stereotype.Component


@Component
class CategoryApiImpl(private val feignClientBuilder: FeignClientBuilder,
                      private val categoryApiConfiguration: ApiConfiguration) : CategoryApi {

    val categoryApi: CategoryApi by lazy {
        feignClientBuilder.getFeignClient(categoryApiConfiguration,
                CategoryApi::class.java)
    }

    override fun getCategoryById(categoryId: String): HystrixCommand<JsonObject> {
        return categoryApi.getCategoryById(categoryId);
    }

}