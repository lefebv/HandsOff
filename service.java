package fr.lefebvre.handsoff;

import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class service extends Service {
	private int notif_id = 42;
	private SensorManager mSensorManager;
	private AudioManager mAudioManager;
	private MediaPlayer player = new MediaPlayer();
	private int initialVol;
	private CountDownTimer timer;
	  private float mAccel; // acceleration apart from gravity
	  private float mAccelCurrent; // current acceleration including gravity
	  private float mAccelLast; // last acceleration including gravity
	  Boolean sensorActivated  = false;

	  private final SensorEventListener mSensorListener = new SensorEventListener() {

		    public void onSensorChanged(SensorEvent se) {
		        float x = se.values[0];
		        float y = se.values[1];
		        float z = se.values[2];
		        mAccelLast = mAccelCurrent;
		        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
		        float delta = mAccelCurrent - mAccelLast;
		        mAccel = mAccel * 0.9f + delta;
		        if(mAccel > 2){ 
		        	if (!sensorActivated)
		        	{
		        	Toast.makeText(getApplicationContext(),"Accelerometre triggered", Toast.LENGTH_SHORT).show();
		        	sensorActivated = true;
		        	timer = new CountDownTimer(10000, 1000) {
		        		public void onTick(long milli) {
		        		}
		   		     public void onFinish() {
			        	try {
			        		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
							AssetFileDescriptor afd = getAssets().openFd("Alarm.mp3");
							player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
							player.prepare();
							player.setLooping(true);
							player.start();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		        	}.start();
		        	}
		        }
		    }

		      public void onAccuracyChanged(Sensor sensor, int accuracy) {
		      }
		    };

		    
    public static boolean isServiceRunning(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo svr : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.class.getName().equals(svr.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
       
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        initialVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    mAccel = 0.00f;
	    mAccelCurrent = SensorManager.GRAVITY_EARTH;
	    mAccelLast = SensorManager.GRAVITY_EARTH;
		createNotification();
	}

	private void	createNotification()
	{
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("Hands Off")
		        .setContentText("Deactivate alarm");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, Unlock.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
																	| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Alarm.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_CANCEL_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		Notification notif = mBuilder.build();
		notif.flags |= Notification.FLAG_ONGOING_EVENT;
		mNotificationManager.notify(notif_id, notif);		
	}
    
      
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
    	super.onDestroy();
    	player.stop();
    	if (timer != null)
    		timer.cancel();
    	mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, initialVol, 0);
    	NotificationManager mNotificationManager =
    		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancel(notif_id);
    }
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
