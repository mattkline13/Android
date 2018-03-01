//View in which to draw to demonstrate various animations
//Written by Aaron Gordon - September, 2016  -Kotlin version, March 2018

package android.matt.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator

class ChalkBoard//Constructor - initialize this View
(context: Context) : View(context) {
    private var displayWidth: Int = 0       //width of screen - initialized in constructor
    private var displayHeight: Int = 0      //height of screen - initialized in constructor

    //values to define rectangle placed on screen
    private var startX = 55.0f  //left-most x-coordinate
    private var width = 300.0f  //rectangle width
    private var stopX = startX + width  //right-most x-coordinate
    private var height = 400.0f  //rectangle height
    private var top = 100.0f  //y-coordinate of rectangle's top
    private var bottom = top + height  //y-coordinate of rectangle's bottom
    private var deltaX = 40.0f   //change in x to next rectangle
    private var deltaY = 40.0f   //change in y to next rectangle
    private var oldX = 0.0f    //previous x-coordinate of rectangle NW corner
    private var oldY = 0.0f    //previous y-coordinate of rectangle NW corner
    private var x1 = startX   //x-coordinate of rectangle NW corner used for animation
    private var y1 = top      //y-coordinate of rectangle NW corner used for animation
    private var x2 = x1 + width   //x-coordinate of rectangle SE corner used for animation
    private var y2 = y1 + height  //y-coordinate of rectangle SE corner used for animation
    private var fraction = 1.0f      //fraction of distance from old rectangle to new one at each step of animation
    private var colorFraction = 1.0f    //fraction of distance from old color to new one at each step of animation
    private var angle = 0.0f        //angle of rotation used to animate rotations
    private var currColor = ChalkColor.randomChalkColor().color  //current color to use
    private var nextColor: ChalkColor  //if animating color change, new color we are heading for
    private var oldColor: ChalkColor? = null   //last color we used
    private var paint = Paint()  //to hold color for rendering
    private var style = RAW  //style holds current type of animation
    private var colorFlag = false     //set when animating color
    private var moveFlag = true      //set when animation involves motion

    init {
        nextColor = ChalkColor(currColor)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val screen = wm.defaultDisplay
        val size = Point()
        screen.getSize(size)
        displayWidth = size.x
        displayHeight = size.y
    }

    //Called from Activity when animation type is selected from menu
    fun setStyle(s: Int) {
        style = s
        colorFlag = s == COLOR_ACC || s == MOVE_RECOLOR || s == MOVE_ROTATE_RECOLOR || s == ROTATE_BOUNCE_COLOR
        moveFlag = s != ROTATE && s != COLOR_ACC
    }

    //called from main when button clicked
    //starts the animation process

    /**
     * Called from main when button clicked.
     * This method starts the animation process.
     */
    fun wander() {
        val anim: ObjectAnimator    //used in many cases below
        if (moveFlag) {
            oldX = startX
            oldY = top
            startX = (0.90 * displayWidth * Math.random()).toFloat()
            deltaX = startX - oldX
            stopX = startX + width
            top = (0.80 * displayHeight * Math.random()).toFloat()
            deltaY = top - oldY
            bottom = top + height
        }
        if (colorFlag) {
            oldColor = nextColor
            nextColor = ChalkColor.randomChalkColor()
        }
        when (style) {
            ANIMATOR      // Smooth Move ObjectAnimator
            -> getObjectAnimator(500, "fraction", 0.0f, 1.0f).start() //local method
            RAW   -> {        //no animation - just jump to spot
                fraction = 1.0f
                step()
            }
            ACCELERATOR  -> { //Accelerate in using AccelerateInterpolator
                anim = getObjectAnimator(500, "fraction", 0.0f, 1.0f) //local method
                anim.interpolator = AccelerateInterpolator(1.5f) //try 1.5f or 0.8f
                anim.start()
            }
            DECELERATE -> {  //Deaccelerate  using AccelerateInterpolator
                anim = getObjectAnimator(500, "fraction", 0.0f, 1.0f) //local method
                anim.interpolator = DecelerateInterpolator(1.5f)
                anim.start()
            }
            BOUNCE -> {  //Accelerate in using AccelerateInterpolator
                anim = getObjectAnimator(500, "fraction", 0.0f, 1.0f) //local method
                anim.interpolator = BounceInterpolator()
                anim.start()
            }
            ROTATE -> {
                anim = getObjectAnimator(700, "angle", 0.0f, 360.0f)
                anim.start()
            }
            MOVE_ROTATE -> {
                val moving = getObjectAnimator(500, "fraction", 0.0f, 1.0f)
                val spinner = getObjectAnimator(700, "angle", 0.0f, 360.0f)
                val spinMove = AnimatorSet()
                spinMove.play(moving).with(spinner)
                spinMove.start()
            }
            COLOR_ACC     //Animate color change
            -> getObjectAnimator(800, "currColor", 0.0f, 1.0f).start() //local method
            MOVE_RECOLOR -> {
                val mover = getObjectAnimator(500, "fraction", 0.0f, 1.0f)
                val recolor = getObjectAnimator(500, "currColor", 0.0f, 1.0f)
                val together = AnimatorSet()
                together.play(mover).with(recolor)
                together.start()
            }
            MOVE_ROTATE_RECOLOR -> {
                val moveguy = getObjectAnimator(500, "fraction", 0.0f, 1.0f)
                val recolorguy = getObjectAnimator(500, "currColor", 0.0f, 1.0f)
                val spinguy = getObjectAnimator(700, "angle", 0.0f, 360.0f)
                val atOnce = AnimatorSet()
                atOnce.play(moveguy).with(spinguy)
                atOnce.play(recolorguy).after(moveguy)
                atOnce.start()
            }
            ROTATE_BOUNCE_COLOR -> {
                val bounce = getObjectAnimator(500, "fraction", 0.0f, 1.0f) //local method
                val spinner = getObjectAnimator(700, "angle", 0.0f, 360.0f)
                val recolor = getObjectAnimator(500, "currColor", 0.0f, 1.0f)
                val together = AnimatorSet()
                together.play(spinner).with(bounce).with(recolor)
                together.interpolator = BounceInterpolator()
                together.start()
            }
            ROTATE_WHILE_ACCELERATING -> {
                val accelerate = getObjectAnimator(500, "fraction", 0.0f, 1.0f) //local method
                val spinner = getObjectAnimator(500, "angle", 0.0f, 1800.0f)
                val together = AnimatorSet()
                together.play(accelerate).with(spinner)
                together.interpolator = AccelerateInterpolator(2f) //try 1.5f or 0.8f
                together.start()
            }
            else -> {
            }
        }
    }

    /**
     * getObjectAnimator is called to build the object that controls the animation
     *
     * @param duration  milliseconds the animation should take
     * @param variable  variable to change at each step of animation
     * @param initialValue starting value for the variable
     * @param finalValue final value for the variable
     * @return the ObjectAnimator that controls the variable's changes
     */
    private fun getObjectAnimator(duration: Int, variable: String, initialValue: Float, finalValue: Float): ObjectAnimator {
        val animation = ObjectAnimator.ofFloat(this, variable, initialValue, finalValue)
        animation.duration = duration.toLong()
        return animation
    }

    //

    /**
     * setFraction sets the fraction of the complete distance at each step in the animation
     * This method is called by the ObjectAnimator
     *
     * @param value the current value to use
     */
    fun setFraction(value: Float) {
        fraction = value
        step()
    }

    /**
     * setCurrColor sets the fraction of the complete distance at each step in the animation
     * This method is called by the ObjectAnimator
     *
     * @param value the current value to use
     */
    fun setCurrColor(value: Float) {
        colorFraction = value
        currColor = oldColor!!.blend(nextColor, value)
        invalidate()
    }

    //
    /**
     * setAngle sets the current angle to use for a rotation at each step in the animation
     * This method is called by the ObjectAnimator
     *
     * @param value the current value to use
     */
    fun setAngle(value: Float) {
        angle = value
        this.rotation = angle
        invalidate()
    }

    //compute values for one step in the animation
    private fun step() {
        x1 = oldX + fraction * deltaX
        y1 = oldY + fraction * deltaY
        x2 = x1 + width
        y2 = y1 + height
        invalidate()
    }

    /**
     * Renders the View
     *
     * @param canvas Where to do the rendering
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = currColor
        canvas.drawRect(x1, y1, x2, y2, paint)
    }

    companion object {  //static stuff

        const val RAW = 0  //Constant to indicate no animation - jumps to new location
        const val ANIMATOR = 1  //Constant to indicate default movement animation
        const val ACCELERATOR = 2  //Constant to indicate accelerate-at-end movement animation
        const val DECELERATE = 3  //Constant to indicate decelerate-at-end movement animation
        const val BOUNCE = 4  //Constant to indicate bounce-at-end movement animation
        const val ROTATE = 5  //Constant to indicate rotate around View center animation
        const val MOVE_ROTATE = 9  //Constant to indicate move and rotate simultaneously animation
        const val COLOR_ACC = 11  //Constant to indicate transition color animation
        const val MOVE_RECOLOR = 12  //Constant to indicate move and change color simultaneously
        const val MOVE_ROTATE_RECOLOR = 23  //Constant to indicate move and rotate simultaneously then recolor animation
        const val ROTATE_BOUNCE_COLOR = 14
        const val ROTATE_WHILE_ACCELERATING = 16
    }
}