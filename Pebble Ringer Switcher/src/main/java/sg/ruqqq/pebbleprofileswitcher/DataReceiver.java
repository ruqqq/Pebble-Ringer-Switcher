package sg.ruqqq.pebbleprofileswitcher;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.util.Log;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import static com.getpebble.android.kit.Constants.APP_UUID;

public class DataReceiver extends PebbleKit.PebbleDataReceiver {
    private static String TAG = "PebbleProfileSwitcher";
    private static java.util.UUID UUID = java.util.UUID.fromString("4f06c0d2-4758-4d5e-9da6-829c599b2219");

    private Uri mRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private MediaPlayer mMediaPlayer;
    private Vibrator mVibrator;

    public DataReceiver() {
        super(UUID);
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        final java.util.UUID receivedUuid = (java.util.UUID) intent.getSerializableExtra(APP_UUID);

        Log.d(TAG, "onReceive: " + receivedUuid);
        super.onReceive(context, intent);
    }

    @Override
    public void receiveData(Context context, int transactionId, PebbleDictionary data) {
        long CURRENT_ACTION = data.getInteger(0x0);
        long CURRENT_PROFILE = data.getInteger(0x1);

        Log.d(TAG, "receiveData: " + CURRENT_ACTION);
        PebbleKit.sendAckToPebble(context, transactionId);

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch ((int) CURRENT_ACTION) {
            case 0x2:
                am.setRingerMode((int) CURRENT_PROFILE);

                if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    try {
                        if (mMediaPlayer == null) {
                            mMediaPlayer = new MediaPlayer();
                            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                            mMediaPlayer.setDataSource(context, mRingtoneUri);
                        }

                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    if (mVibrator == null)
                        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    mVibrator.vibrate(new long[]{0, 500}, -1);
                }
                break;
        }

        data.addUint32(0x1, am.getRingerMode());
        PebbleKit.sendDataToPebble(context, UUID, data);
    }
}
