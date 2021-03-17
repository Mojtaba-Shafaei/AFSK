package com.shafaei.paradox.businessLogic

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WaveDecoderTest {
 lateinit var mWaveDecoder: WaveDecoder
 
 @Before
 fun init() {
  mWaveDecoder = WaveDecoder()
 }
 
 ///////////////////////////////////////////////////////////////////////////////////////////////////
 @Test
 fun trimWaveHeader_returnTrue() {
  val size = 100
  val resultSize = size - 44
  
  val byteArray = ArrayList<Byte>(size)
  repeat(size) { byteArray.add(1) }
  
  val result = mWaveDecoder.trimWaveRiffHeader(byteArray.toByteArray())
  Assert.assertEquals(result.size, resultSize)
 }
 
 ///////////////////////////////////////////////////////////////////////////////////////////////////
}