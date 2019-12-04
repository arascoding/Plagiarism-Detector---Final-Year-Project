
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aras Abdo
 *
 */

/** Class that calculates the similarity between two source code files */
public class SimilarityCalculator {

	/**
	 * This method calculates the sum of all the tile lengths and returns this
	 * value. For loop is used to iterate over each tile
	 */
	private static int sumOfAllTileLengths(ArrayList<TileValuesMatch> tiles) {
		int sum = 0;
		for (TileValuesMatch tile : tiles) {
			sum += tile.length;
		}
		return sum;
	}

	/**
	 * Method returns the similarity value for string one and two's token and the
	 * similar tiles. 2* Sum(length of each tile) / (length of string one + length
	 * of string two)
	 */
	private static float similarity(ArrayList<TileValuesMatch> tiles, List<String> stringOneList,
			List<String> stringTwoList) {
		return ((float) (2 * sumOfAllTileLengths(tiles)) / (float) (stringOneList.size() + stringTwoList.size()));
	}

	public static SimilarityValue calculateSimilarity(List<String> stringOneList, List<String> stringTwoList,
			ArrayList<TileValuesMatch> tiles, float threshold) {
		float similarity = similarity(tiles, stringOneList, stringTwoList);
		boolean suspectedPlagiarism = false;

		if (similarity >= threshold) {
			suspectedPlagiarism = true;
		}

		return (new SimilarityValue(similarity, suspectedPlagiarism));
	}
}
