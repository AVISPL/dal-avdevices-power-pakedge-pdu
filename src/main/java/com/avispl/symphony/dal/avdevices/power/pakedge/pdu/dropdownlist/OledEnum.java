/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * OledEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public enum OledEnum {

	ENABLE("Enable"),
	DISABLE("Disable"),
	DIM("Dim");

	public final String name;

	/**
	 * OledEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OledEnum(String name) {
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