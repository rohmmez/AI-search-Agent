# AI-search-Agent

This is a programming assignment in which we apply AI search techniques to solve some sophisticated 3D Mazes.

Each 3D maze is a grid of points (not cells) with (x, y, z) locations in which our agent may use one of the 18 elementary actions (see their definitions below), named X+, X-, Y+, Y-, Z+, Z-; X+Y+, X-Y+, X+Y-, X-Y-, X+Z+, X+Z-, X-Z+, X-Z-, Y+Z+, Y+Z-, Y-Z+, Y-Z-; to move to one of the 18 neighboring grid point locations.

At each grid point, our agent is given a list of actions that are available for the current point our agent is at. Our agent can select and execute one of these available actions to move inside the 3D maze.

We name or encode these actions as follows:

Act: X+ X- Y+ Y- Z+ Z- X+Y+ X+Y- X-Y+ X-Y- X+Z+ X+Z- X-Z+ X-Z- Y+Z+ Y+Z- Y-Z+ Y-Z-

Code: 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18


To find the solution we will use the following algorithms:

- Breadth-first search (BFS)

- Uniform-cost search (UCS)

- A* search (A*).

Our algorithm should return an optimal path, that is, with shortest possible operational path length. Operational path length is further described below and may not always be equal to geometric path length as per the specifications given ahead. If an optimal path cannot be found, our algorithm should return “FAIL” as further described below.

● Breadth-first search (BFS)

In BFS, each move from one location to any of its neighbors counts for a unit path cost of 1. We do not need to worry about the fact that moving diagonally actually is a bit longer than moving along the North/South, East/West, and Up/Down directions. So, any allowed move from one location to an adjacent location costs 1.

● Uniform-cost search (UCS)

When running UCS, we should compute unit path costs in any of the 2D plane XY, XZ, YZ, on which we are moving. Let us assume that a grid location’s center coordinates projected to a 2D plane are spaced by a 2D distance of 10 units on X and Z plane respectively. That is, on the XZ plane, move from a grid location to one of its 4-connected straight neighbors incurs a unit path cost of 10, while a diagonal move to a neighbor incurs a unit path cost of 14 as an approximation to 10√𝟐 when running UCS.

● A* search (A*).

When running A*, we should compute an approximate integer unit path cost of each move as in the UCS case (unit cost of 10 when moving straight on a plane, and unit cost of 14 when moving diagonally). Notice for A*, we need to design an admissible heuristic for A* for this problem.


Input: The file input.txt in the current directory of our program will be formatted as follows:

● First line: Instruction of which algorithm to use, as a string: BFS, UCS or A*

● Second line: Three strictly positive 32-bit integers separated by one space character, for the size of X, Y, and Z dimensions, respectively.

● Third line: Three non-negative 32-bit integers for the entrance grid location.

● Fourth line: Three non-negative 32-bit integers for the exit grid location.

● Fifth line: A strictly positive 32-bit integer N, indicating the number of grids in the maze where there are actions available.

● Next N lines: Three non-negative 32-bit integers separated by one space character, for the location of the grid, followed by a list of actions that are available at this grid. The grid location is guaranteed to be legal and within the maze.

Output: The file output.txt that our program creates in the current directory should be formatted as follows:

● First line: A single integer C, indicating the total cost of our found solution. If no solution was found (the exit grid location is unreachable from the given entrance, then write the word “FAIL” (all capital) without any other lines following.

● Second line: A single integer N, indicating the total number of steps in our solution including the starting position.

● N lines:
Report the steps in our solution travelling from the entrance grid location to the exit grid location as were given in the input.txt file.
Write out one line per step with cost. Each line should contain a tuple of four integers: X, Y, Z, Cost, separated by a space character, specifying the grid location with the single step cost to visit that grid location by our agent from its last grid during its traveling from the entrance to the exit.

In the end, we have some input and output examples in the file.
