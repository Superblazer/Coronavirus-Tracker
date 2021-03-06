package com.sourabh.coronavirustracker.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourabh.coronavirustracker.models.india.IndianDataModel;
import com.sourabh.coronavirustracker.models.india.StateDeltaModel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class IndianDataService {

    private final static String COVID_DATA_URL = "https://api.covid19india.org/data.json";

    @PostConstruct
    public List<IndianDataModel> getIndianData() throws IOException {

        // Used here to parse Json content
        ObjectMapper objectMapper = new ObjectMapper();
        var indianDataList = new ArrayList<IndianDataModel>();

        JsonNode rootNode = objectMapper.readTree(new URL(COVID_DATA_URL));
        JsonNode statesNode = rootNode.path("statewise");
        for (JsonNode element : statesNode) {

            var indianData = objectMapper.readValue(element.toString(), IndianDataModel.class);

            indianDataList.add(indianData);
        }

        indianDataList.sort(Comparator.comparing(IndianDataModel::getTotalConfirmed).reversed());
        int i = 0;
        for (var e : indianDataList) {
            e.setSlNo(i++);
        }

        System.out.println(indianDataList);

        return indianDataList;
    }
}
