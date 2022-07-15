package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist;

/**
 * ResetPeakEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/14/2022
 * @since 1.0.0
 */
public enum ResetPeakEnum {

	NONE("None"),
	MON("Reset");

	public final String name;

	/**
	 * ResetPeakEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	ResetPeakEnum(String name) {
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