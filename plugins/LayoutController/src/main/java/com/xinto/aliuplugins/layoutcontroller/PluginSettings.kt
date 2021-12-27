package com.xinto.aliuplugins.layoutcontroller

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aliucord.Utils
import com.aliucord.api.SettingsAPI
import com.aliucord.fragments.SettingsPage
import com.xinto.aliuplugins.layoutcontroller.util.patches
import com.xinto.aliuplugins.layoutcontroller.widgets.SwitchItem
import com.aliucord.utils.DimenUtils

class PluginSettings(
    private val settingsAPI: SettingsAPI
    ) : SettingsPage() {

    override fun onViewBound(view: View) {
        super.onViewBound(view)
        setActionBarTitle("LayoutController")
        val context = requireContext()

        Utils.threadPool.execute {
            val list = listOf(
                *patches.map {
                    SwitchItem(context, settingsAPI, it)
                }.sortedBy {
                    it.patch.description
                }.toTypedArray()
            )

            Utils.mainThread.post {
                val shape = ShapeDrawable(RectShape())
                    .apply {
                        setTint(Color.TRANSPARENT)
                        intrinsicHeight = DimenUtils.defaultPadding
                    }

                val decoration = DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(shape)
                }

                val recyclerView = RecyclerView(context)
                    .apply {
                        adapter = RecyclerAdapter(list)
                        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                        addItemDecoration(decoration)
                    }

                addView(recyclerView)
            }
        }
    }
}