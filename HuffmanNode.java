// This class stores a single node of a Huffman tree

public class HuffmanNode implements Comparable<HuffmanNode> {
   public int charCode;
   public int frequency;
   public HuffmanNode left;
   public HuffmanNode right;
   
   // post: constructs a leaf node with given character code and frequency
   public HuffmanNode(int charCode, int frequency) {
      this(charCode, frequency, null, null);
   }
   
   // post: constructs a branch node with given character code, frequency,
   //       left subtree and right subtree
   public HuffmanNode(int charCode, int frequency, HuffmanNode left, HuffmanNode right) {
      this.charCode = charCode;
      this.frequency = frequency;
      this.left = left;
      this.right = right;
   }
   
   public int compareTo(HuffmanNode other) {
      return frequency - other.frequency;
   }
}



