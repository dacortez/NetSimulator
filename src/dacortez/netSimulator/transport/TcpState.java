package dacortez.netSimulator.transport;

public enum TcpState {
	CLOSED, SYN_SENT, ESTABLISHED, FIN_WAIT_1, FIN_WAIT_2, TIME_WAIT,
	LISTEN, SYN_RCVD, CLOSE_WAIT, LAST_ACK;
}
