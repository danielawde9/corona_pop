package com.daniel.project.coronapop

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import com.github.paolorotolo.appintro.model.SliderPage


class IntroActivity : AppIntro() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val sliderPage = SliderPage()
        sliderPage.title = "AVOID CONTACT"
        sliderPage.description = "Cover your mouth and nose with a tissue when you cough or sneeze or use the inside of your elbow."
        sliderPage.imageDrawable = R.drawable.dr_aware
        sliderPage.bgColor = Color.parseColor("#545454")
        addSlide(AppIntroFragment.newInstance(sliderPage))

        val sliderPage2 = SliderPage()
        sliderPage2.title = "MAINTAIN SOCIAL DISTANCING"
        sliderPage2.description = "Maintain at least 1 metre (3 feet) distance between yourself and anyone who is coughing or sneezing."
        sliderPage2.imageDrawable = R.drawable.keep_distance
        sliderPage2.bgColor = Color.parseColor("#1E88E5");
        addSlide(AppIntroFragment.newInstance(sliderPage2))


        val sliderPage3 = SliderPage()
        sliderPage3.title = "STAY HOME"
        sliderPage3.description = "You can do your part to help your community and the world. Do not get close to other people."
        sliderPage3.imageDrawable = R.drawable.stay_home2
        sliderPage3.bgColor = Color.parseColor("#558B2F");
        addSlide(AppIntroFragment.newInstance(sliderPage3))

        val sliderPage4 = SliderPage()
        sliderPage4.title = "WASH YOUR HANDS FREQUENTLY"
        sliderPage4.description = "Regularly and thoroughly clean your hands with an alcohol-based hand rub or wash them with soap and water."
        sliderPage4.imageDrawable = R.drawable.wash_hands3
        sliderPage4.bgColor = Color.parseColor("#FF6E40");
        addSlide(AppIntroFragment.newInstance(sliderPage4))


        val sliderPage5 = SliderPage()
        sliderPage5.title = "CLEAN HOME"
        sliderPage5.description = "Clean AND disinfect frequently touched surfaces daily."
        sliderPage5.imageDrawable = R.drawable.clean_home
        sliderPage5.bgColor = Color.parseColor("#E53935");
        addSlide(AppIntroFragment.newInstance(sliderPage5))


        // OPTIONAL METHODS
        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"))
//        setSeparatorColor(Color.parseColor("#2196F3"))

        // Hide Skip/Done button.

        // Hide Skip/Done button.
        showSkipButton(false)
//        setButtonsEnabled(false)

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true)
        setVibrateIntensity(30)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        // Do something when users tap on Done button.
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onSlideChanged(
        @Nullable oldFragment: Fragment?,
        @Nullable newFragment: Fragment?
    ) {
        super.onSlideChanged(oldFragment, newFragment)
        // Do something when the slide changes.
    }
}
