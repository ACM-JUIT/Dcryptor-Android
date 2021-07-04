package com.anshagrawal.dcmlkit.Dao;


import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.anshagrawal.dcmlkit.Models.Dcryptor;

import java.util.List;

@androidx.room.Dao
public interface DcryptorDao {
    @Query("SELECT * FROM Dcryptor_Database")
    LiveData<List<Dcryptor>> getallCyphers();



    @Insert
    void insertCyphers(Dcryptor... cypher_title);

    @Query("DELETE FROM Dcryptor_Database WHERE id = :id")
    void deleteCypher(int id);

    @Update
    void updateCyphers(Dcryptor cypher_title);
}
