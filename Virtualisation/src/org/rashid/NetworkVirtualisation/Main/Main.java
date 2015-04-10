package org.rashid.NetworkVirtualisation.Main;

import ilog.concert.IloException;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.util.ArrayList;

import org.rashid.NetworkVirtualisation.Network.Networks.SubstrateNetwork;
import org.rashid.NetworkVirtualisation.Utilities.VirtualisationVocabulary;

public class Main implements VirtualisationVocabulary{
	   
   public static void main(String[] args) throws IOException, InterruptedException, StaleProxyException, IloException, CloneNotSupportedException {
	
	 SubstrateNetwork s = new SubstrateNetwork();
	 
	 s.createBriteTopology(25, "substratenetwork");
	 
	 ArrayList<SubstrateNetwork> list = s.copySubstrateNetwork(1);
	 	 
	 VNRequests req = new VNRequests();
	 	 
	 req.sendRequest(list, s);
	 
   	 
   }

}
