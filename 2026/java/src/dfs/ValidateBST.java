package src.dfs;

/*
Given the root of a binary tree, determine if it is a valid binary search tree (BST). A valid BST is defined as follows:

- The left subtree of a node contains only nodes with keys strictly less than the node's key.
- The right subtree of a node contains only nodes with keys strictly greater than the node's key.
- Both the left and right subtrees must also be binary search trees.

Example 1:
        [2]
       /   \
     [1]    [3]

Input: root = [2,1,3]
Output: true

Example 2:
            [5]
           /   \
         [1]    [4]
               /   \
             [3]    [6]

Input: root = [5,1,4,null,null,3,6]
Output: false
Explanation: The root node's value is 5 but its right child's value is 4.

Constraints:

The number of nodes in the tree is in the range [1, 104].
-231 <= Node.val <= 231 - 1
 */

/*
Time Complexity: O(N)

Space Complexity: O(H)
 */
public class ValidateBST {
    private boolean isValidBST(TreeNode root){
        // for a valid BST each node must follow this rule: left node strictly smaller and right node strictly greater.
        // so for every node there is a range. We pass down the range with DFS and bubble up the truth.
        // Root is always valid because it doesn't have any predecessor node. So you take the large ends for low & high.
        // Leaf nodes by themselves are always valid because their successor nodes are null.

        // Why use Long and not int for MIN and MAX values ?
        // Ans: Given constraint: -2 ^ 31 <= Node.val <= 2 ^ 31 - 1. So node value is integer min and max inclusive. If
        // root or leaf node is any of this value, then the breaking condition check triggers and rejects a perfectly
        // valid BST. So we expand our boundaries, hence the use of Long.
        return dfs(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean dfs(TreeNode root, long low, long high){
        if(root == null){
            return true;
        }

        // define breaking condition. Only one wrong/breaking condition is needed for discarding the node.
        if(root.val <= low || root.val >= high){
            return false;
        }

        // now pass the updated low and high value to child nodes.
        // for left node current root.val becomes the high and for right it becomes the low
        boolean leftValue = dfs(root.left, low, root.val);
        boolean rightValue = dfs(root.right, root.val, high);

        return leftValue && rightValue;
    }

    public static void main(String[] args){
        ValidateBST validateBST = new ValidateBST();
        TreeNode t1 = new TreeNode(2);
        TreeNode t2 = new TreeNode(1);
        TreeNode t3 = new TreeNode(3);

        t1.left = t2;
        t1.right = t3;

        System.out.println(validateBST.isValidBST(t1));

        TreeNode t4 = new TreeNode(5);
        TreeNode t5 = new TreeNode(1);
        TreeNode t6 = new TreeNode(4);
        TreeNode t7 = new TreeNode(3);
        TreeNode t8 = new TreeNode(6);

        t4.left = t5;
        t4.right = t6;
        t6.left = t7;
        t6.right = t8;

        System.out.println(validateBST.isValidBST(t4));
    }
}
