package com.shafaei.paradox.ui.mainActivity

data class MainData(val fileName: String, val decodedData: String, val count: Int)
data class ViewState(val loading: Boolean = false, val error: Throwable? = null, val data: MainData? = null) {
 companion object {
  fun loadingState() = ViewState(loading = true, error = null, data = null)
  fun errorState(t: Throwable) = ViewState(loading = false, error = t, data = null)
  fun data(d: MainData) = ViewState(loading = false, error = null, data = d)
 }
 
 fun hasError() = (error != null)
}