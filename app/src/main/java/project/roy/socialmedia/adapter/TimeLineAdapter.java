package project.roy.socialmedia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import project.roy.socialmedia.R;
import project.roy.socialmedia.data.local.SaveUserData;
import project.roy.socialmedia.data.model.Media;
import project.roy.socialmedia.data.model.Reminder;
import project.roy.socialmedia.data.model.Timeline;
import project.roy.socialmedia.data.model.User;
import project.roy.socialmedia.util.Constant;
import project.roy.socialmedia.util.FormatterUtil;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.RoomChatViewHolder> {

    private Context context;
    private List<Timeline> timelineList;
    private OnDetailListener onDetailListener;

    public TimeLineAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Timeline> timelineList){
        this.timelineList = timelineList;
        notifyDataSetChanged();
    }

    @Override
    public TimeLineAdapter.RoomChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item_list_view, parent, false);
        return new TimeLineAdapter.RoomChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomChatViewHolder holder, int position) {
        if (timelineList.get(position).getUser().getPicture() !=null){
            Picasso.get().load(Constant.BASE_URL+ timelineList.get(position).getUser().getPicture())
                    .into(holder.authorImageView);
        }
        else {
            Picasso.get().load(R.drawable.ic_stub).into(holder.authorImageView);
        }
        holder.titleTextView.setText("Dipost oleh " + timelineList.get(position).getUser().getName());
        holder.detailsTextView.setText(timelineList.get(position).getTimelineMoment());
        if (!timelineList.get(position).getMedia().isEmpty()){
            Picasso.get().load(Constant.BASE_URL+ timelineList.get(position).getMedia().get(0).getFilename())
                    .into(holder.postImageView);
        }
        else {
            Picasso.get().load(R.drawable.ic_stub).into(holder.postImageView);
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date d = f.parse(timelineList.get(position).getCreatedAt());
            long milliseconds = d.getTime()+25200000;
            CharSequence date = FormatterUtil.getRelativeTimeSpanStringShort(context,milliseconds);
            holder.dateTextView.setText(date);
            System.out.println("datee : " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> onDetailListener.onItemClick(timelineList.get(position),
                timelineList.get(position).getMedia(),
                timelineList.get(position).getUser(),
                timelineList.get(position).getUser().getPicture()));

        holder.authorImageView.setOnClickListener(v -> onDetailListener.onAuthorClick(timelineList.get(position).getUser().getId()));
        if(timelineList.get(position).getUser().getId()== SaveUserData.getInstance().getUser().getId()){
            holder.imgDeleteTimeline.setVisibility(View.VISIBLE);
        }else {
            holder.imgDeleteTimeline.setVisibility(View.GONE);
        }
        holder.imgDeleteTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailListener.onDeleteTimeline(timelineList.get(position).getId());
            }
        });

        holder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDetailListener.onNameSelected(timelineList.get(position).getUser());
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
        if(timelineList == null) return 0;
        return timelineList.size();
    }

    public class RoomChatViewHolder extends RecyclerView.ViewHolder{

        private ViewGroup likeViewGroup;
        private TextView likeCounterTextView, commentsCountTextView, watcherCounterTextView, dateTextView, titleTextView, detailsTextView;
        private ImageView postImageView, likesImageView, authorImageView, imgDeleteTimeline;


        public RoomChatViewHolder(final View itemView) {
            super(itemView);
            postImageView = itemView.findViewById(R.id.postImageView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
            authorImageView = itemView.findViewById(R.id.authorImageView);
            imgDeleteTimeline = itemView.findViewById(R.id.img_delete_timeline);
        }
    }

    public interface OnDetailListener{
        void onItemClick(Timeline timeline, List<Media> mediaList, User user, String urlPicture);
//
//        void onLikeClick(LikeController likeController, int position);

        void onAuthorClick(int idUser);

        void onDeleteTimeline(int timelineId);

        void onNameSelected(User user);
    }
}
