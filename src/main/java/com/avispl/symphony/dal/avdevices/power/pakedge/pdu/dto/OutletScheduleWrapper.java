/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto;


import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletSchedule;

/**
 * OutletScheduleWrapper class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class OutletScheduleWrapper {

	private String status;
	private OutletSchedule response;

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
	public OutletSchedule getResponse() {
		return response;
	}

	/**
	 * Sets {@code response}
	 *
	 * @param response the {@code com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletSchedule} field
	 */
	public void setResponse(OutletSchedule response) {
		this.response = response;
	}
}