/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon;

/**
 * OutletScheduleEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum OutletScheduleEnum {

	ID("EventID"),
	TIME("Time"),
	DAYS("Day0"),
	ADD_DAY("AddDay"),
	CREATE_EVENT("CreateEvent"),
	ACTION("Action");

	public final String name;

	/**
	 * OutletScheduleEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OutletScheduleEnum(String name) {
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