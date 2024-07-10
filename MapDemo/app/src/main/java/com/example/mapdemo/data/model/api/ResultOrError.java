package com.example.mapdemo.data.model.api;

public class ResultOrError<T, E> {
    private T result;
    private E error;

    private ResultOrError(T result, E error) {
        this.result = result;
        this.error = error;
    }

    public static <T, E> ResultOrError<T, E> success(T result) {
        return new ResultOrError<>(result, null);
    }

    public static <T, E> ResultOrError<T, E> error(E error) {
        return new ResultOrError<>(null, error);
    }

    public T getResult() {
        return result;
    }

    public E getError() {
        return error;
    }

    public boolean isSuccess() {
        return result != null;
    }

    public T body() {
        return result;
    }

    public E errorBody() {
        return error;
    }
}
