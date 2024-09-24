package com.chillin.member

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository
) {
    fun register(accountId: String, refreshToken: String) {
        logger.info("Registering member...")

        val member = Member(accountId, refreshToken)
        memberRepository.save(member)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MemberService::class.java)
    }
}
