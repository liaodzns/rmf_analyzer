package finalproject;

public class RatingDistributionByProf extends DataAnalyzer {

    private MyHashTable<String, MyHashTable<String, Integer>> professorNHT;
    private MyHashTable<String, Integer> ratingsHT;
    public RatingDistributionByProf(Parser p) {
        super(p);
    }

	@Override
	public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
		// ADD YOUR CODE BELOW THIS
        keyword = keyword.toLowerCase();
        keyword = keyword.trim();
        return professorNHT.get(keyword);
		//ADD YOUR CODE ABOVE THIS
	}

	@Override
	public void extractInformation() {
        professorNHT = new MyHashTable<>();
        String[] ratingCategories = {"1", "2", "3", "4", "5"};
		// ADD YOUR CODE BELOW THIS

        for (int i = 0; i < parser.data.size(); i++ ) {
            String[] row = parser.data.get(i);
            String professorName = (row[parser.fields.get("professor_name")]).toLowerCase();  // = row[0]
            String starRating = row[parser.fields.get("student_star")]; // = row[4]
            String ratingCategory = getRatingCategory(starRating);

            if (professorNHT.get(professorName) == null) {
                ratingsHT = new MyHashTable<>();
                for (String rating : ratingCategories) {
                    ratingsHT.put(rating, 0);
                }
                ratingsHT.put(ratingCategory, 1);
                professorNHT.put(professorName, ratingsHT);
            }

            else {
                int ratingCountByProf = professorNHT.get(professorName).get(ratingCategory);
                ratingCountByProf += 1;
                professorNHT.get(professorName).put(ratingCategory, ratingCountByProf);
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

}

