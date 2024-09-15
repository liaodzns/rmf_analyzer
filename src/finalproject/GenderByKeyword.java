package finalproject;

import java.util.ArrayList;

public class GenderByKeyword extends DataAnalyzer {
    private MyHashTable<String, MyHashTable<String, Integer>> wordNHT;

    public GenderByKeyword(Parser p) {
		super(p);
	}

	@Override
	public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        keyword = keyword.trim();
        return wordNHT.get(keyword);
	}

	@Override
	public void extractInformation() {
        wordNHT = new MyHashTable<>();
        String[] genderList = {"M", "F", "X"};

        for (int i = 0; i < parser.data.size(); i++) {
            String[] row = parser.data.get(i);
            String gender = row[parser.fields.get("gender")];  // = row[1]
            String comments = row[parser.fields.get("comments")];

            ArrayList<String> sentence = separateWords(comments);
            ArrayList<String> separatedWords = removeDuplicates(sentence);

            for (String word : separatedWords) {
                if (wordNHT.get(word) == null) { // no genderHT for a certain word

                    MyHashTable<String, Integer> genderHT = new MyHashTable<>();
                    for (String g : genderList) {
                        genderHT.put(g, 0);
                    }
                    genderHT.put(gender, 1);
                    wordNHT.put(word, genderHT);
                }
                else {  // there is a genderHT for this word
                    int wordCountForGender = wordNHT.get(word).get(gender);
                    wordCountForGender += 1;
                    wordNHT.get(word).put(gender, wordCountForGender);
//
                }

            }


        }
	}

    public static ArrayList<String> removeDuplicates(ArrayList<String> list) {

        ArrayList<String> newList = new ArrayList<String>();
        for (String element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    private static ArrayList<String> separateWords(String sentence) {

        StringBuilder cleanedSentence = new StringBuilder();

        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            if (Character.isLetter(c) || c == '\'') {
                cleanedSentence.append(Character.toLowerCase(c));
            } else {
                cleanedSentence.append(' ');
            }
        }
        String[] words = cleanedSentence.toString().split("\\s+"); // "\\s+"

        ArrayList<String> wordList = new ArrayList<>();

        for (String word : words) {
            wordList.add(word);
        }
        return wordList;
    }


}
