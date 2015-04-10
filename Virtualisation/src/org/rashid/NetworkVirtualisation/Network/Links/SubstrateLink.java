package org.rashid.NetworkVirtualisation.Network.Links;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;



/**
 * 
 * This Class is a definition of a SubstrateLink
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */


public class SubstrateLink implements VirtualisationVocabulary, Cloneable, java.io.Serializable{
	
	public SubstrateLink(){
		
		
	}
	
	
	public void startLinkFailures(){
		
		final SubstrateLink slk = this;
		
	//	System.out.println(this.id+" Num: "+tets);
		
//		System.out.println(this.id+" Failure: "+mtbf);
		
//		System.out.println(this.id+" Repair: "+mttr);
		
		 new Thread(new Runnable() {
			 
	           public void run() {

	        	   	scheduleLinkFailure(slk);
	        	   	
	           }
	           
		 }).start();
	}

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2641092712615408575L;
	
	/**
	 * The minimum per unit cost of using this Link for any Virtual Link Mapping
	 */
	
	Double pricemin = 0.0;
	
	Double pricedeviation = 0.0;
	
	
	/**
	 * c1 and c2 are constants used in the logistic pricing model proposed for substrate resources.
	 */
	public Double c1 = 1 + generator.nextDouble()*(9);
	
	public Double c2 = 10 + generator.nextDouble()*(10);

	/**
	 * Total bandwidth capacity of Substrate Link
	 */
	
	Double totalbandwidth = 0.0; 
	
	/**
	 * Total available bandwidth capacity of Substrate Link. Difference between TotalBandwith and what is currently used by embedded Virtual Links
	 */
	
	Double availablebandwidth= 0.0;
	
	/**
	 * The Euclidean length of the Link
	 */
	
	Double linklength = 0.0;
	
	/**
	 * Delay of the Link, given by the Euclidean divided by the speed of Light
	 */
	
	Double linkdelay = 0.0;
	
	/**
	 * Unique Link ID
	 */
	
	String id = ""; 
	
	/**
	 * All virtual links mapped onto the substrate link
	 */
    
    ArrayList<VirtualLink> mappedvirtuallinks = new ArrayList<VirtualLink>();
    
    
	/**
	 * All virtual links for which resources have been reserved for backups
	 */
    
    ArrayList<VirtualLink> backedupvirtuallinks = new ArrayList<VirtualLink>();
    
    /**
     * Substrate Node at one end of the link
     */
    
    SubstrateNode startnode;
    
    /**
     * Substrate Node at other end of the link
     */
    
    SubstrateNode endnode;
    
    /**
     * SubstrateNetwork at one end of the Link
     */
    
    
    SubstrateNetwork startSN;
    
    /**
     * SubstrateNetwork at the other end of the Link
     */
    
    
    SubstrateNetwork endSN;
    
    /**
     * The Mean Time Between Failures for the Link
     */
    
    ArrayList<Double> mtbf = new ArrayList<Double>();
        
    int mtbf_num = 0;
    
    int mttr_num = 0;
    
    /**
     * whether substrate link is current online or otherwise!
     */
    
    boolean status = true;
    
    /**
     * The Mean Time To Repair when the Link Fails
     */
    
    ArrayList<Double> mttr = new ArrayList<Double>();
    
    
    /**
     * Mapping of each virtual link mapped onto the substrate link, and the corresponding virtual link flow carried by the substrate link
     */
    
    LinkedHashMap<VirtualLink, Double> virtuallinkflows = new LinkedHashMap<VirtualLink, Double>();
    
    
    /**
     * Mapping of each backed up virtual link mapped onto the substrate link, and the corresponding virtual link flow which would be carried by the substrate link
     */
    
    LinkedHashMap<VirtualLink, Double> backedupvirtuallinkflows = new LinkedHashMap<VirtualLink, Double>();
    
    
    /**
     * Returns the substrate node at one end of the link
     * @return
     */
    public SubstrateNode getStartNode(){
    	
    	return startnode;
    	
    }
    	
    /**
     * Returns the substrate node at the other end of the link
     * @return
     */
    public SubstrateNode getEndNode(){
    	
    	return endnode;
    	
    }  
    
    /**
     * returns substrate network at one end of the link    
     * @return
     */
    public SubstrateNetwork getStartSN(){
    	
    	return startSN;
    }
    
    
    /**
     * returns substrate network at the other end of the link    
     * @return
     */
    public SubstrateNetwork getEndSN(){
    	
    	return endSN;
    }
      
    
    /**
     * Returns the Total Bandwidth for the Substrate Link
     * @return
     */
    public Double getTotalBandWidth(){
    	
    	return totalbandwidth;
    	
    }  
    
    /**
     * returns the flow associated with the passed virtual link.
     * @param vlink
     * @return
     */
    
    public Double getVirtualLinkFlow(VirtualLink vlink){
    	
    	return virtuallinkflows.get(vlink);
    }
    
    
    /**
     * returns all the flows carried by this substrate link.
     * @return
     */
    
    public LinkedHashMap<VirtualLink, Double> getVirtualLinkFlows(){
    	
    	
    	return virtuallinkflows;
    }
    
    
    public Double getBackedUPVirtualLinkFlow(VirtualLink vlink){
    	
    	return backedupvirtuallinkflows.get(vlink);
    }
    
    public LinkedHashMap<VirtualLink, Double> getBackedUPVirtualLinkFlows(){
    	
    	
    	return backedupvirtuallinkflows;
    }
    
    /**
     * Returns the available bandwidth capacity
     * @return
     */
 
    public Double getAvailableBandwidth(){
    	
    	return availablebandwidth;
    	
    } 
    
    /**
     * Returns the Unique ID for the Node
     * @return
     */
    public String getID(){
    	
    	return id;
    	
    }   
    
    /**
     * Returns the the List of all virtual links that are mapped onto this links
     * @return
     */
    
    public ArrayList<VirtualLink> getMappedVirtualLinks(){
    	
    	return mappedvirtuallinks;
    	
    }
    
    
    public ArrayList<VirtualLink> getBackedUPVirtualLinks(){
    	
    	return backedupvirtuallinks;
    	
    }
    
    
    /**
     * returns length of link
     * @return
     */
    
    public Double getLinkLength(){
    	
    	return linklength;
    }
    
    /**
     * returns delay of link
     * @return
     */
    
    public Double getLinkDelay(){
    	
    	return linkdelay;
    }
    
    /**
     * sets delay of link
     * @param delay
     */
    
    public void setLinkDelay(Double delay){
    	
    	linkdelay = delay;
    }
    
    /**
     * sets length of link
     * @param length
     */
    
    public void setLinkLength(Double length){
    	
    	linklength = length;
    }
    
    
    /**
     * calculates and sets the euclidean length of the link
     */
    public void setLinkLength(){
    	
    		Double xdist = this.getStartNode().getX_Loc() - this.getEndNode().getX_Loc();
    		
    		Double ydist = this.getStartNode().getY_Loc() - this.getEndNode().getY_Loc();
    		
    		linklength = Math.sqrt(Math.pow(xdist, 2.0) + Math.pow(ydist, 2.0));

    }
    
    
    /**
     * calculates and sets the euclidean length of an interdomain Link using the mean of the InP as the connection point
     */
    public void setLinkLength(String Interdomain){
    	
    		Double xdist = (double) (this.getStartSN().getXMean() - this.getEndSN().getXMean());
    		
    		Double ydist = (double) (this.getStartSN().getYMean() - this.getEndSN().getYMean());
    		
    		linklength = Math.sqrt(Math.pow(xdist, 2.0) + Math.pow(ydist, 2.0));
    }
    
    /**
     * Sets the per unit cost of using this link
     * @param Cost
     */
    
    public void setMinimumPrice(Double Cost){
    	
    	pricemin = Cost;
    }
    
    public double getMinimumPrice(){
    	
    	return pricemin;
    }
    
    public void setPriceDeviation(Double dev){
    	
    	pricedeviation = dev;
    }
    
    public double getPriceDeviation(){
    	
    	return pricedeviation;
    }
    
    
    /**
     * calculates and sets delay of link
     * @param delay
     */
    
    public void setLinkDelay(){
    	
    	linkdelay = linklength/299792458; /*divide by speed of light*/;
    }
    
    
    /**
     * Sets the substrate node at one end of the link
     * @param startnode
     */
       
    public void setStartNode(SubstrateNode start){
    	
    	this.startnode = start;
    	
    }
    
    /**
     * Sets the substrate node at other end of the link
     * @param endnode
     */
       
    public void setEndNode(SubstrateNode end){
    	
    	this.endnode = end;
    	
    }
    
    /**
     * sets the Substrate Network at one end of the link
     * @param net
     */
    public void setStartSN(SubstrateNetwork net){
    	
    	startSN = net;
    }
    
    /**
     * sets the Substrate Network at the other end of the link
     * @param net
     */
    public void setEndSN(SubstrateNetwork net){
    	
    	endSN = net;
    }
    
    /**
     * adds the flow value "flow" to the corresponging virtual link "vlink" mapped onto the substrate link
     * @param vlink
     * @param flow
     */
    public void addVirtualLinkFlow(VirtualLink vlink, Double flow){
    	
    	virtuallinkflows.put(vlink, flow);
    }
    
    
    /**
     * removes the flow value of virtual link "vlink" mapped from the substrate link
     * @param vlink
     */
    public void removeVirtualLinkFlow(VirtualLink vlink){
    	
    	virtuallinkflows.remove(vlink);
    }
    
    
    public void addBackedUPVirtualLinkFlow(VirtualLink vlink, Double flow){
    	
    	backedupvirtuallinkflows.put(vlink, flow);
    }
    
    
    /**
     * removes the flow value of virtual link "vlink" mapped from the substrate link
     * @param vlink
     */
    public void removeBackedUPVirtualLinkFlow(VirtualLink vlink){
    	
    	backedupvirtuallinkflows.remove(vlink);
    }
    
    
    /**
     * sets the set of virtual links mapped onto this link
     * @param mappedvirtuallinks
     */
    public void setMappedVirtualLinks(ArrayList<VirtualLink> mapped){
    	
    	this.mappedvirtuallinks = mapped;
    	
    }
    
    
    /**
     * adds a single virtual link to the set of virtual links mapped onto this link
     * @param mappedvirtuallinks
     */
    public void addMappedVirtualLink(VirtualLink mapped){
    	
    	this.mappedvirtuallinks.add(mapped);
    	
    }
    
    
    /**
     * removes a single virtual link from the set of virtual links mapped onto this link
     * @param mappedvirtuallinks
     */
    public void removeMappedVirtualLink(VirtualLink mapped){
    	
    	this.mappedvirtuallinks.remove(mapped);
    	
    }
    

    public void setBackedUPVirtualLinks(ArrayList<VirtualLink> mapped){
    	
    	this.backedupvirtuallinks = mapped;
    	
    }
    

    public void addBackedUPVirtualLink(VirtualLink mapped){
    	
    	this.backedupvirtuallinks.add(mapped);
    	
    }
    

    public void removeBackedUPVirtualLink(VirtualLink mapped){
    	
    	this.backedupvirtuallinks.remove(mapped);
    	
    }
    
    

    /**
     * sets totalbandwidth
     * @param totalbandwidth
     */
    
    public void setTotalBandWidth(Double bandwidth){
    	
    	this.totalbandwidth = bandwidth;
    	}
    
    /**
     * sets availablebandwidth
     * @param availablebandwidth
     */
    
    public void setAvailableBandWidth(Double available){
    	
    	this.availablebandwidth = available;
    	}
    
    /**
     * sets id
     * @param id
     */
    
    public void setID(String id){
    	
    	this.id = id;
    	
    }
    
    /**
     * Returns variables representing mean time to repairs for the link
     * @return
     */
    public ArrayList<Double> getMTTR(){
    	
    	return mttr;
    }
    
    /**
     * Returns variables representing mean time between failures for the link.
     * @return
     */
    
    public ArrayList<Double> getMTBF(){
    	
    	return mtbf;
    }
    
    
    public double getTimeToNextFailure(){
    	
    	double x = mtbf.get(mtbf_num);
    	
    	if(mtbf_num < mtbf.size()-1)
    	mtbf_num += 1;
    	
    	return x;
    }
    
    
    public double getRepairTime(){
    	
    	double x = mttr.get(mttr_num);
    	
    	if(mttr_num < mttr.size()-1)
    	mttr_num += 1;
    	
    	return x;
    }
    
    
	public static void scheduleLinkFailure(final SubstrateLink link) { 
		 
	//	 new Thread(new Runnable() {
			 
    //       public void run() {
        	   
     //   	   while(true) {
        		   
        		 //  	 double delay = link.getTimeToNextFailure()*1000;
        		   	 
        		   	double delay = 10000 + generator.nextInt(500000000);
        		   	 
        		   //	 if(delay <5000)
        				
        			 Timer timer = new Timer();
          	 
        		     timer.schedule(new startFailureManager(link), (long) delay);
          	 
              try {
              	
                 Thread.sleep((long) (delay));
                                   
                 timer.cancel();
                                	                  
              } catch (InterruptedException e) {}
                         
        //	   } 
              
    //       }
           
    //    }).start();
		
	}
	
	
	public static void scheduleLinkRepair(final SubstrateLink link) { 
		 
	//	 new Thread(new Runnable() {
			 
  //        public void run() {
       		   
       		  // 	 double delay = link.getRepairTime()*1000;
       		   	 
       		  double delay = 5000 + generator.nextInt(25000);
       				
       			 Timer timer = new Timer();
         	 
       		     timer.schedule(new startRepairManager(link), (long) delay);
         	 
             try {
             	
                Thread.sleep((long) (delay));
                                  
                timer.cancel();
                               	                  
             } catch (InterruptedException e) {}

             
   //       }
          
  //     }).start();
		
	}
	

    
    /**
     * Clones link to produce new independent copy.
     */
    
    public Object clone() {
    	
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
        	
            // This should never happen
            throw new InternalError(e.toString());
        }
    }
    
    public void setLinkStatus(boolean sta){
    	
    	this.status = sta;
    }
    
    public boolean getLinkStatus(){
    	
    	return status;
    }
      
}





class startFailureManager extends TimerTask {
	
	SubstrateLink link;

	public startFailureManager(SubstrateLink lk) {
		
		link = lk;
    
	}

	@Override
	public void run() {
		
		System.out.println("The Link: "+link.getID()+" has failed . . . . "+System.currentTimeMillis()/1000);
		
		link.setLinkStatus(false);
		
		backUPMappedVirtualLinks(link);
		
		SubstrateLink.scheduleLinkRepair(link);
		            
	}

	private void backUPMappedVirtualLinks(SubstrateLink slink) {
				
		for(VirtualLink lk: slink.getMappedVirtualLinks()){
			
			lk.mappingendtime = System.currentTimeMillis()/1000;
			
			if(lk.primaryresources) {
			
  			for(SubstrateLink slk: lk.getMappingSubstrateLinks()){
				
				double flow = lk.getLinkFlow(slk);
				
				slk.getVirtualLinkFlows().remove(lk);
				
				slk.getMappedVirtualLinks().remove(lk);
				
				slk.setAvailableBandWidth(slk.getAvailableBandwidth()+flow);
										
			}
  			
  			if(lk.backedup){
  				
  				lk.backupmappingstarttime = System.currentTimeMillis()/1000;
  				
  				lk.primaryresources = false;
  			}
  			
  			else{
  				
  				lk.penaltystarttime = System.currentTimeMillis()/1000;
  			}
  			
			}
			
			else {
				
	  			for(SubstrateLink slk: lk.getBackUPSubstrateLinks()){
					
					double flow = lk.getLinkFlow(slk);
					
					slk.getVirtualLinkFlows().remove(lk);
					
					slk.getMappedVirtualLinks().remove(lk);
					
					slk.setAvailableBandWidth(slk.getAvailableBandwidth()+flow);
											
				}
	  			
	  			lk.backupmappingendtime = System.currentTimeMillis()/1000;
	  			
	  			lk.penaltystarttime = System.currentTimeMillis()/1000;
				
			}
  			
		}
		
		link.availablebandwidth = 0.0;
		
	}
		
}


class startRepairManager extends TimerTask {
	
	SubstrateLink link;

	public startRepairManager(SubstrateLink lk) {
		
		link = lk;
    
	}

	@Override
	public void run() {
		
		System.out.println("The Link: "+link.getID()+" has be repaired . . . . "+System.currentTimeMillis()/1000);
		
		link.setLinkStatus(true);
		
		link.availablebandwidth = link.totalbandwidth;

		
		SubstrateLink.scheduleLinkFailure(link);;
		            
	}
		
}
