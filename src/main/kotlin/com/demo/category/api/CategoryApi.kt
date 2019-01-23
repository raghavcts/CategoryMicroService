package com.demo.category.api

import com.demo.category.common.CategoryException
import com.demo.category.common.CategoryNotFoundException
import com.google.gson.JsonObject
import feign.Headers
import feign.Param
import feign.RequestLine

@Headers("Accept: application/json", "Content-Type: application/json")
interface CategoryApi {
    @Throws(CategoryException::class, CategoryNotFoundException::class)
    @RequestLine("GET /v1/categories/{categoryId}/products?key={apiKey}")
    fun getCategoryById(@Param("categoryId") categoryId:
                        String, @Param("apiKey") apiKey: String): JsonObject
}