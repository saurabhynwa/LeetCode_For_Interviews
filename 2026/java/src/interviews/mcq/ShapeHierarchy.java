package src.interviews.mcq;

/*
Company - AlphaSense

MCQ question - Given 4 shapes: Square, Rectangle, Parallelogram, Polygon. Arrange shapes in correct hierarchy w.r.t
inheritance. Start with base and then derived.

Answer: Polygon -> Parallelogram -> Rectangle -> Square

Explanation:
1) To arrange these shapes in the correct object-oriented inheritance hierarchy, you need to go from the most generic
definition (the base class) to the most specific definition (the derived class).

2) The correct hierarchy from base to derived is:
    (i) Polygon (The base class)
    (ii) Parallelogram (Derived from Polygon)
    (iii) Rectangle (Derived from Parallelogram)
    (iv) Square (The most derived/specific class)

3) The "Is-A" Test
In object-oriented programming, a child class must always pass the "Is-A" test relative to its parent class.
    i. Polygon is any closed flat shape with straight sides. It is the most abstract concept here.
    ii. A Parallelogram is a Polygon with 4 sides where opposite sides are parallel.
    iii. A Rectangle is a Parallelogram where all 4 internal angles are exactly 90 degrees.
    iv. A Square is a Rectangle where all 4 sides are of equal length. It is the absolute most specific shape in this group.
 */

public class ShapeHierarchy {
}
