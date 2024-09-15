package finalproject;

import java.util.ArrayList;

public class RatingByGender extends DataAnalyzer{

    private MyHashTable<String, MyHashTable<String, MyHashTable<String, Integer>>> genderNNHT;
    private MyHashTable<String, MyHashTable<String, Integer>> typeRatingNHT;
    private MyHashTable<String, Integer> qualityHT;
    private MyHashTable<String, Integer> difficultyHT;

	public RatingByGender(Parser p) {
		super(p);
	}

	@Override
	public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
       // "F, difficulty"
        keyword = keyword.toLowerCase();
        keyword = keyword.trim();
        ArrayList<String> keys = separateWords(keyword);
        String gender = keys.get(0).toUpperCase();
        String ratingType = keys.get(1);

        return genderNNHT.get(gender).get(ratingType);
	}
	@Override
	public void extractInformation() {
        genderNNHT = new MyHashTable<>();

        for (int i = 0; i < parser.data.size(); i++) {
            String[] row = parser.data.get(i);
            String d = row[parser.fields.get("student_difficult")];
            String q = row[parser.fields.get("student_star")]; // = row[4]

            String difficulty = getRatingCategory(d);
            String quality = getRatingCategory(q);
            String gender = row[parser.fields.get("gender")];

            if (gender != "X") {
                if (genderNNHT.get(gender) == null) { // if there is no gender YET

                    String[] ratingCategories = {"1", "2", "3", "4", "5"};
                    difficultyHT = new MyHashTable<>();
                    qualityHT = new MyHashTable<>();
                    typeRatingNHT = new MyHashTable<>();
                    for (String r : ratingCategories) {
                        difficultyHT.put(r, 0);
                        qualityHT.put(r, 0);
                    }
                    typeRatingNHT.put("difficulty", difficultyHT);
                    typeRatingNHT.put("quality", qualityHT);
                    genderNNHT.put(gender, typeRatingNHT);

                    int diffCount = genderNNHT.get(gender).get("difficulty").get(difficulty);
                    int qualCount = genderNNHT.get(gender).get("quality").get(quality);
                    genderNNHT.get(gender).get("difficulty").put(difficulty, diffCount + 1);
                    genderNNHT.get(gender).get("quality").put(quality, qualCount + 1);
                } else {
                    int diffCount = genderNNHT.get(gender).get("difficulty").get(difficulty);
                    int qualCount = genderNNHT.get(gender).get("quality").get(quality);
                    genderNNHT.get(gender).get("difficulty").put(difficulty, diffCount + 1);
                    genderNNHT.get(gender).get("quality").put(quality, qualCount + 1);
                }
            }
        }
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
