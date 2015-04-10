package org.rashid.NetworkVirtualisation.Utilities;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;


/**
 * Generates Poisson distribution arrivals and service times following exponential distribution
 * 
 * Also initiate process to delete a given VN request after it has expired
 * 
 * @author Rashid Mijumbi
 * 
 * @since  February 2014
 * 
 */

public class Poisson {

		public static int[] Interval(double lambda) //Time Between Arrivals - Exponential Distribution
		
		{   
			int num = 1500;     //Total Number of VN Requests
			
			int [] arriv = new int[num];
			
			for (int i = 0; i<num; i++){
			
			Random generator= new Random();
			
			double x = generator.nextDouble();
			
			double L = -Math.log(x)/lambda; 
		    
		    arriv[i] = (int) L;
		    
			}
		    
		    return arriv; 
		} 	
		
		
		public static int Duration(double lambda) //Average duration of each Request - Exponential Distribution
		
		{   

			Random generator= new Random();
			
			double x = generator.nextDouble();
			
			double L = -Math.log(x)/lambda; 
		    
		    return (int) L*1000; 
		    	    
		} 
		
		
		public static void startTimer(long Delay, final VirtualNetwork virtual, final List<VirtualNetwork> ExpiredVN) {
			
			 final long delay = Delay;
			
			 final Timer timer = new Timer(); 
			 			 
			 new Thread(new Runnable() {
				 
	            public void run() {
	           	 
	           	 timer.schedule(new ReleaseResources(virtual, ExpiredVN), delay);
	           	 
	               try {
	               	
	                  Thread.sleep(delay + 1*1000);
	                                    
	                  timer.cancel();
	                  
	                  	                  
	               } catch (InterruptedException e) {}
	            }
	            
	         }).start();
			
		}
			
	}


class ReleaseResources extends TimerTask {

	private final VirtualNetwork virtual;
	
	List<VirtualNetwork> expiredVN = new CopyOnWriteArrayList<VirtualNetwork>();

	public ReleaseResources(VirtualNetwork virtual, List<VirtualNetwork> ExpiredVN) {
		
	this.expiredVN = ExpiredVN;
		
    this.virtual = virtual;
    
	}

	@Override
	public void run() {
		
		expiredVN.add(virtual);
		            
	}
		
}
