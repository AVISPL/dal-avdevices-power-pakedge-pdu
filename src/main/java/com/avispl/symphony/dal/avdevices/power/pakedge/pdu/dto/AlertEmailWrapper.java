/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertEMail;

/**
 * AlertMailWrapper class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class AlertEmailWrapper {

	private String status;
	private AlertEMail response;

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
	 * Retrieves {@code {@link #response}}
	 *
	 * @return value of {@link #response}
	 */
	public AlertEMail getResponse() {
		return response;
	}

	/**
	 * Sets {@code response}
	 *
	 * @param response the {@code com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertMail} field
	 */
	public void setResponse(AlertEMail response) {
		this.response = response;
	}
}