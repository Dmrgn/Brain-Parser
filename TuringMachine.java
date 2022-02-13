/**
 * TuringMachine
 * @author Daniel Morgan
 * @description Implements the turing archatecture with basic methods to interact with it
 */
public class TuringMachine {
    // the defualt size of a tape
    final private static int DEFUALT_SIZE = 1000;
    // pointer to the current location on the tape
    private int pointer;
    // array of values traversable by the turing machine
    private long tape[]; 
    // constructor to create a turing machine with tape.length = size
    public TuringMachine(int size) {
        tape = new long[size];
    }
    // constructor to create a turing machine with 1000 tape size
    public TuringMachine() {
        tape = new long[DEFUALT_SIZE];
    }
    // writes an error message to the console and then exits the program
    protected void crash(String message) {
        System.out.println("[ERROR]: "+message);
        System.exit(1);
    } 
    // returns true if the pointer is within the tape limits
    private boolean isPointerValid() {
        return (pointer >= 0 && pointer < tape.length);
    }
    // moves the pointer n spaces and returns the value there (positive n to the right, negetive n to the left)
    // crashes the program if the new pointer position is in an invalid location
    public long traverse(int n) {
        pointer+=n;
        if (!isPointerValid())
            crash("The pointer is in an invalid position: " + pointer);
        return tape[pointer];
    }
    // sets the value at the current position on the tape
    public void set(long val) {
        tape[pointer] = val;
    }
    // reads the value at the current position on the tape and returns it
    public long get() {
        return tape[pointer];
    }
    // sets the pointers position
    public void setPointer(int newPos) {
        pointer = newPos;
    }
}