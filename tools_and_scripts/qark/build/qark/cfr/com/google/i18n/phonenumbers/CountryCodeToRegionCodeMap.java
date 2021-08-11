/*
 * Decompiled with CFR 0_124.
 */
package com.google.i18n.phonenumbers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryCodeToRegionCodeMap {
    static Map<Integer, List<String>> getCountryCodeToRegionCodeMap() {
        HashMap<Integer, List<String>> hashMap = new HashMap<Integer, List<String>>(286);
        ArrayList<String> arrayList = new ArrayList<String>(25);
        arrayList.add("US");
        arrayList.add("AG");
        arrayList.add("AI");
        arrayList.add("AS");
        arrayList.add("BB");
        arrayList.add("BM");
        arrayList.add("BS");
        arrayList.add("CA");
        arrayList.add("DM");
        arrayList.add("DO");
        arrayList.add("GD");
        arrayList.add("GU");
        arrayList.add("JM");
        arrayList.add("KN");
        arrayList.add("KY");
        arrayList.add("LC");
        arrayList.add("MP");
        arrayList.add("MS");
        arrayList.add("PR");
        arrayList.add("SX");
        arrayList.add("TC");
        arrayList.add("TT");
        arrayList.add("VC");
        arrayList.add("VG");
        arrayList.add("VI");
        hashMap.put(1, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("RU");
        arrayList.add("KZ");
        hashMap.put(7, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("EG");
        hashMap.put(20, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ZA");
        hashMap.put(27, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GR");
        hashMap.put(30, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NL");
        hashMap.put(31, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BE");
        hashMap.put(32, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("FR");
        hashMap.put(33, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ES");
        hashMap.put(34, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("HU");
        hashMap.put(36, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("IT");
        arrayList.add("VA");
        hashMap.put(39, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("RO");
        hashMap.put(40, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CH");
        hashMap.put(41, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AT");
        hashMap.put(43, arrayList);
        arrayList = new ArrayList(4);
        arrayList.add("GB");
        arrayList.add("GG");
        arrayList.add("IM");
        arrayList.add("JE");
        hashMap.put(44, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("DK");
        hashMap.put(45, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SE");
        hashMap.put(46, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("NO");
        arrayList.add("SJ");
        hashMap.put(47, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PL");
        hashMap.put(48, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("DE");
        hashMap.put(49, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PE");
        hashMap.put(51, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MX");
        hashMap.put(52, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CU");
        hashMap.put(53, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AR");
        hashMap.put(54, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BR");
        hashMap.put(55, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CL");
        hashMap.put(56, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CO");
        hashMap.put(57, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("VE");
        hashMap.put(58, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MY");
        hashMap.put(60, arrayList);
        arrayList = new ArrayList(3);
        arrayList.add("AU");
        arrayList.add("CC");
        arrayList.add("CX");
        hashMap.put(61, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ID");
        hashMap.put(62, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PH");
        hashMap.put(63, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NZ");
        hashMap.put(64, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SG");
        hashMap.put(65, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TH");
        hashMap.put(66, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("JP");
        hashMap.put(81, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KR");
        hashMap.put(82, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("VN");
        hashMap.put(84, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CN");
        hashMap.put(86, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TR");
        hashMap.put(90, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IN");
        hashMap.put(91, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PK");
        hashMap.put(92, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AF");
        hashMap.put(93, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LK");
        hashMap.put(94, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MM");
        hashMap.put(95, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IR");
        hashMap.put(98, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SS");
        hashMap.put(211, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("MA");
        arrayList.add("EH");
        hashMap.put(212, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("DZ");
        hashMap.put(213, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TN");
        hashMap.put(216, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LY");
        hashMap.put(218, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GM");
        hashMap.put(220, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SN");
        hashMap.put(221, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MR");
        hashMap.put(222, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ML");
        hashMap.put(223, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GN");
        hashMap.put(224, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CI");
        hashMap.put(225, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BF");
        hashMap.put(226, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NE");
        hashMap.put(227, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TG");
        hashMap.put(228, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BJ");
        hashMap.put(229, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MU");
        hashMap.put(230, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LR");
        hashMap.put(231, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SL");
        hashMap.put(232, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GH");
        hashMap.put(233, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NG");
        hashMap.put(234, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TD");
        hashMap.put(235, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CF");
        hashMap.put(236, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CM");
        hashMap.put(237, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CV");
        hashMap.put(238, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ST");
        hashMap.put(239, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GQ");
        hashMap.put(240, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GA");
        hashMap.put(241, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CG");
        hashMap.put(242, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CD");
        hashMap.put(243, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AO");
        hashMap.put(244, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GW");
        hashMap.put(245, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IO");
        hashMap.put(246, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AC");
        hashMap.put(247, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SC");
        hashMap.put(248, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SD");
        hashMap.put(249, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("RW");
        hashMap.put(250, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ET");
        hashMap.put(251, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SO");
        hashMap.put(252, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("DJ");
        hashMap.put(253, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KE");
        hashMap.put(254, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TZ");
        hashMap.put(255, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("UG");
        hashMap.put(256, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BI");
        hashMap.put(257, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MZ");
        hashMap.put(258, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ZM");
        hashMap.put(260, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MG");
        hashMap.put(261, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("RE");
        arrayList.add("YT");
        hashMap.put(262, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ZW");
        hashMap.put(263, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NA");
        hashMap.put(264, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MW");
        hashMap.put(265, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LS");
        hashMap.put(266, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BW");
        hashMap.put(267, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SZ");
        hashMap.put(268, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KM");
        hashMap.put(269, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("SH");
        arrayList.add("TA");
        hashMap.put(290, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ER");
        hashMap.put(291, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AW");
        hashMap.put(297, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("FO");
        hashMap.put(298, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GL");
        hashMap.put(299, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GI");
        hashMap.put(350, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PT");
        hashMap.put(351, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LU");
        hashMap.put(352, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IE");
        hashMap.put(353, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IS");
        hashMap.put(354, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AL");
        hashMap.put(355, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MT");
        hashMap.put(356, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CY");
        hashMap.put(357, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("FI");
        arrayList.add("AX");
        hashMap.put(358, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BG");
        hashMap.put(359, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LT");
        hashMap.put(370, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LV");
        hashMap.put(371, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("EE");
        hashMap.put(372, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MD");
        hashMap.put(373, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AM");
        hashMap.put(374, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BY");
        hashMap.put(375, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AD");
        hashMap.put(376, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MC");
        hashMap.put(377, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SM");
        hashMap.put(378, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("UA");
        hashMap.put(380, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("RS");
        hashMap.put(381, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("ME");
        hashMap.put(382, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("XK");
        hashMap.put(383, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("HR");
        hashMap.put(385, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SI");
        hashMap.put(386, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BA");
        hashMap.put(387, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MK");
        hashMap.put(389, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CZ");
        hashMap.put(420, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SK");
        hashMap.put(421, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LI");
        hashMap.put(423, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("FK");
        hashMap.put(500, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BZ");
        hashMap.put(501, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GT");
        hashMap.put(502, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SV");
        hashMap.put(503, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("HN");
        hashMap.put(504, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NI");
        hashMap.put(505, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CR");
        hashMap.put(506, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PA");
        hashMap.put(507, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PM");
        hashMap.put(508, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("HT");
        hashMap.put(509, arrayList);
        arrayList = new ArrayList(3);
        arrayList.add("GP");
        arrayList.add("BL");
        arrayList.add("MF");
        hashMap.put(590, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BO");
        hashMap.put(591, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GY");
        hashMap.put(592, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("EC");
        hashMap.put(593, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GF");
        hashMap.put(594, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PY");
        hashMap.put(595, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MQ");
        hashMap.put(596, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SR");
        hashMap.put(597, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("UY");
        hashMap.put(598, arrayList);
        arrayList = new ArrayList(2);
        arrayList.add("CW");
        arrayList.add("BQ");
        hashMap.put(599, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TL");
        hashMap.put(670, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NF");
        hashMap.put(672, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BN");
        hashMap.put(673, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NR");
        hashMap.put(674, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PG");
        hashMap.put(675, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TO");
        hashMap.put(676, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SB");
        hashMap.put(677, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("VU");
        hashMap.put(678, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("FJ");
        hashMap.put(679, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PW");
        hashMap.put(680, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("WF");
        hashMap.put(681, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("CK");
        hashMap.put(682, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NU");
        hashMap.put(683, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("WS");
        hashMap.put(685, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KI");
        hashMap.put(686, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NC");
        hashMap.put(687, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TV");
        hashMap.put(688, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PF");
        hashMap.put(689, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TK");
        hashMap.put(690, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("FM");
        hashMap.put(691, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MH");
        hashMap.put(692, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(800, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(808, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KP");
        hashMap.put(850, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("HK");
        hashMap.put(852, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MO");
        hashMap.put(853, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KH");
        hashMap.put(855, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LA");
        hashMap.put(856, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(870, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(878, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BD");
        hashMap.put(880, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(881, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(882, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(883, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TW");
        hashMap.put(886, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(888, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MV");
        hashMap.put(960, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("LB");
        hashMap.put(961, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("JO");
        hashMap.put(962, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SY");
        hashMap.put(963, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IQ");
        hashMap.put(964, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KW");
        hashMap.put(965, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("SA");
        hashMap.put(966, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("YE");
        hashMap.put(967, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("OM");
        hashMap.put(968, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("PS");
        hashMap.put(970, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AE");
        hashMap.put(971, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("IL");
        hashMap.put(972, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BH");
        hashMap.put(973, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("QA");
        hashMap.put(974, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("BT");
        hashMap.put(975, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("MN");
        hashMap.put(976, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("NP");
        hashMap.put(977, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("001");
        hashMap.put(979, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TJ");
        hashMap.put(992, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("TM");
        hashMap.put(993, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("AZ");
        hashMap.put(994, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("GE");
        hashMap.put(995, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("KG");
        hashMap.put(996, arrayList);
        arrayList = new ArrayList(1);
        arrayList.add("UZ");
        hashMap.put(998, arrayList);
        return hashMap;
    }
}

