/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;

class PakedgePDUCommunicatorTest {
	private PakedgePDUCommunicator pakedgePDUCommunicator;

	@BeforeEach()
	public void setUp() throws Exception {
		pakedgePDUCommunicator = new PakedgePDUCommunicator();
		pakedgePDUCommunicator.setHost("127.0.0.1");
		pakedgePDUCommunicator.setPort(23);
		pakedgePDUCommunicator.setLogin("su");
		pakedgePDUCommunicator.setPassword("12345");
		pakedgePDUCommunicator.init();
		pakedgePDUCommunicator.connect();
	}

	@AfterEach()
	public void destroy() throws Exception {
		pakedgePDUCommunicator.disconnect();
	}

	/**
	 * Test get statistics: with System info
	 *
	 * @throws Exception When fail to getMultipleStatistics
	 */
	@Test
	@Tag("RealDevice")
	void testGetMultipleStatisticsWithSystemInfo() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String property ="OutletScheduler1Events#AddDay";
		String value = "1";
		controllableProperty.setProperty(property);
		controllableProperty.setValue(value);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		 property ="OutletScheduler1Events#AddDay";
		 value = "1";
		controllableProperty.setProperty(property);
		controllableProperty.setValue(value);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		property ="OutletScheduler1Events#AddDay";
		value = "1";
		controllableProperty.setProperty(property);
		controllableProperty.setValue(value);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		property ="OutletScheduler1Events#AddDay";
		value = "1";
		controllableProperty.setProperty(property);
		controllableProperty.setValue(value);
		pakedgePDUCommunicator.controlProperty(controllableProperty);

	}
}