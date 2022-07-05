/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.deviceinfo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.DeviceInfoEmum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;

/**
 * DeviceInfoResponse class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInfoResponse {

	private String model;
	private String hostname;

	@JsonAlias("firmware_version")
	private String firmwareVersion;

	@JsonAlias("serial_number")
	private String serialNumber;

	@JsonAlias("MAC")
	private String mac;

	/**
	 * Retrieves {@code {@link #model}}
	 *
	 * @return value of {@link #model}
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets {@code model}
	 *
	 * @param model the {@code java.lang.String} field
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Retrieves {@code {@link #hostname}}
	 *
	 * @return value of {@link #hostname}
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Sets {@code hostname}
	 *
	 * @param hostname the {@code java.lang.String} field
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Retrieves {@code {@link #firmwareVersion}}
	 *
	 * @return value of {@link #firmwareVersion}
	 */
	public String getFirmwareVersion() {
		return firmwareVersion;
	}

	/**
	 * Sets {@code firmwareVersion}
	 *
	 * @param firmwareVersion the {@code java.lang.String} field
	 */
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}

	/**
	 * Retrieves {@code {@link #serialNumber}}
	 *
	 * @return value of {@link #serialNumber}
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets {@code serialNumber}
	 *
	 * @param serialNumber the {@code java.lang.String} field
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Retrieves {@code {@link #mac}}
	 *
	 * @return value of {@link #mac}
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * Sets {@code mac}
	 *
	 * @param mac the {@code java.lang.String} field
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(DeviceInfoEmum metric) {
		switch (metric) {
			case SERIAL_NUMBER:
				return getSerialNumber();
			case FIRMWARE_VERSION:
				return getFirmwareVersion();
			case HOSTNAME:
				return getHostname();
			case MAC:
				return getMac();
			case MODEL:
				return getModel();
			default:
				return PDUConstant.NONE;
		}
	}
}