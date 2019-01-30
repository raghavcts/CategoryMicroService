package com.demo.category.common.exception

class CategoryNotFoundException(val responseCode: Int, val responseStatus: String) : Exception()