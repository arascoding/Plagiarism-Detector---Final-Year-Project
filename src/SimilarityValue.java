
/**
 * @author Aras Abdo
 *
 */

/**
 * This class stores the information on the similarity, where the parameters are
 * the similarity between the files compared and sets suspectedplagiarism to
 * True if it is deemed plagiarized.
 */
public class SimilarityValue {
	public final float similarity;
	public final boolean suspectedPlagiarism;

	/**
	 * Constructor that takes two parameters upon initialisation, the value of the
	 * similarity between two files and whether the pair of files have been deemed
	 * as suspected plagiarism
	 */
	public SimilarityValue(float sim, boolean susPlagiarism) {
		super();
		this.similarity = sim;
		this.suspectedPlagiarism = susPlagiarism;
	}
}
