package project.roy.socialmedia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Commentar;
import project.roy.socialmedia.data.model.Reminder;

public class CommentarAdapter extends RecyclerView.Adapter<CommentarAdapter.RoomChatViewHolder> {

    private Context context;
    private List<Commentar> commentarList;
    private OnDetailListener onDetailListener;

    public CommentarAdapter(Context context) {
        this.context = context;
    }

    public CommentarAdapter() {

    }
    public void setData(List<Commentar> commentarList){
        this.commentarList = commentarList;
        notifyDataSetChanged();
    }

    @Override
    public RoomChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_view_commentar, parent, false);
        return new RoomChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomChatViewHolder holder, int position) {
        holder.tvCommentarSender.setText(commentarList.get(position).getUser().getName());
        holder.tvCommentar.setText(commentarList.get(position).getTimelineCommentar());
        if(commentarList.get(position).getUser().getId()== SaveUserData.getInstance().getUser().getId()){
            holder.imgDeleteCommentar.setVisibility(View.VISIBLE);
        }else {
            holder.imgDeleteCommentar.setVisibility(View.GONE);
        }
        holder.imgDeleteCommentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailListener.onImgDeleteCommentarSelected(commentarList.get(position).getTimelineId());
            }
        });
    }

    public void setOnClickDetailListener(OnDetailListener onDetailListener){
        this.onDetailListener = onDetailListener;
    }

    @Override
    public boolean onFailedToRecycleView(RoomChatViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        if(commentarList == null) return 0;
        return commentarList.size();
    }

    public class RoomChatViewHolder extends RecyclerView.ViewHolder{
        TextView tvCommentarSender, tvCommentar;
        ImageView imgDeleteCommentar;


        public RoomChatViewHolder(final View itemView) {
            super(itemView);
            tvCommentar = itemView.findViewById(R.id.tv_commentar);
            tvCommentarSender = itemView.findViewById(R.id.tv_commentar_sender);
            imgDeleteCommentar = itemView.findViewById(R.id.img_delete_commentar);
        }
    }

    public interface OnDetailListener{
        void onItemDetailClicked(int tipsId);
        void onImgDeleteCommentarSelected(int timelineId);

    }
}
