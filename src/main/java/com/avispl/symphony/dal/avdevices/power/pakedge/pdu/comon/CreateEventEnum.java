/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon;

/**
 * CreateEventEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/5/2022
 * @since 1.0.0
 */
public enum CreateEventEnum {

	OUTLET_ID("OutletID"),
	START_TIME("StartTime"),
	DAY_0("Day0"),
	ADD_DAY("AddDay"),
	CREATE_EVENT("CreateEvent"),
	CANCEL("CancelChange"),
	ACTION("Action");

	public final String name;

	/**
	 * CreateEventEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	CreateEventEnum(String name) {
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