/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.power.pakedge.pdu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.AlertGlobalEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.AlertMailEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.AlertOutletEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.CreateEventEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.DeviceInfoEmum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.MonitoringMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.NetworkInfoEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletAutoPingEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletConfigEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletScheduleEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.OutletStatusEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUDisplayEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PDUStatusEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.comon.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.AlertTypeEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.DaysEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.EnumTypeHandler;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OledContrastEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OledEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OutletAction;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.AlertEMailWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.AlertGlobalWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.AlertsOutletWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.DateAndTimeWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.DeviceInfoWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.NetWorkWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.OutletAutoPingWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.OutletConfigWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.OutletScheduleWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.OutletStatusWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.PDUDisplayWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.PDUStatusWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.TimeZoneWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.UptimeWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertGlobal;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertMail;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertOutlet;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.deviceinfo.DeviceInfoResponse;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.events.EventDetails;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletAutoPing;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletConfig;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.outlets.OutletStatus;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu.PDUDisplay;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.pdu.PDUStatus;
import com.avispl.symphony.dal.communicator.TelnetCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

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
	private final List<PDUDisplay> pduDisplayList = new ArrayList<>();
	private final List<PDUStatus> pduStatusList = new ArrayList<>();
	private final List<OutletStatus> outletStatusList = new ArrayList<>();
	private final List<OutletAutoPing> outletAutoPingList = new ArrayList<>();
	private final Map<Integer, List<EventDetails>> outletSchedulerEventList = new HashMap<>();
	private final List<OutletConfig> outletSConfigList = new ArrayList<>();
	private final List<AlertOutlet> alertOutletList = new ArrayList<>();
	ObjectMapper mapper = new ObjectMapper();
	private boolean isEmergencyDelivery;
	/**
	 * Store previous/current ExtendedStatistics
	 */
	private ExtendedStatistics localExtendedStatistics;
	private AlertGlobal alertGlobal = new AlertGlobal();
	private AlertMail alertEmail = new AlertMail();

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

	@Override
	protected void internalInit() throws Exception {
		super.internalInit();
		this.createChannel();
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
		switch (metric) {
			case DEVICE_INFO:
			case NETWORK_INFORMATION:
			case TIMEZONE:
			case UPTIME:
			case TIME:
				break;
			case ALTER_EMAIL:
				populateControlAlertMail(stats, advancedControllableProperties);
				break;
			case ALTER_GLOBAL:
				populateControlAlertGlobal(stats, advancedControllableProperties);
				break;
			case PDU_STATUS:
				populateControlPDUStatus(stats, advancedControllableProperties);
				break;
			case PDU_DISPLAY:
				populateControlPDUDisplay(stats, advancedControllableProperties);
				break;
			case OUTLET_CONF:
				populateControlOutletConfig(stats, advancedControllableProperties);
				break;
			case ALTER_OUTLET:
				populateControlOutletAlerts(stats, advancedControllableProperties);
				break;
			case OUTLET_STATUS:
				populateControlOutletStatus(stats, advancedControllableProperties);
				break;
			case OUTLET_AUTO_PING:
				populateControlOutletAutoPing(stats, advancedControllableProperties);
				break;
			case OUTLET_SCHEDULER_EVENT:
				retrieveOutletScheduler(stats, advancedControllableProperties);
				break;
			default:
				throw new IllegalArgumentException("Do not support PDU monitoring metric is: " + metric.name());
		}
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
				for (int outletID = 1; outletID <= PDUConstant.OUTLET_ID_MAX; outletID++) {
					populateRetrieveDataByMetric(metric, outletID);
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
			String result = responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA));
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
		try {
			switch (metric) {
				case NETWORK_INFORMATION:
				case DEVICE_INFO:
				case UPTIME:
				case TIME:
				case TIMEZONE:
				case ALTER_EMAIL:
					AlertEMailWrapper alertEMailWrapper = mapper.readValue(responseData, AlertEMailWrapper.class);
					if (alertEMailWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(alertEMailWrapper.getStatus())) {
						alertEmail = alertEMailWrapper.getResponse();
					}
					break;
				case ALTER_GLOBAL:
					AlertGlobalWrapper alertGlobalWrapper = mapper.readValue(responseData, AlertGlobalWrapper.class);
					if (alertGlobalWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(alertGlobalWrapper.getStatus())) {
						alertGlobal = alertGlobalWrapper.getResponse();
					}
					break;
				case ALTER_OUTLET:
					AlertsOutletWrapper alertsOutletWrapper = mapper.readValue(responseData, AlertsOutletWrapper.class);
					if (alertsOutletWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(alertsOutletWrapper.getStatus())) {
						alertOutletList.add(alertsOutletWrapper.getResponse());
					}
					break;
				case PDU_STATUS:
					PDUStatusWrapper pduStatusWrapper = mapper.readValue(responseData, PDUStatusWrapper.class);
					if (pduStatusWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(pduStatusWrapper.getStatus())) {
						pduStatusList.add(pduStatusWrapper.getResponse());
					}
					break;
				case PDU_DISPLAY:
					PDUDisplayWrapper pduDisplayWrapper = mapper.readValue(responseData, PDUDisplayWrapper.class);
					if (pduDisplayWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(pduDisplayWrapper.getStatus())) {
						pduDisplayList.add(pduDisplayWrapper.getResponse());
					}
					break;
				case OUTLET_SCHEDULER_EVENT:
					OutletScheduleWrapper outletScheduleWrapper = mapper.readValue(responseData, OutletScheduleWrapper.class);
					if (outletScheduleWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(outletScheduleWrapper.getStatus())) {
						int index = outletSchedulerEventList.size() + 1;
						List<EventDetails> eventDetailsList = outletScheduleWrapper.getResponse().getEvents();
						outletSchedulerEventList.put(index, eventDetailsList);
					}
					break;
				case OUTLET_AUTO_PING:
					OutletAutoPingWrapper outletAutoPingWrapper = mapper.readValue(responseData, OutletAutoPingWrapper.class);
					if (outletAutoPingWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(outletAutoPingWrapper.getStatus())) {
						outletAutoPingList.add(outletAutoPingWrapper.getResponse());
					}
					break;
				case OUTLET_STATUS:
					OutletStatusWrapper outletStatusWrapper = mapper.readValue(responseData, OutletStatusWrapper.class);
					if (outletStatusWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(outletStatusWrapper.getStatus())) {
						outletStatusList.add(outletStatusWrapper.getResponse());
					}
					break;
				case OUTLET_CONF:
					OutletConfigWrapper outletConfigWrapper = mapper.readValue(responseData, OutletConfigWrapper.class);
					if (outletConfigWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(outletConfigWrapper.getStatus())) {
						outletSConfigList.add(outletConfigWrapper.getResponse());
					}
					break;
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
			DeviceInfoWrapper deviceInfoWrapper = mapper.readValue(responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA)),
					DeviceInfoWrapper.class);

			if (deviceInfoWrapper == null || deviceInfoWrapper.getStatus().equalsIgnoreCase(PDUConstant.ERROR)) {
				contributeNoneValueForDeviceInfo(stats);
			} else {
				DeviceInfoResponse deviceInfoWrapperResponse = deviceInfoWrapper.getResponse();
				for (DeviceInfoEmum metricDeviceInfo : DeviceInfoEmum.values()) {
					String value = getDefaultValueForNoneData(deviceInfoWrapperResponse.getValueByMetric(metricDeviceInfo));
					stats.put(metricDeviceInfo.getName(), value);
				}
			}
		} catch (Exception e) {
			//handle None value if retrieve data failed
			contributeNoneValueForDeviceInfo(stats);
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Value of list statistics property of device info is none
	 *
	 * @param stats list statistics
	 */
	private void contributeNoneValueForDeviceInfo(Map<String, String> stats) {
		for (DeviceInfoEmum metricDeviceInfo : DeviceInfoEmum.values()) {
			stats.put(metricDeviceInfo.getName(), PDUConstant.NONE);
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
			TimeZoneWrapper timeZoneWrapper = mapper.readValue(responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA)), TimeZoneWrapper.class);
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
			DateAndTimeWrapper dateAndTimeWrapper = mapper.readValue(responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA)),
					DateAndTimeWrapper.class);
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
			UptimeWrapper uptimeWrapper = mapper.readValue(responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA)), UptimeWrapper.class);
			stats.put(MonitoringMetric.UPTIME.getName(), formatTimeData(uptimeWrapper.getResponse().getUptime()));
		} catch (Exception e) {
			stats.put(MonitoringMetric.UPTIME.getName(), PDUConstant.NONE);
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Control DPU status for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlPDUStatus(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		for (PDUStatus pduStatus : pduStatusList) {
			for (PDUStatusEnum metric : PDUStatusEnum.values()) {
				String key = PDUConstant.PDU + PDUConstant.HASH + metric.getName();
				if (PDUStatusEnum.TEMPERATURE_UNIT.getName().equals(metric.getName())) {
					String temp = String.valueOf(PDUConstant.ZERO);
					if (PDUConstant.TEMPERATURE_F.equals(pduStatus.getPduTempUnit())) {
						temp = String.valueOf(PDUConstant.NUMBER_ONE);
					}
					AdvancedControllableProperty tempControl = controlSwitch(stats, key, temp, PDUConstant.TEMPERATURE_C, PDUConstant.TEMPERATURE_F);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, tempControl);
					continue;
				}
				stats.put(key, convertValueByIndexOfSpace(pduStatus.getValueByMetric(metric)));
			}
		}
	}

	/**
	 * Control Outlet Status for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletStatus(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {

		int i = 1;
		for (OutletStatus outletStatus : outletStatusList) {
			for (OutletStatusEnum metric : OutletStatusEnum.values()) {
				String key = MonitoringMetric.OUTLET_CONF.getName() + i + PDUConstant.HASH + metric.getName();
				if (OutletStatusEnum.STATUS.getName().equals(metric.getName())) {
					String value = outletStatus.getStatus();
					if (PDUConstant.OFF.equals(value)) {
						value = String.valueOf(PDUConstant.ZERO);
					} else {
						value = String.valueOf(PDUConstant.NUMBER_ONE);
					}
					AdvancedControllableProperty notificationControl = controlSwitch(stats, key, value, PDUConstant.OFF, PDUConstant.ON);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, notificationControl);
					continue;
				}
				if (OutletStatusEnum.RESET_PEAK.getName().equals(metric.getName())) {
					stats.put(key, PDUConstant.EMPTY_STRING);
					advancedControllableProperty.add(createButton(key, PDUConstant.RESET, PDUConstant.RESETTING, 0));
				} else {
					stats.put(key, convertValueByIndexOfSpace(outletStatus.getValueByMetric(metric)));
				}
			}
			i++;
		}
	}

	/**
	 * Control Outlet Alerts for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletAlerts(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {

		int outletID = 1;
		for (AlertOutlet alertsOutlet : alertOutletList) {
			for (AlertOutletEnum metric : AlertOutletEnum.values()) {
				String key = MonitoringMetric.ALTER_OUTLET.getName() + outletID + PDUConstant.HASH + metric.getName();
				String value = alertsOutlet.getValueByMetric(metric);
				switch (metric) {
					case POWER_MAX:
					case POWER_MIN:
					case CURRENT_MAX:
					case CURRENT_MIN:
						AdvancedControllableProperty controllableProperty = controlTextOrNumeric(stats, key, value, true);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllableProperty);
						break;
					case POWER_ALERT:
					case CURRENT_ALERT:
						String[] typeDropdown = EnumTypeHandler.getEnumNames(AlertTypeEnum.class);
						AdvancedControllableProperty controllablePropertyControl = controlDropdown(stats, typeDropdown, key, value);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllablePropertyControl);
						break;
					default:
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Controlling AlertOutlet group config %s is not supported.", metric.getName()));
						}
						break;
				}
				stats.put(MonitoringMetric.ALTER_OUTLET.getName() + outletID + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
			}
			outletID++;
		}
	}

	/**
	 * Control Alert Email for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlAlertMail(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		if (alertEmail != null) {
			for (AlertMailEnum metric : AlertMailEnum.values()) {
				String key = MonitoringMetric.ALTER_EMAIL.getName() + PDUConstant.HASH + metric.getName();
				if (AlertMailEnum.RECIPIENT.getName().equals(metric.getName())) {
					AdvancedControllableProperty recipientProperty = controlTextOrNumeric(stats, key, alertEmail.getRecipient(), false);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, recipientProperty);
					continue;
				}
				if (AlertMailEnum.SUBJECT.getName().equals(metric.getName())) {
					AdvancedControllableProperty subjectProperty = controlTextOrNumeric(stats, key, alertEmail.getSubject(), false);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, subjectProperty);
				}
			}
			stats.put(MonitoringMetric.ALTER_EMAIL.getName() + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
		}
	}

	/**
	 * Control Alert Global for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlAlertGlobal(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		String[] typeDropdown = EnumTypeHandler.getEnumNames(AlertTypeEnum.class);
		for (AlertGlobalEnum metric : AlertGlobalEnum.values()) {
			String key = MonitoringMetric.ALTER_GLOBAL.getName() + PDUConstant.HASH + metric.getName();
			String value = alertGlobal.getValueByMetric(metric);
			switch (metric) {
				case TEMP_UNIT:
				case TEMP_SENSOR:
					stats.put(key, value);
					break;
				case CURRENT_MIN:
				case CURRENT_MAX:
				case POWER_MAX:
				case POWER_MIN:
				case TEMP_MAX:
				case TEMP_MIN:
				case VOLTAGE_MAX:
				case VOLTAGE_MIN:
					AdvancedControllableProperty controllableProperty = controlTextOrNumeric(stats, key, value, true);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllableProperty);
					break;
				case POWER_ALERT:
				case TEMP_ALERT:
				case VOLTAGE_ALERT:
				case CURRENT_ALERT:
					AdvancedControllableProperty controllablePropertyAlert = controlDropdown(stats, typeDropdown, key, value);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllablePropertyAlert);
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling alert global group config %s is not supported.", metric.getName()));
					}
					break;
			}
			stats.put(MonitoringMetric.ALTER_GLOBAL.getName() + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
		}
	}

	/**
	 * Control Outlet Auto Ping for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletAutoPing(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		int outletID = 1;
		for (OutletAutoPing outletAutoPing : outletAutoPingList) {
			for (OutletAutoPingEnum metric : OutletAutoPingEnum.values()) {
				String key = MonitoringMetric.OUTLET_AUTO_PING.getName() + outletID + PDUConstant.HASH + metric.getName();
				String value = outletAutoPing.getValueByMetric(metric);
				switch (metric) {
					case LIMIT:
					case ATTEMPTS:
					case TIMEOUT:
					case PERIOD:
					case INTERVAL:
						AdvancedControllableProperty controllableProperty = controlTextOrNumeric(stats, key, value, true);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllableProperty);
						break;
					case ENABLE:
					case NOTIFICATION:
						AdvancedControllableProperty enableControl = controlSwitch(stats, key, value, PDUConstant.DISABLE, PDUConstant.ENABLE);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, enableControl);
						break;
					case DESTINATION:
						if (StringUtils.isNullOrEmpty(value) || PDUConstant.SPACE.equals(value)) {
							value = PDUConstant.EMPTY_STRING;
						}
						AdvancedControllableProperty destinationProperty = controlTextOrNumeric(stats, key, value, false);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, destinationProperty);
						break;
					default:
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Controlling Outlet Auto Ping: %s is not supported.", metric.getName()));
						}
						break;
				}
				stats.put(MonitoringMetric.OUTLET_AUTO_PING.getName() + outletID + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
			}
			outletID++;
		}

	}

	/**
	 * Control Outlet Config for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletConfig(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		//outletID with index 1-9
		int outletID = 1;
		for (OutletConfig outletConfig : outletSConfigList) {
			for (OutletConfigEnum metric : OutletConfigEnum.values()) {
				String key = MonitoringMetric.OUTLET_CONF.getName() + outletID + PDUConstant.HASH + metric.getName();
				String value = outletConfig.getValueByMetric(metric);
				switch (metric) {
					case LOCAL_REBOOT:
						AdvancedControllableProperty localControlProperties = controlSwitch(stats, key, value, PDUConstant.DISABLE, PDUConstant.ENABLE);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, localControlProperties);
						break;
					case POWER_ON_DELAY:
					case NAME:
					case POWER_OFF_DELAY:
						AdvancedControllableProperty controllableProperty = controlTextOrNumeric(stats, key, value, false);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, controllableProperty);
						break;
					default:
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Controlling outlet group config %s is not supported.", metric.getName()));
						}
						break;
				}
				stats.put(MonitoringMetric.OUTLET_CONF.getName() + outletID + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
			}
			outletID++;
		}
	}

	/**
	 * Control Outlet Auto Ping for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void retrieveOutletScheduler(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		for (Entry<Integer, List<EventDetails>> outletSchedule : outletSchedulerEventList.entrySet()) {
			int outletIndex = outletSchedule.getKey();
			List<EventDetails> eventDetailsList = outletSchedule.getValue();
			EventDetails eventDetails = eventDetailsList.get(0);
			List<String> idEvent = new ArrayList<>();
			for (EventDetails eventItem : eventDetailsList) {
				idEvent.add(eventItem.getId());
			}
			for (OutletScheduleEnum metric : OutletScheduleEnum.values()) {
				String value = eventDetails.getValueByMetric(metric);
				String key = MonitoringMetric.OUTLET_SCHEDULER_EVENT.getName() + outletIndex + PDUConstant.EVENTS + PDUConstant.HASH + metric.getName();
				switch (metric) {
					case ACTION:
						String[] actionDropdown = EnumTypeHandler.getEnumNames(OutletAction.class);
						AdvancedControllableProperty actionControl = controlDropdown(stats, actionDropdown, key, value);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, actionControl);
						break;
					case ID:
						String[] idEventDropdown = idEvent.toArray(new String[0]);
						Arrays.sort(idEventDropdown);
						AdvancedControllableProperty idEventControl = controlDropdown(stats, idEventDropdown, key, value);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, idEventControl);
						break;
					case ADD_DAY:
						stats.put(key, PDUConstant.EMPTY_STRING);
						advancedControllableProperty.add(createButton(key, PDUConstant.ADD, PDUConstant.ADDING, 0));
						break;
					case TIME:
						String[] times = { "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12", "13:00", "14:00", "15:00", "16:00",
								"17:00",
								"18:00", "19:00", "20:00", "21:00", "22:00", "23:00" };
						AdvancedControllableProperty timesControl = controlDropdown(stats, times, key, value);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, timesControl);
						break;
					default:
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("Controlling event group config %s is not supported.", metric.getName()));
						}
						break;
				}
				stats.put(MonitoringMetric.OUTLET_SCHEDULER_EVENT.getName() + outletIndex + PDUConstant.EVENTS + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
			}
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
			NetWorkWrapper pduStatusWrapper = mapper.readValue(responseData.substring(request.length() + PDUConstant.NUMBER_TWO, responseData.lastIndexOf(PDUConstant.REGEX_DATA)), NetWorkWrapper.class);
			for (NetworkInfoEnum networkInfoEnum : NetworkInfoEnum.values()) {
				String value = convertValueByIndexOfSpace(pduStatusWrapper.getResponse().getValueByMetric(networkInfoEnum));
				stats.put(networkInfoEnum.getName(), value);
			}
		} catch (Exception e) {
			failedMonitor.put(metric.getName(), e.getMessage());
		}
	}

	/**
	 * Control PDU Display for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlPDUDisplay(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		for (PDUDisplay pduDisplay : pduDisplayList) {
			for (PDUDisplayEnum metric : PDUDisplayEnum.values()) {
				String value = convertValueByIndexOfSpace(pduDisplay.getValueByMetric(metric));
				String key = PDUConstant.PDU + PDUConstant.HASH + metric.getName();
				if (PDUDisplayEnum.OLED_CONTRAST.getName().equals(metric.getName())) {
					String[] oledContrast = EnumTypeHandler.getEnumNames(OledContrastEnum.class);
					AdvancedControllableProperty oledContrastControl = controlDropdown(stats, oledContrast, key, value);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, oledContrastControl);
				}
				if (PDUDisplayEnum.LED.getName().equals(metric.getName())) {
					String[] led = EnumTypeHandler.getEnumNames(OledEnum.class);
					if (String.valueOf(PDUConstant.NUMBER_ONE).equals(value)) {
						value = OledEnum.ENABLE.getName();
					} else if (String.valueOf(PDUConstant.ZERO).equals(value)) {
						value = OledEnum.DISABLE.getName();
					} else {
						value = OledEnum.DIM.getName();
					}
					AdvancedControllableProperty oledContrastControl = controlDropdown(stats, led, key, value);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, oledContrastControl);
				}
				if (PDUDisplayEnum.OLED_ENABLE.getName().equals(metric.getName())) {
					AdvancedControllableProperty ledControl = controlSwitch(stats, key, value, PDUConstant.DISABLE, PDUConstant.ENABLE);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, ledControl);
				}
			}
			stats.put(PDUConstant.PDU + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.FALSE);
		}
	}

	/**
	 * Create Event for the outlet scheduler
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void createEvent(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		String[] outletIdList = PDUConstant.OUTLET_ID_LIST;
		String keyGroup = PDUConstant.CREATE_EVENT + PDUConstant.HASH;

		for (CreateEventEnum event : CreateEventEnum.values()) {
			String key = keyGroup + event.getName();
			switch (event) {
				case ACTION:
					String[] actionDropdown = EnumTypeHandler.getEnumNames(OutletAction.class);
					AdvancedControllableProperty actionControl = controlDropdown(stats, actionDropdown, key, OutletAction.TURN_OFF.getName());
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, actionControl);
					break;
				case ADD_DAY:
					stats.put(key, PDUConstant.EMPTY_STRING);
					advancedControllableProperty.add(createButton(key, PDUConstant.ADD, PDUConstant.ADDING, 0));
					break;
				case DAY_DEFAULT:
					String[] days = EnumTypeHandler.getEnumNames(DaysEnum.class);
					AdvancedControllableProperty daysControl = controlDropdown(stats, days, key, DaysEnum.MON.getName());
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, daysControl);
					break;
				case OUTLET_ID:
					stats.put(key, PDUConstant.EMPTY_STRING);
					advancedControllableProperty.add(controlDropdown(stats, outletIdList, key, outletIdList[0]));
					break;
				case START_TIME:
					String[] startTimes = { "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12", "13:00", "14:00", "15:00", "16:00",
							"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00" };
					AdvancedControllableProperty timesControl = controlDropdown(stats, startTimes, key, "00:00");
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, timesControl);
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling event group config %s is not supported.", event.getName()));
					}
					break;
			}
		}
		stats.put(keyGroup + PDUConstant.CREATE_EVENT, PDUConstant.EMPTY_STRING);
		advancedControllableProperty.add(createButton(keyGroup + PDUConstant.CREATE_EVENT, PDUConstant.CREATE, PDUConstant.CREATING, 0));
		stats.put(keyGroup + PDUConstant.EDITED, PDUConstant.FALSE);
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
	 * Format time data such as x day(s) x hour(s) x minute(s) x second(s)
	 *
	 * @param time the time is String
	 * @return String
	 */
	private String formatTimeData(String time) {
		return time.replace("d", uuidDay).replace("s", PDUConstant.SECOND).replace(uuidDay, PDUConstant.DAYS)
				.replace("h", PDUConstant.HOUR).replace("m", PDUConstant.MINUTE);
	}

	/**
	 * Add text or numeric is control property for metric
	 *
	 * @param stats list statistic
	 * @param name String name of metric
	 * @return AdvancedControllableProperty text and numeric instance
	 */
	private AdvancedControllableProperty controlTextOrNumeric(Map<String, String> stats, String name, String value, boolean isNumeric) {
		stats.put(name, value);

		return isNumeric ? createNumeric(name, value) : createText(name, value);
	}

	/**
	 * Create Numeric is control property for metric
	 *
	 * @param name the name of the property
	 * @param initialValue the initialValue is number
	 * @return AdvancedControllableProperty Numeric instance
	 */
	private AdvancedControllableProperty createNumeric(String name, String initialValue) {
		AdvancedControllableProperty.Numeric numeric = new AdvancedControllableProperty.Numeric();

		return new AdvancedControllableProperty(name, new Date(), numeric, initialValue);
	}

	/**
	 * Create text is control property for metric
	 *
	 * @param name the name of the property
	 * @param stringValue character string
	 * @return AdvancedControllableProperty Text instance
	 */
	private AdvancedControllableProperty createText(String name, String stringValue) {
		AdvancedControllableProperty.Text text = new AdvancedControllableProperty.Text();

		return new AdvancedControllableProperty(name, new Date(), text, stringValue);
	}

	/**
	 * Add/Update advancedControllableProperties if  advancedControllableProperties different empty
	 *
	 * @param advancedControllableProperties advancedControllableProperties is the list that store all controllable properties
	 * @param property the property is item advancedControllableProperties
	 */
	private void addOrUpdateAdvanceControlProperties(List<AdvancedControllableProperty> advancedControllableProperties, AdvancedControllableProperty property) {
		if (property != null) {
			advancedControllableProperties.removeIf(item -> item.getName().equals(property.getName()));
			advancedControllableProperties.add(property);
		}
	}

	/***
	 * Create dropdown advanced controllable property
	 *
	 * @param name the name of the control
	 * @param initialValue initial value of the control
	 * @return AdvancedControllableProperty dropdown instance
	 */
	private AdvancedControllableProperty createDropdown(String name, String[] values, String initialValue) {
		AdvancedControllableProperty.DropDown dropDown = new AdvancedControllableProperty.DropDown();
		dropDown.setOptions(values);
		dropDown.setLabels(values);

		return new AdvancedControllableProperty(name, new Date(), dropDown, initialValue);
	}

	/**
	 * Create a button.
	 *
	 * @param name name of the button
	 * @param label label of the button
	 * @param labelPressed label of the button after pressing it
	 * @param gracePeriod grace period of button
	 * @return This returns the instance of {@link AdvancedControllableProperty} type Button.
	 */
	private AdvancedControllableProperty createButton(String name, String label, String labelPressed, long gracePeriod) {
		AdvancedControllableProperty.Button button = new AdvancedControllableProperty.Button();
		button.setLabel(label);
		button.setLabelPressed(labelPressed);
		button.setGracePeriod(gracePeriod);
		return new AdvancedControllableProperty(name, new Date(), button, PDUConstant.EMPTY_STRING);
	}

	/**
	 * Add dropdown is control property for metric
	 *
	 * @param stats list statistic
	 * @param options list select
	 * @param name String name of metric
	 * @return AdvancedControllableProperty dropdown instance if add dropdown success else will is null
	 */
	private AdvancedControllableProperty controlDropdown(Map<String, String> stats, String[] options, String name, String value) {
		stats.put(name, value);
		return createDropdown(name, options, value);
	}

	/**
	 * Add switch is control property for metric
	 *
	 * @param stats list statistic
	 * @param name String name of metric
	 * @return AdvancedControllableProperty switch instance
	 */
	private AdvancedControllableProperty controlSwitch(Map<String, String> stats, String name, String value, String labelOff, String labelOn) {
		stats.put(name, value);
		if (!PDUConstant.NONE.equals(value)) {
			return createSwitch(name, Integer.parseInt(value), labelOff, labelOn);
		}
		return null;
	}

	/**
	 * Create switch is control property for metric
	 *
	 * @param name the name of property
	 * @param status initial status (0|1)
	 * @return AdvancedControllableProperty switch instance
	 */
	private AdvancedControllableProperty createSwitch(String name, int status, String labelOff, String labelOn) {
		AdvancedControllableProperty.Switch toggle = new AdvancedControllableProperty.Switch();
		toggle.setLabelOff(labelOff);
		toggle.setLabelOn(labelOn);

		AdvancedControllableProperty advancedControllableProperty = new AdvancedControllableProperty();
		advancedControllableProperty.setName(name);
		advancedControllableProperty.setValue(status);
		advancedControllableProperty.setType(toggle);
		advancedControllableProperty.setTimestamp(new Date());

		return advancedControllableProperty;
	}

	/**
	 * Get default value by Null value. if value different Null return the value instead.
	 *
	 * @param value the value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNoneData(String value) {
		if (StringUtils.isNullOrEmpty(value)) {
			value = PDUConstant.NONE;
		}
		return value;
	}
}