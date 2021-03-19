package com.shafaei.paradox.kotlinExt

import kotlin.math.pow

private const val SIGNED_WORD_MAX = 32768 //2.0.pow(15)

/**
 * @throws [NumberFormatException] if the string contains any character instead of 0 and 1
 */
@Throws(NumberFormatException::class)
fun String.toWordInt(): Int {
 //TODO by m.shafaei 2021-03-19: check if string contains just valid data (0 and 1)
 var result = 0
 
 this.substring(1).toCharArray().forEachIndexed { index, char ->
  if(char == '1') result += 2.0.pow(14 - index).toInt()
 }
 
 return if(this[0] == '1') {
  -1 * (SIGNED_WORD_MAX - result)
 } else {
  result
 }
}