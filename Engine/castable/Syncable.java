package castable;

import actors.Existable;
import actors.Movable;

public interface Syncable extends Existable {
	
	public void syncWith(Movable movable);

}
