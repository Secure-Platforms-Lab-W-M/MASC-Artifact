package edu.wm.cs.mplus.model;

import java.util.HashMap;
import java.util.Map;

public enum MutationType {
	ACTIVITY_NOT_DEFINED(1,"ActivityNotDefined"),
	DIFFERENT_ACTIVITY_INTENT_DEFINITION(2,"DifferentActivityIntentDefinition"),
	INVALID_ACTIVITY_PATH(3,"InvalidActivityPATH"),
	INVALID_KEY_INTENT_PUT_EXTRA(4,"InvalidKeyIntentPutExtra"),
	INVALID_LABEL(5,"InvalidLabel"),
	NULL_INTENT(6,"NullIntent"),
	NULL_VALUE_INTENT_PUT_EXTRA(7,"NullValueIntentPutExtra"),
	WRONG_MAIN_ACTIVITY(8,"WrongMainActivity"),
	MISSING_PERMISSION_MANIFEST(9,"MissingPermissionManifest"),
	WRONG_STRING_RESOURCE(10,"WrongStringResource"),
	NOT_PARCELABLE(11,"NotParcelable"),
	SDK_VERSION(12,"SDKVersion"),
	LENGTHY_BACKEND_SERVICE(13,"LengthyBackEndService"),
	LONG_CONNECTION_TIME_OUT(14,"LongConnectionTimeOut"),
	BLUETOOTH_ADAPTER_ALWAYS_AVAILABLE(15,"BluetoothAdapterAlwaysEnabled"),
	NULL_BLUETOOTH_ADAPTER(16,"NullBluetoothAdapter"),
	INVALID_URI(17,"InvalidURI"),
	NULL_GPS_LOCATION(18,"NullGPSLocation"),
	INVALID_DATE(19,"InvalidDate"),
	NULL_BACKEND_SERVICE_RETURN(20,"NullBackEndServiceReturn"),
	INVALID_METHOD_CALL_ARGUMENT(21,"InvalidMethodCallArgument"),
	NULL_METHOD_CALL_ARGUMENT(22,"NullMethodCallArgument"),
	CLOSING_NULL_CURSOR(23,"ClosingNullCursor"),
	INVALID_INDEX_QUERY_PARAMETER(24,"InvalidIndexQueryParameter"),
	INVALID_SQL_QUERY(25,"InvalidSQLQuery"),
	VIEW_COMPONENT_NOT_VISIBLE(26,"ViewComponentNotVisible"),
	FINDVIEWBYID_RETURNS_NULL(27,"FindViewByIdReturnsNull"),
	INVALID_COLOR(28,"InvalidColor"),
	INVALID_VIEW_FOCUS(29,"InvalidViewFocus"),
	BUGGY_GUI_LISTENER(30,"BuggyGUIListener"),
	INVALID_ID_FINDVIEW(31,"InvalidIDFindView"),
	INVALID_FILE_PATH(32,"InvalidFilePath"),
	NULL_INPUT_STREAM(33,"NullInputStream"),
	NOT_SERIALIZABLE(34,"NotSerializable"),
	OOM_LARGE_IMAGE(35,"OOMLargeImage"),
	LENGTHY_GUI_LISTENER(36,"LengthyGUIListener"),
	NULL_OUTPUT_STREAM(37,"NullOutputStream"),
	LENGTHY_GUI_CREATION(38,"LengthyGUICreation"),
	CIPHER_INSTANCE(601,"CipherInstance"),
	RANDOM_INT(602,"RandomInt"),
	IVPARAM_SPEC(603,"IvParameterSpec"),
	SSL_Context(604, "SSLContextInstance"),
	Message_Digest(605, "MessageDigest"),
	Hostname_Verifier(606, "HostnameVerifierInstance"),
	HttpsURLHostname_Verifier(607, "HttpsURLHostnameVerifier"),
	TrustManager(608, "TrustManagerInstace");
	
	private final int id;
	private final String name;
	
	
	private static Map<Integer, MutationType> map = new HashMap<>();
	
	
	static {
        for (MutationType type : MutationType.values()) {
            map.put(type.getId(), type);
        }
    }

	MutationType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static MutationType valueOf(int typeId) {
        return map.get(typeId);
    }
	
	
}
