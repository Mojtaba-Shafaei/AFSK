package com.shafaei.paradox.kotlinExt

/**
 * @return String with fixed len = 8, the result left pad with zero for positive number and left pad with 1 for negative numbers
 */
fun Byte.toBitsString(): String {
 return String.format("%8s", Integer.toBinaryString(this.toInt() and 0xFF)).replace(' ', '0')
}

fun Byte.toBitsString2(): String {
 return String.format("%8s", Integer.toBinaryString(this.toInt() and 0xFF)).replace(' ', '0')
}