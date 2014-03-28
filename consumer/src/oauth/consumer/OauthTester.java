package oauth.consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class OauthTester {

	public static void main(String[] args) {
		new OauthTester();
	}
	
	
	public OauthTester() {
		this.testThing();
		
		//this.testTwitter();
		
		
		
		//	new TestOAuthProviderToken("Tonr.com","tonr-consumer-key","SHHHHH!!!!!!!!!!","Your Photos","Your photos that you have uploaded to sparklr.com.");
		// <oauth:consumer name="iGoogle" key="www.google.com" secret="classpath:/org/springframework/security/oauth/examples/sparklr/certs/igoogle.cert" typeOfSecret="rsa-cert" resourceName="Your Photos" resourceDescription="Your photos that you have uploaded to sparklr.com."/>
		  
	}
	
	public void testTwitter() {
		OAuthService service = new ServiceBuilder()
				.provider(TwitterApi.class)
				.apiKey("8OzE57EELKf1GKzgwO0rkQ")
				.apiSecret("C5VKz1emSXEGOkt5VVDvsdyh9a3STTZRVKgGUgvE")
				.build();

		Token requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
		System.out.println("Go to URL: " + authUrl);
		System.out.println("Enter the verifier");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			input = br.readLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Verifier v = new Verifier(input);
		
		// the requestToken you had from step 2
		Token accessToken = service.getAccessToken(requestToken, v);

		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/settings.json");
		service.signRequest(accessToken, request); // the access token from step 4
		Response response = request.send();
		System.out.println(response.getBody());

	}

	public void testThing() {

		OAuthService service = new ServiceBuilder()
				.provider(TestApi.class)
				.apiKey("key1")
				.apiSecret("secret1")
				.build();

		Token requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);
		System.out.println("Go to URL: " + authUrl);
		System.out.println("Enter the verifier");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		try {
			input = br.readLine();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Verifier v = new Verifier(input);
		
		// the requestToken you had from step 2
		Token accessToken = service.getAccessToken(requestToken, v);

		System.out.println("Done!  Got token: " + accessToken.getToken() + " with secret " + accessToken.getSecret());
		
//		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/settings.json");
//		service.signRequest(accessToken, request); // the access token from step 4
//		Response response = request.send();
//		System.out.println(response.getBody());

		// String token = request.getParameter("oauth_token");
		// String token = "this is the token";
		//
		// //this is a supported consumer
		// ConsumerDetails consumer = new TestConsumerDetails();
		//
		// // OAuthProviderTokenServices tokenServices;
		// // OAuthProviderToken providerToken = tokenServices.getToken(token);
		//
		// // OAuthProviderToken providerToken = new TestOAuthProviderToken();
		//
		//
		// String callback = "callback";
		//
		// HashMap<String, Object> model = new HashMap<>();
		// model.put("oauth_token", token);
		// model.put("oauth_callback", callback);
		// model.put("consumer", consumer);
		//

	}

}
