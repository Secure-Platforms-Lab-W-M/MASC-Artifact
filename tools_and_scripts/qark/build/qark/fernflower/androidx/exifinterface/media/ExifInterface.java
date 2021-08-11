package androidx.exifinterface.media;

import android.content.res.AssetManager.AssetInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
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
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.Map.Entry;
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
   private static final ExifInterface.ExifTag[] EXIF_POINTER_TAGS;
   static final ExifInterface.ExifTag[][] EXIF_TAGS;
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
   private static final List FLIPPED_ROTATION_ORDER;
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
   private static final ExifInterface.ExifTag[] IFD_EXIF_TAGS;
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
   private static final ExifInterface.ExifTag[] IFD_GPS_TAGS;
   private static final ExifInterface.ExifTag[] IFD_INTEROPERABILITY_TAGS;
   private static final int IFD_OFFSET = 8;
   private static final ExifInterface.ExifTag[] IFD_THUMBNAIL_TAGS;
   private static final ExifInterface.ExifTag[] IFD_TIFF_TAGS;
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
   private static final ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_LENGTH_TAG;
   private static final ExifInterface.ExifTag JPEG_INTERCHANGE_FORMAT_TAG;
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
   private static final ExifInterface.ExifTag[] ORF_CAMERA_SETTINGS_TAGS;
   private static final ExifInterface.ExifTag[] ORF_IMAGE_PROCESSING_TAGS;
   private static final byte[] ORF_MAKER_NOTE_HEADER_1;
   private static final int ORF_MAKER_NOTE_HEADER_1_SIZE = 8;
   private static final byte[] ORF_MAKER_NOTE_HEADER_2;
   private static final int ORF_MAKER_NOTE_HEADER_2_SIZE = 12;
   private static final ExifInterface.ExifTag[] ORF_MAKER_NOTE_TAGS;
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
   private static final ExifInterface.ExifTag[] PEF_TAGS;
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
   private static final List ROTATION_ORDER;
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
   private static final ExifInterface.ExifTag TAG_RAF_IMAGE_SIZE;
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
   private static final HashMap sExifPointerTagMap;
   private static final HashMap[] sExifTagMapsForReading;
   private static final HashMap[] sExifTagMapsForWriting;
   private static SimpleDateFormat sFormatter;
   private static final Pattern sGpsTimestampPattern;
   private static final Pattern sNonZeroTimePattern;
   private static final HashSet sTagSetForCompatibility;
   private final AssetInputStream mAssetInputStream;
   private final HashMap[] mAttributes;
   private Set mAttributesOffsets;
   private ByteOrder mExifByteOrder;
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
      Integer var3 = 1;
      Integer var4 = 3;
      Integer var5 = 2;
      Integer var6 = 8;
      ROTATION_ORDER = Arrays.asList(var3, 6, var4, var6);
      Integer var7 = 7;
      Integer var8 = 5;
      FLIPPED_ROTATION_ORDER = Arrays.asList(var5, var7, 4, var8);
      BITS_PER_SAMPLE_RGB = new int[]{8, 8, 8};
      BITS_PER_SAMPLE_GREYSCALE_1 = new int[]{4};
      BITS_PER_SAMPLE_GREYSCALE_2 = new int[]{8};
      JPEG_SIGNATURE = new byte[]{-1, -40, -1};
      ORF_MAKER_NOTE_HEADER_1 = new byte[]{79, 76, 89, 77, 80, 0};
      ORF_MAKER_NOTE_HEADER_2 = new byte[]{79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
      IFD_FORMAT_NAMES = new String[]{"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE"};
      IFD_FORMAT_BYTES_PER_FORMAT = new int[]{0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
      EXIF_ASCII_PREFIX = new byte[]{65, 83, 67, 73, 73, 0, 0, 0};
      IFD_TIFF_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("NewSubfileType", 254, 4), new ExifInterface.ExifTag("SubfileType", 255, 4), new ExifInterface.ExifTag("ImageWidth", 256, 3, 4), new ExifInterface.ExifTag("ImageLength", 257, 3, 4), new ExifInterface.ExifTag("BitsPerSample", 258, 3), new ExifInterface.ExifTag("Compression", 259, 3), new ExifInterface.ExifTag("PhotometricInterpretation", 262, 3), new ExifInterface.ExifTag("ImageDescription", 270, 2), new ExifInterface.ExifTag("Make", 271, 2), new ExifInterface.ExifTag("Model", 272, 2), new ExifInterface.ExifTag("StripOffsets", 273, 3, 4), new ExifInterface.ExifTag("Orientation", 274, 3), new ExifInterface.ExifTag("SamplesPerPixel", 277, 3), new ExifInterface.ExifTag("RowsPerStrip", 278, 3, 4), new ExifInterface.ExifTag("StripByteCounts", 279, 3, 4), new ExifInterface.ExifTag("XResolution", 282, 5), new ExifInterface.ExifTag("YResolution", 283, 5), new ExifInterface.ExifTag("PlanarConfiguration", 284, 3), new ExifInterface.ExifTag("ResolutionUnit", 296, 3), new ExifInterface.ExifTag("TransferFunction", 301, 3), new ExifInterface.ExifTag("Software", 305, 2), new ExifInterface.ExifTag("DateTime", 306, 2), new ExifInterface.ExifTag("Artist", 315, 2), new ExifInterface.ExifTag("WhitePoint", 318, 5), new ExifInterface.ExifTag("PrimaryChromaticities", 319, 5), new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4), new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifInterface.ExifTag("YCbCrCoefficients", 529, 5), new ExifInterface.ExifTag("YCbCrSubSampling", 530, 3), new ExifInterface.ExifTag("YCbCrPositioning", 531, 3), new ExifInterface.ExifTag("ReferenceBlackWhite", 532, 5), new ExifInterface.ExifTag("Copyright", 33432, 2), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("SensorTopBorder", 4, 4), new ExifInterface.ExifTag("SensorLeftBorder", 5, 4), new ExifInterface.ExifTag("SensorBottomBorder", 6, 4), new ExifInterface.ExifTag("SensorRightBorder", 7, 4), new ExifInterface.ExifTag("ISO", 23, 3), new ExifInterface.ExifTag("JpgFromRaw", 46, 7)};
      IFD_EXIF_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ExposureTime", 33434, 5), new ExifInterface.ExifTag("FNumber", 33437, 5), new ExifInterface.ExifTag("ExposureProgram", 34850, 3), new ExifInterface.ExifTag("SpectralSensitivity", 34852, 2), new ExifInterface.ExifTag("PhotographicSensitivity", 34855, 3), new ExifInterface.ExifTag("OECF", 34856, 7), new ExifInterface.ExifTag("ExifVersion", 36864, 2), new ExifInterface.ExifTag("DateTimeOriginal", 36867, 2), new ExifInterface.ExifTag("DateTimeDigitized", 36868, 2), new ExifInterface.ExifTag("ComponentsConfiguration", 37121, 7), new ExifInterface.ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifInterface.ExifTag("ShutterSpeedValue", 37377, 10), new ExifInterface.ExifTag("ApertureValue", 37378, 5), new ExifInterface.ExifTag("BrightnessValue", 37379, 10), new ExifInterface.ExifTag("ExposureBiasValue", 37380, 10), new ExifInterface.ExifTag("MaxApertureValue", 37381, 5), new ExifInterface.ExifTag("SubjectDistance", 37382, 5), new ExifInterface.ExifTag("MeteringMode", 37383, 3), new ExifInterface.ExifTag("LightSource", 37384, 3), new ExifInterface.ExifTag("Flash", 37385, 3), new ExifInterface.ExifTag("FocalLength", 37386, 5), new ExifInterface.ExifTag("SubjectArea", 37396, 3), new ExifInterface.ExifTag("MakerNote", 37500, 7), new ExifInterface.ExifTag("UserComment", 37510, 7), new ExifInterface.ExifTag("SubSecTime", 37520, 2), new ExifInterface.ExifTag("SubSecTimeOriginal", 37521, 2), new ExifInterface.ExifTag("SubSecTimeDigitized", 37522, 2), new ExifInterface.ExifTag("FlashpixVersion", 40960, 7), new ExifInterface.ExifTag("ColorSpace", 40961, 3), new ExifInterface.ExifTag("PixelXDimension", 40962, 3, 4), new ExifInterface.ExifTag("PixelYDimension", 40963, 3, 4), new ExifInterface.ExifTag("RelatedSoundFile", 40964, 2), new ExifInterface.ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifInterface.ExifTag("FlashEnergy", 41483, 5), new ExifInterface.ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifInterface.ExifTag("FocalPlaneXResolution", 41486, 5), new ExifInterface.ExifTag("FocalPlaneYResolution", 41487, 5), new ExifInterface.ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifInterface.ExifTag("SubjectLocation", 41492, 3), new ExifInterface.ExifTag("ExposureIndex", 41493, 5), new ExifInterface.ExifTag("SensingMethod", 41495, 3), new ExifInterface.ExifTag("FileSource", 41728, 7), new ExifInterface.ExifTag("SceneType", 41729, 7), new ExifInterface.ExifTag("CFAPattern", 41730, 7), new ExifInterface.ExifTag("CustomRendered", 41985, 3), new ExifInterface.ExifTag("ExposureMode", 41986, 3), new ExifInterface.ExifTag("WhiteBalance", 41987, 3), new ExifInterface.ExifTag("DigitalZoomRatio", 41988, 5), new ExifInterface.ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifInterface.ExifTag("SceneCaptureType", 41990, 3), new ExifInterface.ExifTag("GainControl", 41991, 3), new ExifInterface.ExifTag("Contrast", 41992, 3), new ExifInterface.ExifTag("Saturation", 41993, 3), new ExifInterface.ExifTag("Sharpness", 41994, 3), new ExifInterface.ExifTag("DeviceSettingDescription", 41995, 7), new ExifInterface.ExifTag("SubjectDistanceRange", 41996, 3), new ExifInterface.ExifTag("ImageUniqueID", 42016, 2), new ExifInterface.ExifTag("DNGVersion", 50706, 1), new ExifInterface.ExifTag("DefaultCropSize", 50720, 3, 4)};
      IFD_GPS_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("GPSVersionID", 0, 1), new ExifInterface.ExifTag("GPSLatitudeRef", 1, 2), new ExifInterface.ExifTag("GPSLatitude", 2, 5), new ExifInterface.ExifTag("GPSLongitudeRef", 3, 2), new ExifInterface.ExifTag("GPSLongitude", 4, 5), new ExifInterface.ExifTag("GPSAltitudeRef", 5, 1), new ExifInterface.ExifTag("GPSAltitude", 6, 5), new ExifInterface.ExifTag("GPSTimeStamp", 7, 5), new ExifInterface.ExifTag("GPSSatellites", 8, 2), new ExifInterface.ExifTag("GPSStatus", 9, 2), new ExifInterface.ExifTag("GPSMeasureMode", 10, 2), new ExifInterface.ExifTag("GPSDOP", 11, 5), new ExifInterface.ExifTag("GPSSpeedRef", 12, 2), new ExifInterface.ExifTag("GPSSpeed", 13, 5), new ExifInterface.ExifTag("GPSTrackRef", 14, 2), new ExifInterface.ExifTag("GPSTrack", 15, 5), new ExifInterface.ExifTag("GPSImgDirectionRef", 16, 2), new ExifInterface.ExifTag("GPSImgDirection", 17, 5), new ExifInterface.ExifTag("GPSMapDatum", 18, 2), new ExifInterface.ExifTag("GPSDestLatitudeRef", 19, 2), new ExifInterface.ExifTag("GPSDestLatitude", 20, 5), new ExifInterface.ExifTag("GPSDestLongitudeRef", 21, 2), new ExifInterface.ExifTag("GPSDestLongitude", 22, 5), new ExifInterface.ExifTag("GPSDestBearingRef", 23, 2), new ExifInterface.ExifTag("GPSDestBearing", 24, 5), new ExifInterface.ExifTag("GPSDestDistanceRef", 25, 2), new ExifInterface.ExifTag("GPSDestDistance", 26, 5), new ExifInterface.ExifTag("GPSProcessingMethod", 27, 7), new ExifInterface.ExifTag("GPSAreaInformation", 28, 7), new ExifInterface.ExifTag("GPSDateStamp", 29, 2), new ExifInterface.ExifTag("GPSDifferential", 30, 3)};
      IFD_INTEROPERABILITY_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("InteroperabilityIndex", 1, 2)};
      IFD_THUMBNAIL_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("NewSubfileType", 254, 4), new ExifInterface.ExifTag("SubfileType", 255, 4), new ExifInterface.ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifInterface.ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifInterface.ExifTag("BitsPerSample", 258, 3), new ExifInterface.ExifTag("Compression", 259, 3), new ExifInterface.ExifTag("PhotometricInterpretation", 262, 3), new ExifInterface.ExifTag("ImageDescription", 270, 2), new ExifInterface.ExifTag("Make", 271, 2), new ExifInterface.ExifTag("Model", 272, 2), new ExifInterface.ExifTag("StripOffsets", 273, 3, 4), new ExifInterface.ExifTag("Orientation", 274, 3), new ExifInterface.ExifTag("SamplesPerPixel", 277, 3), new ExifInterface.ExifTag("RowsPerStrip", 278, 3, 4), new ExifInterface.ExifTag("StripByteCounts", 279, 3, 4), new ExifInterface.ExifTag("XResolution", 282, 5), new ExifInterface.ExifTag("YResolution", 283, 5), new ExifInterface.ExifTag("PlanarConfiguration", 284, 3), new ExifInterface.ExifTag("ResolutionUnit", 296, 3), new ExifInterface.ExifTag("TransferFunction", 301, 3), new ExifInterface.ExifTag("Software", 305, 2), new ExifInterface.ExifTag("DateTime", 306, 2), new ExifInterface.ExifTag("Artist", 315, 2), new ExifInterface.ExifTag("WhitePoint", 318, 5), new ExifInterface.ExifTag("PrimaryChromaticities", 319, 5), new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4), new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifInterface.ExifTag("YCbCrCoefficients", 529, 5), new ExifInterface.ExifTag("YCbCrSubSampling", 530, 3), new ExifInterface.ExifTag("YCbCrPositioning", 531, 3), new ExifInterface.ExifTag("ReferenceBlackWhite", 532, 5), new ExifInterface.ExifTag("Copyright", 33432, 2), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("DNGVersion", 50706, 1), new ExifInterface.ExifTag("DefaultCropSize", 50720, 3, 4)};
      TAG_RAF_IMAGE_SIZE = new ExifInterface.ExifTag("StripOffsets", 273, 3);
      ORF_MAKER_NOTE_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ThumbnailImage", 256, 7), new ExifInterface.ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifInterface.ExifTag("ImageProcessingIFDPointer", 8256, 4)};
      ORF_CAMERA_SETTINGS_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("PreviewImageStart", 257, 4), new ExifInterface.ExifTag("PreviewImageLength", 258, 4)};
      ORF_IMAGE_PROCESSING_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("AspectFrame", 4371, 3)};
      ExifInterface.ExifTag[] var9 = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("ColorSpace", 55, 3)};
      PEF_TAGS = var9;
      ExifInterface.ExifTag[] var10 = IFD_TIFF_TAGS;
      EXIF_TAGS = new ExifInterface.ExifTag[][]{var10, IFD_EXIF_TAGS, IFD_GPS_TAGS, IFD_INTEROPERABILITY_TAGS, IFD_THUMBNAIL_TAGS, var10, ORF_MAKER_NOTE_TAGS, ORF_CAMERA_SETTINGS_TAGS, ORF_IMAGE_PROCESSING_TAGS, var9};
      EXIF_POINTER_TAGS = new ExifInterface.ExifTag[]{new ExifInterface.ExifTag("SubIFDPointer", 330, 4), new ExifInterface.ExifTag("ExifIFDPointer", 34665, 4), new ExifInterface.ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifInterface.ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifInterface.ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifInterface.ExifTag("ImageProcessingIFDPointer", 8256, 1)};
      JPEG_INTERCHANGE_FORMAT_TAG = new ExifInterface.ExifTag("JPEGInterchangeFormat", 513, 4);
      JPEG_INTERCHANGE_FORMAT_LENGTH_TAG = new ExifInterface.ExifTag("JPEGInterchangeFormatLength", 514, 4);
      ExifInterface.ExifTag[][] var11 = EXIF_TAGS;
      sExifTagMapsForReading = new HashMap[var11.length];
      sExifTagMapsForWriting = new HashMap[var11.length];
      sTagSetForCompatibility = new HashSet(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
      sExifPointerTagMap = new HashMap();
      Charset var12 = Charset.forName("US-ASCII");
      ASCII = var12;
      IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(var12);
      SimpleDateFormat var13 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
      sFormatter = var13;
      var13.setTimeZone(TimeZone.getTimeZone("UTC"));

      for(int var0 = 0; var0 < EXIF_TAGS.length; ++var0) {
         sExifTagMapsForReading[var0] = new HashMap();
         sExifTagMapsForWriting[var0] = new HashMap();
         var9 = EXIF_TAGS[var0];
         int var2 = var9.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            ExifInterface.ExifTag var14 = var9[var1];
            sExifTagMapsForReading[var0].put(var14.number, var14);
            sExifTagMapsForWriting[var0].put(var14.name, var14);
         }
      }

      sExifPointerTagMap.put(EXIF_POINTER_TAGS[0].number, var8);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[1].number, var3);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[2].number, var5);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[3].number, var4);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[4].number, var7);
      sExifPointerTagMap.put(EXIF_POINTER_TAGS[5].number, var6);
      sNonZeroTimePattern = Pattern.compile(".*[1-9].*");
      sGpsTimestampPattern = Pattern.compile("^([0-9][0-9]):([0-9][0-9]):([0-9][0-9])$");
   }

   public ExifInterface(InputStream var1) throws IOException {
      this.mAttributes = new HashMap[EXIF_TAGS.length];
      this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
      this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
      if (var1 != null) {
         this.mFilename = null;
         if (var1 instanceof AssetInputStream) {
            this.mAssetInputStream = (AssetInputStream)var1;
         } else {
            this.mAssetInputStream = null;
         }

         this.loadAttributes(var1);
      } else {
         throw new IllegalArgumentException("inputStream cannot be null");
      }
   }

   public ExifInterface(String var1) throws IOException {
      this.mAttributes = new HashMap[EXIF_TAGS.length];
      this.mAttributesOffsets = new HashSet(EXIF_TAGS.length);
      this.mExifByteOrder = ByteOrder.BIG_ENDIAN;
      if (var1 != null) {
         FileInputStream var2 = null;
         this.mAssetInputStream = null;
         this.mFilename = var1;

         FileInputStream var9;
         label78: {
            Throwable var10000;
            label84: {
               boolean var10001;
               try {
                  var9 = new FileInputStream(var1);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label84;
               }

               var2 = var9;

               label73:
               try {
                  this.loadAttributes(var9);
                  break label78;
               } catch (Throwable var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label73;
               }
            }

            Throwable var10 = var10000;
            closeQuietly(var2);
            throw var10;
         }

         closeQuietly(var9);
      } else {
         throw new IllegalArgumentException("filename cannot be null");
      }
   }

   private void addDefaultValuesForCompatibility() {
      String var1 = this.getAttribute("DateTimeOriginal");
      if (var1 != null && this.getAttribute("DateTime") == null) {
         this.mAttributes[0].put("DateTime", ExifInterface.ExifAttribute.createString(var1));
      }

      if (this.getAttribute("ImageWidth") == null) {
         this.mAttributes[0].put("ImageWidth", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("ImageLength") == null) {
         this.mAttributes[0].put("ImageLength", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("Orientation") == null) {
         this.mAttributes[0].put("Orientation", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.getAttribute("LightSource") == null) {
         this.mAttributes[1].put("LightSource", ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

   }

   private static void closeQuietly(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (RuntimeException var1) {
            throw var1;
         } catch (Exception var2) {
         }
      }
   }

   private String convertDecimalDegree(double var1) {
      long var3 = (long)var1;
      long var5 = (long)((var1 - (double)var3) * 60.0D);
      long var7 = Math.round((var1 - (double)var3 - (double)var5 / 60.0D) * 3600.0D * 1.0E7D);
      StringBuilder var9 = new StringBuilder();
      var9.append(var3);
      var9.append("/1,");
      var9.append(var5);
      var9.append("/1,");
      var9.append(var7);
      var9.append("/10000000");
      return var9.toString();
   }

   private static double convertRationalLatLonToDouble(String var0, String var1) {
      try {
         String[] var11 = var0.split(",", -1);
         String[] var8 = var11[0].split("/", -1);
         double var2 = Double.parseDouble(var8[0].trim()) / Double.parseDouble(var8[1].trim());
         var8 = var11[1].split("/", -1);
         double var4 = Double.parseDouble(var8[0].trim()) / Double.parseDouble(var8[1].trim());
         var11 = var11[2].split("/", -1);
         double var6 = Double.parseDouble(var11[0].trim()) / Double.parseDouble(var11[1].trim());
         var2 = var4 / 60.0D + var2 + var6 / 3600.0D;
         if (!var1.equals("S") && !var1.equals("W")) {
            if (!var1.equals("N")) {
               if (var1.equals("E")) {
                  return var2;
               }

               throw new IllegalArgumentException();
            }

            return var2;
         }

         return -var2;
      } catch (NumberFormatException var9) {
      } catch (ArrayIndexOutOfBoundsException var10) {
      }

      throw new IllegalArgumentException();
   }

   private static long[] convertToLongArray(Object var0) {
      if (!(var0 instanceof int[])) {
         return var0 instanceof long[] ? (long[])((long[])var0) : null;
      } else {
         int[] var3 = (int[])((int[])var0);
         long[] var2 = new long[var3.length];

         for(int var1 = 0; var1 < var3.length; ++var1) {
            var2[var1] = (long)var3[var1];
         }

         return var2;
      }
   }

   private static int copy(InputStream var0, OutputStream var1) throws IOException {
      int var2 = 0;
      byte[] var4 = new byte[8192];

      while(true) {
         int var3 = var0.read(var4);
         if (var3 == -1) {
            return var2;
         }

         var2 += var3;
         var1.write(var4, 0, var3);
      }
   }

   private ExifInterface.ExifAttribute getExifAttribute(String var1) {
      String var3 = var1;
      if ("ISOSpeedRatings".equals(var1)) {
         var3 = "PhotographicSensitivity";
      }

      for(int var2 = 0; var2 < EXIF_TAGS.length; ++var2) {
         ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get(var3);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   private void getJpegAttributes(ExifInterface.ByteOrderedDataInputStream var1, int var2, int var3) throws IOException {
      var1.setByteOrder(ByteOrder.BIG_ENDIAN);
      var1.seek((long)var2);
      byte var4 = var1.readByte();
      StringBuilder var8;
      if (var4 != -1) {
         var8 = new StringBuilder();
         var8.append("Invalid marker: ");
         var8.append(Integer.toHexString(var4 & 255));
         throw new IOException(var8.toString());
      } else if (var1.readByte() != -40) {
         var8 = new StringBuilder();
         var8.append("Invalid marker: ");
         var8.append(Integer.toHexString(var4 & 255));
         throw new IOException(var8.toString());
      } else {
         var2 = var2 + 1 + 1;

         while(true) {
            var4 = var1.readByte();
            if (var4 != -1) {
               var8 = new StringBuilder();
               var8.append("Invalid marker:");
               var8.append(Integer.toHexString(var4 & 255));
               throw new IOException(var8.toString());
            }

            var4 = var1.readByte();
            if (var4 == -39 || var4 == -38) {
               var1.setByteOrder(this.mExifByteOrder);
               return;
            }

            int var6 = var1.readUnsignedShort() - 2;
            int var5 = var2 + 1 + 1 + 2;
            if (var6 < 0) {
               throw new IOException("Invalid length");
            }

            byte[] var7;
            int var9;
            if (var4 != -31) {
               if (var4 != -2) {
                  label86:
                  switch(var4) {
                  default:
                     switch(var4) {
                     case -59:
                     case -58:
                     case -57:
                        break;
                     default:
                        switch(var4) {
                        case -55:
                        case -54:
                        case -53:
                           break;
                        default:
                           switch(var4) {
                           case -51:
                           case -50:
                           case -49:
                              break;
                           default:
                              var9 = var5;
                              var2 = var6;
                              break label86;
                           }
                        }
                     }
                  case -64:
                  case -63:
                  case -62:
                  case -61:
                     if (var1.skipBytes(1) != 1) {
                        throw new IOException("Invalid SOFx");
                     }

                     this.mAttributes[var3].put("ImageLength", ExifInterface.ExifAttribute.createULong((long)var1.readUnsignedShort(), this.mExifByteOrder));
                     this.mAttributes[var3].put("ImageWidth", ExifInterface.ExifAttribute.createULong((long)var1.readUnsignedShort(), this.mExifByteOrder));
                     var2 = var6 - 5;
                     var9 = var5;
                  }
               } else {
                  var7 = new byte[var6];
                  if (var1.read(var7) != var6) {
                     throw new IOException("Invalid exif");
                  }

                  byte var10 = 0;
                  var9 = var5;
                  var2 = var10;
                  if (this.getAttribute("UserComment") == null) {
                     this.mAttributes[1].put("UserComment", ExifInterface.ExifAttribute.createString(new String(var7, ASCII)));
                     var9 = var5;
                     var2 = var10;
                  }
               }
            } else if (var6 < 6) {
               var9 = var5;
               var2 = var6;
            } else {
               var7 = new byte[6];
               if (var1.read(var7) != 6) {
                  throw new IOException("Invalid exif");
               }

               var9 = var5 + 6;
               var2 = var6 - 6;
               if (Arrays.equals(var7, IDENTIFIER_EXIF_APP1)) {
                  if (var2 <= 0) {
                     throw new IOException("Invalid exif");
                  }

                  this.mExifOffset = var9;
                  var7 = new byte[var2];
                  if (var1.read(var7) != var2) {
                     throw new IOException("Invalid exif");
                  }

                  var9 += var2;
                  var2 = 0;
                  this.readExifSegment(var7, var3);
               }
            }

            if (var2 < 0) {
               throw new IOException("Invalid length");
            }

            if (var1.skipBytes(var2) != var2) {
               throw new IOException("Invalid JPEG segment");
            }

            var2 += var9;
         }
      }
   }

   private int getMimeType(BufferedInputStream var1) throws IOException {
      var1.mark(5000);
      byte[] var2 = new byte[5000];
      var1.read(var2);
      var1.reset();
      if (isJpegFormat(var2)) {
         return 4;
      } else if (this.isRafFormat(var2)) {
         return 9;
      } else if (this.isOrfFormat(var2)) {
         return 7;
      } else {
         return this.isRw2Format(var2) ? 10 : 0;
      }
   }

   private void getOrfAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.getRawAttributes(var1);
      ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("MakerNote");
      if (var8 != null) {
         var1 = new ExifInterface.ByteOrderedDataInputStream(var8.bytes);
         var1.setByteOrder(this.mExifByteOrder);
         byte[] var6 = new byte[ORF_MAKER_NOTE_HEADER_1.length];
         var1.readFully(var6);
         var1.seek(0L);
         byte[] var7 = new byte[ORF_MAKER_NOTE_HEADER_2.length];
         var1.readFully(var7);
         if (Arrays.equals(var6, ORF_MAKER_NOTE_HEADER_1)) {
            var1.seek(8L);
         } else if (Arrays.equals(var7, ORF_MAKER_NOTE_HEADER_2)) {
            var1.seek(12L);
         }

         this.readImageFileDirectory(var1, 6);
         var8 = (ExifInterface.ExifAttribute)this.mAttributes[7].get("PreviewImageStart");
         ExifInterface.ExifAttribute var10 = (ExifInterface.ExifAttribute)this.mAttributes[7].get("PreviewImageLength");
         if (var8 != null && var10 != null) {
            this.mAttributes[5].put("JPEGInterchangeFormat", var8);
            this.mAttributes[5].put("JPEGInterchangeFormatLength", var10);
         }

         var8 = (ExifInterface.ExifAttribute)this.mAttributes[8].get("AspectFrame");
         if (var8 != null) {
            int[] var9 = (int[])((int[])var8.getValue(this.mExifByteOrder));
            if (var9 == null || var9.length != 4) {
               StringBuilder var11 = new StringBuilder();
               var11.append("Invalid aspect frame values. frame=");
               var11.append(Arrays.toString(var9));
               Log.w("ExifInterface", var11.toString());
               return;
            }

            if (var9[2] > var9[0] && var9[3] > var9[1]) {
               int var5 = var9[2] - var9[0] + 1;
               int var4 = var9[3] - var9[1] + 1;
               int var3 = var5;
               int var2 = var4;
               if (var5 < var4) {
                  var3 = var5 + var4;
                  var2 = var3 - var4;
                  var3 -= var2;
               }

               var8 = ExifInterface.ExifAttribute.createUShort(var3, this.mExifByteOrder);
               var10 = ExifInterface.ExifAttribute.createUShort(var2, this.mExifByteOrder);
               this.mAttributes[0].put("ImageWidth", var8);
               this.mAttributes[0].put("ImageLength", var10);
               return;
            }
         }
      }

   }

   private void getRafAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      var1.skipBytes(84);
      byte[] var6 = new byte[4];
      byte[] var7 = new byte[4];
      var1.read(var6);
      var1.skipBytes(4);
      var1.read(var7);
      int var2 = ByteBuffer.wrap(var6).getInt();
      int var3 = ByteBuffer.wrap(var7).getInt();
      this.getJpegAttributes(var1, var2, 5);
      var1.seek((long)var3);
      var1.setByteOrder(ByteOrder.BIG_ENDIAN);
      var3 = var1.readInt();

      for(var2 = 0; var2 < var3; ++var2) {
         int var4 = var1.readUnsignedShort();
         int var5 = var1.readUnsignedShort();
         if (var4 == TAG_RAF_IMAGE_SIZE.number) {
            short var9 = var1.readShort();
            short var10 = var1.readShort();
            ExifInterface.ExifAttribute var8 = ExifInterface.ExifAttribute.createUShort(var9, this.mExifByteOrder);
            ExifInterface.ExifAttribute var11 = ExifInterface.ExifAttribute.createUShort(var10, this.mExifByteOrder);
            this.mAttributes[0].put("ImageLength", var8);
            this.mAttributes[0].put("ImageWidth", var11);
            return;
         }

         var1.skipBytes(var5);
      }

   }

   private void getRawAttributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.parseTiffHeaders(var1, var1.available());
      this.readImageFileDirectory(var1, 0);
      this.updateImageSizeValues(var1, 0);
      this.updateImageSizeValues(var1, 5);
      this.updateImageSizeValues(var1, 4);
      this.validateImages(var1);
      if (this.mMimeType == 8) {
         ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("MakerNote");
         if (var2 != null) {
            var1 = new ExifInterface.ByteOrderedDataInputStream(var2.bytes);
            var1.setByteOrder(this.mExifByteOrder);
            var1.seek(6L);
            this.readImageFileDirectory(var1, 9);
            var2 = (ExifInterface.ExifAttribute)this.mAttributes[9].get("ColorSpace");
            if (var2 != null) {
               this.mAttributes[1].put("ColorSpace", var2);
            }
         }
      }

   }

   private void getRw2Attributes(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      this.getRawAttributes(var1);
      if ((ExifInterface.ExifAttribute)this.mAttributes[0].get("JpgFromRaw") != null) {
         this.getJpegAttributes(var1, this.mRw2JpgFromRawOffset, 5);
      }

      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[0].get("ISO");
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PhotographicSensitivity");
      if (var3 != null && var2 == null) {
         this.mAttributes[1].put("PhotographicSensitivity", var3);
      }

   }

   private static Pair guessDataFormat(String var0) {
      boolean var5 = var0.contains(",");
      Integer var10 = 2;
      Integer var11 = -1;
      Pair var22;
      if (var5) {
         String[] var25 = var0.split(",", -1);
         var22 = guessDataFormat(var25[0]);
         if ((Integer)var22.first == 2) {
            return var22;
         } else {
            for(int var1 = 1; var1 < var25.length; ++var1) {
               Pair var13 = guessDataFormat(var25[var1]);
               int var2 = -1;
               byte var4 = -1;
               if (((Integer)var13.first).equals(var22.first) || ((Integer)var13.second).equals(var22.first)) {
                  var2 = (Integer)var22.first;
               }

               int var3 = var4;
               if ((Integer)var22.second != -1) {
                  label140: {
                     if (!((Integer)var13.first).equals(var22.second)) {
                        var3 = var4;
                        if (!((Integer)var13.second).equals(var22.second)) {
                           break label140;
                        }
                     }

                     var3 = (Integer)var22.second;
                  }
               }

               if (var2 == -1 && var3 == -1) {
                  return new Pair(var10, var11);
               }

               if (var2 == -1) {
                  var22 = new Pair(var3, var11);
               } else if (var3 == -1) {
                  var22 = new Pair(var2, var11);
               }
            }

            return var22;
         }
      } else {
         boolean var10001;
         if (var0.contains("/")) {
            String[] var23 = var0.split("/", -1);
            if (var23.length == 2) {
               long var6;
               long var8;
               try {
                  var6 = (long)Double.parseDouble(var23[0]);
                  var8 = (long)Double.parseDouble(var23[1]);
               } catch (NumberFormatException var18) {
                  var10001 = false;
                  return new Pair(var10, var11);
               }

               if (var6 >= 0L && var8 >= 0L) {
                  if (var6 <= 2147483647L && var8 <= 2147483647L) {
                     try {
                        return new Pair(10, 5);
                     } catch (NumberFormatException var16) {
                        var10001 = false;
                     }
                  } else {
                     try {
                        return new Pair(5, var11);
                     } catch (NumberFormatException var17) {
                        var10001 = false;
                     }
                  }
               } else {
                  try {
                     var22 = new Pair(10, var11);
                     return var22;
                  } catch (NumberFormatException var15) {
                     var10001 = false;
                  }
               }
            }

            return new Pair(var10, var11);
         } else {
            label139: {
               Long var12;
               try {
                  var12 = Long.parseLong(var0);
                  if (var12 >= 0L && var12 <= 65535L) {
                     return new Pair(3, 4);
                  }
               } catch (NumberFormatException var20) {
                  var10001 = false;
                  break label139;
               }

               try {
                  if (var12 < 0L) {
                     return new Pair(9, var11);
                  }
               } catch (NumberFormatException var21) {
                  var10001 = false;
                  break label139;
               }

               try {
                  Pair var24 = new Pair(4, var11);
                  return var24;
               } catch (NumberFormatException var19) {
                  var10001 = false;
               }
            }

            try {
               Double.parseDouble(var0);
               var22 = new Pair(12, var11);
               return var22;
            } catch (NumberFormatException var14) {
               return new Pair(var10, var11);
            }
         }
      }
   }

   private void handleThumbnailFromJfif(ExifInterface.ByteOrderedDataInputStream var1, HashMap var2) throws IOException {
      ExifInterface.ExifAttribute var7 = (ExifInterface.ExifAttribute)var2.get("JPEGInterchangeFormat");
      ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)var2.get("JPEGInterchangeFormatLength");
      if (var7 != null && var8 != null) {
         int var4 = var7.getIntValue(this.mExifByteOrder);
         int var5 = Math.min(var8.getIntValue(this.mExifByteOrder), var1.available() - var4);
         int var6 = this.mMimeType;
         int var3;
         if (var6 != 4 && var6 != 9 && var6 != 10) {
            var3 = var4;
            if (var6 == 7) {
               var3 = var4 + this.mOrfMakerNoteOffset;
            }
         } else {
            var3 = var4 + this.mExifOffset;
         }

         if (var3 > 0 && var5 > 0) {
            this.mHasThumbnail = true;
            this.mThumbnailOffset = var3;
            this.mThumbnailLength = var5;
            if (this.mFilename == null && this.mAssetInputStream == null) {
               byte[] var9 = new byte[var5];
               var1.seek((long)var3);
               var1.readFully(var9);
               this.mThumbnailBytes = var9;
            }
         }
      }

   }

   private void handleThumbnailFromStrips(ExifInterface.ByteOrderedDataInputStream var1, HashMap var2) throws IOException {
      ExifInterface.ExifAttribute var11 = (ExifInterface.ExifAttribute)var2.get("StripOffsets");
      ExifInterface.ExifAttribute var10 = (ExifInterface.ExifAttribute)var2.get("StripByteCounts");
      if (var11 != null && var10 != null) {
         long[] var16 = convertToLongArray(var11.getValue(this.mExifByteOrder));
         long[] var13 = convertToLongArray(var10.getValue(this.mExifByteOrder));
         if (var16 == null) {
            Log.w("ExifInterface", "stripOffsets should not be null.");
         } else if (var13 == null) {
            Log.w("ExifInterface", "stripByteCounts should not be null.");
         } else {
            long var8 = 0L;
            int var4 = var13.length;

            int var3;
            for(var3 = 0; var3 < var4; ++var3) {
               var8 += var13[var3];
            }

            byte[] var14 = new byte[(int)var8];
            var4 = 0;
            int var5 = 0;

            int var6;
            int var7;
            for(var3 = 0; var3 < var16.length; var4 = var4 + var7 + var6) {
               var7 = (int)var16[var3];
               var6 = (int)var13[var3];
               var7 -= var4;
               if (var7 < 0) {
                  Log.d("ExifInterface", "Invalid strip offset value");
               }

               var1.seek((long)var7);
               byte[] var15 = new byte[var6];
               var1.read(var15);
               System.arraycopy(var15, 0, var14, var5, var15.length);
               var5 += var15.length;
               ++var3;
            }

            this.mHasThumbnail = true;
            this.mThumbnailBytes = var14;
            this.mThumbnailLength = var14.length;
         }
      }
   }

   private static boolean isJpegFormat(byte[] var0) throws IOException {
      int var1 = 0;

      while(true) {
         byte[] var2 = JPEG_SIGNATURE;
         if (var1 >= var2.length) {
            return true;
         }

         if (var0[var1] != var2[var1]) {
            return false;
         }

         ++var1;
      }
   }

   private boolean isOrfFormat(byte[] var1) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var4 = new ExifInterface.ByteOrderedDataInputStream(var1);
      ByteOrder var3 = this.readByteOrder(var4);
      this.mExifByteOrder = var3;
      var4.setByteOrder(var3);
      short var2 = var4.readShort();
      var4.close();
      return var2 == 20306 || var2 == 21330;
   }

   private boolean isRafFormat(byte[] var1) throws IOException {
      byte[] var3 = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());

      for(int var2 = 0; var2 < var3.length; ++var2) {
         if (var1[var2] != var3[var2]) {
            return false;
         }
      }

      return true;
   }

   private boolean isRw2Format(byte[] var1) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var4 = new ExifInterface.ByteOrderedDataInputStream(var1);
      ByteOrder var3 = this.readByteOrder(var4);
      this.mExifByteOrder = var3;
      var4.setByteOrder(var3);
      short var2 = var4.readShort();
      var4.close();
      return var2 == 85;
   }

   private boolean isSupportedDataType(HashMap var1) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)var1.get("BitsPerSample");
      if (var3 != null) {
         int[] var5 = (int[])((int[])var3.getValue(this.mExifByteOrder));
         if (Arrays.equals(BITS_PER_SAMPLE_RGB, var5)) {
            return true;
         }

         if (this.mMimeType == 3) {
            ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var1.get("PhotometricInterpretation");
            if (var4 != null) {
               int var2 = var4.getIntValue(this.mExifByteOrder);
               if (var2 == 1 && Arrays.equals(var5, BITS_PER_SAMPLE_GREYSCALE_2) || var2 == 6 && Arrays.equals(var5, BITS_PER_SAMPLE_RGB)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean isThumbnail(HashMap var1) throws IOException {
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var1.get("ImageLength");
      ExifInterface.ExifAttribute var5 = (ExifInterface.ExifAttribute)var1.get("ImageWidth");
      if (var4 != null && var5 != null) {
         int var2 = var4.getIntValue(this.mExifByteOrder);
         int var3 = var5.getIntValue(this.mExifByteOrder);
         if (var2 <= 512 && var3 <= 512) {
            return true;
         }
      }

      return false;
   }

   private void loadAttributes(InputStream param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void parseTiffHeaders(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      ByteOrder var5 = this.readByteOrder(var1);
      this.mExifByteOrder = var5;
      var1.setByteOrder(var5);
      int var3 = var1.readUnsignedShort();
      int var4 = this.mMimeType;
      StringBuilder var6;
      if (var4 != 7 && var4 != 10 && var3 != 42) {
         var6 = new StringBuilder();
         var6.append("Invalid start code: ");
         var6.append(Integer.toHexString(var3));
         throw new IOException(var6.toString());
      } else {
         var3 = var1.readInt();
         if (var3 >= 8 && var3 < var2) {
            var2 = var3 - 8;
            if (var2 > 0) {
               if (var1.skipBytes(var2) != var2) {
                  var6 = new StringBuilder();
                  var6.append("Couldn't jump to first Ifd: ");
                  var6.append(var2);
                  throw new IOException(var6.toString());
               }
            }
         } else {
            var6 = new StringBuilder();
            var6.append("Invalid first Ifd offset: ");
            var6.append(var3);
            throw new IOException(var6.toString());
         }
      }
   }

   private void printAttributes() {
      for(int var1 = 0; var1 < this.mAttributes.length; ++var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append("The size of tag group[");
         var2.append(var1);
         var2.append("]: ");
         var2.append(this.mAttributes[var1].size());
         Log.d("ExifInterface", var2.toString());
         Iterator var6 = this.mAttributes[var1].entrySet().iterator();

         while(var6.hasNext()) {
            Entry var3 = (Entry)var6.next();
            ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var3.getValue();
            StringBuilder var5 = new StringBuilder();
            var5.append("tagName: ");
            var5.append((String)var3.getKey());
            var5.append(", tagType: ");
            var5.append(var4.toString());
            var5.append(", tagValue: '");
            var5.append(var4.getStringValue(this.mExifByteOrder));
            var5.append("'");
            Log.d("ExifInterface", var5.toString());
         }
      }

   }

   private ByteOrder readByteOrder(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      short var2 = var1.readShort();
      if (var2 != 18761) {
         if (var2 == 19789) {
            return ByteOrder.BIG_ENDIAN;
         } else {
            StringBuilder var3 = new StringBuilder();
            var3.append("Invalid byte order: ");
            var3.append(Integer.toHexString(var2));
            throw new IOException(var3.toString());
         }
      } else {
         return ByteOrder.LITTLE_ENDIAN;
      }
   }

   private void readExifSegment(byte[] var1, int var2) throws IOException {
      ExifInterface.ByteOrderedDataInputStream var3 = new ExifInterface.ByteOrderedDataInputStream(var1);
      this.parseTiffHeaders(var3, var1.length);
      this.readImageFileDirectory(var3, var2);
   }

   private void readImageFileDirectory(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      this.mAttributesOffsets.add(var1.mPosition);
      if (var1.mPosition + 2 <= var1.mLength) {
         short var4 = var1.readShort();
         if (var1.mPosition + var4 * 12 <= var1.mLength) {
            if (var4 > 0) {
               for(short var5 = 0; var5 < var4; ++var5) {
                  int var3;
                  int var6;
                  boolean var7;
                  int var9;
                  int var10;
                  long var11;
                  long var13;
                  ExifInterface.ExifTag var15;
                  label125: {
                     var9 = var1.readUnsignedShort();
                     var6 = var1.readUnsignedShort();
                     var10 = var1.readInt();
                     var13 = (long)var1.peek() + 4L;
                     var15 = (ExifInterface.ExifTag)sExifTagMapsForReading[var2].get(var9);
                     var7 = false;
                     StringBuilder var16;
                     if (var15 == null) {
                        var16 = new StringBuilder();
                        var16.append("Skip the tag entry since tag number is not defined: ");
                        var16.append(var9);
                        Log.w("ExifInterface", var16.toString());
                     } else if (var6 > 0 && var6 < IFD_FORMAT_BYTES_PER_FORMAT.length) {
                        if (var15.isFormatCompatible(var6)) {
                           var3 = var6;
                           if (var6 == 7) {
                              var3 = var15.primaryFormat;
                           }

                           var11 = (long)var10;
                           var11 = (long)IFD_FORMAT_BYTES_PER_FORMAT[var3] * var11;
                           if (var11 >= 0L && var11 <= 2147483647L) {
                              var7 = true;
                              break label125;
                           }

                           var16 = new StringBuilder();
                           var16.append("Skip the tag entry since the number of components is invalid: ");
                           var16.append(var10);
                           Log.w("ExifInterface", var16.toString());
                           break label125;
                        }

                        var16 = new StringBuilder();
                        var16.append("Skip the tag entry since data format (");
                        var16.append(IFD_FORMAT_NAMES[var6]);
                        var16.append(") is unexpected for tag: ");
                        var16.append(var15.name);
                        Log.w("ExifInterface", var16.toString());
                     } else {
                        var16 = new StringBuilder();
                        var16.append("Skip the tag entry since data format is invalid: ");
                        var16.append(var6);
                        Log.w("ExifInterface", var16.toString());
                     }

                     var11 = 0L;
                     var3 = var6;
                  }

                  if (!var7) {
                     var1.seek(var13);
                  } else {
                     StringBuilder var21;
                     ExifInterface.ExifAttribute var22;
                     if (var11 > 4L) {
                        var6 = var1.readInt();
                        int var20 = this.mMimeType;
                        if (var20 == 7) {
                           if ("MakerNote".equals(var15.name)) {
                              this.mOrfMakerNoteOffset = var6;
                           } else if (var2 == 6 && "ThumbnailImage".equals(var15.name)) {
                              this.mOrfThumbnailOffset = var6;
                              this.mOrfThumbnailLength = var10;
                              var22 = ExifInterface.ExifAttribute.createUShort(6, this.mExifByteOrder);
                              ExifInterface.ExifAttribute var17 = ExifInterface.ExifAttribute.createULong((long)this.mOrfThumbnailOffset, this.mExifByteOrder);
                              ExifInterface.ExifAttribute var18 = ExifInterface.ExifAttribute.createULong((long)this.mOrfThumbnailLength, this.mExifByteOrder);
                              this.mAttributes[4].put("Compression", var22);
                              this.mAttributes[4].put("JPEGInterchangeFormat", var17);
                              this.mAttributes[4].put("JPEGInterchangeFormatLength", var18);
                           }
                        } else if (var20 == 10 && "JpgFromRaw".equals(var15.name)) {
                           this.mRw2JpgFromRawOffset = var6;
                        }

                        if ((long)var6 + var11 > (long)var1.mLength) {
                           var21 = new StringBuilder();
                           var21.append("Skip the tag entry since data offset is invalid: ");
                           var21.append(var6);
                           Log.w("ExifInterface", var21.toString());
                           var1.seek(var13);
                           continue;
                        }

                        var1.seek((long)var6);
                     }

                     Integer var23 = (Integer)sExifPointerTagMap.get(var9);
                     if (var23 != null) {
                        var11 = -1L;
                        if (var3 != 3) {
                           if (var3 != 4) {
                              if (var3 != 8) {
                                 if (var3 == 9 || var3 == 13) {
                                    var11 = (long)var1.readInt();
                                 }
                              } else {
                                 var11 = (long)var1.readShort();
                              }
                           } else {
                              var11 = var1.readUnsignedInt();
                           }
                        } else {
                           var11 = (long)var1.readUnsignedShort();
                        }

                        if (var11 > 0L && var11 < (long)var1.mLength) {
                           if (!this.mAttributesOffsets.contains((int)var11)) {
                              var1.seek(var11);
                              this.readImageFileDirectory(var1, var23);
                           } else {
                              var21 = new StringBuilder();
                              var21.append("Skip jump into the IFD since it has already been read: IfdType ");
                              var21.append(var23);
                              var21.append(" (at ");
                              var21.append(var11);
                              var21.append(")");
                              Log.w("ExifInterface", var21.toString());
                           }
                        } else {
                           var21 = new StringBuilder();
                           var21.append("Skip jump into the IFD since its offset is invalid: ");
                           var21.append(var11);
                           Log.w("ExifInterface", var21.toString());
                        }

                        var1.seek(var13);
                     } else {
                        byte[] var24 = new byte[(int)var11];
                        var1.readFully(var24);
                        var22 = new ExifInterface.ExifAttribute(var3, var10, var24);
                        this.mAttributes[var2].put(var15.name, var22);
                        if ("DNGVersion".equals(var15.name)) {
                           this.mMimeType = 3;
                        }

                        if (("Make".equals(var15.name) || "Model".equals(var15.name)) && var22.getStringValue(this.mExifByteOrder).contains("PENTAX") || "Compression".equals(var15.name) && var22.getIntValue(this.mExifByteOrder) == 65535) {
                           this.mMimeType = 8;
                        }

                        if ((long)var1.peek() != var13) {
                           var1.seek(var13);
                        }
                     }
                  }
               }

               if (var1.peek() + 4 <= var1.mLength) {
                  var2 = var1.readInt();
                  StringBuilder var19;
                  if ((long)var2 > 0L && var2 < var1.mLength) {
                     if (this.mAttributesOffsets.contains(var2)) {
                        var19 = new StringBuilder();
                        var19.append("Stop reading file since re-reading an IFD may cause an infinite loop: ");
                        var19.append(var2);
                        Log.w("ExifInterface", var19.toString());
                        return;
                     }

                     var1.seek((long)var2);
                     if (this.mAttributes[4].isEmpty()) {
                        this.readImageFileDirectory(var1, 4);
                        return;
                     }

                     if (this.mAttributes[5].isEmpty()) {
                        this.readImageFileDirectory(var1, 5);
                        return;
                     }
                  } else {
                     var19 = new StringBuilder();
                     var19.append("Stop reading file since a wrong offset may cause an infinite loop: ");
                     var19.append(var2);
                     Log.w("ExifInterface", var19.toString());
                  }
               }

            }
         }
      }
   }

   private void removeAttribute(String var1) {
      for(int var2 = 0; var2 < EXIF_TAGS.length; ++var2) {
         this.mAttributes[var2].remove(var1);
      }

   }

   private void retrieveJpegImageSize(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageLength");
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageWidth");
      if (var3 == null || var4 == null) {
         var3 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("JPEGInterchangeFormat");
         if (var3 != null) {
            this.getJpegAttributes(var1, var3.getIntValue(this.mExifByteOrder), var2);
         }
      }

   }

   private void saveJpegAttributes(InputStream var1, OutputStream var2) throws IOException {
      DataInputStream var7 = new DataInputStream(var1);
      ExifInterface.ByteOrderedDataOutputStream var8 = new ExifInterface.ByteOrderedDataOutputStream(var2, ByteOrder.BIG_ENDIAN);
      if (var7.readByte() != -1) {
         throw new IOException("Invalid marker");
      } else {
         var8.writeByte(-1);
         if (var7.readByte() != -40) {
            throw new IOException("Invalid marker");
         } else {
            var8.writeByte(-40);
            var8.writeByte(-1);
            var8.writeByte(-31);
            this.writeExifSegment(var8, 6);
            byte[] var5 = new byte[4096];

            while(var7.readByte() == -1) {
               byte var3 = var7.readByte();
               if (var3 == -39 || var3 == -38) {
                  var8.writeByte(-1);
                  var8.writeByte(var3);
                  copy(var7, var8);
                  return;
               }

               int var4;
               int var9;
               if (var3 != -31) {
                  var8.writeByte(-1);
                  var8.writeByte(var3);
                  var9 = var7.readUnsignedShort();
                  var8.writeUnsignedShort(var9);
                  var9 -= 2;
                  if (var9 < 0) {
                     throw new IOException("Invalid length");
                  }

                  while(var9 > 0) {
                     var4 = var7.read(var5, 0, Math.min(var9, var5.length));
                     if (var4 < 0) {
                        break;
                     }

                     var8.write(var5, 0, var4);
                     var9 -= var4;
                  }
               } else {
                  var4 = var7.readUnsignedShort() - 2;
                  if (var4 < 0) {
                     throw new IOException("Invalid length");
                  }

                  byte[] var6 = new byte[6];
                  if (var4 >= 6) {
                     if (var7.read(var6) != 6) {
                        throw new IOException("Invalid exif");
                     }

                     if (Arrays.equals(var6, IDENTIFIER_EXIF_APP1)) {
                        if (var7.skipBytes(var4 - 6) != var4 - 6) {
                           throw new IOException("Invalid length");
                        }
                        continue;
                     }
                  }

                  var8.writeByte(-1);
                  var8.writeByte(var3);
                  var8.writeUnsignedShort(var4 + 2);
                  var9 = var4;
                  if (var4 >= 6) {
                     var9 = var4 - 6;
                     var8.write(var6);
                  }

                  while(var9 > 0) {
                     var4 = var7.read(var5, 0, Math.min(var9, var5.length));
                     if (var4 < 0) {
                        break;
                     }

                     var8.write(var5, 0, var4);
                     var9 -= var4;
                  }
               }
            }

            throw new IOException("Invalid marker");
         }
      }
   }

   private void setThumbnailData(ExifInterface.ByteOrderedDataInputStream var1) throws IOException {
      HashMap var3 = this.mAttributes[4];
      ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)var3.get("Compression");
      if (var4 == null) {
         this.mThumbnailCompression = 6;
         this.handleThumbnailFromJfif(var1, var3);
      } else {
         int var2 = var4.getIntValue(this.mExifByteOrder);
         this.mThumbnailCompression = var2;
         if (var2 != 1) {
            if (var2 == 6) {
               this.handleThumbnailFromJfif(var1, var3);
               return;
            }

            if (var2 != 7) {
               return;
            }
         }

         if (this.isSupportedDataType(var3)) {
            this.handleThumbnailFromStrips(var1, var3);
         }

      }
   }

   private void swapBasedOnImageSize(int var1, int var2) throws IOException {
      if (!this.mAttributes[var1].isEmpty()) {
         if (!this.mAttributes[var2].isEmpty()) {
            ExifInterface.ExifAttribute var7 = (ExifInterface.ExifAttribute)this.mAttributes[var1].get("ImageLength");
            ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)this.mAttributes[var1].get("ImageWidth");
            ExifInterface.ExifAttribute var9 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageLength");
            ExifInterface.ExifAttribute var10 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("ImageWidth");
            if (var7 != null) {
               if (var8 == null) {
                  return;
               }

               if (var9 != null) {
                  if (var10 == null) {
                     return;
                  }

                  int var3 = var7.getIntValue(this.mExifByteOrder);
                  int var4 = var8.getIntValue(this.mExifByteOrder);
                  int var5 = var9.getIntValue(this.mExifByteOrder);
                  int var6 = var10.getIntValue(this.mExifByteOrder);
                  if (var3 < var5 && var4 < var6) {
                     HashMap[] var11 = this.mAttributes;
                     HashMap var12 = var11[var1];
                     var11[var1] = var11[var2];
                     var11[var2] = var12;
                  }
               }
            }

         }
      }
   }

   private boolean updateAttribute(String var1, ExifInterface.ExifAttribute var2) {
      boolean var4 = false;

      for(int var3 = 0; var3 < EXIF_TAGS.length; ++var3) {
         if (this.mAttributes[var3].containsKey(var1)) {
            this.mAttributes[var3].put(var1, var2);
            var4 = true;
         }
      }

      return var4;
   }

   private void updateImageSizeValues(ExifInterface.ByteOrderedDataInputStream var1, int var2) throws IOException {
      ExifInterface.ExifAttribute var7 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("DefaultCropSize");
      ExifInterface.ExifAttribute var8 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorTopBorder");
      ExifInterface.ExifAttribute var9 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorLeftBorder");
      ExifInterface.ExifAttribute var10 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorBottomBorder");
      ExifInterface.ExifAttribute var11 = (ExifInterface.ExifAttribute)this.mAttributes[var2].get("SensorRightBorder");
      ExifInterface.ExifAttribute var13;
      if (var7 == null) {
         if (var8 != null && var9 != null && var10 != null && var11 != null) {
            int var3 = var8.getIntValue(this.mExifByteOrder);
            int var4 = var10.getIntValue(this.mExifByteOrder);
            int var5 = var11.getIntValue(this.mExifByteOrder);
            int var6 = var9.getIntValue(this.mExifByteOrder);
            if (var4 > var3 && var5 > var6) {
               var13 = ExifInterface.ExifAttribute.createUShort(var4 - var3, this.mExifByteOrder);
               var7 = ExifInterface.ExifAttribute.createUShort(var5 - var6, this.mExifByteOrder);
               this.mAttributes[var2].put("ImageLength", var13);
               this.mAttributes[var2].put("ImageWidth", var7);
            }

         } else {
            this.retrieveJpegImageSize(var1, var2);
         }
      } else {
         StringBuilder var15;
         if (var7.format == 5) {
            ExifInterface.Rational[] var14 = (ExifInterface.Rational[])((ExifInterface.Rational[])var7.getValue(this.mExifByteOrder));
            if (var14 == null || var14.length != 2) {
               var15 = new StringBuilder();
               var15.append("Invalid crop size values. cropSize=");
               var15.append(Arrays.toString(var14));
               Log.w("ExifInterface", var15.toString());
               return;
            }

            var7 = ExifInterface.ExifAttribute.createURational(var14[0], this.mExifByteOrder);
            var13 = ExifInterface.ExifAttribute.createURational(var14[1], this.mExifByteOrder);
         } else {
            int[] var12 = (int[])((int[])var7.getValue(this.mExifByteOrder));
            if (var12 == null || var12.length != 2) {
               var15 = new StringBuilder();
               var15.append("Invalid crop size values. cropSize=");
               var15.append(Arrays.toString(var12));
               Log.w("ExifInterface", var15.toString());
               return;
            }

            var7 = ExifInterface.ExifAttribute.createUShort(var12[0], this.mExifByteOrder);
            var13 = ExifInterface.ExifAttribute.createUShort(var12[1], this.mExifByteOrder);
         }

         this.mAttributes[var2].put("ImageWidth", var7);
         this.mAttributes[var2].put("ImageLength", var13);
      }
   }

   private void validateImages(InputStream var1) throws IOException {
      this.swapBasedOnImageSize(0, 5);
      this.swapBasedOnImageSize(0, 4);
      this.swapBasedOnImageSize(5, 4);
      ExifInterface.ExifAttribute var3 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PixelXDimension");
      ExifInterface.ExifAttribute var2 = (ExifInterface.ExifAttribute)this.mAttributes[1].get("PixelYDimension");
      if (var3 != null && var2 != null) {
         this.mAttributes[0].put("ImageWidth", var3);
         this.mAttributes[0].put("ImageLength", var2);
      }

      if (this.mAttributes[4].isEmpty() && this.isThumbnail(this.mAttributes[5])) {
         HashMap[] var4 = this.mAttributes;
         var4[4] = var4[5];
         var4[5] = new HashMap();
      }

      if (!this.isThumbnail(this.mAttributes[4])) {
         Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
      }

   }

   private int writeExifSegment(ExifInterface.ByteOrderedDataOutputStream var1, int var2) throws IOException {
      ExifInterface.ExifTag[][] var9 = EXIF_TAGS;
      int[] var8 = new int[var9.length];
      int[] var12 = new int[var9.length];
      ExifInterface.ExifTag[] var10 = EXIF_POINTER_TAGS;
      int var5 = var10.length;

      int var4;
      for(var4 = 0; var4 < var5; ++var4) {
         this.removeAttribute(var10[var4].name);
      }

      this.removeAttribute(JPEG_INTERCHANGE_FORMAT_TAG.name);
      this.removeAttribute(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name);

      int var6;
      for(var4 = 0; var4 < EXIF_TAGS.length; ++var4) {
         Object[] var14 = this.mAttributes[var4].entrySet().toArray();
         var6 = var14.length;

         for(var5 = 0; var5 < var6; ++var5) {
            Entry var11 = (Entry)var14[var5];
            if (var11.getValue() == null) {
               this.mAttributes[var4].remove(var11.getKey());
            }
         }
      }

      if (!this.mAttributes[1].isEmpty()) {
         this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (!this.mAttributes[2].isEmpty()) {
         this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (!this.mAttributes[3].isEmpty()) {
         this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
      }

      if (this.mHasThumbnail) {
         this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifInterface.ExifAttribute.createULong(0L, this.mExifByteOrder));
         this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_LENGTH_TAG.name, ExifInterface.ExifAttribute.createULong((long)this.mThumbnailLength, this.mExifByteOrder));
      }

      int var7;
      for(var4 = 0; var4 < EXIF_TAGS.length; ++var4) {
         var5 = 0;

         for(Iterator var15 = this.mAttributes[var4].entrySet().iterator(); var15.hasNext(); var5 = var6) {
            var7 = ((ExifInterface.ExifAttribute)((Entry)var15.next()).getValue()).size();
            var6 = var5;
            if (var7 > 4) {
               var6 = var5 + var7;
            }
         }

         var12[var4] += var5;
      }

      var4 = 8;

      for(var5 = 0; var5 < EXIF_TAGS.length; var4 = var6) {
         var6 = var4;
         if (!this.mAttributes[var5].isEmpty()) {
            var8[var5] = var4;
            var6 = var4 + this.mAttributes[var5].size() * 12 + 2 + 4 + var12[var5];
         }

         ++var5;
      }

      var5 = var4;
      if (this.mHasThumbnail) {
         this.mAttributes[4].put(JPEG_INTERCHANGE_FORMAT_TAG.name, ExifInterface.ExifAttribute.createULong((long)var4, this.mExifByteOrder));
         this.mThumbnailOffset = var2 + var4;
         var5 = var4 + this.mThumbnailLength;
      }

      var7 = var5 + 8;
      if (!this.mAttributes[1].isEmpty()) {
         this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifInterface.ExifAttribute.createULong((long)var8[1], this.mExifByteOrder));
      }

      if (!this.mAttributes[2].isEmpty()) {
         this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifInterface.ExifAttribute.createULong((long)var8[2], this.mExifByteOrder));
      }

      if (!this.mAttributes[3].isEmpty()) {
         this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifInterface.ExifAttribute.createULong((long)var8[3], this.mExifByteOrder));
      }

      var1.writeUnsignedShort(var7);
      var1.write(IDENTIFIER_EXIF_APP1);
      short var3;
      if (this.mExifByteOrder == ByteOrder.BIG_ENDIAN) {
         var3 = 19789;
      } else {
         var3 = 18761;
      }

      var1.writeShort(var3);
      var1.setByteOrder(this.mExifByteOrder);
      var1.writeUnsignedShort(42);
      var1.writeUnsignedInt(8L);

      for(var2 = 0; var2 < EXIF_TAGS.length; ++var2) {
         if (!this.mAttributes[var2].isEmpty()) {
            var1.writeUnsignedShort(this.mAttributes[var2].size());
            var4 = var8[var2] + 2 + this.mAttributes[var2].size() * 12 + 4;

            Iterator var13;
            ExifInterface.ExifAttribute var17;
            for(var13 = this.mAttributes[var2].entrySet().iterator(); var13.hasNext(); var4 = var5) {
               Entry var16 = (Entry)var13.next();
               var5 = ((ExifInterface.ExifTag)sExifTagMapsForWriting[var2].get(var16.getKey())).number;
               var17 = (ExifInterface.ExifAttribute)var16.getValue();
               var6 = var17.size();
               var1.writeUnsignedShort(var5);
               var1.writeUnsignedShort(var17.format);
               var1.writeInt(var17.numberOfComponents);
               if (var6 > 4) {
                  var1.writeUnsignedInt((long)var4);
                  var5 = var4 + var6;
               } else {
                  var1.write(var17.bytes);
                  var5 = var4;
                  if (var6 < 4) {
                     while(true) {
                        var5 = var4;
                        if (var6 >= 4) {
                           break;
                        }

                        var1.writeByte(0);
                        ++var6;
                     }
                  }
               }
            }

            if (var2 == 0 && !this.mAttributes[4].isEmpty()) {
               var1.writeUnsignedInt((long)var8[4]);
            } else {
               var1.writeUnsignedInt(0L);
            }

            var13 = this.mAttributes[var2].entrySet().iterator();

            while(var13.hasNext()) {
               var17 = (ExifInterface.ExifAttribute)((Entry)var13.next()).getValue();
               if (var17.bytes.length > 4) {
                  var1.write(var17.bytes, 0, var17.bytes.length);
               }
            }
         }
      }

      if (this.mHasThumbnail) {
         var1.write(this.getThumbnailBytes());
      }

      var1.setByteOrder(ByteOrder.BIG_ENDIAN);
      return var7;
   }

   public void flipHorizontally() {
      byte var1;
      switch(this.getAttributeInt("Orientation", 1)) {
      case 1:
         var1 = 2;
         break;
      case 2:
         var1 = 1;
         break;
      case 3:
         var1 = 4;
         break;
      case 4:
         var1 = 3;
         break;
      case 5:
         var1 = 6;
         break;
      case 6:
         var1 = 5;
         break;
      case 7:
         var1 = 8;
         break;
      case 8:
         var1 = 7;
         break;
      default:
         var1 = 0;
      }

      this.setAttribute("Orientation", Integer.toString(var1));
   }

   public void flipVertically() {
      byte var1;
      switch(this.getAttributeInt("Orientation", 1)) {
      case 1:
         var1 = 4;
         break;
      case 2:
         var1 = 3;
         break;
      case 3:
         var1 = 2;
         break;
      case 4:
         var1 = 1;
         break;
      case 5:
         var1 = 8;
         break;
      case 6:
         var1 = 7;
         break;
      case 7:
         var1 = 6;
         break;
      case 8:
         var1 = 5;
         break;
      default:
         var1 = 0;
      }

      this.setAttribute("Orientation", Integer.toString(var1));
   }

   public double getAltitude(double var1) {
      double var3 = this.getAttributeDouble("GPSAltitude", -1.0D);
      byte var5 = -1;
      int var6 = this.getAttributeInt("GPSAltitudeRef", -1);
      if (var3 >= 0.0D && var6 >= 0) {
         if (var6 != 1) {
            var5 = 1;
         }

         return (double)var5 * var3;
      } else {
         return var1;
      }
   }

   public String getAttribute(String var1) {
      ExifInterface.ExifAttribute var2 = this.getExifAttribute(var1);
      if (var2 != null) {
         if (!sTagSetForCompatibility.contains(var1)) {
            return var2.getStringValue(this.mExifByteOrder);
         } else if (var1.equals("GPSTimeStamp")) {
            if (var2.format != 5 && var2.format != 10) {
               StringBuilder var5 = new StringBuilder();
               var5.append("GPS Timestamp format is not rational. format=");
               var5.append(var2.format);
               Log.w("ExifInterface", var5.toString());
               return null;
            } else {
               ExifInterface.Rational[] var4 = (ExifInterface.Rational[])((ExifInterface.Rational[])var2.getValue(this.mExifByteOrder));
               if (var4 != null && var4.length == 3) {
                  return String.format("%02d:%02d:%02d", (int)((float)var4[0].numerator / (float)var4[0].denominator), (int)((float)var4[1].numerator / (float)var4[1].denominator), (int)((float)var4[2].numerator / (float)var4[2].denominator));
               } else {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Invalid GPS Timestamp array. array=");
                  var6.append(Arrays.toString(var4));
                  Log.w("ExifInterface", var6.toString());
                  return null;
               }
            }
         } else {
            try {
               var1 = Double.toString(var2.getDoubleValue(this.mExifByteOrder));
               return var1;
            } catch (NumberFormatException var3) {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public double getAttributeDouble(String var1, double var2) {
      ExifInterface.ExifAttribute var7 = this.getExifAttribute(var1);
      if (var7 == null) {
         return var2;
      } else {
         try {
            double var4 = var7.getDoubleValue(this.mExifByteOrder);
            return var4;
         } catch (NumberFormatException var6) {
            return var2;
         }
      }
   }

   public int getAttributeInt(String var1, int var2) {
      ExifInterface.ExifAttribute var5 = this.getExifAttribute(var1);
      if (var5 == null) {
         return var2;
      } else {
         try {
            int var3 = var5.getIntValue(this.mExifByteOrder);
            return var3;
         } catch (NumberFormatException var4) {
            return var2;
         }
      }
   }

   public long getDateTime() {
      // $FF: Couldn't be decompiled
   }

   public long getGpsDateTime() {
      String var3 = this.getAttribute("GPSDateStamp");
      String var4 = this.getAttribute("GPSTimeStamp");
      if (var3 != null && var4 != null) {
         if (!sNonZeroTimePattern.matcher(var3).matches() && !sNonZeroTimePattern.matcher(var4).matches()) {
            return -1L;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append(var3);
            var5.append(' ');
            var5.append(var4);
            var3 = var5.toString();
            ParsePosition var9 = new ParsePosition(0);

            boolean var10001;
            Date var8;
            try {
               var8 = sFormatter.parse(var3, var9);
            } catch (IllegalArgumentException var7) {
               var10001 = false;
               return -1L;
            }

            if (var8 == null) {
               return -1L;
            } else {
               try {
                  long var1 = var8.getTime();
                  return var1;
               } catch (IllegalArgumentException var6) {
                  var10001 = false;
                  return -1L;
               }
            }
         }
      } else {
         return -1L;
      }
   }

   @Deprecated
   public boolean getLatLong(float[] var1) {
      double[] var2 = this.getLatLong();
      if (var2 == null) {
         return false;
      } else {
         var1[0] = (float)var2[0];
         var1[1] = (float)var2[1];
         return true;
      }
   }

   public double[] getLatLong() {
      String var5 = this.getAttribute("GPSLatitude");
      String var6 = this.getAttribute("GPSLatitudeRef");
      String var7 = this.getAttribute("GPSLongitude");
      String var8 = this.getAttribute("GPSLongitudeRef");
      if (var5 != null && var6 != null && var7 != null && var8 != null) {
         double var1;
         double var3;
         try {
            var1 = convertRationalLatLonToDouble(var5, var6);
            var3 = convertRationalLatLonToDouble(var7, var8);
         } catch (IllegalArgumentException var10) {
            StringBuilder var9 = new StringBuilder();
            var9.append("Latitude/longitude values are not parseable. ");
            var9.append(String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", var5, var6, var7, var8));
            Log.w("ExifInterface", var9.toString());
            return null;
         }

         return new double[]{var1, var3};
      } else {
         return null;
      }
   }

   public int getRotationDegrees() {
      switch(this.getAttributeInt("Orientation", 1)) {
      case 3:
      case 4:
         return 180;
      case 5:
      case 8:
         return 270;
      case 6:
      case 7:
         return 90;
      default:
         return 0;
      }
   }

   public byte[] getThumbnail() {
      int var1 = this.mThumbnailCompression;
      return var1 != 6 && var1 != 7 ? null : this.getThumbnailBytes();
   }

   public Bitmap getThumbnailBitmap() {
      if (!this.mHasThumbnail) {
         return null;
      } else {
         if (this.mThumbnailBytes == null) {
            this.mThumbnailBytes = this.getThumbnailBytes();
         }

         int var1 = this.mThumbnailCompression;
         if (var1 != 6 && var1 != 7) {
            if (var1 == 1) {
               int[] var2 = new int[this.mThumbnailBytes.length / 3];

               for(var1 = 0; var1 < var2.length; ++var1) {
                  byte[] var3 = this.mThumbnailBytes;
                  var2[var1] = (var3[var1 * 3] << 16) + 0 + (var3[var1 * 3 + 1] << 8) + var3[var1 * 3 + 2];
               }

               ExifInterface.ExifAttribute var5 = (ExifInterface.ExifAttribute)this.mAttributes[4].get("ImageLength");
               ExifInterface.ExifAttribute var4 = (ExifInterface.ExifAttribute)this.mAttributes[4].get("ImageWidth");
               if (var5 != null && var4 != null) {
                  var1 = var5.getIntValue(this.mExifByteOrder);
                  return Bitmap.createBitmap(var2, var4.getIntValue(this.mExifByteOrder), var1, Config.ARGB_8888);
               }
            }

            return null;
         } else {
            return BitmapFactory.decodeByteArray(this.mThumbnailBytes, 0, this.mThumbnailLength);
         }
      }
   }

   public byte[] getThumbnailBytes() {
      // $FF: Couldn't be decompiled
   }

   public long[] getThumbnailRange() {
      return !this.mHasThumbnail ? null : new long[]{(long)this.mThumbnailOffset, (long)this.mThumbnailLength};
   }

   public boolean hasThumbnail() {
      return this.mHasThumbnail;
   }

   public boolean isFlipped() {
      int var1 = this.getAttributeInt("Orientation", 1);
      return var1 == 2 || var1 == 7 || var1 == 4 || var1 == 5;
   }

   public boolean isThumbnailCompressed() {
      int var1 = this.mThumbnailCompression;
      return var1 == 6 || var1 == 7;
   }

   public void resetOrientation() {
      this.setAttribute("Orientation", Integer.toString(1));
   }

   public void rotate(int var1) {
      if (var1 % 90 == 0) {
         int var4 = this.getAttributeInt("Orientation", 1);
         boolean var5 = ROTATION_ORDER.contains(var4);
         byte var3 = 0;
         byte var2 = 0;
         byte var6;
         if (var5) {
            int var8 = ROTATION_ORDER.indexOf(var4);
            var8 = (var1 / 90 + var8) % 4;
            var6 = var2;
            if (var8 < 0) {
               var6 = 4;
            }

            var1 = (Integer)ROTATION_ORDER.get(var8 + var6);
         } else if (FLIPPED_ROTATION_ORDER.contains(var4)) {
            int var7 = FLIPPED_ROTATION_ORDER.indexOf(var4);
            var7 = (var1 / 90 + var7) % 4;
            var6 = var3;
            if (var7 < 0) {
               var6 = 4;
            }

            var1 = (Integer)FLIPPED_ROTATION_ORDER.get(var7 + var6);
         } else {
            var1 = 0;
         }

         this.setAttribute("Orientation", Integer.toString(var1));
      } else {
         throw new IllegalArgumentException("degree should be a multiple of 90");
      }
   }

   public void saveAttributes() throws IOException {
      if (this.mIsSupportedFile && this.mMimeType == 4) {
         if (this.mFilename == null) {
            throw new IOException("ExifInterface does not support saving attributes for the current input.");
         } else {
            this.mThumbnailBytes = this.getThumbnail();
            StringBuilder var1 = new StringBuilder();
            var1.append(this.mFilename);
            var1.append(".tmp");
            File var5 = new File(var1.toString());
            if ((new File(this.mFilename)).renameTo(var5)) {
               FileInputStream var18 = null;
               FileOutputStream var3 = null;
               FileOutputStream var2 = var3;

               FileInputStream var4;
               label173: {
                  Throwable var10000;
                  label184: {
                     boolean var10001;
                     try {
                        var4 = new FileInputStream(var5);
                     } catch (Throwable var17) {
                        var10000 = var17;
                        var10001 = false;
                        break label184;
                     }

                     var18 = var4;
                     var2 = var3;

                     try {
                        var3 = new FileOutputStream(this.mFilename);
                     } catch (Throwable var16) {
                        var10000 = var16;
                        var10001 = false;
                        break label184;
                     }

                     var18 = var4;
                     var2 = var3;

                     label164:
                     try {
                        this.saveJpegAttributes(var4, var3);
                        break label173;
                     } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break label164;
                     }
                  }

                  Throwable var19 = var10000;
                  closeQuietly(var18);
                  closeQuietly(var2);
                  var5.delete();
                  throw var19;
               }

               closeQuietly(var4);
               closeQuietly(var3);
               var5.delete();
               this.mThumbnailBytes = null;
            } else {
               var1 = new StringBuilder();
               var1.append("Could not rename to ");
               var1.append(var5.getAbsolutePath());
               throw new IOException(var1.toString());
            }
         }
      } else {
         throw new IOException("ExifInterface only supports saving attributes on JPEG formats.");
      }
   }

   public void setAltitude(double var1) {
      String var3;
      if (var1 >= 0.0D) {
         var3 = "0";
      } else {
         var3 = "1";
      }

      this.setAttribute("GPSAltitude", (new ExifInterface.Rational(Math.abs(var1))).toString());
      this.setAttribute("GPSAltitudeRef", var3);
   }

   public void setAttribute(String var1, String var2) {
      String var5 = var1;
      if ("ISOSpeedRatings".equals(var1)) {
         var5 = "PhotographicSensitivity";
      }

      var1 = var2;
      StringBuilder var12;
      if (var2 != null) {
         var1 = var2;
         if (sTagSetForCompatibility.contains(var5)) {
            StringBuilder var13;
            if (var5.equals("GPSTimeStamp")) {
               Matcher var11 = sGpsTimestampPattern.matcher(var2);
               if (!var11.find()) {
                  var13 = new StringBuilder();
                  var13.append("Invalid value for ");
                  var13.append(var5);
                  var13.append(" : ");
                  var13.append(var2);
                  Log.w("ExifInterface", var13.toString());
                  return;
               }

               var12 = new StringBuilder();
               var12.append(Integer.parseInt(var11.group(1)));
               var12.append("/1,");
               var12.append(Integer.parseInt(var11.group(2)));
               var12.append("/1,");
               var12.append(Integer.parseInt(var11.group(3)));
               var12.append("/1");
               var1 = var12.toString();
            } else {
               try {
                  var1 = (new ExifInterface.Rational(Double.parseDouble(var2))).toString();
               } catch (NumberFormatException var10) {
                  var13 = new StringBuilder();
                  var13.append("Invalid value for ");
                  var13.append(var5);
                  var13.append(" : ");
                  var13.append(var2);
                  Log.w("ExifInterface", var13.toString());
                  return;
               }
            }
         }
      }

      for(int var3 = 0; var3 < EXIF_TAGS.length; ++var3) {
         if (var3 != 4 || this.mHasThumbnail) {
            ExifInterface.ExifTag var7 = (ExifInterface.ExifTag)sExifTagMapsForWriting[var3].get(var5);
            if (var7 != null) {
               if (var1 == null) {
                  this.mAttributes[var3].remove(var5);
               } else {
                  Pair var6 = guessDataFormat(var1);
                  int var4;
                  if (var7.primaryFormat != (Integer)var6.first && var7.primaryFormat != (Integer)var6.second) {
                     if (var7.secondaryFormat == -1 || var7.secondaryFormat != (Integer)var6.first && var7.secondaryFormat != (Integer)var6.second) {
                        if (var7.primaryFormat != 1 && var7.primaryFormat != 7 && var7.primaryFormat != 2) {
                           StringBuilder var21 = new StringBuilder();
                           var21.append("Given tag (");
                           var21.append(var5);
                           var21.append(") value didn't match with one of expected ");
                           var21.append("formats: ");
                           var21.append(IFD_FORMAT_NAMES[var7.primaryFormat]);
                           var4 = var7.secondaryFormat;
                           String var20 = "";
                           if (var4 == -1) {
                              var2 = "";
                           } else {
                              var12 = new StringBuilder();
                              var12.append(", ");
                              var12.append(IFD_FORMAT_NAMES[var7.secondaryFormat]);
                              var2 = var12.toString();
                           }

                           var21.append(var2);
                           var21.append(" (guess: ");
                           var21.append(IFD_FORMAT_NAMES[(Integer)var6.first]);
                           if ((Integer)var6.second == -1) {
                              var2 = var20;
                           } else {
                              var12 = new StringBuilder();
                              var12.append(", ");
                              var12.append(IFD_FORMAT_NAMES[(Integer)var6.second]);
                              var2 = var12.toString();
                           }

                           var21.append(var2);
                           var21.append(")");
                           Log.w("ExifInterface", var21.toString());
                           continue;
                        }

                        var4 = var7.primaryFormat;
                     } else {
                        var4 = var7.secondaryFormat;
                     }
                  } else {
                     var4 = var7.primaryFormat;
                  }

                  String[] var14;
                  int[] var17;
                  String[] var19;
                  switch(var4) {
                  case 1:
                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createByte(var1));
                     break;
                  case 2:
                  case 7:
                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createString(var1));
                     break;
                  case 3:
                     var14 = var1.split(",", -1);
                     var17 = new int[var14.length];

                     for(var4 = 0; var4 < var14.length; ++var4) {
                        var17[var4] = Integer.parseInt(var14[var4]);
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createUShort(var17, this.mExifByteOrder));
                     break;
                  case 4:
                     var14 = var1.split(",", -1);
                     long[] var18 = new long[var14.length];

                     for(var4 = 0; var4 < var14.length; ++var4) {
                        var18[var4] = Long.parseLong(var14[var4]);
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createULong(var18, this.mExifByteOrder));
                     break;
                  case 5:
                     String[] var8 = var1.split(",", -1);
                     ExifInterface.Rational[] var9 = new ExifInterface.Rational[var8.length];

                     for(var4 = 0; var4 < var8.length; ++var4) {
                        var19 = var8[var4].split("/", -1);
                        var9[var4] = new ExifInterface.Rational((long)Double.parseDouble(var19[0]), (long)Double.parseDouble(var19[1]));
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createURational(var9, this.mExifByteOrder));
                     break;
                  case 6:
                  case 8:
                  case 11:
                  default:
                     var12 = new StringBuilder();
                     var12.append("Data format isn't one of expected formats: ");
                     var12.append(var4);
                     Log.w("ExifInterface", var12.toString());
                     break;
                  case 9:
                     var14 = var1.split(",", -1);
                     var17 = new int[var14.length];

                     for(var4 = 0; var4 < var14.length; ++var4) {
                        var17[var4] = Integer.parseInt(var14[var4]);
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createSLong(var17, this.mExifByteOrder));
                     break;
                  case 10:
                     var14 = var1.split(",", -1);
                     ExifInterface.Rational[] var16 = new ExifInterface.Rational[var14.length];

                     for(var4 = 0; var4 < var14.length; ++var4) {
                        var19 = var14[var4].split("/", -1);
                        var16[var4] = new ExifInterface.Rational((long)Double.parseDouble(var19[0]), (long)Double.parseDouble(var19[1]));
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createSRational(var16, this.mExifByteOrder));
                     break;
                  case 12:
                     var14 = var1.split(",", -1);
                     double[] var15 = new double[var14.length];

                     for(var4 = 0; var4 < var14.length; ++var4) {
                        var15[var4] = Double.parseDouble(var14[var4]);
                     }

                     this.mAttributes[var3].put(var5, ExifInterface.ExifAttribute.createDouble(var15, this.mExifByteOrder));
                  }
               }
            }
         }
      }

   }

   public void setDateTime(long var1) {
      this.setAttribute("DateTime", sFormatter.format(new Date(var1)));
      this.setAttribute("SubSecTime", Long.toString(var1 % 1000L));
   }

   public void setGpsInfo(Location var1) {
      if (var1 != null) {
         this.setAttribute("GPSProcessingMethod", var1.getProvider());
         this.setLatLong(var1.getLatitude(), var1.getLongitude());
         this.setAltitude(var1.getAltitude());
         this.setAttribute("GPSSpeedRef", "K");
         this.setAttribute("GPSSpeed", (new ExifInterface.Rational((double)(var1.getSpeed() * (float)TimeUnit.HOURS.toSeconds(1L) / 1000.0F))).toString());
         String[] var2 = sFormatter.format(new Date(var1.getTime())).split("\\s+", -1);
         this.setAttribute("GPSDateStamp", var2[0]);
         this.setAttribute("GPSTimeStamp", var2[1]);
      }
   }

   public void setLatLong(double var1, double var3) {
      StringBuilder var5;
      if (var1 >= -90.0D && var1 <= 90.0D && !Double.isNaN(var1)) {
         if (var3 >= -180.0D && var3 <= 180.0D && !Double.isNaN(var3)) {
            String var6;
            if (var1 >= 0.0D) {
               var6 = "N";
            } else {
               var6 = "S";
            }

            this.setAttribute("GPSLatitudeRef", var6);
            this.setAttribute("GPSLatitude", this.convertDecimalDegree(Math.abs(var1)));
            if (var3 >= 0.0D) {
               var6 = "E";
            } else {
               var6 = "W";
            }

            this.setAttribute("GPSLongitudeRef", var6);
            this.setAttribute("GPSLongitude", this.convertDecimalDegree(Math.abs(var3)));
         } else {
            var5 = new StringBuilder();
            var5.append("Longitude value ");
            var5.append(var3);
            var5.append(" is not valid.");
            throw new IllegalArgumentException(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Latitude value ");
         var5.append(var1);
         var5.append(" is not valid.");
         throw new IllegalArgumentException(var5.toString());
      }
   }

   private static class ByteOrderedDataInputStream extends InputStream implements DataInput {
      private static final ByteOrder BIG_ENDIAN;
      private static final ByteOrder LITTLE_ENDIAN;
      private ByteOrder mByteOrder;
      private DataInputStream mDataInputStream;
      final int mLength;
      int mPosition;

      static {
         LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
         BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
      }

      public ByteOrderedDataInputStream(InputStream var1) throws IOException {
         this.mByteOrder = ByteOrder.BIG_ENDIAN;
         DataInputStream var3 = new DataInputStream(var1);
         this.mDataInputStream = var3;
         int var2 = var3.available();
         this.mLength = var2;
         this.mPosition = 0;
         this.mDataInputStream.mark(var2);
      }

      public ByteOrderedDataInputStream(byte[] var1) throws IOException {
         this((InputStream)(new ByteArrayInputStream(var1)));
      }

      public int available() throws IOException {
         return this.mDataInputStream.available();
      }

      public int peek() {
         return this.mPosition;
      }

      public int read() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.read();
      }

      public int read(byte[] var1, int var2, int var3) throws IOException {
         var2 = this.mDataInputStream.read(var1, var2, var3);
         this.mPosition += var2;
         return var2;
      }

      public boolean readBoolean() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.readBoolean();
      }

      public byte readByte() throws IOException {
         int var1 = this.mPosition + 1;
         this.mPosition = var1;
         if (var1 <= this.mLength) {
            var1 = this.mDataInputStream.read();
            if (var1 >= 0) {
               return (byte)var1;
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public char readChar() throws IOException {
         this.mPosition += 2;
         return this.mDataInputStream.readChar();
      }

      public double readDouble() throws IOException {
         return Double.longBitsToDouble(this.readLong());
      }

      public float readFloat() throws IOException {
         return Float.intBitsToFloat(this.readInt());
      }

      public void readFully(byte[] var1) throws IOException {
         int var2 = this.mPosition + var1.length;
         this.mPosition = var2;
         if (var2 <= this.mLength) {
            if (this.mDataInputStream.read(var1, 0, var1.length) != var1.length) {
               throw new IOException("Couldn't read up to the length of buffer");
            }
         } else {
            throw new EOFException();
         }
      }

      public void readFully(byte[] var1, int var2, int var3) throws IOException {
         int var4 = this.mPosition + var3;
         this.mPosition = var4;
         if (var4 <= this.mLength) {
            if (this.mDataInputStream.read(var1, var2, var3) != var3) {
               throw new IOException("Couldn't read up to the length of buffer");
            }
         } else {
            throw new EOFException();
         }
      }

      public int readInt() throws IOException {
         int var1 = this.mPosition + 4;
         this.mPosition = var1;
         if (var1 <= this.mLength) {
            var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            int var3 = this.mDataInputStream.read();
            int var4 = this.mDataInputStream.read();
            if ((var1 | var2 | var3 | var4) >= 0) {
               ByteOrder var5 = this.mByteOrder;
               if (var5 == LITTLE_ENDIAN) {
                  return (var4 << 24) + (var3 << 16) + (var2 << 8) + var1;
               } else if (var5 == BIG_ENDIAN) {
                  return (var1 << 24) + (var2 << 16) + (var3 << 8) + var4;
               } else {
                  StringBuilder var6 = new StringBuilder();
                  var6.append("Invalid byte order: ");
                  var6.append(this.mByteOrder);
                  throw new IOException(var6.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public String readLine() throws IOException {
         Log.d("ExifInterface", "Currently unsupported");
         return null;
      }

      public long readLong() throws IOException {
         int var1 = this.mPosition + 8;
         this.mPosition = var1;
         if (var1 <= this.mLength) {
            var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            int var3 = this.mDataInputStream.read();
            int var4 = this.mDataInputStream.read();
            int var5 = this.mDataInputStream.read();
            int var6 = this.mDataInputStream.read();
            int var7 = this.mDataInputStream.read();
            int var8 = this.mDataInputStream.read();
            if ((var1 | var2 | var3 | var4 | var5 | var6 | var7 | var8) >= 0) {
               ByteOrder var9 = this.mByteOrder;
               if (var9 == LITTLE_ENDIAN) {
                  return ((long)var8 << 56) + ((long)var7 << 48) + ((long)var6 << 40) + ((long)var5 << 32) + ((long)var4 << 24) + ((long)var3 << 16) + ((long)var2 << 8) + (long)var1;
               } else if (var9 == BIG_ENDIAN) {
                  return ((long)var1 << 56) + ((long)var2 << 48) + ((long)var3 << 40) + ((long)var4 << 32) + ((long)var5 << 24) + ((long)var6 << 16) + ((long)var7 << 8) + (long)var8;
               } else {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("Invalid byte order: ");
                  var10.append(this.mByteOrder);
                  throw new IOException(var10.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public short readShort() throws IOException {
         int var1 = this.mPosition + 2;
         this.mPosition = var1;
         if (var1 <= this.mLength) {
            var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            if ((var1 | var2) >= 0) {
               ByteOrder var3 = this.mByteOrder;
               if (var3 == LITTLE_ENDIAN) {
                  return (short)((var2 << 8) + var1);
               } else if (var3 == BIG_ENDIAN) {
                  return (short)((var1 << 8) + var2);
               } else {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Invalid byte order: ");
                  var4.append(this.mByteOrder);
                  throw new IOException(var4.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public String readUTF() throws IOException {
         this.mPosition += 2;
         return this.mDataInputStream.readUTF();
      }

      public int readUnsignedByte() throws IOException {
         ++this.mPosition;
         return this.mDataInputStream.readUnsignedByte();
      }

      public long readUnsignedInt() throws IOException {
         return (long)this.readInt() & 4294967295L;
      }

      public int readUnsignedShort() throws IOException {
         int var1 = this.mPosition + 2;
         this.mPosition = var1;
         if (var1 <= this.mLength) {
            var1 = this.mDataInputStream.read();
            int var2 = this.mDataInputStream.read();
            if ((var1 | var2) >= 0) {
               ByteOrder var3 = this.mByteOrder;
               if (var3 == LITTLE_ENDIAN) {
                  return (var2 << 8) + var1;
               } else if (var3 == BIG_ENDIAN) {
                  return (var1 << 8) + var2;
               } else {
                  StringBuilder var4 = new StringBuilder();
                  var4.append("Invalid byte order: ");
                  var4.append(this.mByteOrder);
                  throw new IOException(var4.toString());
               }
            } else {
               throw new EOFException();
            }
         } else {
            throw new EOFException();
         }
      }

      public void seek(long var1) throws IOException {
         int var3 = this.mPosition;
         if ((long)var3 > var1) {
            this.mPosition = 0;
            this.mDataInputStream.reset();
            this.mDataInputStream.mark(this.mLength);
         } else {
            var1 -= (long)var3;
         }

         if (this.skipBytes((int)var1) != (int)var1) {
            throw new IOException("Couldn't seek up to the byteCount");
         }
      }

      public void setByteOrder(ByteOrder var1) {
         this.mByteOrder = var1;
      }

      public int skipBytes(int var1) throws IOException {
         int var2 = Math.min(var1, this.mLength - this.mPosition);

         for(var1 = 0; var1 < var2; var1 += this.mDataInputStream.skipBytes(var2 - var1)) {
         }

         this.mPosition += var1;
         return var1;
      }
   }

   private static class ByteOrderedDataOutputStream extends FilterOutputStream {
      private ByteOrder mByteOrder;
      private final OutputStream mOutputStream;

      public ByteOrderedDataOutputStream(OutputStream var1, ByteOrder var2) {
         super(var1);
         this.mOutputStream = var1;
         this.mByteOrder = var2;
      }

      public void setByteOrder(ByteOrder var1) {
         this.mByteOrder = var1;
      }

      public void write(byte[] var1) throws IOException {
         this.mOutputStream.write(var1);
      }

      public void write(byte[] var1, int var2, int var3) throws IOException {
         this.mOutputStream.write(var1, var2, var3);
      }

      public void writeByte(int var1) throws IOException {
         this.mOutputStream.write(var1);
      }

      public void writeInt(int var1) throws IOException {
         if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.mOutputStream.write(var1 >>> 0 & 255);
            this.mOutputStream.write(var1 >>> 8 & 255);
            this.mOutputStream.write(var1 >>> 16 & 255);
            this.mOutputStream.write(var1 >>> 24 & 255);
         } else {
            if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
               this.mOutputStream.write(var1 >>> 24 & 255);
               this.mOutputStream.write(var1 >>> 16 & 255);
               this.mOutputStream.write(var1 >>> 8 & 255);
               this.mOutputStream.write(var1 >>> 0 & 255);
            }

         }
      }

      public void writeShort(short var1) throws IOException {
         if (this.mByteOrder == ByteOrder.LITTLE_ENDIAN) {
            this.mOutputStream.write(var1 >>> 0 & 255);
            this.mOutputStream.write(var1 >>> 8 & 255);
         } else {
            if (this.mByteOrder == ByteOrder.BIG_ENDIAN) {
               this.mOutputStream.write(var1 >>> 8 & 255);
               this.mOutputStream.write(var1 >>> 0 & 255);
            }

         }
      }

      public void writeUnsignedInt(long var1) throws IOException {
         this.writeInt((int)var1);
      }

      public void writeUnsignedShort(int var1) throws IOException {
         this.writeShort((short)var1);
      }
   }

   private static class ExifAttribute {
      public final byte[] bytes;
      public final int format;
      public final int numberOfComponents;

      ExifAttribute(int var1, int var2, byte[] var3) {
         this.format = var1;
         this.numberOfComponents = var2;
         this.bytes = var3;
      }

      public static ExifInterface.ExifAttribute createByte(String var0) {
         if (var0.length() == 1 && var0.charAt(0) >= '0' && var0.charAt(0) <= '1') {
            byte[] var1 = new byte[]{(byte)(var0.charAt(0) - 48)};
            return new ExifInterface.ExifAttribute(1, var1.length, var1);
         } else {
            byte[] var2 = var0.getBytes(ExifInterface.ASCII);
            return new ExifInterface.ExifAttribute(1, var2.length, var2);
         }
      }

      public static ExifInterface.ExifAttribute createDouble(double var0, ByteOrder var2) {
         return createDouble(new double[]{var0}, var2);
      }

      public static ExifInterface.ExifAttribute createDouble(double[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[12] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.putDouble(var0[var2]);
         }

         return new ExifInterface.ExifAttribute(12, var0.length, var4.array());
      }

      public static ExifInterface.ExifAttribute createSLong(int var0, ByteOrder var1) {
         return createSLong(new int[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createSLong(int[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[9] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.putInt(var0[var2]);
         }

         return new ExifInterface.ExifAttribute(9, var0.length, var4.array());
      }

      public static ExifInterface.ExifAttribute createSRational(ExifInterface.Rational var0, ByteOrder var1) {
         return createSRational(new ExifInterface.Rational[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createSRational(ExifInterface.Rational[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[10] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            ExifInterface.Rational var5 = var0[var2];
            var4.putInt((int)var5.numerator);
            var4.putInt((int)var5.denominator);
         }

         return new ExifInterface.ExifAttribute(10, var0.length, var4.array());
      }

      public static ExifInterface.ExifAttribute createString(String var0) {
         StringBuilder var1 = new StringBuilder();
         var1.append(var0);
         var1.append('\u0000');
         byte[] var2 = var1.toString().getBytes(ExifInterface.ASCII);
         return new ExifInterface.ExifAttribute(2, var2.length, var2);
      }

      public static ExifInterface.ExifAttribute createULong(long var0, ByteOrder var2) {
         return createULong(new long[]{var0}, var2);
      }

      public static ExifInterface.ExifAttribute createULong(long[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.putInt((int)var0[var2]);
         }

         return new ExifInterface.ExifAttribute(4, var0.length, var4.array());
      }

      public static ExifInterface.ExifAttribute createURational(ExifInterface.Rational var0, ByteOrder var1) {
         return createURational(new ExifInterface.Rational[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createURational(ExifInterface.Rational[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            ExifInterface.Rational var5 = var0[var2];
            var4.putInt((int)var5.numerator);
            var4.putInt((int)var5.denominator);
         }

         return new ExifInterface.ExifAttribute(5, var0.length, var4.array());
      }

      public static ExifInterface.ExifAttribute createUShort(int var0, ByteOrder var1) {
         return createUShort(new int[]{var0}, var1);
      }

      public static ExifInterface.ExifAttribute createUShort(int[] var0, ByteOrder var1) {
         ByteBuffer var4 = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * var0.length]);
         var4.order(var1);
         int var3 = var0.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            var4.putShort((short)var0[var2]);
         }

         return new ExifInterface.ExifAttribute(3, var0.length, var4.array());
      }

      public double getDoubleValue(ByteOrder var1) {
         Object var2 = this.getValue(var1);
         if (var2 != null) {
            if (var2 instanceof String) {
               return Double.parseDouble((String)var2);
            } else if (var2 instanceof long[]) {
               long[] var6 = (long[])((long[])var2);
               if (var6.length == 1) {
                  return (double)var6[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof int[]) {
               int[] var5 = (int[])((int[])var2);
               if (var5.length == 1) {
                  return (double)var5[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof double[]) {
               double[] var4 = (double[])((double[])var2);
               if (var4.length == 1) {
                  return var4[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof ExifInterface.Rational[]) {
               ExifInterface.Rational[] var3 = (ExifInterface.Rational[])((ExifInterface.Rational[])var2);
               if (var3.length == 1) {
                  return var3[0].calculate();
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else {
               throw new NumberFormatException("Couldn't find a double value");
            }
         } else {
            throw new NumberFormatException("NULL can't be converted to a double value");
         }
      }

      public int getIntValue(ByteOrder var1) {
         Object var2 = this.getValue(var1);
         if (var2 != null) {
            if (var2 instanceof String) {
               return Integer.parseInt((String)var2);
            } else if (var2 instanceof long[]) {
               long[] var4 = (long[])((long[])var2);
               if (var4.length == 1) {
                  return (int)var4[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else if (var2 instanceof int[]) {
               int[] var3 = (int[])((int[])var2);
               if (var3.length == 1) {
                  return var3[0];
               } else {
                  throw new NumberFormatException("There are more than one component");
               }
            } else {
               throw new NumberFormatException("Couldn't find a integer value");
            }
         } else {
            throw new NumberFormatException("NULL can't be converted to a integer value");
         }
      }

      public String getStringValue(ByteOrder var1) {
         Object var3 = this.getValue(var1);
         if (var3 == null) {
            return null;
         } else if (var3 instanceof String) {
            return (String)var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            int var2;
            if (var3 instanceof long[]) {
               long[] var8 = (long[])((long[])var3);

               for(var2 = 0; var2 < var8.length; ++var2) {
                  var4.append(var8[var2]);
                  if (var2 + 1 != var8.length) {
                     var4.append(",");
                  }
               }

               return var4.toString();
            } else if (var3 instanceof int[]) {
               int[] var7 = (int[])((int[])var3);

               for(var2 = 0; var2 < var7.length; ++var2) {
                  var4.append(var7[var2]);
                  if (var2 + 1 != var7.length) {
                     var4.append(",");
                  }
               }

               return var4.toString();
            } else if (var3 instanceof double[]) {
               double[] var6 = (double[])((double[])var3);

               for(var2 = 0; var2 < var6.length; ++var2) {
                  var4.append(var6[var2]);
                  if (var2 + 1 != var6.length) {
                     var4.append(",");
                  }
               }

               return var4.toString();
            } else if (var3 instanceof ExifInterface.Rational[]) {
               ExifInterface.Rational[] var5 = (ExifInterface.Rational[])((ExifInterface.Rational[])var3);

               for(var2 = 0; var2 < var5.length; ++var2) {
                  var4.append(var5[var2].numerator);
                  var4.append('/');
                  var4.append(var5[var2].denominator);
                  if (var2 + 1 != var5.length) {
                     var4.append(",");
                  }
               }

               return var4.toString();
            } else {
               return null;
            }
         }
      }

      Object getValue(ByteOrder param1) {
         // $FF: Couldn't be decompiled
      }

      public int size() {
         return ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[this.format] * this.numberOfComponents;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("(");
         var1.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
         var1.append(", data length:");
         var1.append(this.bytes.length);
         var1.append(")");
         return var1.toString();
      }
   }

   static class ExifTag {
      public final String name;
      public final int number;
      public final int primaryFormat;
      public final int secondaryFormat;

      ExifTag(String var1, int var2, int var3) {
         this.name = var1;
         this.number = var2;
         this.primaryFormat = var3;
         this.secondaryFormat = -1;
      }

      ExifTag(String var1, int var2, int var3, int var4) {
         this.name = var1;
         this.number = var2;
         this.primaryFormat = var3;
         this.secondaryFormat = var4;
      }

      boolean isFormatCompatible(int var1) {
         int var2 = this.primaryFormat;
         if (var2 != 7) {
            if (var1 == 7) {
               return true;
            } else if (var2 != var1) {
               int var3 = this.secondaryFormat;
               if (var3 == var1) {
                  return true;
               } else if ((var2 == 4 || var3 == 4) && var1 == 3) {
                  return true;
               } else if ((this.primaryFormat == 9 || this.secondaryFormat == 9) && var1 == 8) {
                  return true;
               } else {
                  return (this.primaryFormat == 12 || this.secondaryFormat == 12) && var1 == 11;
               }
            } else {
               return true;
            }
         } else {
            return true;
         }
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface IfdType {
   }

   private static class Rational {
      public final long denominator;
      public final long numerator;

      Rational(double var1) {
         this((long)(10000.0D * var1), 10000L);
      }

      Rational(long var1, long var3) {
         if (var3 == 0L) {
            this.numerator = 0L;
            this.denominator = 1L;
         } else {
            this.numerator = var1;
            this.denominator = var3;
         }
      }

      public double calculate() {
         return (double)this.numerator / (double)this.denominator;
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.numerator);
         var1.append("/");
         var1.append(this.denominator);
         return var1.toString();
      }
   }
}
