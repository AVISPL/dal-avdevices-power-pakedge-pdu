/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto;

/**
 * ResponseControlDataWrapper class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/8/2022
 * @since 1.0.0
 */
public class ResponseControlDataWrapper {

	private String status;
	private String msg;

	/**
	 * Retrieves {@code {@link #status}}
	 *
	 * @return value of {@link #status}
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets {@code status}
	 *
	 * @param status the {@code java.lang.String} field
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Retrieves {@code {@link #msg}}
	 *
	 * @return value of {@link #msg}
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * Sets {@code msg}
	 *
	 * @param msg the {@code java.lang.String} field
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}