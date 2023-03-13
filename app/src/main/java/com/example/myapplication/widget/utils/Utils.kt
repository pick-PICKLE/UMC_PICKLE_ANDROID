package com.example.myapplication.widget.utils

import retrofit2.Retrofit

object Utils {
    const val SERVER_TOKEN: String = "server_token" // jwt 저장하는 sharedpref를 찾을 때 필요한 key값
    const val X_ACCESS_TOKEN: String = "x_access_token" // jwt 저장하는 sharedpref에서 jwt 찾을 때 필요한 key값
    const val SHARED_SEARCH_HISTORY = "shared_search_histroy" // 검색기록을 저장하는 sharedpref를 찾을 때 필요한 key값
    const val KEY_SEARCH_HISTORY = "key_search_history" // 검색기록을 저장하는 sharedpref에서 검색기록을 찾을 때 필요한 key값
    const val KAKAO_APP_KEY = "e9c2a8bf10ae12652fdc9ee9059ac02f" // 카카오 앱키
    const val BASE_URL = "https://pick-cle.shop/api/" // API 의 base_url

    lateinit var username: String
}