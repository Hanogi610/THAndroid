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

class ListViewModel : ViewModel() {
    val works = MutableLiveData<List<Work>>()

    fun getWorks(context: Context){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val instance = AppDatabase.getInstance(context)
                works.postValue(instance.workDao().getAllWork())
            }
        }
    }

    fun addWork(context: Context, work: Work):Job{
        return viewModelScope.launch {
            withContext(Dispatchers.IO){
                val instance = AppDatabase.getInstance(context)
                instance.workDao().insert(work)
            }
        }
    }

    fun deleteWork(context: Context, work: Work) : Job{
        return viewModelScope.launch {
            withContext(Dispatchers.IO){
                val instance = AppDatabase.getInstance(context)
                instance.workDao().delete(work.id)
            }
        }
    }

    fun updateWork(context: Context, work: Work) : Job{
        return viewModelScope.launch {
            withContext(Dispatchers.IO){
                val instance = AppDatabase.getInstance(context)
                instance.workDao().updateWork(work.name, work.content,work.date,work.status,work.coop,work.id)
            }
        }
    }
}