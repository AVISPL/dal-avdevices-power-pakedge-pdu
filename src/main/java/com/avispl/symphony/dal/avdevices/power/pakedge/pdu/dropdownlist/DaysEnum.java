/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * DaysEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public enum DaysEnum {

	MON("Mon"),
	TUES("Tue"),
	WED("Wed"),
	THU("Thu"),
	FRI("Fri"),
	SAT("Sat"),
	SUN("Sun");

	public final String name;

	/**
	 * DaysEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	DaysEnum(String name) {
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