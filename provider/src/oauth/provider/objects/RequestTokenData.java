package oauth.provider.objects;

import java.util.UUID;

public class RequestTokenData {
	
	public UUID tokenSecret;
	public ThirdPartyConsumer thirdPartyConsumer;
	
	public RequestTokenData(UUID secret, ThirdPartyConsumer thirdPartyConsumer) {
		this.tokenSecret = UUID.randomUUID();
		this.thirdPartyConsumer = thirdPartyConsumer;
	}
}
