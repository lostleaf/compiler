package compiler.regAlloc;

import compiler.temp.Temp;
import compiler.util.Config;

public interface RegAlloc extends Config {
	String map(Temp temp);
}
