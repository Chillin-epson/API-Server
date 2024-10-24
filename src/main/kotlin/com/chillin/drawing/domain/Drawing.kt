package com.chillin.drawing.domain

import com.chillin.member.Member
import com.chillin.type.DrawingType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime


@Entity
@EntityListeners(AuditingEntityListener::class)
class Drawing(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @Column
    val pathname: String,

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

    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
}