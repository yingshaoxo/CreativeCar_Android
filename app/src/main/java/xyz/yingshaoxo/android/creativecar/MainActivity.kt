package xyz.yingshaoxo.android.creativecar

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.*
import android.widget.Toast
import com.github.kittinunf.fuel.httpPost

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_drive.view.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import kotlinx.android.synthetic.main.fragment_ai.view.*
import kotlinx.android.synthetic.main.fragment_camera.*
import org.json.JSONObject


val HOST_address = "http://192.168.43.7"


fun POST(json: JSONObject, content: Context) {
    val text = json.toString()
    val request = (HOST_address + ":9958/car").httpPost().body(text).timeout(500)
    request.headers["Content-Type"] = "application/json"
    request.response { request, response, result ->
        result.fold(
                {
                    //success
                    Toast.makeText(content, "success:\n" + response.responseMessage, Toast.LENGTH_SHORT).show()
                },
                {
                    //fail
                    Toast.makeText(content, "failed:\n" + response.responseMessage, Toast.LENGTH_SHORT).show()
                }
        )
    }
}

fun POST(json: JSONObject) {
    val text = json.toString()
    val request = "http://192.168.43.7:9958/car".httpPost().body(text).timeout(500)
    request.headers["Content-Type"] = "application/json"
    request.response { request, response, result ->
    }
}


class MainActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return DriveFragment.newInstance(position + 1)
            }
            else if (position == 1) {
                return AIFragment.newInstance(position + 1)
            }
            else if (position == 2) {
                return CameraFragment.newInstance(position + 1)
            }

            return DriveFragment.newInstance(position + 1)
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }

    /**
     * A placeholder fragment containing a drive view.
     */
    class DriveFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_drive, container, false)

            fun post(action: String) {
                val json = JSONObject()
                json.put("action", action)
                json.put("speed", rootView.drive_seekBar_speed.progress.toString())

                POST(json)
            }

            fun up() {
                post("go")
            }

            fun down() {
                post("back")
            }

            fun left() {
                post("left")
            }

            fun right() {
                post("right")
            }

            fun stop() {
                post("stop")
            }

            rootView.drive_button_up.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            up()
                        }
                        MotionEvent.ACTION_UP -> {
                            stop()
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })


            rootView.drive_button_down.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            down()
                        }
                        MotionEvent.ACTION_UP -> {
                            stop()
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.drive_button_left.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            left()
                        }
                        MotionEvent.ACTION_UP -> {
                            stop()
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.drive_button_right.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            right()
                        }
                        MotionEvent.ACTION_UP -> {
                            stop()
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): DriveFragment {
                val fragment = DriveFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * A placeholder fragment containing a camera view.
     */
    class AIFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_ai, container, false)
            rootView.ai_section_label.text = getString(R.string.section_format, arguments.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): AIFragment {
                val fragment = AIFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    /**
     * A placeholder fragment containing a camera view.
     */
    class CameraFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_camera, container, false)

            fun post(action: String) {
                val json = JSONObject()
                json.put("camera_action", action)

                POST(json)
            }

            fun up() {
                post("up")
            }

            fun down() {
                post("down")
            }

            fun left() {
                post("left")
            }

            fun right() {
                post("right")
            }

            fun reset() {
                post("reset")
            }

            rootView.camera_button_up.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            up()
                        }
                        MotionEvent.ACTION_UP -> {
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.camera_button_left.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            left()
                        }
                        MotionEvent.ACTION_UP -> {
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.camera_button_right.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            right()
                        }
                        MotionEvent.ACTION_UP -> {
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.camera_button_reset.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            reset()
                        }
                        MotionEvent.ACTION_UP -> {
                        }
                    }
                    return v?.onTouchEvent(event) ?: true
                }
            })

            rootView.camera_webview.getSettings().setLoadWithOverviewMode(true)
            rootView.camera_webview.getSettings().setUseWideViewPort(true)
            rootView.camera_webview.loadUrl(HOST_address + ":8081" )

            return rootView
        }

        override fun onResume() {
            super.onResume()
            camera_webview.loadUrl(HOST_address + ":8081")
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): CameraFragment {
                val fragment = CameraFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

}
