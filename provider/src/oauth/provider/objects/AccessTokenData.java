package oauth.provider.objects;

import java.util.UUID;

public class AccessTokenData {
	
	public ThirdPartyConsumer thirdPartyConsumer;
	public Integer userId;
	public UUID tokenSecret;

	public AccessTokenData(Integer userId, ThirdPartyConsumer thirdPartyConsumer, UUID tokenSecret) {
		this.userId = userId;
		this.thirdPartyConsumer = thirdPartyConsumer;
		this.tokenSecret = tokenSecret;
	}
		
}