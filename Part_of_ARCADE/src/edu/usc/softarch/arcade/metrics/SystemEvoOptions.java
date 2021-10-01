package edu.usc.softarch.arcade.metrics;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class SystemEvoOptions {
	@Parameter
	public List<String> parameters = new ArrayList<String>();
	
	@Parameter(names = "--help", help = true, description = "print this help menu")
	private boolean help;
}
