package org.rashid.NetworkVirtualisation.Main;

import ilog.concert.IloException;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;

import org.rashid.NetworkVirtualisation.Network.Links.SubstrateLink;
import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Network.Nodes.SubstrateNode;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

public class Main implements VirtualisationVocabulary{
	   
   public static void main(String[] args) throws IOException, InterruptedException, StaleProxyException, IloException, CloneNotSupportedException {
	
	 SubstrateNetwork s = new SubstrateNetwork();
	 
	 s.createBriteTopology(25, "substratenetwork");
	 
	 ArrayList<SubstrateNetwork> list = s.copySubstrateNetwork(1);
	 
	 //add node and link pricing
	 SubstrateNetwork dynamicallyPricedSubstrateNetwork = list.get(0);
	 ArrayList<SubstrateNode> nodes = dynamicallyPricedSubstrateNetwork.getNodes();
	 ArrayList<SubstrateLink> links = dynamicallyPricedSubstrateNetwork.getLinks();
	 for (int n = 0; n < nodes.size(); n++){
		 nodes.get(n).setNodeCost(100.0);
	 }
	 for (int l = 0; l < links.size(); l++){
		 links.get(l).setLinkCost(100.0);
	 }
	 	 
	 VNRequests req = new VNRequests();
	 	 
	 req.sendRequest(list, s);
	 
   	 
   }

}
