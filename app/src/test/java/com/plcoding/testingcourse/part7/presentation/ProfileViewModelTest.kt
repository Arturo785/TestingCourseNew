package com.plcoding.testingcourse.part7.presentation

import androidx.lifecycle.SavedStateHandle
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import com.plcoding.testingcourse.part7.data.UserRepositoryFake
import com.plcoding.testingcourse.util.MainCoroutineExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainCoroutineExtension::class)
class ProfileViewModelTest {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    // we set a test dispatcher to be able to mock our coruoutine context and "wait for idle" and
    // more
    @BeforeEach
    fun setUp() {
        val testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        repository = UserRepositoryFake()
        viewModel = ProfileViewModel(
            repository = repository,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "userId" to repository.profileToReturn.user.id
                )
            )
        )
    }

    // we need to go back to normal when we use the dispatcher to not affect other tests
    @AfterEach
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    /**
    * runTest allows us to skip time in the coroutine tests, also automatically skips delays and
     * that kind of stuff that only makes tests slower without any reason
     *
     *     testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1"
     *
     *     // Android test
     *     androidTestImplementation "io.mockk:mockk-android:1.12.5"
     *     androidTestImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4"
     *
    * */



    @Test
    fun `Test loading profile success`() = runTest {
        viewModel.loadProfile()

        // with this we wait until the coroutine and the JVM is Idle
        advanceUntilIdle()

        // after the idle then we are sure the job was completed
        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `Test loading profile success 2`() = runTest {
        // With this approach we assign the work to a job
        val jobToWait = launch {
            viewModel.loadProfile()
        }

        // then with this .join() we make sure the job get's a completion
        //Suspends the coroutine until this job is complete.
        jobToWait.join()

        // it's supposed to be completed
        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `Test loading profile error`() = runTest {
        repository.errorToReturn = Exception("Test exception")

        viewModel.loadProfile()

        advanceUntilIdle()

        assertThat(viewModel.state.value.profile).isNull()
        assertThat(viewModel.state.value.errorMessage).isEqualTo("Test exception")
        assertThat(viewModel.state.value.isLoading).isFalse()
    }
}