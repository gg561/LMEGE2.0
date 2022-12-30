package actors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CompoundActor implements Actor {
	
	private List<Actor> actors;
	
	public CompoundActor(Actor...actors) {
		this.actors = new ArrayList<Actor>(actors.length);
		Collections.addAll(this.actors, actors);
	}
	
	public abstract void syncActors();
	
}
