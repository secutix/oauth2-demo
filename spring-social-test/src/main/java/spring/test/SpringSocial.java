package spring.test;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;

public class SpringSocial {
	
	//Singleton
	private static SpringSocial ssc=null;
	
	//Connection parameters
	private final static String FACEBOOK_IDENTIFIER="facebook";
	private String FACEBOOK_CLIENTID;
	private String FACEBOOK_SECRET;
	private final static String FACEBOOK_REDIRECT_URI="http://localhost:8080/spring/";
	
	private ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
    
	
	private SpringSocial(){
		InitialContext init;
		try {
			init = new InitialContext();
			this.FACEBOOK_CLIENTID=(String)init.lookup("java:/comp/env/facebook-appID");
			this.FACEBOOK_SECRET=(String)init.lookup("java:/comp/env/facebook-secret");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
		registry.addConnectionFactory(new FacebookConnectionFactory(FACEBOOK_CLIENTID,FACEBOOK_SECRET));
	    
	}
	
	public static SpringSocial getInstance(){
		if(ssc==null){
			ssc=new SpringSocial();
		}
		return ssc;
	}
	
	//Give URL
	public String getURL(String provider){
		if(provider.equals(FACEBOOK_IDENTIFIER)){
			FacebookConnectionFactory connectionFactory = (FacebookConnectionFactory) registry.getConnectionFactory("facebook");
				OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
				OAuth2Parameters oAuth2Parameters = new OAuth2Parameters();
				oAuth2Parameters.setRedirectUri(FACEBOOK_REDIRECT_URI);
				String authorizeUrl = oauthOperations.buildAuthorizeUrl(GrantType.AUTHORIZATION_CODE, oAuth2Parameters);
				return authorizeUrl;
		}
		return null;
	
	}
	
	//Give ID of the user depending of the provider
	public String getID(String provider, String code){
		if(provider.equals(FACEBOOK_IDENTIFIER)){
			FacebookConnectionFactory connectionFactory = (FacebookConnectionFactory) registry.getConnectionFactory("facebook");
			OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
			
			//Get the access token
			AccessGrant accessGrant = oauthOperations.exchangeForAccess(code, FACEBOOK_REDIRECT_URI, null);
			Connection<Facebook> connection = connectionFactory.createConnection(accessGrant);
			Facebook facebook = (Facebook) (connection != null ? connection.getApi() : new FacebookTemplate());
			return facebook.userOperations().getUserProfile().getId();
			
		}
		return null;
	}
	
	
}
