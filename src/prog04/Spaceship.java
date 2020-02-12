package prog04;

import processing.core.PApplet;
import processing.core.PGraphics;

/** This is the class that creates the spaceship
 * 
 * @author Paige Courtemanche
 *
 */
public class Spaceship extends GraphicObject implements ApplicationConstants, AnimatedObject {
	public static final int SPACESHIP = 0;
	public static final int LEFT_FIN = 1;
	public static final int RIGHT_FIN = 2;
	public static final int MAIN_SHIP = 3;
	public static final int NUM_OF_PARTS = 3;
	public static final int []PART_COLOR =  {	
												0xFFFF0000,	//	WHOLE SHIP
												0xFF00FF00,	//	LEFT_FIN
												0xFF00FF00,	//	RIGHT_FIN
												0xFF0000FF	//	FACE
											};
	
	public static final float MAIN_SHIP_WIDTH = 2;
	public static final float MAIN_SHIP_HEIGHT = 2.5f*MAIN_SHIP_WIDTH;
	public static final float SHIP_TIP_HEIGHT = MAIN_SHIP_WIDTH/2;
	public static final float SHIP_TIP_WIDTH = MAIN_SHIP_WIDTH;
	public static final float WINDOW_DIAMETER = 0.4f*MAIN_SHIP_WIDTH;
	public static final float OUTER_FIN_HEIGHT = MAIN_SHIP_WIDTH*2;
	public static final float OUTER_FIN_WIDTH = MAIN_SHIP_WIDTH/2;
	public static final float INNER_FIN_HEIGHT = 1.5f*MAIN_SHIP_WIDTH;
	public static final float INNER_FIN_WIDTH = MAIN_SHIP_WIDTH/10;

	public static final float OUTER_FIN_OFFSET = MAIN_SHIP_WIDTH/2;
	
	private static final float SHIP_TIP_X2 = MAIN_SHIP_WIDTH;
	private static final float SHIP_TIP_X3 = MAIN_SHIP_WIDTH/2;
	private static final float SHIP_TIP_Y3 = -MAIN_SHIP_WIDTH/2;
	
	private static final float WINDOW_X = 0;
	private static final float TOP_WINDOW_Y = 0.5f*MAIN_SHIP_WIDTH;
	private static final float BOTTOM_WINDOW_Y = 1.1f*MAIN_SHIP_WIDTH;
	
	private static final float FIN_Y1 = 1.5f*MAIN_SHIP_WIDTH;
	private static final float FIN_Y2 = 3.5f*MAIN_SHIP_WIDTH;
	private static final float FIN_Y3 = MAIN_SHIP_WIDTH*2;
	private static final float RIGHT_FIN_X3 = -OUTER_FIN_OFFSET;
	private static final float LEFT_FIN_X3 = OUTER_FIN_OFFSET;
	
	private static final float LEFT_FIN_X1 = MAIN_SHIP_WIDTH;

	private static final float MIDDLE_FIN_OFFSET_X = 0.45f*MAIN_SHIP_WIDTH;
	private static final float MIDDLE_FIN_OFFSET_Y = 1.7f*MAIN_SHIP_WIDTH;
	private static final float MIDDLE_FIN_HEIGHT = 1.2f*MAIN_SHIP_WIDTH;
	private static final float MIDDLE_FIN_WIDTH = MAIN_SHIP_WIDTH/10;
	
	private static final float LEFT_BOX_X = -3*MAIN_SHIP_WIDTH/4;
	private static final float LEFT_BOX_Y = MAIN_SHIP_WIDTH*2;
	private static final float RIGHT_BOX_X = 3*MAIN_SHIP_WIDTH/4;
	private static final float RIGHT_BOX_Y = MAIN_SHIP_WIDTH*2;
	
	private static final float FIN_WIDTH = MAIN_SHIP_WIDTH/4;
	private static final float FIN_HEIGHT = MAIN_SHIP_WIDTH;
	
	public static final float WHOLE_MAIN_SHIP_Y = MAIN_SHIP_HEIGHT/2 - 1.1f*SHIP_TIP_HEIGHT;
	public static final float WHOLE_MAIN_SHIP_HEIGHT = MAIN_SHIP_HEIGHT + SHIP_TIP_HEIGHT + MIDDLE_FIN_HEIGHT/3;

	/** Constructor
	 * 
	 */
	public Spaceship() {

		//NEW
		//	Our random initialization is now taken care of in the parent class's constructor.
		super();
		
		//	Create the relative bounding boxes
		relativeBox_ = new BoundingBox[NUM_OF_PARTS+1];
		relativeBox_[LEFT_FIN] = new BoundingBox(LEFT_BOX_X - FIN_WIDTH, 	//	xmin
												 LEFT_BOX_X + FIN_WIDTH,	//	xmax
												 LEFT_BOX_Y - FIN_HEIGHT, 	//	ymin
												 LEFT_BOX_Y + FIN_HEIGHT, 	//	ymax
												 PART_COLOR[LEFT_FIN]);
		relativeBox_[RIGHT_FIN] = new BoundingBox(RIGHT_BOX_X - FIN_WIDTH, 	//	xmin
				 								  RIGHT_BOX_X + FIN_WIDTH,	//	xmax
				 								  RIGHT_BOX_Y - FIN_HEIGHT, 	//	ymin
				 							      RIGHT_BOX_Y + FIN_HEIGHT, 	//	ymax
												  PART_COLOR[RIGHT_FIN]);
		relativeBox_[MAIN_SHIP] = new BoundingBox(-MAIN_SHIP_WIDTH/2, 	//	xmin
												  MAIN_SHIP_WIDTH/2,		//	xmax
												  WHOLE_MAIN_SHIP_Y - WHOLE_MAIN_SHIP_HEIGHT/2, 	//	ymin: bottom of middle fin
												  WHOLE_MAIN_SHIP_Y + WHOLE_MAIN_SHIP_HEIGHT/2, 	//	ymax: tip of ship nose
											      PART_COLOR[MAIN_SHIP]);
		relativeBox_[SPACESHIP] = new BoundingBox(LEFT_BOX_X - FIN_WIDTH, 	//	xmin
											      RIGHT_BOX_X + FIN_WIDTH,	//	xmax 
											      WHOLE_MAIN_SHIP_Y - WHOLE_MAIN_SHIP_HEIGHT/2, //	ymin
											      RIGHT_BOX_Y + FIN_HEIGHT, 	//	ymax
												  PART_COLOR[SPACESHIP]);
		
		//	create the absolute boxes
		absoluteBox_ = new BoundingBox[NUM_OF_PARTS+1];
		
		//	So here we first create the boxes, and then "update" their state.
		for (int k=0; k<= NUM_OF_PARTS; k++) {
			absoluteBox_[k] = new BoundingBox(PART_COLOR[k]);
		}
		updateAbsoluteBoxes_();
	}


	/**	renders the Spaceship object
	 * 
	 * @param app		reference to the sketch
	 * @param theMode	should the object be drawn with a bounding box?
	 */
	public void draw(PGraphics g, BoundingBoxMode boxMode) {
		//NEW
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

	/** draws all the quadrants
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
	 */
	public void drawAllQuadrants(PGraphics g, BoundingBoxMode boxMode) {
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

	/**	renders the Spaceship object
	 * 
	 * @param g				The Processing application in which the action takes place
	 * @param boxMode		whichever type of box is drawn around the object
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

	/**	renders the object.  At this point, we are already in the object's 
	 * reference frame.
	 * 
	 * @param g		The Processing application in which the action takes place
	 */
	public void draw_(PGraphics g) {
		// we use this object's instance variable to access the application's instance methods and variables
		g.pushMatrix();

		g.pushMatrix();
		g.noStroke();
		g.fill(color_);
		g.translate(-MAIN_SHIP_WIDTH/2, -MAIN_SHIP_WIDTH/2);
		g.rect(0, 0, MAIN_SHIP_WIDTH, MAIN_SHIP_HEIGHT);
		g.popMatrix();

		//	draw the tip
		g.pushMatrix();
		g.noStroke();
		g.fill(150);
		g.translate(-MAIN_SHIP_WIDTH/2, -MAIN_SHIP_WIDTH/2);
		g.triangle(0, 0, SHIP_TIP_X2, 0, SHIP_TIP_X3, SHIP_TIP_Y3);
		g.popMatrix();  

		//	draw the top window
		g.pushMatrix();
		g.noStroke();
		g.fill(0);
		g.translate(0, -MAIN_SHIP_WIDTH/2);
		g.ellipse(WINDOW_X, TOP_WINDOW_Y, WINDOW_DIAMETER, WINDOW_DIAMETER);
		g.popMatrix();  
		
		//	draw the bottom window
		g.pushMatrix();
		g.noStroke();
		g.fill(0);
		g.translate(0, -MAIN_SHIP_WIDTH/2);
		g.ellipse(WINDOW_X, BOTTOM_WINDOW_Y, WINDOW_DIAMETER, WINDOW_DIAMETER);
		g.popMatrix(); 
		
		// draw the right fin
		g.pushMatrix();
		g.noStroke();
		g.fill(0);
		g.translate(-MAIN_SHIP_WIDTH/2, -MAIN_SHIP_WIDTH/2);
		g.triangle(0, FIN_Y1, 0, FIN_Y2, RIGHT_FIN_X3, FIN_Y3);
		g.popMatrix();
		
		//draw the left fin
		g.pushMatrix();
		g.noStroke();
		g.fill(0);
		g.translate(-MAIN_SHIP_WIDTH/2 + LEFT_FIN_X1, -MAIN_SHIP_WIDTH/2);
		g.triangle(0, FIN_Y1, 0, FIN_Y2, LEFT_FIN_X3, FIN_Y3);
		g.popMatrix();  
		
		//draw the middle fin
		g.pushMatrix();
		g.noStroke();
		g.fill(0);
		g.translate(-MAIN_SHIP_WIDTH/2 + MIDDLE_FIN_OFFSET_X, -MAIN_SHIP_WIDTH/2 + MIDDLE_FIN_OFFSET_Y);
		g.rect(0, 0, MIDDLE_FIN_WIDTH, MIDDLE_FIN_HEIGHT);
		g.popMatrix();
		
		g.popMatrix();	
	}

	/**
	 * 	Computes the new dimensions of the object's absolute bounding boxes, for
	 * the object's current position and orientation.
	 */
	protected void updateAbsoluteBoxes_(){
		float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
		float 	centerLeftFinX = x_ + cA*LEFT_BOX_X - sA*LEFT_BOX_Y,
				centerLeftFinY = y_ + cA*LEFT_BOX_Y + sA*LEFT_BOX_X,
				centerRightFinX = x_ + cA*RIGHT_BOX_X - sA*RIGHT_BOX_Y,
				centerRightFinY = y_ + cA*RIGHT_BOX_Y + sA*RIGHT_BOX_X;
		
		absoluteBox_[LEFT_FIN].updatePosition(centerLeftFinX - LEFT_BOX_X/2,	 //	xmin
											  centerLeftFinX + LEFT_BOX_X/2,	 //	xmax
											  centerLeftFinY - LEFT_BOX_Y/2,	 //	ymin
											  centerLeftFinY + LEFT_BOX_Y/2);	 //	ymax
		absoluteBox_[RIGHT_FIN].updatePosition(centerRightFinX - RIGHT_BOX_X/2,	 //	xmin
											   centerRightFinX + RIGHT_BOX_X/2,	 //	xmax
											   centerRightFinY - RIGHT_BOX_Y/2,	 //	ymin
											   centerRightFinY + RIGHT_BOX_Y/2); //	ymax
		absoluteBox_[MAIN_SHIP].updatePosition(x_ - MAIN_SHIP_WIDTH/2,	// xmin
											   x_ + MAIN_SHIP_WIDTH/2,	//	xmax
											   y_ + WHOLE_MAIN_SHIP_Y - WHOLE_MAIN_SHIP_HEIGHT/2,	//	ymin
											   y_ + WHOLE_MAIN_SHIP_Y + WHOLE_MAIN_SHIP_HEIGHT/2);	//	ymax)

		absoluteBox_[SPACESHIP].updatePosition(PApplet.min(absoluteBox_[LEFT_FIN].getXmin(),
														 absoluteBox_[RIGHT_FIN].getXmin(),
														 absoluteBox_[MAIN_SHIP].getXmin()),	// xmin
												PApplet.max(absoluteBox_[LEFT_FIN].getXmax(),
															absoluteBox_[RIGHT_FIN].getXmax(),
															absoluteBox_[MAIN_SHIP].getXmax()),	// xmax
												PApplet.min(absoluteBox_[LEFT_FIN].getYmin(),
															absoluteBox_[RIGHT_FIN].getYmin(),
															absoluteBox_[MAIN_SHIP].getYmin()),	// ymin
												PApplet.max(absoluteBox_[LEFT_FIN].getYmax(),
															absoluteBox_[RIGHT_FIN].getYmax(),
															absoluteBox_[MAIN_SHIP].getYmax()));	// xmax
	}
	
	/** Spins the ship counter-clockwise
	 * 
	 */
	public void spinLeft() {
		angle_ += angularv_;
	}
	
	/** Spins the ship clockwise
	 * 
	 */
	public void spinRight() {
		angle_ -= angularv_;
	}
	
	/** Increases the velocity of the ship
	 * 
	 */
	public void applyThrust() {
		vx_ += accelv_*PApplet.sin(angle_);
		vy_ += -accelv_*PApplet.cos(angle_);
	}
	
	/** Creates a new missile that starts at the top of the ship's tip
	 * 
	 * @return  returns the newly created missile
	 */
	public Missile missileShoot() {
		float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
		//float v = (float) (angularv_*(MAX_SPEED-MIN_SPEED) + MIN_SPEED);
		
		float aTx = x_ + cA*(SHIP_TIP_X3 - MAIN_SHIP_WIDTH/2) - sA*(SHIP_TIP_Y3 - MAIN_SHIP_WIDTH/2);
		float aTy = y_ + sA*(SHIP_TIP_X3 - MAIN_SHIP_WIDTH/2) - cA*(SHIP_TIP_Y3 - MAIN_SHIP_WIDTH/2);
		float mVx = vx_ + 5*sA;
		float mVy = vy_ - 5*cA;
		return new Missile(aTx, aTy, angle_, mVx, mVy);
	}
	
	
	
	/**	Performs a hierarchical search to determine whether the point received
	 * as parameter is inside the ship.
	 * 
	 * @param x		x coordinate of a point in the world reference frame
	 * @param y		y coordinate of a point in the world reference frame
	 * @return	true if the point at (x, y) lies inside this face object.
	 */
	public boolean isInside(float x, float y) {
		boolean inside = false;
		//	If the point is inside the global absolute bounding box
		if (absoluteBox_[SPACESHIP].isInside(x, y)) {
			//	test if the point is inside one of the sub-boxes
			inside = absoluteBox_[LEFT_FIN].isInside(x, y) ||
					 absoluteBox_[RIGHT_FIN].isInside(x, y) ||
					 absoluteBox_[MAIN_SHIP].isInside(x, y);
		}
		return inside;
	}
}
