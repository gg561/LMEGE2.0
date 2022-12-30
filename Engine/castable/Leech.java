package castable;

/**
 * 
 * A leech has no actual properties. It takes on whatever properties the leeched class has and simply adds certain other manoeuvers to them.
 * A leech's properties derive from abusing referenced properties (Property sharing), making it susceptible to mismanagement and shadowed actions.
 * 
 * @author huangyoulin
 *
 */

public interface Leech {
	
	public static void leechWarning() {
		throw new RuntimeException("This method cannot be called here : This is a leech object, please refer instead to its host. This Proprty has ben made Immutable due to Leeching.");
	}

}
