package com.maha.sam_contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.maha.sam_contact.adapter.ContactRecycler;
import com.maha.sam_contact.model.ContactModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class MainActivity extends ActivityManagePermission implements PermissionResult {

   List<ContactModel> mContactList = new ArrayList<>();

   RecyclerView mRecyclerView;


   @Override
   protected void onCreate( Bundle savedInstanceState ) {
      super.onCreate( savedInstanceState );
      setContentView( R.layout.activity_main );
      askPermission();

      init();


   }

   private void init() {
      mRecyclerView = findViewById( R.id.contact_detail_list );

      LinearLayoutManager aLayoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false );
      mRecyclerView.setHasFixedSize( true );

      mRecyclerView.setLayoutManager( aLayoutManager );



   }


   public List<ContactModel> getContacts( Context ctx ) {
      List<ContactModel> list = new ArrayList<>();
      ContentResolver contentResolver = ctx.getContentResolver();
      Cursor cursor = contentResolver.query( ContactsContract.Contacts.CONTENT_URI, null, null, null, null );
      if( cursor.getCount() > 0 ) {
         while( cursor.moveToNext() ) {
            String id = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts._ID ) );
            if( cursor.getInt( cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER ) ) > 0 ) {
               Cursor cursorInfo = contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                       ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{ id }, null );
               InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream( ctx.getContentResolver(),
                       ContentUris.withAppendedId( ContactsContract.Contacts.CONTENT_URI, new Long( id ) ) );

               Uri person = ContentUris.withAppendedId( ContactsContract.Contacts.CONTENT_URI, new Long( id ) );
               Uri pURI = Uri.withAppendedPath( person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY );

               Bitmap photo = null;
               if( inputStream != null ) {
                  photo = BitmapFactory.decodeStream( inputStream );
               }
               while( cursorInfo.moveToNext() ) {
                  ContactModel info = new ContactModel();
                  info.id = id;
                  info.name = cursor.getString( cursor.getColumnIndex( ContactsContract.Contacts.DISPLAY_NAME ) );
                  info.mobileNumber = cursorInfo.getString( cursorInfo.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER ) );
                  info.photo = photo;
                  info.photoURI = pURI;
                  list.add( info );
               }

               cursorInfo.close();
            }
         }
         cursor.close();
      }
      return list;
   }

   @Override
   protected void onResume() {
      super.onResume();


      if( checkPermissionGranted() ) {
         mContactList = getContacts( this );
         Log.e( "SIZE", "" + mContactList.size() );

         ContactRecycler mContactRecycler = new ContactRecycler( this, mContactList );
         mRecyclerView.setAdapter( mContactRecycler );
      }
   }

   private void askPermission() {
      try {
         askCompactPermissions( new String[]{
                 PermissionUtils.Manifest_READ_CONTACTS,
                 PermissionUtils.Manifest_WRITE_CONTACTS
         }, this );
      } catch( Exception e ) {
         e.printStackTrace();
      }
   }


   public boolean checkPermissionGranted() {
      boolean isGranted = false;
      try {
         isGranted = isPermissionsGranted( this, new String[]{ PermissionUtils.Manifest_READ_CONTACTS,
                 PermissionUtils.Manifest_WRITE_CONTACTS } );
      } catch( Exception e ) {
         e.printStackTrace();
      } finally {
         if( isGranted ) {

         }
      }
      return isGranted;
   }

   @Override
   public void permissionGranted() {

   }

   @Override
   public void permissionDenied() {

   }

   @Override
   public void permissionForeverDenied() {

   }
}
