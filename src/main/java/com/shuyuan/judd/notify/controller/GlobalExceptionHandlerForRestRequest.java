package com.shuyuan.judd.notify.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import spring.shuyuan.judd.base.model.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice(basePackages={"com.shuyuan.judd.channel.ldlj"})
public class GlobalExceptionHandlerForRestRequest extends ResponseEntityExceptionHandler{
	private static final Logger logger = LoggerFactory.getLogger( GlobalExceptionHandlerForRestRequest.class );
	
	@ExceptionHandler(value={Exception.class, Throwable.class})
	@ResponseBody
	Response handlerAllException(HttpServletRequest request, Throwable ex, HttpServletResponse response){
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		logger.error("internal error", ex);
//		response.setStatus(500);
		return Response.createFail("internalErr");
	}
	
}