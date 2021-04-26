package com.csci310_group29.trojancheckincheckout.data.datasource.firebase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.csci310_group29.trojancheckincheckout.R
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.ByteArrayOutputStream
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class PictureFirebaseDataSourceTest {

    companion object {
        private val TAG = "PictureFirebaseDataSourceTest"
    }

    private lateinit var dataSource: PictureFirebaseDataSource
    private lateinit var app: FirebaseApp
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        app = FirebaseApp.initializeApp(context)!!
        val storage = FirebaseStorage.getInstance()
        dataSource = PictureFirebaseDataSource(storage)
    }

    @After
    fun teardown() {
        app.delete()
    }

    @Test
    fun createGetDeleteTest() {
        val stream = context.resources.openRawResource(R.drawable.tennis_ball)
        val bitmap = BitmapFactory.decodeStream(stream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val url = "sampleTennisBall.jpg"
        createPicture(url, byteArray)
        getPicture(url, byteArray)
        deletePicture(url)
        getPicture(url, null, true)
    }

    @Test
    fun getFromUrlTest() {
        val url = "https://images-na.ssl-images-amazon.com/images/I/61oT0J2ipHL._AC_UL1000_.jpg"
        getFromUrl(url)

    }

    fun createPicture(url: String, byteArray: ByteArray) {
        val completable = dataSource.create(url, byteArray)
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("got an exception when trying to upload a picture")
        }
    }

    fun getFromUrl(url: String) {
        val single = dataSource.getFromExternalUrl(url)
        try {
            val picture = single.blockingGet()
            Log.d(TAG, "picture size is ${picture.size}")
            assertTrue(picture.isNotEmpty())
        } catch(e: Exception) {
            fail("got an exception when trying to get picture from url: ${e.localizedMessage}")
        }
    }

    fun getPicture(url: String, testByteArray: ByteArray?, expectError: Boolean = false) {
        val single = dataSource.get(url)
        try {
            val byteArray = single.blockingGet()
            if (!expectError) {
                assertArrayEquals(testByteArray, byteArray)
            } else {
                fail("expected an exception when trying to get picture")
            }
        } catch(e: Exception) {
            if (!expectError) fail("got an exception when trying to get picture: ${e.localizedMessage}")
        }
    }

    fun deletePicture(url: String) {
        val completable = dataSource.delete(url)
        try {
            completable.blockingAwait()
        } catch(e: Exception) {
            fail("got an exception when trying to delete picture: ${e.localizedMessage}")
        }
    }
}