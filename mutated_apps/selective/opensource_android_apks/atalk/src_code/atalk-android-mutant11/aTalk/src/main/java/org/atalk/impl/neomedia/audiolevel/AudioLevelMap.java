/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.audiolevel;

/**
 * The class implements a basic mapping utility that allows binding <tt>long</tt> CSRC ID-s to
 * <tt>int</tt> audio levels. The class does not implement any synchronization for neither read nor
 * write operations but it is still intended to handle concurrent access in a manner that can be
 * considered graceful for the audio level use case. The class uses a bi-dimensional
 * <tt>long[][]</tt> matrix that is recreated every time a new CSRC is added or an existing one is
 * removed. Iterating through the matrix is only possible after obtaining a direct reference to it.
 * It is possible for this reference to become invalid shortly after someone has obtained it (e.g.
 * because someone added a new CSRC) but this should not cause problems for the CSRC audio level
 * delivery case.
 *
 * @author Emil Ivov
 */
public class AudioLevelMap
{
	/**
	 * The matrix containing a CSRC-to-level mappings.
	 */
	private long[][] levels = null;

	/**
	 * If this map already contains <tt>csrc</tt> this method updates its level, otherwise we add a
	 * new entry mapping <tt>csrc</tt> to <tt>level</tt>.
	 *
	 * @param csrc
	 * 		the CSRC key that we'd like to add/update.
	 * @param level
	 * 		the new audio level for the specified <tt>csrc</tt>.
	 */
	public void putLevel(long csrc, int level)
	{
		// copy the levels matrix so that no one pulls it from under our feet.
		long[][] levelsRef = levels;
		int csrcIndex = findCSRC(levelsRef, csrc);

		if (csrcIndex == -1) {
			// we don't have the csrc in there yet so we need a new row.
			levels = appendCSRCToMatrix(levelsRef, csrc, level);
		}
		else {
			levelsRef[csrcIndex][1] = level;
		}
	}

	/**
	 * Removes <tt>csrc</tt> and its mapped level from this map.
	 *
	 * @param csrc
	 * 		the CSRC ID that we'd like to remove from this map.
	 * @return <tt>true</tt> if <tt>csrc</tt> was present in the <tt>Map</tt> and <tt>false</tt>
	 * otherwise.
	 */
	public boolean removeLevel(long csrc)
	{
		// copy the levels matrix so that no one pulls it from under our feet.
		long[][] levelsRef = levels;
		int index = findCSRC(levelsRef, csrc);

		if (index == -1)
			return false;

		if (levelsRef.length == 1) {
			levels = null;
			return true;
		}

		// copy levelsRef into newLevels ref making sure we skip the entry
		// containing the CSRC ID that we are trying to remove;
		long[][] newLevelsRef = new long[levelsRef.length - 1][];

		System.arraycopy(levelsRef, 0, newLevelsRef, 0, index);
		System.arraycopy(levelsRef, index + 1, newLevelsRef, index, newLevelsRef.length - index);

		levels = newLevelsRef;
		return true;
	}

	/**
	 * Returns the audio level of the specified <tt>csrc</tt> id or <tt>-1</tt> if <tt>csrc</tt> is
	 * not currently registered in this map.
	 *
	 * @param csrc
	 * 		the CSRC ID whose level we'd like to obtain.
	 * @return the audio level of the specified <tt>csrc</tt> id or <tt>-1</tt> if <tt>csrc</tt> is
	 * not currently registered in this map.
	 */
	public int getLevel(long csrc)
	{
		long[][] levelsRef = levels;
		int index = findCSRC(levelsRef, csrc);

		return (index == -1) ? -1 : ((int) levelsRef[index][1]);
	}

	/**
	 * Returns the index of the specified <tt>csrc</tt> level in the <tt>levels</tt> matrix or
	 * <tt>-1</tt> if <tt>levels</tt> is <tt>null</tt> or does not contain <tt>csrc</tt>.
	 *
	 * @param levels
	 * 		the bi-dimensional array that we'd like to search for the specified <tt>csrc</tt>.
	 * @param csrc
	 * 		the CSRC identifier that we are looking for.
	 * @return the the index of the specified <tt>csrc</tt> level in the <tt>levels</tt> matrix or
	 * <tt>-1</tt> if <tt>levels</tt> is <tt>null</tt> or does not contain <tt>csrc</tt>.
	 */
	private int findCSRC(long[][] levels, long csrc)
	{
		if (levels != null) {
			for (int i = 0; i < levels.length; i++) {
				if (levels[i][0] == csrc)
					return i;
			}
		}
		return -1;
	}

	/**
	 * Creates a new bi-dimensional array containing all entries (if any) from the <tt>levels</tt>
	 * matrix and an extra entry for the specified <tt>csrc</tt> and <tt>level</tt>.
	 *
	 * @param levels
	 * 		the bi-dimensional levels array that we'd like to add a mapping to.
	 * @param csrc
	 * 		the CSRC identifier that we'd like to add to the <tt>levels</tt> bi-dimensional array.
	 * @param level
	 * 		the level corresponding to the <tt>csrc</tt> identifier.
	 * @return a new matrix containing all entries from levels and a new one mapping <tt>csrc</tt>
	 * to <tt>level</tt>
	 */
	private long[][] appendCSRCToMatrix(long[][] levels, long csrc, int level)
	{
		int newLength = 1 + ((levels == null) ? 0 : levels.length);
		long[][] newLevels = new long[newLength][];

		// put the new level.
		newLevels[0] = new long[]{csrc, level};

		if (newLength == 1)
			return newLevels;

		System.arraycopy(levels, 0, newLevels, 1, levels.length);

		return newLevels;
	}
}
