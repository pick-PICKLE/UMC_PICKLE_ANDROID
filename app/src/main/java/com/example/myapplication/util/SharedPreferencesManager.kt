package com.example.myapplication.util

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.ApplicationClass.Companion.SERVER_TOKEN
import com.example.myapplication.db.remote.model.search.SearchHistroyData
import com.example.myapplication.ApplicationClass.Companion.SHARED_SEARCH_HISTORY
import com.google.gson.Gson


class SharedPreferencesManager(context: Context) {

    // // 쉐어드 만들기
    // 검색 기록
    private var searchhistoryprefs: SharedPreferences =
        context.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)

    // jwt
    private var jwtprefs: SharedPreferences =
        context.getSharedPreferences(SERVER_TOKEN, Context.MODE_PRIVATE)

    // 검색 기록 데이터 출력
    fun getsearchhistoryString(key: String): MutableList<SearchHistroyData> {
        var storedSearchHistoryList = ArrayList<SearchHistroyData>()
        val storedSearchHistoryListString = searchhistoryprefs.getString(key, "")!!
        // 검색 목록에 값이 있다면
        if (storedSearchHistoryListString.isNotEmpty()) {
            // 저장된 문자열을 객체 배열로 변경
            storedSearchHistoryList =
                Gson().fromJson(storedSearchHistoryListString, Array<SearchHistroyData>::class.java)
                    .toMutableList() as ArrayList<SearchHistroyData>
        }
        return storedSearchHistoryList
    }

    // 검색 기록 데이터 저장
    fun setsearchhistoryString(key: String, searchHistoryList: MutableList<SearchHistroyData>) {
        // 매개변수로 들어온 배열을 문자열로 변환
        val storedSearchHistoryListString = Gson().toJson(searchHistoryList)
        // 쉐어드에 저장
        searchhistoryprefs.edit().putString(key, storedSearchHistoryListString).apply()
    }

    // 검색 기록 데이터 삭제
    fun deletesearchhistoryString() {
        searchhistoryprefs.edit().clear().apply()
    }

    // jwt 데이터 출력
    fun getJwt(key:String): String?= jwtprefs.getString(key, null)


    // jwt 데이터 저장
    fun setJwt(key : String, jwtToken: String) {
        jwtprefs.edit().putString(key, jwtToken).apply()
    }

    // jwt 데이터 삭제
    fun deleteJwt(){
        jwtprefs.edit().clear().apply()
    }

}