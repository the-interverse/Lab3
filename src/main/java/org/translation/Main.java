package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */

public class Main {

    // Correcting the CheckStyle Error of multiple String occurences:
    private static final String QUIT = "quit";
    private static final LanguageCodeConverter LAC = new LanguageCodeConverter();
    private static final CountryCodeConverter CNC = new CountryCodeConverter("country-codes.txt");

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */

    public static void main(String[] args) {
        Translator translator = new JSONTranslator("sample.json");
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String country = promptForCountry(translator);
            if (country.equals(QUIT)) {
                break;
            }

            Map<String, String> cntc = new HashMap<>();
            List<String> co = translator.getCountries();
            for (String c : co) {
                String n = translator.translate(c, "en");
                cntc.put(n, c);
            }
            String cC = cntc.get(country);

            String language = promptForLanguage(translator, cC);

            if (language.equals(QUIT)) {
                break;
            }

            String lC = LAC.fromLanguage(language);

            System.out.println(country + " in " + language + " is " + translator.translate(cC, lC));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (QUIT.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        Map<String, String> ctcp = CNC.getCtcountry();

        List<String> codesToRemove = List.of(
                "SHN", "MSR", "SXM", "CCK", "BLM", "TCA", "MNP", "MAF", "HMD", "FRO", "PSE", "FLK", "WLF",
                "ASM", "VGB", "CYM", "COK", "PCN", "IMN", "CUW", "UMI", "NIU", "MAC", "IOT", "GRL", "HKG", "TKL", "MYT",
                "MTQ", "BES", "GUM", "GIB", "GUF", "SPM", "NCL", "ABW", "TWN", "AIA", "ALA", "BVT", "BMU", "VIR", "ATF",
                "CXR", "ESH", "NFK", "PYF", "PRI", "SJM", "JEY", "REU", "VAT", "GLP", "GGY", "SGS", "ATA"
        );

        for (String code : codesToRemove) {
            ctcp.remove(code);
        }

        List<String> cn = new ArrayList<>();

        for (String a3c : ctcp.keySet()) {
            String countryName = CNC.fromCountryCode(a3c);
            if (!countryName.isEmpty()) {
                cn.add(countryName);
            }
        }

        Collections.sort(cn);

        for (String name : cn) {
            System.out.println(name);
        }

        System.out.println("select a country from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        List<String> lC = translator.getCountryLanguages(country);
        Map<String, String> lntc = new HashMap<>();
        for (String c : lC) {
            String name = LAC.fromLanguageCode(c);
            lntc.put(name, c);
        }
        List<String> lN = new ArrayList<>(lntc.keySet());
        Collections.sort(lN);
        for (String name : lN) {
            System.out.println(name);
        }

        System.out.println("select a language from above:");

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
