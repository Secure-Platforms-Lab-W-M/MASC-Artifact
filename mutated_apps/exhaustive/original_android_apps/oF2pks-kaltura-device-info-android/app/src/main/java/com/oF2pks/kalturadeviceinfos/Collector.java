package com.oF2pks.kalturadeviceinfos;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.drm.DrmInfo;
import android.drm.DrmInfoRequest;
import android.drm.DrmManagerClient;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.display.DisplayManager;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaDrm;
import android.media.UnsupportedSchemeException;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Range;
import android.view.Display;
import android.view.WindowManager;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.safetynet.SafetyNet;
//import com.google.android.gms.safetynet.SafetyNetApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Provider;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.UUID;

import static com.oF2pks.kalturadeviceinfos.Utils.getProp;
import static com.oF2pks.kalturadeviceinfos.Utils.getZinfo;


/**
 * Created by noamt on 17/05/2016.
 */
class Collector {
    private static final String TAG = "Collector";
    static final UUID WIDEVINE_UUID = new UUID(0xEDEF8BA979D64ACEL, 0xA3C827DCD51D21EDL);
    private final Context mContext;
    private final boolean includeSafetyNet;
    private final JSONObject mRoot = new JSONObject();
    private final int numCameras = Camera.getNumberOfCameras();


    private static String sReport;

    static String getReport(Context ctx, boolean includeSafetyNet) {
        sReport = null;
        if (sReport == null) {
            Collector collector = new Collector(ctx, includeSafetyNet);
            JSONObject jsonReport = collector.collect();

            try {
                sReport = jsonReport.toString(4);
                sReport = sReport.replace("\\/", "/");
                
            } catch (JSONException e) {
                sReport = "{}";
            }
        }

        return sReport;
    }
    
    Collector(Context context, boolean includeSafetyNet) {
        mContext = context;
        this.includeSafetyNet = includeSafetyNet;
    }
    
    JSONObject collect() {
        final JSONObject[] safetyNetResult = new JSONObject[1];
//        Thread safetyNetThread = new Thread() {
//            @Override
//            public void run() {
//                try {
//                    safetyNetResult[0]=collectSafetyNet();
//                } catch (JSONException e) {
//                    Log.e(TAG, "Failed converting safetyNet response to JSON");
//                }
//            }
//        };
//
//        if (includeSafetyNet) {
//            safetyNetThread.start();
//        }
        try {
            JSONObject root = mRoot;
            root.put("#META=", meta());
            root.put("#ARCH=", systemArch());
            root.put("#DRM=", drmInfo());
            root.put("#ANDROID",androidInfo());
            root.put("#SYSTEM=", systemInfo());
            root.put("#DISPLAY=", displayInfo());
            root.put("#MEDIA=", mediaCodecInfo());
            root.put("#SECURITY=", securityProviders());
            root.put("#CAMERA=",cameraOldAPI());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) root.put("#CAMERA2API=",camera2API());
            root.put("#LIBS&FEATURES=", systemFL());
            root.put("#SERVICES&SVC=", dumpsysL());
            root.put("#ROOT?=", rootInfo());
            if (getProp("ro.treble.enabled").equals("true")) root.put("#TREBLE=", trebleInfo());
            root.put("#MOUNTS", selfMounts());

//            if (includeSafetyNet) {
//                safetyNetThread.join(20 * 1000);
//                root.put("safetyNet", safetyNetResult[0]);
//            }
            
        } catch (JSONException e) {
            Log.e(TAG, "Error");
//        } catch (InterruptedException e) {
//            Log.d(TAG, "Interrupted");
        }
        return mRoot;
    }

    private JSONObject trebleInfo() throws JSONException {
        String s = getProp("ro.vndk.version");
        JSONObject marshmalow = new JSONObject();
        marshmalow.put("TrebleGetprop", getProp("ro.treble.enabled"));
        marshmalow.put("ro.vendor.vndk.version", getProp("ro.vendor.vndk.version"));
        marshmalow.put("ro.product.first_api_level", getProp("ro.product.first_api_level"));
        marshmalow.put("ro.vndk.lite", getProp("ro.vndk.lite"));
        marshmalow.put("ro.vndk.version", s);
        if (!(s.equals(""))) s="-"+s;
        marshmalow.put("system/lib/vndk" + s, getZinfo("ls /system/lib/vndk" + s, "/",false));
        marshmalow.put("system/lib/vndk-sp" + s, getZinfo("ls /system/lib/vndk-sp" + s, "/",false));
        marshmalow.put("vendor/lib/vndk" + s, getZinfo("ls /vendor/lib/vndk" + s, "/",false));
        marshmalow.put("vendor/lib/vndk-sp" + s, getZinfo("ls /vendor/lib/vndk-sp" + s, "/",false));
        marshmalow.put("system/lib64/vndk" + s, getZinfo("ls /system/lib64/vndk" + s, "/",false));
        marshmalow.put("system/lib64/vndk-sp" + s, getZinfo("ls /system/lib64/vndk-sp" + s, "/",false));
        marshmalow.put("vendor/lib64/vndk" + s, getZinfo("ls /vendor/lib64/vndk" + s, "/",false));
        marshmalow.put("vendor/lib64/vndk-sp" + s, getZinfo("ls /vendor/lib64/vndk-sp" + s, "/",false));

        return marshmalow;
    }

    private JSONObject cameraOldAPI() throws JSONException {
        JSONObject archT = new JSONObject();
        String sCams = "";
        Camera camera = null;
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            try {
                camera = Camera.open(i);
                Camera.Parameters parms = camera.getParameters();
                sCams = parms.flatten();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    archT.put(Integer.toString(i) + "/" + numCameras + (info.facing == 0 ? "Back" : "Front"),
                            Utils.semicolonSortedJson(sCams, "=", ";"));
                else archT.put(Integer.toString(i) + "/" + numCameras + (info.facing == 0 ? "Back" : "Front"),
                        Utils.semicolonJson(sCams, "=", ";"));
                camera.release();
            } catch (Exception e) {
                archT.put("MISSING permission_CAMERA","!Marshmallow and up!");
            }
        }
        return archT;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JSONObject camera2API() throws JSONException {
    JSONObject archT = new JSONObject();
    String sCams = "";

        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        if (manager==null) return archT.put("WARNING","CameraAPi2 removed");
        try {
            for (String cameraId : manager.getCameraIdList()) {
                sCams += "%"+cameraId;
                CameraCharacteristics chars = manager.getCameraCharacteristics(cameraId);
                String s=cameraId+"=";
                switch (chars.get(CameraCharacteristics.LENS_FACING)){
                    case 0:
                        s+="front"; break;
                    case 1:
                        s+="back"; break;
                    case 2:
                        s+="external";
                }
                s+="<";
                switch (chars.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)){
                    case 0:
                        s+="LIMITED"; break;
                    case 1:
                        s+="FULL"; break;
                    case 2:
                        s+="LEGACY"; break;
                    case 3:
                        s+="LEVEL3"; break;
                    case 4:
                        s+="EXTERNAL";
                }
                s+=">"+chars.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE)+"("+chars.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE)+")";
                archT.put(s,tech2API(chars));
                // Do something with the characteristics
            }
            } catch(CameraAccessException e){
                e.printStackTrace();
            } catch(NullPointerException e){
                // Currently an NPE is thrown when the Camera2API is used but not supported on the
                // device this code runs.
            }
    return archT;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private JSONObject tech2API(CameraCharacteristics chars) throws JSONException {
        JSONObject arch = new JSONObject();
        CameraCharacteristics.Key c;
        String tmp ="";
        String oo="";
        int[] ii=chars.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES);
        String[] z={"OFF","ON"};
        String[] zz={"BACKWARD_COMPATIBLE"//21"
                ,"MANUAL_SENSOR"//21"
                ,"MANUAL_POST_PROCESSING"//21"
                ,"RAW"//21"
                ,"PRIVATE_REPROCESSING"//23"
                ,"READ_SENSOR_SETTINGS"//22"
                ,"BURST_CAPTURE"//22"
                ,"YUV_REPROCESSING"//23"
                ,"DEPTH_OUTPUT"//23"
                ,"CONSTRAINED_HIGH_SPEED_VIDEO"//23"
                ,"MOTION_TRACKING"//28"
                ,"LOGICAL_MULTI_CAMERA"//28"
                ,"MONOCHROME"};//28"};
        for (int i:ii) if (i<zz.length) tmp+="+ "+zz[i];
        arch.put("MAIN_specs",tmp);tmp="";

        Range[] jj = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
        for (Range r:jj) tmp+=r.toString();
        arch.put("FPS_ranges",tmp);tmp="";

        float[] jjj = chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
        if (jjj!=null) {
            for (float f:jjj) tmp+=f;
            arch.put("Apertures",tmp);tmp="";
        }

        float[] jjjj = chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
        for (float f:jjjj) tmp+=f;
        arch.put("Focal_lenghts",tmp);tmp="";

        int[] iii= chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
        String[] zzz={"OFF","ON","ON_AUTO_FLASH","ON_ALWAYS_FLASH","ON_AUTO_FLASH_REDEYE","ON_EXTERNAL_FLASH"};
        for (int i:iii) if (i<zzz.length) tmp+="+ "+zzz[i];
        arch.put("AE_modes",tmp);tmp="";

        int[] iiii= chars.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
        String[] zzzz={"OFF","AUTO","MACRO","CONTINUOUS_VIDEO","CONTINUOUS_PICTURE","EDOF"};
        for (int i:iiii) if (i<zzzz.length) tmp+="+ "+zzzz[i];
        arch.put("AF_modes",tmp);tmp="";

        int[] iiiii= chars.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
        String[] zzzzz={"OFF","AUTO","INCANDESCENT","FLUORESCENT","WARM_FLUORESCENT","DAYLIGHT","CLOUDY_DAYLIGHT","TWILIGHT","SHADE"};
        for (int i:iiiii) if (i<zzzzz.length) tmp+="+ "+zzzzz[i];
        arch.put("AWB_modes",tmp);tmp="";

        int[] iiiiii = chars.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION);
        if (iiiiii!=null) {
            for (int i:iiiiii) if (i<z.length) tmp+="+ "+z[i];
            arch.put("OIS_modes",tmp);tmp="";
        }
        List<CameraCharacteristics.Key<?>> tt = chars.getKeys();
        ListIterator<CameraCharacteristics.Key<?>> it = tt.listIterator();
        while(it.hasNext()){
                c=it.next();
                tmp = chars.get(c).toString();
                if (!tmp.startsWith("[") || tmp.endsWith("]")) arch.put(c.getName(),(tmp));
                else {
                    oo+="/"+c.getName();
                    //arch.put(c.getName(),c.getClass().getSimpleName());
                }
        }
        oo=oo.replaceAll("\\."," ");
        if (oo.length()>0)arch.put("UNSOLVEDs",oo);
        return arch;

    }

    private JSONObject selfMounts() throws JSONException {
        JSONObject archT = new JSONObject();
        JSONObject arch = new JSONObject();
        String[] s3 =new String[3];
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/self/mounts");
            InputStream is = null;
            //if (p.waitFor() == 0) {
                is = p.getInputStream();
            /*} else {
                is = p.getErrorStream();
            }*/
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            SortedSet set = new TreeSet();

            while ((tmp = br.readLine()) != null)
            {
                set.add(tmp);
            }
            is.close();
            br.close();

            Iterator it = set.iterator();
            s3=it.next().toString().split(" ",3);
            arch.put(s3[1],s3[2]);
            tmp=s3[0];
            while (it.hasNext()) {
                while (it.hasNext()) {
                    s3=it.next().toString().split(" ",3);
                    if (!tmp.equals(s3[0])){
                        archT.put("\u25A0"+tmp,arch);
                        arch = new JSONObject();
                        arch.put(s3[1],s3[2]);
                        tmp=s3[0];
                        break;
                    }
                    arch.put(s3[1],s3[2]);
                }
            }

        } catch (Exception ex) {
            arch.put("error", ex.getMessage() + '\n' + Log.getStackTraceString(ex));
        }
        return archT;
    }

    private JSONObject dumpsysL() throws JSONException {
        JSONObject archT = new JSONObject();
        String s = getZinfo("dumpsys -l","/",true);
        archT.put("dumpsys-l",s.replaceAll("\\."," "));
        s= getZpack("getprop",".svc.",false);
        s=s.replaceAll("init\\u002Esvc\\u002E","");
        s=s.replaceAll("\\]: \\[running\\]","");
        s=s.replaceAll("\\]: \\[stopped\\]","\u25A0");
        s=s.replaceAll("\\[","");
        return archT.put("InitSVC",s.replaceAll("\\."," "));
    }

    private JSONObject systemFL() throws JSONException {
            JSONObject archT = new JSONObject();
            SortedSet set = new TreeSet();
            String s = "" ;
            PackageManager packageManager = mContext.getPackageManager();
            FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] Libs = packageManager.getSystemSharedLibraryNames();
                Arrays.sort(Libs);
                for (String ss : Libs) s+= " / "+ ss.replaceAll("\\."," ");
                archT.put("SystemSharedLibraryNames", s);
                s="";
            }
            for (FeatureInfo featureInfo : featureInfos) {
                if (featureInfo != null && featureInfo.name != null ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (featureInfo.version !=0){
                            set.add(featureInfo.name+"\u25A0"+(String.valueOf(featureInfo.version)));
                        } else set.add(featureInfo.name+" °");
                    }
                    else set.add(featureInfo.name);
                }
            }
            Iterator it = set.iterator();
            while (it.hasNext()) {
                s+="/ "+it.next().toString();
            }
            return archT.put("Features",s.replaceAll("\\."," "));
        }


        private JSONObject systemArch() throws JSONException {
            JSONObject archT = new JSONObject();
            JSONObject arch = new JSONObject();
            archT.put("uname-m",getZinfo("uname -m","",false));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                arch.put("SUPPORTED_ABIS", new JSONArray(Build.SUPPORTED_ABIS));
                arch.put("SUPPORTED_32_BIT_ABIS", new JSONArray(Build.SUPPORTED_32_BIT_ABIS));
                arch.put("SUPPORTED_64_BIT_ABIS", new JSONArray(Build.SUPPORTED_64_BIT_ABIS));
            } else {
                arch.put("CPU_ABI", Build.CPU_ABI);
                arch.put("CPU_ABI2", Build.CPU_ABI2);
            }
            return archT.put(System.getProperty("os.arch"),arch);
        }

    private JSONObject meta() throws JSONException {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return new JSONObject()
                .put("versionName", BuildConfig.VERSION_NAME)
                .put("versionCode", BuildConfig.VERSION_CODE)
                .put("timestamp", nowAsISO);
    }

    private JSONObject displayInfo() throws JSONException {
        JSONObject marshmalow = new JSONObject();
        String s = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().toString();
        int i,ii=0 ;
        marshmalow.put("ro.sf.lcd_density", getProp("ro.sf.lcd_density"));
        marshmalow.put("RefreshRate",(((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRefreshRate()));
        i= s.indexOf(", real ");
        if (i>=0) {
            ii= s.indexOf(", DisplayMetrics{");
            if (ii>=0) s=s.substring(0,ii);
            s=s.substring(i+7);
            marshmalow.put("ScreenSpecs",s.substring(0,s.indexOf(",")));
            //marshmalow.put("AllMetrics",s);
            marshmalow.put("ViewingMetrics", mContext.getResources().getDisplayMetrics().toString());
        } else {
            //marshmalow.put("AllMetrics",s);//android.view.Display@...
            return marshmalow.put("Metrics", mContext.getResources().getDisplayMetrics().toString());
        }
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)marshmalow.put("isHDR",((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().isHdr());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i= s.indexOf(", hdrCapa");
            ii= s.indexOf("ilities@");
            if (i>=0 && ii>=0) s=s.substring(0,i)+s.substring(ii+16);
            marshmalow.put("AllMetrics",s);

            s="";
            String[] zz = {"DOLBY_VISION","HDR10","HLG"};
            Display.HdrCapabilities dHDR =((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHdrCapabilities();
            for(int j:dHDR.getSupportedHdrTypes()) if(j<zz.length) s+="+ "+zz[j-1];
            marshmalow.put("SupportedHdrTypes",s);
            s="";
            Display.Mode[] dModes = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSupportedModes();
            for (Display.Mode mode:dModes)s+=mode.toString();//+","+mode.getRefreshRate()+">"+mode.getPhysicalHeight()+"x"+mode.getPhysicalWidth();
            marshmalow.put("getSupportedModes",s);

        } else marshmalow.put("AllMetrics",s);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            s="";
            float[] dRefsreshRates = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSupportedRefreshRates();
            for (float f:dRefsreshRates)s+=f;
            marshmalow.put("RefreshRates21",s);
        }

            return marshmalow;
    }

    private JSONObject drmInfo() throws JSONException {
        return new JSONObject()
                .put("modular", modularDrmInfo())
                .put("classic", classicDrmInfo());
                
    }

    private JSONObject classicDrmInfo() throws JSONException {
        JSONObject json = new JSONObject();
        
        DrmManagerClient drmManagerClient = new DrmManagerClient(mContext);
        String[] availableDrmEngines = drmManagerClient.getAvailableDrmEngines();

        JSONArray engines = jsonArray(availableDrmEngines);
        json.put("engines", engines);
        
        try {
            if (drmManagerClient.canHandle("", "video/wvm")) {
                DrmInfoRequest request = new DrmInfoRequest(DrmInfoRequest.TYPE_REGISTRATION_INFO, "video/wvm");
                request.put("WVPortalKey", "OEM");
                DrmInfo response = drmManagerClient.acquireDrmInfo(request);
                String status = (String) response.get("WVDrmInfoRequestStatusKey");
                
                status = new String[]{"HD_SD", null, "SD"}[Integer.parseInt(status)];
                json.put("widevine", 
                        new JSONObject()
                                .put("version", response.get("WVDrmInfoRequestVersionKey"))
                                .put("status", status)
                );
            }
        } catch (Exception e) {
            json.put("error", e.getMessage() + '\n' + Log.getStackTraceString(e));
        }

        drmManagerClient.release();
        
        return json;
    }

    @NonNull
    private JSONArray jsonArray(String[] stringArray) {
        JSONArray jsonArray = new JSONArray();
        for (String string : stringArray) {
            if (!TextUtils.isEmpty(string)) {
                jsonArray.put(string);
            }
        }
        return jsonArray;
    }

    private String mediaCodecInfo(MediaCodecInfo mediaCodec) {
        String s = "";
        String[] types = mediaCodec.getSupportedTypes();
        for (int j = 0; j < types.length; j++) {
            s +="~"+types[j];
        }
        return s;
    }
    
    private JSONObject mediaCodecInfo() throws JSONException {

        ArrayList<MediaCodecInfo> mediaCodecs = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.ALL_CODECS);
            MediaCodecInfo[] codecInfos = mediaCodecList.getCodecInfos();
            
            Collections.addAll(mediaCodecs, codecInfos);
        } else {
            for (int i=0, n=MediaCodecList.getCodecCount(); i<n; i++) {
                mediaCodecs.add(MediaCodecList.getCodecInfoAt(i));
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) Collections.sort(mediaCodecs,McComparator);
        JSONObject info = new JSONObject();
        JSONObject jsonDecoders = new JSONObject();
        JSONObject jsonEncoders = new JSONObject();
        for (MediaCodecInfo mediaCodec : mediaCodecs) {
            if (mediaCodec.isEncoder()) {
                jsonEncoders.put(mediaCodec.getName(), mediaCodecInfo(mediaCodec));
            } else {
                jsonDecoders.put(mediaCodec.getName(), mediaCodecInfo(mediaCodec));
            }
        }
        info.put("decoders", jsonDecoders);
        info.put("encoders", jsonEncoders);

        return info;
        
    }

    private static Comparator<MediaCodecInfo> McComparator = new Comparator<MediaCodecInfo>(){
        public int compare(MediaCodecInfo m1,MediaCodecInfo m2){
            String mName1 = m1.getName().toUpperCase();
            String mName2 = m2.getName().toUpperCase();

            //ascending order
            return mName1.compareTo(mName2);
        }
    };

    private JSONObject modularDrmInfo() throws JSONException {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return new JSONObject()
                    .put("widevine", widevineModularDrmInfo());
        } else {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private JSONObject widevineModularDrmInfo() throws JSONException {
        if (!MediaDrm.isCryptoSchemeSupported(WIDEVINE_UUID)) {
            return null;
        }

        MediaDrm mediaDrm;
        try {
            mediaDrm = new MediaDrm(WIDEVINE_UUID);
        } catch (UnsupportedSchemeException e) {
            return null;
        }
        
        final JSONArray mediaDrmEvents = new JSONArray();
        
        mediaDrm.setOnEventListener(new MediaDrm.OnEventListener() {
            @Override
            public void onEvent(@NonNull MediaDrm md, byte[] sessionId, int event, int extra, byte[] data) {
                try {
                    String encodedData = data == null ? null : Base64.encodeToString(data, Base64.NO_WRAP);
                    
                    mediaDrmEvents.put(new JSONObject().put("event", event).put("extra", extra).put("data", encodedData));
                } catch (JSONException e) {
                    Log.e(TAG, "JSONError", e);
                }
            }
        });

        try {
            byte[] session;
            session = mediaDrm.openSession();
            mediaDrm.closeSession(session);
        } catch (Exception e) {
            mediaDrmEvents.put(new JSONObject().put("Exception(openSession)", e.toString()));
        }


        String[] stringProps = {MediaDrm.PROPERTY_VENDOR, MediaDrm.PROPERTY_VERSION, MediaDrm.PROPERTY_DESCRIPTION, MediaDrm.PROPERTY_ALGORITHMS, "securityLevel", "systemId", "privacyMode", "sessionSharing", "usageReportingSupport", "appId", "origin", "hdcpLevel", "maxHdcpLevel", "maxNumberOfSessions", "numberOfOpenSessions"};
        String[] byteArrayProps = {MediaDrm.PROPERTY_DEVICE_UNIQUE_ID, "provisioningUniqueId", "serviceCertificate"};
        
        JSONObject props = new JSONObject();
        
        for (String prop : stringProps) {
            String value;
            try {
                value = mediaDrm.getPropertyString(prop);
            } catch (IllegalStateException e) {
                value = "<unknown>";
            }
            props.put(prop, value);
        }
        for (String prop : byteArrayProps) {
            String value;
            try {
                value = Base64.encodeToString(mediaDrm.getPropertyByteArray(prop), Base64.NO_WRAP);
            } catch (IllegalStateException|NullPointerException e) {
                value = "<unknown>";
            }
            props.put(prop, value);
        }

        JSONObject response = new JSONObject();
        response.put("properties", props);
        response.put("events", mediaDrmEvents);

        return response;
    }

    private JSONObject androidInfo() throws JSONException {
        JSONObject marshmalow = new JSONObject();
        marshmalow.put("RELEASE", Build.VERSION.RELEASE);
        marshmalow.put("KERNEL", System.getProperty("os.version"));
        marshmalow.put("SDK_INT", Build.VERSION.SDK_INT);
        marshmalow.put("TAGS", Build.TAGS);
        marshmalow.put("systemName", System.getProperty("user.name"));
        marshmalow.put("osName", System.getProperty("os.name"));
        marshmalow.put("JAVA", System.getProperty("java.vendor"));
        marshmalow.put("JAVA_url", System.getProperty("java.vendor.url"));
        marshmalow.put("JAVA_vm_version", System.getProperty("java.vm.version"));
        marshmalow.put("JAVA_home", System.getProperty("java.home"));
        marshmalow.put("JAVA_path", System.getProperty("java.class.path"));
        marshmalow.put("JAVA_version", System.getProperty("java.version"));
        marshmalow.put("JAVA_runtime", System.getProperty("java.runtime.version"));
        marshmalow.put("ro.build.flavor", getProp("ro.build.flavor"));
        marshmalow.put("ro.crypto.type", getProp("ro.crypto.type"));
        marshmalow.put("ALLOWED_GEOLOCATION_ORIGINS", Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ALLOWED_GEOLOCATION_ORIGINS));
        marshmalow.put("LOCATION_MODE", Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.LOCATION_MODE));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            marshmalow.put("LOCATION_PROVIDERS_ALLOWED", Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            marshmalow.put("BASE_OS", Build.VERSION.BASE_OS);
            marshmalow.put("PREVIEW_SDK_INT", Build.VERSION.PREVIEW_SDK_INT);
            marshmalow.put("SECURITY_PATCH", Build.VERSION.SECURITY_PATCH);
            marshmalow.put("ro.secure", getProp("ro.secure"));
            marshmalow.put("TrebleGetprop", getProp("ro.treble.enabled"));
            marshmalow.put("ABupdateGetprop", getProp("ro.build.ab_update"));
        }
        marshmalow.put("/Vendor", getZpack("df vendor","vendor",true));
        marshmalow.put("/System", getZpack("df system","system",true));
        return marshmalow;
    }

    private JSONObject systemInfo() throws JSONException {
        JSONObject marshmalow = new JSONObject();
        marshmalow.put("Cpu", getCpu());
        marshmalow.put("BOARD", getProp("ro.board.platform"));
        marshmalow.put("HARDWARE", Build.HARDWARE);
        marshmalow.put("nbrCams", numCameras);
        marshmalow.put("KernelFull", getZinfo("uname -a","",false));
        marshmalow.put("CODENAME", Build.VERSION.CODENAME);
        marshmalow.put("BOOTLOADER", Build.BOOTLOADER);
        marshmalow.put("ro.boot.bootdevice", getProp("ro.boot.bootdevice"));
        marshmalow.put("BRAND", Build.BRAND);
        marshmalow.put("MODEL", Build.MODEL);
        marshmalow.put("MANUFACTURER", Build.MANUFACTURER);
        marshmalow.put("DEVICE", Build.DEVICE);
        marshmalow.put("ID", Build.ID);
        marshmalow.put("HOST", Build.HOST);
        marshmalow.put("PRODUCT", Build.PRODUCT);
        marshmalow.put("TYPE", Build.TYPE);
        marshmalow.put("USER", Build.USER);
        marshmalow.put("DISPLAY", Build.DISPLAY);
        marshmalow.put("INCREMENTAL", Build.VERSION.INCREMENTAL);
        marshmalow.put("RadioVersion", Build.getRadioVersion());
        marshmalow.put("FINGERPRINT", Build.FINGERPRINT);
        marshmalow.put("FINGERPRINTvendor", getProp("ro.vendor.build.fingerprint"));

        final ActivityManager activityManager =
                (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
                marshmalow.put("glEsVersion", configurationInfo.getGlEsVersion());
                marshmalow.put("reqGlEsVersion", String.valueOf(configurationInfo.reqGlEsVersion));

        //marshmalow.put("zz", mContext.getPackageManager().hasSystemFeature (PackageManager.FEATURE_OPENGLES_EXTENSION_PACK));

            return marshmalow;
    }

    private JSONObject securityProviders() throws JSONException {
        Provider[] providers = Security.getProviders();
        JSONObject providergroup = new JSONObject();
        for (int i = 0; i < providers.length; i++) {
            JSONObject provider = new JSONObject().put("Info",providers[i].getInfo());

            provider.put("Version",String.valueOf(providers[i].getVersion()));
            provider.put("Class",providers[i].getClass().getName());
            providergroup.put(providers[i].getName(), provider);

        }
        return providergroup;
    }


    private JSONObject rootInfo() throws JSONException {

        JSONObject info = new JSONObject();

        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su" , "/data/adb/magisk.img" ,"/data/magisk.img" };
        JSONArray files = new JSONArray();
        for (String path : paths) {
            if (new File(path).exists()) {
                files.put(path);
            }
        }
        info.put("existingFiles", files);

        return info;
    }

    // NOTE: this application is meant for user-initiated diagnostics. 
    // It doesn't attempt to use the best security practices or to validate the result. 
//    private JSONObject collectSafetyNet() throws JSONException {
//        GoogleApiClient client = new GoogleApiClient.Builder(mContext)
//                .addApi(SafetyNet.API)
//                .build();
//        ConnectionResult connectionResult = client.blockingConnect(20, TimeUnit.SECONDS);
//        if (!connectionResult.isSuccess()) {
//            return new JSONObject().put("connectionError", connectionResult.toString());
//        }
//        byte[] nonce = getRequestNonce();
//        SafetyNetApi.AttestationResult result = SafetyNet.SafetyNetApi.attest(client, nonce).await(20, TimeUnit.SECONDS);
//                        Status status = result.getStatus();
//        if (!status.isSuccess()) {
//            return new JSONObject().put("testingError", status.toString());
//        }
//        String jwsResult = result.getJwsResult();
//
//        // Extract the payload, ignore the rest.
//        String[] parts = jwsResult.split("\\.");
//        if (parts.length != 3) {
//            return new JSONObject().put("invalidResponse", jwsResult);
//        }
//
//        String decoded = new String(Base64.decode(parts[1], Base64.URL_SAFE));
//
//        JSONObject jsonObject = new JSONObject(decoded);
//
//        // Remove the boring keys
//        for (String key : new String[]{"nonce", "timestampMs", "apkPackageName", "apkCertificateDigestSha256", "apkDigestSha256"}) {
//            jsonObject.remove(key);
//        }
//
//        return jsonObject;
//    }

    private byte[] getRequestNonce() {
        byte[] bytes = new byte[32];
        new Random().nextBytes(bytes);
        return bytes;
    }
    /*private String ipAddress() {
        try {
            for (final Enumeration<NetworkInterface> enumerationNetworkInterface = NetworkInterface.getNetworkInterfaces(); enumerationNetworkInterface.hasMoreElements();) {
                final NetworkInterface networkInterface = enumerationNetworkInterface.nextElement();
                for (Enumeration<InetAddress> enumerationInetAddress = networkInterface.getInetAddresses(); enumerationInetAddress.hasMoreElements();) {
                    final InetAddress inetAddress = enumerationInetAddress.nextElement();
                    if (! inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
            return "";
        }
        catch (final Exception e) {
            return "";
        }
    }*/
    //
    private static String getZpack(String s, String grp, boolean bool) {
        try {
            Process p = Runtime.getRuntime().exec(s);
            InputStream is = null;
            //if (p.waitFor() == 0) {
            is = p.getInputStream();
            /*} else {
                is = p.getErrorStream();
            }*/
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            String tmp2 = "";
            if (bool) tmp = br.readLine();
            while ((tmp = br.readLine()) != null)
            {
                if (tmp.contains(grp)) tmp2 +="\n"+tmp;//.replaceAll(" ","");
            }
            is.close();
            br.close();
/*            if (grp.equals(".svc.")) {
                tmp2=tmp2.replaceAll("init\\u002Esvc\\u002E","");
                tmp2=tmp2.replaceAll("\\]: \\[running\\]","");
                tmp2=tmp2.replaceAll("\\]: \\[stopped\\]","\u25A0");
                tmp2=tmp2.replaceAll("\\[","");
            }*/
            if (tmp2.length() != 0) return tmp2.replaceAll("\\n"," /");
            return "Unknow";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

    private static String getCpu() {
        try {
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            InputStream is = null;
            //if (p.waitFor() == 0) {
            is = p.getInputStream();
            /*} else {
                is = p.getErrorStream();
            }*/
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String tmp;
            String tmp2 = null;

            while ((tmp = br.readLine()) != null)
            {
                if (tmp.contains("Hardware")) return tmp.substring(11);
                if (tmp.contains("model name")) tmp2= tmp.substring(13);
            }
            is.close();
            br.close();
            if (tmp2 != null) return tmp2;
            return "Unknow";
        } catch (Exception ex) {
            return "ERROR: " + ex.getMessage();
        }
    }

}

