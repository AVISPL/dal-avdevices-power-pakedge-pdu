/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets;

import java.util.List;

import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events.EventDetails;

/**
 * OutletSchedule class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 7/1/2022
 * @since 1.0.0
 */
public class OutletSchedule {

	private List<EventDetails> events;

	/**
	 * Retrieves {@code {@link #events}}
	 *
	 * @return value of {@link #events}
	 */
	public List<EventDetails> getEvents() {
		return events;
	}

	/**
	 * Sets {@code events}
	 *
	 * @param events the {@code java.util.List<com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events.EventDetails>} field
	 */
	public void setEvents(List<EventDetails> events) {
		this.events = events;
	}
}