/*
 * Decompiled with CFR 0_124.
 */
package androidx.core.text.util;

import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class FindAddress {
    private static final String HOUSE_COMPONENT = "(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)";
    private static final String HOUSE_END = "(?=[,\"'\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";
    private static final String HOUSE_POST_DELIM = ",\"'\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String HOUSE_PRE_DELIM = ":,\"'\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int MAX_ADDRESS_LINES = 5;
    private static final int MAX_ADDRESS_WORDS = 14;
    private static final int MAX_LOCATION_NAME_DISTANCE = 5;
    private static final int MIN_ADDRESS_WORDS = 4;
    private static final String NL = "\n\u000b\f\r\u0085\u2028\u2029";
    private static final String SP = "\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000";
    private static final String WORD_DELIM = ",*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String WORD_END = "(?=[,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";
    private static final String WS = "\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int kMaxAddressNameWordLength = 25;
    private static final Pattern sHouseNumberRe;
    private static final Pattern sLocationNameRe;
    private static final Pattern sStateRe;
    private static final ZipRange[] sStateZipCodeRanges;
    private static final Pattern sSuffixedNumberRe;
    private static final Pattern sWordRe;
    private static final Pattern sZipCodeRe;

    static {
        sStateZipCodeRanges = new ZipRange[]{new ZipRange(99, 99, -1, -1), new ZipRange(35, 36, -1, -1), new ZipRange(71, 72, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(85, 86, -1, -1), new ZipRange(90, 96, -1, -1), new ZipRange(80, 81, -1, -1), new ZipRange(6, 6, -1, -1), new ZipRange(20, 20, -1, -1), new ZipRange(19, 19, -1, -1), new ZipRange(32, 34, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(30, 31, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(50, 52, -1, -1), new ZipRange(83, 83, -1, -1), new ZipRange(60, 62, -1, -1), new ZipRange(46, 47, -1, -1), new ZipRange(66, 67, 73, -1), new ZipRange(40, 42, -1, -1), new ZipRange(70, 71, -1, -1), new ZipRange(1, 2, -1, -1), new ZipRange(20, 21, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(48, 49, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(63, 65, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(38, 39, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(27, 28, -1, -1), new ZipRange(58, 58, -1, -1), new ZipRange(68, 69, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(7, 8, -1, -1), new ZipRange(87, 88, 86, -1), new ZipRange(88, 89, 96, -1), new ZipRange(10, 14, 0, 6), new ZipRange(43, 45, -1, -1), new ZipRange(73, 74, -1, -1), new ZipRange(97, 97, -1, -1), new ZipRange(15, 19, -1, -1), new ZipRange(6, 6, 0, 9), new ZipRange(96, 96, -1, -1), new ZipRange(2, 2, -1, -1), new ZipRange(29, 29, -1, -1), new ZipRange(57, 57, -1, -1), new ZipRange(37, 38, -1, -1), new ZipRange(75, 79, 87, 88), new ZipRange(84, 84, -1, -1), new ZipRange(22, 24, 20, -1), new ZipRange(6, 9, -1, -1), new ZipRange(5, 5, -1, -1), new ZipRange(98, 99, -1, -1), new ZipRange(53, 54, -1, -1), new ZipRange(24, 26, -1, -1), new ZipRange(82, 83, -1, -1)};
        sWordRe = Pattern.compile("[^,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]+(?=[,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sHouseNumberRe = Pattern.compile("(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)(?:-(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?))*(?=[,\"'\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sStateRe = Pattern.compile("(?:(ak|alaska)|(al|alabama)|(ar|arkansas)|(as|american[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+samoa)|(az|arizona)|(ca|california)|(co|colorado)|(ct|connecticut)|(dc|district[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+of[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+columbia)|(de|delaware)|(fl|florida)|(fm|federated[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+states[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+of[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+micronesia)|(ga|georgia)|(gu|guam)|(hi|hawaii)|(ia|iowa)|(id|idaho)|(il|illinois)|(in|indiana)|(ks|kansas)|(ky|kentucky)|(la|louisiana)|(ma|massachusetts)|(md|maryland)|(me|maine)|(mh|marshall[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(mi|michigan)|(mn|minnesota)|(mo|missouri)|(mp|northern[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+mariana[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(ms|mississippi)|(mt|montana)|(nc|north[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+carolina)|(nd|north[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+dakota)|(ne|nebraska)|(nh|new[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+hampshire)|(nj|new[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+jersey)|(nm|new[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+mexico)|(nv|nevada)|(ny|new[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+york)|(oh|ohio)|(ok|oklahoma)|(or|oregon)|(pa|pennsylvania)|(pr|puerto[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+rico)|(pw|palau)|(ri|rhode[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+island)|(sc|south[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+carolina)|(sd|south[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+dakota)|(tn|tennessee)|(tx|texas)|(ut|utah)|(va|virginia)|(vi|virgin[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(vt|vermont)|(wa|washington)|(wi|wisconsin)|(wv|west[\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+virginia)|(wy|wyoming))(?=[,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sLocationNameRe = Pattern.compile("(?:alley|annex|arcade|ave[.]?|avenue|alameda|bayou|beach|bend|bluffs?|bottom|boulevard|branch|bridge|brooks?|burgs?|bypass|broadway|camino|camp|canyon|cape|causeway|centers?|circles?|cliffs?|club|common|corners?|course|courts?|coves?|creek|crescent|crest|crossing|crossroad|curve|circulo|dale|dam|divide|drives?|estates?|expressway|extensions?|falls?|ferry|fields?|flats?|fords?|forest|forges?|forks?|fort|freeway|gardens?|gateway|glens?|greens?|groves?|harbors?|haven|heights|highway|hills?|hollow|inlet|islands?|isle|junctions?|keys?|knolls?|lakes?|land|landing|lane|lights?|loaf|locks?|lodge|loop|mall|manors?|meadows?|mews|mills?|mission|motorway|mount|mountains?|neck|orchard|oval|overpass|parks?|parkways?|pass|passage|path|pike|pines?|plains?|plaza|points?|ports?|prairie|privada|radial|ramp|ranch|rapids?|rd[.]?|rest|ridges?|river|roads?|route|row|rue|run|shoals?|shores?|skyway|springs?|spurs?|squares?|station|stravenue|stream|st[.]?|streets?|summit|speedway|terrace|throughway|trace|track|trafficway|trail|tunnel|turnpike|underpass|unions?|valleys?|viaduct|views?|villages?|ville|vista|walks?|wall|ways?|wells?|xing|xrd)(?=[,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sSuffixedNumberRe = Pattern.compile("(\\d+)(st|nd|rd|th)", 2);
        sZipCodeRe = Pattern.compile("(?:\\d{5}(?:-\\d{4})?)(?=[,*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
    }

    private FindAddress() {
    }

    private static int attemptMatch(String string2, MatchResult object) {
        int n;
        int n2 = -1;
        int n3 = -1;
        int n4 = object.end();
        int n5 = 1;
        int n6 = 1;
        boolean bl = false;
        int n7 = 1;
        object = "";
        Matcher matcher = sWordRe.matcher(string2);
        do {
            boolean bl2;
            int n8;
            int n9;
            n = n4;
            if (n4 >= string2.length()) break;
            if (!matcher.find(n4)) {
                return - string2.length();
            }
            if (matcher.end() - matcher.start() > 25) {
                return - matcher.end();
            }
            for (n = n4; n < matcher.start(); ++n) {
                n4 = n5;
                if ("\n\u000b\f\r\u0085\u2028\u2029".indexOf(string2.charAt(n)) != -1) {
                    n4 = n5 + 1;
                }
                n5 = n4;
            }
            if (n5 > 5 || (n9 = n7 + 1) > 14) break;
            if (FindAddress.matchHouseNumber(string2, n) != null) {
                if (n6 != 0 && n5 > 1) {
                    return - n;
                }
                n4 = n2;
                n8 = n3;
                n7 = n6;
                bl2 = bl;
                if (n2 == -1) {
                    n4 = n;
                    n8 = n3;
                    n7 = n6;
                    bl2 = bl;
                }
            } else {
                n6 = 0;
                if (FindAddress.isValidLocationName(matcher.group(0))) {
                    bl2 = true;
                    n4 = n2;
                    n8 = n3;
                    n7 = n6;
                } else {
                    if (n9 == 5 && !bl) {
                        n = matcher.end();
                        break;
                    }
                    n4 = n2;
                    n8 = n3;
                    n7 = n6;
                    bl2 = bl;
                    if (bl) {
                        n4 = n2;
                        n8 = n3;
                        n7 = n6;
                        bl2 = bl;
                        if (n9 > 4) {
                            MatchResult matchResult = FindAddress.matchState(string2, n);
                            n4 = n2;
                            n8 = n3;
                            n7 = n6;
                            bl2 = bl;
                            if (matchResult != null) {
                                if (object.equals("et") && matchResult.group(0).equals("al")) {
                                    n = matchResult.end();
                                    break;
                                }
                                object = sWordRe.matcher(string2);
                                if (object.find(matchResult.end())) {
                                    n4 = n2;
                                    n8 = n3;
                                    n7 = n6;
                                    bl2 = bl;
                                    if (FindAddress.isValidZipCode(object.group(0), matchResult)) {
                                        return object.end();
                                    }
                                } else {
                                    n8 = matchResult.end();
                                    bl2 = bl;
                                    n7 = n6;
                                    n4 = n2;
                                }
                            }
                        }
                    }
                }
            }
            object = matcher.group(0);
            n = matcher.end();
            n2 = n4;
            n3 = n8;
            n4 = n;
            n6 = n7;
            bl = bl2;
            n7 = n9;
        } while (true);
        if (n3 > 0) {
            return n3;
        }
        if (n2 > 0) {
            n = n2;
        }
        return - n;
    }

    private static boolean checkHouseNumber(String object) {
        int n;
        int n2 = 0;
        for (n = 0; n < object.length(); ++n) {
            int n3 = n2;
            if (Character.isDigit(object.charAt(n))) {
                n3 = n2 + 1;
            }
            n2 = n3;
        }
        if (n2 > 5) {
            return false;
        }
        if ((object = sSuffixedNumberRe.matcher((CharSequence)object)).find()) {
            n = Integer.parseInt(object.group(1));
            if (n == 0) {
                return false;
            }
            String string2 = object.group(2).toLowerCase(Locale.getDefault());
            n2 = n % 10;
            object = "th";
            if (n2 != 1) {
                if (n2 != 2) {
                    if (n2 != 3) {
                        return string2.equals("th");
                    }
                    if (n % 100 != 13) {
                        object = "rd";
                    }
                    return string2.equals(object);
                }
                if (n % 100 != 12) {
                    object = "nd";
                }
                return string2.equals(object);
            }
            if (n % 100 != 11) {
                object = "st";
            }
            return string2.equals(object);
        }
        return true;
    }

    static String findAddress(String string2) {
        Matcher matcher = sHouseNumberRe.matcher(string2);
        int n = 0;
        while (matcher.find(n)) {
            if (FindAddress.checkHouseNumber(matcher.group(0))) {
                n = matcher.start();
                int n2 = FindAddress.attemptMatch(string2, matcher);
                if (n2 > 0) {
                    return string2.substring(n, n2);
                }
                n = - n2;
                continue;
            }
            n = matcher.end();
        }
        return null;
    }

    public static boolean isValidLocationName(String string2) {
        return sLocationNameRe.matcher(string2).matches();
    }

    public static boolean isValidZipCode(String string2) {
        return sZipCodeRe.matcher(string2).matches();
    }

    public static boolean isValidZipCode(String string2, String string3) {
        return FindAddress.isValidZipCode(string2, FindAddress.matchState(string3, 0));
    }

    private static boolean isValidZipCode(String string2, MatchResult matchResult) {
        int n;
        if (matchResult == null) {
            return false;
        }
        int n2 = matchResult.groupCount();
        do {
            n = n2;
            if (n2 <= 0) break;
            n = n2 - 1;
            if (matchResult.group(n2) != null) break;
            n2 = n;
        } while (true);
        if (sZipCodeRe.matcher(string2).matches() && sStateZipCodeRanges[n].matches(string2)) {
            return true;
        }
        return false;
    }

    public static MatchResult matchHouseNumber(String object, int n) {
        if (n > 0 && ":,\"'\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029".indexOf(object.charAt(n - 1)) == -1) {
            return null;
        }
        if ((object = sHouseNumberRe.matcher((CharSequence)object).region(n, object.length())).lookingAt() && FindAddress.checkHouseNumber((object = object.toMatchResult()).group(0))) {
            return object;
        }
        return null;
    }

    public static MatchResult matchState(String object, int n) {
        Object var2_2 = null;
        if (n > 0 && ",*\u2022\t \u00a0\u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029".indexOf(object.charAt(n - 1)) == -1) {
            return null;
        }
        Matcher matcher = sStateRe.matcher((CharSequence)object).region(n, object.length());
        object = var2_2;
        if (matcher.lookingAt()) {
            object = matcher.toMatchResult();
        }
        return object;
    }

    private static class ZipRange {
        int mException1;
        int mException2;
        int mHigh;
        int mLow;

        ZipRange(int n, int n2, int n3, int n4) {
            this.mLow = n;
            this.mHigh = n2;
            this.mException1 = n3;
            this.mException2 = n4;
        }

        boolean matches(String string2) {
            boolean bl = false;
            int n = Integer.parseInt(string2.substring(0, 2));
            if (this.mLow <= n && n <= this.mHigh || n == this.mException1 || n == this.mException2) {
                bl = true;
            }
            return bl;
        }
    }

}

