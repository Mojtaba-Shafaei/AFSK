package com.shafaei.paradox.kotlinExt

import org.junit.Assert.*

import org.junit.Test

class ByteExtKtTest {
 
 @Test
 fun toBitsString() {
  var byte: Byte = 0xF9.toByte() // -7
  var bytes = "11111001"
  assertEquals(bytes, byte.toBitsString())
 
  byte = 0x1D.toByte() // +29
  bytes = "00011101"
  assertEquals(bytes, byte.toBitsString())
 }
}