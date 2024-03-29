public class InternMessage implements Comparable<InternMessage> {
    int message;
    int time;
    int nodeID;
    int numOfNodes;

    public InternMessage(int message, int time, int nodeID, int numOfNodes) {
        this.message = message;
        this.time = time;
        this.nodeID = nodeID;
        this.numOfNodes = numOfNodes;
    }

    @Override
    public int compareTo(InternMessage internMessage) { // compare messages , and if 2 message have the same timestamp
                                                        // then compare id number
        if (Double.compare(this.time, internMessage.time) == 0) {
            return Double.compare(this.nodeID, internMessage.nodeID);
        }
        return Double.compare(this.time, internMessage.time);
    }

}
