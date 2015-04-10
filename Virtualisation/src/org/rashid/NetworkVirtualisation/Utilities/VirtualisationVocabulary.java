package org.rashid.NetworkVirtualisation.Utilities;

import java.util.Random;


/**
 * 
 * This Class contains the definitions of the constants that are used by most classes
 *  
 * @author Rashid Mijumbi
 * 
 * @since  January 2014
 * 
 */


public interface VirtualisationVocabulary {
	
	/******************************************************************************************
	 * General Parameters
	 ******************************************************************************************/
	
	/**
	 * URI of Workspace
	 */
	//uncomment for Mario
	//String workspace = "E:/workspace/";
	//uncomment for Bram
	String workspace =  "C:/Users/bnaudts/git/vne/";
    /**
     * 1 VN every 3 seconds
     */
			
	public static final double averagewaitingtime = 10.0;			//3
	
	/**
	 * Mean service time of 60 seconds
	 */
	public static final double servicetime = 150.0;				//60

	/**
	 * Maximum number of nodes for virtual networks
	 */
	public static final int MaxNumVNNodes = 10;
	
	/**
	 * Time period between advertising of node locations and costs by each agent.
	 */
	
	public static final long shareperiod_min = 60000;
	
	public static final long shareperiod_max = 300000;

	/**
	 * For generation of random values
	 */
    public static final Random generator = new Random();
    
    /**
     * Parameter used in mathematical formulations for extent of load balancing
     */

	public static final double loadbalance = 1.0;
	
	/**
	 * Full decsription of agents
	 */
		
	public static final String AgentFullName = "@147.83.106.107:11099/JADE";
	
	
	
	/*******************************************************************************************
	 * Controller, Substrate and Virtual Network Creation Parameters
	 ********************************************************************************************/
	
	/**
	 * Controller network Grid - Length
	 */
    public static final int XSize = 5000;
    
	/**
	 * Controller network Grid - Height
	 */	
    public static final int YSize = 10000;
	
	/**
	 * Controller network Grid - Min Length
	 */
    public static final int MinLength = 300;
    
	/**
	 * Controller network Grid - Min Height
	 */
	
    public static final int MinHeight = 500;
        
	/**
	 * Controller network Grid - Max Length
	 */
    public static final int MaxLength = 600;
    
	/**
	 * Controller network Grid - Max Height
	 */
	
    public static final int MaxHeight = 1000;  
    
		
	/**
	 * Lower bound of Sub Node Capacities
	 */
	public static final double SubNodeLowerCPU = 100.0;			//150
	
	/**
	 * Lower bound of Vir Node Demands
	 */
	public static final double VirNodeLowerCPU = 50.0;  		//50
	
	/**
	 * Upper bound of Sub Node Capacities
	 */
	public static final double SubNodeUpperCPU  = 200.0;		//250
	
	/**
	 * Upper bound of Vir Node Demands
	 */
	public static final double VirNodeUpperCPU = 100.0;			//100
	
	/**
	 * Lower bound of Inter-domain Link Capacities
	 */
	public static final double IDLinkLowerBandWidth = 500.0;
	
	/**
	 * Upper bound of Inter-domain Links Capacities
	 */
	public static final double IDLinkUpperBandWidth = 1000.0;
	
	/**
	 * Lower bound of Sub Link Capacities
	 */
	public static final double SubLinkLowerBandWidth = 200.0;
	
	/**
	 * Lower bound of Vir Link Demands
	 */
	public static final double VirLinkLowerBandWidth = 40.0;		//100
	
	/**
	 * Upper bound of Sub Link Capacities
	 */
	public static final double SubLinkUpperBandWidth = 500.0;
	
	/**
	 * Upper bound of Vir Link Demands
	 */
	public static final double VirLinkUpperBandWidth = 100.0;			//200
       
	/**
	 * Maximum deviation in X direction of Virtual Node in Mapping
	 */
    public static final Double DeviationX = 15.0;
    
    /**
     * Maximum deviation in Y direction of Virtual Node in Mapping
     */
    public static final Double DeviationY = 15.0;
    

    
    
    /**
     * Minimum cost of Substrate Network nodes
     */
    public static final double MinSNCost = 50.0;
    
    /**
     * Maximum cost of Sbbstarte Network nodes
     */
    public static final double MaxSNCost= 200.0;
    
    
    /**
     * Maximum number of Substrate Network nodes
     */
    public static final int MaxSNNodes = 60;		//150    
    
    /**
     * Minimum number of Substrate Network nodes
     */
    public static final int MinSNNodes = 30;		//120
    
    /**
     * Maximum number of Virtual nodes
     */
    public static final int MaxVNNodes = 20;		//40
    
    /**
     * Maximum number of Virtual nodes
     */
    public static final int MinVNNodes = 10;		//20
        
    /**
     * Probability of having an inter-domain link between any two given substrate networks
     */
    public static final double probability = 0.02;
    
    
    /**************************************************************************************************************************
     * Brite Parameters
     * 
     * Alberto Medina, Anukool Lakhina, Ibrahim Matta, John Byers, BRITE: An Approach to Universal Topology Generation, 
     * Proceedings of the 9th International Symposium in Modeling, Analysis and Simulation of Computer and Telecommunication
     * Systems, Page 346, IEEE Computer Society Washington, DC, USA, 2001.
     ***************************************************************************************************************************/
	
    //public static final int HS = 500;		//Varied due to varying network sizes - see network parameters	
	
    //public static final int LS = 500;
	
    public static final int NodePlacement = 1;
	
    public static final int NumberNodeNeighbours = 3;
	
    public static final float alpha = (float) 0.15;
	
    public static final float beta = (float) 0.20;
	
    public static final int growthtype = 1;
	
    public static final int bwDistribution = 2;   //Uniform Distribution
	  
    
    //public static final String Path = "D:/workspace/NetworkVirtualisation/DataFiles/";
    
    /**
     * Network definitions based on definitions in PolyViNE. Using Network Mean and Variance to determine network size and 
     * hence node locations.
     * 
     * We consider that we have 256 major cities. We represent these cities on a square grid of 16x16 such that each square has one city.
     * 
     * |________________________________________________________________|
     * |1	|2	|3	|4	|5	|6	|7	|8	|9	|10	|11	|12	|13	|14	|15	|16	|
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|
	 * |17	|18	|	|	|	|	|	|	|	|	|	|	|	|	|	|32	|
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|
     * |	|	|	|	|	|	|	|	|	|	|	|	|	|	|	|	|.
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|.
     * |	|	|	|	|	|	|	|	|	|	|	|	|	|	|	|	|.
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|.
     * |225	|226|	|	|	|	|	|	|	|	|	|	|	|	|239|240|
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|
     * |241	|242|243|244|245|246|247|248|249|250|251|252|253|254|255|256|
     * |____|___|___|___|___|___|___|___|___|___|___|___|___|___|___|___|
     * |																|
     * 
     * As the distribution of network nodes follows a normal distribution, we can have nodes on either side of the mean location, 
     * implying that in the worst case, we will need to have nodes located a maximum variance from the mean on each side. So our ultimate
     * grid size should be 3 \times (16x16)  = 48x48, and so the mean value is always chosen from the middle grid 16-32 units to allow
     * for nodes on either side of the mean value 
     */
    
    public static final int numCities = 256;
    
    public static final int totalgrid = 16;
    
    public static final int halfgrid = 8;
    
    public static final int varianceLower = 1;
    
    public static final int varianceUpper = halfgrid;
    
    /**
     * Maximum number of InPs to which mapping proposals can be sent
     */
    
    public static final int numInPs = 75;
    
    public static final int initialSetNum = 6;
    
    public static final double minBudget = 20000;
    
    public static final double maxBudget = 50000;
    
    public static final double minProcessingRatio = 0.25;
    
    public static final double maxProcessingRatio = 0.35;
    
    public static final int citydeviation = 5;
    
    public static final long timeout = 20;
    
    /**
     * Weibull Distribution for MTBF and MTTR: Both values are taken as the mean of the distribution!
     * 
     * and are calculated as: alpha \times gammafunction (1 + 1/beta)
     * 
     * ref: http://www.real-statistics.com/other-key-distributions/weibull-distribution/
     * 
     * http://www.ee.ucl.ac.uk/~mflanaga/java/PsRandom.html
     * 
     * 1/alpha == failure rate
     * 
     */
    
    public static final double weibull_alpha_mtbf = 0.1;
    
    public static final double weibull_beta_mtbf = 0.5;
    
    public static final double weibull_alpha_mttr = 0.3;
    
    public static final double weibull_beta_mttr = 0.5;
    
    public static final int num_failures = 100;
    
    
    /**
     * Pricing of Virtual Links between VNOs and VNPs
     */
    
    public static final double Y_min = 10.0;
    
    public static final double Y_max = 50.0;
    
    public static final double Z_min = 5.0;
    
    public static final double Z_max = 20.0;
    
    
    
    /**
     * VNP Proposal evaluation parameters
     */
    
    public static final double alpha1 = 0.00001;
    
    public static final double beta1 = 0.1;
    
    public static final double gamma1 = 1;
    
    public static final double eta = 0.0001;
    	
}
