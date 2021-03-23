package com.shafaei.paradox.constants

/**
 * this class keeps constants and variables of the modulation system
 */
object Modulation {
 //    112 Bytes in byteEditor equals (2t)
 // so 56 bytes equals (t)
 //
 //    28 bars in audio file equals (2t)
 // so 14 bars equals (t)
 //
 // 56 bytes / 14 bars => 4 bytes per bar
 // each T means (56 bytes, 14 bars)
 private const val MODULATION_T_BAR_COUNT = 14
 val MODULATION_T_BAR_COUNT_RANGE = (MODULATION_T_BAR_COUNT - 2)..(MODULATION_T_BAR_COUNT + 1)
 private const val MODULATION_2T_BAR_COUNT = 28
 val MODULATION_2T_BAR_COUNT_RANGE = (MODULATION_2T_BAR_COUNT - 3)..(MODULATION_2T_BAR_COUNT + 2)
 
 const val MODULATION_T_VALUE = 1
 const val MODULATION_2T_VALUE = 0
 
 //
 const val MODULATION_BAR_BYTE_COUNT = 4
 
 const val MODULATION_BYTE_BITS_SIZE = 11 //11 = 1(0x0) + 8(any) + 2(0xFF)
 
 object LEADER {
  const val MODULATION_LEADER_BYTE_SIZE = 650
  const val MODULATION_LEADER_ID_BITS_SIZE = (MODULATION_LEADER_BYTE_SIZE + 2) * MODULATION_BYTE_BITS_SIZE // 652 * 11 = 7172
 }
 
 /**
  * generate a wave Byte that has 11 bits. and it means 0xFF in bytes stream
  */
 val ONE_WAVE_BYTE: List<Int> by lazy { listOf(0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1) }
 
 /**
  * generate a wave Byte that has 11 bits. and it means 0x00 in bytes stream
  */
 val ZERO_WAVE_BYTE: List<Int> by lazy { listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1) }
 
 /**
  * LEAD section in the bytes stream has 650 Bytes, and each byte has 11 bit of 0xFF.
  *
  * Each Byte contains 11 bits, 1 bit 0 + 8 bits(any value) + 2 bits 1 so (650 * 11) = 7,172 bits
  */
 val LEAD_DATA: ArrayList<Int> by lazy {
  val leaderArray = ArrayList<Int>(LEADER.MODULATION_LEADER_ID_BITS_SIZE)
  repeat(LEADER.MODULATION_LEADER_BYTE_SIZE) { leaderArray.addAll(ONE_WAVE_BYTE) }
  leaderArray.addAll(listOf(0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 1)) // 0x42 in LSB
  leaderArray.addAll(listOf(0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1)) //0x3 in LSB
  leaderArray
 }
 
 /**
  * END BLOCK has 130 Bytes that consists of 1 byte of 0x00 and 129 bytes of 0xFF
  */
 val END_BLOCK_DATA: ArrayList<Int> by lazy {
  val endArray = ArrayList<Int>(130 * MODULATION_BYTE_BITS_SIZE) // each byte has 11 bits
  endArray.addAll(ZERO_WAVE_BYTE) // add one Zero byte at the start of block of 0x00
  repeat(129) { endArray.addAll(ONE_WAVE_BYTE) } // add 129 wave bytes of 0xFF
  endArray
 }
}