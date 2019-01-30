package com.demo.category.common.exception


class CategoryException(val responseCode: Int, val responseStatus: String) : Exception()