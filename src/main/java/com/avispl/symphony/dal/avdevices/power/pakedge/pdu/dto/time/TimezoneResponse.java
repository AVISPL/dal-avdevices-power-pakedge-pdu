/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.time;

/**
 * TimezoneResponse class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class TimezoneResponse {

	private String timezone;

	/**
	 * Retrieves {@code {@link #timezone}}
	 *
	 * @return value of {@link #timezone}
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * Sets {@code timezone}
	 *
	 * @param timezone the {@code java.lang.String} field
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
}