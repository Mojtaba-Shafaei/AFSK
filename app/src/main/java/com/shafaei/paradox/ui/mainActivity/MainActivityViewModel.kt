package com.shafaei.paradox.ui.mainActivity

import androidx.lifecycle.ViewModel
import com.shafaei.paradox.businessLogic.WaveDecoder
import com.shafaei.paradox.constants.Modulation
import com.shafaei.paradox.constants.Modulation.LEADER
import com.shafaei.paradox.constants.Modulation.MODULATION_2T_VALUE
import com.shafaei.paradox.constants.Modulation.MODULATION_BAR_BYTE_COUNT
import com.shafaei.paradox.constants.Modulation.MODULATION_BYTE_BITS_SIZE
import com.shafaei.paradox.constants.Modulation.MODULATION_T_VALUE
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import kotlin.collections.ArrayList

class MainActivityViewModel(private val decoder: WaveDecoder) : ViewModel() {
 private val mDisposables = CompositeDisposable()
 private val mStates: BehaviorSubject<ViewState> = BehaviorSubject.create()
 val states: Observable<ViewState> = mStates
 //////////////////////////////////////////////////////////////////////////////////////////////////
 
 fun decodeWaveFile(fileName: String, rawBytes: ByteArray) {
  mDisposables.add(
   Single.fromCallable {
    // trim wave file header from the start of file
    val stereoBytes = decoder.trimWaveRiffHeader(rawBytes)
  
    // convert wave bits bars to Data bits
    // each Bar in wave file appears as 4 (2stereo * 2Byte) bytes in the byteEditor
    // I think each one takes 2 bytes because the audio file is in PCM 16bit format.
    //TODO by m.shafaei 2021-03-18: convert audio file to 8bit and check your scenario
  
    // keeps converted wave bars as bits
    val bitList = ArrayList<Int>()
  
    var valuePrev: ByteArray? = null
    var valueCurrent: ByteArray
    var counter = 0
  
    for(index in 0..(stereoBytes.size - MODULATION_BAR_BYTE_COUNT) step MODULATION_BAR_BYTE_COUNT) {
     valueCurrent = stereoBytes.copyOfRange(index, index + MODULATION_BAR_BYTE_COUNT)
   
     if(!valueCurrent.contentEquals(valuePrev)) {
      when(counter) {
       Modulation.MODULATION_T_BAR_COUNT -> bitList.add(MODULATION_T_VALUE)
       Modulation.MODULATION_2T_BAR_COUNT -> bitList.add(MODULATION_2T_VALUE)
       else -> { /*NOOP*/
       }
      }
      valuePrev = valueCurrent
      counter = 0
     }
   
     counter++
    }
  
    // find beginning index of LEADER
    val indexOfStartingLeader = Collections.indexOfSubList(bitList, decoder.dummyLeadArray)
    // find beginning index of data
    val indexOfStartData = indexOfStartingLeader + LEADER.MODULATION_LEADER_TOTAL_BITS_SIZE
  
    // find begging index of END BLOCK
    val indexOfStartingEndBlock = Collections.lastIndexOfSubList(bitList, decoder.dummyEndBlockArray)
  
    // extract the data bit streams
    val dataBits = bitList.subList(indexOfStartData, indexOfStartingEndBlock)
  
    // convert bits stream to real data. each 8 bits to one real byte integer
    val result = ArrayList<Int>()
    counter = 0
    for(index in 0 until dataBits.size - MODULATION_BYTE_BITS_SIZE step MODULATION_BYTE_BITS_SIZE) {
     counter++
   
     if(counter.rem(31) != 0) { // ignore 31'th, 62th, 93th byte. because they are checksums
      result += dataBits.subList(index + 1, index + 9).reversed().joinToString("").toInt(2)
     }
    }
    result
   }
    .observeOn(Schedulers.computation())
    .toObservable()
    .map { ViewState(data = MainData(fileName = fileName, decodedData = it.joinToString(), count = it.size)) }
    .startWith(ViewState(loading = true))
    .subscribeOn(Schedulers.io())
    .subscribe({ mStates.onNext(it) }, { mStates.onNext(ViewState(error = it)) })
  )
  
 }
 
 override fun onCleared() {
  super.onCleared()
  mDisposables.clear()
 }
}