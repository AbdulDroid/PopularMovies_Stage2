package com.kafilicious.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kafilicious.popularmovies.models.VideoResults;
import com.kafilicious.popularmovies.R;
import com.kafilicious.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*
 * Copyright (C) 2017 Popular Movies, Stage 2
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    List<VideoResults>video_results;
    private Context context;

    public VideoAdapter(Context context, List<VideoResults> results) {
        this.context = context;
        this.video_results = results;
    }

    public void setData(List<VideoResults> videoData){
        this.video_results = videoData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.video_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String site = video_results.get(position).site;

        holder.videoName.setText(video_results.get(position).name);
        holder.videoType.setText(video_results.get(position).type);

        if (site.equals("YouTube")) {

            String url = NetworkUtils.buildYoutubeImageUrl(video_results.get(position).key).toString();
            holder.playImageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(url)
                    .into(holder.youtubeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (video_results == null) return 0;
        return video_results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView youtubeImageView;
        TextView videoType, videoName;
        CircleImageView playImageView;

        public ViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            youtubeImageView = (ImageView) view.findViewById(R.id.youtube_iv);
            videoName = (TextView) view.findViewById(R.id.video_name);
            videoType = (TextView) view.findViewById(R.id.video_type);
            playImageView = (CircleImageView) view.findViewById(R.id.circle_play);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            URL url = NetworkUtils.buildYoutubeVideoUrl(video_results.get(position).key);
            if (url != null){
                Uri uri = Uri.parse(url.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                if (intent.resolveActivity(context.getPackageManager()) != null){
                    context.startActivity(intent);
                }else {
                    Log.i("Results", "Could not call" + uri.toString() + ", no receiving app installed on your device!");
                }
            }

        }
    }


}


