/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.networkinfo;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.NetworkInfoEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;

/**
 * NetworkInformation class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class NetworkInformation {

	private String type;
	private String ip;
	private String netmask;
	private String gateway;
	private String dns;

	/**
	 * Retrieves {@code {@link #type}}
	 *
	 * @return value of {@link #type}
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets {@code type}
	 *
	 * @param type the {@code java.lang.String} field
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Retrieves {@code {@link #ip}}
	 *
	 * @return value of {@link #ip}
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets {@code ip}
	 *
	 * @param ip the {@code java.lang.String} field
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Retrieves {@code {@link #netmask}}
	 *
	 * @return value of {@link #netmask}
	 */
	public String getNetmask() {
		return netmask;
	}

	/**
	 * Sets {@code netmask}
	 *
	 * @param netmask the {@code java.lang.String} field
	 */
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	/**
	 * Retrieves {@code {@link #gateway}}
	 *
	 * @return value of {@link #gateway}
	 */
	public String getGateway() {
		return gateway;
	}

	/**
	 * Sets {@code gateway}
	 *
	 * @param gateway the {@code java.lang.String} field
	 */
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	/**
	 * Retrieves {@code {@link #dns}}
	 *
	 * @return value of {@link #dns}
	 */
	public String getDns() {
		return dns;
	}

	/**
	 * Sets {@code dns}
	 *
	 * @param dns the {@code java.lang.String} field
	 */
	public void setDns(String dns) {
		this.dns = dns;
	}

	/**
	 * Get the value by the metric monitoring
	 *
	 * @param metric the metric is metric monitoring
	 * @return String value of encoder monitoring properties by metric
	 */
	public String getValueByMetric(NetworkInfoEnum metric) {
		switch (metric) {
			case TYPE:
				return getType();
			case IP:
				return getIp();
			case NETMASK:
				return getNetmask();
			case GATEWAY:
				return getGateway();
			case DNS:
				return getDns();
			default:
				return PDUConstant.NONE;
		}
	}
}