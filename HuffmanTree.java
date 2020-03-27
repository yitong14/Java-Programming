// This class implements Huffman coding. This class builds, writes, and reads the 
// Huffman tree, and decodes the given compressed file which using Huffman coding.

import java.io.*;
import java.util.*;

public class HuffmanTree {
   private HuffmanNode overallRoot;
   
   // pre : count[i] should be the number of character with integer value i
   // post: constructs the initial Huffman tree using the given array of frequencies
   public HuffmanTree(int[] count) {
      Queue<HuffmanNode> queue = new PriorityQueue<>();
      int pseudo_eof = count.length;
      int countNode = 0;
      // constructs leaf node and add them into queue
      for (int i = 0; i < count.length; i++) {
         if (count[i] > 0) {
            queue.add(new HuffmanNode(i, count[i]));
            countNode++;
         }
      }
      // add pseudo-eof node into queue
      queue.add(new HuffmanNode(pseudo_eof, 1));
      // remove two smallest node, and add one 
      // until there is only one node in the queue, which is overallRoot
      for (int i = 0; i < countNode; i++) {
         HuffmanNode first = queue.remove();
         HuffmanNode second = queue.remove();
         HuffmanNode branch = new HuffmanNode(-1, first.frequency + second.frequency, first, second);
         queue.add(branch);
      }
      overallRoot = queue.remove();
   }
   
   // pre : given file contains a tree stored in standard format
   // post: constructs the Huffman tree from the given file
   public HuffmanTree(Scanner input) {
      while (input.hasNext()) {
         int n = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = read(overallRoot, code, n);
      }
   }
   
   // post: constructs and returns a leaf node which has given integer code
   //       and given given paths
   private HuffmanNode read(HuffmanNode root, String code, int n) {
      if (code.length() == 0) {
         root = new HuffmanNode(n, -1);
      } else {
         char direction = code.charAt(0);
         code = code.substring(1);
         if (root == null) {
            root = new HuffmanNode(-1, -1);
         }
         if (direction == '0') {
            root.left = read(root.left, code, n);
         } else {
            root.right = read(root.right, code, n);
         }
      }
      return root;
   }
   
   // post: writes the tree to the given output stream in standard format
   public void write(PrintStream output) {
      write(overallRoot, output, "");
   }
   
   // post: writes leaf node of the given tree to the given output stream 
   //       in standard format
   private void write(HuffmanNode root, PrintStream output, String sofar) {
      // prints the leaf node
      if (root.left == null) {
         output.println(root.charCode);
         output.println(sofar);
      } else {            // prints the leaf node of the branch node
         write(root.left, output, sofar + 0);
         write(root.right, output, sofar + 1);
      }
   }
   
   // pre : input stream contains a legal encoding of characters for this tree¡¯s Huffman code.
   // post: decodes the given input, and writes the corresponding integer value of characters to the output.
   //       the character with value equal to eof parameter means the end of decoding (will not be written)
   public void decode(BitInputStream input, PrintStream output, int eof) {
      boolean ok = true;
      HuffmanNode root = overallRoot;
      while (ok) {
         if (root.left == null) {
            int code = root.charCode;
            if (code == eof) {
               ok = false;
            } else {
               output.write(code);
               root = overallRoot;
            }
         } else {
            int bit = input.readBit();
            if (bit == 0) {
               root = root.left;
            } else {
               root = root.right;
            }
         }
      } 
   }
}