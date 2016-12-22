package ex12Q2;

/**
 * Used to start the evolutionary algorithm simulation program.
 * The program creates M men, M women and M dogs each with a list of length M of preferences
 * and attempts to produce the best possible matching between the men, women and dogs
 * using an evolutionary algorithm.
 * @author Yaniv Tzur
 */
public class Main 
{
	public static void main(String[] args) 
	{
		EASimulationApplication app = new EASimulationApplication();
		app.start();
	}
}
