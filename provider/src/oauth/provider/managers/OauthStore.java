package oauth.provider.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import oauth.provider.objects.AccessTokenData;
import oauth.provider.objects.RequestTokenData;
import oauth.provider.objects.ThirdPartyConsumer;
import oauth.provider.objects.UserVerifiedApp;

public class OauthStore {
	
	private static List<ThirdPartyConsumer> allowedApps;
	
	private static Map<UUID, RequestTokenData> requestTokens;
	
	private static Map<Integer, UserVerifiedApp> verifiers;
	
	private static Map<UUID, AccessTokenData> accessTokens;
	
	static{
		allowedApps = new ArrayList<>();
		allowedApps.add(new ThirdPartyConsumer("app1", "key1", "secret1"));
		allowedApps.add(new ThirdPartyConsumer("app2", "key2", "secret2"));
		allowedApps.add(new ThirdPartyConsumer("app3", "key3", "secret3"));
		
		requestTokens = new HashMap<>();
		verifiers = new HashMap<>();
		accessTokens = new HashMap<>();
	}
	
	public ThirdPartyConsumer getThirdPartyConsumer(String consumerKey){
		
		for(ThirdPartyConsumer app : allowedApps){
			if(app.consumerKey.equals(consumerKey))
				return app;
		}
		
		return null;
	}
	
	public String getSecret(String consumerKey){
		ThirdPartyConsumer user = this.getThirdPartyConsumer(consumerKey);
		
		if(user == null) return null;
		
		return user.secret;
	}
	
	
	public UUID newRequestToken(ThirdPartyConsumer thirdPartyConsumer){
		
		UUID secret = UUID.randomUUID();
		UUID token = UUID.randomUUID();
		
		requestTokens.put(token, new RequestTokenData(secret, thirdPartyConsumer));
		
		return token;
	}
	
	public RequestTokenData getTokenSecret(UUID token){
		return requestTokens.get(token);
	}
	
	public int newVerifier(Integer userId, ThirdPartyConsumer app){
		
		int verifier = Math.abs(new Random().nextInt());
		
		System.out.println("creating new verifier: " + verifier);
		
		verifiers.put(verifier, new UserVerifiedApp(userId, app));
		
		return verifier;
	}
	
	public UserVerifiedApp getVerifier(Integer verifier){
		return verifiers.get(verifier);
	}

	public UUID createAccessToken(UserVerifiedApp verifiedData) {
		UUID accessToken = UUID.randomUUID();
		UUID accessTokenSecret = UUID.randomUUID();
		
		accessTokens.put(accessToken, new AccessTokenData(verifiedData.userId, verifiedData.thirdPartyConsumer, accessTokenSecret));
		
		return accessToken;
	}

	public AccessTokenData getAccessTokenData(UUID accessToken) {
		return accessTokens.get(accessToken);
	}
	
}
