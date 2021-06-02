package com.woohyman.gameble_virtual_space.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.woohyman.gameble_virtual_space.PermissionProxy
import com.woohyman.gameble_virtual_space.R
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.RequestExecutor
import com.yanzhenjie.permission.runtime.Permission
import timber.log.Timber

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

        val keySource = DashboardFragment::class.java.simpleName
        val proxy = PermissionProxy.Builder()
            .setKeySource(keySource)
            .setRawRationale { context: Context?, data: List<String?>?, executor: RequestExecutor? ->
                Timber.tag(keySource).i("== RawRationale ==")
            }
            .setRawGranted { data: List<String?>? ->
                Timber.tag(keySource).i("== RawGranted ==")
            }
            .setRawDenied { permissions: List<String?>? ->
                Timber.tag(keySource).i("== RawDenied ==")
            }
            .create()

        AndPermission.with(this)
            .runtime()
            .permission(Permission.READ_EXTERNAL_STORAGE)
            .rationale(proxy.rationale)
            .onGranted(proxy.granted)
            .onDenied(proxy.denied)
            .start()

        return root
    }
}