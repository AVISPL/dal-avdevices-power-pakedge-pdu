/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * OutletAction  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public enum OutletAction {

	TURN_ON("TurnOn"),
	TURN_OFF("TurnOff"),
	DELETE("Delete"),
	REBOOT("Reboot");

	public final String name;

	/**
	 * OutletAction instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OutletAction(String name) {
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