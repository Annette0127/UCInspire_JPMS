package edu.usc.softarch.arcade.antipattern.detection;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.common.base.Joiner;

import edu.usc.softarch.arcade.config.Config;
import edu.usc.softarch.arcade.util.FileUtil;

public class BatchSmellEvaluatorRunner {
	
	static Logger logger = Logger.getLogger(BatchSmellEvaluatorRunner.class);
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(Config.getLoggingConfigFilename());
		
		String smellTypeXTechSummaryFilename = args[0];
		
		String[] systems = 
			{
				"ArchStudio",
				"Hadoop",
				"OODT",
				"Bash",
				"Linux-C",
				"Linux-D",
				"Mozilla-C",
				"Mozilla-D",
			};
		
		String[] argsArr = 
		{
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/archstudio/ground_truth/archstudio_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/archstudio/techniques/\" \"/home/joshua/recovery/smell_detection/archstudio/archstudio_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/archstudio/ground_truth/archstudio_gt_smells.ser\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/hadoop/ground_truth/hadoop_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/hadoop/techniques/\" \"/home/joshua/recovery/smell_detection/hadoop/hadoop_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/hadoop/ground_truth/hadoop_gt_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/hadoop/hadoop_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/oodt/ground_truth/oodt_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/oodt/techniques/\" \"/home/joshua/recovery/smell_detection/oodt/oodt_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/oodt/ground_truth/oodt_gt_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/oodt/oodt_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/bash/ground_truth/bash_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/bash/techniques/\" \"/home/joshua/recovery/smell_detection/bash/bash_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/bash/ground_truth/bash_gt_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/bash/bash_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/linux-compact/ground_truth/linux-compact_gt_smells2classes.csv\" \"/home/joshua/recovery/smell_detection/linux-compact/techniques/\" \"/home/joshua/recovery/smell_detection/linux-compact/linux-compact_smellXtech_table.csv\" \"/home/joshua/recovery/smell_detection/linux-compact/ground_truth/linux_groundtruth_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/linux-compact/linux_compact_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/linux-detailed/ground_truth/linux-detailed_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/linux-detailed/techniques/\" \"/home/joshua/recovery/smell_detection/linux-detailed/linux-detailed_smellXtech_table.csv\" \"/home/joshua/recovery/smell_detection/linux-detailed/ground_truth/linux_groundtruth_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/linux-detailed/linux_detailed_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-compact/ground_truth/mozilla-compact_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-compact/techniques/\" \"/home/joshua/recovery/smell_detection/mozilla-compact/mozilla-compact_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-compact/ground_truth/mozilla-compact_gt_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-compact/mozilla-compact_ser_to_mojofm.csv\"",
				"\"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-detailed/ground_truth/mozilla-detailed_gt_smells2classes.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-detailed/techniques/\" \"/home/joshua/recovery/smell_detection/mozilla-detailed/mozilla-detailed_smellXtech_table.csv\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-detailed/ground_truth/mozilla-detailed_gt_smells.ser\" \"/home/joshua/Documents/Software Engineering Research/projects/recovery/smell_detection/mozilla-detailed/mozilla-detailed_ser_to_mojofm.csv\"",
				
		};
		
		String[] techniques = {
				"arc",
				"acdc",
				"bunch nahc",
				"bunch sahc",
				"wca ue",
				"wca uenm",
				"limbo",
				"zem",
				"zuni"
			};
			
		Set<String> techniquesSet = new HashSet<String>(Arrays.asList(techniques));
		
		assert systems.length == argsArr.length;
		
		Set<String> filePrefixes = new HashSet<String>();
		
		// Key: System name, Value: Map of coverage data where keys are smell type and technique pairs
		Map<String,Map<Pair<Class,String>,Double>> coverageMap = new HashMap<String,Map<Pair<Class,String>,Double>>();
		for (int i=0;i<systems.length;i++) {
			String eArgs = argsArr[i];
			String system = systems[i];
			
			List<String> tokensList = new ArrayList<String>();
			Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(eArgs);
			while (m.find())
			    tokensList.add(m.group(1).replace("\"", ""));
			
			String[] tokensArr = new String[tokensList.size()];
			tokensList.toArray(tokensArr);
			System.out.println(Joiner.on("\n").join(tokensArr));
			
			SmellDetectionEvaluator.configureLogging = false; // do not log at class level for SmellDetectionEvaluator
			SmellDetectionEvaluator.main(tokensArr);
			coverageMap.put(system,new HashMap<Pair<Class,String>,Double>(SmellDetectionEvaluator.smellTypeTechRatioMap));
			logger.debug("completed evaluator for system: " + system);
			logger.debug("coverage map contents:");
			String output = "";
			for (String systemInMap : coverageMap.keySet()) {
				output += systemInMap + "\n";
				Map<Pair<Class,String>,Double> ratioMap = coverageMap.get(systemInMap);
				for (Entry<Pair<Class,String>,Double> entry : ratioMap.entrySet()) {
					Pair<Class,String> pair = entry.getKey();
					Double coverageRatio = entry.getValue();
					
					String smellType = pair.getLeft().getSimpleName();
					String filePrefix = FileUtil.extractFilenamePrefix(pair.getRight());
					filePrefixes.add(filePrefix);
					output += "\t" + smellType + "," + filePrefix + "=" + coverageRatio + "\n";
				}
				output += "\n";
			}
			logger.debug(output);
			logger.debug("");
		}
		
		Map<Pair<Class,String>,Double> coverageAvgAcrossSystemsMap = buildCoverMapAcrossSystems(systems, coverageMap, techniquesSet);
		logger.debug("");
		String tableOutput = "";
		tableOutput += ",";
		for (String technique : techniques) {
			tableOutput += technique + ",";
		}
		tableOutput += "\n";
		for (Class smellClass : SmellUtil.getSmellClasses()) {
			tableOutput += smellClass.getSimpleName() + ",";
			for (String technique : techniques) {
				Pair<Class,String> pair = new ImmutablePair<Class,String>(smellClass,technique);
				Double ratio = coverageAvgAcrossSystemsMap.get(pair);
				if (ratio==null) {
					tableOutput += 0 + ",";
				}
				else {
					tableOutput += ratio + ",";
				}
			}
			tableOutput += "\n";
		}
		
		logger.debug("\n" + tableOutput);
		
		PrintWriter writer;
		try {
			writer = new PrintWriter(smellTypeXTechSummaryFilename, "UTF-8");
			
			writer.println(tableOutput);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static Map<Pair<Class,String>,Double> buildCoverMapAcrossSystems(String[] systems,
			Map<String, Map<Pair<Class, String>, Double>> coverageMap, Set<String> techniquesSet) {
		Map<Pair<Class,String>,Double> coverageAvgAcrossSystemsMap = new HashMap<Pair<Class,String>,Double>();
		
		for (String system : systems) {
			Map<Pair<Class,String>,Double> smellTypeTechRatioMap = coverageMap.get(system);
			for (Entry<Pair<Class,String>,Double> entry : smellTypeTechRatioMap.entrySet()) {
				Pair<Class,String> origPair = entry.getKey();
				Class smellType = origPair.getLeft();
				String origFilename = origPair.getRight();
				String origPrefix = FileUtil.extractFilenamePrefix(origFilename);
				String alteredPrefix = origPrefix.replaceAll("[^a-zA-Z]", " ").toLowerCase();
				boolean foundMatchingTechnique = false;
				for (String technique : techniquesSet) {
					if (alteredPrefix.contains(technique)) {
						// wca is a special case that needs to be treated differently from the rest
						if (technique.equals("wca ue")) { 
							if (alteredPrefix.contains("wca uem")) {
								alteredPrefix = "wca ue";
								
							}
							else if (alteredPrefix.contains("wca uenm")) {
								alteredPrefix = "wca uenm";
							}
							else {
								alteredPrefix = "wca ue";
							}
							foundMatchingTechnique = true;
						} else {
							alteredPrefix = technique;
							foundMatchingTechnique = true;
						}
					}
				}
				assert foundMatchingTechnique : "No matching technique for " + alteredPrefix;
				Pair<Class,String> alteredPair = new ImmutablePair<Class,String>(smellType,alteredPrefix);
				Double existingRatio = smellTypeTechRatioMap.get(origPair);
				if (coverageAvgAcrossSystemsMap.containsKey(alteredPair)) {
					Double ratioToAccumulate = coverageAvgAcrossSystemsMap.get(alteredPair);
					ratioToAccumulate += existingRatio;
					coverageAvgAcrossSystemsMap.put(alteredPair, ratioToAccumulate);
				}
				else {
					coverageAvgAcrossSystemsMap.put(alteredPair, existingRatio);
				}
			}
		}
		for (Entry<Pair<Class,String>,Double> entry : coverageAvgAcrossSystemsMap.entrySet()) {
			Pair<Class,String> currPair = entry.getKey();
			Double ratioToNormalize = coverageAvgAcrossSystemsMap.get(currPair);
			ratioToNormalize /= systems.length;
			coverageAvgAcrossSystemsMap.put(currPair, ratioToNormalize);
		}
		return coverageAvgAcrossSystemsMap;
	}

}
