// Class for storing a single node of a binary question tree

public class QuestionNode {
   public String doc;
   public QuestionNode yes;
   public QuestionNode no;
   
   // constructs a leaf node with given string
   public QuestionNode(String doc) {
      this(doc, null, null);
   }
   
   // constructs a branch node with given string, left subtree
   // right subtree
   public QuestionNode(String doc, QuestionNode yes,
                        QuestionNode no) {
      this.doc = doc;
      this.yes = yes;
      this.no = no;
   }
}