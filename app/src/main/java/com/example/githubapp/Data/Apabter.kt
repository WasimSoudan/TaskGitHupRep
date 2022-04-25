package com.example.githubapp.Data

import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.githubapp.R


class GitHubAdapter: RecyclerView.Adapter<GitHubAdapter.MyViewHolder> {
    protected lateinit var ct: Context
    protected lateinit var listOfRep: MutableList<GitHub>

    constructor(ct: Context, listOfRep: MutableList<GitHub>) {
        this.ct = ct
        this.listOfRep = listOfRep
    }

    constructor() : super()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.rep_img)
        val repName: TextView = itemView.findViewById(R.id.repName)
        val repDes: TextView = itemView.findViewById(R.id.repDes)
        val repStar: TextView = itemView.findViewById(R.id.repStar)
        val repIssues: TextView = itemView.findViewById(R.id.repIssues)
        val repTimeandOwner: TextView = itemView.findViewById(R.id.repTimeandOwner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubAdapter.MyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(ct)
        var view = inflater.inflate(R.layout.data, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            holder.repName.text = listOfRep[position].Rep_name()
            holder.repDes.text = listOfRep[position].Rep_des()
            holder.repStar.text = listOfRep[position].Rep_stars()
            holder.repIssues.text = listOfRep[position].Rep_Issues()
            Glide.with(ct)
                .load(listOfRep[position].Owner_img())
                .override(60, 60)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView)
            holder.repTimeandOwner.text = listOfRep[position].TimeandOwner()
            holder.imageView.setOnClickListener {
                val Intent : Intent = Intent(ct,webapp::class.java)
                Intent.putExtra("url",listOfRep[position].getUrl())
                ct.startActivity(Intent)
            }
        } catch (ex: Exception) {
            Toast.makeText(ct, ex.toString(), Toast.LENGTH_LONG).show()
        }

    }
    fun SerchFilterList(listOfRep: MutableList<GitHub>){
        this.listOfRep=listOfRep
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listOfRep.size
    }
}
