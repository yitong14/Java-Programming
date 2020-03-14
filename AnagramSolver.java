// This class uses a dictionary to find all combinations of words
//    that hace the same letters as a given phrase.

import java.util.*;

public class AnagramSolver {
   private List<String> dictionary;                           // list of words which can be used in anagram
   private Map<String, LetterInventory> letterstock;          // letter inventory of each word
   
   // pre : list is a nonempty collection of nonempty sequences of letters, contains no duplicates and
   //       doesn't change in state as the program executes (output may be confusing otherwise)
   //       the list is supposed to contains only alphabetic letters (ignores the nonalphabetic character if exist)
   //       ignores the case of the letters
   // post: constructs an anagram solver that will use the given list in given order as its dictionary;
   public AnagramSolver(List<String> list) {
      dictionary = list;
      letterstock = new HashMap<>();
      for (String word : list) {
         letterstock.put(word, new LetterInventory(word));
      }
   }
   
   // pre : max >= 0 (throws IllegalArgumentException otherwise)
   //       s shoud not be empty (prints nothing if it is empty)
   //       ignoring the case of letters of s and ignoring any non-alphabetic characters
   // post: finds and prints combinations of words that have the same letters as the given string
   //       including at most max words (or unlimited number of words if max is 0);
   //       the solutions and the words are printed in given order and given cases of the dictionary;
   //       words are in square brackets and separated by comma;
   //       prints nothing if no legal anagrams exists
   public void print(String s, int max) {
      if (max < 0) {
         throw new IllegalArgumentException();
      }
      LetterInventory rest = new LetterInventory(s);
      List<String> dict = prune(rest);
      print(rest, max, dict, new Stack<>());
   }
   
   // post: finds and prints combinations of words that have the same letters as the given letter inventory
   //       including at most max words (or unlimited number of words if max is 0);
   //       the solutions and the words are printed in given order and given cases of the dictionary;
   //       words are in square brackets and separated by comma;
   //       prints nothing if no legal anagrams exists
   private void print(LetterInventory rest, int max, 
                        List<String> dict, Stack<String> sofar) {
      if (max == 0 || sofar.size() <= max) {
         if (rest.size() == 0) {
            System.out.println(sofar);
         } else {
            for (String word : dict) {
               LetterInventory letters = rest.subtract(letterstock.get(word));
               if (letters != null) {
                  sofar.push(word);
                  print(letters, max, dict, sofar);
                  sofar.pop();
               }
            }
         }
      }
   }
   
   // post: reduces and returns the new lists of relevant words which can be subtracted
   //       from the given letter inventory
   private List<String> prune(LetterInventory rest) {
      List<String> dict = new ArrayList<>();
      for (String word : dictionary) {
         if (rest.subtract(letterstock.get(word)) != null) {
            dict.add(word);
         }
      }
      return dict;
   }
}