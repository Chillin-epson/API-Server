package com.chillin.drawing.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Drawing(

    @Column
    val filename: String,

    @Enumerated(EnumType.STRING)
    val type: DrawingType,

    @Column(length = 1024)
    val rawPrompt: String? = null,

    @Column(length = 1024)
    val revisedPrompt: String? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val drawingId: Long = 0L
}