/**
 * TuringMachine
 * 
 * @author Daniel Morgan
 * @description Implements the turing archatecture with basic methods to
 *              interact with it
 */
public class TuringMachine {
    /** The defualt size of a tape */
    final private static int DEFUALT_SIZE = 1000;
    /** The length of the tape */
    private int tapeLength;
    /** Pointer to the current location on the tape */
    private int pointer;
    /** Array of values traversable by the turing machine */
    private long tape[];
    /** How many low level operations this machine has performed */
    protected int lowOps = 0;
    /** How many high level operations this machine has performed */
    protected int highOps = 0;

    /**
     * Constructor to create a turing machine with tape.length = size
     * 
     * @see #TuringMachine()
     * @see BrainParser
     * @param size The length of the tape to be created with the machine
     */
    public TuringMachine(int size) {
        tape = new long[size];
        tapeLength = size;
    }

    /**
     * Constructor to create a turing machine with the {@link #DEFUALT_SIZE defualt
     * tape size}.
     * 
     * @see #TuringMachine()
     * @see BrainParser
     * @param size The length of the tape to be created with the machine
     */
    public TuringMachine() {
        tape = new long[DEFUALT_SIZE];
        tapeLength = DEFUALT_SIZE;
    }

    /**
     * Writes an error message to the console and then exits the program
     * 
     * @param message The message to crash the program with.
     * @return An empty abys for the few bits and bytes not cleaned up by the
     *         garbage collector to float through in eternal darkness
     */
    protected void crash(String message) {
        System.out.println("[ERROR]: " + message);
        System.exit(1);
    }

    /**
     * Sets the pointers position. Crashes the program if the new pointer position
     * is in an invalid location.
     * 
     * @param newPos The index in the {@link #tape tape array} to attempt to set the
     *               pointer to.
     */
    public void setPointer(int newPos) {
        // increase number of low ops
        lowOps += Math.abs(newPos - pointer);
        pointer = newPos;
        if (!isPointerValid())
            crash("The pointer is in an invalid position: " + pointer);
    }

    /**
     * Gets the pointers current position on the tape and returns it.
     * 
     * @return The pointers current position.
     */
    // TODO: implement
    public int getPointer() {
        // increase number of high ops
        highOps++;
        return pointer;
    }

    // TODO: implement
    /**
     * Changes the tape length by the specified amount
     * 
     * @param addlength
     */
    public void changeTapeLength(int addlength) {
        // increase number of high ops
        highOps++;
        long[] newtape = new long[tape.length + addlength];
        // check if removing cells or adding cells
        if (newtape.length > tape.length)
            // copy cells over
            for (int i = 0; i < tape.length; i++)
                newtape[i] = tape[i];
        else
            // removes cells at end of tape
            for (int i = 0; i < newtape.length; i++)
                newtape[i] = tape[i];
        tape = newtape;
        tapeLength = tape.length;
    }

    // TODO: implement
    /**
     * Attempts to set the tape length to the specified value.
     * 
     * @param newlength The new length to attempt to set the length of the tape to.
     */
    public void setTapeLength(int newlength) {
        // increase number of high ops
        highOps++;
        changeTapeLength(newlength - tape.length);
    }

    // TODO: implement
    /**
     * Gets the length of the tape.
     * 
     * @return The length of the tape.
     */
    public int getTapeLength() {
        // increase number of high ops
        highOps++;
        return tape.length;
    }

    // TODO: implement
    /**
     * Sets the tape which is currently being read to a new tape.
     * 
     * @param newTape The tape to set as the current tape.
     */
    public void setTape(long[] newTape) {
        // increase number of high ops
        highOps++;
        tape = newTape.clone();
    }

    // TODO: implement
    /**
     * Gets the current tape.
     * 
     * @return The current tape.
     */
    public long[] getTape() {
        // increase number of high ops
        highOps++;
        return tape;
    }

    /**
     * The the value of the cell on {@link #tape current tape} beneath the
     * {@link #pointer pointer}.
     * 
     * @param val The value to set the cell under the pointer to.
     */
    public void set(long val) {
        // increase number of low ops
        lowOps++;
        tape[pointer] = val;
    }

    /**
     * Gets the value at the current {@link #pointer position} on the {@link #tape
     * current tape} and returns it.
     * 
     * @return The value at the current {@link #pointer position} of the
     *         {@link #tape current tape}.
     */
    public long get() {
        // increase number of low ops
        lowOps++;
        return tape[pointer];
    }

    /**
     * Moves the pointer n spaces and returns the value there (positive n to the
     * right, negetive n to the left). Crashes the program if the new pointer
     * position is in an invalid location.
     * 
     * @param n The number of spaces to move the {@link #pointer pointer} along the
     *          {@link #tape tape}
     * @return The value at the current {@link #pointer position} of the
     *         {@link #tape current tape}.
     */
    public long traverse(int n) {
        pointer += n;
        if (!isPointerValid())
            crash("The pointer is in an invalid position: " + pointer);
        // increase number of low ops
        lowOps++;
        return tape[pointer];
    }

    /**
     * Statistics on the number of low level operations.
     * 
     * @return Statistics on the number of low level operations.
     */
    public int getLowOps() {
        return lowOps;
    }

    /**
     * Statistics on the number of high level operations.
     * 
     * @return Statistics on the number of low level operations.
     */
    public int getHighOps() {
        return highOps;
    }

    /**
     * Utility function to check if the pointer is at a valid position on the tape.
     * 
     * @return Returns a boolean denoting whether the {@link #pointer pointer} is at
     *         a valid position on the tape or not.
     */
    private boolean isPointerValid() {
        return (pointer >= 0 && pointer < tape.length);
    }
}