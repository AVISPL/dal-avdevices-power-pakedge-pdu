/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUDisplayEnum;

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