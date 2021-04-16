package com.example.nacho;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.nacho.R.drawable.nosongs;

public class TemporaryAdapter extends RecyclerView.Adapter<TemporaryAdapter.TemporaryViewHolder> {

    private Context context;
    private ArrayList<SongFiles> mySongs;
    public static ArrayList<SongFiles> temporaryList;
    boolean checkedState[];
    CheckBox checkBoxs[];
    private int selectedPosition = -1;
    SparseBooleanArray checkboxStateArray;
    public static String string;
//    public static ArrayList<SelectedSongs> selectedSongsArrayList;


    public TemporaryAdapter(Context context, ArrayList<SongFiles> mySongs) {
        this.context = context;
        this.mySongs = mySongs;
        this.temporaryList=new ArrayList<>();
        checkedState=new boolean[mySongs.size()];
        checkBoxs=new CheckBox[mySongs.size()];
        checkboxStateArray=new SparseBooleanArray();
//        selectedSongsArrayList=new ArrayList<>();
    }



    @NonNull
    @Override
    public TemporaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.temporary_list_songs,parent,false);

        return new TemporaryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mySongs.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final TemporaryViewHolder holder, final int position) {
        String title = mySongs.get(position).getTitle();
//        if(title.length() >= 35){
//            title=title.substring(0,32);
//            title.concat("..");
//        }
        int bracketIndex = title.indexOf("(");
        if (bracketIndex != -1) {
            title = title.substring(0, bracketIndex);
        } else {
            int hyphenIndex = title.indexOf("-");
            if (hyphenIndex != -1) {
                title = title.substring(0, hyphenIndex);
            }
        }
        holder.songTitle.setText(title);
        String songArtist = mySongs.get(position).getArtist();

        String songArtistsArr[] = songArtist.split(",");
        holder.artist.setText(songArtistsArr[0]);
        String songDuration = mySongs.get(position).getDuration();
        songDuration = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(songDuration)),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(songDuration)) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(songDuration))));

        int colonIndex = songDuration.indexOf(":");
        if (colonIndex != -1) {
            String min = songDuration.substring(0, colonIndex);

            String sec = songDuration.substring(colonIndex + 1);
            if (sec.length() == 1) {
                songDuration = min + ":" + "0" + sec;
            }
        }

        holder.duration.setText(songDuration);
        byte[] image;
        image = getAlbumArt(mySongs.get(position).getPath());


        if (image != null) {
            Glide.with(context).asBitmap()
                    .load(image)
                    .into(holder.songImage);
        } else {
            Glide.with(context)
                    .load(nosongs)
                    .into(holder.songImage);
        }

//        selectedPosition = holder.getAdapterPosition();


//        holder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked=((CheckBox) view).isChecked();
//
//                switch (position){
//
//                }
//            }
//        });
//        holder.setIsRecyclable(false);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//        @Override
//            public void onClick(View view) {
//                boolean checked=((CheckBox) view).isChecked();
//                holder.checkBox.setChecked(selectedPosition == position);
//                pos=position;
//                Log.i("selected", String.valueOf(selectedPosition));
//                CheckBox cb=checkBoxs[position];
//                boolean checked=holder.checkBox.isChecked();
//                if (!checked) {

//                    checkedState[position]=true;
//                    for (int i = 0; i < mySongs.size(); i++) {
//                        if (i == position){
        if (!checkboxStateArray.get(position, false)) {
            holder.checkBox.setChecked(false);
        }
        else {
            holder.checkBox.setChecked(true);
        }

//                    }else{
//                            holder.checkBox.setChecked(false);
//                    }
//                }

//                    temporaryList.add(mySongs.get(position));
//                    Log.i("checked",mySongs.get(position).getTitle());
//                    notifyDataSetChanged();

//                }else {
//                    checkedState[position]=false;
//                    holder.checkBox.setChecked(false);
//                    temporaryList.remove(mySongs.get(position));
//                    Log.i("Unchecked",mySongs.get(position).getTitle());
//                }
//                holder.checkBox.setOnCheckedChangeListener(null);
//                notifyDataSetChanged();


//            }

//        });

//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked=((CheckBox) view).isChecked();
//
//                selectedPosition = holder.getAdapterPosition();
////                boolean checked=holder.checkBox.isChecked();
//                if (checked) {
////                    checkedState[position]=true;
////                    for (int i = 0; i < mySongs.size(); i++) {
////                        if (i == position){
//                            holder.checkBox.setChecked(true);
////                        }else{
////                            holder.checkBox.setChecked(false);
////                        }
////                    }
//                    temporaryList.add(mySongs.get(position));
//                    Log.i("checked",mySongs.get(position).getTitle());
//                }else {
////                    checkedState[position]=false;
//                    holder.checkBox.setChecked(false);
//                    temporaryList.remove(mySongs.get(position));
//                    Log.i("Unchecked",mySongs.get(position).getTitle());
//                }
////                holder.checkBox.setOnCheckedChangeListener(null);
////                notifyDataSetChanged();
//            }
//        });

//    }
    }
//    @Override
//    public void onViewRecycled(@NonNull TemporaryViewHolder holder) {
//        if (selectedPosition == pos) {
//            holder.checkBox.setSelected(true);
//            holder.checkBox.setChecked(true);
//        }else {
//            holder.checkBox.setChecked(false);
//        }
//        super.onViewRecycled(holder);
//    }

    public class TemporaryViewHolder extends RecyclerView.ViewHolder {

        ImageView songImage;
        TextView songTitle;
        TextView artist;
        TextView duration;
        CheckBox checkBox;

        public TemporaryViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.songImage);
            songTitle = itemView.findViewById(R.id.songTitle);
            artist = itemView.findViewById(R.id.artist);
            duration=itemView.findViewById(R.id.duration);
            checkBox=itemView.findViewById(R.id.checkBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    boolean checked=((CheckBox) view).isChecked();
//                    boolean checked=checkBox.isChecked();
//                    if(!checked){
                        if(!checkboxStateArray.get(getAdapterPosition(),false)){
                            checkBox.setChecked(true);
                            Log.i("checked",mySongs.get(getAdapterPosition()).getTitle());
//                            mySongs.get(getAdapterPosition()).setPosition(pos);
                            temporaryList.add(mySongs.get(getAdapterPosition()));
//                            pos++;
//                            SelectedSongs selectedSongs=new SelectedSongs(getAdapterPosition(),mySongs.get(getAdapterPosition()));
                            checkboxStateArray.put(getAdapterPosition(),true);
//                            selectedSongsArrayList.add(selectedSongs);
                            string=null;
                        }else {
                            Log.i("Unchecked",mySongs.get(getAdapterPosition()).getTitle());
                            checkBox.setChecked(false);
                            temporaryList.remove(mySongs.get(getAdapterPosition()));
                            checkboxStateArray.put(getAdapterPosition(),false);
//                            SelectedSongs selectedSongs=new SelectedSongs(getAdapterPosition(),mySongs.get(getAdapterPosition()));
//                            selectedSongsArrayList.remove(selectedSongs);
                            string=null;
                        }

                        Log.i("adapter", String.valueOf(getAdapterPosition()));
                    Log.i("size", String.valueOf(temporaryList.size()));
//                    }else {
//                        checkBox.setChecked(false);

                    }
//                }
            });
            Log.i("size", String.valueOf(temporaryList.size()));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    boolean checked=((CheckBox) view).isChecked();
//                    boolean checked=checkBox.isChecked();
//                    if(checked){
                        if(!checkboxStateArray.get(getAdapterPosition(),false)){
                            Log.i("checked",mySongs.get(getAdapterPosition()).getTitle());
                            checkBox.setChecked(true);
//                            mySongs.get(getAdapterPosition()).setPosition(pos);
                            temporaryList.add(mySongs.get(getAdapterPosition()));
//                            pos++;
                            checkboxStateArray.put(getAdapterPosition(),true);
//                            SelectedSongs selectedSongs=new SelectedSongs(getAdapterPosition(),mySongs.get(getAdapterPosition()));
//                            selectedSongsArrayList.add(selectedSongs);
                            string=null;
                        }else {
                            Log.i("unchecked",mySongs.get(getAdapterPosition()).getTitle());
                            checkBox.setChecked(false);
                            temporaryList.remove(mySongs.get(getAdapterPosition()));
                            checkboxStateArray.put(getAdapterPosition(),false);
//                            SelectedSongs selectedSongs=new SelectedSongs(getAdapterPosition(),mySongs.get(getAdapterPosition()));
//                            selectedSongsArrayList.remove(selectedSongs);
                            string=null;
                        }



//                    }else {
//                        checkBox.setChecked(false);
                    }
//                }
            });

        }

    }

    private byte[] getAlbumArt(String uri){
//        Bitmap bArt = null;
//        BitmapFactory.Options bfo=new BitmapFactory.Options();
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }



}
