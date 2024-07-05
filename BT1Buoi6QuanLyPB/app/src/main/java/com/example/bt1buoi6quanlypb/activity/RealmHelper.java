package com.example.bt1buoi6quanlypb.activity;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmHelper {
    private Realm realm;
    public RealmHelper(Context context){
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("qlphongban1.realm")
                .schemaVersion(1)
                .build();
        realm = Realm.getDefaultInstance();
        Realm.setDefaultConfiguration(config);
    }
    public void closeRealm(){
        realm.close();
    }
    public Realm getRealm() {
        return realm;
    }
}
