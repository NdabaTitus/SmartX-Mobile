package za.ac.iie.smartx

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Unit tests for login input validation.
 * Demonstrates testing of business logic independent of the Android framework.
 */
class LoginValidationTest {

    private fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && email.contains("@") && email.contains(".")
    }

    @Test
    fun `valid email returns true`() {
        assertThat(isValidEmail("student@iie.ac.za")).isTrue()
        assertThat(isValidEmail("ndaba@example.com")).isTrue()
    }

    @Test
    fun `email without at symbol is invalid`() {
        assertThat(isValidEmail("studentexample.com")).isFalse()
    }

    @Test
    fun `email without domain is invalid`() {
        assertThat(isValidEmail("student@")).isFalse()
    }

    @Test
    fun `blank email is invalid`() {
        assertThat(isValidEmail("")).isFalse()
        assertThat(isValidEmail("   ")).isFalse()
    }
}