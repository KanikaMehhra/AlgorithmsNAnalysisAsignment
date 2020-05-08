import java.io.PrintWriter;
import java.lang.String;


/**
 * Implementation of the Runqueue interface using an Ordered Array.
 *
 * Your task is to complete the implementation of this class.
 * You may add methods and attributes, but ensure your modified class compiles and runs.
 *
 * @author Sajal Halder, Minyi Li, Jeffrey Chan
 */
public class OrderedArrayRQ implements Runqueue {    
    private Proc[] procArray;
    /**
     * Constructs empty queue
     */
    public OrderedArrayRQ() {
        // Implement Me
        procArray = new Proc[0];
    }  // end of OrderedArrayRQ()


    @Override
    public void enqueue(String procLabel, int vt) {
        // Implement me
        boolean bool = true;
        Proc newProc = new Proc(procLabel, vt);
        // If there is no proc in the array yet:
        if (procArray.length == 0) {
            Proc[] newArray = new Proc[1];
            newArray[0] = newProc;
            procArray = newArray;
        } else {
            for (int i = 0; i < procArray.length; i++) {
                if (procArray[i].getLabel().compareTo(procLabel) == 0) {
                    System.out.println("");
                    System.out.println("Pick a different process label.");
                    System.out.println("");
                    bool = false;
                }
            }
            if (bool == true) {
                procArray = enqNewArray(newProc);
            }
        }
        
    } // end of enqueue()


    @Override
    public String dequeue() {
        // Implement me
        if (procArray.length == 0) {
            return "";
        }
        else {
            String label = procArray[0].getLabel();
            procArray = deqNewArray();
            return label;
        }
    } // end of dequeue()


    @Override
    public boolean findProcess(String procLabel){
        // Implement me
        for (int i = 0; i < procArray.length; i++) {
            if (procArray[i].getLabel().compareTo(procLabel) == 0) {
                return true;
            }
        }
        return false;
    } // end of findProcess()


    @Override
    public boolean removeProcess(String procLabel) {
        // Implement me
        if (!findProcess(procLabel)) {
            return false;
        }
        Proc[] newArray = new Proc[(procArray.length - 1)];
        int i = 0;
        for (Proc process : procArray) {
            if (process.getLabel().compareTo(procLabel) != 0) {
                newArray[i] = process;
                i++;
            }
        }
        procArray = newArray;
        return true;
    } // end of removeProcess()


    @Override
    public int precedingProcessTime(String procLabel) {
        // Implement me
        int pTime = 0;
        if (findProcess(procLabel)) {
            for (int i = 0; i < procArray.length; i++) {
                if (procArray[i].getLabel().compareTo(procLabel) == 0) {
                    return pTime;
                }
                
                pTime += procArray[i].getVt();
            }
        }
        return -1;
    }// end of precedingProcessTime()


    @Override
    public int succeedingProcessTime(String procLabel) {
        // Implement me
        int pTime = 0;
        int index = 0;
        if (!findProcess(procLabel)) {
            return -1;
        }
        for (int i = 0; i < procArray.length; i++) {
            if (procArray[i].getLabel().compareTo(procLabel) == 0) {
                index = i;
            }
        }
        for (int i = 0; i < procArray.length; i++) {
            if (i > index) {
                pTime += procArray[i].getVt();
            }
        }
        return pTime;
    } // end of precedingProcessTime()


    @Override
    public void printAllProcesses(PrintWriter os) {
        //Implement me
        String string = "";
        for (int i = 0; i < procArray.length; i++) {
            if (i == procArray.length) {
                string += procArray[i].getLabel();
            } else {
                string += procArray[i].getLabel() + " ";
            }
        }
        os.println(string);
        os.flush();
    } // end of printAllProcesses()

    /*
     * Created by Lewis:
     * Creates new array of +1 length, copies elements to new array but inserts newProc into corect index.
     */
    public Proc[] enqNewArray(Proc newProc) {
        Proc[] newArray = new Proc[(procArray.length + 1)];
        int i = 0;
        boolean bool = false;
        for (Proc process : procArray) {
            if (process.getVt() > newProc.getVt() && bool == false) {
                newArray[i] = newProc;
                i++;
                bool = true;
            }
            newArray[i] = process;
            i++;
        }

        if (newArray[(newArray.length - 1)] == null) {
            newArray[(newArray.length - 1)] = newProc;
        }
        return newArray;
    }

    /*
     * Created by Lewis:
     * Saves label of proc being removed (highest priority proc (lowest vc) in index 0).
     * Creates new array of -1 length. Copies all elements except removed proc to new array.
     */
    public Proc[] deqNewArray() {
        Proc[] newArray = new Proc[(procArray.length - 1)];
        for (int i = 1; i < procArray.length; i++) {
            newArray[(i-1)] = procArray[i];
        }
        return newArray;
    }
} // end of class OrderedArrayRQ
