/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

import java.util.Objects;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandMonitor;

/**
 * PakedgePDUUtil class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public class PakedgePDUUtil {

	/**
	 * Retrieves the command for monitoring and controlling process
	 *
	 * @param monitoringMetric is instance of MonitoringMetric
	 * @return String is full command for monitoring and controlling request
	 * @throws IllegalArgumentException if the name is not supported
	 */
	public static String getMonitorCommand(MonitoringMetric monitoringMetric) {
		Objects.requireNonNull(monitoringMetric);

		switch (monitoringMetric) {
			case UPTIME:
				return CommandMonitor.UPTIME.getName();
			case TIMEZONE:
				return CommandMonitor.TIME_ZONE.getName();
			case DEVICE_INFO:
				return CommandMonitor.DEVICE_INFO.getName();
			case PDU_STATUS:
				return CommandMonitor.PDU_STATUS.getName();
			case NETWORK_INFORMATION:
				return CommandMonitor.NET.getName();
			case START_TIME:
				return CommandMonitor.TIME.getName();
			case ALTER_EMAIL:
				return CommandMonitor.ALERTS_EMAIL.getName();
			case PDU_DISPLAY:
				return CommandMonitor.PDU_DISPLAY.getName();
			case OUTLET_CONF:
				return CommandMonitor.OUTLET_CONF.getName();
			case ALTER_GLOBAL:
				return CommandMonitor.ALTER_GLOBAL.getName();
			case ALTER_OUTLET:
				return CommandMonitor.ALTER_OUTLET.getName();
			case OUTLET_AUTO_PING:
				return CommandMonitor.OUTLET_AUTO_PING.getName();
			case OUTLET_STATUS:
				return CommandMonitor.OUTLET_STATUS.getName();
			case OUTLET_SCHEDULER_EVENT:
				return CommandMonitor.OUTLET_SCHEDULER_EVENT.getName();
			default:
				throw new IllegalArgumentException("Unsupported monitor command: " + monitoringMetric.name());
		}
	}

}