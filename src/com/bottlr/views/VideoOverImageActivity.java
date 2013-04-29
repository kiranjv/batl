package com.bottlr.views;

import com.bottlr.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.EditText;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.View;
import android.content.res.Resources;
import android.widget.ImageView;



public class VideoOverImageActivity extends Activity 
{



public VideoOverImageActivity() 
{

}


public void onCreate(Bundle icicle) 
{

super.onCreate(icicle);
setContentView(R.layout.videoviewdemo_layout);
final EditText edittext = (EditText) findViewById(R.id.edittext);

edittext.setOnKeyListener(new OnKeyListener() 
{
public boolean onKey(View v, int keyCode, KeyEvent event) 
{
// If the event is a key-down event on the "enter" button
if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
// Perform action on key press

int aInt=0;
try
{
aInt = Integer.parseInt(edittext.getText().toString());
}
catch (NumberFormatException e)
{
Log.e("debug","Error finding Image: "+e.getMessage());

}

VideoView videoHolder = (VideoView) findViewById(R.id.surface_view);
MediaController mc=new MediaController(VideoOverImageActivity.this);
Boolean returnValue=true;

switch (aInt) 
{

case 1:


startPlaying(videoHolder,mc,"file:///sdcard/video1.mp4",0);
break;

case 2:
startPlaying(videoHolder,mc,"file:///sdcard/video2.mp4",1);
break;

default: 
returnValue=false; 
break;

}


return returnValue;


}

return false;
}
});

}

public void startPlaying(VideoView videoHolder,MediaController mc,String nameVideo, int song)
{

Resources res = VideoOverImageActivity.this.getResources();


ImageView image = (ImageView) findViewById(R.id.overlayImage);

try
{
R.drawable.class.getField("b" + song).getInt(0);
image.setImageDrawable(res.getDrawable(R.drawable.class.getField("b" + song).getInt(0)));
image.getDrawable().setAlpha(55);
}
catch (Exception e)
{
Log.e("debug","Error finding Image: "+e.getMessage());

}

videoHolder.setMediaController(mc);
videoHolder.setVideoURI(Uri.parse(nameVideo));
videoHolder.requestFocus();
videoHolder.start();

videoHolder.setOnCompletionListener(new OnCompletionListener()
{ 
public void onCompletion(MediaPlayer arg0) 
{ 
try
{
Log.e("debug","MediaPlayer seek to 0...");
arg0.seekTo(0);
Log.e("debug","MediaPlayer start...");
arg0.start();
Log.e("debug","MediaPlayer started");
}

catch(Exception ex)
{
Log.e("debug","MediaPlayer error: "+ex.toString());
}
} 
});



}


}