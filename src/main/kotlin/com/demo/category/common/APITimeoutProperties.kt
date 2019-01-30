package com.demo.category.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class APITimeoutProperties(
        @param:Value("\${api.timeout.connect.millis}") val connectMillis: Long,
        @param:Value("\${api.timeout.read.millis}") val readMillis: Long)