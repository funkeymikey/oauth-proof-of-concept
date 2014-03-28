package oauth.provider.objects;

public class Wrapper<T> {

	public T response;
	
	public Wrapper(T arg) {
		response = arg;
	}
}
