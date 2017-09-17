/*******************************************************
 * Preferences Utilities
 *******************************************************/
package com.lenderman.ncidpop.utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import com.lenderman.ncidpop.compat.TableSorter;
import com.lenderman.ncidpop.preferences.jfd.NcidPopPreferences;
import com.lenderman.ncidpop.speech.TextToSpeechManager;

/**
 * Utilities for managing preferences.
 * 
 * @author Chris Lenderman
 */
public class PrefUtils
{
    /** Class logger */
    private static Logger log = Logger.getLogger(PrefUtils.class);

    /** Static instance of this class */
    public static PrefUtils instance = new PrefUtils(
            Preferences.userNodeForPackage(PrefUtils.class));

    /** The root preferences */
    private Preferences prefs = null;

    /**
     * A backing store for preferences. Used to support "dummy" test preferences
     * for when the prefs object is null
     */
    private ConcurrentHashMap<String, Object> backingStore = new ConcurrentHashMap<String, Object>();

    /** Text field preference strings */
    public static String PREF_SERVERS = "serverName";
    public static String PREF_PHONE_NUMBER_FORMAT = "phoneNumberFmt";
    public static String PREF_REVERSE_LOOKUP_URL = "reverseLookupUrl";
    public static String PREF_TEXT_MESSAGE_URL = "textMessageUrl";
    public static String PREF_TEXT_MESSAGE_EXTERNAL_PROGRAM = "textMessageExternalProgram";
    public static String PREF_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME = "textMessageGatewayRelayDeviceName";
    public static String PREF_RINGTONE = "ringtone";
    public static String PREF_THIRD_PARTY_NOTIFIER_STRING = "thirdPartyNotifierString";
    public static String PREF_THIRD_PARTY_NAME_READING_STRING = "thirdPartyNameReadingString";
    public static String PREF_THIRD_PARTY_MESSAGE_NOTIFIER_STRING = "thirdPartyMessageNotifierString";
    public static String PREF_MIXER_NAME = "mixerName";

    /** Radio button preference strings */
    public static String PREF_THIRD_PARTY_NAME_READING_SELECTION = "thirdPartyNameReadingSelection";
    public static String PREF_MESSAGE_POPUP_DISPLAY_SELECTION = "messagePopupDisplaySelection";
    public static String PREF_MESSAGE_FORMAT_OPTIONS_SELECTION = "messageFormatOptionsSelection";

    /**
     * A list to map relative wav files to absolute paths. Used for preference
     * portability.
     */
    public static HashMap<String, String> relativeWavFileToLocationMap = new HashMap<String, String>();

    /** Static init block */
    static
    {
        /**
         * Finds and returns a list of all supported wav files in the current
         * directory
         */
        File directoryFile;
        try
        {
            directoryFile = new File(OsUtils.getCurrentPath());

            String directory = directoryFile.getAbsolutePath();
            File srcFile = new File(directory);
            if ((srcFile != null) && srcFile.isDirectory())
            {
                for (File file : srcFile.listFiles())
                {
                    if (!file.isDirectory() && file.getName().endsWith("wav"))
                    {
                        String shortPath = file.getAbsolutePath().substring(
                                directory.length() + 1,
                                file.getAbsolutePath().length());

                        relativeWavFileToLocationMap.put(shortPath,
                                file.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.info("Couldn't get relative wav file path: " + e);
        }
    }

    /** Default text field constants */
    private static String DEFAULT_SERVERS = "localhost";
    private static String DEFAULT_PHONE_NUMBER_FORMAT = "$AREA-$NPA-$NXX";
    private static String DEFAULT_REVERSE_LOOKUP_URL = "http://www.411.com/phone/$AREA-$NPA-$NXX";
    private static String DEFAULT_TEXT_MESSAGE_URL = "http://www.somewebsite.com/sendText.php?phonenumber=$NUMBER";
    private static String DEFAULT_TEXT_MESSAGE_EXTERNAL_PROGRAM = "somexternalprogram $NUMBER \'$MESSAGE\'";
    private static String DEFAULT_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME = "";

    /** Default combo box constants */
    public static String DEFAULT_RINGTONE = "CIDRing.wav";
    public static String DEFAULT_MIXER_NAME = "(default)";

    /** Default radio button value "constants" */
    private static String DEFAULT_MESSAGE_POPUP_DISPLAY_SELECTION = NcidPopPreferences.MESSAGE_NOTIFY_DIALOG_LABEL;
    private static String DEFAULT_MESSAGE_FORMAT_OPTIONS_SELECTION = NcidPopPreferences.MESSAGE_FORMAT_PRETTY_PRINT_LABEL;

    /** This default for name reading is a method because it is dynamic */
    public static String DEFAULT_NAME_READING_SELECTION()
    {
        if (TextToSpeechManager.isSupported())
        {
            return NcidPopPreferences.NAME_READING_SYSTEM_DEFAULT_LABEL;
        }
        else if (OsUtils.getDefaultThirdPartyNameReadingString().length() > 0)
        {
            return NcidPopPreferences.NAME_READING_THIRD_PARTY_LABEL;
        }
        else
        {
            return NcidPopPreferences.NAME_READING_NONE_LABEL;
        }
    }

    /** Total call count preference key and default value */
    public static String PREF_TOTAL_CALL_COUNT = "totalCallCount";
    public static String DEFAULT_TOTAL_CALL_COUNT = "0";

    /** Total message count preference key and default value */
    public static String PREF_TOTAL_MSG_COUNT = "totalMessageCount";
    public static String DEFAULT_TOTAL_MSG_COUNT = "0";

    /** Preferences version compatibility */
    public static String PREF_VERSION_COMPATIBILITY = "prefsVersionCompatibility";
    public static final String DEFAULT_PREFS_VERSION_COMPATIBILITY_NUMBER = "0";

    /** Mapping from string preferences to default string values */
    private static HashMap<String, String> DEFAULT_STRING = new HashMap<String, String>();
    static
    {
        DEFAULT_STRING.put(PREF_SERVERS, DEFAULT_SERVERS);
        DEFAULT_STRING.put(PREF_PHONE_NUMBER_FORMAT,
                DEFAULT_PHONE_NUMBER_FORMAT);
        DEFAULT_STRING.put(PREF_REVERSE_LOOKUP_URL, DEFAULT_REVERSE_LOOKUP_URL);
        DEFAULT_STRING.put(PREF_TEXT_MESSAGE_URL, DEFAULT_TEXT_MESSAGE_URL);
        DEFAULT_STRING.put(PREF_TEXT_MESSAGE_EXTERNAL_PROGRAM,
                DEFAULT_TEXT_MESSAGE_EXTERNAL_PROGRAM);
        DEFAULT_STRING.put(PREF_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME,
                DEFAULT_TEXT_MESSAGE_GATEWAY_RELAY_DEVICE_NAME);
        DEFAULT_STRING.put(PREF_RINGTONE, DEFAULT_RINGTONE);
        DEFAULT_STRING.put(PREF_TOTAL_CALL_COUNT, DEFAULT_TOTAL_CALL_COUNT);
        DEFAULT_STRING.put(PREF_TOTAL_MSG_COUNT, DEFAULT_TOTAL_MSG_COUNT);
        DEFAULT_STRING.put(PREF_VERSION_COMPATIBILITY,
                DEFAULT_PREFS_VERSION_COMPATIBILITY_NUMBER);
        DEFAULT_STRING.put(PREF_THIRD_PARTY_NOTIFIER_STRING,
                OsUtils.getDefaultThirdPartyNotifierString("$POPUP"));
        DEFAULT_STRING.put(PREF_THIRD_PARTY_NAME_READING_STRING,
                OsUtils.getDefaultThirdPartyNameReadingString());
        DEFAULT_STRING.put(PREF_THIRD_PARTY_NAME_READING_SELECTION,
                DEFAULT_NAME_READING_SELECTION());
        DEFAULT_STRING.put(PREF_MIXER_NAME, DEFAULT_MIXER_NAME);
        DEFAULT_STRING.put(PREF_MESSAGE_POPUP_DISPLAY_SELECTION,
                DEFAULT_MESSAGE_POPUP_DISPLAY_SELECTION);
        DEFAULT_STRING.put(PREF_THIRD_PARTY_MESSAGE_NOTIFIER_STRING,
                OsUtils.getDefaultThirdPartyNotifierString("$MESSAGE"));
        DEFAULT_STRING.put(PREF_MESSAGE_FORMAT_OPTIONS_SELECTION,
                DEFAULT_MESSAGE_FORMAT_OPTIONS_SELECTION);
    }

    /** Check box preference strings */
    public static String PREF_TYPE_SELECTED = "typeSelected";
    public static String PREF_NAME_SELECTED = "nameSelected";
    public static String PREF_NUMBER_SELECTED = "numberSelected";
    public static String PREF_DATE_SELECTED = "dateSelected";
    public static String PREF_TIME_SELECTED = "timeSelected";
    public static String PREF_LINE_SELECTED = "lineSelected";
    public static String PREF_MESSAGE_TYPE_SELECTED = "messageTypeSelected";
    public static String PREF_MESSAGE_DATE_SELECTED = "messageDateSelected";
    public static String PREF_MESSAGE_TIME_SELECTED = "messageTimeSelected";
    public static String PREF_MESSAGE_DEVICE_SELECTED = "messageDeviceSelected";
    public static String PREF_MESSAGE_FROM_TO_SELECTED = "messageFromToSelected";
    public static String PREF_MESSAGE_TEXT_SELECTED = "messageTextSelected";
    public static String PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY = "enableSmartPhoneTextNotify";
    public static String PREF_ENABLE_THIRD_PARTY_NOTIFY = "enableThirdPartyNotify";
    public static String PREF_ENABLE_TEXT_MESSAGE_URL = "enableTextMessageUrl";
    public static String PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM = "enableTextMessageExternalProgram";
    public static String PREF_ENABLE_TEXT_MESSAGE_EXTERNAL_PROGRAM_OUTPUT = "enableTextMessageExternalProgramOutput";
    public static String PREF_ENABLE_TEXT_MESSAGE_GATEWAY_RELAY = "enableTextMessageGatewayRelay";
    public static String PREF_READ_MESSAGES = "readMessages";
    public static String PREF_READ_INCOMING_CALLER_NAMES = "readNames";
    public static String PREF_READ_OUTGOING_AND_STATUS_CALLER_NAMES = "readOutgoingAndStatusNames";
    public static String PREF_USE_RAW_DATE_TIME = "useRawDateTime";
    public static String PREF_USE_EUROPEAN_DATE_TIME = "useEuropeanDateTime";
    public static String PREF_USE_SYSTEM_ADDRESS_BOOK = "useSystemAddressBook";
    public static String PREF_DISPLAY_EXTRA_MESSAGE_LOG_FIELDS = "displayExtraMessageLogFields";
    public static String PREF_DISPLAY_INCOMING_CALL_POPUPS = "displayCallPopups";
    public static String PREF_DISPLAY_OUTGOING_AND_STATUS_CALL_POPUPS = "displayOutgoingAndStatusCallPopups";
    public static String PREF_DISPLAY_INCOMING_MESSAGE_POPUPS = "displayMessagePopups";
    public static String PREF_ENABLE_LOW_BATTERY_MESSAGE = "enableLowBatteryMessage";
    public static String PREF_ENABLE_BATTERY_CHARGED_MESSAGE = "enableBatteryChargedMessage";
    public static String PREF_REQUEST_CALL_LOG_FROM_SERVER = "requestCallLogFromServer";

    /** Mapping from string preferences to default boolean values */
    private static HashSet<String> DEFAULT_BOOLEAN = new HashSet<String>();
    static
    {
        DEFAULT_BOOLEAN.add(PREF_TYPE_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_NAME_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_NUMBER_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_MESSAGE_TYPE_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_MESSAGE_DEVICE_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_MESSAGE_FROM_TO_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_MESSAGE_TEXT_SELECTED);
        DEFAULT_BOOLEAN.add(PREF_READ_INCOMING_CALLER_NAMES);
        DEFAULT_BOOLEAN.add(PREF_ENABLE_SMART_PHONE_TEXT_NOTIFY);
        if (OsUtils.getDefaultThirdPartyNotifierString("$POPUP") != "")
        {
            DEFAULT_BOOLEAN.add(PREF_ENABLE_THIRD_PARTY_NOTIFY);
        }
        DEFAULT_BOOLEAN.add(PREF_DISPLAY_INCOMING_CALL_POPUPS);
        DEFAULT_BOOLEAN.add(PREF_DISPLAY_INCOMING_MESSAGE_POPUPS);
    }

    /** Integer preference strings */
    public static String PREF_CALL_TYPE_COL_WIDTH = "callTypeColWidth";
    public static String PREF_CALL_DATE_TIME_COL_WIDTH = "callDateTimeColWidth";
    public static String PREF_CALL_LINE_COL_WIDTH = "callLineColWidth";
    public static String PREF_CALL_NUMBER_COL_WIDTH = "callNumberColWidth";
    public static String PREF_MSG_TYPE_COL_WIDTH = "msgTypeColWidth";
    public static String PREF_MSG_DATE_TIME_COL_WIDTH = "msgDateTimeColWidth";
    public static String PREF_MSG_DEVICE_COL_WIDTH = "msgDeviceColWidth";
    public static String PREF_MSG_FROM_TO_COL_WIDTH = "msgFromToColWidth";
    public static String PREF_LOW_BATTERY_MESSAGE_THRESHOLD = "lowBatteryMessageThreshold";
    public static String PREF_CALL_TABLE_SORT_COLUMN = "callTableSortColumn";
    public static String PREF_CALL_TABLE_SORT_DIRECTION = "callTableSortDirection";
    public static String PREF_MESSAGE_TABLE_SORT_COLUMN = "messageTableSortColumn";
    public static String PREF_MESSAGE_TABLE_SORT_DIRECTION = "messageTableSortDirection";

    /** Default integer preference strings */
    public static int DEFAULT_CALL_TYPE_COL_WIDTH = 55;
    public static int DEFAULT_CALL_DATE_TIME_COL_WIDTH = 155;
    public static int DEFAULT_CALL_LINE_COL_WIDTH = 50;
    public static int DEFAULT_CALL_NUMBER_COL_WIDTH = 110;
    public static int DEFAULT_MSG_TYPE_COL_WIDTH = 65;
    public static int DEFAULT_MSG_DATE_TIME_COL_WIDTH = 155;
    public static int DEFAULT_MSG_DEVICE_COL_WIDTH = 50;
    public static int DEFAULT_MSG_FROM_TO_COL_WIDTH = 110;
    private static int DEFAULT_LOW_BATTERY_MESSAGE_THRESHOLD = 15;
    private static int DEFAULT_CALL_TABLE_SORT_COLUMN = -1;
    private static int DEFAULT_CALL_TABLE_SORT_DIRECTION = TableSorter.NOT_SORTED;
    private static int DEFAULT_MESSAGE_TABLE_SORT_COLUMN = -1;
    private static int DEFAULT_MESSAGE_TABLE_SORT_DIRECTION = TableSorter.NOT_SORTED;

    /** Mapping from string preferences to default integer values */
    private static HashMap<String, Integer> DEFAULT_INTEGER = new HashMap<String, Integer>();
    static
    {
        DEFAULT_INTEGER.put(PREF_CALL_TYPE_COL_WIDTH,
                DEFAULT_CALL_TYPE_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_CALL_DATE_TIME_COL_WIDTH,
                DEFAULT_CALL_DATE_TIME_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_CALL_LINE_COL_WIDTH,
                DEFAULT_CALL_LINE_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_CALL_NUMBER_COL_WIDTH,
                DEFAULT_CALL_NUMBER_COL_WIDTH);
        DEFAULT_INTEGER
                .put(PREF_MSG_TYPE_COL_WIDTH, DEFAULT_MSG_TYPE_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_MSG_DATE_TIME_COL_WIDTH,
                DEFAULT_MSG_DATE_TIME_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_MSG_DEVICE_COL_WIDTH,
                DEFAULT_MSG_DEVICE_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_MSG_FROM_TO_COL_WIDTH,
                DEFAULT_MSG_FROM_TO_COL_WIDTH);
        DEFAULT_INTEGER.put(PREF_LOW_BATTERY_MESSAGE_THRESHOLD,
                DEFAULT_LOW_BATTERY_MESSAGE_THRESHOLD);
        DEFAULT_INTEGER.put(PREF_CALL_TABLE_SORT_COLUMN,
                DEFAULT_CALL_TABLE_SORT_COLUMN);
        DEFAULT_INTEGER.put(PREF_CALL_TABLE_SORT_DIRECTION,
                DEFAULT_CALL_TABLE_SORT_DIRECTION);
        DEFAULT_INTEGER.put(PREF_MESSAGE_TABLE_SORT_COLUMN,
                DEFAULT_MESSAGE_TABLE_SORT_COLUMN);
        DEFAULT_INTEGER.put(PREF_MESSAGE_TABLE_SORT_DIRECTION,
                DEFAULT_MESSAGE_TABLE_SORT_DIRECTION);
    }

    /**
     * Constructor
     */
    public PrefUtils(Preferences prefs)
    {
        this.prefs = prefs;
    }

    /**
     * Given a string preference, returns the default string associated with it
     * 
     * @param String the string preference
     * @return String the default String for that preference
     */
    public static String getDefaultString(String preference)
    {
        return DEFAULT_STRING.get(preference);
    }

    /**
     * Given a string preference, returns the string associated with it
     * 
     * @param String the string preference
     * @return String the String for that preference
     */
    public String getString(String preference)
    {
        if (prefs != null)
        {
            return prefs.get(preference, DEFAULT_STRING.get(preference));
        }
        else
        {
            String str = (String) backingStore.get(preference);
            if (str == null)
            {
                return DEFAULT_STRING.get(preference);
            }
            return str;
        }
    }

    /**
     * Sets a String preference
     * 
     * @param String the string preference
     * @param String for that preference
     */
    public void setString(String preference, String value)
    {
        if (!getString(preference).equals(value))
        {
            if (prefs != null)
            {
                prefs.put(preference, value);
            }
            backingStore.put(preference, value);
        }
    }

    /**
     * Given a string preference, returns the default boolean preference
     * associated with it
     * 
     * @param String the string preference
     * @return boolean the default boolean for that preference
     */
    public static boolean getDefaultBoolean(String preference)
    {
        return DEFAULT_BOOLEAN.contains(preference);
    }

    /**
     * Given a string preference, returns the boolean preference associated with
     * it
     * 
     * @param String the string preference
     * @return boolean the boolean for that preference
     */
    public boolean getBoolean(String preference)
    {
        if (prefs != null)
        {
            return prefs.getBoolean(preference,
                    DEFAULT_BOOLEAN.contains(preference));
        }
        else
        {
            Boolean result = (Boolean) backingStore.get(preference);
            if (result == null)
            {
                return DEFAULT_BOOLEAN.contains(preference);
            }
            return result;
        }
    }

    /**
     * Sets a boolean preference
     * 
     * @param String the string preference
     * @param boolean for that preference
     */
    public void setBoolean(String preference, boolean value)
    {
        if (getBoolean(preference) != value)
        {
            if (prefs != null)
            {
                prefs.putBoolean(preference, value);
            }
            backingStore.put(preference, value);
        }
    }

    /**
     * Given a string preference, returns the default integer associated with it
     * 
     * @param String the string preference
     * @return String the default integer for that preference
     */
    public static int getDefaultInteger(String preference)
    {
        return DEFAULT_INTEGER.get(preference);
    }

    /**
     * Given a string preference, returns the integer associated with it
     * 
     * @param String the string preference
     * @return String the integer for that preference
     */
    public int getInteger(String preference)
    {
        if (prefs != null)
        {
            return prefs.getInt(preference, DEFAULT_INTEGER.get(preference));
        }
        else
        {
            Integer integer = (Integer) backingStore.get(preference);
            if (integer == null)
            {
                return DEFAULT_INTEGER.get(preference);
            }
            return integer;
        }
    }

    /**
     * Sets an integer preference
     * 
     * @param String the string preference
     * @param int value for that preference
     */
    public void setInteger(String preference, int value)
    {
        if (getInteger(preference) != value)
        {
            if (prefs != null)
            {
                prefs.putInt(preference, value);
            }
            backingStore.put(preference, value);
        }
    }

    /**
     * Returns the ringtone path for the current ringtone preference. Needed to
     * support preference portability for ringtones.
     * 
     * @return ringtone path
     */
    public String getRingtonePathForPreference()
    {
        return getRingtonePathFor(getString(PREF_RINGTONE));
    }

    /**
     * Returns the ringtone path for the specified ringtone. Needed to support
     * preference portability for ringtones.
     * 
     * @param String ringtone to find
     * @return ringtone path
     */
    public static String getRingtonePathFor(String ringtone)
    {
        if (relativeWavFileToLocationMap.get(ringtone) == null)
        {
            return ringtone;
        }
        else
        {
            return relativeWavFileToLocationMap.get(ringtone);
        }
    }
}