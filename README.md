# Conway's Game of Life - Scala

A scala implementation of Conway's Game of Life. This implementation takes
in an input file and returns the result after a given number of iterations
or ticks. The the number of cells in each iteration logged to `population.out`

Created as part of CMPT 470 Winter 2021 Term.

## Compile & Execution

sbt can be used to build and run the Scala implementation GoL:

    `sbt compile`
    `sbt "run [-h][-t <ticks>][-g <geo>] <input_file> <output_file>"`

### Options
    `-h`          displays the program usage
    `-t <ticks>`  sets the number of ticks/iterations to run the program for, 
                    represented by the positive integer <ticks>
    `-g <geo>`    sets the geometry type. Valid geometry types include:
                        `f` --- flat 
                        `c` --- cylindrical
                        `t` --- toroidal 

### Arguments
    `input_file` - the path to an input text file
    `output_file` - the path to an output text file

### Example Usage
    sbt "run -t 1000 inputs.txt output.txt"

## File Format
The first line in the file should contain the number of rows, $M$, and the
number of columns, $N$, separated by a space `' '`. The rest of the
file should be $M$ by $N$ characters where the character `'*'` 
represents cells that are "Alive" and a space `' '` represents cells that 
are "Dead".

### Example
```
6 6

      
      
 ***  
      
      
```