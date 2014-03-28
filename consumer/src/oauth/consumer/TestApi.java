package oauth.consumer;
import org.scribe.model.Token;

public class TestApi extends org.scribe.builder.api.DefaultApi10a {

	private static final java.lang.String AUTHORIZE_URL = "http://localhost:8080/OauthService/oauth/authorize?oauth_token=%s";

	private static final java.lang.String REQUEST_TOKEN_RESOURCE = "http://localhost:8080/OauthService/oauth/request_token";

	private static final java.lang.String ACCESS_TOKEN_RESOURCE = "http://localhost:8080/OauthService/oauth/access_token";

	@Override
	public String getAccessTokenEndpoint() {
		return ACCESS_TOKEN_RESOURCE;
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {		
		return String.format(AUTHORIZE_URL, requestToken.getToken());
	}
	

	@Override
	public String getRequestTokenEndpoint() {
		return REQUEST_TOKEN_RESOURCE;
	}

}
