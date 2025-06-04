// ResetPasswordResponse.java
package com.fitlife.api;
public class ResetPasswordResponse {
    public boolean success;
    public String message;
    public ResetPasswordResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
