package com.example.ui_exmaple.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ui_exmaple.PermissionProxy
import com.example.ui_exmaple.R
import com.example.ui_exmaple.ui.home.HomeFragment
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        PermissionProxy.getRequestProxy(
            AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE)
        )
            .setKeySource(HomeFragment::class.java.simpleName)
            .onGranted { data: List<String?>? -> }
            .onDenied { permissions: List<String?>? -> }
            .start()

        return root
    }
}