package sainero.dani.appvote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView

class AdapterPollSingleChoice(val rowOptionsPoll: MutableList<PollOption>) : RecyclerView.Adapter<AdapterPollSingleChoice.ViewHolder>() {

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val option: RadioButton
        init {
            option = v.findViewById(R.id.rbOptionPoll)
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
        holder.option.text = p.texto
        holder.option.setOnCheckedChangeListener{ i, b ->
            p.isChecked = holder.option.isChecked
        }
    }

}