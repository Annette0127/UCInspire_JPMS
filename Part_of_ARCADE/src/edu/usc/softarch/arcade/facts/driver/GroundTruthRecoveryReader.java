package edu.usc.softarch.arcade.facts.driver;

import edu.usc.softarch.arcade.config.Config;
import edu.usc.softarch.arcade.facts.ConcernCluster;
import edu.usc.softarch.arcade.facts.GroundTruthFileParser;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.HashSet;
import java.util.Set;

public class GroundTruthRecoveryReader {

	static Logger logger = Logger.getLogger(GroundTruthRecoveryReader.class);
	private static Set<ConcernCluster> clusters = new HashSet<ConcernCluster>();

	public static Set<ConcernCluster> getClusters() {
		return clusters;
	}

	public static void main(String[] args) {
		Options options = new Options();

		Option help = new Option("help", "print this message");

		Option projFile = OptionBuilder.withArgName("file").hasArg()
				.withDescription("project configuration file")
				.create("projfile");

		options.addOption(help);
		options.addOption(projFile);

		// create the parser
		CommandLineParser parser = new GnuParser();
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);

			if (line.hasOption("projfile")) {
				Config.setProjConfigFilename(line.getOptionValue("projfile"));
			}
			if (line.hasOption("help")) {
				// automatically generate the help statement
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("GroundTruthRecoveryReader", options);
				System.exit(0);
			}
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
		}

		PropertyConfigurator.configure(Config.getLoggingConfigFilename());

		Config.initConfigFromFile(Config.getProjConfigFilename());
		System.out.println("Reading in ground truth file: " + Config.getGroundTruthFile());
		GroundTruthFileParser.parseHadoopStyle(Config.getGroundTruthFile());
		clusters = GroundTruthFileParser.getClusters();
	}

}
