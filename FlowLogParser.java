import java.io.*;
import java.util.*;

public class FlowLogParser {

    // Maps a port/protocol combination to a tag
    private static Map<String, String> tagLookup = new HashMap<>();
    // Maps protocol numbers to protocols
    private static Map<String, String> protocolMap = new HashMap<>();
    // Keeps counts of tags and port/protocol combinations
    private static Map<String, Integer> tagCounts = new HashMap<>();
    private static Map<String, Integer> portProtocolCounts = new HashMap<>();

    public static void main(String[] args) throws IOException {
        String lookupFilePath = "lookup_table.csv";  // Replace with your lookup table file path
	String protocolPath = "protocol-numbers-1.csv"; // // Replace with your protocol table file path
        String logFilePath = "flow_logs.txt";        // Replace with your flow logs file path

        // Step 1: Load the lookup table
        loadLookupTable(lookupFilePath);

	// Step 2: Load the protocol mapping
	loadProtocolTable(protocolPath);

        // Step 3: Parse the flow logs
        parseFlowLogs(logFilePath);

        // Step 4: Output the counts
        printTagCounts();
        printPortProtocolCounts();
    }

    // Loads the lookup table from a CSV file
    private static void loadLookupTable(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine();  // Skip the header

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String port = parts[0].trim();
            String protocol = parts[1].trim().toLowerCase();
            String tag = parts[2].trim();
            tagLookup.put(port + "_" + protocol, tag);
        }

        reader.close();
    }

   // Loads the protocol numbers table from a CSV file
    private static void loadProtocolTable(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        reader.readLine();  // Skip the header

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String decimal = parts[0].trim();
            String keyword = parts[1].trim();
            int n = Integer.parseInt(decimal);
            if(n>=146&&n<=252)
            	protocolMap(decimal,""); // assuming empty string for 146<=n<=252
            else
            	protocolMap(decimal,keyword);

        }

        reader.close();
    }

    // Parses the flow log file and counts tags and port/protocol combinations
    private static void parseFlowLogs(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\\s+");
            String dstPort = parts[6];
            String protocol = parts[7];
	    String protocol_name = protocolMap.get(protocol);  // Assuming protocol 6 is TCP, else UDP
            String key = dstPort + "_" + protocol_name;

            // Check if the port/protocol combination exists in the lookup table
            String tag = tagLookup.getOrDefault(key, "Untagged");

            // Count the tag
            tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);

            // Count the port/protocol combination
            portProtocolCounts.put(key, portProtocolCounts.getOrDefault(key, 0) + 1);
        }

        reader.close();
    }

    // Outputs the counts of each tag
    private static void printTagCounts() {
        System.out.println("Tag Counts:");
        System.out.println("Tag,Count");
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }

    // Outputs the counts of each port/protocol combination
    private static void printPortProtocolCounts() {
        System.out.println("\nPort/Protocol Combination Counts:");
        System.out.println("Port,Protocol,Count");
        for (Map.Entry<String, Integer> entry : portProtocolCounts.entrySet()) {
            String[] keyParts = entry.getKey().split("_");
            String port = keyParts[0];
            String protocol = keyParts[1];
            System.out.println(port + "," + protocol + "," + entry.getValue());
        }
    }
}
