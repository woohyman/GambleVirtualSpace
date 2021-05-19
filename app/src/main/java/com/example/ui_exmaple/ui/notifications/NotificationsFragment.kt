package com.example.ui_exmaple.ui.notifications

import android.os.Bundle
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

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        PermissionProxy.getRequestProxy(
            AndPermission.with(this)
                .runtime()
                .permission(Permission.CALL_PHONE, Permission.SEND_SMS)
        )
            .setKeySource(HomeFragment::class.java.simpleName)
            .onGranted { data: List<String?>? -> }
            .onDenied { permissions: List<String?>? -> }
            .start()

        return root
    }
}