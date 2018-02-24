package ex12Q2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CollectionUtilities
{
    /**
     * Removes an existing element from an input array and puts the last element in the array
     * instead of the removed element, if such a last element still exists after the deletion.
     * @param array the array to delete the existing element from.
     * @param existingElement the existing element to delete.
     * @param <T> the type of the element and array.
     */
    public static <T> void removeExistingElement(T[] array, T existingElement)
    {
        if (array != null && array.length > 0)
        {
            for (int i = 0; i < array.length ; i++)
            {
                if (array[i].equals(existingElement))
                    array[i] = array[array.length - 1]; // Replace found element with
                                                        // the last element in the array.
            }
        }
    }

    /**
     * Uniformly at random chooses an element from the input array and returns it.
     * @param array an input array to choose an element from uniformly at random.
     * @param <T> the type of the input array.
     * @return an element in the array chosen uniformly at random.
     */
    public static <T> T getRandomElement(T[] array)
    {
        if (array != null)
            return array[RNGUtilities.generateRandomInteger(0, array.length-1)];
        return null;
    }

    /**
     * Receives an array of one dimensional arrays of some type and the type of the arrays
     * and returns an array which is a concatenation of all the input arrays.
     * @param elementClass the class of each element of each of the input arrays.
     * @param arrays an array of one dimensional arrays.
     * @return a concatenation of all input arrays.
     */
    public static <T> T[] merge(Class<T> elementClass, T[] ... arrays)
    {
        int offset = 0;
        int newArrayLength = 0;
        T[] newArray = null;
        for (T[] array : arrays)
            newArrayLength += array.length;
        newArray = (T[]) Array.newInstance(elementClass, newArrayLength);
        for (int i = 0; i < arrays.length; i++)
        {
            for(int j = 0; j < arrays[i].length; j++)
                newArray[j + offset] = arrays[i][j];
            offset += arrays[i].length;
        }
        return newArray;
    }

    /**
     * Receives a set of elements and a subset of this set of elements known to exist
     * in a certain context and returns the different between the first and second sets.
     * @param allElements the set of all elements.
     * @param existingElements the set of existing elements.
     * @param <T> the type of the elements in each set.
     * @return the difference between the first set and the second set.
     */
    public static <T> Collection<T> getMissingElements(Collection<T> allElements,
                                                       Collection<T> existingElements)
    {
        Set<T> missingElements = new HashSet<>(allElements);
        missingElements.removeAll(existingElements);
        return missingElements;
    }
}
