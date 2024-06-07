package com.geosys.common.exception;

import com.geosys.common.utils.Messages;
import com.geosys.common.utils.R;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * 异常处理器
 */
@RestControllerAdvice
public class CostomExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(CostomException.class)
	public R handleRRException(CostomException e){
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public R handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return R.error(404, Messages.get("x_handler_error"));
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error(Messages.get("x_email_exists"));
	}

	@ExceptionHandler(AuthorizationException.class)
	public R handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return R.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public R handleIllegalArgumentException(IllegalArgumentException e) {
		logger.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error();
	}

	@ExceptionHandler(MailSendException.class)
	public R handleMailSendException(MailSendException e) {
		logger.error(e.getMessage(), e);
		return R.error(Messages.get("x_mail_send_failed"));
	}

	@ExceptionHandler(MailAuthenticationException.class)
	public R handleMailAuthenticationException(MailAuthenticationException e) {
		logger.error(e.getMessage());
		return R.error(Messages.get("x_mail_authentication_failed"));
	}

	/**
	 * 统一的异常处理 无法直接处理SQLException及其子类异常
	 * 原因：1、SQLException及其子类异常的最底层的异常是org.springframework.daoDataAccessException的子类，
	 * 		   说明是属于spring dao 层处理的异常类
	 * 		2、而Spring的dao为了统一处理，屏蔽了与特定技术相关的异常,例如SQLException或HibernateException,
	 * 	       抛出的异常是与特定技术无关的org.springframework.dao.DataAccessException类的子类。
	 *
	 *      3、统一的异常处理中使用的是spring的注解，导致现在无法直接捕获处理SQLException；
	 *     	  所以要去处理DataAccessException
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DataAccessException.class)
	public R handlerPSQLException(DataAccessException e) {
		String info = e.getCause().getMessage();
		logger.error(e.getMessage(), e);
		return R.error(200, info);
	}
}
