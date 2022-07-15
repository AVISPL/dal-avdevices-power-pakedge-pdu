/*
 * Copyright (c) 2022 AVI-SPL, Inc. All Rights Reserved.
 */

package com.avispl.symphony.dal.avdevices.power.pakedge.pdu.common;

/**
 * PDUConstant class provides during the monitoring and controlling process
 *
 * @author Harry / Symphony Dev Team<br>
 * Created on 6/30/2022
 * @since 1.0.0
 */
public class PDUConstant {

	public static final String NONE = "None";
	public static final String HASH = "#";
	public static final String FALSE = "False";
	public static final String TRUE = "True";
	public static final String SPACE = " ";
	public static final String EMPTY_STRING = "";
	public static final String COMMA = ",";
	public static final String ENABLE = "Enable";
	public static final String DISABLE = "Disable";
	public static final String OFF = "Off";
	public static final String ON = "On";
	public static final String TEMPERATURE_C = "C";
	public static final String TEMPERATURE_F = "F";
	public static final String DAY = "Day";
	public static final String DAY_0 = "Day0";
	public static final String DAYS = " day(s) ";
	public static final String HOUR = " hour(s) ";
	public static final String MINUTE = " minute(s) ";
	public static final String SECOND = " second(s)";
	public static final String DASH = "-";
	public static final String EDITED = "Edited";
	public static final String APPLY = "Apply";
	public static final String APPLYING = "Applying";
	public static final String CANCEL = "Cancel";
	public static final String CANCELING = "Canceling";
	public static final String DATE = "Date";
	public static final String TIME = "Time";
	public static final String PDU = "PDU";
	public static final String RESET = "Reset";
	public static final String RESETTING = "Resetting";
	public static final String EVENTS = "Events";
	public static final String ADD = "Add";
	public static final String ADDING = "Adding";
	public static final String CREATE_EVENT = "CreateEvent";
	public static final String CREATE = "Create";
	public static final String CREATING = "Creating";
	public static final String CANCEL_CHANGE = "CancelChange";
	public static final String APPLY_CHANGE = "ApplyChange";
	public static final String ERROR = "Error";
	public static final String SUCCESS = "success";
	public static final String REGEX_DATA = "\r\n";
	public static final String PARAM_DASH_V = " -v ";
	public static final String PARAM_DASH_O = " -o ";
	public static final String PARAM_DASH_R = " -r ";
	public static final String PARAM_DASH_U = " -u ";
	public static final String QUOTATION_MARKS = "\"\"";
	public static final String[] OUTLET_ID_LIST = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
	public static final int MIN_LIMIT = 5;
	public static final int MAX_LIMIT = 240;
	public static final int MIN_ATTEMPTS = 2;
	public static final int MAX_ATTEMPTS = 20;
	public static final int MIN_TIMEOUT = 2;
	public static final int MAX_TIMEOUT = 10;
	public static final int MIN_INTERVAL = 0;
	public static final int MAX_INTERVAL = 60;
	public static final int MIN_PERIOD = 1;
	public static final int MAX_PERIOD = 24;
	public static final int DELAY_MIN = 0;
	public static final int DELAY_MAX = 300;
	public static final int CURRENT_MIN = 0;
	public static final int CURRENT_MAX = 15;
	public static final int POWER_MIN = 0;
	public static final int POWER_MAX = 3900;
	public static final int TEMPERATURE_MIN = 10;
	public static final int TEMPERATURE_MAX = 50;
	public static final int VOLTAGE_MIN = 50;
	public static final int VOLTAGE_MAX = 260;
	public static final int OUTLET_ID_MAX = 9;
	public static final int NUMBER_ONE = 1;
	public static final int ZERO = 0;
	public static final int NUMBER_TWO = 2;
	public static final int EVENT_DAYS_MAX = 6;
	public static final int MAX_INDEX_DAY = 6;
	public static final String ERROR_MESSAGE = "Can't retrieve %s: %s";
	public static final String PARAM_ALERT_GLOBAL = " -a %s -b %s -c %s -d %s -e %s -f %s -l %s -h %s -t %s -p %s -q %s -r %s";
	public static final String PARAM_ALERT_EMAIL = " -r %s -s %s";
	public static final String PARAM_ALERT_OUTLET = " -o %s -a %s -v %s -c %s -d %s -e %s -f %s";
	public static final String PARAM_OUTLET_EVENT = " -o %s %s -s %s -d %s -a %s";
	public static final String PARAM_OUTLET_AUTO_PING = " -o %s -e %s -d %s -t %s -i %s -l %s -p %s -a %s -n %s";
	public static final String PARAM_OUTLET_CONFIG = " -o %s -n %s -b %s -d %s -f %s";
	public static final String PARAM_PDU_DISPLAY = " -v %s -c %s -l %s";
	public static final String CONNECTION_REFUSED = "connection refused";
	public static final String CONNECTION_TIMEOUT = "connect timed out";
	public static final String CONNECTION_REFUSED_MESSAGE = "Failed to connect to connection refused";
	public static final String CONNECTION_TIMEOUT_MESSAGE = "Timeout: socket is not established";

}