package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.AccessToken;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service //denote this is the service component
/*@Transactional // define the scope of a single database transactio*/
public class EbayAPIService {

    //load authentication infomation firstly
    public EbayAPIService() {
        try {
            CredentialUtil.load(new FileInputStream("src/main/resources/ebayconfig.yaml"));
        } catch (IOException e) {
            // Handle exception if the file cannot be loaded
            e.printStackTrace();
        }
    }

    //generate application token
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
    /*
    //single item
    //parse json info in response
    public static Optional<Item> extractItemInfo(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseBody);

            String itemTitle = responseJson.at("/itemSummaries/0/title").asText();
            float itemPrice = (float) responseJson.at("/itemSummaries/0/price/value").asDouble();
            String firstCategory = responseJson.at("/itemSummaries/0/categories/0/categoryName").asText();
            String itemImage1 = responseJson.at("/itemSummaries/0/image/imageUrl").asText();
            String itemImage2 = responseJson.at("/itemSummaries/0/additionalImages/1/imageUrl").asText();
            float leftrange = 0;
            float rightrange = 2*itemPrice;


            Item item = new Item();
            item.setItemTitle(itemTitle);
            item.setPrice(itemPrice);
            item.setItemCat(firstCategory);
            item.setLeftRange(leftrange);
            item.setRightRange(rightrange);

            if (itemImage1 == null && itemImage2 == null) {
                item.setImageURL("default"); // Set default image URL
            } else {
                // Set imageURL to itemImage2 if itemImage1 is null, otherwise set it to itemImage1
                item.setImageURL(itemImage1 != null ? itemImage1 : itemImage2);
            }
            return Optional.of(item);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
*/
    public static List<Item> extractItemsInfo(String responseBody) {
        List<Item> items = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(responseBody);

            JsonNode itemSummaries = responseJson.get("itemSummaries");
            for (JsonNode itemSummary : itemSummaries) {
                String itemTitle = itemSummary.at("/title").asText();
                float itemPrice = (float) itemSummary.at("/price/value").asDouble();
                String firstCategory = itemSummary.at("/categories/0/categoryName").asText();
                String itemImage1 = itemSummary.at("/image/imageUrl").asText();
                String itemImage2 = itemSummary.at("/additionalImages/1/imageUrl").asText();
                float leftrange = 0;
                float rightrange = 2 * itemPrice;

                Item item = new Item();
                item.setItemTitle(itemTitle);
                item.setPrice(itemPrice);
                item.setItemCat(firstCategory);
                item.setLeftRange(leftrange);
                item.setRightRange(rightrange);

                if (itemImage1 == null && itemImage2 == null) {
                    item.setImageURL("default"); // Set default image URL
                }
                else {
                    // Set imageURL to itemImage2 if itemImage1 is null, otherwise set it to itemImage1
                    item.setImageURL(itemImage1 != null ? itemImage1 : itemImage2);
                }

                items.add(item);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }
}