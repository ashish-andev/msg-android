package io.msgapp.android;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.msgapp.android.model.ChatPreview;

/**
 * Created by matheus on 11/15/15.
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    private List<ChatPreview> previews;
    private static ClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView avatar;
        public TextView userName;
        public TextView messagePreview;
        public TextView messageWhen;
        public ImageView messageStatus;
        public Context context;


        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            this.avatar = (CircleImageView) v.findViewById(R.id.avatar_chat_preview);
            this.userName = (TextView) v.findViewById(R.id.user_name_chat_preview);
            this.messagePreview = (TextView) v.findViewById(R.id.last_message_preview);
            this.messageWhen = (TextView) v.findViewById(R.id.last_message_when);
            this.messageStatus = (ImageView) v.findViewById(R.id.last_message_status);
            this.context = v.getContext();

            // This needs to be called on every new ViewHolder because the context is different
            // on each one and the tint is context dependant.
            // TODO: check if this is REALLY context dependant.
            Util.tint(this.context, R.color.textSecondary, R.drawable.ic_clock_grey,
                    R.drawable.ic_done_all_grey, R.drawable.ic_done_grey, R.drawable.ic_error_grey);
            Util.tint(this.context, R.color.green, R.drawable.ic_done_all_green);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition(), v);
            }
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View view);
    }

    public ChatPreview getItem(int position) {
        return this.previews.get(position);
    }

    public void setClickListener(ClickListener clickListener) {
        MainRecyclerViewAdapter.clickListener = clickListener;
    }

    public MainRecyclerViewAdapter(List<ChatPreview> previews) {
        this.previews = previews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return new ViewHolder(LayoutInflater
                .from(parent.getContext()).inflate(R.layout.chat_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatPreview preview = this.previews.get(position);
        // TODO Picasso.with(holder.context).load(preview.avatarUrl...
        holder.userName.setText(preview.getUserName());
        holder.messagePreview.setText(preview.getMessagePreview());
        holder.messageWhen.setText(preview.getMessageWhen());


        switch (preview.getStatus()) {
            case ChatPreview.STATUS_LOCAL:
                holder.messageStatus.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_error_grey));
                break;

            case ChatPreview.STATUS_SENT:
                holder.messageStatus.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_done_grey));
                break;

            case ChatPreview.STATUS_DELIVERED:
                holder.messageStatus.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_done_all_grey));
                break;

            case ChatPreview.STATUS_READ:
                holder.messageStatus.setImageDrawable(holder.context.getResources().getDrawable(R.drawable.ic_done_all_green));
                break;
            default:
                holder.messageStatus.setImageResource(android.R.color.transparent);
        }
    }

    @Override
    public int getItemCount() {
        return this.previews.size();
    }
}
