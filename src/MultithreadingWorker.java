
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author Aras Abdo
 *
 */

/**
 * Class for multithreading Class supported by
 * https://dzone.com/articles/dnp-java-concurrency-%E2%80%93-part-4
 * https://www.tutorialspoint.com/java/java_multithreading.htm
 */
public class MultithreadingWorker implements Runnable {
	private Thread thread;

	// The list of jobs that need to be completed.
	private List<Job> listOfJobs;

	/**
	 * So only one thread can enter the area. Once it has acquired the key (lock),
	 * it can proceed with the work and others must wait until the lock is released.
	 * [Just 1 Thread]
	 */
	private Semaphore mutex;

	/**
	 * Constructor to create the MultithreadingWorker, with list of pending jobs and
	 * the mutex. Once called new thread is created.
	 */
	public MultithreadingWorker(List<Job> pendingJobs, Semaphore mutex) {
		inside(pendingJobs, mutex);
	}

	// inside of the MultithreadingWorker constructor
	private final void inside(List<Job> pendingJobs, Semaphore mutex) {
		this.listOfJobs = pendingJobs;
		this.mutex = mutex;
		thread = new Thread(this);
	}

	// Start the thread
	public void start() {
		thread.start();
	}

	// Method throws InterruptedException, waits for end of all the jobs.
	public void join() throws InterruptedException {
		thread.join();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			// While loop to change the job status
			while (listOfJobs.size() > 0) {
				Job currentJob = null;
				// Acquiring the lock.
				mutex.acquire();
				// for each job, !check if it hasStarted, and set to true if it hasn't.
				for (int i = 0; i < listOfJobs.size(); i++) {
					if (!listOfJobs.get(i).hasStarted) {
						currentJob = listOfJobs.get(i);
						currentJob.hasStarted = true;
						break;
					}
				}
				// Releasing the lock.
				mutex.release();

				if (currentJob == null) {
					break;
				}

				String firstFile = Normaliser.normaliser(currentJob.firstDirectory, false);
				String secondFile = Normaliser.normaliser(currentJob.secondDirectory, false);

				List<String> firstDirectoryList = ReaderWriter.createTokenList(firstFile);
				List<String> secondDirectoryList = ReaderWriter.createTokenList(secondFile);
				ArrayList<TileValuesMatch> tiles = RunningKarpRabin_GreedyStringTiling.run(
						firstDirectoryList.toArray(new String[firstDirectoryList.size()]),
						secondDirectoryList.toArray(new String[secondDirectoryList.size()]), Frame.minimumTokenMatch);
				SimilarityValue similarityResult = SimilarityCalculator.calculateSimilarity(firstDirectoryList,
						secondDirectoryList, tiles, Frame.minimumSimilarity);

				float similarity = similarityResult.similarity;
				boolean suspectedPlagiarism = similarityResult.suspectedPlagiarism;
				currentJob.similarity = similarity;
				currentJob.suspectedPlagiarism = suspectedPlagiarism;
				currentJob.tiles = tiles;
			}
		} catch (InterruptedException e) {
			System.out.println("run() METHOD : " + e);
		}
	}
}
