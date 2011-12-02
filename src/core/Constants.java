package core;

import java.awt.Color;
import java.awt.Paint;

import ec.util.MersenneTwister;

public class Constants {
	public static final int TURNS_NUMBER = 200;
	public static final int FIELD_SIZE = 32;
	public static final double FOOD_PROBABILITY = 0.05;
	public static final int START_ROW = 0;
	public static final int START_COLUMN = 0;
	public static final Direction START_DIRECTION = Direction.RIGHT;
	public static final int STATES_NUMBER = 10;
	public static final int VISIBLE_CELLS = 8;
	public static final int SIGNIFICANT_INPUTS = 4;
	public static final int GENERATION_SIZE = 50;
	public static final double ELITE_PART = 0.2;
	public static final int FIELDS_IN_GENERATION = 50;
	public static final int ITERATIONS = 1000;
	public static final int SEARCHER_ITERATIONS = 5000;

	public static final double[] MUTATION_PROBABILITIES = { 0.1, 0.3, 0.5, 0.7,
			1. };

	public static final double[] BEST_MUTATION_PROBABILITIES = { 0.5, 1.0, 0.7,
			1.0 };

	public static final MersenneTwister rand = new MersenneTwister(System
			.nanoTime());

	public static final int THREADS = 2;
	public static final int RUNNINGS_PER_THREAD = 5;
	public static final int SEARCHES_PER_THREAD = 1;

	public static final Paint[] DEFAULT_COLORS = { Color.RED, Color.GREEN,
			Color.BLUE, Color.BLACK, new Color(250, 127, 50) /* bronze */,
			Color.ORANGE, Color.YELLOW, Color.CYAN, Color.GRAY, Color.MAGENTA };
	public static final double GRAPH_WIDTH = 1.5;

	public static final String RESULTS_DIR = "results";
	public static final String AUTO_FILENAME = "auto";
	public static final String GENERATIONS_FILENAME = "generations";
	public static final String BEST_AUTO_DIR = "bestauto";
	public static final String FOOD_FILENAME = "aphid.jpg";
	public static final String SCREENSHOT_FILENAME = "graph.png";
	public static final int SCREENSHOT_WIDTH = 1280;
	public static final int SCREENSHOT_HEIGHT = 1024;

	public static final int FIELD_CELL_SIZE = 20;
	public static final int AUTOMATA_TABLE_HEIGHT = 25;
	public static final int[] AUTOMATA_TABLE_WIDTH = { 100, 250, 200 };
	public static final int CENTERED_BUTTON_STRUT = 2;
	public static final int TRANSITION_TABLE_HEIGHT = 25;
	public static final int[] TRANSITION_TABLE_WIDTH = { 100, 230 };
	public static final int DEFAULT_STRUT = 10;

	public static final Color ANT_POSITION_COLOR = Color.CYAN;
	public static final Color SIGNIFICANT_CELL_COLOR = new Color(250, 127, 50); // bronze
	public static final Color INSIGNIFICANT_CELL_COLOR = Color.LIGHT_GRAY;
	public static final Color EMPTY_CELL_COLOR = Color.WHITE;
	public static final Color ACTIVE_STATE_COLOR = Color.CYAN;
	public static final Color INACTIVE_STATE_COLOR = Color.WHITE;
}