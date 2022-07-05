/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.time;

/**
 * DateTimeResponse class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class DateTimeResponse {

	private String date;
	private String time;

	/**
	 * Retrieves {@code {@link #date}}
	 *
	 * @return value of {@link #date}
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets {@code date}
	 *
	 * @param date the {@code java.lang.String} field
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Retrieves {@code {@link #time}}
	 *
	 * @return value of {@link #time}
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets {@code time}
	 *
	 * @param time the {@code java.lang.String} field
	 */
	public void setTime(String time) {
		this.time = time;
	}
}