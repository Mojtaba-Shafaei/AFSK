package com.shafaei.paradox.ui.mainActivity

import androidx.lifecycle.ViewModel
import com.shafaei.paradox.businessLogic.WaveDecoder
import com.shafaei.paradox.constants.Modulation
import com.shafaei.paradox.constants.Modulation.MODULATION_2T_BAR_COUNT_RANGE
import com.shafaei.paradox.constants.Modulation.MODULATION_2T_VALUE
import com.shafaei.paradox.constants.Modulation.MODULATION_BAR_BYTE_COUNT
import com.shafaei.paradox.constants.Modulation.MODULATION_BYTE_BITS_SIZE
import com.shafaei.paradox.constants.Modulation.MODULATION_T_BAR_COUNT_RANGE
import com.shafaei.paradox.constants.Modulation.MODULATION_T_VALUE
import com.shafaei.paradox.kotlinExt.toBitsString
import com.shafaei.paradox.kotlinExt.toWordInt
import com.shafaei.paradox.rx.RxSingleSchedulers
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sign

class MainActivityViewModel(private val decoder: WaveDecoder, private val rxSingleSchedulers: RxSingleSchedulers) : ViewModel() {
 private val mDisposables = CompositeDisposable()
 private val mStates: BehaviorSubject<ViewState> = BehaviorSubject.create()
 val states: Observable<ViewState> = mStates.distinctUntilChanged()
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
  
    var valuePrev: Int? = null
    var valueCurrent: Int
    var counter = 0
  
    val toIndex = stereoBytes.size - MODULATION_BAR_BYTE_COUNT
    for(index in 0 until toIndex step MODULATION_BAR_BYTE_COUNT) {
     // convert each 4 bytes to an integer value. So the stereo will converts to mono(2 bytes) and finally I have a decimal number for that
     // converting stereo to mono done by getting average of 2 channels
     valueCurrent = stereoBytes.copyOfRange(index, index + MODULATION_BAR_BYTE_COUNT)
      .run {
       (((this[1].toBitsString() + this[0].toBitsString()).toWordInt()) +
         ((this[3].toBitsString() + this[2].toBitsString()).toWordInt())
         ) / 2
      }
   
     if(valueCurrent.sign != valuePrev?.sign) {
      when(counter) {
       in MODULATION_T_BAR_COUNT_RANGE -> bitList.add(MODULATION_T_VALUE)
       in MODULATION_2T_BAR_COUNT_RANGE -> bitList.add(MODULATION_2T_VALUE)
       else -> { /* NOOP-this kind of data known as noise or silent, I ignore them to remove from the output*/
       }
      }
      valuePrev = valueCurrent
      counter = 0
     }
   
     counter++
    }
  
    // find beginning index of LEADER
    val indexOfStartingLeader = Collections.indexOfSubList(bitList, Modulation.LEAD_DATA)
    // find beginning index of data
    val indexOfStartData = indexOfStartingLeader + Modulation.LEAD_DATA.size
  
    // find beginning index of END BLOCK
    val indexOfStartingEndBlock = Collections.lastIndexOfSubList(bitList, Modulation.END_BLOCK_DATA)
  
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
    .compose(rxSingleSchedulers.applySchedulers())
    .toObservable()
    .map { ViewState(data = MainData(fileName = fileName, decodedData = it.joinToString(), count = it.size)) }
    .startWith(ViewState(loading = true))
    .subscribe({ mStates.onNext(it) }, { mStates.onNext(ViewState(error = it)) })
  )
  
 }
 
 override fun onCleared() {
  super.onCleared()
  mDisposables.clear()
 }
}