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

    fun findMemberByAccountId(accountId: String): Member {
        return memberRepository.findByAccountId(accountId) ?: throw RuntimeException("Member not found")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(MemberService::class.java)
    }
}
