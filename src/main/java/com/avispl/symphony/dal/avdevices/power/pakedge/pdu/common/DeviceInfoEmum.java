/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * DeviceInfoMetric  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum DeviceInfoEmum {

	MODEL("Model"),
	HOSTNAME("HostName"),
	FIRMWARE_VERSION("FirmwareVersion"),
	SERIAL_NUMBER("SerialNumber"),
	MAC("MAC");

	public final String name;

	/**
	 * DeviceInfoMetric instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	DeviceInfoEmum(String name) {
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