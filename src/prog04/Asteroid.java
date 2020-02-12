package prog04;

//import processing.core.PApplet;
import processing.core.PGraphics;
//import processing.core.PImage;

/** Graphic class to create an asteroid
 * 
 * @author Paige Courtemanche
 *
 */
public class Asteroid extends GraphicObject implements ApplicationConstants, AnimatedObject {
	
	public static final int WHOLE_ASTEROID = 0;
	public static final int NUM_OF_PARTS = 0;
	public static final int []PART_COLOR =  {	
												0xFFFF0000,	//	WHOLE ASTEROID
											};

	public static final float ASTEROID_DIAMETER = (10 - 4) * (float) Math.random() + 4;
	
	/** Constructor
	 * 
	 */
	public Asteroid() {

		//NEW
		//	Our random initialization is now taken care of in the parent class's constructor.
		super();
		
		//	Create the relative bounding boxes
		relativeBox_ = new BoundingBox[NUM_OF_PARTS+1];
		
		relativeBox_[WHOLE_ASTEROID] = new BoundingBox( -ASTEROID_DIAMETER/2, 	//	xmin
														ASTEROID_DIAMETER/2,	//	xmax
														-ASTEROID_DIAMETER/2, 	//	ymin
														ASTEROID_DIAMETER/2,	//	ymax
														PART_COLOR[WHOLE_ASTEROID]
														);
		
		//	create the absolute boxes
		absoluteBox_ = new BoundingBox[NUM_OF_PARTS+1];
		
		// Here the boxes are created, and then "update" their state.
		for (int k=0; k<= NUM_OF_PARTS; k++)
		{
			absoluteBox_[k] = new BoundingBox(PART_COLOR[k]);
		}
		updateAbsoluteBoxes_();
	}

	
	/** renders the Asteroid object
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode)
	{
		//NEW
		//	Invokes method declared in the parent class, that draws the object
		draw_(g);
		
		//	Then draw the boxes, if needed

		if (boxMode == BoundingBoxMode.RELATIVE_BOX)
		{
			// we use this object's instance variable to access the application's instance methods and variables
			g.pushMatrix();

			g.translate(x_,  y_);
			g.rotate(angle_);		

			for (BoundingBox box : relativeBox_)
				box.draw(g);
			
			g.popMatrix();	
		}
		
		else if (boxMode == BoundingBoxMode.ABSOLUTE_BOX)
		{
			for (BoundingBox box : absoluteBox_)
				if (box != null)
					box.draw(g);
			
		}
	}

	/** draws all the quadrants
	 * 
	 * @param g				reference to the sketch
	 * @param boxMode	    reference to the box drawn around the object
	 */
	public void drawAllQuadrants(PGraphics g, BoundingBoxMode boxMode)
	{
		draw(g, boxMode);
		
		if (shouldGetDrawnInQuadrant_[NORTH_WEST]) {
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[NORTH]) {
			g.pushMatrix();
			g.translate(0, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[NORTH_EAST]) {
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMIN-YMAX);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[EAST]) {
			g.pushMatrix();
			g.translate(XMAX-XMIN, 0);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[SOUTH_EAST]) {
			g.pushMatrix();
			g.translate(XMAX-XMIN, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[SOUTH]) {
			g.pushMatrix();
			g.translate(0, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[SOUTH_WEST]) {
			g.pushMatrix();
			g.translate(XMIN-XMAX, YMAX-YMIN);
			draw(g, boxMode);
			g.popMatrix();
		}
		
		if (shouldGetDrawnInQuadrant_[WEST]) {
			g.pushMatrix();
			g.translate(XMIN-XMAX, 0);
			draw(g, boxMode);
			g.popMatrix();
		}
	}

	/**	renders the Asteroid object
	 * 
	 * @param app		reference to the sketch
	 * @param theMode	should the object be drawn with a bounding box?
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode, int quad) {
		if (quad >= NORTH && quad <= NORTH_EAST && shouldGetDrawnInQuadrant_[quad]) {
			//	Invokes method declared in the parent class, that draws the object
			draw_(g);

			//	Then draw the boxes, if needed

			if (boxMode == BoundingBoxMode.RELATIVE_BOX) {
				// we use this object's instance variable to access the application's instance methods and variables
				g.pushMatrix();

				g.translate(x_,  y_);
				g.rotate(angle_);		

				for (BoundingBox box : relativeBox_)
					box.draw(g);

				g.popMatrix();	
			}

			else if (boxMode == BoundingBoxMode.ABSOLUTE_BOX) {
				for (BoundingBox box : absoluteBox_)
					if (box != null)
						box.draw(g);
			}
		}
	}

	/**	renders the Asteroid object. At this point, we are already in the object's 
	 * reference frame.
	 * 
	 * @param g		reference to the sketch
	 */
	public void draw_(PGraphics g)
	{
		// we use this object's instance variable to access the application's instance methods and variables
		g.pushMatrix();

		g.noStroke();
		g.fill(color_);
		g.ellipse(0, 0, ASTEROID_DIAMETER, ASTEROID_DIAMETER);
		
//		if(ASTEROID_DIAMETER < 3) {
//			g.image(smallAsteroid, 0, 0, ASTEROID_DIAMETER, ASTEROID_DIAMETER);
//		} else if (3 <= ASTEROID_DIAMETER < 7) {
//			g.image(mediumAsteroid, 0, 0, ASTEROID_DIAMETER, ASTEROID_DIAMETER);
//		} else if (7 <= ASTEROID_DIAMETER) {
//			g.image(largeAsteroid, 0, 0, ASTEROID_DIAMETER, ASTEROID_DIAMETER);
//		}
//		
		g.popMatrix();	
	}

	/**
	 * 	Computes the new dimensions of the object's absolute bounding boxes, for
	 * the object's current position and orientation.
	 */
	protected void updateAbsoluteBoxes_()
	{
		//float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
		
		absoluteBox_[WHOLE_ASTEROID].updatePosition(x_ - ASTEROID_DIAMETER/2,		// xmin
													x_ + ASTEROID_DIAMETER/2,		//	xmax
													y_ - ASTEROID_DIAMETER/2,		//	ymin
													y_ + ASTEROID_DIAMETER/2);	//	ymax
	}
	
	/**	Performs a hierarchical search to determine whether the point received
	 * as parameter is inside the face.  OK, it's a pretty simply hierarchy, being
	 * only one level deep, but it gives us a chance to think about the problem.
	 * 
	 * @param x		x coordinate of a point in the world reference frame
	 * @param y		y coordinate of a point in the world reference frame
	 * @return	true if the point at (x, y) lies inside this face object.
	 */
	public boolean isInside(float x, float y)
	{
		boolean inside = false;
		//	If the point is inside the global absolute bounding box
		inside = relativeBox_[WHOLE_ASTEROID].isInside(x, y);

		return inside;
	}
}
