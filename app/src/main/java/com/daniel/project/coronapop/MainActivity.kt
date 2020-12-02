package com.daniel.project.coronapop

import android.R.id
import android.content.Intent
import android.graphics.Point
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.collision.Ray
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.function.Consumer


class MainActivity : AppCompatActivity() {
    private lateinit var scene: Scene
    private lateinit var camera: Camera
    private var bulletRenderable: ModelRenderable? = null
    private var shouldStartTimer = true
    private var coronaVirusLeft = 20
    private lateinit var point: Point
    private var coronaVirusLeftTxt: TextView? = null
    private var soundPool: SoundPool? = null
    private var sound = 0

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = windowManager.defaultDisplay
        point = Point()
        display.getRealSize(point)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_main)

        loadSoundPool()

        coronaVirusLeftTxt = findViewById(R.id.coronaVirusCntTxt)
        val arFragment: ArFragment =
            supportFragmentManager.findFragmentById(R.id.sceneform_fragment) as ArFragment

        arFragment.planeDiscoveryController.hide()
        arFragment.planeDiscoveryController.setInstructionView(null)

        scene = arFragment.arSceneView.scene
        camera = scene.camera

        addCoronaVirusToScene()
        buildSoap()

        coronaCardButton.setOnClickListener {

            coronaCard.visibility = GONE
            arFragmentLayout.visibility = VISIBLE
        }

        val shoot: Button = findViewById(R.id.shootButton)
        shoot.setOnClickListener {
            if (shouldStartTimer) {
                startTimer()
                shouldStartTimer = false
            }

            shoot()
        }

    }


    private fun buildSoap() {
        ModelRenderable
            .builder()
            .setSource(this, Uri.parse("soap.sfb"))
            .build()
            .thenAccept { renderable: ModelRenderable -> bulletRenderable = renderable }
    }

    private fun addCoronaVirusToScene() {
        ModelRenderable
            .builder()
            .setSource(this, Uri.parse("coronavirus.sfb"))
            .build()
            .thenAccept(Consumer<ModelRenderable> { renderable: ModelRenderable? ->
                for (i in 0..19) {
                    val node = Node()
                    node.renderable = renderable
                    scene.addChild(node)
                    val random = Random()
                    val x: Int = random.nextInt(10)
                    var z: Int = random.nextInt(10)
                    val y: Int = random.nextInt(20)
                    z = -z
                    node.worldPosition = Vector3(
                        x.toFloat(),
                        y / 10f,
                        z.toFloat()
                    )
                }
            })
    }

    var minutesPassed = 0
    var secondsPassed = 0


    private fun startTimer() {
        val timer = findViewById<TextView>(R.id.timerText)
        Thread(Runnable {
            var seconds = 0
            while (coronaVirusLeft > 0) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                seconds++
                minutesPassed = seconds / 60
                secondsPassed = seconds % 60
                runOnUiThread { timer.text = "$minutesPassed:$secondsPassed" }
            }
        }).start()
    }

    private fun shoot() {
        val ray: Ray = camera.screenPointToRay(point.x / 2f, point.y / 2f)
        val node = Node()
        node.renderable = bulletRenderable
        scene.addChild(node)
        Thread(Runnable {
            for (i in 0..199) {
                runOnUiThread {
                    val vector3 = ray.getPoint(i * 0.1f)
                    node.worldPosition = vector3


                    val nodeInContact = scene.overlapTest(node)

                    if (nodeInContact != null) {
                        coronaVirusLeft--
                        coronaVirusLeftTxt!!.text = "CoronaVirus Left: " + coronaVirusLeft
                        scene.removeChild(nodeInContact)
                        soundPool!!.play(
                            sound, 1f, 1f, 1, 0
                            , 1f
                        )
                        if (coronaVirusLeft == 0) {
                            arFragmentLayout.visibility = GONE
//                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
                            // [START custom_event]
                            val params = Bundle()
                            params.putString(
                                "Time_win",
                                "$minutesPassed minutues and $secondsPassed seconds"
                            )

                            firebaseAnalytics.logEvent(
                                "Time_win",
                                params
                            )
                            coronaFinishCard.visibility = VISIBLE
                            coronaFinishCardResultMinText.text = "$minutesPassed minute(s)"
                            coronaFinishCardResultSecText.text = "$secondsPassed second(s)"
                            coronaFinishCardButton.setOnClickListener {
                                startActivity(Intent(this, SplashActivity::class.java))

                            }
                        }
                    }

                }
                try {
                    Thread.sleep(10)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            runOnUiThread { scene.removeChild(node) }
        }).start()
    }

    private fun loadSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        sound = soundPool!!.load(this, R.raw.blop_sound, 1)
    }

}
