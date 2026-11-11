Task 4.1 – Locate the Defect (2 Marks)
The main defect is the incorrect method name:
public void mouseclicked(MouseEvent e)
The correct method name is:
public void mouseClicked(MouseEvent e)
Explanation:
1.	Java method names are case-sensitive.
2.	MouseAdapter provides a method named mouseClicked().
3.	The incorrectly named mouseclicked() does not override the actual event-handler method.
4.	Therefore, the mouse-click event is never handled by this method.
5.	Since the handler is not executed, the counter is not incremented and the label remains "Clicks: 0".
Another issue: The original code uses getGraphics() to draw directly on the window. This is not a reliable way to maintain drawings because the graphics can be cleared during repainting.
Task 4.2 – Construct a Failing Interaction (2 Marks)
Even after correcting the method name, the drawing can disappear.
Interaction to reproduce the problem:
1.	Run the drawing-pad application.
2.	Click several positions inside the window.
3.	Observe that dots appear.
4.	Minimise the window.
5.	Restore the window.
6.	Observe that the previously drawn dots may disappear.
Reason:
The original implementation draws dots directly using getGraphics(). It does not permanently store their positions.
When the window is minimised and restored, the operating system may request the component to repaint. During repainting, the application redraws only what is provided by its painting logic. Since the earlier points were not stored, the dots cannot be restored.
Conclusion: Drawing information must be stored in a collection and redrawn whenever paint() is called.
Task 4.3 – Repair the Program (2 Marks)
Solution: Store every clicked position in an ArrayList<Point> and redraw all stored points inside the paint() method.
Complete Java code
Create Task04/DrawPad.java.

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class DrawPad extends Frame {

    private final List<Point> points = new ArrayList<>();
    private final Label status = new Label("Clicks: 0");

    public DrawPad() {

        super("Draw Pad");

        add(status, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                // Store the clicked position
                points.add(e.getPoint());

                // Update the click counter
                status.setText("Clicks: " + points.size());

                // Request repainting
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(400, 300);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        // Draw all stored points
        for (Point p : points) {
            g.fillOval(
                p.x - 3,
                p.y - 3,
                6,
                6
            );
        }
    }

    public static void main(String[] args) {

        new DrawPad().setVisible(true);
    }
}
How the corrected program works:
1.	The user clicks inside the drawing window.
2.	mouseClicked() receives the mouse event.
3.	e.getPoint() obtains the clicked position.
4.	The point is stored in the ArrayList.
5.	The label is updated using points.size().
6.	repaint() requests the window to redraw.
7.	paint() draws all previously stored points.
Compile and run:
javac DrawPad.java
java DrawPad
Expected output behaviour:
java - Drawing super crowded points in Swing - Stack Overflow
•	First click: Clicks: 1
•	Second click: Clicks: 2
•	Third click: Clicks: 3
•	After minimising and restoring: all stored dots should be drawn again.
Task 4.4 – Prevent Recurrence (1 Mark)
The @Override annotation informs the compiler that a method is intended to override a method from its parent class or interface.
If we write:
@Override
public void mouseclicked(MouseEvent e) {
    // Event handling code
}
The compiler identifies that mouseclicked() does not correctly override the mouseClicked() method.
Expected compiler message:
error: method does not override or implement
a method from a supertype
Without @Override, the misspelled method may compile as a separate method, but the mouse-click event will not invoke it.
Conclusion: Using @Override helps detect incorrect method names and prevents event-handling errors during compilation.
Alternative Concept and Complexity Analysis
Alternative: Instead of storing individual points in an ArrayList, use an off-screen BufferedImage to store the drawing.
Approach 1 – ArrayList of Points
•	Store every clicked coordinate as a Point.
•	During every repaint, iterate through the list.
•	Draw all stored dots.
for (Point p : points) {
    g.fillOval(p.x - 3, p.y - 3, 6, 6);
}
Approach 2 – BufferedImage
•	Create an off-screen image with the required window dimensions.
•	Obtain its Graphics2D object.
•	Draw each new dot directly onto the image when the user clicks.
•	During repainting, copy the stored image onto the window.
Example:
BufferedImage image =
    new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

Graphics2D g2 = image.createGraphics();

g2.fillOval(x - 3, y - 3, 6, 6);

g2.dispose();
During repainting:
g.drawImage(image, 0, 0, null);
Complexity comparison
Let:
•	\(n\) = number of clicked points.
•	\(W\) = image width.
•	\(H\) = image height.
Factor	ArrayList of Points	BufferedImage
Adding a click	\(O(1)\) amortized	\(O(1)\) drawing operation
Repainting	\(O(n)\)	\(O(W \times H)\) pixel-copy work
Additional storage	\(O(n)\)	\(O(W \times H)\) pixels
Repeated drawing	Redraws every point	Reuses existing image
Implementation	Simple	Requires image and graphics management
Conclusion:
The ArrayList<Point> approach is suitable for a simple drawing application with a relatively small number of clicks. A BufferedImage is useful when there are many drawing operations and repeated repainting is required. It avoids recalculating every individual dot during repainting but consumes memory proportional to the image dimensions.
