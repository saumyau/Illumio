Flow Log Parser

Description
This program processes AWS VPC flow log data and maps each log entry to a tag based on a lookup table. The lookup table contains mappings for dstport (destination port) and protocol combinations that map to specific tags. The program outputs two sets of counts:

Tag Counts: The number of times each tag appears in the flow logs.
Port/Protocol Combination Counts: The number of times each unique port/protocol combination appears.

Features:
Supports AWS VPC flow logs in the default format.
Matches flow log entries to a tag using a lookup table provided as a CSV file.
Provides counts for tag occurrences and port/protocol combinations.

Assumptions:
The program supports all default AWS flow logs and all versions of the log format.
If n>=146&&n<=252 where n is decimal number in protocol-numbers-1.csv, then we assume empty string ""


The log format is as follows:
<version> <account-id> <interface-id> <srcaddr> <dstaddr> <srcport> <dstport> <protocol> <packets> <bytes> <start> <end> <action> <log-status>
Example:
2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK

The lookup table (CSV) contains the following columns:
dstport,protocol,tag
Example:
Copy code
25,tcp,sv_P1
443,tcp,sv_P2
993,tcp,email
If a port/protocol combination is not found in the lookup table, it is tagged as "Untagged".

The output consists of two sections:

Tag Counts: The number of occurrences of each tag.
Port/Protocol Combination Counts: The number of occurrences of each unique port/protocol combination.


====================================================================================================================================================

Instructions to Run the Program
Requirements:
Java 8 or higher
Input files for:
Flow logs (flow_logs.txt)
Lookup table (lookup_table.csv)

Compilation:
Save the provided Java code in a file named FlowLogParser.java.
Open a terminal and navigate to the directory where the FlowLogParser.java file is saved.

Compile the Java program using the following command:
javac FlowLogParser.java

Running the Program:
Once compiled, run the program with:
java FlowLogParser

Ensure that the input files (flow_logs.txt and lookup_table.csv) are in the same directory as the Java program.
Sample Files:
Flow Logs: Create a text file named flow_logs.txt with the flow logs in the following format:

2 123456789012 eni-0a1b2c3d 10.0.1.201 198.51.100.2 443 49153 6 25 20000 1620140761 1620140821 ACCEPT OK
2 123456789012 eni-4d3c2b1a 192.168.1.100 203.0.113.101 23 49154 6 15 12000 1620140761 1620140821 REJECT OK

Lookup Table: Create a CSV file named lookup_table.csv with the port/protocol-to-tag mapping:

dstport,protocol,tag
25,tcp,sv_P1
443,tcp,sv_P2
993,tcp,email
110,tcp,email
