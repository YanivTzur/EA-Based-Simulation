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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;

import ex12Q2.EASimulationController.PreferenceListGenerationMode;

public class EASimulationGraphicalView extends JFrame implements ActionListener
{	
	private EASimulationController _controller; // Controls the internal logic of the three-way matching
											   // simulation progrmam.
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
												 // creating offspring.
	private JTextArea _resultsTextArea; // Displays the results of running the simulation every
										// certain amount of time.
	private JLabel _elapsedTimeLabel; // Shows the time elapsed since the beginning of the current
									  // run.
	private JLabel _currRunNumLabel; // Shows the ordinal number of the current run carried out
									 // in the current executed sequence of runs.
	
	public EASimulationGraphicalView() 
	{
		setTitle("Three-way Matching Simulation Program");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
		Insets defaultInsets = new Insets(0, 5, 5, 5);
		
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		mainPanel.add(resultsPanel);
		resultsPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel resultsPaneTitle = new JLabel("Results");
		resultsPaneTitle.setHorizontalAlignment(SwingConstants.CENTER);
		resultsPaneTitle.setFont(titleFont);
		resultsPanel.add(resultsPaneTitle, BorderLayout.NORTH);
		
		JScrollPane resultsSectionScrollPane = new JScrollPane();
		resultsPanel.add(resultsSectionScrollPane);
		
		_resultsTextArea = new JTextArea();
		_resultsTextArea.setEditable(false);
		resultsSectionScrollPane.setViewportView(_resultsTextArea);
		
		JPanel controlsPanel = new JPanel();
		mainPanel.add(controlsPanel);
		controlsPanel.setBorder(new CompoundBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null), new BevelBorder(BevelBorder.LOWERED, null, null, null, null)));
		controlsPanel.setLayout(new GridLayout(3, 1, 5, 0));
		
		JPanel runControlPanel = new JPanel();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
		
		JLabel numberOfRunsLabel = new JLabel("Number of Runs:");
		GridBagConstraints gbc_numberOfRunsLabel = new GridBagConstraints();
		gbc_numberOfRunsLabel.weightx = 0.1;
		gbc_numberOfRunsLabel.anchor = GridBagConstraints.WEST;
		gbc_numberOfRunsLabel.insets = defaultInsets;
		gbc_numberOfRunsLabel.gridx = 0;
		gbc_numberOfRunsLabel.gridy = 1;
		runControlPanel.add(numberOfRunsLabel, gbc_numberOfRunsLabel);
		
		_numberOfRunsTextField = new JTextField();
		_numberOfRunsTextField.setToolTipText("Number of consecutive times to run the simulation.");
		GridBagConstraints gbc_numberOfRunsTextField = new GridBagConstraints();
		gbc_numberOfRunsTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_numberOfRunsTextField.weightx = 1.0;
		gbc_numberOfRunsTextField.anchor = GridBagConstraints.WEST;
		gbc_numberOfRunsTextField.insets = new Insets(0, 0, 5, 5);
		gbc_numberOfRunsTextField.gridx = 1;
		gbc_numberOfRunsTextField.gridy = 1;
		runControlPanel.add(_numberOfRunsTextField, gbc_numberOfRunsTextField);
		_numberOfRunsTextField.setColumns(10);
		
		JLabel timeBtwnGenLabel = new JLabel("Time Between Generations (msec):");
		GridBagConstraints gbc_timeBtwnGenLabel = new GridBagConstraints();
		gbc_timeBtwnGenLabel.anchor = GridBagConstraints.WEST;
		gbc_timeBtwnGenLabel.weightx = 0.1;
		gbc_timeBtwnGenLabel.insets = defaultInsets;
		gbc_timeBtwnGenLabel.gridx = 0;
		gbc_timeBtwnGenLabel.gridy = 2;
		runControlPanel.add(timeBtwnGenLabel, gbc_timeBtwnGenLabel);
		
		_timeBtwnGenTextField = new JTextField();
		_timeBtwnGenTextField.setToolTipText("The time it takes to advance from the current generation to the next.");
		GridBagConstraints gbc_timeBtwnGenTextField = new GridBagConstraints();
		gbc_timeBtwnGenTextField.weightx = 1.0;
		gbc_timeBtwnGenTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_timeBtwnGenTextField.anchor = GridBagConstraints.WEST;
		gbc_timeBtwnGenTextField.gridx = 1;
		gbc_timeBtwnGenTextField.gridy = 2;
		gbc_timeBtwnGenTextField.insets = new Insets(0, 0, 5, 5);
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
		gbc_runControlButtonsPanel.gridy = 3;
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
		parametersPanel.setLayout(gbl_parametersPanel);
		
		JLabel parametersPanelTitleLabel = new JLabel("Parameters");
		parametersPanelTitleLabel.setFont(titleFont);
		GridBagConstraints gbc_parametersPanelTitleLabel = new GridBagConstraints();
		gbc_parametersPanelTitleLabel.weighty = 0.2;
		gbc_parametersPanelTitleLabel.weightx = 0.1;
		gbc_parametersPanelTitleLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_parametersPanelTitleLabel.insets = defaultInsets;
		gbc_parametersPanelTitleLabel.gridx = 0;
		gbc_parametersPanelTitleLabel.gridy = 0;
		parametersPanel.add(parametersPanelTitleLabel, gbc_parametersPanelTitleLabel);
		
		_crossOverEnabledCheckbox = new JCheckBox("");
		_crossOverEnabledCheckbox.setSelected(true);
		_crossOverEnabledCheckbox.setToolTipText("Whether crossover should be applied when creating offspring.");
		GridBagConstraints gbc_crossOverEnabledCheckbox = new GridBagConstraints();
		gbc_crossOverEnabledCheckbox.weightx = 1.0;
		gbc_crossOverEnabledCheckbox.anchor = GridBagConstraints.NORTHWEST;
		gbc_crossOverEnabledCheckbox.weighty = 1.0;
		gbc_crossOverEnabledCheckbox.insets = defaultInsets;
		gbc_crossOverEnabledCheckbox.gridx = 1;
		gbc_crossOverEnabledCheckbox.gridy = 2;
		parametersPanel.add(_crossOverEnabledCheckbox, gbc_crossOverEnabledCheckbox);
		
		JLabel crossOverEnabledLabel = new JLabel("Crossover Enabled:");
		GridBagConstraints gbc_crossOverEnabledLabel = new GridBagConstraints();
		gbc_crossOverEnabledLabel.weighty = 1.0;
		gbc_crossOverEnabledLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_crossOverEnabledLabel.insets = defaultInsets;
		gbc_crossOverEnabledLabel.gridx = 0;
		gbc_crossOverEnabledLabel.gridy = 2;
		parametersPanel.add(crossOverEnabledLabel, gbc_crossOverEnabledLabel);
		
		JLabel preferencesLabel = new JLabel("Preference List Generation:");
		preferencesLabel.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_preferencesLabel = new GridBagConstraints();
		gbc_preferencesLabel.weightx = 0.1;
		gbc_preferencesLabel.insets = new Insets(0, 5, 0, 5);
		gbc_preferencesLabel.anchor = GridBagConstraints.NORTHWEST;
		gbc_preferencesLabel.gridx = 0;
		gbc_preferencesLabel.gridy = 1;
		parametersPanel.add(preferencesLabel, gbc_preferencesLabel);
		
		_preferenceListGenerationModeComboBox = new JComboBox<String>();
		_preferenceListGenerationModeComboBox.setToolTipText("Choose whether individuals should have constant preference lists or whether they should be regenerated randomly each generation.");
		_preferenceListGenerationModeComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"One-Time Generation", "Re-generate Each Generation"}));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.anchor = GridBagConstraints.NORTHEAST;
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.weighty = 0.2;
		gbc_comboBox.insets = new Insets(0, 9, 5, 5);
		gbc_comboBox.weightx = 1.0;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		parametersPanel.add(_preferenceListGenerationModeComboBox, gbc_comboBox);
	}

	/**
	 * Clears all text in the results section.
	 */
	public void clearResultsSection() 
	{
		_resultsTextArea.setText("");
	}
	
	/**
	 * Updates the graphical display of the EA based three-way matching simulation program with
	 * the input fitness values, current generation number and number of each type of individual.
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
	public void updateView(boolean isFirstUpdate,
						   int bestFitness, int averageFitness, int worstFitness,
						   double elapsedTime, int currentRunNumber, int currentGenNumber,
						   int numOfMen, int numOfWomen, int numOfDogs) 
	{		
		if (isFirstUpdate == false) // Not first update, print out fitness values by generation.
			printFitnessValues(bestFitness, averageFitness, worstFitness,
							   currentRunNumber, currentGenNumber);
		_currGenNumberLabel.setText(String.valueOf(currentGenNumber));
		_numOfMenLabel.setText(String.valueOf(numOfMen));
		_numOfWomenLabel.setText(String.valueOf(numOfWomen));
		_numOfDogsLabel.setText(String.valueOf(numOfDogs));
		_currRunNumLabel.setText(String.valueOf(currentRunNumber));
		_elapsedTimeLabel.setText(String.valueOf(elapsedTime));
	}
	
	/**
	 * Prints the best, average and worst fitness values for the current generation in a formatted
	 * form.
	 * @param bestFitness The best fitness value in the current generation.
	 * @param averageFitness The average fitness value in the current generation.
	 * @param worstFitness The worst fitness value in the current generation.
	 * @param currentRunNumber The number of the current run.
	 * @param currentGenNumber The serial number of the current generation (starts from 0).
	 */
	private void printFitnessValues(int bestFitness, int averageFitness, int worstFitness,
									int currentRunNumber, int currentGenNumber)
	{
		_resultsTextArea.append(String
								.format(
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

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		JButton button = null;
		EASimulationController.PreferenceListGenerationMode preferenceListGenerationMode = getPreferenceListGenerationMode();
		boolean isCrossoverEnabled = getCrossoverEnabledState();
		int numOfRuns = getNumOfRuns();
		int timeBetweenGenerations = getTimeBetweenGenerations();
		if (source instanceof JButton)
		{
			button = (JButton) source;
			if (button.getText() == "Start") // Start a new sequence of runs from scratch.
				_controller.startRun(preferenceListGenerationMode, isCrossoverEnabled, numOfRuns,
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
