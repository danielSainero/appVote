package sainero.dani.appvote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterPollSingleChoice(val rowOptionsPoll: MutableList<PollOption>) : RecyclerView.Adapter<AdapterPollSingleChoice.ViewHolder>() {

    var allImgArray: MutableList<ImageView> = mutableListOf()

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val textOption: TextView
        val imageOptionTick : ImageView
        init {
            textOption = v.findViewById(R.id.optionName)
            imageOptionTick = v.findViewById(R.id.imageOption)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPollSingleChoice.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_poll_single_choice,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return rowOptionsPoll.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = rowOptionsPoll[position]
        allImgArray.add(holder.imageOptionTick)

        holder.textOption.text = p.texto
        holder.imageOptionTick.visibility = View.GONE
        holder.textOption.setOnClickListener{
            for (i in rowOptionsPoll) {
                i.isChecked = false
            }
            for (i in allImgArray) {
                i.visibility = View.GONE
            }
            holder.imageOptionTick.visibility = View.VISIBLE
            p.isChecked = true

        }
    }

}