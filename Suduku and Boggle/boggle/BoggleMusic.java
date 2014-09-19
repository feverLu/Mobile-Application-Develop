package edu.neu.madcourse.binbinlu.boggle;


import java.util.TimerTask;

import edu.neu.madcourse.binbinlu.sudoku.Prefs;

import android.content.Context;
import android.media.MediaPlayer;

public class BoggleMusic {
   private static MediaPlayer mp = null;
   private static MediaPlayer nn = null;

   /** Stop old song and start new one */
   
   public static void play(Context context, int resource) {
      stop(context);

      // Start music only if not disabled in preferences
      if (Prefs.getMusic(context)) {
         mp = MediaPlayer.create(context, resource);
         mp.setLooping(false);
         mp.start();
//         nn = MediaPlayer.create(context, resource);
//         nn.setLooping(false);
//         nn.start();
      }
   }
   
   /** Stop the music */
   public static void stop(Context context) { 
      if (mp != null) {
         mp.stop();
         mp.release();
         mp = null;
      }
//      if (nn != null) {
//          nn.stop();
//          nn.release();
//          nn = null;
//       }
   }
}
