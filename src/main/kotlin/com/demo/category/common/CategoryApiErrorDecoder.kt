package com.demo.category.common

import feign.Response
import feign.codec.ErrorDecoder

object CategoryApiErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception {
        return when (response.status()) {
            404 -> CategoryNotFoundException(
                    404,
                    "The category is not a valid category")
            else -> CategoryException(
                    response.status(),
                    response.reason())
        }

    }
}