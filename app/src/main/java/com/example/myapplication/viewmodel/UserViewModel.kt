package com.example.myapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.UserService
import com.example.myapplication.db.remote.model.ResultOfSetDto
import com.example.myapplication.db.remote.model.UserProfileDto
import com.example.myapplication.db.remote.model.UserProfileEditDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel(){

    private var _user_profile_data = MutableLiveData<UserProfileDto>()
    val user_profile_data: LiveData<UserProfileDto> get() = _user_profile_data

    private var _user_profile_edit_result_data = MutableLiveData<ResultOfSetDto>()
    val user_profile_edit_result_data: LiveData<ResultOfSetDto> get() = _user_profile_edit_result_data

    private var _profile_photo = MutableLiveData<Uri>()
    val profile_photo: LiveData<Uri> get() = _profile_photo

    private var _default_profile_photo=MutableLiveData<Int>()
    val default_profile_photo:LiveData<Int>get() = _default_profile_photo

    fun set_profile_photo(photo: Uri){
        _profile_photo.value = photo
    }

    fun set_default_photo(def:Int){
        _default_profile_photo.value=def
    }
    fun get_user_profile_data(){
        UserService.userService.get_user_profile_data().enqueue(object :
            Callback<UserProfileDto> {
            override fun onResponse(call: Call<UserProfileDto>, response: Response<UserProfileDto>) {
                if(response.isSuccessful) {
                    _user_profile_data.postValue(response.body())
                }else{
                    Log.d("whatisthis","_user_profile_data, 실패")
                }
            }

            override fun onFailure(call: Call<UserProfileDto>, t: Throwable) {
                Log.d("whatisthis","get_user_profile_data, 네트워크 오류가 발생했습니다."+ t.message.toString())
            }
        })
    }

    fun set_user_profile_data(userUpdateDtoDto: UserProfileEditDto){
        UserService.userService.set_user_profile_data(userUpdateDtoDto).enqueue(object :
            Callback<ResultOfSetDto> {
            override fun onResponse(call: Call<ResultOfSetDto>, response: Response<ResultOfSetDto>) {
                if(response.isSuccessful) {
                    _user_profile_edit_result_data.postValue(response.body())
                }else{
                    Log.d("whatisthis","_user_profile_edit_result_data, 실패")
                }
            }

            override fun onFailure(call: Call<ResultOfSetDto>, t: Throwable) {
                Log.d("whatisthis","set_user_profile_data, 네트워크 오류가 발생했습니다."+ t.message.toString())
            }
        })
    }

}