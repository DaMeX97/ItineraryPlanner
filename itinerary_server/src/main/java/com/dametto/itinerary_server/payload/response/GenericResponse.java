package com.dametto.itinerary_server.payload.response;

public class GenericResponse<T> {

    private T data;
    private Boolean error = false;
    private String errorString = "";

    public GenericResponse(T data, Boolean error, String errorString) {
        this.data = data;
        this.error = error;
        this.errorString = errorString;
    }

    public GenericResponse(T data) {
        this.data = data;
        this.error = false;
        this.errorString = "";
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}
