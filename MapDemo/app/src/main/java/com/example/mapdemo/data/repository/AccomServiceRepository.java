package com.example.mapdemo.data.repository;

import com.example.mapdemo.data.model.AccomService;

public interface AccomServiceRepository {
    void fetchServicesFromApi();
    void addOrUpdateAccomService(AccomService accomService);
    void deleteAccomService(String idAccom);
    void dispose();
    AccomService getAccServiceById(String idAccom);

}
