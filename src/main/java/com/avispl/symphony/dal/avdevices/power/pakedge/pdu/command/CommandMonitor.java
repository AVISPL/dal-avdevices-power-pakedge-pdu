/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command;

/**
 * CommandMonitor class defined the enum for monitoring process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public enum CommandMonitor {

	SHOW("show "),
	DEVICE_INFO("device-info"),
	UPTIME("uptime"),
	TIME("time"),
	TIME_ZONE("timezone"),
	NET("net"),
	PDU_STATUS("pdu-status"),
	OUTLET_STATUS("outlet-status -o "),
	ALERTS_EMAIL("alerts-email"),
	PDU_DISPLAY("pdu-display"),
	OUTLET_CONF("outlet-conf -o "),
	ALTER_GLOBAL("alerts-global"),
	ALTER_OUTLET("alerts-outlet -o "),
	OUTLET_AUTO_PING("outlet-auto-ping -o "),
	OUTLET_SCHEDULER_EVENT("outlet-scheduler -o ");

	public final String name;

	/**
	 * CommandMonitor instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	CommandMonitor(String name) {
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