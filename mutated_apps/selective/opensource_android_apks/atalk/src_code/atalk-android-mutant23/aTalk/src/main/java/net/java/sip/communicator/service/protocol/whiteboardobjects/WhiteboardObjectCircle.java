/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.java.sip.communicator.service.protocol.whiteboardobjects;

import net.java.sip.communicator.service.protocol.*;

/**
 * Used to access the content of instant whiteboard objects that are sent or received via the
 * WhiteboardOperationSet.
 *
 * @author Julien Waechter
 */
public interface WhiteboardObjectCircle extends WhiteboardObject
{
	/**
	 * A type string constant indicating that an object is of type circle.
	 */
	public static final String NAME = "WHITEBOARDOBJECTCIRCLE";

	/**
	 * Returns the coordinates of this whiteboard object.
	 *
	 * @return the coordinates of this object.
	 */
	public WhiteboardPoint getWhiteboardPoint();

	/**
	 * Sets the coordinates of this whiteboard object.
	 *
	 * @param whiteboardPoint
	 *        the coordinates of this object.
	 */
	public void setWhiteboardPoint(WhiteboardPoint whiteboardPoint);

	/**
	 * Returns the radius (in pixels) of this whiteboard circle.
	 *
	 * @return the number of pixels for the radius.
	 */
	public double getRadius();

	/**
	 * Sets the radius (in pixels) of this whiteboard circle.
	 *
	 * @param radius
	 *        the number of pixels for the radius.
	 */
	public void setRadius(double radius);

	/**
	 * Returns the fill state of the WhiteboardObject.
	 *
	 * @return True is filled, false is unfilled.
	 */
	public boolean isFill();

	/**
	 * Sets the fill state of the WhiteboardObject. True is filled, false is unfilled.
	 *
	 * @param fill
	 *        The new fill state.
	 */
	public void setFill(boolean fill);

	/**
	 * Specifies the background color for this object. The color parameter must be encoded with
	 * standard RGB encoding: bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue.
	 *
	 * @param color
	 *        the color that we'd like to set for the background of this <tt>WhiteboardObject</tt>
	 *        (using standard RGB encoding).
	 */
	public void setBackgroundColor(int color);

	/**
	 * Returns an integer representing the background color of this object. The return value uses
	 * standard RGB encoding: bits 24-31 are alpha, 16-23 are red, 8-15 are green, 0-7 are blue.
	 *
	 * @return the RGB value of the background color of this object.
	 */
	public int getBackgroundColor();
}
