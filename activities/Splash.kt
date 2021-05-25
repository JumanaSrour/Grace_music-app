package iug.jumana.grace.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import iug.jumana.grace.R
import java.util.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(object: TimerTask(){
            override fun run() {
                val intent= Intent(this@Splash, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },1200L)
    }
}
