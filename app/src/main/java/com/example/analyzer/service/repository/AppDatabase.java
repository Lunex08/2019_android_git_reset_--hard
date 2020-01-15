package com.example.analyzer.service.repository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.analyzer.service.model.Operator;
import com.example.analyzer.service.model.TariffDataset;

@Database(entities = {Operator.class, TariffDataset.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OperatorsDao operatorsDao();
    public abstract TariffsDao tariffsDao();
}