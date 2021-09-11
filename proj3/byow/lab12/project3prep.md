# Project 3 Prep

**For tessellating hexagons, one of the hardest parts is figuring out where to place each hexagon/how to easily place hexagons on screen in an algorithmic way.
After looking at your own implementation, consider the implementation provided near the end of the lab.
How did your implementation differ from the given one? What lessons can be learned from it?**

Answer: My implementation is a bit too complex. For individual hexigons add operations, I implement two helper function. One dealing with the upper half,
one deal with the lower half. Each of the half first increase the index of the horizontol coordinates due to blanks, and then iterte and add each coordinates
with colors or walls after for specific numbers; and then, finally, it will return to the next recursion and increase the verticle coordinates. For the Tesselation,
my plan (haven't fully finished yet) was to view the scenerio as a bigger hexagon, doing the same process as the single hexagon with few twickes in coordinates and number of hexagons in rows. 

-----

**Can you think of an analogy between the process of tessellating hexagons and randomly generating a world using rooms and hallways?
What is the hexagon and what is the tesselation on the Project 3 side?**

Answer: For tessellating hexagons we set a bunch of rules for generation of the hexagons: where can we add a set of hexgons. Similarly,
for rooms and hallways, they own have their own set of generation rules: 1. there souldn't be a room that is unreachable, 2. the hall way
can only have 1-2 width, 3. all the rooms and hallways need to be inclosed by set of walls. Like when I implement the hexogon generation, I
view the hexgons in different levels, seperate them into different parts. For this project, we can view the rooms and hallways seperate objects.
Building an algorithm to connect the rondomly geenerated rooms, similar to my plan of randomily generating hexogons in a predeterminent index. 

-----
**If you were to start working on world generation, what kind of method would you think of writing first? 
Think back to the lab and the process used to eventually get to tessellating hexagons.**

Answer: First would be the ininitializer which first by manking the world blank to avoid generation erorrs. Than would possibily be the 
generating algorithms for the hallways and rooms. After all that is define how to connect each objects, how to add them into the real world. 

-----
**What distinguishes a hallway from a room? How are they similar?**

Answer: They are the same that they all enclosed by a wall, and have the ground different from the wall, and all of both objects should have a space size
from 1 to 2 as the entrence for connection. They are different in the sense that hallway's path should be equally width throughout, but the rooms doesn't,
it only reuire the space except the entrence need to be equal width throughout. 
