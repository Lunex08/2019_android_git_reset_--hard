package com.example.analyzer.service.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.analyzer.service.model.Post;

import java.util.List;

@Dao
public interface TariffsDao {
    @Query("SELECT * FROM tariffs")
    List<Post> getLast();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Post> tariffs);
}