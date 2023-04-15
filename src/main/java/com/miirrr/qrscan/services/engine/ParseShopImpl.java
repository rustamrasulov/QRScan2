package com.miirrr.qrscan.services.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miirrr.qrscan.entities.City;
import com.miirrr.qrscan.entities.Shop;
import com.miirrr.qrscan.services.entities.CityService;
import com.miirrr.qrscan.services.entities.CityServiceImpl;
import com.miirrr.qrscan.services.entities.ShopService;
import com.miirrr.qrscan.services.entities.ShopServiceImpl;
import com.miirrr.qrscan.services.web.WebService;
import com.miirrr.qrscan.services.web.WebServiceImpl;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class ParseShopImpl implements ParseShop {


    private final WebService webService;

    private final ShopService shopService;

    private final CityService cityService;

    public ParseShopImpl() {
        webService = new WebServiceImpl().getInstance();
        shopService = new ShopServiceImpl();
        cityService = new CityServiceImpl();
    }

    /**
     * {
     * "name": "Сургут г. . ул. Бажова. д. 4/2)",
     * "inn": "860230487090",
     * "city": "Сургут",
     * "id": "k125"
     * },
     */
    @Override
    public void loadStores() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree((webService.request("getshopslist")));
            if (!jsonNode.toString().contains("code")) {
                final int[] count = {0};

                JsonNode jsonArray = jsonNode.get("shopslist");
                jsonArray.forEach(j -> {



                    String cityName = j.get("city").asText();
                    if(cityName.length() == 0) cityName = "Все";

//                    String cityName = j.get("city").asText();
//                    switch (count[0] % 4) {
//                        case 0: cityName = "Сургут";
//                        break;
//                        case 1: cityName = "Пыть-Ях";
//                        break;
//                        case 2: cityName = "Нижневартовск";
//                        break;
//                        case 3: cityName = "Нефтеюганск";
//                        break;
//                    }
//
//                    count[0]++;
                    City city = cityService.findByName(cityName);
                    if(city == null) {
                        city = cityService.save(new City(cityName));
                    }


//                    System.err.println(j.get("id").asText() + ": " + j.get("name").asText());
                    Shop shop  = shopService.findByName(j.get("name").asText());
                    if(shop == null) {
                        shopService.save(new Shop(
                                j.get("id").asText(),
                                j.get("name").asText(),
                                j.get("inn").asText(),
                                city
                        ));
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
