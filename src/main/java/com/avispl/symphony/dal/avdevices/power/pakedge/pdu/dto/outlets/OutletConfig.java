/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandControl;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletConfigEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;

/**
 * OutletConfig class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OutletConfig {

	private String name;
	private String id;
	private String status;

	@JsonAlias("local_reboot")
	private String localReboot;

	@JsonAlias("power_on_delay")
	private String powerOnDelay;

	@JsonAlias("power_off_delay")
	private String powerOffDelay;

	/**
	 * Retrieves {@code {@link #id}}
	 *
	 * @return value of {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets {@code id}
	 *
	 * @param id the {@code java.lang.String} field
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets {@code name}
	 *
	 * @param name the {@code java.lang.String} field
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #localReboot}}
	 *
	 * @return value of {@link #localReboot}
	 */
	public String getLocalReboot() {
		return localReboot;
	}

	/**
	 * Sets {@code localReboot}
	 *
	 * @param localReboot the {@code java.lang.String} field
	 */
	public void setLocalReboot(String localReboot) {
		this.localReboot = localReboot;
	}

	/**
	 * Retrieves {@code {@link #powerOnDelay}}
	 *
	 * @return value of {@link #powerOnDelay}
	 */
	public String getPowerOnDelay() {
		return powerOnDelay;
	}

	/**
	 * Sets {@code powerOnDelay}
	 *
	 * @param powerOnDelay the {@code java.lang.String} field
	 */
	public void setPowerOnDelay(String powerOnDelay) {
		this.powerOnDelay = powerOnDelay;
	}

	/**
	 * Retrieves {@code {@link #powerOffDelay}}
	 *
	 * @return value of {@link #powerOffDelay}
	 */
	public String getPowerOffDelay() {
		return powerOffDelay;
	}

	/**
	 * Sets {@code powerOffDelay}
	 *
	 * @param powerOffDelay the {@code java.lang.String} field
	 */
	public void setPowerOffDelay(String powerOffDelay) {
		this.powerOffDelay = powerOffDelay;
	}

	/**
	 * Retrieves {@code {@link #status}}
	 *
	 * @return value of {@link #status}
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets {@code status}
	 *
	 * @param status the {@code java.lang.String} field
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/***
	 * Build param request for the Outlet Config
	 *
	 * @return String is param of Outlet Config
	 */
	public String getParamRequestOfOutletConfig() {
		//set outlet-conf -o <outletNo> -n <name> -b <localReboot> -d <delay_on> -f <delay_off>
		return CommandControl.SET_OUTLET_CONFIG.getName() + String.format(PDUConstant.PARAM_OUTLET_CONFIG, id, name, localReboot, powerOnDelay, powerOffDelay);
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(OutletConfigEnum metric) {
		switch (metric) {
			case POWER_ON_DELAY:
				return getPowerOnDelay();
			case POWER_OFF_DELAY:
				return getPowerOffDelay();
			case LOCAL_REBOOT:
				return getLocalReboot();
			case NAME:
				return getName();
			default:
				return PDUConstant.NONE;
		}
	}
}