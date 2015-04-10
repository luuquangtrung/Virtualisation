/****************************************************************************/
/*                  Copyright 2001, Trustees of Boston University.          */
/*                               All Rights Reserved.                       */
/*                                                                          */
/* Permission to use, copy, or modify this software and its documentation   */
/* for educational and research purposes only and without fee is hereby     */
/* granted, provided that this copyright notice appear on all copies and    */
/* supporting documentation.  For any other uses of this software, in       */
/* original or modified form, including but not limited to distribution in  */
/* whole or in part, specific prior permission must be obtained from Boston */
/* University.  These programs shall not be used, rewritten, or adapted as  */
/* the basis of a commercial software or hardware product without first     */
/* obtaining appropriate licenses from Boston University.  Boston University*/
/* and the author(s) make no representations about the suitability of this  */
/* software for any purpose.  It is provided "as is" without express or     */
/* implied warranty.                                                        */
/*                                                                          */
/****************************************************************************/
/*                                                                          */
/*  Author:     Alberto Medina                                              */
/*              Anukool Lakhina                                             */
/*  Title:     BRITE: Boston university Representative Topology gEnerator   */
/*  Revision:  2.0         4/02/2001                                        */
/****************************************************************************/


/**
 * This file is a slight modification of the Orginal File. Modifications by Rashid Mijumbi
 * rashid@tsc.upc.edu aimed at using Brite for generating Topolgies for Network Virtualisation
 */

package Brite.Export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Network.Nodes.VirtualNode;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

import Brite.Graph.ASEdgeConf;
import Brite.Graph.ASNodeConf;
import Brite.Graph.Edge;
import Brite.Graph.Graph;
import Brite.Graph.GraphConstants;
import Brite.Graph.Node;
import Brite.Graph.NodeConf;
import Brite.Graph.RouterEdgeConf;
import Brite.Graph.RouterNodeConf;
import Brite.Model.ModelConstants;
import Brite.Topology.Topology;
import Brite.Util.Util;


/**
   Export.BriteExport provides functionality to export a topology into
   a BRITE format file.  The BRITE format looks like:
   <br>
   
   <pre>
   Topology: ( [numNodes] Nodes, [numEdges] Edges )
   BriteModel ( [ModelNum] ):  [BriteModel.toString()]
   
   Nodes: ([numNodes]):
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   [NodeID]  [x-coord]  [y-coord]  [inDegree] [outDegree] [ASid]  [type]
   ...

   Edges: ([numEdges]):
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]
   [EdgeID]  [fromNodeID]  [toNodeID]  [Length]  [Delay]  [Bandwidth]  [ASFromNodeID]  [ASToNodeID]  [EdgeType]
   ...
   </pre>
   <br>
   Please see the BRITE User Manual (at http://www.cs.bu.edu/brite/docs.htm) for more information.
 */
public class BriteExport implements VirtualisationVocabulary{
    
    private Topology t;
    private BufferedWriter bw;
   
    /**
       Class Constructor: Returns a BriteExport object which your code
       my keep around.  Does not actually write the topology to the
       file.  You must explicitly call the <code>export()</code> method of this
       object in order to write to the file.
       
       @param t the topology object to export
       @param outFile the destination file to write the topology to.
    */
    public BriteExport(Topology t, File outFile) {
	this.t = t;
	
	try {
	    bw = new BufferedWriter(new FileWriter(outFile));
	}
	catch (IOException e) {
	    Util.ERR(" Error creating BufferedWriter in BriteExport: " + e);
	}
    }

    
    /**
       Writes the contents of the topolgy in the BRITE format to the
       destination file specified in the constructor.  
    */
    @SuppressWarnings("unchecked")
	public void export() {
	Graph g = t.getGraph();
     	try {
	    
	    bw.write("Topology: ( " + g.getNumNodes() + " Nodes, " + g.getNumEdges()+ " Edges )");
	    bw.newLine();
	    bw.write(t.getModel().toString());
	    bw.newLine();
	    bw.write("Nodes: ( "+g.getNumNodes()+" )");
	    bw.newLine();
	    
	    /*output nodes*/
	    // ArrayList nodes = g.getNodesVector();
	    
	    Node[] nodes = g.getNodesArray();
	    Arrays.sort(nodes, Node.IDcomparator);
	    
	    for (int i=0; i< nodes.length; ++i) {
		Node n =  nodes[i];
		int x = (int) ((NodeConf) n.getNodeConf()).getX();
		int y = (int)  ((NodeConf)n.getNodeConf()).getY();
		int specificNodeType=-1;
		int ASid = -1;
		int outdegree = n.getOutDegree();
		int indegree = n.getInDegree();
		int nodeID = n.getID();
		
		if (n.getNodeConf() instanceof RouterNodeConf) { 
		    ASid = ((RouterNodeConf)n.getNodeConf()).getCorrAS();
		    specificNodeType = ((RouterNodeConf)n.getNodeConf()).getType();
		}
		if (n.getNodeConf() instanceof ASNodeConf) {
		    specificNodeType = ((ASNodeConf)n.getNodeConf()).getType();
		    ASid = nodeID;
		}
		
		bw.write(nodeID + "\t" + x + "\t" +y+ "\t" + indegree+ "\t" +outdegree+"\t"+ASid);
	
		if (n.getNodeConf() instanceof RouterNodeConf) {
		    if (specificNodeType == ModelConstants.RT_LEAF)
			bw.write("\tRT_LEAF");
		    else if (specificNodeType == ModelConstants.RT_BORDER) 
			bw.write("\tRT_BORDER");
		    else if (specificNodeType == ModelConstants.RT_STUB)
			bw.write("\tRT_STUB");
		    else if (specificNodeType == ModelConstants.RT_BACKBONE) 
			bw.write("\tRT_BACKBONE");
		    else if (specificNodeType == ModelConstants.NONE)
			bw.write("\tRT_NONE");
		}
		else if (n.getNodeConf() instanceof ASNodeConf) {
		    if (specificNodeType == ModelConstants.AS_LEAF)
			bw.write("\tAS_LEAF");
		    else if (specificNodeType == ModelConstants.AS_BORDER) 
			bw.write("\tAS_BORDER");
		    else if (specificNodeType == ModelConstants.AS_STUB)
			bw.write("\tAS_STUB");
		    else if (specificNodeType == ModelConstants.AS_BACKBONE) 
			bw.write("\tAS_BACKBONE");
		    else if (specificNodeType == ModelConstants.NONE)
			bw.write("\tAS_NONE");
		}
		

		bw.newLine();
	    }
	    bw.newLine();
	    bw.newLine();
	    /*output edges*/
	    
	    Edge[] edges = g.getEdgesArray();
	    //    ArrayList edges = g.getEdgesVector();
	    bw.write("Edges: ( "+edges.length+" )");
	    bw.newLine();
	    
	    Arrays.sort(edges, Edge.IDcomparator);
	    for (int i=0; i<edges.length; ++i) {
		Edge e = (Edge) edges[i];
		Node src = e.getSrc();
		Node dst = e.getDst();
		float dist = e.getEuclideanDist();
		float delay = 0; //dist/299792458; /*divide by speed of light*/
		if (e.getEdgeConf() instanceof ASEdgeConf)
		    delay = -1;
		int asFrom= src.getID();
		int asTo = dst.getID();
		if (src.getNodeConf() instanceof RouterNodeConf)
		    asFrom  =((RouterNodeConf) src.getNodeConf()).getCorrAS();
		if (dst.getNodeConf() instanceof RouterNodeConf)
		    asTo  =((RouterNodeConf) dst.getNodeConf()).getCorrAS();
		
		bw.write(e.getID() + "\t" + src.getID() + "\t" + dst.getID());
		bw.write("\t"+ dist + "\t" +delay+ "\t" + e.getBW());
		bw.write("\t"+ asFrom + "\t" + asTo);
		
		
		if (e.getEdgeConf() instanceof ASEdgeConf) {
		    int specificEdgeType = ((ASEdgeConf)e.getEdgeConf()).getType();
		    if (specificEdgeType == ModelConstants.E_AS_STUB)
			bw.write("\tE_AS_STUB");
		    else if (specificEdgeType == ModelConstants.E_AS_BORDER)
			bw.write("\tE_AS_BORDER");
		    else if (specificEdgeType == ModelConstants.NONE)
			bw.write("\tE_AS_NONE");
		    else /*backbone*/
			bw.write("\tE_AS_BACKBONE_LINK");
		}
		else  /*we have a router*/{
		    int specificEdgeType = ((RouterEdgeConf)e.getEdgeConf()).getType();
		    if (specificEdgeType == ModelConstants.E_RT_STUB)
			bw.write("\tE_RT_STUB");
		    else if (specificEdgeType == ModelConstants.E_RT_BORDER)
			bw.write("\tE_RT_BORDER");
		    else if (specificEdgeType == ModelConstants.NONE)
			bw.write("\tE_RT_NONE");
		    else /*backbone*/
			bw.write("\tE_RT_BACKBONE");
		}
	       
		if (e.getDirection() == GraphConstants.DIRECTED) 
		    bw.write("\tD");
		else bw.write("\tU");
		
		bw.newLine();
		
	    }
	    bw.close();
	}
	catch (Exception e) {
	  System.out.println("[BRITE ERROR]: Error exporting to file. " + e);
	  e.printStackTrace();
	    System.exit(0);
	    
	}
    }
    
    
    
    @SuppressWarnings("unchecked")
	public SubstrateNetwork getSubstrateNetwork(SubstrateNetwork substrate, String name) {
    
		substrate.setNetworkID(name);
		 
	    ArrayList<SubstrateLink> link = new ArrayList<SubstrateLink>();
	    
	    ArrayList<SubstrateNode> node = new ArrayList<SubstrateNode>();
    	
    	Graph g = t.getGraph();
	        
	    Node[] nodes = g.getNodesArray();
	    
	    Arrays.sort(nodes, Node.IDcomparator);
	    
	//	double stddev_x = Math.sqrt(substrate.getXVariance());
		
	//	double stddev_y = Math.sqrt(substrate.getYVariance());
		
		//System.out.println("Mean is: "+ substrate.getXMean()+" and "+substrate.getYMean()+" Variance is: "+ substrate.getXVariance()+" and "+substrate.getYVariance()+" and standard deviation is: "+stddev_x+ " and "+stddev_y);
	    
	    for (int i=0; i< nodes.length; ++i) {
		
		int x = generator.nextInt(50);
		
		int y = generator.nextInt(50);
		
	//    int x = (int) ((NodeConf) nodes[i].getNodeConf()).getX();
		
	//    int y = (int)  ((NodeConf)nodes[i].getNodeConf()).getY();
		
		//System.out.println("Node Lccation is: "+ x+" and "+y);
		
		SubstrateNode nd = new SubstrateNode();
		
		nd.setID(name+"node"+i);
		
		if(x*y >= 256)
		
		nd.setCity(256);
		
		else nd.setCity(x*y);
		
		//substrate.getControllerNetwork().addNodeLocation(nd, nd.getCity());
		
		//substrate.addNodeLocation(nd.getCity(), nd);
		
		double cost = MinSNCost + generator.nextDouble()*(MaxSNCost - MinSNCost);
		
		nd.setNodeCost(cost);
		
		//System.out.println("Node Lccation is: "+ x+" and "+y+" and city is: "+ nd.getCity());
		
    	nd.setX_Loc((double) x); 
    	
    	nd.setY_Loc((double) y);
    	
    	double cpu = SubNodeLowerCPU + generator.nextDouble()*(SubNodeUpperCPU - SubNodeLowerCPU);
    	
		nd.setTotalCPU(truncate(cpu));	
		
		nd.setAvailableCPU(nd.getTotalCPU());
		
		nd.setSubstrateNetwork(substrate);
		
		node.add(nd);
		
		}
		
	    
	    Edge[] edges = g.getEdgesArray();
	    
	    //    ArrayList edges = g.getEdgesVector();
	    int offset = nodes[0].getID();
	    
	    Arrays.sort(edges, Edge.IDcomparator);
	    
	    for (int i=0; i<edges.length; ++i) {
	    	
		Edge e = (Edge) edges[i];
		
		Node src = e.getSrc();
		
		Node dst = e.getDst();
		
		float dist = e.getEuclideanDist();
		
		float delay = dist/299792458; /*divide by speed of light*/
		
		if (e.getEdgeConf() instanceof ASEdgeConf)
			
		    delay = -1;
		
		int asFrom= src.getID();
		
		int asTo = dst.getID();
		
		SubstrateLink lk = new SubstrateLink();
		
		SubstrateNode nodei = node.get(asFrom-offset);
		
		SubstrateNode nodej = node.get(asTo-offset);
		
		nodei.addAdjacentSubstrateNode(nodej);
		
		nodej.addAdjacentSubstrateNode(nodei);
		
		nodei.addAdjacentSubstrateLink(lk);
		
		nodej.addAdjacentSubstrateLink(lk);
		
		lk.setLinkDelay((double) delay);
		
		lk.setLinkLength((double) dist);
		
		lk.setMinimumPrice(lk.getLinkLength());  
		
		lk.setPriceDeviation(generator.nextDouble()*lk.getMinimumPrice());
		
		lk.setStartNode(nodei);
		
		lk.setEndNode(nodej);
		
		lk.setStartSN(substrate);
		
		lk.setEndSN(substrate);
		
		lk.setID(nodej.getID()+nodei.getID());
		
		double lbw = e.getBW();
		
		lk.setTotalBandWidth(truncate(lbw));
		
		lk.setAvailableBandWidth(lk.getTotalBandWidth());
		
		link.add(lk);
		
	    }
	    
		substrate.setSubstrateNodes(node);
		
		substrate.setSubstrateLinks(link);
	    
		return substrate;

    }
    
    
    @SuppressWarnings("unchecked")
	public VirtualNetwork getVirtualNetwork(String name, VirtualNetwork virtual) {
         	
		virtual.setNetworkID(name);
		 
	    ArrayList<VirtualLink> link = new ArrayList<VirtualLink>();
	    
	    ArrayList<VirtualNode> node = new ArrayList<VirtualNode>();
    	    
    	Graph g = t.getGraph();
	       
	    Node[] nodes = g.getNodesArray();
	    
	    Arrays.sort(nodes, Node.IDcomparator);
	    
	    
	    
	//	double stddev_x = Math.sqrt(virtual.getXVariance());
		
	//	double stddev_y = Math.sqrt(virtual.getYVariance());
	    
	   // List<SubstrateNode> nodlocations = selectNodes(substrate.getSubstrateNodes(), nodes.length);
	    
	    for (int i=0; i< nodes.length; ++i) {
	    	
			//Node n =  nodes[i];
					
		//    int x = (int) ((NodeConf) nodes[i].getNodeConf()).getX();
			
		//    int y = (int)  ((NodeConf)nodes[i].getNodeConf()).getY();
		    
			int x = generator.nextInt(50);
			
			int y = generator.nextInt(50);
		
		VirtualNode nd = new VirtualNode();
		
		nd.setID(name+"node"+i);
		
		nd.setCity(x*y);
		
    	nd.setX_Loc((double) x); 
    	
    	nd.setY_Loc((double) y);
		
    	nd.setX_Dev(DeviationX);		
    	
    	nd.setY_Dev(DeviationY);
    	
    	double cpu = VirNodeLowerCPU + generator.nextDouble()*(VirNodeUpperCPU - VirNodeLowerCPU);
		
    	nd.setTotalCPU(truncate(cpu));		
		
    	nd.setAvailableCPU(nd.getTotalCPU());
    	
    	//nd.set
		
    	node.add(nd);
		
		}
		    
	    Edge[] edges = g.getEdgesArray();
	    
	    //    ArrayList edges = g.getEdgesVector();
	    
	    Arrays.sort(edges, Edge.IDcomparator);
	    
	    int offset = nodes[0].getID();
	    
	    for (int i=0; i<edges.length; ++i) {
		
	    Edge e = (Edge) edges[i];
		
	    Node src = e.getSrc();
		
	    Node dst = e.getDst();
		
	    float dist = e.getEuclideanDist();
		
	    float delay = dist/299792458; /*divide by speed of light*/
		
	    if (e.getEdgeConf() instanceof ASEdgeConf)
		   
	    	delay = -1;
		
	    int asFrom= src.getID();
		
	    int asTo = dst.getID();
		
		VirtualLink lk = new VirtualLink();
		
		VirtualNode nodei = node.get(asFrom-offset);
		
		VirtualNode nodej = node.get(asTo-offset);
		
		nodei.addAdjacentVirtualLink(lk);
		
		nodej.addAdjacentVirtualLink(lk);
		
		nodei.addAdjacentVirtualNode(nodej);
		
		nodej.addAdjacentVirtualNode(nodei);
		
		lk.setLinkDelay((double) delay);
		
		lk.setLinkLength((double) dist);
		
		lk.setStartNode(nodei);
		
		lk.setEndNode(nodej);
		
		lk.setID(nodej.getID()+nodei.getID());
		
		double lbw = e.getBW();
		
		lk.setTotalBandWidth(truncate(lbw));
		
		lk.setAvailableBandWidth(lk.getTotalBandWidth());
		
		link.add(lk);
		
	    }
	    
		virtual.setVirtualNodes(node);
		
		virtual.setVirtualLinks(link);
	    
		return virtual;

    }


  //randomly selects locations of a given "numberofNodes" from the substrate Network

    public static ArrayList<SubstrateNode> selectNodes(ArrayList<SubstrateNode> nodelist, int numberofNodes) {
    	
    	ArrayList<SubstrateNode> ret = new ArrayList<SubstrateNode>();
    	
    	for (int k = 0; k<numberofNodes; k++){
    	
    	ret.add(new SubstrateNode());
    	  
    	}
    	  
    	  int AddedNodes = 0, i = 0, RemainingNodes = nodelist.size();
    	 	  
    	  while (numberofNodes > 0) {
    		  		  
    	    int rand = generator.nextInt(RemainingNodes);
    	    	    
    	    if (rand < numberofNodes) {
    	    	
    	    	ret.set(AddedNodes++, nodelist.get(i));
    	    	
    	        numberofNodes--;
    	      
    	    }
    	    
    	    RemainingNodes--;
    	    
    	    i++;
    	    
    	  }
    	  
    	  return ret;
    	  
    	}

    /**
     * Returns the parameter num truncated to 2 decimal places. Only aimed at avoiding very long decimal numbers
     * @param num
     * @return
     */
	private Double truncate(double num) {
		
		double newNum = Math.round(num*100.0)/100.0;
		
		return newNum;
	}


}




