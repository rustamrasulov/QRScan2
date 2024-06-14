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
import java.io.IOException;
import lombok.extern.log4j.Log4j2;

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

                JsonNode jsonArray = jsonNode.get("shopslist");
                jsonArray.forEach(j -> {
                    String cityName = j.get("city").asText()
                            .replace(" (СКЛАД)", "")
                            .replace("НИЖНЕВАРТОВСК", "Н-ВАРТОВСК")
                            .replace("НОВЫЙ УРЕНГОЙ", "Н. УРЕНГОЙ");
                    if (cityName.isEmpty()) {
                        cityName = "Все";
                    }

                    City city = cityService.findByName(cityName);
                    if(city == null) {
                        city = cityService.save(new City(cityName));
                    }

                    Shop shop  = shopService.findByName(j.get("name").asText());
                    if(shop == null) {
                        shopService.save(new Shop(
                                j.get("id").asText(),
                                j.get("name").asText(),
                                j.get("inn").asText(),
                                j.get("ip").asText(),
                                city
                        ));
                    } else if (!shop.getInn().equals(j.get("inn").asText())) {
                        String inn = j.get("inn").asText();

                        ReportExport reportExport = new ReportExport();
                        reportExport.exportShop(shop, inn);

                        shop.setInn(inn);
                        shop.setIpName(j.get("ip").asText());

                        shopService.save(shop);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cityService.findAll().stream().filter(city -> city.getShops().isEmpty())
            .forEach(city -> cityService.deleteById(city.getId()));
    }
}
