package com.example.ui_exmaple.ui.notifications

import android.content.Context
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
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.RequestExecutor
import com.yanzhenjie.permission.runtime.Permission
import timber.log.Timber

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

        val keySource = NotificationsFragment::class.java.simpleName
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
            .permission(Permission.CALL_PHONE, Permission.SEND_SMS)
            .rationale(proxy.rationale)
            .onGranted(proxy.granted)
            .onDenied(proxy.denied)
            .start()

        return root
    }
}