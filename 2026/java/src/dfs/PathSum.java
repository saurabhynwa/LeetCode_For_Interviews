package src.dfs;

/*
Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that
adding up all the values along the path equals targetSum. A leaf is a node with no children.

Example 1:
Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22

            [5]
           /   \
         [4]    [8]
        /       /   \
      [11]     [13]  [4]
     /   \              \
   [7]    [2]           [1]

Output: true

Explanation: The root-to-leaf path with the target sum is shown.
Example 2:


Input: root = [1,2,3], targetSum = 5
Output: false
Explanation: There are two root-to-leaf paths in the tree:
(1 --> 2): The sum is 3.
(1 --> 3): The sum is 4.
There is no root-to-leaf path with sum = 5.
Example 3:

Input: root = [], targetSum = 0
Output: false
Explanation: Since the tree is empty, there are no root-to-leaf paths.


Constraints:

The number of nodes in the tree is in the range [0, 5000].
-1000 <= Node.val <= 1000
-1000 <= targetSum <= 1000
 */

/*
Time Complexity: O(N) because in the worst-case scenario (where no path matches or the valid path is at the very last
leaf), the recursion engine visits every single node in the tree exactly once.

Space Complexity: O(H) where H is the height of the tree.
 */
public class PathSum {
    private boolean hasPathSum(TreeNode root, int targetSum){
        // intuition here is to subtract the root's value from the targetSum and pass it down the child node. If we
        // found a terminal node we check whether the target sum at that point is zero or not and return the boolean
        // based on that. Since we are doing DFS, we will first hit the null node on the left side. So for null node on
        // left or right node, we return false. This is because a null node doesn't have a 'val' component, so there is
        // nothing to compare.

        // In this DFS, while going down we pass the updated reducedSum in form of targetSum. And the child node bubble
        // up the true and false answer till the root. Another trick to write recursion is to write code as per stack
        // call. Rearrange them once you have the logic laid out. If we go writing chronologically, recursion will
        // always be difficult to see.

        // when we are at null node, no 'val' component to reduce, so return false
        if(root == null){
            return false;
        }

        // We are on a not null node. Subtract current val from targetSum
        targetSum -= root.val;

        // check if we are on terminal node
        if(root.left == null && root.right == null){
            return targetSum == 0;
        }

        // now pass this updated targetSum to left and right child. Use '||' because if we find even one side with an
        // answer, that's good enough for us.
        return hasPathSum(root.left, targetSum) || hasPathSum(root.right, targetSum);
    }

    public static void main(String[] args){
        TreeNode t1 = new TreeNode(5);
        TreeNode t2 = new TreeNode(4);
        TreeNode t3 = new TreeNode(11);
        TreeNode t4 = new TreeNode(7);
        TreeNode t5 = new TreeNode(2);
        TreeNode t6 = new TreeNode(8);
        TreeNode t7 = new TreeNode(13);
        TreeNode t8 = new TreeNode(4);
        TreeNode t9 = new TreeNode(1);

        t1.left = t2;
        t1.right = t6;
        t2.left = t3;
        t3.left = t4;
        t3.right = t5;
        t6.left = t7;
        t6.right = t8;
        t8.right = t9;

        PathSum pathSum = new PathSum();
        System.out.println(pathSum.hasPathSum(t1, 22));
    }
}
