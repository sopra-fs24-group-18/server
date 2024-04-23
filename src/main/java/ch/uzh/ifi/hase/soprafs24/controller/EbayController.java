package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.service.EbayAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class EbayController {

    @Autowired
    private RestTemplate restTemplate; // Autowire RestTemplate

    @Autowired
    private ItemRepository itemRepository; // Autowire ItemRepository



    @GetMapping("/searchItems")
    public ResponseEntity<?> searchItems(@RequestParam(required = false) String keyword,
                                         @RequestParam(required = false) String categoryId) {

        // Get the application token
        String applicationToken;
        try {
            applicationToken = EbayAPIService.getToken();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get application token");
        }

        // Build the eBay API URL based on the provided parameters
        StringBuilder apiUrl = new StringBuilder("https://api.ebay.com/buy/browse/v1/item_summary/search?");
        if (keyword != null) {
            apiUrl.append("q=").append(keyword).append("&");//https://api.ebay.com/buy/browse/v1/item_summary/search?q=iphone
        }

        if (categoryId != null) {
            apiUrl.append("category_ids=").append(categoryId).append("&");
        }

        apiUrl.append("limit=200").append("&"); // Limit the number of results to 500 for each call
        apiUrl.append("filter=price:[1..1000],priceCurrency:CHF");//Limit the upper bound
        try {
            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept-Language", "en-US"); // Set the language to English
            headers.set("X-EBAY-C-MARKETPLACE-ID", "EBAY_CH"); // Set marketplace ID to EBAY_CH
            headers.set("Authorization", "Bearer " + applicationToken); // Set Authorization header with application token

            // Make the HTTP GET request to the eBay API with required headers
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl.toString(), HttpMethod.GET, entity, String.class);

            List<Item> extractedItems = EbayAPIService.extractItemsInfo(response.getBody());
            itemRepository.saveAll(extractedItems);

            return ResponseEntity.ok ("Item saved successfully");
        } catch (HttpClientErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

}

