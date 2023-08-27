package com.example.myapplication.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.database.entity.MovieEntity

class MainAdapter : ListAdapter<MovieEntity, MainViewHolder>(MainAdapter) {

    private var listener: ((MovieEntity) -> Unit)? = null
    private var longPressListener: ((MovieEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position), listener, longPressListener)
    }

    private companion object : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }

    fun setListener(listener: ((MovieEntity) -> Unit)){
        this.listener = listener
    }

    fun setLongListener(listener: ((MovieEntity) -> Unit)){
        this.longPressListener = listener
    }

}

class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private var movieName: AppCompatTextView
    private var movieStatus: AppCompatTextView

    init {
        movieName = view.findViewById(R.id.nome_filme)
        movieStatus = view.findViewById(R.id.status_filme)
    }

    fun bind(movieEntity: MovieEntity, listener: ((MovieEntity) -> Unit)?, longListener: ((MovieEntity) -> Unit)?) {
        movieName.text = movieEntity.name
        movieStatus.text =
            if (movieEntity.isRented)
                itemView.context.getString(R.string.status_filme_alugado)
            else
                itemView.context.getString(R.string.status_filme_nao_alugado)
        itemView.setOnClickListener {
            listener?.invoke(movieEntity)
        }
        itemView.setOnLongClickListener {
            longListener?.invoke(movieEntity)
            true
        }
    }
}