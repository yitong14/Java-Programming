// Class AssassinManager can be used to keep track of two different lists:
//       the list of those currently alive and the list of those who are dead.

import java.util.*;

public class AssassinManager {
   private AssassinNode killRingFront;
   private AssassinNode graveyardFront;
   
   // pre : names are nonempty strings (throws an IllegalArgumentException if empty);
   //       every name should be unique (ignoring case), the kill ring will still 
   //       be constructed otherwise, but it may cause confusion.
   // post: constructs a kill ring according to the given names in given case and in given order,
   //       initialize the graveyard to null
   public AssassinManager(List<String> names) {
      if (names.size() == 0) {
         throw new IllegalArgumentException("Illegal names: " + names);
      }
      killRingFront = new AssassinNode(names.get(0));
      AssassinNode current = killRingFront;
      for (int i = 1; i < names.size(); i++) {
         current.next = new AssassinNode(names.get(i));
         current = current.next;
      }
      graveyardFront = null;
   }
   
   // pre : the kill ring should contain at least one person
   // post: prints the names of the people in the kill ring, one per line,
   //       indented four space, in the form of "<name> is stalking <name>"
   //       if there is only one person, report should be that the person is stalking themselves
   public void printKillRing() {
      AssassinNode current = killRingFront.next;
      System.out.print("    " + killRingFront.name + " is stalking ");
      while (current != null) {
         System.out.println(current.name);
         System.out.print("    " + current.name + " is stalking ");
         current = current.next;
      }
      System.out.println(killRingFront.name);
   }
   
   // post: prints the names of the people in the graveyard, one per line,
   //       indented four space, in the form of "<name> was killed by <name>".
   //       produces no output if the graveyard is empty.
   public void printGraveyard() {
      AssassinNode current = graveyardFront;
      while (current != null) {
         System.out.println("    " + current.name + " was killed by " + current.killer);
         current = current.next;
      }
   }
   
   // pre : not case sensitive.
   // post: returns true if the given name is in the current kill ring,
   //       returns false otherwise.
   public boolean killRingContains(String name) {
      return contains(killRingFront, name);
   }
   
   // pre : not case sensitive.
   // post: returns true if the given name is in the current graveyard,
   //       returns false otherwise.
   public boolean graveyardContains(String name) {
      return contains(graveyardFront, name);
   }
   
   // post: returns true if the game is over, i.e. if the kill ring has
   //       just one person in it, returns false otherwise.
   public boolean gameOver() {
      if (killRingFront.next == null) {
         return true;
      }
      return false;
   }
   
   // post: returns the name of the winner of the game if the game is over,
   //       returns null otherwise.
   public String winner() {
      if (gameOver()) {
         return killRingFront.name;
      }
      return null;
   }
   
   // pre : not case sensitive. the game should not be over (throws an IllegalStateException otherwise)
   //       && the given name should be in the kill ring (throws an IllegalArgumentException otherwise)
   // post: records the killing of the person with the given name by transferring the person 
   //       from the kill ring to the graveyard, also records who killed the person. (not case sensitive)
   //       the graveyard is in reverse kill order, the most recent killed first.
   public void kill(String name) {
      if (gameOver()) {
         throw new IllegalStateException("Game is over!");
      }
      if (!killRingContains(name)) {
         throw new IllegalArgumentException("kill ring does not contains: " + name);
      }
      name = name.toLowerCase();
      if (killRingFront.name.toLowerCase().equals(name)) {
         // transfers the person from the kill ring to the graveyard
         killRing2Graveyard(killRingFront);         
         // records who killed the person
         AssassinNode current = killRingFront;
         while (current.next != null) {
            current = current.next;
         }
         graveyardFront.killer = current.name;
      } else {
         AssassinNode current = killRingFront;
         while (current != null && current.next != null) {
            if (current.next.name.toLowerCase().equals(name)) {
               // transfers the person from the kill ring to the graveyard
               killRing2Graveyard(current.next);               
               // records who killed the person
               graveyardFront.killer = current.name;
            }
            current = current.next;
         }
      }
   }
   
   // transfers the person from the kill ring to the graveyard
   private void killRing2Graveyard(AssassinNode killRingNode) {
      AssassinNode temp = killRingNode;
      killRingNode = killRingNode.next;
      temp.next = graveyardFront;
      graveyardFront = temp;
   }
   
   // post: checks whether the list beginning with the given node contains the given name.
   //       returns true if contains; returns false otherwise.
   private boolean contains(AssassinNode front, String name) {
      name = name.toLowerCase();
      AssassinNode current = front;
      while (current != null) {
         if (current.name.toLowerCase().equals(name)) {
            return true;
         }
         current = current.next;
      }
      return false;
   }
}