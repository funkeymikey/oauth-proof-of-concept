package oauth.provider.objects;

public class UserVerifiedApp {

	public Integer userId;
	public ThirdPartyConsumer thirdPartyConsumer;

	public UserVerifiedApp(Integer userId, ThirdPartyConsumer thirdPartyConsumer) {
		this.userId = userId;
		this.thirdPartyConsumer = thirdPartyConsumer;
	}
}
