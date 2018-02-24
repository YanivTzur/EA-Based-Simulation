# EA-Based-Simulation
Three-way Matching Simulation Program\
Author: Yaniv Tzur\
Date: 24/02/18\
Version: 1.0

## Description:

The program is an application of evolutionary algorithms. The program's execution is divided into
runs, each of which is in turn divided into generations.
At the beginning of each run, three sets are created: one of men, another of women and another of dogs.
Each of these "individuals" wants to form a triplet of the overall form (man, woman, dog). 
Also, each of these individuals has a list of preferences for each type of individual different than its own
type. In this list, each individual of each of the other types is given some rank, relative to the
other individuals of the same type. For example, each man ranks each of the other women and each of
the other dogs based on how much he wants to form a triplet with each of them.
At the beginning of the run, and based on the user's chosen parameters, possibly also at the beginning
of each generation, a list of preferences is randomly generated for each individual.
The goal of the program's execution is to create a three-way matching between all men, women and
dogs, such that at the end of a run, the best solution found is as better as possible than the initial
solution (matching).
This is carried out as follows. At the end of each generation, a new population of solutions
(chromosomes) of the same size as that of the last generation is created, by applying the operations
of crossover and mutation to the solutions of the previous generation (crossover can be optionally
disabled from the GUI though). Crossover creates new solutions by combining old solutions. Mutation
creates new solutions by taking an existing solution and for each gene (component) of the chromosome,
with some low probability, replacing the gene with another one chosen uniformly at random.

## Installation:
        
You will require the following dependencies:
* jcommon-1.0.23.jar
* jfreechart-1.0.19-experimental.jar
* jfreechart-1.0.19-demo.jar
* hamcrest-core-1.3.jar
* jfreechart-1.0.19.jar
* jfreesvg-2.0.jar
* junit-4.11.jar
* orsoncharts-1.4-eval-nofx.jar
* orsonpdf-1.6-eval.jar
* servlet.jar
* swtgraphics2d.jar\\

## How to Run the Program:	
As a user:\
	Double click EASimulationProgram.jar.\
From an IDE (Developer):\
	Run Main.class.

## GUI Description:
Parameters (Top Right Side of the Screen):
	* Number of Chromosomes: The number of solutions (chromosomes) maintained in each generation.
	* Preference List Generation: Whether preference lists should be generated once at the
			      	      beginning of each run, or at the beginning of each generation.
				      The latter option is supposed to produce a relatively "random"
				      behavior, as any attempt to improve the solution is rendered
				      useless, as the preference lists change in each generation.
	* Crossover Enabled:          Whether crossover should be enabled. This is a behavior
				      which combines solutions at the end of each generation
				      to create new combined solutions (chromosomes) for the
				      population of the next generation.
Statistics (Middle Right Side of the Screen):
        * Elapsed Time (sec): The time elapsed in seconds since the beginning of the last run.
        * Current Run: The serial number of the current run (starts at 0).
        * Current Generation: The serial number of the current generation in the current run (starts at 0).
        * Number of Men: The number of men in the solution space.
        * Number of Women: The number of women in the solution space.
        * Number of Dogs: The number of dogs in the solution space.

Run Control (Bottom Right Side of the Screen):
   * Number of Runs: The number of runs to execute sequentially.
   * Number of Generations: The number of generations to create per run.
   * Time Between Generations: The amount of time in milliseconds that should pass between
			       the end of one generation and the creation of the next.
			       The minimum is 50 milliseconds and the default is 1000.
   * Start: Starts a new sequence of runs with the parameters currently input in the window.
   * Continue: Continues a paused sequence of runs. If the method of generating preference lists
	       or whether crossover is enabled have been changed, this will be applied upon
	       pressing the button.
   * Stop/Pause: Pauses the current sequence of runs. You can resume by pressing "Continue".

Results (Left Side of the Screen):\
	The upper part of the screen contains a line chart showing the best, average and worst fitness
	values of the solutions in each generation of the current run.
	The fitness of a solution reflects how good is the matching between all men, women and dogs.
	The fitness of a solution is higher, the more individuals there are with mates ranked higher
	in their lists of preferences.    
	The lower part of the screen shows a textual display of the results of the current sequence of runs.
