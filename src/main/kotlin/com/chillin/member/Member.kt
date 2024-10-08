package com.chillin.member

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    var accountId: String,
    var refreshToken: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var memberId: Long = 0L

    var nickname: String? = null

    var printerAddress: String? = null
}