package oauth.provider.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.SimpleOAuthValidator;
import net.oauth.server.OAuthServlet;

import oauth.provider.managers.OauthStore;
import oauth.provider.objects.AccessTokenData;
import oauth.provider.objects.RequestTokenData;
import oauth.provider.objects.ThirdPartyConsumer;
import oauth.provider.objects.UserVerifiedApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("oauth")
public class OauthController {
	
	private OauthStore store;
	private SimpleOAuthValidator validator;
	
	public OauthController() {
		store = new OauthStore();
		validator  = new SimpleOAuthValidator();
	}
	
	@RequestMapping(value="request_token", method = RequestMethod.POST)
	@ResponseBody
	public String requestToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//convert the httprequest to an oauth message
		OAuthMessage msg = OAuthServlet.getMessage(request, null);
				
		//make sure it has all the parameters required
		msg.requireParameters(OAuth.OAUTH_CONSUMER_KEY, OAuth.OAUTH_NONCE, OAuth.OAUTH_SIGNATURE_METHOD,
				OAuth.OAUTH_SIGNATURE, OAuth.OAUTH_TIMESTAMP, OAuth.OAUTH_CALLBACK);
		
		//get the consumer key from the message		
		String consumerKey = msg.getParameter(OAuth.OAUTH_CONSUMER_KEY);		
		ThirdPartyConsumer thirdPartyConsumer = store.getThirdPartyConsumer(consumerKey);
		
		//validate the message signature/timestamp/nonce
		OAuthAccessor accessor = new OAuthAccessor(new OAuthConsumer(null, consumerKey, thirdPartyConsumer.secret, null));
		validator.validateMessage(msg, accessor);
		
		//create the request token
		UUID requestToken = store.newRequestToken(thirdPartyConsumer);
		UUID requestTokenSecret = store.getTokenSecret(requestToken).tokenSecret;
		
		//dump it out in the response body - this should be done better
		String responseBody =
			OAuth.OAUTH_TOKEN + "=" + requestToken + "&" +
			OAuth.OAUTH_TOKEN_SECRET + "=" + requestTokenSecret;
		
		return responseBody;
	}

	@RequestMapping(value="authorize", method = RequestMethod.GET)
	@ResponseBody
	public String authorize(@RequestParam("oauth_token") UUID token) {
		
		RequestTokenData rtd = store.getTokenSecret(token);
		
		System.out.println("this is a login page");
		System.out.println("User with ID 1 successfully logs in");
		
		System.out.println("after login, we ask if they want to allow " + rtd.thirdPartyConsumer.name + " to access data");
		System.out.println("they say yes, and we create a verifier");
		
		//save this verifier for this person
		Integer verifier = store.newVerifier(1, rtd.thirdPartyConsumer);
		
		return "" + verifier;
	}
	
	@RequestMapping(value="access_token", method = RequestMethod.POST)
	@ResponseBody
	public String accessToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//convert the httprequest to an oauth message
		OAuthMessage msg = OAuthServlet.getMessage(request, null);
				
		//make sure it has all the parameters required
		msg.requireParameters(OAuth.OAUTH_CONSUMER_KEY, OAuth.OAUTH_NONCE, OAuth.OAUTH_SIGNATURE_METHOD,
				OAuth.OAUTH_SIGNATURE, OAuth.OAUTH_TIMESTAMP, OAuth.OAUTH_VERIFIER, OAuth.OAUTH_TOKEN);
				
		//get the consumer key from the message		
		String consumerKey = msg.getParameter(OAuth.OAUTH_CONSUMER_KEY);		
		ThirdPartyConsumer thirdPartyConsumer = store.getThirdPartyConsumer(consumerKey);
		UUID requestToken = UUID.fromString(msg.getParameter(OAuth.OAUTH_TOKEN));
		UUID requestTokenSecret = store.getTokenSecret(requestToken).tokenSecret;
		
		System.out.println("found consumer: " + consumerKey);
		System.out.println("TPC: " + thirdPartyConsumer);
		
		
		System.out.println(msg.getParameters());
		
		
		//validate the message signature/timestamp/nonce
		OAuthAccessor accessor = new OAuthAccessor(new OAuthConsumer(null, consumerKey, thirdPartyConsumer.secret, null));
		accessor.requestToken = requestToken.toString();
		accessor.tokenSecret = requestTokenSecret.toString();		
		validator.validateMessage(msg, accessor);
		
		//extract the verifier parameter and look it up
		String verifyString = msg.getParameter(OAuth.OAUTH_VERIFIER);
		System.out.println("verif: " + verifyString);
		Integer verifier = Integer.parseInt(verifyString);
		System.out.println("inputted verifier: " + verifier);
		UserVerifiedApp vd = store.getVerifier(verifier);
		System.out.println("tpa: " + vd);
		
		//if we can't look it up successfully, error
		if(vd == null)
			return "";
		
		//if the consumer doesn't match the one for the verifier, error 
		if(!vd.thirdPartyConsumer.consumerKey.equals(consumerKey))
			return "";
		
		//create the access token
		UUID accessToken = store.createAccessToken(vd);
		AccessTokenData accessTokenData = store.getAccessTokenData(accessToken);
		
		//dump it out in the response body - this should be done better
		String responseBody =
			OAuth.OAUTH_TOKEN + "=" + accessToken + "&" +
			OAuth.OAUTH_TOKEN_SECRET + "=" + accessTokenData.tokenSecret;
		
		System.out.println("responding with: " + responseBody);
		
		return responseBody;
	}
}
