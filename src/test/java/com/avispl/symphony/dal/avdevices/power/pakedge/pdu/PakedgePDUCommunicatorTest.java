/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandControl;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.ControllingMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.MonitoringMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PakedgePDUUtil;

;

class PakedgePDUCommunicatorTest {
	private PakedgePDUCommunicator pakedgePDUCommunicator;
	MockedStatic<PakedgePDUUtil> mock = Mockito.mockStatic(PakedgePDUUtil.class);

	@BeforeEach()
	public void setUp() throws Exception {
		pakedgePDUCommunicator = new PakedgePDUCommunicator();
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.DEVICE_INFO)).thenReturn("show device-info");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.UPTIME)).thenReturn("show uptime");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.START_TIME)).thenReturn("show time");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.TIMEZONE)).thenReturn("show timezone");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.NETWORK_INFORMATION)).thenReturn("show net");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.PDU_STATUS)).thenReturn("show pdu-status");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.PDU_DISPLAY)).thenReturn("show pdu-display");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.OUTLET_STATUS)).thenReturn("show outlet-status -o ");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.ALTER_EMAIL)).thenReturn("show alerts-email");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.OUTLET_CONF)).thenReturn("show outlet-conf -o ");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.ALTER_GLOBAL)).thenReturn("show alerts-global");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.ALTER_OUTLET)).thenReturn("show alerts-outlet -o ");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.OUTLET_AUTO_PING)).thenReturn("show outlet-auto-ping -o ");
		mock.when(() -> PakedgePDUUtil.getMonitorCommand(MonitoringMetric.OUTLET_SCHEDULER_EVENT)).thenReturn("show outlet-scheduler -o ");
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
		mock.close();
	}

	/**
	 * Test get statistics: with device info
	 *
	 * @throws Exception When fail to getMultipleStatistics
	 */
	@Test
	@Tag("RealDevice")
	void testGetMultipleStatisticsWithDeviceSystemInfo() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals("22  minute(s)  51  second(s)", stats.get("Uptime"));
		Assertions.assertEquals("America/Los Angeles", stats.get("Timezone"));
		Assertions.assertEquals("11:00:00", stats.get("Time"));
		Assertions.assertEquals("PE-09NC10101", stats.get("SerialNumber"));
		Assertions.assertEquals("PE-09N", stats.get("Model"));
		Assertions.assertEquals("90:A7:C1:53:00:6", stats.get("MAC"));
		Assertions.assertEquals("Pakedge-PE-09N-5300069", stats.get("HostName"));
		Assertions.assertEquals("1.0.0", stats.get("FirmwareVersion"));
		Assertions.assertEquals("wed 05-11-2012", stats.get("Date"));
	}

	/**
	 * Test get statistics: with network information
	 *
	 * @throws Exception When fail to getMultipleStatistics
	 */
	@Test
	@Tag("RealDevice")
	void testGetMultipleStatisticsWithNetworkInformation() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals("dhcp", stats.get("Type"));
		Assertions.assertEquals("255.255.255.0", stats.get("Netmask"));
		Assertions.assertEquals("192.168.1.110", stats.get("IP"));
		Assertions.assertEquals("192.168.1.99", stats.get("Gateway"));
		Assertions.assertEquals("8.8.8.8", stats.get("DNS"));
	}

	/**
	 * Test get statistics: PDU status
	 *
	 * @throws Exception When fail to getMultipleStatistics
	 */
	@Test
	@Tag("RealDevice")
	void testMonitoringDataWithPDUStatus() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals("0", stats.get("PDU#Alerts"));
		Assertions.assertEquals("0.15", stats.get("PDU#Current(A)"));
		Assertions.assertEquals("60", stats.get("PDU#Frequency(Hz)"));
		Assertions.assertEquals("41", stats.get("PDU#Humidity(%)"));
		Assertions.assertEquals("79", stats.get("PDU#Temperature"));
		Assertions.assertEquals("119", stats.get("PDU#Voltage(V)"));
	}

	/**
	 * Test get statistics: Outlet config status
	 *
	 * @throws Exception When fail to getMultipleStatistics
	 */
	@Test
	@Tag("RealDevice")
	void testMonitoringDataWithOutletConfigStatus() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		Assertions.assertEquals("0", stats.get("OutletConfig1#Alerts"));
		Assertions.assertEquals("0.00", stats.get("OutletConfig1#Current(A)"));
		Assertions.assertEquals("0.02", stats.get("OutletConfig1#Peak(A)"));
		Assertions.assertEquals("0", stats.get("OutletConfig1#Power(W)"));
	}
// Control----------------------------------------------------------------------------------------------------------------------


	/**
	 * Test control : Alert Email
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAllMetricOfAlertEmail() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("AlertEmail#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertEmail#Recipient";
		String propValue = "Test";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertEmail#Edited"));

		propName = "AlertEmail#Subject";
		propValue = "";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertEmail#Edited"));

		propName = "AlertEmail#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("AlertEmail#Edited"));
	}

	/**
	 * Test control : Alert Email
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAllMetricOfAlertGlobalIsDropdownList() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertGlobal#CurrentAlert";
		String propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#PowerAlert";
		propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#VoltageAlert";
		propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#TemperatureAlert";
		propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));
	}

	/**
	 * Test control : Alert Email
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAllMetricOfAlertGlobalIsTextOrNumeric() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertGlobal#CurrentMax";
		String propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#CurrentMin";
		propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#PowerMax";
		propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#TemperatureMax";
		propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#TemperatureMin";
		propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#VoltageMax";
		propValue = "50";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		propName = "AlertGlobal#VoltageMin";
		propValue = "50";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));
	}

	/**
	 * Test control : Save all change for Alert Email
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlApplyChange() throws Exception {
		testControlAllMetricOfAlertGlobalIsDropdownList();
		testControlAllMetricOfAlertGlobalIsTextOrNumeric();
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("True", stats.get("AlertGlobal#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertGlobal#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);

		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("AlertGlobal#Edited"));
	}

	/**
	 * Test control : all metric of Alert Outlet
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAlertOutlet() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("AlertOutlet1#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertOutlet1#CurrentAlertType";
		String propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#PowerAlertType";
		propValue = "Buzzer";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#CurrentMax";
		propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#CurrentMin";
		propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#PowerMax";
		propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#PowerMin";
		propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "AlertOutlet1#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);

		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("AlertOutlet1#Edited"));
	}

	/**
	 * Test control : All metric of Outlet Auto Ping
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlOutletAutoPing() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletAutoPing1#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletAutoPing1#Attempts";
		String propValue = "20";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Destination";
		propValue = "10.0.0.8";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Interval";
		propValue = "5";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Limit";
		propValue = "5";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Notification";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#OutletState";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Period";
		propValue = "6";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#Timeout";
		propValue = "6";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletAutoPing1#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletAutoPing1#Edited"));
	}

	/**
	 * Test control : All metric of Outlet Config
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlOutletConfig() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletConfig1#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletConfig1#LocalRebootState";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#OutletName";
		propValue = "Pakedge PDU";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#OutletStatus";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#PowerOffDelay";
		propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#PowerOnDelay";
		propValue = "15";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#ResetPeakAction";
		propValue = "";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletConfig1#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletConfig1#Edited"));
	}

	/**
	 * Test control : All metric of Outlet Scheduler Events
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlOutletSchedulerEvents() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletScheduler1Events#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletScheduler1Events#Action";
		String propValue = "TurnOff";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletScheduler1Events#Day0";
		propValue = "Tue";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletScheduler1Events#EventID";
		propValue = "2";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletScheduler1Events#Time";
		propValue = "01:00";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "OutletScheduler1Events#AddDay";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("Mon", stats.get("OutletScheduler1Events#Day1"));

		propName = "OutletScheduler1Events#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("OutletScheduler1Events#Edited"));
	}

	/**
	 * Test control : All metric of PDU Display
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlPDUDisplay() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("PDU#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "PDU#LedState";
		String propValue = "Disable";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "PDU#OledContrast";
		propValue = "100";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "PDU#OledState";
		propValue = "0";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "PDU#TemperatureUnit";
		propValue = "0";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "PDU#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("PDU#Edited"));
	}

	/**
	 * Test control : create Scheduler Event
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlCreateNewSchedulerEvent() throws Exception {
		ExtendedStatistics extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		Map<String, String> stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("CreateEvent#Edited"));

		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "CreateEvent#Action";
		String propValue = "TurnOff";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "CreateEvent#Day0";
		propValue = "Tue";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "CreateEvent#OutletID";
		propValue = "2";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "CreateEvent#StartTime";
		propValue = "01:00";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals(propValue, stats.get(propName));

		propName = "CreateEvent#AddDay";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("Mon", stats.get("CreateEvent#Day1"));

		propName = "CreateEvent#CreateEvent";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);
		extendedStatistics = (ExtendedStatistics) pakedgePDUCommunicator.getMultipleStatistics().get(0);
		stats = extendedStatistics.getStatistics();
		Assertions.assertEquals("False", stats.get("CreateEvent#Edited"));
	}

	/**
	 * Test control : Apply change Alert Email
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAlertMailIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.ALTER_EMAIL)).thenReturn(CommandControl.SET_ALERT_EMAIL.getName() + " error");
		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertEmail#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because can't set data");
	}

	/**
	 * Test control : Apply change PDU
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlPDUIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.PDU)).thenReturn(CommandControl.SET_PDU_DISPLAY.getName() + " error");
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.TEMP_UNIT)).thenReturn(CommandControl.SET_TEMP_UNIT.getName() + " error");
		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "PDU#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change Alert Global
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAlertGlobalIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.ALTER_GLOBAL)).thenReturn(CommandControl.SET_ALERT_GLOBAL.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertGlobal#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change Alert Outlet
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlAlertOutletIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.ALTER_OUTLET)).thenReturn(CommandControl.SET_ALERT_OUTLET.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "AlertOutlet1#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change Alert Outlet
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlOutletConfigIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_CONF)).thenReturn(CommandControl.SET_OUTLET_CONFIG.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletConfig1#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change power status
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlPowerStatusIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_CONF)).thenReturn(CommandControl.SET_OUTLET_CONFIG.getName());
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.POWER_STATUS)).thenReturn(CommandControl.SET_POWER_STATUS.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletConfig1#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change action reset peak
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlActionResetPeakIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_CONF)).thenReturn(CommandControl.SET_OUTLET_CONFIG.getName());
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.POWER_STATUS)).thenReturn(CommandControl.SET_POWER_STATUS.getName());
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.RESET_PEAK)).thenReturn(CommandControl.SET_RESET_PEAK.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletConfig1#ResetPeakAction";
		String propValue = "Reset";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		pakedgePDUCommunicator.controlProperty(controllableProperty);

		propName = "OutletConfig1#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change OutletAutoPing
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlOutletAutoPingIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_AUTO_PING)).thenReturn(CommandControl.SET_OUTLET_AUTO_PING.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletAutoPing#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change SchedulerEvent
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlSchedulerEventIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_SCHEDULER_EVENT)).thenReturn(CommandControl.SET_OUTLET_SCHEDULER.getName() + " error");

		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletScheduler1Events#ApplyChange";
		String propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}

	/**
	 * Test control : Apply change Delete SchedulerEvent
	 *
	 * @throws Exception When fail to getMultipleStatistics or ControllableProperty
	 */
	@Test
	@Tag("RealDevice")
	void testControlDeleteSchedulerEventIsFailed() throws Exception {
		mock.when(() -> PakedgePDUUtil.getControlCommand(ControllingMetric.OUTLET_SCHEDULER_EVENT)).thenReturn(CommandControl.SET_OUTLET_SCHEDULER.getName() + " error");
		pakedgePDUCommunicator.getMultipleStatistics();
		ControllableProperty controllableProperty = new ControllableProperty();
		String propName = "OutletScheduler1Events#Action";
		String propValue = "Delete";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		propName = "OutletScheduler1Events#ApplyChange";
		propValue = "1";
		controllableProperty.setProperty(propName);
		controllableProperty.setValue(propValue);
		Assertions.assertThrows(ResourceNotReachableException.class, () -> pakedgePDUCommunicator.controlProperty(controllableProperty), "Expect failed because retrieve monitoring data failed");
	}
}