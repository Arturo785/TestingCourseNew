package com.plcoding.testingcourse.core.data

import assertk.assertThat
import assertk.assertions.isTrue
import com.plcoding.testingcourse.core.domain.AnalyticsLogger
import com.plcoding.testingcourse.core.domain.LogParam
import com.plcoding.testingcourse.core.domain.Product
import com.plcoding.testingcourse.core.domain.ProductRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import retrofit2.Response

internal class ProductRepositoryImplTest {

    private lateinit var repository: ProductRepositoryImpl
    private lateinit var productApi: ProductApi
    private lateinit var analyticsLogger: AnalyticsLogger

    @BeforeEach
    fun setUp() {
        productApi = mockk()
        // with relaxed means that the functions inside the implementation will contain dummy
        // return values in the impl and we only care about the ones we want to mock like in this case
        // productApi.purchaseProducts(any())
        // in logger we don't care about it just to have dummy data on all the methods
        analyticsLogger = mockk(relaxed = true)
        repository = ProductRepositoryImpl(productApi, analyticsLogger)
    }

    @Test
    fun `Response error, exception is logged`() = runBlocking {
        // the thing to return when that method is called with that kind of parameters,
        // we can mock for a certain kind of parameters or in this case all kind with any

        // we also mock our response in this case the HttpException
        coEvery { productApi.purchaseProducts(any()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test message"
        }

        // the mock is called and it's supposed to return what we sait to it
        val result = repository.purchaseProducts(listOf())

        assertThat(result.isFailure).isTrue()

        // this helps us to ensure that the method in our logger was called and also we can make
        // sure that was called with the parameters that we expected to, in this case the same ones
        // that in the mock response
        verify {
            analyticsLogger.logEvent(
                "http_error",
                LogParam("code", 404),
                LogParam("message", "Test message"),
            )
        }
    }
}