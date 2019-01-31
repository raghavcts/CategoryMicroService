package com.demo.category.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ApiConfiguration(
        @param:Value("\${category.api.host}") val apiHost: String,
        @param:Value("\${category.api.key}") val apiKey: String)