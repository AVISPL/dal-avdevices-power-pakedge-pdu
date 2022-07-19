/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * PDUStatusEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum PDUStatusEnum {

	VOLTAGE("Voltage(V)"),
	CURRENT("Current(A)"),
	FREQUENCY("Frequency(Hz)"),
	TEMPERATURE("Temperature"),
	HUMIDITY("Humidity(%)"),
	ALERT("Alerts"),
	TEMPERATURE_UNIT("TemperatureUnit");

	public final String name;

	/**
	 * DeviceInfoMetric instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	PDUStatusEnum(String name) {
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