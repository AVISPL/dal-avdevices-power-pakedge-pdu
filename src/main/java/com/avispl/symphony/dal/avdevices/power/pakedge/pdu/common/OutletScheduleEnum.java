/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

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
	ACTION("Action"),
	APPLY_CHANGE("ApplyChange"),
	CANCEL("CancelChange");

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