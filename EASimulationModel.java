package ex12Q2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controls the internal state of the EA based three-way matching program and access to it.
 * @author Yaniv Tzur
 */
public class EASimulationModel 
{	
	public static final int DIVISION_BY_ZERO_ERROR = -1; // Error code returned when a
                                                         // division by zero was about to occur.
	public static final int DEFAULT_NUMBER_OF_CHROMOSOMES = 30; // Default number of chromosomes to
                                                                // use in each generation.
	
	private Man[] _men; // All men in the solution space.
	private Woman[] _women; // All women in the solution space.
	private Dog[] _dogs; // All dogs in the solution space.
	private Chromosome[] _chromosomes; // The chromosomes (solutions) currently existing in the simulation.
	private int _currentGenerationNumber; // The number of the current generation (starts at 0).
	private int _numberOfChromosomes; // The number of chromosomes to use.
	
	/**
	 * Creates a new empty model.
	 */
	public EASimulationModel()
	{
		_currentGenerationNumber = 0;
		_numberOfChromosomes = DEFAULT_NUMBER_OF_CHROMOSOMES;
	}
	
	/**
	 * Calls the initializeChromosomes(chromosomes) method with a new array of chromosomes of
	 * a the defined default size.
	 * @param newPreferenceLists Whether to create new preference lists or use the same existing
	 * 							 ones. If no preference lists have been generated yet,
	 * 							 ignores the parameter and creates initial preference lists.
	 */
	public void initializeChromosomes(boolean newPreferenceLists)
	{
		_chromosomes = new Chromosome[_numberOfChromosomes];
		initializeChromosomes(newPreferenceLists, _chromosomes);
	}
	
	/**
	 * Creates a random initial population of possible three-way matches.
	 * @param  newPreferenceLists Whether to create new preference lists or use the same existing
	 * 							  ones. If no preference lists have been generated yet,
	 * 							  ignores the parameter and creates initial preference lists.
	 * @param chromosomes Used to store the population of possible solutions in each generation.
	 */
	public void initializeChromosomes(boolean newPreferenceLists, Chromosome[] chromosomes)
	{
		if (_men == null || _women == null || _dogs == null || newPreferenceLists == true)
		{
			_men = initializeMen(Chromosome.NUM_OF_GENES);
			_women = initializeWomen(Chromosome.NUM_OF_GENES);
			_dogs = initializeDogs(Chromosome.NUM_OF_GENES);
		}
		randomizedGenerateInitialPopulation(newPreferenceLists, chromosomes, _men,
				                            _women, _dogs);
	}
	
	/**
	 * Returns references to all stored Man objects.
	 * @return References to all stored Man objects.
	 */
	public Man[] getMen()
	{
		return _men;
	}
	
	/**
	 * Returns references to all stored Woman objects.
	 * @return References to all stored Woman objects.
	 */
	public Woman[] getWomen()
	{
		return _women;
	}
	
	/**
	 * Returns references to all stored Dog objects.
	 * @return References to all stored Dog objects.
	 */
	public Dog[] getDogs()
	{
		return _dogs;
	}
	
	/**
	 * Creates an initial set of men.
	 * @param length The size of the set of men to create.
	 * @return The initial set of men created.
	 */
	private Man[] initializeMen(int length)
	{
		Man[] men = new Man[length];
		for (int i = 0; i < men.length; i++)
			men[i] = new Man();
		return men;
	}
	
	/**
	 * Creates an initial set of women.
	 * @param length The size of the set of women to create.
	 * @return The initial set of women created.
	 */
	private Woman[] initializeWomen(int length)
	{
		Woman[] women = new Woman[length];
		for (int i = 0; i < women.length; i++)
			women[i] = new Woman();
		return women;
	}
	
	/**
	 * Creates an initial set of dogs.
	 * @param length The size of the set of dogs to create.
	 * @return The initial set of dogs created.
	 */
	private Dog[] initializeDogs(int length)
	{
		Dog[] dogs = new Dog[length];
		for (int i = 0; i < dogs.length; i++)
			dogs[i] = new Dog();
		return dogs;
	}
	
	/**
	 * Creates a randomly generated initial population of solutions (chromosomes).
	 * @param  newPreferenceLists Whether to create new preference lists or use the same existing
	 * 							  ones. If no preference lists have been generated yet,
	 * 							  ignores the parameter and creates initial preference lists.
	 * @param chromosomes Stores the chromosomes that exist in the current generation.
	 * @param men Stores all existing men.
	 * @param women Stores all existing women.
	 * @param dogs Stores all existing dogs.
	 */
	private void randomizedGenerateInitialPopulation(boolean newPreferenceLists,
													 Chromosome[] chromosomes,
													 Man[] men, 
													 Woman[] women,
													 Dog[] dogs)
	{
		if (chromosomes != null && men != null && women != null && dogs != null)
            {
                for (int i = 0; i < chromosomes.length; i++)
                    chromosomes[i] = randomizedGenerateChromosome(men, women, dogs);
                if (newPreferenceLists == true) // Randomly create new preference lists.
                    randomizedGeneratePreferenceLists(men, women, dogs);
            }
	}

    /**
     * Constructs a new chromosome by iteratively selecting uniformly at random a triplet of
     * the form (man, woman, dog) from the sets of men, women and dogs that haven't been
     * selected yet, and adding it to the gradually built chromosome.
     * @param men the set of all men.
     * @param women the set of all women.
     * @param dogs the set of all dogs.
     * @return the new chromosome created as described above.
     */
    private Chromosome randomizedGenerateChromosome(Man[] men, Woman[] women, Dog[] dogs)
    {
        Gene[] newGenes = new Gene[Chromosome.NUM_OF_GENES];
        List<Man> availableMen = Arrays.asList(Arrays.copyOf(men,men.length));
        List<Woman> availableWomen = Arrays.asList(Arrays.copyOf(women, women.length));
        List<Dog> availableDogs = Arrays.asList(Arrays.copyOf(dogs, dogs.length));
        for (int i = 0; i < Chromosome.NUM_OF_GENES; i++)
            newGenes[i] = new Gene(availableMen.get(RNGUtilities.generateRandomInteger(0,
                                                    availableMen.size() - 1)),
                                   availableWomen.get(RNGUtilities.generateRandomInteger(0,
                                                      availableWomen.size() - 1)),
                                   availableDogs.get(RNGUtilities.generateRandomInteger(0,
                                                     availableDogs.size() - 1)));
        return new Chromosome(newGenes);
    }


    /**
	 * Randomly generates a list of preferences for each defined man, woman and dog.
	 * @param men An array containing references to all defined men.
	 * @param women An array containing references to all defined women.
	 * @param dogs An array containing references to all defined dogs.
	 */
	private void randomizedGeneratePreferenceLists(Man[] men, Woman[] women, Dog[] dogs) 
	{
		if (men != null && women != null && dogs != null)
		{
			for (int i = 0; i < men.length; i++)
				randomizedGeneratePreferenceLists(men[i], women, dogs);
			for (int i = 0; i < women.length; i++)
				randomizedGeneratePreferenceLists(women[i], men, dogs);
			for (int i = 0; i < dogs.length; i++)
				randomizedGeneratePreferenceLists(dogs[i], men, women);
		}
	}
	
	/**
	 * Creates randomized lists of preferences for the input man for women and dogs.
	 * @param man The man to create randomized lists of preferences for.
	 * @param women The women to set the man preferences for.
	 * @param dogs The dogs to set the man preferences for.
	 */
	private void randomizedGeneratePreferenceLists(Man man, Woman[] women, Dog[] dogs)
	{
		List<Woman> availableWomen = new ArrayList<Woman>(Arrays.asList(women));
		List<Dog> availableDogs = new ArrayList<Dog>(Arrays.asList(dogs));
		int currentLength = 0;
		if (man != null && women != null && dogs != null
			&&
			women.length == dogs.length)
		{
			currentLength = women.length;
			if (man.arePreferenceListsEmpty() == false)
				man.clearPreferenceLists();
			while (currentLength > 0)
			{
				man.addToPreferenceList(Woman.class.getName(),
										availableWomen.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				man.addToPreferenceList(Dog.class.getName(),
										availableDogs.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				currentLength--;
			}
		}
	}
	
	/**
	 * Creates randomized lists of preferences for the input woman for men and dogs.
	 * @param woman The woman to create randomized lists of preferences for.
	 * @param men The men to set the woman preferences for.
	 * @param dogs The dogs to set the woman preferences for.
	 */
	private void randomizedGeneratePreferenceLists(Woman woman, Man[] men, Dog[] dogs)
	{
		List<Man> availableMen = new ArrayList<Man>(Arrays.asList(men));
		List<Dog> availableDogs = new ArrayList<Dog>(Arrays.asList(dogs));
		int currentLength = 0;
		if (woman != null && men != null && dogs != null
			&&
			men.length == dogs.length)
		{
			currentLength = men.length;
			if (woman.arePreferenceListsEmpty() == false)
				woman.clearPreferenceLists();
			while (currentLength > 0)
			{
				woman.addToPreferenceList(Man.class.getName(),
										  availableMen.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				woman.addToPreferenceList(Dog.class.getName(),
										  availableDogs.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				currentLength--;
			}
		}
	}
	
	/**
	 * Creates randomized lists of preferences for the input dog for men and women.
	 * @param dog The dog to create randomized lists of preferences for.
	 * @param men The men to set the dog preferences for.
	 * @param women The women to set the dog preferences for.
	 */
	private void randomizedGeneratePreferenceLists(Dog dog, Man[] men, Woman[] women)
	{
		List<Man> availableMen = new ArrayList<Man>(Arrays.asList(men));
		List<Woman> avilableWomen = new ArrayList<Woman>(Arrays.asList(women));
		int currentLength = 0;
		if (dog != null && men != null && women != null
			&&
			men.length == women.length)
		{
			currentLength = men.length;
			if (dog.arePreferenceListsEmpty() == false)
				dog.clearPreferenceLists();
			while (currentLength > 0)
			{
				dog.addToPreferenceList(Man.class.getName(),
										availableMen.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				dog.addToPreferenceList(Woman.class.getName(),
										avilableWomen.remove(RNGUtilities.generateRandomInteger(0, currentLength-1)));
				currentLength--;
			}
		}
	}
	
	/**
	 * Returns an array of the chromosomes (solutions) in the current generation.
	 * @return An array of the chromosomes (solutions) in the current generation.
	 */
	public Chromosome[] getChromosomes()
	{
		return _chromosomes;
	}
	
	/**
	 * Returns the fitness value of the chromosome with the highest fitness value in the current
	 * generation.
	 * @return The fitness value of the chromosome with the highest fitness value in the current
	 * 		   generation.
	 */
	public int getBestFitness() 
	{
		int maxFitness = 0;
		int currFitness = 0;
		if (_chromosomes != null)
			for(Chromosome chromosome : _chromosomes)
			{
				currFitness = chromosome.getFitness();
				if (currFitness > maxFitness)
					maxFitness = currFitness;
			}
		return maxFitness;
	}

	/**
	 * Returns the average fitness value of the chromosomes/solutions in the current generation.
	 * @return The average fitness value of the chromosomes/solutions in the current generation.
	 */
	public int getAverageFitness() 
	{
		int count = 0;
		int totalFitness = 0;
		if (_chromosomes != null)
			for(Chromosome chromosome : _chromosomes)
			{
				count++;
				totalFitness += chromosome.getFitness();
			}
		if (count != 0)
			return (totalFitness/count);
		return DIVISION_BY_ZERO_ERROR;
	}

	/**
	 * Returns the fitness value of the chromosome with the lowest fitness value in the current
	 * generation.
	 * @return The fitness value of the chromosome with the lowest fitness value in the current
	 * 		   generation.
	 */
	public int getWorstFitness() 
	{
		int minFitness = Integer.MAX_VALUE;
		int currFitness = 0;
		if (_chromosomes != null)
			for(Chromosome chromosome : _chromosomes)
			{
				currFitness = chromosome.getFitness();
				if (currFitness < minFitness)
					minFitness = currFitness;
			}
		return minFitness;
	}

	/**
	 * Returns the number of the current generation.
	 * @return The number of the current generation.
	 */
	public int getCurrentGenerationNumber() 
	{
		return _currentGenerationNumber;
	}

	/**
	 * Get the total number of men in solution space.
	 * @return The total number of men in the solution space.
	 */
	public int getNumOfMen() 
	{
		if (_chromosomes != null)
			return _men.length;
		return 0;
	}

	/**
	 * Get the total number of women in solution space.
	 * @return The total number of women in the solution space.
	 */
	public int getNumOfWomen() 
	{
		if (_chromosomes != null)
			return _women.length;
		return 0;
	}

	/**
	 * Get the total number of dogs in solution space.
	 * @return The total number of dogs in the solution space.
	 */
	public int getNumOfDogs() 
	{
		if (_chromosomes != null)
			return _dogs.length;
		return 0;
	}

	/**
	 * Sets the population of solutions to the new input population and sets the new generation
	 * number to the input number.
	 * @param newPopulation The new population of solutions (chromosomes) to set as the current
	 * 						population.
	 */
	public void setNewGeneration(Chromosome[] newPopulation)
	{
		setChromosomes(newPopulation);
	}
	
	/**
	 * Increments the current generation number by 1.
	 */
	public void incrementGeneration()
	{
		setGenerationNumber(_currentGenerationNumber + 1);
	}
	
	/**
	 * Replaces the current population of solutions (chromosomes with the input population.
	 * @param newPopulation The new population of solutions (chromosomes) with which to replace the
	 * 						existing population.
	 */
	private void setChromosomes(Chromosome[] newPopulation)
	{
		_chromosomes = newPopulation;
	}
	
	/**
	 * Sets the current generation number to the input number.
	 * @param nextGenerationNumber The number to set as the current generation number.
	 */
	public void setGenerationNumber(int nextGenerationNumber)
	{
		_currentGenerationNumber = nextGenerationNumber;
	}

	/**
	 * Sets the number of chromosomes to be used in the program to the input value.
	 * @param numberOfChromosomes The new number of chromosomes to be use.
	 */
	public void setNumberOfChromosomes(int numberOfChromosomes) 
	{
		_numberOfChromosomes = numberOfChromosomes;
	}
}
