package com.anshagrawal.dcmlkit.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "Dcryptor_Database")

public class Dcryptor {
    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "cypher_title")
    public String cypherTitle;

    @ColumnInfo(name = "cypher_date")
    public String cypherDate;

    @ColumnInfo(name = "cypher_time")
    public String cypherTime;




}
