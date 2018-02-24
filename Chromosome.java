package ex12Q2;

import java.util.Arrays;

/**
 * Represents a possible solution to the three-way matching optimization problem.
 * @author Yaniv Tzur
 */
public class Chromosome 
{
	public static int NUM_OF_GENES = 50; // The number of components/genes in each chromosome.
	
	private Gene[] _genes; // Contains all genes of the chromosome. In this application a gene is
                           // a triplet of man, woman and dog.


	public Chromosome()
    {

    }

    public Chromosome(Gene[] genes)
    {
        setGenes(genes);
    }

    public Chromosome(Gene[] firstHalfGenes, Gene[] secondHalfGenes) throws Exception
    {
        this();
        if (firstHalfGenes != null && secondHalfGenes != null
                && ((firstHalfGenes.length +  secondHalfGenes.length) == NUM_OF_GENES))
                    _genes = new Gene[firstHalfGenes.length + secondHalfGenes.length];
        else throw new Exception("Error: tried to create a new chromosome with an incorrect "
                                 +
                                 "number of genes.\n"
                                 +
                                 "Correct number is " + NUM_OF_GENES + " .\n"
                                 +
                                 "Number of genes in the input was "
                                 +
                                 firstHalfGenes.length + secondHalfGenes.length
                                 +
                                 " .\n");
    }

	/**
	 * Receives the number of chromosomes in the current generation and returns the fitness
	 * of the chromosome.
	 * The fitness is calculated as the maximum possible sum of distances minus the actual sum of
	 * distances to obtain a monotonically increasing function.
	 * @return The fitness of the chromosome.
	 */
	public int getFitness()
	{
		int sum = 0;
		if (_genes != null)
			for (int i = 0; i < _genes.length; i++)
				sum += _genes[i].getSumOfDistances();
		return sum;
	}

    public Gene[] getGenes()
    {
	    if (_genes == null)
	        return null;
        return Arrays.copyOf(_genes, _genes.length);
    }

    public void setGenes(Gene[] genes) {
        this._genes = genes;
    }

    /**
     * Searches if the first input individual exists in some gene in the chromosome. If it does,
     * replaces it with the second input individual.
     * Precondition: The same individual must not exist in two different genes to begin with.
     *               The logic of the program is supposed to guarantee this.
     * @param oldIndividual the individual for which to check if it already exists in some gene
     *                      of the chromosome.
     * @param newIndividual the individual to use to replace the old individual in an existing
     *                      gene if the old individual is equal to an individual in that gene.
     */
    public void replaceIfExists(Individual oldIndividual, Individual newIndividual)
    {
        Arrays.stream(getGenes())
                .forEach(gene -> gene.replaceIfExists(oldIndividual, newIndividual));
    }
}
