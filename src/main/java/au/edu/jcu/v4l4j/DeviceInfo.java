/*
* Copyright (C) 2007-2008 Gilles Gigan (gilles.gigan@gmail.com)
* eResearch Centre, James Cook University (eresearch.jcu.edu.au)
*
* This program was developed as part of the ARCHER project
* (Australian Research Enabling Environment) funded by a   
* Systemic Infrastructure Initiative (SII) grant and supported by the Australian
* Department of Innovation, Industry, Science and Research
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public  License as published by the
* Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE.  
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package au.edu.jcu.v4l4j;

import java.util.List;
import java.util.Vector;

import au.edu.jcu.v4l4j.exceptions.StateException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

/**
 * Objects of this class encapsulate the following information about a video 
 * device:
 * <ul>
 * <li>its device file,</li>
 * <li>the name of the video device,</li>
 * <li>a list of {@link InputInfo} objects providing information about each 
 * video input,</li>
 * <li>a list of {@link ImageFormat}s, capture resolutions 
 * ({@link ResolutionInfo}) and frame intervals supported by the video device.
 * </ul>
 * To retrieve information about a video device, call 
 * {@link VideoDevice#getDeviceInfo()} on its 
 * <code>VideoDevice</code> object, and retrieve parameters of interest.
 * Example:<br>
 * <code>
 * VideoDevice vd = new VideoDevice("/dev/video0");<br>
 * DeviceInfo d = vd.getDeviceInfo();<br>
 * System.out.println("name: "+d.getName());<br>
 * System.out.println("Device file: "+d.getDeviceFile());<br>
 * System.out.println("Supported formats:");<br>
 * for(ImageFormat f : d.getFormatList().getNativeFormats())<br>
 * &nbsp;&nbsp;System.out.println("\t"+f.toNiceString());<br>
 * System.out.println("\tFormats that can be RGB24-converted: "
 *				+(vd.supportRGBConversion()?"":"None"));<br>
 * if(vd.supportRGBConversion())<br>
 * &nbsp;&nbsp;for(ImageFormat f: d.getFormatList().getRGBEncodableFormats())<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\t"+f.toNiceString());<br>
 * System.out.println("Inputs:");<br>
 * for(InputInfo i: d.getInputs()){<br>
 * &nbsp;&nbsp;System.out.println("\tName: "+i.getName());<br>
 * &nbsp;&nbsp;System.out.println("\tType: "+i.getType()+"("+
 * (i.getType() == V4L4JConstants.CAMERA ? "Camera" : "Tuner")+")");<br>
 * &nbsp;&nbsp;System.out.println("\tIndex: "+i.getIndex());<br>
 * &nbsp;&nbsp;System.out.println("\tSupported standards:");<br>
 * &nbsp;&nbsp;for(Integer s: i.getSupportedStandards()){<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.print("\t\t"+s);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;if(s==V4L4JConstants.PAL)<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("(PAL)");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;else if(s==V4L4JConstants.NTSC)<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("(NTSC)");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;else if(s==V4L4JConstants.SECAM)<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("(SECAM)");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;else<br
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("(None/Webcam)");<br>
 * &nbsp;&nbsp;}<br>
 * &nbsp;&nbsp;if(i.getType() == V4L4JConstants.TUNER) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;TunerInfo t = i.getTuner();<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\tTuner");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tname: "+t.getName());<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tIndex: "+t.getIndex());<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tRange high: "+
 * t.getRangeHigh());<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tRange low: "+
 * t.getRangeLow());<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tUnit: "+
 * t.getUnit()+"("+(t.getUnit() == TunerInfo.MHZ ? "MHz" : "kHz")+")");<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("\t\tType: "+
 * t.getType()+"("+(t.getType() == TunerInfo.RADIO ? "Radio" : "TV")+")");<br>
 * &nbsp;&nbsp;}<br>
 * }<br>
 * vd.release();<br>
 * </code>
 * <br>
 * {@link DeviceInfo} objects can not be used anymore once the matching 
 * VideoDevice object has been released. Calling any methods will then throw
 * a {@link StateException}. 
 * 
 * @author gilles
 *
 */
public class DeviceInfo {	
	/**
	 * Native method interacting with libvideo to get info on device f
	 * @param f the full path to the V4L device file
	 */
	private native void getInfo(long o);
	
	/**
	 * this method releases the libvideo query interface 
	 * @param o a pointer to a v4l4j_Device struct
	 */
	private native void doRelease(long o);
	
	/**
	 * this method returns a frame interval object representing the supported
	 * frame intervals for capture at the given resolution using the given 
	 * format
	 * @param o a JNI pointer to a  struct v4l4j_device
	 * @param imf the libvideo image format
	 * @param w the capture width
	 * @param h the capture height
	 * @return a frame interval object
	 */
	private native FrameInterval doListIntervals(long o, int imf, int w, int h);
	
	
	static {
		try {
			System.loadLibrary("v4l4j");
		} catch (UnsatisfiedLinkError e) {
			System.err.println("Cant load v4l4j JNI library");
			throw e;
		}
	}
	
	/**
	 * The name of the video device
	 */
	private String name;
	
	/**
	 * The path to the device file
	 */
	private String deviceFile;
	
	/**
	 * A list of inputs connected to this device
	 */
	private List<InputInfo> inputs;
	
	/**
	 * A list of supported image formats
	 */
	private ImageFormatList formats;	
	
	/**
	 * a pointer to a C struct v4l4j_device
	 */
	private long object;
	
	/**
	 * whether the device info has been released
	 */
	private boolean released;
	
	
	/**
	 * This method returns the name of the video device.
	 * @return the name of the video device.
	 * @throws StateException if the associated VideoDevice has been released
	 */
	public synchronized String getName() {
		checkRelease();
		return name;
	}

	/**
	 * This method returns the device file associated with the video device.
	 * @return the device file
	 * @throws StateException if the associated VideoDevice has been released
	 */
	public synchronized String getDeviceFile() {
		checkRelease();
		return deviceFile;
	}


	/**
	 * This method returns a list of {@link InputInfo} objects which provide
	 * information about each video input supported by the video device.
	 * @return a list of <code>InputInfo</code> objects
	 * @throws StateException if the associated VideoDevice has been released
	 * @see InputInfo
	 */
	public synchronized List<InputInfo> getInputs() {
		checkRelease();
		return new Vector<InputInfo>(inputs);
	}


	/**
	 * This method returns an {@link ImageFormatList} object containing
	 * the various image formats, capture resolutions and frame intervals the 
	 * video device supports.
	 * @return an<code>ImageFormatList</code> object
	 * @throws StateException if the associated VideoDevice has been released
	 * @see ImageFormatList
	 */
	public synchronized ImageFormatList getFormatList() {
		checkRelease();
		return formats;
	}
	
	
	/**
	 * This method returns a {@link FrameInterval} object containing information
	 * about the supported frame intervals for capture at the given resolution
	 * and image format. <b>Note that the returned {@link FrameInterval} object
	 * could have its type set to {@link FrameInterval.Type#UNSUPPORTED} if the
	 * driver does not support frame interval enumeration OR if the device is
	 * currently being used by another application and frame intervals cannot
	 * be enumerated at this time.</b><br>Frame interval information can also
	 * be obtained through {@link ResolutionInfo} objects, attached to each
	 * {@link ImageFormat}. See {@link #getFormatList()}.
	 * @return a {@link FrameInterval} object containing information about
	 * the supported frame intervals
	 * @param imf the capture image format for which the frame intervals should 
	 * be enumerated
	 * @param width the capture width for which the frame intervals should be 
	 * enumerated
	 * @param height the capture height for which the frame intervals should be 
	 * enumerated
	 * @throws StateException if the associated VideoDevice has been released
	 */
	public synchronized FrameInterval listIntervals(ImageFormat imf, 
			int width, int height){
		checkRelease();
		return doListIntervals(object, imf.getIndex(), width, height);
	}


	/**
	 * This constructor build a DeviceInfo object containing information about 
	 * the given V4L device.
	 * @param object the JNI C pointer to struct v4l4j_device
	 * @throws V4L4JException if there is an error retrieving information from
	 * the video device.
	 */	
	DeviceInfo(long object, String dev) throws V4L4JException{
		inputs = new Vector<InputInfo>();
		deviceFile = dev;
		getInfo(object);
		this.object = object;
	}
	
	/**
	 * This method releases the libvideo query interface
	 */
	synchronized void release() {
		doRelease(object);
	}
	
	/**
	 * checks if this object has been released (by the owning video device 
	 * object). If yes, throws a {@link StateException}
	 * @throws StateException if this object has been released 
	 */
	private void checkRelease() throws StateException{
		if(released)
			throw new StateException("The video device object has been released");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deviceFile == null) ? 0 : deviceFile.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DeviceInfo)) {
			return false;
		}
		DeviceInfo other = (DeviceInfo) obj;
		if (deviceFile == null) {
			if (other.deviceFile != null) {
				return false;
			}
		} else if (!deviceFile.equals(other.deviceFile)) {
			return false;
		}
		return true;
	}
}
