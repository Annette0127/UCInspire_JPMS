package edu.usc.softarch.arcade.topics;

import cc.mallet.pipe.*;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import edu.usc.softarch.arcade.config.Config;
import edu.usc.softarch.arcade.util.FileListing;
import edu.usc.softarch.arcade.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author joshua
 */
public class DocTopics {
    ArrayList<DocTopicItem> dtItemList = new ArrayList<DocTopicItem>();
    private Logger logger = Logger.getLogger(DocTopics.class);

    public DocTopics() {
        super();
    }

    public DocTopics(DocTopics docTopics) {
        for (DocTopicItem docTopicItem : docTopics.dtItemList) {
            dtItemList.add(new DocTopicItem(docTopicItem));
        }
    }

    public DocTopics(String srcDir, String artifactsDir, int numTopics, String topicModelFilename, String docTopicsFilename, String topWordsFilename) throws Exception {
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();
        numTopics = 100;
        // Pipes: alphanumeric only, camel case separation, lowercase, tokenize,
        // remove stopwords english, remove stopwords java, stem, map to
        // features
        pipeList.add(new CharSequenceReplace(Pattern.compile("[^A-Za-z]"), " "));
        pipeList.add(new CamelCaseSeparatorPipe());
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(Pattern
                .compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
        pipeList.add(new TokenSequenceRemoveStopwords(new File(
                "stoplists/en.txt"), "UTF-8", false, false, false));

        if (Config.getSelectedLanguage().equals(Config.Language.c)) {
            pipeList.add(new TokenSequenceRemoveStopwords(new File(
                    "res/ckeywords"), "UTF-8", false, false, false));
            pipeList.add(new TokenSequenceRemoveStopwords(new File(
                    "res/cppkeywords"), "UTF-8", false, false, false));
        } else {
            pipeList.add(new TokenSequenceRemoveStopwords(new File(
                    "res/javakeywords"), "UTF-8", false, false, false));
        }
        pipeList.add(new StemmerPipe());
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        String testDir = srcDir;
        logger.debug("Building instances for mallet...");
        for (File file : FileListing.getFileListing(new File(testDir))) {
            logger.debug("Should I add " + file.getName() + " to instances?");
            if (file.isFile() && file.getName().endsWith(".java")) {
                String shortClassName = file.getName().replace(".java", "");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = null;
                String fullClassName = "";
                while ((line = reader.readLine()) != null) {
                    String packageName = FileUtil.findPackageName(line);
                    if (packageName != null) {
                        fullClassName = packageName + "." + shortClassName;
                        logger.debug("\t I've identified the following full class name from analyzing files: " + fullClassName);
                    }
                }
                reader.close();
                logger.debug("I'm going to add this file to instances: " + file);
                String data = FileUtil.readFile(file.getAbsolutePath(),
                        Charset.defaultCharset());
                Instance instance = new Instance(data, "X", fullClassName,
                        file.getAbsolutePath());
                instances.addThruPipe(instance);
            }
            Pattern p = Pattern.compile("\\.(c|cpp|cc|s|h|hpp|icc|ia|tbl|p)$");
            // if we found a c or c++ file
            if (file.isFile() && p.matcher(file.getName()).find()) {
                logger.debug("I'm going to add this file to instances: " + file);
                String depsStyleFilename = file.getAbsolutePath().replace(testDir, "");
                String data = FileUtil.readFile(file.getAbsolutePath(),
                        Charset.defaultCharset());
                Instance instance = new Instance(data, "X", depsStyleFilename,
                        file.getAbsolutePath());
                instances.addThruPipe(instance);
            }
        }

//
//		InstanceList previousInstances;
//		File preIns = new File("tmp/int.data");
//		if (preIns.exists()){
//			previousInstances = InstanceList.load(preIns);
//		}else
//		{
//			previousInstances = instances;
//		}
//		//save for next time
//		instances.save(new File("tmp/int.data"));
        InstanceList previousInstances = InstanceList.load(new File(artifactsDir + "/output.pipe"));

        /*
         * Reader fileReader = new InputStreamReader(new FileInputStream(new
         * File( args[0])), "UTF-8"); instances.addThruPipe(new
         * CsvIterator(fileReader, Pattern
         * .compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"), 3, 2, 1)); // data, //
         * label, // name // fields
         */
        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        // Note that the first parameter is passed as the sum over topics, while
        // the second is
        //int numTopics = 40;
        double alpha = (double) 50 / (double) numTopics;
        double beta = .01;
        ParallelTopicModel model = null;
        File topicModelFile = new File(topicModelFilename);
        File docTopicsFile = new File(docTopicsFilename);
        System.out.println(docTopicsFile);
//		System.out.println(docTopicsFile.canRead());

        File topWordsFile = new File(topWordsFilename);

        TopicInferencer inferencer =
                TopicInferencer.read(new File(artifactsDir + "/infer.mallet"));

//		if (topicModelFile.exists()) {
//			try {
//				model = ParallelTopicModel.read(topicModelFile);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			model = new ParallelTopicModel(numTopics, alpha, beta);
//			model.addInstances(instances);
//
//			// Use two parallel samplers, which each look at one half the corpus and
//			// combine
//			// statistics after every iteration.
//			model.setNumThreads(2);
//
//			// Run the model for 50 iterations and stop (this is for testing only,
//			// for real applications, use 1000 to 2000 iterations)
//			int numIterations = 1000;
//			model.setNumIterations(numIterations);
//			model.setRandomSeed(10);
//			model.estimate();
//			model.write(topicModelFile);
//			model.printDocumentTopics(docTopicsFile);
//			boolean useNewLines = false;
//			int numTopWords = 20;
//			model.printTopWords(topWordsFile, numTopWords, useNewLines);
//		}

        for (int instIndex = 0; instIndex < previousInstances.size(); instIndex++) {
            DocTopicItem dtItem = new DocTopicItem();
            dtItem.doc = instIndex;
            dtItem.source = (String) previousInstances.get(instIndex).getName();

            dtItem.topics = new ArrayList<TopicItem>();

//			double[] topicDistribution = model.getTopicProbabilities(instIndex);
            double[] topicDistribution = inferencer.getSampledDistribution(previousInstances.get(instIndex), 1000, 10, 10);
            for (int topicIdx = 0; topicIdx < numTopics; topicIdx++) {
                TopicItem t = new TopicItem();
                t.topicNum = topicIdx;
                t.proportion = topicDistribution[topicIdx];
                dtItem.topics.add(t);
            }
            dtItemList.add(dtItem);
        }
        System.out.println("Length of dtItemList:"+dtItemList.size());
    }

    /**
     * @param filename
     * @throws FileNotFoundException
     */
    public DocTopics(String filename) throws FileNotFoundException {
        loadFromFile(filename);
    }

    public DocTopicItem getDocTopicItemForJava(String name) {
        for (DocTopicItem dti : dtItemList) {
            String altName = name.replaceAll("/", ".").replaceAll(".java", "").trim();
//            System.out.println("name:" + name + "  altName:" + altName);
//            System.out.println("dti:"+dti);
            if (dti.source.endsWith(name)) {
                return dti;
            } else if (altName.equals(dti.source.trim())) {
                return dti;
            }
        }
        return null;
    }

    public DocTopicItem getDocTopicItemForC(String name) {
        for (DocTopicItem dti : dtItemList) {
            String strippedSource = null;
            String nameWithoutQuotations = null;
            if (dti.source.endsWith(".func")) {
                strippedSource = dti.source.substring(dti.source.lastIndexOf('/') + 1, dti.source.lastIndexOf(".func"));
                nameWithoutQuotations = name.replaceAll("\"", "");
                if (strippedSource.contains(nameWithoutQuotations)) {
                    return dti;
                }
            } else if (dti.source.endsWith(".c") || dti.source.endsWith(".h")
                    || dti.source.endsWith(".tbl") || dti.source.endsWith(".p")
                    || dti.source.endsWith(".cpp") || dti.source.endsWith(".s")
                    || dti.source.endsWith(".hpp")
                    || dti.source.endsWith(".icc")
                    || dti.source.endsWith(".ia")) {
                //strippedSource = dti.source.substring(dti.source.lastIndexOf('/')+1,dti.source.length());
                nameWithoutQuotations = name.replaceAll("\"", "");
                if (dti.source.endsWith(nameWithoutQuotations)) {
                    return dti;
                }
            } else if (dti.source.endsWith(".S")) {
                String dtiSourceRenamed = dti.source.replace(".S", ".c");
                nameWithoutQuotations = name.replaceAll("\"", "");
                if (dtiSourceRenamed.endsWith(nameWithoutQuotations)) {
                    return dti;
                }
            } else {
                //System.err.println("Unknown file type for " + dti.source);
                //System.exit(1);
                //return dti;
                continue;
            }

        }
        logger.error("Cannot find doc topic for: " + name);
        return null;
    }

    public ArrayList<DocTopicItem> getDocTopicItemList() {
        return dtItemList;
    }

    public void loadFromFile(String filename) throws FileNotFoundException {
        logger.debug("Loading DocTopics from file...");
        boolean localDebug = false;
        File f = new File(filename);

        Scanner s = new Scanner(f);

        dtItemList = new ArrayList<DocTopicItem>();

        while (s.hasNext()) {
            String line = s.nextLine();
            if (line.startsWith("#")) {
                continue;
            }
            String[] items = line.split("\\s+");

            DocTopicItem dtItem = new DocTopicItem();
            dtItem.doc = (new Integer(items[0])).intValue();
            dtItem.source = items[1];

            dtItem.topics = new ArrayList<TopicItem>();

            TopicItem t = new TopicItem();
            for (int i = 2; i < items.length; i++) {
                if (i % 2 == 0) {
                    t.topicNum = (new Integer(items[i])).intValue();
                } else {
                    t.proportion = (new Double(items[i])).doubleValue();
                    dtItem.topics.add(t);
                    t = new TopicItem();
                }
            }
            dtItemList.add(dtItem);
            if (localDebug)
                logger.debug(line);

        }

        if (localDebug)
            logger.debug("\n");
        for (DocTopicItem dtItem : dtItemList) {
            if (localDebug)
                logger.debug(dtItem);
        }

    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

//package edu.usc.softarch.arcade.topics;
//
//import cc.mallet.pipe.CharSequence2TokenSequence;
//import cc.mallet.pipe.CharSequenceLowercase;
//import cc.mallet.pipe.CharSequenceReplace;
//import cc.mallet.pipe.Pipe;
//import cc.mallet.pipe.SerialPipes;
//import cc.mallet.pipe.TokenSequence2FeatureSequence;
//import cc.mallet.pipe.TokenSequenceRemoveStopwords;
//import cc.mallet.topics.ParallelTopicModel;
//import cc.mallet.topics.TopicInferencer;
//import cc.mallet.types.Instance;
//import cc.mallet.types.InstanceList;
//import edu.usc.softarch.arcade.config.Config;
//import edu.usc.softarch.arcade.config.Config.Language;
//import edu.usc.softarch.arcade.util.FileListing;
//import edu.usc.softarch.arcade.util.FileUtil;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.nio.charset.Charset;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Scanner;
//import java.util.regex.Pattern;
//import org.apache.log4j.Logger;
//
//public class DocTopics {
//	ArrayList<DocTopicItem> dtItemList = new ArrayList();
//	private Logger logger = Logger.getLogger(DocTopics.class);
//
//	public DocTopics() {
//	}
//
//	public DocTopics(DocTopics docTopics) {
//		Iterator var3 = docTopics.dtItemList.iterator();
//
//		while(var3.hasNext()) {
//			DocTopicItem docTopicItem = (DocTopicItem)var3.next();
//			this.dtItemList.add(new DocTopicItem(docTopicItem));
//		}
//
//	}
//
//	public DocTopics(String srcDir, String artifactsDir, int numTopics, String revision) throws Exception {
//		ArrayList<Pipe> pipeList = new ArrayList();
//		pipeList.add(new CharSequenceReplace(Pattern.compile("[^A-Za-z]"), " "));
//		pipeList.add(new CamelCaseSeparatorPipe());
//		pipeList.add(new CharSequenceLowercase());
//		pipeList.add(new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")));
//		pipeList.add(new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false));
//		if (Config.getSelectedLanguage().equals(Language.c)) {
//			pipeList.add(new TokenSequenceRemoveStopwords(new File("res/ckeywords"), "UTF-8", false, false, false));
//			pipeList.add(new TokenSequenceRemoveStopwords(new File("res/cppkeywords"), "UTF-8", false, false, false));
//		} else {
//			pipeList.add(new TokenSequenceRemoveStopwords(new File("res/javakeywords"), "UTF-8", false, false, false));
//		}
//
//		pipeList.add(new StemmerPipe());
//		pipeList.add(new TokenSequence2FeatureSequence());
//		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
//		String testDir = srcDir;
//		this.logger.debug("Building instances for mallet...");
//		Iterator var9 = FileListing.getFileListing(new File(srcDir)).iterator();
//
//		while(true) {
//			File file;
//			String line;
//			while(true) {
//				String shortClassName;
//				if (!var9.hasNext()) {
//					ParallelTopicModel ptm = new ParallelTopicModel(numTopics);
//					ptm.addInstances(instances);
//					ptm.setRandomSeed(1);
//					ptm.setNumThreads(Runtime.getRuntime().availableProcessors());
//					ptm.estimate();
//					String tmFileName = artifactsDir + File.separatorChar + revision + "_topicmodel.tmf";
//					shortClassName = "Writing topic model file " + tmFileName;
//					this.logger.debug(shortClassName);
//					System.out.println(shortClassName);
//					ptm.write(new File(tmFileName));
//					TopicInferencer inferencer = ptm.getInferencer();
//
//					for(int instIndex = 0; instIndex < instances.size(); ++instIndex) {
//						DocTopicItem dtItem = new DocTopicItem();
//						dtItem.doc = instIndex;
//						dtItem.source = (String)((Instance)instances.get(instIndex)).getName();
//						dtItem.topics = new ArrayList();
//						double[] topicDistribution = inferencer.getSampledDistribution((Instance)instances.get(instIndex), 1000, 10, 10);
//
//						for(int topicIdx = 0; topicIdx < numTopics; ++topicIdx) {
//							TopicItem t = new TopicItem();
//							t.topicNum = topicIdx;
//							t.proportion = topicDistribution[topicIdx];
//							dtItem.topics.add(t);
//						}
//
//						this.dtItemList.add(dtItem);
//					}
//
//					return;
//				}
//
//				file = (File)var9.next();
//				this.logger.debug("Should I add " + file.getName() + " to instances?");
//				if (!file.isFile() || !file.getName().endsWith(".java")) {
//					break;
//				}
//
//				shortClassName = file.getName().replace(".java", "");
//				BufferedReader reader = new BufferedReader(new FileReader(file));
//				line = null;
//				String fullClassName = "";
//
//				String data;
//				while((line = reader.readLine()) != null) {
//					data = FileUtil.findPackageName(line);
//					if (data != null) {
//						fullClassName = data + "." + shortClassName;
//						this.logger.debug("\t I've identified the following full class name from analyzing files: " + fullClassName);
//						break;
//					}
//				}
//
//				reader.close();
//				if (!fullClassName.equals("")) {
//					this.logger.debug("I'm going to add this file to instances: " + file);
//					data = FileUtil.readFile(file.getAbsolutePath(), Charset.defaultCharset());
//					Instance instance = new Instance(data, "X", fullClassName, file.getAbsolutePath());
//					instances.addThruPipe(instance);
//					break;
//				}
//			}
//
//			Pattern p = Pattern.compile("\\.(c|cpp|cc|s|h|hpp|icc|ia|tbl|p)$");
//			if (p.matcher(file.getName()).find()) {
//				this.logger.debug("I'm going to add this file to instances: " + file);
//				String depsStyleFilename = file.getAbsolutePath().replace(testDir, "");
//				line = FileUtil.readFile(file.getAbsolutePath(), Charset.defaultCharset());
//				Instance instance = new Instance(line, "X", depsStyleFilename, file.getAbsolutePath());
//				instances.addThruPipe(instance);
//			}
//		}
//	}
//
//	public DocTopics(String filename) throws FileNotFoundException {
//		this.loadFromFile(filename);
//	}
//
//	public DocTopicItem getDocTopicItemForJava(String name) {
//		Iterator var3 = this.dtItemList.iterator();
//
//		while(var3.hasNext()) {
//			DocTopicItem dti = (DocTopicItem)var3.next();
//			String altName = name.replaceAll("/", ".").replaceAll(".java", "").trim();
//			if (dti.source.endsWith(name)) {
//				return dti;
//			}
//
//			if (altName.equals(dti.source.trim())) {
//				return dti;
//			}
//		}
//
//		return null;
//	}
//
//	public DocTopicItem getDocTopicItemForC(String name) {
//		Iterator var3 = this.dtItemList.iterator();
//
//		DocTopicItem dti;
//		String strippedSource;
//		String nameWithoutQuotations;
//		label45:
//		do {
//			while(var3.hasNext()) {
//				dti = (DocTopicItem)var3.next();
//				strippedSource = null;
//				nameWithoutQuotations = null;
//				if (dti.source.endsWith(".func")) {
//					strippedSource = dti.source.substring(dti.source.lastIndexOf(47) + 1, dti.source.lastIndexOf(".func"));
//					nameWithoutQuotations = name.replaceAll("\"", "");
//					continue label45;
//				}
//
//				if (!dti.source.endsWith(".c") && !dti.source.endsWith(".h") && !dti.source.endsWith(".tbl") && !dti.source.endsWith(".p") && !dti.source.endsWith(".cpp") && !dti.source.endsWith(".s") && !dti.source.endsWith(".hpp") && !dti.source.endsWith(".icc") && !dti.source.endsWith(".ia")) {
//					if (dti.source.endsWith(".S")) {
//						String dtiSourceRenamed = dti.source.replace(".S", ".c");
//						nameWithoutQuotations = name.replaceAll("\"", "");
//						if (dtiSourceRenamed.endsWith(nameWithoutQuotations)) {
//							return dti;
//						}
//					}
//				} else {
//					nameWithoutQuotations = name.replaceAll("\"", "");
//					if (dti.source.endsWith(nameWithoutQuotations)) {
//						return dti;
//					}
//				}
//			}
//
//			this.logger.error("Cannot find doc topic for: " + name);
//			return null;
//		} while(!strippedSource.contains(nameWithoutQuotations));
//
//		return dti;
//	}
//
//	public ArrayList<DocTopicItem> getDocTopicItemList() {
//		return this.dtItemList;
//	}
//
//	public void loadFromFile(String filename) throws FileNotFoundException {
//		this.logger.debug("Loading DocTopics from file...");
//		boolean localDebug = false;
//		File f = new File(filename);
//		Scanner s = new Scanner(f);
//		this.dtItemList = new ArrayList();
//
//		while(true) {
//			String line;
//			do {
//				if (!s.hasNext()) {
//					if (localDebug) {
//						this.logger.debug("\n");
//					}
//
//					Iterator var11 = this.dtItemList.iterator();
//
//					while(var11.hasNext()) {
//						DocTopicItem dtItem = (DocTopicItem)var11.next();
//						if (localDebug) {
//							this.logger.debug(dtItem);
//						}
//					}
//
//					return;
//				}
//
//				line = s.nextLine();
//			} while(line.startsWith("#"));
//
//			String[] items = line.split("\\s+");
//			DocTopicItem dtItem = new DocTopicItem();
//			dtItem.doc = new Integer(items[0]);
//			dtItem.source = items[1];
//			dtItem.topics = new ArrayList();
//			TopicItem t = new TopicItem();
//
//			for(int i = 2; i < items.length; ++i) {
//				if (i % 2 == 0) {
//					t.topicNum = new Integer(items[i]);
//				} else {
//					t.proportion = new Double(items[i]);
//					dtItem.topics.add(t);
//					t = new TopicItem();
//				}
//			}
//
//			this.dtItemList.add(dtItem);
//			if (localDebug) {
//				this.logger.debug(line);
//			}
//		}
//	}
//}
