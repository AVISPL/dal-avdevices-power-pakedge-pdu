/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.ControllingMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletScheduleEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.EnumTypeHandler;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * EventDetails class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class EventDetails {

	private String id;
	private String outletId;
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
	 * Retrieves {@code {@link #outletId}}
	 *
	 * @return value of {@link #outletId}
	 */
	public String getOutletId() {
		return outletId;
	}

	/**
	 * Sets {@code outletId}
	 *
	 * @param outletId the {@code java.lang.String} field
	 */
	public void setOutletId(String outletId) {
		this.outletId = outletId;
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
	public String[] getDays() {
		return days;
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

	/***
	 * Build param request for the Event Details
	 *
	 * @return String is param of Event Details
	 */
	public String getParamRequestOfEventDetails() {
		//set outlet-scheduler -o <outletNo> [-r<eventID>] -s <start_time> -d <days> -a <action>
		String eventId = String.format("-r %s", id);
		if (StringUtils.isNullOrEmpty(id)) {
			eventId = PDUConstant.EMPTY_STRING;
		}
		String day = EnumTypeHandler.getValueByStringArray(days);
		if (StringUtils.isNullOrEmpty(day)) {
			day = PDUConstant.QUOTATION_MARKS;
		}
		return PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_SCHEDULER_EVENT) + String.format(PDUConstant.PARAM_OUTLET_EVENT, outletId, eventId, time, day, action);
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
			case EVENT_ID:
				return getId();
			case TIME:
				return getTime();
			default:
				return PDUConstant.NONE;
		}
	}
}