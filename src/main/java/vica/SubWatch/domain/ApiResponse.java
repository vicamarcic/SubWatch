package vica.SubWatch.domain;

public class ApiResponse {
    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiResponse(String message) {
        this.message = message;
    }
}
