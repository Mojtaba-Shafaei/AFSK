package com.shafaei.paradox.businessLogic

import com.shafaei.paradox.constants.Modulation.LEADER.MODULATION_LEADER_BITS_SIZE
import com.shafaei.paradox.constants.Modulation.LEADER.MODULATION_LEADER_BYTE_SIZE

class WaveDecoder {
 /**
  * generate a wave Byte that has 11 bits. and it means 0xFF in bytes stream
  */
 private val ONE_WAVE_BYTE: List<Int> by lazy { listOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1) }
 
 /**
  * generate a wave Byte that has 11 bits. and it means 0x00 in bytes stream
  */
 private val ZERO_WAVE_BYTE: List<Int> by lazy { listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1) }
 
 /**
  * Trim wave file header from the start of file
  *
  * @see <a href="http://www.topherlee.com/software/pcm-tut-wavformat.html">Digital Audio - Creating a WAV (RIFF) file</a>
  */
 fun trimWaveRiffHeader(bytes: ByteArray): ByteArray {
  // 44 bits at the beginning of the file are known as the Wave Header and I don't need them, so, I delete them
  return bytes.copyOfRange(44, bytes.size)
 }
 
 /**
  * LEAD section in the bytes stream has 650 Bytes, and each byte has 11 bit of 0xFF.
  *
  * Each Byte contains 11 bits, 1 bit 0 + 8 bits(any value) + 2 bits 1 so (650 * 11) = 7,172 bits
  */
 val dummyLeadArray: ArrayList<Int> by lazy {
  val leaderArray = ArrayList<Int>(MODULATION_LEADER_BITS_SIZE)
  repeat(MODULATION_LEADER_BYTE_SIZE) { leaderArray.addAll(ONE_WAVE_BYTE) }
  leaderArray
 }
 
 val dummyEndBlockArray: ArrayList<Int> by lazy {
  val endArray = ArrayList<Int>(130 * 11)
  endArray.addAll(ZERO_WAVE_BYTE) // add one Zero byte at the start of block of 0x00
  repeat(129) { endArray.addAll(ONE_WAVE_BYTE) } // add 129 wave bytes of 0xFF
  endArray
 }
}