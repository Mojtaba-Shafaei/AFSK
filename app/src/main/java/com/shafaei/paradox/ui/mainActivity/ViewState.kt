package com.shafaei.paradox.ui.mainActivity

data class MainData(val fileName: String, val decodedData: String, val count: Int)
data class ViewState(val loading: Boolean = false, val error: Throwable? = null, val data: MainData? = null)