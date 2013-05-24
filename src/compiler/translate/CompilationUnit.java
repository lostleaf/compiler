package compiler.translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import compiler.analysis.Analyzer;
import compiler.analysis.BasicBlock;
import compiler.analysis.LiveInterval;
import compiler.quad.Quad;
import compiler.temp.Label;


public class CompilationUnit {
	
	private List<Quad> quads = null;
	private Label label = null;
	private Level level = null;

	public CompilationUnit(List<Quad> quads, Label label, Level level) {
		this.quads = quads;
		this.label = label;
		this.level = level;
	}

	public List<Quad> getQuads() {
		return quads;
	}
	
	public void setQuads(List<Quad> quads) {
		if (!(quads instanceof LinkedList)) {
			quads = new LinkedList<Quad>(quads);
		}
		// keep it a list
		this.quads = quads;
	}
	
	public Label getLabel() {
		return label;
	}
	
	public Level getLevel() {
		return level;
	}

	public void findBranches(Analyzer analyzer) {
		quads = analyzer.findBranches(quads);
	}
	
	private LinkedList<BasicBlock> blocks = null;
	
	public void findBasicBlocks(Analyzer analyzer) {
		blocks = analyzer.getBasicBlocks(quads);
	}
	
	public void findLiveness(Analyzer analyzer) {
		analyzer.findLiveness(blocks);
	}

	public LinkedList<BasicBlock> getBasicBlocks() {
		return blocks;
	}
	
	private ArrayList<LiveInterval> liveIntervals;
	
	public void findLiveIntervals(Analyzer analyzer) {
		liveIntervals = new ArrayList<LiveInterval>(analyzer.findLiveIntervals(quads).values());
		Collections.sort(liveIntervals);	// sort by start point
	}
	
	public ArrayList<LiveInterval> getLiveIntervals() {
		return liveIntervals;
	}
}