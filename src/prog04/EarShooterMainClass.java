package prog04;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/*--------------------------------------------------------------------------+
|	Keyboard commands:														|
|		• ' ' pauses/resumes animation										|
|		• 'b' set the animation mode to "BOX_WORLD"							|
|		• 'c' set the animation mode to "CYLINDER_WORLD"					|
|		• 'f' displays the objects' reference frames						|
|		• 'q' displays the objects' absolute bounding boxes					|
|		• 'r' displays the objects' relative bounding boxes					|
|		• 'n' disables display of the objects' bounding boxes				|
|		• 'LEFT', 'a', '[' make the spaceship spin left						|
|	    • 'RIGHT', 'd', ']' make the spaceship spin left					|
|	    • 'UP' and 'w' make the spaceship move in the direction it is facing|
|																			|
|																			|
|																			|
|	Jean-Yves Hervé, Nov. 2012 (version for design grad students).			|
|					 Revised Nov. 2019 for CSC406.							|
|				Revised Nov. 2019 by Paige Courtemanche						|
+--------------------------------------------------------------------------*/

/** This draws and updates all of the objects and the background
 * 	The user can also interact with the game by pressing the kes listed above
 * 
 * @author Paige Courtemanche and jyh
 *
 */
public class EarShooterMainClass extends PApplet implements ApplicationConstants {
	//-----------------------------
	//	graphical objects
	//-----------------------------
	KeyframedSpaceship spaceship_; // This is the spaceship
	
	ArrayList<GraphicObject> asteroidList_; //This is the list of asteroids
	
	ArrayList<GraphicObject> missileList_; // This is the list of missiles
	
	GraphicObject asteroid;
	
	GraphicObject missile;

	//-----------------------------
	//	Various status variables
	//-----------------------------
	/**	Desired rendering frame rate
	 * 
	 */
	static final float RENDERING_FRAME_RATE = 60;
	
	/**	Ratio of animation frames over rendering frames 
	 * 
	 */
	static final int ANIMATION_RENDERING_FRAME_RATIO = 5;
	
	/**	computed animation frame rate
	 * 
	 */
	static final float ANIMATION_FRAME_RATE = RENDERING_FRAME_RATE * ANIMATION_RENDERING_FRAME_RATIO;
	
	
	/**	Variable keeping track of the last time the update method was invoked.
	 * 	The different between current time and last time is sent to the update
	 * 	method of each object. 
	 */
	private int lastUpdateTime_;
	
	/**	A counter for animation frames
	 * 
	 */
	private int frameCount_;


	/**	Whether to draw the reference frame of each object (for debugging
	 * purposes.
	 */
	private boolean drawRefFrame_ = false;

	/**	Used to pause the animation
	 */
	boolean animate_ = true;
	
	/** How we handle edges of the window
	 * 	
	 */
	private AnimationMode animationMode_ = AnimationMode.BOX_WORLD;
	
	/**	The off-screen buffer used for double buffering (and display content
	 * for some rectangles.
	 */
	private PGraphics offScreenBuffer_;

	/**	Previous value of the off-screen buffer (after the last call to draw()
	 */
	private PGraphics lastBuffer_;

	/** Toggles on-off double buffering.
	 */
	private boolean doDoubleBuffer_ = false;
	
	/** This is background image
	 */
	PImage backgroundImage;
	
//	PImage smallAsteroidImage;
//	PImage mediumAsteroidImage;
//	PImage largeAsteroidImage;

	/** This is the settings function where the window is defined
	 * 
	 */
	public void settings() {
		//  dimension of window in pixels
		size(WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	/** The world is setup and the spaceship and asteroids are created
	 * 
	 */
	public void setup() {    
		if (BAIL_OUT_IF_ASPECT_RATIOS_DONT_MATCH) {
			if (Math.abs(WORLD_HEIGHT - PIXEL_TO_WORLD*WINDOW_HEIGHT) > 1.0E5) {
				System.out.println("World and Window aspect ratios don't match");
				System.exit(1);
			}
		}
		
		frameRate(ANIMATION_FRAME_RATE);
		frameCount_ = 0;
		
		//Space background
		backgroundImage = loadImage("space.jpg");
		
//		smallAsteroidImage = loadImage("small_asteroid.jpg");
//		mediumAsteroidImage = loadImage("medium_asteroid.jpg");
//		largeAsteroidImage = loadImage("large_asteroid.jpg");

		
		asteroidList_ = new ArrayList<GraphicObject>();
		missileList_ = new ArrayList<GraphicObject>();
		
		// Asteroids are created and added to the asteroid list
		for (int k=0; k<4; k++){
			asteroidList_.add(new Asteroid());
		}
		
							//	   x    y    θ     red   green   t
		float [][]keyFrames = { { 50,   50,  0.f,   255,  255,   0},		//	frame 0 [center, red]
								{  0,   0,  0.f,  255,    0,   2},		//	frame 1 [center, red]
								{-15,  10,  0.f,    0,  255,   6},		//	frame 2 [upper-left, green]
								{-15,  10, 2*PI,  255,  255,   8},		//	frame 3 [upper-left, yellow]
								{+15,  10,  -PI,  255,  127,  12},		//	frame 4 [upper-right, orange]
								{+15, -10, 6*PI,  127,  127,  22},		//	frame 5 [lower-right, kaki] 
								{+15, -10, 4*PI,  255,    0,  30}       //	frame 6 [lower-right, red]
							  };	
		LinearKeyframeInterpolator faceInterpolator = new LinearKeyframeInterpolator(keyFrames);
		spaceship_ = new KeyframedSpaceship(faceInterpolator);
	}

	/** Processing sketch rendering callback function
	 * 
	 */
	public void draw() {
		//================================================================
		//	Only render one frame out of ANIMATION_RENDERING_FRAME_RATIO
		//================================================================
		if (frameCount_ % ANIMATION_RENDERING_FRAME_RATIO == 0) {
			//	draw the background image
			//	                   x  y  width  height
			image(backgroundImage, 0, 0, width, height);
			
			PGraphics gc;
			if (doDoubleBuffer_) {
				//	I say that the drawing will take place inside of my off-screen buffer
				gc = offScreenBuffer_;
				offScreenBuffer_.beginDraw();
			} else
				//	Otherwise, the "graphic context" is that of this PApplet
				gc = this.g;


			// define world reference frame:  
			//    Origin at windows's center and 
			//    y pointing up
			//    scaled in world units
			gc.translate(WORLD_X, WORLD_Y); 
			gc.scale(DRAW_IN_WORLD_UNITS_SCALE, -DRAW_IN_WORLD_UNITS_SCALE);

			if (drawRefFrame_)
				GraphicObject.drawReferenceFrame(gc);

			if ((animationMode_ == AnimationMode.BOX_WORLD) || (animationMode_ == AnimationMode.WINDOW_WORLD)) {
				spaceship_.draw(gc);
				
				for (GraphicObject obj : asteroidList_)
					if(obj.hits == 0) {
						obj.draw(gc);
					}
				
				for (GraphicObject missile : missileList_)
					missile.draw(gc);
				
			} else {
				spaceship_.draw(gc);
				
				for (GraphicObject obj : asteroidList_)
					obj.drawAllQuadrants(gc);
				
				for (GraphicObject missile: missileList_)
					missile.drawAllQuadrants(gc);
				
			}
			
			if (doDoubleBuffer_) {
				offScreenBuffer_.endDraw();

				image(offScreenBuffer_, 0, 0);				
	
				//	For some reason this doesn't work and I don't understand why.
				lastBuffer_.beginDraw();
				lastBuffer_.image(offScreenBuffer_, 0, 0);
				lastBuffer_.endDraw();

				int []pixelLB = lastBuffer_.pixels;
				int []pixelOSB = offScreenBuffer_.pixels;
				//int nbPixels = width*height;
				//	Copy pixel info last buffer.  Since we flipped the y axis,
				//	we need to invert the row order while copying
				for (int i=0, k=height-1; i<height; i++,k--)
					for (int j=0; j<width; j++)
						pixelLB[k*width + j] = pixelOSB[i*width + j];
				
				lastBuffer_.updatePixels();
			}
			
			if (keyPressed) {
				handleKeyPressed();
			}
			
			hitCheck();
		
		}

		//  and then update their state
		if (animate_) {
			update();
		}
		
		frameCount_++;
	}
	
	/** Updates the locations of the objects based on how much time has passed
	 * 
	 */
	public void update() {

		int time = millis();

		if (animate_) {
			//  update the state of the objects ---> physics
			float dt = (time - lastUpdateTime_)*0.001f;
			
			spaceship_.update(dt);
			
			for (GraphicObject obj : asteroidList_) {
				if (obj instanceof AnimatedObject)
					obj.update(dt);

			}
			
			for (GraphicObject missile : missileList_) {
				if (missile instanceof AnimatedObject)
					missile.update(dt);

			}
			
		}

		lastUpdateTime_ = time;
	}
	

	/** Processing callback function for keyboard events
	 *  See the top of file to see what each key press means
	 */
	public void keyReleased(){
		switch(key) {
			case ' ':
				animate_ = !animate_;
				if (animate_)
					lastUpdateTime_ = millis();
				break;
			
				
			case 'n':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.NO_BOX);
				break;
	
			case 'r':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.RELATIVE_BOX);
				break;
	
			//the case is 'q' because the controls use 'a'
			case 'q':
				GraphicObject.setBoundingBoxMode(BoundingBoxMode.ABSOLUTE_BOX);
				break;
				
			case 'f':
				drawRefFrame_ = !drawRefFrame_;
				GraphicObject.setDrafReferenceFrame(drawRefFrame_);
				break;
			
			//
			case 'b':
				GraphicObject.setAnimationMode(AnimationMode.BOX_WORLD);
				animationMode_ = AnimationMode.BOX_WORLD;
				break;
	
			case 'c':
				GraphicObject.setAnimationMode(AnimationMode.CYLINDER_WORLD);
				animationMode_ = AnimationMode.CYLINDER_WORLD;
				break;

			default:
				break;
		}
	}
	
	/**
	 * These are the game key controls
	 * 'LEFT', 'a', '[' make the spaceship spin left
	 * 'RIGHT', 'd', ']' make the spaceship spin left
	 * 'UP' and 'w' make the spaceship move in the direction it is facing
	 */
	private void handleKeyPressed(){
		if (key == CODED) {
			switch(keyCode) {
			case LEFT:
				spaceship_.spinLeft();
				break;
			case RIGHT:
				spaceship_.spinRight();
				break;
			case UP:
				spaceship_.applyThrust();
				break;	
			}
		} else switch(key) {
			case 'a':
			case '[':
				spaceship_.spinLeft();
				break;
			case 'd':
			case ']':
				spaceship_.spinRight();
				break;
			case 'w':
				spaceship_.applyThrust();
				break;
		}
	}
	
	/**
	 * When the mouse is pressed, a new missile is made and added to the list of missiles
	 */
	public void mousePressed() {
		missileList_.add(spaceship_.missileShoot());
	}
	
	/**
	 * This method checks each asteroid to see if any of the missiles struck it
	 * If a missile hits an asteroid, both the missile and the asteroid disappear
	 */
	public void hitCheck(){
		//This loop checks each of the asteroids in the asteroid list 
		for (int i = asteroidList_.size() - 1; i >= 0; i--) {
			asteroid = asteroidList_.get(i);
			
			//This loop checks each of the missiles in the missile list 
			for (int j = missileList_.size() - 1; j >= 0; j--) {
				missile = missileList_.get(j);
				//println(missile.x_, missile.y_);
				float missileX = missile.x_;
				float missileY = missile.y_;
			
				// If the coordinates of the missile are inside the asteroid's relative box,
				// then both the missile and the asteroid are removed from their lists
				// which makes them disappear
				if(asteroid.isInside(missileX, missileY) == true) {
					//println("YOOOOOOO");
					asteroidList_.remove(asteroid);
					missileList_.remove(missile);
					break;
				}
			}
		}
	}

	public static void main(String[] argv) {
		PApplet.main("prog04.EarShooterMainClass");
	}

}
