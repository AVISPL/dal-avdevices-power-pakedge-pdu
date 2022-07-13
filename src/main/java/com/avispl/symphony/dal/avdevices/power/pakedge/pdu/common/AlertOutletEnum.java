/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * AlertsOutletEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum AlertOutletEnum {

	CURRENT_MIN("CurrentMin"),
	CURRENT_MAX("CurrentMax"),
	CURRENT_ALERT("CurrentAlertType"),
	POWER_MIN("PowerMin"),
	POWER_MAX("PowerMax"),
	POWER_ALERT("PowerAlertType"),
	APPLY_CHANGE("ApplyChange"),
	CANCEL("CancelChange");

	public final String name;

	/**
	 * AlertsOutletEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	AlertOutletEnum(String name) {
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