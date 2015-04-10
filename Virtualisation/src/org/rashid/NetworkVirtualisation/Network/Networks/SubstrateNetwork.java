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

import javax.swing.JFrame;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Utilities.BriteTopology;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

import Brite.Export.BriteExport;
import Brite.Model.BriteModel;
import Brite.Model.RouterWaxman;


/**
 * 
 * This Class defines the Substrate Network
 * 
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */

public class SubstrateNetwork implements Cloneable, java.io.Serializable, VirtualisationVocabulary{
    						
	
	public SubstrateNetwork(){
		
	//	controller = cn;
		
    	int var_x = varianceLower + generator.nextInt(varianceUpper - varianceLower);
    	
    	int var_y = varianceLower + generator.nextInt(varianceUpper - varianceLower);
    	
    	this.setXVariance(var_x);
    	
    	this.setYVariance(var_y);
    	
    	int mean_x = var_x + generator.nextInt(totalgrid - 2*var_x);
    	
    	int mean_y = var_y + generator.nextInt(totalgrid - 2*var_y);
    	
    	this.setXMean(mean_x);
    	
    	this.setYMean(mean_y);
    	
	}
	
	/**
	 * Used to indicate to a substrate network when the mapping of a given VN has already been finished, and so no need for more negotiation attempts
	 */
	public ArrayList<VirtualNetwork> status = new ArrayList<VirtualNetwork>();

	/**
	 * "LAPDatabase" for any given InP as defined in:
	 * 
	 * F. Samuel, M. Chowdhury and R. Boutaba, PolyViNE: policy-based virtual network embedding across multiple domains, Journal of Internet 
	 * Services and Applications, 4:6, 2013.
	 */
	
//	ControllerNetwork controller;
	
	public int nummsgs = 0;
	
	public double backupresratio = 0.0;
	
	/**
	 * In case this SN is a copy, this gives the parent/original network
	 */

	public SubstrateNetwork parentnetwork;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1715677672732740932L;

	/**
	 * List of Substrate Nodes that belong to this network
	 */

	ArrayList<SubstrateNode> nodes = new ArrayList<SubstrateNode>();
	
	/**
	 * SubstrateNetworks that are directly connected to this Network
	 */
	
	ArrayList<SubstrateNetwork> neighbours = new ArrayList<SubstrateNetwork>();
	
	/**
	 * List of Substrate Links that belong to this Network
	 */
    
    ArrayList<SubstrateLink> links = new ArrayList<SubstrateLink>();
    
	/**
	 * List of Substrate Links that belong to this Network that are current offline
	 */
    
    ArrayList<SubstrateLink> failedlinks = new ArrayList<SubstrateLink>();
    
    
    public ArrayList<SubstrateLink> failedlinksqueue = new ArrayList<SubstrateLink>();
    
	/**
	 * List of Interdomain Links which connect this Network to other substrate networks
	 */
    
    ArrayList<SubstrateLink> interdomainlinks = new ArrayList<SubstrateLink>();
    
    public Map<String, SubstrateNode> ingressnode = new LinkedHashMap<String, SubstrateNode>();
    
    public Map<VirtualLink, SubstrateNode> egressnode = new LinkedHashMap<VirtualLink, SubstrateNode>();
    
    /**
     * Unique ID for this network
     */
    
    String networkid = "";
   
    
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
     * The variance on the x location of nodes
     */
    int var_y = 1; 
    
    
    /**
     * List of all virtual networks that are mapped onto this substrate network
     */
    ArrayList<VirtualNetwork> mappedvirtualnetworks;
    
    /**
     * Used to check if a given ControllerNetwork (made up of substratenetworks) is connected
     * Connections DONT need to be direct. But we enforce that a controller network MUST be connected
     */
    boolean connected = false;
    
    /**
     * Mapping of each city and the list of substratenodes in it
     */
    
    Map<Integer, ArrayList<SubstrateNode> > nodeLocations = new LinkedHashMap<Integer, ArrayList<SubstrateNode> >();
    
    
       
    /**
     * returns list of nodes 
     * @return
     */
    public ArrayList<SubstrateNode> getSubstrateNodes(){
    	
    	return nodes;
    	
    }
    
    /**
     * returns substrate node whose ID is name
     * @param name
     * @return
     */
    public SubstrateNode getSubstrateNode(String name){
    	
    	for(SubstrateNode nd: nodes){
    		
    		if(nd.getID().equalsIgnoreCase(name)){
    			
    			return nd;
    		}
    	}
    	
    	return null;
    }
    
    /**
     * returns set of virtual networks currently mapped
     * @return
     */
    
    public ArrayList<VirtualNetwork> getVirtualNetworks(){
    	
    	return mappedvirtualnetworks;
    }
    
    /**
     * returns list of links
     * @return
     */
    public ArrayList<SubstrateLink> getSubstrateLinks(){
    	
    	return links;
    	
    }
    
    
    /**
     * returns list of InterDomainlinks
     * @return
     */
    public ArrayList<SubstrateLink> getInterDomainLinks(){
    	
    	return interdomainlinks;
    	
    }
    
    /**
     * returns list of Direct Neighbours
     * @return
     */
    public ArrayList<SubstrateNetwork> getNeighbours(){
    	
    	return neighbours;
    	
    }
    
    /**
     * returns unique network ID
     * @return
     */
    public String getNetworkID(){
    	
    	return networkid;
    	
    }
    
    /**
     * returns a value to show if network is able to connect to any other network.
     * @return
     */
    
    public boolean checkConnected(){
    	
    	return connected;
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
    
    /**
     * sets the list of nodes
     * @param nodes
     */
    
    public void setSubstrateNodes(ArrayList<SubstrateNode> nodes){
    	
    	this.nodes = nodes;
    	
    }
    
    /**
     * sets connected to con
     * @param con
     */

    public void setConnected(boolean con){
    	
    	connected = con;
    }
    

    

    
    

    
    

    
    
    /**
     * sets list of virtual networks currently mapped
     * @param virtualnets
     */  
    public void setMappedVirtualNetworks(ArrayList<VirtualNetwork> virtualnets){
    	
    	mappedvirtualnetworks = virtualnets;
    }
    
    /**
     * adds virtualnet to list of virtual networks currently mapped
     * @param virtualnets
     */  
    public void addMappedVirtualNetwork(VirtualNetwork virtualnet){
    	
    	mappedvirtualnetworks.add(virtualnet);
    }
    
    
    /**
     * removes virtualnet fromlist of virtual networks currently mapped
     * @param virtualnet
     */  
    public void removeMappedVirtualNetwork(VirtualNetwork virtualnet){
    	
    	mappedvirtualnetworks.remove(virtualnet);
    }
    
    /**
     * adds a node to the list of nodes
     * @param node
     */
    
    public void addSubstrateNode(SubstrateNode node){
    	
    	this.nodes.add(node);
    	
    }
    
    
    /**
     * sets list of links
     * @param links
     */
    public void setSubstrateLinks(ArrayList<SubstrateLink> links){
    	
    	this.links = links;
    	
    }
    
    /**
     * sets list of interdomainlinks
     * @param links
     */
    public void setInterDomainLinks(ArrayList<SubstrateLink> links){
    	
    	this.interdomainlinks = links;
    	
    }
    
    /**
     * sets list of neighbours
     * @param neighbours
     */
    public void setNeighbours(ArrayList<SubstrateNetwork> neg){
    	
    	this.neighbours = neg;
    	
    }
    
    /**
     * add a link to the list of links
     * @param links
     */
    public void addSubstrateLink(SubstrateLink link){
    	
    	this.links.add(link);
    	
    }
    
    
    /**
     * add an inter domain link to the list of interdomainlinks
     * @param links
     */
    public void addInterDomainLink(SubstrateLink link){
    	
    	this.interdomainlinks.add(link);
    	
    }
    
    
    /**
     * add a neighbour to the list of neighbours
     * @param neighbour
     */
    public void addNeighbour(SubstrateNetwork neighbour){
    	
    	this.neighbours.add(neighbour);
    	
    }
    
    
    
   /**
    * sets network ID 
    * @param networkid
    */
    public void setNetworkID(String networkid){
    	
    	this.networkid = networkid;
    	
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
    
    
    public ArrayList<SubstrateNode> getNodesInCity(int city){
    	
    	ArrayList<SubstrateNode> nds = new ArrayList<SubstrateNode>();
    	
    	for(SubstrateNode sn: nodes){
    		
    		if(sn.getCity() == city){
    			
    			nds.add(sn);
    		}
    		  		
    	}
    	
    	return nds;
    }
   
    
	public void createBriteTopology (int N, String name){
			
		float bwMin = (float) SubLinkLowerBandWidth;
		
		float bwMax = (float) (SubLinkUpperBandWidth - SubLinkLowerBandWidth);
		
		BriteModel m = null;
		
		int HS = this.getXVariance()*1000;	//1000 allows for a bigger grid for many nodes in network grid
		
		int LS = this.getYVariance()*1000;
	    
	    m = new RouterWaxman(N, HS, LS, NodePlacement, NumberNodeNeighbours, alpha, beta, growthtype, bwDistribution, bwMin, bwMax);
	    	
	    BriteTopology top = new BriteTopology();
	    
	    BriteExport topology = top.createTopology(m);
		
		topology.getSubstrateNetwork(this, name);
		
		//initialiseLAPDatabase(this);
		
		assignNodesToInterDomainLinks();
		
	    System.out.println("Substrate Network "+this.networkid+" with "+nodes.size()+" nodes,  "+links.size()+" intradomainlinks and "+interdomainlinks.size()+" interdomainlinks has been created");
	    
}
	
	
	/**
	 * Uses a uniform distribution to assign end nodes to inter domain links attached to this network
	 */
	
	private void assignNodesToInterDomainLinks() {

		for(SubstrateLink link: interdomainlinks){
			
			if(link.getEndSN().getNetworkID().equalsIgnoreCase(networkid)){
	
				link.setEndNode(nodes.get(generator.nextInt(nodes.size())));
				
			//	System.out.println("End Node for Inter DOMAIN LINK has been set . . . . . . "+link.getEndNode().getID());				
			}
			
			else if(link.getStartSN().getNetworkID().equalsIgnoreCase(networkid)){
				
				link.setStartNode(nodes.get(generator.nextInt(nodes.size())));
			}
		}
		
	}
	
	
	public ArrayList<SubstrateNetwork> copySubstrateNetwork(int Num){
		
		ArrayList<SubstrateNetwork> list = new ArrayList<SubstrateNetwork>();
		
		for(int i = 0; i<Num; i++){
			
			list.add(copySubstrateNetwork());
			
		}
	
		return list;
	}
	
	
	public SubstrateNetwork copySubstrateNetwork(){
		
		SubstrateNetwork sn = new SubstrateNetwork();
		
		sn.parentnetwork = this;
		
	//	sn.setControllerNetwork(cn);
		
		ArrayList<SubstrateLink> copiedlinks = new ArrayList<SubstrateLink>();
		
		ArrayList<SubstrateNode> copiednodes = new ArrayList<SubstrateNode>();
		
		sn.setNetworkID(this.networkid);
		
		sn.setSubstrateNodes(copiednodes);
		
		sn.setSubstrateLinks(copiedlinks);
		
		for(SubstrateNode nd: nodes){
			
			SubstrateNode snd = new SubstrateNode();
			
			snd.setAvailableCPU(nd.getAvailableCPU());
			
			snd.setID(nd.getID());
			
			snd.setCity(nd.getCity());
			
		//	cn.addNodeLocation(snd, snd.getCity());
			
			snd.setNodeCost(nd.getNodeCost());
			
			snd.setSubstrateNetwork(sn);
			
			snd.setTotalCPU(nd.getTotalCPU());
			
			snd.setX_Loc(nd.getX_Loc());
			
			snd.setY_Loc(nd.getY_Loc());
			
			snd.setSubstrateNetwork(sn);
			
			copiednodes.add(snd);
			
		}
		
		for(SubstrateLink lk: links){
			
			SubstrateLink slk = new SubstrateLink();
			
			slk.setID(lk.getID());
			
			slk.setMinimumPrice(lk.getMinimumPrice());
			
			slk.setPriceDeviation(lk.getPriceDeviation());
			
			slk.setLinkLength(lk.getLinkLength());
			
			slk.setLinkDelay(lk.getLinkDelay());
			
			slk.setTotalBandWidth(lk.getTotalBandWidth());
			
			slk.setAvailableBandWidth(lk.getAvailableBandwidth());
			
						
			for(SubstrateNode nd: copiednodes){
				
				if(nd.getID().equalsIgnoreCase(lk.getStartNode().getID())){
					
					slk.setStartNode(nd);
					
					nd.addAdjacentSubstrateLink(slk);
				}
				
				if(nd.getID().equalsIgnoreCase(lk.getEndNode().getID())){
					
					slk.setEndNode(nd);
					
					nd.addAdjacentSubstrateLink(slk);
				}
			}
			
			slk.getStartNode().addAdjacentSubstrateNode(slk.getEndNode());
			
			slk.getEndNode().addAdjacentSubstrateNode(slk.getStartNode());
			
			copiedlinks.add(slk);
		}
		
				
		return sn;
		
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

	public SubstrateNode getIngressNode(VirtualLink virtuallink) {
		
		for(Iterator<String> lk = ingressnode.keySet().iterator(); lk.hasNext();){
			
			String link = lk.next();
			
			if(link.equalsIgnoreCase(virtuallink.getID())){
				
				return ingressnode.get(link);
			}
				
		}		
		
		System.out.println("The Ingress Node CANNOT be found . . . . .  something wrong!!!! /. . . .  SubstrateNetwork");
		
		try {
			Thread.sleep(900000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public void setFinished(VirtualNetwork vnet){
		
		status.remove(vnet);
	}
	
	public boolean checkFinished(VirtualNetwork vnet){
		
		if(status.contains(vnet)) return true;
		
		return false;
	}
	
	public void addPendingVN(VirtualNetwork vnet){
		
		status.add(vnet);
	}
	
	
/*	public void setLinkFinished(VirtualLink vlk){
		
		linkstatus.remove(vlk);
	}
	
	public boolean checkLinkFinished(VirtualLink vlk){
		
		if(linkstatus.contains(vlk)) return true;
		
		return false;
	}
	
	public void addLinkPendingVN(VirtualLink vlk){
		
		linkstatus.add(vlk);
	}*/
		
		
	public void addFailedLink(SubstrateLink slk){
		
		if(!failedlinks.contains(slk))
		
			failedlinks.add(slk);
		
		

	}
	
	public void addFailedLinkToNetworkQueue(SubstrateLink slk){
		
		if(!failedlinksqueue.contains(slk))
		
			failedlinksqueue.add(slk);
		
	}

	
	
	public void setRepairedLink(SubstrateLink slk){
		
			failedlinks.remove(slk);
			
/*			if(this.getNetworkID().contains("SN0")){
				
				System.out.println(System.currentTimeMillis()*1000+" Link Repair: "+slk.getID());
			}*/
	}
	
	
	public ArrayList<SubstrateLink> getFailedLinks(){
		
		return failedlinks;
	}
     
}