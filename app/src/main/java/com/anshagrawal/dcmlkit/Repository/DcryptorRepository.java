package com.anshagrawal.dcmlkit.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.anshagrawal.dcmlkit.Dao.DcryptorDao;
import com.anshagrawal.dcmlkit.Database.DcryptorDatabase;
import com.anshagrawal.dcmlkit.Models.Dcryptor;

import java.util.List;

public class DcryptorRepository {

    public DcryptorDao dcryptorDao;
    public LiveData<List<Dcryptor>> getallCyphers;

    public DcryptorRepository(Application application){
        DcryptorDatabase database = DcryptorDatabase.getDatabaseInstance(application);
        dcryptorDao = database.dcryptorDao();
        getallCyphers = dcryptorDao.getallCyphers();
    }

    public void insertCyphers(Dcryptor dcryptor){
        dcryptorDao.insertCyphers(dcryptor);
    }

    public void deleteCyphers(int id){
        dcryptorDao.deleteCypher(id);
    }

    public void updateCyphers(Dcryptor dcryptor){
        dcryptorDao.updateCyphers(dcryptor);
    }
}
