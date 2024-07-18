package com.example.mapdemo.helper;

public interface LoadingHelper {
    default void onLoadingStarted(){}

    default void onLoadingFinished(){}
}
