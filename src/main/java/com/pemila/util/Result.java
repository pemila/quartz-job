package com.pemila.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 结果处理类
 * @author 月在未央
 * @date 2018年7月23日上午9:30:55	
 */
@Data
public class Result<T> {
	private int result;
	private String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;
	
	/** 请求成功时调用*/
	public static <T> Result<T> success(T data){
		return new Result<T>(0,"请求成功！",data);
	}
	/** 请求成功时调用*/
	public static <T> Result<T> success(){
		return new Result<T>(0,"请求成功！");
	}
	

	/** 请求失败时调用*/
	public static <T> Result<T> fail(String msg){
		return new Result<T>(1,msg);
	}

	private Result(T data){
		this.data = data;
	}
	
	private Result(int code, String msg) {
		this.result = code;
		this.message = msg;
	}
	
    private Result(int code, String msg, T t) {
        this.result = code;
        this.message = msg;
		this.data = t;
    }


}



