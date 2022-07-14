/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandControl;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUDisplayEnum;

/**
 * PDUDisplay class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PDUDisplay {

	private String led;

	@JsonAlias("temp_unit")
	private String tempUnit;

	@JsonAlias("oled_enabled")
	private String oledEnabled;

	@JsonAlias("oled_contrast")
	private String oledContrast;

	/**
	 * Retrieves {@code {@link #led}}
	 *
	 * @return value of {@link #led}
	 */
	public String getLed() {
		return led;
	}

	/**
	 * Sets {@code led}
	 *
	 * @param led the {@code java.lang.String} field
	 */
	public void setLed(String led) {
		this.led = led;
	}

	/**
	 * Retrieves {@code {@link #oledEnabled}}
	 *
	 * @return value of {@link #oledEnabled}
	 */
	public String getOledEnabled() {
		return oledEnabled;
	}

	/**
	 * Sets {@code oledEnabled}
	 *
	 * @param oledEnabled the {@code java.lang.String} field
	 */
	public void setOledEnabled(String oledEnabled) {
		this.oledEnabled = oledEnabled;
	}

	/**
	 * Retrieves {@code {@link #oledContrast}}
	 *
	 * @return value of {@link #oledContrast}
	 */
	public String getOledContrast() {
		return oledContrast;
	}

	/**
	 * Sets {@code oledContrast}
	 *
	 * @param oledContrast the {@code java.lang.String} field
	 */
	public void setOledContrast(String oledContrast) {
		this.oledContrast = oledContrast;
	}

	/**
	 * Retrieves {@code {@link #tempUnit }}
	 *
	 * @return value of {@link #tempUnit}
	 */
	public String getTempUnit() {
		return tempUnit;
	}

	/**
	 * Sets {@code temp_unit}
	 *
	 * @param tempUnit the {@code java.lang.String} field
	 */
	public void setTempUnit(String tempUnit) {
		this.tempUnit = tempUnit;
	}

	/***
	 * Build param request for the PDU Display
	 *
	 * @return String is param of PDU Display
	 */
	public String getParamRequestOfPDUDisplay() {
		//command control: set pdu-display -v <oled> -c <oled_contrast> -l <led_status>
		return CommandControl.SET_PDU_DISPLAY + String.format(PDUConstant.PARAM_PDU_DISPLAY, oledEnabled, oledContrast, led);
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(PDUDisplayEnum metric) {
		switch (metric) {
			case LED:
				return getLed();
			case OLED_CONTRAST:
				return getOledContrast();
			case OLED_ENABLE:
				return getOledEnabled();
			default:
				return PDUConstant.NONE;
		}
	}
}