// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.text.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

class FindAddress
{
    private static final String HOUSE_COMPONENT = "(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)";
    private static final String HOUSE_END = "(?=[,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";
    private static final String HOUSE_POST_DELIM = ",\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String HOUSE_PRE_DELIM = ":,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int MAX_ADDRESS_LINES = 5;
    private static final int MAX_ADDRESS_WORDS = 14;
    private static final int MAX_LOCATION_NAME_DISTANCE = 5;
    private static final int MIN_ADDRESS_WORDS = 4;
    private static final String NL = "\n\u000b\f\r\u0085\u2028\u2029";
    private static final String SP = "\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000";
    private static final String WORD_DELIM = ",*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final String WORD_END = "(?=[,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)";
    private static final String WS = "\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029";
    private static final int kMaxAddressNameWordLength = 25;
    private static final Pattern sHouseNumberRe;
    private static final Pattern sLocationNameRe;
    private static final Pattern sStateRe;
    private static final ZipRange[] sStateZipCodeRanges;
    private static final Pattern sSuffixedNumberRe;
    private static final Pattern sWordRe;
    private static final Pattern sZipCodeRe;
    
    static {
        sStateZipCodeRanges = new ZipRange[] { new ZipRange(99, 99, -1, -1), new ZipRange(35, 36, -1, -1), new ZipRange(71, 72, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(85, 86, -1, -1), new ZipRange(90, 96, -1, -1), new ZipRange(80, 81, -1, -1), new ZipRange(6, 6, -1, -1), new ZipRange(20, 20, -1, -1), new ZipRange(19, 19, -1, -1), new ZipRange(32, 34, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(30, 31, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(50, 52, -1, -1), new ZipRange(83, 83, -1, -1), new ZipRange(60, 62, -1, -1), new ZipRange(46, 47, -1, -1), new ZipRange(66, 67, 73, -1), new ZipRange(40, 42, -1, -1), new ZipRange(70, 71, -1, -1), new ZipRange(1, 2, -1, -1), new ZipRange(20, 21, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(48, 49, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(63, 65, -1, -1), new ZipRange(96, 96, -1, -1), new ZipRange(38, 39, -1, -1), new ZipRange(55, 56, -1, -1), new ZipRange(27, 28, -1, -1), new ZipRange(58, 58, -1, -1), new ZipRange(68, 69, -1, -1), new ZipRange(3, 4, -1, -1), new ZipRange(7, 8, -1, -1), new ZipRange(87, 88, 86, -1), new ZipRange(88, 89, 96, -1), new ZipRange(10, 14, 0, 6), new ZipRange(43, 45, -1, -1), new ZipRange(73, 74, -1, -1), new ZipRange(97, 97, -1, -1), new ZipRange(15, 19, -1, -1), new ZipRange(6, 6, 0, 9), new ZipRange(96, 96, -1, -1), new ZipRange(2, 2, -1, -1), new ZipRange(29, 29, -1, -1), new ZipRange(57, 57, -1, -1), new ZipRange(37, 38, -1, -1), new ZipRange(75, 79, 87, 88), new ZipRange(84, 84, -1, -1), new ZipRange(22, 24, 20, -1), new ZipRange(6, 9, -1, -1), new ZipRange(5, 5, -1, -1), new ZipRange(98, 99, -1, -1), new ZipRange(53, 54, -1, -1), new ZipRange(24, 26, -1, -1), new ZipRange(82, 83, -1, -1) };
        sWordRe = Pattern.compile("[^,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]+(?=[,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sHouseNumberRe = Pattern.compile("(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?)(?:-(?:one|\\d+([a-z](?=[^a-z]|$)|st|nd|rd|th)?))*(?=[,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sStateRe = Pattern.compile("(?:(ak|alaska)|(al|alabama)|(ar|arkansas)|(as|american[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+samoa)|(az|arizona)|(ca|california)|(co|colorado)|(ct|connecticut)|(dc|district[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+of[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+columbia)|(de|delaware)|(fl|florida)|(fm|federated[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+states[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+of[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+micronesia)|(ga|georgia)|(gu|guam)|(hi|hawaii)|(ia|iowa)|(id|idaho)|(il|illinois)|(in|indiana)|(ks|kansas)|(ky|kentucky)|(la|louisiana)|(ma|massachusetts)|(md|maryland)|(me|maine)|(mh|marshall[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(mi|michigan)|(mn|minnesota)|(mo|missouri)|(mp|northern[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+mariana[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(ms|mississippi)|(mt|montana)|(nc|north[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+carolina)|(nd|north[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+dakota)|(ne|nebraska)|(nh|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+hampshire)|(nj|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+jersey)|(nm|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+mexico)|(nv|nevada)|(ny|new[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+york)|(oh|ohio)|(ok|oklahoma)|(or|oregon)|(pa|pennsylvania)|(pr|puerto[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+rico)|(pw|palau)|(ri|rhode[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+island)|(sc|south[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+carolina)|(sd|south[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+dakota)|(tn|tennessee)|(tx|texas)|(ut|utah)|(va|virginia)|(vi|virgin[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+islands)|(vt|vermont)|(wa|washington)|(wi|wisconsin)|(wv|west[\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000]+virginia)|(wy|wyoming))(?=[,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sLocationNameRe = Pattern.compile("(?:alley|annex|arcade|ave[.]?|avenue|alameda|bayou|beach|bend|bluffs?|bottom|boulevard|branch|bridge|brooks?|burgs?|bypass|broadway|camino|camp|canyon|cape|causeway|centers?|circles?|cliffs?|club|common|corners?|course|courts?|coves?|creek|crescent|crest|crossing|crossroad|curve|circulo|dale|dam|divide|drives?|estates?|expressway|extensions?|falls?|ferry|fields?|flats?|fords?|forest|forges?|forks?|fort|freeway|gardens?|gateway|glens?|greens?|groves?|harbors?|haven|heights|highway|hills?|hollow|inlet|islands?|isle|junctions?|keys?|knolls?|lakes?|land|landing|lane|lights?|loaf|locks?|lodge|loop|mall|manors?|meadows?|mews|mills?|mission|motorway|mount|mountains?|neck|orchard|oval|overpass|parks?|parkways?|pass|passage|path|pike|pines?|plains?|plaza|points?|ports?|prairie|privada|radial|ramp|ranch|rapids?|rd[.]?|rest|ridges?|river|roads?|route|row|rue|run|shoals?|shores?|skyway|springs?|spurs?|squares?|station|stravenue|stream|st[.]?|streets?|summit|speedway|terrace|throughway|trace|track|trafficway|trail|tunnel|turnpike|underpass|unions?|valleys?|viaduct|views?|villages?|ville|vista|walks?|wall|ways?|wells?|xing|xrd)(?=[,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
        sSuffixedNumberRe = Pattern.compile("(\\d+)(st|nd|rd|th)", 2);
        sZipCodeRe = Pattern.compile("(?:\\d{5}(?:-\\d{4})?)(?=[,*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029]|$)", 2);
    }
    
    private FindAddress() {
    }
    
    private static int attemptMatch(final String s, final MatchResult matchResult) {
        int n = -1;
        int n2 = -1;
        int end = matchResult.end();
        int n3 = 1;
        int n4 = 1;
        int n5 = 0;
        int n6 = 1;
        String group = "";
        final Matcher matcher = FindAddress.sWordRe.matcher(s);
        int i;
        while (true) {
            i = end;
            if (end >= s.length()) {
                break;
            }
            if (!matcher.find(end)) {
                return -s.length();
            }
            i = end;
            if (matcher.end() - matcher.start() > 25) {
                return -matcher.end();
            }
            while (i < matcher.start()) {
                int n7 = n3;
                if ("\n\u000b\f\r\u0085\u2028\u2029".indexOf(s.charAt(i)) != -1) {
                    n7 = n3 + 1;
                }
                ++i;
                n3 = n7;
            }
            if (n3 > 5) {
                break;
            }
            final int n8 = n6 + 1;
            if (n8 > 14) {
                break;
            }
            int n9;
            int end2;
            int n10;
            int n11;
            if (matchHouseNumber(s, i) != null) {
                if (n4 != 0 && n3 > 1) {
                    return -i;
                }
                n9 = n;
                end2 = n2;
                n10 = n4;
                n11 = n5;
                if (n == -1) {
                    n9 = i;
                    end2 = n2;
                    n10 = n4;
                    n11 = n5;
                }
            }
            else {
                final boolean b = false;
                if (isValidLocationName(matcher.group(0))) {
                    n11 = 1;
                    n9 = n;
                    end2 = n2;
                    n10 = (b ? 1 : 0);
                }
                else {
                    if (n8 == 5 && n5 == 0) {
                        i = matcher.end();
                        break;
                    }
                    n9 = n;
                    end2 = n2;
                    n10 = (b ? 1 : 0);
                    if ((n11 = n5) != 0) {
                        n9 = n;
                        end2 = n2;
                        n10 = (b ? 1 : 0);
                        n11 = n5;
                        if (n8 > 4) {
                            final MatchResult matchState = matchState(s, i);
                            n9 = n;
                            end2 = n2;
                            n10 = (b ? 1 : 0);
                            n11 = n5;
                            if (matchState != null) {
                                if (group.equals("et") && matchState.group(0).equals("al")) {
                                    i = matchState.end();
                                    break;
                                }
                                final Matcher matcher2 = FindAddress.sWordRe.matcher(s);
                                if (matcher2.find(matchState.end())) {
                                    n9 = n;
                                    end2 = n2;
                                    n10 = (b ? 1 : 0);
                                    n11 = n5;
                                    if (isValidZipCode(matcher2.group(0), matchState)) {
                                        return matcher2.end();
                                    }
                                }
                                else {
                                    end2 = matchState.end();
                                    n11 = n5;
                                    n10 = (b ? 1 : 0);
                                    n9 = n;
                                }
                            }
                        }
                    }
                }
            }
            group = matcher.group(0);
            final int end3 = matcher.end();
            n = n9;
            n2 = end2;
            end = end3;
            n4 = n10;
            n5 = n11;
            n6 = n8;
        }
        if (n2 > 0) {
            return n2;
        }
        if (n > 0) {
            i = n;
        }
        return -i;
    }
    
    private static boolean checkHouseNumber(String s) {
        int n = 0;
        int n2;
        for (int i = 0; i < s.length(); ++i, n = n2) {
            n2 = n;
            if (Character.isDigit(s.charAt(i))) {
                n2 = n + 1;
            }
        }
        if (n > 5) {
            return false;
        }
        final Matcher matcher = FindAddress.sSuffixedNumberRe.matcher(s);
        if (!matcher.find()) {
            return true;
        }
        final int int1 = Integer.parseInt(matcher.group(1));
        if (int1 == 0) {
            return false;
        }
        final String lowerCase = matcher.group(2).toLowerCase(Locale.getDefault());
        final int n3 = int1 % 10;
        s = "th";
        if (n3 == 1) {
            if (int1 % 100 != 11) {
                s = "st";
            }
            return lowerCase.equals(s);
        }
        if (n3 == 2) {
            if (int1 % 100 != 12) {
                s = "nd";
            }
            return lowerCase.equals(s);
        }
        if (n3 != 3) {
            return lowerCase.equals("th");
        }
        if (int1 % 100 != 13) {
            s = "rd";
        }
        return lowerCase.equals(s);
    }
    
    static String findAddress(final String s) {
        final Matcher matcher = FindAddress.sHouseNumberRe.matcher(s);
        int end = 0;
        while (matcher.find(end)) {
            if (checkHouseNumber(matcher.group(0))) {
                final int start = matcher.start();
                final int attemptMatch = attemptMatch(s, matcher);
                if (attemptMatch > 0) {
                    return s.substring(start, attemptMatch);
                }
                end = -attemptMatch;
            }
            else {
                end = matcher.end();
            }
        }
        return null;
    }
    
    public static boolean isValidLocationName(final String s) {
        return FindAddress.sLocationNameRe.matcher(s).matches();
    }
    
    public static boolean isValidZipCode(final String s) {
        return FindAddress.sZipCodeRe.matcher(s).matches();
    }
    
    public static boolean isValidZipCode(final String s, final String s2) {
        return isValidZipCode(s, matchState(s2, 0));
    }
    
    private static boolean isValidZipCode(final String s, final MatchResult matchResult) {
        if (matchResult == null) {
            return false;
        }
        int groupCount = matchResult.groupCount();
        int n;
        while (true) {
            n = groupCount;
            if (groupCount <= 0) {
                break;
            }
            n = groupCount - 1;
            if (matchResult.group(groupCount) != null) {
                break;
            }
            groupCount = n;
        }
        return FindAddress.sZipCodeRe.matcher(s).matches() && FindAddress.sStateZipCodeRanges[n].matches(s);
    }
    
    public static MatchResult matchHouseNumber(final String s, final int n) {
        if (n > 0 && ":,\"'\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029".indexOf(s.charAt(n - 1)) == -1) {
            return null;
        }
        final Matcher region = FindAddress.sHouseNumberRe.matcher(s).region(n, s.length());
        if (region.lookingAt()) {
            final MatchResult matchResult = region.toMatchResult();
            if (checkHouseNumber(matchResult.group(0))) {
                return matchResult;
            }
        }
        return null;
    }
    
    public static MatchResult matchState(final String s, final int n) {
        final MatchResult matchResult = null;
        if (n > 0 && ",*\u2022\t  \u1680\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200a\u202f\u205f\u3000\n\u000b\f\r\u0085\u2028\u2029".indexOf(s.charAt(n - 1)) == -1) {
            return null;
        }
        final Matcher region = FindAddress.sStateRe.matcher(s).region(n, s.length());
        MatchResult matchResult2 = matchResult;
        if (region.lookingAt()) {
            matchResult2 = region.toMatchResult();
        }
        return matchResult2;
    }
    
    private static class ZipRange
    {
        int mException1;
        int mException2;
        int mHigh;
        int mLow;
        
        ZipRange(final int mLow, final int mHigh, final int mException1, final int mException2) {
            this.mLow = mLow;
            this.mHigh = mHigh;
            this.mException1 = mException1;
            this.mException2 = mException2;
        }
        
        boolean matches(final String s) {
            boolean b = false;
            final int int1 = Integer.parseInt(s.substring(0, 2));
            if ((this.mLow <= int1 && int1 <= this.mHigh) || int1 == this.mException1 || int1 == this.mException2) {
                b = true;
            }
            return b;
        }
    }
}
