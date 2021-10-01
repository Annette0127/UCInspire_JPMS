//package edu.usc.softarch.arcade;

import edu.usc.softarch.arcade.config.Config;
import edu.usc.softarch.arcade.facts.driver.CSourceToDepsBuilder;
import edu.usc.softarch.arcade.facts.driver.JavaSourceToDepsBuilder;
import edu.usc.softarch.arcade.facts.driver.SourceToDepsBuilder;
import edu.usc.softarch.arcade.util.FileUtil;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class Main {


    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure(Config.getLoggingConfigFilename());

        // inputDirName is a directory where each subdirectory contains a revision or version of the system to be analyzed
        String inputDirName = args[0];
        File inputDir = new File(FileUtil.tildeExpandPath(inputDirName));

        // outputDirName is the directory where dependencies rsf files, cluster rsf files, and detected smells ser files are generated
        String outputDirName = args[1];
        File outputDir = new File(FileUtil.tildeExpandPath(outputDirName));

        File[] files = inputDir.listFiles();
        Set<File> fileSet = new TreeSet<File>(Arrays.asList(files));
        for (File vFolder : fileSet) {
            if (vFolder.isDirectory()) {
                single(vFolder, args, outputDir);
            }
        }
    }

    public static void single(File versionFolder, String[] args, File outputDir) throws IOException {
        // the revision number is really just the name of the subdirectory, for hadoop I actually name each subdirectory based on the revision number
        String revisionNumber = versionFolder.getName();

        // classesDir is the directory in each subdirectory of the dir directory that contains the compiled classes of the subdirectory
        String classesDir = args[2];
        String absoluteClassesDir = versionFolder.getAbsolutePath() + File.separatorChar + classesDir;
        File classesDirFile = new File(absoluteClassesDir);
        if (!classesDirFile.exists())
            return;

        // depsRsfFilename is the file name of the dependencies rsf file (one is created per subdirectory of dir)
        String depsRsfFilename = outputDir.getAbsolutePath() + File.separatorChar + revisionNumber + "_deps.rsf";
        String[] builderArgs = {absoluteClassesDir, depsRsfFilename};
        File depsRsfFile = new File(depsRsfFilename);
        if (!depsRsfFile.getParentFile().exists())
            depsRsfFile.getParentFile().mkdirs();

        SourceToDepsBuilder builder = new JavaSourceToDepsBuilder();
        if (args.length == 4) {
            if (args[3].equals("c")) {
                builder = new CSourceToDepsBuilder();
            }
        }

        builder.build(builderArgs);
        if (builder.getEdges().size() == 0) {
            return;
        }
    }
}
