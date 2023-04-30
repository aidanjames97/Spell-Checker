import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.ArrayList;

public class Spell {
    // initialize hashtable
    static Hashtable<String, Boolean> dictionary = new Hashtable<String, Boolean>();
    public static char letters[] = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    Spell(String s, String k){
        // Load dictionary words from file into Hashtable

        // entre try catch
        try {
            Scanner scanner = new Scanner(new File(s));
            // another line in txt file

            while (scanner.hasNextLine()) {
                String word = scanner.nextLine();
                dictionary.put(word, true);
            }
            // completed reading file to hashtable
            scanner.close();

            // exception caught
        } catch (Exception e) {
            System.out.println("1 Caught exception: " + e);
        }

        try {
            Scanner scanner = new Scanner(new File(k));
            // another line in txt file
            while (scanner.hasNextLine()) {
                // calling spelling check function
                checkSpelling(scanner.nextLine());
            }

        } catch (Exception e) {
            System.out.println("2 Caught exception: " + e);
        }
    }

    public static void main(String[] args) {
        // init an object of type Spell
        Spell spell = new Spell(args[0], args[1]);

        // add your code here
    }

    // this function check if the dictionay is loaded or not
    public static Hashtable<String, Boolean> getDictionary(){
        return dictionary;
    }

    // This function takes a String word as an argument to check if the word exists in the dictionary.
    // If the word exists, it will print it with a message "Incorrect Spelling:" to the console.
    // Else it will call the suggestCorrections function to provide the correct word from the words given in the dictionary file.
    public static boolean checkSpelling(String word){
        // changing for case
        word = word.toLowerCase();
        // checking if word is in dictionary
        try {
            // checking for numbers or other characters
            char[] chars = word.toCharArray();
            StringBuilder sb = new StringBuilder();
            for (char c : chars) {
                if (!(Character.isDigit(c))) {
                    sb.append(c);
                } else {
                    break;
                }
            }
            // to catch non-char strings
            if (sb.toString().length() == 0) {
                return false;
            } else if (dictionary.containsKey(sb.toString())) {
                System.out.println(sb + ": Correct Spelling");
            } else {
                // calling suggest Corrections to suggest user corrections to word
                suggestCorrections(sb.toString());
            }
        } catch (Exception e) {

        }
        // default return
        return true;
    }

    // This function takes a String input word as argument.
    // It starts by printing the message word: Incorrect Spelling to the console.
    // The function also uses four different methods (correctSpellingWithSubstitution,
    // correctSpellingWithOmission, correctSpellingWithInsertion, correctSpellingWithReversal)
    // to generate possible corrected spellings for the input word.
    // The function then returns the suggestions list which contains the possible corrected spellings.
    public static boolean suggestCorrections(String word) {
        // printing incorrect spelling
        System.out.println(word + ": Incorrect Spelling");
        // String to hold corrections
        StringBuilder out = new StringBuilder(word + " => ");

        // variables to hold correct spelling of each type
        String sub = correctSpellingSubstitution(word);
        String om = correctSpellingWithOmission(word);
        ArrayList<String> in = correctSpellingWithInsertion(word);
        String rev = correctSpellingWithReversal(word);

        // if there are any corrections adding them
        if(sub != "") {
            out.append(sub + ", ");
        }
        if(om != "") {
            out.append(om + ", ");
        }
        if(!(in.isEmpty())) {
            for(int i = 0; i < in.size(); i++) {
                out.append(in.get(i) + ", ");
            }
        }
        if(rev != "") {
            out.append(rev + ", ");
        }
        // removing final ',' from string if the is one
        if((out.charAt(out.length() - 1) == ' ') && (out.charAt(out.length() - 2) == ',')) {
            out.deleteCharAt(out.length() - 2);
        }
        // final print
        System.out.println(out);
        // default return
        return true;
    }

    // This function takes in a string word and tries to correct the spelling by substituting letters and
    // check if the resulting new word is in the dictionary. (working)
    static String correctSpellingSubstitution(String word) {
        // turing work into stringBuilder as we can manipulate it
        StringBuilder newWord = new StringBuilder(word);
        // going through each char in word to replace and check if valid
        for(int i = 0; i < word.length(); i++) {
            char charChk = word.charAt(i);

            // going through each letter in alphabet (minus letter currently there)
            for(int let = 0; let < 26; let++) {
                // checking for original letter
                if(letters[let] != charChk) {
                    // make new string
                    newWord.setCharAt(i, letters[let]);
                    // check if valid
                    try {
                        if (dictionary.containsKey(newWord.toString())) {
                            // returning correctly spelt word
                            return newWord.toString();
                        }
                    } catch (Exception e) {

                    }
                }
            }
            // putting original letter back
            newWord.setCharAt(i, charChk);
        }
        // retuning if nothing found :(
        return "";
    }

    // This function tries to omit (in turn, one by one) a single character in the misspelled word
    // and check if the resulting new word is in the dictionary. (working)
    static String correctSpellingWithOmission(String word) {
        // turing work into stringBuilder as we can manipulate it
        StringBuilder newWord = new StringBuilder(word);
        // going through each character in word
        for(int i = 0; i < word.length(); i++) {
            // saving char going to be removed
            char save = word.charAt(i);
            // removing char
            newWord.deleteCharAt(i);
            // checking if new word is in dictionary
            try {
                if (dictionary.containsKey(newWord.toString())) {
                    // returning correctly spelt word
                    return newWord.toString();
                    // word not in dictionary
                }
            } catch (Exception e) {

            }
            // inserting letter back
            newWord.insert(i, save);
        }
        // returning if nothing found :(
        return "";
    }

    // This function tries to insert a letter in the misspelled word
    // and check if the resulting new word is in the dictionary.
    static ArrayList<String> correctSpellingWithInsertion(String word) {
        // turing work into stringBuilder as we can manipulate it
        StringBuilder newWord = new StringBuilder(word);

        // initializing ArrayList to hold valid words
        ArrayList<String> out = new ArrayList<String>();


        // going through each character in word
        for(int i = 0; i < word.length()+1; i++) {
            // trying each letter
            for(int let = 0; let < 26; let++) {
                newWord.insert(i,letters[let]);
                // checking if word is valid
                try {
                    if (dictionary.containsKey(newWord.toString())) {
                        // word valid, append to array list
                        // adding correctly spelt word to Array list
                        out.add(newWord.toString());
                    }
                } catch (Exception e) {

                }
                 // remove letter tested
                newWord.deleteCharAt(i);
            }
        }
        // returning Array List of type String
        return out;
    }

    // This function tries swapping every pair of adjacent characters
    // and check if the resulting new word is in the dictionary.
    static String correctSpellingWithReversal(String word) {
        // turing work into stringBuilder as we can manipulate it
        StringBuilder newWord = new StringBuilder(word);

        // looping through word
        for(int i = 0; i < word.length() - 1; i++) {
            // variables to hold chars being reversed
            char first = newWord.charAt(i);
            char second = newWord.charAt(i + 1);

            // reversing adjacent letters
            newWord.setCharAt(i, second);
            newWord.setCharAt(i+1, first);

            // checking if word valid
            try {
                if (dictionary.containsKey(newWord.toString())) {
                    // returning correctly spelt word
                    return newWord.toString();
                }
            } catch (Exception e) {

            }

            // resetting reversal
            newWord.setCharAt(i, first);
            newWord.setCharAt(i+1, second);
        }
        // returning if nothing found :(
        return "";
    }
}