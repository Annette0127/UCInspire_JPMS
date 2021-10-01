//package edu.usc.softarch.arcade.facts.driver;
//
//import edu.usc.softarch.arcade.config.Config;
//import edu.usc.softarch.extractors.cda.edu.usc.softarch.arcade.odem.Container;
//import edu.usc.softarch.extractors.cda.edu.usc.softarch.arcade.odem.Namespace;
//import edu.usc.softarch.extractors.cda.edu.usc.softarch.arcade.odem.ODEM;
//import edu.usc.softarch.extractors.cda.edu.usc.softarch.arcade.odem.Type;
//import org.apache.commons.cli.*;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ODEMReader {
//	private static List<Type> allTypes = new ArrayList<Type>();
//
//	static Logger logger = Logger.getLogger(ODEMReader.class);
//
//	public static void main(String[] args) {
//		Options options = new Options();
//
//		Option help = new Option("help", "print this message");
//
//		Option projFile = OptionBuilder.withArgName("file").hasArg()
//				.withDescription("project configuration file")
//				.create("projfile");
//
//		options.addOption(help);
//		options.addOption(projFile);
//
//		// create the parser
//		CommandLineParser parser = new GnuParser();
//		try {
//			// parse the command line arguments
//			CommandLine line = parser.parse(options, args);
//
//			if (line.hasOption("projfile")) {
//				Config.setProjConfigFilename(line.getOptionValue("projfile"));
//			}
//			if (line.hasOption("help")) {
//				// automatically generate the help statement
//				HelpFormatter formatter = new HelpFormatter();
//				formatter.printHelp("ODEMReader", options);
//				System.exit(0);
//			}
//		} catch (ParseException exp) {
//			// oops, something went wrong
//			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
//		}
//
//
//		PropertyConfigurator.configure(Config.getLoggingConfigFilename());
//
//		Config.initConfigFromFile(Config.getProjConfigFilename());
//
//		System.out.println("Reading in edu.usc.softarch.arcade.odem file " + Config.getOdemFile()  + "...");
//
//		setTypesFromODEMFile(Config.getOdemFile());
//
//
//
//
//		//parseXmlFile(Config.getOdemFile());
//	}
//
//	public static void setTypesFromODEMFile(String odemFile) {
//		JAXBContext context;
//		try {
//			context = JAXBContext.newInstance(ODEM.class);
//			Unmarshaller u = context.createUnmarshaller();
//			ODEM edu.usc.softarch.arcade.odem = (ODEM) u.unmarshal(new File(odemFile));
//			for (Container container : edu.usc.softarch.arcade.odem.getContext().getContainer()) {
//				for (Namespace n : container.getNamespace()) {
//					List<Type> types = n.getType();
//					allTypes.addAll(types);
//				}
//			}
//			int typeCount = 0;
//			for (Type t : allTypes) {
//				logger.debug(typeCount + ": " + t.getName());
//				typeCount++;
//			}
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//
//	public static List<Type> getAllTypes() {
//		return allTypes;
//	}
//
//}
