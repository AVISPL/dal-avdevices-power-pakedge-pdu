/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * OledContrastEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/4/2022
 * @since 1.0.0
 */
public enum OledContrastEnum {

	OLED_20("20"),
	OLED_50("50"),
	OLED_75("75"),
	OLED_100("100");

	public final String name;

	/**
	 * OledContrastEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	OledContrastEnum(String name) {
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