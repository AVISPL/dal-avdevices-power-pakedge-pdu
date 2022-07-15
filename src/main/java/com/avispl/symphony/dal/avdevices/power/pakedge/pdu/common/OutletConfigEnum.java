/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * OutletConfigEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum OutletConfigEnum {

	OUTLET_STATUS("OutletStatus"),
	RESET_PEAK("ResetPeakAction"),
	NAME("OutletName"),
	LOCAL_REBOOT("LocalRebootState"),
	POWER_ON_DELAY("PowerOnDelay"),
	POWER_OFF_DELAY("PowerOffDelay"),
	APPLY_CHANGE("ApplyChange"),
	CANCEL("CancelChange");

	public final String name;

	/**
	 * OutletConfigEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OutletConfigEnum(String name) {
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