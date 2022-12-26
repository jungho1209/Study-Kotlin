package com.kotlin.good.global.filter

import com.kotlin.good.global.security.jwt.JwtProperty
import com.kotlin.good.global.security.jwt.JwtTokenParser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenFilter(
    private val jwtTokenParser: JwtTokenParser
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)

        token?.run {
            SecurityContextHolder.getContext().authentication = jwtTokenParser.getAuthentication(token)
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(JwtProperty.HEADER)

        if (bearerToken != null && bearerToken.startsWith(JwtProperty.PREFIX)) {
            return bearerToken.substring(7)
        }

        return null
    }

}