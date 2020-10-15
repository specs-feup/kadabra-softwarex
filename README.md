# kadabra-softwarex
Support repository for the SoftwareX publication about Kadabra

# Example

## SortWithTimer


## SortWithAutotuner

This example insturments the code so that when running tries several sorting algorithms, deciding for each problem size which algorithm to use. 

To run this example, open a command-line in the folder 'run' and launch the script 'SortWithAutotuner'.

This script will execute a Kadabra configuration over the Java code in the folder 'src-sorts', and write the modified code in the folder 'generated/SortWithAutotuner/src'.

The folder 'generated/SortWithAutotuner' already contains an Eclipse projet configuration with the necessary dependencies required by the generated code (e.g., RAAPI), which is compiled with the application 'eclipse-build'.

Finally, the script executes the instrumented program (i.e., SortWithAutotuner.jar).