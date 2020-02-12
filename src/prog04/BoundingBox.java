package prog04;

import processing.core.PGraphics;

/** creates the bounding box of an object
 * 
 * @author jvh
 *
 */
public class BoundingBox implements ApplicationConstants {
	private float xmin_, xmax_, ymin_, ymax_;
	private int color_;
	
	/**	Creates a new bounding box at set dimensions and color.
	 * Note: Should verify that xmin ≤ xmax and ymin ≤ ymax
	 * 
	 * @param xmin	left bound of the box
	 * @param xmax	right bound of the box
	 * @param ymin	lower bound of the box
	 * @param ymax	upper bound of the box
	 * @param color	color of the box's contour
	 */
	public BoundingBox(float xmin, float xmax, float ymin, float ymax, int color) {
		xmin_ = xmin;
		xmax_ = xmax;
		ymin_ = ymin;
		ymax_ = ymax;
		color_ = color;
	}

	/**	Creates an empty bounding box with the specified contour color
	 * 
	 * @param color	color of the box's contour
	 */
	public BoundingBox(int color) {
		color_ = color;
	}

	/**	Creates an empty black box
	 * 
	 */
	public BoundingBox() {
		xmin_ = xmax_ = ymin_ = ymax_ = 0.f;
		color_ = 0xFF000000;
	}
	
	/** Updates the position of the box
	 * 
	 * @param xmin   left bound of the box
	 * @param xmax	 right bound of the box
	 * @param ymin 	 lower bound of the box
	 * @param ymax   upper bound of the box
	 */
	public void updatePosition(float xmin, float xmax, float ymin, float ymax) {
		xmin_ = xmin;
		xmax_ = xmax;
		ymin_ = ymin;
		ymax_ = ymax;
	}

	/** gets the left bound of the box
	 * 
	 * @return   the float value of the left bound of the box
	 */
	public float getXmin() {
		return xmin_;
	}
	
	/** gets the right bound of the box
	 * 
	 * @return   the float value of the right bound of the box
	 */
	public float getXmax() {
		return xmax_;
	}
	
	/** gets the lower bound of the box
	 * 
	 * @return   the float value of the lower bound of the box
	 */
	public float getYmin() {
		return ymin_;
	}
	
	/** gets the upper bound of the box
	 * 
	 * @return   the float value of the upper bound of the box
	 */
	public float getYmax() {
		return ymax_;
	}
	
	/**	Performs a hierarchical search to determine whether the point received
	 * as parameter is inside the face.  OK, it's a pretty simply hierarchy, being
	 * only one level deep, but it gives us a chance to think about the problem.
	 * 
	 * @param x		x coordinate of a point in the world reference frame
	 * @param y		y coordinate of a point in the world reference frame
	 * @return	true if the point at (x, y) lies inside this face object.
	 */
	public boolean isInside(float x, float y) {		
		return ((x>=xmin_) && (x<=xmax_) && (y>=ymin_) && (y<=ymax_));
	}
	
	/** Draws the box
	 * 
	 * @param g		method from the parent class that draws the object
	 */
	public void draw(PGraphics g) {
		g.strokeWeight(1.0f*PIXEL_TO_WORLD);
		g.stroke(color_);
		g.noFill();
		g.rect(xmin_, ymin_, xmax_-xmin_, ymax_-ymin_);
	}
}
