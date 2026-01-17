package edu.dei.examination.phd.dto;

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object errors;

    public ApiResponse() {}

    public ApiResponse(boolean success, String message, T data, Object errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(true, msg, data, null);
    }

//    public static ApiResponse<?> error(String msg, Object errors) {
//        return new ApiResponse<>(false, msg, null, errors);
//    }
    
    public static  <T>ApiResponse<T> error(String msg, Object errors) {
        return new ApiResponse<>(false, msg, null, errors);
    }

	public boolean isSuccess() { return success; }

	public void setSuccess(boolean success) { this.success = success; }

	public String getMessage() { return message; }

	public void setMessage(String message) { this.message = message; }

	public T getData() { return data; }

	public void setData(T data) { this.data = data; }

	public Object getErrors() { return errors; }

	public void setErrors(Object errors) { this.errors = errors; }

    // getters & setters
    
    
}

