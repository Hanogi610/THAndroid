package com.example.thandroid.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.thandroid.model.Work

@Dao
interface WorkDao {
    @Insert
    fun insert(work : Work) : Long

    @Query("SELECT * FROM work")
    fun getAllWork() : List<Work>

    @Query("DELETE FROM work WHERE id = :id")
    fun delete(id : Int)

    @Query("UPDATE work SET name = :name, content = :content, date = :date, status = :status, coop = :coop WHERE id = :id")
    fun updateWork(name : String, content : String, date : Long, status : String, coop : Boolean, id : Int)
}