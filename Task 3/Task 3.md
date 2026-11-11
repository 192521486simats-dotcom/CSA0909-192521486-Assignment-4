
Task 3.1 – Choose the Layout Managers (3 Marks)
The application can use nested BorderLayout and FlowLayout managers to achieve the required resizing behaviour.

Layout manager	Application area	Reason
BorderLayout	Main JFrame	Divides the window into North, Center, South, East and West regions.
FlowLayout	Search panel	Arranges the search field, category selection and button in a row.
BorderLayout	Results panel	Allows the results component in the centre to expand when the window grows.
FlowLayout	Bottom panel	Keeps Prev, Next and Reserve buttons arranged in a row.
GridLayout	Optional title/status panel	Useful for arranging components in a fixed grid when required.

Why BorderLayout is important:
•	The centre region receives the extra available space.
•	The north and south regions retain their preferred heights.
•	The search and navigation controls do not unnecessarily stretch when the window is resized.

Task 3.2 – Design the Nested Containers (2 Marks)
JFrame – Library Search
BorderLayout
North – Title Panel
JLabel: Campus Library Search
North – Search Panel (FlowLayout)
Search field
Category
Search
Center – Results Panel (BorderLayout)
JTable inside JScrollPane
Expands when the window is resized
South – Navigation Panel (FlowLayout)
Prev | Next | Reserve
Status message – JLabel

Alternative Concept and Complexity Analysis
Alternative: Use GridBagLayout or absolute positioning instead of nested BorderLayout and FlowLayout panels.

Approach 1 – GridBagLayout
GridBagLayout positions components using row, column, weight and fill constraints.
Example:
setLayout(new GridBagLayout());

GridBagConstraints gbc = new GridBagConstraints();

gbc.gridx = 0;
gbc.gridy = 0;
gbc.weightx = 1.0;
gbc.weighty = 1.0;
gbc.fill = GridBagConstraints.BOTH;

add(new JScrollPane(table), gbc);
Advantages:
•	Provides flexible component positioning.
•	Supports resizing using weight constraints.
•	Can create complex layouts with fewer nested panels.
Disadvantage: More constraints must be configured, making the layout harder to understand and maintain.

Approach 2 – Absolute Positioning
Absolute positioning uses setLayout(null) and setBounds() to specify the exact position and size of each component.
Example:
setLayout(null);

JButton search = new JButton("Search");

search.setBounds(300, 50, 100, 30);

add(search);
Advantages:
•	Gives direct control over component positions.
•	Simple for fixed-size interfaces.
Disadvantages:
•	Components do not automatically adjust when the window is resized.
•	Different screen sizes and font settings can cause overlapping.
•	Manual resize handling is required.

Comparison
Factor	BorderLayout + FlowLayout	GridBagLayout	Absolute positioning
Component arrangement	Nested regions and rows	Grid-based constraints	Fixed coordinates
Resizing	Good	Very good with correct constraints	Poor without manual handling
Layout computation	Approximately O(n)	Approximately O(n)	O(1) initial placement per component
Space complexity	O (n+c)	O (n+c)	O (n)
Maintenance	Easy	More complex	Difficult
Suitable for	Standard resizable screens	Complex responsive screens	Fixed-size screens

Here, n  represents the number of components and c represents the number of containers. These are simplified layout-computation estimates, not guaranteed implementation-level bounds.

Conclusion:
Nested BorderLayout and FlowLayout panels are suitable for the Campus Library Search Screen because they provide simple and automatic resizing. GridBagLayout is useful for more complex layouts, while absolute positioning is not recommended for a resizable application because component positions must be manually adjusted.
