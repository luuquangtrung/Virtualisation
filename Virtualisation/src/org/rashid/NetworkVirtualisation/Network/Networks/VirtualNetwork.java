/* Copyright (C) 2014, Rashid MIJUMBI
 * 
 * This work has been done by Rashid Mijumbi, Joan Serrat and Juan-Luis Gorricho of the 
 * Universitat Politecnica de Catalunya (UPC), Barcelona, Spain.
 * 
 * The work has been carried out as part of the objectives of the PhD, "Multi-Agent based
 * Reinforcement Learning for Dynamic Resource Allocation in Next Generation Virtual Networks" 
 * in the department Signal Theory and Communications of the UPC.
 * 
 * This is free software; you can redistribute it and/or modify it under the terms 
 * of the GNU General Public License Version 3 or later (the "GPL"), or the GNU Lesser 
 * General Public License Version 3 or later(the "LGPL") as published by the Free 
 * Software Foundation. It is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE.
 * 
 * Please See the GNU General Public License or the GNU Lesser General Public License at 
 * <http://www.gnu.org/licenses/> for more details.
 */


package org.rashid.NetworkVirtualisation.Network.Networks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Network.Nodes.VirtualNode;
import org.rashid.NetworkVirtualisation.Utilities.BriteTopology;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

import Brite.Export.BriteExport;
import Brite.Model.BriteModel;
import Brite.Model.RouterWaxman;

/**
 * 
 * This Class defines the Virtual Network
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */

public class VirtualNetwork implements Cloneable, java.io.Serializable, VirtualisationVocabulary{
	
	
	public VirtualNetwork(){
		
    	int var_x = varianceLower + generator.nextInt(varianceUpper - varianceLower);
    	
    	int var_y = varianceLower + generator.nextInt(varianceUpper - varianceLower);
    	
    	this.setXVariance(var_x);
    	
    	this.setYVariance(var_y);
    	
    	int mean_x = var_x + generator.nextInt(totalgrid - 2*var_x);
    	
    	int mean_y = var_y + generator.nextInt(totalgrid - 2*var_y);
    	
    	this.setXMean(mean_x);
    	
    	this.setYMean(mean_y);
    	
    	//int numInP = numInPs + generator.nextInt(maxInPs - numInPs);
    	
    	this.setMaxInPCount(numInPs);
    	
    	double budget = minBudget + generator.nextDouble()*(maxBudget - minBudget);
    	
    	this.setBudget(budget);
    	
    	double proc = minProcessingRatio + generator.nextDouble()*(maxProcessingRatio - minProcessingRatio);
    	
    	this.setProcessing(proc);
    	
	}
	
	public synchronized void printNetwork(){
		
		for(VirtualNode nd: nodes){
			
			SubstrateNode sn = nd.getSubstrateNode();
						
			System.out.println("VirtualNode: "+nd.getID()+" has CPU: "+nd.getTotalCPU()+" has XLOC: "+nd.getX_Loc()+"  has YLOC: "+nd.getY_Loc() );
			
			if(sn != null)
				
			System.out.println("\thas been mapped onto SubstrateNode: "+sn.getID()+" located in: "+sn.getCity()+" with TotalCPU: "+sn.getTotalCPU()+" and AvailableCPU: "+sn.getAvailableCPU()+"  with XLOC:  "+sn.getX_Loc()+"  with YLOC:  "+sn.getY_Loc());
		
			System.out.print("Has "+nd.getPossibleSubstrateNodes().size()+" possible substrate nodes:\t");
			
			for(SubstrateNode snd: nd.getPossibleSubstrateNodes()){
				
				System.out.print(snd.getID()+" with XLOC: "+snd.getX_Loc()+"  with YLOC:  "+snd.getY_Loc()+"  with capac.  "+snd.getAvailableCPU());
			}
		
			System.out.println();
		
		}
		

		
		

		for(VirtualLink lk: links){
			
			System.out.println("VirtualLink: "+lk.getID()+" has BW: "+lk.getTotalBandWidth());
			
			for(SubstrateLink sl: lk.getMappingSubstrateLinks()){
				
				System.out.println("\thas flows on SubstrateLink: "+sl.getID()+" with total BW: "+sl.getTotalBandWidth()+" available BW: "+sl.getAvailableBandwidth()+" and the flow is: "+lk.getLinkFlow(sl));
			}
		}

	}
	
	
/*	public synchronized void saveNetwork(FileStream file, String id) throws IOException{
		
		file.addText("The Virtual Network is: "+this.getNetworkID()+"  and the identifier is:  "+id);
		
		synchronized(nodes){
		
		for(VirtualNode nd: nodes){
			
			SubstrateNode sn = nd.getSubstrateNode();
						
			file.addText("VirtualNode: "+nd.getID()+" has CPU: "+nd.getTotalCPU()+" is located in City: "+nd.getCity());
			
			if(sn != null)
				
				file.addText("\thas been mapped onto SubstrateNode: "+sn.getID()+" located in: "+sn.getCity()+" with TotalCPU: "+sn.getTotalCPU()+" and AvailableCPU: "+sn.getAvailableCPU());
		}
		
		}
		
		
		synchronized(links){
		for(VirtualLink lk: links){
			
			file.addText("VirtualLink: "+lk.getID()+" has BW: "+lk.getTotalBandWidth());
			
			for(SubstrateLink sl: lk.getMappingSubstrateLinks()){
				
				file.addText("\thas flows on SubstrateLink: "+sl.getID()+"  with startNode: "+sl.getStartNode().getID()+"  endNode: "+sl.getEndNode().getID()+" with total BW: "+sl.getTotalBandWidth()+" available BW: "+sl.getAvailableBandwidth()+" and the flow is: "+lk.getLinkFlow(sl));
			}
		}
		
		}
	}*/
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1392013755261822599L;

	/**
	 * List of Virtual Nodes that belong to this network
	 */

	ArrayList<VirtualNode> nodes = new ArrayList<VirtualNode>();
	
	/**
	 * List of Virtual Nodes that belong to this network that have been mapped
	 */

	ArrayList<VirtualNode> mappednodes = new ArrayList<VirtualNode>();
	
	/**
	 * List of Virtual Nodes that belong to this network that have not yet been mapped
	 */

	ArrayList<VirtualNode> unmappednodes = new ArrayList<VirtualNode>();
	
	/**
	 * List of Virtual Links that belong to this Network
	 */
    
    ArrayList<VirtualLink> links = new ArrayList<VirtualLink>();
    
    /**
     * List of those links for which atleast one of its ends has already been mapped, but the link is not yet mapped
     */
    
    ArrayList<VirtualLink> pendinginterdomain = new ArrayList<VirtualLink>();
    
    /**
     * Unique ID for this network
     */
    
    String networkid = "";
    
    /**
     * List of all substrate networks that are mapping this Virtual network
     */
    
    ArrayList<SubstrateNetwork> mappingsubstratenetworks = new ArrayList<SubstrateNetwork>();
    
    /**
     * The x coordinate of mean location of nodes
     * 
     */
    int mean_x = 16;
    
    /**
     * The y coordinate of mean location of nodes
     * 
     */
    int mean_y = 16;
    
    /**
     * The variance on the x location of nodes
     */
    int var_x = 1;
    
    /**
	 *The variance on the x location of nodes
     */
    int var_y = 1; 
    
    
    double budget, processing;
    
    int maxInPcount;
    
    
    public void setMaxInPCount(int count){
    	
    	maxInPcount = count;
    }
    
    public int getMaxInPCount(){
    	
    	return maxInPcount;
    }
    
    
    public void setBudget(double bud){
    	
    	budget = bud;
    }
    
    public void setProcessing(double pro){
    	
    	processing = pro;
    }
    
    public double getBudget(){
    	
    	return budget;
    }
    
    public double getProcessing(){
    	
    	return processing;
    }
 
    
    
    /**
     * sets x value of mean location of network nodes
     * @param mean
     */
    public void setXMean(int mean){
    	
    	this.mean_x = mean;
    	
    }
    
    /**
     * sets y value of mean location of network nodes
     * @param mean
     */
    public void setYMean(int mean){
    	
    	this.mean_y = mean;
    	
    }
    
    /**
     * sets variance of x value on node locations
     * @param xvar
     */
    public void setXVariance(int xvar){
    	
    	this.var_x = xvar;
    	
    }
    
    /**
     * sets variance of y value on node locations
     * @param yvar
     */
    public void setYVariance(int yvar){
    	
    	this.var_y = yvar;
    	
    }
    


    
    /**
     * returns the x value of mean location of nodes
     * @return
     */
    public int getXMean(){
    	
    	return mean_x;
    	
    }
    
    /**
     * returns the y value of mean location of nodes
     * @return
     */
    public int getYMean(){
    	
    	return mean_y;
    	
    }
    
    /**
     * returns variance on x location of network nodes
     * @return
     */
    public int getXVariance(){
    	
    	return var_x;
    	
    }
    
    /**
     * returns variance on y location of network nodes
     * @return
     */
    public int getYVariance(){
    	
    	return var_y;
    	
    }
    
    
    public void addPendingInterdomainLink(VirtualLink link){
    	
    	pendinginterdomain.add(link);
    }
    
    public void addPendingInterdomainLink(ArrayList<VirtualLink> links){
    	
    	pendinginterdomain.addAll(links);
    }
    
    public void setPendingInterdomainLinks(ArrayList<VirtualLink> links){
    	
    	pendinginterdomain = links;
    }
    
    
    public VirtualLink getPendingInterdomainLink(String name){
    	
    	VirtualLink link = null;
    	
    	for(VirtualLink lk: pendinginterdomain){
    		
    		if(lk.getID().equalsIgnoreCase(name)){
    			
    			return lk;
    		}
    		
    	}
    	
    	System.out.println("The Link with name: "+name+"  cannot be found . . . . .");
    	
    	return link;
    }
    
    
    
    public void removeFromPendingLinks(String name){
    	
    	VirtualLink lk = getPendingInterdomainLink(name);
    	
    	pendinginterdomain.remove(lk);
    }
    
    public ArrayList<VirtualLink> getPendingInterdomainLinks(){
    	
    	return pendinginterdomain;
    }
    
    
    /**
     * returns list of nodes 
     * @return
     */
    public ArrayList<VirtualNode> getVirtualNodes(){
    	
    	return nodes;
    	
    }
    
    
    /**
     * returns list of nodes  mapped nodes
     * @return
     */
    public ArrayList<VirtualNode> getMappedVirtualNodes(){
    	
    	return mappednodes;
    	
    }
    
    /**
     * returns list of un mapped nodes 
     * @return
     */
    public ArrayList<VirtualNode> getUnMappedVirtualNodes(){
    	
    	return unmappednodes;
    	
    }
    
    /**
     * returns set of substrate networks currently mapping this virtual network
     * @return
     */
    
    public ArrayList<SubstrateNetwork> getSubstrateNetworks(){
    	
    	return mappingsubstratenetworks;
    }
    
    /**
     * returns list of links
     * @return
     */
    public ArrayList<VirtualLink> getVirtualLinks(){
    	
    	return links;
    	
    }
    
    /**
     * returns unique network ID
     * @return
     */
    public String getNetworkID(){
    	
    	return networkid;
    	
    }
    
    /**
     * sets the list of nodes
     * @param nodes
     */
    
    public void setVirtualNodes(ArrayList<VirtualNode> nodes){
    	
    	this.nodes = nodes;
    	
    }
    
    
    /**
     * sets the list of mapped nodes
     * @param nodes
     */
    
    public void setMappedVirtualNodes(ArrayList<VirtualNode> nodes){
    	
    	this.mappednodes = nodes;
    	
    }
    
    /**
     * sets the list of un mapped nodes
     * @param nodes
     */
    
    public void setUnMappedVirtualNodes(ArrayList<VirtualNode> nodes){
    	
    	this.unmappednodes = nodes;
    	
    }
    
    
    /**
     * sets list of substrate networks
     * @param nets
     */  
    public void setMappedVirtualNetworks(ArrayList<SubstrateNetwork> nets){
    	
    	mappingsubstratenetworks = nets;
    }
    
    /**
     * adds net to list of substrate networks currently mapping virtual network
     * @param net
     */  
    public void addMappingSubstrateNetwork(SubstrateNetwork net){
    	
    	mappingsubstratenetworks.add(net);
    }
    
    
    /**
     * removes net from list of substrate networks
     * @param net
     */  
    public void removeMappingSubstrateNetwork(SubstrateNetwork net){
    	
    	mappingsubstratenetworks.remove(net);
    }
    
    /**
     * adds a node to the list of nodes
     * @param node
     */
    
    public void addVirtualNode(VirtualNode node){
    	
    	this.nodes.add(node);
    	
    }
    
    /**
     * adds a node to the list of mapped nodes
     * @param node
     */
    
    public void addMappedVirtualNode(VirtualNode node){
    	
    	this.mappednodes.add(node);
    	
    }
    
    /**
     * adds a node to the list of un mapped nodes
     * @param node
     */
    
    public void addUnMappedVirtualNode(VirtualNode node){
    	
    	this.unmappednodes.add(node);
    	
    }
    
    
    /**
     * sets list of links
     * @param links
     */
    public void setVirtualLinks(ArrayList<VirtualLink> links){
    	
    	this.links = links;
    	
    }
    
    /**
     * add a link to the list of links
     * @param links
     */
    public void addVirtualLink(VirtualLink link){
    	
    	this.links.add(link);
    	
    }
    
    
    
   /**
    * sets network ID 
    * @param networkid
    */
    public void setNetworkID(String networkid){
    	
    	this.networkid = networkid;
    	
    }
    
    
	public void createBriteTopology (int N, String name){
	
		
		float bwMin = (float) VirLinkLowerBandWidth;
		
		float bwMax = (float) (VirLinkUpperBandWidth - VirLinkLowerBandWidth);
		
		BriteModel m = null;
		
		int HS = 500;	//1000 allows for a bigger grid for many nodes in network grid;
		
		int LS = 500;
	    
	    m = new RouterWaxman(N, HS, LS, NodePlacement, NumberNodeNeighbours, alpha, beta, growthtype, bwDistribution, bwMin, bwMax);
	    	
	    BriteTopology top = new BriteTopology();
	    
	    BriteExport topology = top.createTopology(m);
		
		topology.getVirtualNetwork(name, this);
		
		initiliselatestMapping();
		
	    System.out.println("Virtual Network "+this.networkid+" with "+nodes.size()+" nodes and "+links.size()+" links has been created");
	    
}
    
  
	private void initiliselatestMapping() {
		
		
		for(VirtualLink lk: this.getVirtualLinks()){
			
			VirtualNode vn1 = lk.getStartNode();
			
			SubstrateNetwork sn1 = null;
			
			SubstrateNode nd1 = null;
			
			vn1.setLatestMappingSubstrateNetwork(lk, sn1);
			
			vn1.setLatestMappingSubstrateNode(lk, nd1);
			
			VirtualNode vn2 = lk.getEndNode();
			
			SubstrateNetwork sn2 = null;
			
			SubstrateNode nd2 = null;
			
			vn2.setLatestMappingSubstrateNetwork(lk, sn2);
			
			vn2.setLatestMappingSubstrateNode(lk, nd2);
		}
			
	}

/*	*//**
	 * Creates a visual representation of the Network
	 *//*
	   public void createPlot() {
		   
	        final NetworkPlot plot = new NetworkPlot(this);
	        
	        plot.pack();
	        
	        RefineryUtilities.centerFrameOnScreen(plot);
	        
	        plot.setVisible(true);
   }*/
	   
	   
	   
		public VirtualNetwork copyVirtualNetwork(){
				
			VirtualNetwork vn = new VirtualNetwork();
			
			vn.setNetworkID(this.getNetworkID());
			
			ArrayList<VirtualLink> copiedlinks = new ArrayList<VirtualLink>();
			
			ArrayList<VirtualNode> copiednodes = new ArrayList<VirtualNode>();
			
			vn.setVirtualNodes(copiednodes);
			
			vn.setVirtualLinks(copiedlinks);
			
			for(VirtualNode nd: nodes){
				
					VirtualNode vnd = nd.copyNode();
				
				copiednodes.add(vnd);
				
			}
			
			synchronized(links){
				
			for(VirtualLink lk: links){
				
				VirtualLink vlk = lk.copyLink(copiednodes);
					
				copiedlinks.add(vlk);
			}
			
		}
					
			return vn;
			
		}
		



/*		*//**
		 * Makes a copy of this virtual network, but uses the originalVN to get the original set of nodes, in case the VN has been changed due 
		 * to sequential mapping.
		 * @param originalVN
		 * @return
		 *//*
		
		public VirtualNetwork copyVirtualNetwork(VirtualNetwork originalVN){
			
	
			VirtualNetwork vn = new VirtualNetwork();
			
			vn.setNetworkID(this.getNetworkID());
			
			ArrayList<VirtualLink> copiedlinks = new ArrayList<VirtualLink>();
			
			ArrayList<VirtualNode> copiednodes = new ArrayList<VirtualNode>();
			
		//	vn.setNetworkID(this.networkid);
			
			vn.setVirtualNodes(copiednodes);
			
			vn.setVirtualLinks(copiedlinks);
			
			for(VirtualNode nd: nodes){
				
				VirtualNode vnd = new VirtualNode();
				
				vnd.setAvailableCPU(nd.getAvailableCPU());
				
				vnd.setID(nd.getID());
				
				vnd.setCity(nd.getCity());
				
				vnd.setSubstrateNode(nd.getSubstrateNode());
				
				vnd.setTotalCPU(nd.getTotalCPU());
				
				vnd.setX_Loc(nd.getX_Loc());
				
				vnd.setY_Loc(nd.getY_Loc());
				
				copiednodes.add(vnd);
				
			}
			
			synchronized(links){
				
			for(VirtualLink lk: links){
				
				VirtualLink vlk = new VirtualLink();
				
				vlk.setID(lk.getID());
				
				vlk.setLinkLength(lk.getLinkLength());
				
				vlk.setLinkDelay(lk.getLinkDelay());
				
				vlk.setTotalBandWidth(lk.getTotalBandWidth());
				
				vlk.setAvailableBandWidth(lk.getAvailableBandwidth());
				
				vlk.setMappingSubstrateLinks(lk.getMappingSubstrateLinks());
				
									
				for(VirtualNode nd: copiednodes){
					
				//	if(lk.getStartNode()!=null)
					if(nd.getID().equalsIgnoreCase(lk.getStartNode().getID())){
						
						vlk.setStartNode(nd);
						
						nd.addAdjacentVirtualLink(vlk);
					}
					
				//	if(lk.getEndNode()!=null)
					if(nd.getID().equalsIgnoreCase(lk.getEndNode().getID())){
						
						vlk.setEndNode(nd);
						
						nd.addAdjacentVirtualLink(vlk);
					}
				}
				
				if(vlk.getStartNode()==null || vlk.getEndNode()==null)
				for(VirtualNode nd: originalVN.getVirtualNodes()){
					
					if(vlk.getStartNode()==null)
					if(nd.getID().equalsIgnoreCase(lk.getStartNode().getID())){
						
						vlk.setStartNode(nd);
						
						nd.addAdjacentVirtualLink(vlk);
					}
					
					if(vlk.getEndNode()==null)
					if(nd.getID().equalsIgnoreCase(lk.getEndNode().getID())){
						
						vlk.setEndNode(nd);
						
						nd.addAdjacentVirtualLink(vlk);
					}
				}
				
				
				if(vlk.getStartNode()==null || vlk.getEndNode()==null)  {
					
					System.out.println("Virtual Link Without end Node!!! This MUST never happed!!!! . . . . VirtualNetwork");
					
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				System.out.println("Start Node is: "+vlk.getStartNode().getID());
				System.out.println("Adj. Size : "+vlk.getStartNode().getAdjacentVirtualNodes().size());
				System.out.println("End Node is: "+vlk.getEndNode().getID());
				System.out.println("Adj. Size : "+vlk.getEndNode().getAdjacentVirtualNodes().size());
				
				vlk.getStartNode().addAdjacentVirtualNode(vlk.getEndNode());
				
				vlk.getEndNode().addAdjacentVirtualNode(vlk.getStartNode());
				
				copiedlinks.add(vlk);
			}
			
		}
			
					
			return vn;
			
		}*/
		
/*		public boolean containsBadLink(){
			
			boolean result = false;
			
			for(VirtualLink vl: links){
				
				VirtualNode vn1 = vl.getStartNode();
				
				VirtualNode vn2 = vl.getEndNode();
				
				if(vn1 ==null || vn2 == null){
					
					result = true;
					
					return result;
				}
			}
			
			return result;
		}*/
		
		
		public ArrayList<VirtualNetwork> copyVirtualNetwork(int Num){
			
			ArrayList<VirtualNetwork> list = new ArrayList<VirtualNetwork>();
			
			for(int i = 0; i<Num; i++){
				
				list.add(copyVirtualNetwork());
				
			}
		
			return list;
		}
    
    
    /**
     * creates independent copy of this network
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
    
   

	public void checkLatestMapping() {
		
		for(VirtualNode nd: this.nodes){
			
			System.out.println(nd.getID()+" . .  . "+nd.getLatestMappingSubstrateNetwork());
		}
		
		try {
			Thread.sleep(9000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
     
}