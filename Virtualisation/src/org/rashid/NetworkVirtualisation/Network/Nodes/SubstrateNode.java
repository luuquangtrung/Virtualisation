package org.rashid.NetworkVirtualisation.Network.Nodes;

import java.util.ArrayList;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;



/**
 * 
 * This Class is a definition of a SubstrateNode
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */


public class SubstrateNode implements Cloneable, java.io.Serializable{
	
	
	/**
	 * The per unit cost of using this Node for any Virtual Node Mapping
	 */
	
	Double cost = 0.0;
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -6235268057205994311L;

	/**
	 * X Location of Substrate Node
	 */

	Double x_loc = 0.0;
	
	/**
	 * Y Location of Substrate Node
	 */
	
	Double y_loc = 0.0; 
 
	
	/**
	 * Total CPU capacity of Substrate Node
	 */
	
	Double totalcpu = 0.0; 
	
	/**
	 * Total available CPU capacity of Substrate Node. Difference between TotalCPU and what is currently used by embedded Virtual Networks
	 */
	
	Double availablecpu = 0.0;
	
    /**
     * The city, chosen from 256 major cities in which the node is located
     */
    int city = 0;
	
	/**
	 * Unique Node ID
	 */
	
	String id = ""; 
	
	/**
	 * All virtual nodes mapped onto the substrate node
	 */
	
	ArrayList<VirtualNode> mappedvirtualnodes = new ArrayList<VirtualNode>();
	
	   /**
     * SubstrateNetwork to which the node belongs
     */
        
    SubstrateNetwork substratenetwork;
    
   
	/**
	 * All substrate nodes that are direct neighbhours.
	 */
    
    ArrayList<SubstrateNode> adjacentsubstratenodes = new ArrayList<SubstrateNode>();	
    
    /**
     * All Substrate links that connect this node to its respective neighbours
     */
    
    ArrayList<SubstrateLink> adjacentsubstratelinks = new ArrayList<SubstrateLink>();
    
    
    /**
     * Returns the X Location of the Substrate node
     * @return
     */
    public Double getX_Loc(){
    	
    	return this.x_loc;
    	
    }
    	
    /**
     * Returns the Y Location of the Substrate Node
     * @return
     */
    public Double getY_Loc(){
    	
    	return this.y_loc;
    	
    }   
      
    
    /**
     * Returns the Total CPU for the Substrate Node
     * @return
     */
    public Double getTotalCPU(){
    	
    	return this.totalcpu;
    	
    } 
    
    
    /**
     * returns substrate network to which node is attached.
     */
    public SubstrateNetwork getSubstrateNetwork(){
    	
    	return substratenetwork;
    }
    
    /**
     * returns list of all virtual nodes currently mapped
     * @return
     */
    public ArrayList<VirtualNode> getMappedVirtualNodes(){
    	
    	return this.mappedvirtualnodes;
    }
    
    /**
     * Returns the available CPU capacity
     * @return
     */
 
    public Double getAvailableCPU(){
    	
    	return this.availablecpu;
    	
    } 
    
    /**
     * Returns the Unique ID for the Node
     * @return
     */
    public String getID(){
    	
    	return this.id;
    	
    }   
    
    /**
     * Returns the the List of all Substrate nodes that are directly connected to the node
     * @return
     */
    
    public ArrayList<SubstrateNode> getAdjacentSubstrateNodes(){
    	
    	return this.adjacentsubstratenodes;
    	
    }
    
    
    /**
     * Returns the list of all Substrate links directly connected to this Node
     * @return
     */
    public ArrayList<SubstrateLink> getAdjacentSubstrateLinks(){
    	
    	return this.adjacentsubstratelinks;
    	
    }
    
    
    /**
     * Sets the list of Substrate nodes adjacent to this node
     * @param adjacent
     */
    public void setAdjacentSubstrateNodes(ArrayList<SubstrateNode> adjacent){
    	
    	this.adjacentsubstratenodes = adjacent;
    	
    }
    
    /**
     * adds a singlenode to the set of Substrate nodes directly connected to this Node
     * @param adjacent
     */
    public void addAdjacentSubstrateNode(SubstrateNode adjacent){
    	
    	this.adjacentsubstratenodes.add(adjacent);
    	
    }
    
    /**
     * sets list of mapped nodes
     * @param nodes
     */
    public void setMappedNodes(ArrayList<VirtualNode> nodes){
    	
    	this.mappedvirtualnodes = nodes;
    }
    
    /**
     * sets substrate network to which node is attached.
     * @param sub
     */
    public void setSubstrateNetwork(SubstrateNetwork sub){
    	
    	substratenetwork = sub;
    }
    
    
    /**
     * adds node to list of mapped nodes
     * @param nodes
     */
    public void addMappedNode(VirtualNode node){
    	
    	this.mappedvirtualnodes.add(node);
    }
    
    /**
     * remove node from list of mapped nodes
     * @param nodes
     */
    public void removeMappedNode(VirtualNode node){
    	
    	this.mappedvirtualnodes.remove(node);
    }
 
    
    /**
     * Sets the list of substrate links that connect the Substrate node to each of neighbouring substrate nodes
     * @param sublinks
     */
    
    public void setAdjacentSubstrateLinks(ArrayList<SubstrateLink> sublinks){
    	
    	this.adjacentsubstratelinks = sublinks;
    	
    }
    
    /**
     * adds a single substrate link to adjacentsubstratelinks
     * @param sublink
     */
    public void addAdjacentSubstrateLink(SubstrateLink sublink){
    	
    	this.adjacentsubstratelinks.add(sublink);
    	
    }
    
        
    /**
     * sets the X Location of this Substrate Node
     * @param x_loc
     */
    
    public void setX_Loc(Double x_loc){
    	
    	this.x_loc = x_loc;
    	
    }
    
    /**
     * sets the city in which node is located
     * @param city
     */
    public void setCity(int city){
    	
    	this.city = city;
    	
    }
    
    /**
     * Sets the Y Location of this Substrate node
     * @param y_loc
     */
       
    public void setY_Loc(Double y_loc){
    	
    	this.y_loc = y_loc;
    	
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
    
    
    /**
     * Sets the per unit cost of uning this node
     * @param Cost
     */
    
    public void setNodeCost(Double Cost){
    	
    	cost = Cost;
    }
    
    public double getNodeCost(){
    	
    	return cost;
    }
    
    public void setAvailableCPU(Double cpu){
    	
    	availablecpu = cpu;
    	
    	}
    
    /**
     * sets id
     * @param id
     */
    
    public void setID(String id){
    	
    	this.id = id;
    	
    }
    
    /**
     * returns the city in which the node is located
     * @return
     */
    public int getCity(){
    	
    	return city;
    	
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
      
}
