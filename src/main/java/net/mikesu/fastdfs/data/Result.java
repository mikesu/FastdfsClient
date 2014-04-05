package net.mikesu.fastdfs.data;

public class Result<T> {

	private int code;
	private String message;
	private T data;
	
	public Result(int code) {
		super();
		this.code = code;
	}
	
	public Result(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public Result(int code, T data) {
		super();
		this.code = code;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
}
