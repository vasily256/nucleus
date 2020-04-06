package biz.markov.nucleus.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain


/**
 * SecurityConfig.
 *
 * @author Vasily_Markov
 */
@EnableWebFluxSecurity
class SecurityConfig(

        @Value("\${spring.ldap.embedded.port}")
        val ldapPort: Int,

        @Value("\${spring.ldap.embedded.base-dn}")
        val baseDn: String

) : AuthenticationConfiguration() {

    @Bean
    fun userDetailsService(authManagerBuilder: AuthenticationManagerBuilder): ReactiveAuthenticationManager {
        authManagerBuilder
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=groups")
                .contextSource()
                .url("ldap://localhost:$ldapPort/$baseDn")
                .and()
                .passwordCompare()
                .passwordEncoder(BCryptPasswordEncoder())
                .passwordAttribute("userPassword")

        return ReactiveAuthenticationManagerAdapter(authManagerBuilder.build())
    }

    @Bean
    fun springSecurityFilterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        serverHttpSecurity
                .authorizeExchange()
                .anyExchange().authenticated()
                .and()
                .httpBasic()

        return serverHttpSecurity.build()
    }
}
