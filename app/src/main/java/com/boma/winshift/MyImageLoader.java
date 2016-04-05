package com.boma.winshift;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BOMA on 11/26/2015.
 */
public class MyImageLoader {




    static void setImageOnViewFromNet(Context context,String dataStr,int thumbnailSize,ImageView imageView){
        new SetImageFromServer(context,dataStr,thumbnailSize,imageView).execute();
    }

    static InputStream getImageInputStreamFromNet(String imageDownloadLink) throws IOException{

        URL url = new URL(imageDownloadLink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
       return conn.getInputStream();

    }



    static public  Bitmap getThumbnail(Context context,String dataStr,int thumbnailSize) throws IOException{
        InputStream input = getImageInputStreamFromNet(dataStr);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > thumbnailSize) ? (originalSize / thumbnailSize) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither=true;//optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        input = getImageInputStreamFromNet(dataStr);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }
   static private  int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }




    static class SetImageFromServer extends AsyncTask<Void,Void,Bitmap> {
        String dataStrng;
        int thumbnailSize;
        ImageView imageView;
        Context context;

        SetImageFromServer(Context context,String dataStr,int thumbnailSize,ImageView imageView){
            this.dataStrng = dataStr;
            this.thumbnailSize = thumbnailSize;
            this.imageView = imageView;
            this.context = context;

        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            MessageClass.log("Confirmed setimagefromserver.doinbackground was called from myimageloader ");
            Bitmap b = null;

            try {
                b = getThumbnail(context,dataStrng,thumbnailSize);
                MessageClass.log("No Problem from myIMageloader bitmap str is -- "+b.toString());

            } catch (IOException e) {
                e.printStackTrace();
                MessageClass.log("Problem from myIMageloader bitmap str is -- " + b.toString());
            }
            if (b==null){
                b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bh);
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
