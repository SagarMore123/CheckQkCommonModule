package com.astrika.checqk.discount.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.astrika.checqk.commonmodules.model.SystemValueMasterDTO
import com.astrika.checqk.discount.databinding.MembershipTypeItemCellLayoutBinding

class MembershipTypeAdapter(
    private var mActivity: Activity,
    private var clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MembershipTypeAdapter.ViewHolder>() {

    private var arrayList = listOf<SystemValueMasterDTO>()

    fun submitList(updatedList: List<SystemValueMasterDTO>) {
        val oldList = arrayList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ItemDiffCallback(oldList, updatedList)
        )
        arrayList = updatedList
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class ItemDiffCallback(
        private var oldList: List<SystemValueMasterDTO>,
        private var newList: List<SystemValueMasterDTO>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].serialId == newList[oldItemPosition].serialId)
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldList[oldItemPosition].equals(newList[oldItemPosition]))
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = arrayList[position]
        holder.bind(mActivity, position, item, clickListener)
    }

    class ViewHolder(private val binding: MembershipTypeItemCellLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            activity: Activity,
            position: Int,
            dto: SystemValueMasterDTO,
            itemClick: OnItemClickListener
        ) {

            binding.text = dto.value ?: ""
            binding.isSelected = dto.selected

            binding.radioBtn.setOnClickListener {
                dto.selected = true
                itemClick.onItemClick(position, dto)
            }

        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    MembershipTypeItemCellLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(
            position: Int,
            systemValueMasterDTO: SystemValueMasterDTO
        )
    }


}