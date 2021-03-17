package com.shafaei.paradox.ui.mainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mojtaba_shafaei.android.ErrorMessage.State
import com.shafaei.paradox.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.InputStream
import java.io.PrintWriter

class MainActivity : AppCompatActivity() {
 private val TAG = "MainActivity"
 private lateinit var viewBinding: ActivityMainBinding
 private val mViewModel: MainActivityViewModel by viewModel()
 
 private val mDisposables = CompositeDisposable()
 
 //////////////////////////////////////////////////////////////////////////////////////////////////
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  viewBinding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(viewBinding.root)
  
  viewBinding.btn1.setOnClickListener {
   // read wave file from disk
   val fis: InputStream = assets.open("file_1.wav")
   val rawBytes = fis.readBytes()
   mViewModel.decodeWaveFile("file_1", rawBytes)
  }
  
  viewBinding.btn2.setOnClickListener {
   // read wave file from disk
   val fis: InputStream = assets.open("file_2.wav")
   val rawBytes = fis.readBytes()
   mViewModel.decodeWaveFile("file_2", rawBytes)
  }
  
  viewBinding.btn3.setOnClickListener {
   // read wave file from disk
   val fis: InputStream = assets.open("file_3.wav")
   val rawBytes = fis.readBytes()
   mViewModel.decodeWaveFile("file_3", rawBytes)
  }
 }
 
 override fun onStart() {
  super.onStart()
  mDisposables.add(
   mViewModel.states
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe({ state ->
     if(state.loading) {
      viewBinding.em.state = State.loading()
     }
   
     if(state.error != null) {
      viewBinding.em.state = State.error(state.error.message ?: "Error Happened!!!")
     }
   
     if(state.data != null) {
      logAndShowDecodedData(state.data)
     }
   
     if(state.error == null && !state.loading) {
      viewBinding.em.state = State.hidden()
     }
    }, { logAndShowError(it, "view state stream") })
  )
 }
 
 override fun onPause() {
  super.onPause()
  mDisposables.clear()
 }
 
 @SuppressLint("SetTextI18n")
 private fun logAndShowDecodedData(data: MainData) {
  Log.d(TAG, "decoded data is:$data")
  viewBinding.tvResult.text = "Count = ${data.count} \n\n ${data.decodedData}"
  
  val file = File.createTempFile(data.fileName, ".txt", cacheDir)
  try {
   // response is the data written to file
   PrintWriter(file).use { out -> out.println(data.decodedData) }
  } catch(e: Exception) {
   logAndShowError(e, "logAndShowDecodedData")
  }
 }
 
 private fun logAndShowError(throwable: Throwable, methodName: String) {
  Log.e(TAG, "$methodName failed.", throwable)
  showErrors(throwable)
 }
 
 private fun showErrors(throwable: Throwable) {
  Toast.makeText(this.applicationContext, throwable.message, Toast.LENGTH_LONG).show()
 }
}