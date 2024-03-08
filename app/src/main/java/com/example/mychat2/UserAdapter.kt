package com.example.mychat2

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat2.databinding.UserListItemBinding

class UserAdapter : ListAdapter<User,UserAdapter.ItemHolder>(ItemComparator()){

    class ItemHolder(private  val binding:UserListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(user: User)= with(binding){ //передаем суда пользователя  и получаем доступ к разметке
      message.text=user.message
      userName.text=user.name
        }
        //компаньен длязаполнения ItemHolder и создания его
        companion object {
            fun create(parent: ViewGroup):ItemHolder{//возвращает ItemHolder
       return ItemHolder(UserListItemBinding
           .inflate(LayoutInflater.from(parent.context),parent,false))
        }
        }
    }
    class ItemComparator: DiffUtil.ItemCallback<User>(){
        //сравнивает между собой item между старым и новым
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return  ItemHolder.create(parent)//возвращает инстанцию класса ItemHolder
    }// in parent приходит контекст

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        //происходит заполнение=рисовка каждого элемента
        holder.bind(getItem(position))
    }
}