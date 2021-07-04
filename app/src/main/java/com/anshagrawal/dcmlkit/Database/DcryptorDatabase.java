package com.anshagrawal.dcmlkit.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.anshagrawal.dcmlkit.Dao.DcryptorDao;
import com.anshagrawal.dcmlkit.Models.Dcryptor;

@Database(entities = {Dcryptor.class}, version = 1)
public abstract class DcryptorDatabase extends RoomDatabase {
    public abstract DcryptorDao dcryptorDao();
    public static DcryptorDatabase INSTANCE;

    public static DcryptorDatabase getDatabaseInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    DcryptorDatabase.class, "Dcryptor_Database").build();
        }
            return INSTANCE;
    }
}
