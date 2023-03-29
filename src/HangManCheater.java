import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class HangManCheater {
    public static void main(String[] args) {
        //get the words from the file


        String fileName = "words.txt";
        ArrayList<String> wordsList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] words = line.split("\\s+");
                for (String word : words) {
                    for (char c : word.toCharArray()) {
                        if (!Character.isLetter(c) && !Character.isDigit(c))//if the character is not a letter
                        {
                            word = word.replace(c, ' ');//replace it with a space
                        }
                    }
                    word = word.replace(" ", "");//replace all the spaces
                    wordsList.add(word);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }


        //declare a list for the word lengths
        ArrayList<Integer> wordLengths = new ArrayList<>();
        //declare a map for the words, the keys are the length of the words
        Map<String, Set<String>> wordMap = new HashMap<>();
        for (String word : wordsList) {
            String key = "";
            key += word.length();
            wordLengths.add(word.length());
            addWordToMap(word, wordMap, key);
        }


        //get the length of the word from the user
        boolean hasLength = false;
        int length;
        do {
            System.out.print("Enter the length of the word you want to guess: ");
            Scanner input = new Scanner(System.in);
            length = input.nextInt();
            for (int wordLength : wordLengths) {
                if (wordLength == length) {
                    hasLength = true;
                    break;
                }
            }
        } while (!hasLength);//if the length is not in the list, ask the user to enter a new length


        //create a string with the length of the word, and fill it with underscores as a default output
        String outputWord = "";
        for (int i = 0; i < length; i++) {
            outputWord += "_";
        }


        //get the number of guesses from the user
        System.out.print("Enter the number of guesses you want to make: ");
        Scanner input = new Scanner(System.in);
        int guesses = input.nextInt();


        //get the set of words with the length of the word
        Set<String> currentSet = wordMap.get(Integer.toString(length));
        System.out.println(currentSet);


        //declare a string builder for the wrong letters
        StringBuilder wrongLetters = new StringBuilder();


        //start the game if the user has guesses left
        while (guesses > 0) {
            //get the letter from the user
            String letter = "";
            //check if the input is a letter
            while (letter.length() != 1 || !Character.isLetter(letter.charAt(0))) {
                System.out.print("Enter a letter to guess: ");
                Scanner letterInput = new Scanner(System.in);
                letter = letterInput.nextLine();
            }


            //check if the letter is in the word with the current set of words
            Iterator<String> wordIterator = currentSet.iterator();
            Map<String, Set<String>> cheatingMap = new HashMap<>();
            while (wordIterator.hasNext()) {
                String word = wordIterator.next();
                //if the letter is in the word, add the word to the map with the key of the letter and the index of the letter
                if (word.contains(letter) || word.contains(letter.toUpperCase()) || word.contains(letter.toLowerCase())) {
                    StringBuilder key = new StringBuilder(letter);
                    for (int i = 0; i < word.length(); i++) {
                        if (Character.toLowerCase(word.charAt(i)) == Character.toLowerCase(letter.charAt(0))) {
                            key.append(" ").append(i);
                        }
                    }
                    addWordToMap(word, cheatingMap, key.toString());
                } else {
                    addWordToMap(word, cheatingMap, letter);
                }
            }


            //get the largest set of words from the map and the key of the largest set
            Set<String> keys = cheatingMap.keySet();
            Iterator<String> keyIterator = keys.iterator();
            Set<String> largestSet;
            String key = keyIterator.next();
            String keySaver = key;
            largestSet = cheatingMap.get(key);
            while (keyIterator.hasNext()) {
                key = keyIterator.next();
                if (cheatingMap.get(key).size() > largestSet.size()) {
                    largestSet = cheatingMap.get(key);
                    keySaver = key;
                }
                if (cheatingMap.get(key).size() == largestSet.size() && key.length() < keySaver.length()) {
                    largestSet = cheatingMap.get(key);
                    keySaver = key;
                }
            }


            //if the letter is not in the word, add it to the wrong letters string builder, with the key of the largest set
            boolean hasLetter = false;

            for (int i = 0; i < keySaver.split(" ").length; i++) {

                if (!keySaver.split(" ")[i].equals(letter)) {
                    int position = Integer.parseInt(keySaver.split(" ")[i]);
                    outputWord = outputWord.substring(0, position) + letter + outputWord.substring(position + 1);
                    hasLetter = true;
                }
            }

            if (!hasLetter) {
                wrongLetters.append(letter).append(" ");
            }


            //print the output word, the wrong letters, the number of guesses left, and the largest set of words
            System.out.println(outputWord);
            System.out.println("Wrong guesses: " + wrongLetters);
            guesses--;
            System.out.println("You have " + guesses + " guesses left.");
            currentSet = largestSet;
            System.out.println(largestSet);


            //if the output word does not contain any underscores, the user wins
            if (!outputWord.contains("_")) {
                System.out.println("You win!");
                break;
            }
        }
    }

    private static void addWordToMap(String word, Map<String, Set<String>> cheatingMap, String key) {
        if (cheatingMap.containsKey(key)) {
            //if the key is already in the map, add the word to the set of words
            Set<String> wordSet;
            wordSet = cheatingMap.get(key);
            wordSet.add(word);
            cheatingMap.put(key, wordSet);
        } else {
            //if the key is not in the map, create a new set of words and add the word to it
            Set<String> wordSet = new HashSet<>();
            wordSet.add(word);
            cheatingMap.put(key, wordSet);
        }
    }

}