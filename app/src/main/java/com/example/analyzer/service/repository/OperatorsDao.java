package com.example.analyzer.service.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.analyzer.service.model.Operator;

import java.util.List;

@Dao
public interface OperatorsDao {
    @Query("SELECT * FROM operators")
    List<Operator> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Operator> operators);

    @Query("DELETE FROM operators")
    void clear();
}