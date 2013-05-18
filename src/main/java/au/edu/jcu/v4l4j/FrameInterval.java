/*
* Copyright (C) 2011 Gilles Gigan (gilles.gigan@gmail.com)
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

import au.edu.jcu.v4l4j.ResolutionInfo.DiscreteResolution;
import au.edu.jcu.v4l4j.ResolutionInfo.StepwiseResolution;
import au.edu.jcu.v4l4j.exceptions.UnsupportedMethod;

/**
 * This class encapsulates information about supported frame intervals for a 
 * given image format and resolution. A frame interval represents the amount of 
 * time elapsed between two captured frames. It is a floating-point 
 * number (in seconds) given as a numerator and denominator. Given a frame
 * interval 'fi', the corresponding frame rate 'fr' is 'fr = 1 / fi'.
 * 
 * A frame interval object is not directly instantiated. Instead, it can be 
 * obtained through either a {@link DiscreteResolution} object, a 
 * {@link StepwiseResolution} object or by calling 
 * {@link DeviceInfo#listIntervals(ImageFormat, int, int)}.
 * @author gilles
 *
 */
public class FrameInterval {
	
	public enum Type{
		/**
		 * An UNSUPPORTED type means supported frame interval information cannot
		 * be obtained from the driver. Calling any methods on the 
		 * {@link FrameInterval} object (except 
		 * {@link FrameInterval#getType()} will throw an
		 * {@link UnsupportedMethod} exception. 
		 */
		UNSUPPORTED,
		/**
		 * A DISCRETE type means frame intervals are reported as a list of 
		 * {@link DiscreteInterval} objects, using 
		 * {@link FrameInterval#getDiscreteIntervals()}
		 */
		DISCRETE,
		/**
		 * A STEPWISE type means that frame intervals are reported as minimum,
		 * maximum and step values, encapsulated in a
		 * {@link StepwiseInterval} object. Use 
		 * {@link FrameInterval#getStepwiseInterval()} to retrieve it.  
		 */
		STEPWISE
	};

	/**
	 * The type of this frame interval
	 */
	private final Type type;
	
	/**
	 * The list of discrete intervals if type ==  DISCRETE
	 */
	private List<DiscreteInterval> discreteValues;
	
	/**
	 * the stepwise interval object if type == STEPWISE
	 */
	private StepwiseInterval stepwiseInterval;
	
	/**
	 * this method returns the type of the frame interval for the given
	 * frame resolution. It assumes the query interface is acquired.
	 * @param o a pointer to a frame_size_discrete or frame_size_stepwise 
	 * struct
	 * @param t the type of the structure pointed to by 'o': 
	 * 0:frame_size_discrete, 1:frame_size_stepwise (min res), 
	 * 2:frame_size_stepwise (max res)  
	 * @return the interval type (0: unsupported, 1: discrete, 2: continuous)
	 */
	private native int doGetFrameIntvType(int t, long o);
	
	/**
	 * this method populates the discreteIntervals member for the given
	 * frame resolution. It assumes the query interface is acquired.
	 * @param o a pointer to a frame_size_discrete or frame_size_stepwise 
	 * struct
	 * @param t the type of the structure pointed to by 'o': 
	 * 0:frame_size_discrete, 1:frame_size_stepwise (min res), 
	 * 2:frame_size_stepwise (max res)  
	 */
	private native void doGetDiscrete(int t, long o);
	
	/**
	 * this method populates the continuousInterval member for the given
	 * frame resolution. It assumes the query interface is acquired.
	 * @param o a pointer to a frame_size_discrete or frame_size_stepwise 
	 * struct
	 * @param t the type of the structure pointed to by 'o': 
	 * 0:frame_size_discrete, 1:frame_size_stepwise (min res), 
	 * 2:frame_size_stepwise (max res)  
	 */
	private native void doGetStepwise(int t, long o);
	
	/**
	 * This method builds a new FrameInterval object.
	 * It must be called while libvideo's query interface is acquired !
	 * @param o a JNI pointer to a frame_size_discrete or frame_size_stepwise
	 * @param ptr_type the type of the structure pointed to by 'o':
	 * 0: o points to a struct frame_size_discrete and a FrameInterval
	 * object matching the interval_type in this structure is to be constructed
	 * 1: o points to a struct frame_size_continuous and a FrameInterval
	 * object matching the interval_type_min_res in this structure is to be 
	 * constructed 
	 * 2: o points to a struct frame_size_continuous and a FrameInterval
	 * object matching the interval_type_max_res in this structure is to be 
	 * constructed  
	 * 3: o points to NULL and the constructed object will be of type 
	 * {@link Type#UNSUPPORTED}.
	 * 4: o points to a struct frame_intv_discrete and a matching FrameInterval
	 * object is to be constructed
	 * 5: o points to a struct frame_intv_continuous and a matching FrameInterval
	 * object is to be constructed 
	 */
	FrameInterval(int ptr_type, long o){
		int frameIntvType;
		
		//check the pointer type
		switch(ptr_type){
		case 3:
			frameIntvType = 0; //UNSUPPORTED
			break;
		case 4:
			frameIntvType = 1; //DISCRETE
			break;
		case 5:
			frameIntvType = 2; //STEPWISE
			break;
		default:
			try {
				frameIntvType = doGetFrameIntvType(ptr_type, o);
			} catch (Exception e){
				//error checking frame interval type
				e.printStackTrace();
				System.err.println("There was an error checking the supported"
						+" interval types.\nPlease report this error to the"
						+"v4l4j mailing list.\nSee REAME file for "
						+"information on reporting bugs");
				type = Type.UNSUPPORTED;
				return;
			}
		}
		
		try {
			if(frameIntvType==1){
				discreteValues = new Vector<DiscreteInterval>();
				doGetDiscrete(ptr_type, o);
			} else if(frameIntvType==2){
				doGetStepwise(ptr_type, o);
			}
		} catch (Exception e){
			//error checking supported intervals
			e.printStackTrace();
			System.err.println("There was an error checking the supported"
					+" intervals.\nPlease report this error to the"
					+"v4l4j mailing list.\nSee REAME file for "
					+"information on reporting bugs");
			type = Type.UNSUPPORTED;
			return;
		}
		
		if(frameIntvType==0)
			type = Type.UNSUPPORTED;
		else if(frameIntvType==1){
			type = Type.DISCRETE;
		} else {
			type = Type.STEPWISE;
		}
	}
	
	/**
	 * This method returns the {@link Type} of this frame interval object.
	 * @return the {@link Type} of this frame interval object.
	 */
	public Type getType(){
		return type;
	}
	
	/**
	 * This method returns a list of {@link DiscreteInterval}s, or throws a 
	 * {@link UnsupportedMethod} exception if this frame interval object
	 * is not of type {@link Type#DISCRETE}.
	 * @return a list of {@link DiscreteInterval}s
	 * @throws UnsupportedMethod if this frame interval object
	 * is not of type {@link Type#DISCRETE}.
	 */
	public List<DiscreteInterval> getDiscreteIntervals(){
		if(type!=Type.DISCRETE)
			throw new UnsupportedMethod("Supported intervals are not discrete");
		return new Vector<DiscreteInterval>(discreteValues);
	}
	
	
	/**
	 * This method returns a {@link StepwiseInterval} object, or throws a 
	 * {@link UnsupportedMethod} exception if this frame interval object
	 * is not of type {@link Type#STEPWISE}.
	 * @return a {@link StepwiseInterval} object
	 * @throws UnsupportedMethod if this frame interval object
	 * is not of type {@link Type#STEPWISE}.
	 */
	public StepwiseInterval getStepwiseInterval(){
		if(type!=Type.STEPWISE)
			throw new UnsupportedMethod("Supported interval is not continuous");
		return stepwiseInterval;
	}
	
	@Override
	public String toString(){
		String s;
		if(type==Type.DISCRETE){
			s = "";
			for(DiscreteInterval i: discreteValues)
				s += i.toString() + " - ";
		} else if(type==Type.STEPWISE){
			s = stepwiseInterval.toString();
		} else
			s = "no frame interval information";
		return s;
	}
	
	
	/**
	 * This class encapsulates information about a discrete frame interval, 
	 * expressed as a numerator and denominator. {@link DiscreteInterval} 
	 * objects are not instantiated directly, instead these objects can be 
	 * obtained through a {@link FrameInterval} object.
	 * 
	 * @author gilles
	 *
	 */
	public static class DiscreteInterval {	
		/**
		 * The numerator for this discrete frame interval.
		 */
		public final int numerator;
		
		/**
		 * The denominator for this discrete frame interval.
		 */
		public final int denominator;
		
		DiscreteInterval(int n, int d){
			numerator = n;
			denominator = d;
		}
		
		/**
		 * This method returns the numerator of this frame interval.
		 * @return the numerator of the frame interval
		 */
		public int getNum(){
			return numerator;
		}
		
		/**
		 * This method returns the denominator of this frame interval.
		 * @return the denominator of this frame interval
		 */
		public int getDenom(){
			return denominator;
		}
		
		@Override
		public String toString(){
			return numerator+"/"+denominator;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + denominator;
			result = prime * result + numerator;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof DiscreteInterval))
				return false;
			DiscreteInterval other = (DiscreteInterval) obj;
			if (denominator != other.denominator)
				return false;
			if (numerator != other.numerator)
				return false;
			return true;
		}
	}
	
	/**
	 * A continuous frame interval is a bounded frame interval. It has 
	 * minimum, maximum and step values, all expressed as numerator/denominator
	 * pairs contained in a {@link DiscreteInterval} object. A continuous 
	 * interval object encapsulates the bounds for a continuous frame interval. 
	 * @author gilles
	 *
	 */
	public static class StepwiseInterval {
		/**
		 * The minimum frame interval value
		 */
		public final DiscreteInterval minIntv;
		
		/**
		 * The maximum frame interval value
		 */
		public final DiscreteInterval maxIntv;
		
		/**
		 * The step frame interval value
		 */
		public final DiscreteInterval stepIntv;
		
		private StepwiseInterval(int minN, int minD, int maxN, int maxD, 
				int stepN, int stepD) {
			minIntv = new DiscreteInterval(minN, minD);
			maxIntv = new DiscreteInterval(maxN, maxD);
			stepIntv = new DiscreteInterval(stepN, stepD);
		}
		
		/**
		 * This continuous frame interval has a minimum bound. This method 
		 * returns it.
		 * @return the minimum value of this continuous frame interval
		 */
		public DiscreteInterval getMinInterval(){
			return minIntv;
		}
		
		/**
		 * This continuous frame interval has a maximum bound. This method 
		 * returns it.
		 * @return the minimum value of this continuous frame interval
		 */
		public DiscreteInterval getMaxInterval(){
			return maxIntv;
		}
		
		/**
		 * This continuous frame interval has a maximum bound. This method 
		 * returns it.
		 * @return the minimum value of this continuous frame interval
		 */
		public DiscreteInterval getStepInterval(){
			return stepIntv;
		}
		
		@Override
		public String toString(){
			return minIntv+" - "+maxIntv+" - "+stepIntv;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((maxIntv == null) ? 0 : maxIntv.hashCode());
			result = prime * result
					+ ((minIntv == null) ? 0 : minIntv.hashCode());
			result = prime * result
					+ ((stepIntv == null) ? 0 : stepIntv.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof StepwiseInterval))
				return false;
			StepwiseInterval other = (StepwiseInterval) obj;
			if (maxIntv == null) {
				if (other.maxIntv != null)
					return false;
			} else if (!maxIntv.equals(other.maxIntv))
				return false;
			if (minIntv == null) {
				if (other.minIntv != null)
					return false;
			} else if (!minIntv.equals(other.minIntv))
				return false;
			if (stepIntv == null) {
				if (other.stepIntv != null)
					return false;
			} else if (!stepIntv.equals(other.stepIntv))
				return false;
			return true;
		}
	}
	
}
