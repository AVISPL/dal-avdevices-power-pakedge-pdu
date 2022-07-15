/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * NetworkInfoEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/3/2022
 * @since 1.0.0
 */
public enum NetworkInfoEnum {

	TYPE("Type"),
	IP("IP"),
	NETMASK("Netmask"),
	GATEWAY("Gateway"),
	DNS("DNS");

	public final String name;

	/**
	 * NetworkInfoEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	NetworkInfoEnum(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@code {@link #name}}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}
}