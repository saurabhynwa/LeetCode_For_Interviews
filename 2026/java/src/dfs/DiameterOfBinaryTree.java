package src.dfs;

/*
Given the root of a binary tree, return the length of the diameter of the tree. The diameter of a binary tree is the
length of the longest path between any two nodes in a tree. This path may or may not pass through the root. The length
of a path between two nodes is represented by the number of edges between them.

Example 1:
Input: root = [1,2,3,4,5]

        [1]
       /   \
     [2]    [3]
    /   \
  [4]   [5]

Output: 3
Explanation: 3 is the length of the path [4,2,1,3] or [5,2,1,3].

Example 2:
Input: root = [1,2]
Output: 1

Constraints:

The number of nodes in the tree is in the range [1, 104].
-100 <= Node.val <= 100
 */

/*
Time Complexity = O(N), all nodes visited exactly once.

Space Complexity = O(H) , h = max depth of binary tree

- if tree is perfectly balanced then it is O(log N)
- if tree is skewed like a linked list then it is O(N)
 */

public class DiameterOfBinaryTree {
    private int maxDiameterOfBinaryTree;

    private int getMaxDiameterOfBinaryTree(TreeNode root){
        // the most important realization here is the fact that the answer doesn't necessarily need to be traversing
        // through root. A skewed subtree can also have the longest diameter. So you need to keep a global variable
        // which tracks the maximum diameter at each node.

        // At each node, diameter = sum of its left and right depth. Now we need to think what information will a node
        // bubble up to its parent. Each parent has 2 immediate child nodes. And those child nodes will have immediate 2
        // child of their own. So the immediate child of a parent from left, return 2 depths. Left and right of the
        // grand child. Similarly, the right immediate child also returns 2 depths. Now think that you are parent. You
        // are also a node, so the same logic applies to you as well for diameter calculation. But the confusion is what
        // to choose from left and right child returns ? Simple, choose max depth from either side. So left child gave
        // left and right depth. Choose max, that becomes the left value for the parent. Similar logic for right child
        // value return. This itself resolves what the main return needs to be from the function. And that will be the
        // (max of left and right depth) + 1. Now what is this '+1' ? In binary tree all the nodes are connected. So the
        // moment you land on a non-null node, it means there can be a possible edge upward (not true in case of root!).
        // so the main recursion return max of left,right and +1. This '+1' indicates the real max depth on that side in
        // case there is an upper node available.
        getMaxDepthForNode(root);
        return maxDiameterOfBinaryTree;
    }

    private int getMaxDepthForNode(TreeNode root){
        if(root == null){
            return 0;
        }

        int leftDepth = getMaxDepthForNode(root.left);
        int rightDepth = getMaxDepthForNode(root.right);

        int currentDiameter = leftDepth + rightDepth;

        // evaluate max
        maxDiameterOfBinaryTree = Math.max(maxDiameterOfBinaryTree, currentDiameter);

        // now the node needs to return the max depth to the parent node. Max depth will be the max of left and right.
        // And '+1' will be for the edge going upward, so that the recursion continues. If there isn't any upward node,
        // then that return just gets ignored. So no harm.
        return Math.max(leftDepth, rightDepth) + 1;
    }

    public static void main(String[] args){
        DiameterOfBinaryTree obj = new DiameterOfBinaryTree();
        // [3, 9, null, 1, 4, null, null, 2, null, 3]
        TreeNode t1 = new TreeNode(3);
        TreeNode t2 = new TreeNode(9);
        TreeNode t3 = new TreeNode(1);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(2);
        TreeNode t6 = new TreeNode(3);

        t1.left = t2;
        t2.left = t3;
        t2.right = t4;
        t3.left = t5;
        t4.left = t6;

        /*
                [3]
               /
              [9]
             /   \
           [1]    [4]
           /      /
         [2]     [3]

         (The longest path is 2 -> 1 -> 9 -> 4 -> 3) for a total of 4 edges
         */

        System.out.println(obj.getMaxDiameterOfBinaryTree(t1));
    }
}
