/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletAutoPingEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;

/**
 * OutletAutoPing class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class OutletAutoPing {

	private String id;
	private String enabled;
	private String destination;
	private String timeout;
	private String interval;
	private String limit;
	private String period;
	private String attempts;
	private String notification;
	private String status;

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
	 * Retrieves {@code {@link #enabled}}
	 *
	 * @return value of {@link #enabled}
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * Sets {@code enabled}
	 *
	 * @param enabled the {@code java.lang.String} field
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	/**
	 * Retrieves {@code {@link #destination}}
	 *
	 * @return value of {@link #destination}
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets {@code destination}
	 *
	 * @param destination the {@code java.lang.String} field
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

	/**
	 * Retrieves {@code {@link #timeout}}
	 *
	 * @return value of {@link #timeout}
	 */
	public String getTimeout() {
		return timeout;
	}

	/**
	 * Sets {@code timeout}
	 *
	 * @param timeout the {@code java.lang.String} field
	 */
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	/**
	 * Retrieves {@code {@link #interval}}
	 *
	 * @return value of {@link #interval}
	 */
	public String getInterval() {
		return interval;
	}

	/**
	 * Sets {@code interval}
	 *
	 * @param interval the {@code java.lang.String} field
	 */
	public void setInterval(String interval) {
		this.interval = interval;
	}

	/**
	 * Retrieves {@code {@link #limit}}
	 *
	 * @return value of {@link #limit}
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * Sets {@code limit}
	 *
	 * @param limit the {@code java.lang.String} field
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * Retrieves {@code {@link #period}}
	 *
	 * @return value of {@link #period}
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * Sets {@code period}
	 *
	 * @param period the {@code java.lang.String} field
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * Retrieves {@code {@link #attempts}}
	 *
	 * @return value of {@link #attempts}
	 */
	public String getAttempts() {
		return attempts;
	}

	/**
	 * Sets {@code attempts}
	 *
	 * @param attempts the {@code java.lang.String} field
	 */
	public void setAttempts(String attempts) {
		this.attempts = attempts;
	}

	/**
	 * Retrieves {@code {@link #notification}}
	 *
	 * @return value of {@link #notification}
	 */
	public String getNotification() {
		return notification;
	}

	/**
	 * Sets {@code notification}
	 *
	 * @param notification the {@code java.lang.String} field
	 */
	public void setNotification(String notification) {
		this.notification = notification;
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

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(OutletAutoPingEnum metric) {
		switch (metric) {
			case ATTEMPTS:
				return getAttempts();
			case DESTINATION:
				return getDestination();
			case OUTLET_STATE:
				return getEnabled();
			case INTERVAL:
				return getInterval();
			case LIMIT:
				return getLimit();
			case NOTIFICATION:
				return getNotification();
			case PERIOD:
				return getPeriod();
			case TIMEOUT:
				return getTimeout();
			default:
				return PDUConstant.NONE;
		}
	}
}