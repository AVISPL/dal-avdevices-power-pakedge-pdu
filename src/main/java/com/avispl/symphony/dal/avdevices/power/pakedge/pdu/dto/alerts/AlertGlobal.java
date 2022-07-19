/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.AlertGlobalEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.ControllingMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.EnumTypeHandler;

/**
 * AlertGlobal class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertGlobal {

	@JsonAlias("voltage_min")
	private String voltageMin;

	@JsonAlias("voltage_max")
	private String voltageMax;

	@JsonAlias("voltage_alert")
	private String[] voltageAlert;

	@JsonAlias("current_min")
	private String currentMin;

	@JsonAlias("current_max")
	private String currentMax;

	@JsonAlias("current_alert")
	private String[] currentAlert;

	@JsonAlias("power_min")
	private String powerMin;

	@JsonAlias("power_max")
	private String powerMax;

	@JsonAlias("power_alert")
	private String[] powerAlert;

	@JsonAlias("temp_min")
	private String tempMin;

	@JsonAlias("temp_max")
	private String tempMax;

	@JsonAlias("temp_unit")
	private String tempUnit;

	@JsonAlias("temp_sensor")
	private String tempSensor;

	@JsonAlias("temp_alert")
	private String[] tempAlert;

	/**
	 * Retrieves {@code {@link #voltageMin}}
	 *
	 * @return value of {@link #voltageMin}
	 */
	public String getVoltageMin() {
		return voltageMin;
	}

	/**
	 * Sets {@code voltageMin}
	 *
	 * @param voltageMin the {@code java.lang.String} field
	 */
	public void setVoltageMin(String voltageMin) {
		this.voltageMin = voltageMin;
	}

	/**
	 * Retrieves {@code {@link #voltageMax}}
	 *
	 * @return value of {@link #voltageMax}
	 */
	public String getVoltageMax() {
		return voltageMax;
	}

	/**
	 * Sets {@code voltageMax}
	 *
	 * @param voltageMax the {@code java.lang.String} field
	 */
	public void setVoltageMax(String voltageMax) {
		this.voltageMax = voltageMax;
	}

	/**
	 * Retrieves {@code {@link #voltageAlert}}
	 *
	 * @return value of {@link #voltageAlert}
	 */
	public String[] getVoltageAlert() {

		return voltageAlert;
	}

	/**
	 * Sets {@code voltageAlert}
	 *
	 * @param voltageAlert the {@code java.lang.String[]} field
	 */
	public void setVoltageAlert(String[] voltageAlert) {
		this.voltageAlert = voltageAlert;
	}

	/**
	 * Retrieves {@code {@link #currentMin}}
	 *
	 * @return value of {@link #currentMin}
	 */
	public String getCurrentMin() {
		return currentMin;
	}

	/**
	 * Sets {@code currentMin}
	 *
	 * @param currentMin the {@code java.lang.String} field
	 */
	public void setCurrentMin(String currentMin) {
		this.currentMin = currentMin;
	}

	/**
	 * Retrieves {@code {@link #currentMax}}
	 *
	 * @return value of {@link #currentMax}
	 */
	public String getCurrentMax() {
		return currentMax;
	}

	/**
	 * Sets {@code currentMax}
	 *
	 * @param currentMax the {@code java.lang.String} field
	 */
	public void setCurrentMax(String currentMax) {
		this.currentMax = currentMax;
	}

	/**
	 * Retrieves {@code {@link #currentAlert}}
	 *
	 * @return value of {@link #currentAlert}
	 */
	public String[] getCurrentAlert() {
		return currentAlert;
	}

	/**
	 * Sets {@code currentAlert}
	 *
	 * @param currentAlert the {@code java.lang.String[]} field
	 */
	public void setCurrentAlert(String[] currentAlert) {
		this.currentAlert = currentAlert;
	}

	/**
	 * Retrieves {@code {@link #powerMin}}
	 *
	 * @return value of {@link #powerMin}
	 */
	public String getPowerMin() {
		return powerMin;
	}

	/**
	 * Sets {@code powerMin}
	 *
	 * @param powerMin the {@code java.lang.String} field
	 */
	public void setPowerMin(String powerMin) {
		this.powerMin = powerMin;
	}

	/**
	 * Retrieves {@code {@link #powerMax}}
	 *
	 * @return value of {@link #powerMax}
	 */
	public String getPowerMax() {
		return powerMax;
	}

	/**
	 * Sets {@code powerMax}
	 *
	 * @param powerMax the {@code java.lang.String} field
	 */
	public void setPowerMax(String powerMax) {
		this.powerMax = powerMax;
	}

	/**
	 * Retrieves {@code {@link #powerAlert}}
	 *
	 * @return value of {@link #powerAlert}
	 */
	public String[] getPowerAlert() {
		return powerAlert;
	}

	/**
	 * Sets {@code powerAlert}
	 *
	 * @param powerAlert the {@code java.lang.String[]} field
	 */
	public void setPowerAlert(String[] powerAlert) {
		this.powerAlert = powerAlert;
	}

	/**
	 * Retrieves {@code {@link #tempMin}}
	 *
	 * @return value of {@link #tempMin}
	 */
	public String getTempMin() {
		return tempMin;
	}

	/**
	 * Sets {@code tempMin}
	 *
	 * @param tempMin the {@code java.lang.String} field
	 */
	public void setTempMin(String tempMin) {
		this.tempMin = tempMin;
	}

	/**
	 * Retrieves {@code {@link #tempMax}}
	 *
	 * @return value of {@link #tempMax}
	 */
	public String getTempMax() {
		return tempMax;
	}

	/**
	 * Sets {@code tempMax}
	 *
	 * @param tempMax the {@code java.lang.String} field
	 */
	public void setTempMax(String tempMax) {
		this.tempMax = tempMax;
	}

	/**
	 * Retrieves {@code {@link #tempUnit}}
	 *
	 * @return value of {@link #tempUnit}
	 */
	public String getTempUnit() {
		return tempUnit;
	}

	/**
	 * Sets {@code tempUnit}
	 *
	 * @param tempUnit the {@code java.lang.String} field
	 */
	public void setTempUnit(String tempUnit) {
		this.tempUnit = tempUnit;
	}

	/**
	 * Retrieves {@code {@link #tempSensor}}
	 *
	 * @return value of {@link #tempSensor}
	 */
	public String getTempSensor() {
		return tempSensor;
	}

	/**
	 * Sets {@code tempSensor}
	 *
	 * @param tempSensor the {@code java.lang.String} field
	 */
	public void setTempSensor(String tempSensor) {
		this.tempSensor = tempSensor;
	}

	/**
	 * Retrieves {@code {@link #tempAlert}}
	 *
	 * @return value of {@link #tempAlert}
	 */
	public String[] getTempAlert() {
		return tempAlert;
	}

	/**
	 * Sets {@code tempAlert}
	 *
	 * @param tempAlert the {@code java.lang.String[]} field
	 */
	public void setTempAlert(String[] tempAlert) {
		this.tempAlert = tempAlert;
	}

	/***
	 * Build param request for the AlertGlobal
	 *
	 * @return String is param of AlertGlobal
	 */
	public String getParamRequestOfAlertGlobal() {
		//set alerts-global - a <voltageMin> -b <voltageMax> -c <alertType> -d <powMin> -e <powMax>
		//-f <alertType> -l <tempMin> -h <tempMax> -t <alertType> -p <currentMin> -q <currentMax>
		//-r <alertType
		return PakedgePDUUtil.getControlCommand(ControllingMetric.ALTER_GLOBAL) +
				String.format(PDUConstant.PARAM_ALERT_GLOBAL, voltageMin, voltageMax, EnumTypeHandler.getValueByStringArray(getVoltageAlert()), powerMin, powerMax,
						EnumTypeHandler.getValueByStringArray(getPowerAlert()), tempMin, tempMax, EnumTypeHandler.getValueByStringArray(getTempAlert()), currentMin, currentMax,
						EnumTypeHandler.getValueByStringArray(getCurrentAlert()));
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(AlertGlobalEnum metric) {
		switch (metric) {
			case TEMP_SENSOR:
				return getTempSensor();
			case TEMP_ALERT:
				return EnumTypeHandler.getValueByStringArray(getTempAlert());
			case TEMP_UNIT:
				return getTempUnit();
			case TEMP_MAX:
				return getTempMax();
			case TEMP_MIN:
				return getTempMin();
			case POWER_MAX:
				return getPowerMax();
			case POWER_MIN:
				return getPowerMin();
			case POWER_ALERT:
				return EnumTypeHandler.getValueByStringArray(getPowerAlert());
			case CURRENT_ALERT:
				return EnumTypeHandler.getValueByStringArray(getCurrentAlert());
			case CURRENT_MAX:
				return getCurrentMax();
			case CURRENT_MIN:
				return getCurrentMin();
			case VOLTAGE_ALERT:
				return EnumTypeHandler.getValueByStringArray(getVoltageAlert());
			case VOLTAGE_MAX:
				return getVoltageMax();
			case VOLTAGE_MIN:
				return getVoltageMin();
			default:
				return PDUConstant.NONE;
		}
	}
}