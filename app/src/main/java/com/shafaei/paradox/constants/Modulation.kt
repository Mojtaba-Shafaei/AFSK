package com.shafaei.paradox.constants

object Modulation {
 //    112 Bytes in byteEditor equals (2t)
 // so 56 bytes equals (t)
 //
 //    28 bars in audio file equals (2t)
 // so 14 bars equals (t)
 //
 // 56 bytes / 14 bars => 4 bytes per bar
 // each T means (56 bytes, 14 bars)
 const val MODULATION_T_BAR_COUNT = 14
 const val MODULATION_2T_BAR_COUNT = 28
 
 const val MODULATION_T_VALUE = 1
 const val MODULATION_2T_VALUE = 0
 
 //
 const val MODULATION_BAR_BYTE_COUNT = 4
 
 const val MODULATION_BYTE_BITS_SIZE = 11 //11 = 1(0x0) + 8(any) + 2(0xFF)
 
 object LEADER {
  const val MODULATION_LEADER_BYTE_SIZE = 650
  const val MODULATION_LEADER_BITS_SIZE = MODULATION_LEADER_BYTE_SIZE * MODULATION_BYTE_BITS_SIZE
  const val MODULATION_LEADER_TOTAL_BITS_SIZE = (MODULATION_LEADER_BYTE_SIZE + 2) * MODULATION_BYTE_BITS_SIZE // 652 = 650(LEADER) + 2(0x43 , 0x03). ,
 }
}