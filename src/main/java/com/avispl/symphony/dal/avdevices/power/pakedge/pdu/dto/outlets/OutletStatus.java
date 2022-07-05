/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletStatusEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;

/**
 * OutletStatus class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OutletStatus {

	private String status;
	private String power;
	private String current;
	private String peak;

	@JsonAlias("is_alert")
	private String isAlert;

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

	/**
	 * Retrieves {@code {@link #power}}
	 *
	 * @return value of {@link #power}
	 */
	public String getPower() {
		return power;
	}

	/**
	 * Sets {@code power}
	 *
	 * @param power the {@code java.lang.String} field
	 */
	public void setPower(String power) {
		this.power = power;
	}

	/**
	 * Retrieves {@code {@link #current}}
	 *
	 * @return value of {@link #current}
	 */
	public String getCurrent() {
		return current;
	}

	/**
	 * Sets {@code current}
	 *
	 * @param current the {@code java.lang.String} field
	 */
	public void setCurrent(String current) {
		this.current = current;
	}

	/**
	 * Retrieves {@code {@link #peak}}
	 *
	 * @return value of {@link #peak}
	 */
	public String getPeak() {
		return peak;
	}

	/**
	 * Sets {@code peak}
	 *
	 * @param peak the {@code java.lang.String} field
	 */
	public void setPeak(String peak) {
		this.peak = peak;
	}

	/**
	 * Retrieves {@code {@link #isAlert}}
	 *
	 * @return value of {@link #isAlert}
	 */
	public String getIsAlert() {
		return isAlert;
	}

	/**
	 * Sets {@code isAlert}
	 *
	 * @param isAlert the {@code java.lang.String} field
	 */
	public void setIsAlert(String isAlert) {
		this.isAlert = isAlert;
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(OutletStatusEnum metric) {
		switch (metric) {
			case CURRENT:
				return getCurrent();
			case IS_ALERTS:
				return getIsAlert();
			case STATUS:
				return getStatus();
			case PEAK:
				return getPeak();
			case POWER:
				return getPower();
			default:
				return PDUConstant.NONE;
		}
	}
}