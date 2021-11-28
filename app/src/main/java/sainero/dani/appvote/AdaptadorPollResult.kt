package sainero.dani.appvote

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorPollResult (val rowResultsPoll: MutableList<DataSetpollResult>) : RecyclerView.Adapter<AdaptadorPollResult.ViewHolder>()  {

    class ViewHolder(vR: View): RecyclerView.ViewHolder(vR) {
        val text: TextView
        val selected: TextView
        init {
            text = vR.findViewById(R.id.pollRowResultOption)
            selected = vR.findViewById(R.id.pollRowResultNumber)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdaptadorPollResult.ViewHolder {
        val vR = LayoutInflater.from(parent.context).inflate(R.layout.poll_row_result,parent,false)
        return ViewHolder(vR)
    }
    override fun onBindViewHolder(holder: AdaptadorPollResult.ViewHolder, position: Int) {
        val p = rowResultsPoll[position]
        holder.text.text = p.option
        holder.selected.text = p.numSelected.toString()
    }

    override fun getItemCount(): Int {
        return rowResultsPoll.size
    }




}