package com.example.thandroid

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thandroid.database.AppDatabase
import com.example.thandroid.model.Work
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchStatisticViewModel : ViewModel() {
    val works = MutableLiveData<List<Work>>()

    fun getAllWorks(context: Context) : Job {
        return viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(context)
                works.postValue(db.workDao().getAllWork())
            }
        }
    }
}