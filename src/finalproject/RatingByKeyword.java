package finalproject;

import java.util.ArrayList;
public class RatingByKeyword extends DataAnalyzer {

    private MyHashTable<String, MyHashTable<String, Integer>> wordsNHT;
    private MyHashTable<String, Integer> ratingsHT;
    public RatingByKeyword(Parser p) {
        super(p);
    }

	@Override
	public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        keyword = keyword.trim();
        return wordsNHT.get((keyword).toLowerCase());
	}

	@Override
	public void extractInformation() {
        wordsNHT = new MyHashTable<>();
        String[] ratingCategories = {"1", "2", "3", "4", "5"};

        for (int i = 0; i < parser.data.size(); i++) {
            String[] row = parser.data.get(i);
            String comments = row[parser.fields.get("comments")];
            String starRating = row[parser.fields.get("student_star")]; // = row[4]

            String rating = getRatingCategory(starRating);
            ArrayList<String> sentence = separateWords(comments);
            ArrayList<String> separatedWords = removeDuplicates(sentence);


            for (String word : separatedWords) {
                if (wordsNHT.get(word) == null) {

                    ratingsHT = new MyHashTable<>();
                    for (String r : ratingCategories) {
                        ratingsHT.put(r, 0);
                    }
                    ratingsHT.put(rating, 1);
                    wordsNHT.put(word, ratingsHT);
                }

                else {
                    int ratingCount = wordsNHT.get(word).get(rating);
                    ratingCount += 1;
                    wordsNHT.get(word).put(rating, ratingCount);
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

    private String getRatingCategory(String qR) {
        double qualityRating = Double.parseDouble(qR);
        if (qualityRating >= 1 && qualityRating < 2) {
            return "1";
        } else if (qualityRating >= 2 && qualityRating < 3) {
            return "2";
        } else if (qualityRating >= 3 && qualityRating < 4) {
            return "3";
        } else if (qualityRating >= 4 && qualityRating < 5) {
            return "4";
        } else {
            return "5";
        }
    }

}
