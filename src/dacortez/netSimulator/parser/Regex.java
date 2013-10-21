package dacortez.netSimulator.parser;

import java.util.regex.Pattern;

public class Regex {
	// #
	public static final Pattern COMMENT = Pattern.compile("\\s*#");
	
	// set h0 [$simulator host]
	public static final Pattern SET_HOST = Pattern.compile("set\\s+(\\w+)\\s+\\[\\$simulator\\s+host\\]");
	
	// set r0 [$simulator router 3]
	public static final Pattern SET_ROUTER = Pattern.compile("set\\s+(\\w+)\\s+\\[\\$simulator\\s+router\\s+(\\d+)\\]");
	
	// $simulator duplex-link $h0 $r0.0 10Mbps 10ms
	// $simulator duplex-link $r0.2 $r1.0 2Mbps 20ms
	public static final Pattern DUPLEX_LINK = Pattern.compile("\\$simulator\\s+duplex-link\\s+\\$(\\w+).?(\\d+)?\\s+\\$(\\w+).?(\\d+)?\\s+([\\d.]+)Mbps\\s+([\\d.]+)ms");

	// $simulator $h0 10.0.0.1 10.0.0.2 192.168.1.1
	public static final Pattern HOST_IPS = Pattern.compile("\\$simulator\\s+\\$(\\w+)\\s+(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})\\s+(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})\\s+(\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3})");
	
	// $simulator $r0 0 10.0.0.2 1 10.1.1.2 2 192.168.3.3
	public static final Pattern ROUTER_IPS = Pattern.compile("\\$simulator\\s+\\$(\\w+)\\s+((?:\\d+\\s+\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\s*)+)");
	
	// $simulator $r0 performance 100us 0 1000 1 1000 2 1000
	public static final Pattern ROUTER_PERFORMANCE = Pattern.compile("\\$simulator\\s+\\$(\\w+)\\s+performance\\s+([\\d.]+)us\\s+((?:\\d+\\s*)+)");

	// set httpc0 [new Agent/HTTPClient]
	public static final Pattern SET_HTTP_CLIENT = Pattern.compile("set\\s+(\\w+)\\s+\\[new Agent/HTTPClient\\]");

	// set https2 [new Agent/HTTPServer]
	public static final Pattern SET_HTTP_SERVER = Pattern.compile("set\\s+(\\w+)\\s+\\[new Agent/HTTPServer\\]");
	
	// set dns3 [new Agent/DNSServer]
	public static final Pattern SET_DNS_SERVER = Pattern.compile("set\\s+(\\w+)\\s+\\[new Agent/DNSServer\\]");
	
	// $simulator attach-agent $httpc0 $h0
	public static final Pattern ATTACH_AGENT = Pattern.compile("\\$simulator\\s+attach-agent\\s+\\$(\\w+)\\s+\\$(\\w+)\\s*$");
	
	// set sniffer1 [new Agent/Sniffer]
	public static final Pattern SET_SNIFFER = Pattern.compile("set\\s+(\\w+)\\s+\\[new Agent/Sniffer\\]");
	
	// $simulator attach-agent $sniffer1 $r0.2 $r1.0 "/tmp/sniffer1"
	public static final Pattern ATTACH_SNIFFER = Pattern.compile("\\$simulator\\s+attach-agent\\s+\\$(\\w+)\\s+\\$(\\w+).?(\\d+)?\\s+\\$(\\w+).?(\\d+)?\\s+\"([\\w/]+)\"");
	
	// $simulator at 0.5 "httpc0 GET h2"
	public static final Pattern SIM_EVENT = Pattern.compile("\\$simulator\\s+at\\s+([\\d.]+)\\s+\"([\\w\\s]+)\"");
}
