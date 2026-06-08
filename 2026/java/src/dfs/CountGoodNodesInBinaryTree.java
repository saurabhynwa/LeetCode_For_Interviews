package src.dfs;

/*
Given a binary tree root, a node X in the tree is named good if in the path from root to X there are no nodes with a
value greater than X. Return the number of good nodes in the binary tree.

Example 1:
Input: root = [3,1,4,3,null,1,5]
Output: 4
Explanation: Nodes in blue are good.
Root Node (3) is always a good node.
Node 4 -> (3,4) is the maximum value in the path starting from the root.
Node 5 -> (3,4,5) is the maximum value in the path
Node 3 -> (3,1,3) is the maximum value in the path.

Example 2:
Input: root = [3,3,null,4,2]
Output: 3
Explanation: Node 2 -> (3, 3, 2) is not good, because "3" is higher than it.
Example 3:

Input: root = [1]
Output: 1
Explanation: Root is considered as good.

Constraints:

The number of nodes in the binary tree is in the range [1, 10^5].
Each node's value is between [-10^4, 10^4].
 */

/*
Time Complexity = O(N), N = total nodes in binary tree, each visited once exactly

Space Complexity = O(H), H is the maximum height of the tree, representing the memory allocation footprint for your
execution call stack records
 */
public class CountGoodNodesInBinaryTree {
    private int getNumberOfGoodNodes(TreeNode root){
        // root will always be the good node because it's the starting point and the value cannot be smaller than
        // nothing. So start the comparison with minimum value
        return calculateGoodNodes(root, Integer.MIN_VALUE);
    }

    private int calculateGoodNodes(TreeNode root, int maxValue){
        // Base case: An empty node contains exactly 0 good nodes
        if(root == null){
            // no good nodes
            return 0;
        }

        int currentNodeContribution = 0;
        // Evaluate the invariant constraint: Am I greater than or equal to the maximum above me?
        if(root.val >= maxValue){
            // current node is good. Add its count
            currentNodeContribution = 1;
            // update maxValue
            maxValue = root.val;
        }

        // pass this updated maxValue to child nodes
        // Divide and Conquer: Gather the total good node reports from both branches
        int leftGoodNodes = calculateGoodNodes(root.left, maxValue);
        int rightGoodNodes = calculateGoodNodes(root.right, maxValue);

        // Bubble-Up Step: Sum up your contribution plus your kids' reports to pass up the stack
        return currentNodeContribution + leftGoodNodes + rightGoodNodes;
    }

    public static void main(String[] args){
        CountGoodNodesInBinaryTree obj = new CountGoodNodesInBinaryTree();
        // [3,1,4,3,null,1,5]
        TreeNode t1 = new TreeNode(3);
        TreeNode t2 = new TreeNode(1);
        TreeNode t3 = new TreeNode(4);
        TreeNode t4 = new TreeNode(3);
        TreeNode t5 = new TreeNode(1);
        TreeNode t6 = new TreeNode(5);

        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t3.left = t5;
        t3.right = t6;

        System.out.println(obj.getNumberOfGoodNodes(t1));
    }
}
