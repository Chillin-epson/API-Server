package com.chillin.drawing;

import com.chillin.drawing.domain.Drawing
import org.springframework.data.jpa.repository.JpaRepository

interface DrawingRepository : JpaRepository<Drawing, Long> {
}