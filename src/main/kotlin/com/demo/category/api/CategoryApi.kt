package com.demo.category.api

import com.demo.category.common.exception.CategoryException
import com.demo.category.common.exception.CategoryNotFoundException
import com.google.gson.JsonObject
import com.netflix.hystrix.HystrixCommand
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Accept: application/json", "Content-Type: application/json")
interface CategoryApi {
    @Throws(CategoryException::class, CategoryNotFoundException::class)
    @RequestLine("GET /v1/categories/{categoryId}/products")
    fun getCategoryById(@Param("categoryId") categoryId: String): HystrixCommand<JsonObject>
}