package oauth.provider.objects;

public class ThirdPartyConsumer {

	//add user id or username here to find out what actual user it is
	public String consumerKey;
	public String secret;
	public String name;
	
	public ThirdPartyConsumer(String name, String consumerKey, String secret){
		this.consumerKey = consumerKey;
		this.secret = secret;
		this.name = name;
	}
}
