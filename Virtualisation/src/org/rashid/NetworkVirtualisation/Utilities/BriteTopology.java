
/*  This file is a slight modification of the Brite.java file in the Brite Topology generation tool*/

package org.rashid.NetworkVirtualisation.Utilities;

import java.io.File;

import Brite.Export.BriteExport;
import Brite.Graph.Graph;
import Brite.Model.BriteModel;
import Brite.Topology.Topology;
import Brite.Util.RandomGenManager;
import Brite.Util.Util;

final public class BriteTopology implements VirtualisationVocabulary{
	
	BriteExport topology;
    
    public BriteExport createTopology(BriteModel m) {

    	
     // String filename="C:/workspace/NetworkVirtualisation/src/Brite/Files/RTWaxman.conf";
        
       String outFile = workspace.concat("MultiAgentSurvivability/src/Brite/Files/OUTPUT");
        
        String seedFile= workspace.concat("MultiAgentSurvivability/src/Brite/Files/seed_file");
        
   //     String seedFile= "/home/rashid/MultiAgentSurvivability/src/Brite/Files/seed_file";
        
 //       String outFile= "/home/rashid/MultiAgentSurvivability/src/Brite/Files/OUTPUT";
      
            
      RandomGenManager rgm = new RandomGenManager();
      
      rgm.parse(seedFile);

      	      
      /*create our glorious model and give it a random gen manager*/
   //   BriteModel m = ParseConfFile.Parse(filename);
      
      m.setRandomGenManager(rgm);
      
      /*now create our wonderful topology. ie call model.generate()*/
      Topology t = new Topology(m);
      
      /*check if our wonderful topology is connected*/
      Util.MSGN("Checking for connectivity:");
      
      Graph g = t.getGraph();
      
      boolean isConnected = (g.isConnected());
      
      if (isConnected)
    	  
	   System.out.println("\tConnected");
      
      else System.out.println("\t***NOT*** Connected");
      
      
      /*export to brite format outfile*/
   //   HashSet exportFormats = ParseConfFile.ParseExportFormats();
   //   ParseConfFile.close();
   //   if (exportFormats.contains("BRITE")) {
	//  Util.MSG("Exporting Topology in BRITE format to: " + outFile+".brite");
	  topology = new BriteExport(t, new File(outFile+".brite"));
	//  topology.export();
  //    }

      
      /*outputting seed file*/
      Util.MSG("Exporting random number seeds to seedfile");
      rgm.export("last_seed_file", seedFile);
	
      
      /*we're done (and hopefully successfully)*/
      Util.MSG("Topology Generation Complete.");
      
	return topology;

      //t.dumpToOutput();

  }
  
  
}
