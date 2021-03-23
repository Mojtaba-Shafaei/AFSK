package com.shafaei.paradox.ui.mainActivity

import com.shafaei.paradox.businessLogic.WaveDecoder
import com.shafaei.paradox.rx.RxSingleSchedulers
import io.reactivex.observers.TestObserver
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MainActivityViewModelTest {
 
 /* @Rule
  @JvmField // Fix "The @Rule 'instantExecutorRule' must be public." compile error
  var instantExecutorRule = InstantTaskExecutorRule()
  */
 private var viewModel: MainActivityViewModel? = null
 
 //////////////////////////////////////////////////////////////////////////////////////////////////
 @Before
 fun setUp() {
  val decoder = WaveDecoder()
  viewModel = MainActivityViewModel(decoder, RxSingleSchedulers.TEST_SCHEDULER);
  
 }
 
 @Test
 fun testNull() {
  assertThat("ViewModel not initialized yet", viewModel, notNullValue())
  assertThat("View model states is null", viewModel!!.states, notNullValue())
 }
 
 @Test
 fun decodeWaveFile_EmptyFile_ReturnError() {
  val testObserver: TestObserver<ViewState> = viewModel!!.states.test()
  viewModel!!.decodeWaveFile("empty_file_name", emptyList<Byte>().toByteArray())
  
  testObserver.assertValueCount(2)
  testObserver.assertValueAt(0) { viewState -> viewState.loading }
  testObserver.assertValueAt(1) { viewState -> viewState.hasError() }
 }
 
 /*@Test
 fun decodeWaveFile_RealFile_ReturnTrue() {
  val testObserver: TestObserver<ViewState> = viewModel!!.states.test()
  
  viewModel!!.decodeWaveFile("file_1", emptyList<Byte>().toByteArray())
  
  testObserver.assertValueCount(2)
  testObserver.assertValueAt(0) { viewState -> viewState.loading }
  testObserver.assertValueAt(1) { viewState -> viewState.data != null }
 }*/
 
 @After
 fun tearDown() {

 }
}