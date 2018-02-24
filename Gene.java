package ex12Q2;

public class Gene 
{
    private Man man;
    private Woman woman;
    private Dog dog;

    public Gene() {
    }

    public Gene(Man man, Woman woman, Dog dog)
    {
        this();
        this.man = man;
        this.woman = woman;
        this.dog = dog;
        setPartners();
    }

    /**
     * Creates a copy of a gene, setting the input individual (gene component) instead of the
     * input gene's component of the same type.
     * @param individual An individual (gene component) to set instead of the component of
     *                   the input gene of the same type.
     * @param gene An input gene.
     * @param <T> The type of the input individual/gene component.
     * @return A new gene, with the same individuals (components) of the input gene except
     *         the one of the type of the input individual, which is replaced by the input one.
     */
    public static <T> Gene createGene (T individual, Gene gene)
    {
        Gene testGene = new Gene();
        testGene.setIndividual((Individual)individual);
        if(!(individual instanceof Man))
            testGene.setMan(gene.getMan());
        if(!(individual instanceof Woman))
            testGene.setWoman(gene.getWoman());
        if(!(individual instanceof Dog))
            testGene.setDog(gene.getDog());
        return testGene;
    }

    /**
     * For each component (individual) of the gene, sets its partners to the appropriate
     * values.
     */
    private void setPartners()
    {
        if (this.man != null)
            this.man.setPartners(this.woman, this.dog);
        if (this.woman != null)
            this.woman.setPartners(this.man, this.dog);
        if (this.dog != null)
            this.dog.setPartners(this.man, this.woman);
    }

    public Man getMan()
    {
        return man;
    }

    public void setMan(Man man)
    {
        this.man = man;
        setPartners();
    }

    public Woman getWoman() {
        return woman;
    }

    public void setWoman(Woman woman)
    {
        this.woman = woman;
        setPartners();
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog)
    {
        this.dog = dog;
        setPartners();
    }

    /**
     * Finds if the first input individual is found in the gene. If it does, replaces it
     * with the second input individual.
     * @param oldIndividual the individual for which to check if it is already found in
     *                      the gene.
     * @param newIndividual the individual to put instead of the current individual in the
     *                      relevant field, if the current individual equals the input old
     *                      individual.
     */
    public void replaceIfExists(Individual oldIndividual, Individual newIndividual)
    {
        if(oldIndividual instanceof Man && ((Man)oldIndividual).equals(getMan())
                && newIndividual instanceof Man)
            setMan((Man)newIndividual);
        else if(oldIndividual instanceof Woman && ((Woman)oldIndividual).equals(getWoman())
                && newIndividual instanceof Woman)
            setWoman((Woman)newIndividual);
        else if(oldIndividual instanceof Dog && ((Dog)oldIndividual).equals(getDog())
                && newIndividual instanceof Dog)
            setDog((Dog)newIndividual);
    }

    /**
     * Returns the individual in the gene corresponding to the input class.
     * @param individualClass the class of the individual in the gene to return.
     * @param <T> the type of the individual in the gene to return.
     * @return the individual in the gene corresponding to the input class.
     */
    public <T> Individual getIndividual(Class<T> individualClass)
    {
        if (individualClass.equals(Man.class))
            return getMan();
        else if (individualClass.equals(Woman.class))
            return getWoman();
        else if (individualClass.equals(Dog.class))
            return getDog();
        return null;
    }

    /**
     * Sets the field corresponding to the input individual to the input individual.
     * @param newIndividual the individual to set as the new value of the corresponding field.
     */
    public <T> void setIndividual(Individual newIndividual)
    {
        if (newIndividual instanceof Man)
            setMan((Man)newIndividual);
        else if (newIndividual instanceof Woman)
            setWoman((Woman)newIndividual);
        else if (newIndividual instanceof Dog)
            setDog((Dog)newIndividual);
    }

    /**
     * Gets the sum of distances between the gene components (individuals). This is used to
     * compute the fitness of a chromosome.
     * @return the sum of distances between gene components (individuals).
     */
    public int getSumOfDistances()
    {
        return (getMan().getDistance() + getWoman().getDistance() + getDog().getDistance());
    }
}
