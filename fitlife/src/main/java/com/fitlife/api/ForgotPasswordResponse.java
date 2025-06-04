// ForgotPasswordResponse.java
package com.fitlife.api;
public class ForgotPasswordResponse {
    public boolean success;
    public String message;
    public ForgotPasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
