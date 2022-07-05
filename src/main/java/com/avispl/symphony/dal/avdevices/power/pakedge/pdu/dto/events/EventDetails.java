/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletScheduleEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;

/**
 * EventDetails class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class EventDetails {

	private String id;
	private String time;
	private String[] days;
	private String action;

	/**
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.String} field
	 */
	public void setId(String id) {
		this.id = id;
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

	/**
	 * Retrieves {@code {@link #days}}
	 *
	 * @return value of {@link #days}
	 */
	public String getDays() {
		String result = "";
		for (String day : days) {
			result = result + day;
		}
		return result;
	}

	/**
	 * Sets {@code days}
	 *
	 * @param days the {@code java.lang.String[]} field
	 */
	public void setDays(String[] days) {
		this.days = days;
	}

	/**
	 * Retrieves {@code {@link #action}}
	 *
	 * @return value of {@link #action}
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Sets {@code action}
	 *
	 * @param action the {@code java.lang.String} field
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(OutletScheduleEnum metric) {
		switch (metric) {
			case ACTION:
				return getAction();
			case DAYS:
				return getDays();
			case ID:
				return getId();
			case TIME:
				return getTime();
			default:
				return PDUConstant.NONE;
		}
	}
}