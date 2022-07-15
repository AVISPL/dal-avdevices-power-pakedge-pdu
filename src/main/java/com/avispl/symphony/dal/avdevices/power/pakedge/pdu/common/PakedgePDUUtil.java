/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

import java.util.Objects;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandControl;
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

	/**
	 * Retrieves the command for controlling process
	 *
	 * @param metric is instance of ControllingMetric
	 * @return String is full command for controlling request
	 * @throws IllegalArgumentException if the name is not supported
	 */
	public static String getControlCommand(ControllingMetric metric) {
		Objects.requireNonNull(metric);

		switch (metric) {
			case ALTER_GLOBAL:
				return CommandControl.SET_ALERT_GLOBAL.getName();
			case PDU:
				return CommandControl.SET_PDU_DISPLAY.getName();
			case CREATE_EVENT:
			case OUTLET_SCHEDULER_EVENT:
				return CommandControl.SET_OUTLET_SCHEDULER.getName();
			case ALTER_EMAIL:
				return CommandControl.SET_ALERT_EMAIL.getName();
			case ALTER_OUTLET:
				return CommandControl.SET_ALERT_OUTLET.getName();
			case OUTLET_AUTO_PING:
				return CommandControl.SET_OUTLET_AUTO_PING.getName();
			case OUTLET_CONF:
				return CommandControl.SET_OUTLET_CONFIG.getName();
			case RESET_PEAK:
				return CommandControl.SET_RESET_PEAK.getName();
			case POWER_STATUS:
				return CommandControl.SET_POWER_STATUS.getName();
			case TEMP_UNIT:
				return CommandControl.SET_TEMP_UNIT.getName();
			case DELETE_EVENT:
				return CommandControl.SET_DELETE_EVENT.getName();
			default:
				throw new IllegalArgumentException("Unsupported control command: " + metric.name());
		}
	}
}