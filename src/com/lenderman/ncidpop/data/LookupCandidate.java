/*******************************************************
 * Lookup Candidate
 *******************************************************/
package com.lenderman.ncidpop.data;

/**
 * Lookup candidate data, used to return search results for name/number pairs.
 * 
 * @author Chris Lenderman
 */
public class LookupCandidate
{
    /** The lookup candidate name */
    public String name;

    /** The lookup candidate number */
    public String number;

    /**
     * Constructor
     */
    public LookupCandidate(String name, String number)
    {
        this.name = name;
        this.number = number;
    }

    /** @inheritDoc */
    @Override
    public String toString()
    {
        return this.name + " (" + this.number + ")";
    }

    /** @inheritDoc */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((name == null) ? 0 : name.hashCode());
        result = (prime * result) + ((number == null) ? 0 : number.hashCode());
        return result;
    }

    /** @inheritDoc */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        LookupCandidate other = (LookupCandidate) obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        if (number == null)
        {
            if (other.number != null)
            {
                return false;
            }
        }
        else if (!number.equals(other.number))
        {
            return false;
        }
        return true;
    }
}