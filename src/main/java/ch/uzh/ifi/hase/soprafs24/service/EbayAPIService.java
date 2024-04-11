package ch.uzh.ifi.hase.soprafs24.service;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import org.springframework.stereotype.Service;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service //denote this is the service component
/*@Transactional // define the scope of a single database transactio*/
public class EbayAPIService {

    public EbayAPIService() {
        try {
            CredentialUtil.load(new FileInputStream("src/main/resources/ebay-config.yaml"));
        } catch (IOException e) {
            // Handle exception if the file cannot be loaded
            e.printStackTrace();
        }
    }

    public static String getToken() throws IOException {
        OAuth2Api oauth2Api = new OAuth2Api();
        List<String> scopes = new ArrayList<>();
        scopes.add("https://api.ebay.com/oauth/api_scope");//in case more scopes are needed in the future
        OAuthResponse response = oauth2Api.getApplicationToken(Environment.PRODUCTION, scopes);
        // Extract the access token content after "token="
        Optional<AccessToken> accessTokenOptional = response.getAccessToken();
        if (accessTokenOptional.isPresent()) {
            AccessToken accessToken = accessTokenOptional.get();
            String tokenContent = accessToken.getToken();
            tokenContent = tokenContent.substring(tokenContent.indexOf("token=")+1);//the former part is not needed in http header
           // System.out.println("Content after token=: " + tokenContent);//for test
            return tokenContent;
        };
        return null;
    }

}