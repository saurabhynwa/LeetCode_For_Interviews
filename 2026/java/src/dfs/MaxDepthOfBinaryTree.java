package src.dfs;

/*
Given the root of a binary tree, return its maximum depth. A binary tree's maximum depth is the number of nodes along
the longest path from the root node down to the farthest leaf node.

Example 1:
        [3]
       /   \
     [9]    [20]
           /    \
         [15]    [7]

Input: root = [3,9,20,null,null,15,7]
Output: 3
Example 2:

Input: root = [1,null,2]
Output: 2

Constraints:

The number of nodes in the tree is in the range [0, 104].
-100 <= Node.val <= 100
 */

/*
Time Complexity: O(N) because the recursion engine visits every single node in the binary tree exactly once.

Space Complexity: O(H) where (H) is the height of the tree. In the absolute worst-case scenario (a skewed tree that acts
 like a linked list), the space complexity scales to O(N) due to the call stack frames.
 */

// sketch out the code on paper using stack, it will become clear and self-explanatory.
public class MaxDepthOfBinaryTree {
    private int getMaxDepthOfBinaryTree(TreeNode root){
        // edge case + recursion end scenario. Recursion ends when there is no node. And when there is no node we
        // shouldn't return anything.
        if(root == null){
            return 0;
        }

        /*
        The root counts the max level from left and right child and adds '1' to it for its own. So when we hit null, the
        root node gets zero from left first. Then it starts searching in right, it gets zero again. Now it takes max of
        both, which is zero. It adds one to the current depth for itself. So if you see this function has 2 types of
        return.

        1) The null return check tells that the part of subtree has reaches its end. Returning zero signifies that.
        2) The last return is literally 'backtracking' done by the currentRoot.

        The root then passes it's depth to its higher root which had called it. The upper root takes the max from its
        left and right child and add '1' to it for itself.
         */

        // Deep dive left and right. The nodes don't care what height came before them!
        int leftDepth = getMaxDepthOfBinaryTree(root.left);
        int rightDepth = getMaxDepthOfBinaryTree(root.right);

        // Post-Order Step: Gather the kids' reports, pick the tallest, and add 1 for yourself
        return Math.max(leftDepth, rightDepth) + 1;
    }
}
