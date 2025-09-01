package com.twugteam.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setUp() {
        userDataValidator = UserDataValidator(
            patternValidator = object : PatternValidator {
                override fun matches(values: String): Boolean {
                    return true
                }

            }
        )
    }

    @Test
    fun `test add two number`() {
        assertThat(2 + 2).isEqualTo(4)
    }

    @ParameterizedTest
    @CsvSource(
        "password123,false",
        "Password123,true",
        "123,false",
        "Password,false",
    )
    fun testValidPassword(password: String, expectedResult: Boolean) {
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isPasswordValid).isEqualTo(expectedResult)
    }
}