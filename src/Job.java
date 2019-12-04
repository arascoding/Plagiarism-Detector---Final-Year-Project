
import java.util.ArrayList;

/**
 * @author Aras Abdo
 *
 */

// Multithreading class, details of what each job contains
public class Job {
	// Contents of the two sub directories at the main specified directory path.
	public String firstDirectory;
	public String secondDirectory;

	// Boolean value of job status
	public boolean hasStarted = false;

	// Result of the similarity
	public float similarity;

	// States if it has been deemed as plagarised
	public boolean suspectedPlagiarism;

	// named title that matches with another
	public ArrayList<TileValuesMatch> tiles = new ArrayList<>();
}
