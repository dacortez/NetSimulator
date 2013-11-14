package dacortez.netSimulator.application.dns;

import java.util.ArrayList;
import java.util.List;

import dacortez.netSimulator.application.messages.Message;

/**
 * @author dacortez (dacortez79@gmail.com)
 * @version 2013.11.14
 */
public class DnsMessage extends Message {
	// Número de identificação.
	private int id;
	// Identifica mensagem como resposta.
	private boolean isReply;
	// Número de consultas.
	private int numberOfQuestions;
	// Número de respostas.
	private int numberOfAnswers;
	// Lista de perguntas.
	private List<DnsQuestion> questions;
	// Lista de respostas.
	private List<DnsAnswer> answers;
	// Tamanho do cabeçalho DNS em bytes.
	private static final int HEADER_SIZE = 12;

	public int getId() {
		return id;
	}
	
	public boolean isReply() {
		return isReply;
	}
	
	public List<DnsQuestion> getQuestions() {
		return questions;
	}
	
	public List<DnsAnswer> getAnswears() {
		return answers;
	}
	
	public DnsMessage(int id, boolean isReply) {
		this.id = id;
		this.isReply = isReply;
		numberOfQuestions = 0;
		numberOfAnswers = 0;
		questions = new ArrayList<DnsQuestion>();
		answers = new ArrayList<DnsAnswer>();
	}
	
	public void addQuestion(DnsQuestion question) {
		questions.add(question);
		numberOfQuestions++;
	}
	
	public void addAnswer(DnsAnswer answer) {
		answers.add(answer);
		numberOfAnswers++;
	}
	
	@Override
	public int getNumberOfBytes() {
		int total = HEADER_SIZE;
		for (DnsQuestion question: questions)
			total += question.getNumberOfBytes();
		for (DnsAnswer answer: answers)
			total += answer.getNumberOfBytes();
		return total;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("DNS_MESSAGE (").append(getNumberOfBytes()).append(" bytes):\n");
		sb.append("Identification: ").append(id).append("\n");
		sb.append("IsReply: ").append(isReply).append("\n");
		sb.append("Number of questions: ").append(numberOfQuestions).append("\n");
		sb.append("Number of answers: ").append(numberOfAnswers).append("\n");
		for (DnsQuestion question: questions)
			sb.append(question).append("\n");
		for (DnsAnswer answer: answers)
			sb.append(answer).append("\n");
		return sb.toString();
	}
}
