package org.rashid.NetworkVirtualisation.Main;


import ilog.concert.IloException;
import jade.wrapper.StaleProxyException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.rashid.NetworkVirtualisation.Embedding.OneShotVNMapping;
import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Links.VirtualLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Networks.VirtualNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Network.Nodes.VirtualNode;
import org.rashid.NetworkVirtualisation.Utilities.Poisson;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;


/**
 * 
 * Generates VN Requests following a Poisson distribution, monitors these VNs and deletes those
 * whose service time has expired following an exponential distribution
 * 
 * @author Rashid Mijumbi
 * @author Joan Serrat
 * @author Juan-Luis Gorricho
 * 
 * @since  January 2013
 * 
 */

public class VNRequests implements VirtualisationVocabulary{
	
	public static Boolean result = false;
		
	int virtualnetworkDuration = 0;
	
	FileWriter filestream;
	
	VirtualNetwork virtual = new VirtualNetwork();
	
	Map<SubstrateNetwork, Double>TotalNodeCapacity = new LinkedHashMap<SubstrateNetwork, Double>();
	
	Map<SubstrateNetwork, Double>TotalLinkCapacity = new LinkedHashMap<SubstrateNetwork, Double>();
	
	Map<SubstrateNetwork, VirtualNetwork> SubstrateVirtualMap = new LinkedHashMap<SubstrateNetwork, VirtualNetwork>();
	
	List<SubstrateNetwork> substratenetworklist = new ArrayList<SubstrateNetwork>();
	
	List<VirtualNetwork> virtualnetworklist = new ArrayList<VirtualNetwork>();
	
	 Map<SubstrateNetwork, List<Double>> NodeUtilization = new LinkedHashMap<SubstrateNetwork, List<Double>>();
	
	 Map<SubstrateNetwork, List<Double>> LinkUtilization = new LinkedHashMap<SubstrateNetwork, List<Double>>();
	
	 Map<SubstrateNetwork, List<Double>> Cost = new LinkedHashMap<SubstrateNetwork, List<Double>>();
	
	 Map<SubstrateNetwork, List<Double>> Revenue = new LinkedHashMap<SubstrateNetwork, List<Double>>();
	
	 Map<SubstrateNetwork, List<Integer>> TotalRequests = new LinkedHashMap<SubstrateNetwork, List<Integer>>();
	
	 Map<SubstrateNetwork, List<Integer>> AccepetedRequests = new LinkedHashMap<SubstrateNetwork, List<Integer>>();
	
	 Map<SubstrateNetwork, List<Double>> AccepetenceRatio = new LinkedHashMap<SubstrateNetwork, List<Double>>();
	
	 Map<SubstrateNetwork, Boolean> MappingResult = new LinkedHashMap<SubstrateNetwork, Boolean>();
	
	 Map<SubstrateNetwork, List<VirtualNetwork>> VirtualNetworks = new LinkedHashMap<SubstrateNetwork, List<VirtualNetwork>>();
	
	 Map<SubstrateNetwork, List<VirtualNetwork>> ExpiredVirtualNetworks = new LinkedHashMap<SubstrateNetwork, List<VirtualNetwork>>();
	
	 double averagerate = 0.0;
	 
	 double averagearrivalrate = 0.0;
	 
	 SubstrateNetwork substrate;
	
	
	public void sendRequest(List<SubstrateNetwork> substratelist, SubstrateNetwork subnet) throws InterruptedException, IloException, IOException {
			
		averagerate = 1.0/averagewaitingtime;			  //Average service time for each arrival
		
		averagearrivalrate = 1.0/servicetime;   //Average number of arrivals per time unit 
		
		int[] arrivals = Poisson.Interval(averagearrivalrate);
		
		substratenetworklist = substratelist;
		
		substrate = subnet;
		
		initializeStatistics();
		
		Thread.sleep(2000);			//wait for all the node and link to be created.
			
		int i = 0; 
				
		  while(i<arrivals.length) {	  
		   		   
		   try {
			       				   			
			deleteExpiredVNS();
			
			createVirtualNetwork(i);
			
			//substratenetworklist.get(0).printNetwork();
			
			//virtualnetworklist.get(0).printNetwork();
			
			experiments();
		 

				 for(SubstrateNetwork substrate: substratenetworklist){
					 
					 	VirtualNetwork virtual = new VirtualNetwork();
					 
						List<Integer> totalrequests = new ArrayList<Integer>();
						
						List<Integer> acceptedrequests = new ArrayList<Integer>();
						
						List<Double> acceptenceratio = new ArrayList<Double>();
						
						List<Double> nodeutilization = new ArrayList<Double>();
						
						List<Double> linkutilization = new ArrayList<Double>();
						
						List<Double> cost = new ArrayList<Double>();
						
						List<Double> revenue = new ArrayList<Double>();
						
						List<VirtualNetwork> expiredVN = new ArrayList<VirtualNetwork>();
						
						List<VirtualNetwork> unexpiredVN = new ArrayList<VirtualNetwork>();
								
						expiredVN = ExpiredVirtualNetworks.get(substrate);
						
						unexpiredVN = VirtualNetworks.get(substrate);
						
						totalrequests = TotalRequests.get(substrate);
						
						acceptedrequests = AccepetedRequests.get(substrate);
						
						acceptenceratio = AccepetenceRatio.get(substrate);
						
						nodeutilization = NodeUtilization.get(substrate);
						
						linkutilization = LinkUtilization.get(substrate);
						
						cost = Cost.get(substrate);
						
						revenue = Revenue.get(substrate);
						
						result = MappingResult.get(substrate);
						
						virtual = SubstrateVirtualMap.get(substrate);
						
				 int size = totalrequests.size();
				 
				 if(size>0)
				 
				 totalrequests.add(size, totalrequests.get(size-1)+1);
				 
				 else
				 
					 totalrequests.add(size, 1);		
					
					
            if(result == false) {
            	            	
				 int a = acceptedrequests.size();
				 				 
				 if(a>0)
				 
				 acceptedrequests.add(a, acceptedrequests.get(size-1));
				 
				 else 
					 
					 acceptedrequests.add(a, 0);
					 
            	
            	System.err.println("The VN cannot be mapped. . . . .  . . . . . . . . .  . . . . . . . . . . . . .");
            }
                        
            if(result == true){
            	            	
				 int a = acceptedrequests.size();
				 
				 if(a>0)
				 
				 acceptedrequests.add(a, acceptedrequests.get(size-1)+1);
				 
				 else
					 
					 acceptedrequests.add(a, 1);
            	
            	 System.err.println("The Virtual Network "+ virtual.getNetworkID()+" has successfully been Mapped . . . . . ");
            	 
            	 List<VirtualNetwork> virt = VirtualNetworks.get(substrate); virt.add(virtual);
            	 
            	 VirtualNetworks.put(subnet, virt);
            	 
            	 Poisson.startTimer(virtualnetworkDuration, virtual, expiredVN);
            	 
                }
                        
            updateNodeUtilisation(substrate, nodeutilization);
            
            updateLinkUtilisation(substrate, linkutilization);
            
            updateCost(substrate, virtual, cost, unexpiredVN);
            
            updateRevenue(substrate, virtual, revenue, unexpiredVN);
            

           	     Double h = ((double)acceptedrequests.get(acceptedrequests.size()-1)/(double)totalrequests.get(totalrequests.size()-1));
            	    	   
            	   acceptenceratio.add(h);
            	             	     
       		try {
    			
    			filestream = new FileWriter("E:/workspace/VNE-RL/datafiles/Evaluation/Evaluation.dat");
    			
    			BufferedWriter out = new BufferedWriter(this.filestream);
    			
    			out.write("\n\n"+substrate.getNetworkID()+" Total requests:");
               
			for(int c = 0; c<totalrequests.size(); c++){
				
				out.write(","+ totalrequests.get(c));
						
					}   
			
				out.write("\n\n"+substrate.getNetworkID()+" Accepted requests:");
	            
			for(int c = 0; c<acceptedrequests.size(); c++){
				
				out.write(","+ acceptedrequests.get(c));
						
					} 
			
				out.write("\n\n"+substrate.getNetworkID()+" Acceptance ratio:");
		        
			for(int c = 0; c<acceptenceratio.size(); c++){
				
				out.write(","+ acceptenceratio.get(c));
					
					} 
		
				out.write("\n\n"+substrate.getNetworkID()+" Node Utilization:");
			    
			for(int c = 0; c<nodeutilization.size(); c++){
				
				out.write(","+ nodeutilization.get(c));
						
					} 
			
				out.write("\n\n"+substrate.getNetworkID()+" Link Utilisation:");
			
			for(int c = 0; c<linkutilization.size(); c++){
			
				out.write(","+ linkutilization.get(c));
					
					} 
			
				out.write("\n\n"+substrate.getNetworkID()+" Average Revenue:");
				
			for(int c = 0; c<revenue.size(); c++){
			
				out.write(","+ revenue.get(c));
					
					} 
			
			out.write("\n\n"+substrate.getNetworkID()+" expired:");
			
			for(int c = 0; c<ExpiredVirtualNetworks.get(substrate).size(); c++){
			
				out.write(","+ ExpiredVirtualNetworks.get(substrate).get(c).getNetworkID());
					
					} 
		
				out.write("\n\n"+substrate.getNetworkID()+" Unexpired:");
				
			for(int c = 0; c<VirtualNetworks.get(substrate).size(); c++){
			
				out.write(","+ VirtualNetworks.get(substrate).get(c).getNetworkID());
					
					} 
								
				out.write("\n\n"+substrate.getNetworkID()+" Average Cost:");
				
			for(int c = 0; c<cost.size(); c++){
			
				out.write(","+ cost.get(c));

			} 
				
	out.close(); 
			
       		} catch (Exception e) {System.err.println("Error: " + e.getMessage());} 
               
			 
		}
				
				i++;
				
				Thread.sleep((long)arrivals[i]);
				
				 } catch (InterruptedException e) {e.printStackTrace();} catch (CloneNotSupportedException e) {
					
					e.printStackTrace();
				} catch (StaleProxyException e) {
					
					e.printStackTrace();
				}
					 
		   }

	}



		private void experiments() throws StaleProxyException, IloException, CloneNotSupportedException, InterruptedException, IOException {
				
			virtualnetworklist.get(0).printNetwork();
			
			
		//	substratenetworklist.get(0).
			
			 OneShotVNMapping map1 = new OneShotVNMapping(virtualnetworklist.get(0), substratenetworklist.get(0));
			 
			 result = map1.performMapping();
			 			  
			 MappingResult.put(substratenetworklist.get(0), result);
						 
		
	}



		private void createVirtualNetwork(int i) {
					
			 String name = "VN"+i;
			 
			 int NumVNNodes = 3+generator.nextInt(MaxNumVNNodes-3);    //Minimum number of nodes is 3 and max if MaxNumVNNodes
			 
			// System.err.println("VN NUMBERRRRRRRR" + i+"  with "+NumVNNodes+"  nodes has been received . . .  .");
			 
			// int NumVNNodes = 5;
			 
			 virtual = new VirtualNetwork();
			 
			 virtual.createBriteTopology(NumVNNodes, name);
			 
			 virtualnetworkDuration = Poisson.Duration(averagerate);
			 
        	 System.err.println("Duration of Virtual Network is   " + virtualnetworkDuration);
		
			 virtualnetworklist = new ArrayList<VirtualNetwork>();
			 
			 for(int h = 0; h<substratenetworklist.size(); h++){
				 					 
				 VirtualNetwork vir = new VirtualNetwork();
			 
				 vir.setNetworkID(virtual.getNetworkID()+h+"-");
			 
				 List<VirtualNode> nodes  = 	new ArrayList<VirtualNode>();
			 
				 List<VirtualLink> links  = 	new ArrayList<VirtualLink>();
			 
		        for (VirtualLink obj : virtual.getVirtualLinks()) {
					
		        	links.add((VirtualLink) obj.clone());
					
				}
		        
		        for (VirtualNode obj : virtual.getVirtualNodes()) {
					
		        	nodes.add((VirtualNode) obj.clone());
					
				}
		        
		        
		        for(VirtualLink lk : links){
		        	
		        //	lk.setVirtualNetwork(vir);
		        	
		        	for(VirtualNode nd : nodes){
		        		
		        	//	nd.setVirtualNetwork(vir);
		        		
		        		if(lk.getStartNode().getID().equalsIgnoreCase(nd.getID()))
		        			
		        			lk.setStartNode(nd);
		        		
		        		else if(lk.getEndNode().getID().equalsIgnoreCase(nd.getID()))
		        			
		        			lk.setEndNode(nd);
		        			
		        	}
		        	
		        }
		        
		        vir.setVirtualLinks((ArrayList<VirtualLink>) links);
		        
		        vir.setVirtualNodes((ArrayList<VirtualNode>) nodes);
		        
		        nodes = getadjacentnodes(vir.getVirtualNodes(), substratenetworklist.get(h).getSubstrateNodes());

		        virtualnetworklist.add(vir);
			 
		        SubstrateVirtualMap.put(substratenetworklist.get(h), vir);
		 
		 }
/*			 
			 for(VirtualNode vn: virtualnetworklist.get(0).getNodes()){
				 
				 System.out.println(vn+" ID "+vn.getID());
			 }*/
		
	}



		private void deleteExpiredVNS() {
	
			for(Iterator<SubstrateNetwork> Net = ExpiredVirtualNetworks.keySet().iterator(); Net.hasNext();){
				
				SubstrateNetwork substrate = new SubstrateNetwork();
				
				substrate = Net.next();
			
			    List<VirtualNetwork> expiredVN = new ArrayList<VirtualNetwork>();
			    
			    List<VirtualNetwork> unexpiredVN = new ArrayList<VirtualNetwork>();

			    expiredVN =  ExpiredVirtualNetworks.get(substrate);
			    
			    unexpiredVN = VirtualNetworks.get(substrate);
			
			if(expiredVN.size()>0){
				
				for(Iterator<VirtualNetwork> iter = expiredVN.iterator(); iter.hasNext();){
					
					VirtualNetwork network = new VirtualNetwork();
					
					network = iter.next();
					
					unexpiredVN.remove(network);
					
					for(VirtualNode nodes: network.getVirtualNodes()){
						
						for(SubstrateNode nod: substrate.getSubstrateNodes()){
							
							if(nod.getMappedVirtualNodes().contains(nodes)){
								
								nod.getMappedVirtualNodes().remove(nodes);
														
								nod.setAvailableCPU(nod.getAvailableCPU()+nodes.getTotalCPU());
																							
							}
						}
					}
				
					for(VirtualLink links: network.getVirtualLinks()){
						
						for(SubstrateLink lk: substrate.getSubstrateLinks()){
							
							if(lk.getMappedVirtualLinks().contains(links)){
								
								lk.getMappedVirtualLinks().remove(links);
								
								if(substrate.getNetworkID().contains("SN6") || substrate.getNetworkID().contains("SN1") || substrate.getNetworkID().contains("SN3") || substrate.getNetworkID().contains("SN4")){
								
								Double totalflows = 0.0;
								
								for(Iterator<VirtualLink> it = lk.getVirtualLinkFlows().keySet().iterator(); it.hasNext();){
									
									VirtualLink virlink = it.next();
									
									if(virlink == links) {
										
										totalflows += lk.getVirtualLinkFlows().get(virlink);
										
									}
										
								}
								
								lk.getVirtualLinkFlows().remove(links);
								
								lk.setAvailableBandWidth(lk.getAvailableBandwidth()+totalflows);
									
								}
								else if (substrate.getNetworkID().contains("SN0")  || substrate.getNetworkID().contains("SN5")  || substrate.getNetworkID().contains("SN2") 
										
										||substrate.getNetworkID().contains("SN8") || substrate.getNetworkID().contains("SN7")){
	
								lk.setAvailableBandWidth(lk.getAvailableBandwidth()+links.getTotalBandWidth());
								
								}							
							}
						}
					}
								
					System.err.println("The VN  "+network.getNetworkID()+" has been deleted . . . ................................ ");
					
					iter.remove();
											
					}							
				}
					
			}
			
		}


		private void initializeStatistics() {


			for(SubstrateNetwork network: substratenetworklist){
				
				Double totalsubnodecapacity = 0.0;
				
				for(SubstrateNode subnode: network.getSubstrateNodes()){
					
					totalsubnodecapacity += subnode.getTotalCPU();
				
				}
				
				TotalNodeCapacity.put(network, totalsubnodecapacity);
									
				Double totalsublinkcapacity = 0.0;
				
				for(SubstrateLink sublink: network.getSubstrateLinks()){
					
					totalsublinkcapacity += sublink.getTotalBandWidth();
				
				}
				
				TotalLinkCapacity.put(network, totalsublinkcapacity);
				
				
				TotalRequests.put(network, new ArrayList<Integer>());
				
				AccepetedRequests.put(network, new ArrayList<Integer>());
				
				AccepetenceRatio.put(network, new ArrayList<Double>());
				
				NodeUtilization.put(network, new ArrayList<Double>());
				
				LinkUtilization.put(network, new ArrayList<Double>());
				
				Cost.put(network, new ArrayList<Double>());
				
				Revenue.put(network, new ArrayList<Double>());

				ExpiredVirtualNetworks.put(network, new ArrayList<VirtualNetwork>());
				
				VirtualNetworks.put(network, new ArrayList<VirtualNetwork>());
						
			}
			
	
		}

			private List<VirtualNode> getadjacentnodes(List<VirtualNode> virtualnodes, List<SubstrateNode> substratenodes) {
				
				List<SubstrateNode> possiblenodes;
				
				for (int i = 0; i < virtualnodes.size(); i++){
					
					possiblenodes = new ArrayList<SubstrateNode>();
					
					for (int j = 0; j < substratenodes.size(); j++){
						
						if(Math.abs((virtualnodes.get(i).getX_Loc())-(substratenodes.get(j).getX_Loc()))<=virtualnodes.get(i).getX_Dev() && 
								
						   Math.abs((virtualnodes.get(i).getY_Loc())-(substratenodes.get(j).getY_Loc()))<=virtualnodes.get(i).getY_Dev()){
							
							if( (substratenodes.get(j).getAvailableCPU()) >= (virtualnodes.get(i).getTotalCPU()) )
									
							possiblenodes.add(substratenodes.get(j));
						}
						
					}
					
					virtualnodes.get(i).getPossibleSubstrateNodes().clear();
					
					virtualnodes.get(i).setPossibleSubstrateNodes((ArrayList<SubstrateNode>) possiblenodes);			
				}
				
			return virtualnodes;
			
			}
			
			
			
			
			
			
			private void updateLinkUtilisation(SubstrateNetwork substrate, List<Double> linkutilization) {           
			        
			        Double availlinkcapacity = 0.0;
			        
			        Double averagelinkutilization = 0.0;
			        
			        Double totalsublinkcapacity = TotalLinkCapacity.get(substrate);
			        
			        for(SubstrateLink lk: substrate.getSubstrateLinks()){
			        	
			        availlinkcapacity += lk.getAvailableBandwidth();	
			        	 	
			        }
			        
			        averagelinkutilization = ((1 - availlinkcapacity/(totalsublinkcapacity)));
			        
			        linkutilization.add(averagelinkutilization); 
				
			}
			
			private void updateNodeUtilisation(SubstrateNetwork substrate, List<Double> nodeutilization) {
				
				    Double availnodecapacity = 0.0;
			        
			        Double averagenodeutilization = 0.0;
			        
			        Double totalsubnodecapacity = TotalNodeCapacity.get(substrate);
			        
			        for(SubstrateNode nd: substrate.getSubstrateNodes()){
			        	
			        availnodecapacity += nd.getAvailableCPU();	
			        	 	
			        }
			        
			        averagenodeutilization = ((1 - availnodecapacity/(totalsubnodecapacity)));
			        		        
			        nodeutilization.add(averagenodeutilization); 
				
			}
			
			
			private void updateRevenue(SubstrateNetwork substrate, VirtualNetwork virtual, List<Double> revenue, List<VirtualNetwork> unexpiredVN) {

				Double totalnodevalue = 0.0;
				
				Double totallinkvalue = 0.0;
				
				Double totalvalue = 0.0;
				
				Double averagerevenue = 0.0;
				
				for(VirtualNetwork net: unexpiredVN){
				
				for(VirtualNode nd: net.getVirtualNodes()){
					
					totalnodevalue += nd.getTotalCPU();
					
				}
				
			}
				
				
				for(VirtualNetwork net: unexpiredVN){
					
				for(VirtualLink lk: net.getVirtualLinks()){
					
					totallinkvalue += lk.getTotalBandWidth();
					
				}
			}
				
				
				totalvalue = 2*totalnodevalue + totallinkvalue;
				
				
		        for (int m =0; m<revenue.size(); m++){
		        	
		        	averagerevenue += revenue.get(m);
		        	
		        	if(m == (revenue.size()-1) ){
		        		
		        		averagerevenue = (averagerevenue+totalvalue)/(1+revenue.size());
		        		
		        	}
		        	
		        }
				
				revenue.add(averagerevenue);
				
			}

			
			
			private void updateCost(SubstrateNetwork substrate, VirtualNetwork virtual, List<Double> cost, List<VirtualNetwork> unexpiredVN) {
				
				Double totalnodevalue = 0.0;
				
				Double totallinkvalue = 0.0;
				
				Double totalvalue = 0.0;
				
				Double averagecost = 0.0;
				
				Map<VirtualLink, Double> Flows = new LinkedHashMap<VirtualLink, Double>();
				
				for(VirtualNetwork net: unexpiredVN){
										
				for(VirtualNode nd: net.getVirtualNodes()){
					
					totalnodevalue += nd.getTotalCPU();
					
				}
				
			}
				
				for(SubstrateLink sublk: substrate.getSubstrateLinks()){
					
					Flows = new LinkedHashMap<VirtualLink, Double>();
					
					Flows = sublk.getVirtualLinkFlows();
					
					for(VirtualNetwork net: unexpiredVN){
					
						for (VirtualLink virlk: net.getVirtualLinks()){
						
							if(Flows.containsKey(virlk))
						
								totallinkvalue += Flows.get(virlk);
					}
					
				}
					
			}
				
				totalvalue = 2*totalnodevalue + totallinkvalue;
				
		        for (int m =0; m<cost.size(); m++){
		        	
		        	averagecost += cost.get(m);
		        	
		        	if(m == (cost.size()-1) ){
		        		
		        		averagecost = (averagecost+totalvalue)/(1+cost.size());
		        		
		        	}
		        	
		        }
				
				cost.add(averagecost);
				
	
			}
			
		
			
}
