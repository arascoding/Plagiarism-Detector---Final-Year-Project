
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * @author Aras Abdo
 *
 */

public class Frame {

	private JFrame frame;
	public static String path; // = "C:\\Users\\Aras Abdo\\Desktop\\New folder";

	// Array of file extensions that have to be checked. Extensions will be defined
	// by user in JTextField
	static ArrayList<String> extensions = new ArrayList<String>();

	// Array of files that have to be excluded. Blacklisted files will be defined by
	// user in JTextField
	static ArrayList<String> blacklist = new ArrayList<String>();

	// The minimum amount of token matching in a row. Default is 6, can be changed
	// by user in JTextField
	public static int minimumTokenMatch = 6;

	// The minimum similarity.
	public static float minimumSimilarity = 0.5f;

	public static String fileName1;
	public static String fileName2;
	public static ArrayList<String> susBool = new ArrayList<>();
	public static ArrayList<String> susSimilarity = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 378, 252);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		JLabel label1 = new JLabel("Choose Base Directory:");
		label1.setBounds(13, 18, 159, 16);
		frame.getContentPane().add(label1);

		JLabel labelSet = new JLabel("");
		labelSet.setHorizontalAlignment(SwingConstants.CENTER);
		labelSet.setBounds(312, 21, 50, 16);
		frame.getContentPane().add(labelSet);

		JLabel label2 = new JLabel("Programming Language Extention:");
		label2.setBounds(13, 48, 201, 16);
		frame.getContentPane().add(label2);

		JLabel label3 = new JLabel("Blacklisted Files to Exclude:");
		label3.setBounds(13, 81, 159, 16);
		frame.getContentPane().add(label3);

		JLabel label4 = new JLabel("Minimum Token Match:");
		label4.setBounds(13, 114, 159, 16);
		frame.getContentPane().add(label4);

		JLabel label5 = new JLabel("Minimum Similarity:");
		label5.setBounds(13, 144, 159, 16);
		frame.getContentPane().add(label5);

		/**
		 * ComboBox for File Extensions to Check and BlackList of Files to Exclude, Also
		 * button that runs the Plagiarism detector. Takes ComboBox input and stores in
		 * to appropriate array.
		 */

		// ComboBox with list of items, where user can also set their own item aswell
		JComboBox<String> comboBoxExtensions = new JComboBox();
		comboBoxExtensions.setSelectedItem(null);
		comboBoxExtensions.addItem("java");
		comboBoxExtensions.addItem("c");
		comboBoxExtensions.addItem("h");
		comboBoxExtensions.addItem("Add New");
		comboBoxExtensions.setEditable(true);
		comboBoxExtensions.setBounds(224, 48, 138, 22);
		frame.getContentPane().add(comboBoxExtensions);

		JComboBox<String> comboBoxBlacklisted = new JComboBox();
		comboBoxBlacklisted.setSelectedItem(null);
		comboBoxBlacklisted.addItem("example.java");
		comboBoxBlacklisted.addItem("example.project");
		comboBoxBlacklisted.setEditable(true);
		comboBoxBlacklisted.setBounds(224, 81, 138, 22);
		frame.getContentPane().add(comboBoxBlacklisted);

		JTextField textFieldMinimumTokenMatch = new JTextField();
		textFieldMinimumTokenMatch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				char c = arg0.getKeyChar();
				if (!Character.isDigit(c) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)) {
					arg0.consume();
				}
			}
		});
		textFieldMinimumTokenMatch.setToolTipText("Default at 6, unless otherwise required");
		textFieldMinimumTokenMatch.setText("6");
		textFieldMinimumTokenMatch.setColumns(10);
		textFieldMinimumTokenMatch.setBounds(224, 114, 58, 22);
		frame.getContentPane().add(textFieldMinimumTokenMatch);

		JTextField textFieldMinimumSimilarity = new JTextField();
		textFieldMinimumSimilarity.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (Character.isLetter(c) && !e.isAltDown()) {
					e.consume();
				}
			}
		});
		textFieldMinimumSimilarity.setToolTipText("Default at 0.5f, unless otherwise required");
		textFieldMinimumSimilarity.setText("0.5");
		textFieldMinimumSimilarity.setColumns(10);
		textFieldMinimumSimilarity.setBounds(224, 144, 58, 22);
		frame.getContentPane().add(textFieldMinimumSimilarity);

		JFileChooser directoryChooser = new JFileChooser();

		JButton btnNewButton_1 = new JButton("Select");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Null sets it to the default path of the computer.
				directoryChooser.setCurrentDirectory(null);
				directoryChooser.setDialogTitle("Choose Directory");
				directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int returnValue = directoryChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					// Sets path to the location of the directory chosen by user.
					path = directoryChooser.getSelectedFile().getAbsolutePath();
					labelSet.setText("Path Set");
				} else {
					labelSet.setText("Not Set");
				}
			}
		});
		btnNewButton_1.setBounds(224, 18, 78, 23);
		frame.getContentPane().add(btnNewButton_1);

		JButton btnNewButton = new JButton("Run Comparison");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String allowedExtentionPicked = (String) comboBoxExtensions.getSelectedItem();
				String excludedExtentionPicked = (String) comboBoxBlacklisted.getSelectedItem();
				// checks if string is null, or contains a dot, or starts with a dot.
				if (allowedExtentionPicked == null || allowedExtentionPicked.contains(".")
						|| allowedExtentionPicked.startsWith(".")) {
					JOptionPane.showMessageDialog(null, "Please insure you have entered a valid input");
				} else if ((excludedExtentionPicked == null || !excludedExtentionPicked.contains(".")
						|| excludedExtentionPicked.startsWith("."))) {
					JOptionPane.showMessageDialog(null,
							"Please insure you have entered a file name with its extension. Example [excludeThis.ping]");
				} else if ((textFieldMinimumTokenMatch.getText().isEmpty()
						|| Integer.parseInt(textFieldMinimumTokenMatch.getText()) < 1
						|| Integer.parseInt(textFieldMinimumTokenMatch.getText()) > 15)) {
					JOptionPane.showMessageDialog(null,
							"Please insure you have entered a value greater than 1 and less than 15.");
				} else if ((textFieldMinimumSimilarity.getText().isEmpty()
						|| Float.parseFloat(textFieldMinimumSimilarity.getText()) < 0
						|| Float.parseFloat(textFieldMinimumSimilarity.getText()) > 1)) {
					JOptionPane.showMessageDialog(null,
							"Please insure you have entered a value less than 1 and greater than 0.");
				} else if (path == null) {
					JOptionPane.showMessageDialog(null, "Please select the base directory.");
				} else {
					extensions.clear();
					blacklist.clear();

					extensions.add(allowedExtentionPicked);
					blacklist.add(excludedExtentionPicked);
					minimumTokenMatch = Integer.parseInt(textFieldMinimumTokenMatch.getText());
					minimumSimilarity = Float.parseFloat(textFieldMinimumSimilarity.getText());

					try {
						forMain();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						createVisualiserFrame();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnNewButton.setBounds(123, 183, 159, 29);
		frame.getContentPane().add(btnNewButton);

	}

	public static void forMain() throws InterruptedException {
		ArrayList<String> directoryNames = new ArrayList<>();
		ArrayList<String> fileStrings = new ArrayList<>();
		List<Job> jobList = new ArrayList<>();
		MultithreadingWorker[] mtWorkerList = new MultithreadingWorker[Runtime.getRuntime().availableProcessors() + 1];
		Semaphore mutex = new Semaphore(1);

		// gets files from directory.
		ReaderWriter.getDirectoriesFromPathAndFillInformation(directoryNames, fileStrings, path, extensions, blacklist);

		// Adds the Strings to the StringBuilders, that will be .csv files
		ReaderWriter.append("Folder Names: ");
		for (int i = 0; i < directoryNames.size(); i++) {
			if (i != directoryNames.size() - 1) {
				ReaderWriter.append(directoryNames.get(i) + " : ");
			} else {
				ReaderWriter.append(directoryNames.get(i) + "\n");
			}
		}

		/**
		 * Sets the two strings that need to be compared into the job class, then new
		 * job is added to the list. e.g. first directory (first for loop), is matched
		 * with all other directories (nested loop).
		 */
		for (int i = 0; i < directoryNames.size(); i++) {
			for (int j = 0; j < directoryNames.size(); j++) {
				Job newJob = new Job();
				newJob.firstDirectory = fileStrings.get(i);
				newJob.secondDirectory = fileStrings.get(j);
				jobList.add(newJob);
			}
		}

		// pendingJobs that will be started in turn.
		for (int i = 0; i < mtWorkerList.length; i++) {
			mtWorkerList[i] = new MultithreadingWorker(jobList, mutex);
			mtWorkerList[i].start();
		}
		for (int i = 0; i < mtWorkerList.length; i++) {
			mtWorkerList[i].join();
		}

		for (int i = 0; i < directoryNames.size(); i++) {
			ReaderWriter.append(directoryNames.get(i) + ": ");
			String str1 = fileStrings.get(i);

			String string = Normaliser.normaliser(str1, false);

			List<String> strList = ReaderWriter.createTokenList(string);
			boolean[] strDir = new boolean[strList.size()];
			for (int j = 0; j < strDir.length; j++) {
				strDir[j] = false;
			}
			for (int k = 0; k < directoryNames.size(); k++) {
				if (i != k) {
					Job job = jobList.get((i * directoryNames.size()) + k);

					ReaderWriter.setTokenWithPlagiarism(strDir, job.tiles);
					ReaderWriter.extra(job);
					System.out.println(directoryNames.get(i) + ":" + directoryNames.get(k) + "= "
							+ String.format("%04f", job.similarity) + " Suspected Plagiarism = "
							+ job.suspectedPlagiarism);
					susBool.add(Boolean.toString(job.suspectedPlagiarism));
					susSimilarity.add(String.format("%04f", job.similarity));
				}
			}
			StringBuilder stringBuilderHTML;
			stringBuilderHTML = ReaderWriter.createPlagiarismIndication(string, strList, strDir);
			ReaderWriter.writeFile(
					"HTML File Indicatior - " + directoryNames.get(i) + " - " + minimumTokenMatch + ".html",
					stringBuilderHTML.toString());
			ReaderWriter.append("\n");
			fileName1 = "HTML File Indicatior - " + directoryNames.get(0) + " - " + minimumTokenMatch + ".html";
			fileName2 = "HTML File Indicatior - " + directoryNames.get(1) + " - " + minimumTokenMatch + ".html";
		}
		ReaderWriter.writeCSV(minimumTokenMatch);

	}

	public static void createVisualiserFrame() throws MalformedURLException, IOException {
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 1212, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);

		// Visualiser | Two Programs Displayed
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(10, 11, 1184, 696);
		frame.getContentPane().add(splitPane);
		splitPane.setDividerLocation(592);
	
		JEditorPane leftScrollPane = new JEditorPane();
		leftScrollPane.setEditable(false);
		leftScrollPane.setContentType("text/html");

		JEditorPane rightScrollPane = new JEditorPane();
		rightScrollPane.setEditable(false);
		rightScrollPane.setContentType("text/html");
		 
		JScrollPane scrollPane = new JScrollPane(leftScrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        splitPane.setLeftComponent(scrollPane);
        
        JScrollPane scrollPane2 = new JScrollPane(rightScrollPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		splitPane.setRightComponent(scrollPane2);


		JLabel labelSimilarityResult = new JLabel("Similarity Result: ");
		labelSimilarityResult.setFont(new Font("Tahoma", Font.PLAIN, 15));
		labelSimilarityResult.setBounds(20, 718, 339, 33);
		frame.getContentPane().add(labelSimilarityResult);

		float temp = Float.parseFloat(susSimilarity.get(0)) * 100;
		labelSimilarityResult.setText("Similarity Value: " + temp);

		JLabel lblSuspectedPlagiarism = new JLabel("Suspected Plagiarism: ");
		lblSuspectedPlagiarism.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSuspectedPlagiarism.setBounds(375, 718, 339, 33);
		frame.getContentPane().add(lblSuspectedPlagiarism);
		lblSuspectedPlagiarism.setText("Suspected Plagiarism: " + susBool.get(0));

		File folder = new File(".");
		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File current : files) {
				if (current.getName().equals(fileName1)) {
					leftScrollPane.setPage(current.toURI().toURL());
				} else if (current.getName().equals(fileName2)) {
					rightScrollPane.setPage(current.toURI().toURL());
				}
			}
		}
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
}
