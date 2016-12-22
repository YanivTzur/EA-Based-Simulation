package ex12Q2;

/**
 * The program creates M men, M women and M dogs each with a list of length M of preferences
 * and attempts to produce the best possible matching between the men, women and dogs
 * using an evolutionary algorithm.
 * @author Yaniv Tzur
 */
public class EASimulationApplication 
{	
	private EASimulationGraphicalView _view; // The display of the EA simulation application.
	private EASimulationController _controller; // Controls the program's logic.
	private EASimulationModel _model; // Controls access to the program's internal state
									  // and stored data.
	
	/**
	 * Creates a new instance of the evolutionary algorithms (EA) based three-way matching simulation
	 * program.
	 */
	public EASimulationApplication()
	{
		_view = new EASimulationGraphicalView();
		_controller = new EASimulationController();
		_model = new EASimulationModel();
		_controller.setView(_view);
		_controller.setModel(_model);
		_view.setController(_controller);
	}
	
	/**
	 * Starts the display of the application.
	 */
	public void start()
	{
		_view.showView();
	}
}
