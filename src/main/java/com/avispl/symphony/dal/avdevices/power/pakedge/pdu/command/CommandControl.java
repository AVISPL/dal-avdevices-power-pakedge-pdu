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

	SET_PDU_DISPLAY("set pdu-display"),
	SET_ALERT_EMAIL("set alerts-email"),
	SET_ALERT_GLOBAL("set alerts-global"),
	SET_ALERT_OUTLET("set alerts-outlet"),
	SET_OUTLET_CONFIG("set outlet-conf"),
	SET_OUTLET_AUTO_PING("set outlet-auto-ping"),
	SET_OUTLET_SCHEDULER("set outlet-scheduler"),
	;

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