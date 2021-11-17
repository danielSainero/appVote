package sainero.dani.appvote

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class AdaptadorPoll(val rowOptionsPoll: MutableList<PollOption>) : RecyclerView.Adapter<AdaptadorPoll.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val option: CheckBox
        init {
            option = v.findViewById(R.id.cbOptionPoll)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorPoll.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_poll,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = rowOptionsPoll[position]
        holder.option.text = p.texto
        holder.option.setOnCheckedChangeListener{ i, b ->
            p.isChecked = holder.option.isChecked
        }
    }

    override fun getItemCount(): Int {
        return rowOptionsPoll.size
    }

}