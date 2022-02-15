/**
 * TuringMachine
 * @author Daniel Morgan
 * @description Implements the turing archatecture with basic methods to interact with it
 */
public class TuringMachine {
    // the defualt size of a tape
    final private static int DEFUALT_SIZE = 1000;
    // the length of the tape
    private int tapeLength;
    // pointer to the current location on the tape
    private int pointer;
    // array of values traversable by the turing machine
    private long tape[];
    // how many low level operations this machine has performed
    protected int lowOps = 0;
    // how many high level operations this machine has performed
    protected int highOps = 0;
    // constructor to create a turing machine with tape.length = size
    public TuringMachine(int size) {
        tape = new long[size];
        tapeLength = size;
    }
    // constructor to create a turing machine with 1000 tape size
    public TuringMachine() {
        tape = new long[DEFUALT_SIZE];
        tapeLength = DEFUALT_SIZE;
    }
    // writes an error message to the console and then exits the program
    protected void crash(String message) {
        System.out.println("[ERROR]: "+message);
        System.exit(1);
    }
    // sets the pointers position
    // crashes the program if the new pointer position is in an invalid location
    public void setPointer(int newPos) {
        // increase number of low ops
        lowOps+=Math.abs(newPos-pointer);
        pointer = newPos;
        if (!isPointerValid())
            crash("The pointer is in an invalid position: " + pointer);
    }
    // gets the pointers current position on the tape
    // TODO: implement
    public int getPointer() {
        // increase number of high ops
        highOps++;
        return pointer;
    }
    // TODO: implement
    // changes the tape length by the specified amount
    public void changeTapeLength(int addlength) {
        // increase number of high ops
        highOps++;
        long[] newtape = new long[tape.length+addlength];
        // check if removing cells or adding cells
        if (newtape.length > tape.length)
            // copy cells over
            for (int i = 0 ; i < tape.length; i++)
                newtape[i] = tape[i];
        else
            // removes cells at end of tape
            for (int i = 0 ; i < newtape.length; i++)
                newtape[i] = tape[i];
        tape = newtape;
        tapeLength = tape.length;
    }
    // TODO: implement
    // sets the tape length
    public void setTapeLength(int newlength) {
        // increase number of high ops
        highOps++;
        changeTapeLength(newlength-tape.length);
    }
    // TODO: implement
    // get the length of the tape
    public int getTapeLength() {
        // increase number of high ops
        highOps++;
        return tape.length;
    }
    // TODO: implement
    // sets the currently read tape
    public void setTape(long[] newTape) {
        // increase number of high ops
        highOps++;
        tape = newTape.clone();
    }
    // TODO: implement
    // returns the current tape
    public long[] getTape() {
        // increase number of high ops
        highOps++;
        return tape;
    }
    // sets the value at the current position on the tape
    public void set(long val) {
        // increase number of low ops
        lowOps++;
        tape[pointer] = val;
    }
    // reads the value at the current position on the tape and returns it
    public long get() {
        // increase number of low ops
        lowOps++;
        return tape[pointer];
    }
    // moves the pointer n spaces and returns the value there (positive n to the right, negetive n to the left)
    // crashes the program if the new pointer position is in an invalid location
    public long traverse(int n) {
        pointer+=n;
        if (!isPointerValid())
            crash("The pointer is in an invalid position: " + pointer);
        // increase number of low ops
        lowOps++;
        return tape[pointer];
    }
    // returns statistics on the number of low level operations
    public int getLowOps() {
        return lowOps;
    }
    // returns statistics on the number of high level operations
    public int getHighOps() {
        return highOps;
    }
    // returns true if the pointer is within the tape limits
    private boolean isPointerValid() {
        return (pointer >= 0 && pointer < tape.length);
    }
}