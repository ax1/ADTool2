package ax1.simulators;

import java.util.ArrayList;

import lu.uni.adtool.ADToolMain;
import lu.uni.adtool.domains.rings.Ring;
import lu.uni.adtool.tree.Node;
import lu.uni.adtool.ui.canvas.AbstractDomainCanvas;

public class Simulator {

	public static AbstractDomainCanvas<Ring> canvas;
	public static ADToolMain main;

	public static void simulate() {
		System.out.println("SIMULATION!");
		main.getController().report("SIMULATION!");
		ArrayList<Node> nodes = canvas.getTree().getChildrenList(canvas.getTree().getRoot(true), true);
		canvas.valuesUpdated(false);
	}
}
