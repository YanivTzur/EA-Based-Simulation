package ex12Q2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import ex12Q2.EASimulationController.PreferenceListGenerationMode;

public class EASimulationGraphicalView extends JFrame implements ActionListener
{	
	private EASimulationController _controller; // Controls the internal logic of the three-way matching
											    // simulation program.
	/**
	 * Needed for extending JFrame.
	 */
	private static final long serialVersionUID = -1865535495747027302L;
	private JTextField _numberOfRunsTextField; // Used to control the number of consecutive runs to run
											   // the simulation for.
	private JTextField _timeBtwnGenTextField; // The time in milliseconds that passes between one
										      // generation to the next.
	private JLabel _currGenNumberLabel; // The number of the current generation.
	private JLabel _numOfMenLabel; // Shows the number of men in the solution space.
	private JLabel _numOfWomenLabel; // Shows the number of women in the solution space.
	private JLabel _numOfDogsLabel; // Shows the number of dogs in the solution space.
	private JComboBox<String> _preferenceListGenerationModeComboBox; // Dictates what mode should be
																	 // used for generating preference
																	 // lists for individuals.
	private JCheckBox _crossOverEnabledCheckbox; // Controls whether crossover should be used when
										// certain amount of time.
	private JLabel _elapsedTimeLabel; // Shows the time elapsed since the beginning of the current
									  // run.
	private JLabel _currRunNumLabel; // Shows the ordinal number of the current run carried out
	private JTextField numberOfChromosomesTextField;
	private JTextField _numberOfGenerationsTextField;
									 // in the current executed sequence of runs.
	private JPanel _resultsPanel;
	private DefaultCategoryDataset _dataset;
	private JFreeChart _chart;
	private ChartPanel _chartPanel;
	private JPanel _chartContainingPanel;
	private JTextArea _resultsTextArea;
	
	public EASimulationGraphicalView() 
	{
		try 
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (ClassNotFoundException | InstantiationException 
				| IllegalAccessException | UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
		setTitle("Three-way Matching Simulation Program");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
		Insets defaultInsets = new Insets(0, 5, 5, 5);
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(1, 2, 0, 0));
		
		_resultsPanel = new JPanel();
		_resultsPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		mainPanel.add(_resultsPanel);
		GridBagLayout gbl__resultsPanel = new GridBagLayout();
		_resultsPanel.setLayout(gbl__resultsPanel);
		
		JLabel resultsPaneTitle = new JLabel("Results");
		resultsPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
		resultsPaneTitle.setFont(titleFont);
		GridBagConstraints gbc_resultsPaneTitle = new GridBagConstraints();
		gbc_resultsPaneTitle.anchor = GridBagConstraints.NORTH;
		gbc_resultsPaneTitle.weightx = 1.0;
		gbc_resultsPaneTitle.insets = new Insets(0, 0, 5, 0);
		gbc_resultsPaneTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_resultsPaneTitle.gridx = 0;
		gbc_resultsPaneTitle.gridy = 0;
		_resultsPanel.add(resultsPaneTitle, gbc_resultsPaneTitle);
		
		_chartContainingPanel = new JPanel();
		GridBagConstraints gbc__chartContainingPanel = new GridBagConstraints();
		gbc__chartContainingPanel.weighty = 0.6;
		gbc__chartContainingPanel.weightx = 1.0;
		gbc__chartContainingPanel.insets = new Insets(0, 0, 5, 0);
		gbc__chartContainingPanel.fill = GridBagConstraints.BOTH;
		gbc__chartContainingPanel.gridx = 0;
		gbc__chartContainingPanel.gridy = 1;
		_resultsPanel.add(_chartContainingPanel, gbc__chartContainingPanel);
		_chartContainingPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel _resultsTextPanel = new JPanel();
		GridBagConstraints gbc__resultsTextPanel = new GridBagConstraints();
		gbc__resultsTextPanel.weighty = 0.4;
		gbc__resultsTextPanel.weightx = 1.0;
		gbc__resultsTextPanel.fill = GridBagConstraints.BOTH;
		gbc__resultsTextPanel.gridx = 0;
		gbc__resultsTextPanel.gridy = 2;
		_resultsPanel.add(_resultsTextPanel, gbc__resultsTextPanel);
		_resultsTextPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane resultsScrollPane = new JScrollPane();
		resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_resultsTextPanel.add(resultsScrollPane, BorderLayout.CENTER);
		
		_resultsTextArea = new JTextArea();
		_resultsTextArea.setFont(new Font(_resultsTextArea.getFont().getFamily(),
										  _resultsTextArea.getFont().getStyle(),
										  12));
		resultsScrollPane.setViewportView(_resultsTextArea);
		
		JPanel controlsPanel = new JPanel();
		mainPanel.add(controlsPanel);
		controlsPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		controlsPanel.setLayout(new GridLayout(3, 1, 5, 0));
		
		JPanel runControlPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		runControlPanel.setLayout(gridBagLayout);
		
		JPanel parametersPanel = new JPanel();
		controlsPanel.add(parametersPanel);
		
		JPanel statisticsPanel = new JPanel();
		statisticsPanel.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(0, 0, 0)));
		controlsPanel.add(statisticsPanel);
		GridBagLayout gbl_statisticsPanel = new GridBagLayout();
		gbl_statisticsPanel.columnWidths = new int[]{0, 0, 0};
		gbl_statisticsPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_statisticsPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_statisticsPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		statisticsPanel.setLayout(gbl_statisticsPanel);
		
		_elapsedTimeLabel = new JLabel("0");
		GridBagConstraints gbc__elapsedTimeLabel = new GridBagConstraints();
		gbc__elapsedTimeLabel.insets = new Insets(0, 5, 5, 0);
		gbc__elapsedTimeLabel.gridx = 1;
		gbc__elapsedTimeLabel.gridy = 1;
		statisticsPanel.add(_elapsedTimeLabel, gbc__elapsedTimeLabel);
		
		JLabel elapsedTimeDescLabel = new JLabel("Elapsed Time (sec):");
		GridBagConstraints gbc_elapsedTimeDescLabel = new GridBagConstraints();
		gbc_elapsedTimeDescLabel.anchor = GridBagConstraints.WEST;
		gbc_elapsedTimeDescLabel.insets = new Insets(0, 5, 5, 5);
		gbc_elapsedTimeDescLabel.gridx = 0;
		gbc_elapsedTimeDescLabel.gridy = 1;
		statisticsPanel.add(elapsedTimeDescLabel, gbc_elapsedTimeDescLabel);
		
		JLabel statisticsPanelTitleLabel = new JLabel("Statistics");
		GridBagConstraints gbc_statisticsPanelTitleLabel = new GridBagConstraints();
		gbc_statisticsPanelTitleLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_statisticsPanelTitleLabel.gridx = 0;
		gbc_statisticsPanelTitleLabel.gridy = 0;
		gbc_statisticsPanelTitleLabel.insets = new Insets(0, 5, 5, 5);
		statisticsPanelTitleLabel.setFont(titleFont);
		statisticsPanel.add(statisticsPanelTitleLabel, gbc_statisticsPanelTitleLabel);
		
		_currRunNumLabel = new JLabel("0");
		GridBagConstraints gbc__currRunNumLabel = new GridBagConstraints();
		gbc__currRunNumLabel.insets = new Insets(0, 5, 5, 0);
		gbc__currRunNumLabel.gridx = 1;
		gbc__currRunNumLabel.gridy = 2;
		statisticsPanel.add(_currRunNumLabel, gbc__currRunNumLabel);
		
		JLabel currRunNumDescLabel = new JLabel("Current Run:");
		GridBagConstraints gbc_currRunNumDescLabel = new GridBagConstraints();
		gbc_currRunNumDescLabel.anchor = GridBagConstraints.WEST;
		gbc_currRunNumDescLabel.insets = new Insets(0, 5, 5, 5);
		gbc_currRunNumDescLabel.gridx = 0;
		gbc_currRunNumDescLabel.gridy = 2;
		statisticsPanel.add(currRunNumDescLabel, gbc_currRunNumDescLabel);
		
		JLabel curGenNumberDescriptionLabel = new JLabel("Current Generation:");
		GridBagConstraints gbc_curGenNumberDescriptionLabel = new GridBagConstraints();
		gbc_curGenNumberDescriptionLabel.anchor = GridBagConstraints.WEST;
		gbc_curGenNumberDescriptionLabel.insets = defaultInsets;
		gbc_curGenNumberDescriptionLabel.gridx = 0;
		gbc_curGenNumberDescriptionLabel.gridy = 3;
		statisticsPanel.add(curGenNumberDescriptionLabel, gbc_curGenNumberDescriptionLabel);
		
		_currGenNumberLabel = new JLabel("0");
		GridBagConstraints gbc_currGenNumberLabel = new GridBagConstraints();
		gbc_currGenNumberLabel.insets = new Insets(0, 5, 5, 0);
		gbc_currGenNumberLabel.gridx = 1;
		gbc_currGenNumberLabel.gridy = 3;
		statisticsPanel.add(_currGenNumberLabel, gbc_currGenNumberLabel);
		
		JLabel numOfMenDescLabel = new JLabel("Number of Men:");
		GridBagConstraints gbc_numOfMenDescLabel = new GridBagConstraints();
		gbc_numOfMenDescLabel.anchor = GridBagConstraints.WEST;
		gbc_numOfMenDescLabel.insets = defaultInsets;
		gbc_numOfMenDescLabel.gridx = 0;
		gbc_numOfMenDescLabel.gridy = 4;
		statisticsPanel.add(numOfMenDescLabel, gbc_numOfMenDescLabel);
		
		_numOfMenLabel = new JLabel("0");
		GridBagConstraints gbc_numOfMenLabel = new GridBagConstraints();
		gbc_numOfMenLabel.insets = new Insets(0, 5, 5, 0);
		gbc_numOfMenLabel.gridx = 1;
		gbc_numOfMenLabel.gridy = 4;
		statisticsPanel.add(_numOfMenLabel, gbc_numOfMenLabel);
		
		JLabel numOfWomenDescLabel = new JLabel("Number of Women:");
		GridBagConstraints gbc_numOfWomenDescLabel = new GridBagConstraints();
		gbc_numOfWomenDescLabel.anchor = GridBagConstraints.WEST;
		gbc_numOfWomenDescLabel.insets =defaultInsets;
		gbc_numOfWomenDescLabel.gridx = 0;
		gbc_numOfWomenDescLabel.gridy = 5;
		statisticsPanel.add(numOfWomenDescLabel, gbc_numOfWomenDescLabel);
		
		_numOfWomenLabel = new JLabel("0");
		GridBagConstraints gbc_numOfWomenLabel = new GridBagConstraints();
		gbc_numOfWomenLabel.insets = new Insets(0, 5, 5, 0);
		gbc_numOfWomenLabel.gridx = 1;
		gbc_numOfWomenLabel.gridy = 5;
		statisticsPanel.add(_numOfWomenLabel, gbc_numOfWomenLabel);
		
		JLabel numOfDogsDescLabel = new JLabel("Number of Dogs:");
		GridBagConstraints gbc_numOfDogsDescLabel = new GridBagConstraints();
		gbc_numOfDogsDescLabel.anchor = GridBagConstraints.WEST;
		gbc_numOfDogsDescLabel.insets = new Insets(0, 5, 0, 5);
		gbc_numOfDogsDescLabel.gridx = 0;
		gbc_numOfDogsDescLabel.gridy = 6;
		statisticsPanel.add(numOfDogsDescLabel, gbc_numOfDogsDescLabel);
		
		_numOfDogsLabel = new JLabel("0");
		GridBagConstraints gbc_numOfDogsLabel = new GridBagConstraints();
		gbc_numOfDogsLabel.gridx = 1;
		gbc_numOfDogsLabel.gridy = 6;
		gbc_numOfDogsLabel.insets = new Insets(0, 5, 0, 0);
		statisticsPanel.add(_numOfDogsLabel, gbc_numOfDogsLabel);
		controlsPanel.add(runControlPanel);
		
		JLabel runControlTitleLabel = new JLabel("Run Control");
		runControlTitleLabel.setFont(titleFont);
		GridBagConstraints gbc_runControlTitleLabel = new GridBagConstraints();
		gbc_runControlTitleLabel.weightx = 0.1;
		gbc_runControlTitleLabel.anchor = GridBagConstraints.WEST;
		gbc_runControlTitleLabel.insets = defaultInsets;
		gbc_runControlTitleLabel.gridx = 0;
		gbc_runControlTitleLabel.gridy = 0;
		runControlPanel.add(runControlTitleLabel, gbc_runControlTitleLabel);
		
		String numberOfGenerationsString = String.valueOf(EASimulationController.DEFAULT_NUMBER_OF_GENERATIONS);
		_numberOfGenerationsTextField = new JTextField();
		_numberOfGenerationsTextField.setToolTipText("Number of generations per run.");
		_numberOfGenerationsTextField.setText(numberOfGenerationsString);
		GridBagConstraints gbc__numberOfGenerationsTextField = new GridBagConstraints();
		gbc__numberOfGenerationsTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc__numberOfGenerationsTextField.insets = new Insets(0, 0, 5, 0);
		gbc__numberOfGenerationsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc__numberOfGenerationsTextField.gridx = 1;
		gbc__numberOfGenerationsTextField.gridy = 2;
		runControlPanel.add(_numberOfGenerationsTextField, gbc__numberOfGenerationsTextField);
		_numberOfGenerationsTextField.setColumns(10);
		
		JLabel numberOfRunsLabel = new JLabel("Number of Runs:");
		GridBagConstraints gbc_numberOfRunsLabel = new GridBagConstraints();
		gbc_numberOfRunsLabel.weightx = 0.1;
		gbc_numberOfRunsLabel.anchor = GridBagConstraints.WEST;
		gbc_numberOfRunsLabel.insets = defaultInsets;
		gbc_numberOfRunsLabel.gridx = 0;
		gbc_numberOfRunsLabel.gridy = 1;
		runControlPanel.add(numberOfRunsLabel, gbc_numberOfRunsLabel);
		
		String numberOfRunsString = String.valueOf(EASimulationController.DEFAULT_NUM_OF_RUNS);
		_numberOfRunsTextField = new JTextField(numberOfRunsString);
		_numberOfRunsTextField.setToolTipText("Number of consecutive times to run the simulation.");
		GridBagConstraints gbc_numberOfRunsTextField = new GridBagConstraints();
		gbc_numberOfRunsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_numberOfRunsTextField.weightx = 1.0;
		gbc_numberOfRunsTextField.anchor = GridBagConstraints.WEST;
		gbc_numberOfRunsTextField.insets = new Insets(0, 0, 5, 0);
		gbc_numberOfRunsTextField.gridx = 1;
		gbc_numberOfRunsTextField.gridy = 1;
		runControlPanel.add(_numberOfRunsTextField, gbc_numberOfRunsTextField);
		_numberOfRunsTextField.setColumns(10);
		
		JLabel numberOfGenerationsLabel = new JLabel("Number of Generations:");
		GridBagConstraints gbc_numberOfGenerationsLabel = new GridBagConstraints();
		gbc_numberOfGenerationsLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_numberOfGenerationsLabel.insets = new Insets(0, 5, 5, 5);
		gbc_numberOfGenerationsLabel.gridx = 0;
		gbc_numberOfGenerationsLabel.gridy = 2;
		runControlPanel.add(numberOfGenerationsLabel, gbc_numberOfGenerationsLabel);
		
		JLabel timeBtwnGenLabel = new JLabel("Time Between Generations (msec):");
		GridBagConstraints gbc_timeBtwnGenLabel = new GridBagConstraints();
		gbc_timeBtwnGenLabel.anchor = GridBagConstraints.WEST;
		gbc_timeBtwnGenLabel.weightx = 0.1;
		gbc_timeBtwnGenLabel.insets = defaultInsets;
		gbc_timeBtwnGenLabel.gridx = 0;
		gbc_timeBtwnGenLabel.gridy = 3;
		runControlPanel.add(timeBtwnGenLabel, gbc_timeBtwnGenLabel);
		
		String timeBtwnGenerationsString = String.valueOf(EASimulationController.DEFAULT_TIME_BTWN_GENERATIONS);
		_timeBtwnGenTextField = new JTextField(timeBtwnGenerationsString);
		_timeBtwnGenTextField.setToolTipText("The time it takes to advance from the current"
											 + 
										     " generation to the next.");
		GridBagConstraints gbc_timeBtwnGenTextField = new GridBagConstraints();
		gbc_timeBtwnGenTextField.weightx = 1.0;
		gbc_timeBtwnGenTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_timeBtwnGenTextField.anchor = GridBagConstraints.WEST;
		gbc_timeBtwnGenTextField.gridx = 1;
		gbc_timeBtwnGenTextField.gridy = 3;
		gbc_timeBtwnGenTextField.insets = new Insets(0, 0, 5, 0);
		runControlPanel.add(_timeBtwnGenTextField, gbc_timeBtwnGenTextField);
		_timeBtwnGenTextField.setColumns(10);
		
		JPanel runControlButtonsPanel = new JPanel();
		FlowLayout fl_runControlButtonsPanel = (FlowLayout) runControlButtonsPanel.getLayout();
		fl_runControlButtonsPanel.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_runControlButtonsPanel = new GridBagConstraints();
		gbc_runControlButtonsPanel.weightx = 0.1;
		gbc_runControlButtonsPanel.anchor = GridBagConstraints.NORTHWEST;
		gbc_runControlButtonsPanel.insets = new Insets(0, 0, 0, 5);
		gbc_runControlButtonsPanel.gridx = 0;
		gbc_runControlButtonsPanel.gridy = 4;
		runControlPanel.add(runControlButtonsPanel, gbc_runControlButtonsPanel);
		
		JButton startButton = new JButton("Start");
		startButton.setToolTipText("Start a new run.");
		startButton.addActionListener(this);
		runControlButtonsPanel.add(startButton);
		
		JButton continueButton = new JButton("Continue");
		continueButton.setToolTipText("Continue a paused run.");
		continueButton.addActionListener(this);
		runControlButtonsPanel.add(continueButton);
		
		JButton stopButton = new JButton("Stop/Pause");
		stopButton.setToolTipText("Halts the current run.");
		stopButton.addActionListener(this);
		runControlButtonsPanel.add(stopButton);
		
		parametersPanel.setBorder(new MatteBorder(0, 0, 2, 0, (Color) new Color(0, 0, 0)));
		GridBagLayout gbl_parametersPanel = new GridBagLayout();
		gbl_parametersPanel.columnWeights = new double[]{0.0, 1.0};
		parametersPanel.setLayout(gbl_parametersPanel);
		
		JLabel parametersPanelTitleLabel = new JLabel("Parameters");
		parametersPanelTitleLabel.setFont(titleFont);
		GridBagConstraints gbc_parametersPanelTitleLabel = new GridBagConstraints();
		gbc_parametersPanelTitleLabel.weightx = 0.1;
		gbc_parametersPanelTitleLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_parametersPanelTitleLabel.insets = defaultInsets;
		gbc_parametersPanelTitleLabel.gridx = 0;
		gbc_parametersPanelTitleLabel.gridy = 0;
		parametersPanel.add(parametersPanelTitleLabel, gbc_parametersPanelTitleLabel);
		
		JLabel numOfChromosomesLabel = new JLabel("Number of Chromosomes:");
		GridBagConstraints gbc_numOfChromosomesLabel = new GridBagConstraints();
		gbc_numOfChromosomesLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_numOfChromosomesLabel.insets = new Insets(0, 5, 5, 5);
		gbc_numOfChromosomesLabel.gridx = 0;
		gbc_numOfChromosomesLabel.gridy = 1;
		parametersPanel.add(numOfChromosomesLabel, gbc_numOfChromosomesLabel);
		
		numberOfChromosomesTextField = new JTextField();
		numberOfChromosomesTextField.setToolTipText("The number of chromosomes to use in the simulation. (Only applies when pressing \"Start\".)");
		numberOfChromosomesTextField.setText(String.valueOf(EASimulationModel.DEFAULT_NUMBER_OF_CHROMOSOMES));
		GridBagConstraints gbc_numberOfChromosomesTextField = new GridBagConstraints();
		gbc_numberOfChromosomesTextField.anchor = GridBagConstraints.NORTHWEST;
		gbc_numberOfChromosomesTextField.insets = new Insets(0, 9, 5, 0);
		gbc_numberOfChromosomesTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_numberOfChromosomesTextField.gridx = 1;
		gbc_numberOfChromosomesTextField.gridy = 1;
		parametersPanel.add(numberOfChromosomesTextField, gbc_numberOfChromosomesTextField);
		numberOfChromosomesTextField.setColumns(10);
		
		JLabel preferencesLabel = new JLabel("Preference List Generation:");
		preferencesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_preferencesLabel = new GridBagConstraints();
		gbc_preferencesLabel.weightx = 0.1;
		gbc_preferencesLabel.insets = new Insets(0, 5, 5, 5);
		gbc_preferencesLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_preferencesLabel.gridx = 0;
		gbc_preferencesLabel.gridy = 2;
		parametersPanel.add(preferencesLabel, gbc_preferencesLabel);
		
		_preferenceListGenerationModeComboBox = new JComboBox<String>();
		_preferenceListGenerationModeComboBox.setToolTipText("Choose whether individuals should have constant preference lists or whether they should be regenerated randomly each generation.");
		_preferenceListGenerationModeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"One-Time Generation", "Re-generate Each Generation"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.NORTHEAST;
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(0, 9, 5, 0);
		gbc_comboBox.weightx = 1.0;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 2;
		parametersPanel.add(_preferenceListGenerationModeComboBox, gbc_comboBox);
		
		JLabel crossOverEnabledLabel = new JLabel("Crossover Enabled:");
		GridBagConstraints gbc_crossOverEnabledLabel = new GridBagConstraints();
		gbc_crossOverEnabledLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_crossOverEnabledLabel.insets = new Insets(0, 5, 0, 5);
		gbc_crossOverEnabledLabel.gridx = 0;
		gbc_crossOverEnabledLabel.gridy = 3;
		parametersPanel.add(crossOverEnabledLabel, gbc_crossOverEnabledLabel);
		
		_crossOverEnabledCheckbox = new JCheckBox("");
		_crossOverEnabledCheckbox.setSelected(true);
		_crossOverEnabledCheckbox.setToolTipText("Whether crossover should be applied when creating offspring.");
		GridBagConstraints gbc_crossOverEnabledCheckbox = new GridBagConstraints();
		gbc_crossOverEnabledCheckbox.weightx = 1.0;
		gbc_crossOverEnabledCheckbox.anchor = GridBagConstraints.NORTHWEST;
		gbc_crossOverEnabledCheckbox.insets = new Insets(0, 5, 0, 0);
		gbc_crossOverEnabledCheckbox.gridx = 1;
		gbc_crossOverEnabledCheckbox.gridy = 3;
		parametersPanel.add(_crossOverEnabledCheckbox, gbc_crossOverEnabledCheckbox);	
	}
	
	/**
	 * Updates the graphical display of the EA based three-way matching simulation program with
	 * the input fitness values, current generation number and number of each type of individual.
	 * @param printSummary Whether to print a summary of the results.
	 * @param clearResultsTextArea Whether to clear the results text area.
	 * @param isFirstUpdate Whether this is the first update, that is initialization and therefore
	 * 						no fitness values should be printed out.
	 * @param bestFitness The best fitness value in the current generation.
	 * @param averageFitness The average fitness value in the current generation.
	 * @param worstFitness The worst fitness value in the current generation.
	 * @param elapsedTime The time elapsed since the begininng of the current run.
	 * @param currentRunNumber The ordinal number of the current run in the currently executed
	 * 						   sequence of runs.
	 * @param currentGenNumber The number of the current generation.
	 * @param numOfMen The number of men in the solution space.
	 * @param numOfWomen The number of women in the solution space.
	 * @param numOfDogs The number of dogs in the solution space.
	 */
	public void updateView(boolean printSummary, boolean clearResultsTextArea, boolean isFirstUpdate,
						   int bestFitness, int averageFitness, int worstFitness,
						   int initialBestFitness, int bestSolutionFitness,
						   double elapsedTime, int currentRunNumber, int currentGenNumber,
						   int numOfMen, int numOfWomen, int numOfDogs) 
	{	
		if (printSummary)
			printSummary(initialBestFitness, bestSolutionFitness, currentRunNumber, 
						 currentGenNumber);
		else if (clearResultsTextArea) // Clear text area, ignore other arguments.
			_resultsTextArea.setText("");
		else // No need to clear results' text area, consider other arguments.
		{
			if (isFirstUpdate == false) // Not first update, print out fitness values by generation.
			{
				updateChart(bestFitness, averageFitness, worstFitness, 
							currentRunNumber, currentGenNumber);
				printFitnessValues(bestFitness, averageFitness, worstFitness,
						   	       currentRunNumber, currentGenNumber);
			}
			_currGenNumberLabel.setText(String.valueOf(currentGenNumber));
			_numOfMenLabel.setText(String.valueOf(numOfMen));
			_numOfWomenLabel.setText(String.valueOf(numOfWomen));
			_numOfDogsLabel.setText(String.valueOf(numOfDogs));
			_currRunNumLabel.setText(String.valueOf(currentRunNumber));
			_elapsedTimeLabel.setText(String.valueOf(elapsedTime));
		}
	}

	/**
	 * Updates the chart showing the history of fitness values so far for the current
	 * run.
	 * @param bestFitness The best fitness value in the current generation.
	 * @param averageFitness The average fitness value in the current generation.
	 * @param worstFitness The worst fitness value in the current generation.
	 * @param currentRunNumber The number of the current run.
	 * @param currentGenNumber The number of the current generation.
	 */
	private void updateChart(int bestFitness, int averageFitness, int worstFitness, 
							 int currentRunNumber, int currentGenNumber) 
	{
	    // Create dataset
		createDataset(bestFitness, averageFitness, worstFitness,
	    			  currentRunNumber, currentGenNumber);
	    
   		if (_controller.getRunNumber() == 0 
			||
			_controller.getRunNumber() > _controller.getPreviousRunNumber())
		{
		    // Create chart
		    _chart = ChartFactory.createLineChart(
		        "Run No. " + currentRunNumber, // Chart title
		        "Generation No.", // X-Axis Label
		        "Fitness Value", // Y-Axis Label
		        _dataset,
		        PlotOrientation.VERTICAL,
		        true, true, false
		        );
		    if (_chartPanel == null)
		    {
		    	_chartPanel = new ChartPanel(_chart);
			    _chartContainingPanel.add(_chartPanel, BorderLayout.CENTER);
		    }
		    else
		    	_chartPanel.setChart(_chart);
		}
	}

	/**
	 * Creates a new dataset for the chart of fitness values if necessary, and updates it using
	 * the input fitness values and the numbers of the current run and of the current generation.
	 * @param bestFitness The best fitness value in the current generation.
	 * @param averageFitness The average fitness value in the current generation.
	 * @param worstFitness The worst fitness value in the current generation.
	 * @param currentRunNumber The number of the current run.
	 * @param currentGenNumber The number of the current generation.
	 */
	private void createDataset(int bestFitness, int averageFitness, 
							   int worstFitness, int currentRunNumber, 
							   int currentGenNumber) 
	{
		String bestFitnessSeries = "Best Fitness";
	    String averageFitnessSeries = "Average Fitness";
	    String worstFitnessSeries = "Worst Fitness";

	    if (_controller.getCurrentGenerationNumber() == 0)
    		_dataset = new DefaultCategoryDataset();

	    _dataset.addValue(bestFitness, bestFitnessSeries, String.valueOf(currentGenNumber));
	    _dataset.addValue(averageFitness, averageFitnessSeries, String.valueOf(currentGenNumber));
	    _dataset.addValue(worstFitness, worstFitnessSeries, String.valueOf(currentGenNumber));
	}

	/**
	 * Prints the best, average and worst fitness values for the current generation in a formatted
	 * form, into the end of a file.
	 * @param bestFitness The best fitness value in the current generation.
	 * @param averageFitness The average fitness value in the current generation.
	 * @param worstFitness The worst fitness value in the current generation.
	 * @param currentRunNumber The number of the current run.
	 * @param currentGenNumber The serial number of the current generation (starts from 0).
	 */
	private void printFitnessValues(int bestFitness, int averageFitness, int worstFitness,
									int currentRunNumber, int currentGenNumber)
	{
		_resultsTextArea.append(String.format(
							    "Run No. %d - Generation No. %d:\n"
						        + 
						        "Best fitness: %d\n"
						        + 
						        "Average fitness: %d\n"
						        + 
						        "Worst fitness: %d\n",
						        currentRunNumber,
						        currentGenNumber,
						        bestFitness,
						        averageFitness,
						        worstFitness));
	}

	/**
	 * Prints a summary of the current run.
	 * @param initialBestFitness The initial best fitness in the population of the first generation
	 * 							 (generation 0).
	 * @param bestSolutionFitness The fitness of the best solution found during the entire run.
	 * @param currentRunNumber    The number of the current run.
	 * @param currentGenNumber    The number of the current generation.
	 */
	private void printSummary(int initialBestFitness, int bestSolutionFitness, 
							  int currentRunNumber, int currentGenNumber) 
	{
		_resultsTextArea.append(String.format(
			    "Run No. %d - Summary:\n"
			    + 
			    "Number of generations: %d:\n"
		        + 
		        "Best initial fitness: %d\n"
		        + 
		        "Fitness of the best solution in the run: %d\n",
		        currentRunNumber,
		        currentGenNumber,
		        initialBestFitness,
		        bestSolutionFitness));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		JButton button = null;
		EASimulationController.PreferenceListGenerationMode preferenceListGenerationMode = null;
		int numberOfChromosomes = getNumberOfChromosomes();
		boolean isCrossoverEnabled = getCrossoverEnabledState();
		int numOfRuns = getNumOfRuns();
		int numOfGenerationsPerRun = getNumOfGenerations();
		int timeBetweenGenerations = getTimeBetweenGenerations();
		preferenceListGenerationMode = getPreferenceListGenerationMode();
		if (source instanceof JButton)
		{
			button = (JButton) source;
			if (button.getText() == "Start") // Start a new sequence of runs from scratch.
				_controller.startRun(numberOfChromosomes, preferenceListGenerationMode, 
									 isCrossoverEnabled, numOfRuns, numOfGenerationsPerRun, 
									 timeBetweenGenerations);
			else if (button.getText() == "Continue") // Continue a previously ordered series of runs
													 // that has been paused.
				_controller.continueRun(preferenceListGenerationMode, isCrossoverEnabled,
						  		  	 	timeBetweenGenerations);
			else if (button.getText() == "Stop/Pause") // Stop/pause a current sequence of runs.
				_controller.stopRun();
		}
	}

	/**
	 * Returns the number of generations per run.
	 * @return the number of generations per run.
	 */
	private int getNumOfGenerations() 
	{
		int numOfGenerationsPerRun;
		try 
		{
			numOfGenerationsPerRun = Integer.parseInt(_numberOfGenerationsTextField.getText());
		} 
		catch (Exception e) 
		{
			numOfGenerationsPerRun = EASimulationController.DEFAULT_NUMBER_OF_GENERATIONS;
		}
		return numOfGenerationsPerRun;
	}

	/**
	 * Returns the number of chromosomes selected by the user.
	 * @return the number of chromosomes selected by the user.
	 */
	private int getNumberOfChromosomes() 
	{
		int returnedValue;
		try
		{
			returnedValue = Integer.parseInt(numberOfChromosomesTextField.getText());
		}
		catch(Exception e)
		{
			returnedValue = EASimulationModel.DEFAULT_NUMBER_OF_CHROMOSOMES;
		}
		return returnedValue;
	}

	/**
	 * Returns the selected preference list generation mode (e.g. whether preference lists should be
	 * generated once for a sequence of runs or generated each time a new run starts).
	 * @return The selected preference list generation mode.
	 */
	private PreferenceListGenerationMode getPreferenceListGenerationMode() 
	{
		String selectedMode = _preferenceListGenerationModeComboBox.getSelectedItem().toString();
		if (selectedMode == "Re-generate Each Generation")
			return PreferenceListGenerationMode.EACH_RUN;
		return PreferenceListGenerationMode.ONE_TIME;
	}

	/**
	 * Returns whether crossover should be used when creating offspring.
	 * @return Whether crossover should be used when creating offspring.
	 */
	private boolean getCrossoverEnabledState() 
	{
		return _crossOverEnabledCheckbox.isSelected();
	}
	
	/**
	 * Returns the number of runs entered by the user to execute in the current sequence of runs.
	 * If no value is entered or an invalid value is entered, the program performs a single run.
	 * @return If a valid value was entered in the field used for entering the number of desired runs
	 * 		   in the following sequence of runs, returns the number entered.
	 * 		   Otherwise, returns EASimulationController.DEFAULT_NUM_OF_RUNS.
	 */
	private int getNumOfRuns() 
	{
		int numOfRuns = EASimulationController.DEFAULT_NUM_OF_RUNS;
		String numOfRunsTextFieldContent = _numberOfRunsTextField.getText();
		if (numOfRunsTextFieldContent != null && numOfRunsTextFieldContent.equals("") == false)
		{
			try
			{
				numOfRuns = Integer.parseInt(numOfRunsTextFieldContent);
			}	
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null, "Invalid input in the field denoting number of "
													+ 
													"desired runs.");
			}
		}
		return numOfRuns;
	}
	
	/**
	 * Returns the time between generations in milliseconds entered by the user.
	 * If no value is entered or an invalid value is entered, the program waits 
	 * EASimulationController.DEFAULT_TIME_BTWN_GENERATIONS between each pair of consecutive
	 * generations.
	 * @return If a valid value was entered in the field used for entering the time to wait in
	 * 		   milliseconds between each pair of consecutive generations returns the time entered.
	 * 		   Otherwise, returns EASimulationController.DEFAULT_TIME_BTWN_GENERATIONS.
	 */
	private int getTimeBetweenGenerations() 
	{
		int timeBetweenGenerations = EASimulationController.DEFAULT_TIME_BTWN_GENERATIONS;
		String _timeBtwnGenTextFieldContent = _timeBtwnGenTextField.getText();
		if (_timeBtwnGenTextFieldContent != null && _timeBtwnGenTextFieldContent.equals("") == false)
		{
			try
			{
				timeBetweenGenerations = Integer.parseInt(_timeBtwnGenTextFieldContent);
			}	
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null, "Invalid input in the field denoting the time"
													+ 
													"between generations.");
			}
		}
		return timeBetweenGenerations;
	}
	
	/**
	 * Displays the graphical view of the simulation program.
	 */
	public void showView() 
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = getWindowSize(screenSize);
		setSize((int)windowSize.getWidth(), (int)windowSize.getHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Returns the desired window size based on the screen's resolution.
	 * @param screenSize The screen's resolution.
	 * @return The desired window size.
	 */
	private Dimension getWindowSize(Dimension screenSize)
	{
		return new Dimension((int)(screenSize.getWidth()*0.75), (int)(screenSize.getHeight()*0.75));
	}

	/**
	 * Sets the associated controller of the view to the input value.
	 * @param controller The controller to associate with the view.
	 */
	public void setController(EASimulationController controller)
	{
		_controller = controller;
	}
}