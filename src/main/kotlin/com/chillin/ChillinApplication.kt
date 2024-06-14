package com.chillin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChillinApplication

fun main(args: Array<String>) {
    runApplication<ChillinApplication>(*args)
}
