package tanlab.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KProducer {
	private String brokerURL;
	private String topicName;
	private int sentCounter;
	public KProducer() {
		
	}
	public KProducer(String brokerUrl, String topicName) {
		this.brokerURL = brokerUrl;
		this.topicName = topicName;
		sentCounter = 0;
	}
	public void publicMessage(String msg) {
		String clientID = "HTIP-ManagerHome1"; 
		Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokerURL);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, clientID);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
   
        KafkaProducer<Integer, String> kafkaProducer = new KafkaProducer<Integer, String>(properties);
        try{
        	ProducerRecord<Integer, String> msgToSend = new ProducerRecord<Integer, String>(this.topicName,sentCounter, msg);
        	kafkaProducer.send(msgToSend);
        	this.sentCounter += 1;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            kafkaProducer.close();
        }
    }
	
	public String getBrokerURL() {
		return brokerURL;
	}
	public void setBrokerURL(String brokerURL) {
		this.brokerURL = brokerURL;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public int getSentCounter() {
		return sentCounter;
	}
	public void setSentCounter(int sentCounter) {
		this.sentCounter = sentCounter;
	}
	
	
}