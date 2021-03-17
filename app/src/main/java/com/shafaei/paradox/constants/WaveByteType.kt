package com.shafaei.paradox.constants

/**
 * @author m.shafaei
 *
 * This class helps to hard code Byte type in a bits stream
 *
 * This enum class represent the 2 types of BYTE that there is in the bit stream
 * Might a byte such as Header bytes is 0xFF(ONE) or as END-BLOCK is 0x00(ZERO)
 */
enum class WaveByteType {
 ZERO, ONE
}