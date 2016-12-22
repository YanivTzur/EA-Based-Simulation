package ex12Q2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Timer;

/**
 * Controls the internal logic of the EA based three-way matching simulation program.
 * @author Yaniv Tzur
 */
public class EASimulationController implements ActionListener
{	
	/**
	 * Whether preference lists should be randomly generated only once for a series of runs
	 * or from scratch for each run in a series of runs.
	 * @author Yaniv Tzur
	 */
	public enum PreferenceListGenerationMode
	{
		ONE_TIME, // Generate preference lists once at the beginning of a series of runs.
		EACH_RUN // Generate preference lists from scratch on the start of each run.
	}

	public static final double MUTATION_THRESHOLD = 0.95; // The threshold a randomly generated number
														  // in the interval [0,1] must pass in
														  // order for a mutation to occur.
	public static final int DEFAULT_NUM_OF_RUNS = 1; // Default number of consecutive runs to carry
													  // out when the simulation has been started.
	public static final int DEFAULT_TIME_BTWN_GENERATIONS = 1000; // Default time between generations
																  // in milliseconds.
	public static final int DEFAULT_TOTAL_RUN_TIME = 180000; // Default total run time in milliseconds.
	private static final int MIN_TIME_BETWEEN_GENERATIONS = 50; // The minimum time between generations
																// milliseconds a user can set.
	
	private EASimulationGraphicalView _view; // The display of the program.
	private EASimulationModel _model; // Controls the internal state of the program and access to it.
	private Timer _nextGenerationTimer; // Controls the waiting time between each pair of consecutive
										// generations.
	private PreferenceListGenerationMode _preferenceListGenerationMode; // The mode used for generating
																		// preference lists.
	private boolean _isCrossoverEnabled; // Whether crossover is applied when creating offspring.
	private int _numOfDesiredRuns; // The number of runs in the last executed sequence.
	private int _currentRunNumber; // The serial number of the current run (starts from 0).
	private int _currentElapsedTotalRunTime; // The time elapsed so far since the beginning of the
											 // current run.
	private int _timeBetweenGenerations; // The time between generations in milliseconds a user has
										 // set.
	
	/**
	 * Associates the input view with the controller object.
	 * @param view The view to associate with this controller object.
	 */
	public void setView(EASimulationGraphicalView view)
	{
		_view = view;
	}
	
	/***
	 * Associates the input model with the controller object.
	 * @param model The model to associate with this controller object.
	 */
	public void setModel(EASimulationModel model)
	{
		_model = model;
	}
	
	/**
	 * Creates an initial display for the program.
	 */
	public void initializeView()
	{
		if (_view != null && _model != null)
			_view.updateView(true,
							 _model.getBestFitness(),
							 _model.getAverageFitness(),
							 _model.getWorstFitness(),
							 0,
							 getRunNumber(),
							 _model.getCurrentGenerationNumber(),
							 _model.getNumOfMen(),
							 _model.getNumOfWomen(),
							 _model.getNumOfDogs());
	}

	/**
	 * Starts a new sequence of runs from scratch.
	 * @param preferenceListGenerationMode Whether to generate preference lists for each individual
	 * 									   once in the beginning of the sequence of runs or at the
	 * 									   start of each run.
	 * @param isCrossoverEnabled Whether crossover should be applied when creating offspring.
	 * @param numOfRuns The number of runs to execute in the current sequence.
	 * @param timeBetweenGenerations The time to wait between the generation of each pair of
	 * 								 consecutive generations.
	 */
	public void startRun(PreferenceListGenerationMode preferenceListGenerationMode, 
						 boolean isCrossoverEnabled, int numOfRuns, int timeBetweenGenerations) 
	{
		if (_nextGenerationTimer == null || _nextGenerationTimer.isRunning() == false)
		{
			setPreferenceListGenerationMode(preferenceListGenerationMode);
			setCrossoverEnabledMode(isCrossoverEnabled);
			setNumOfDesiredRuns(numOfRuns);
			setCurrentRunNumber(0);
			_model.setGenerationNumber(0);
			_view.clearResultsSection();
			_currentElapsedTotalRunTime = 0;
			startTimer(timeBetweenGenerations);
		}
	}

	/**
	 * Continues a sequence of runs that has been paused from the point at which it was paused.
	 * @param preferenceListGenerationMode Whether to generate preference lists for each individual
	 * 									   once in the beginning of the sequence of runs or at the
	 * 									   start of each run.  
	 * @param isCrossoverEnabled Whether crossover should be applied when creating offspring.
	 * @param timeBetweenGenerations The time to wait between the generation of each pair of
	 * 								 consecutive generations.
	 */
	public void continueRun(PreferenceListGenerationMode preferenceListGenerationMode,
							boolean isCrossoverEnabled, int timeBetweenGenerations) 
	{
		setPreferenceListGenerationMode(preferenceListGenerationMode);
		setCrossoverEnabledMode(isCrossoverEnabled);
		if (_nextGenerationTimer == null || _nextGenerationTimer.isRunning() == false)
			startTimer(timeBetweenGenerations);
	}

	/**
	 * Starts the timer controlling the rate at which to move forward to the next generation
	 * and at which to update the screen accordingly.
	 * @param timeBetweenGenerations The time in milliseconds to wait between the generation of
	 * 								 the current generation to that of the next.
	 */
	private void startTimer(int timeBetweenGenerations)
	{
		setTimeBetweenGenerations(timeBetweenGenerations);
		_nextGenerationTimer = new Timer(timeBetweenGenerations, this);
		_nextGenerationTimer.start();
	}
	
	/**
	 * Saves the input value as the time to wait between the generation of each pair of
	 * consecutive generations.
	 * @param timeBetweenGenerations The new time in milliseconds to wait between the generation
	 * 								 of each pair of consecutive generations.
	 */
	private void setTimeBetweenGenerations(int timeBetweenGenerations)
	{
		if (timeBetweenGenerations >= MIN_TIME_BETWEEN_GENERATIONS)
			_timeBetweenGenerations = timeBetweenGenerations;
		else
			timeBetweenGenerations = DEFAULT_TIME_BTWN_GENERATIONS;
	}
	
	/**
	 * Returns the currently set preference list generation mode.
	 * @return The currently set preference list generation mode.
	 */
	public PreferenceListGenerationMode getPreferenceListGenerationMode()
	{
		return _preferenceListGenerationMode;
	}
	
	/**
	 * Sets the preference list generation mode to the input mode.
	 * @param preferenceListGenerationMode The preference list generation mode to use.
	 */
	private void setPreferenceListGenerationMode(PreferenceListGenerationMode preferenceListGenerationMode)
	{
		_preferenceListGenerationMode = preferenceListGenerationMode;
	}
	
	/**
	 * Sets whether crossover is applied when creating offspring in the creation of the next
	 * generation.
	 * @param isCrossoverEnabled Whether crossover is applied when creating offspring.
	 */
	private void setCrossoverEnabledMode(boolean isCrossoverEnabled)
	{
		_isCrossoverEnabled = isCrossoverEnabled;
	}
	
	/**
	 * Sets the number of desired runs in the next sequence of runs.
	 * @param numOfRuns The number of runs to include in the next executed sequence.
	 */
	private void setNumOfDesiredRuns(int numOfRuns)
	{
		_numOfDesiredRuns = numOfRuns;
	}
	
	/**
	 * Returns the number of the currently executed run in the current sequence.
	 * @return The number of the currently executed run in the current sequence.
	 */
	private int getCurrentRunNumber()
	{
		return _currentRunNumber;
	}
	
	/**
	 * Sets the serial number of the current run in the current sequence to the input number.
	 * @param runNumber The number to set as the ordinal number of the current run in the current
	 * 					sequence.
	 */
	private void setCurrentRunNumber(int runNumber)
	{
		_currentRunNumber = runNumber;
	}
	
	/**
	 * Stops the generation of subsequent generations.
	 */
	public void stopRun() 
	{
		if(_nextGenerationTimer != null && _nextGenerationTimer.isRunning() == true)
			_nextGenerationTimer.stop();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == _nextGenerationTimer)
		{
			if(_currentRunNumber >= _numOfDesiredRuns)
			{
				stopRun();
				setNumOfDesiredRuns(0);
				setCurrentRunNumber(0);
			}
			else
			{
				if (_model.getCurrentGenerationNumber() == 0) // New run, initialize new model.
					// Each individual uses the same preference lists in all runs in a given sequence.
					if (getPreferenceListGenerationMode() == PreferenceListGenerationMode.EACH_RUN)
						_model.initializeChromosomes(true); // 
					else // Each individual uses a different preference list in each different run.
					{
						if (getCurrentRunNumber() == 0)
							_model.initializeChromosomes(true);
						else _model.initializeChromosomes(false);
					}
				else
				{
					_model.setNewGeneration(calculateNextGeneration(_model.getChromosomes()));
				}
				_view.updateView(false,
								 _model.getBestFitness(),
								 _model.getAverageFitness(),
								 _model.getWorstFitness(),
								 (double)_currentElapsedTotalRunTime / 1000,
								 getRunNumber(),
								 _model.getCurrentGenerationNumber(),
								 _model.getNumOfMen(),
								 _model.getNumOfWomen(),
								 _model.getNumOfDogs());
			}
			_model.incrementGeneration();
			_currentElapsedTotalRunTime += _timeBetweenGenerations;
			if (_currentElapsedTotalRunTime > DEFAULT_TOTAL_RUN_TIME)
			{
				_currentRunNumber++;
				_model.setGenerationNumber(0);
				_currentElapsedTotalRunTime = 0;
			}
		}
	}
	
	/**
	 * Returns the number of the current run (starts from 0).
	 * @return The number of the current run.
	 */
	public int getRunNumber()
	{
		return _currentRunNumber;
	}
	
	/**
	 * Calculates the next generation of solutions and returns the newly generated population.
	 * @param currentPopulation The current population of solutions (chromosomes).
	 * @return The new generation of solutions (chromosomes).
	 */
	private Chromosome[] calculateNextGeneration(Chromosome[] currentPopulation)
	{
		ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
		Chromosome parent1 = null;
		Chromosome parent2 = null;
		Chromosome offspring[] = null; // Expected to be an array of size 2.
		
		while(newPopulation.size() < currentPopulation.length)
		{
			parent1 = selectParent(currentPopulation);
			parent2 = selectParent(parent1, currentPopulation);
			if (_isCrossoverEnabled == true)
				offspring = crossover(parent1, parent2);
			else
				offspring = new Chromosome[] {parent1, parent2};
			offspring = tryMutate(offspring, _model.getMen(), _model.getWomen(), _model.getDogs(),
								  newPopulation);
			newPopulation.addAll(Arrays.asList(offspring));
		}
		
		return newPopulation.toArray(new Chromosome[newPopulation.size()]);
	}

	/**
	 * Selects a parent for creating offspring and returns it.
	 * @param currentPopulation The population of solutions in the current generation.
	 * @return The parent selected.
	 */
	private Chromosome selectParent(Chromosome[] currentPopulation)
	{
		return selectParent(null, currentPopulation);
	}
	
	/**
	 * Selects a parent for creating offspring and returns it.
	 * @param alreadySelectedParent A parent that has already been selected. Passed as an argument
	 * 								to avoid selecting the same parent twice.
	 * @param currentPopulation The population of solutions in the current generation.
	 * @return The parent selected.
	 */
	private Chromosome selectParent(Chromosome alreadySelectedParent, Chromosome[] currentPopulation)
	{
		ArrayList<Chromosome> availableChromosomes = new ArrayList<Chromosome>(Arrays.asList(currentPopulation));
		int totalFitness = 0;
		double wheelLocation = 0;
		int currentSum = 0;
		int populationSize = 0;
		int i;
		
		if (alreadySelectedParent != null && availableChromosomes.contains(alreadySelectedParent))
			availableChromosomes.remove(alreadySelectedParent);
		populationSize = availableChromosomes.size();
		totalFitness = sumFitness(availableChromosomes, currentPopulation.length);
		wheelLocation = RNGUtilities.generateRandomDouble() * totalFitness;
		for(i = 0; currentSum < wheelLocation && i < populationSize; i++)
			currentSum += availableChromosomes.get(i).getFitness(currentPopulation.length);
		if(i >= populationSize)
			return availableChromosomes.get(i-1);
		return availableChromosomes.get(i);
	}
	
	/**
	 * Sums the fitness of all selectable chromosomes (all chromosomes not already selected as parents)
	 * and returns the obtained sum.
	 * @param selectableChromosomes All chromosomes not already selected as parents.
	 * @param totalPopulationSize The number of all chromosomes in the population in the current
	 * 							  generation.
	 * @return The sum of the fitness values of all selectable chromosomes.
	 */
	private int sumFitness(ArrayList<Chromosome> selectableChromosomes, int totalPopulationSize)
	{
		int totalFitness = 0;
		if (selectableChromosomes != null)
			for (int i = 0; i < selectableChromosomes.size(); i++)
				totalFitness += selectableChromosomes.get(i).getFitness(totalPopulationSize);
		return totalFitness;
	}
	
	/**
	 * Performs a crossover on the two selected parents' genes, creating two offspring.
	 * The first of the offspring has the man component (gene) of the first parent and the rest
	 * of its components (genes) from the second parent.
	 * The second of the offspring has the man component (gene) of the second parent and the
	 * rest of its components (genes) from the first parent.
	 * @param parent1 The first selected parent.
	 * @param parent2 The second selected parent.
	 * @return The produced offspring.
	 */
	private Chromosome[] crossover(Chromosome parent1, Chromosome parent2) 
	{
		Man offspring1Man = null;
		Woman offspring1Woman = null;
		Dog offspring1Dog = null;
		Man offspring2Man = null;
		Woman offspring2Woman = null;
		Dog offspring2Dog = null;
		Chromosome offspring1 = null;
		Chromosome offspring2 = null;
		
		if (parent1 != null && parent2 != null)
		{
			offspring1Man = parent1.getMan();
			offspring1Woman = parent2.getWoman();
			offspring1Dog = parent2.getDog();
			
			offspring2Man = parent2.getMan();
			offspring2Woman = parent1.getWoman();
			offspring2Dog = parent1.getDog();
			
			offspring1 = new Chromosome(offspring1Man, offspring1Woman, offspring1Dog);
			offspring2 = new Chromosome(offspring2Man, offspring2Woman, offspring2Dog);
		}
		
		return new Chromosome[] {offspring1, offspring2};
	}
	
	/**
	 * Generates a random number for each gene of each chromosome (solution) to check if it
	 * should be mutated. If the random number passes the defined threshold, the gene is mutated,
	 * that is, it is replaced with a different gene of the same type (e.g. one man gene with a different
	 * man gene).
	 * @param offspring The two offspring currently calculated in order to be added to the population
	 * 					of offspring, before mutation.
	 * @param men All men (man genes) in the solution space.
	 * @param women All woman (woman genes) in the solution space.
	 * @param dogs All dogs (dog genes) in the solution space.
	 * @param newPopulation The complete offspring population constructed so far.
	 * @return The resulting two offspring, after mutation has been applied or not.
	 */
	private Chromosome[] tryMutate(Chromosome[] offspring, Man[] men, Woman[] women, Dog[] dogs,
								   ArrayList<Chromosome> newPopulation) 
	{
		double randomlyGeneratedNumber = 0; // A randomly generated number in the interval [0,1].
		Chromosome[] newOffspring = new Chromosome[offspring.length];
		
		for (int i = 0; i < offspring.length; i++)
		{
			newOffspring[i] = offspring[i];
			for(int j = 0; j < Chromosome.NUM_OF_GENES; j++)
			{
				randomlyGeneratedNumber = RNGUtilities.generateRandomDouble();
				if (randomlyGeneratedNumber >= MUTATION_THRESHOLD)
					if(j == 0)
						newOffspring[i] = mutate(newOffspring[i], men, newPopulation);
					else if(j == 1)
						newOffspring[i] = mutate(newOffspring[i], women, newPopulation);
					else if (j == 2)
						newOffspring[i] = mutate(newOffspring[i], dogs, newPopulation);
			}
		}

		return newOffspring;
	}
	
	/**
	 * Receives an offspring before mutation and mutates its man component (gene) by replacing
	 * it with a different man component.
	 * @param currOffspring An offspring to mutate.
	 * @param men All men genes in the solution space.
	 * @param newPopulation The complete offspring population constructed so far.
	 * @return The input offspring chromosome after it has been mutated.
	 */
	private Chromosome mutate(Chromosome currOffspring, Man[] men, ArrayList<Chromosome> newPopulation)
	{
		Man[] availableMen = getAvailableMen(men, newPopulation);
		Chromosome newOffspring = currOffspring;
		if (currOffspring != null && availableMen.length > 0)
			newOffspring.setMan(availableMen[RNGUtilities.generateRandomInteger(0, availableMen.length-1)],
								newOffspring.getWoman(),
								newOffspring.getDog());
		return newOffspring;
	}

	/**
	 * Receives an offspring before mutation and mutates its woman component (gene) by replacing
	 * it with a different woman component.
	 * @param currOffspring An offspring to mutate.
	 * @param women All women genes in the solution space.
	 * @param newPopulation The complete offspring population constructed so far.
	 * @return The input offspring chromosome after it has been mutated.
	 */
	private Chromosome mutate(Chromosome currOffspring, Woman[] women, ArrayList<Chromosome> newPopulation)
	{
		Woman[] availableWomen = getAvailableWomen(women, newPopulation);
		Chromosome newOffspring = currOffspring;
		if (currOffspring != null && availableWomen.length > 0)
			newOffspring.setWoman(availableWomen[RNGUtilities.generateRandomInteger(0, availableWomen.length-1)],
								  newOffspring.getMan(),
								  newOffspring.getDog());
		return newOffspring;
	}

	/**
	 * Receives an offspring before mutation and mutates its dog component (gene) by replacing
	 * it with a different dog component.
	 * @param currOffspring An offspring to mutate.
	 * @param dogs All dog genes in the solution space.
	 * @param newPopulation The complete offspring population constructed so far.
	 * @return The input offspring chromosome after it has been mutated.
	 */
	private Chromosome mutate(Chromosome currOffspring, Dog[] dogs, ArrayList<Chromosome> newPopulation)
	{
		Dog[] availableDogs = getAvailableDogs(dogs, newPopulation);
		Chromosome newOffspring = currOffspring;
		if (currOffspring != null && availableDogs.length > 0)
			newOffspring.setDog(availableDogs[RNGUtilities.generateRandomInteger(0, availableDogs.length-1)],
								newOffspring.getMan(),
								newOffspring.getWoman());
		return newOffspring;
	}

	/**
	 * Returns all man genes that haven't been assigned yet to any offspring that is already a part
	 * of the offspring population constructed so far.
	 * @param men All man genes in the solution space.
	 * @param newPopulation The offspring population constructed so far in the calculation of the
	 * 						next generation.
	 * @return All man genes that haven't been assigned yet to any offspring that is already a part
	 * 		   of the offspring population constructed so far.
	 */
	private Man[] getAvailableMen(Man[] men, ArrayList<Chromosome> newPopulation) 
	{
		ArrayList<Man> availableMen = new ArrayList<Man>();
		Man manComponent = null;
		boolean available = true;
		for(Man man : men)
		{
			available = true;
			for(Chromosome chromosome : newPopulation)
			{
				manComponent = chromosome.getMan();
				if (manComponent == man) // Man component already taken by an existing chromosome.
				{
					available = false;
					break;
				}
			}
			if(available == true)
				availableMen.add(man);
		}
		return availableMen.toArray(new Man[availableMen.size()]);
	}
	
	/**
	 * Returns all woman genes that haven't been assigned yet to any offspring that is already a part
	 * of the offspring population constructed so far.
	 * @param women All woman genes in the solution space.
	 * @param newPopulation The offspring population constructed so far in the calculation of the
	 * 						next generation.
	 * @return All woman genes that haven't been assigned yet to any offspring that is already a part
	 * 		   of the offspring population constructed so far.
	 */
	private Woman[] getAvailableWomen(Woman[] women, ArrayList<Chromosome> newPopulation) 
	{
		ArrayList<Woman> availableWomen = new ArrayList<Woman>();
		Woman womanComponent = null;
		boolean available = true;
		for(Woman woman : women)
		{
			available = true;
			for(Chromosome chromosome : newPopulation)
			{
				womanComponent = chromosome.getWoman();
				if (womanComponent == woman) // Man component already taken by an existing chromosome.
				{
					available = false;
					break;
				}
			}
			if(available == true)
				availableWomen.add(woman);
		}
		return availableWomen.toArray(new Woman[availableWomen.size()]);
	}
	
	/**
	 * Returns all dog genes that haven't been assigned yet to any offspring that is already a part
	 * of the offspring population constructed so far.
	 * @param dogs All dog genes in the solution space.
	 * @param newPopulation The offspring population constructed so far in the calculation of the
	 * 						next generation.
	 * @return All dog genes that haven't been assigned yet to any offspring that is already a part
	 * 		   of the offspring population constructed so far.
	 */
	private Dog[] getAvailableDogs(Dog[] dogs, ArrayList<Chromosome> newPopulation) 
	{
		ArrayList<Dog> availableDogs = new ArrayList<Dog>();
		Dog dogComponent = null;
		boolean available = true;
		for(Dog dog : dogs)
		{
			available = true;
			for(Chromosome chromosome : newPopulation)
			{
				dogComponent = chromosome.getDog();
				if (dogComponent == dog) // Man component already taken by an existing chromosome.
				{
					available = false;
					break;
				}
			}
			if(available == true)
				availableDogs.add(dog);
		}
		return availableDogs.toArray(new Dog[availableDogs.size()]);
	}
}
