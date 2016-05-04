package com.hbs.hashbrownsys.locallinkers.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbs.hashbrownsys.locallinkers.Change_Password;
import com.hbs.hashbrownsys.locallinkers.Constants;
import com.hbs.hashbrownsys.locallinkers.Home;
import com.hbs.hashbrownsys.locallinkers.R;
import com.hbs.hashbrownsys.locallinkers.http.CommonPostRequestThread;
import com.hbs.hashbrownsys.locallinkers.http.IHttpExceptionListener;
import com.hbs.hashbrownsys.locallinkers.http.IHttpResponseListener;
import com.hbs.hashbrownsys.locallinkers.http.Utilities;
import com.hs.image.ImageIntentHandler;
import com.hs.image.ImageUtils;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private static String Final_image;
    TextView txt_add_new_add;
    Typeface Font, Font1;
    Button btn_profile, btn_password, btn_update;
    TextView txt_fill_info;
    EditText ed_Name, ed_mail, ed_phone_no, ed_add, ed_City;
    CircleImageView profile_imageView;
    final static int CAMERADATA = 0;
    final static int RESULT_LOAD_IMAGE = 1;
    String mselectedImagePath, mfilenameSel;
    Bitmap bitmap;
    String image_path1;
    Uri fileUri;
    TextView txt_contact_info;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "MYPACK";
    String first_name, email, phone, address, city;
    ProgressDialog progressDialog;
    public final String tag = this.getClass().getSimpleName();
    int user_Id;
    int user_id;
    String profile_pic_url = "";
    SharedPreferences prefs;
    LinearLayout linear_layout, main_layout;
    String Email, Phone, UserName, Image, Address, City;
    ImageView filter_image, location_image, search_icon;
    Button btn_add, btn_cancle;
    SearchView search;

    ImageIntentHandler.ImagePair mImagePair;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_profile, container, false);

        Font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");
        Font1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/MyriadPro-Regular.otf");

        search = (SearchView) Home.topToolBar.findViewById(R.id.search);
        search.setVisibility(View.GONE);
        search_icon = (ImageView) Home.topToolBar.findViewById(R.id.search_icon);
        search_icon.setVisibility(View.GONE);
        location_image = (ImageView) Home.topToolBar.findViewById(R.id.location_image);
        location_image.setVisibility(View.GONE);
        filter_image = (ImageView) Home.topToolBar.findViewById(R.id.filter_image);
        filter_image.setVisibility(View.GONE);
        location_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linear_layout = (LinearLayout) root_view.findViewById(R.id.linear_layout);
        btn_add = (Button) root_view.findViewById(R.id.btn_add);
        btn_add.setTypeface(Font);
        btn_cancle = (Button) root_view.findViewById(R.id.btn_cancle);
        btn_cancle.setTypeface(Font);

        prefs = getActivity().getSharedPreferences(Constants.LOCAL_LINKER_APP_PREFERENCES, Context.MODE_PRIVATE);
        user_id = prefs.getInt("user_id", 0);
        Log.e("", "user_id" + user_id);
        Email = prefs.getString(Constants.EMAIL, null);
        Log.e("", "Email" + Email);
        Phone = prefs.getString(Constants.PHONE_NUMBER, null);
        Log.e("", "Phone" + Phone);
        UserName = prefs.getString(Constants.USER_NAME, null);
        Log.e("", "UserName" + UserName);
        Image = prefs.getString(Constants.PRODUCT_PHOTO, null);
        Log.e("", "Image" + Image);
        Address = prefs.getString(Constants.ADDRESS, null);
        Log.e("", "Address" + Address);
        City = prefs.getString(Constants.CITY, null);
        Log.e("", "City" + City);

        txt_contact_info = (TextView) root_view.findViewById(R.id.txt_contact_info);
        txt_contact_info.setTypeface(Font);
        btn_profile = (Button) root_view.findViewById(R.id.btn_profile);
        btn_profile.setTypeface(Font);
        btn_password = (Button) root_view.findViewById(R.id.btn_password);
        btn_password.setTypeface(Font);
        btn_update = (Button) root_view.findViewById(R.id.btn_update);
        btn_update.setTypeface(Font);
        txt_fill_info = (TextView) root_view.findViewById(R.id.txt_fill_info);
        txt_fill_info.setTypeface(Font);
        ed_Name = (EditText) root_view.findViewById(R.id.ed_Name);
        ed_Name.setTypeface(Font1);
        ed_mail = (EditText) root_view.findViewById(R.id.ed_mail);
        ed_mail.setTypeface(Font1);
        ed_phone_no = (EditText) root_view.findViewById(R.id.ed_phone_no);
        ed_phone_no.setTypeface(Font1);
        ed_add = (EditText) root_view.findViewById(R.id.ed_add);
        ed_add.setTypeface(Font1);
        ed_City = (EditText) root_view.findViewById(R.id.ed_City);
        ed_City.setTypeface(Font1);
        profile_imageView = (CircleImageView) root_view.findViewById(R.id.imageView1);


        ed_Name.setText(UserName);
        ed_mail.setText(Email);
        ed_phone_no.setText(Phone);
        ed_add.setText(Address);
        ed_City.setText(City);

        if (Image != null && !Image.trim().equals("") && !Image.trim().equals("null")) {
            try {
                ImageLoader imageLoader = ImageLoader.getInstance();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .build();

                imageLoader.displayImage("http://locallinkers.azurewebsites.net/admin/categoryimages/" + Image, profile_imageView, options);

            } catch (Exception e) {
                Log.e("ERROR ", e.toString());
            }
        } else {
            retrivesharedPreferences();
        }


        profile_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Change_Password.class);
                startActivity(intent);

            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_Name.setFocusableInTouchMode(true);
                ed_mail.setFocusableInTouchMode(true);
                ed_add.setFocusableInTouchMode(true);
                ed_City.setFocusableInTouchMode(true);


                btn_update.setVisibility(View.GONE);
                linear_layout.setVisibility(View.VISIBLE);
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_Name.setFocusable(false);
                ed_phone_no.setFocusable(false);
                ed_mail.setFocusable(false);
                ed_add.setFocusable(false);
                ed_City.setFocusable(false);
                btn_update.setVisibility(View.VISIBLE);
                linear_layout.setVisibility(View.GONE);
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_Name.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter your first name", Toast.LENGTH_SHORT).show();
                } else if (ed_mail.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (ed_phone_no.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
                } else if (ed_add.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else if (ed_City.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter your city name", Toast.LENGTH_SHORT).show();
                } else {
                    first_name = ed_Name.getText().toString();
                    email = ed_mail.getText().toString();
                    phone = ed_phone_no.getText().toString();
                    address = ed_add.getText().toString();
                    city = ed_City.getText().toString();
                    Upload_Profile_Data();
                }

            }
        });


        return root_view;
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    if (!checkPermissionForCamera()) {
                        requestPermissionForCamera();
                    } else {

                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent, ImageIntentHandler.REQUEST_CAPTURE);

                    }


                } else if (options[item].equals("Choose from Gallery")) {
                    mImagePair = new ImageIntentHandler.ImagePair(profile_imageView, null);
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, ImageIntentHandler.REQUEST_GALLERY);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public void requestPermissionForCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), "Camera permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == ImageIntentHandler.REQUEST_CAPTURE) {
                if (resultCode == Activity.RESULT_OK) {
                    // successfully captured the image
                    // display it in image view
                    previewCapturedImage(requestCode, resultCode, data);


                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == ImageIntentHandler.REQUEST_GALLERY) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                image_path1 = picturePath;
                c.close();
                bitmap = (BitmapFactory.decodeFile(picturePath));
                Log.w("...path...", picturePath + "");

                ImageIntentHandler intentHandler =
                        new ImageIntentHandler(getActivity(), mImagePair)
                                .folder("LocalLinkers")
                                .sizeDp(200);
                intentHandler.handleIntent(requestCode, resultCode, data);

                encodeTobase64(bitmap);

            }
        }
    }


    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage(int requestCode, int resultCode, Intent data) {
        try {

            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            image_path1 = picturePath;
            c.close();
            bitmap = (BitmapFactory.decodeFile(picturePath));
            profile_imageView.setImageBitmap(bitmap);
            Log.w("...path...", picturePath + "");

            encodeTobase64(bitmap);
            Uri tempUri = getImageUri(getActivity(), bitmap);
            Log.v("", "mImageCaptureUri" + tempUri);
            File finalFile = new File(getRealPathFromURI(tempUri));
            Log.v("", "mImageCaptureUri" + finalFile);


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.e("", ".....................path............." + path);

        image_path1 = path;

        return Uri.parse(path);
    }


    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.e("LOOK", "imageEncoded" + imageEncoded);
        return imageEncoded;
    }


    public void Upload_Profile_Data() {
        JSONObject json = prepareJsonObject();
        progressDialog = ProgressDialog.show(getActivity(), "", "Checking. Please wait...", false);
        String url = Constants.URL + Constants.UPDATE_PROFILE;
        CommonPostRequestThread requestThread = new CommonPostRequestThread(url, json.toString(), responseListener, exceptionListener);
        requestThread.start();
    }

    public JSONObject prepareJsonObject() {
        JSONObject innerJsonObject = new JSONObject();
        Boolean Image_status = false;
        String file = null;

        if (bitmap != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            file = Base64.encodeToString(data, Base64.DEFAULT);
            Log.e("file", "" + file);
            Image_status = true;
        }


        try {
            innerJsonObject.put("Address1", "" + address);
            innerJsonObject.put("Address2", "");
            innerJsonObject.put("City", city);
            innerJsonObject.put("Country", "");
            innerJsonObject.put("Email", email);
            innerJsonObject.put("Image", file);
            innerJsonObject.put("ImageChange", Image_status);
            innerJsonObject.put("PhoneNumber", phone);
            innerJsonObject.put("PostalCode", "");
            innerJsonObject.put("State", "");
            innerJsonObject.put("UserId", user_id);
            innerJsonObject.put("UserName", first_name);
            innerJsonObject.put("Website", "");
            Utilities.printD(tag, "" + innerJsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return innerJsonObject;
    }

    IHttpExceptionListener exceptionListener = new IHttpExceptionListener() {

        @Override
        public void handleException(String message) {
            try {
                if (progressDialog != null && progressDialog.isShowing() == true)
                    progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    IHttpResponseListener responseListener = new IHttpResponseListener() {

        @Override
        public void handleResponse(String response) {
            try {
                if (response != null && response.length() != 0) {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = Utilities.getJSONStringValue(jsonObject, "Result", null);
                    Log.e("", "response" + response);
                    if (status.equals("0")) {
                        handler.sendEmptyMessage(0);
                    }
                    if (status.equals("1")) {
                        user_Id = Utilities.getJSONIntValue(jsonObject, "UserId", 0);
                        profile_pic_url = Utilities.getJSONStringValue(jsonObject, "ImageName", "");
                        Log.d("", "user_Id" + user_Id);
                        handler.sendEmptyMessage(1);

                    } else if (status.equals("2")) {
                        handler.sendEmptyMessage(2);

                    } else if (status.equals("3")) {

                        handler.sendEmptyMessage(3);

                    } else if (status.equals("4")) {

                        handler.sendEmptyMessage(4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {


                case 0:
                    // Registration not Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Excepion..", Toast.LENGTH_LONG).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    // Registration Successful
                    try {
                        if (progressDialog != null && progressDialog.isShowing() == true)
                            progressDialog.dismiss();
                        ed_Name.setFocusable(false);
                        ed_phone_no.setFocusable(false);
                        ed_mail.setFocusable(false);
                        ed_add.setFocusable(false);
                        ed_City.setFocusable(false);
                        btn_update.setVisibility(View.VISIBLE);
                        linear_layout.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Profile Updated successfully..", Toast.LENGTH_LONG).show();
                        prefs.edit().putString(Constants.EMAIL, email).commit();
                        prefs.edit().putString(Constants.PHONE_NUMBER, phone).commit();
                        prefs.edit().putString(Constants.USER_NAME, first_name).commit();
                        prefs.edit().putString(Constants.ADDRESS, address).commit();
                        prefs.edit().putString(Constants.CITY, city).commit();
                        if (bitmap != null) {
                            prefs.edit().putString(Constants.IMAGE, null).commit();
                            sharedPreferences(profile_pic_url);

                        } else {
                            Log.e("not changed", "Image not changed");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }
    };

    private void sharedPreferences(String url) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PRODUCT_PHOTO", url);
        editor.commit();
    }


    private void retrivesharedPreferences() {
        String photo = prefs.getString("PRODUCT_PHOTO", "photo");
        if (!photo.equals("photo")) {

            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .build();

            imageLoader.displayImage("http://locallinkers.com/UserImages/" + photo + "?width=120&mode=crop",
                    profile_imageView, options);


        } else {
            profile_imageView.setImageResource(R.drawable.no_preview);
        }
    }


}