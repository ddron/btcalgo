package com.algo.btce.templates;

public class LoginTemplate {
    private int success;
    private String error;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginTemplate{");
        sb.append("success=").append(success);
        sb.append(", error='").append(error).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

