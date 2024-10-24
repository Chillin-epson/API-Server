package com.chillin.drawing;

import com.chillin.drawing.domain.Drawing
import com.chillin.member.Member
import com.chillin.type.DrawingType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DrawingRepository : JpaRepository<Drawing, Long> {
    @Query("SELECT d FROM Drawing d WHERE d.type = :type AND d.member = :member ORDER BY d.createdAt DESC")
    fun findAllByType(type: DrawingType, member: Member): List<Drawing>
}