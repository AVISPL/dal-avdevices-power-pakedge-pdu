/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * ControllingMetric  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/6/2022
 * @since 1.0.0
 */
public enum ControllingMetric {

	PDU("PDU"),
	OUTLET_AUTO_PING("OutletAutoPing"),
	OUTLET_SCHEDULER_EVENT("OutletScheduler"),
	OUTLET_CONF("OutletConfig"),
	ALTER_OUTLET("AlertOutlet"),
	ALTER_GLOBAL("AlertGlobal"),
	ALTER_EMAIL("AlertEmail"),
	RESET_PEAK("ResetPeakAction"),
	POWER_STATUS("OutletStatus"),
	TEMP_UNIT("TemperatureUnit"),
	DELETE_EVENT("DeleteEvent"),
	CREATE_EVENT("CreateEvent");

	public final String name;

	/**
	 * MonitoringMetric instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	ControllingMetric(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	public static ControllingMetric getMetricByValue(String value) {
		for (ControllingMetric metric : ControllingMetric.values()) {
			if (value.contains(metric.getName())) {
				return metric;
			}
		}
		throw new IllegalStateException("The metric not supported: " + value);
	}
}