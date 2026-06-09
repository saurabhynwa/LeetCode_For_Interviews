package src.dfs;

/*
Given the root of a binary tree, return the sum of every tree node's tilt. The tilt of a tree node is the absolute
difference between the sum of all left subtree node values and all right subtree node values. If a node does not have a
left child, then the sum of the left subtree node values is treated as 0. The rule is similar if the node does not have
a right child.

Example 1:
Input: root = [1,2,3]
    [1]             [1]
   /   \     =>    /   \
 [2]    [3]      [0]    [0]

Output: 1
Explanation:
Tilt of node 2 : |0-0| = 0 (no children)
Tilt of node 3 : |0-0| = 0 (no children)
Tilt of node 1 : |2-3| = 1 (left subtree is just left child, so sum is 2; right subtree is just right child, so sum is 3)
Sum of every tilt : 0 + 0 + 1 = 1


Example 2:
Input: root = [4,2,9,3,5,null,7]

        [4]                     [6]
       /   \                   /   \
     [2]    [9]      =>      [2]    [7]
    /   \      \            /   \      \
  [3]    [5]    [7]       [0]    [0]    [0]

Output: 15
Explanation:
Tilt of node 3 : |0-0| = 0 (no children)
Tilt of node 5 : |0-0| = 0 (no children)
Tilt of node 7 : |0-0| = 0 (no children)
Tilt of node 2 : |3-5| = 2 (left subtree is just left child, so sum is 3; right subtree is just right child, so sum is 5)
Tilt of node 9 : |0-7| = 7 (no left child, so sum is 0; right subtree is just right child, so sum is 7)
Tilt of node 4 : |(3+5+2)-(9+7)| = |10-16| = 6 (left subtree values are 3, 5, and 2, which sums to 10; right subtree values are 9 and 7, which sums to 16)
Sum of every tilt : 0 + 0 + 0 + 2 + 7 + 6 = 15


Example 3:
Input: root = [21,7,14,1,1,2,2,3,3]
Output: 9

Constraints:

The number of nodes in the tree is in the range [0, 104].
-1000 <= Node.val <= 1000
 */

/*
Time Complexity = O(N)
Space Complexity = O(H)
 */

public class BinaryTreeTilt {
    private int tiltSum;

    private int findTilt(TreeNode root) {
        dfs(root);
        return tiltSum;
    }

    private int dfs(TreeNode root){
        if(root == null){
            return 0;
        }

        int left = dfs(root.left);
        int right = dfs(root.right);

        tiltSum += Math.abs(left - right);

        return left + right + root.val;
    }

    /*
    If two parallel application worker threads call findTilt on different trees at the exact same time inside an
    enterprise service mesh, their executions will overwrite tiltSum concurrently, corrupting the metrics. Can you
    refactor this to be entirely stateless and thread-safe without a global variable?
     */
    private int findTiltThreadSafe(TreeNode root){
        // Core pass: Returns an array where index 0 is Sum, and index 1 is Total Tilt.
        return computeSumAndTiltStateless(root)[1];
    }

    private int[] computeSumAndTiltStateless(TreeNode root) {
        // Base Case: An empty node contributes 0 to both Sum and Tilt.
        if (root == null) {
            return new int[]{0, 0};
        }

        int[] left = computeSumAndTiltStateless(root.left);
        int[] right = computeSumAndTiltStateless(root.right);

        // for the subtree root node, let's first secure the sum of itself and, it's child node that we want to bubble
        // up through index 0;
        int currentSubTreeSum = root.val + left[0] + right[0];

        // now for tilt there are 2 cases. One is the running total and second is the local total. Let's get the local
        // tilt first. It is the absolute difference of sum of left subtree nodes and right subtree nodes. We have this
        // data computed already.
        int currentNodeTilt = Math.abs(left[0] - right[0]);

        // Now we have the tilt from left and right subtrees in the first index. We get both of them and sum it up with
        // the current tilt and bubble that aggregated sum through index 1.
        int sumOfTiltTillNow = currentNodeTilt + left[1] + right[1];

        // now bubble up the sum of subtree and sum of tilts
        return new int[]{currentSubTreeSum, sumOfTiltTillNow};
    }

    public static void main(String[] args){
        BinaryTreeTilt binaryTreeTilt = new BinaryTreeTilt();
        TreeNode t1 = new TreeNode(4);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(9);
        TreeNode t4 = new TreeNode(3);
        TreeNode t5 = new TreeNode(5);
        TreeNode t6 = new TreeNode(7);

        t1.left = t2;
        t1.right = t3;
        t2.left = t4;
        t2.right = t5;
        t3.right = t6;

        // copy of head
        TreeNode head1 = t1;
        System.out.println(binaryTreeTilt.findTilt(head1));
        System.out.println(binaryTreeTilt.findTiltThreadSafe(t1));

    }
}
