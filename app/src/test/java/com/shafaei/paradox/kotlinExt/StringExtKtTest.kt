package com.shafaei.paradox.kotlinExt

import junit.framework.TestCase
import org.junit.Test

class StringExtKtTest : TestCase() {
 
 @Test
 fun testBinaryStringToInt() {
  assertEquals(-16383,"1100000000000001".toWordInt())
  assertEquals(-1763, "1111100100011101".toWordInt())//1111 1001 0001 1101 ___ 0xF91D
 }
}