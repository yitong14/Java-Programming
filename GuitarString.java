// this class models a vibrating guitar string of a 
// given frequency.

import java.util.*;

public class GuitarString {
   private Queue<Double> ringBuffer;
      
   public static final int SAMPLE_RATE = StdAudio.SAMPLE_RATE;
   public static final double ENERGY_DECAY_FACTOR = 0.996;
   
   // pre : frequency > 0 && resulting size of the ring buffer >= 2
   //       (throws an IllegalArgumentException if any of these is not satisfied)
   //       (resulting size means sampling rate divided by frequency, rounded to nearest integer)
   // post: Constructs a guitar string of the given frequency by sampling its displacement
   //       at N equally spaced points.
   public GuitarString(double frequency) {
      if (frequency <= 0) {
         throw new IllegalArgumentException("illegal frequency: " + frequency);
      }
      checkSize(Math.round((float) (SAMPLE_RATE / frequency)));
      
      int N = Math.round((float) (SAMPLE_RATE / frequency));
      ringBuffer = new LinkedList<Double>();
      for (int i = 0; i < N; i++) {
         ringBuffer.add(0.0);
      }
   }
   
   // pre : numbers of the elements in the array >= 2
   //       (throws an IllegalArgumentException if not)
   // post: Constructs a giutar string and initializes the contents of the ring buffer
   //       to the values in the array
   public GuitarString(double[] init) {
      checkSize(init.length);
      
      ringBuffer = new LinkedList<Double>();
      for (double val : init) {
         ringBuffer.add(val);
      }
   }
   
   // post: replaces N elements in the ring buffer with N random values
   //       (between -0.5 and 0.5)
   public void pluck() {
      for (int i = 0; i < ringBuffer.size(); i++) {
         ringBuffer.remove();
         ringBuffer.add(Math.random() - 0.5);
      }
   }
   
   // post: apply the Karplus-Strong update once, that is removing first sample 
   //       from the front of ring buffer and adds to the end using Karplus-Strong algorithm
   public void tic() {
      double removedElement = ringBuffer.remove();
      ringBuffer.add(ENERGY_DECAY_FACTOR * (ringBuffer.peek() + removedElement) / 2);
   }
   
   // post: returns the current sample (the value at the front of the ring buffer).
   public double sample() {
      return ringBuffer.peek();
   }
   
   // post: checks if the given size is more than or equals to 2
   //       (throws an IllegalArgumentException if not)
   private void checkSize(int size) {
      if (size < 2) {
         throw new IllegalArgumentException("illegal size: " + size);
      }
   }
}