import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.View;

/**
 * Created by hocklo on 26/10/17.
 */
public class JGroupCluster {

    private static final int SLEEP_TIME_IN_MILLIS = 1000;
    private static final String IM_LEADER = "I'm (%s) the leader\n";
    private static final String IM_NOT_LEADER = "I'm (%s) not the leader, the leader is %s\n";
    private static final String CLUSTER_MEMBER = ">> %s";
    private static final String CLUSTER_MEMBERS = "Cluster members";

    public static void main(String[] args) throws Exception {
        JChannel channel = new JChannel();
        while (true) {
            channel.connect("The Test Cluster"); // Cluster name grouping the leaders with slaves.
            checkLeaderStatus(channel);
            sleep();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_TIME_IN_MILLIS);
        } catch (InterruptedException e) {
            // Ignored
        }
    }

    private static void checkLeaderStatus(JChannel channel) {
        View view = channel.getView();
        printMembers(view);
        printWhoIam(channel, view);
    }

    private static void printWhoIam(JChannel channel, View view) {
        Address address = view.getMembers().get(0);
        if (address.equals(channel.getAddress())) {
            System.out.println(String.format(IM_LEADER, channel.getAddress()));
        } else {
            System.out.println(String.format(IM_NOT_LEADER, channel.getAddress(), address));
        }
    }

    private static void printMembers(View view) {
        System.out.println(CLUSTER_MEMBERS);
        for (Address address : view.getMembers()) {
            System.out.println(String.format(CLUSTER_MEMBER, address));
        }
    }
}
