/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon;

/**
 * OutletStatusEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum OutletStatusEnum {

	STATUS("OutletStatus"),
	POWER("Power (W)"),
	CURRENT("Current (A)"),
	PEAK("Peak (A)"),
	IS_ALERTS("Alerts"),
	RESET_PEAK("ResetPeak");

	public final String name;

	/**
	 * OutletStatusEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OutletStatusEnum(String name) {
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