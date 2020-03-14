// This class stores the grammar and generates random elements of the grammar

import java.util.*;

public class GrammarSolver {
   private SortedMap<String, String[]> elementStock;
   
   // pre : the grammar should not be empty && each entry in the grammar should be the grammar
   //       of different nonterminal (throws an IllegalArgumentException if any of these is not satisfied)
   //       case sensitive, the nonterminal and its rules should be separated by "::=" without quotes
   //       the nonterminal of each string should not be empty, and does not contain ant whitespace
   //       there can be duplicates among the rules of one nonterminal
   // post: stores the given grammar for the convenience of later generating parts of grammar
   public GrammarSolver(List<String> grammar) {
      if (grammar.size() == 0) {
         throw new IllegalArgumentException();
      }
      elementStock = new TreeMap<String, String[]>();
      for (String line : grammar) {
         // separates nonterminal and its rules by "::=" without quotes
         String[] elements = line.split("::=");
         // key is the nonterminal
         String key = elements[0].trim();
         // values are the rules separated by "|" without quotes
         String[] values = elements[1].split("[|]");
         elementStock.put(key, values);
      }
      
      if (grammar.size() > elementStock.keySet().size()) {
         elementStock.clear();
         throw new IllegalArgumentException();
      }
   }
   
   // post: returns true if the given symbol is a nonterminal of the grammar
   //       returns false otherwise
   public boolean grammarContains(String symbol) {
      return elementStock.containsKey(symbol);
   }
   
   // post: return a string representation of the various nonterminal symbols
   //       from the grammar as a sorted, comma-separated list enclosed in square brackets
   public String getSymbols() {
      Set<String> keys = elementStock.keySet();
      return keys.toString();
   }
   
   // pre : the given symbol should be included in the grammar && times >= 0
   //       (throws an IllegalArgumentException if any of these is not satisfied)
   //       case-sensitive
   // post: randomly generates the given number of occurrences of the given symbol
   //       (each rules with equal probability && duplicate rules with higher probability)
   public String[] generate(String symbol, int times) {
      if (!elementStock.containsKey(symbol) || times <0) {
         throw new IllegalArgumentException();
      }
      String[] result = new String[times];
      for (int i = 0; i < times; i++) {
         result[i] = generate(symbol);
      }
      return result;
   }
   
   // pre : the given symbol should be included in the grammar
   // post: randomly generates one occurrence of the given symbol
   //       (each rules with equal probability && duplicate rules with higher probability)
   private String generate(String symbol) {
      String result = "";
      // gets all the rules of the symbol
      String[] values = elementStock.get(symbol);
      // generates random index and gets a certain rule
      int num = (int) (Math.random() * (values.length));
      String[] terminals = values[num].trim().split("[ \t]+");
      // generate each elements in this rule
      for (String t : terminals) {
         if (grammarContains(t)) {
            result = result + " " + generate(t);
         } else {
            result = result + " " + t;
         }
      }
      return result.trim();
   }
}