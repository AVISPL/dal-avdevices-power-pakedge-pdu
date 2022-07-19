/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * AlertTypeEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public enum AlertTypeEnum {

	EMAIL("Email"),
	BUZZER("Buzzer"),
	DISABLE("Disable");

	public final String name;

	/**
	 * AlertTypeEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	AlertTypeEnum(String name) {
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