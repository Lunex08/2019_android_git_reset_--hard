package com.example.analyzer.service.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.analyzer.service.model.TariffDataset;

import java.util.List;

@Dao
public interface TariffsDao {
    @Query("SELECT * FROM tariffs")
    List<TariffDataset> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TariffDataset> tariffs);

    @Query("DELETE FROM tariffs")
    void clear();
}