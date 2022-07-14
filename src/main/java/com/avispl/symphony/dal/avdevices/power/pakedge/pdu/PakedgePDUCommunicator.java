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
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.command.CommandControl;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.AlertEmailEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.AlertGlobalEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.AlertOutletEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.ControllingMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.CreateEventEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.DeviceInfoEmum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.MonitoringMetric;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.NetworkInfoEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletAutoPingEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletConfigEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletScheduleEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.OutletStatusEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUConstant;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUDisplayEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PDUStatusEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common.PakedgePDUUtil;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.AlertTypeEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.DaysEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.EnumTypeHandler;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OledContrastEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OledEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.OutletAction;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dropdownlist.TimeEnum;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.AlertEmailWrapper;
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
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.ResponseControlDataWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.TimeZoneWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.UptimeWrapper;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertEMail;
import com.avispl.symphony.dal.avdevices.power.pakedge.pdu.dto.alerts.AlertGlobal;
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

	private boolean isEmergencyDelivery;
	private boolean isCreateSchedulerEvent;
	private final ObjectMapper mapper = new ObjectMapper();
	private final String uuidDay = UUID.randomUUID().toString().replace(PDUConstant.DASH, PDUConstant.EMPTY_STRING);
	private final Map<String, String> failedMonitor = new HashMap<>();
	private Integer noOfMonitoringMetric = 0;
	//DTO data response
	private AlertEMail alertEmail = new AlertEMail();
	private PDUDisplay pduDisplay = new PDUDisplay();
	private PDUStatus pduStatus = new PDUStatus();
	private AlertGlobal alertGlobal = new AlertGlobal();

	//The properties adapter
	private String outletIDFilter;

	/**
	 * List of outletID
	 */
	private List<String> outletIdExtractedList = new ArrayList<>();

	/**
	 * List of Outlet Status
	 */
	private final List<OutletStatus> outletStatusList = new ArrayList<>();

	/**
	 * List of Outlet Auto Ping
	 */
	private final List<OutletAutoPing> outletAutoPingList = new ArrayList<>();

	/**
	 * List of Outlet Config
	 */
	private final List<OutletConfig> outletConfigList = new ArrayList<>();

	/**
	 * List of Alert Outlet
	 */
	private final List<AlertOutlet> alertOutletList = new ArrayList<>();

	/**
	 * Map of eventId and Day with key is eventId, value is day of DaysEnum
	 */
	private final Map<String, String> eventIdAndDayMap = new HashMap<>();

	/**
	 * Map of outletId and List EventDetails with key is outletId , value is list EventDetails
	 */
	private final Map<Integer, List<EventDetails>> outletIdAndEventDetailsMap = new HashMap<>();

	/**
	 * Map of outletId and Map of EventId and Day with key is the outletId, value is Map of EventId and Day with key is EventID, value is Day of DaysEnum
	 */
	private final Map<String, Map<String, String>> outletIdEventIdAndDayMap = new HashMap<>();

	private final Map<Integer, Integer> outletIdAndEventId = new HashMap<>();

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
	 * ReentrantLock to prevent telnet session is closed when adapter is retrieving statistics from the device.
	 */
	private final ReentrantLock reentrantLock = new ReentrantLock();

	/**
	 * Store previous/current ExtendedStatistics
	 */
	private ExtendedStatistics localExtendedStatistics;

	/**
	 * Store previous/current create event ExtendedStatistics
	 */
	private ExtendedStatistics localCreateEvent;

	/**
	 * Retrieves {@code {@link #outletIDFilter}}
	 *
	 * @return value of {@link #outletIDFilter}
	 */
	public String getOutletIDFilter() {
		return outletIDFilter;
	}

	/**
	 * Sets {@code outletIDFilter}
	 *
	 * @param outletIDFilter the {@code java.lang.String} field
	 */
	public void setOutletIDFilter(String outletIDFilter) {
		this.outletIDFilter = outletIDFilter;
	}

	/**
	 * PakedgePDUCommunicator constructor
	 */
	public PakedgePDUCommunicator() {
		//Because we implement with simulator, the characters below may change when a real device is available
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
	public List<Statistics> getMultipleStatistics() {
		if (logger.isDebugEnabled()) {
			logger.debug("PakedgePDUCommunicator: Perform getMultipleStatistics()");
		}
		ExtendedStatistics extendedStatistics = new ExtendedStatistics();
		List<AdvancedControllableProperty> advancedControllableProperty = new ArrayList<>();
		Map<String, String> stats = new HashMap<>();
		reentrantLock.lock();
		try {
			if (!isEmergencyDelivery) {
				clearBeforeFetchingData();
				extractListOutletIDFilter(outletIDFilter);
				populateInformationFromDevice(stats, advancedControllableProperty);
				if (!isCreateSchedulerEvent) {
					localCreateEvent = new ExtendedStatistics();
					List<AdvancedControllableProperty> advancedControlOfCreateEvent = new ArrayList<>();
					Map<String, String> createEventS = new HashMap<>();
					createEvent(createEventS, advancedControlOfCreateEvent);
					localCreateEvent.setStatistics(createEventS);
					localCreateEvent.setControllableProperties(advancedControlOfCreateEvent);
					isCreateSchedulerEvent = true;
				}
				extendedStatistics.setStatistics(stats);
				extendedStatistics.setControllableProperties(advancedControllableProperty);
				localExtendedStatistics = extendedStatistics;
			}
			// add all stats of create event into local stats
			Map<String, String> localStats = localExtendedStatistics.getStatistics();
			Map<String, String> localStreamStats = localCreateEvent.getStatistics();
			localStats.putAll(localStreamStats);

			//remove and update control property for create event into localExtendedStatistics
			List<AdvancedControllableProperty> localAdvancedControl = localExtendedStatistics.getControllableProperties();
			List<AdvancedControllableProperty> localStreamAdvancedControl = localCreateEvent.getControllableProperties();
			List<String> nameList = localStreamAdvancedControl.stream().map(AdvancedControllableProperty::getName).collect(Collectors.toList());
			localAdvancedControl.removeIf(item -> nameList.contains(item.getName()));
			localAdvancedControl.addAll(localStreamAdvancedControl);
		} finally {
			reentrantLock.unlock();
		}
		return Collections.singletonList(localExtendedStatistics);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalInit() throws Exception {
		super.internalInit();
		this.createChannel();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalDestroy() {
		isEmergencyDelivery = false;
		isCreateSchedulerEvent = false;
		if (localExtendedStatistics != null && localExtendedStatistics.getStatistics() != null && localExtendedStatistics.getControllableProperties() != null) {
			localExtendedStatistics.getStatistics().clear();
			localExtendedStatistics.getControllableProperties().clear();
		}
		outletIdExtractedList.clear();
		super.internalDestroy();
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
			Map<String, String> extendedStatistics = localExtendedStatistics.getStatistics();
			List<AdvancedControllableProperty> advancedControllableProperties = localExtendedStatistics.getControllableProperties();

			String[] propertySplit = property.split(PDUConstant.HASH);
			String propertyName = propertySplit[0];
			ControllingMetric metric = ControllingMetric.getMetricByValue(propertyName);
			if (ControllingMetric.CREATE_EVENT.getName().equals(metric.getName())) {
				createNewSchedulerEvent(property, value);
			} else {
				isEmergencyDelivery = true;
				switch (metric) {
					case PDU:
						controlPDUDisplay(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case ALTER_EMAIL:
						controlAlertEmail(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case ALTER_GLOBAL:
						controlAlertGlobal(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case ALTER_OUTLET:
						controlAlertOutlet(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case OUTLET_CONF:
						controlOutletConfig(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case OUTLET_AUTO_PING:
						controlOutletAutoPing(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case OUTLET_SCHEDULER_EVENT:
						controlOutletSchedulerEvent(property, value, extendedStatistics, advancedControllableProperties);
						break;
					case CREATE_EVENT:
						break;
					default:
						if (logger.isDebugEnabled()) {
							logger.debug(String.format("The metric group %s is not supported.", metric.getName()));
						}
						break;
				}
				if (isEmergencyDelivery) {
					extendedStatistics.put(propertyName + PDUConstant.HASH + PDUConstant.APPLY_CHANGE, PDUConstant.EMPTY_STRING);
					advancedControllableProperties.add(createButton(propertyName + PDUConstant.HASH + PDUConstant.APPLY_CHANGE, PDUConstant.APPLY, PDUConstant.APPLYING, 0));

					extendedStatistics.put(propertyName + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.TRUE);
					extendedStatistics.put(propertyName + PDUConstant.HASH + PDUConstant.CANCEL_CHANGE, PDUConstant.EMPTY_STRING);
					advancedControllableProperties.add(createButton(propertyName + PDUConstant.HASH + PDUConstant.CANCEL_CHANGE, PDUConstant.CANCEL, PDUConstant.CANCELING, 0));
				}
			}
		} finally {
			this.timeout = statisticsTelnetTimeout;
			reentrantLock.unlock();
		}
	}

	/**
	 * Create outlet scheduler event
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 */
	private void createNewSchedulerEvent(String property, String value) {
		isCreateSchedulerEvent = true;
		Map<String, String> stats = localCreateEvent.getStatistics();
		List<AdvancedControllableProperty> advancedControllableProperties = localCreateEvent.getControllableProperties();

		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		CreateEventEnum createEventEnum = EnumTypeHandler.getMetricOfEnumByName(CreateEventEnum.class, propertyKey);
		if (propertyKey.contains(PDUConstant.DAY) && !propertyKey.contains(CreateEventEnum.ADD_DAY.getName())) {
			updateDayForSchedulerEventDropdownList(property, value, stats, advancedControllableProperties, propertyKey, eventIdAndDayMap);
		} else {
			switch (createEventEnum) {
				case ACTION:
				case OUTLET_ID:
				case START_TIME:
					updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
					break;
				case ADD_DAY:
					addNewDayForSchedulerEvent(propertyName, stats, advancedControllableProperties);
					break;
				case CREATE_EVENT:
					EventDetails eventDetails = convertOutletScheduleByValues(propertyName, stats, true);
					sendCommandToControlMetric(eventDetails.getParamRequestOfEventDetails());
					isCreateSchedulerEvent = false;
					isEmergencyDelivery = false;
					break;
				case CANCEL:
					isCreateSchedulerEvent = false;
					isEmergencyDelivery = false;
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling create outlet scheduler event group config %s is not supported.", createEventEnum.getName()));
					}
			}
		}
		if (isCreateSchedulerEvent) {
			stats.put(propertyName + PDUConstant.HASH + PDUConstant.EDITED, PDUConstant.TRUE);
			stats.put(propertyName + PDUConstant.HASH + PDUConstant.CANCEL_CHANGE, PDUConstant.EMPTY_STRING);
			advancedControllableProperties.add(createButton(propertyName + PDUConstant.HASH + PDUConstant.CANCEL_CHANGE, PDUConstant.CANCEL, PDUConstant.CANCELING, 0));
		} else {
			stats.clear();
			advancedControllableProperties.clear();
		}
	}

	/**
	 * Control outlet scheduler event
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlOutletSchedulerEvent(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		if (propertyKey.contains(PDUConstant.DAY) && !propertyKey.contains(CreateEventEnum.ADD_DAY.getName())) {
			int len = propertyName.length() - PDUConstant.EVENTS.length();
			String outletID = propertyName.substring(len - 1, len);
			updateDayForSchedulerEventDropdownList(property, value, stats, advancedControllableProperties, propertyKey, outletIdEventIdAndDayMap.get(outletID));
		} else {
			OutletScheduleEnum outletScheduleEnum = EnumTypeHandler.getMetricOfEnumByName(OutletScheduleEnum.class, propertyKey);
			switch (outletScheduleEnum) {
				case EVENT_ID:
					updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
					int len = propertyName.length() - PDUConstant.EVENTS.length();
					String outletId = propertyName.substring(len - 1, len);
					updateSchedulerEventById(value, outletId, stats, advancedControllableProperties);
					outletIdAndEventId.put(Integer.parseInt(outletId), Integer.parseInt(value));
					isEmergencyDelivery = false;
					break;
				case ACTION:
				case TIME:
					updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
					break;
				case ADD_DAY:
					addNewDayForSchedulerEvent(propertyName, stats, advancedControllableProperties);
					break;
				case APPLY_CHANGE:
					EventDetails eventDetails = convertOutletScheduleByValues(propertyName, stats, false);
					sendCommandToActionSchedulerEvent(eventDetails);
					isEmergencyDelivery = false;
					break;
				case CANCEL:
					isEmergencyDelivery = false;
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling outlet scheduler event group config %s is not supported.", outletScheduleEnum.getName()));
					}
			}
		}
	}

	/**
	 * Update Event by Id of Scheduler Event
	 *
	 * @param eventId the eventId is id of Event
	 * @param outletId the outlet is id of Outlet
	 * @param stats the stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void updateSchedulerEventById(String eventId, String outletId, Map<String, String> stats,
			List<AdvancedControllableProperty> advancedControllableProperties) {
		EventDetails eventDetails = outletIdAndEventDetailsMap.get(Integer.parseInt(outletId)).get(Integer.parseInt(eventId));
		for (OutletScheduleEnum metric : OutletScheduleEnum.values()) {
			String value = eventDetails.getValueByMetric(metric);
			String key = MonitoringMetric.OUTLET_SCHEDULER_EVENT.getName() + outletId + PDUConstant.EVENTS + PDUConstant.HASH + metric.getName();
			switch (metric) {
				case ACTION:
					String[] actionDropdown = EnumTypeHandler.getEnumNames(OutletAction.class);
					AdvancedControllableProperty actionControl = controlDropdown(stats, actionDropdown, key, value);
					addOrUpdateAdvanceControlProperties(advancedControllableProperties, actionControl);
					break;
				case EVENT_ID:
					break;
				case ADD_DAY:
					stats.put(key, PDUConstant.EMPTY_STRING);
					advancedControllableProperties.add(createButton(key, PDUConstant.ADD, PDUConstant.ADDING, 0));
					break;
				case TIME:
					String[] times = EnumTypeHandler.getEnumNames(TimeEnum.class);
					AdvancedControllableProperty timesControl = controlDropdown(stats, times, key, value);
					addOrUpdateAdvanceControlProperties(advancedControllableProperties, timesControl);
					break;
				case DAYS:
					populateDaysForSchedulerEvent(MonitoringMetric.OUTLET_SCHEDULER_EVENT.getName() + outletId, eventId, stats, advancedControllableProperties);
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling event group config %s is not supported.", metric.getName()));
					}
					break;
			}
		}
	}

	/**
	 * Send Command to action Scheduler Event
	 *
	 * @param eventDetails the eventDetails is EventDetails instance
	 */
	private void sendCommandToActionSchedulerEvent(EventDetails eventDetails) {
		List<EventDetails> eventList = outletIdAndEventDetailsMap.get(Integer.valueOf(eventDetails.getOutletId()));
		Optional<EventDetails> details = eventList.stream().filter(event -> event.getId().equals(eventDetails.getId())).findFirst();
		if (details.isPresent() && !details.get().getAction().equals(eventDetails.getAction())) {
			String request = CommandControl.SET_DELETE_EVENT.getName() + PDUConstant.PARAM_DASH_O + eventDetails.getOutletId() + PDUConstant.PARAM_DASH_R + eventDetails.getAction();
			sendCommandToControlMetric(request);
		} else {
			sendCommandToControlMetric(eventDetails.getParamRequestOfEventDetails());
		}
	}

	/**
	 * Update list day for scheduler event
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param stats list of stats
	 * @param daysList Map<String,String> of list Day as Mon, Tue, Wed, Thu, Fri... etc
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void updateDayForSchedulerEventDropdownList(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties, String dayName,
			Map<String, String> daysList) {
		if (PDUConstant.NONE.equals(value) && !PDUConstant.DAY_0.equals(dayName)) {
			stats.remove(property);
			daysList.put(dayName, null);
		} else {
			String[] dayList = EnumTypeHandler.getEnumNames(DaysEnum.class);
			AdvancedControllableProperty addDayControlProperty = controlDropdown(stats, dayList, property, value);
			addOrUpdateAdvanceControlProperties(advancedControllableProperties, addDayControlProperty);
			daysList.put(dayName, value);
		}
	}

	/**
	 * Send command to control metric
	 *
	 * @param request the request is request param of command
	 */
	private void sendCommandToControlMetric(String request) {
		try {
			String response = send(request);
			response = response.substring(request.length() + PDUConstant.NUMBER_TWO, response.lastIndexOf(PDUConstant.REGEX_DATA));
			ResponseControlDataWrapper responseControlDataWrapper = mapper.readValue(response, ResponseControlDataWrapper.class);
			if (PDUConstant.ERROR.equalsIgnoreCase(responseControlDataWrapper.getStatus())) {
				throw new ResourceNotReachableException(responseControlDataWrapper.getMsg());
			}
		} catch (Exception e) {
			throw new ResourceNotReachableException(String.format("Error while controlling %s", e.getMessage()), e);
		}
	}

	/**
	 * Convert Outlet Scheduler Event by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of OutletAutoPing
	 */
	private EventDetails convertOutletScheduleByValues(String propertyName, Map<String, String> stats, boolean isCreateEvent) {
		EventDetails eventDetails = new EventDetails();
		String groupKey = propertyName + PDUConstant.HASH;
		String eventID = stats.get(groupKey + OutletScheduleEnum.EVENT_ID.getName());
		String time = stats.get(groupKey + OutletScheduleEnum.TIME.getName());
		String action = stats.get(groupKey + OutletScheduleEnum.ACTION.getName());
		String outletID;
		Map<String, String> mapOfDayAndEvent;
		if (isCreateEvent) {
			outletID = stats.get(groupKey + CreateEventEnum.OUTLET_ID.getName());
			mapOfDayAndEvent = eventIdAndDayMap;
		} else {
			//Example name OutletScheduler1Event get 1 in name
			int indexOutlet = propertyName.length() - PDUConstant.EVENTS.length();
			outletID = propertyName.substring(indexOutlet - 1, indexOutlet);
			mapOfDayAndEvent = outletIdEventIdAndDayMap.get(outletID);
		}

		eventDetails.setOutletId(outletID);
		eventDetails.setId(eventID);
		eventDetails.setAction(action);
		eventDetails.setTime(time);
		eventDetails.setDays(mapOfDayAndEvent.keySet().toArray(new String[0]));

		return eventDetails;
	}

	/**
	 * Add new Day for scheduler event
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void addNewDayForSchedulerEvent(String property, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] dayArrays = EnumTypeHandler.getEnumNames(DaysEnum.class);
		String key = PDUConstant.EMPTY_STRING;
		String value = PDUConstant.EMPTY_STRING;
		Map<String, String> mapOfDayAndEvent;
		if (property.contains(CreateEventEnum.CREATE_EVENT.getName())) {
			mapOfDayAndEvent = eventIdAndDayMap;
		} else {
			int len = property.length() - PDUConstant.EVENTS.length();
			String outletID = property.substring(len - 1, len);
			mapOfDayAndEvent = outletIdEventIdAndDayMap.get(outletID);
		}

		if (mapOfDayAndEvent != null) {
			for (Map.Entry<String, String> keyEntry : mapOfDayAndEvent.entrySet()) {
				if (keyEntry.getValue() == null) {
					key = keyEntry.getKey();
					break;
				}
			}
			if (StringUtils.isNullOrEmpty(key)) {
				throw new ResourceNotReachableException("Maximum of day is 7 with index from Day0 - Day6");
			}
			for (String day : dayArrays) {
				if (!mapOfDayAndEvent.containsValue(day) && !PDUConstant.NONE.equals(day)) {
					value = day;
					break;
				}
			}
			advancedControllableProperties.add(controlDropdown(stats, dayArrays, property + PDUConstant.HASH + key, value));
			mapOfDayAndEvent.put(key, value);
		}
	}

	/**
	 * Populate add new day for scheduler event
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param eventId the eventId is id of SchedulerEvent
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void populateDaysForSchedulerEvent(String property, String eventId, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] daysArray = EnumTypeHandler.getEnumNames(DaysEnum.class);
		Map<String, String> mapOfDayAndEvent = outletIdEventIdAndDayMap.get(eventId);
		if (mapOfDayAndEvent != null) {
			for (Map.Entry<String, String> keyEntry : mapOfDayAndEvent.entrySet()) {
				if (keyEntry.getValue() != null) {
					advancedControllableProperties.add(controlDropdown(stats, daysArray, property + PDUConstant.EVENTS + PDUConstant.HASH + keyEntry.getKey(), keyEntry.getValue()));
				}
			}
		}
	}

	/**
	 * Control Outlet Auto Ping
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlOutletAutoPing(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		OutletAutoPingEnum outletAutoPingEnum = EnumTypeHandler.getMetricOfEnumByName(OutletAutoPingEnum.class, propertyKey);
		switch (outletAutoPingEnum) {
			case NOTIFICATION:
			case DESTINATION:
			case OUTLET_STATE:
				updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
				break;
			case ATTEMPTS:
				value = String.valueOf(getValueByRange(PDUConstant.MIN_ATTEMPTS, PDUConstant.MAX_ATTEMPTS, value));
				AdvancedControllableProperty attemptsControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, attemptsControllableProperty);
				break;
			case INTERVAL:
				value = String.valueOf(getValueByRange(PDUConstant.MIN_INTERVAL, PDUConstant.MAX_INTERVAL, value));
				AdvancedControllableProperty intervalControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, intervalControllableProperty);
				break;
			case LIMIT:
				value = String.valueOf(getValueByRange(PDUConstant.MIN_LIMIT, PDUConstant.MAX_LIMIT, value));
				AdvancedControllableProperty limitControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, limitControllableProperty);
				break;
			case PERIOD:
				value = String.valueOf(getValueByRange(PDUConstant.MIN_PERIOD, PDUConstant.MAX_PERIOD, value));
				AdvancedControllableProperty periodControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, periodControllableProperty);
				break;
			case TIMEOUT:
				value = String.valueOf(getValueByRange(PDUConstant.MIN_TIMEOUT, PDUConstant.MAX_TIMEOUT, value));
				AdvancedControllableProperty timeoutControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, timeoutControllableProperty);
				break;
			case APPLY_CHANGE:
				OutletAutoPing outletAutoPing = convertOutletAutoPingByValues(propertyName, stats);
				sendCommandToControlMetric(outletAutoPing.getParamRequestOfOutletAutoPing());
				isEmergencyDelivery = false;
				break;
			case CANCEL:
				isEmergencyDelivery = false;
				break;
			default:
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Controlling outlet auto ping group config %s is not supported.", outletAutoPingEnum.getName()));
				}
		}
	}

	/**
	 * Convert Outlet Auto Ping by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of OutletAutoPing
	 */
	private OutletAutoPing convertOutletAutoPingByValues(String propertyName, Map<String, String> stats) {
		OutletAutoPing outletAutoPing = new OutletAutoPing();
		String groupKey = propertyName + PDUConstant.HASH;
		String outletState = stats.get(groupKey + OutletAutoPingEnum.OUTLET_STATE.getName());
		String destination = stats.get(groupKey + OutletAutoPingEnum.DESTINATION.getName());
		String timeout = stats.get(groupKey + OutletAutoPingEnum.TIMEOUT.getName());
		String interval = stats.get(groupKey + OutletAutoPingEnum.INTERVAL.getName());
		String limit = stats.get(groupKey + OutletAutoPingEnum.LIMIT.getName());
		String period = stats.get(groupKey + OutletAutoPingEnum.PERIOD.getName());
		String attempts = stats.get(groupKey + OutletAutoPingEnum.ATTEMPTS.getName());
		String notification = stats.get(groupKey + OutletAutoPingEnum.NOTIFICATION.getName());
		String id = propertyName.substring(propertyName.length() - 1);

		outletAutoPing.setId(id);
		outletAutoPing.setEnabled(outletState);
		outletAutoPing.setDestination(destination);
		outletAutoPing.setTimeout(timeout);
		outletAutoPing.setInterval(interval);
		outletAutoPing.setLimit(limit);
		outletAutoPing.setPeriod(period);
		outletAutoPing.setAttempts(attempts);
		outletAutoPing.setNotification(notification);

		return outletAutoPing;
	}

	/**
	 * Control Alert Config
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlOutletConfig(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		if (propertyKey.equalsIgnoreCase(OutletStatusEnum.RESET_PEAK.getName())) {
			String id = propertyName.substring(propertyName.length() - 1);
			sendCommandToControlResetPeak(id);
		} else {
			OutletConfigEnum outletConfigEnum = EnumTypeHandler.getMetricOfEnumByName(OutletConfigEnum.class, propertyKey);
			switch (outletConfigEnum) {
				case NAME:
				case LOCAL_REBOOT:
					updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
					break;
				case POWER_OFF_DELAY:
				case POWER_ON_DELAY:
					value = String.valueOf(getValueByRange(PDUConstant.DELAY_MIN, PDUConstant.DELAY_MAX, value));
					AdvancedControllableProperty powerControllableProperty = controlTextOrNumeric(stats, property, value, true);
					addOrUpdateAdvanceControlProperties(advancedControllableProperties, powerControllableProperty);
					break;
				case APPLY_CHANGE:
					OutletConfig outletConfig = convertOutletConfigByValues(propertyName, stats);
					sendCommandToControlMetric(outletConfig.getParamRequestOfOutletConfig());
					sendCommandToControlPowerStatus(outletConfig);
					isEmergencyDelivery = false;
					break;
				case CANCEL:
					isEmergencyDelivery = false;
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling outlet config group config %s is not supported.", outletConfigEnum.getName()));
					}
			}
		}
	}

	/**
	 * Send Command to control Outlet Config
	 *
	 * @param outletConfig the alertOutlet is OutletConfig DTO instance
	 * @throws ResourceNotReachableException if control OutletConfig error
	 */
	private void sendCommandToControlPowerStatus(OutletConfig outletConfig) {
		String status = outletStatusList.get(Integer.parseInt(outletConfig.getId())).getStatus();
		if (!outletConfig.getStatus().equalsIgnoreCase(status)) {

			// set outlet-power -o <outletNo> -v <value>
			String request = CommandControl.SET_POWER_STATUS.getName() + PDUConstant.PARAM_DASH_O + outletConfig.getId() + PDUConstant.PARAM_DASH_V + outletConfig.getStatus();
			sendCommandToControlMetric(request);
		}
	}

	/**
	 * Send Command to reset peak
	 *
	 * @param outletID the outletID is of outlet
	 * @throws ResourceNotReachableException if control OutletConfig error
	 */
	private void sendCommandToControlResetPeak(String outletID) {
		//set outlet-peak-reset -o <outletNo>
		String request = CommandControl.SET_RESET_PEAK.getName() + PDUConstant.PARAM_DASH_O + outletID;
		sendCommandToControlMetric(request);
	}

	/**
	 * Convert Outlet Config by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of OutletConfig
	 */
	private OutletConfig convertOutletConfigByValues(String propertyName, Map<String, String> stats) {
		OutletConfig outletConfig = new OutletConfig();
		String groupKey = propertyName + PDUConstant.HASH;
		String name = stats.get(groupKey + OutletConfigEnum.NAME.getName());
		String localReboot = stats.get(groupKey + OutletConfigEnum.LOCAL_REBOOT.getName());
		String powerOff = stats.get(groupKey + OutletConfigEnum.POWER_OFF_DELAY.getName());
		String powerOn = stats.get(groupKey + OutletConfigEnum.POWER_ON_DELAY.getName());
		String statusValue = stats.get(groupKey + OutletConfigEnum.OUTLET_STATUS.getName());
		String id = propertyName.substring(propertyName.length() - 1);
		String status = PDUConstant.OFF;
		if (String.valueOf(PDUConstant.NUMBER_ONE).equals(statusValue)) {
			status = PDUConstant.ON;
		}
		outletConfig.setId(id);
		outletConfig.setName(name);
		outletConfig.setLocalReboot(localReboot);
		outletConfig.setPowerOffDelay(powerOff);
		outletConfig.setPowerOnDelay(powerOn);
		outletConfig.setStatus(status);

		return outletConfig;
	}

	/**
	 * Control Alert Outlet
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlAlertOutlet(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		AlertOutletEnum alertOutletEnum = EnumTypeHandler.getMetricOfEnumByName(AlertOutletEnum.class, propertyKey);
		switch (alertOutletEnum) {
			case CURRENT_ALERT:
			case POWER_ALERT:
				String[] typeDropdown = EnumTypeHandler.getEnumNames(AlertTypeEnum.class);
				AdvancedControllableProperty alertControllableProperty = controlDropdown(stats, typeDropdown, property, value);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, alertControllableProperty);
				break;
			case CURRENT_MAX:
			case CURRENT_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.CURRENT_MIN, PDUConstant.CURRENT_MAX, value));
				AdvancedControllableProperty currentControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, currentControllableProperty);
				break;
			case POWER_MAX:
			case POWER_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.POWER_MIN, PDUConstant.POWER_MAX, value));
				AdvancedControllableProperty powerControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, powerControllableProperty);
				break;
			case APPLY_CHANGE:
				AlertOutlet alertOutlet = convertAlertOutletByValues(propertyName, stats);
				sendCommandToControlMetric(alertOutlet.getParamRequestOfAlertOutlet());
				isEmergencyDelivery = false;
				break;
			case CANCEL:
				isEmergencyDelivery = false;
				break;
			default:
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Controlling alert outlet group config %s is not supported.", alertOutletEnum.getName()));
				}
		}
	}

	/**
	 * Convert Alert Outlet by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of AlertOutlet
	 */
	private AlertOutlet convertAlertOutletByValues(String propertyName, Map<String, String> stats) {
		AlertOutlet alertOutlet = new AlertOutlet();
		String groupKey = propertyName + PDUConstant.HASH;
		String currentAlert = stats.get(groupKey + AlertOutletEnum.CURRENT_ALERT.getName());
		String powerAlert = stats.get(groupKey + AlertOutletEnum.POWER_ALERT.getName());
		String currentMin = stats.get(groupKey + AlertOutletEnum.CURRENT_MIN.getName());
		String currentMax = stats.get(groupKey + AlertOutletEnum.CURRENT_MAX.getName());
		String powerMin = stats.get(groupKey + AlertOutletEnum.POWER_MIN.getName());
		String powerMax = stats.get(groupKey + AlertOutletEnum.POWER_MAX.getName());
		String id = propertyName.substring(propertyName.length() - 1);

		alertOutlet.setId(id);
		alertOutlet.setCurrentAlert(currentAlert.split(PDUConstant.COMMA));
		alertOutlet.setPowerAlert(powerAlert.split(PDUConstant.COMMA));
		alertOutlet.setCurrentMin(currentMin);
		alertOutlet.setCurrentMax(currentMax);
		alertOutlet.setPowerMin(powerMin);
		alertOutlet.setPowerMax(powerMax);

		return alertOutlet;
	}

	/**
	 * Control Alert Global
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlAlertGlobal(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		AlertGlobalEnum alertGlobalEnum = EnumTypeHandler.getMetricOfEnumByName(AlertGlobalEnum.class, propertyKey);

		switch (alertGlobalEnum) {
			case POWER_ALERT:
			case CURRENT_ALERT:
			case VOLTAGE_ALERT:
			case TEMP_ALERT:
				String[] typeDropdown = EnumTypeHandler.getEnumNames(AlertTypeEnum.class);
				AdvancedControllableProperty alertControllableProperty = controlDropdown(stats, typeDropdown, property, value);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, alertControllableProperty);
				break;
			case CURRENT_MAX:
			case CURRENT_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.CURRENT_MIN, PDUConstant.CURRENT_MAX, value));
				AdvancedControllableProperty currentControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, currentControllableProperty);
				break;
			case POWER_MAX:
			case POWER_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.POWER_MIN, PDUConstant.POWER_MAX, value));
				AdvancedControllableProperty powerControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, powerControllableProperty);
				break;
			case TEMP_MAX:
			case TEMP_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.TEMPERATURE_MIN, PDUConstant.TEMPERATURE_MAX, value));
				AdvancedControllableProperty tempControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, tempControllableProperty);
				break;
			case VOLTAGE_MAX:
			case VOLTAGE_MIN:
				value = String.valueOf(getValueByRange(PDUConstant.VOLTAGE_MIN, PDUConstant.VOLTAGE_MAX, value));
				AdvancedControllableProperty voltageControllableProperty = controlTextOrNumeric(stats, property, value, true);
				addOrUpdateAdvanceControlProperties(advancedControllableProperties, voltageControllableProperty);
				break;
			case APPLY_CHANGE:
				AlertGlobal alertGlobalDTO = convertAlertGlobalByValues(propertyName, stats);
				sendCommandToControlMetric(alertGlobalDTO.getParamRequestOfAlertGlobal());
				isEmergencyDelivery = false;
				break;
			case CANCEL:
				isEmergencyDelivery = false;
				break;
			case TEMP_SENSOR:
			case TEMP_UNIT:
				break;
			default:
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Controlling alert global group config %s is not supported.", alertGlobalEnum.getName()));
				}
		}
	}

	/**
	 * Convert Alert Global by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of alertGlobal
	 */
	private AlertGlobal convertAlertGlobalByValues(String propertyName, Map<String, String> stats) {
		AlertGlobal alertGlobalDTO = new AlertGlobal();
		String groupKey = propertyName + PDUConstant.HASH;
		String currentAlert = stats.get(groupKey + AlertGlobalEnum.CURRENT_ALERT.getName());
		String powerAlert = stats.get(groupKey + AlertGlobalEnum.POWER_ALERT.getName());
		String tempAlert = stats.get(groupKey + AlertGlobalEnum.TEMP_ALERT.getName());
		String voltageAlert = stats.get(groupKey + AlertGlobalEnum.VOLTAGE_ALERT.getName());
		String currentMin = stats.get(groupKey + AlertGlobalEnum.CURRENT_MIN.getName());
		String currentMax = stats.get(groupKey + AlertGlobalEnum.CURRENT_MAX.getName());
		String powerMin = stats.get(groupKey + AlertGlobalEnum.POWER_MIN.getName());
		String powerMax = stats.get(groupKey + AlertGlobalEnum.POWER_MAX.getName());
		String tempMin = stats.get(groupKey + AlertGlobalEnum.TEMP_MIN.getName());
		String tempMax = stats.get(groupKey + AlertGlobalEnum.TEMP_MAX.getName());
		String voltageMin = stats.get(groupKey + AlertGlobalEnum.VOLTAGE_MIN.getName());
		String voltageMax = stats.get(groupKey + AlertGlobalEnum.VOLTAGE_MAX.getName());

		alertGlobalDTO.setCurrentAlert(currentAlert.split(PDUConstant.COMMA));
		alertGlobalDTO.setPowerAlert(powerAlert.split(PDUConstant.COMMA));
		alertGlobalDTO.setTempAlert(tempAlert.split(PDUConstant.COMMA));
		alertGlobalDTO.setVoltageAlert(voltageAlert.split(PDUConstant.COMMA));
		alertGlobalDTO.setCurrentMax(currentMax);
		alertGlobalDTO.setCurrentMin(currentMin);
		alertGlobalDTO.setPowerMin(powerMin);
		alertGlobalDTO.setPowerMax(powerMax);
		alertGlobalDTO.setTempMin(tempMin);
		alertGlobalDTO.setTempMax(tempMax);
		alertGlobalDTO.setVoltageMin(voltageMin);
		alertGlobalDTO.setVoltageMax(voltageMax);
		return alertGlobalDTO;
	}

	/**
	 * Control Alert Email
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlAlertEmail(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		AlertEmailEnum alertMailEnum = EnumTypeHandler.getMetricOfEnumByName(AlertEmailEnum.class, propertyKey);
		switch (alertMailEnum) {
			case SUBJECT:
			case RECIPIENT:
				updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
				break;
			case CANCEL_CHANGE:
				isEmergencyDelivery = false;
				break;
			case APPLY_CHANGE:
				AlertEMail alertMail = convertAlertEmailValues(propertyName, stats);
				sendCommandToControlMetric(alertMail.getParamRequestOfAlertMail());
				isEmergencyDelivery = false;
				break;
			default:
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Controlling alert email group config %s is not supported.", alertMailEnum.getName()));
				}
		}
	}

	/**
	 * Convert Alert Email by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of DPUDisplay and index
	 */
	private AlertEMail convertAlertEmailValues(String propertyName, Map<String, String> stats) {
		AlertEMail alertMail = new AlertEMail();
		String recipient = stats.get(propertyName + PDUConstant.HASH + AlertEmailEnum.RECIPIENT.getName());
		String subject = stats.get(propertyName + PDUConstant.HASH + AlertEmailEnum.SUBJECT.getName());
		alertMail.setRecipient(recipient);
		alertMail.setSubject(subject);

		return alertMail;
	}

	/**
	 * Control PDU Display
	 *
	 * @param property the property is the filed name of controlling metric
	 * @param value the value is value of metric
	 * @param stats list of stats
	 * @param advancedControllableProperties the advancedControllableProperties is advancedControllableProperties instance
	 */
	private void controlPDUDisplay(String property, String value, Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperties) {
		String[] propertySplit = property.split(PDUConstant.HASH);
		String propertyName = propertySplit[0];
		String propertyKey = propertySplit[1];
		PDUDisplayEnum displayEnum = EnumTypeHandler.getMetricOfEnumByName(PDUDisplayEnum.class, propertyKey);
		switch (displayEnum) {
			case LED:
			case OLED_CONTRAST:
			case OLED_ENABLE:
			case TEMPERATURE_UNIT:
				updateValueForTheControllableProperty(property, value, stats, advancedControllableProperties);
				break;
			case CANCEL_CHANGE:
				isEmergencyDelivery = false;
				break;
			case APPLY_CHANGE:
				PDUDisplay pduDisplayData = convertDPUDisplayByValues(propertyName, stats);
				sendCommandToControlMetric(pduDisplayData.getParamRequestOfPDUDisplay());
				sendCommandToControlTemperatureUnit(pduDisplayData);
				isEmergencyDelivery = false;
				break;
			default:
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Controlling pdu display group config %s is not supported.", displayEnum.getName()));
				}
		}
	}

	/**
	 * Send Command to control TemperatureUnit
	 *
	 * @param pdu the pdu is DPUDisplay instance
	 * @throws ResourceNotReachableException if control TemperatureUnit error
	 */
	private void sendCommandToControlTemperatureUnit(PDUDisplay pdu) {
		String tempUnitCurrent = pdu.getTempUnit();
		String tempUnit = pduStatus.getPduTempUnit();
		if (!tempUnitCurrent.equalsIgnoreCase(tempUnit)) {
			//set temperature-unit -u <unit>
			String request = CommandControl.SET_TEMP_UNIT.getName() + PDUConstant.PARAM_DASH_U + tempUnitCurrent;
			sendCommandToControlMetric(request);
		}
	}

	/**
	 * Convert DPU Display by value
	 *
	 * @param stats the stats are list of stats
	 * @return propertyName is name of DPUDisplay and index
	 */
	private PDUDisplay convertDPUDisplayByValues(String propertyName, Map<String, String> stats) {
		PDUDisplay pduDisplayData = new PDUDisplay();
		String led = stats.get(propertyName + PDUConstant.HASH + PDUDisplayEnum.LED.getName());
		String oledContrast = stats.get(propertyName + PDUConstant.HASH + PDUDisplayEnum.OLED_CONTRAST.getName());
		String oledEnable = stats.get(propertyName + PDUConstant.HASH + PDUDisplayEnum.OLED_ENABLE.getName());
		String tempUnit = stats.get(propertyName + PDUConstant.HASH + PDUDisplayEnum.TEMPERATURE_UNIT.getName());

		String tempUnitValue = PDUConstant.TEMPERATURE_C;
		if (String.valueOf(PDUConstant.NUMBER_ONE).equalsIgnoreCase(tempUnit)) {
			tempUnitValue = PDUConstant.TEMPERATURE_F;
		}
		pduDisplayData.setTempUnit(tempUnitValue);
		pduDisplayData.setLed(led);
		pduDisplayData.setOledContrast(oledContrast);
		pduDisplayData.setOledEnabled(oledEnable);

		return pduDisplayData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void controlProperties(List<ControllableProperty> list) {
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
		outletStatusList.clear();
		outletAutoPingList.clear();
		outletIdAndEventDetailsMap.clear();
		outletConfigList.clear();
		alertOutletList.clear();
		outletIdExtractedList.clear();
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
		}
		if (outletIdAndEventId.isEmpty()) {
			//init outletId and EventId
			for (int outletId = 1; outletId <= PDUConstant.OUTLET_ID_MAX; outletId++) {
				outletIdAndEventId.put(outletId, 0);
			}
		}
		if (noOfMonitoringMetric == 0) {
			noOfMonitoringMetric = MonitoringMetric.values().length;
		}
		if (failedMonitor.size() == noOfMonitoringMetric) {
			StringBuilder errorMessage = new StringBuilder();
			for (Map.Entry<String, String> messageFailed : failedMonitor.entrySet()) {
				String value = messageFailed.getValue();
				if (!errorMessage.toString().contains(value)) {
					errorMessage.append(value + PDUConstant.SPACE);
				}
			}
			failedMonitor.clear();
			throw new ResourceNotReachableException("Get monitoring data failed: " + errorMessage);
		}
		for (MonitoringMetric metric : MonitoringMetric.values()) {
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
			case START_TIME:
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
				controlOutletScheduler(stats, advancedControllableProperties);
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
			case START_TIME:
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
					if (outletIdExtractedList != null && !outletIdExtractedList.isEmpty()) {
						if (outletIdExtractedList.contains(String.valueOf(outletID))) {
							populateRetrieveDataByMetric(metric, outletID);
						}
					} else {
						populateRetrieveDataByMetric(metric, outletID);
					}
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
			if (responseData == null || result == null || !result.contains(PDUConstant.SUCCESS)) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
			}
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
				case START_TIME:
				case TIMEZONE:
				case ALTER_EMAIL:
					AlertEmailWrapper alertEMailWrapper = mapper.readValue(responseData, AlertEmailWrapper.class);
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
						pduStatus = pduStatusWrapper.getResponse();
					}
					break;
				case PDU_DISPLAY:
					PDUDisplayWrapper pduDisplayWrapper = mapper.readValue(responseData, PDUDisplayWrapper.class);
					if (pduDisplayWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(pduDisplayWrapper.getStatus())) {
						pduDisplay = pduDisplayWrapper.getResponse();
					}
					break;
				case OUTLET_SCHEDULER_EVENT:
					OutletScheduleWrapper outletScheduleWrapper = mapper.readValue(responseData, OutletScheduleWrapper.class);
					if (outletScheduleWrapper != null && !PDUConstant.ERROR.equalsIgnoreCase(outletScheduleWrapper.getStatus())) {
						int index = outletIdAndEventDetailsMap.size() + 1;
						List<EventDetails> eventDetailsList = outletScheduleWrapper.getResponse().getEvents();
						outletIdAndEventDetailsMap.put(index, eventDetailsList);

						for (EventDetails event : eventDetailsList) {
							Map<String, String> dayIndexAndValue = new HashMap<>();
							//init map with index to 0-6
							for (int i = 0; i <= PDUConstant.MAX_INDEX_DAY; i++) {
								dayIndexAndValue.put(PDUConstant.DAY + i, null);
							}
							int i = 0;
							for (String item : event.getDays()) {
								dayIndexAndValue.put(PDUConstant.DAY + i, item);
								i++;
							}
							outletIdEventIdAndDayMap.put(event.getId(), dayIndexAndValue);
						}
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
						outletConfigList.add(outletConfigWrapper.getResponse());
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

			if (responseData == null || deviceInfoWrapper == null || deviceInfoWrapper.getStatus().equalsIgnoreCase(PDUConstant.ERROR)) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
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
			if (responseData == null || timeZoneWrapper == null || PDUConstant.ERROR.equals(timeZoneWrapper.getStatus())) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
				stats.put(MonitoringMetric.TIMEZONE.getName(), PDUConstant.NONE);
			} else {
				stats.put(MonitoringMetric.TIMEZONE.getName(), timeZoneWrapper.getResponse().getTimezone());
			}
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
			if (dateAndTimeWrapper == null || PDUConstant.ERROR.equals(dateAndTimeWrapper.getStatus())) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
				stats.put(PDUConstant.DATE, PDUConstant.NONE);
			} else {
				stats.put(PDUConstant.TIME, dateAndTimeWrapper.getResponse().getTime());
				stats.put(PDUConstant.DATE, dateAndTimeWrapper.getResponse().getDate());
			}
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
			if (uptimeWrapper == null || PDUConstant.ERROR.equals(uptimeWrapper.getStatus())) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
			} else {
				stats.put(MonitoringMetric.UPTIME.getName(), formatTimeData(uptimeWrapper.getResponse().getUptime()));
			}
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

	/**
	 * Control Outlet Status for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletStatus(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {

		int outletID = 1;
		for (OutletStatus outletStatus : outletStatusList) {
			for (OutletStatusEnum metric : OutletStatusEnum.values()) {
				String key = MonitoringMetric.OUTLET_CONF.getName() + outletID + PDUConstant.HASH + metric.getName();
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
			outletID++;
		}
	}

	/**
	 * Control Outlet Alerts for the Pakedge PDU
	 *
	 * @param stats list of statistics property
	 * @param advancedControllableProperty the advancedControllableProperty is list AdvancedControllableProperties instance
	 */
	private void populateControlOutletAlerts(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {

		//init outletId = 1 and maximum outletId is 9
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
			for (AlertEmailEnum metric : AlertEmailEnum.values()) {
				String key = MonitoringMetric.ALTER_EMAIL.getName() + PDUConstant.HASH + metric.getName();
				if (AlertEmailEnum.RECIPIENT.getName().equals(metric.getName())) {
					AdvancedControllableProperty recipientProperty = controlTextOrNumeric(stats, key, alertEmail.getRecipient(), false);
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, recipientProperty);
					continue;
				}
				if (AlertEmailEnum.SUBJECT.getName().equals(metric.getName())) {
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
					case OUTLET_STATE:
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
		for (OutletConfig outletConfig : outletConfigList) {
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
	private void controlOutletScheduler(Map<String, String> stats, List<AdvancedControllableProperty> advancedControllableProperty) {
		for (Entry<Integer, List<EventDetails>> outletSchedule : outletIdAndEventDetailsMap.entrySet()) {
			int outletIndex = outletSchedule.getKey();
			List<EventDetails> eventDetailsList = outletSchedule.getValue();
			int eventIdCurrent = outletIdAndEventId.get(outletIndex);
			EventDetails eventDetails = eventDetailsList.get(eventIdCurrent);
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
					case EVENT_ID:
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
						String[] times = EnumTypeHandler.getEnumNames(TimeEnum.class);
						AdvancedControllableProperty timesControl = controlDropdown(stats, times, key, value);
						addOrUpdateAdvanceControlProperties(advancedControllableProperty, timesControl);
						break;
					case DAYS:
						populateDaysForSchedulerEvent(MonitoringMetric.OUTLET_SCHEDULER_EVENT.getName() + outletIndex, String.valueOf(eventIdCurrent), stats, advancedControllableProperty);
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
			if (pduStatusWrapper == null || PDUConstant.ERROR.equals(pduStatusWrapper.getStatus())) {
				failedMonitor.put(metric.getName(), String.format(PDUConstant.ERROR_MESSAGE, metric.getName(), responseData));
			} else {
				for (NetworkInfoEnum networkInfoEnum : NetworkInfoEnum.values()) {
					String value = convertValueByIndexOfSpace(pduStatusWrapper.getResponse().getValueByMetric(networkInfoEnum));
					stats.put(networkInfoEnum.getName(), value);
				}
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
				case DAY_0:
					//init list day
					for (int i = 0; i <= 6; i++) {
						eventIdAndDayMap.put(PDUConstant.DAY + i, null);
					}
					String[] days = EnumTypeHandler.getEnumNames(DaysEnum.class);
					AdvancedControllableProperty daysControl = controlDropdown(stats, days, key, DaysEnum.MON.getName());
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, daysControl);
					eventIdAndDayMap.put(CreateEventEnum.DAY_0.getName(), DaysEnum.MON.getName());
					break;
				case OUTLET_ID:
					stats.put(key, PDUConstant.EMPTY_STRING);
					advancedControllableProperty.add(controlDropdown(stats, outletIdList, key, outletIdList[0]));
					break;
				case START_TIME:
					String[] startTimes = EnumTypeHandler.getEnumNames(TimeEnum.class);
					AdvancedControllableProperty timesControl = controlDropdown(stats, startTimes, key, TimeEnum.HOUR_0.getName());
					addOrUpdateAdvanceControlProperties(advancedControllableProperty, timesControl);
					break;
				default:
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Controlling create scheduler event group %s is not supported.", event.getName()));
					}
					break;
			}
		}
		stats.put(keyGroup + PDUConstant.CREATE_EVENT, PDUConstant.EMPTY_STRING);
		advancedControllableProperty.add(createButton(keyGroup + PDUConstant.CREATE_EVENT, PDUConstant.CREATE, PDUConstant.CREATING, 0));
		stats.put(keyGroup + PDUConstant.EDITED, PDUConstant.FALSE);
	}

	/**
	 * Get value by range if the value out of range return the initial value
	 *
	 * @param min is the minimum value
	 * @param max is the maximum value
	 * @param value is the value to compare between min and max value
	 * @return int is value or initial value
	 */
	private int getValueByRange(int min, int max, String value) {
		int initial = min;
		try {
			int valueCompare = Integer.parseInt(value);
			if (min <= valueCompare && valueCompare <= max) {
				return valueCompare;
			}
			if (valueCompare > max) {
				initial = max;
			}
			return initial;
		} catch (Exception e) {
			//example value  1xxxxxxx, return max value
			//example value -1xxxxxxx, return min value
			if (!value.contains(PDUConstant.DASH)) {
				initial = max;
			}
			return initial;
		}
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
	 * Get list outlet id by adapter filter
	 *
	 * @param outletId the outletId is list id of Outlet
	 */
	private void extractListOutletIDFilter(String outletId) {
		List<String> outletIdList = new ArrayList<>();
		if (!StringUtils.isNullOrEmpty(outletId)) {
			String[] nameStringFilter = outletId.split(PDUConstant.COMMA);
			for (String listNameItem : nameStringFilter) {
				outletIdList.add(listNameItem.trim());
			}
		}
		outletIdExtractedList = outletIdList;
	}

	/**
	 * Get default value by Null value. if value different Null return the value instead.
	 *
	 * @param value the value of monitoring properties
	 * @return String (none/value)
	 */
	private String getDefaultValueForNoneData(String value) {

		return StringUtils.isNullOrEmpty(value) ? PDUConstant.NONE : value;
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
	 * Update the value for the control metric
	 *
	 * @param property is name of the metric
	 * @param value the value is value of properties
	 * @param extendedStatistics list statistics property
	 * @param advancedControllableProperties the advancedControllableProperties is list AdvancedControllableProperties
	 */
	private void updateValueForTheControllableProperty(String property, String value, Map<String, String> extendedStatistics, List<AdvancedControllableProperty> advancedControllableProperties) {
		for (AdvancedControllableProperty advancedControllableProperty : advancedControllableProperties) {
			if (advancedControllableProperty.getName().equals(property)) {
				extendedStatistics.put(property, value);
				advancedControllableProperty.setValue(value);
				break;
			}
		}
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
		if (StringUtils.isNullOrEmpty(value)) {
			value = PDUConstant.NONE;
		}
		stats.put(name, value);
		if (!PDUConstant.NONE.equals(value) && !StringUtils.isNullOrEmpty(value)) {
			return createSwitch(name, Integer.parseInt(value), labelOff, labelOn);
		}
		// if response data is null or none. Only display monitoring data not display controlling data
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
}