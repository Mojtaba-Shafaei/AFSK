package com.shafaei.paradox.businessLogic

class WaveDecoder {
 /**
  * Trim wave file header from the start of file
  *
  * @see <a href="http://www.topherlee.com/software/pcm-tut-wavformat.html">Digital Audio - Creating a WAV (RIFF) file</a>
  */
 fun trimWaveRiffHeader(bytes: ByteArray): ByteArray {
  // 44 bits at the beginning of the file are known as the Wave Header and I don't need them, so, I delete them
  return bytes.copyOfRange(44, bytes.size)
 }
}