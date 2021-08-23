package com.aliucord.plugins.layoutcontroller

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.aliucord.plugins.layoutcontroller.widgets.SwitchItem

class RecyclerAdapter(
    private val switches: List<SwitchItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(LinearLayout(parent.context))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder.itemView as LinearLayout).addView(switches[position])
    }

    override fun getItemCount(): Int {
        return switches.size
    }

}