package com.pami.exception;

public class InitWidgetException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4946991712840706971L;

	public InitWidgetException(String message) {
		super(message);
	}
	
	public InitWidgetException(Throwable e){
		super(e);
	}
	
	public InitWidgetException(String message, Throwable e){
		super(message, e);
	}
}
