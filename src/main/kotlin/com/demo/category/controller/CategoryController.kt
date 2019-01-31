package com.demo.category.controller

import com.demo.category.common.exception.CategoryException
import com.demo.category.common.exception.CategoryNotFoundException
import com.demo.category.model.Products
import com.demo.category.service.CategoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(value = "Category Controller", description = "Rest API for getting filtered prodcuts")
class CategoryController(@Autowired
                         private var categoryService: CategoryService) {

    private val log = LoggerFactory.getLogger(CategoryController::class.java)

    @ApiOperation(value = "Displays the filtered products")
    @GetMapping("/category/{categoryId}", produces = ["application/json"])
    fun getProductsBycategoryId(@PathVariable("categoryId") categoryId: String
                                , @RequestParam("labelType", required = false, defaultValue = "ShowWasNow")
                                @ApiParam(value = "Label Type Would be ShowWasNow,ShowWasThenNow,ShowPercDscount")
                                labelType: String): ResponseEntity<Any> {
        return try {
            val products: Products? = categoryService.getCategory(categoryId, labelType)
            ResponseEntity.ok(products!!)
        } catch (e: CategoryException) {
            log.error("Internal Error Occurred ${e.message}")
            ResponseEntity.ok(Products(arrayListOf()))
        } catch (e: CategoryNotFoundException) {
            log.error("No data found for selected category")
            val map: HashMap<String, String> = hashMapOf("Status" to e.responseStatus)
            ResponseEntity.ok(map)
        } catch (e: Exception) {
            log.error("Unknown Error Occurred ${e.message}")
            ResponseEntity.ok(Products(arrayListOf()))
        }
    }
}