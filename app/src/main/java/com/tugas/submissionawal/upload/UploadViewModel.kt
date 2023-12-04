package com.tugas.submissionawal.upload

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tugas.submissionawal.api.ApiConfig
import com.tugas.submissionawal.response.FileUploadResponse
import com.tugas.submissionawal.data.pref.UserModel
import com.tugas.submissionawal.data.pref.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(private val pref: UserPreference): ViewModel() {

    val data = MutableLiveData<FileUploadResponse>()

    fun setPostStory(token: String, image: MultipartBody.Part, desc: RequestBody) {
        val service = ApiConfig.getApiService().postStory("Bearer $token", image, desc)
        service.enqueue(object : Callback<FileUploadResponse> {
            override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                if (response.isSuccessful) {
                    data.postValue(response.body())
                    response.body()?.message?.let {
                        Log.d("RESULT POST :", it)
                    }
                }
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                Log.d("Error :", t.message!!)
            }
        })
    }

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getStory(): LiveData<FileUploadResponse> {
        return data
    }
}