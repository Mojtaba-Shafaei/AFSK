package com.shafaei.paradox

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AFSKApplicationTest {
 @Test
 @Throws(Exception::class)
 fun useAppContext() {
  // Context of the app under test.
  val appContext: Context = androidx.test.core.app.ApplicationProvider.getApplicationContext()
  Assert.assertEquals("com.shafaei.paradox", appContext.packageName)
 }
}