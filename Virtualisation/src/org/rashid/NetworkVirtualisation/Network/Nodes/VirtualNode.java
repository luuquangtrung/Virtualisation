package org.rashid.NetworkVirtualisation.Network.Nodes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;


/**
 * 
 * This Class is a definition of a VirtualNode
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */


public class VirtualNode implements Cloneable, java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8115761095933051028L;

	/**
	 * X Location of Virtual Node
	 */

	Double x_loc = 0.0;
	
	/**
	 * Y Location of Virtual Node
	 */
	
	Double y_loc = 0.0; 
	
    /**
     * The city, chosen from 256 major cities in which the node is located
     */
    int city = 0;
	
	/**
	 * Maximum deviation that the X Location could be changed during the embedding phase
	 */
	
	Double x_dev = 0.0; 
	
	/**
	 * Maximum deviation that the Y Location could be changed during the embedding phase
	 */
	
	Double y_dev = 0.0; 
	
	/**
	 * Total Required CPU capacity of Virtual Node
	 */
	
	Double totalcpu = 0.0; 
	
	/**
	 * Total available CPU capacity of Virtual Node. Difference between TotalCPU and what is currently used by, say, User Traffic
	 */
	
	Double availablecpu = 0.0;
	
	/**
	 * Unique Node ID
	 */
	
	String id = ""; 
	
	/**
	 * All other virtual nodes in the same Virtual Network that are directly connected to the to the Virtual Node
	 */
    
    ArrayList<VirtualNode> adjacentvirtualnodes = new ArrayList<VirtualNode>();
    
	/**
	 * All other virtual links in the same Virtual Network that are directly connected to the to the Virtual Node
	 */
    
    ArrayList<VirtualLink> adjacentvirtuallinks = new ArrayList<VirtualLink>();
    
	/**
	 * All other substrate nodes in a given substrate network that have enough CPU capacity, and are located within the maximum deviations x_dev and y_dev
	 */
    
    ArrayList<SubstrateNode> possiblesubstratenodes = new ArrayList<SubstrateNode>();	
    
    /**
     * M. Chowdhury, M. Rahman and R. Boutaba, “ViNEYard: Virtual Network Embedding Algorithms With Coordinated Node and Link
	 * Mapping”, IEEE/ACM transactions on networking, VOL. 20, NO. 1, 2012.
     */
    
    ArrayList<SubstrateLink> adjacentmetalinks = new ArrayList<SubstrateLink>();
    
    /**
     * Substrate Node onto which virtual Node is Mapped
     */
    SubstrateNode substratenode;
    
    /**
     * Mapping of substrate network hat made the latest mapping of a given virtual link attached to this node. used to track interdomain link mapping
     */
    Map<String, SubstrateNetwork> latestmappingsubstratenetwork = new LinkedHashMap<String, SubstrateNetwork>();
    
    

    /**
     * Mapping of substrate node that made the latest mapping of a given virtual link attached to this node. used to track interdomain link mapping
     */
    Map<String, SubstrateNode> latestmappingsubstratenode = new LinkedHashMap<String, SubstrateNode>();
    
    
    
    /**
     * Returns the required X Location of the virtual node
     * @return
     */
    public Double getX_Loc(){
    	
    	return x_loc;
    	
    }
    	
    /**
     * Returns the required Y Location of the virtual Node
     * @return
     */
    public Double getY_Loc(){
    	
    	return y_loc;
    	
    }   
    
/*    *//**
     * Returns the maximum allowed deviation on the X position value
     * @return
     */
    public Double getX_Dev(){
    	
    	return x_dev;
    	
    }  
    
/*    *//**
     * Returns the maximum allowed deviation on the Y position value
     * @return
     */
    public Double getY_Dev(){
    	
    	return y_dev;
    	
    } 
    
    /**
     * Returns the Total required CPU for the Virtual Node
     * @return
     */
    public Double getTotalCPU(){
    	
    	return totalcpu;
    	
    }  
    
    /**
     * Returns the available CPU capacity after deducting, say, what is currently used by user traffic
     * @return
     */
 
    public Double getAvailableCPU(){
    	
    	return availablecpu;
    	
    } 
    
    /**
     * Returns the Unique ID for the Node
     * @return
     */
    public String getID(){
    	
    	return id;
    	
    }   
    
    /**
     * Returns the the List of all virtual nodes that are directly connected to the node
     * @return
     */
    
    public ArrayList<VirtualNode> getAdjacentVirtualNodes(){
    	
    	return adjacentvirtualnodes;
    	
    }
    
    /**
     * Returns the Substrate Network that made the latest mapping associated to the link in question
     * @return
     */
    
    public SubstrateNetwork getLatestMappingSubstrateNetwork(VirtualLink lk){
    	
    	for(Iterator<String> vlink = latestmappingsubstratenetwork.keySet().iterator(); vlink.hasNext();){
    		
    		String link = vlink.next();
    		
    		if(link.equalsIgnoreCase(lk.getID())){
    			
    			return latestmappingsubstratenetwork.get(link);
    		}
    		
    	}
    	
    	System.err.println(this.getID()+"  The Latest Mapping Substrate Network CANNOT be found . . . . . . .in  VirtualNode .  The VLink is:"+lk.getID());
    	
    	System.out.println(this.latestmappingsubstratenetwork);
    	
    	for(VirtualLink lik: this.getAdjacentVirtualLinks()){
    		
    		System.out.println(lik.getID());
    	}
    	
    	try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    	
    }
    
    
    /**
     * Returns the Substrate Node that made the latest mapping associated to the link in question
     * @return
     */
    
    public SubstrateNode getLatestMappingSubstrateNode(VirtualLink lk){
    	
    	for(Iterator<String> vlink = latestmappingsubstratenode.keySet().iterator(); vlink.hasNext();){
    		
    		String link = vlink.next();
    		
    		if(link.equalsIgnoreCase(lk.getID())){
    			
    			return latestmappingsubstratenode.get(link);
    		}
    		
    	}
    	
    	System.err.println(this.getID()+"  The Latest Mapping Substrate Node CANNOT be found . . . . . . .in  VirtualNode .  The VLink is:"+lk.getID());
    	
    	System.out.println(this.latestmappingsubstratenode);
    	
    	for(VirtualLink lik: this.getAdjacentVirtualLinks()){
    		
    		System.out.println(lik.getID());
    	}
    	
    	try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    	
    }
    
    
    
    
    /**
     * Returns the Substrate Network which made the latest mapping associated with the link attached to the node..
     * @return
     */
    
    public SubstrateNode getSubstrateNode(){
    	
    	return substratenode;
    	
    }
    
    /**
     * Returns the set of substrate nodes onto which the Virtual node could be mapped
     * @return
     */
    public ArrayList<SubstrateNode> getPossibleSubstrateNodes(){
    	
    	return possiblesubstratenodes;
    	
    }
    
    
    /**
     * Returns the list of all virtual links directly connected to this Node
     * @return
     */
    public ArrayList<VirtualLink> getAdjacentVirtualLinks(){
    	
    	return adjacentvirtuallinks;
    	
    }
    
    /**
     * Returns the list of meta links that connect this Node to each of its possible Substrate Node Mappings
     * @return
     */
    
    public ArrayList<SubstrateLink> getAdjacentMetaLinks(){
    	
    	return adjacentmetalinks;
    	
    }
    
    /**
     * returns the city in which the node is located
     * @return
     */
    public int getCity(){
    	
    	return city;
    	
    }
    
    /**
     * Sets the list of virtual nodes adjacent to this node
     * @param adjacent
     */
    public void setAdjacentVirtualNodes(ArrayList<VirtualNode> adjacent){
    	
    	this.adjacentvirtualnodes = adjacent;
    	
    }
    
    /**
     * adds a singlenode to the set of virtual nodes directly connected to this Node
     * @param adjacent
     */
    public void addAdjacentVirtualNode(VirtualNode adjacent){
    	
    	this.adjacentvirtualnodes.add(adjacent);
    	
    }
    
    /**
     * Sets the substrate node to which this Virtual Node has been mapped
     * @param snode
     */
    
    public void setSubstrateNode(SubstrateNode snode){
    	
    	this.substratenode = snode;
    	
    }
   
    /**
     * Sets the list of substrate nodes onto which this virtual node could possibly be mapped
     * @param subnodes
     */
    
    public void setPossibleSubstrateNodes(ArrayList<SubstrateNode> subnodes){
    	
    	this.possiblesubstratenodes = subnodes;
    	
    }
    
    
    /**
     * sets the city in which node is located
     * @param city
     */
    public void setCity(int city){
    	
    	this.city = city;
    	
    }
    
    /**
     * adds a single substrate node to the list of those nodes onto whic this Virtual nodes could be mapped
     * @param subnode
     */
    
    public void addPossibleSubstrateNode(SubstrateNode subnode){
    	
    	this.possiblesubstratenodes.add(subnode);
    	
    }
    
    /**
     * Sets the list of substrate links that connect the virtual node to each of the substrate nodes onto which it could be mapped.
     * @param sublinks
     */
    
    public void setAdjacentMetaLinks(ArrayList<SubstrateLink> sublinks){
    	
    	this.adjacentmetalinks = sublinks;
    	
    }
    
    /**
     * adds a single meta link
     * @param sublink
     */
    public void addAdjacentMetaLink(SubstrateLink sublink){
    	
    	this.adjacentmetalinks.add(sublink);
    	
    }
    
    /**
     * sets the list of virtual links that connect this node to all its direct neighbours
     * @param virlinks
     */
    
    public void setAdjacentVirtualLinks(ArrayList<VirtualLink> virlinks){
    	
    	this.adjacentvirtuallinks = virlinks;
    	
    }
    
    /**
     * adds a single virtual link to the list of those links that directly connect this node to a neighbouring node
     * @param virlink
     */
    
    public void addAdjacentVirtualLink(VirtualLink virlink){
    	
    	this.adjacentvirtuallinks.add(virlink);
    	
    }
    
    /**
     * sets the required X Location of this virtual Node
     * @param x_loc
     */
    
    public void setX_Loc(Double x_loc){
    	
    	this.x_loc = x_loc;
    	
    }
    
    /**
     * Sets the required Y Location of this Virtual node
     * @param y_loc
     */
       
    public void setY_Loc(Double y_loc){
    	
    	this.y_loc = y_loc;
    	
    }
    
/*    *//**
     * sets x_dev
     * @param x_dev
     */
    public void setX_Dev(Double x_dev){
    	
    	this.x_dev = x_dev;
    	
    }
    
    /**
     * sets y_dev
     * @param y_dev
     */
    public void setY_Dev(Double y_dev){
    	
    	this.y_dev = y_dev;
    	
    }
    
    /**
     * sets totalcpu
     * @param cpu
     */
    
    public void setTotalCPU(Double cpu){
    	
    	this.totalcpu = cpu;
    	}
    
    /**
     * sets availablecpu
     * @param availablecpu
     */
    
    public void setAvailableCPU(Double availablecpu){
    	
    	this.availablecpu = availablecpu;
    	}
    
    /**
     * sets id
     * @param id
     */
    
    public void setID(String id){
    	
    	this.id = id;
    	
    }
    
    /**
     * Clones node to produce new independent copy.
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
    

	public void setLatestMappingSubstrateNetwork(Map<String, SubstrateNetwork> mappings) {
		
        this.latestmappingsubstratenetwork = mappings;
	
	}

	

	public void setLatestMappingSubstrateNode(Map<String, SubstrateNode> mappings) {
		
        this.latestmappingsubstratenode = mappings;
	
	}
	
	
	public Map<String, SubstrateNetwork> getLatestMappingSubstrateNetwork() {
		
        return this.latestmappingsubstratenetwork;
	
	}
	
	
public Map<String, SubstrateNode> getLatestMappingSubstrateNode() {
		
        return this.latestmappingsubstratenode;
	
	}
	
	
    /**
     * Sets the SubstrateNetwork sn to be the one that made the latest mapping work with regard to the virtual link vlk which is connected to this node.
     * @param vlk
     * @param sn
     */

	public void setLatestMappingSubstrateNetwork(VirtualLink vlk, SubstrateNetwork sn) {
		
		Boolean found = false;

    	for(Iterator<String> vlink = latestmappingsubstratenetwork.keySet().iterator(); vlink.hasNext();){
    		
    		String link = vlink.next();
    		
    		if(link.equalsIgnoreCase(vlk.getID())){
    			
    			latestmappingsubstratenetwork.put(link, sn);
    			
    			found = true;
    		}
		
	}
    	
    	if(found == false){
    		
    		latestmappingsubstratenetwork.put(vlk.getID(), sn);
    	}
    	
	}
      

/**
 * Sets the SubstrateNode sn to be the one that made the latest mapping work with regard to the virtual link vlk which is connected to this node.
 * @param vlk
 * @param sn
 */

public void setLatestMappingSubstrateNode(VirtualLink vlk, SubstrateNode sn) {
	
	Boolean found = false;

	for(Iterator<String> vlink = latestmappingsubstratenode.keySet().iterator(); vlink.hasNext();){
		
		String link = vlink.next();
		
		if(link.equalsIgnoreCase(vlk.getID())){
			
			latestmappingsubstratenode.put(link, sn);
			
			found = true;
		}
	
}
	
	if(found == false){
		
		latestmappingsubstratenode.put(vlk.getID(), sn);
	}
	
}


public VirtualNode copyNode() {
	
	VirtualNode vnd = new VirtualNode();
	
	vnd.setAvailableCPU(this.getAvailableCPU());
	
	vnd.setID(this.getID());
	
	vnd.setCity(this.getCity());
	
	vnd.setSubstrateNode(this.getSubstrateNode());
	
	vnd.setTotalCPU(this.getTotalCPU());
	
	vnd.setX_Loc(this.getX_Loc());
	
	vnd.setY_Loc(this.getY_Loc());
	
	vnd.setLatestMappingSubstrateNetwork(copyMap(this.getLatestMappingSubstrateNetwork()));
	
	vnd.setLatestMappingSubstrateNode(copyNodeMap(this.getLatestMappingSubstrateNode()));
	
	return vnd;
}


private Map<String, SubstrateNetwork> copyMap(Map<String, SubstrateNetwork> latestmap) {

	Map<String, SubstrateNetwork> newmap = new LinkedHashMap<String, SubstrateNetwork>();
	
	synchronized(latestmap){

		for(Iterator<String> link = latestmap.keySet().iterator(); link.hasNext();){
			
			String lk = link.next();
			
			SubstrateNetwork sn = latestmap.get(lk);
			
			newmap.put(lk, sn);	
		}
	
	}
		return newmap;
}


private Map<String, SubstrateNode> copyNodeMap(Map<String, SubstrateNode> latestmap) {

	Map<String, SubstrateNode> newmap = new LinkedHashMap<String, SubstrateNode>();
	
	synchronized(latestmap){

		for(Iterator<String> link = latestmap.keySet().iterator(); link.hasNext();){
			
			String lk = link.next();
			
			SubstrateNode sn = latestmap.get(lk);
			
			newmap.put(lk, sn);	
		}
	
	}
		return newmap;
}
  
}

