package org.rashid.NetworkVirtualisation.Network.Links;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;





import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.VirtualNode;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

/**
 * 
 * This Class is a definition of a VirtualLink
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */


public class VirtualLink implements VirtualisationVocabulary, Cloneable, java.io.Serializable{
	
	public VirtualLink(){
		
		qos = 1 + generator.nextInt(5);
		
		income = Y_min*Math.pow((Y_max/Y_min),(qos-1)) + generator.nextDouble()*(Y_max*Math.pow((Y_max/Y_min),(qos-1)) - Y_min*Math.pow((Y_max/Y_min),(qos-1)));
		
		penalty = Z_min*Math.pow((Z_max/Z_min),(qos-1)) + generator.nextDouble()*(Z_max*Math.pow((Z_max/Z_min),(qos-1)) - Z_min*Math.pow((Z_max/Z_min),(qos-1)));
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5963787343334254002L;

	/**
	 * Total required bandwidth capacity of Virtual Link
	 */
	
	Double totalbandwidth = 0.0; 
	
	/**
	 * the time when the virtual link gets mapped. used for calculating costs/income.
	 */
	
	public long mappingstarttime = 0;
	
	public long backupmappingstarttime = 0;
	
	public long penaltystarttime = 0;
	
	public long mappingendtime = 0;
	
	public long backupmappingendtime = 0;
	
	/**
	 * indicates if the original mapping substrate link already failed and the current link is utilising back up resources.
	 */
	
	public boolean primaryresources = true;
	
	
	/**
	 * indicates whether resources have been provisioned for link back-up.
	 */
	public boolean backedup = false;
	
	
	
	/**
	 * Total available bandwidth capacity of Virtual Link. Difference between TotalBandwith and what is currently used by, say, user traffic
	 */
	
	Double availablebandwidth= 0.0;
	
	/**
	 * Unique Link ID
	 */
	
	String id = "";
	
	
	int qos = 1;
	
	double income = 0.0;
	
	public double penalty = 0.0;
	
	double previousprice = 0.0;
	
	double reserveprice = 0.0;
	
	/**
	 * The Euclidean length of the Link
	 */
	
	Double linklength = 0.0;
	
	/**
	 * Delay of the Link, given by the Euclidean divided by the speed of Light
	 */
	
	Double linkdelay = 0.0;
	
	/**
	 * All substrate links onto which the Virtual link is mapped
	 */
    
    ArrayList<SubstrateLink> mappingsubstratelinks = new ArrayList<SubstrateLink>();
    
    
	/**
	 * All substrate links onto which the Virtual link may be mapped incase of physical link failure
	 */
    
    ArrayList<SubstrateLink> backupsubstratelinks = new ArrayList<SubstrateLink>();
    
	/**
	 * All substrate networks onto which the Virtual link is mapped
	 */
    
    ArrayList<SubstrateNetwork> mappingsubstratenetworks = new ArrayList<SubstrateNetwork>();
    
	/**
	 * All substrate networks onto which the Virtual link may be mapped incase of physical link failure
	 */
    
    ArrayList<SubstrateNetwork> backupsubstratenetworks = new ArrayList<SubstrateNetwork>();
    
    /**
     * Virtual Node at one end of the link
     */
    
     VirtualNode startnode;
    
    /**
     * Virtual Node at other end of the link
     */
    
    VirtualNode endnode;
    
    VirtualNetwork virtualnetwork;
    
    /**
     * The amount of this virtual link's flow that is carried by the respective substrate links onto which it is mapped
     */
       
    LinkedHashMap<SubstrateLink, Double> linkflows = new LinkedHashMap<SubstrateLink, Double>();
    
    
    /**
     * The amount of this virtual link's flow that will be carried by each back-up substrate link
     */
       
    LinkedHashMap<SubstrateLink, Double> backuplinkflows = new LinkedHashMap<SubstrateLink, Double>();
    
    
    public boolean completedbackup = false;
    
    /**
     * Returns the Virtual node at one end of the link
     * @return
     */
    public VirtualNode getStartNode(){
    	
    	return startnode;
    	
    }
    
    
    public void setVirtualNetwork(VirtualNetwork vn){
    	
    	virtualnetwork = vn;
    }
    
    /**
     * returns the amount of this link's flow carried by the passed substrate link
     * @param slink
     * @return
     */
    
    public Double getLinkFlow(SubstrateLink slink){
    	
    	for(Iterator<SubstrateLink> slk = linkflows.keySet().iterator(); slk.hasNext();){
    		
    		SubstrateLink lk = slk.next();
    		
    		if(lk.getID().equalsIgnoreCase(slink.getID())){
    			
    			return linkflows.get(lk);
    		}
	
    	}
    	
    	System.err.println("The Substrate Link Flow for: "+slink.getID()+"  cannot be found . . . .");
    	
    	return linkflows.get(slink);
    }
    
    
    /**
     * returns the amount of this link's flow carried by the passed substrate link
     * @param slink
     * @return
     */
    
    public Double getBackUPLinkFlow(SubstrateLink slink){
    	
    	for(Iterator<SubstrateLink> slk = backuplinkflows.keySet().iterator(); slk.hasNext();){
    		
    		SubstrateLink lk = slk.next();
    		
    		if(lk.getID().equalsIgnoreCase(slink.getID())){
    			
    			return backuplinkflows.get(lk);
    		}
	
    	}
    	
    	System.err.println("The Substrate Link Flow for: "+slink.getID()+"  cannot be found . . . .");
    	
    	return backuplinkflows.get(slink);
    }
    
    
    /**
     * returns the amount of this link's flow carried by all substrate links
     * @return
     */
    
    public LinkedHashMap<SubstrateLink, Double> getLinkFlows(){
    	
    	return linkflows;
    }
    
    /**
     * returns the amount of this link's flow carried by all substrate links
     * @return
     */
    
    public LinkedHashMap<SubstrateLink, Double> getBackUPLinkFlows(){
    	
    	return backuplinkflows;
    }
    
    	
    /**
     * Returns the Virtual node at the other end of the link
     * @return
     */
    public VirtualNode getEndNode(){
    	
    	return endnode;
    	
    }  
      
    
    /**
     * Returns the Total Bandwidth for the Virtual Link
     * @return
     */
    public Double getTotalBandWidth(){
    	
    	return totalbandwidth;
    	
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
     * Returns the the List of all substrate links mapping this virtual link
     * @return
     */
    
    public ArrayList<SubstrateLink> getMappingSubstrateLinks(){
    	
    	return mappingsubstratelinks;
    	
    }
    
    
    /**
     * Returns the the List of all substrate links mapping this virtual link
     * @return
     */
    
    public ArrayList<SubstrateLink> getBackUPSubstrateLinks(){
    	
    	return backupsubstratelinks;
    	
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
    	
    	linkdelay = length;
    }
    
    
    
    /**
     * Sets the virtual node at one end of the link
     * @param startnode
     */
       
    public void setStartNode(VirtualNode start){
    	
    	this.startnode = start;
    	
    }
    
    /**
     * Sets the virtual node at other end of the link
     * @param endnode
     */
       
    public void setEndNode(VirtualNode end){
    	
    	this.endnode = end;
    	
    }
    
    
    /**
     * adds the substrate link slink and its corresponding flow value to the set of substrate links supporting this virtual link
     * @param slink
     * @param flow
     */
    
    public void addLinkFlow(SubstrateLink slink, Double flow){
    	
    	linkflows.put(slink, flow);
    }
    
    
    public void addBackUPLinkFlow(SubstrateLink slink, Double flow){
    	
    	backuplinkflows.put(slink, flow);
    }
    
      
    public void setLinkFlows(LinkedHashMap<SubstrateLink, Double> flow){
    	
    	linkflows = flow;
    }
    
    
    public void setBackUPLinkFlows(LinkedHashMap<SubstrateLink, Double> flow){
    	
    	backuplinkflows = flow;
    }
    
    
    /**
     * sets the set of substrate links mapping this link
     * @param mappingsubstratelinks
     */
    public void setMappingSubstrateLinks(ArrayList<SubstrateLink> mapping){
    	
    	this.mappingsubstratelinks = mapping;
    	
    }
    
    
    public void setBackUPSubstrateLinks(ArrayList<SubstrateLink> mapping){
    	
    	this.backupsubstratelinks = mapping;
    	
    }
    
    /**
     * adds a single substrate link to the set of mappingsubstratelinks
     * @param mappingsubstratelinks
     */
    public void addMappingSubstrateLink(SubstrateLink mapping){
    	
    	synchronized(mappingsubstratelinks){
    	
    	this.mappingsubstratelinks.add(mapping);
    	
    	}
    }
    
    public void addBackUPSubstrateLink(SubstrateLink mapping){
    	
    	synchronized(backupsubstratelinks){
    	
    	this.backupsubstratelinks.add(mapping);
    	
    	}
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


	public void addSubstrateNetwork(SubstrateNetwork sub) {
		
		if(!containsNetwork(mappingsubstratenetworks, sub)){

		mappingsubstratenetworks.add(sub);
		
		}
		
	}
	
	
	public void addBackUPSubstrateNetwork(SubstrateNetwork sub) {
		
		if(!containsNetwork(backupsubstratenetworks, sub)){

		backupsubstratenetworks.add(sub);
		
		}
		
	}
	
	
	public void setSubstrateNetworks(ArrayList<SubstrateNetwork> subs) {
		
		mappingsubstratenetworks = subs;
		
	}
	
	
	public void setBackUPSubstrateNetworks(ArrayList<SubstrateNetwork> subs) {
		
		backupsubstratenetworks = subs;
		
	}
	
	
	private boolean containsNetwork( ArrayList<SubstrateNetwork> list, SubstrateNetwork sub) {

		boolean contains = false;
		
		for(SubstrateNetwork sn: list){
			
			if(sn.getNetworkID().equalsIgnoreCase(sub.getNetworkID())){
				
				contains = true;
			}
		}
				
		return contains;
	}


	public ArrayList<SubstrateNetwork>  getMappingSubstrateNetworks() {

		return mappingsubstratenetworks;
		
	}
	
	
	public ArrayList<SubstrateNetwork>  getBackUPSubstrateNetworks() {

		return backupsubstratenetworks;
		
	}
	

	public VirtualLink copyLink() {
		
		ArrayList<VirtualNode> copiednodes = new ArrayList<VirtualNode>();
		
		VirtualNode vn1 = this.getStartNode().copyNode();
		
		VirtualNode vn2 = this.getEndNode().copyNode();
		
		copiednodes.add(vn1);  copiednodes.add(vn2);

		VirtualLink vlk = new VirtualLink();
		
		vlk.setID(this.getID());
		
		vlk.setLinkLength(this.getLinkLength());
		
		vlk.setLinkDelay(this.getLinkDelay());
		
		vlk.setQoS(this.getQoS());
		
		vlk.setTotalBandWidth(this.getTotalBandWidth());
		
		vlk.setAvailableBandWidth(this.getAvailableBandwidth());
		
		vlk.setMappingSubstrateLinks(copyLinks(this.getMappingSubstrateLinks()));
		
		vlk.setBackUPSubstrateLinks(copyLinks(this.getBackUPSubstrateLinks()));
		
		vlk.setLinkFlows((LinkedHashMap<SubstrateLink, Double>) copyLinkFlows(this.getLinkFlows()));
		
		vlk.setBackUPLinkFlows((LinkedHashMap<SubstrateLink, Double>) copyLinkFlows(this.getBackUPLinkFlows()));
		
		vlk.setSubstrateNetworks(new ArrayList<SubstrateNetwork>());
		
		vlk.getMappingSubstrateNetworks().addAll(this.getMappingSubstrateNetworks());
		
		vlk.setBackUPSubstrateNetworks(new ArrayList<SubstrateNetwork>());
		
		vlk.getBackUPSubstrateNetworks().addAll(this.getBackUPSubstrateNetworks());
					
		for(VirtualNode nd: copiednodes){
			
			if(nd.getID().equalsIgnoreCase(this.getStartNode().getID())){
				
				vlk.setStartNode(nd);
				
				
				nd.addAdjacentVirtualLink(vlk);
			}
			

			if(nd.getID().equalsIgnoreCase(this.getEndNode().getID())){
				
				vlk.setEndNode(nd);
				
				nd.addAdjacentVirtualLink(vlk);
			}
		}
		
			
			vlk.getStartNode().addAdjacentVirtualNode(vlk.getEndNode());

			
			vlk.getEndNode().addAdjacentVirtualNode(vlk.getStartNode());
		

		return vlk;
	}
	
	
	
	
	
	
	public VirtualLink copyLink(ArrayList<VirtualNode> copiednodes) {

		VirtualLink vlk = new VirtualLink();
		
		vlk.setID(this.getID());
		
		vlk.setLinkLength(this.getLinkLength());
		
		vlk.setLinkDelay(this.getLinkDelay());
		
		vlk.setTotalBandWidth(this.getTotalBandWidth());
		
		vlk.setAvailableBandWidth(this.getAvailableBandwidth());
		
		vlk.setMappingSubstrateLinks(copyLinks(this.getMappingSubstrateLinks()));
		
		vlk.setLinkFlows((LinkedHashMap<SubstrateLink, Double>) copyLinkFlows(this.getLinkFlows()));
		
		vlk.setSubstrateNetworks(new ArrayList<SubstrateNetwork>());
		
		vlk.getMappingSubstrateNetworks().addAll(this.getMappingSubstrateNetworks());
		
		
					
		for(VirtualNode nd: copiednodes){
			
			if(nd.getID().equalsIgnoreCase(this.getStartNode().getID())){
				
				vlk.setStartNode(nd);
				
				
				nd.addAdjacentVirtualLink(vlk);
			}
			

			if(nd.getID().equalsIgnoreCase(this.getEndNode().getID())){
				
				vlk.setEndNode(nd);
				
				nd.addAdjacentVirtualLink(vlk);
			}
		}
		
			
			vlk.getStartNode().addAdjacentVirtualNode(vlk.getEndNode());

			
			vlk.getEndNode().addAdjacentVirtualNode(vlk.getStartNode());
		

		return vlk;
	}


	private ArrayList<SubstrateLink> copyLinks( ArrayList<SubstrateLink> lks) {
		
		ArrayList<SubstrateLink> links = new ArrayList<SubstrateLink>();
		
		for(SubstrateLink lk: lks)
			
			links.add(lk);
		
				return links;
			}



	private Map<SubstrateLink, Double> copyLinkFlows(Map<SubstrateLink, Double> map) {

		Map<SubstrateLink, Double> newmap = new LinkedHashMap<SubstrateLink, Double>();
		
		synchronized(map){

			for(Iterator<SubstrateLink> link = map.keySet().iterator(); link.hasNext();){
				
				SubstrateLink lk = link.next();
				
				Double val = map.get(lk);
				
				newmap.put(lk, val);	
			}
		
		}
			return newmap;
	}
	
	public int getQoS(){
		
		return qos;
	}
	
	public double getIncome(){
		
		return income;
	}
	
	public double getPenalty(){
		
		return penalty;
	}
	
	public void setQoS(int val){
		
		qos = val;
	}
	
	public void setReservePrice(double px){
		
		reserveprice = px;
	}
	
	public void setPreviousPrice(double px){
		
		previousprice = px;
	}
	
	public double getReservePrice(){
		
		return reserveprice;
	}
	
	public double getPreviousPrice(){
		
		return previousprice;
	}


	public void setBackUPMappings(VirtualLink lk, VirtualLink vlk) {
		
		lk.backedup = true;
		
		for(SubstrateLink slk: vlk.getBackUPSubstrateLinks()){
			
			lk.addBackUPLinkFlow(slk, vlk.getBackUPLinkFlow(slk));
			
			lk.addBackUPSubstrateLink(slk);
					
			lk.setBackUPSubstrateNetworks(vlk.getBackUPSubstrateNetworks());
			
			slk.addBackedUPVirtualLink(lk);
			
			slk.addBackedUPVirtualLinkFlow(lk, vlk.getBackUPLinkFlow(slk));
			
			slk.setAvailableBandWidth(slk.getAvailableBandwidth() - vlk.getBackUPLinkFlow(slk));
			
		//	addEmbeddingSN(vnet, slk);
			
		}
		
			
		
/*			
			lk.backupmappingstarttime = System.currentTimeMillis()/1000;
			
			lk.mappingendtime = System.currentTimeMillis()/1000;
			
  			for(SubstrateLink slk: lk.getMappingSubstrateLinks()){
				
				double flow = lk.getLinkFlow(slk);
				
				slk.getVirtualLinkFlows().remove(lk);
				
				slk.getMappedVirtualLinks().remove(lk);
				
				slk.setAvailableBandWidth(slk.getAvailableBandwidth()+flow);
										
			}*/
		
	}
	
/*	public SubstrateNetwork  getLastMappingSubstrateNetwork() {
		
		int size = mappingsubstratenetworks.size();

		return mappingsubstratenetworks.get(size-1);
		
	}*/
      
}
