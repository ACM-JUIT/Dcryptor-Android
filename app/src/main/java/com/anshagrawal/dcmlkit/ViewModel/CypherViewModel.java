package com.anshagrawal.dcmlkit.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.anshagrawal.dcmlkit.Models.Dcryptor;
import com.anshagrawal.dcmlkit.Repository.DcryptorRepository;

import java.util.List;

public class CypherViewModel extends AndroidViewModel {

    public DcryptorRepository repository;
    public LiveData<List<Dcryptor>> getallCyphers;

    public CypherViewModel(@NonNull Application application) {
        super(application);

        repository = new DcryptorRepository(application);
        getallCyphers = repository.getallCyphers;

    }

    public void insertCypher(Dcryptor dcryptor) {
        repository.insertCyphers(dcryptor);
    }

    public void deleteCypher(int id) {
        repository.deleteCyphers(id);
    }

    public void updateCypher(Dcryptor dcryptor) {
        repository.updateCyphers(dcryptor);
    }
}
