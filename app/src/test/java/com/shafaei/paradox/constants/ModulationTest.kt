package com.shafaei.paradox.constants

import com.shafaei.paradox.constants.Modulation.ONE_WAVE_BYTE
import org.junit.Assert
import org.junit.Test

class ModulationTest {
 
 @Test
 fun getONE_WAVE_BYTE_ReturnTrue() {
  val len = 11
  val one = ONE_WAVE_BYTE
  // check the size
  Assert.assertEquals("size of byte mismatched", len, one.size)
  
  // the only and only zero is located at the first and the other bits are one
  Assert.assertEquals(0, one.lastIndexOf(0))
  
 }
 
 @Test
 fun getZERO_WAVE_BYTE_ReturnTrue() {
  val len = 11
  val zero = Modulation.ZERO_WAVE_BYTE
  
  // check the size
  Assert.assertEquals("size of byte mismatched", len, zero.size)
  
  // check zero bits
  var check = true
  for(i in 0 until 9) {
   if(zero[i] != 0) {
    check = false
    break
   }
  }
  Assert.assertEquals("Zero-bit count mismatched", true, check)
  
  // check the one bits
  // the only and only zero is located at the first and the other bits are one
  check = true
  for(i in 9 until len) {
   if(zero[i] != 1) {
    check = false
    break
   }
  }
  Assert.assertEquals("One-bit count mismatched", true, check)
  
 }
 
 @Test
 fun getLEAD_DATA_ReturnTrue() {
  val count = 7172 // Each Byte contains 11 bits, 1 bit 0 + 8 bits(any value) + 2 bits 1 so (650 * 11) = 7,172 bits
  Assert.assertEquals("The size mismatched", count, Modulation.LEAD_DATA.size)
  
  // check 650 * 0xff
  var index = 0
  var byteArrayList: List<Int> = emptyList()
  for(i in 0 until (650 * 11 - 11) step 11) {
   byteArrayList = Modulation.LEAD_DATA.subList(i, i + 11)
   if(!byteArrayList.containsAll(ONE_WAVE_BYTE)) {
    index = i
    break
   }
  }
  Assert.assertEquals("Byte value that starts from index $index is not a ONE Byte", ONE_WAVE_BYTE, byteArrayList)
  
  Assert.assertEquals("the 0x42 byte not found", listOf(0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1), Modulation.LEAD_DATA.subList(650 * 11, 650 * 11 + 11))
  
  Assert.assertEquals("the 0x03 byte not found", listOf(0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1), Modulation.LEAD_DATA.subList(651 * 11, 651 * 11 + 11))
 }
 
 @Test
 fun getEND_BLOCK_DATA_ReturnTrue() {
  Assert.assertEquals("the 0x00 byte at the start of END-DATA section not found", listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1), Modulation.END_BLOCK_DATA.subList(0, 11))
  // check 129 * 0xff
  var index = 0
  var byteArrayList: List<Int> = emptyList()
  for(i in 0 until (129 * 11 - 11) step 11) {
   byteArrayList = Modulation.END_BLOCK_DATA.subList(i, i + 11)
   if(!byteArrayList.containsAll(ONE_WAVE_BYTE)) {
    index = i
    break
   }
  }
  Assert.assertEquals("Byte value that starts from index $index is not a ONE Byte", ONE_WAVE_BYTE, byteArrayList)
 }
}