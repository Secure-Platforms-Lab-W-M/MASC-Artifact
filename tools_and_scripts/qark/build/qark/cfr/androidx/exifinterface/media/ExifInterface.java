/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetManager
 *  android.content.res.AssetManager$AssetInputStream
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.location.Location
 *  android.util.Log
 *  android.util.Pair
 */
package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExifInterface {
    public static final short ALTITUDE_ABOVE_SEA_LEVEL = 0;
    public static final short ALTITUDE_BELOW_SEA_LEVEL = 1;
    static final Charset ASCII;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_1;
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2;
    public static final int[] BITS_PER_SAMPLE_RGB;
    static final short BYTE_ALIGN_II = 18761;
    static final short BYTE_ALIGN_MM = 19789;
    public static final int COLOR_SPACE_S_RGB = 1;
    public static final int COLOR_SPACE_UNCALIBRATED = 65535;
    public static final short CONTRAST_HARD = 2;
    public static final short CONTRAST_NORMAL = 0;
    public static final short CONTRAST_SOFT = 1;
    public static final int DATA_DEFLATE_ZIP = 8;
    public static final int DATA_HUFFMAN_COMPRESSED = 2;
    public static final int DATA_JPEG = 6;
    public static final int DATA_JPEG_COMPRESSED = 7;
    public static final int DATA_LOSSY_JPEG = 34892;
    public static final int DATA_PACK_BITS_COMPRESSED = 32773;
    public static final int DATA_UNCOMPRESSED = 1;
    private static final boolean DEBUG = false;
    static final byte[] EXIF_ASCII_PREFIX;
    private static final ExifTag[] EXIF_POINTER_TAGS;
    static final ExifTag[][] EXIF_TAGS;
    public static final short EXPOSURE_MODE_AUTO = 0;
    public static final short EXPOSURE_MODE_AUTO_BRACKET = 2;
    public static final short EXPOSURE_MODE_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_ACTION = 6;
    public static final short EXPOSURE_PROGRAM_APERTURE_PRIORITY = 3;
    public static final short EXPOSURE_PROGRAM_CREATIVE = 5;
    public static final short EXPOSURE_PROGRAM_LANDSCAPE_MODE = 8;
    public static final short EXPOSURE_PROGRAM_MANUAL = 1;
    public static final short EXPOSURE_PROGRAM_NORMAL = 2;
    public static final short EXPOSURE_PROGRAM_NOT_DEFINED = 0;
    public static final short EXPOSURE_PROGRAM_PORTRAIT_MODE = 7;
    public static final short EXPOSURE_PROGRAM_SHUTTER_PRIORITY = 4;
    public static final short FILE_SOURCE_DSC = 3;
    public static final short FILE_SOURCE_OTHER = 0;
    public static final short FILE_SOURCE_REFLEX_SCANNER = 2;
    public static final short FILE_SOURCE_TRANSPARENT_SCANNER = 1;
    public static final short FLAG_FLASH_FIRED = 1;
    public static final short FLAG_FLASH_MODE_AUTO = 24;
    public static final short FLAG_FLASH_MODE_COMPULSORY_FIRING = 8;
    public static final short FLAG_FLASH_MODE_COMPULSORY_SUPPRESSION = 16;
    public static final short FLAG_FLASH_NO_FLASH_FUNCTION = 32;
    public static final short FLAG_FLASH_RED_EYE_SUPPORTED = 64;
    public static final short FLAG_FLASH_RETURN_LIGHT_DETECTED = 6;
    public static final short FLAG_FLASH_RETURN_LIGHT_NOT_DETECTED = 4;
    private static final List<Integer> FLIPPED_ROTATION_ORDER;
    public static final short FORMAT_CHUNKY = 1;
    public static final short FORMAT_PLANAR = 2;
    public static final short GAIN_CONTROL_HIGH_GAIN_DOWN = 4;
    public static final short GAIN_CONTROL_HIGH_GAIN_UP = 2;
    public static final short GAIN_CONTROL_LOW_GAIN_DOWN = 3;
    public static final short GAIN_CONTROL_LOW_GAIN_UP = 1;
    public static final short GAIN_CONTROL_NONE = 0;
    public static final String GPS_DIRECTION_MAGNETIC = "M";
    public static final String GPS_DIRECTION_TRUE = "T";
    public static final String GPS_DISTANCE_KILOMETERS = "K";
    public static final String GPS_DISTANCE_MILES = "M";
    public static final String GPS_DISTANCE_NAUTICAL_MILES = "N";
    public static final String GPS_MEASUREMENT_2D = "2";
    public static final String GPS_MEASUREMENT_3D = "3";
    public static final short GPS_MEASUREMENT_DIFFERENTIAL_CORRECTED = 1;
    public static final String GPS_MEASUREMENT_INTERRUPTED = "V";
    public static final String GPS_MEASUREMENT_IN_PROGRESS = "A";
    public static final short GPS_MEASUREMENT_NO_DIFFERENTIAL = 0;
    public static final String GPS_SPEED_KILOMETERS_PER_HOUR = "K";
    public static final String GPS_SPEED_KNOTS = "N";
    public static final String GPS_SPEED_MILES_PER_HOUR = "M";
    static final byte[] IDENTIFIER_EXIF_APP1;
    private static final ExifTag[] IFD_EXIF_TAGS;
    private static final int IFD_FORMAT_BYTE = 1;
    static final int[] IFD_FORMAT_BYTES_PER_FORMAT;
    private static final int IFD_FORMAT_DOUBLE = 12;
    private static final int IFD_FORMAT_IFD = 13;
    static final String[] IFD_FORMAT_NAMES;
    private static final int IFD_FORMAT_SBYTE = 6;
    private static final int IFD_FORMAT_SINGLE = 11;
    private static final int IFD_FORMAT_SLONG = 9;
    private static final int IFD_FORMAT_SRATIONAL = 10;
    private static final int IFD_FORMAT_SSHORT = 8;
    private static final int IFD_FORMAT_STRING = 2;
    private static final int IFD_FORMAT_ULONG = 4;
    private static final int IFD_FORMAT_UNDEFINED = 7;
    private static final int IFD_FORMAT_URATIONAL = 5;
    private static final int IFD_FORMAT_USHORT = 3;
    private static final ExifTag[] IFD_GPS_TAGS;
    private static final ExifTag[] IFD_INTEROPERABILITY_TAGS;
    private static final int IFD_OFFSET = 8;
    private static final ExifTag[] IFD_THUMBNAIL_TAGS;
    private static final ExifTag[] IFD_TIFF_TAGS;
    private static final int IFD_TYPE_EXIF = 1;
    private static final int IFD_TYPE_GPS = 2;
    private static final int IFD_TYPE_INTEROPERABILITY = 3;
    private static final int IFD_TYPE_ORF_CAMERA_SETTINGS = 7;
    private static final int IFD_TYPE_ORF_IMAGE_PROCESSING = 8;
    private static final int IFD_TYPE_ORF_MAKER_NOTE = 6;
    private static final int IFD_TYPE_PEF = 9;
    static final int IFD_TYPE_PREVIEW = 5;
    static final int IFD_TYPE_PRIMARY = 0;
    static final int IFD_TYPE_THUMBNAIL = 4;
    private static final int IMAGE_TYPE_ARW = 1;
    private static final int IMAGE_TYPE_CR2 = 2;
    private static final int IMAGE_TYPE_DNG = 3;
    private static final int IMAGE_TYPE_JPEG = 4;
    private static final int IMAGE_TYPE_NEF = 5;
    private static final int IMAGE_TYPE_NRW = 6;
    private static final int IMAGE_TYPE_ORF = 7;
    private static final int IMAGE_TYPE_PEF = 8;
    private static final int IMAGE_TYPE_RAF = 9;
    private static final int IMAGE_TYPE_RW2 = 10;
    private static final int IMAGE_TYPE_SRW = 11;
    private static final int IMAGE_TYPE_UNKNOWN = 0;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
    private static final ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
    static final byte[] JPEG_SIGNATURE;
    public static final String LATITUDE_NORTH = "N";
    public static final String LATITUDE_SOUTH = "S";
    public static final short LIGHT_SOURCE_CLOUDY_WEATHER = 10;
    public static final short LIGHT_SOURCE_COOL_WHITE_FLUORESCENT = 14;
    public static final short LIGHT_SOURCE_D50 = 23;
    public static final short LIGHT_SOURCE_D55 = 20;
    public static final short LIGHT_SOURCE_D65 = 21;
    public static final short LIGHT_SOURCE_D75 = 22;
    public static final short LIGHT_SOURCE_DAYLIGHT = 1;
    public static final short LIGHT_SOURCE_DAYLIGHT_FLUORESCENT = 12;
    public static final short LIGHT_SOURCE_DAY_WHITE_FLUORESCENT = 13;
    public static final short LIGHT_SOURCE_FINE_WEATHER = 9;
    public static final short LIGHT_SOURCE_FLASH = 4;
    public static final short LIGHT_SOURCE_FLUORESCENT = 2;
    public static final short LIGHT_SOURCE_ISO_STUDIO_TUNGSTEN = 24;
    public static final short LIGHT_SOURCE_OTHER = 255;
    public static final short LIGHT_SOURCE_SHADE = 11;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_A = 17;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_B = 18;
    public static final short LIGHT_SOURCE_STANDARD_LIGHT_C = 19;
    public static final short LIGHT_SOURCE_TUNGSTEN = 3;
    public static final short LIGHT_SOURCE_UNKNOWN = 0;
    public static final short LIGHT_SOURCE_WARM_WHITE_FLUORESCENT = 16;
    public static final short LIGHT_SOURCE_WHITE_FLUORESCENT = 15;
    public static final String LONGITUDE_EAST = "E";
    public static final String LONGITUDE_WEST = "W";
    static final byte MARKER = -1;
    static final byte MARKER_APP1 = -31;
    private static final byte MARKER_COM = -2;
    static final byte MARKER_EOI = -39;
    private static final byte MARKER_SOF0 = -64;
    private static final byte MARKER_SOF1 = -63;
    private static final byte MARKER_SOF10 = -54;
    private static final byte MARKER_SOF11 = -53;
    private static final byte MARKER_SOF13 = -51;
    private static final byte MARKER_SOF14 = -50;
    private static final byte MARKER_SOF15 = -49;
    private static final byte MARKER_SOF2 = -62;
    private static final byte MARKER_SOF3 = -61;
    private static final byte MARKER_SOF5 = -59;
    private static final byte MARKER_SOF6 = -58;
    private static final byte MARKER_SOF7 = -57;
    private static final byte MARKER_SOF9 = -55;
    private static final byte MARKER_SOI = -40;
    private static final byte MARKER_SOS = -38;
    private static final int MAX_THUMBNAIL_SIZE = 512;
    public static final short METERING_MODE_AVERAGE = 1;
    public static final short METERING_MODE_CENTER_WEIGHT_AVERAGE = 2;
    public static final short METERING_MODE_MULTI_SPOT = 4;
    public static final short METERING_MODE_OTHER = 255;
    public static final short METERING_MODE_PARTIAL = 6;
    public static final short METERING_MODE_PATTERN = 5;
    public static final short METERING_MODE_SPOT = 3;
    public static final short METERING_MODE_UNKNOWN = 0;
    private static final ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
    private static final ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
    private static final byte[] ORF_MAKER_NOTE_HEADER_1;
    private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
    private static final byte[] ORF_MAKER_NOTE_HEADER_2;
    private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
    private static final ExifTag[] ORF_MAKER_NOTE_TAGS;
    private static final short ORF_SIGNATURE_1 = 20306;
    private static final short ORF_SIGNATURE_2 = 21330;
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIGINAL_RESOLUTION_IMAGE = 0;
    private static final int PEF_MAKER_NOTE_SKIP_SIZE = 6;
    private static final String PEF_SIGNATURE = "PENTAX";
    private static final ExifTag[] PEF_TAGS;
    public static final int PHOTOMETRIC_INTERPRETATION_BLACK_IS_ZERO = 1;
    public static final int PHOTOMETRIC_INTERPRETATION_RGB = 2;
    public static final int PHOTOMETRIC_INTERPRETATION_WHITE_IS_ZERO = 0;
    public static final int PHOTOMETRIC_INTERPRETATION_YCBCR = 6;
    private static final int RAF_INFO_SIZE = 160;
    private static final int RAF_JPEG_LENGTH_VALUE_SIZE = 4;
    private static final int RAF_OFFSET_TO_JPEG_IMAGE_OFFSET = 84;
    private static final String RAF_SIGNATURE = "FUJIFILMCCD-RAW";
    public static final int REDUCED_RESOLUTION_IMAGE = 1;
    public static final short RENDERED_PROCESS_CUSTOM = 1;
    public static final short RENDERED_PROCESS_NORMAL = 0;
    public static final short RESOLUTION_UNIT_CENTIMETERS = 3;
    public static final short RESOLUTION_UNIT_INCHES = 2;
    private static final List<Integer> ROTATION_ORDER;
    private static final short RW2_SIGNATURE = 85;
    public static final short SATURATION_HIGH = 0;
    public static final short SATURATION_LOW = 0;
    public static final short SATURATION_NORMAL = 0;
    public static final short SCENE_CAPTURE_TYPE_LANDSCAPE = 1;
    public static final short SCENE_CAPTURE_TYPE_NIGHT = 3;
    public static final short SCENE_CAPTURE_TYPE_PORTRAIT = 2;
    public static final short SCENE_CAPTURE_TYPE_STANDARD = 0;
    public static final short SCENE_TYPE_DIRECTLY_PHOTOGRAPHED = 1;
    public static final short SENSITIVITY_TYPE_ISO_SPEED = 3;
    public static final short SENSITIVITY_TYPE_REI = 2;
    public static final short SENSITIVITY_TYPE_REI_AND_ISO = 6;
    public static final short SENSITIVITY_TYPE_SOS = 1;
    public static final short SENSITIVITY_TYPE_SOS_AND_ISO = 5;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI = 4;
    public static final short SENSITIVITY_TYPE_SOS_AND_REI_AND_ISO = 7;
    public static final short SENSITIVITY_TYPE_UNKNOWN = 0;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL = 5;
    public static final short SENSOR_TYPE_COLOR_SEQUENTIAL_LINEAR = 8;
    public static final short SENSOR_TYPE_NOT_DEFINED = 1;
    public static final short SENSOR_TYPE_ONE_CHIP = 2;
    public static final short SENSOR_TYPE_THREE_CHIP = 4;
    public static final short SENSOR_TYPE_TRILINEAR = 7;
    public static final short SENSOR_TYPE_TWO_CHIP = 3;
    public static final short SHARPNESS_HARD = 2;
    public static final short SHARPNESS_NORMAL = 0;
    public static final short SHARPNESS_SOFT = 1;
    private static final int SIGNATURE_CHECK_SIZE = 5000;
    static final byte START_CODE = 42;
    public static final short SUBJECT_DISTANCE_RANGE_CLOSE_VIEW = 2;
    public static final short SUBJECT_DISTANCE_RANGE_DISTANT_VIEW = 3;
    public static final short SUBJECT_DISTANCE_RANGE_MACRO = 1;
    public static final short SUBJECT_DISTANCE_RANGE_UNKNOWN = 0;
    private static final String TAG = "ExifInterface";
    public static final String TAG_APERTURE_VALUE = "ApertureValue";
    public static final String TAG_ARTIST = "Artist";
    public static final String TAG_BITS_PER_SAMPLE = "BitsPerSample";
    public static final String TAG_BODY_SERIAL_NUMBER = "BodySerialNumber";
    public static final String TAG_BRIGHTNESS_VALUE = "BrightnessValue";
    public static final String TAG_CAMARA_OWNER_NAME = "CameraOwnerName";
    public static final String TAG_CFA_PATTERN = "CFAPattern";
    public static final String TAG_COLOR_SPACE = "ColorSpace";
    public static final String TAG_COMPONENTS_CONFIGURATION = "ComponentsConfiguration";
    public static final String TAG_COMPRESSED_BITS_PER_PIXEL = "CompressedBitsPerPixel";
    public static final String TAG_COMPRESSION = "Compression";
    public static final String TAG_CONTRAST = "Contrast";
    public static final String TAG_COPYRIGHT = "Copyright";
    public static final String TAG_CUSTOM_RENDERED = "CustomRendered";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_DATETIME_DIGITIZED = "DateTimeDigitized";
    public static final String TAG_DATETIME_ORIGINAL = "DateTimeOriginal";
    public static final String TAG_DEFAULT_CROP_SIZE = "DefaultCropSize";
    public static final String TAG_DEVICE_SETTING_DESCRIPTION = "DeviceSettingDescription";
    public static final String TAG_DIGITAL_ZOOM_RATIO = "DigitalZoomRatio";
    public static final String TAG_DNG_VERSION = "DNGVersion";
    private static final String TAG_EXIF_IFD_POINTER = "ExifIFDPointer";
    public static final String TAG_EXIF_VERSION = "ExifVersion";
    public static final String TAG_EXPOSURE_BIAS_VALUE = "ExposureBiasValue";
    public static final String TAG_EXPOSURE_INDEX = "ExposureIndex";
    public static final String TAG_EXPOSURE_MODE = "ExposureMode";
    public static final String TAG_EXPOSURE_PROGRAM = "ExposureProgram";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FILE_SOURCE = "FileSource";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FLASHPIX_VERSION = "FlashpixVersion";
    public static final String TAG_FLASH_ENERGY = "FlashEnergy";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_FOCAL_LENGTH_IN_35MM_FILM = "FocalLengthIn35mmFilm";
    public static final String TAG_FOCAL_PLANE_RESOLUTION_UNIT = "FocalPlaneResolutionUnit";
    public static final String TAG_FOCAL_PLANE_X_RESOLUTION = "FocalPlaneXResolution";
    public static final String TAG_FOCAL_PLANE_Y_RESOLUTION = "FocalPlaneYResolution";
    public static final String TAG_F_NUMBER = "FNumber";
    public static final String TAG_GAIN_CONTROL = "GainControl";
    public static final String TAG_GAMMA = "Gamma";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_AREA_INFORMATION = "GPSAreaInformation";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_DEST_BEARING = "GPSDestBearing";
    public static final String TAG_GPS_DEST_BEARING_REF = "GPSDestBearingRef";
    public static final String TAG_GPS_DEST_DISTANCE = "GPSDestDistance";
    public static final String TAG_GPS_DEST_DISTANCE_REF = "GPSDestDistanceRef";
    public static final String TAG_GPS_DEST_LATITUDE = "GPSDestLatitude";
    public static final String TAG_GPS_DEST_LATITUDE_REF = "GPSDestLatitudeRef";
    public static final String TAG_GPS_DEST_LONGITUDE = "GPSDestLongitude";
    public static final String TAG_GPS_DEST_LONGITUDE_REF = "GPSDestLongitudeRef";
    public static final String TAG_GPS_DIFFERENTIAL = "GPSDifferential";
    public static final String TAG_GPS_DOP = "GPSDOP";
    public static final String TAG_GPS_H_POSITIONING_ERROR = "GPSHPositioningError";
    public static final String TAG_GPS_IMG_DIRECTION = "GPSImgDirection";
    public static final String TAG_GPS_IMG_DIRECTION_REF = "GPSImgDirectionRef";
    private static final String TAG_GPS_INFO_IFD_POINTER = "GPSInfoIFDPointer";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_MAP_DATUM = "GPSMapDatum";
    public static final String TAG_GPS_MEASURE_MODE = "GPSMeasureMode";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_SATELLITES = "GPSSatellites";
    public static final String TAG_GPS_SPEED = "GPSSpeed";
    public static final String TAG_GPS_SPEED_REF = "GPSSpeedRef";
    public static final String TAG_GPS_STATUS = "GPSStatus";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_GPS_TRACK = "GPSTrack";
    public static final String TAG_GPS_TRACK_REF = "GPSTrackRef";
    public static final String TAG_GPS_VERSION_ID = "GPSVersionID";
    private static final String TAG_HAS_THUMBNAIL = "HasThumbnail";
    public static final String TAG_IMAGE_DESCRIPTION = "ImageDescription";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_UNIQUE_ID = "ImageUniqueID";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    private static final String TAG_INTEROPERABILITY_IFD_POINTER = "InteroperabilityIFDPointer";
    public static final String TAG_INTEROPERABILITY_INDEX = "InteroperabilityIndex";
    public static final String TAG_ISO_SPEED = "ISOSpeed";
    public static final String TAG_ISO_SPEED_LATITUDE_YYY = "ISOSpeedLatitudeyyy";
    public static final String TAG_ISO_SPEED_LATITUDE_ZZZ = "ISOSpeedLatitudezzz";
    @Deprecated
    public static final String TAG_ISO_SPEED_RATINGS = "ISOSpeedRatings";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT = "JPEGInterchangeFormat";
    public static final String TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = "JPEGInterchangeFormatLength";
    public static final String TAG_LENS_MAKE = "LensMake";
    public static final String TAG_LENS_MODEL = "LensModel";
    public static final String TAG_LENS_SERIAL_NUMBER = "LensSerialNumber";
    public static final String TAG_LENS_SPECIFICATION = "LensSpecification";
    public static final String TAG_LIGHT_SOURCE = "LightSource";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MAKER_NOTE = "MakerNote";
    public static final String TAG_MAX_APERTURE_VALUE = "MaxApertureValue";
    public static final String TAG_METERING_MODE = "MeteringMode";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_NEW_SUBFILE_TYPE = "NewSubfileType";
    public static final String TAG_OECF = "OECF";
    public static final String TAG_ORF_ASPECT_FRAME = "AspectFrame";
    private static final String TAG_ORF_CAMERA_SETTINGS_IFD_POINTER = "CameraSettingsIFDPointer";
    private static final String TAG_ORF_IMAGE_PROCESSING_IFD_POINTER = "ImageProcessingIFDPointer";
    public static final String TAG_ORF_PREVIEW_IMAGE_LENGTH = "PreviewImageLength";
    public static final String TAG_ORF_PREVIEW_IMAGE_START = "PreviewImageStart";
    public static final String TAG_ORF_THUMBNAIL_IMAGE = "ThumbnailImage";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_PHOTOGRAPHIC_SENSITIVITY = "PhotographicSensitivity";
    public static final String TAG_PHOTOMETRIC_INTERPRETATION = "PhotometricInterpretation";
    public static final String TAG_PIXEL_X_DIMENSION = "PixelXDimension";
    public static final String TAG_PIXEL_Y_DIMENSION = "PixelYDimension";
    public static final String TAG_PLANAR_CONFIGURATION = "PlanarConfiguration";
    public static final String TAG_PRIMARY_CHROMATICITIES = "PrimaryChromaticities";
    private static final ExifTag TAG_RAF_IMAGE_SIZE;
    public static final String TAG_RECOMMENDED_EXPOSURE_INDEX = "RecommendedExposureIndex";
    public static final String TAG_REFERENCE_BLACK_WHITE = "ReferenceBlackWhite";
    public static final String TAG_RELATED_SOUND_FILE = "RelatedSoundFile";
    public static final String TAG_RESOLUTION_UNIT = "ResolutionUnit";
    public static final String TAG_ROWS_PER_STRIP = "RowsPerStrip";
    public static final String TAG_RW2_ISO = "ISO";
    public static final String TAG_RW2_JPG_FROM_RAW = "JpgFromRaw";
    public static final String TAG_RW2_SENSOR_BOTTOM_BORDER = "SensorBottomBorder";
    public static final String TAG_RW2_SENSOR_LEFT_BORDER = "SensorLeftBorder";
    public static final String TAG_RW2_SENSOR_RIGHT_BORDER = "SensorRightBorder";
    public static final String TAG_RW2_SENSOR_TOP_BORDER = "SensorTopBorder";
    public static final String TAG_SAMPLES_PER_PIXEL = "SamplesPerPixel";
    public static final String TAG_SATURATION = "Saturation";
    public static final String TAG_SCENE_CAPTURE_TYPE = "SceneCaptureType";
    public static final String TAG_SCENE_TYPE = "SceneType";
    public static final String TAG_SENSING_METHOD = "SensingMethod";
    public static final String TAG_SENSITIVITY_TYPE = "SensitivityType";
    public static final String TAG_SHARPNESS = "Sharpness";
    public static final String TAG_SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    public static final String TAG_SOFTWARE = "Software";
    public static final String TAG_SPATIAL_FREQUENCY_RESPONSE = "SpatialFrequencyResponse";
    public static final String TAG_SPECTRAL_SENSITIVITY = "SpectralSensitivity";
    public static final String TAG_STANDARD_OUTPUT_SENSITIVITY = "StandardOutputSensitivity";
    public static final String TAG_STRIP_BYTE_COUNTS = "StripByteCounts";
    public static final String TAG_STRIP_OFFSETS = "StripOffsets";
    public static final String TAG_SUBFILE_TYPE = "SubfileType";
    public static final String TAG_SUBJECT_AREA = "SubjectArea";
    public static final String TAG_SUBJECT_DISTANCE = "SubjectDistance";
    public static final String TAG_SUBJECT_DISTANCE_RANGE = "SubjectDistanceRange";
    public static final String TAG_SUBJECT_LOCATION = "SubjectLocation";
    public static final String TAG_SUBSEC_TIME = "SubSecTime";
    public static final String TAG_SUBSEC_TIME_DIGITIZED = "SubSecTimeDigitized";
    public static final String TAG_SUBSEC_TIME_ORIGINAL = "SubSecTimeOriginal";
    private static final String TAG_SUB_IFD_POINTER = "SubIFDPointer";
    private static final String TAG_THUMBNAIL_DATA = "ThumbnailData";
    public static final String TAG_THUMBNAIL_IMAGE_LENGTH = "ThumbnailImageLength";
    public static final String TAG_THUMBNAIL_IMAGE_WIDTH = "ThumbnailImageWidth";
    private static final String TAG_THUMBNAIL_LENGTH = "ThumbnailLength";
    private static final String TAG_THUMBNAIL_OFFSET = "ThumbnailOffset";
    public static final String TAG_TRANSFER_FUNCTION = "TransferFunction";
    public static final String TAG_USER_COMMENT = "UserComment";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final String TAG_WHITE_POINT = "WhitePoint";
    public static final String TAG_X_RESOLUTION = "XResolution";
    public static final String TAG_Y_CB_CR_COEFFICIENTS = "YCbCrCoefficients";
    public static final String TAG_Y_CB_CR_POSITIONING = "YCbCrPositioning";
    public static final String TAG_Y_CB_CR_SUB_SAMPLING = "YCbCrSubSampling";
    public static final String TAG_Y_RESOLUTION = "YResolution";
    @Deprecated
    public static final int WHITEBALANCE_AUTO = 0;
    @Deprecated
    public static final int WHITEBALANCE_MANUAL = 1;
    public static final short WHITE_BALANCE_AUTO = 0;
    public static final short WHITE_BALANCE_MANUAL = 1;
    public static final short Y_CB_CR_POSITIONING_CENTERED = 1;
    public static final short Y_CB_CR_POSITIONING_CO_SITED = 2;
    private static final HashMap<Integer, Integer> sExifPointerTagMap;
    private static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading;
    private static final HashMap<String, ExifTag>[] sExifTagMapsForWriting;
    private static SimpleDateFormat sFormatter;
    private static final Pattern sGpsTimestampPattern;
    private static final Pattern sNonZeroTimePattern;
    private static final HashSet<String> sTagSetForCompatibility;
    private final AssetManager.AssetInputStream mAssetInputStream;
    private final HashMap<String, ExifAttribute>[] mAttributes = new HashMap[EXIF_TAGS.length];
    private Set<Integer> mAttributesOffsets = new HashSet<Integer>(EXIF_TAGS.length);
    private ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    private int mExifOffset;
    private final String mFilename;
    private boolean mHasThumbnail;
    private boolean mIsSupportedFile;
    private int mMimeType;
    private int mOrfMakerNoteOffset;
    private int mOrfThumbnailLength;
    private int mOrfThumbnailOffset;
    private int mRw2JpgFromRawOffset;
    private byte[] mThumbnailBytes;
    private int mThumbnailCompression;
    private int mThumbnailLength;
    private int mThumbnailOffset;

    static {
        Integer n = 1;
        Integer n2 = 3;
        Integer n3 = 2;
        Integer n4 = 8;
        ROTATION_ORDER = Arrays.asList(n, 6, n2, n4);
        Integer n5 = 7;
        Integer n6 = 5;
        FLIPPED_ROTATION_ORDER = Arrays.asList(n3, n5, 4, n6);
        BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
        BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
        BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
        JPEG_SIGNATURE = new byte[]{-1, -40, -1};
        ORF_MAKER_NOTE_HEADER_1 = new byte[]{79, 76, 89, 77, 80, 0};
        ORF_MAKER_NOTE_HEADER_2 = new byte[]{79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
        IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
        IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
        EXIF_ASCII_PREFIX = new byte[]{65, 83, 67, 73, 73, 0, 0, 0};
        IFD_TIFF_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7)};
        IFD_EXIF_TAGS = new ExifTag[]{new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
        IFD_GPS_TAGS = new ExifTag[]{new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3)};
        IFD_INTEROPERABILITY_TAGS = new ExifTag[]{new ExifTag("InteroperabilityIndex", 1, 2)};
        IFD_THUMBNAIL_TAGS = new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)};
        TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
        ORF_MAKER_NOTE_TAGS = new ExifTag[]{new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4)};
        ORF_CAMERA_SETTINGS_TAGS = new ExifTag[]{new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4)};
        ORF_IMAGE_PROCESSING_TAGS = new ExifTag[]{new ExifTag("AspectFrame", 4371, 3)};
        ExifTag[] arrexifTag = new ExifTag[]{new ExifTag("ColorSpace", 55, 3)};
        PEF_TAGS = arrexifTag;
        ExifTag[] object2 = IFD_TIFF_TAGS;
        EXIF_TAGS = new ExifTag[][]{object2, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, object2, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, arrexifTag};
        EXIF_POINTER_TAGS = new ExifTag[]{new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1)};
        JPEG_INTERCHANGE_FORMAT_TAG = new ExifTag("JPEGInterchangeFormat", 513, 4);
        JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifTag("JPEGInterchangeFormatLength", 514, 4);
        arrexifTag = EXIF_TAGS;
        sExifTagMapsForReading = new HashMap[arrexifTag.length];
        sExifTagMapsForWriting = new HashMap[arrexifTag.length];
        sTagSetForCompatibility = new HashSet<String>(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
        sExifPointerTagMap = new HashMap();
        arrexifTag = Charset.forName("US-ASCII");
        ASCII = arrexifTag;
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes((Charset)arrexifTag);
        arrexifTag = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        sFormatter = arrexifTag;
        arrexifTag.setTimeZone(TimeZone.getTimeZone("UTC"));
        for (int i = 0; i < EXIF_TAGS.length; ++i) {
            ExifInterface.sExifTagMapsForReading[i] = new HashMap();
            ExifInterface.sExifTagMapsForWriting[i] = new HashMap();
            for (ExifTag exifTag : EXIF_TAGS[i]) {
                sExifTagMapsForReading[i].put(exifTag.number, exifTag);
                sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
            }
        }
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[0].number, n6);
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[1].number, n);
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[2].number, n3);
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[3].number, n2);
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[4].number, n5);
        sExifPointerTagMap.put(ExifInterface.EXIF_POINTER_TAGS[5].number, n4);
        sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
        sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
    }

    public ExifInterface(InputStream inputStream) throws IOException {
        if (inputStream != null) {
            this.mFilename = null;
            this.mAssetInputStream = inputStream instanceof AssetManager.AssetInputStream ? (AssetManager.AssetInputStream)inputStream : null;
            this.loadAttributes(inputStream);
            return;
        }
        throw new IllegalArgumentException("inputStream cannot be null");
    }

    public ExifInterface(String object) throws IOException {
        if (object != null) {
            Object object2 = null;
            this.mAssetInputStream = null;
            this.mFilename = object;
            try {
                object2 = object = new FileInputStream((String)object);
            }
            catch (Throwable throwable) {
                ExifInterface.closeQuietly((Closeable)object2);
                throw throwable;
            }
            this.loadAttributes((InputStream)object);
            ExifInterface.closeQuietly((Closeable)object);
            return;
        }
        throw new IllegalArgumentException("filename cannot be null");
    }

    private void addDefaultValuesForCompatibility() {
        String string2 = this.getAttribute("DateTimeOriginal");
        if (string2 != null && this.getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(string2));
        }
        if (this.getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return;
            }
            catch (Exception exception) {
                return;
            }
            catch (RuntimeException runtimeException) {
                throw runtimeException;
            }
        }
    }

    private String convertDecimalDegree(double d) {
        long l = (long)d;
        long l2 = (long)((d - (double)l) * 60.0);
        long l3 = Math.round((d - (double)l - (double)l2 / 60.0) * 3600.0 * 1.0E7);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(l);
        stringBuilder.append("/1,");
        stringBuilder.append(l2);
        stringBuilder.append("/1,");
        stringBuilder.append(l3);
        stringBuilder.append("/10000000");
        return stringBuilder.toString();
    }

    private static double convertRationalLatLonToDouble(String arrstring, String string2) {
        block7 : {
            double d;
            block4 : {
                block5 : {
                    block6 : {
                        arrstring = arrstring.split(",", -1);
                        String[] arrstring2 = arrstring[0].split("/", -1);
                        d = Double.parseDouble(arrstring2[0].trim()) / Double.parseDouble(arrstring2[1].trim());
                        arrstring2 = arrstring[1].split("/", -1);
                        double d2 = Double.parseDouble(arrstring2[0].trim()) / Double.parseDouble(arrstring2[1].trim());
                        arrstring = arrstring[2].split("/", -1);
                        double d3 = Double.parseDouble(arrstring[0].trim()) / Double.parseDouble(arrstring[1].trim());
                        d = d2 / 60.0 + d + d3 / 3600.0;
                        if (string2.equals("S") || string2.equals("W")) break block4;
                        if (string2.equals("N")) break block5;
                        if (!string2.equals("E")) break block6;
                        return d;
                    }
                    try {
                        throw new IllegalArgumentException();
                    }
                    catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                        break block7;
                    }
                    catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                }
                return d;
            }
            return - d;
        }
        throw new IllegalArgumentException();
    }

    private static long[] convertToLongArray(Object arrn) {
        if (arrn instanceof int[]) {
            arrn = arrn;
            long[] arrl = new long[arrn.length];
            for (int i = 0; i < arrn.length; ++i) {
                arrl[i] = arrn[i];
            }
            return arrl;
        }
        if (arrn instanceof long[]) {
            return arrn;
        }
        return null;
    }

    private static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        int n;
        int n2 = 0;
        byte[] arrby = new byte[8192];
        while ((n = inputStream.read(arrby)) != -1) {
            n2 += n;
            outputStream.write(arrby, 0, n);
        }
        return n2;
    }

    private ExifAttribute getExifAttribute(String object) {
        String string2 = object;
        if ("ISOSpeedRatings".equals(object)) {
            string2 = "PhotographicSensitivity";
        }
        for (int i = 0; i < EXIF_TAGS.length; ++i) {
            object = this.mAttributes[i].get(string2);
            if (object == null) continue;
            return object;
        }
        return null;
    }

    private void getJpegAttributes(ByteOrderedDataInputStream object, int n, int n2) throws IOException {
        int n3;
        block20 : {
            block21 : {
                object.setByteOrder(ByteOrder.BIG_ENDIAN);
                object.seek(n);
                n3 = object.readByte();
                if (n3 != -1) break block20;
                if (object.readByte() != -40) break block21;
                n = n + 1 + 1;
                while ((n3 = object.readByte()) == -1) {
                    block22 : {
                        block23 : {
                            block30 : {
                                block31 : {
                                    block32 : {
                                        block26 : {
                                            byte[] arrby;
                                            int n4;
                                            int n5;
                                            block29 : {
                                                block24 : {
                                                    block28 : {
                                                        block25 : {
                                                            block27 : {
                                                                n3 = object.readByte();
                                                                if (n3 == -39 || n3 == -38) break block22;
                                                                n5 = object.readUnsignedShort() - 2;
                                                                n4 = n + 1 + 1 + 2;
                                                                if (n5 < 0) break block23;
                                                                if (n3 == -31) break block24;
                                                                if (n3 == -2) break block25;
                                                                switch (n3) {
                                                                    default: {
                                                                        switch (n3) {
                                                                            default: {
                                                                                switch (n3) {
                                                                                    default: {
                                                                                        switch (n3) {
                                                                                            default: {
                                                                                                n3 = n4;
                                                                                                n = n5;
                                                                                                break block26;
                                                                                            }
                                                                                            case -51: 
                                                                                            case -50: 
                                                                                            case -49: 
                                                                                        }
                                                                                    }
                                                                                    case -55: 
                                                                                    case -54: 
                                                                                    case -53: 
                                                                                }
                                                                            }
                                                                            case -59: 
                                                                            case -58: 
                                                                            case -57: 
                                                                        }
                                                                    }
                                                                    case -64: 
                                                                    case -63: 
                                                                    case -62: 
                                                                    case -61: 
                                                                }
                                                                if (object.skipBytes(1) != 1) break block27;
                                                                this.mAttributes[n2].put("ImageLength", ExifAttribute.createULong(object.readUnsignedShort(), this.mExifByteOrder));
                                                                this.mAttributes[n2].put("ImageWidth", ExifAttribute.createULong(object.readUnsignedShort(), this.mExifByteOrder));
                                                                n = n5 - 5;
                                                                n3 = n4;
                                                                break block26;
                                                            }
                                                            throw new IOException("Invalid SOFx");
                                                        }
                                                        arrby = new byte[n5];
                                                        if (object.read(arrby) != n5) break block28;
                                                        n5 = 0;
                                                        n3 = n4;
                                                        n = n5;
                                                        if (this.getAttribute("UserComment") == null) {
                                                            this.mAttributes[1].put("UserComment", ExifAttribute.createString(new String(arrby, ASCII)));
                                                            n3 = n4;
                                                            n = n5;
                                                        }
                                                        break block26;
                                                    }
                                                    throw new IOException("Invalid exif");
                                                }
                                                if (n5 >= 6) break block29;
                                                n3 = n4;
                                                n = n5;
                                                break block26;
                                            }
                                            arrby = new byte[6];
                                            if (object.read(arrby) != 6) break block30;
                                            n3 = n4 + 6;
                                            n = n5 - 6;
                                            if (!Arrays.equals(arrby, IDENTIFIER_EXIF_APP1)) break block26;
                                            if (n <= 0) break block31;
                                            this.mExifOffset = n3;
                                            arrby = new byte[n];
                                            if (object.read(arrby) != n) break block32;
                                            n3 += n;
                                            n = 0;
                                            this.readExifSegment(arrby, n2);
                                        }
                                        if (n >= 0) {
                                            if (object.skipBytes(n) == n) {
                                                n = n3 + n;
                                                continue;
                                            }
                                            throw new IOException("Invalid JPEG segment");
                                        }
                                        throw new IOException("Invalid length");
                                    }
                                    throw new IOException("Invalid exif");
                                }
                                throw new IOException("Invalid exif");
                            }
                            throw new IOException("Invalid exif");
                        }
                        throw new IOException("Invalid length");
                    }
                    object.setByteOrder(this.mExifByteOrder);
                    return;
                }
                object = new StringBuilder();
                object.append("Invalid marker:");
                object.append(Integer.toHexString(n3 & 255));
                throw new IOException(object.toString());
            }
            object = new StringBuilder();
            object.append("Invalid marker: ");
            object.append(Integer.toHexString(n3 & 255));
            throw new IOException(object.toString());
        }
        object = new StringBuilder();
        object.append("Invalid marker: ");
        object.append(Integer.toHexString(n3 & 255));
        throw new IOException(object.toString());
    }

    private int getMimeType(BufferedInputStream bufferedInputStream) throws IOException {
        bufferedInputStream.mark(5000);
        byte[] arrby = new byte[5000];
        bufferedInputStream.read(arrby);
        bufferedInputStream.reset();
        if (ExifInterface.isJpegFormat(arrby)) {
            return 4;
        }
        if (this.isRafFormat(arrby)) {
            return 9;
        }
        if (this.isOrfFormat(arrby)) {
            return 7;
        }
        if (this.isRw2Format(arrby)) {
            return 10;
        }
        return 0;
    }

    private void getOrfAttributes(ByteOrderedDataInputStream object) throws IOException {
        this.getRawAttributes((ByteOrderedDataInputStream)object);
        object = this.mAttributes[1].get("MakerNote");
        if (object != null) {
            object = new ByteOrderedDataInputStream(object.bytes);
            object.setByteOrder(this.mExifByteOrder);
            Object object2 = new byte[ORF_MAKER_NOTE_HEADER_1.length];
            object.readFully((byte[])object2);
            object.seek(0L);
            byte[] arrby = new byte[ORF_MAKER_NOTE_HEADER_2.length];
            object.readFully(arrby);
            if (Arrays.equals((byte[])object2, ORF_MAKER_NOTE_HEADER_1)) {
                object.seek(8L);
            } else if (Arrays.equals(arrby, ORF_MAKER_NOTE_HEADER_2)) {
                object.seek(12L);
            }
            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 6);
            object = this.mAttributes[7].get("PreviewImageStart");
            object2 = this.mAttributes[7].get("PreviewImageLength");
            if (object != null && object2 != null) {
                this.mAttributes[5].put("JPEGInterchangeFormat", (ExifAttribute)object);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", (ExifAttribute)object2);
            }
            if ((object = this.mAttributes[8].get("AspectFrame")) != null) {
                if ((object = (int[])object.getValue(this.mExifByteOrder)) != null && object.length == 4) {
                    if (object[2] > object[0] && object[3] > object[1]) {
                        int n = object[2] - object[0] + 1;
                        int n2 = object[3] - object[1] + 1;
                        int n3 = n;
                        int n4 = n2;
                        if (n < n2) {
                            n3 = n + n2;
                            n4 = n3 - n2;
                            n3 -= n4;
                        }
                        object = ExifAttribute.createUShort(n3, this.mExifByteOrder);
                        object2 = ExifAttribute.createUShort(n4, this.mExifByteOrder);
                        this.mAttributes[0].put("ImageWidth", (ExifAttribute)object);
                        this.mAttributes[0].put("ImageLength", (ExifAttribute)object2);
                        return;
                    }
                } else {
                    object2 = new StringBuilder();
                    object2.append("Invalid aspect frame values. frame=");
                    object2.append(Arrays.toString((int[])object));
                    Log.w((String)"ExifInterface", (String)object2.toString());
                    return;
                }
            }
        }
    }

    private void getRafAttributes(ByteOrderedDataInputStream object) throws IOException {
        object.skipBytes(84);
        Object object2 = new byte[4];
        byte[] arrby = new byte[4];
        object.read((byte[])object2);
        object.skipBytes(4);
        object.read(arrby);
        int n = ByteBuffer.wrap((byte[])object2).getInt();
        int n2 = ByteBuffer.wrap(arrby).getInt();
        this.getJpegAttributes((ByteOrderedDataInputStream)object, n, 5);
        object.seek(n2);
        object.setByteOrder(ByteOrder.BIG_ENDIAN);
        n2 = object.readInt();
        for (n = 0; n < n2; ++n) {
            int n3 = object.readUnsignedShort();
            int n4 = object.readUnsignedShort();
            if (n3 == ExifInterface.TAG_RAF_IMAGE_SIZE.number) {
                n = object.readShort();
                n2 = object.readShort();
                object = ExifAttribute.createUShort(n, this.mExifByteOrder);
                object2 = ExifAttribute.createUShort(n2, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", (ExifAttribute)object);
                this.mAttributes[0].put("ImageWidth", (ExifAttribute)object2);
                return;
            }
            object.skipBytes(n4);
        }
    }

    private void getRawAttributes(ByteOrderedDataInputStream object) throws IOException {
        this.parseTiffHeaders((ByteOrderedDataInputStream)object, object.available());
        this.readImageFileDirectory((ByteOrderedDataInputStream)object, 0);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 0);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 5);
        this.updateImageSizeValues((ByteOrderedDataInputStream)object, 4);
        this.validateImages((InputStream)object);
        if (this.mMimeType == 8 && (object = this.mAttributes[1].get("MakerNote")) != null) {
            object = new ByteOrderedDataInputStream(object.bytes);
            object.setByteOrder(this.mExifByteOrder);
            object.seek(6L);
            this.readImageFileDirectory((ByteOrderedDataInputStream)object, 9);
            object = this.mAttributes[9].get("ColorSpace");
            if (object != null) {
                this.mAttributes[1].put("ColorSpace", (ExifAttribute)object);
            }
        }
    }

    private void getRw2Attributes(ByteOrderedDataInputStream object) throws IOException {
        this.getRawAttributes((ByteOrderedDataInputStream)object);
        if (this.mAttributes[0].get("JpgFromRaw") != null) {
            this.getJpegAttributes((ByteOrderedDataInputStream)object, this.mRw2JpgFromRawOffset, 5);
        }
        object = this.mAttributes[0].get("ISO");
        ExifAttribute exifAttribute = this.mAttributes[1].get("PhotographicSensitivity");
        if (object != null && exifAttribute == null) {
            this.mAttributes[1].put("PhotographicSensitivity", (ExifAttribute)object);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Pair<Integer, Integer> guessDataFormat(String pair) {
        String[] arrstring;
        boolean bl = pair.contains(",");
        Integer n = 2;
        Integer n2 = -1;
        if (bl) {
            arrstring = pair.split(",", -1);
            pair = ExifInterface.guessDataFormat(arrstring[0]);
            if ((Integer)pair.first == 2) {
                return pair;
            }
        } else {
            long l;
            long l2;
            block23 : {
                if (pair.contains("/")) {
                    if ((pair = pair.split("/", -1)).length != 2) return new Pair((Object)n, (Object)n2);
                    try {
                        l2 = (long)Double.parseDouble(pair[0]);
                        l = (long)Double.parseDouble((String)pair[1]);
                        if (l2 < 0L) return new Pair((Object)10, (Object)n2);
                        if (l < 0L) {
                            return new Pair((Object)10, (Object)n2);
                        }
                        break block23;
                    }
                    catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                    return new Pair((Object)n, (Object)n2);
                }
                try {
                    Long l3 = Long.parseLong((String)pair);
                    if (l3 >= 0L && l3 <= 65535L) {
                        return new Pair((Object)3, (Object)4);
                    }
                    if (l3 >= 0L) return new Pair((Object)4, (Object)n2);
                    return new Pair((Object)9, (Object)n2);
                }
                catch (NumberFormatException numberFormatException) {
                    try {
                        Double.parseDouble(pair);
                        return new Pair((Object)12, (Object)n2);
                    }
                    catch (NumberFormatException numberFormatException2) {
                        return new Pair((Object)n, (Object)n2);
                    }
                }
            }
            if (l2 > Integer.MAX_VALUE) return new Pair((Object)5, (Object)n2);
            if (l <= Integer.MAX_VALUE) return new Pair((Object)10, (Object)5);
            return new Pair((Object)5, (Object)n2);
        }
        int n3 = 1;
        while (n3 < arrstring.length) {
            int n4;
            int n5;
            block24 : {
                block25 : {
                    Pair<Integer, Integer> pair2 = ExifInterface.guessDataFormat(arrstring[n3]);
                    n4 = -1;
                    int n6 = -1;
                    if (((Integer)pair2.first).equals(pair.first) || ((Integer)pair2.second).equals(pair.first)) {
                        n4 = (Integer)pair.first;
                    }
                    n5 = n6;
                    if ((Integer)pair.second == -1) break block24;
                    if (((Integer)pair2.first).equals(pair.second)) break block25;
                    n5 = n6;
                    if (!((Integer)pair2.second).equals(pair.second)) break block24;
                }
                n5 = (Integer)pair.second;
            }
            if (n4 == -1 && n5 == -1) {
                return new Pair((Object)n, (Object)n2);
            }
            if (n4 == -1) {
                pair = new Pair((Object)n5, (Object)n2);
            } else if (n5 == -1) {
                pair = new Pair((Object)n4, (Object)n2);
            }
            ++n3;
        }
        return pair;
    }

    private void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap arrby) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute)arrby.get("JPEGInterchangeFormat");
        arrby = (ExifAttribute)arrby.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && arrby != null) {
            int n;
            int n2 = exifAttribute.getIntValue(this.mExifByteOrder);
            int n3 = Math.min(arrby.getIntValue(this.mExifByteOrder), byteOrderedDataInputStream.available() - n2);
            int n4 = this.mMimeType;
            if (n4 != 4 && n4 != 9 && n4 != 10) {
                n = n2;
                if (n4 == 7) {
                    n = n2 + this.mOrfMakerNoteOffset;
                }
            } else {
                n = n2 + this.mExifOffset;
            }
            if (n > 0 && n3 > 0) {
                this.mHasThumbnail = true;
                this.mThumbnailOffset = n;
                this.mThumbnailLength = n3;
                if (this.mFilename == null && this.mAssetInputStream == null) {
                    arrby = new byte[n3];
                    byteOrderedDataInputStream.seek(n);
                    byteOrderedDataInputStream.readFully(arrby);
                    this.mThumbnailBytes = arrby;
                }
            }
        }
    }

    private void handleThumbnailFromStrips(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap arrl) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute)arrl.get("StripOffsets");
        ExifAttribute exifAttribute2 = (ExifAttribute)arrl.get("StripByteCounts");
        if (exifAttribute != null && exifAttribute2 != null) {
            int n;
            arrl = ExifInterface.convertToLongArray(exifAttribute.getValue(this.mExifByteOrder));
            long[] arrl2 = ExifInterface.convertToLongArray(exifAttribute2.getValue(this.mExifByteOrder));
            if (arrl == null) {
                Log.w((String)"ExifInterface", (String)"stripOffsets should not be null.");
                return;
            }
            if (arrl2 == null) {
                Log.w((String)"ExifInterface", (String)"stripByteCounts should not be null.");
                return;
            }
            long l = 0L;
            int n2 = arrl2.length;
            for (n = 0; n < n2; ++n) {
                l += arrl2[n];
            }
            byte[] arrby = new byte[(int)l];
            n2 = 0;
            int n3 = 0;
            n = 0;
            do {
                ByteOrderedDataInputStream byteOrderedDataInputStream2 = byteOrderedDataInputStream;
                if (n >= arrl.length) break;
                int n4 = (int)arrl[n];
                int n5 = (int)arrl2[n];
                if ((n4 -= n2) < 0) {
                    Log.d((String)"ExifInterface", (String)"Invalid strip offset value");
                }
                byteOrderedDataInputStream2.seek(n4);
                byte[] arrby2 = new byte[n5];
                byteOrderedDataInputStream2.read(arrby2);
                System.arraycopy(arrby2, 0, arrby, n3, arrby2.length);
                n3 += arrby2.length;
                ++n;
                n2 = n2 + n4 + n5;
            } while (true);
            this.mHasThumbnail = true;
            this.mThumbnailBytes = arrby;
            this.mThumbnailLength = arrby.length;
            return;
        }
    }

    private static boolean isJpegFormat(byte[] arrby) throws IOException {
        byte[] arrby2;
        for (int i = 0; i < (arrby2 = JPEG_SIGNATURE).length; ++i) {
            if (arrby[i] == arrby2[i]) continue;
            return false;
        }
        return true;
    }

    private boolean isOrfFormat(byte[] object) throws IOException {
        ByteOrder byteOrder;
        object = new ByteOrderedDataInputStream((byte[])object);
        this.mExifByteOrder = byteOrder = this.readByteOrder((ByteOrderedDataInputStream)object);
        object.setByteOrder(byteOrder);
        short s = object.readShort();
        object.close();
        if (s != 20306 && s != 21330) {
            return false;
        }
        return true;
    }

    private boolean isRafFormat(byte[] arrby) throws IOException {
        byte[] arrby2 = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i = 0; i < arrby2.length; ++i) {
            if (arrby[i] == arrby2[i]) continue;
            return false;
        }
        return true;
    }

    private boolean isRw2Format(byte[] object) throws IOException {
        ByteOrder byteOrder;
        object = new ByteOrderedDataInputStream((byte[])object);
        this.mExifByteOrder = byteOrder = this.readByteOrder((ByteOrderedDataInputStream)object);
        object.setByteOrder(byteOrder);
        short s = object.readShort();
        object.close();
        if (s == 85) {
            return true;
        }
        return false;
    }

    private boolean isSupportedDataType(HashMap object) throws IOException {
        int[] arrn = (int[])object.get("BitsPerSample");
        if (arrn != null) {
            int n;
            if (Arrays.equals(BITS_PER_SAMPLE_RGB, arrn = (int[])arrn.getValue(this.mExifByteOrder))) {
                return true;
            }
            if (this.mMimeType == 3 && (object = (ExifAttribute)object.get("PhotometricInterpretation")) != null && ((n = object.getIntValue(this.mExifByteOrder)) == 1 && Arrays.equals(arrn, BITS_PER_SAMPLE_GREYSCALE_2) || n == 6 && Arrays.equals(arrn, BITS_PER_SAMPLE_RGB))) {
                return true;
            }
        }
        return false;
    }

    private boolean isThumbnail(HashMap object) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute)object.get("ImageLength");
        object = (ExifAttribute)object.get("ImageWidth");
        if (exifAttribute != null && object != null) {
            int n = exifAttribute.getIntValue(this.mExifByteOrder);
            int n2 = object.getIntValue(this.mExifByteOrder);
            if (n <= 512 && n2 <= 512) {
                return true;
            }
        }
        return false;
    }

    /*
     * Exception decompiling
     */
    private void loadAttributes(InputStream var1_1) throws IOException {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    private void parseTiffHeaders(ByteOrderedDataInputStream object, int n) throws IOException {
        ByteOrder byteOrder;
        this.mExifByteOrder = byteOrder = this.readByteOrder((ByteOrderedDataInputStream)object);
        object.setByteOrder(byteOrder);
        int n2 = object.readUnsignedShort();
        int n3 = this.mMimeType;
        if (n3 != 7 && n3 != 10 && n2 != 42) {
            object = new StringBuilder();
            object.append("Invalid start code: ");
            object.append(Integer.toHexString(n2));
            throw new IOException(object.toString());
        }
        n2 = object.readInt();
        if (n2 >= 8 && n2 < n) {
            n = n2 - 8;
            if (n > 0) {
                if (object.skipBytes(n) == n) {
                    return;
                }
                object = new StringBuilder();
                object.append("Couldn't jump to first Ifd: ");
                object.append(n);
                throw new IOException(object.toString());
            }
            return;
        }
        object = new StringBuilder();
        object.append("Invalid first Ifd offset: ");
        object.append(n2);
        throw new IOException(object.toString());
    }

    private void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("The size of tag group[");
            stringBuilder.append(i);
            stringBuilder.append("]: ");
            stringBuilder.append(this.mAttributes[i].size());
            Log.d((String)"ExifInterface", (String)stringBuilder.toString());
            for (Map.Entry entry : this.mAttributes[i].entrySet()) {
                ExifAttribute exifAttribute = (ExifAttribute)entry.getValue();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("tagName: ");
                stringBuilder2.append((String)entry.getKey());
                stringBuilder2.append(", tagType: ");
                stringBuilder2.append(exifAttribute.toString());
                stringBuilder2.append(", tagValue: '");
                stringBuilder2.append(exifAttribute.getStringValue(this.mExifByteOrder));
                stringBuilder2.append("'");
                Log.d((String)"ExifInterface", (String)stringBuilder2.toString());
            }
        }
    }

    private ByteOrder readByteOrder(ByteOrderedDataInputStream object) throws IOException {
        short s = object.readShort();
        if (s != 18761) {
            if (s == 19789) {
                return ByteOrder.BIG_ENDIAN;
            }
            object = new StringBuilder();
            object.append("Invalid byte order: ");
            object.append(Integer.toHexString(s));
            throw new IOException(object.toString());
        }
        return ByteOrder.LITTLE_ENDIAN;
    }

    private void readExifSegment(byte[] arrby, int n) throws IOException {
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(arrby);
        this.parseTiffHeaders(byteOrderedDataInputStream, arrby.length);
        this.readImageFileDirectory(byteOrderedDataInputStream, n);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void readImageFileDirectory(ByteOrderedDataInputStream var1_1, int var2_2) throws IOException {
        this.mAttributesOffsets.add(var1_1.mPosition);
        if (var1_1.mPosition + 2 > var1_1.mLength) {
            return;
        }
        var4_3 = var1_1.readShort();
        if (var1_1.mPosition + var4_3 * 12 > var1_1.mLength) return;
        if (var4_3 <= 0) {
            return;
        }
        var5_4 = 0;
        do {
            block39 : {
                block38 : {
                    block37 : {
                        block34 : {
                            block35 : {
                                block36 : {
                                    block33 : {
                                        var8_8 = var2_2;
                                        if (var5_4 >= var4_3) break;
                                        var9_9 = var1_1.readUnsignedShort();
                                        var6_6 = var1_1.readUnsignedShort();
                                        var10_10 = var1_1.readInt();
                                        var13_12 = (long)var1_1.peek() + 4L;
                                        var15_13 = ExifInterface.sExifTagMapsForReading[var8_8].get(var9_9);
                                        var7_7 = 0;
                                        if (var15_13 != null) break block33;
                                        var16_14 = new StringBuilder();
                                        var16_14.append("Skip the tag entry since tag number is not defined: ");
                                        var16_14.append(var9_9);
                                        Log.w((String)"ExifInterface", (String)var16_14.toString());
                                        break block34;
                                    }
                                    if (var6_6 <= 0 || var6_6 >= ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT.length) break block35;
                                    if (var15_13.isFormatCompatible(var6_6)) break block36;
                                    var16_14 = new StringBuilder();
                                    var16_14.append("Skip the tag entry since data format (");
                                    var16_14.append(ExifInterface.IFD_FORMAT_NAMES[var6_6]);
                                    var16_14.append(") is unexpected for tag: ");
                                    var16_14.append(var15_13.name);
                                    Log.w((String)"ExifInterface", (String)var16_14.toString());
                                    break block34;
                                }
                                var3_5 = var6_6;
                                if (var6_6 == 7) {
                                    var3_5 = var15_13.primaryFormat;
                                }
                                var11_11 = var10_10;
                                if ((var11_11 = (long)ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[var3_5] * var11_11) >= 0L && var11_11 <= Integer.MAX_VALUE) {
                                    var7_7 = 1;
                                } else {
                                    var16_14 = new StringBuilder();
                                    var16_14.append("Skip the tag entry since the number of components is invalid: ");
                                    var16_14.append(var10_10);
                                    Log.w((String)"ExifInterface", (String)var16_14.toString());
                                }
                                break block37;
                            }
                            var16_14 = new StringBuilder();
                            var16_14.append("Skip the tag entry since data format is invalid: ");
                            var16_14.append(var6_6);
                            Log.w((String)"ExifInterface", (String)var16_14.toString());
                        }
                        var11_11 = 0L;
                        var3_5 = var6_6;
                    }
                    if (var7_7 != 0) break block38;
                    var1_1.seek(var13_12);
                    break block39;
                }
                if (var11_11 <= 4L) ** GOTO lbl87
                var6_6 = var1_1.readInt();
                var7_7 = this.mMimeType;
                if (var7_7 == 7) {
                    if ("MakerNote".equals(var15_13.name)) {
                        this.mOrfMakerNoteOffset = var6_6;
                    } else if (var8_8 == 6 && "ThumbnailImage".equals(var15_13.name)) {
                        this.mOrfThumbnailOffset = var6_6;
                        this.mOrfThumbnailLength = var10_10;
                        var16_14 = ExifAttribute.createUShort(6, this.mExifByteOrder);
                        var17_15 = ExifAttribute.createULong(this.mOrfThumbnailOffset, this.mExifByteOrder);
                        var18_16 = ExifAttribute.createULong(this.mOrfThumbnailLength, this.mExifByteOrder);
                        this.mAttributes[4].put("Compression", (ExifAttribute)var16_14);
                        this.mAttributes[4].put("JPEGInterchangeFormat", var17_15);
                        this.mAttributes[4].put("JPEGInterchangeFormatLength", var18_16);
                    }
                } else if (var7_7 == 10 && "JpgFromRaw".equals(var15_13.name)) {
                    this.mRw2JpgFromRawOffset = var6_6;
                }
                if ((long)var6_6 + var11_11 > (long)var1_1.mLength) {
                    var15_13 = new StringBuilder();
                    var15_13.append("Skip the tag entry since data offset is invalid: ");
                    var15_13.append(var6_6);
                    Log.w((String)"ExifInterface", (String)var15_13.toString());
                    var1_1.seek(var13_12);
                } else {
                    var1_1.seek(var6_6);
lbl87: // 2 sources:
                    if ((var16_14 = ExifInterface.sExifPointerTagMap.get(var9_9)) != null) {
                        var11_11 = -1L;
                        if (var3_5 != 3) {
                            if (var3_5 != 4) {
                                if (var3_5 != 8) {
                                    if (var3_5 == 9 || var3_5 == 13) {
                                        var11_11 = var1_1.readInt();
                                    }
                                } else {
                                    var11_11 = var1_1.readShort();
                                }
                            } else {
                                var11_11 = var1_1.readUnsignedInt();
                            }
                        } else {
                            var11_11 = var1_1.readUnsignedShort();
                        }
                        if (var11_11 > 0L && var11_11 < (long)var1_1.mLength) {
                            if (!this.mAttributesOffsets.contains((int)var11_11)) {
                                var1_1.seek(var11_11);
                                this.readImageFileDirectory((ByteOrderedDataInputStream)var1_1, var16_14.intValue());
                            } else {
                                var15_13 = new StringBuilder();
                                var15_13.append("Skip jump into the IFD since it has already been read: IfdType ");
                                var15_13.append(var16_14);
                                var15_13.append(" (at ");
                                var15_13.append(var11_11);
                                var15_13.append(")");
                                Log.w((String)"ExifInterface", (String)var15_13.toString());
                            }
                        } else {
                            var15_13 = new StringBuilder();
                            var15_13.append("Skip jump into the IFD since its offset is invalid: ");
                            var15_13.append(var11_11);
                            Log.w((String)"ExifInterface", (String)var15_13.toString());
                        }
                        var1_1.seek(var13_12);
                    } else {
                        var16_14 = new byte[(int)var11_11];
                        var1_1.readFully((byte[])var16_14);
                        var16_14 = new ExifAttribute(var3_5, var10_10, (byte[])var16_14);
                        this.mAttributes[var2_2].put(var15_13.name, (ExifAttribute)var16_14);
                        if ("DNGVersion".equals(var15_13.name)) {
                            this.mMimeType = 3;
                        }
                        if (("Make".equals(var15_13.name) || "Model".equals(var15_13.name)) && var16_14.getStringValue(this.mExifByteOrder).contains("PENTAX") || "Compression".equals(var15_13.name) && var16_14.getIntValue(this.mExifByteOrder) == 65535) {
                            this.mMimeType = 8;
                        }
                        if ((long)var1_1.peek() != var13_12) {
                            var1_1.seek(var13_12);
                        }
                    }
                }
            }
            var5_4 = (short)(var5_4 + 1);
        } while (true);
        if (var1_1.peek() + 4 > var1_1.mLength) return;
        var2_2 = var1_1.readInt();
        if ((long)var2_2 > 0L && var2_2 < var1_1.mLength) {
            if (this.mAttributesOffsets.contains(var2_2)) {
                var1_1 = new StringBuilder();
                var1_1.append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
                var1_1.append(var2_2);
                Log.w((String)"ExifInterface", (String)var1_1.toString());
                return;
            }
            var1_1.seek(var2_2);
            if (this.mAttributes[4].isEmpty()) {
                this.readImageFileDirectory((ByteOrderedDataInputStream)var1_1, 4);
                return;
            }
            if (this.mAttributes[5].isEmpty() == false) return;
            this.readImageFileDirectory((ByteOrderedDataInputStream)var1_1, 5);
            return;
        }
        var1_1 = new StringBuilder();
        var1_1.append("Stop reading file since a wrong offset may cause an infinite loop: ");
        var1_1.append(var2_2);
        Log.w((String)"ExifInterface", (String)var1_1.toString());
    }

    private void removeAttribute(String string2) {
        for (int i = 0; i < EXIF_TAGS.length; ++i) {
            this.mAttributes[i].remove(string2);
        }
    }

    private void retrieveJpegImageSize(ByteOrderedDataInputStream byteOrderedDataInputStream, int n) throws IOException {
        ExifAttribute exifAttribute = this.mAttributes[n].get("ImageLength");
        ExifAttribute exifAttribute2 = this.mAttributes[n].get("ImageWidth");
        if ((exifAttribute == null || exifAttribute2 == null) && (exifAttribute = this.mAttributes[n].get("JPEGInterchangeFormat")) != null) {
            this.getJpegAttributes(byteOrderedDataInputStream, exifAttribute.getIntValue(this.mExifByteOrder), n);
        }
    }

    private void saveJpegAttributes(InputStream inputStream, OutputStream outputStream) throws IOException {
        inputStream = new DataInputStream(inputStream);
        outputStream = new ByteOrderedDataOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        if (inputStream.readByte() == -1) {
            outputStream.writeByte(-1);
            if (inputStream.readByte() == -40) {
                outputStream.writeByte(-40);
                outputStream.writeByte(-1);
                outputStream.writeByte(-31);
                this.writeExifSegment((ByteOrderedDataOutputStream)outputStream, 6);
                byte[] arrby = new byte[4096];
                while (inputStream.readByte() == -1) {
                    int n = inputStream.readByte();
                    if (n != -39 && n != -38) {
                        int n2;
                        if (n != -31) {
                            outputStream.writeByte(-1);
                            outputStream.writeByte(n);
                            n = inputStream.readUnsignedShort();
                            outputStream.writeUnsignedShort(n);
                            if ((n -= 2) >= 0) {
                                while (n > 0 && (n2 = inputStream.read(arrby, 0, Math.min(n, arrby.length))) >= 0) {
                                    outputStream.write(arrby, 0, n2);
                                    n -= n2;
                                }
                                continue;
                            }
                            throw new IOException("Invalid length");
                        }
                        n2 = inputStream.readUnsignedShort() - 2;
                        if (n2 >= 0) {
                            byte[] arrby2 = new byte[6];
                            if (n2 >= 6) {
                                if (inputStream.read(arrby2) == 6) {
                                    if (Arrays.equals(arrby2, IDENTIFIER_EXIF_APP1)) {
                                        if (inputStream.skipBytes(n2 - 6) == n2 - 6) continue;
                                        throw new IOException("Invalid length");
                                    }
                                } else {
                                    throw new IOException("Invalid exif");
                                }
                            }
                            outputStream.writeByte(-1);
                            outputStream.writeByte(n);
                            outputStream.writeUnsignedShort(n2 + 2);
                            n = n2;
                            if (n2 >= 6) {
                                n = n2 - 6;
                                outputStream.write(arrby2);
                            }
                            while (n > 0 && (n2 = inputStream.read(arrby, 0, Math.min(n, arrby.length))) >= 0) {
                                outputStream.write(arrby, 0, n2);
                                n -= n2;
                            }
                            continue;
                        }
                        throw new IOException("Invalid length");
                    }
                    outputStream.writeByte(-1);
                    outputStream.writeByte(n);
                    ExifInterface.copy(inputStream, outputStream);
                    return;
                }
                throw new IOException("Invalid marker");
            }
            throw new IOException("Invalid marker");
        }
        throw new IOException("Invalid marker");
    }

    private void setThumbnailData(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        HashMap<String, ExifAttribute> hashMap;
        block1 : {
            block4 : {
                block2 : {
                    block3 : {
                        int n;
                        hashMap = this.mAttributes[4];
                        ExifAttribute exifAttribute = hashMap.get("Compression");
                        if (exifAttribute == null) break block1;
                        this.mThumbnailCompression = n = exifAttribute.getIntValue(this.mExifByteOrder);
                        if (n == 1) break block2;
                        if (n == 6) break block3;
                        if (n == 7) break block2;
                        break block4;
                    }
                    this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
                    break block4;
                }
                if (this.isSupportedDataType(hashMap)) {
                    this.handleThumbnailFromStrips(byteOrderedDataInputStream, hashMap);
                }
            }
            return;
        }
        this.mThumbnailCompression = 6;
        this.handleThumbnailFromJfif(byteOrderedDataInputStream, hashMap);
    }

    private void swapBasedOnImageSize(int n, int n2) throws IOException {
        if (!this.mAttributes[n].isEmpty()) {
            if (this.mAttributes[n2].isEmpty()) {
                return;
            }
            HashMap<String, ExifAttribute>[] arrhashMap = this.mAttributes[n].get("ImageLength");
            Object object = this.mAttributes[n].get("ImageWidth");
            ExifAttribute exifAttribute = this.mAttributes[n2].get("ImageLength");
            ExifAttribute exifAttribute2 = this.mAttributes[n2].get("ImageWidth");
            if (arrhashMap != null) {
                if (object == null) {
                    return;
                }
                if (exifAttribute != null) {
                    if (exifAttribute2 == null) {
                        return;
                    }
                    int n3 = arrhashMap.getIntValue(this.mExifByteOrder);
                    int n4 = object.getIntValue(this.mExifByteOrder);
                    int n5 = exifAttribute.getIntValue(this.mExifByteOrder);
                    int n6 = exifAttribute2.getIntValue(this.mExifByteOrder);
                    if (n3 < n5 && n4 < n6) {
                        arrhashMap = this.mAttributes;
                        object = arrhashMap[n];
                        arrhashMap[n] = arrhashMap[n2];
                        arrhashMap[n2] = object;
                    }
                }
            }
            return;
        }
    }

    private boolean updateAttribute(String string2, ExifAttribute exifAttribute) {
        boolean bl = false;
        for (int i = 0; i < EXIF_TAGS.length; ++i) {
            if (!this.mAttributes[i].containsKey(string2)) continue;
            this.mAttributes[i].put(string2, exifAttribute);
            bl = true;
        }
        return bl;
    }

    private void updateImageSizeValues(ByteOrderedDataInputStream arrrational, int n) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3;
        ExifAttribute exifAttribute4;
        Object object;
        block5 : {
            block9 : {
                block8 : {
                    block6 : {
                        block7 : {
                            object = this.mAttributes[n].get("DefaultCropSize");
                            exifAttribute3 = this.mAttributes[n].get("SensorTopBorder");
                            exifAttribute4 = this.mAttributes[n].get("SensorLeftBorder");
                            exifAttribute = this.mAttributes[n].get("SensorBottomBorder");
                            exifAttribute2 = this.mAttributes[n].get("SensorRightBorder");
                            if (object == null) break block5;
                            if (object.format != 5) break block6;
                            arrrational = (Rational[])object.getValue(this.mExifByteOrder);
                            if (arrrational == null || arrrational.length != 2) break block7;
                            object = ExifAttribute.createURational(arrrational[0], this.mExifByteOrder);
                            arrrational = ExifAttribute.createURational((Rational)arrrational[1], this.mExifByteOrder);
                            break block8;
                        }
                        object = new StringBuilder();
                        object.append("Invalid crop size values. cropSize=");
                        object.append(Arrays.toString(arrrational));
                        Log.w((String)"ExifInterface", (String)object.toString());
                        return;
                    }
                    arrrational = (int[])object.getValue(this.mExifByteOrder);
                    if (arrrational == null || arrrational.length != 2) break block9;
                    object = ExifAttribute.createUShort((int)arrrational[0], this.mExifByteOrder);
                    arrrational = ExifAttribute.createUShort((int)arrrational[1], this.mExifByteOrder);
                }
                this.mAttributes[n].put("ImageWidth", (ExifAttribute)object);
                this.mAttributes[n].put("ImageLength", (ExifAttribute)arrrational);
                return;
            }
            object = new StringBuilder();
            object.append("Invalid crop size values. cropSize=");
            object.append(Arrays.toString((int[])arrrational));
            Log.w((String)"ExifInterface", (String)object.toString());
            return;
        }
        if (exifAttribute3 != null && exifAttribute4 != null && exifAttribute != null && exifAttribute2 != null) {
            int n2 = exifAttribute3.getIntValue(this.mExifByteOrder);
            int n3 = exifAttribute.getIntValue(this.mExifByteOrder);
            int n4 = exifAttribute2.getIntValue(this.mExifByteOrder);
            int n5 = exifAttribute4.getIntValue(this.mExifByteOrder);
            if (n3 > n2 && n4 > n5) {
                arrrational = ExifAttribute.createUShort(n3 - n2, this.mExifByteOrder);
                object = ExifAttribute.createUShort(n4 - n5, this.mExifByteOrder);
                this.mAttributes[n].put("ImageLength", (ExifAttribute)arrrational);
                this.mAttributes[n].put("ImageWidth", (ExifAttribute)object);
            }
            return;
        }
        this.retrieveJpegImageSize((ByteOrderedDataInputStream)arrrational, n);
    }

    private void validateImages(InputStream arrhashMap) throws IOException {
        this.swapBasedOnImageSize(0, 5);
        this.swapBasedOnImageSize(0, 4);
        this.swapBasedOnImageSize(5, 4);
        arrhashMap = this.mAttributes[1].get("PixelXDimension");
        ExifAttribute exifAttribute = this.mAttributes[1].get("PixelYDimension");
        if (arrhashMap != null && exifAttribute != null) {
            this.mAttributes[0].put("ImageWidth", (ExifAttribute)arrhashMap);
            this.mAttributes[0].put("ImageLength", exifAttribute);
        }
        if (this.mAttributes[4].isEmpty() && this.isThumbnail(this.mAttributes[5])) {
            arrhashMap = this.mAttributes;
            arrhashMap[4] = arrhashMap[5];
            arrhashMap[5] = new HashMap();
        }
        if (!this.isThumbnail(this.mAttributes[4])) {
            Log.d((String)"ExifInterface", (String)"No image meets the size requirements of a thumbnail image.");
        }
    }

    private int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream, int n) throws IOException {
        int n2;
        int n3;
        int n4;
        Iterator<Map.Entry<String, ExifAttribute>> iterator = EXIF_TAGS;
        int[] arrn = new int[iterator.length];
        iterator = new int[iterator.length];
        Object object = EXIF_POINTER_TAGS;
        int n5 = object.length;
        for (n3 = 0; n3 < n5; ++n3) {
            this.removeAttribute(object[n3].name);
        }
        this.removeAttribute(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name);
        this.removeAttribute(ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);
        for (n3 = 0; n3 < EXIF_TAGS.length; ++n3) {
            object = this.mAttributes[n3].entrySet().toArray();
            n4 = object.length;
            for (n5 = 0; n5 < n4; ++n5) {
                Map.Entry entry = (Map.Entry)((Object)object[n5]);
                if (entry.getValue() != null) continue;
                this.mAttributes[n3].remove(entry.getKey());
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(ExifInterface.EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(0L, this.mExifByteOrder));
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifAttribute.createULong(this.mThumbnailLength, this.mExifByteOrder));
        }
        for (n3 = 0; n3 < EXIF_TAGS.length; ++n3) {
            n5 = 0;
            object = this.mAttributes[n3].entrySet().iterator();
            while (object.hasNext()) {
                n2 = ((ExifAttribute)((Map.Entry)object.next()).getValue()).size();
                n4 = n5;
                if (n2 > 4) {
                    n4 = n5 + n2;
                }
                n5 = n4;
            }
            iterator[n3] = iterator[n3] + n5;
        }
        n3 = 8;
        for (n5 = 0; n5 < EXIF_TAGS.length; ++n5) {
            n4 = n3;
            if (!this.mAttributes[n5].isEmpty()) {
                arrn[n5] = n3;
                n4 = n3 + (this.mAttributes[n5].size() * 12 + 2 + 4 + iterator[n5]);
            }
            n3 = n4;
        }
        n5 = n3;
        if (this.mHasThumbnail) {
            this.mAttributes[4].put(ExifInterface.JPEG_INTERCHANGE_FORMAT_TAG.name, ExifAttribute.createULong(n3, this.mExifByteOrder));
            this.mThumbnailOffset = n + n3;
            n5 = n3 + this.mThumbnailLength;
        }
        n2 = n5 + 8;
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(arrn[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(ExifInterface.EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(arrn[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(ExifInterface.EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(arrn[3], this.mExifByteOrder));
        }
        byteOrderedDataOutputStream.writeUnsignedShort(n2);
        byteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
        short s = this.mExifByteOrder == ByteOrder.BIG_ENDIAN ? 19789 : 18761;
        byteOrderedDataOutputStream.writeShort(s);
        byteOrderedDataOutputStream.setByteOrder(this.mExifByteOrder);
        byteOrderedDataOutputStream.writeUnsignedShort(42);
        byteOrderedDataOutputStream.writeUnsignedInt(8L);
        for (n = 0; n < EXIF_TAGS.length; ++n) {
            if (this.mAttributes[n].isEmpty()) continue;
            byteOrderedDataOutputStream.writeUnsignedShort(this.mAttributes[n].size());
            n3 = arrn[n] + 2 + this.mAttributes[n].size() * 12 + 4;
            iterator = this.mAttributes[n].entrySet().iterator();
            while (iterator.hasNext()) {
                object = (Map.Entry)iterator.next();
                n5 = ExifInterface.sExifTagMapsForWriting[n].get(object.getKey()).number;
                object = (ExifAttribute)object.getValue();
                n4 = object.size();
                byteOrderedDataOutputStream.writeUnsignedShort(n5);
                byteOrderedDataOutputStream.writeUnsignedShort(object.format);
                byteOrderedDataOutputStream.writeInt(object.numberOfComponents);
                if (n4 > 4) {
                    byteOrderedDataOutputStream.writeUnsignedInt(n3);
                    n5 = n3 + n4;
                } else {
                    byteOrderedDataOutputStream.write(object.bytes);
                    n5 = n3;
                    if (n4 < 4) {
                        do {
                            n5 = n3;
                            if (n4 >= 4) break;
                            byteOrderedDataOutputStream.writeByte(0);
                            ++n4;
                        } while (true);
                    }
                }
                n3 = n5;
            }
            if (n == 0 && !this.mAttributes[4].isEmpty()) {
                byteOrderedDataOutputStream.writeUnsignedInt(arrn[4]);
            } else {
                byteOrderedDataOutputStream.writeUnsignedInt(0L);
            }
            iterator = this.mAttributes[n].entrySet().iterator();
            while (iterator.hasNext()) {
                object = iterator.next().getValue();
                if (object.bytes.length <= 4) continue;
                byteOrderedDataOutputStream.write(object.bytes, 0, object.bytes.length);
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream.write(this.getThumbnailBytes());
        }
        byteOrderedDataOutputStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        return n2;
    }

    public void flipHorizontally() {
        int n;
        switch (this.getAttributeInt("Orientation", 1)) {
            default: {
                n = 0;
                break;
            }
            case 8: {
                n = 7;
                break;
            }
            case 7: {
                n = 8;
                break;
            }
            case 6: {
                n = 5;
                break;
            }
            case 5: {
                n = 6;
                break;
            }
            case 4: {
                n = 3;
                break;
            }
            case 3: {
                n = 4;
                break;
            }
            case 2: {
                n = 1;
                break;
            }
            case 1: {
                n = 2;
            }
        }
        this.setAttribute("Orientation", Integer.toString(n));
    }

    public void flipVertically() {
        int n;
        switch (this.getAttributeInt("Orientation", 1)) {
            default: {
                n = 0;
                break;
            }
            case 8: {
                n = 5;
                break;
            }
            case 7: {
                n = 6;
                break;
            }
            case 6: {
                n = 7;
                break;
            }
            case 5: {
                n = 8;
                break;
            }
            case 4: {
                n = 1;
                break;
            }
            case 3: {
                n = 2;
                break;
            }
            case 2: {
                n = 3;
                break;
            }
            case 1: {
                n = 4;
            }
        }
        this.setAttribute("Orientation", Integer.toString(n));
    }

    public double getAltitude(double d) {
        double d2 = this.getAttributeDouble("GPSAltitude", -1.0);
        int n = -1;
        int n2 = this.getAttributeInt("GPSAltitudeRef", -1);
        if (d2 >= 0.0 && n2 >= 0) {
            if (n2 != 1) {
                n = 1;
            }
            return (double)n * d2;
        }
        return d;
    }

    public String getAttribute(String arrrational) {
        Object object = this.getExifAttribute((String)arrrational);
        if (object != null) {
            if (!sTagSetForCompatibility.contains(arrrational)) {
                return object.getStringValue(this.mExifByteOrder);
            }
            if (arrrational.equals("GPSTimeStamp")) {
                if (object.format != 5 && object.format != 10) {
                    arrrational = new StringBuilder();
                    arrrational.append("GPS Timestamp format is not rational. format=");
                    arrrational.append(object.format);
                    Log.w((String)"ExifInterface", (String)arrrational.toString());
                    return null;
                }
                arrrational = (Rational[])object.getValue(this.mExifByteOrder);
                if (arrrational != null && arrrational.length == 3) {
                    return String.format("%02d:%02d:%02d", (int)((float)arrrational[0].numerator / (float)arrrational[0].denominator), (int)((float)arrrational[1].numerator / (float)arrrational[1].denominator), (int)((float)arrrational[2].numerator / (float)arrrational[2].denominator));
                }
                object = new StringBuilder();
                object.append("Invalid GPS Timestamp array. array=");
                object.append(Arrays.toString(arrrational));
                Log.w((String)"ExifInterface", (String)object.toString());
                return null;
            }
            try {
                arrrational = Double.toString(object.getDoubleValue(this.mExifByteOrder));
                return arrrational;
            }
            catch (NumberFormatException numberFormatException) {
                return null;
            }
        }
        return null;
    }

    public double getAttributeDouble(String object, double d) {
        if ((object = this.getExifAttribute((String)object)) == null) {
            return d;
        }
        try {
            double d2 = object.getDoubleValue(this.mExifByteOrder);
            return d2;
        }
        catch (NumberFormatException numberFormatException) {
            return d;
        }
    }

    public int getAttributeInt(String object, int n) {
        if ((object = this.getExifAttribute((String)object)) == null) {
            return n;
        }
        try {
            int n2 = object.getIntValue(this.mExifByteOrder);
            return n2;
        }
        catch (NumberFormatException numberFormatException) {
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long getDateTime() {
        Object object = this.getAttribute("DateTime");
        if (object == null) return -1L;
        if (!sNonZeroTimePattern.matcher((CharSequence)object).matches()) {
            return -1L;
        }
        ParsePosition parsePosition = new ParsePosition(0);
        try {
            object = sFormatter.parse((String)object, parsePosition);
            if (object == null) {
                return -1L;
            }
            long l = object.getTime();
            object = this.getAttribute("SubSecTime");
            if (object == null) return l;
            try {
                long l2 = Long.parseLong((String)object);
                while (l2 > 1000L) {
                    l2 /= 10L;
                }
                return l + l2;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            return l;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return -1L;
        }
    }

    public long getGpsDateTime() {
        Object object = this.getAttribute("GPSDateStamp");
        Object object2 = this.getAttribute("GPSTimeStamp");
        if (object != null && object2 != null) {
            block5 : {
                if (!sNonZeroTimePattern.matcher((CharSequence)object).matches() && !sNonZeroTimePattern.matcher((CharSequence)object2).matches()) {
                    return -1L;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append((String)object);
                stringBuilder.append(' ');
                stringBuilder.append((String)object2);
                object = stringBuilder.toString();
                object2 = new ParsePosition(0);
                try {
                    object = sFormatter.parse((String)object, (ParsePosition)object2);
                    if (object != null) break block5;
                    return -1L;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    return -1L;
                }
            }
            long l = object.getTime();
            return l;
        }
        return -1L;
    }

    @Deprecated
    public boolean getLatLong(float[] arrf) {
        double[] arrd = this.getLatLong();
        if (arrd == null) {
            return false;
        }
        arrf[0] = (float)arrd[0];
        arrf[1] = (float)arrd[1];
        return true;
    }

    public double[] getLatLong() {
        String string2 = this.getAttribute("GPSLatitude");
        String string3 = this.getAttribute("GPSLatitudeRef");
        String string4 = this.getAttribute("GPSLongitude");
        String string5 = this.getAttribute("GPSLongitudeRef");
        if (string2 != null && string3 != null && string4 != null && string5 != null) {
            double d;
            double d2;
            try {
                d = ExifInterface.convertRationalLatLonToDouble(string2, string3);
                d2 = ExifInterface.convertRationalLatLonToDouble(string4, string5);
            }
            catch (IllegalArgumentException illegalArgumentException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Latitude/longitude values are not parseable. ");
                stringBuilder.append(String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", string2, string3, string4, string5));
                Log.w((String)"ExifInterface", (String)stringBuilder.toString());
            }
            return new double[]{d, d2};
        }
        return null;
    }

    public int getRotationDegrees() {
        switch (this.getAttributeInt("Orientation", 1)) {
            default: {
                return 0;
            }
            case 6: 
            case 7: {
                return 90;
            }
            case 5: 
            case 8: {
                return 270;
            }
            case 3: 
            case 4: 
        }
        return 180;
    }

    public byte[] getThumbnail() {
        int n = this.mThumbnailCompression;
        if (n != 6 && n != 7) {
            return null;
        }
        return this.getThumbnailBytes();
    }

    public Bitmap getThumbnailBitmap() {
        int n;
        if (!this.mHasThumbnail) {
            return null;
        }
        if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = this.getThumbnailBytes();
        }
        if ((n = this.mThumbnailCompression) != 6 && n != 7) {
            if (n == 1) {
                Object object;
                int[] arrn = new int[this.mThumbnailBytes.length / 3];
                for (n = 0; n < arrn.length; ++n) {
                    object = this.mThumbnailBytes;
                    arrn[n] = (object[n * 3] << 16) + 0 + (object[n * 3 + 1] << 8) + object[n * 3 + 2];
                }
                object = this.mAttributes[4].get("ImageLength");
                ExifAttribute exifAttribute = this.mAttributes[4].get("ImageWidth");
                if (object != null && exifAttribute != null) {
                    n = object.getIntValue(this.mExifByteOrder);
                    return Bitmap.createBitmap((int[])arrn, (int)exifAttribute.getIntValue(this.mExifByteOrder), (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                }
            }
            return null;
        }
        return BitmapFactory.decodeByteArray((byte[])this.mThumbnailBytes, (int)0, (int)this.mThumbnailLength);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] getThumbnailBytes() {
        block11 : {
            block10 : {
                block9 : {
                    block12 : {
                        if (!this.mHasThumbnail) {
                            return null;
                        }
                        var4_1 = this.mThumbnailBytes;
                        if (var4_1 != null) {
                            return var4_1;
                        }
                        var7_4 = null;
                        var8_5 = null;
                        var4_1 = null;
                        var5_6 = var7_4;
                        var6_7 = var8_5;
                        if (this.mAssetInputStream == null) break block9;
                        var5_6 = var7_4;
                        var6_7 = var8_5;
                        var5_6 = var4_1 = this.mAssetInputStream;
                        var6_7 = var4_1;
                        if (!var4_1.markSupported()) break block12;
                        var5_6 = var4_1;
                        var6_7 = var4_1;
                        var4_1.reset();
                        ** GOTO lbl36
                    }
                    var5_6 = var4_1;
                    var6_7 = var4_1;
                    Log.d((String)"ExifInterface", (String)"Cannot read thumbnail from inputstream without mark/reset support");
                    ExifInterface.closeQuietly((Closeable)var4_1);
                    return null;
                }
                var5_6 = var7_4;
                var6_7 = var8_5;
                if (this.mFilename != null) {
                    var5_6 = var7_4;
                    var6_7 = var8_5;
                    var4_1 = new FileInputStream(this.mFilename);
                }
lbl36: // 4 sources:
                if (var4_1 == null) ** GOTO lbl64
                var5_6 = var4_1;
                var6_7 = var4_1;
                var2_8 = var4_1.skip(this.mThumbnailOffset);
                var5_6 = var4_1;
                var6_7 = var4_1;
                var1_9 = this.mThumbnailOffset;
                if (var2_8 != (long)var1_9) ** GOTO lbl61
                var5_6 = var4_1;
                var6_7 = var4_1;
                var7_4 = new byte[this.mThumbnailLength];
                var5_6 = var4_1;
                var6_7 = var4_1;
                if (var4_1.read(var7_4) != this.mThumbnailLength) break block10;
                var5_6 = var4_1;
                var6_7 = var4_1;
                this.mThumbnailBytes = var7_4;
                ExifInterface.closeQuietly((Closeable)var4_1);
                return var7_4;
            }
            var5_6 = var4_1;
            var6_7 = var4_1;
            try {
                throw new IOException("Corrupted image");
lbl61: // 1 sources:
                var5_6 = var4_1;
                var6_7 = var4_1;
                throw new IOException("Corrupted image");
lbl64: // 1 sources:
                var5_6 = var4_1;
                var6_7 = var4_1;
                throw new FileNotFoundException();
            }
            catch (Throwable var4_2) {
                break block11;
            }
            catch (IOException var4_3) {
                var5_6 = var6_7;
                Log.d((String)"ExifInterface", (String)"Encountered exception while getting thumbnail", (Throwable)var4_3);
                ExifInterface.closeQuietly((Closeable)var6_7);
                return null;
            }
        }
        ExifInterface.closeQuietly((Closeable)var5_6);
        throw var4_2;
    }

    public long[] getThumbnailRange() {
        if (!this.mHasThumbnail) {
            return null;
        }
        return new long[]{this.mThumbnailOffset, this.mThumbnailLength};
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean isFlipped() {
        int n = this.getAttributeInt("Orientation", 1);
        if (n != 2 && n != 7 && n != 4 && n != 5) {
            return false;
        }
        return true;
    }

    public boolean isThumbnailCompressed() {
        int n = this.mThumbnailCompression;
        if (n != 6 && n != 7) {
            return false;
        }
        return true;
    }

    public void resetOrientation() {
        this.setAttribute("Orientation", Integer.toString(1));
    }

    public void rotate(int n) {
        if (n % 90 == 0) {
            int n2 = this.getAttributeInt("Orientation", 1);
            boolean bl = ROTATION_ORDER.contains(n2);
            int n3 = 0;
            int n4 = 0;
            if (bl) {
                n3 = ROTATION_ORDER.indexOf(n2);
                n3 = (n / 90 + n3) % 4;
                n = n4;
                if (n3 < 0) {
                    n = 4;
                }
                n = ROTATION_ORDER.get(n3 + n);
            } else if (FLIPPED_ROTATION_ORDER.contains(n2)) {
                n4 = FLIPPED_ROTATION_ORDER.indexOf(n2);
                n4 = (n / 90 + n4) % 4;
                n = n3;
                if (n4 < 0) {
                    n = 4;
                }
                n = FLIPPED_ROTATION_ORDER.get(n4 + n);
            } else {
                n = 0;
            }
            this.setAttribute("Orientation", Integer.toString(n));
            return;
        }
        throw new IllegalArgumentException("degree should be a multiple of 90");
    }

    public void saveAttributes() throws IOException {
        if (this.mIsSupportedFile && this.mMimeType == 4) {
            if (this.mFilename != null) {
                this.mThumbnailBytes = this.getThumbnail();
                Object object = new StringBuilder();
                object.append(this.mFilename);
                object.append(".tmp");
                File file = new File(object.toString());
                if (new File(this.mFilename).renameTo(file)) {
                    FileOutputStream fileOutputStream;
                    FileInputStream fileInputStream;
                    object = null;
                    FileOutputStream fileOutputStream2 = fileOutputStream = null;
                    try {
                        fileInputStream = new FileInputStream(file);
                        object = fileInputStream;
                        fileOutputStream2 = fileOutputStream;
                    }
                    catch (Throwable throwable) {
                        ExifInterface.closeQuietly((Closeable)object);
                        ExifInterface.closeQuietly(fileOutputStream2);
                        file.delete();
                        throw throwable;
                    }
                    fileOutputStream = new FileOutputStream(this.mFilename);
                    object = fileInputStream;
                    fileOutputStream2 = fileOutputStream;
                    this.saveJpegAttributes(fileInputStream, fileOutputStream);
                    ExifInterface.closeQuietly(fileInputStream);
                    ExifInterface.closeQuietly(fileOutputStream);
                    file.delete();
                    this.mThumbnailBytes = null;
                    return;
                }
                object = new StringBuilder();
                object.append("Could not rename to ");
                object.append(file.getAbsolutePath());
                throw new IOException(object.toString());
            }
            throw new IOException("ExifInterface does not support saving attributes for the current input.");
        }
        throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
    }

    public void setAltitude(double d) {
        String string2 = d >= 0.0 ? "0" : "1";
        this.setAttribute("GPSAltitude", new Rational(Math.abs(d)).toString());
        this.setAttribute("GPSAltitudeRef", string2);
    }

    public void setAttribute(String object, String object2) {
        Object object3 = object2;
        Pair<Integer, Integer> pair = object;
        if ("ISOSpeedRatings".equals(object)) {
            pair = "PhotographicSensitivity";
        }
        object = object3;
        if (object3 != null) {
            object = object3;
            if (sTagSetForCompatibility.contains(pair)) {
                if (pair.equals((Object)"GPSTimeStamp")) {
                    object = sGpsTimestampPattern.matcher((CharSequence)object3);
                    if (!object.find()) {
                        object = new StringBuilder();
                        object.append("Invalid value for ");
                        object.append((String)pair);
                        object.append(" : ");
                        object.append((String)object3);
                        Log.w((String)"ExifInterface", (String)object.toString());
                        return;
                    }
                    object2 = new StringBuilder();
                    object2.append(Integer.parseInt(object.group(1)));
                    object2.append("/1,");
                    object2.append(Integer.parseInt(object.group(2)));
                    object2.append("/1,");
                    object2.append(Integer.parseInt(object.group(3)));
                    object2.append("/1");
                    object = object2.toString();
                } else {
                    try {
                        object = new Rational(Double.parseDouble(object2)).toString();
                    }
                    catch (NumberFormatException numberFormatException) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid value for ");
                        stringBuilder.append((String)pair);
                        stringBuilder.append(" : ");
                        stringBuilder.append((String)object3);
                        Log.w((String)"ExifInterface", (String)stringBuilder.toString());
                        return;
                    }
                }
            }
        }
        block12 : for (int i = 0; i < EXIF_TAGS.length; ++i) {
            String[] arrstring;
            int n;
            Object object4;
            Object object5;
            if (i == 4 && !this.mHasThumbnail || (arrstring = sExifTagMapsForWriting[i].get(pair)) == null) continue;
            if (object == null) {
                this.mAttributes[i].remove(pair);
                continue;
            }
            object3 = ExifInterface.guessDataFormat((String)object);
            if (arrstring.primaryFormat != (Integer)object3.first && arrstring.primaryFormat != (Integer)object3.second) {
                if (arrstring.secondaryFormat != -1 && (arrstring.secondaryFormat == (Integer)object3.first || arrstring.secondaryFormat == (Integer)object3.second)) {
                    n = arrstring.secondaryFormat;
                } else {
                    if (arrstring.primaryFormat != 1 && arrstring.primaryFormat != 7 && arrstring.primaryFormat != 2) {
                        object4 = new StringBuilder();
                        object4.append("Given tag (");
                        object4.append((String)pair);
                        object4.append(") value didn't match with one of expected ");
                        object4.append("formats: ");
                        object4.append(IFD_FORMAT_NAMES[arrstring.primaryFormat]);
                        n = arrstring.secondaryFormat;
                        object5 = "";
                        if (n == -1) {
                            object2 = "";
                        } else {
                            object2 = new StringBuilder();
                            object2.append(", ");
                            object2.append(IFD_FORMAT_NAMES[arrstring.secondaryFormat]);
                            object2 = object2.toString();
                        }
                        object4.append((String)object2);
                        object4.append(" (guess: ");
                        object4.append(IFD_FORMAT_NAMES[(Integer)object3.first]);
                        if ((Integer)object3.second == -1) {
                            object2 = object5;
                        } else {
                            object2 = new StringBuilder();
                            object2.append(", ");
                            object2.append(IFD_FORMAT_NAMES[(Integer)object3.second]);
                            object2 = object2.toString();
                        }
                        object4.append((String)object2);
                        object4.append(")");
                        Log.w((String)"ExifInterface", (String)object4.toString());
                        continue;
                    }
                    n = arrstring.primaryFormat;
                }
            } else {
                n = arrstring.primaryFormat;
            }
            switch (n) {
                default: {
                    object2 = new StringBuilder();
                    object2.append("Data format isn't one of expected formats: ");
                    object2.append(n);
                    Log.w((String)"ExifInterface", (String)object2.toString());
                    continue block12;
                }
                case 12: {
                    object2 = object.split(",", -1);
                    object3 = new double[object2.length];
                    for (n = 0; n < object2.length; ++n) {
                        object3[n] = Double.parseDouble(object2[n]);
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createDouble((double[])object3, this.mExifByteOrder));
                    continue block12;
                }
                case 10: {
                    object2 = object.split(",", -1);
                    object3 = new Rational[object2.length];
                    for (n = 0; n < object2.length; ++n) {
                        arrstring = object2[n].split("/", -1);
                        object3[n] = new Rational((long)Double.parseDouble(arrstring[0]), (long)Double.parseDouble(arrstring[1]));
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createSRational((Rational[])object3, this.mExifByteOrder));
                    continue block12;
                }
                case 9: {
                    object2 = object.split(",", -1);
                    object3 = new int[object2.length];
                    for (n = 0; n < object2.length; ++n) {
                        object3[n] = Integer.parseInt(object2[n]);
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createSLong((int[])object3, this.mExifByteOrder));
                    continue block12;
                }
                case 5: {
                    object5 = object.split(",", -1);
                    object4 = new Rational[object5.length];
                    object2 = object3;
                    object3 = arrstring;
                    for (n = 0; n < object5.length; ++n) {
                        arrstring = object5[n].split("/", -1);
                        object4[n] = new Rational((long)Double.parseDouble(arrstring[0]), (long)Double.parseDouble(arrstring[1]));
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createURational((Rational[])object4, this.mExifByteOrder));
                    continue block12;
                }
                case 4: {
                    object2 = object.split(",", -1);
                    object3 = new long[object2.length];
                    for (n = 0; n < object2.length; ++n) {
                        object3[n] = Long.parseLong(object2[n]);
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createULong((long[])object3, this.mExifByteOrder));
                    continue block12;
                }
                case 3: {
                    object2 = object.split(",", -1);
                    object3 = new int[object2.length];
                    for (n = 0; n < object2.length; ++n) {
                        object3[n] = Integer.parseInt(object2[n]);
                    }
                    this.mAttributes[i].put((String)pair, ExifAttribute.createUShort((int[])object3, this.mExifByteOrder));
                    continue block12;
                }
                case 2: 
                case 7: {
                    this.mAttributes[i].put((String)pair, ExifAttribute.createString((String)object));
                    continue block12;
                }
                case 1: {
                    this.mAttributes[i].put((String)pair, ExifAttribute.createByte((String)object));
                    continue block12;
                }
            }
        }
    }

    public void setDateTime(long l) {
        this.setAttribute("DateTime", sFormatter.format(new Date(l)));
        this.setAttribute("SubSecTime", Long.toString(l % 1000L));
    }

    public void setGpsInfo(Location arrstring) {
        if (arrstring == null) {
            return;
        }
        this.setAttribute("GPSProcessingMethod", arrstring.getProvider());
        this.setLatLong(arrstring.getLatitude(), arrstring.getLongitude());
        this.setAltitude(arrstring.getAltitude());
        this.setAttribute("GPSSpeedRef", "K");
        this.setAttribute("GPSSpeed", new Rational(arrstring.getSpeed() * (float)TimeUnit.HOURS.toSeconds(1L) / 1000.0f).toString());
        arrstring = sFormatter.format(new Date(arrstring.getTime())).split("\\s+", -1);
        this.setAttribute("GPSDateStamp", arrstring[0]);
        this.setAttribute("GPSTimeStamp", arrstring[1]);
    }

    public void setLatLong(double d, double d2) {
        if (d >= -90.0 && d <= 90.0 && !Double.isNaN(d)) {
            if (d2 >= -180.0 && d2 <= 180.0 && !Double.isNaN(d2)) {
                String string2 = d >= 0.0 ? "N" : "S";
                this.setAttribute("GPSLatitudeRef", string2);
                this.setAttribute("GPSLatitude", this.convertDecimalDegree(Math.abs(d)));
                string2 = d2 >= 0.0 ? "E" : "W";
                this.setAttribute("GPSLongitudeRef", string2);
                this.setAttribute("GPSLongitude", this.convertDecimalDegree(Math.abs(d2)));
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Longitude value ");
            stringBuilder.append(d2);
            stringBuilder.append(" is not valid.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Latitude value ");
        stringBuilder.append(d);
        stringBuilder.append(" is not valid.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static class ByteOrderedDataInputStream
    extends InputStream
    implements DataInput {
        private static final ByteOrder BIG_ENDIAN;
        private static final ByteOrder LITTLE_ENDIAN;
        private ByteOrder mByteOrder = ByteOrder.BIG_ENDIAN;
        private DataInputStream mDataInputStream;
        final int mLength;
        int mPosition;

        static {
            LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
            BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        }

        public ByteOrderedDataInputStream(InputStream inputStream) throws IOException {
            int n;
            inputStream = new DataInputStream(inputStream);
            this.mDataInputStream = inputStream;
            this.mLength = n = inputStream.available();
            this.mPosition = 0;
            this.mDataInputStream.mark(n);
        }

        public ByteOrderedDataInputStream(byte[] arrby) throws IOException {
            this(new ByteArrayInputStream(arrby));
        }

        @Override
        public int available() throws IOException {
            return this.mDataInputStream.available();
        }

        public int peek() {
            return this.mPosition;
        }

        @Override
        public int read() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.read();
        }

        @Override
        public int read(byte[] arrby, int n, int n2) throws IOException {
            n = this.mDataInputStream.read(arrby, n, n2);
            this.mPosition += n;
            return n;
        }

        @Override
        public boolean readBoolean() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readBoolean();
        }

        @Override
        public byte readByte() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 1;
            if (n <= this.mLength) {
                n = this.mDataInputStream.read();
                if (n >= 0) {
                    return (byte)n;
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        @Override
        public double readDouble() throws IOException {
            return Double.longBitsToDouble(this.readLong());
        }

        @Override
        public float readFloat() throws IOException {
            return Float.intBitsToFloat(this.readInt());
        }

        @Override
        public void readFully(byte[] arrby) throws IOException {
            int n;
            this.mPosition = n = this.mPosition + arrby.length;
            if (n <= this.mLength) {
                if (this.mDataInputStream.read(arrby, 0, arrby.length) == arrby.length) {
                    return;
                }
                throw new IOException("Couldn't read up to the length of buffer");
            }
            throw new EOFException();
        }

        @Override
        public void readFully(byte[] arrby, int n, int n2) throws IOException {
            int n3;
            this.mPosition = n3 = this.mPosition + n2;
            if (n3 <= this.mLength) {
                if (this.mDataInputStream.read(arrby, n, n2) == n2) {
                    return;
                }
                throw new IOException("Couldn't read up to the length of buffer");
            }
            throw new EOFException();
        }

        @Override
        public int readInt() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 4;
            if (n <= this.mLength) {
                int n2;
                int n3;
                int n4;
                n = this.mDataInputStream.read();
                if ((n | (n3 = this.mDataInputStream.read()) | (n2 = this.mDataInputStream.read()) | (n4 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (n4 << 24) + (n2 << 16) + (n3 << 8) + n;
                    }
                    if (object == BIG_ENDIAN) {
                        return (n << 24) + (n3 << 16) + (n2 << 8) + n4;
                    }
                    object = new StringBuilder();
                    object.append("Invalid byte order: ");
                    object.append(this.mByteOrder);
                    throw new IOException(object.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public String readLine() throws IOException {
            Log.d((String)"ExifInterface", (String)"Currently unsupported");
            return null;
        }

        @Override
        public long readLong() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 8;
            if (n <= this.mLength) {
                int n2;
                int n3;
                int n4;
                int n5;
                int n6;
                int n7;
                int n8;
                n = this.mDataInputStream.read();
                if ((n | (n4 = this.mDataInputStream.read()) | (n2 = this.mDataInputStream.read()) | (n7 = this.mDataInputStream.read()) | (n5 = this.mDataInputStream.read()) | (n8 = this.mDataInputStream.read()) | (n3 = this.mDataInputStream.read()) | (n6 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return ((long)n6 << 56) + ((long)n3 << 48) + ((long)n8 << 40) + ((long)n5 << 32) + ((long)n7 << 24) + ((long)n2 << 16) + ((long)n4 << 8) + (long)n;
                    }
                    if (object == BIG_ENDIAN) {
                        return ((long)n << 56) + ((long)n4 << 48) + ((long)n2 << 40) + ((long)n7 << 32) + ((long)n5 << 24) + ((long)n8 << 16) + ((long)n3 << 8) + (long)n6;
                    }
                    object = new StringBuilder();
                    object.append("Invalid byte order: ");
                    object.append(this.mByteOrder);
                    throw new IOException(object.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public short readShort() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 2;
            if (n <= this.mLength) {
                int n2;
                n = this.mDataInputStream.read();
                if ((n | (n2 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (short)((n2 << 8) + n);
                    }
                    if (object == BIG_ENDIAN) {
                        return (short)((n << 8) + n2);
                    }
                    object = new StringBuilder();
                    object.append("Invalid byte order: ");
                    object.append(this.mByteOrder);
                    throw new IOException(object.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        @Override
        public String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        @Override
        public int readUnsignedByte() throws IOException {
            ++this.mPosition;
            return this.mDataInputStream.readUnsignedByte();
        }

        public long readUnsignedInt() throws IOException {
            return (long)this.readInt() & 0xFFFFFFFFL;
        }

        @Override
        public int readUnsignedShort() throws IOException {
            int n;
            this.mPosition = n = this.mPosition + 2;
            if (n <= this.mLength) {
                int n2;
                n = this.mDataInputStream.read();
                if ((n | (n2 = this.mDataInputStream.read())) >= 0) {
                    Object object = this.mByteOrder;
                    if (object == LITTLE_ENDIAN) {
                        return (n2 << 8) + n;
                    }
                    if (object == BIG_ENDIAN) {
                        return (n << 8) + n2;
                    }
                    object = new StringBuilder();
                    object.append("Invalid byte order: ");
                    object.append(this.mByteOrder);
                    throw new IOException(object.toString());
                }
                throw new EOFException();
            }
            throw new EOFException();
        }

        public void seek(long l) throws IOException {
            int n = this.mPosition;
            if ((long)n > l) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
                this.mDataInputStream.mark(this.mLength);
            } else {
                l -= (long)n;
            }
            if (this.skipBytes((int)l) == (int)l) {
                return;
            }
            throw new IOException("Couldn't seek up to the byteCount");
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        @Override
        public int skipBytes(int n) throws IOException {
            int n2 = Math.min(n, this.mLength - this.mPosition);
            for (n = 0; n < n2; n += this.mDataInputStream.skipBytes((int)(n2 - n))) {
            }
            this.mPosition += n;
            return n;
        }
    }

    private static class ByteOrderedDataOutputStream
    extends FilterOutputStream {
        private ByteOrder mByteOrder;
        private final OutputStream mOutputStream;

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }

        public void setByteOrder(ByteOrder byteOrder) {
            this.mByteOrder = byteOrder;
        }

        @Override
        public void write(byte[] arrby) throws IOException {
            this.mOutputStream.write(arrby);
        }

        @Override
        public void write(byte[] arrby, int n, int n2) throws IOException {
            this.mOutputStream.write(arrby, n, n2);
        }

        public void writeByte(int n) throws IOException {
            this.mOutputStream.write(n);
        }

        public void writeInt(int n) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write(n >>> 0 & 255);
                this.mOutputStream.write(n >>> 8 & 255);
                this.mOutputStream.write(n >>> 16 & 255);
                this.mOutputStream.write(n >>> 24 & 255);
                return;
            }
            if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write(n >>> 24 & 255);
                this.mOutputStream.write(n >>> 16 & 255);
                this.mOutputStream.write(n >>> 8 & 255);
                this.mOutputStream.write(n >>> 0 & 255);
            }
        }

        public void writeShort(short s) throws IOException {
            if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write(s >>> 0 & 255);
                this.mOutputStream.write(s >>> 8 & 255);
                return;
            }
            if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write(s >>> 8 & 255);
                this.mOutputStream.write(s >>> 0 & 255);
            }
        }

        public void writeUnsignedInt(long l) throws IOException {
            this.writeInt((int)l);
        }

        public void writeUnsignedShort(int n) throws IOException {
            this.writeShort((short)n);
        }
    }

    private static class ExifAttribute {
        public final byte[] bytes;
        public final int format;
        public final int numberOfComponents;

        ExifAttribute(int n, int n2, byte[] arrby) {
            this.format = n;
            this.numberOfComponents = n2;
            this.bytes = arrby;
        }

        public static ExifAttribute createByte(String arrby) {
            if (arrby.length() == 1 && arrby.charAt(0) >= '0' && arrby.charAt(0) <= '1') {
                byte[] arrby2 = new byte[]{(byte)(arrby.charAt(0) - 48)};
                return new ExifAttribute(1, arrby2.length, arrby2);
            }
            arrby = arrby.getBytes(ExifInterface.ASCII);
            return new ExifAttribute(1, arrby.length, arrby);
        }

        public static ExifAttribute createDouble(double d, ByteOrder byteOrder) {
            return ExifAttribute.createDouble(new double[]{d}, byteOrder);
        }

        public static ExifAttribute createDouble(double[] arrd, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * arrd.length]);
            byteBuffer.order(byteOrder);
            int n = arrd.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putDouble(arrd[i]);
            }
            return new ExifAttribute(12, arrd.length, byteBuffer.array());
        }

        public static ExifAttribute createSLong(int n, ByteOrder byteOrder) {
            return ExifAttribute.createSLong(new int[]{n}, byteOrder);
        }

        public static ExifAttribute createSLong(int[] arrn, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * arrn.length]);
            byteBuffer.order(byteOrder);
            int n = arrn.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putInt(arrn[i]);
            }
            return new ExifAttribute(9, arrn.length, byteBuffer.array());
        }

        public static ExifAttribute createSRational(Rational rational, ByteOrder byteOrder) {
            return ExifAttribute.createSRational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createSRational(Rational[] arrrational, ByteOrder object) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * arrrational.length]);
            byteBuffer.order((ByteOrder)object);
            int n = arrrational.length;
            for (int i = 0; i < n; ++i) {
                object = arrrational[i];
                byteBuffer.putInt((int)object.numerator);
                byteBuffer.putInt((int)object.denominator);
            }
            return new ExifAttribute(10, arrrational.length, byteBuffer.array());
        }

        public static ExifAttribute createString(String arrby) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)arrby);
            stringBuilder.append('\u0000');
            arrby = stringBuilder.toString().getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, arrby.length, arrby);
        }

        public static ExifAttribute createULong(long l, ByteOrder byteOrder) {
            return ExifAttribute.createULong(new long[]{l}, byteOrder);
        }

        public static ExifAttribute createULong(long[] arrl, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * arrl.length]);
            byteBuffer.order(byteOrder);
            int n = arrl.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putInt((int)arrl[i]);
            }
            return new ExifAttribute(4, arrl.length, byteBuffer.array());
        }

        public static ExifAttribute createURational(Rational rational, ByteOrder byteOrder) {
            return ExifAttribute.createURational(new Rational[]{rational}, byteOrder);
        }

        public static ExifAttribute createURational(Rational[] arrrational, ByteOrder object) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * arrrational.length]);
            byteBuffer.order((ByteOrder)object);
            int n = arrrational.length;
            for (int i = 0; i < n; ++i) {
                object = arrrational[i];
                byteBuffer.putInt((int)object.numerator);
                byteBuffer.putInt((int)object.denominator);
            }
            return new ExifAttribute(5, arrrational.length, byteBuffer.array());
        }

        public static ExifAttribute createUShort(int n, ByteOrder byteOrder) {
            return ExifAttribute.createUShort(new int[]{n}, byteOrder);
        }

        public static ExifAttribute createUShort(int[] arrn, ByteOrder byteOrder) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * arrn.length]);
            byteBuffer.order(byteOrder);
            int n = arrn.length;
            for (int i = 0; i < n; ++i) {
                byteBuffer.putShort((short)arrn[i]);
            }
            return new ExifAttribute(3, arrn.length, byteBuffer.array());
        }

        public double getDoubleValue(ByteOrder arrl) {
            if ((arrl = this.getValue((ByteOrder)arrl)) != null) {
                if (arrl instanceof String) {
                    return Double.parseDouble((String)arrl);
                }
                if (arrl instanceof long[]) {
                    if ((arrl = (long[])arrl).length == 1) {
                        return arrl[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (arrl instanceof int[]) {
                    if ((arrl = (int[])arrl).length == 1) {
                        return arrl[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (arrl instanceof double[]) {
                    if ((arrl = (double[])arrl).length == 1) {
                        return arrl[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (arrl instanceof Rational[]) {
                    if ((arrl = (Rational[])arrl).length == 1) {
                        return arrl[0].calculate();
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                throw new NumberFormatException("Couldn't find a double value");
            }
            throw new NumberFormatException("NULL can't be converted to a double value");
        }

        public int getIntValue(ByteOrder arrl) {
            if ((arrl = this.getValue((ByteOrder)arrl)) != null) {
                if (arrl instanceof String) {
                    return Integer.parseInt((String)arrl);
                }
                if (arrl instanceof long[]) {
                    if ((arrl = (long[])arrl).length == 1) {
                        return (int)arrl[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                if (arrl instanceof int[]) {
                    if ((arrl = (int[])arrl).length == 1) {
                        return (int)arrl[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                }
                throw new NumberFormatException("Couldn't find a integer value");
            }
            throw new NumberFormatException("NULL can't be converted to a integer value");
        }

        public String getStringValue(ByteOrder object) {
            long[] arrl = this.getValue((ByteOrder)object);
            if (arrl == null) {
                return null;
            }
            if (arrl instanceof String) {
                return (String)arrl;
            }
            object = new StringBuilder();
            if (arrl instanceof long[]) {
                arrl = arrl;
                for (int i = 0; i < arrl.length; ++i) {
                    object.append(arrl[i]);
                    if (i + 1 == arrl.length) continue;
                    object.append(",");
                }
                return object.toString();
            }
            if (arrl instanceof int[]) {
                arrl = (int[])arrl;
                for (int i = 0; i < arrl.length; ++i) {
                    object.append((int)arrl[i]);
                    if (i + 1 == arrl.length) continue;
                    object.append(",");
                }
                return object.toString();
            }
            if (arrl instanceof double[]) {
                arrl = arrl;
                for (int i = 0; i < arrl.length; ++i) {
                    object.append((double)arrl[i]);
                    if (i + 1 == arrl.length) continue;
                    object.append(",");
                }
                return object.toString();
            }
            if (arrl instanceof Rational[]) {
                arrl = (Rational[])arrl;
                for (int i = 0; i < arrl.length; ++i) {
                    object.append(arrl[i].numerator);
                    object.append('/');
                    object.append(arrl[i].denominator);
                    if (i + 1 == arrl.length) continue;
                    object.append(",");
                }
                return object.toString();
            }
            return null;
        }

        /*
         * Exception decompiling
         */
        Object getValue(ByteOrder var1_1) {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:357)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:769)
            // org.benf.cfr.reader.entities.ClassFile.analyseInnerClassesPass1(ClassFile.java:682)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:765)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:701)
            // org.benf.cfr.reader.Main.doJar(Main.java:134)
            // org.benf.cfr.reader.Main.main(Main.java:189)
            throw new IllegalStateException("Decompilation failed");
        }

        public int size() {
            return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");
            stringBuilder.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            stringBuilder.append(", data length:");
            stringBuilder.append(this.bytes.length);
            stringBuilder.append(")");
            return stringBuilder.toString();
        }
    }

    static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        ExifTag(String string2, int n, int n2) {
            this.name = string2;
            this.number = n;
            this.primaryFormat = n2;
            this.secondaryFormat = -1;
        }

        ExifTag(String string2, int n, int n2, int n3) {
            this.name = string2;
            this.number = n;
            this.primaryFormat = n2;
            this.secondaryFormat = n3;
        }

        boolean isFormatCompatible(int n) {
            int n2 = this.primaryFormat;
            if (n2 != 7) {
                if (n == 7) {
                    return true;
                }
                if (n2 != n) {
                    int n3 = this.secondaryFormat;
                    if (n3 == n) {
                        return true;
                    }
                    if ((n2 == 4 || n3 == 4) && n == 3) {
                        return true;
                    }
                    if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && n == 8) {
                        return true;
                    }
                    if ((this.primaryFormat == 12 || this.secondaryFormat == 12) && n == 11) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
            return true;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface IfdType {
    }

    private static class Rational {
        public final long denominator;
        public final long numerator;

        Rational(double d) {
            this((long)(10000.0 * d), 10000L);
        }

        Rational(long l, long l2) {
            if (l2 == 0L) {
                this.numerator = 0L;
                this.denominator = 1L;
                return;
            }
            this.numerator = l;
            this.denominator = l2;
        }

        public double calculate() {
            return (double)this.numerator / (double)this.denominator;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.numerator);
            stringBuilder.append("/");
            stringBuilder.append(this.denominator);
            return stringBuilder.toString();
        }
    }

}

