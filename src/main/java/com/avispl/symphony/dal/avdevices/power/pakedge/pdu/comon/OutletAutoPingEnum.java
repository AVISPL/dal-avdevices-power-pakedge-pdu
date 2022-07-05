/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon;

/**
 * OutletAutoPingEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum OutletAutoPingEnum {

	ENABLE("OutletState"),
	DESTINATION("Destination"),
	TIMEOUT("Timeout"),
	INTERVAL("Interval"),
	LIMIT("Limit"),
	PERIOD("Period"),
	ATTEMPTS("Attempts"),
	NOTIFICATION("Notification");

	public final String name;

	/**
	 * OutletAutoPingEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OutletAutoPingEnum(String name) {
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