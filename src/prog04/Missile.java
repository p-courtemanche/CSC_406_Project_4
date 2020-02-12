package prog04;

//import processing.core.PApplet;
import processing.core.PGraphics;

/** Graphic class to draw a missile
 * 
 * @author Paige Courtemanche
 *
 */
public class Missile extends GraphicObject implements ApplicationConstants, AnimatedObject {
	public static final int WHOLE_MISSILE = 0;
	public static final int NUM_OF_PARTS = 0;
	public static final int []PART_COLOR =  {	
												0xFFFF0000,};//	WHOLE SILLY FACE
	
	public static final float MISSILE_DIAMETER = 0.2f;

	/** Constructor
	 * 
	 * @param x			x coordinate of the missile's center (in world coordinates)
	 * @param y			y coordinate of the missile's center (in world coordinates)
	 * @param angle_	the angle the missile is facing
	 * @param vx_		the speed in which the missile travels on the x-axis
	 * @param vy_		the speed in which the missile travels on the y-axis
	 */
	public Missile(float x_, float y_, float angle_, float vx_, float vy_) {

		//NEW
		//	Our random initialization is now taken care of in the parent class's constructor.
		super(x_, y_, angle_, 0, 0, 0xFFFFFFFF, vx_, vy_, 0, 0, 0);
		
		//	Create the relative bounding boxes
		relativeBox_ = new BoundingBox[NUM_OF_PARTS+1];
	 	relativeBox_[WHOLE_MISSILE] = new BoundingBox(-x_ * MISSILE_DIAMETER/2,   //	xmin
													   x_ * MISSILE_DIAMETER/2,   //	xmax
												  	  -y_ * MISSILE_DIAMETER/2,   //	ymin
												  	   y_ * MISSILE_DIAMETER/2,   //	ymax
												  	   PART_COLOR[WHOLE_MISSILE]);
		
		//	create the absolute boxes
		absoluteBox_ = new BoundingBox[NUM_OF_PARTS+1];
	
		//	So here we first create the boxes, and then "update" their state.
		for (int k=0; k<= NUM_OF_PARTS; k++)
		{
			absoluteBox_[k] = new BoundingBox(PART_COLOR[k]);
		}
		updateAbsoluteBoxes_();
	}
	
	/**	renders the missile object.  At this point, we are already in the object's 
	 * reference frame.
	 * 
	 *  @param g	The Processing application in which the action takes place
	 */
	public void draw_(PGraphics g) {
		// we use this object's instance variable to access the application's instance methods and variables
		g.pushMatrix();

		g.noStroke();
		g.fill(color_);
		g.ellipse(0, 0, MISSILE_DIAMETER, MISSILE_DIAMETER);

		g.popMatrix();	
	}


	/**	This method isn't utilized but the method is left because the parent class requires it
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode) {
		
	}

	/** This method isn't utilized but the method is left because the parent class requires it
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
	 */
	public void drawAllQuadrants(PGraphics g, BoundingBoxMode boxMode) {
		
	}

	/**	This method isn't utilized but the method is left because the parent class requires it
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode, int quad) {
		
	}

	/** This method isn't utilized but the method is left because the parent class requires it
	 * 
	 * 	Computes the new dimensions of the object's absolute bounding boxes, for
	 * the object's current position and orientation.
	 */
	protected void updateAbsoluteBoxes_() {
		
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
		boolean inside = false;
		return inside;
	}
}