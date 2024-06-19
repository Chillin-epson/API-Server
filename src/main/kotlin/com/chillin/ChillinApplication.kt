package com.chillin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class ChillinApplication

fun main(args: Array<String>) {
    runApplication<ChillinApplication>(*args)
}
