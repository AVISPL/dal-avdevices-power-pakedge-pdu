/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.AlertOutletEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.ControllingMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.EnumTypeHandler;

/**
 * AlertOutlet class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlertOutlet {

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

	private String id;

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

	/***
	 * Build param request for the AlertOutlet
	 *
	 * @return String is param of AlertOutlet
	 */
	public String getParamRequestOfAlertOutlet() {
		// set alerts-outlet -o <outletNo> -a <currentMin> -b <currentMax> -c <alertTtype> -d
		//<powMin> -e <powMax> -f <alertTtype>
		return PakedgePDUUtil.getControlCommand(ControllingMetric.ALTER_OUTLET) + String.format(PDUConstant.PARAM_ALERT_OUTLET, id, currentMin, currentMax,
				EnumTypeHandler.getValueByStringArray(getCurrentAlert()), powerMin,
				powerMax, EnumTypeHandler.getValueByStringArray(getPowerAlert()));
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(AlertOutletEnum metric) {
		switch (metric) {
			case CURRENT_ALERT:
				return EnumTypeHandler.getValueByStringArray(getCurrentAlert());
			case CURRENT_MIN:
				return getCurrentMin();
			case CURRENT_MAX:
				return getCurrentMax();
			case POWER_MIN:
				return getPowerMin();
			case POWER_MAX:
				return getPowerMax();
			case POWER_ALERT:
				return EnumTypeHandler.getValueByStringArray(getPowerAlert());
			default:
				return PDUConstant.NONE;
		}
	}
}