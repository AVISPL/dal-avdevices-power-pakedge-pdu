/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.power.pakedge.pdu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.DeviceInfoEmum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.MonitoringMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.NetworkInfoEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.DateAndTimeWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.NetWorkWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.TimeZoneWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.UptimeWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.deviceinfo.DeviceInfoResponse;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events.EventDetails;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.AlertOutlet;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletAutoPing;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletConfig;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletStatus;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu.PDUDisplay;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu.PDUStatus;
import com.avispl.symphony.dal.communicator.TelnetCommunicator;

/**
 * Pakedge PDU Adapter
 *
 * <p> Monitorable properties:</p>
 *
 * <ol>
 *   <li>Device Information</li>
 *   <li>Network Information</li>
 *   <li>Outlet Status</li>
 *   <li>DPU Status</li>
 * </ol>
 *
 * <p> Controllable properties: </p>
 *
 * 	<li>PDU Display</li>
 * 	<li>Outlet Config</li>
 * 	<li>Outlet Auto Ping</li>
 * 	<li>Outlet Auto Scheduler Event</li>
 * 	<li>Alert Email</li>
 * 	<li>Alert Global</li>
 * 	<li>Alert Outlet</li>
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/20/2022
 * @since 1.0.0
 */
public class PakedgePDUCommunicator extends TelnetCommunicator implements Monitorable, Controller {

	/**
	 * Prevent case where {@link PakedgePDUCommunicator#controlProperty(ControllableProperty)} slow down -
	 * the getMultipleStatistics interval if it's fail to send the cmd
	 */
	private static final int controlTelnetTimeout = 3000;
	/**
	 * Set back to default timeout value in {@link TelnetCommunicator}
	 */
	private static final int statisticsTelnetTimeout = 30000;
	private final Map<String, String> failedMonitor = new HashMap<>();
	private final String uuidDay = UUID.randomUUID().toString().replace(PDUConstant.DASH, PDUConstant.EMPTY_STRING);
	/**
	 * ReentrantLock to prevent telnet session is closed when adapter is retrieving statistics from the device.
	 */
	private final ReentrantLock reentrantLock = new ReentrantLock();
	ObjectMapper mapper = new ObjectMapper();
	private boolean isEmergencyDelivery;
	/**
	 * Store previous/current ExtendedStatistics
	 */
	private ExtendedStatistics localExtendedStatistics;
	private final List<PDUDisplay> pduDisplayList = new ArrayList<>();
	private final List<PDUStatus> pduStatusList = new ArrayList<>();
	private final List<OutletStatus> outletStatusList = new ArrayList<>();
	private final List<OutletAutoPing> outletAutoPingList = new ArrayList<>();
	private final Map<Integer, List<EventDetails>> outletSchedulerEventList = new HashMap<>();
	private final List<OutletConfig> outletSConfigList = new ArrayList<>();
	private final List<AlertOutlet> alertOutletList = new ArrayList<>();

	public PakedgePDUCommunicator() {
		this.setLoginPrompt("login as: \r\n\u001B8\u001B[3;14H");
		this.setPasswordPrompt("password: \r\n\u001B8\u001B[5;14H");
		this.setCommandSuccessList(
				Collections.singletonList("$ "));
		this.setCommandErrorList(Collections.singletonList("A"));
		this.setLoginSuccessList(Arrays.asList(
				"\u001B8\u001B[3;14H",
				"\r\n\u001B8\u001B[5;14H",
				".....\u001B[2J\u001B[HWelcome to Pakdge PDU.\r\n Type 'help' for more information\r\nPakedge$ "
		));

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("PakedgePDUCommunicator: Perform getMultipleStatistics()");
		}
		ExtendedStatistics extendedStatistics = new ExtendedStatistics();
		List<AdvancedControllableProperty> advancedControllableProperty = new ArrayList<>();
		Map<String, String> stats = new HashMap<>();
		reentrantLock.lock();
		try {
			this.timeout = controlTelnetTimeout;
			if (!isEmergencyDelivery) {
				clearBeforeFetchingData();
				populateInformationFromDevice(stats, advancedControllableProperty);
				createEvent(stats, advancedControllableProperty);
				extendedStatistics.setStatistics(stats);
				extendedStatistics.setControllableProperties(advancedControllableProperty);
				localExtendedStatistics = extendedStatistics;
			}
		} finally {
			this.timeout = statisticsTelnetTimeout;
			reentrantLock.unlock();
		}
		return Collections.singletonList(localExtendedStatistics);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlProperty(ControllableProperty controllableProperty) {
		String property = controllableProperty.getProperty();
		String value = String.valueOf(controllableProperty.getValue());
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Perform control operation with property: %s and value: %s", property, value));
		}
		reentrantLock.lock();
		try {
			this.timeout = controlTelnetTimeout;
			if (localExtendedStatistics == null) {
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Error while controlling %s metric", property));
				}
				return;
			}
		} finally {
			this.timeout = statisticsTelnetTimeout;
			reentrantLock.unlock();
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

	/**
	 * Clear data before fetching data
	 */
	private void clearBeforeFetchingData() {
		pduDisplayList.clear();
		pduStatusList.clear();
		outletStatusList.clear();
		outletAutoPingList.clear();
		outletSchedulerEventList.clear();
		outletSConfigList.clear();
		alertOutletList.clear();
	}

	/**
	 * Retrieve data and add to stats of the device
	 *
	 * @param stats list statistics property
	 * @param advancedControllableProperties the advancedControllableProperties is list AdvancedControllableProperties
	 */
	private void populateInformationFromDevice(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		for (MonitoringMetric metric : MonitoringMetric.values()) {
			retrieveDataByMetric(stats, metric);
			populateControl(metric, stats, advancedControllableProperties);
		}
	}

	/**
	 * Populate control for the device
	 *
	 * @param metric list metric of device
	 * @param stats list statistics property
	 * @param advancedControllableProperties the advancedControllableProperties is list AdvancedControllableProperties
	 */
	private void populateControl(MonitoringMetric metric, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		Objects.requireNonNull(metric);
		Objects.requireNonNull(stats);
		Objects.requireNonNull(advancedControllableProperties);
		//TODO
	}


	/**
	 * Retrieve data from the device
	 *
	 * @param metric list metric of device
	 * @param stats stats list statistics property
	 * @throws IllegalArgumentException if the name is not supported
	 */
	private void retrieveDataByMetric(Map<String, String> stats, MonitoringMetric metric) {
		Objects.requireNonNull(metric);

		switch (metric) {
			case DEVICE_INFO:
				retrieveDeviceInfo(metric, stats);
				break;
			case NETWORK_INFORMATION:
				retrieveNetworkInformation(metric, stats);
				break;
			case TIMEZONE:
				retrieveTimezone(metric, stats);
				break;
			case UPTIME:
				retrieveUptime(metric, stats);
				break;
			case TIME:
				retrieveDateAndTime(metric, stats);
				break;
			case ALTER_EMAIL:
			case ALTER_GLOBAL:
			case PDU_DISPLAY:
			case PDU_STATUS:
				populateRetrieveDataByMetric(metric, 0);
				break;
			case OUTLET_CONF:
			case ALTER_OUTLET:
			case OUTLET_STATUS:
			case OUTLET_AUTO_PING:
			case OUTLET_SCHEDULER_EVENT:
				for (int i = 1; i <= PDUConstant.OUTLET_INDEX; i++) {
					populateRetrieveDataByMetric(metric, i);
				}
				break;
			default:
				throw new IllegalArgumentException("Do not support PDU monitoring metric is: " + metric.name());
		}
	}

	/**
	 * populate retrieve data by metric
	 *
	 * @param outletID the outletId is ID of Outlet
	 * @param metric list metric of device
	 */
	private void populateRetrieveDataByMetric(MonitoringMetric metric, int outletID) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		if (outletID != 0) {
			request = request + outletID;
		}
		try {
			String responseData = send(request);
			String result = responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n"));
			retrieveDataDetails(result, metric);
		} catch (Exception e) {
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Retrieve data by metric
	 *
	 * @param responseData is data get to device
	 * @param metric the metric is instance MonitoringMetric
	 * @throws IllegalArgumentException if the name is not supported
	 */
	private void retrieveDataDetails(String responseData, MonitoringMetric metric) {
		Objects.requireNonNull(responseData);
		try {
			switch (metric) {
				case NETWORK_INFORMATION:
				case DEVICE_INFO:
				case UPTIME:
				case TIME:
				case TIMEZONE:
				case ALTER_EMAIL:
				case ALTER_GLOBAL:
				case ALTER_OUTLET:
				case PDU_STATUS:
				case PDU_DISPLAY:
				case OUTLET_SCHEDULER_EVENT:
				case OUTLET_AUTO_PING:
				case OUTLET_STATUS:
				case OUTLET_CONF:
				default:
					throw new IllegalArgumentException("Do not support monitoring metric: : " + metric.name());
			}
		} catch (Exception e) {
			logger.error("Error while convert data: ", e);
		}
	}

	/**
	 * Retrieve device info of the Pakedge PDU
	 *
	 * @param stats list statistics property
	 * @param metric the metric instance in MonitoringMetric
	 */
	private void retrieveDeviceInfo(MonitoringMetric metric, Map<String, String> stats) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		try {
			String responseData = send(request);
			DeviceInfoWrapper deviceInfoWrapper = mapper.readValue(responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n")), DeviceInfoWrapper.class);
			for (DeviceInfoEmum metricDeviceInfo : DeviceInfoEmum.values()) {
				DeviceInfoResponse deviceInfoWrapperResponse = deviceInfoWrapper.getResponse();
				stats.put(metricDeviceInfo.getName(), deviceInfoWrapperResponse.getValueByMetric(metricDeviceInfo));
			}
		} catch (Exception e) {
			//handle None value if retrieve data failed
			for (DeviceInfoEmum metricDeviceInfo : DeviceInfoEmum.values()) {
				stats.put(metricDeviceInfo.getName(), PDUConstant.NONE);
			}
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Retrieve timezome of the Pakedge PDU
	 *
	 * @param stats list statistics property
	 * @param metric the metric instance in MonitoringMetric
	 */
	private void retrieveTimezone(MonitoringMetric metric, Map<String, String> stats) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		try {
			String responseData = send(request);
			TimeZoneWrapper timeZoneWrapper = mapper.readValue(responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n")), TimeZoneWrapper.class);
			stats.put(MonitoringMetric.TIMEZONE.getName(), timeZoneWrapper.getResponse().getTimezone());
		} catch (Exception e) {
			stats.put(MonitoringMetric.TIMEZONE.getName(), PDUConstant.NONE);
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Retrieve time and date of the Pakedge PDU
	 *
	 * @param stats list statistics property
	 * @param metric the metric instance in MonitoringMetric
	 */
	private void retrieveDateAndTime(MonitoringMetric metric, Map<String, String> stats) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		try {
			String responseData = send(request);
			DateAndTimeWrapper dateAndTimeWrapper = mapper.readValue(responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n")), DateAndTimeWrapper.class);
			stats.put(PDUConstant.TIME, dateAndTimeWrapper.getResponse().getTime());
			stats.put(PDUConstant.DATE, dateAndTimeWrapper.getResponse().getDate());
		} catch (Exception e) {
			stats.put(PDUConstant.TIME, PDUConstant.NONE);
			stats.put(PDUConstant.DATE, PDUConstant.NONE);
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Retrieve uptime of the Pakedge PDU
	 *
	 * @param stats list statistics property
	 * @param metric the metric instance in MonitoringMetric
	 */
	private void retrieveUptime(MonitoringMetric metric, Map<String, String> stats) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		try {
			String responseData = send(request);
			UptimeWrapper uptimeWrapper = mapper.readValue(responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n")), UptimeWrapper.class);
			stats.put(MonitoringMetric.UPTIME.getName(), formatTimeData(uptimeWrapper.getResponse().getUptime()));
		} catch (Exception e) {
			stats.put(MonitoringMetric.UPTIME.getName(), PDUConstant.NONE);
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Retrieve network information of the Pakedge PDU
	 *
	 * @param stats list statistics property
	 * @param metric the metric instance in MonitoringMetric
	 */
	private void retrieveNetworkInformation(MonitoringMetric metric, Map<String, String> stats) {
		String request = PakedgePDUUtil.getMonitorCommand(metric);
		try {
			String responseData = send(request);
			NetWorkWrapper pduStatusWrapper = mapper.readValue(responseData.substring(request.length() + 2, responseData.lastIndexOf("\r\n")), NetWorkWrapper.class);
			for (NetworkInfoEnum networkInfoEnum : NetworkInfoEnum.values()) {
				String value = convertValueByIndexOfSpace(pduStatusWrapper.getResponse().getValueByMetric(networkInfoEnum));
				stats.put(networkInfoEnum.getName(), value);
			}
		} catch (Exception e) {
			failedMonitor.put("DeviceInfo", e.getMessage());
		}
	}

	/**
	 * Create Event for the outlet scheduler
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void createEvent(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		Objects.requireNonNull(stats);
		Objects.requireNonNull(advancedControllableProperty);
		//TODO
	}

	/**
	 * Convert value by the index of space. If it does not contain space, return the value instead.
	 *
	 * @param value the value is string value
	 * @return value extracted
	 */
	private String convertValueByIndexOfSpace(String value) {
		try {
			//Example format of value 10 ms => we only take 10
			return value.substring(0, value.indexOf(" "));
		} catch (Exception e) {
			//if exception occur (no space in value) we return the initial value
			return value;
		}
	}

	/**
	 * Format time data such as x day(s) x hour(s) x minute(s) x minute(s)
	 *
	 * @param time the time is String
	 * @return String
	 */
	private String formatTimeData(String time) {
		return time.replace("d", uuidDay).replace("s", PDUConstant.SECOND).replace(uuidDay, PDUConstant.DAYS)
				.replace("h", PDUConstant.HOUR).replace("m", PDUConstant.MINUTE);
	}
}