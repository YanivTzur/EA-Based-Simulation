package ex12Q2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
     *
     * @author Yaniv Tzur
     */
    public enum PreferenceListGenerationMode {
        ONE_TIME, // Generate preference lists once at the beginning of a series of runs.
        EACH_RUN // Generate preference lists from scratch on the start of each run.
    }

    public static final double DEFAULT_MUTATION_THRESHOLD = 0.99; // The threshold a randomly 
    														      // generated number in the 
    															  // interval [0,1] must pass 
    															  // in order for a mutation to occur.
    public static final int DEFAULT_NUM_OF_RUNS = 1; // Default number of consecutive runs to carry
                                                     // out when the simulation has been started.
    public static final int DEFAULT_TIME_BTWN_GENERATIONS = 1000; // Default time between generations
    															  // in milliseconds.
    private static final int MIN_TIME_BETWEEN_GENERATIONS = 50; // The minimum time between generations
    															// milliseconds a user can set.
	public static final int DEFAULT_NUMBER_OF_GENERATIONS = 50; // Default number of generations
																// per run.

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
	private int _numOfGenerations; // Number of generations per run.
	private int _previousRunNumber; // The number of the previous run.
    
	private Map<Integer, Chromosome> _initialFitnessValues; // The best, average and worst fitness
														    // values and the corresponding
														    // chromosomes obtaining these values
													        // where relevant.
	private int _bestSolutionFitness;    // The fitness of the best solution so
									     // far in the current run.

    /**
     * Associates the input view with the controller object.
     *
     * @param view The view to associate with this controller object.
     */
    public void setView(EASimulationGraphicalView view) {
        _view = view;
    }

    /***
     * Associates the input model with the controller object.
     * @param model The model to associate with this controller object.
     */
    public void setModel(EASimulationModel model) {
        _model = model;
    }

    /**
     * Creates an initial display for the program.
     */
    public void initializeView() {
        if (_view != null && _model != null)
            _view.updateView(false, false, true,
                    getBestFitnessValues().keySet().iterator().next(),
                    getAverageFitnessValue().keySet().iterator().next(),
                    getWorstFitnessValues().keySet().iterator().next(),
                    0,
                    0,
                    0,
                    getRunNumber(),
                    _model.getCurrentGenerationNumber(),
                    _model.getNumOfMen(),
                    _model.getNumOfWomen(),
                    _model.getNumOfDogs());
    }

    /**
     * Starts a new sequence of runs from scratch.
     *
     * @param numberOfChromosomes		   The number of chromosomes (solutions) to maintain.
     * @param preferenceListGenerationMode Whether to generate preference lists for each individual
     *                                     once in the beginning of the sequence of runs or at the
     *                                     start of each run.
     * @param isCrossoverEnabled           Whether crossover should be applied when creating offspring.
     * @param numOfRuns                    The number of runs to execute in the current sequence.
     * @param numOfGenerations			   The number of generations per run.
     * @param timeBetweenGenerations       The time to wait between the generation of each pair of
     *                                     consecutive generations.
     */
    public void startRun(int numberOfChromosomes,
    					 PreferenceListGenerationMode preferenceListGenerationMode,
                         boolean isCrossoverEnabled, int numOfRuns, int numOfGenerations,
                         int timeBetweenGenerations) 
    {
        if (_nextGenerationTimer == null || _nextGenerationTimer.isRunning() == false) 
        {
        	setNumberOfChromosomes(numberOfChromosomes);
            setPreferenceListGenerationMode(preferenceListGenerationMode);
            setCrossoverEnabledMode(isCrossoverEnabled);
            setNumOfDesiredRuns(numOfRuns);
            setNumOfGenerations(numOfGenerations);
            setPreviousRunNumber(0);
            setCurrentRunNumber(0);
            _model.setGenerationNumber(0);
            _currentElapsedTotalRunTime = 0;
            // Clear results text area.
            _initialFitnessValues = null;
            _bestSolutionFitness = 0;
            _view.updateView(false, true, false, _model.getBestFitness(), 
            				 _model.getAverageFitness(), _model.getWorstFitness(),
            				 0, _bestSolutionFitness,
            				 _currentElapsedTotalRunTime, _currentRunNumber, 
            				 _model.getCurrentGenerationNumber(), _model.getNumOfMen(), 
            				 _model.getNumOfWomen(), _model.getNumOfDogs());
            startTimer(timeBetweenGenerations);
        }
    }
    
    /**
     * Returns the number of the previous run.
     * @return the number of the previous run.
     */
    public int getPreviousRunNumber()
    {
    	return _previousRunNumber;
    }
    
    /**
     * Sets the number of the previous run.
     * @param previousRunNumber The number of the previous run.
     */
    public void setPreviousRunNumber(int previousRunNumber) 
    {
		_previousRunNumber = previousRunNumber;
	}

	/**
     * Sets the number of generations per run to the input value.
     * @param numOfGenerations The new value to set for the number of generations per run.
     */
    private void setNumOfGenerations(int numOfGenerations) 
    {
    	_numOfGenerations = numOfGenerations;
	}

	/**
     * Sets the number of chromosomes in the program to the input value.
     * @param numberOfChromosomes the new number of chromosomes to be used.
     */
    private void setNumberOfChromosomes(int numberOfChromosomes) 
    {
    	_model.setNumberOfChromosomes(numberOfChromosomes);
	}

	/**
     * Continues a sequence of runs that has been paused from the point at which it was paused.
     *
     * @param preferenceListGenerationMode Whether to generate preference lists for each individual
     *                                     once in the beginning of the sequence of runs or at the
     *                                     start of each run.
     * @param isCrossoverEnabled           Whether crossover should be applied when creating offspring.
     * @param timeBetweenGenerations       The time to wait between the generation of each pair of
     *                                     consecutive generations.
     */
    public void continueRun(PreferenceListGenerationMode preferenceListGenerationMode,
                            boolean isCrossoverEnabled, int timeBetweenGenerations) {
        setPreferenceListGenerationMode(preferenceListGenerationMode);
        setCrossoverEnabledMode(isCrossoverEnabled);
        if (_nextGenerationTimer == null || _nextGenerationTimer.isRunning() == false)
            startTimer(timeBetweenGenerations);
    }

    /**
     * Starts the timer controlling the rate at which to move forward to the next generation
     * and at which to update the screen accordingly.
     *
     * @param timeBetweenGenerations The time in milliseconds to wait between the generation of
     *                               the current generation to that of the next.
     */
    private void startTimer(int timeBetweenGenerations) 
    {
        setTimeBetweenGenerations(timeBetweenGenerations);
        if (_nextGenerationTimer != null && _nextGenerationTimer.isRunning())
        	_nextGenerationTimer.stop();
        _nextGenerationTimer = new Timer(timeBetweenGenerations, this);
        _nextGenerationTimer.start();
    }

    /**
     * Saves the input value as the time to wait between the generation of each pair of
     * consecutive generations.
     *
     * @param timeBetweenGenerations The new time in milliseconds to wait between the generation
     *                               of each pair of consecutive generations.
     */
    private void setTimeBetweenGenerations(int timeBetweenGenerations) {
        if (timeBetweenGenerations >= MIN_TIME_BETWEEN_GENERATIONS)
            _timeBetweenGenerations = timeBetweenGenerations;
        else
            timeBetweenGenerations = DEFAULT_TIME_BTWN_GENERATIONS;
    }

    /**
     * Returns the currently set preference list generation mode.
     *
     * @return The currently set preference list generation mode.
     */
    public PreferenceListGenerationMode getPreferenceListGenerationMode() {
        return _preferenceListGenerationMode;
    }

    /**
     * Sets the preference list generation mode to the input mode.
     *
     * @param preferenceListGenerationMode The preference list generation mode to use.
     */
    private void setPreferenceListGenerationMode(PreferenceListGenerationMode preferenceListGenerationMode) {
        _preferenceListGenerationMode = preferenceListGenerationMode;
    }

    /**
     * Sets whether crossover is applied when creating offspring in the creation of the next
     * generation.
     *
     * @param isCrossoverEnabled Whether crossover is applied when creating offspring.
     */
    private void setCrossoverEnabledMode(boolean isCrossoverEnabled) {
        _isCrossoverEnabled = isCrossoverEnabled;
    }

    /**
     * Sets the number of desired runs in the next sequence of runs.
     *
     * @param numOfRuns The number of runs to include in the next executed sequence.
     */
    private void setNumOfDesiredRuns(int numOfRuns) {
        _numOfDesiredRuns = numOfRuns;
    }

    /**
     * Returns the number of the currently executed run in the current sequence.
     *
     * @return The number of the currently executed run in the current sequence.
     */
    private int getCurrentRunNumber() {
        return _currentRunNumber;
    }

    /**
     * Sets the serial number of the current run in the current sequence to the input number.
     *
     * @param runNumber The number to set as the ordinal number of the current run in the current
     *                  sequence.
     */
    private void setCurrentRunNumber(int runNumber) {
        _currentRunNumber = runNumber;
    }

    /**
     * Stops the generation of subsequent generations.
     */
    public void stopRun() {
        if (_nextGenerationTimer != null && _nextGenerationTimer.isRunning() == true)
            _nextGenerationTimer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == _nextGenerationTimer) 
        {
            if (_currentRunNumber >= _numOfDesiredRuns) // Performed all runs.
            {
                stopRun();
                setNumOfDesiredRuns(0);
                setCurrentRunNumber(0);
            } 
            else // Not all runs have finished.
            {
                if (_model.getCurrentGenerationNumber() == 0) // New run, initialize new model.
                {
                	// Each individual uses a different list of preferences in each different run.
                    if (getPreferenceListGenerationMode() == PreferenceListGenerationMode.EACH_RUN)
                        _model.initializeChromosomes(true); //
                    else  // Each individual uses the same preference lists in 
                    	  // all runs in a given sequence.
                    {
                        if (getCurrentRunNumber() == 0)
                            _model.initializeChromosomes(true);
                        else _model.initializeChromosomes(false);
                    }
                	_initialFitnessValues = getFitnessValues();
                }
                else 
                    _model.setNewGeneration(calculateNextGeneration(_model.getChromosomes()));
                _bestSolutionFitness = updateBestSolutionFitness(_bestSolutionFitness);
                _view.updateView(false, false, false,
                        getBestFitnessValues().keySet().iterator().next(),
                        getAverageFitnessValue().keySet().iterator().next(),
                        getWorstFitnessValues().keySet().iterator().next(),
                        getInitialBestFitness(),
                        getBestSolutionFitness(),
                        (double) _currentElapsedTotalRunTime / 1000,
                        getRunNumber(),
                        _model.getCurrentGenerationNumber(),
                        _model.getNumOfMen(),
                        _model.getNumOfWomen(),
                        _model.getNumOfDogs());
            }
            _model.incrementGeneration();
            _currentElapsedTotalRunTime += _timeBetweenGenerations;
            if (_model.getCurrentGenerationNumber() > getNumOfGenerations()) 
            {
                _view.updateView(true, false, false,
                        getBestFitnessValues().keySet().iterator().next(),
                        getAverageFitnessValue().keySet().iterator().next(),
                        getWorstFitnessValues().keySet().iterator().next(),
                        getInitialBestFitness(),
                        getBestSolutionFitness(),
                        (double) _currentElapsedTotalRunTime / 1000,
                        getRunNumber(),
                        _model.getCurrentGenerationNumber() - 1, // Generation No. before last
                        										 // increment.
                        _model.getNumOfMen(),
                        _model.getNumOfWomen(),
                        _model.getNumOfDogs());
            	_previousRunNumber = _currentRunNumber;
                _currentRunNumber++;
                _model.setGenerationNumber(0);
                _currentElapsedTotalRunTime = 0;
            }
        }
    }

    /**
     * Returns the fitness of the best solution so far in the current run.
     * @param bestSolutionFitness The fitness of the best solution before this method was invoked.
     * @return the fitness of the best solution so far in the current run.
     */
    private int updateBestSolutionFitness(int bestSolutionFitness) 
    {
    	int currentGenerationBestFitness = getBestFitnessValues().keySet().iterator().next();
    	
    	if (currentGenerationBestFitness > bestSolutionFitness)
    		return currentGenerationBestFitness;
    	return bestSolutionFitness;
	}

	/**
     * Returns the best fitness value among the fitness values of the chromosomes in the first
     * generation (generation 0).
     * @return the best fitness value among the fitness values of the chromosomes in the first
     * 		   generation (generation 0).
     */
    private int getInitialBestFitness() 
    {
    	return _initialFitnessValues.keySet()
    								.stream()
    								.mapToInt(fitness -> fitness)
    								.max()
    								.orElse(0);
	}

    /**
     * Returns the fitness of the best solution so far in the current run.
     * @return the fitness of the best solution so far in the current run.
     */
	private int getBestSolutionFitness() 
	{
		return _bestSolutionFitness;
	}

	/**
     * Returns a map containing the best, average and worst fitness values for this generation and
     * the corresponding chromosomes obtaining those values.
     * @return a map containing the best, average and worst fitness values for this generation and
     *         the corresponding chromosomes obtaining those values.
     */
	private Map<Integer, Chromosome> getFitnessValues() 
	{
		Map<Integer, Chromosome> fitnessValues = new HashMap<>();
		
		fitnessValues.putAll(getBestFitnessValues());
		fitnessValues.putAll(getAverageFitnessValue());
		fitnessValues.putAll(getWorstFitnessValues());
		
		return fitnessValues;
	}

	/**
	 * Returns a map containing the best fitness value of all chromosomes in the current generation, 
	 * and the corresponding chromosome.
	 * @return a map containing the best fitness value of all chromosomes in the current generation, 
	 * 		   and the corresponding chromosome.
	 */
	private Map<Integer, Chromosome> getBestFitnessValues() 
	{
		Map<Integer, Chromosome> bestFitnessValues = new HashMap<>();
		Chromosome maxChromosome = null;
		int maxFitness = 0;
		
		for(Chromosome chromosome : _model.getChromosomes())
			if(chromosome.getFitness() > maxFitness)
			{
				maxChromosome = new Chromosome(chromosome.getGenes());
				maxFitness = chromosome.getFitness();
			}
		bestFitnessValues.put(maxFitness, maxChromosome);
		
		return bestFitnessValues;
	}

	/**
	 * Returns a map containing the average fitness value in the current generation.
	 * @return a map containing the average fitness value in the current generation.
	 */
	private Map<Integer, Chromosome> getAverageFitnessValue() 
	{
		Map<Integer, Chromosome> averageFitnessValueMap = new HashMap<Integer, Chromosome>();
		int averageFitnessValue = 0;
		
		if (_model.getChromosomes().length > 0)
			averageFitnessValue = (int)Arrays.stream(_model.getChromosomes())
										 .mapToInt(chromosome -> chromosome.getFitness())
										 .average()
										 .orElse(0);
		averageFitnessValueMap.put(averageFitnessValue, null);
		
		return averageFitnessValueMap;
	}

	/**
	 * Returns a map containing the worst fitness value of all chromosomes in the current generation, 
	 * and the corresponding chromosome.
	 * @return a map containing the worst fitness value of all chromosomes in the current generation, 
	 * 		   and the corresponding chromosome.
	 */
	private Map<Integer, Chromosome> getWorstFitnessValues() 
	{
		Map<Integer, Chromosome> worstFitnessValues = new HashMap<>();
		Chromosome minChromosome = null;
		int minFitness = Integer.MAX_VALUE;
		
		for(Chromosome chromosome : _model.getChromosomes())
			if(chromosome.getFitness() < minFitness)
			{
				minChromosome = new Chromosome(chromosome.getGenes());
				minFitness = chromosome.getFitness();
			}
		worstFitnessValues.put(minFitness, minChromosome);
		
		return worstFitnessValues;	
	}

	/**
     * Returns the number of generations per run.
     * @return the number of generations per run.
     */
    private int getNumOfGenerations() 
    {
    	return _numOfGenerations;
	}

	/**
     * Returns the number of the current run (starts from 0).
     *
     * @return The number of the current run.
     */
    public int getRunNumber() {
        return _currentRunNumber;
    }

    /**
     * Calculates the next generation of solutions and returns the newly generated population.
     *
     * @param currentPopulation The current population of solutions (chromosomes).
     * @return The new generation of solutions (chromosomes).
     */
    private Chromosome[] calculateNextGeneration(Chromosome[] currentPopulation) {
        ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();
        Chromosome parent1 = null;
        Chromosome parent2 = null;
        Chromosome offspring[] = null; // Expected to be an array of size 2.

        while (newPopulation.size() < currentPopulation.length) {
            parent1 = selectParent(currentPopulation);
            parent2 = selectParent(parent1, currentPopulation);
            if (_isCrossoverEnabled == true)
                offspring = crossover(parent1, parent2);
            else
                offspring = new Chromosome[]{parent1, parent2};
            offspring = tryMutate(offspring, _model.getMen(), _model.getWomen(),
                    _model.getDogs(), newPopulation);
            newPopulation.addAll(Arrays.asList(offspring));
        }

        return newPopulation.toArray(new Chromosome[newPopulation.size()]);
    }

    /**
     * Selects a parent for creating offspring and returns it.
     *
     * @param currentPopulation The population of solutions in the current generation.
     * @return The parent selected.
     */
    private Chromosome selectParent(Chromosome[] currentPopulation) {
        return selectParent(null, currentPopulation);
    }

    /**
     * Selects a parent for creating offspring and returns it.
     *
     * @param alreadySelectedParent A parent that has already been selected. Passed as an argument
     *                              to avoid selecting the same parent twice.
     * @param currentPopulation     The population of solutions in the current generation.
     * @return The parent selected.
     */
    private Chromosome selectParent(Chromosome alreadySelectedParent,
                                    Chromosome[] currentPopulation)
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
        for (i = 0; currentSum < wheelLocation && i < populationSize; i++)
            currentSum += availableChromosomes.get(i).getFitness();
        if (i >= populationSize)
            return availableChromosomes.get(i - 1);
        return availableChromosomes.get(i);
    }

    /**
     * Sums the fitness of all selectable chromosomes (all chromosomes not already selected as parents)
     * and returns the obtained sum.
     *
     * @param selectableChromosomes All chromosomes not already selected as parents.
     * @param totalPopulationSize   The number of all chromosomes in the population in the current
     *                              generation.
     * @return The sum of the fitness values of all selectable chromosomes.
     */
    private int sumFitness(ArrayList<Chromosome> selectableChromosomes, int totalPopulationSize) {
        int totalFitness = 0;
        if (selectableChromosomes != null)
            for (int i = 0; i < selectableChromosomes.size(); i++)
                totalFitness += selectableChromosomes.get(i).getFitness();
        return totalFitness;
    }

    /**
     * Performs a crossover on the two selected parents' genes, creating two offspring.
     * The first of the offspring has the first half genes of the first parent and the last
     * half genes of the second parent.
     * The second of the offspring has the last half of the genes of the first parent and
     * the first half of the genes of the second parent.
     *
     * @param parent1 The first selected parent.
     * @param parent2 The second selected parent.
     * @return The produced offspring.
     */
    private Chromosome[] crossover(Chromosome parent1, Chromosome parent2)
    {
        Gene[] parent1Genes = parent1.getGenes();
        Gene[] parent2Genes = parent2.getGenes();

        Chromosome offspring1 = crossover(Arrays.copyOfRange(parent1Genes,
                                                        0,
                                                          parent1Genes.length / 2),
                                          Arrays.copyOfRange(parent2Genes,
                                                        parent2Genes.length / 2,
                                                             parent2Genes.length));
        Chromosome offspring2 = crossover(Arrays.copyOfRange(parent1Genes,
                                                        parent1Genes.length / 2,
                                                             parent1Genes.length),
                                          Arrays.copyOfRange(parent2Genes,
                                                        0,
                                                          parent2Genes.length / 2));

        return new Chromosome[]{offspring1, offspring2};
    }

    /**
     * Receives two arrays of genes, and constructs a new array of genes that is some
     * mix of the two arrays and constitutes a proper solution to the problem at hand.
     * The method then returns a chromosome constructed using the new array of genes
     * that the method created.
     * @param firstGenes the first input array of genes.
     * @param secondGenes the second input array of genes.
     * @return a new chromosome constructed as above.
     */
    private Chromosome crossover(Gene[] firstGenes, Gene[] secondGenes)
    {
        Gene[] newGenes = CollectionUtilities.merge(Gene.class, firstGenes, secondGenes);
        newGenes = correctSolutionComponents(newGenes);
        return new Chromosome(newGenes);
    }

    /**
     * Receives the genes (components) of a possibly incorrect solution, produces
     * a set of genes constituting a proper solution to the problem at hand instead,
     * based on the input array of genes, and returns it.
     * @param genes The genes (components) of a possibly partially incorrect solution.
     * @return  The corrected solution
     */
    private Gene[] correctSolutionComponents(Gene[] genes)
    {
        Gene[] newGenes = null;

        Set<Man> allMen = new HashSet<>(Arrays.asList(_model.getMen()));
        Set<Woman> allWomen = new HashSet<>(Arrays.asList(_model.getWomen()));
        Set<Dog> allDogs = new HashSet<>(Arrays.asList(_model.getDogs()));

        Set<Man> encounteredMen = new HashSet<>();
        Set<Woman> encounteredWomen = new HashSet<>();
        Set<Dog> encounteredDogs = new HashSet<>();

        HashMap<Man, List<Integer>> redundantMen = new HashMap<>();
        HashMap<Woman, List<Integer>> redundantWomen = new HashMap<>();
        HashMap<Dog, List<Integer>> redundantDogs = new HashMap<>();

        Set<Man> missingMen = null;
        Set<Woman> missingWomen = null;
        Set<Dog> missingDogs = null;

        for(int i = 0; i < genes.length; i++)
        {
            handleRedundantIndividuals(genes[i], i, encounteredMen, encounteredWomen,
                                       encounteredDogs,
                                       redundantMen, redundantWomen, redundantDogs);
            handleEncounteredIndividuals(genes[i],
                                         encounteredMen, encounteredWomen,
                                         encounteredDogs);
        }

        missingMen = new HashSet<>(CollectionUtilities.getMissingElements(allMen,
                                                                          encounteredMen));
        missingWomen = new HashSet<>(CollectionUtilities.getMissingElements(allWomen,
                                                                            encounteredWomen));
        missingDogs = new HashSet<>(CollectionUtilities.getMissingElements(allDogs,
                                                                           encounteredDogs));

        newGenes =  correctSolutionComponents(genes,
                                              missingMen,
                                              missingWomen,
                                              missingDogs,
                                              redundantMen,
                                              redundantWomen,
                                              redundantDogs);
        return newGenes;
    }

    /**
     * Receives a gene and sets of men, women and dogs encountered so far and adds each
     * component of the gene (man, woman, dog) to the respective set.
     * @param gene A component of a solution (chromosome).
     * @param encounteredMen Men encountered so far while iterating over a collection of genes.
     * @param encounteredWomen Women encountered so far while iterating over a collection of
     *                         genes.
     * @param encounteredDogs Dogs encountered so far while iterating over a collection of
     *                        genes.
     */
    private void handleEncounteredIndividuals(Gene gene,
                                              Set<Man> encounteredMen,
                                              Set<Woman> encounteredWomen,
                                              Set<Dog> encounteredDogs)
    {
        encounteredMen.add(gene.getMan());
        encounteredWomen.add(gene.getWoman());
        encounteredDogs.add(gene.getDog());
    }

    /**
     * For an input gene, for each component of the gene, if the component has already been
     * encountered in a previous gene, during an iteration over a collection of genes,
     * adds the index of the gene to a list of all indices in the gene array where
     * a repetition of the same component occurs.
     * @param gene An input gene (a component of the solution/chromosome).
     * @param encounteredMen The set of all men that have been encountered so far.
     * @param encounteredWomen The set of all women that have been encountered so far.
     * @param encounteredDogs The set of all dogs that have been encountered so far.
     * @param redundantMen A mapping between each man and the indices of the genes
     *                     in the array of genes where a redundancy of the same man
     *                     component occurs.
     * @param redundantWomen A mapping between each woman and the indices of the genes
     *                       in the array of genes where a redundancy of the same woman
     *                       component occurs.
     * @param redundantDogs A mapping between each dog and the indices of the genes
     *                      in the array of genes where a redundancy of the same dog
     *                      component occurs.
     */
    private void handleRedundantIndividuals(Gene gene,
                                            int index,
                                            Set<Man> encounteredMen,
                                            Set<Woman> encounteredWomen,
                                            Set<Dog> encounteredDogs,
                                            HashMap<Man, List<Integer>> redundantMen,
                                            HashMap<Woman, List<Integer>> redundantWomen,
                                            HashMap<Dog, List<Integer>> redundantDogs)
    {
        handleRedundantIndividuals(gene, index, encounteredMen, redundantMen, Man.class);
        handleRedundantIndividuals(gene, index, encounteredWomen, redundantWomen, Woman.class);
        handleRedundantIndividuals(gene, index, encounteredDogs, redundantDogs, Dog.class);
    }

    /**
     * For an input component of a gene with some input index, checks if the component
     * of the gene has already been encountered. If it has already been encountered,
     * adds the index of the gene to the mapping between each component of the input
     * type to the indices of the genes where a repetition of it appears.
     * @param gene An input gene.
     * @param index The index of the gene in the original input array of genes.
     * @param encounteredIndividuals The set of individuals, of the input type of
     *                               individuals, encountered so far while iterating over
     *                               the genes of a chromosome.
     * @param redundantIndividuals A mapping between each component of a gene of the
     *                             input type and the indices of the genes in the original
     *                             input array of genes, where the component appears as
     *                             a repetition.
     * @param individualClass      The type of the component to perform the
     *                             above-mentioned operations for.
     * @param <T>                  The type of the component to perform the
     *                             above-mentioned operations for.
     */
    @SuppressWarnings("unchecked")
	private <T> void handleRedundantIndividuals(Gene gene, int index,
                                                Set<T> encounteredIndividuals,
                                                HashMap<T, List<Integer>> redundantIndividuals,
                                                Class<T> individualClass)
    {
        if(encounteredIndividuals.contains(gene.getIndividual(individualClass)))
        {
            if(!redundantIndividuals.containsKey(gene.getIndividual(individualClass)))
                redundantIndividuals.put((T)gene.getIndividual(individualClass),
                                          new ArrayList<>());
            redundantIndividuals.get(gene.getIndividual(individualClass)).add(index);
        }
    }

    /**
     * Replaces each redundant gene component with a missing component, preferably in
     * a way that tries to optimize the overall fitness value of the chromosome
     * with the resulting array of genes.
     * @param genes The original gene array, before correction.
     * @param missingMen All missing man components.
     * @param missingWomen All missing woman components.
     * @param missingDogs All missing dog components.
     * @param redundantMen A mapping between each man component that appears more than
     *                     once and the indices of all genes where it repeats itself.
     * @param redundantWomen A mapping between each woman component that appears more than
     *                       once and the indices of all genes where it repeats itself.
     * @param redundantDogs A mapping between each dog component that appears more than
     *                      once and the indices of all genes where it repeats itself.
     * @return A new array of genes after each repeated instance of a gene component
     *         has been replaced with a missing component, creating a correct solution.
     */
    private Gene[] correctSolutionComponents(Gene[] genes,
                                             Set<Man> missingMen,
                                             Set<Woman> missingWomen,
                                             Set<Dog> missingDogs,
                                             Map<Man, List<Integer>> redundantMen,
                                             Map<Woman, List<Integer>> redundantWomen,
                                             Map<Dog, List<Integer>> redundantDogs)
    {
        Set<Man> newMissingMen = new HashSet<>(missingMen);
        Set<Woman> newMissingWoman= new HashSet<>(missingWomen);
        Set<Dog> newMissingDogs = new HashSet<>(missingDogs);
        Gene[] newGenes = Arrays.copyOf(genes, genes.length);

        correctSolutionComponents(newGenes, newMissingMen, redundantMen);
        correctSolutionComponents(newGenes, newMissingWoman, redundantWomen);
        correctSolutionComponents(newGenes, newMissingDogs, redundantDogs);

        return newGenes;
    }

    /**
     * Replaces each repetition of a gene component of the input parameter type with
     * a different missing component of the same type.
     * @param genes An array of genes.
     * @param missingIndividuals Gene components that should be in some gene but aren't.
     * @param redundantIndividuals Gene components that appear multiple times.
     * @param <T> The type of the gene component for which to carry out the correction.
     */
    private <T> void correctSolutionComponents(Gene[] genes, Set<T> missingIndividuals,
                                               Map<T, List<Integer>> redundantIndividuals)
    {
        Individual maxMissingIndividual;
        int redundantGeneIndex = 0;
        for(Entry<T,List<Integer>> entry : redundantIndividuals.entrySet())
        {
            while(entry.getValue().size() > 0)
                if (missingIndividuals.size() > 0)
                {
                    redundantGeneIndex = entry.getValue().remove(0);
                    maxMissingIndividual = getMaxMissingIndividual(missingIndividuals,
                                                                   genes[redundantGeneIndex]);
                    genes[redundantGeneIndex] = Gene.createGene(maxMissingIndividual,
                                                                genes[redundantGeneIndex]);
                    missingIndividuals.remove(maxMissingIndividual);
                }
        }
    }

    /**
     * Retrieves the missing individual that maximizes the input gene's contribution to the
     * complete chromosome's fitness.
     * @param missingIndividuals A set of all missing individuals of the input type.
     * @param gene An input gene.
     * @param <T> The type of the missing individuals.
     * @return the individual that maximizes the input gene's contribution to the
     *         complete chromosome's fitness.
     */
    private <T> Individual getMaxMissingIndividual(Set<T> missingIndividuals, Gene gene)
    {
        Gene geneForTesting;
        Gene maxGene = null;
        Individual firstIndividual;
        Individual maxIndividual = null;

        if (missingIndividuals.size() > 0) // Initialize max individual, max gene.
        {
            firstIndividual = (Individual) missingIndividuals.iterator().next();
            maxIndividual = firstIndividual;
            geneForTesting = Gene.createGene(firstIndividual, gene);
            maxGene = geneForTesting;
        }

        for (T individual : missingIndividuals)
        {
            geneForTesting = Gene.createGene(individual, gene);
            if (geneForTesting.getSumOfDistances() > maxGene.getSumOfDistances())
            {
                maxGene = geneForTesting;
                maxIndividual = (Individual) individual;
            }
        }

        return maxIndividual;
    }

    private Chromosome[] tryMutate(Chromosome[] offspring, Man[] men, Woman[] women,
                                   Dog[] dogs, ArrayList<Chromosome> newPopulation)
    {
        double randomlyGeneratedNumber; // A randomly generated number
                                        // in the interval [0,1].
        Chromosome[] newOffspring = null;
        Chromosome newChromosome;
        Gene[] newChromosomeGenes;

        if (offspring != null)
        {
            newOffspring = new Chromosome[offspring.length];

            for (int i = 0; i < offspring.length; i++)
            {
                if (offspring[i] != null && offspring[i].getGenes() != null)
                {
                    newChromosome = new Chromosome();
                    newChromosomeGenes = Arrays.copyOf(offspring[i].getGenes(),
                                                       offspring[i].getGenes().length);

                    for (int j = 0; j < Chromosome.NUM_OF_GENES; j++) {
                        randomlyGeneratedNumber = RNGUtilities.generateRandomDouble();
                        if (randomlyGeneratedNumber > DEFAULT_MUTATION_THRESHOLD)
                            newChromosomeGenes[j] = mutate(newChromosomeGenes[j],
                                                           men, women, dogs,
                                                           newPopulation);
                    }
                    newChromosome.setGenes(newChromosomeGenes);
                    newOffspring[i] = newChromosome;
                }
            }
        }
        return newOffspring;
    }

    /**
     * Uniformly at random chooses one of the components (individuals) of a a gene
     * and replaces it with another component, where the other component is also
     * chosen uniformly at random.
     * @param gene An input gene.
     * @param men An array containing all men in the solution space.
     * @param women An array containing all women in the solution space.
     * @param dogs An array containing all dogs in the solution space.
     * @param newPopulation The gradually built new population of the next generation.
     * @return The same gene with one of its components (individuals) replaced.
     */
    private Gene mutate(Gene gene, Man[] men, Woman[] women, Dog[] dogs,
                        ArrayList<Chromosome> newPopulation) {
        double randomNumber = RNGUtilities.generateRandomDouble();
        if (randomNumber <= 0.33)
            return mutate(gene, men, Man.class, newPopulation);
        else if (randomNumber <= 0.66)
            return mutate(gene, women, Woman.class, newPopulation);
        else
            return mutate(gene, dogs, Dog.class, newPopulation);
    }

    /**
     * Chooses uniformly at random another individual to replace the current individual
     * in the input gene, where both individuals are of the type corresponding to the input
     * class. Then, if the new individual used to replace the old one in the input gene already
     * exists in some other gene in some other chromosome in the population of the new
     * generation, the method replaces it with the old individual of the input gene.
     *
     * @param gene            a gene for which to replace the current individual, of the type corresponding
     *                        to the input type, with another individual chosen uniformly at random.
     * @param individuals     an array containing all individuals of the same type as the type
     *                        of individual that needs to be replaced in the input gene.
     * @param individualClass the type of individual to be replaced in the input gene.
     * @param newPopulation   the population of the new generation currently being created.
     * @param <T>             the type of individual to be replaced in the input gene.
     * @return the gene after the appropriate individual has been replaced.
     */
    private <T> Gene mutate(Gene gene, Individual[] individuals,
                            Class<T> individualClass,
                            ArrayList<Chromosome> newPopulation) {
        Individual prevIndividual = gene.getIndividual(individualClass);
        Individual newIndividual = null;
        Individual[] individualsCopy = Arrays.copyOf(individuals, individuals.length);
        CollectionUtilities.removeExistingElement(individualsCopy, prevIndividual);
        newIndividual = CollectionUtilities.getRandomElement(individualsCopy);
        gene.setIndividual(newIndividual);
        replaceIfExists(newIndividual, prevIndividual, newPopulation);
        return gene;
    }

    /**
     * Searches if the first input individual already exists in the population of the new
     * generation. If it does, replaces it with the input replacement individual
     * @param currentIndividual the individual for which to check if it already exists in some
     *                          gene of some chromosome in the population of the new generation.
     * @param replacement       the individual to use to replace the old individual if the
     *                          old individual already exists in the population of the new
     *                          generation.
     * @param newPopulation     the population of the new generation, currently being generated.
     */
    private void replaceIfExists(Individual currentIndividual, Individual replacement,
                                 ArrayList<Chromosome> newPopulation) {
        newPopulation.stream().forEach(chromosome
                                        ->
                                        chromosome.replaceIfExists(currentIndividual,
                                                                   replacement));
    }

	public int getCurrentGenerationNumber() 
	{
		return _model.getCurrentGenerationNumber();
	}
}
