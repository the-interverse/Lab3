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
    private static LanguageCodeConverter laC = new LanguageCodeConverter();
    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */

    public static void main(String[] args) {
        Translator translator = new JSONTranslator();
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

            String lC = laC.fromLanguage(language);

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
        List<String> countries = translator.getCountries();

        Map<String, String> countryNameToCode = new HashMap<>();
        for (String code : countries) {
            String name = translator.translate(code, "en");
            countryNameToCode.put(name, code);
        }
        List<String> countryNames = new ArrayList<>(countryNameToCode.keySet());
        Collections.sort(countryNames);
        for (String name : countryNames) {
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
            String name = laC.fromLanguageCode(c);
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