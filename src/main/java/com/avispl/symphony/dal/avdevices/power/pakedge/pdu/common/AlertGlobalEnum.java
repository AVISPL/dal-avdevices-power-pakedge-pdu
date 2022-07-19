/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * AlertGlobalEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum AlertGlobalEnum {

	TEMP_SENSOR("TemperatureSensor"),
	TEMP_ALERT("TemperatureAlert"),
	TEMP_UNIT("TemperatureUnit"),
	TEMP_MAX("TemperatureMax"),
	TEMP_MIN("TemperatureMin"),
	POWER_MAX("PowerMax"),
	POWER_MIN("PowerMin"),
	POWER_ALERT("PowerAlert"),
	CURRENT_ALERT("CurrentAlert"),
	CURRENT_MAX("CurrentMax"),
	CURRENT_MIN("CurrentMin"),
	VOLTAGE_ALERT("VoltageAlert"),
	VOLTAGE_MAX("VoltageMax"),
	VOLTAGE_MIN("VoltageMin"),
	CANCEL("CancelChange"),
	APPLY_CHANGE("ApplyChange");

	public final String name;

	/**
	 * AlertGlobalEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	AlertGlobalEnum(String name) {
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
}