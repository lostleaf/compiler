package compiler.temp;

import java.util.ArrayList;
import java.util.List;

public class AddrList implements Addr {
	public List<Addr> addrs = new ArrayList<Addr>();

	public AddrList() {
	}

	public AddrList(List<Addr> l) {
		addrs = l;
	}

	public void add(Addr addr) {
		addrs.add(addr);
	}

}
