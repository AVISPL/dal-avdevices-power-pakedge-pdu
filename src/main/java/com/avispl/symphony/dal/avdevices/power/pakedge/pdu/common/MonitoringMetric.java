/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * MonitoringMetric  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum MonitoringMetric {

	DEVICE_INFO("DeviceInfo"),
	TIMEZONE("Timezone"),
	START_TIME("Time"),
	UPTIME("Uptime"),
	NETWORK_INFORMATION("NetworkInformation"),
	PDU_DISPLAY("PDUDisplay"),
	PDU_STATUS("PDUStatus"),
	OUTLET_STATUS("OutletStatus"),
	OUTLET_AUTO_PING("OutletAutoPing"),
	OUTLET_SCHEDULER_EVENT("OutletScheduler"),
	OUTLET_CONF("OutletConfig"),
	ALTER_OUTLET("AlertOutlet"),
	ALTER_GLOBAL("AlertGlobal"),
	ALTER_EMAIL("AlertEmail"),
	;

	public final String name;

	/**
	 * MonitoringMetric instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	MonitoringMetric(String name) {
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