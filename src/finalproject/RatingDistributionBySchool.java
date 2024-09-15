package finalproject;

import java.util.ArrayList;


public class RatingDistributionBySchool extends DataAnalyzer {

    private MyHashTable<String, MyHashTable<String, double[]>> schoolNHT;
    private MyHashTable<String, double[]> professorHT;

    private MyHashTable<String, Integer> formattedHT;

    public RatingDistributionBySchool(Parser p) {
        super(p);
    }

    @Override
    public MyHashTable<String, Integer> getDistByKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        keyword = keyword.trim();
        formattedHT = new MyHashTable<>();
        MyHashTable<String, double[]> profsInfo = schoolNHT.get(keyword);
        ArrayList<String> profNames = profsInfo.getKeySet();
        for (String name: profNames) {
            double[] profStats = profsInfo.get(name);
            formattedHT.put(name + "\n" + calculateAverage(profStats[0], (int) profStats[1]), (int) profStats[1]);
        }
        return formattedHT;
    }

    @Override
    public void extractInformation() {
        schoolNHT = new MyHashTable<>();
        professorHT = new MyHashTable<>();

        for (int i = 0; i < parser.data.size(); i++) {
            String[] row = parser.data.get(i);
            String schoolName = (row[parser.fields.get("school_name")]).toLowerCase();  // = row[1]
            String professorName = (row[parser.fields.get("professor_name")]).toLowerCase();  // = row[0]
            String starRating = row[parser.fields.get("student_star")]; // = row[4]
            double rating = Double.parseDouble(starRating);

            if (schoolNHT.get(schoolName) == null) {

                double[] profStats = new double[2];
                profStats[0] = rating;
                profStats[1] = 1;
                professorHT.put(professorName, profStats);
                schoolNHT.put(schoolName, professorHT);

                professorHT = new MyHashTable<>();

            }
            else {  // there exists a hashtable for professors in this school
                if (schoolNHT.get(schoolName).get(professorName) == null) { // prof has no stats yet
                    double[] profStats = new double[2];
                    profStats[0] = rating;
                    profStats[1] = 1;
                    schoolNHT.get(schoolName).put(professorName, profStats);
                }
                else { // prof has stats
                    double[] profStats = schoolNHT.get(schoolName).get(professorName);
                    double curTotalRating = profStats[0];
                    double curCount = profStats[1];

                    profStats[0] = curTotalRating + rating;
                    profStats[1] = curCount + 1;
                    schoolNHT.get(schoolName).put(professorName, profStats);
                }
            }
        }
    }

    private double calculateAverage(double totalRating, int timesRated) {
        double result = totalRating / timesRated;
        result = Math.round(result * 100.0) / 100.0;
        return result;
    }
}
