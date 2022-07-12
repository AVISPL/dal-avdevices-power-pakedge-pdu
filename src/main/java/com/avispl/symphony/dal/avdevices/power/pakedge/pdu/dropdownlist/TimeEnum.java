/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * TimeEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/8/2022
 * @since 1.0.0
 */
public enum TimeEnum {

	HOUR_0("00:000"),
	HOUR_1("01:00"),
	HOUR_2("02:00"),
	HOUR_3("03:00"),
	HOUR_4("04:00"),
	HOUR_5("05:00"),
	HOUR_6("06:00"),
	HOUR_7("07:00"),
	HOUR_8("08:00"),
	HOUR_9("09:00"),
	HOUR_10("10:00"),
	HOUR_11("11:00"),
	HOUR_12("12:00"),
	HOUR_13("13:00"),
	HOUR_14("14:00"),
	HOUR_15("15:00"),
	HOUR_16("16:00"),
	HOUR_17("17:00"),
	HOUR_18("18:00"),
	HOUR_19("19:00"),
	HOUR_20("20:00"),
	HOUR_21("21:00"),
	HOUR_22("22:00"),
	HOUR_23("23:00");

	public final String name;

	/**
	 * TimeEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	TimeEnum(String name) {
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