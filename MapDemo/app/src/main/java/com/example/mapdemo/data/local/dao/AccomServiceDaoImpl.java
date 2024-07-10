package com.example.mapdemo.data.local.dao;

import com.example.mapdemo.helper.RealmHelper;
import com.example.mapdemo.data.model.AccomService;

import io.realm.Realm;

public class AccomServiceDaoImpl implements AccomServiceDao {
    private Realm realm;
    private RealmHelper realmHelper;
    public AccomServiceDaoImpl(RealmHelper realmHelper){
        this.realmHelper = realmHelper;
        this.realm = realmHelper.getRealm();
    }
    @Override
    public void addOrUpdateAccomService(AccomService accomService) {
        realm.executeTransactionAsync(r -> {
            r.copyToRealmOrUpdate(accomService);
        });
    }

    @Override
    public void deleteAccomService(String idAccom) {
        realm.executeTransactionAsync(r -> {
            AccomService accommodation = r.where(AccomService.class).equalTo("idAccom", idAccom).findFirst();
            if (accommodation != null) {
                accommodation.deleteFromRealm();
            }
        });
    }

    @Override
    public AccomService getAccomnServiceById(String idAccom) {
        AccomService realmResults = realm.where(AccomService.class).equalTo("idAccom", idAccom).findFirst();
        String id =  realmResults.getDescription();
        return realmResults;
    }
}
