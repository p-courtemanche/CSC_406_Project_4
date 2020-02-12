package prog04;

import processing.core.*; 

/**	The Box class rewritten as a subclass of GraphicObject
 * 
 * @author jyh
 *
 */
public class Box extends GraphicObject {
	/**	
	 * 	The image to display as a "fill" for this object
	 */
	PImage image_;
	
	/**
	 * 	Horizontal scale to apply to the image
	 */
	float scaleX_;
	
	/**
	 * 	Vertical scale to apply to the image
	 */
	float scaleY_;
	
	
	/**	Default constructor. Initializes all instance variables with random values.
	 */
	public Box() {
		super();		

		setupDefaultBoundingBoxes_();
	}

	/**	Constructor. Creates a random object at set location 
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 */
	public Box(float x, float y, float accelv, float angularv) {
		super(x, y, accelv, angularv);

		setupDefaultBoundingBoxes_();
	}
	
	/** Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param color		The color is stored as a single int in hex format
	 * @param accelv	the acceleration that will be applied to the existing velocity
	 * @param angularv  the angular velocity that will turn the object
	 */
	public Box(float x, float y, float angle, float width, float height, int color, float accelv, float angularv) {
		super(x, y, angle, width, height, color, accelv, angularv);

		setupDefaultBoundingBoxes_();
	}
	
	/** Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 * @param accelv	the acceleration that will be applied to the existing velocity
	 * @param angularv  the angular velocity that will turn the object
	 * @param image		The image to draw inside this box
	 */
	public Box(float x, float y, float angle, float width, float height, float accelv, float angularv, PImage image) {
		super(x, y, angle, width, height, 0xFF000000, accelv, angularv);

		image_ = image;
		scaleX_ = width/image.width;
		scaleY_ = height/image.height;

		setupDefaultBoundingBoxes_();
	}
	
	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param x			x coordinate of the object's center (in world coordinates)
	 * @param y			y coordinate of the object's center (in world coordinates)
	 * @param angle		orientation of the object (in rad)
	 * @param scale		scale to apply to the image
	 * @param accelv	the acceleration that will be applied to the existing velocity
	 * @param angularv  the angular velocity that will turn the object
	 * @param image		The image to draw inside this box
	 */
	public Box(float x, float y, float angle, float scale, float accelv, float angularv, PImage image) {
		super(x, y, angle, 0, 0, 0, 0, 0);
		
		image_ = image;
		width_ = image.width*scale;
		height_ = image.height*scale;
		
		scaleX_ = scaleY_ = scale;

		setupDefaultBoundingBoxes_();
	}
	
	
	/**	Constructor. Initializes all instance variables to the values set by the arguments
	 * 
	 * @param image		The image to draw inside this box
	 * @param width		width of the object (in world units)
	 * @param height	height of the object (in world units)
	 */
	public Box(PImage image, float width, float height)
	{
		super();

		width_ = width;
		height_ = height;
		image_ = image;
		scaleX_ = width/image.width;
		scaleY_ = height/image.height;

		setupDefaultBoundingBoxes_();
	}
	

	/**	Initializes all instance variables with random values, except for the filling image
	 * @param image		The image to draw inside this box
	 * @param scale		scale to apply to the image
	 */
	public Box(PImage image, float scale)
	{
		super();

		width_ = image.width*scale;
		height_ = image.height*scale;
		image_ = image;
		scaleX_ = scaleY_ = scale;

		setupDefaultBoundingBoxes_();
	}

	/**	Rendering code specific to boxes
	 * 
	 * @param g	The Processing application in which the action takes place
	 */
	protected void draw_(PGraphics g)
	{
		g.pushMatrix();
		g.translate(-width_/2,  -height_/2);
		if (image_ == null)
			g.rect(0,  0,  width_, height_);
		else
		{
			g.scale(scaleX_, scaleY_);
			g.image(image_, 0, 0,  image_.width, image_.height);
		}
		g.popMatrix();
	}

	/** Updates the boxes
	 * 
	 */
	protected void updateAbsoluteBoxes_() {
		//	could definitely be optimized
		float cA = PApplet.cos(angle_), sA = PApplet.sin(angle_);
		float hwidth = width_/2, hheight = height_/2;
		float dx = Math.max(Math.abs(cA*hwidth - sA*hheight),
							Math.abs(cA*hwidth + sA*hheight)),
			  dy = Math.max(Math.abs(sA*hwidth + cA*hheight),
					  		Math.abs(sA*hwidth - cA*hheight));
		
		float	[]cornerX = {	x_ - dx,	//	upper left
								x_ + dx,	//	upper right
								x_ + dx,	//	lower right
								x_ - dx};	//	lower left
		
		float	[]cornerY = {	y_ - dy,	//	upper left
								y_ + dy,	//	upper right
								y_ + dy,	//	lower right
								y_ - dy};	//	lower left
				
		absoluteBox_[0].updatePosition(	PApplet.min(cornerX),	//	xmin
										PApplet.max(cornerX),	//	xmax
										PApplet.min(cornerY),	//	ymin
										PApplet.max(cornerY));	//	ymax
		
	}
	
	/**	Performs a hierarchical search to determine whether the point received
	 * as parameter is inside the face. 
	 * 
	 * @param x		x coordinate of a point in the world reference frame
	 * @param y		y coordinate of a point in the world reference frame
	 * @return	true if the point at (x, y) lies inside this face object.
	 */
	public boolean isInside(float x, float y) {
		//	Convert x and y into this object's reference frame coordinates
		double cosAngle = Math.cos(angle_), sinAngle = Math.sin(angle_);
		double xb = cosAngle*(x - x_) + sinAngle*(y - y_);
		double yb = cosAngle*(y - y_) + sinAngle*(x_ - x);
		return ((PApplet.abs((float) xb) <= width_/2) && (PApplet.abs((float) yb) <= height_/2));
	}
	
}