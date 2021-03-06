/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * PDUDisplayEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum PDUDisplayEnum {

	TEMPERATURE_UNIT("TemperatureUnit"),
	LED("LedState"),
	OLED_ENABLE("OledState"),
	APPLY_CHANGE("ApplyChange"),
	CANCEL_CHANGE("CancelChange"),
	OLED_CONTRAST("OledContrast");

	public final String name;

	/**
	 * PDUDisplayEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	PDUDisplayEnum(String name) {
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