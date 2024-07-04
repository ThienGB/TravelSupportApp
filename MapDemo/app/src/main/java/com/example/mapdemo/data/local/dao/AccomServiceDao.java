package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.data.model.AccomService;
import com.example.mapdemo.data.model.Accommodation;

import io.realm.RealmResults;

public interface AccomServiceDao {
    void addOrUpdateAccomService(AccomService accomService);
    void deleteAccomService(String idAccom);
    AccomService getAccomnServiceById(String idAccom);
}
