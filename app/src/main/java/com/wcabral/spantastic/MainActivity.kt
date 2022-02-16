package com.wcabral.spantasticsample

import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.text.method.LinkMovementMethod
import android.text.style.DynamicDrawableSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.wcabral.spantastic.*
import com.wcabral.spantastic_sample.databinding.ActivityMainBinding
import com.wcabral.spantastic_sample.R
import android.text.Spannable

import android.text.style.ImageSpan

import android.text.SpannableString




@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val drawable by lazy {
        ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_android_black_24dp,
            null
        )?.apply {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }!!
    }

    private val smallBitmap by lazy {
        resources.decodeSampledBitmapFromResource(R.drawable.android, 16, 16)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSpantastic()
    }

    private fun initSpantastic() {
//        binding.edtSpan.text = spantastic(binding.edtSpan.text.asSpannableStringBuilder()) {
//            breakLine()
//            h1("Teste")
//            title("Mais coisas")
//
//            "Outro texto" {
//                background(Color.BLACK)
//                foreground(Color.WHITE)
//            }
//        }

        val span = spantastic {

            "Image from resource id centered:\n" {
                setPosition(6, 7) {
                    image(context = this@MainActivity, resourceId = R.drawable.android)
                }
            }






//            h1("Text Style")
//
//            title("Bold:")
//            "Text bold" {
//                bold()
//            }
//
//            divider()
//
//            title("Italic:")
//            "Text italic" {
//                italic()
//            }
//
//            divider()
//
//            title("Underline:")
//            "Text with underline" {
//                underline()
//            }
//
//            divider()
//
//            title("Foreground:")
//            "Text with a foreground color" {
//                foreground(Color.BLUE)
//            }
//
//            divider()
//
//            title("Background:")
//            "Text with a background color" {
//                background(Color.GREEN)
//            }
//
//            divider()
//
//            title("Strikethrough:")
//            "Text with a strikethrough" {
//                strike()
//            }
//
//            divider()
//
//            title("Mask:")
//            "Text with a blur mask" {
//                mask(BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL))
//            }
//
//            divider()
//
//            title("Clickable:")
//            "Text with a clickable area" {
//                clickable {
//                    Toast.makeText(this@MainActivity, "Text was clicked", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            divider()
//
//            title("Absolute size:")
//            "Text with absolute size of 24" {
//                absoluteSize(24)
//            }
//
//            divider()
//
//            title("Relative size:")
//            "Text increased by 50%" {
//                relativeSize(1.5F)
//            }
//
//            divider()
//
//            title("ScaleX:")
//            "Text with ScaleX" {
//                scaleX(2F)
//            }
//
//            divider()
//
//            title("Text Appearance:")
//            "Text with a custom appearance" {
//                textAppearance(this@MainActivity, R.style.CustomStyle)
//            }
//
//            divider()
//
//            title("Custom font (typeface)")
//            "Text with a custom typeface" {
//                // Custom Font
//                val myTypeface = Typeface.create(
//                    ResourcesCompat.getFont(
//                        this@MainActivity,
//                        R.font.aguafina_script
//                    ),
//                    Typeface.NORMAL
//                )
//                typeface(myTypeface)
//                absoluteSize(22)
//            }
//
//            divider()
//
//            title("Style:")
//            "Bold" {
//                style(Typeface.BOLD)
//            }
//            + " and "
//            "italic" {
//                style(Typeface.ITALIC)
//            }
//
//            divider()
//
//            title("Subscript:")
//            + "☕- C"
//            "8" { subscript() }
//            + "H"
//            "10" { subscript() }
//            + "N"
//            "4" { subscript() }
//            + "O"
//            "2" { subscript() }
//
//            divider()
//
//            title("Superscript:")
//            "1" {
//                "st" { superscript() }
//            }
//
//            divider()
//
//            title("Alignment:")
//            "Text with normal alignment" {
//                align(Layout.Alignment.ALIGN_NORMAL)
//            }
//            breakLine()
//            "Text with center alignment" {
//                align(Layout.Alignment.ALIGN_CENTER)
//            }
//            breakLine()
//            "Text with opposite alignment" {
//                align(Layout.Alignment.ALIGN_OPPOSITE)
//            }
//
//            divider()
//
//            title("Leading margin:")
//            "Text with leading margin" {
//                leadingMargin(50)
//            }
//
//            divider()
//
//            title("Line height:")
//            "Text with line height\nThis is a multiline paragraph\nThis is a multiline paragraph" {
//                lineHeight(100)
//            }
//
//            divider()
//
//            h1("Groups")
//
//            title("Bullets:", shouldBreakLine = false)
//            bullet(
//                color = Color.RED,
//                gapWidth = 5,
//                bulletRadius = 10
//            ) {
//                + "Bullet 1"
//            }
//
//            bullet(
//                color = Color.BLUE,
//                gapWidth = 5,
//                bulletRadius = 10
//            ) {
//                + "Bullet 2"
//            }
//
//            bullet(
//                color = Color.MAGENTA,
//                gapWidth = 5,
//                bulletRadius = 10
//            ) {
//                + "Bullet 3"
//            }
//
//            divider()
//
//            title("Quote:", shouldBreakLine = false)
//            quote(
//                color = Color.RED,
//                stripeWidth = 20,
//                gapWidth = 30,
//            ) {
//                + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,when an unknown printer took a galley of type and scrambled it to make a type specimen book."
//            }
//
//            divider()
//
//            title("Tab stop:")
//            tab(offset = 50) {
//                + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,when an unknown printer took a galley of type and scrambled it to make a type specimen book."
//            }
//
//            divider()
//
//            h1("Images")
//
//            title("Drawable margin:")
//            "Text with a drawable margin" {
//                drawableMargin(
//                    drawable = drawable,
//                    padding = 5
//                )
//            }
//
//            divider()
//
//            title("Icon margin:")
//            "Text with a icon margin" {
//                iconMargin(
//                    bitmap = smallBitmap,
//                    padding = 5
//                )
//            }
//
//            divider()
//
//            title("Image:")
//            "Text with image bottom" {
//                image(this@MainActivity, R.drawable.ic_android_black_24dp)
//            }
//            breakLine()
//            "Text with image baseline " {
//                image(this@MainActivity, R.drawable.ic_android_black_24dp, DynamicDrawableSpan.ALIGN_BASELINE)
//            }
//
//            divider()
//
//            title("Image from Bitmap:")
//            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.android)
//            image(
//                context = this@MainActivity,
//                bitmap = bitmap,
//                verticalAlignment = DynamicDrawableSpan.ALIGN_CENTER
//            )
//
//            divider()
//
//            title("Image from resource id centered:")
//            image(
//                context = this@MainActivity,
//                resourceId = R.drawable.android,
//                verticalAlignment = DynamicDrawableSpan.ALIGN_BASELINE
//            ) {
//                align(Layout.Alignment.ALIGN_CENTER)
//            }
//
//            divider()
//
//            h1("Complex Spans")
//
//            title("Example:")
//            " An" {
//                background(Color.BLACK)
//                foreground(Color.WHITE)
//                absoluteSize(52)
//                "droid " {
//                    background(Color.BLACK)
//                    foreground(Color.GREEN)
//                    absoluteSize(52)
//                }
//            }
//
//            divider()
//
//            title("Example:")
//            + "By continuing, you agree to the "
//            "Terms os Service " {
//                url("https://www.google.com")
//            }
//            + "and "
//            "Privacy Policy" {
//                url("https://github.com/wellingtoncabral")
//            }
//            + "."
//
//            divider()
//
//            title("Example:")
//            "Lorem Ipsum is simply dummy text of the printing and typesetting industry." {
//                lineHeight(60)
//                background(Color.CYAN)
//                foreground(Color.BLACK)
//            }
//            + " "
//            "Lorem Ipsum has been the industry's standard dummy" {
//                background(Color.MAGENTA)
//                foreground(Color.WHITE)
//            }
//            + " "
//            "text ever since the 1500s when an unknown printer took a galley" {
//                background(Color.GRAY)
//                foreground(Color.WHITE)
//            }
//            + " "
//            "of type and scrambled it to make a type specimen book." {
//                background(Color.YELLOW)
//                foreground(Color.BLACK)
//            }
//
//            divider()

        }

        binding.tvSpannable.apply {
            movementMethod = LinkMovementMethod.getInstance()
            text = span
        }
    }
}