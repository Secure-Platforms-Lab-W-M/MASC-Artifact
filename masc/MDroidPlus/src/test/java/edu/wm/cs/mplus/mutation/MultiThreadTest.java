package edu.wm.cs.mplus.mutation;

import java.io.IOException;
import java.util.HashMap;

import edu.wm.cs.mplus.MPlus;

public class MultiThreadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
		hashMap.put("a2dpVol",a2dpVol);
		//		hashMap.put("aarddict", aarddict);
		//		hashMap.put("aLogCat", aLogCat);
		//		hashMap.put("Amazed", Amazed);
		//		hashMap.put("AnyCut", AnyCut);
		//		hashMap.put("anymemo", anymemo);
		//		hashMap.put("batterydog", batterydog);
		//		hashMap.put("swiftp", swiftp);
		//		hashMap.put("bites", bites);
		//		hashMap.put("blikenlights_battery", blikenlights_battery);
		//		hashMap.put("alarmclock", alarmclock);
		//		hashMap.put("manpages", manpages);
		//		hashMap.put("mileage",mileage);
		//		hashMap.put("autoanswer", autoanswer);
		//		hashMap.put("hndroid",hndroid);
		//		hashMap.put("multisms", multisms);
		//		hashMap.put("worldclock",  worldclock);
		//		hashMap.put("jamendo", jamendo);
		//		hashMap.put("yahtzee", yahtzee);
		//		hashMap.put("countdownTimer", countdownTimer);
		//		hashMap.put("sanity", sanity);
		//		hashMap.put("mirrored", mirrored);
		//		hashMap.put("dialer", dialer);
		//		hashMap.put("fileexplorer", fileexplorer);
		//		hashMap.put("adsdroid",  adsdroid);
		//		hashMap.put("myLock", myLock);
		//		hashMap.put("lockPatternGenerator",lockPatternGenerator);
		//		hashMap.put("aGrep", aGrep);
		//		hashMap.put("lolcatBuilder", lolcatBuilder);
		//		hashMap.put("munchLife", munchLife);
		//		hashMap.put("myexpenses", myexpenses);
		//		hashMap.put("LMN", LMN);
		//		hashMap.put("netcounter", netcounter);
		//		hashMap.put("bomber", bomber);
		//		hashMap.put("frozenbubble", frozenbubble);
		//		hashMap.put("blokish", blokish);
		//		hashMap.put("zooborns", zooborns);
		//		hashMap.put("importcontacts", importcontacts);
		//		hashMap.put("wikipedia", wikipedia);
		//		hashMap.put("passMakerPro", passMakerPro);
		//		hashMap.put("photostream",photostream);
		//		hashMap.put("quickSettings", quickSettings);
		//		hashMap.put("randomMusicPlayer", randomMusicPlayer);
		//		hashMap.put("soundboard", soundboard);
		//		hashMap.put("spriteMethodTest", spriteMethodTest);
		//		hashMap.put("spriteText", spriteText);
		//		hashMap.put("syncmypix", syncmypix);
		//		hashMap.put("tippyTipper", tippyTipper);
		//		hashMap.put("tomdroid", tomdroid);
		//		hashMap.put("translate", translate);
		//		hashMap.put("triangle", triangle);
		//		hashMap.put("weightChart",weightChart);
		//		hashMap.put("keepass", keepass);
		//		hashMap.put("bequick", bequick);
		//		hashMap.put("bookcatalogue",bookcatalogue);


		String[] appsToProcess = {
				"a2dpVol",
				//				"aarddict",
				//				"aLogCat",
				//				"Amazed",
				//				"AnyCut",
				//				"anymemo",
				//				"batterydog",
				//				"swiftp",
				//				"bites",
				//				"blikenlights_battery",
				//				"alarmclock",
				//				"manpages",
				//				"mileage",
				//				"autoanswer",
				//				"hndroid",
				//				"multisms",
				//				"worldclock",
				//				"jamendo",
				//				"yahtzee",
				//				"countdownTimer",
				//				"sanity",
				//				"mirrored",
				//				"dialer",
				//				"fileexplorer",
				//				"adsdroid",
				//				"myLock",
				//				"lockPatternGenerator",
				//				"aGrep",
				//				"lolcatBuilder",
				//				"munchLife",
				//				"myexpenses",
				//				"LMN",
				//				"netcounter",
				//				"bomber",
				//				"frozenbubble",
				//				"blokish",
				//				"zooborns",
				//				"importcontacts",
				//				"wikipedia",
				//				"passMakerPro",
				//				"photostream",
				//				"quickSettings",
				//				"randomMusicPlayer",
				//				"soundboard",
				//				"spriteMethodTest",
				//				"spriteText",
				//				"syncmypix",
				//				"tippyTipper",
				//				"tomdroid",
				//				"translate",
				//				"triangle",
				//				"weightChart",
				//				"keepass",
				//				"bequick",
				//				"bookcatalogue"
		};

		String[] app = null;

		for(String appToProcess : appsToProcess){
			app = hashMap.get(appToProcess);
			//TerminalHelper.executeCommand("mkdir /Volumes/Macintosh_HD_2/Research-Files/MPlus.project/MPlus-Statistics-Generation/MPlus-Mutants-Generation/" + app[2]);
			System.out.println("Generating Mutants for: "+appToProcess);
			try {
				MPlus.runMPlus(app);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static final String[] a2dpVol  = {
			"/scratch/mtufano.scratch/projects/Android-Mutation/MDroidPlus/MDroidPlus/libs4ast",
			"/scratch/mtufano.scratch/tmp/MDroidPlustTest/apps/androtest_apps/a2dp.Vol_93_src/",
			// change this path
			"a2dp.Vol", 
	"/scratch/mtufano.scratch/tmp/MDroidPlustTest/out/a2dp.Vol",
	"/scratch/mtufano.scratch/tmp/MDroidPlustTest/operators/",
	"true"};


}