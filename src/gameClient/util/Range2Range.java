<<<<<<< HEAD
package gameClient.util;

import api.geo_location;
=======
import geo_location;
>>>>>>> 53f9b34e32401d6a5efb11ad026c12dc35ff5805

/**
 * This class represents a simple world 2 frame conversion (both ways).
 * @author boaz.benmoshe
 *
 */

public class Range2Range {
<<<<<<< HEAD
	private gameClient.util.Range2D _world, _frame;
	
	public Range2Range(gameClient.util.Range2D w, gameClient.util.Range2D f) {
		_world = new gameClient.util.Range2D(w);
		_frame = new gameClient.util.Range2D(f);
=======
	private Range2D _world, _frame;
	
	public Range2Range(Range2D w, Range2D f) {
		_world = new Range2D(w);
		_frame = new Range2D(f);
>>>>>>> 53f9b34e32401d6a5efb11ad026c12dc35ff5805
	}
	public geo_location world2frame(geo_location p) {
		Point3D d = _world.getPortion(p);
		Point3D ans = _frame.fromPortion(d);
		return ans;
	}
	public geo_location frame2world(geo_location p) {
		Point3D d = _frame.getPortion(p);
		Point3D ans = _world.fromPortion(d);
		return ans;
	}
<<<<<<< HEAD
	public gameClient.util.Range2D getWorld() {
=======
	public Range2D getWorld() {
>>>>>>> 53f9b34e32401d6a5efb11ad026c12dc35ff5805
		return _world;
	}
	public Range2D getFrame() {
		return _frame;
	}
}
