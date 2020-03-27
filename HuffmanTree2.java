// This is a starter file that includes the read9/write9 methods described in
// the bonus assignment writeup.

import java.util.*;
import java.io.*;

public class HuffmanTree2 {
    private HuffmanNode overallRoot;
   
   // pre : count[i] should be the number of character with integer value i
   // post: constructs the initial Huffman tree using the given array of frequencies
   public HuffmanTree2(int[] count) {
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
   
   // pre : input has the standard bit representation for the tree.
   // post: Constructs a Huffman tree from the given input stream.
   public HuffmanTree2(BitInputStream input) {
      overallRoot = read2(input);
   }
   
   // pre : input has the standard bit representation for the tree.
   // post: Constructs a subtree from the given input stream.
   private HuffmanNode read2(BitInputStream input) {
      int n = input.readBit();
      HuffmanNode root = null;
      if (n == 0) {
         root = new HuffmanNode(-1, -1);
         root.left = read2(input);
         root.right = read2(input);
      } else {
         int charCode = read9(input);
         root = new HuffmanNode(charCode, -1);
      }
      return root;
   }
   
   // pre : the array has null values before the method is called.
   // post: Assigns codes for each character of the tree
   public void assign(String[] codes) {
      assign(overallRoot, "", codes);
   }
   
   // post: records and assigns codes for each character of the tree
   private void assign(HuffmanNode root, String sofar, String[] codes) {
      if (root.left == null) {
         codes[root.charCode] = sofar;
      } else {
         assign(root.left, sofar + 0, codes);
         assign(root.right, sofar + 1, codes);
      }
   }

   // post: writes the current tree to the output stream using the 
   //       standard bit representation
   public void writeHeader(BitOutputStream output) {
      writeHeader(overallRoot, output);
   }
   
   // post: writes the current subtree to the output stream using the 
   //       standard bit representation
   private void writeHeader(HuffmanNode root, BitOutputStream output) {
      if (root.left == null) {
         output.writeBit(1);
         write9(output, root.charCode);
      } else {
         output.writeBit(0);
         writeHeader(root.left, output);
         writeHeader(root.right, output);
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

   // pre : an integer n has been encoded using write9 or its equivalent
   // post: reads 9 bits to reconstruct the original integer
   private int read9(BitInputStream input) {
      int multiplier = 1;
      int sum = 0;
      for (int i = 0; i < 9; i++) {
         sum += multiplier * input.readBit();
         multiplier = multiplier * 2;
      }
      return sum;
   }

   // pre : 0 <= n < 512
   // post: writes a 9-bit representation of n to the given output stream
   private void write9(BitOutputStream output, int n) {
      for (int i = 0; i < 9; i++) {
         output.writeBit(n % 2);
         n = n / 2;
      }
   }
}
