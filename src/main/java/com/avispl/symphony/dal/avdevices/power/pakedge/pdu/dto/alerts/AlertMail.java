/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts;

/**
 * AlertMail class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class AlertMail {

	private String recipient;
	private String subject;

	/**
	 * Retrieves {@code {@link #recipient}}
	 *
	 * @return value of {@link #recipient}
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * Sets {@code recipient}
	 *
	 * @param recipient the {@code java.lang.String} field
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * Retrieves {@code {@link #subject}}
	 *
	 * @return value of {@link #subject}
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets {@code subject}
	 *
	 * @param subject the {@code java.lang.String} field
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
}