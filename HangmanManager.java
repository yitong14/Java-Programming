// This class keeps track of the state of the hangman game.

import java.util.*;

public class HangmanManager {
   private Set<String> words;       // current set of words being considered
   private Set<Character> guesses;  // letters that has already been guessed
   private String pattern;          // current pattern to be displayed
   private int guessesLeft;         // the number of guesses the player has left
   
   // pre : length >= 1 && max >= 0 (throws an IllegalArgumentException if any of
   //       these is not satisfied);
   //       the dictionay of wrods should not have duplicates (not case-sensitive)
   //       (if has, the duplicates will be ignored)
   // post: initializes the state of the game, and keeps record of the set of words
   //       with given length, and the number of wrong guesses the player is allowed
   //       to make.
   public HangmanManager(Collection<String> dictionary, int length, int max) {
      if (length < 1 || max < 0) {
         throw new IllegalArgumentException("length: " + length +
                                            "; max guesses: " + max);
      }
      // initialize the set of words
      words = new TreeSet<String>();
      for (String word : dictionary) {
         if (word.length() == length) {
            words.add(word.toLowerCase());
         }
      }
      // initialize the pattern to dashes and spaces according to given word length
      pattern = "-";
      for (int i = 0; i < length - 1; i++) {
         pattern = pattern + " -";
      }
      // initialize the guesses and guessesLeft
      guesses = new TreeSet<Character>();
      guessesLeft = max;
   }
   
   // post: presents the current set of words being considered by the hangmanmanager
   //       (the words will be shown in square brackets and separated by commas)
   public Set<String> words() {
      return words;
   }
   
   // post: returns how many guesses the player has left;
   public int guessesLeft() {
      return guessesLeft;
   }
   
   // post: presents the current set of letters that have been guessed
   //       (the letters will be shown in square brackets and separated by commas)
   public Set<Character> guesses() {
      return guesses;
   }
   
   // pre : the current set of words being considered should not be empty
   //       (throws an IllegalStateException otherwise)
   // post: returns the current pattern to be displayed for the hangman game 
   //       taking into account guesses that have been made. 
   //       Letters that have not yet been guessed are displayed as a dash and
   //       all the letters or dashes are separated by spaces without leading or trailing spaces.
   public String pattern() {
      if (words.isEmpty()) {
         throw new IllegalStateException("The set of words is empty.");
      }
      return pattern;
   }
   
   // pre : the number of guesses left >= 1 && the set of words being considered is not empty
   //       (throws an IllegalStateException otherwise)
   //       the character being guesses should not be guessed previously
   //       (throws an IllegalArgumentException otherwise)
   //       the character should be alphabetic letter and not case-sensitive to the guessed letter
   //       (the non-alphabetic letter will only minus the number of guesses left)
   // post: returns the number of occurrences of the guessed letter int the new pattern.
   //       records the guess of the letter. updates the number of the guesses left,
   //       updates the new set with the most words being considered as well as the new pattern
   //       according to the set of words.
   public int record(char guess) {
      if (guessesLeft < 1 || words.isEmpty()) {
         throw new IllegalStateException();
      }
      guess = Character.toLowerCase(guess);
      if (guesses.contains(guess)) {
         throw new IllegalArgumentException(guess + " has been guessesd.");
      }
      // records the guess of the letter
      guesses.add(guess);
      // updates the set with largest remaining word families and the new pattern of it
      changePattern(guess);
      // updates the number of the guesses left
      if (!pattern.contains("" + guess)) {
         guessesLeft--;
         return 0;
      }
      int occurrences = 0;
      for (int i = 0; i < pattern.length(); i++) {
         if (pattern.charAt(i) == guess) {
            occurrences++;
         }
      }
      return occurrences;
   }
   
   // pre : the letter should be in lower case
   //       (or it may cause confusion when telling whether the word contains the letter)
   // post: updates the set with largest remaining word families and the new pattern taking
   //       into account all guesses
   private void changePattern(char guess) {
      String guessPattern = guessPattern(guess);
      String newPattern = "";
      for (int i = 0; i < pattern.length(); i++) {
         if (pattern.charAt(i) == '-') {
            newPattern = newPattern + guessPattern.charAt(i);
         } else {
            newPattern = newPattern + pattern.charAt(i);
         }
      }
      pattern = newPattern;
   }
   
   // pre : the letter should be in lower case
   //       (or it may cause confusion when telling whether the word contains the letter)
   // post: chooses the largest of the remaining word families and
   //       updates the set of words being considered.
   //       returns the pattern according to the chosen set of wrods.
   //       (this pattern only shows whether and where do the words have 
   //       the current guessed letter, not including previous guess)
   private String guessPattern(char guess) {
      Map<String, Set<String>> map = patternOfWords(guess);
      int max = 0;
      String guessPattern = pattern;
      for (String key : map.keySet()) {
         int size = map.get(key).size();
         if (size > max) {
            max = size;
            guessPattern = key;
            words = map.get(guessPattern);
         }
      }
      return guessPattern;
   }
   
   // pre : the letter should be in lower case
   //       (or it may cause confusion when telling whether the word contains the letter)
   // post: generates and returns the map with different patterns as keys
   //       and the set of words with the same pattern as values
   //       (this pattern only shows whether and where do the words have 
   //       the current guessed letter, not including previous guess)
   private Map<String, Set<String>> patternOfWords(char guess) {
      Map<String, Set<String>> map = new TreeMap<>();
      for (String word : words) {
         String key = patternOfWord(guess, word);
         if (!map.containsKey(key)) {
            map.put(key, new TreeSet<String>());
         }
         map.get(key).add(word);
      }
      return map;
   }
   
   // pre : the word and the letter should be in lower case
   //       (or it may cause confusion when telling whether the word contains the letter)
   // post: generates the pattern of given word with given guessed letter
   //       (this pattern only shows whether and where does the word have 
   //       the current guessed letter, not including previous guess)
   private String patternOfWord(char guess, String word) {
      String key;
      // initializes the first character due to the fence problem
      if (word.charAt(0) == guess) {
         key = "" + guess;
      } else {
         key = "-";
      }
      // adds the rest of the fence
      for (int i = 1; i < word.length(); i++) {
         if (word.charAt(i) == guess) {
            key = key + " " + guess;
         } else {
            key = key + " -";
         }
      }
      return key;
   }
}