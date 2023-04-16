package org.techtowm.retrofit2.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtowm.retrofit2.Data.UserDTO;
import org.techtowm.retrofit2.databinding.Usertab2RecyclerItemBinding;

import java.util.List;

public class UTF2RecyclerViewAdapter extends RecyclerView.Adapter<UTF2RecyclerViewAdapter.ViewHolder> {
    private final List<UserDTO> mData;

    public UTF2RecyclerViewAdapter(List<UserDTO> data) {
        this.mData = data;
    }

    @NonNull
    @Override
    public UTF2RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Usertab2RecyclerItemBinding binding = Usertab2RecyclerItemBinding
                .inflate((LayoutInflater.from(parent.getContext())), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UTF2RecyclerViewAdapter.ViewHolder holder, int position) {
        UserDTO userDTO = mData.get(position);
        holder.tvDate.setText(String.valueOf(userDTO.getDate()));
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvId.setText(String.valueOf(userDTO.getID()));
        holder.tvTime.setText(getTimeFormat(userDTO.getTime()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final Usertab2RecyclerItemBinding binding;
        public TextView tvRank;
        public TextView tvDate;
        public TextView tvId;
        public TextView tvTime;

        public ViewHolder(Usertab2RecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            tvRank = binding.tvRank;
            tvDate = binding.tvDate;
            tvId = binding.tvId;
            tvTime = binding.tvTime;
        }
    }

    public String getTimeFormat(Integer time) {
        String min = String.valueOf(time/60);
        String sec = String.valueOf(time%60);

        String out_time = min + " : " + sec ;
        return out_time;
    }
}
