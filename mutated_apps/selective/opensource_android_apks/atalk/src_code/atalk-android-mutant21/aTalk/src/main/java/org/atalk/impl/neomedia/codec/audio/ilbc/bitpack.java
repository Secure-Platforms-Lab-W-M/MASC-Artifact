/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 * 
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.impl.neomedia.codec.audio.ilbc;

/**
 * @author Jean Lorchat
 */
class bitpack
{

	int firstpart;
	int rest;

	public bitpack()
	{
		firstpart = 0;
		rest = 0;
	}

	public bitpack(int fp, int r)
	{
		firstpart = fp;
		rest = r;
	}

	public int get_firstpart()
	{
		return firstpart;
	}

	public void set_firstpart(int fp)
	{
		firstpart = fp;
	}

	public int get_rest()
	{
		return rest;
	}

	public void set_rest(int r)
	{
		rest = r;
	}

}
