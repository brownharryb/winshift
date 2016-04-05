package com.boma.winshift;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BOMA on 11/18/2015.
 */
public class MyImageHandler {
    Bitmap bitmap,  imageToBeLoadedInList;
    String nameOfImage, imgLocation;
    Uri.Builder builder;
    Context context;


    MyImageHandler(Context context,String imgLocation, String nameOfImage){
        this.imgLocation = imgLocation;
        this.nameOfImage = nameOfImage;
        this.context = context;

    }
    MyImageHandler(Context context,String imgLocation){
        this.imgLocation = imgLocation;
        this.context = context;

    }



    public void uploadToServer(){
        if(InternetHandler.internetIsAvailable(context)) {
            String link = AllStrings.PIC_LOCATION_PAGE_LINK;
                new MyImageUpload(nameOfImage, link).execute();
                MessageClass.log("link MyImageHandler -- "+link);

        }
    }


    public  Bitmap getThumbnail(String dataStr,int thumbnailSize) throws IOException{
        Uri uri = Uri.parse(dataStr);
        InputStream input = context.getContentResolver().openInputStream(uri);




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
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private  int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }


    class MyImageUpload extends AsyncTask<Void,Void,Void>{
        String nameOfImage, imageUploadLink;

        MyImageUpload(String nameOfImage, String link){
            this.nameOfImage = nameOfImage;
            this.imageUploadLink = link;
        }




        @Override
        protected Void doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            if(imgLocation==null){return null;}
            MessageClass.log("from myimagehandler imglocation is -- "+imgLocation);
            try {
                bitmap = getThumbnail(imgLocation,200);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                builder = new Uri.Builder()
                        .appendQueryParameter("name", nameOfImage)
                        .appendQueryParameter("image", encodedImage);
                String query = builder.build().getEncodedQuery();
                    uploadImg(query);
            } catch (IOException e) {
                e.printStackTrace();
                MessageClass.log("There's a problem on MyImageHandler -- "+e.getMessage());
            }
            finally {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        private void uploadImg(String query)throws IOException{
            HttpURLConnection conn = null;
            InputStream is = null;
            OutputStream os = null;
            try {
                URL url = new URL(imageUploadLink);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                conn.connect();
                String responseString = "";


                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                   responseString = InternetHandler.getStringFromInputStream(is);
                }

                MessageClass.log("Response message from Myimagehandler is -- "+responseString);
            }
            finally {
                if(conn!=null)conn.disconnect();
                if(is!=null){is.close();}
                if(os!=null){os.close();}


            }

        }


    }




}
