package com.shafaei.paradox.di

import com.shafaei.paradox.businessLogic.WaveDecoder
import com.shafaei.paradox.ui.mainActivity.MainActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val decoder = module { single { WaveDecoder() } }
val mainViewModel = module { viewModel { MainActivityViewModel(get()) } }
