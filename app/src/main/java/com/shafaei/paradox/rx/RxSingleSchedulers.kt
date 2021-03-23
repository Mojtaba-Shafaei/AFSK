package com.shafaei.paradox.rx

import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface RxSingleSchedulers {
 companion object {
  val DEFAULT: RxSingleSchedulers = object : RxSingleSchedulers {
   override fun <T> applySchedulers(): SingleTransformer<T, T> {
    return SingleTransformer { single: Single<T> ->
     single
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
    }
   }
  }
  var TEST_SCHEDULER: RxSingleSchedulers = object : RxSingleSchedulers {
   override fun <T> applySchedulers(): SingleTransformer<T, T> {
    return SingleTransformer { single: Single<T> ->
     single
      .subscribeOn(Schedulers.trampoline())
      .observeOn(Schedulers.trampoline())
    }
   }
  }
 }
 
 fun <T> applySchedulers(): SingleTransformer<T, T>
}