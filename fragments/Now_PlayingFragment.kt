package iug.jumana.grace.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler

import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isNotEmpty
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView

import iug.jumana.grace.R
import iug.jumana.grace.activities.Countrymusic
import iug.jumana.grace.database.DatabaseHelper

import iug.jumana.grace.fragments.Now_PlayingFragment.Statified.myActivity
import iug.jumana.grace.fragments.Now_PlayingFragment.Statified.startTime
import iug.jumana.grace.model.CurrentSongHelper
import iug.jumana.grace.model.SongsCountry
import kotlinx.android.synthetic.main.fragment_now__playing.*
import kotlinx.android.synthetic.main.fragment_now__playing.view.*
import kotlinx.android.synthetic.main.fragment_trending.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class Now_PlayingFragment : Fragment() {
    object Statified {
        var myActivity: Activity? = null
        var mediaPLayer: MediaPlayer? =null
        var startTime: TextView? = null
        var endTime: TextView? = null
        var playPauseButton: ImageButton? = null
        var previousButton: ImageButton? = null
        var nextButton: ImageButton? = null
        var loopButton: ImageButton? = null
        var seekBar: SeekBar? = null
        var songArtistView: TextView? = null
        var songTitleView: TextView? = null
        var shuffleButton: ImageButton? = null
        var fab: ImageButton? = null
        var MY_PRESS = "ShakeFeature"

        var currentPosition: Int = 0
        var fetchSongs: ArrayList<SongsCountry>? = null
        var currentSongHelper: CurrentSongHelper? = null
        var audioVisualization: AudioVisualization? = null
        var glView: GLAudioVisualizationView? = null

        var favoriteContent: DatabaseHelper? = null
        var mSensoreManager: SensorManager? = null
        var mSensorListener: SensorEventListener? = null

        var updateSongTime = object : Runnable {
            override fun run() {
                val getCurrent = mediaPLayer?.currentPosition
                startTime?.setText(
                    String.format(
                        "Xd:Xd",

                        TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long),
                        TimeUnit.MILLISECONDS.toSeconds(getCurrent?.toLong() as Long) -
                                TimeUnit.MILLISECONDS.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(
                                        getCurrent?.toLong() as Long
                                    )
                                )
                    )
                )

                seekBar?.setProgress(getCurrent?.toInt() as Int)
                Handler().postDelayed(this, 1000)
            }

        }
    }

    object Staticated {
        var MY_PRESS_SHUFFLE = "Shuffle feature"
        var MY_PRESS_LOOP = "Loop feature"
        fun onSongComplete() {
            if (Statified.currentSongHelper?.isShuffle as Boolean) {
                playNext("PlayNextLikeNormalShuffle")
                Statified.currentSongHelper?.isPlaying = true
            } else {
                if (Statified.currentSongHelper?.isLoop as Boolean) {
                    Statified.currentSongHelper?.isPlaying = true
                    var nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
                    Statified.currentSongHelper?.songTitle = nextSong?.songTitle
                    Statified.currentSongHelper?.songPath = nextSong?.songData
                    Statified.currentSongHelper?.currentPosition = Statified.currentPosition
                    Statified.currentSongHelper?.songId = nextSong?.songId as Long
                    updateTextViews(
                        Statified.currentSongHelper?.songTitle as String,
                        Statified.currentSongHelper?.songArtist as String
                    )
                    Statified.mediaPLayer?.reset()
                    try {
                        Statified.mediaPLayer?.setDataSource(
                            myActivity as Context,
                            Uri.parse(Statified.currentSongHelper?.songPath)
                        )
                        Statified.mediaPLayer?.prepare()
                        Statified.mediaPLayer?.start()
                        processInformation(Statified.mediaPLayer as MediaPlayer)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    playNext("PlayNextNormal")
                    Statified.currentSongHelper?.isPlaying = true
                }
            }
            if (Statified.favoriteContent?.checkIfIdExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(
                    ContextCompat.getDrawable(
                        myActivity as Context,
                        R.drawable.favorite_on
                    )
                )
            } else {
                Statified.fab?.setImageDrawable(
                    ContextCompat.getDrawable(
                        myActivity as Context,
                        R.drawable.favorite_off
                    )
                )
            }

        }

        fun updateTextViews(songTitle: String, songArtist: String) {
            var songTitleUpdated=songTitle
            var songArtistUpdated=songArtist
            if (songTitle.equals("<unknown>",true)){
                songTitleUpdated="unknown"
            }
            if (songArtist.equals("<unknown>",true)){
                songArtistUpdated="unknown"
            }
            Statified.songTitleView?.setText(songTitleUpdated)
            Statified.songArtistView?.setText(songArtistUpdated)
        }

        var mAccelaration: Float = 0f
        var mAccelarationCurrent: Float = 0f
        var mAccelarationLast: Float = 0f

        fun processInformation(mediaPlayer: MediaPlayer) {
            val finalTime = mediaPlayer.duration
            val startTimeView = mediaPlayer.currentPosition
            Statified.seekBar?.max = finalTime
            Statified.startTime?.setText(
                String.format(
                    "%d:%d",

                    TimeUnit.MILLISECONDS.toMinutes(Statified.startTime?.toString() as Long),
                    TimeUnit.MILLISECONDS.toSeconds(Statified.startTime?.toString() as Long) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime?.toString() as Long))
                )
            )

            Statified.endTime?.setText(
                String.format(
                    "%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(Statified.endTime?.toString() as Long),
                    TimeUnit.MILLISECONDS.toSeconds(Statified.endTime?.toString() as Long) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Statified.endTime?.toString() as Long))
                )
            )

            Statified.seekBar?.setProgress(startTimeView)
            Handler().postDelayed(Statified.updateSongTime, 1000)
        }

        fun playNext(check: String) {
            if (check.equals("PlayNextNormal", true)) {
                Statified.currentPosition = Statified.currentPosition + 1
            } else if (check.equals("PlayNextLikeNormalShuffle", true)) {
                var randomObject = java.util.Random()
                var randomPosition =
                    randomObject.nextInt(Statified.fetchSongs?.size?.plus(1) as Int)
                Statified.currentPosition = randomPosition


            }
            if (Statified.currentPosition == Statified.fetchSongs?.size) {
                Statified.currentPosition = 0
            }
            Statified.currentSongHelper?.isLoop = false
            var nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
            Statified.currentSongHelper?.songTitle = nextSong?.songTitle
            Statified.currentSongHelper?.songPath = nextSong?.songData
            Statified.currentSongHelper?.currentPosition = Statified.currentPosition
            Statified.currentSongHelper?.songId = nextSong?.songId as Long

            updateTextViews(
                Statified.currentSongHelper?.songTitle as String,
                Statified.currentSongHelper?.songArtist as String
            )

            Statified.mediaPLayer?.reset()
            try {
                Statified.mediaPLayer?.setDataSource(
                    myActivity as Context,
                    Uri.parse(Statified.currentSongHelper?.songPath)
                )
                Statified.mediaPLayer?.prepare()
                Statified.mediaPLayer?.start()
                processInformation(Statified.mediaPLayer as MediaPlayer)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (Statified.favoriteContent?.checkIfIdExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean) {
                Statified.fab?.setImageDrawable(
                    ContextCompat.getDrawable(
                        myActivity as Context,
                        R.drawable.favorite_on
                    )
                )
            } else {
                Statified.fab?.setImageDrawable(
                    ContextCompat.getDrawable(
                        myActivity as Context,
                        R.drawable.favorite_off
                    )
                )
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_now__playing, container, false)
        activity!!.title="Now playing"
        setHasOptionsMenu(true)
        Statified.seekBar = root.seekBar
        Statified.startTime = root.startTime
        Statified.endTime = root.endTime
        Statified.playPauseButton = root.playPauseButton
        Statified.nextButton = root.nextButton
        Statified.previousButton = root.previousButton
        Statified.loopButton = root.loopButton
        Statified.shuffleButton = root.shuffleButton
        Statified.songArtistView = root.songArtist
        Statified.songTitleView = root.songTitle
        Statified.glView = root.visualizer_view
        Statified.fab = root.favoriteIcon
        Statified.fab?.alpha = 0.8f

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Statified.audioVisualization = Statified.glView as AudioVisualization
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Statified.myActivity = context as Activity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        Statified.myActivity = activity
    }

    override fun onResume() {
        super.onResume()
        Statified.audioVisualization?.onResume()
        Statified.mSensoreManager?.registerListener(
            Statified.mSensorListener,
            Statified.mSensoreManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }

    override fun onPause() {
        super.onPause()
        Statified.audioVisualization?.onPause()
        Statified.mSensoreManager?.unregisterListener(Statified.mSensorListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Statified.audioVisualization?.release()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Statified.mSensoreManager =
            Statified.myActivity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Staticated.mAccelaration = 0.0f
        Staticated.mAccelarationCurrent = SensorManager.GRAVITY_EARTH
        Staticated.mAccelarationLast = SensorManager.GRAVITY_EARTH
        bindShakeListener()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu?.clear()
        inflater?.inflate(R.menu.now_playing_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item: MenuItem? = menu?.findItem(R.id.actionredirect)
        item?.isVisible = true
        val item2: MenuItem? = menu?.findItem(R.id.actionredirect)
        item2?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionredirect -> {
               Statified.myActivity?.onBackPressed()
                return false
            }
        }
        return false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Statified.favoriteContent = DatabaseHelper(myActivity!!)
        Statified.currentSongHelper = CurrentSongHelper()
        Statified.currentSongHelper?.isPlaying = true
        Statified.currentSongHelper?.isShuffle = false
        Statified.currentSongHelper?.isLoop = false
        var path: String? = null
        var songTitle: String? = null
        var songArtist: String? = null
        var songId: Long = 0
        try {
            path = arguments?.getString("path")
            songTitle = arguments?.getString("songTitle")
            songArtist = arguments?.getString("songArtist")
            songId = arguments?.getInt("songId")!!.toLong() as Long
            Statified.currentPosition = arguments?.getInt("songPosition")!!.toInt()
            Statified.fetchSongs = arguments?.getParcelableArrayList<SongsCountry>("songData")

            Statified.currentSongHelper?.songPath = path
            Statified.currentSongHelper?.songTitle = songTitle
            Statified.currentSongHelper?.songArtist = songArtist
            Statified.currentSongHelper?.songId = songId
            Statified.currentSongHelper?.currentPosition = Statified.currentPosition
            Staticated.updateTextViews(
                Statified.currentSongHelper?.songTitle as String,
                Statified.currentSongHelper?.songArtist as String
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        var fromFavBottomBar = arguments?.get("FavBottomBar") as? String
        if (fromFavBottomBar != null) {
            Statified.mediaPLayer = Favorite.Statified.mediaPlayer
        } else {
            Statified.mediaPLayer = MediaPlayer()
            Statified.mediaPLayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            try {
                Statified.mediaPLayer?.setDataSource(context!!, Uri.parse(path))
                Statified.mediaPLayer?.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Statified.mediaPLayer?.start()
        }
        Staticated.processInformation(Statified.mediaPLayer as MediaPlayer)
        if (Statified.currentSongHelper?.isPlaying as Boolean) {
            Statified.playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
        }
        fun clickHandler() {
            Statified.fab?.setOnClickListener {
                if (Statified.favoriteContent?.checkIfIdExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean) {
                    Statified.fab?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            R.drawable.favorite_off
                        )
                    )
                    Statified.favoriteContent?.deleteFavorite(Statified.currentSongHelper?.songId?.toInt() as Int)
                    Toast.makeText(
                        Statified.myActivity,
                        "Removed from favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Statified.fab?.setImageDrawable(
                        ContextCompat.getDrawable(
                            context!!,
                            R.drawable.favorite_on
                        )
                    )
                    Statified.favoriteContent?.storeAsFavorite(
                        Statified.currentSongHelper?.songId?.toInt(),
                        Statified.currentSongHelper?.songArtist,
                        Statified.currentSongHelper?.songTitle,
                        Statified.currentSongHelper?.songPath
                    )
                    Toast.makeText(Statified.myActivity, "Added to favorite", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            Statified.shuffleButton?.setOnClickListener {
                var editorShuffle = Statified.myActivity?.getSharedPreferences(
                    Staticated.MY_PRESS_SHUFFLE,
                    Context.MODE_PRIVATE
                )?.edit()
                var editorLoop = Statified.myActivity?.getSharedPreferences(
                    Staticated.MY_PRESS_LOOP,
                    Context.MODE_PRIVATE
                )?.edit()

                if (Statified.currentSongHelper?.isShuffle as Boolean) {
                    shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                    Statified.currentSongHelper?.isShuffle = false
                    editorShuffle?.putBoolean("feature", false)
                    editorShuffle?.apply()
                } else {
                    Statified.currentSongHelper?.isShuffle = true
                    Statified.currentSongHelper?.isLoop = false
                    shuffleButton?.setBackgroundResource(R.drawable.shuffle_icon)
                    loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
                    editorShuffle?.putBoolean("feature", true)
                    editorShuffle?.apply()
                    editorLoop?.putBoolean("feature", false)
                    editorLoop?.apply()
                }
            }
            Statified.nextButton?.setOnClickListener {
                Statified.currentSongHelper?.isPlaying = true
                Statified.playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
                if (Statified.currentSongHelper?.isShuffle as Boolean) {
                    Staticated.playNext("PlayNextLikeNormalShuffle")
                } else {
                    Staticated.playNext("PlayNextNormal")
                }
            }
            Statified.previousButton?.setOnClickListener {
                Statified.currentSongHelper?.isPlaying = true
                if (Statified.currentSongHelper?.isLoop as Boolean) {
                    loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
                } else {
                    playPrevious()
                }
            }
            Statified.loopButton?.setOnClickListener {
                var editorShuffle = Statified.myActivity?.getSharedPreferences(
                    Staticated.MY_PRESS_SHUFFLE,
                    Context.MODE_PRIVATE
                )?.edit()
                var editorLoop = Statified.myActivity?.getSharedPreferences(
                    Staticated.MY_PRESS_LOOP,
                    Context.MODE_PRIVATE
                )?.edit()

                if (Statified.currentSongHelper?.isLoop as Boolean) {
                    Statified.currentSongHelper?.isLoop = false
                    loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
                    editorLoop?.putBoolean("feature", false)
                    editorLoop?.apply()
                } else {
                    Statified.currentSongHelper?.isLoop = true
                    Statified.currentSongHelper?.isShuffle = false
                    loopButton?.setBackgroundResource(R.drawable.loop_icon)
                    shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                    editorShuffle?.putBoolean("feature", false)
                    editorShuffle?.apply()
                    editorLoop?.putBoolean("feature", true)
                    editorLoop?.apply()

                }
            }
            Statified.playPauseButton?.setOnClickListener {
                if (Statified.mediaPLayer?.isPlaying as Boolean) {
                    Statified.mediaPLayer?.pause()
                    Statified.currentSongHelper?.isPlaying = false
                    Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
                } else {
                    Statified.mediaPLayer?.start()
                    Statified.currentSongHelper?.isPlaying = true
                    Statified.playPauseButton?.setBackgroundResource(R.drawable.pause_icon)
                }
            }
        }
        Statified.mediaPLayer?.setOnCompletionListener {
            Staticated.onSongComplete()
        }
        clickHandler()
        var visualizationHandler = DbmHandler.Factory.newVisualizerHandler(context!!, 0)
        Statified.audioVisualization?.linkTo(visualizationHandler)

        var prefsForShuffle = Statified.myActivity?.getSharedPreferences(
            Staticated.MY_PRESS_SHUFFLE,
            Context.MODE_PRIVATE
        )
        var isShuffleAllowed = prefsForShuffle?.getBoolean("feature", false)
        if (isShuffleAllowed as Boolean) {
            Statified.currentSongHelper?.isShuffle = true
            Statified.currentSongHelper?.isLoop = false
            shuffleButton?.setBackgroundResource(R.drawable.shuffle_icon)
            loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
        } else {
            Statified.currentSongHelper?.isShuffle = false
            shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
        }

        var prefsForLoop = Statified.myActivity?.getSharedPreferences(
            Staticated.MY_PRESS_SHUFFLE,
            Context.MODE_PRIVATE
        )
        var isLoopAllowed = prefsForLoop?.getBoolean("feature", false)
        if (isLoopAllowed as Boolean) {
            Statified.currentSongHelper?.isShuffle = false
            Statified.currentSongHelper?.isLoop = true
            shuffleButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
            loopButton?.setBackgroundResource(R.drawable.loop_icon)
        } else {
            Statified.currentSongHelper?.isLoop = true
            loopButton?.setBackgroundResource(R.drawable.loop_white_icon)
        }
        if (Statified.favoriteContent?.checkIfIdExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.favorite_on
                )
            )
        } else {
            Statified.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.favorite_off
                )
            )
        }
    }

    fun playPrevious() {
        Statified.currentPosition = Statified.currentPosition - 1
        if (Statified.currentPosition == -1) {
            Statified.currentPosition = 0
        }
        if (Statified.currentSongHelper?.isPlaying as Boolean) {
            Statified.playPauseButton?.setBackgroundResource(R.drawable.pause_icon)

        } else {
            Statified.playPauseButton?.setBackgroundResource(R.drawable.play_icon)
        }
        Statified.currentSongHelper?.isLoop = false
        val nextSong = Statified.fetchSongs?.get(Statified.currentPosition)
        Statified.currentSongHelper?.songTitle = nextSong?.songTitle
        Statified.currentSongHelper?.songPath = nextSong?.songData
        Statified.currentSongHelper?.currentPosition = Statified.currentPosition
        Statified.currentSongHelper?.songId = nextSong?.songId as Long

        Staticated.updateTextViews(
            Statified.currentSongHelper?.songTitle as String,
            Statified.currentSongHelper?.songArtist as String
        )


        Statified.mediaPLayer?.reset()
        try {
            Statified.mediaPLayer?.setDataSource(
                context!!,
                Uri.parse(Statified.currentSongHelper?.songPath)
            )
            Statified.mediaPLayer?.prepare()
            Statified.mediaPLayer?.start()
            Staticated.processInformation(Statified.mediaPLayer as MediaPlayer)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (Statified.favoriteContent?.checkIfIdExists(Statified.currentSongHelper?.songId?.toInt() as Int) as Boolean) {
            Statified.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.favorite_on
                )
            )
        } else {
            Statified.fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    context!!,
                    R.drawable.favorite_off
                )
            )
        }
    }

    fun bindShakeListener() {
        Statified.mSensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }

            @SuppressLint("CommitPrefEdits")
            override fun onSensorChanged(p0: SensorEvent?) {
                val x = p0!!.values[0]
                val y = p0!!.values[1]
                val z = p0!!.values[2]
                Staticated.mAccelarationLast = Staticated.mAccelarationCurrent
                Staticated.mAccelarationCurrent =
                    Math.sqrt(((x * x + y * y + z * z).toDouble())).toFloat()
                val delta = Staticated.mAccelarationCurrent - Staticated.mAccelarationLast
                Staticated.mAccelaration = Staticated.mAccelaration * 0.9f + delta
                if (Staticated.mAccelaration > 12) {
                    val prefs =
                        context!!.getSharedPreferences(Statified.MY_PRESS, Context.MODE_PRIVATE)
                            ?.edit()
                    val isAllowed = prefs?.putBoolean("feature", false)//getboolean
                    if (isAllowed as Boolean) {
                        Staticated.playNext("PlayNextNormal")

                    }
                }
            }

        }
    }

}