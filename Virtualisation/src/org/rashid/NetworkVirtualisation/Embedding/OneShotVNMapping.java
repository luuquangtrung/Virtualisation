/* Copyright (C) 2012, Rashid MIJUMBI
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

package org.rashid.NetworkVirtualisation.Embedding;

import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.concert.IloSemiContVar;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.UnknownObjectException;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Network.Nodes.VirtualNode;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;


public class OneShotVNMapping implements VirtualisationVocabulary{
		
	 SubstrateNetwork AugmentedSub = new SubstrateNetwork();
	
	 List<SubstrateNode> AugSubNodes = new ArrayList<SubstrateNode>();
	
	 List<SubstrateLink> AugSubLinks = new ArrayList<SubstrateLink>();
	 
	 Map<VirtualNode, Double> connectedLinkCapacity = new LinkedHashMap<VirtualNode, Double>();
	 
	 Map<VirtualNode, List<SubstrateLink>> connectedLinks = new LinkedHashMap<VirtualNode, List<SubstrateLink>>();
	
	 Map<SubstrateLink, Double> LinkFlows_F = new LinkedHashMap<SubstrateLink, Double>();
	
	 Map<SubstrateLink, Double> LinkFlows_X = new LinkedHashMap<SubstrateLink, Double>();
	
	 Map<VirtualNode, Map<SubstrateNode, Double>> virflows = new LinkedHashMap<VirtualNode, Map<SubstrateNode, Double>>();
	
	 List<SubstrateNode> subnodes = new ArrayList<SubstrateNode>();
	
	 List<SubstrateLink> sublinks = new ArrayList<SubstrateLink>();
	
	 List<VirtualNode> virnodes = new ArrayList<VirtualNode>();
	
	 List<VirtualLink> virlinks = new ArrayList<VirtualLink>();
	 
	 Map<VirtualNode, SubstrateNode> nodeMappings = new LinkedHashMap<VirtualNode, SubstrateNode>();
	 		
	 double alpha = 1.0;
	
	 double beta = 1.0;
	
	 double delta = 0.0001;
	
	 Map<String, Integer> variables = new LinkedHashMap<String, Integer>();
	
	 boolean mappingresult;
	 
	 SubstrateNetwork substrate = new SubstrateNetwork();
	 
	 VirtualNetwork virtual = new VirtualNetwork();
	
	 
		public OneShotVNMapping (VirtualNetwork vir, SubstrateNetwork sub){
			
			this.substrate = sub;
			
			this.virtual = vir;
	
			this.subnodes = substrate.getSubstrateNodes();
			
			this.sublinks = substrate.getSubstrateLinks();
			
			this.virnodes = virtual.getVirtualNodes();
			
			this.virlinks = virtual.getVirtualLinks();
			
			this.mappingresult = true;
			 
		}
		

	public boolean performMapping() throws IloException, StaleProxyException, CloneNotSupportedException, InterruptedException, IOException{
		
		initializeNodeLinkCapacities();
		
		AugmentedSub = createAugmentedSubstrate(substrate, virtual);
		
		AugSubNodes = AugmentedSub.getSubstrateNodes();
		
		AugSubLinks = AugmentedSub.getSubstrateLinks();		
			
        IloCplex cplex = new IloCplex();
        
        cplex.setParam(IloCplex.IntParam.RootAlg, IloCplex.Algorithm.Network);
               		
		  int numvar_f = 2*(AugSubLinks.size())*(virlinks.size());	
		  
		  int numvar_x = getNumVariables();;
		   		  		          
		  IloSemiContVar[] f = new IloSemiContVar[numvar_f];				
		  
		  IloSemiContVar[] x = new IloSemiContVar[numvar_x];  				
		           
		  List<IloConstraint> constraints = new ArrayList<IloConstraint>();	
		
	      populateModel(cplex, f, x, constraints, numvar_f, numvar_x);	      
	        
	      IloConstraint[]  rng = new IloConstraint[constraints.size()];
	        
	      constraints.toArray(rng);
	      	      
	        if (cplex.solve()) {
	        	
	        	mappingresult = true;
	        	
	        	mappingresult = retrievesolution(cplex, substrate, virtual, AugmentedSub, numvar_f, numvar_x, f, x);
	        	
	        //	cplex.exportModel("OneShotVNMapping.lp");
	        	
	        	cplex.end();
	        	
	        	return mappingresult;
	        	
	        }
	        
	        else {
	        	
	        	
	        	
	        	mappingresult = false;
	        	        	
	        	System.err.println("CPLEX could NOT find a Soulution - One Shot Optimal Failed");
	        	
	        	//cplex.exportModel("OneShotVNMapping.lp");
	        	
	        	cplex.end();
	        	
	        	return mappingresult;
	        }
	        
	
	}
	
	private  int getNumVariables() {
		
		int x = 0;
		
		for(VirtualNode vnd: virnodes){
			
			for(@SuppressWarnings("unused") SubstrateNode snd: vnd.getPossibleSubstrateNodes()){
				
				x++;
			}
		}
				
		return x;
	}


	private void initializeNodeLinkCapacities() {

		for(VirtualNode nd: virnodes){
			
			connectedLinkCapacity.put(nd, 0.0);
			
			List<SubstrateLink> links = new ArrayList<SubstrateLink>();
			
			connectedLinks.put(nd, links);
			
		}
		
		for(VirtualLink lk: virlinks){
			
			VirtualNode n1 = lk.getStartNode();
			
			VirtualNode n2 = lk.getEndNode();
			
			Double val = lk.getTotalBandWidth();
			
			connectedLinkCapacity.put(n1, (connectedLinkCapacity.get(n1)+val));
			
			connectedLinkCapacity.put(n2, (connectedLinkCapacity.get(n2)+val));
		}
		
	}


	private boolean retrievesolution(IloCplex cplex, SubstrateNetwork substrate, VirtualNetwork virtual, SubstrateNetwork augmentedSub, 
			
			int numvar_f, int numvar_x, IloNumVar[] f, IloSemiContVar[] x) throws UnknownObjectException, IloException, StaleProxyException, CloneNotSupportedException, InterruptedException, IOException {
				
        System.out.println("Solution status = " + cplex.getStatus());
        
        System.out.println("Solution value  = " + cplex.getObjValue());
		
		        for (int j = 0; j < (numvar_x); ++j) {
		        	
		            double val = cplex.getValue(x[j]);
		            
		            String name = x[j].getName();
		                           
		            double cost = calculateCost(substrate,virtual);
		            if(val > 0.99 && val < 1.01){
		            	
		            	//only update node mapping when the willingness to pay is bigger than the cost 
		            	if(virtual.getWillingnessToPay() > cost){
		            		//System.out.println("Variable " + name + ": Value = " + val);
		            		updateNodeMapping(name);
		            	} else {
		            		mappingresult = false;
		            	}
		         	          		   	                      
		         }
		            
		     }
        
		        finaliseNodeMapping();
        
        
        for (int j = 0; j < (numvar_f); ++j) {
        	
           double val = cplex.getValue(f[j]);
           
           String name, startvirtual, endvirtual, startsubstrate, endsubstrate;
           
           name = f[j].getName();
           
           if(val!=0.0){    
        	   
        //	   System.out.println("Variable " + name + ": Value = " + val);
        	   
      	   		int a, b, c;
    			
       			a = name.indexOf("_"); String namea = name.replaceFirst("_", "X");
       			
       			b = namea.indexOf("_"); String nameb = namea.replaceFirst("_", "X");
       			
       			c = nameb.indexOf("_");
       			
       			startsubstrate = name.substring(0, a);
       			
       			endsubstrate = name.substring(a+1, b);
       			
       			startvirtual = name.substring(b+1, c);
       			
       			endvirtual = name.substring(c+1);
       			
       			setSub_Vir_Links(startvirtual, endvirtual, startsubstrate, endsubstrate, val);
        	           	           
           }
                         
        }
        
		return mappingresult;
	}

	
	private double calculateCost(SubstrateNetwork substrate,
			VirtualNetwork virtual) {
		double cost = 0.0;
		
		ArrayList<VirtualLink> virtualLinks = virtual.getVirtualLinks();
		ArrayList<VirtualNode> virtualNodes = virtual.getVirtualNodes();
		
		//iterate over all virtual nodes, proportionally add costs based on utilization
		for (int i = 0; i < virtualNodes.size();i++){
			SubstrateNode substrateNode = virtualNodes.get(i).getSubstrateNode();
			cost += substrateNode.getNodeCost()/substrateNode.getTotalCPU() * virtualNodes.get(i).getTotalCPU();
		}
		//iterate over all virtual links, proportionally add costs based on utilization
		for (int i =0; i < virtualLinks.size();i++){
			VirtualLink virtualLink = virtualLinks.get(i);
			ArrayList<SubstrateLink> substrateLinks = virtualLink.getMappingSubstrateLinks();
			for (int j = 0; j < substrateLinks.size();j++){
				SubstrateLink substrateLink = substrateLinks.get(j);
				cost += substrateLink.getLinkCost()/substrateLink.getTotalBandWidth() * virtualLink.getTotalBandWidth();
			}
		}
		
		return cost;
	}


	private void finaliseNodeMapping() throws StaleProxyException {
		
		for(VirtualNode nodvir: virnodes){
			
			SubstrateNode nod = nodeMappings.get(nodvir);
			
			nodvir.setSubstrateNode(nod);
			
			nod.addMappedNode(nodvir);
			
			nod.setAvailableCPU(nod.getAvailableCPU() - nodvir.getTotalCPU());
			
			//nodvir.seta(nodvir.getTotalCPU());
			
		//	Agents.MessageAgent(nod, nodvir); 	//Send Update to Substrate Node Agent for resource Management
				
		}
		
	}

	private void updateNodeMapping(String name) {
		
		int index = name.indexOf("_");

		String vir = name.substring(0, index);
		
		String sub = name.substring(index+1);
		
		VirtualNode vnd = getVirNode(vir, virnodes);
		
	//	System.out.println("Vir Node is "+vnd.getID());
		
		SubstrateNode snd = getSubNode(sub, subnodes);
		
	//	System.out.println("Sub Node is "+snd.getID());
		
		nodeMappings.put(vnd, snd);
		
	}


	private VirtualNode getVirNode(String name, List<VirtualNode> nodes) {
			
		VirtualNode nd = new VirtualNode();
			
			for(VirtualNode nod: nodes){
				
				if(nod.getID().equalsIgnoreCase(name)){
					
					nd = nod;
					
					return nd;
				}
			}
				
			return nd;

	}
	
	
	private SubstrateNode getSubNode(String name, List<SubstrateNode> nodes) {
		
		SubstrateNode nd = new SubstrateNode();
			
			for(SubstrateNode nod: nodes){
				
				if(nod.getID().equalsIgnoreCase(name)){
					
					nd = nod;
					
					return nd;
				}
			}
				
			return nd;

	}


	private void setSub_Vir_Links(String startvirtual, String endvirtual, 
			
								  String startsubstrate, String endsubstrate, double val) throws StaleProxyException {
		
		
		VirtualNode startvirtualnode = new VirtualNode(), endvirtualnode = new VirtualNode(); 
				
		SubstrateNode startsubstratenode = new SubstrateNode(), endsubstratenode = new SubstrateNode();
		
		VirtualLink virtuallink = new VirtualLink();
		
		SubstrateLink substratelink = new SubstrateLink();
		
		for(VirtualNode node: virnodes){
			
			if(node.getID().equalsIgnoreCase(startvirtual)) {
				
				startvirtualnode = node;
				
			//	System.out.println("StartV node Found:  "+ node.getID());
				
			}
			
			if(node.getID().equalsIgnoreCase(endvirtual)){
				
				endvirtualnode = node;
				
			//	System.out.println("EndV node Found:  "+ node.getID());
				
			}
		}
		
		for(VirtualLink link: virlinks){
			
			if(link.getStartNode()==startvirtualnode && link.getEndNode()==endvirtualnode){
				
				virtuallink  = link;
				
			//	System.out.println("VLink Found:  "+ link.getID());
			
			}
			
			else if(link.getStartNode()==startvirtualnode && link.getEndNode()==endvirtualnode){
			
				virtuallink  = link;
				
			//	System.out.println("VLink Found:  "+ link.getID());
				
			}

		}
		
		List<SubstrateNode> AugNodes = new ArrayList<SubstrateNode>();
		
		AugNodes.addAll(AugSubNodes);
		
		for(SubstrateNode node: AugNodes){
			
			if(node.getID().equalsIgnoreCase(startsubstrate)) {
				
				startsubstratenode = node;
				
			//	System.out.println("StartS node Found:  "+ node.getID());
			}
			
			if(node.getID().equalsIgnoreCase(endsubstrate)){
				
				endsubstratenode = node;
				
			//	System.out.println("EndS node Found:  "+ node.getID());
			}
		}
		
		for(SubstrateLink link: sublinks){
			
			if(link.getStartNode()==startsubstratenode && link.getEndNode()==endsubstratenode){
				
				substratelink  = link;
				
			}
			
			else if(link.getEndNode()==startsubstratenode && link.getStartNode()==endsubstratenode){
				
				substratelink  = link;
					
			}

		}
		
		if(!substratelink.getID().equalsIgnoreCase("")){
			
		Map<VirtualLink, Double> flow = substratelink.getVirtualLinkFlows(); 					   		
		
		substratelink.setAvailableBandWidth(substratelink.getAvailableBandwidth()- val);
		
	    flow.put(virtuallink, val);
	    
	    substratelink.addMappedVirtualLink(virtuallink);
	    
	    virtuallink.addMappingSubstrateLink(substratelink);
	    
	//    Agents.MessageAgent(substratelink, virtuallink);  //Send Update to Substrate Link Agent for resource Management
				
		}
				
	}

	private void populateModel(IloCplex model, IloSemiContVar[] f, IloNumVar[] x, List<IloConstraint> constraint, int numvar_f, int numvar_x) throws IloException {
		  	      
	      double[] objvals_f = new double [numvar_f];
	      
	      double[] objvals_x = new double [numvar_x];
	      
	      IloNumVarType type = IloNumVarType.Float;
	      
	      IloLinearNumExpr fvars;
	      
	      IloLinearNumExpr xvars;
	       
	      int i = 0;
	      
	      //Definition of the first set of Variables - f

	      for(SubstrateLink slk: AugSubLinks){
	    	    		  	    	      	  	    	  
	    	  for(VirtualLink vlk: virlinks){
	    		  
	    		  String name1 = (slk.getStartNode().getID()+"_"+slk.getEndNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID());
	    		  
	    		//  IloSemiContVar f1  = model.semiContVar(vlk.getTotalBandWidth(), vlk.getTotalBandWidth(), type);
	    		  
	    		  IloSemiContVar f1  = model.semiContVar(0, vlk.getTotalBandWidth(), type);
	    		 		   
	    		  f1.setName(name1);
	    		  
	    		  String name2 = (slk.getEndNode().getID()+"_"+slk.getStartNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID());
	    		  
	    		  IloSemiContVar f2  = model.semiContVar(0, vlk.getTotalBandWidth(), type);
	    		  
	    		//  IloSemiContVar f2  = model.semiContVar(vlk.getTotalBandWidth(), vlk.getTotalBandWidth(), type);
	    		  
	    		  f2.setName(name2);
	    		  		  
	    		  variables.put(name1, i); 
	    		  
	    		  variables.put(name2, i+1);
	    	    		  
	    	    		  objvals_f[i] = 1.0/slk.getAvailableBandwidth();
	    	    		  
	    	    		  objvals_f[i+1] = objvals_f[i];
	    	    		  
	    	    		  f[i] = f1;
	    	    		  
	    	    		  f[i+1] = f2;
	    		 	    		  	    		  
	    		  i+=2;
	    		  	    		  	    		  
	    	  }
	      }
	      
	      fvars = model.scalProd(f, objvals_f);
	      
	      
	      //Definition of the second set of Variables - x
	      
	      int j = 0;
	      
	      for(VirtualNode vnd: virnodes){
	      
	    	  for(SubstrateNode snd: vnd.getPossibleSubstrateNodes()){
	    			  
	    			  String name = vnd.getID()+"_"+snd.getID();
  	    			  
	    			  IloSemiContVar xvar  = model.semiContVar(1.0, 1.0, type);
	    			  
	    			  xvar.setName(name);

	    			  objvals_x[j] = 1.0/snd.getAvailableCPU();
	    		
	    	   		  x[j] = xvar;
	    	   		  	
		    		  variables.put(name, j);
		    		  		    		  	    		  
		    		  j++;	    			      			  	    			  	    		  
	    	  }
	      
	      }
	      	     
	      xvars = model.scalProd(x, objvals_x);
	      
	      
	      // Objective Function definition
	      
	      model.addMinimize(model.sum(xvars, fvars));
	      
	       	      
   //Each Virtual Node Must be Mapped once to ONLY one substrate Node in its neighbourhood 
  
	  for(VirtualNode vnd: virnodes){
		  
    	  Set<IloNumExpr> vars = new LinkedHashSet<IloNumExpr>();
    	  
  	    		  for(SubstrateNode snd: vnd.getPossibleSubstrateNodes()){
  	    			  
  	    			Set<IloNumExpr> varsf = new LinkedHashSet<IloNumExpr>();
  	    		  
  		    		      String name = vnd.getID()+"_"+snd.getID();
  		    		   
  	    				  vars.add(model.prod(1.0, x[variables.get(name)]));
  	    				   	    				  
  	    				  for(VirtualLink vlk: virlinks){
  	    					  
  	    				      String name1 = vnd.getID()+"_"+snd.getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();
  	  		    		   
    	    				  varsf.add(model.prod(1.0, f[variables.get(name1)])); 
    	    				  
  	    				      String name2 = snd.getID()+"_"+vnd.getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();
  	  	  		    		   
    	    				  varsf.add(model.prod(1.0, f[variables.get(name2)]));
  	    					  
  	    				  }
  	    				    	    					  		    		
  	 		    		IloNumExpr[] varssf = new IloNumExpr [varsf.size()];
  	 	  	    		  
  	  		    		varsf.toArray(varssf);
  	  		    		
  	  		    		constraint.add(model.addLe(model.sum(varssf), model.prod(1000.0, x[variables.get(name)])));

  	    	  	  } 
  	    		  		  	          	  		  		    		
  		    		IloNumExpr[] varss = new IloNumExpr [vars.size()];
  	    		  
  		    		vars.toArray(varss);
  		    		
  		    		constraint.add(model.addEq(model.sum(varss), 1.0));
  		    				    		
	  			}
	  
	  
	  //Each Substrate MAY only be used to atmost ONE of the possible virtual nodes
	  
		  for(SubstrateNode snd: subnodes){
		  
			  Set<IloNumExpr> vars = new LinkedHashSet<IloNumExpr>();
    	  
    	  	for(VirtualNode vnd: virnodes){  
    	  		
    	  		if(vnd.getPossibleSubstrateNodes().contains(snd)){
  	    		  
  		    		      String name = vnd.getID()+"_"+snd.getID();
  		    		   
  	    				  vars.add(model.prod(1.0, x[variables.get(name)]));
  	    				  
    	  		      }
  	    	  	  } 
    	  	
    	  	if(vars.size()!=0){
  	    		  		  	          	  		  		    		
  		    		IloNumExpr[] varss = new IloNumExpr [vars.size()];
  	    		  
  		    		vars.toArray(varss);
  		    		
  		    		constraint.add(model.addLe(model.sum(varss), 1.0));
  		    		
    	  	}
  		    		
	  }
	      
	            	    	      	      
	     //Capacity Conservation Constraints
	      
	      for(SubstrateLink slk: AugSubLinks){
	    	  
	    	  Set<IloNumExpr> vars = new LinkedHashSet<IloNumExpr>();	    	  
	    	  	    	  
	    		  for(VirtualLink vlk: virlinks){
	    		  
		    		      String name1 = (slk.getStartNode().getID()+"_"+slk.getEndNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID());
		    		  
		    		      String name2 = (slk.getEndNode().getID()+"_"+slk.getStartNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID());
	    			  
	    				  vars.add(model.prod(1.0, f[variables.get(name1)])); 
	    				  
	    				  vars.add(model.prod(1.0, f[variables.get(name2)]));

	    	  } 	    		  
	          	  
		    		IloNumExpr[] varss = new IloNumExpr [vars.size()];
		    		
		    		vars.toArray(varss);
		    				    			    					    		  
		    		constraint.add(model.addLe(model.sum(varss), slk.getAvailableBandwidth()));
    	  
	      }
	      
	      
	     //Flow Constraints
	      
	      for(VirtualLink vlk: virlinks){
	    	  
	    	  VirtualNode start = vlk.getStartNode();
	    	  
	    	  VirtualNode end = vlk.getEndNode();
	    	  
	    	  List<SubstrateLink> startlks = connectedLinks.get(start);
	    	  
	    	  List<SubstrateLink> endlks = connectedLinks.get(end);
	    	  
	    	  Set<IloNumExpr> startvars = new LinkedHashSet<IloNumExpr>();  	    	  
	    	  
	    	  Set<IloNumExpr> endvars = new LinkedHashSet<IloNumExpr>();		

	    	  
	    	  //Source Constraints
	    	  
	    	  for(SubstrateLink slk: startlks){
	    		  
	    		  String name = slk.getStartNode().getID()+"_"+slk.getEndNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();		    		 
	    		  
	    		  startvars.add(model.prod(1.0, f[variables.get(name)]));
	    		      		  
	    		  String name1 = slk.getEndNode().getID()+"_"+slk.getStartNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();		    		 
	    		  
	    		  startvars.add(model.prod(-1.0, f[variables.get(name1)]));
	    		  
	    	  }
	    	  	    	 
	    		IloNumExpr[] varsstart = new IloNumExpr [startvars.size()];
	    		  
	    		startvars.toArray(varsstart); 
	    		
	    		constraint.add(model.addEq(model.sum(varsstart), vlk.getTotalBandWidth()));
	    			    		
	    		
	    		// Sink Constraints
	    		
		    	  for(SubstrateLink slk: endlks){
		    		  
			    		  String name = slk.getEndNode().getID()+"_"+slk.getStartNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();		    		 
			    		  
			    		  endvars.add(model.prod(1.0, f[variables.get(name)]));
			    		  
			    		  String name1 = slk.getStartNode().getID()+"_"+slk.getEndNode().getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();		    		 
			    		  
			    		  endvars.add(model.prod(-1.0, f[variables.get(name1)]));
		    		  
		    	  }
		    	  
			    		IloNumExpr[] varsend = new IloNumExpr [endvars.size()];
			    		  
			    		endvars.toArray(varsend); 
			    		
			    		constraint.add(model.addEq(model.sum(varsend), vlk.getTotalBandWidth()));
		   
			    		
			    // Intermediate Nodes
			    		
		  	      for(SubstrateNode snd: subnodes){      
			    	  
			    	  Set<IloNumExpr> vars = new LinkedHashSet<IloNumExpr>();
			    	  					    		  
			    		  List<SubstrateNode> lks = getConnectedNodes(snd, AugSubLinks);  
			    		  
				    		for(SubstrateNode slk: lks){
				    			
				    			 String name1 = snd.getID()+"_"+slk.getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();  		  			 
				 	    		  
				 	    		 String name2 = slk.getID()+"_"+snd.getID()+"_"+vlk.getStartNode().getID()+"_"+vlk.getEndNode().getID();
				 	    		 
					    		  vars.add(model.prod(1.0, f[variables.get(name1)]));
					    		  
					    		  vars.add(model.prod(-1.0, f[variables.get(name2)]));
					    					    			
					    		}
			    	  
			    		IloNumExpr[] varss = new IloNumExpr [vars.size()];
			    		  
			    		vars.toArray(varss); 
			    		
			    		constraint.add(model.addEq(model.sum(varss), 0.0)); 
		    		
		  	      }
		    				    		
	      } 
	          	        	
	}


	private  List<SubstrateNode> getConnectedNodes(SubstrateNode node, List<SubstrateLink> links) {
		
		List<SubstrateNode> list = new ArrayList<SubstrateNode>();
		
		for(SubstrateLink lk: links){
			
			if(lk.getStartNode()== node){
				
				list.add(lk.getEndNode());
			}
			
			if(lk.getEndNode()==node){
				
				list.add(lk.getStartNode());
			}
		}

		return list;
	}


	private SubstrateNetwork createAugmentedSubstrate(SubstrateNetwork substrate, VirtualNetwork virtual) {
		
			SubstrateNetwork augsubstrate = new SubstrateNetwork();
			
			augsubstrate = (SubstrateNetwork) substrate.clone();
			
			List<SubstrateNode> AugNodes = new ArrayList<SubstrateNode>();
			
			List<SubstrateLink> AugLinks = new ArrayList<SubstrateLink>();
			
			for(SubstrateNode node: substrate.getSubstrateNodes()){
				
				AugNodes.add(node);
				
			}
				
			for(SubstrateLink link: substrate.getSubstrateLinks()){
				
				AugLinks.add(link);
				
				LinkFlows_F.put(link, 0.000001);
				
				LinkFlows_X.put(link, 0.000001);
			}
		
			for(VirtualNode node: virtual.getVirtualNodes()){
				
			//	System.out.println("hereeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
				
				List<SubstrateLink> links = new ArrayList<SubstrateLink>();
				
				SubstrateNode nd = new SubstrateNode();
				
				nd.setID(node.getID());
				
				AugNodes.add(nd);
				
			//	System.out.println("Node ND "+nd.getID());
				
				for(SubstrateNode neighbours: node.getPossibleSubstrateNodes()) {
					
		//			System.out.println("neighbours "+neighbours.getID());
				
				SubstrateLink link = new SubstrateLink();
				
				link.setTotalBandWidth(connectedLinkCapacity.get(node));
				
				link.setStartNode(nd);
				
				link.setEndNode(neighbours);
				
				link.setID(link.getStartNode().getID()+"-"+link.getEndNode().getID());
						
				link.setAvailableBandWidth(link.getTotalBandWidth());
				
				AugLinks.add(link);
				
				links.add(link);
				
				LinkFlows_F.put(link, 0.000001);
				
				LinkFlows_X.put(link, 0.000001);
				
				}
				
				connectedLinks.put(node, links);
			}
			
		augsubstrate.setSubstrateNodes((ArrayList<SubstrateNode>) AugNodes);
				
		augsubstrate.setSubstrateLinks((ArrayList<SubstrateLink>) AugLinks);
			
		return augsubstrate;
	
	}
	
}
