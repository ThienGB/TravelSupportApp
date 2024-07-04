package com.example.mapdemo.data.model.api;

import com.example.mapdemo.data.model.Place;

import java.util.List;

public class PlaceResponse {
    private List<Place> results;

    public List<Place> getResults() {
        return results;
    }

    public void setResults(List<Place> results) {
        this.results = results;
    }
}
