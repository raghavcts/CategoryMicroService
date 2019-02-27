# Category Api
Retrieving the discounted products under a Category sorted as "highest price reduction first". Price reduction is calculated using [price.was - price.now].

## Requirements
* Oracle JDK 1.8u40 or higher
* Java IDE with Kotlin Support (e.g. IntelliJ IDEA)

## For Creating IntelliJ IDEA project

* Install essential IntelliJ plugins
  * Lombok Plugin
* Enable Annotation Processors
  * Go to `File > Settings` and search for `Annotation Processors`
  * Check `Enable annotation processing`
* Checkout the project using Git
* Using `New > Project from existing sources` (or from the welcome screen, use `Import Project`), select the build.gradle file
* Select a 1.8 JVM for `Gradle JVM` and check `Enable auto import`

## To Build the application

Run the gradle wrapper in the Category API directory.

#### Linux or Mac

    ./gradlew clean build

#### Windows

    gradlew clean build

## To Run the application

Run the below command to start the application once "Build Successful" message is displayed

#### Linux or Mac

    ./gradlew bootRun

#### Windows

    gradlew bootRun

## To Run the application

Run the below command to test the application.Test reports available in /build/reports directory.

#### Linux or Mac

    ./gradlew test

#### Windows

    gradlew test

## API Documentation

API information is documented using swagger. We can access the swagger document in local using below link

http://localhost:8080/swagger-ui.html#/

## API Endpoint

The api endpoint with out labelType parameter is  http://localhost:8080/category/600001506

The api endpoint with labelType ShowWasNow is  http://localhost:8080/category/600001506?labelType=ShowWasNow

The api endpoint with labelType ShowWasThenNow is  http://localhost:8080/category/600001506?labelType=ShowWasThenNow

The api endpoint with labelType ShowPercDscount is  http://localhost:8080/category/600001506?labelType=ShowPercDscount

## Hystrix Dashboard available in below link

http://localhost:8080/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8080%2Factuator%2Fhystrix.stream&title=CategoryMicroService


Formatted JSON response of discounted products under that category or Respective Error messages should get displayed on the browser.
Price label formatting is according to the allowed labelType passed as URL parameter (default is - Show Was, Now).


