package com.chillin.drawing;

import com.chillin.drawing.domain.Drawing
import com.chillin.type.DrawingType
import org.springframework.data.jpa.repository.JpaRepository

interface DrawingRepository : JpaRepository<Drawing, Long> {
    fun findAllByTypeOrderByCreatedAtDesc(type: DrawingType): List<Drawing>
}