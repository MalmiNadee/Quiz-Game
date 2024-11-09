package com.example.quizgame

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizgame.databinding.ItemBinding

class QuizListAdapter(private val quizModelList: List<QuizModel>) :
    RecyclerView.Adapter<QuizListAdapter.MyViewHolder>() {

    class MyViewHolder(private val binding: ItemBinding ):RecyclerView.ViewHolder(binding.root){

        @SuppressLint("SetTextI18n")
        fun bind(model : QuizModel){
            //bind all the views
            binding.apply {
                title.text = model.title
                subtitle.text = model.subtitle
                time.text = model.time + "min"
                root.setOnClickListener{
                    val intent = Intent(root.context, QuestionActivity::class.java)
                    //assign quiz to related button
                    QuestionActivity.questionModelList = model.questionList
                    QuestionActivity.time = model.time
                    root.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return quizModelList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(quizModelList[position])
    }
}