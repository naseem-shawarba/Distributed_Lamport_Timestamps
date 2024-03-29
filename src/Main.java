import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        int threadsNumber = 0;
        int messageNumber = 0;

        if (args.length != 2) {

            System.err.println("Usage: java Main.class <threadsNumber> <messageNumber>");
            System.exit(1);

        } else {
            try {
                threadsNumber = Integer.parseInt(args[0]);
                messageNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                // System.err.println("Argument : " + args[0] + " must be an integer.");
                System.exit(1);
            }

        }

        List<Thread> Threads_List = new ArrayList<Thread>();
        List<ThreadState> threadsState_List = new ArrayList<ThreadState>();
        for (int i = 0; i < threadsNumber; i++) {

            threadsState_List.add(new ThreadState(i, messageNumber));
            Threads_List.add(new Thread(threadsState_List.get(i)));

        }
        for (ThreadState thrState : threadsState_List) {
            thrState.threadsState_List = threadsState_List;
        }
        Thread messageGenerator = new Thread(new MessageGenerator(threadsState_List, threadsNumber, messageNumber));

        // Starting the threads
        messageGenerator.start();
        for (Thread thr : Threads_List) {
            thr.start();
        }

    }

}