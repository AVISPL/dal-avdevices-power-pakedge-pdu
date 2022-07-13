/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * AlertMailEnum  class defined the enum for monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public enum AlertMailEnum {

	RECIPIENT("Recipient"),
	APPLY_CHANGE("ApplyChange"),
	CANCEL_CHANGE("CancelChange"),
	SUBJECT("Subject");

	public final String name;

	/**
	 * AlertMailEnum instantiation
	 *
	 * @param name {@code {@link #name}}
	 */
	AlertMailEnum(String name) {
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