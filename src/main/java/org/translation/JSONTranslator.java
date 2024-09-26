package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private Map<String, Map<String, String>> tR = new HashMap<>();
    private Map<String, List<String>> cTl = new HashMap<>();
    private List<String> cC = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int x = 0; x < jsonArray.length(); x++) {

                String alpha3 = "alpha3";

                cC.add(jsonArray.getJSONObject(x).getString(alpha3));

                List<String> lgs = new ArrayList<>();
                Map<String, String> lts = new HashMap<>();

                for (String k : jsonArray.getJSONObject(x).keySet()) {

                    if (!"alpha3".equals(k) && !"id".equals(k) && !"alpha2".equals(k)) {
                        lgs.add(k);
                        lts.put(k, jsonArray.getJSONObject(x).getString(k));
                    }

                }

                tR.put(jsonArray.getJSONObject(x).getString(alpha3), lts);
                cTl.put(jsonArray.getJSONObject(x).getString(alpha3), lgs);

            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        return new ArrayList<>(cTl.get(country));
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(cC);
    }

    @Override
    public String translate(String country, String language) {
        if (tR.get(country).containsKey(language) && tR.containsKey(country)) {
            return tR.get(country).get(language);
        }
        return "Country not found";
    }
}
