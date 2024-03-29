import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.*;

public class ThreadState implements Runnable {

    private Logger logr;
    BlockingQueue<Integer> inbox = new LinkedBlockingQueue<Integer>(); // for external messages
    PriorityBlockingQueue<InternMessage> logQueue; // can be viewed as inbox for internal messages
    Stack<Integer> logMessages = new Stack<>();

    List<ThreadState> threadsState_List;
    boolean messageGeneratorClosing = false;
    int timestamp = 0;
    int nodeID;

    public ThreadState(int nodeID, int messageNumber) {
        this.nodeID = nodeID;
        logQueue = new PriorityBlockingQueue<InternMessage>(messageNumber, Comparator.reverseOrder());
    }

    @Override
    public void run() {
        // creat a logger
        logr = Logger.getLogger(Thread.currentThread().getName());
        LogManager.getLogManager().reset();
        logr.setLevel(Level.ALL);
        String loggerfilename = String.format("Thread_%d.log", nodeID);
        FileHandler fh = null;
        try {
            fh = new FileHandler(loggerfilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s"); // log message=(5$) --> to print only
                                                                                // the log message without timestamp
        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.INFO);
        logr.addHandler(fh);

        while (true) {

            if (!logQueue.isEmpty()) { // if a revieced message has higher timestamp then we must update our timeStamp
                if (logQueue.peek().time > timestamp) {
                    timestamp = logQueue.peek().time;
                }
            }

            if (!inbox.isEmpty()) {
                int message = inbox.poll();
                timestamp++;
                InternMessage inMessage = new InternMessage(message, timestamp, nodeID, threadsState_List.size());
                logQueue.add(inMessage);

                // distribute message to other threads
                for (ThreadState thrState : threadsState_List) {
                    if (thrState.nodeID == nodeID) {
                        continue;
                    }
                    thrState.logQueue.add(inMessage);
                }
            }

            // count how many nodes have just finished brodcasting messages;
            int counter = 0;
            for (ThreadState thrState : threadsState_List) {
                if (thrState.inbox.size() == 0) {
                    counter++;
                }
            }
            if ((counter == threadsState_List.size()) && messageGeneratorClosing) {
                // save logs
                while (!logQueue.isEmpty()) {
                    logMessages.push(logQueue.poll().message);
                }
                while (!logMessages.empty()) {
                    String logMessage = String.format("%d\n", logMessages.pop());
                    this.logr.info(logMessage);
                }

                break;
            }

        }

    }
}