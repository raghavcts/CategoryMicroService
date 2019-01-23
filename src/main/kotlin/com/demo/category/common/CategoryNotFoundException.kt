package com.demo.category.common

class CategoryNotFoundException(val responseCode: Int, val responseStatus: String) : Exception()