// Class LetterInventory can be used to keep track of 
//       an inventory of letters of the alphabet

public class LetterInventory {
   private int[] letterStock;      // list of counts of each alphabetic letter (a-z)
   private int size;               // current sum of all counts
   
   public static final int ALPHABET_NUM = 26;
   
   // pre : only alphabetic letter is counted (ignore if it is not)
   //       the case of the letter is ignored
   // post: constructs an inventory of the alphabetic letters in the
   //       given string
   public LetterInventory(String data) {
      letterStock = new int[ALPHABET_NUM];
      size = 0;
      data = data.toLowerCase();

      for (int i = 0; i < data.length(); i++) {
         char letter = data.charAt(i);
         if (letter >= 'a' && letter <= 'z') {
            letterStock[letter - 'a']++;
            size++;
         }
      }
   }
   
   // post: returns the sum of all of the counts in the letter inventory
   public int size() {
      return size;
   }
   
   // post: check whether the inventory is empty (true if it is empty)
   public boolean isEmpty() {
      return size == 0;
   }
   
   // pre : letter should be alphabetic (throws an IllegalArgumentException
   //       if not); the case of the letter is ignored
   // post: returns a count of this letter in the inventory
   public int get(char letter) {
      isAlphabetic(letter);
      letter = Character.toLowerCase(letter);
      return letterStock[letter - 'a'];
   }
   
   // post: creates a String representation of the letter inventory
   //       in alphabetical order between square bracket
   public String toString() {
      String result = "[";
      for (int i = 0; i < letterStock.length; i++) {
         for (int j = 0; j < letterStock[i]; j++) {
            result += (char) ('a' + i);
         }
      }
      result += "]";
      return result;
   }
   
   // pre : letter shoulde be alphabetic && value should be non-negative
   //       (throws and IllegalArgumentException if any of these is not satisfied)
   //       the case of the letter is ignored
   // post: sets the count for the given letter to the given value
   public void set(char letter, int value) {
      if (value < 0) {
         throw new IllegalArgumentException("value: " + value);
      }
      isAlphabetic(letter);
      letter = Character.toLowerCase(letter);
      size += value - letterStock[letter - 'a'];
      letterStock[letter - 'a'] = value;
   }
   
   // post: constructs and returns a new LetterInventory object
   //       which is sum of this and other LetterInventory objects
   public LetterInventory add(LetterInventory other) {
      LetterInventory result = new LetterInventory("");
      for (int i = 0; i < letterStock.length; i++) {
         result.set((char) ('a' + i), letterStock[i] + other.letterStock[i]);
      }
      return result;
   }
   
   // pre : resulting count from subtracting the counts of a certain letter
   //       should be non-negative (return null if not)
   // post: constructs and returns a new LetterInventory object
   //       which is the result of subtracting the other inventory
   //       from this inventory
   public LetterInventory subtract(LetterInventory other) {
      LetterInventory result = new LetterInventory("");
      for (int i = 0; i < letterStock.length; i++) {
         if (letterStock[i] < other.letterStock[i]) {
            return null;
         } else {
            result.set((char) ('a' + i), letterStock[i] - other.letterStock[i]);
         }
      }
      return result;
   }
   
   // post: checks if the given letter is alphabetic
   //       (throw an IllegalArgumentException if it is not)
   private void isAlphabetic(char letter) {
      if (!Character.isLetter(letter)) {
         throw new IllegalArgumentException("letter: " + letter);
      }
   }
}