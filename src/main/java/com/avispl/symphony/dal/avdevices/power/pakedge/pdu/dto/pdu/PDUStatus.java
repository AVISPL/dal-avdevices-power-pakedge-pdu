/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu;

import com.fasterxml.jackson.annotation.JsonAlias;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUStatusEnum;

/**
 * Info class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class PDUStatus {

	@JsonAlias("pdu_voltage")
	private String pduVoltage;

	@JsonAlias("pdu_frequency")
	private String pduFrequency;

	@JsonAlias("pdu_alert")
	private String pduAlert;

	@JsonAlias("pdu_current")
	private String pduCurrent;

	@JsonAlias("pdu_temp")
	private String pduTemp;

	@JsonAlias("pdu_temp_unit")
	private String pduTempUnit;

	@JsonAlias("pdu_humidity")
	private String pduHumidity;

	/**
	 * Retrieves {@code {@link #pduVoltage}}
	 *
	 * @return value of {@link #pduVoltage}
	 */
	public String getPduVoltage() {
		return pduVoltage;
	}

	/**
	 * Sets {@code pduVoltage}
	 *
	 * @param pduVoltage the {@code java.lang.String} field
	 */
	public void setPduVoltage(String pduVoltage) {
		this.pduVoltage = pduVoltage;
	}

	/**
	 * Retrieves {@code {@link #pduFrequency}}
	 *
	 * @return value of {@link #pduFrequency}
	 */
	public String getPduFrequency() {
		return pduFrequency;
	}

	/**
	 * Sets {@code pduFrequency}
	 *
	 * @param pduFrequency the {@code java.lang.String} field
	 */
	public void setPduFrequency(String pduFrequency) {
		this.pduFrequency = pduFrequency;
	}

	/**
	 * Retrieves {@code {@link #pduAlert}}
	 *
	 * @return value of {@link #pduAlert}
	 */
	public String getPduAlert() {
		return pduAlert;
	}

	/**
	 * Sets {@code pduAlert}
	 *
	 * @param pduAlert the {@code java.lang.String} field
	 */
	public void setPduAlert(String pduAlert) {
		this.pduAlert = pduAlert;
	}

	/**
	 * Retrieves {@code {@link #pduCurrent}}
	 *
	 * @return value of {@link #pduCurrent}
	 */
	public String getPduCurrent() {
		return pduCurrent;
	}

	/**
	 * Sets {@code pduCurrent}
	 *
	 * @param pduCurrent the {@code java.lang.String} field
	 */
	public void setPduCurrent(String pduCurrent) {
		this.pduCurrent = pduCurrent;
	}

	/**
	 * Retrieves {@code {@link #pduTemp}}
	 *
	 * @return value of {@link #pduTemp}
	 */
	public String getPduTemp() {
		return pduTemp;
	}

	/**
	 * Sets {@code pduTemp}
	 *
	 * @param pduTemp the {@code java.lang.String} field
	 */
	public void setPduTemp(String pduTemp) {
		this.pduTemp = pduTemp;
	}

	/**
	 * Retrieves {@code {@link #pduTempUnit}}
	 *
	 * @return value of {@link #pduTempUnit}
	 */
	public String getPduTempUnit() {
		return pduTempUnit;
	}

	/**
	 * Sets {@code pduTempUnit}
	 *
	 * @param pduTempUnit the {@code java.lang.String} field
	 */
	public void setPduTempUnit(String pduTempUnit) {
		this.pduTempUnit = pduTempUnit;
	}

	/**
	 * Retrieves {@code {@link #pduHumidity}}
	 *
	 * @return value of {@link #pduHumidity}
	 */
	public String getPduHumidity() {
		return pduHumidity;
	}

	/**
	 * Sets {@code pduHumidity}
	 *
	 * @param pduHumidity the {@code java.lang.String} field
	 */
	public void setPduHumidity(String pduHumidity) {
		this.pduHumidity = pduHumidity;
	}


	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(PDUStatusEnum metric) {
		switch (metric) {
			case VOLTAGE:
				return getPduVoltage();
			case CURRENT:
				return getPduCurrent();
			case FREQUENCY:
				return getPduFrequency();
			case TEMPERATURE:
				return getPduTemp();
			case HUMIDITY:
				return getPduHumidity();
			case ALERT:
				return getPduAlert();
			case TEMPERATURE_UNIT:
				return getPduTempUnit();
			default:
				return PDUConstant.NONE;
		}
	}

}