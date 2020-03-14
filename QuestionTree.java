// This class implements a yes/no guessing game

import java.io.*;
import java.util.*;

public class QuestionTree {
   private QuestionNode overallRoot;
   private Scanner console;
   
   public static final String DEFAULT_OBJECT = "computer";
   
   // post: constructs a question tree with one leaf node representing
   //       the default object
   public QuestionTree() {
      overallRoot = new QuestionNode(DEFAULT_OBJECT);
      console = new Scanner(System.in);
   }
   
   // post: write the question tree to given output file in standard format
   //       in preorder
   public void write(PrintStream output) {
      write(overallRoot, output);
   }
   
   // post: write the given node and its child nodes to given output file
   //       in standard format in preorder
   private void write(QuestionNode root, PrintStream output) {
      if (root.yes == null) {
         output.println("A:");
         output.println(root.doc);
      } else {
         output.println("Q:");
         output.println(root.doc);
         write(root.yes, output);
         write(root.no, output);
      }
   }
   
   // pre : the input file is legal and in standard format
   // post: replaces the current question tree by reading another one
   //       from the given file
   public void read(Scanner input) {
      overallRoot = readHelper(input);
   }
   
   // pre : the input file is legal and in standard format
   // post: returns the node by reading from the given file
   private QuestionNode readHelper(Scanner input) {
      String type = input.nextLine();
      String content = input.nextLine();
      QuestionNode root = new QuestionNode(content);
      if (type.equals("Q:")) {
         root.yes = readHelper(input);
         root.no = readHelper(input);
      }
      return root;
   }
   
   // post: asks the user a series of yes/no questions according to 
   //       the current question tree until correct guess or until fail;
   //       if fail, expands the tree to include the correct object name
   //       and a new question to distinguish the two objects
   public void askQuestions() {
      overallRoot = askQuestions(overallRoot);
   }
   
   // post: ask a yes/no questions according to given node
   //       until correct guess or until fail;
   //       if fail, expands the tree to include the correct object name
   //       and a new question to distinguish the two objects
   private QuestionNode askQuestions(QuestionNode root) {
      if (root.yes != null) {
         if (yesTo(root.doc)) {
            root.yes = askQuestions(root.yes);
         } else {
            root.no = askQuestions(root.no);
         }
      } else {
         String question = "Would your object happen to be " + root.doc + "?";
         if (!yesTo(question)) {
            root = expandTree(root);
         } else {
             System.out.println("Great, I got it right!");
         }
      }
      return root;
   }
   
   // post: expands and returns the tree node to include the correct object name
   //       and a new question to distinguish the two objects
   private QuestionNode expandTree(QuestionNode leaf) {
      String objectName = getName();
      String question = getQuestion();

      QuestionNode correctName = new QuestionNode(objectName);
      if (yesTo("And what is the answer for your object?")) {
         leaf = new QuestionNode(question, correctName, leaf);
      } else {
         leaf = new QuestionNode(question, leaf, correctName);
      }
      return leaf;
   }
   
   // post: gets and returns the correct object name
   private String getName() {
      System.out.print("What is the name of your object? ");
      String objectName = console.nextLine().trim().toLowerCase();
      return objectName;
   }
   
   // post: gets and returns a new question that distinguishes between 
   //       the correct answer and incorrect one
   private String getQuestion() {
      System.out.println("Please give me a yes/no question that");
      System.out.println("distinguishes between your object");
      System.out.print("and mine--> ");
      String question = console.nextLine().trim();
      return question;
   }
   
   // post: asks the user a question, forcing an answer of "y" or "n";
   //       returns true if the answer was yes, returns false otherwise
   public boolean yesTo(String prompt) {
      System.out.print(prompt + " (y/n)? ");
      String response = console.nextLine().trim().toLowerCase();
      while (!response.equals("y") && !response.equals("n")) {
         System.out.println("Please answer y or n.");
         System.out.print(prompt + " (y/n)? ");
         response = console.nextLine().trim().toLowerCase();
      }
      return response.equals("y");
   }
}