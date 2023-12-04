package com.tugas.submissionawal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tugas.submissionawal.data.pref.UserPreference
import com.tugas.submissionawal.upload.UploadViewModel
import com.tugas.submissionawal.view.login.LoginViewModel
import com.tugas.submissionawal.view.maps.MapsViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}