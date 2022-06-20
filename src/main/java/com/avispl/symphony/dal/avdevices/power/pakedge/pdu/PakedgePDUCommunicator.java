/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.power.pakedge.pdu;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.CollectionUtils;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.communicator.TelnetCommunicator;

/**
 * Pakedge PDU Adapter
 *
 * @author Kevin / Symphony Dev Team<br>
 * Created on 6/20/2022
 * @since 1.0.0
 */
public class PakedgePDUCommunicator extends TelnetCommunicator implements Monitorable, Controller {

	/**
	 * Store previous/current ExtendedStatistics
	 */
	private ExtendedStatistics localExtendedStatistics;

	/**
	 * ReentrantLock to prevent telnet session is closed when adapter is retrieving statistics from the device.
	 */
	private final ReentrantLock reentrantLock = new ReentrantLock();

	/**
	 * Prevent case where {@link PakedgePDUCommunicator#controlProperty(ControllableProperty)} slow down -
	 * the getMultipleStatistics interval if it's fail to send the cmd
	 */
	private static final int controlTelnetTimeout = 3000;

	/**
	 * Set back to default timeout value in {@link TelnetCommunicator}
	 */
	private static final int statisticsTelnetTimeout = 30000;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("PakedgePDUCommunicator: Perform getMultipleStatistics()");
		}
		ExtendedStatistics extendedStatistics = new ExtendedStatistics();
		reentrantLock.lock();
		try {
			this.timeout = controlTelnetTimeout;
		} finally {
			this.timeout = statisticsTelnetTimeout;
			reentrantLock.unlock();
		}
		localExtendedStatistics = extendedStatistics;
		return Collections.singletonList(localExtendedStatistics);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlProperty(ControllableProperty controllableProperty) throws Exception {
		String property = controllableProperty.getProperty();
		String propertyValue = String.valueOf(controllableProperty.getValue());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Perform control operation with property: %s and value: %s", property, propertyValue));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlProperties(List<ControllableProperty> list) throws Exception {
		if (CollectionUtils.isEmpty(list)) {
			throw new IllegalArgumentException("Controllable properties cannot be null or empty");
		}
		for (ControllableProperty controllableProperty : list) {
			controlProperty(controllableProperty);
		}
	}
}