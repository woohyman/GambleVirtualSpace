package com.example.ui_exmaple.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.ui_exmaple.R
import com.example.ui_exmaple.manager.heartBeat
import com.example.ui_exmaple.view.SegmentProgressBar

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var mTextHome:SegmentProgressBar? = null
    private var mRoot: View? = null
    private var mAutoRunnable:Runnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        mRoot = inflater.inflate(R.layout.fragment_home, container, false)
        mTextHome = mRoot?.findViewById(R.id.text_home)
        mAutoRunnable = Runnable {
            mTextHome?.setProgress(heartBeat.getInstance().progress)
            mTextHome?.postDelayed(mAutoRunnable,1000)
        }
        return mRoot
    }

    override fun onResume() {
        super.onResume()
        mTextHome?.setProgress(heartBeat.getInstance().progress)
        onStartRecycle()
    }

    override fun onPause() {
        super.onPause()
        stopRecycle()
    }

    private fun onStartRecycle(){
        mTextHome?.post(mAutoRunnable)
    }

    private fun stopRecycle(){
        mTextHome?.removeCallbacks(mAutoRunnable)
    }
}