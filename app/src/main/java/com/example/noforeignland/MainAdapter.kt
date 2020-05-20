package com.example.noforeignland

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.fragment_feature.view.*
import java.util.*
import kotlin.collections.ArrayList


class MainAdapter(val featureList: ArrayList<Feature>): Adapter<ViewHolder>(), Filterable {

    class FeatureHolder(itemView: View): ViewHolder(itemView)

    private var modifiedFeatureList = ArrayList<Feature>()

    init {
        this.modifiedFeatureList = featureList;
    }

    override fun getItemCount(): Int {
        return this.modifiedFeatureList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.fragment_feature, parent, false)
        return  FeatureHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature: Feature = this.modifiedFeatureList[position]

        holder.itemView.featureName.text = feature.properties.name

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, InfoActivity::class.java)
            intent.putExtra("featureId", feature.properties.id)
            holder.itemView.context.startActivity(intent)
        }

        holder.itemView.mapImageButton.setOnClickListener{
            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
            intent.putExtra("featureName", feature.properties.name)
            intent.putExtra("featureLat", feature.geometry.coordinates[0])
            intent.putExtra("featureLon", feature.geometry.coordinates[1])
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    modifiedFeatureList = featureList
                } else {
                    val resultList = ArrayList<Feature>()
                    for (feature in featureList) {
                        if (feature.properties.name.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(feature)
                        }
                    }
                    modifiedFeatureList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = modifiedFeatureList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                modifiedFeatureList = results?.values as ArrayList<Feature>
                notifyDataSetChanged()
            }
        }
    }
}