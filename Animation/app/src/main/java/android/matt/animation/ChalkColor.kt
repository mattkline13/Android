//This class holds colors for the ChalkBoard class
//Written by Aaron Gordon - September, 2016 - Kotlin version March, 2018

package android.matt.animation

import android.graphics.Color

class ChalkColor (c: Int) {
    private var blue: Int = 0    //blue component
    private var red: Int = 0     //red component
    private var green: Int = 0   //green component
    private var alpha: Int = 0   //alpha component
    var color: Int = 0
        internal set   //complete color value

    init {
        color = c
        blue = Color.blue(c)
        red = Color.red(c)
        green = Color.green(c)
        alpha = Color.alpha(c)
    }

    /**
     * This method takes another color and returns the int representing
     * the color a fraction of the way from this color to other
     *
     * @param other The color we are heading to
     * @param fraction How far along the way we want to be
     * @return The Color at the desired point
     */
    fun blend(other: ChalkColor, fraction: Float): Int {
        val r = red + (fraction * (other.red - red)).toInt()
        val g = green + (fraction * (other.green - green)).toInt()
        val b = blue + (fraction * (other.blue - blue)).toInt()
        val a = alpha + (fraction * (other.alpha - alpha)).toInt()
        return Color.argb(a, r, g, b)
    }

    companion object {

        /**
         * This static method creates a new ChalkColor object holding a random color
         * @return The created object.
         */
        fun randomChalkColor(): ChalkColor {
            val c = Color.argb(255, (255 * Math.random()).toInt(),
                    (255 * Math.random()).toInt(),
                    (255 * Math.random()).toInt())
            return ChalkColor(c)
        }
    }

}