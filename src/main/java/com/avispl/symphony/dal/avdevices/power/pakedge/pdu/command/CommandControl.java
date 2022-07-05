/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command;

/**
 * CommandControl class defined the enum for controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum CommandControl {

	MAC("MAC");

	public final String name;

	/**
	 * CommandControl instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	CommandControl(String name) {
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