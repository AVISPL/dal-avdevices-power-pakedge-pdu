/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.networkinfo.NetworkInformation;

/**
 * NetWorkWrapper class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class NetWorkWrapper {

	private String status;
	private NetworkInformation response;

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
	public NetworkInformation getResponse() {
		return response;
	}

	/**
	 * Sets {@code response}
	 *
	 * @param response the {@code com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.networkinfo.NetworkInformation} field
	 */
	public void setResponse(NetworkInformation response) {
		this.response = response;
	}
}