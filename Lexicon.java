import structure5.*;
import java.util.Iterator;

/*
 * This interface is used to represent a lexicon, or word list. The main
 * difference between the lexicon abstraction and a dictionary is that 
 * the lexicon does not provide any mechanism for storing definitions of
 * words; a string is just either in the list or not.
 * 
 * The lexicon supports lookup of words and prefixes. You can 
 * populate the lexicon by adding words one at a time or by reading word
 * data from a file.  You can also remove words from the lexicon, and
 * write the contents of the lexicon to a file.
 * 
 * Here is sample use of a LexiconTrie object (LexiconTrie implements Lexicon):
 *
 *	LexiconTrie lex = new LexiconTrie();
 *	lex.addWordsFromFile("lexicon.dat"); {
  *	lex.addWord("doughnut");
 *  if (lex.containsPrefix("fru") || lex.containsWord("ball"))
 *		//do something
 */

public interface Lexicon {
 
    /**
     * Add a single word to the lexicon.
     * This operation runs in time proportional to the length of
     * the word being added.
	 *
     * @return true if the word was added (i.e. previously did
     * not appear in this lexicon) and false otherwise.
	 * @pre The word is uniformly cased (either lower or upper)
     */
    
    public boolean addWord(String word);
     

    /**
     * Open a text file, and add each word in the file to this lexicon.
	 *
	 * @pre filename is the path of a text file containing one word per line.
	 * @return the count of new words that were added. If the file doesn't exist or was unable
     * to be opened, the function returns 0.
     */
    
    public int addWordsFromFile(String filename);


    /**
     * Remove a word from this lexicon. This operation 
     * runs in time proportional to the length of the word being removed.
	 *
	 * Note that you do not need to remove unneeded nodes from the tree 
     * (this is an optional extension).
	 *
	 * @post If @word appears in the lexicon, it is removed,
     * If @word is not contained in the lexicon, the lexicon is unchanged.
	 * @return  true if a word was removed, false otherwise.
     
     */
    
    public boolean removeWord(String word);

 
	/**
	 * Returns the number of words in the lexicon (in constant time).
	 *
     * @return the number of words contained in this lexicon.
     */
    public int numWords();


    /**
	 * Case-insensitive membership check. This operation runs in time
	 * proportional to the length of the target word.
	 *
     * @return true if the specified
     * word exists in this lexicon, false otherwise.
     */

    public boolean containsWord(String word);


    /**
	 * Check whether one or more words contained in the lexicon start
	 * with the target prefix. A word is defined to be a prefix of
	 * itself.  The empty string is a prefix of everything.  Prefixes
	 * are considered case-insensitively. This operation runs in time
	 * proportional to the length of the prefix.
	 *
     * @return true if any words in the lexicon begin with the specified prefix, false
     * otherwise.
     */

    public boolean containsPrefix(String prefix);


    /**
	 * Returns an iterator over the words in the lexicon.  This
	 * operation runs in O(N) time where N is the total number of
	 * words in the lexicon.
	 *
     * @return an iterator over all words contained in the
     * lexicon. Accessing the words from the iterator will retrieve
     * them in alphabetical order.
     */
    public Iterator<String> iterator();


    /**
     * Generates a set of suggested corrections for the target word.
     * All words in the lexicon with a distance to the target that is
     * less than or equal to the parameter distance should be in the
     * returned set. This operation runs in time proportional to the
     * length of the target string.
	 *
     * @return a pointer to a complete set of strings that are
	 * within @maxDistance of @target.
     */
    public Set<String> suggestCorrections(String target, int maxDistance);
	

    /**
	 * Generates a set of strings that match a target pattern.  All
     * words in the lexicon that match the pattern should be in the
     * returned set.
	 *
	 * @return a pointer to a complete set of strings from this
     * lexicon that match the regular expression @pattern.
     */
    public Set<String> matchRegex(String pattern);
    	
}

