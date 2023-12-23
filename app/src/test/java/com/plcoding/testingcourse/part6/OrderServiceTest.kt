package com.plcoding.testingcourse.part6

import android.net.Uri
import android.os.Parcel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.MultiFactor
import com.google.firebase.auth.UserInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OrderServiceTest {

    private var auth: FirebaseAuth = mockk(relaxed = true)
    private val emailClient: EmailClient = mockk(relaxed = true)
    private lateinit var orderService: OrderService

    @BeforeEach
    fun init() {
        orderService = OrderService(auth = auth, emailClient = emailClient)
    }


    @Test
    fun `Place order with auth user, emailClient sent`() {
        val customerEmail = "testEmail"
        val productName = "productName"

        //  every { auth.currentUser } returns createFirebaseUserObject()
        useMocks()

        orderService.placeOrder(
            customerEmail = customerEmail,
            productName = productName
        )
        verify {
            emailClient.send(
                email = Email(
                    subject = "Order Confirmation",
                    content = "Thank you for your order of $productName.",
                    recipient = customerEmail
                )
            )
        }
    }

    @Test
    fun `Place order with not auth user, emailClient not sent`() {
        val customerEmail = "testEmail"
        val productName = "productName"

        //every { auth.currentUser } returns createFirebaseUserObject(isAnonymous = true)
        useMocks(isAnonymousParam = true)

        orderService.placeOrder(
            customerEmail = customerEmail,
            productName = productName
        )
        verify(exactly = 0) {
            emailClient.send(
                email = Email(
                    subject = "Order Confirmation",
                    content = "Thank you for your order of $productName.",
                    recipient = customerEmail
                )
            )
        }
    }


    // this is a better way of doing it
    fun useMocks(isAnonymousParam: Boolean = false) {
        val firebaseUser = mockk<FirebaseUser>(relaxed = true) {
            every { isAnonymous } returns isAnonymousParam
        }
        auth = mockk(relaxed = true) {
            every { currentUser } returns firebaseUser
        }

        orderService = OrderService(
            auth = auth,
            emailClient = emailClient
        )
    }


    // works but can be more efficient like this ** useMocks **
    fun createFirebaseUserObject(isAnonymous: Boolean = false): FirebaseUser {
        val testUser = object : FirebaseUser() {
            override fun writeToParcel(dest: Parcel, flags: Int) {
                TODO("Not yet implemented")
            }

            override fun getPhotoUrl(): Uri? {
                TODO("Not yet implemented")
            }

            override fun getDisplayName(): String? {
                TODO("Not yet implemented")
            }

            override fun getEmail(): String? {
                TODO("Not yet implemented")
            }

            override fun getPhoneNumber(): String? {
                TODO("Not yet implemented")
            }

            override fun getProviderId(): String {
                TODO("Not yet implemented")
            }

            override fun getUid(): String {
                TODO("Not yet implemented")
            }

            override fun isEmailVerified(): Boolean {
                TODO("Not yet implemented")
            }

            override fun getMetadata(): FirebaseUserMetadata? {
                TODO("Not yet implemented")
            }

            override fun getMultiFactor(): MultiFactor {
                TODO("Not yet implemented")
            }

            override fun getTenantId(): String? {
                TODO("Not yet implemented")
            }

            override fun getProviderData(): MutableList<out UserInfo> {
                TODO("Not yet implemented")
            }

            override fun isAnonymous(): Boolean {
                return isAnonymous
            }

            override fun zza(): FirebaseApp {
                TODO("Not yet implemented")
            }

            override fun zzb(): FirebaseUser {
                TODO("Not yet implemented")
            }

            override fun zzc(p0: MutableList<Any?>): FirebaseUser {
                TODO("Not yet implemented")
            }

            override fun zzd(): com.google.android.gms.internal.`firebase-auth-api`.zzzy {
                TODO("Not yet implemented")
            }

            override fun zze(): String {
                TODO("Not yet implemented")
            }

            override fun zzf(): String {
                TODO("Not yet implemented")
            }

            override fun zzg(): MutableList<Any?>? {
                TODO("Not yet implemented")
            }

            override fun zzh(p0: com.google.android.gms.internal.`firebase-auth-api`.zzzy) {
                TODO("Not yet implemented")
            }

            override fun zzi(p0: MutableList<Any?>) {
                TODO("Not yet implemented")
            }
        }
        return testUser
    }
}