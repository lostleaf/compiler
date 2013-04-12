package compiler.absyn;

import java.util.List;

public class Program extends Node {
	public List<Node> node = null;
	public Program(List<Node> n){
		node = n;
	}
}
