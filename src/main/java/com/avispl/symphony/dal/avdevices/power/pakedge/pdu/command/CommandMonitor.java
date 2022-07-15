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

	DEVICE_INFO("show device-info"),
	UPTIME("show uptime"),
	TIME("show time"),
	TIME_ZONE("show timezone"),
	NET("show net"),
	PDU_STATUS("show pdu-status"),
	OUTLET_STATUS("show outlet-status -o "),
	ALERTS_EMAIL("show alerts-email"),
	PDU_DISPLAY("show pdu-display"),
	OUTLET_CONF("show outlet-conf -o "),
	ALTER_GLOBAL("show alerts-global"),
	ALTER_OUTLET("show alerts-outlet -o "),
	OUTLET_AUTO_PING("show outlet-auto-ping -o "),
	OUTLET_SCHEDULER_EVENT("show outlet-scheduler -o ");

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