package edu.usc.softarch.arcade.facts.driver;

import edu.usc.softarch.arcade.facts.ConcernCluster;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConcernClusterRsf {
	private static boolean containsClusterWithName(
			Set<ConcernCluster> clusters, String clusterName) {
		for (ConcernCluster cluster : clusters) {
			if (cluster.getName().equals(clusterName)) {
				return true;
			}
		}
		return false;
	}
	
	public static Set<ConcernCluster> extractConcernClustersFromRsfFile(String rsfFilename) {
		RsfReader.loadRsfDataFromFile(rsfFilename);
		Iterable<List<String>> clusterFacts = RsfReader.filteredRoutineFacts;
		Set<ConcernCluster> clusters = new HashSet<ConcernCluster>();
		for (List<String> fact : clusterFacts) {
			String clusterName = fact.get(1).trim();
			String element = fact.get(2).trim();
			if (containsClusterWithName(clusters,clusterName)) {
				for (ConcernCluster cluster : clusters) {
					if (cluster.getName().equals(clusterName)) {
						cluster.addEntity(element);
					}
				}
			}
			else {
				ConcernCluster newCluster = new ConcernCluster();
				newCluster.setName(clusterName);
				newCluster.addEntity(element);
				clusters.add(newCluster);
			}
		}
		return clusters;
	}

	
}
