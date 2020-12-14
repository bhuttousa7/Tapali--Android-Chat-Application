package com.dajjas.tapali;

public class Error  {

    private Integer code = null;
    private String message = null;

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String toString()  {
        StringBuilder sb = new StringBuilder();
        sb.append("class Error {\n");

        sb.append("  code: ").append(code).append("\n");
        sb.append("  message: ").append(message).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
